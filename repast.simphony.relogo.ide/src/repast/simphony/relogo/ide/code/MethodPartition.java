/*
 * Calculate the invocation context of all symbols used in the model.
 * Question: would this be simplified if I constructed separate entries
 * for each of the closures or implicit methods that are passed to e.g. 'ask'
 * and 'create'?
 * Might also do some typing of blocks etc.
 */
package repast.simphony.relogo.ide.code;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author CBURKE
 */
public class MethodPartition {

    protected HashMap<String, HashSet<String>> procContexts;
    protected HashMap<String, HashSet<String>> attrContexts;
    protected LinkedList<RelogoClass> relogoClasses;
    
    public MethodPartition(LinkedList<ProcedureDefinition> procDefns, SymbolTable symbolTable) {
        procContexts = new HashMap<String, HashSet<String>>();
        attrContexts = new HashMap<String, HashSet<String>>();
        LinkedHashMap<String, Object> procedures = symbolTable.getSymbolCategory("*PROCEDURE*");
        for (Map.Entry me : procedures.entrySet()) {
            if (me.getValue() != null) {
                for (Profile profile = (Profile) me.getValue(); profile != null; profile = profile.alternative) {
                	// TODO: Not sure why we iterate over profile alternatives since they all have the same 'name'
                    procContexts.put(profile.name, new HashSet<String>());
                    attrContexts.put(profile.name, new HashSet<String>());
                }
            }
        }
        boolean continueIteration = true;
        // may also want to add entries for 'ask'&'create' contexts
        while(continueIteration){
        	continueIteration = false;
	        for (ProcedureDefinition procDef : procDefns) {
	            HashSet<String> reqContexts = attrContexts.get(procDef.profile.name);
	            int length = reqContexts.size();
	            collectContexts(reqContexts, procDef.code);
	            if (length != reqContexts.size()){
	            	continueIteration = true;
	            }
	        }
        }
        // may also want to add entries for 'ask'&'create' contexts
        
    	for (ProcedureDefinition procDef : procDefns) {
            HashSet<String> reqContexts = attrContexts.get(procDef.profile.name);
            HashSet<String> prefContexts = procContexts.get(procDef.profile.name);
            HashSet<String> combinedContext = new HashSet<String>();
            combinedContext.addAll(reqContexts);
//            combinedContext.addAll(prefContexts);
            String[] longestPath = null;
            boolean conflict = false;
            boolean tpAmbiguous = false;
            boolean tlAmbiguous = false;
            for (String s : combinedContext) {
            	if (s.equals("tandp")){
            		tpAmbiguous = true;
            	}
            	else if (s.equals("tandl")){
            		tlAmbiguous = true;
            	}
            	else {
	                String[] classPath = symbolTable.getInheritancePath(s);
	                if (classPath == null) continue;
	                if (longestPath == null) {
	                    longestPath = classPath;
	                } else {
	                    String[] longestCompatiblePath = longestCompatibleOverlay(longestPath, classPath, conflict);
	                    if (longestCompatiblePath.length < longestPath.length) {
	                        conflict = true;
	                    }
	                    longestPath = longestCompatiblePath;
	                }
            	}
            }
            if (longestPath == null && !(tpAmbiguous || tlAmbiguous)) {
                //System.out.println(procDef.name + ": *global*");
                RelogoClass owningClass = symbolTable.findClass("*global*");
                if (owningClass == null) {
                    System.err.println("cannot find owning class *global*!");
                } else {
                    owningClass.addMethod(procDef);
                }
            } else {
            	if (tpAmbiguous){
            		if (pathContainsOr(longestPath,"turtles","patches")){
            			tpAmbiguous = false;
            		}
            		if (tpAmbiguous){
            			RelogoClass turtleClass = symbolTable.findClass("turtles");
            			RelogoClass patchClass = symbolTable.findClass("patches");
                        if (turtleClass == null){
                            System.err.println("cannot find owning class turtles");
                        }
                        else if( patchClass == null) {
                        	System.err.println("cannot find owning class patches");
                        }
                        else {
                        	turtleClass.addMethod(procDef);
                        	patchClass.addMethod(procDef);
                    	}
                        continue;
                    }
        		}        	
            	// The added tpAmbiguous check is to avoid adding procedures to both links and patches
            	// (presumably this would never happen, but just in case)
            	if (tlAmbiguous && !tpAmbiguous){
            		if(pathContainsOr(longestPath,"turtles","links")){
            			tlAmbiguous = false;
            		}
            		if (tlAmbiguous){
            			RelogoClass turtleClass = symbolTable.findClass("turtles");
            			RelogoClass linkClass = symbolTable.findClass("links");
                        if (turtleClass == null){
                            System.err.println("cannot find owning class turtles");
                        }
                        else if( linkClass == null) {
                        	System.err.println("cannot find owning class links");
                        }
                        else {
                        	turtleClass.addMethod(procDef);
                        	linkClass.addMethod(procDef);
                    	}
                        continue;
                    }
            	}
                //System.out.println(procDef.name + ": "+longestPath[longestPath.length-1]);
                RelogoClass owningClass = symbolTable.findClass(longestPath[longestPath.length-1]);
                if (owningClass == null) {
                    System.err.println("cannot find owning class "+longestPath[longestPath.length-1]);
                } else {
                    owningClass.addMethod(procDef);
                }
            }
        }

        //
        // now for special cases.
        // case one: all patch variables are accessible from a turtle in
        // the patch. add all patch attributes to the turtle class.
        /*RelogoClass patchesClass = symbolTable.findClass("patches");
        RelogoClass turtlesClass = symbolTable.findClass("turtles");
        for (Attribute attr : patchesClass.attributes) {
            turtlesClass.addAttribute(attr);
        }*/
        // case 2: observer class takes all methods from global (model)
        RelogoClass globalClass = symbolTable.findClass("*global*");
        RelogoClass observerClass = symbolTable.findClass("*observer*");
        for (ProcedureDefinition procDef : globalClass.methods) {
            observerClass.addMethod(procDef);
        }
        globalClass.methods.clear();
        relogoClasses = symbolTable.getAllClasses();
//        System.out.println("There are " + relogoClasses.size() + " relogo classes.");
        for (RelogoClass rc: relogoClasses) {
//        	System.out.println("The class: " + rc.getClassName());
            String[] classPath = symbolTable.getInheritancePath(rc.getClassName());
            
        	for (int i=0; i<classPath.length; i++) {
        		if (classPath[i] == null) {
        			continue;
        		} else if (classPath[i].equals("patches")) {
        			rc.setGenericCategory(RelogoClass.RELOGO_CLASS_PATCH);
        		} else if (classPath[i].equals("turtles")) {
            		rc.setGenericCategory(RelogoClass.RELOGO_CLASS_TURTLE);
        		} else if (classPath[i].equals("links")) {
                	rc.setGenericCategory(RelogoClass.RELOGO_CLASS_LINK);
        		}
        	}
        }
    }
    
    protected boolean pathContainsOr(String[] path, String firstString, String secondString){
    	if (path == null){
    		return false;
    	}
    	for (int i = 0; i < path.length ; i++){
			String pathEntry = path[i];
			if (pathEntry != null){
				if (pathEntry.equals(firstString) || pathEntry.equals(secondString)){
					return true;
				}
			}
		}
    	return false;
    }

    public LinkedList<RelogoClass> getAllClasses() {
        return relogoClasses;
    }
    
    protected String[] longestCompatibleOverlay(String[] one, String[] two, boolean conflict) {
        for (int i=0; i<one.length; i++) {
            if (i >= two.length) break;
            if (one[i] == two[i]) continue;  // if both null or same object, then equal
            if (one[i] != null && one[i].equals(two[i])) continue;
            if ("patches".equals(one[i]) && "turtles".equals(two[i])) {
                //System.out.println("Resolving patch/turtle conflict");
                return two;
            } else if ("turtles".equals(one[i]) && "patches".equals(two[i])) {
                //System.out.println("Resolving patch/turtle conflict");
                return one;
            }
            String[] subset = new String[i];
            for (int j=0; j<i; j++) {
                subset[j] = one[j];
            }
            return subset;
        }
        if (conflict) return one;  // by convention, always pass the persistent value as 'one'
        if (one.length >= two.length) {
            return one;
        } else {
            return two;
        }
    }
    
    protected void collectContexts(HashSet<String> reqContexts, Collection collection) {
        for (Object o : collection) {
            if (o instanceof ProcedureInvocation) {
                collectContexts(reqContexts, (ProcedureInvocation) o);
            } else if (o instanceof Block) {
                collectContexts(reqContexts, (Block) o);
            } else if (o instanceof Paren) {
                collectContexts(reqContexts, ((Paren) o).contents);
            } else if (o instanceof Collection) {
                collectContexts(reqContexts, (Collection) o);
            } else if (o instanceof Attribute) {
                Attribute attr = (Attribute) o;
                if (attr.breed == null) {
                    System.err.println("*** Attribute with null breed!");
                } else if (attr.breed.equals("*FORMAL*")) {
                // formal parameters don't define type
                } else if (attr.breed.equals("*LOCAL*")) {
                // local variables don't define type
                } else if (attr.breed.equals("*GLOBAL*")) {
                // global variables don't affect method placement
                } else if (attr.breed.equals("*CLOSURE*")) {
                // presence in a closure doesn't affect method placement
                } else {
                    //System.out.println(" attribute " + attr);
                    //System.out.println("required context: " + attr.breed);
                	if (attr.breed.equals("patches")){
                		reqContexts.add("tandp");
                	}
                	else{
                		reqContexts.add(attr.breed);
                	}
                }
            } else if (o instanceof Profile) {
                System.err.println("$$$ UNREDUCED PROFILE!");
            } else if (o == null) {
                System.out.println("  null code block!");
            } else if (o instanceof String) {
            } else if (o instanceof Number) {
            } else {
                System.out.println(o.getClass().getName() + ": " + o);
            }
        }
    }

    protected void collectContexts(HashSet<String> reqContexts, Block code) {
        //    System.out.println(code);
        if (code == null || code.contents == null) {
            return;
        }
        collectContexts(reqContexts, code.contents);
    }

    protected String extractAgentType(String s) {
        int i = s.indexOf("(");
        int j = s.indexOf(")");
        if (i >= 0 && j >= 0) {
            return s.substring(i + 1, j);
        } else {
            return s;
        }
    }

    protected void collectContexts(HashSet<String> reqContexts, ProcedureInvocation code) {
        //    System.out.println(code);
        Profile calledProfile = code.profile;
        HashSet<String> reqContextsOfProcedure = attrContexts.get(code.getProfile().name);
        if (reqContextsOfProcedure.size() > 0){
        	reqContexts.addAll(reqContextsOfProcedure);
        }
        if (calledProfile == null) {
            System.err.println("*** NULL profile in ProcedureInvocation!!");
        }
        if (code.type != null) {
        //System.out.println(calledProfile.name + " returning " + code.type);
        }
        if (calledProfile.breed != null) {
            // we have a type for this function. if it's not one of the
            // special built-in contexts, add it to the required set.
            if (!calledProfile.breed.startsWith("*")) {
                reqContexts.add(calledProfile.breed);
            }
        }
        /*
         * If this procedure takes a closure, collect its context separately.
         * Each argument could potentially be a closure
         */
        if (calledProfile.createsContext()) {
        	//TODO: this might be where to fix the stop vs return
            // divide the arguments into two sets: those which are evaluated
            // within the current context, and those with the new context.
            // the new context is either the known profile breed, or the
            // type of an agt/agtset in the arguments.
            LinkedList inPrevContext = new LinkedList();
            LinkedList inSubContext = new LinkedList();
            String subContextType = null;
            HashSet<String> subContext = new HashSet<String>();
            if (calledProfile.breed != null) {
                if (!calledProfile.breed.startsWith("*")) {
                    subContext.add(calledProfile.breed);
                }
            }
            for (Object o : code.arguments) {
                String type = code.getType(o);
                if ("cmd".equals(type) || "rpt".equals(type)) {
                    inSubContext.add(o);
                } else {
                    inPrevContext.add(o);
                    if (o instanceof ProcedureInvocation) {
                        String setType = ((ProcedureInvocation) o).calculateType("agtset");
                        String agtType = ((ProcedureInvocation) o).calculateType("agt");
                        if (agtType == null) {
                            if (setType != null) {
                                subContext.add(extractAgentType(setType));
                            }
                        } else {
                            subContext.add(extractAgentType(agtType));
                        }
                    }
                }
            }
            collectContexts(reqContexts, inPrevContext);
            //System.out.println("*** creating subordinate context: "+subContext);
            collectContexts(subContext, inSubContext);
            collectCallers(subContext, inSubContext);
        //System.out.println("*** closure requires: "+subContext);
        } else {
            collectContexts(reqContexts, code.arguments);
        }
    }

    protected void collectCallers(HashSet<String> caller, Collection collection) {
        for (Object o : collection) {
            if (o instanceof ProcedureInvocation) {
                collectCallers(caller, (ProcedureInvocation) o);
            } else if (o instanceof Block) {
                collectCallers(caller, (Block) o);
            } else if (o instanceof Paren) {
                collectCallers(caller, ((Paren) o).contents);
            } else if (o instanceof Collection) {
                collectCallers(caller, (Collection) o);
            } else if (o instanceof Attribute) {
                // attributes are already placed, this would only be done
                // to verify unconflicting placement.
            } else if (o instanceof Profile) {
                System.err.println("$$$ UNREDUCED PROFILE!");
            } else if (o == null) {
                System.err.println("  null code block!");
            } else if (o instanceof String) {
            } else if (o instanceof Number) {
            } else {
                System.out.println(o.getClass().getName() + ": " + o);
            }
        }
    }

    protected void collectCallers(HashSet<String> caller, Block code) {
        //    System.out.println(code);
        if (code == null || code.contents == null) {
            return;
        }
        collectCallers(caller, code.contents);
    }

    protected void collectCallers(HashSet<String> caller, ProcedureInvocation code) {
        //    System.out.println(code);
        Profile calledProfile = code.profile;
        if (calledProfile == null) {
            System.err.println("*** NULL profile in ProcedureInvocation!!");
        } else {
            procContexts.get(calledProfile.name).addAll(caller);
        }
        collectCallers(caller, code.arguments);
    }
}
