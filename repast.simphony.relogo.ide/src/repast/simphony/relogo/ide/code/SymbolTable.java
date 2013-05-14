/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repast.simphony.relogo.ide.code;

// note: __includes needs to be handled somewhere.
//note: parentheses are significant in some commands, particularly those
//which have variable arguments.
//(to set) and reporters (to get); default is a getter.\

// NOTE: for implementation of an interpreter, I used an AgentImplementations
// class which is not supported inside Relogo per se.
//import org.mitre.g8.netlogo.AgentImplementations;
import java.awt.Color;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import repast.simphony.relogo.ide.wizards.WizardUtilities;
import repast.simphony.relogo.util.ReLogoSupport;

/**
 *
 * @author CBURKE
 */
public class SymbolTable {

    /**
     * If <code>true</code>, symbols are converted to lowercase when defined.
     */
    static public boolean CASE_INSENSITIVE_SYMBOLS = false;
    // order of declaration matters for some things, so might need
    // some kind of threaded map; possibly a threaded tree map,
    // if that class exists.
    protected HashMap<String, LinkedHashMap<String, Object>> symbolTable;
    protected HashMap<String, HashSet<String>> symbolIndex;
    protected Stack<Block> scopeStack;
    protected TreeModel classTree;
//    protected ExtensionManager extMan;
    //protected AgentImplementations agentImplementations;
    public SymbolTable() {
        symbolTable = new HashMap<String, LinkedHashMap<String, Object>>();
        symbolIndex = new HashMap<String, HashSet<String>>();
        scopeStack = new Stack<Block>();
//        extMan = new ExtensionManager();
        buildClassTree();
        createBuiltInSymbols();
        initPrimitives();
        declareSpecialLocalVariables();
    }

    protected void buildClassTree() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        classTree = new DefaultTreeModel(rootNode);
        declareClass("*agent*", null);
        declareClass("*observer*", null);
        declareClass("*global*", null);
    }

    public RelogoClass findClass(String className) {
        DefaultMutableTreeNode dtn = (DefaultMutableTreeNode) classTree.getRoot();
        Enumeration e = dtn.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode ch = (DefaultMutableTreeNode) e.nextElement();
            RelogoClass theClass = (RelogoClass) ch.getUserObject();
            if (theClass == null) {  // root node
                continue;
            }
            String nodeName = theClass.className;
            if (nodeName == null) {
                if (className == null) {
                    return theClass;
                }
            } else if (nodeName.equals(className)) {
                return theClass;
            }
        }
        return null;
    }

    public LinkedList<RelogoClass> getAllClasses() {
        LinkedList<RelogoClass> allClasses = new LinkedList<RelogoClass>();
        DefaultMutableTreeNode dtn = (DefaultMutableTreeNode) classTree.getRoot();
        Enumeration e = dtn.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode ch = (DefaultMutableTreeNode) e.nextElement();
            RelogoClass theClass = (RelogoClass) ch.getUserObject();
            if (theClass != null && theClass.className != null) {
                allClasses.add(theClass);
            }
        }
        return allClasses;
    }

    public void declareClass(String className, String parentClassName) {
        DefaultMutableTreeNode dtn = (DefaultMutableTreeNode) classTree.getRoot();
        Enumeration e = dtn.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode ch = (DefaultMutableTreeNode) e.nextElement();
            Object nodeClass = ch.getUserObject();
            if (nodeClass == null) {
                if (parentClassName == null) {
                    // add a new node here
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode();
                    newNode.setUserObject(new RelogoClass(className, parentClassName));
                    ch.add(newNode);
                    return;
                }
            } else if (((RelogoClass) nodeClass).className.equals(parentClassName)) {
                // add a new node here
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode();
                RelogoClass rlc = new RelogoClass(className, parentClassName);
                
                newNode.setUserObject(rlc);
                ch.add(newNode);
                return;
            }
        }
        System.out.println("Fell through parent class loop!");
    }

    protected String[] getInheritancePath(String className) {
        DefaultMutableTreeNode dtn = (DefaultMutableTreeNode) classTree.getRoot();
        DefaultMutableTreeNode target = null;
        Enumeration e = dtn.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode ch = (DefaultMutableTreeNode) e.nextElement();
            RelogoClass theClass = (RelogoClass) ch.getUserObject();
            if (theClass == null) {
                continue;  // root node
            }
            String nodeName = theClass.className;
            if (nodeName == null) {
                if (className == null) {
                    target = ch;
                    break;
                }
            } else if (nodeName.equals(className)) {
                target = ch;
                break;
            }
        }
        if (target == null) {
            System.err.println("Reference to undefined class name " + className + "!");
            return null;
        }
        TreeNode[] path = target.getPath();
        String[] classPath = new String[path.length];
        for (int i = 0; i < path.length; i++) {
            RelogoClass theClass = (RelogoClass) ((DefaultMutableTreeNode) path[i]).getUserObject();
            if (theClass == null) {
                classPath[i] = null;
            } else {
                classPath[i] = theClass.className;
            }
        }
        return classPath;
    }

//    public boolean declareExtension(String name) {
//        return extMan.loadExtension(name);
//    }

//    public void declareExtensionSymbols() {
//        for (String prim : extMan.getExtensionSymbols()) {
//            String ret = extMan.getReturnTypeName(prim);
//            String left = extMan.getLeftTypeName(prim);
//            String[] right = extMan.getRightTypeNames(prim);
//            String syntaxCode = ret + "=" + left + "/";
//            if (right.length == 0) {
//                syntaxCode = syntaxCode + "void";
//            } else {
//                syntaxCode = syntaxCode + right[0];
//                for (int i = 1; i < right.length; i++) {
//                    syntaxCode = syntaxCode + "," + right[i];
//                }
//            }
//            declarePrimitive(new Profile(prim, syntaxCode) {
//                // need to override the default implementation of getJavaName
//                // so that the correct primitive object is obtained.
//                // the exact invocation depends on whether this is a Command
//                // or a Reporter primitive.
//                public String getJavaName() {
//                    if (name == null) {
//                        return null;
//                    } else {
//                        String[] extName = name.split(":");
//                        extName[0] = camelCase(extName[0] + "Instance");
//                        extName[1] = camelCase(extName[1]);
//                        return extName[0] + "." + extName[1];
//                    }
//                }
//            });
//        }
//    }

    public void openScope(Block scope) {
        scopeStack.push(scope);
    }

    public void closeScope() {
        scopeStack.pop();
    }

    public void defineLocalVariable(Attribute attr) {
        scopeStack.peek().defineLocalVariable(attr);
    }

    public Object findSymbol(String symbol) {
        symbol = symbol.toLowerCase();
        HashSet<String> found = symbolIndex.get(symbol);
        if (found == null) {
            // check the scope stack. given the standard implementation,
            // the top of the stack is the last item returned by the
            // iterator; if the user got fancy and hid a variable with
            // a nested declaration, we will find multiple Attributes,
            // so we have to check all open scopes.
            Attribute localAttribute = null;
            for (Block scope : scopeStack) {
                Attribute attr = scope.findSymbol(symbol);
                if (attr != null) {
                    localAttribute = attr;
                }
            }
            if (localAttribute != null) {
                LinkedList defns = new LinkedList();
                defns.add(localAttribute);
                return defns;
            }
            return null;
        } else {
            LinkedList defns = new LinkedList();
            for (String cat : found) {
                LinkedHashMap<String, Object> category = symbolTable.get(cat);
                Object defn = category.get(symbol);
                if (defn == null) {
                    System.err.println("*** Lost access to symbol definition: " + symbol);
                }
                defns.add(defn);
            }
            return defns;
        }
    }

    protected LinkedHashMap<String, Object> getSymbolCategory(String type) {
        LinkedHashMap<String, Object> category = symbolTable.get(type);
        if (category == null) {
            category = new LinkedHashMap<String, Object>();
            symbolTable.put(type, category);
        }
        return category;
    }

    public void declareSymbol(String domain, String name, Object data) {
        if (CASE_INSENSITIVE_SYMBOLS) {
            name = name.toLowerCase();
        }
        HashSet<String> symbolDefinition = symbolIndex.get(name);
        if (symbolDefinition != null && symbolDefinition.contains(domain)) {
            if (data instanceof Profile) {
                LinkedHashMap<String, Object> domainMap = getSymbolCategory(domain);
                if (domainMap == null) {
                    System.err.println("domain map is null for redefined symbol '" + name + "', a " + symbolDefinition);
                } else {
                    Object profile = domainMap.get(name);
                    if (profile == null) {
                        System.err.println("existing profile is null for redefined symbol '" + name + "', a " + symbolDefinition);
                    } else {
                        ((Profile) profile).addAlternative((Profile) data);
                        System.err.println("'" + name + "' overloaded");
                    }
                }
            } else {
                System.err.println("Warning: '" + name + "' redefined as '" + domain + "'");
            }
        } else if (symbolDefinition == null) {
            symbolDefinition = new HashSet<String>();
            symbolIndex.put(name.toLowerCase(), symbolDefinition);  // may want to compose domain and data here
        }
        symbolDefinition.add(domain);  // may want to compose domain and data...
        LinkedHashMap<String, Object> domainMembers = getSymbolCategory(domain);
        domainMembers.put(name.toLowerCase(), data);
    }

    /**
     * Declare an attribute with the specified name on the specified breed
     * and all of its descendant breeds. Fails silently if an attribute
     * of the specified name already exists for that breed.
     * 
     * @param breed
     * @param name
     */
    protected void declareAttribute(String breed, String name) {
    	declareAttribute(breed, name, null, null, null, true);
    }

    /**
     * Declare an attribute with the specified name on the specified breed
     * and all of its descendant breeds. Fails silently if an attribute
     * of the specified name already exists for that breed.
     * 
     * @param breed
     * @param name
     */
    protected void declareAttribute(String breed, String name, boolean gen) {
    	declareAttribute(breed, name, null, null, null, gen);
    }

    /**
     * Declare an attribute with the specified name and the specified initial
     * value on the specified breed and all of its descendant breeds. Fails
     * silently if an attribute of the specified name already exists for that breed.
     * 
     * @param breed
     * @param name
     */
    protected void declareAttribute(String breed, String name, Object ivalue, boolean g) {
    	declareAttribute(breed, name, null, null, ivalue, g);
    }
    /**
     * Declare an attribute with the specified name on the specified breed
     * and all of its descendant breeds. Fails silently if an attribute
     * of the specified name already exists for that breed.
     * 
     * @param breed
     * @param name
     */
    protected void declareAttribute(String breed, String name, String label, String type, Object initialValue, boolean g) {
//    	String domain;
//    	if (breed.equals("tandp")){
//    		domain = "tandp";
//    		breed = "patches";
//    	}
//    	else {
//    		domain = breed;
//    	}
        RelogoClass owningClass = findClass(breed.toLowerCase());
        if (CASE_INSENSITIVE_SYMBOLS) {
            name = name.toLowerCase();
        }
        Object defns = findSymbol(name);
        if (defns instanceof List) {
            for (Object o : ((List) defns)) {
                if (o instanceof Attribute) {
                    if (breed.equals(((Attribute) o).breed)) {
                        return;   // redundant declaration
                    }
                }
            }
        }
        Attribute theAttribute = new Attribute(name, breed, label, type, initialValue, g);
        //differentiate the domain of the symbol with the owning class
        declareSymbol(breed, name, theAttribute);
        if (owningClass != null) {
            owningClass.addAttribute(theAttribute);
        }
    }

    /**
     * Declare an attribute of a breed with a specific implementation.
     * Returns false if an attribute of the specified name already exists
     * for that breed. Most useful for an interpreter.
     * 
     * @param attr
     * @return
     */
    public boolean declareAttribute(Attribute attr) {
        RelogoClass owningClass = findClass(attr.breed.toLowerCase());
        Object defns = findSymbol(attr.name);
        if (defns instanceof List) {
            for (Object o : ((List) defns)) {
                if (o instanceof Attribute) {
                    if (attr.breed.equals(((Attribute) o).breed)) {
                        return false;   // redundant declaration
                    }
                }
            }
        }
        declareSymbol(attr.breed, attr.name, attr);
        if (owningClass != null) {
            owningClass.addAttribute(attr);
        }
        return true;
    }

    protected String camelCase(String text) {
        StringBuffer buf = new StringBuffer(text);
        for (int i = 0; i < buf.length(); i++) {
            if (buf.charAt(i) == '-' && buf.length() > (i + 1)) {
                buf.deleteCharAt(i);
                if (buf.charAt(i) != '?' && buf.charAt(i) != '%'){
                	buf.setCharAt(i, Character.toUpperCase(buf.charAt(i)));
                }
                else if (buf.charAt(i) == '?') {
                    buf.setCharAt(i, 'Q');
                } else if (buf.charAt(i) == '%') {
                    buf.setCharAt(i, 'P');
                }
            } else if (buf.charAt(i) == '?') {
                buf.setCharAt(i, 'Q');
            } else if (buf.charAt(i) == '%') {
                buf.setCharAt(i, 'p');
            }
        }
        return buf.toString();
    }
    
    protected void declareBreed(String plural, String singular) {
        // a breed should have an agentset generation procedure which can
        // take an optional selector as an argument.
        if (plural != null) {
            if (CASE_INSENSITIVE_SYMBOLS) {
                plural = plural.toLowerCase();
            }
            declareSymbol("*BREED*", plural, "plural");
            if (plural.equals("patches") || plural.equals("turtles") || plural.equals("links")) {
                declareClass(plural, "*agent*");
            } else {
                declareClass(plural, "turtles");
            }
            Profile bareBreed = new Profile(plural, "agtset(" + plural + ")=void/void");
            declarePrimitive(bareBreed);
            declarePrimitive(new Profile("create-" + plural, "@void=void/num,cmd!void=void/num"){
            	public String encodeInstance(ProcedureInvocation pi, int indent) {
                    StringBuffer buf = new StringBuffer();
                    buf.append(getJavaName() + "(");
                    Object o = pi.arguments.get(0);
                    if (o instanceof Block) {
                    	Block block = (Block)o;
                    	if (block.contents != null && block.contents.size() == 1 && block.contents.get(0) instanceof ProcedureInvocation) {
                    		ProcedureInvocation proc = (ProcedureInvocation)block.contents.get(0);
                    		String type = proc.type==null ? "" : proc.type.toString();
                            buf.append(proc.toString(indent + 4));
                    	} else {
                            buf.append(block.toString(indent + 4));
                    	}
                    } else if (o instanceof ProcedureInvocation) {
                		ProcedureInvocation proc = (ProcedureInvocation)o;
                		String type = proc.type==null ? "" : proc.type.toString();
                        buf.append(proc.toString(indent + 4));
                    } else if (o == null) {
                        buf.append(" /* NULL BLOCK NO-OP */ ");
                    } else {
//                        buf.append("[");
                        buf.append(o.toString());
//                        buf.append("]");
                    }
                    buf.append(")");
                    if (pi.arguments.size() > 1){
	                    try {
	                        Block block = (Block) pi.arguments.get(1);
	                        buf.append(block.toStringAsClosure(indent, null));
	                    } catch (ClassCastException e) {
	                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
	                    }
                    }
                    return buf.toString();
                }
            });
            declarePrimitive(new Profile("create-ordered-" + plural, "@void=void/num,cmd!void=void/num"){
            	public String encodeInstance(ProcedureInvocation pi, int indent) {
                    StringBuffer buf = new StringBuffer();
                    buf.append(getJavaName() + "(");
                    Object o = pi.arguments.get(0);
                    if (o instanceof Block) {
                    	Block block = (Block)o;
                    	if (block.contents != null && block.contents.size() == 1 && block.contents.get(0) instanceof ProcedureInvocation) {
                    		ProcedureInvocation proc = (ProcedureInvocation)block.contents.get(0);
                    		String type = proc.type==null ? "" : proc.type.toString();
                            buf.append(proc.toString(indent + 4));
                    	} else {
                            buf.append(block.toString(indent + 4));
                    	}
                    } else if (o instanceof ProcedureInvocation) {
                		ProcedureInvocation proc = (ProcedureInvocation)o;
                		String type = proc.type==null ? "" : proc.type.toString();
                        buf.append(proc.toString(indent + 4));
                    } else if (o == null) {
                        buf.append(" /* NULL BLOCK NO-OP */ ");
                    } else {
//                        buf.append("[");
                        buf.append(o.toString());
//                        buf.append("]");
                    }
                    buf.append(")");
                    if (pi.arguments.size() > 1){
	                    try {
	                        Block block = (Block) pi.arguments.get(1);
	                        buf.append(block.toStringAsClosure(indent, null));
	                    } catch (ClassCastException e) {
	                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
	                    }
                    }
                    return buf.toString();
                }
            });
            declarePrimitive(new Profile("hatch-" + plural, "turtles" , "[turtle]@void=void/num,cmd![turtle]void=void/num"){
            	public String encodeInstance(ProcedureInvocation pi, int indent) {
                    StringBuffer buf = new StringBuffer();
                    buf.append(getJavaName() + "(");
                    Object o = pi.arguments.get(0);
                    if (o instanceof Block) {
                    	Block block = (Block)o;
                    	if (block.contents != null && block.contents.size() == 1 && block.contents.get(0) instanceof ProcedureInvocation) {
                    		ProcedureInvocation proc = (ProcedureInvocation)block.contents.get(0);
                    		String type = proc.type==null ? "" : proc.type.toString();
                            buf.append(proc.toString(indent + 4));
                    	} else {
                            buf.append(block.toString(indent + 4));
                    	}
                    } else if (o instanceof ProcedureInvocation) {
                		ProcedureInvocation proc = (ProcedureInvocation)o;
                		String type = proc.type==null ? "" : proc.type.toString();
                        buf.append(proc.toString(indent + 4));
                    } else if (o == null) {
                        buf.append(" /* NULL BLOCK NO-OP */ ");
                    } else {
//                        buf.append("[");
                        buf.append(o.toString());
//                        buf.append("]");
                    }
                    buf.append(")");
                    if (pi.arguments.size() > 1){
	                    try {
	                        Block block = (Block) pi.arguments.get(1);
	                        buf.append(block.toStringAsClosure(indent, null));
	                    } catch (ClassCastException e) {
	                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
	                    }
                    }
                    return buf.toString();
                }
            });
            declarePrimitive(new Profile("sprout-" + plural, "patches" , "[patch]@void=void/num,cmd![patch]void=void/num"){
            	public String encodeInstance(ProcedureInvocation pi, int indent) {
                    StringBuffer buf = new StringBuffer();
                    buf.append(getJavaName() + "(");
                    Object o = pi.arguments.get(0);
                    if (o instanceof Block) {
                    	Block block = (Block)o;
                    	if (block.contents != null && block.contents.size() == 1 && block.contents.get(0) instanceof ProcedureInvocation) {
                    		ProcedureInvocation proc = (ProcedureInvocation)block.contents.get(0);
                    		String type = proc.type==null ? "" : proc.type.toString();
                            buf.append(proc.toString(indent + 4));
                    	} else {
                            buf.append(block.toString(indent + 4));
                    	}
                    } else if (o instanceof ProcedureInvocation) {
                		ProcedureInvocation proc = (ProcedureInvocation)o;
                		String type = proc.type==null ? "" : proc.type.toString();
                        buf.append(proc.toString(indent + 4));
                    } else if (o == null) {
                        buf.append(" /* NULL BLOCK NO-OP */ ");
                    } else {
//                        buf.append("[");
                        buf.append(o.toString());
//                        buf.append("]");
                    }
                    buf.append(")");
                    if (pi.arguments.size() > 1){
	                    try {
	                        Block block = (Block) pi.arguments.get(1);
	                        buf.append(block.toStringAsClosure(indent, null));
	                    } catch (ClassCastException e) {
	                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
	                    }
                    }
                    return buf.toString();
                }
            });
            declarePrimitive(new Profile(plural + "-at", "tandp", "agtset(" + plural + ")=void/num,num"));
            declarePrimitive(new Profile(plural + "-here", "[agt]agtset(" + plural + ")=void/void"));  // this needs to imply patch|turtle, not gglobal . . .
            declarePrimitive(new Profile(plural + "-on", "agtset(" + plural + ")=void/agt!agtset(" + plural + ")=void/agtset"));
        }
        if (singular != null) {
            if (CASE_INSENSITIVE_SYMBOLS) {
                singular = singular.toLowerCase();
            }
            declareSymbol("*BREED*", singular, "singular");
            // references conflict on this procedure spec
            declarePrimitive(new Profile("is-a-" + singular, "bool=void/*"));
            declarePrimitive(new Profile("is-" + singular, "bool=void/*"));
        }
        RelogoClass rlc = findClass(plural);
        rlc.createBreed(plural, singular);
        
    }

    protected void declareDirectedLinkBreed(String plural, String singular) {
        if (plural != null) {
            if (CASE_INSENSITIVE_SYMBOLS) {
                plural = plural.toLowerCase();
            }
            declareSymbol("*DIRECTED-LINK-BREED*", plural, "plural");
            declareClass(plural, "links");
            declarePrimitive(new Profile(plural, "agtset=void/void!agtset=void/sel"));
            declarePrimitive(new Profile("create-" + plural + "-from", "turtles" , "[agt]@void=void/agtset,cmd![agt]void=void/agtset"){
            	public String encodeInstance(ProcedureInvocation pi, int indent) {
                    StringBuffer buf = new StringBuffer();
                    buf.append(getJavaName() + "(");
                    encodeArgument(pi.arguments.get(0), indent, buf);
                    buf.append(")");
                    if (pi.arguments.size() > 1){
	                    try {
	                        Block block = (Block) pi.arguments.get(1);
	                        buf.append(block.toStringAsClosure(indent, null));
	                    } catch (ClassCastException e) {
	                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
	                    }
                    }
                    return buf.toString();
                }
            });
            declarePrimitive(new Profile("create-" + plural + "-to", "turtles" , "[agt]@void=void/agtset,cmd![agt]void=void/agtset"){
            	public String encodeInstance(ProcedureInvocation pi, int indent) {
                    StringBuffer buf = new StringBuffer();
                    buf.append(getJavaName() + "(");
                    encodeArgument(pi.arguments.get(0), indent, buf);
                    buf.append(")");
                    if (pi.arguments.size() > 1){
	                    try {
	                        Block block = (Block) pi.arguments.get(1);
	                        buf.append(block.toStringAsClosure(indent, null));
	                    } catch (ClassCastException e) {
	                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
	                    }
                    }
                    return buf.toString();
                }
            });
            declarePrimitive(new Profile("create-" + plural + "-with", "turtles" , "[agt]@void=void/agtset,cmd![agt]void=void/agtset"){
            	public String encodeInstance(ProcedureInvocation pi, int indent) {
                    StringBuffer buf = new StringBuffer();
                    buf.append(getJavaName() + "(");
                    encodeArgument(pi.arguments.get(0), indent, buf);
                    buf.append(")");
                    if (pi.arguments.size() > 1){
	                    try {
	                        Block block = (Block) pi.arguments.get(1);
	                        buf.append(block.toStringAsClosure(indent, null));
	                    } catch (ClassCastException e) {
	                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
	                    }
                    }
                    return buf.toString();
                }
            });
            declarePrimitive(new Profile("my-in-" + plural, "turtles" , "[agt]agtset(<breeds>)=void/void"));
            declarePrimitive(new Profile("my-out-" + plural, "turtles" , "[agt]agtset(<breeds>)=void/void"));
        }
        if (singular != null) {
            if (CASE_INSENSITIVE_SYMBOLS) {
                singular = singular.toLowerCase();
            }
            declareSymbol("*DIRECTED-LINK-BREED*", singular, "singular");
            declarePrimitive(new Profile("create-" + singular + "-from", "turtles" , "[agt]@void=void/agt,cmd![agt]void=void/agt"){
            	public String encodeInstance(ProcedureInvocation pi, int indent) {
                    StringBuffer buf = new StringBuffer();
                    buf.append(getJavaName() + "(");
                    encodeArgument(pi.arguments.get(0), indent, buf);
                    buf.append(")");
                    if (pi.arguments.size() > 1){
	                    try {
	                        Block block = (Block) pi.arguments.get(1);
	                        buf.append(block.toStringAsClosure(indent, null));
	                    } catch (ClassCastException e) {
	                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
	                    }
                    }
                    return buf.toString();
                }
            });
            declarePrimitive(new Profile("create-" + singular + "-to", "turtles" , "[agt]@void=void/agt,cmd![agt]void=void/agt"){
            	public String encodeInstance(ProcedureInvocation pi, int indent) {
                    StringBuffer buf = new StringBuffer();
                    buf.append(getJavaName() + "(");
                    encodeArgument(pi.arguments.get(0), indent, buf);
                    buf.append(")");
                    if (pi.arguments.size() > 1){
	                    try {
	                        Block block = (Block) pi.arguments.get(1);
	                        buf.append(block.toStringAsClosure(indent, null));
	                    } catch (ClassCastException e) {
	                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
	                    }
                    }
                    return buf.toString();
                }
            });
            declarePrimitive(new Profile("create-" + singular + "-with", "turtles" , "[agt]@void=void/agt,cmd![agt]void=void/agt"){
            	public String encodeInstance(ProcedureInvocation pi, int indent) {
                    StringBuffer buf = new StringBuffer();
                    buf.append(getJavaName() + "(");
                    encodeArgument(pi.arguments.get(0), indent, buf);
                    buf.append(")");
                    if (pi.arguments.size() > 1){
	                    try {
	                        Block block = (Block) pi.arguments.get(1);
	                        buf.append(block.toStringAsClosure(indent, null));
	                    } catch (ClassCastException e) {
	                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
	                    }
                    }
                    return buf.toString();
                }
            });
            declarePrimitive(new Profile("in-" + singular + "-neighbor?", "turtles" , "[agt]bool=void/agt"));
            declarePrimitive(new Profile("in-" + singular + "-neighbors", "turtles" , "[agt]agtset(<breed>)=void/void"));
            declarePrimitive(new Profile("in-" + singular + "-from", "turtles" , "[agt]agt=void/agt"));
            declarePrimitive(new Profile("out-" + singular + "-neighbor?", "turtles" , "[agt]bool=void/agt"));
            declarePrimitive(new Profile("out-" + singular + "-neighbors", "turtles" , "[agt]agtset(<breed>)=void/void"));
            declarePrimitive(new Profile("out-" + singular + "-to", "turtles" , "[agt]agt(<breed>)=void/agt"));
        }
        RelogoClass rlc = findClass(plural);
        rlc.createLinkBreed(plural, singular,true);
    }

    protected void declareUndirectedLinkBreed(String plural, String singular) {
        if (plural != null) {
            if (CASE_INSENSITIVE_SYMBOLS) {
                plural = plural.toLowerCase();
            }
            declareSymbol("*UNDIRECTED-LINK-BREED*", plural, "plural");
            declareClass(plural, "links");
            declarePrimitive(new Profile(plural, "agtset=void/void!agtset=void/sel"));
            declarePrimitive(new Profile("create-" + plural + "-with", "turtles" , "[agt]@void=void/agtset,cmd![agt]void=void/agtset"){
            	public String encodeInstance(ProcedureInvocation pi, int indent) {
                    StringBuffer buf = new StringBuffer();
                    buf.append(getJavaName() + "(");
                    encodeArgument(pi.arguments.get(0), indent, buf);
                    buf.append(")");
                    if (pi.arguments.size() > 1){
	                    try {
	                        Block block = (Block) pi.arguments.get(1);
	                        buf.append(block.toStringAsClosure(indent, null));
	                    } catch (ClassCastException e) {
	                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
	                    }
                    }
                    return buf.toString();
                }
            });
            declarePrimitive(new Profile("my-" + plural, "turtles" , "[agt]agtset(<breeds>)=void/void"));
        }
        if (singular != null) {
            if (CASE_INSENSITIVE_SYMBOLS) {
                singular = singular.toLowerCase();
            }
            declareSymbol("*UNDIRECTED-LINK-BREED*", singular, "singular");
            declarePrimitive(new Profile("create-" + singular + "-with", "turtles" , "[agt]@void=void/agt,cmd![agt]void=void/agt"){
            	public String encodeInstance(ProcedureInvocation pi, int indent) {
                    StringBuffer buf = new StringBuffer();
                    buf.append(getJavaName() + "(");
                    encodeArgument(pi.arguments.get(0), indent, buf);
                    buf.append(")");
                    if (pi.arguments.size() > 1){
	                    try {
	                        Block block = (Block) pi.arguments.get(1);
	                        buf.append(block.toStringAsClosure(indent, null));
	                    } catch (ClassCastException e) {
	                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
	                    }
                    }
                    return buf.toString();
                }
            });
            declarePrimitive(new Profile(singular + "-neighbor?", "turtles" , "[agt]bool=void/agt"));
            declarePrimitive(new Profile(singular + "-neighbors", "turtles" , "[agt]agtset(<breed>)=void/void"));
            declarePrimitive(new Profile(singular + "-with", "turtles" , "[agt]agt=void/agt"));
        }
        RelogoClass rlc = findClass(plural);
        rlc.createLinkBreed(plural, singular,false);
    }

    protected void createBuiltInSymbols() {
        declareBreed("patches", "patch");
        declareAttribute("tandp", "pcolor", false);
        declareAttribute("tandp", "plabel", false);
        declareAttribute("tandp", "plabel-color", false);
        declareAttribute("tandp", "pxcor", false);
        declareAttribute("tandp", "pycor", false);
        declareAttribute("*GLOBAL*", "min-pxcor", -16, false);
        declareAttribute("*GLOBAL*", "min-pycor", -16, false);
        declareAttribute("*GLOBAL*", "max-pxcor", 16, false);
        declareAttribute("*GLOBAL*", "max-pycor", 16, false);
        /* in some implementations, these are handled by an AgentImplementations class. */
        declareBreed("turtles", "turtle");
        declareAttribute("turtles", "breed", false);
        declareAttribute("tandl", "color", false);
        declareAttribute("turtles", "heading", false);
        declareAttribute("tandl", "hidden?", false);
        declareAttribute("tandl", "label", false);
        declareAttribute("tandl", "label-color", false);
        declareAttribute("turtles", "pen-mode", false);
        declareAttribute("turtles", "pen-size", false);
        declareAttribute("tandl", "shape", false);
        declareAttribute("turtles", "size", false);
        declareAttribute("turtles", "who", false);
        declareAttribute("turtles", "xcor", false);
        declareAttribute("turtles", "ycor", false);
        declareBreed("links", "link");
        declareAttribute("links", "breed", false);
        declareAttribute("links", "color", false);
        declareAttribute("links", "end1", false);
        declareAttribute("links", "end2", false);
        declareAttribute("links", "hidden?", false);
        declareAttribute("links", "label", false);
        declareAttribute("links", "label-color", false);
        declareAttribute("links", "shape", false);
        declareAttribute("links", "thickness", false);
        declareAttribute("links", "tie-mode", false);
         /* */
        // create anonymous base classes for directed and undirected links
        declareClass("*ulink*", "links");
        declareClass("*dlink*", "links");
    }

    public void declarePrimitive(Profile prof) {
        declareSymbol("*PROCEDURE*", prof.name, prof);
    }

    public void initPrimitives() {
        Profile opProf = null;
        /* */
        // list creation
        declarePrimitive(new Profile("CB", "list=void/void"));
//Turtle-related 
        declarePrimitive(new Profile("back", "turtles" , "[turtle]void=void/num"));
        declarePrimitive(new Profile("bk", "turtles" , "[turtle]void=void/num"));
        declarePrimitive(new Profile("can-move?", "turtles" , "[turtle]bool=void/num"));
//        declarePrimitive(new Profile("create-ordered-turtles", "[obs]void=void/num![obs]void=void/num,cmd"));
        declarePrimitive(new Profile("cro", "[obs]void=void/num![obs]@void=void/num,cmd"));
//        declarePrimitive(new Profile("create-turtles", "[obs]void=void/num![obs]@void=void/num,cmd"));
        declarePrimitive(new Profile("crt", "[obs]void=void/num![obs]@void=void/num,cmd"));
        declarePrimitive(new Profile("die", "tandl" , "[turtle]void=void/void"));
        declarePrimitive(new Profile("distance", "tandp" , "[agt]num=void/agt"));
        declarePrimitive(new Profile("distancexy", "tandp" , "[agt]num=void/num,num"));
        declarePrimitive(new Profile("downhill", "turtles" , "[agt]void=void/rpt"){
        	
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append(getJavaName() + "(\"");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append("\")");
                return buf.toString();
            }
        	
        });
        declarePrimitive(new Profile("downhill4", "turtles" , "[agt]void=void/rpt"){
        	
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append(getJavaName() + "(\"");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append("\")");
                return buf.toString();
            }
        	
        });
        declarePrimitive(new Profile("dx", "turtles" , "[agt]num=void/void"));
        declarePrimitive(new Profile("dy", "turtles" , "[agt]num=void/void"));
        declarePrimitive(new Profile("face", "turtles" , "[agt]void=void/agt"));
        declarePrimitive(new Profile("facexy", "turtles" , "[agt]void=void/num,num"));
        declarePrimitive(new Profile("forward", "turtles" , "[turtle]void=void/num"));
        declarePrimitive(new Profile("fd", "turtles" , "[turtle]void=void/num"));
        declarePrimitive(new Profile("hatch", "turtles" , "[turtle]@void=void/num,cmd![turtle]void=void/num"));
        declarePrimitive(new Profile("hide-turtle", "turtles" , "[turtle]void=void/void"));
        declarePrimitive(new Profile("ht", "turtles" , "[turtle]void=void/void"));
        declarePrimitive(new Profile("home", "turtles" , "[turtle]void=void/void"));
        declarePrimitive(new Profile("inspect", "[obs]void=void/agt"));
        declarePrimitive(new Profile("is-turtle?", "bool=void/*"));
        declarePrimitive(new Profile("jump", "turtles" , "[turtle]void=void/num"));
        declarePrimitive(new Profile("left", "turtles" , "[turtle]void=void/num"));
        declarePrimitive(new Profile("lt", "turtles" , "[turtle]void=void/num"));
        declarePrimitive(new Profile("move-to", "turtles" , "[turtle]void=void/agt"));
        // myself is owner of previous (calling) context
        declarePrimitive(new Profile("myself","tandp", "[agt]agt=void/void"));
        declarePrimitive(new Profile("nobody", "agt=void/void"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                if (getPreSize() > 0) {
                    encodeArgument(pi.arguments.get(0), indent, buf);
                    buf.append(" ");
                    buf.append(getJavaName());
                    for (int i = 0; i < getPostSize(); i++) {
                        buf.append(" ");
                        encodeArgument(pi.arguments.get(i + 1), indent, buf);
                    }
                } else {
                    buf.append("null");
                }
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("no-turtles", "agtset(turtles)=void/void"));
        declarePrimitive(new Profile("of", "@list=rpt/agtset!@*=rpt/agt") {
            @Override
            public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                try {
                    Block block = (Block) pi.arguments.get(0);
                    buf.append(block.toStringAsClosure(indent, null));
                } catch (ClassCastException e) {
                    buf.append("{not a block! (" + pi.arguments.get(0).getClass().getName() + ")}");
                }
                buf.append(".of(");
                encodeArgument(pi.arguments.get(1), indent, buf);
                buf.append(")");
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("other","tandp", "[agt]agtset=void/agtset"));
        declarePrimitive(new Profile("patch-ahead", "turtles" , "[turtle]agt(patches)=void/num"));
        declarePrimitive(new Profile("patch-at", "tandp", "[agt]agt(patches)=void/num,num"));
        declarePrimitive(new Profile("patch-at-heading-and-distance", "tandp" , "[agt]agt(patches)=void/num,num"));
        declarePrimitive(new Profile("patch-here", "turtles" , "[turtle]agt(patches)=void/void"));
        declarePrimitive(new Profile("patch-left-and-ahead", "turtles" , "[turtle]agt(patches)=void/num,num"));
        declarePrimitive(new Profile("patch-right-and-ahead", "turtles" , "[turtle]agt(patches)=void/num,num"));
        declarePrimitive(new Profile("pen-down", "turtles" , "[agt]void=void/void"));
        declarePrimitive(new Profile("pd", "turtles" , "[agt]void=void/void"));
        declarePrimitive(new Profile("pen-erase", "turtles" , "[agt]void=void/void"));
        declarePrimitive(new Profile("pe", "turtles" , "[agt]void=void/void"));
        declarePrimitive(new Profile("pen-up", "turtles" , "[agt]void=void/void"));
        declarePrimitive(new Profile("pu", "turtles" , "[agt]void=void/void"));
        declarePrimitive(new Profile("random-xcor", "num=void/void"));
        declarePrimitive(new Profile("random-ycor", "num=void/void"));
        declarePrimitive(new Profile("right", "turtles" , "[turtle]void=void/num"));
        declarePrimitive(new Profile("rt", "turtles", "[turtle]void=void/num"));
        declarePrimitive(new Profile("self", "tandp" , "[agt]agt=void/void"));
        declarePrimitive(new Profile("set-default-shape", "[obs]void=void/str,str"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }

				buf.append(getJavaName() + "(\"");// +
													// encodeArguments(pi.arguments,
													// indent) + ")");
				Object o = pi.arguments.get(0);
				if (o instanceof ProcedureInvocation) {
					Profile pf = ((ProcedureInvocation) o).getProfile();
					String st = WizardUtilities.capitalize(pf.getJavaName());
					buf.append(st);
				}
				buf.append("\",");
				encodeArgument(pi.arguments.get(1), indent, buf);
				buf.append(")");

                return buf.toString();
            }
        });
        declarePrimitive(new Profile("__set-line-thickness", "turtles" , "[turtle]void=void/num"));
        declarePrimitive(new Profile("setxy", "turtles" , "[turtle]void=void/num,num"));
        declarePrimitive(new Profile("shapes", "list(str)=void/void"));
        declarePrimitive(new Profile("show-turtle", "turtles" , "[turtle]void=void/void"));
        declarePrimitive(new Profile("st", "turtles" , "[turtle]void=void/void"));
        declarePrimitive(new Profile("sprout", "patches" , "[patch]@void=void/num,cmd![patch]void=void/num"));
        //sprout-<breeds>    void=void/num{,cmd}"));
        declarePrimitive(new Profile("stamp", "tandl" , "[agt]void=void/void"));  // turtle or link
        declarePrimitive(new Profile("stamp-erase", "tandl" , "[agt]void=void/void"));
        declarePrimitive(new Profile("subject", "[obs]agt=void/void"));
        declarePrimitive(new Profile("subtract-headings", "num=void/num,num"));
        declarePrimitive(new Profile("towards", "tandp" , "[agt]num=void/agt"));
        declarePrimitive(new Profile("towardsxy", "tandp" , "[agt]num=void/num,num"));
        declarePrimitive(new Profile("turtle", "agt=void/num"));
        declarePrimitive(new Profile("turtle-set", "agtset(turtles)=void/*"));
        declarePrimitive(new Profile("(turtle-set", "(agtset(turtles)=void/*"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                buf.append("turtleSet(" + encodeArguments(pi.arguments, indent) + ")");
                return buf.toString();
            }
        	
        });
        //declarePrimitive(new Profile("turtles-at", "[agt]agtset=void/num,num"));
        //declarePrimitive(new Profile("turtles-here", "[agt]agtset=void/void"));
        //declarePrimitive(new Profile("turtles-on", "[agt]agtset=void/num,num"));
        declarePrimitive(new Profile("uphill", "turtles" , "[agt]void=void/var"){
        	
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append(getJavaName() + "(\"");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append("\")");
                return buf.toString();
            }
        	
        });
        declarePrimitive(new Profile("uphill4", "turtles" , "[agt]void=void/var"){
        	
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append(getJavaName() + "(\"");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append("\")");
                return buf.toString();
            }
        	
        });

//Patch-related 
        declarePrimitive(new Profile("cpd", "void=void/void"));
        declarePrimitive(new Profile("diffuse", "[obs]void=void/str,num"){
        	
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append("diffuse(");
                buf.append(encodeArguments(pi.arguments,indent));
//                Object o = pi.arguments.get(0);
//                if (o instanceof Attribute){
//                	String st = ((Attribute)o).toString();
//                	buf.append(st);
//                }
//                buf.append("\",");
//                encodeArgument(pi.arguments.get(1), indent, buf);
                buf.append(")");
                return buf.toString();
            }
        	
        	public Object disambiguateParameter(int pos, int postCnt, Object actual) {
                Object typeCode = null;
                switch (pos) {
                    case -1:
                        typeCode = pre;
                        break;
                    case 0:
                        break;
                    default:
                        if (isVarargs()) {
                            if (post.length == 1) {
                                typeCode = post[0];
                            } else if (pos < (postCnt - post.length) + 1) {
                                typeCode = post[0];
                            } else {
                                pos = pos - (postCnt - post.length);
                                try {
                                    typeCode = post[pos - 1];
                                } catch (IndexOutOfBoundsException ioobe) {
                                    ioobe.printStackTrace();
                                }
                            }
                        } else {
                            try {
                                typeCode = post[pos - 1];
                            } catch (IndexOutOfBoundsException ioobe) {
                                ioobe.printStackTrace();
                            }
                        }
                        break;
                    }
                if (actual instanceof LinkedList) {
                    // greedily grab the first interpretation that is compatible
                    for (Object o : ((LinkedList) actual)) {
                        if (typeIsCompatible(typeCode, o)) {
                            actual = o;
                        }
                    }
                }
                // if the typeCode specifies a closure, we need to make sure we have
                // a block that can be marked as a closure. Other type checks would
                // be good, too, but the closure activities have to be done to support
                // distribution of methods to agent classes.
                if (typeCode.toString().equals("cmd")) {
                    if (actual instanceof Block) {
                        ((Block) actual).setType("void-closure");
                    } else {
                        //System.err.println("*** void closure required, creating block");
                        Block voidBlock = new Block();
                        voidBlock.setType("void-closure");
                        LinkedList contents = new LinkedList();
                        contents.add(actual);
                        voidBlock.setContents(contents);
                        actual = voidBlock;
                    }
                } else if (typeCode.toString().equals("rpt")) {
                    if (actual instanceof Block) {
                        ((Block) actual).setType("value-closure");
                    } else {
                        //System.err.println("*** value closure required, creating block: "+actual);
                        // NOTE: empirically this happens quite a bit; should look
                        // more closely at the symbol table to see if it's necessary.
                        Block valueBlock = new Block();
                        valueBlock.setType("value-closure");
                        LinkedList contents = new LinkedList();
                        contents.add(actual);
                        valueBlock.setContents(contents);
                        actual = valueBlock;
                    }
                }
                else if (typeCode.toString().equals("str")){
                	if (actual instanceof Attribute){
                		StringBuffer sb = new StringBuffer();
                		sb.append("\"");
                    	sb.append(((Attribute)actual).toString());
                    	sb.append("\"");
                    	actual = sb.toString();
                    }
                }
                return actual;
            }
        });
        declarePrimitive(new Profile("diffuse4", "[obs]void=void/str,num"){
        	
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append("diffuse4(");
                buf.append(encodeArguments(pi.arguments,indent));
//                Object o = pi.arguments.get(0);
//                if (o instanceof Attribute){
//                	String st = ((Attribute)o).toString();
//                	buf.append(st);
//                }
//                buf.append("\",");
//                encodeArgument(pi.arguments.get(1), indent, buf);
                buf.append(")");
                return buf.toString();
            }
        	
        	public Object disambiguateParameter(int pos, int postCnt, Object actual) {
                Object typeCode = null;
                switch (pos) {
                    case -1:
                        typeCode = pre;
                        break;
                    case 0:
                        break;
                    default:
                        if (isVarargs()) {
                            if (post.length == 1) {
                                typeCode = post[0];
                            } else if (pos < (postCnt - post.length) + 1) {
                                typeCode = post[0];
                            } else {
                                pos = pos - (postCnt - post.length);
                                try {
                                    typeCode = post[pos - 1];
                                } catch (IndexOutOfBoundsException ioobe) {
                                    ioobe.printStackTrace();
                                }
                            }
                        } else {
                            try {
                                typeCode = post[pos - 1];
                            } catch (IndexOutOfBoundsException ioobe) {
                                ioobe.printStackTrace();
                            }
                        }
                        break;
                    }
                if (actual instanceof LinkedList) {
                    // greedily grab the first interpretation that is compatible
                    for (Object o : ((LinkedList) actual)) {
                        if (typeIsCompatible(typeCode, o)) {
                            actual = o;
                        }
                    }
                }
                // if the typeCode specifies a closure, we need to make sure we have
                // a block that can be marked as a closure. Other type checks would
                // be good, too, but the closure activities have to be done to support
                // distribution of methods to agent classes.
                if (typeCode.toString().equals("cmd")) {
                    if (actual instanceof Block) {
                        ((Block) actual).setType("void-closure");
                    } else {
                        //System.err.println("*** void closure required, creating block");
                        Block voidBlock = new Block();
                        voidBlock.setType("void-closure");
                        LinkedList contents = new LinkedList();
                        contents.add(actual);
                        voidBlock.setContents(contents);
                        actual = voidBlock;
                    }
                } else if (typeCode.toString().equals("rpt")) {
                    if (actual instanceof Block) {
                        ((Block) actual).setType("value-closure");
                    } else {
                        //System.err.println("*** value closure required, creating block: "+actual);
                        // NOTE: empirically this happens quite a bit; should look
                        // more closely at the symbol table to see if it's necessary.
                        Block valueBlock = new Block();
                        valueBlock.setType("value-closure");
                        LinkedList contents = new LinkedList();
                        contents.add(actual);
                        valueBlock.setContents(contents);
                        actual = valueBlock;
                    }
                }
                else if (typeCode.toString().equals("str")){
                	if (actual instanceof Attribute){
                		StringBuffer sb = new StringBuffer();
                		sb.append("\"");
                    	sb.append(((Attribute)actual).toString());
                    	sb.append("\"");
                    	actual = sb.toString();
                    }
                }
                return actual;
            }
        });
        declarePrimitive(new Profile("import-pcolors", "void=void/str"));
        declarePrimitive(new Profile("import-pcolors-rgb", "void=void/str"));
        declarePrimitive(new Profile("is-patch?", "bool=void/void"));
        declarePrimitive(new Profile("patch", "patches=void/num,num"));
        declarePrimitive(new Profile("random-pxcor", "num=void/void"));
        declarePrimitive(new Profile("random-pycor", "num=void/void"));

        //Agentset 
        declarePrimitive(new Profile("all?", "@bool=void/agtset,rpt"));
        declarePrimitive(new Profile("any?", "bool=void/agtset") {
            
            public Object execute(LinkedList args, Object context) {
                // single argument must be agentset
                // return value is true if agentset not empty
                Collection agtset = (Collection) args.get(0);
                return !agtset.isEmpty();
            }
        });
        declarePrimitive(new Profile("at-points", "@agtset=agtset/list(list(num))"));
        declarePrimitive(new Profile("count", "num=void/agtset"));
        declarePrimitive(new Profile("in-cone", "turtles" , "[turtle]agtset=agtset/num,num"));
        declarePrimitive(new Profile("incone", "agtset=agtset/num,num"));
        declarePrimitive(new Profile("in-radius", "tandp" , "[agt]gtset=agtset/num"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append("inRadius(");
                buf.append(encodeArguments(pi.arguments, indent));
                buf.append(")");
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("inradius", "turtles" , "[agt]agtset=agtset/num"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append("inRadius(");
                buf.append(encodeArguments(pi.arguments, indent));
                buf.append(")");
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("is-agent?", "bool=void/*"));
        declarePrimitive(new Profile("is-agentset?", "bool=void/*"));
        declarePrimitive(new Profile("is-patch-set?", "bool=void/*"));
        declarePrimitive(new Profile("is-turtle-set?", "bool=void/*"));
        declarePrimitive(new Profile("link-heading", "[link]num=void/void"));
        declarePrimitive(new Profile("link-length", "[link]num=void/void"));
        declarePrimitive(new Profile("link-set", "agtset=void/*"));
        declarePrimitive(new Profile("(link-set", "(agtset=void/*"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                buf.append("linkSet(" + encodeArguments(pi.arguments, indent) + ")");
                return buf.toString();
            }
        	
        });
        declarePrimitive(new Profile("link-shapes", "list=void/void"));
        declarePrimitive(new Profile("max-n-of", "@agtset=void/num,agtset,rpt"));
        declarePrimitive(new Profile("max-one-of", "@agtset=void/agtset,rpt"));
        declarePrimitive(new Profile("min-n-of", "@agtset=void/num,agtset,rpt"));
        declarePrimitive(new Profile("min-one-of", "@agtset=void/agtset,rpt"));
        declarePrimitive(new Profile("n-of", "agtset=void/num,agtset!list=void/num,list"));
        declarePrimitive(new Profile("neighbors","tandp", "[agt]agtset(patches)=void/void"));
        declarePrimitive(new Profile("neighbors4","tandp", "[agt]agtset(patches)=void/void"));
        declarePrimitive(new Profile("no-patches", "agtset(patches)=void/void"));
        declarePrimitive(new Profile("one-of", "agt=void/agtset!*=void/list(*)") {
        	
        	@Override
            protected String getReturnType(ProcedureInvocation pi) {
                Object agtset = pi.arguments.get(0);
                if (agtset instanceof ProcedureInvocation) {
                    return ((ProcedureInvocation) agtset).calculateType(result);
                }
                return result;
            }
        	
        });
        declarePrimitive(new Profile("patch-set", "agtset(patches)=void/*"));
        declarePrimitive(new Profile("(patch-set", "(agtset(patches)=void/*"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                buf.append("patchSet(" + encodeArguments(pi.arguments, indent) + ")");
                return buf.toString();
            }
        	
        });
        declarePrimitive(new Profile("sort", "list=void/agtset!list=void/list"));
        declarePrimitive(new Profile("sort-by", "@list=void/rpt,agtset!list=void/rpt,list"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                buf.append(getJavaName() + "(");// + encodeArguments(pi.arguments, indent) + ")");
                Object o = pi.arguments.get(0);
                if (o instanceof Block){
                	String tempString = ((Block)o).toString(indent + 4);
                	String resultString = "{ Q ->" + tempString.substring(1);
                	buf.append(resultString);
                }
                else {
                	System.err.println("The first argument of sort-by must be a command block");
                }
                buf.append(",");
                encodeArgument(pi.arguments.get(1), indent, buf);
                buf.append(")");
                return buf.toString();
            }
        	
        });
        declarePrimitive(new Profile("with", "@agtset=agtset/rpt") {

            @Override
            protected String getReturnType(ProcedureInvocation pi) {
                Object agtset = pi.arguments.get(0);
                if (agtset instanceof ProcedureInvocation) {
                    return ((ProcedureInvocation) agtset).calculateType(result);
                }
                return result;
            }

            public Object execute(LinkedList args, Object context) {
                // first argument should be agent or agentset
                // second argument should be a block
                // return value is subset of agents in starting set
                LinkedList retset = new LinkedList();
                Collection agtset = (Collection) args.get(0);
                Block cmd = (Block) args.get(1);
                if (agtset == null || cmd == null) {
                    System.err.println("WITH failure: ");
                    System.err.println("   agtset == " + agtset);
                    System.err.println("   cmd == " + cmd);
                    return null;
                }
                for (Object o : agtset) {
                    Boolean rslt = (Boolean) cmd.execute(o);
                    if (rslt == Boolean.TRUE) {
                        retset.add(o);
                    //System.out.println("accepting "+o);
                    } else {
                    //System.out.println("excluding "+o);
                    }
                }
                return retset;
            }

            public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append(".with ");
                try {
                    Block block = (Block) pi.arguments.get(1);
                    buf.append(block.toStringAsClosure(indent, null));
                } catch (ClassCastException e) {
                    buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
                }
                buf.append("");
                return buf.toString();
            }

        });
        declarePrimitive(new Profile("with-max", "@agtset=agtset/rpt"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append("(");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append(").withMax(");
                try {
                    Block block = (Block) pi.arguments.get(1);
                    buf.append(block.toStringAsClosure(indent, null));
                } catch (ClassCastException e) {
                    buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
                }
                buf.append(")");
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("with-min", "@agtset=agtset/rpt"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append("(");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append(").withMin(");
                try {
                    Block block = (Block) pi.arguments.get(1);
                    buf.append(block.toStringAsClosure(indent, null));
                } catch (ClassCastException e) {
                    buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
                }
                buf.append(")");
                return buf.toString();
            }
        });

        declarePrimitive(new Profile("approximate-hsb", "void=void/void") {

            public Object execute(LinkedList args, Object context) {
                throw new UnsupportedOperationException();
            }
            });
        declarePrimitive(new Profile("approximate-rgb", "void=void/void") {

            public Object execute(LinkedList args, Object context) {
                throw new UnsupportedOperationException();
            }
            });
        declarePrimitive(new Profile("base-colors", "list=void/void") {

            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.baseColors();
            }
            });
        declarePrimitive(new Profile("extract-hsb", "void=void/color") {

            public Object execute(LinkedList args, Object context) {
                throw new UnsupportedOperationException();
            }
            });
        declarePrimitive(new Profile("extract-rgb", "void=void/color") {

            public Object execute(LinkedList args, Object context) {
                throw new UnsupportedOperationException();
            }
            });
        declarePrimitive(new Profile("hsb", "list(int)=void/num,num,num") {

            public Object execute(LinkedList args, Object context) {
                int r = ((Number) args.get(0)).intValue();
                if (r < 0 || r > 255) {
                    throw new IllegalArgumentException("HSV component out of range.");
                }
                int g = ((Number) args.get(1)).intValue();
                if (g < 0 || g > 255) {
                    throw new IllegalArgumentException("HSV component out of range.");
                }
                int b = ((Number) args.get(2)).intValue();
                if (b < 0 || b > 255) {
                    throw new IllegalArgumentException("HSV component out of range.");
                }
                return Color.getHSBColor((float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f);
            }
            });
        declarePrimitive(new Profile("rgb", "list(int)=void/num,num,num") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                int r = ((Number) args.get(0)).intValue();
                if (r < 0 || r > 255) {
                    throw new IllegalArgumentException("RGB component out of range.");
                }
                int g = ((Number) args.get(1)).intValue();
                if (g < 0 || g > 255) {
                    throw new IllegalArgumentException("RGB component out of range.");
                }
                int b = ((Number) args.get(2)).intValue();
                if (b < 0 || b > 255) {
                    throw new IllegalArgumentException("RGB component out of range.");
                }
                return new Color(r, g, b);
            }
            });
        //TODO: check if this can be easily implemented (might just be an issue of my end)
        declarePrimitive(new Profile("scale-color", "num=void/num,num,num,num") {

            public Object execute(LinkedList args, Object context) {
                throw new UnsupportedOperationException();
            }
            });
        //TODO: check if this can be easily implemented (might just be an issue of my end)
        declarePrimitive(new Profile("shade-of?", "bool=void/color,color") {

            public Object execute(LinkedList args, Object context) {
                throw new UnsupportedOperationException();
            }
            });
        declarePrimitive(new Profile("wrap-color", "color=void/num") {

            public Object execute(LinkedList args, Object context) {
                double d = ((Number) args.get(0)).doubleValue();
                if (d < 0.0) {
                    while (d < 0.0) {
                        d += 140.0;
                    }
                } else if (d >= 140.0) {
                    while (d >= 140.0) {
                        d -= 140.0;
                    }
                }
                return d;
            }
            });
        declarePrimitive(new Profile("black", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(0);
            }
            });
        declarePrimitive(new Profile("gray", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(5);
            }
            });
        declarePrimitive(new Profile("grey", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(5);
            }
            });
        declarePrimitive(new Profile("white", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(9.99);
            }
            });
        declarePrimitive(new Profile("red", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(15);
            }
            });
        declarePrimitive(new Profile("orange", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(25);
            }
            });
        declarePrimitive(new Profile("brown", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(35);
            }
            });
        declarePrimitive(new Profile("yellow", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(45);
            }
            });
        declarePrimitive(new Profile("green", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(55);
            }
            });
        declarePrimitive(new Profile("lime", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(65);
            }
            });
        declarePrimitive(new Profile("turquoise", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(75);
            }
            });
        declarePrimitive(new Profile("cyan", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(85);
            }
            });
        declarePrimitive(new Profile("sky", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(95);
            }
            });
        declarePrimitive(new Profile("blue", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(105);
            }
            });
        declarePrimitive(new Profile("violet", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(115);
            }
            });
        declarePrimitive(new Profile("magenta", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(125);
            }
            });
        declarePrimitive(new Profile("pink", "color=void/void") {
            // Reports a RGB list when three numbers describing an RGB color. The numbers are range checked to be between 0 and 255.
            // NOTE: for this interpreter, going straight to java.awt.Color rather than list.
            public Object execute(LinkedList args, Object context) {
                return ReLogoSupport.lookupColor(135);
            }
            });


        //Control flow and logic 
        declarePrimitive(opProf = new Profile("and", "bool=bool/bool") {

            @Override
            public String getJavaName() {
                return "&&";
            }

            @Override
            public Object execute(LinkedList args, Object context) {
                return ((Boolean) args.get(0)) && ((Boolean) args.get(1));
            }
        });
        opProf.setPrecedence(80);
        Profile askProfileSingle = new Profile("ask", "[obs]@void=void/agt,cmd![obs]@void=void/agtset,cmd") {

            // how do you apply a closure to a single object?
            // trying it by building an array to feed the iterator.
            @Override
            
            
            public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append("ask (");
                Object o = pi.arguments.get(0);
                if (o instanceof Block) {
                	Block block = (Block)o;
                	if (block.contents != null && block.contents.size() == 1 && block.contents.get(0) instanceof ProcedureInvocation) {
                		ProcedureInvocation proc = (ProcedureInvocation)block.contents.get(0);
                		String type = proc.type==null ? "" : proc.type.toString();
                        buf.append(proc.toString(indent + 4));
                	} else {
                        buf.append(block.toString(indent + 4));
                	}
                } else if (o instanceof ProcedureInvocation) {
            		ProcedureInvocation proc = (ProcedureInvocation)o;
            		String type = proc.type==null ? "" : proc.type.toString();
                    buf.append(proc.toString(indent + 4));
                } else if (o == null) {
                    buf.append(" /* NULL BLOCK NO-OP */ ");
                } else {
//                    buf.append("[");
                    buf.append(o.toString());
//                    buf.append("]");
                }
                buf.append(")");
                try {
                    Block block = (Block) pi.arguments.get(1);
                    buf.append(block.toStringAsClosure(indent, null));
                } catch (ClassCastException e) {
                    buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
                }
                return buf.toString();
            }

            public Object execute(LinkedList args, Object context) {
                // first argument should be agent or agentset
                // second argument should be a block
                Collection agtset = (Collection) args.get(0);
                Block cmd = (Block) args.get(1);
                if (agtset == null || cmd == null) {
                    System.err.println("ASK failure: ");
                    System.err.println("   agtset == " + agtset);
                    System.err.println("   cmd == " + cmd);
                    return null;
                }
                for (Object o : agtset) {
                    cmd.execute(o);
                }
                return null;
            }
        };
        askProfileSingle.setPrecedence(80);
        declarePrimitive(askProfileSingle);
        //TODO: might need to make this unsupported
        declarePrimitive(new Profile("ask-concurrent", "@void=void/agtset,cmd"));
        declarePrimitive(new Profile("carefully", "void=void/cmd,cmd"));  // try block
        declarePrimitive(new Profile("end", "void=void/void"));
        declarePrimitive(new Profile("every", "[agt]void=void/num,cmd"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append("/* -- \"every\" is not currently implemented \n " + getJavaName() + "(" + encodeArguments(pi.arguments, indent) + ") */");
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("error-message", "str=void/void"));
        declarePrimitive(new Profile("foreach", "void=void/list(*),cmd") {
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                buf.append(getJavaName() + "(");// + encodeArguments(pi.arguments, indent) + ")");
                Object o = pi.arguments.get(1);
                if (o instanceof Block){
                	String tempString = ((Block)o).toString(indent + 4);
                	String resultString = "{ Q ->" + tempString.substring(1);
                	buf.append(resultString);
                }
                else {
                	System.err.println("The second argument of foreach must be a command block");
                }
                buf.append(",");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append(")");
                return buf.toString();
            }
        	
        });   // possibly problematic as written
        declarePrimitive(new Profile("(foreach", "(void=void/*") {
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                buf.append("foreach(");
                int argumentsLength = pi.arguments.size();
                Object o = pi.arguments.get(argumentsLength - 1);
                if (o instanceof Block){
                	((Block)o).setType("void-closure");
                	String tempString = ((Block)o).toString(indent + 4);
                	StringBuffer argumentsBuffer = new StringBuffer();
                	for (int i = 1; i < argumentsLength ; i++){
                		argumentsBuffer.append("Q"+ i + ", ");
                	}
                	int lengthOfArgumentsBuffer = argumentsBuffer.length();
                	String argumentsString = argumentsBuffer.toString().substring(0, lengthOfArgumentsBuffer - 2);// eats up the last comma 
                	String resultString = "{ " + argumentsString + " ->" + tempString.substring(1);
                	buf.append(resultString);
                }
                else {
                	System.err.println("The last argument of foreach must be a command block");
                }
                for (int i = 0; i < argumentsLength - 1 ; i++){
                	buf.append(",");
                	encodeArgument(pi.arguments.get(i), indent, buf);
                }
                buf.append(")");
                return buf.toString();
            }
        	
        });
        declarePrimitive(new Profile("if", "void=void/bool,cmd") {

            public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append("if (");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append(") ");
                try {
                    Block block = (Block) pi.arguments.get(1);
                    buf.append(block.toString(indent));
                } catch (NullPointerException npe) {
                    buf.append("{NULL?: " + pi.arguments.get(1) + "}");
                } catch (ClassCastException cce) {
                    buf.append("{NOT A BLOCK: " + pi.arguments.get(1) + "}");
                }
                return buf.toString();
            }
        });

        declarePrimitive(new Profile("ifelse", "void=void/bool,cmd,cmd") {

            public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append("if (");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append(") ");
                try {
                    Block block = (Block) pi.arguments.get(1);
                    buf.append(block.toString(indent));
                } catch (ClassCastException cce) {
                    buf.append("{NOT A BLOCK: " + pi.arguments.get(1) + "}");
                }
                buf.append(" else ");
                try {
                    Block block = (Block) pi.arguments.get(2);
                    buf.append(block.toString(indent));
                } catch (ClassCastException cce) {
                    buf.append("{NOT A BLOCK: " + pi.arguments.get(2) + "}");
                }
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("ifelse-value", "void=void/bool,rpt,rpt") {

            public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append("{ if (");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append(") ");
                encodeArgument(pi.arguments.get(1), indent, buf);
                buf.append(" else ");
                encodeArgument(pi.arguments.get(2), indent, buf);
                buf.append("}");
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("let", "void=void/*,*"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append("def ");
                Object o = pi.arguments.get(0);
                if (o instanceof Attribute){
                	String st = ((Attribute)o).toString();
                	buf.append(st);
                }
                buf.append(" = ");
                o = pi.arguments.get(1);
                if (o instanceof Block){
                	buf.append(((Block)o).toStringAsList());
                }
                else {
                	encodeArgument(pi.arguments.get(1), indent, buf);
                }
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("loop", "void=void/cmd"));
        // note: syntax of extended 'map' differs from other uses
        declarePrimitive(new Profile("map", "void=void/rpt,list(*)"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                buf.append(getJavaName() + "(");// + encodeArguments(pi.arguments, indent) + ")");
                Object o = pi.arguments.get(0);
                if (o instanceof Block){
                	String tempString = ((Block)o).toString(indent + 4);
                	String resultString = "{ Q ->" + tempString.substring(1);
                	buf.append(resultString);
                }
                else {
                	System.err.println("The first argument of map must be a command block");
                }
                buf.append(",");
                encodeArgument(pi.arguments.get(1), indent, buf);
                buf.append(")");
                return buf.toString();
            }
        	
        });
        declarePrimitive(new Profile("(map", "(void=void/*"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                buf.append("map(");
                int argumentsLength = pi.arguments.size();
                Object o = pi.arguments.get(0);
                if (o instanceof Block){
                	((Block)o).setType("void-closure");
                	String tempString = ((Block)o).toString(indent + 4);
                	StringBuffer argumentsBuffer = new StringBuffer();
                	for (int i = 1; i < argumentsLength ; i++){
                		argumentsBuffer.append("Q"+ i);
                		if (i < argumentsLength - 1){
                			argumentsBuffer.append(", ");
                		}
                	}
//                	int lengthOfArgumentsBuffer = argumentsBuffer.length();
//                	String argumentsString = argumentsBuffer.toString().substring(0, lengthOfArgumentsBuffer - 2);// eats up the last comma 
                	String resultString = "{ " + argumentsBuffer.toString() + " ->" + tempString.substring(1);
                	buf.append(resultString);
                }
                else {
                	System.err.println("The last argument of map must be a command block");
                }
                buf.append(",");
                for (int i = 1; i < argumentsLength ; i++){
                	encodeArgument(pi.arguments.get(i), indent, buf);
                	if (i < argumentsLength - 1) {
                		buf.append(",");
                	}
                }
                
                buf.append(")");
                return buf.toString();
            }
        	
        });
        declarePrimitive(new Profile("not", "bool=void/bool") {

            @Override
            public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append("!");
                try {
                    encodeArgument(pi.arguments.get(0), indent, buf);
                } catch (Exception e) {
                    System.err.println("<ERR>: null pointer in invocation of 'not'");
                }
                return buf.toString();
            }

            @Override
            public Object execute(LinkedList args, Object context) {
                Object arg = args.get(0);
                if (arg instanceof Boolean) {
                    return !((Boolean) arg);
                }
                if (arg == null) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        declarePrimitive(opProf = new Profile("or", "bool=bool/bool") {

            @Override
            public String getJavaName() {
                return "||";
            }

            @Override
            public Object execute(LinkedList args, Object context) {
                return ((Boolean) args.get(0)) || ((Boolean) args.get(1));
            }
        });
        opProf.setPrecedence(80);
        declarePrimitive(new Profile("repeat", "void=void/num,cmd"));
        declarePrimitive(new Profile("report", "*=void/*"){
        	@Override
            public String getJavaName() {
                return "return";
            }
        	
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append(getJavaName() + " ");
                encodeArgument(pi.arguments.get(0),indent,buf);
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("run", "[agt]void=void/str"));
        declarePrimitive(new Profile("runresult", "[agt]*=void/str"));
        declarePrimitive(new Profile("set", "void=void/var,*") {

            public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                Object ovar = pi.arguments.get(0);
                Attribute var = disambiguate(ovar);
                if (var != null) {
                    buf.append(var.toString());
                } else {
                	// The Attribute was already converted to its String representation.
//                	buf.append(ovar);
                    buf.append("{not an attribute: " + ovar + "}");
                }
                buf.append(" = ");
                encodeArgument(pi.arguments.get(1), indent, buf);
                /*if (var.toString().equals("breed")){
                	buf.append("\"");
                	Object oo = pi.arguments.get(1);
                	String st;
                	if (oo instanceof Attribute && ((Attribute) oo).getBreed().equals("*FORMAL*")){
                		st = 
                	} else {
						Profile pf = ((ProcedureInvocation) oo).getProfile();
						st = pf.getJavaName();
                	}
					buf.append(st);
    				buf.append("\"");
                }
                else {
                	encodeArgument(pi.arguments.get(1), indent, buf);
                }*/
//                buf.append(")");
                return buf.toString();
            }

            /**
             * Execute the code associated with this Profile for the specified
             * ProcedureInvocation, using the specified object as the context object.
             * Set does this differently than most, so it must override this.
             * 
             * @param pi
             * @return
             */
            @Override
            public Object executeInstance(ProcedureInvocation pi, Object context) {
                LinkedList args = new LinkedList();
                if (pi.arguments != null) {
                    for (Object arg : pi.arguments) {
                        if (arg instanceof Attribute) {
                            args.add(arg);
                        } else if (arg instanceof Block) {
                            args.add(arg);  // defer evaluation of this block
                        } else if (arg instanceof Number) {
                            args.add(arg);  // numeric constant needs no evaluation
                        } else if (arg instanceof String) {
                            args.add(arg);  // string constant needs no evaluation
                        } else if (arg instanceof ProcedureInvocation) {
                            ProcedureInvocation api = (ProcedureInvocation) arg;
                            args.add(api.profile.executeInstance(api, context));
                        } else {
                            throw new ClassCastException(breed + ":" + name);
                        }
                    }
                }
                return execute(args, context);
            }

            public Object execute(LinkedList args, Object context) {
                Attribute attr = (Attribute) args.get(0);
                Object o2 = args.get(1);
                attr.set(context, o2);
                return null;
            }
        });
        declarePrimitive(new Profile("stop", "[agt]void=void/void"){
        	public String encodeInstance(ProcedureInvocation pi, int indent){
        		StringBuffer buf = new StringBuffer();
        		buf.append("return stop()");
        		return buf.toString();
        	}
        });  // equiv to return?
        declarePrimitive(new Profile("wait", "void=void/num"));
        declarePrimitive(new Profile("while", "void=void/rpt,cmd"){
	        public String encodeInstance(ProcedureInvocation pi, int indent) {
	            StringBuffer buf = new StringBuffer();
	            Object o1 = pi.arguments.get(0);
	            Object o2 = pi.arguments.get(1);
	            buf.append("while (");
	            encodeArgument(o1,indent,buf);
	            buf.append(")");
	            encodeArgument(o2,indent,buf);
	            return buf.toString();
	        }
        });
        declarePrimitive(new Profile("with-local-randomness", "@void=void/cmd"));  // special context with new Random
        declarePrimitive(new Profile("without-interruption", "void=void/cmd"));  // only with ask-concurrent . . .
        declarePrimitive(new Profile("xor", "bool=bool/bool"));

        //World 
        declarePrimitive(new Profile("clear-all", "void=void/void"));
        declarePrimitive(new Profile("ca", "void=void/void"));
        declarePrimitive(new Profile("clear-drawing", "void=void/void"));
        declarePrimitive(new Profile("cd", "void=void/void"));
        declarePrimitive(new Profile("clear-patches", "void=void/void"));
        declarePrimitive(new Profile("cp", "void=void/void"));
        declarePrimitive(new Profile("clear-turtles", "void=void/void"));
        declarePrimitive(new Profile("ct", "void=void/void"));
        declarePrimitive(new Profile("display", "void=void/void"));
        declarePrimitive(new Profile("no-display", "void=void/void"));
        declarePrimitive(new Profile("reset-ticks", "[obs]void=void/void"));
        declarePrimitive(new Profile("tick", "[obs]num=void/void"));
        declarePrimitive(new Profile("tick-advance", "[obs]void=void/num"));
        declarePrimitive(new Profile("ticks", "[obs]num=void/void"));
        declarePrimitive(new Profile("world-width", "[obs]num=void/void"));
        declarePrimitive(new Profile("world-height", "[obs]num=void/void"));

        //Perspective 
        declarePrimitive(new Profile("follow", "[obs]void=void/turtle"));
        declarePrimitive(new Profile("follow-me", "turtles" , "[turtle]void=void/void"));
        declarePrimitive(new Profile("reset-perspective", "[obs]void=void/void"));
        declarePrimitive(new Profile("rp", "[obs]void=void/void"));
        declarePrimitive(new Profile("ride", "[obs]void=void/turtle"));
        declarePrimitive(new Profile("ride-me", "turtles" , "[turtle]void=void/void"));
        declarePrimitive(new Profile("watch", "[obs]void=void/agt"));
        declarePrimitive(new Profile("watch-me", "tandp" , "[agt]void=void/void"));

        //HubNet 
        declarePrimitive(new Profile("hubnet-broadcast", "void=void/name,value"));
        declarePrimitive(new Profile("hubnet-broadcast-view", "void=void/void"));
        declarePrimitive(new Profile("hubnet-enter-message?", "bool=void/void"));
        declarePrimitive(new Profile("hubnet-exit-message?", "bool=void/void"));
        declarePrimitive(new Profile("hubnet-fetch-message", "void=void/void"));
        declarePrimitive(new Profile("hubnet-message", "*=void/void"));
        declarePrimitive(new Profile("hubnet-message-source", "*=void/void"));
        declarePrimitive(new Profile("hubnet-message-tag", "*=void/void"));
        declarePrimitive(new Profile("hubnet-message-waiting?", "bool=void/void"));
        declarePrimitive(new Profile("hubnet-reset", "void=void/void"));
        declarePrimitive(new Profile("hubnet-send", "void=void/str,name,value!void=void/list(str),name,value"));
        declarePrimitive(new Profile("hubnet-send-view", "void=void/str"));
        declarePrimitive(new Profile("hubnet-set-client-interface", "void=void/str,list"));

        //Input/output 
        declarePrimitive(new Profile("beep", "void=void/void"));
        declarePrimitive(new Profile("clear-output", "void=void/void"));
        declarePrimitive(new Profile("date-and-time", "str=void/void"));
        declarePrimitive(new Profile("export-view", "void=void/str"));
        declarePrimitive(new Profile("export-interface", "void=void/str"));
        declarePrimitive(new Profile("export-output", "void=void/str"));
        declarePrimitive(new Profile("export-plot", "void=void/str"));
        declarePrimitive(new Profile("export-all-plots", "void=void/str"));
        declarePrimitive(new Profile("export-world", "void=void/str"));
        declarePrimitive(new Profile("import-drawing", "void=void/str"));
        declarePrimitive(new Profile("import-world", "void=void/str"));
        //TODO: check to see what the latest is on mouse motion
        declarePrimitive(new Profile("[obs]mouse-down?", "bool=void/void"));
        declarePrimitive(new Profile("[obs]mouse-inside?", "bool=void/void"));
        declarePrimitive(new Profile("[obs]mouse-patch", "patches=void/void"));
        declarePrimitive(new Profile("[obs]mouse-xcor", "num=void/void"));
        declarePrimitive(new Profile("[obs]mouse-ycor", "num=void/void"));
        declarePrimitive(new Profile("output-print", "void=void/*"));
        declarePrimitive(new Profile("output-show", "void=void/*"));
        declarePrimitive(new Profile("output-type", "void=void/*"));
        declarePrimitive(new Profile("output-write", "void=void/*"));
        declarePrimitive(new Profile("print", "void=void/*"));
        declarePrimitive(new Profile("read-from-string", "*=void/str"));
        declarePrimitive(new Profile("reset-timer", "[obs]void=void/void"));
        declarePrimitive(new Profile("set-current-directory", "void=void/str"));
        declarePrimitive(new Profile("show", "void=void/*"));
        declarePrimitive(new Profile("timer", "[obs]num=void/void"));
        declarePrimitive(new Profile("type", "void=void/void"));
        declarePrimitive(new Profile("user-input", "[obs]*=void/*"));
        declarePrimitive(new Profile("user-message", "[obs]void=void/*"));
        declarePrimitive(new Profile("user-one-of", "[obs]*=void/*,list"));
        declarePrimitive(new Profile("user-yes-or-no?", "[obs]bool=void/*"));
        declarePrimitive(new Profile("write", "void=void/*"));

        //File 
        declarePrimitive(new Profile("file-at-end?", "bool=void/void"));
        declarePrimitive(new Profile("file-close", "void=void/void"));
        declarePrimitive(new Profile("file-close-all", "void=void/void"));
        declarePrimitive(new Profile("file-delete", "void=void/str"));
        declarePrimitive(new Profile("file-exists?", "bool=void/str"));
        declarePrimitive(new Profile("file-flush", "void=void/void"));
        declarePrimitive(new Profile("file-open", "void=void/str"));
        declarePrimitive(new Profile("file-print", "void=void/*"));
        declarePrimitive(new Profile("file-read", "*=void/void"));
        declarePrimitive(new Profile("file-read-characters", "*=void/num"));
        declarePrimitive(new Profile("file-read-line", "str=void/void"));
        declarePrimitive(new Profile("file-show", "void=void/*"));
        declarePrimitive(new Profile("file-type", "void=void/*"));
        declarePrimitive(new Profile("file-write", "void=void/*"));
        declarePrimitive(new Profile("user-directory", "*=void/void"));
        declarePrimitive(new Profile("user-file", "*=void/void"));
        declarePrimitive(new Profile("user-new-file", "*=void/void"));

        //List 
        // butfirst, butlast commands added because dictionary disagrees with
        // some of the working sample code.
        declarePrimitive(new Profile("but-first", "list=void/list!str=void/str"));
        declarePrimitive(new Profile("butfirst", "list=void/list!str=void/str"));
        declarePrimitive(new Profile("bf", "list=void/list!str=void/str"));
        declarePrimitive(new Profile("but-last", "list=void/list!str=void/str"));
        declarePrimitive(new Profile("butlast", "list=void/list!str=void/str"));
        declarePrimitive(new Profile("bl", "list=void/list!str=void/str"));
        declarePrimitive(new Profile("empty?", "bool=void/list!bool=void/str"));
        declarePrimitive(new Profile("filter", "list=void/rpt,list"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                buf.append(getJavaName() + "(");// + encodeArguments(pi.arguments, indent) + ")");
                Object o = pi.arguments.get(0);
                if (o instanceof Block){
                	String tempString = ((Block)o).toString(indent + 4);
                	String resultString = "{ Q ->" + tempString.substring(1);
                	buf.append(resultString);
                }
                else {
                	System.err.println("The first argument of filter must be a command block");
                }
                buf.append(",");
                encodeArgument(pi.arguments.get(1), indent, buf);
                buf.append(")");
                return buf.toString();
            }
        	
        });
        declarePrimitive(new Profile("first", "*=void/list(*)!str=void/str"));
        declarePrimitive(new Profile("fput", "void=void/*,list"));
        declarePrimitive(new Profile("is-list?", "bool=void/*"));
        declarePrimitive(new Profile("item", "*=void/int,list!*=void/int,str"));
        declarePrimitive(new Profile("last", "*=void/list!str=void/str"));
        declarePrimitive(new Profile("length", "num=void/list!num=void/str"));
        declarePrimitive(new Profile("list", "list=void/*,*"));
        declarePrimitive(new Profile("(list", "(list=void/*"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                buf.append("list(" + encodeArguments(pi.arguments, indent) + ")");
                return buf.toString();
            }
        	
        });
        declarePrimitive(new Profile("lput", "void=void/*,list"));
        declarePrimitive(new Profile("member?", "bool=void/*,list(*)!bool=void/str,str!bool=void/agt,agtset"));
        declarePrimitive(new Profile("n-values", "list=void/num,rpt"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                buf.append(getJavaName() + "(");// + encodeArguments(pi.arguments, indent) + ")");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append(",");
                Object o = pi.arguments.get(1);
                if (o instanceof Block){
                	String tempString = ((Block)o).toString(indent + 4);
                	String resultString = "{ Q ->" + tempString.substring(1);
                	buf.append(resultString);
                }
                else {
                	System.err.println("The second argument of n-values must be a command block");
                }
                buf.append(")");
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("position", "int=void/str,str!int=void/*,list"));
        declarePrimitive(new Profile("reduce", "list=void/rpt,list"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                buf.append(getJavaName() + "(");// + encodeArguments(pi.arguments, indent) + ")");
                Object o = pi.arguments.get(0);
                if (o instanceof Block){
                	String tempString = ((Block)o).toString(indent + 4);
                	String resultString = "{ Q1, Q2 ->" + tempString.substring(1);
                	buf.append(resultString);
                }
                else {
                	System.err.println("The first argument of reduce must be a command block");
                }
                buf.append(",");
                encodeArgument(pi.arguments.get(1), indent, buf);
                buf.append(")");
                return buf.toString();
            }
        	
        });
        declarePrimitive(new Profile("remove", "list=void/*,list!str=void/str,str"));
        declarePrimitive(new Profile("remove-duplicates", "list=void/list"));
        declarePrimitive(new Profile("remove-item", "list=void/num,list!str=void/num,str"));
        declarePrimitive(new Profile("replace-item", "list=void/num,list,*!str=void/num,str,str"));
        declarePrimitive(new Profile("reverse", "list=void/list!str=void/str"));
        declarePrimitive(new Profile("sentence", "(list=void/*,*!list=void/*,*"));
        declarePrimitive(new Profile("se", "(list=void/*,*!list=void/*,*"));
        declarePrimitive(new Profile("shuffle", "list=void/list"));
        declarePrimitive(new Profile("sublist", "list=void/list,num,num"));

        //Boolean
        declarePrimitive(new Profile("true", "bool=void/void"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                if (getPreSize() > 0) {
                    encodeArgument(pi.arguments.get(0), indent, buf);
                    buf.append(" ");
                    buf.append(getJavaName());
                    for (int i = 0; i < getPostSize(); i++) {
                        buf.append(" ");
                        encodeArgument(pi.arguments.get(i + 1), indent, buf);
                    }
                } else {
                    buf.append("true");
                }
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("false", "bool=void/void"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                if (getPreSize() > 0) {
                    encodeArgument(pi.arguments.get(0), indent, buf);
                    buf.append(" ");
                    buf.append(getJavaName());
                    for (int i = 0; i < getPostSize(); i++) {
                        buf.append(" ");
                        encodeArgument(pi.arguments.get(i + 1), indent, buf);
                    }
                } else {
                    buf.append("false");
                }
                return buf.toString();
            }
        });

        //String 
        declarePrimitive(new Profile("is-string?", "bool=void/*"));
        declarePrimitive(new Profile("substring", "str=void/str,num,num"));
        declarePrimitive(new Profile("word", "str=void/*,*"));
        declarePrimitive(new Profile("(word", "(str=void/*"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                if (pi.SHOW_PI_TYPE) {
                    buf.append("/*pi[" + (pi.type == null ? "C" : "R") + "]*/   ");
                }
                buf.append("word(" + encodeArguments(pi.arguments, indent) + ")");
                return buf.toString();
            }
        	
        });

        //Mathematical 
        declarePrimitive(opProf = new Profile("+", "num=num/num"));
        opProf.setPrecedence(120);
        declarePrimitive(opProf = new Profile("*", "num=num/num"));
        opProf.setPrecedence(150);
        declarePrimitive(opProf = new Profile("-", "(num=void/num!num=num/num"));
        opProf.setPrecedence(120);
        declarePrimitive(opProf = new Profile("/", "num=num/num"));
        opProf.setPrecedence(150);
        declarePrimitive(opProf = new Profile("^", "num=num/num") {

        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append(getJavaName());
                buf.append("(");
            	Object o = pi.arguments.get(0);
                encodeArgument(o, indent, buf);
                buf.append(",");
                Object oo = pi.arguments.get(1);
                encodeArgument(oo, indent, buf);
                buf.append(")");
                return buf.toString();
            }
        	
            public String getJavaName() {
                return "Math.pow";
            }
        });
        opProf.setPrecedence(170);
        declarePrimitive(opProf = new Profile("<", "bool=num/num!bool=str/str"));
        opProf.setPrecedence(110);
        declarePrimitive(opProf = new Profile(">", "bool=num/num!bool=str/str"));
        opProf.setPrecedence(110);
        declarePrimitive(opProf = new Profile("=", "bool=num/num!bool=str/str") {

        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
            	Object o = pi.arguments.get(0);
                encodeArgument(o, indent, buf);
                buf.append(" ");
                buf.append(getJavaName());
                buf.append(" ");
                Object oo = pi.arguments.get(1);
                if (o instanceof Attribute && ((Attribute)o).toString().equals("breed")){
                	buf.append("\"");
    				if (oo instanceof ProcedureInvocation) {
    					Profile pf = ((ProcedureInvocation) oo).getProfile();
    					String st = pf.getJavaName();
    					buf.append(st);
    				}
    				buf.append("\"");
                }
                else {
                	encodeArgument(oo, indent, buf);
                }
                return buf.toString();
            }
        	
            public String getJavaName() {
                return " == ";
            }

            public Object execute(LinkedList args, Object context) {
                // context is irrelevant here
                Object o1 = args.get(0);
                Object o2 = args.get(1);
                if (o1 instanceof Number && o2 instanceof Number) {
                    return ((Number) o1).doubleValue() == ((Number) o2).doubleValue();
                } else if (o1 instanceof String && o2 instanceof String) {
                    return o1.equals(o2);
                } else {
                    return o1.toString().equals(o2.toString());
                }
            }
        });
        opProf.setPrecedence(110);
        declarePrimitive(opProf = new Profile("!=", "bool=num/num!bool=str/str"));
        opProf.setPrecedence(110);
        declarePrimitive(opProf = new Profile("<=", "bool=num/num!bool=str/str"));
        opProf.setPrecedence(110);
        declarePrimitive(opProf = new Profile(">=", "bool=num/num!bool=str/str"));
        opProf.setPrecedence(110);
        declarePrimitive(new Profile("abs", "num=void/num"));
        declarePrimitive(new Profile("acos", "num=void/num"));
        declarePrimitive(new Profile("asin", "num=void/num"));
        declarePrimitive(new Profile("atan", "num=void/num,num"));
        declarePrimitive(new Profile("ceiling", "num=void/num"));
        declarePrimitive(new Profile("cos", "num=void/num"));
        declarePrimitive(new Profile("e", "float=void/void"));
        declarePrimitive(new Profile("exp", "num=void/num"));
        declarePrimitive(new Profile("floor", "num=void/num"));
        declarePrimitive(new Profile("int", "num=void/num"){
        	public String getJavaName() {
                return "intPart";
            }
        });
        declarePrimitive(new Profile("ln", "num=void/num"));
        declarePrimitive(new Profile("log", "num=void/num,num"));
        declarePrimitive(new Profile("max", "num=void/list(num)"));
        declarePrimitive(new Profile("mean", "num=void/list(num)"));
        declarePrimitive(new Profile("median", "num=void/list(num)"));
        declarePrimitive(new Profile("min", "num=void/list(num)"));
        declarePrimitive(new Profile("mod", "num=num/num"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append("mod(");
                buf.append(encodeArguments(pi.arguments, indent));
                buf.append(")");
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("modes", "num=void/list(*)"));
        declarePrimitive(new Profile("new-seed", "num=void/void"));
        declarePrimitive(new Profile("pi", "float=void/void"));
        declarePrimitive(new Profile("precision", "num=void/num,num"));
        declarePrimitive(new Profile("random", "num=void/num"));
        declarePrimitive(new Profile("random-exponential", "num=void/num"));
        declarePrimitive(new Profile("random-float", "num=void/num"));
        declarePrimitive(new Profile("random-gamma", "num=void/num,num"));
        declarePrimitive(new Profile("random-normal", "num=void/num,num"));
        declarePrimitive(new Profile("random-poisson", "num=void/num"));
        declarePrimitive(new Profile("random-seed", "num=void/num"));
        declarePrimitive(new Profile("remainder", "num=void/num,num"));
        declarePrimitive(new Profile("round", "num=void/num"));
        declarePrimitive(new Profile("sin", "num=void/num"));
        declarePrimitive(new Profile("sqrt", "num=void/num"));
        declarePrimitive(new Profile("standard-deviation", "num=void/list(num)"));
        declarePrimitive(new Profile("sum", "num=void/list(num)"));
        declarePrimitive(new Profile("tan", "num=void/num"));
        declarePrimitive(new Profile("variance", "num=void/list(num)"));

        //Plotting 
        declarePrimitive(new Profile("autoplot?", "boolean=void/void"));
        declarePrimitive(new Profile("auto-plot-off", "void=void/void"));
        declarePrimitive(new Profile("auto-plot-on", "void=void/void"));
        declarePrimitive(new Profile("clear-all-plots", "void=void/void"));
        declarePrimitive(new Profile("clear-plot", "void=void/void"));
        declarePrimitive(new Profile("create-temporary-plot-pen", "void=void/void"));
        declarePrimitive(new Profile("histogram", "void=void/void"));
        declarePrimitive(new Profile("plot", "void=void/num"));
        declarePrimitive(new Profile("plot-name", "str=void/void"));
        declarePrimitive(new Profile("plot-pen-exists?", "bool=void/void"));
        declarePrimitive(new Profile("plot-pen-down", "void=void/void"));
        declarePrimitive(new Profile("plot-pen-reset", "void=void/void"));
        declarePrimitive(new Profile("plot-pen-up", "void=void/void"));
        declarePrimitive(new Profile("plot-x-max", "num=void/void"));
        declarePrimitive(new Profile("plot-x-min", "num=void/void"));
        declarePrimitive(new Profile("plot-y-max", "num=void/void"));
        declarePrimitive(new Profile("plot-y-min", "num=void/void"));
        declarePrimitive(new Profile("plotxy", "void=void/num,num"));
        declarePrimitive(new Profile("set-current-plot", "void=void/str"));
        declarePrimitive(new Profile("set-current-plot-pen", "void=void/str"));
        declarePrimitive(new Profile("set-histogram-num-bars", "void=void/num"));
        declarePrimitive(new Profile("set-plot-pen-color", "void=void/color"));
        declarePrimitive(new Profile("set-plot-pen-interval", "void=void/num"));
        declarePrimitive(new Profile("set-plot-pen-mode", "void=void/num"));
        declarePrimitive(new Profile("set-plot-x-range", "void=void/num,num"));
        declarePrimitive(new Profile("set-plot-y-range", "void=void/num,num"));

        //Links 
        declarePrimitive(new Profile("both-ends","links", "[link]agtset=void/void"));
        declarePrimitive(new Profile("clear-links", "void=void/void"));
        declarePrimitive(new Profile("create-link-from", "@agt(links)=void/agt,cmd!agt(links)=void/agt"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append(getJavaName() + "(");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append(")");
                if (pi.arguments.size() > 1){
                    try {
                        Block block = (Block) pi.arguments.get(1);
                        buf.append(block.toStringAsClosure(indent, null));
                    } catch (ClassCastException e) {
                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
                    }
                }
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("create-links-from", "@agtset(links)=void/agtset,cmd!agtset(links)=void/agtset"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append(getJavaName() + "(");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append(")");
                if (pi.arguments.size() > 1){
                    try {
                        Block block = (Block) pi.arguments.get(1);
                        buf.append(block.toStringAsClosure(indent, null));
                    } catch (ClassCastException e) {
                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
                    }
                }
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("create-link-to", "@agt(links)=void/agt,cmd!agt(links)=void/agt"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append(getJavaName() + "(");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append(")");
                if (pi.arguments.size() > 1){
                    try {
                        Block block = (Block) pi.arguments.get(1);
                        buf.append(block.toStringAsClosure(indent, null));
                    } catch (ClassCastException e) {
                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
                    }
                }
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("create-links-to", "@agtset(links)=void/agtset,cmd!agtset(links)=void/agtset"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append(getJavaName() + "(");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append(")");
                if (pi.arguments.size() > 1){
                    try {
                        Block block = (Block) pi.arguments.get(1);
                        buf.append(block.toStringAsClosure(indent, null));
                    } catch (ClassCastException e) {
                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
                    }
                }
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("create-link-with", "@agt(links)=void/agt,cmd!agt(links)=void/agt"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append(getJavaName() + "(");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append(")");
                if (pi.arguments.size() > 1){
                    try {
                        Block block = (Block) pi.arguments.get(1);
                        buf.append(block.toStringAsClosure(indent, null));
                    } catch (ClassCastException e) {
                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
                    }
                }
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("create-links-with", "@agtset(links)=void/agtset,cmd!agtset(links)=void/agtset"){
        	public String encodeInstance(ProcedureInvocation pi, int indent) {
                StringBuffer buf = new StringBuffer();
                buf.append(getJavaName() + "(");
                encodeArgument(pi.arguments.get(0), indent, buf);
                buf.append(")");
                if (pi.arguments.size() > 1){
                    try {
                        Block block = (Block) pi.arguments.get(1);
                        buf.append(block.toStringAsClosure(indent, null));
                    } catch (ClassCastException e) {
                        buf.append("{not a block! (" + pi.arguments.get(1).getClass().getName() + ")}");
                    }
                }
                return buf.toString();
            }
        });
        declarePrimitive(new Profile("in-link-neighbor?", "turtles" , "[agt]bool=void/agt"));
        declarePrimitive(new Profile("in-link-neighbors", "turtles" , "[agt]agtset=void/agt"));
        declarePrimitive(new Profile("in-link-from", "turtles" , "[agt]link=void/agt"));
        declarePrimitive(new Profile("is-directed-link?", "bool=void/agt"));
        declarePrimitive(new Profile("is-link?", "bool=void/agt"));
        declarePrimitive(new Profile("is-undirected-link?", "bool=void/agt"));
        declarePrimitive(new Profile("layout-circle", "[obs]void=void/agtset,num"));
        declarePrimitive(new Profile("__layout-magspring", "[obs]void=void/agtset,linkset,num,num,num,num,num,bool"));
        declarePrimitive(new Profile("layout-radial", "[obs]void=void/agtset,linkset,agt"));
        declarePrimitive(new Profile("layout-spring", "[obs]void=void/agtset,linkset,num,num,num"));
        declarePrimitive(new Profile("layout-tutte", "[obs]void=void/agtset,linkset,num"));
        declarePrimitive(new Profile("hide-link","links", "[link]void=void/void"));
        declarePrimitive(new Profile("link-neighbor?", "turtles" , "[agt]bool=void/agt"));
        declarePrimitive(new Profile("link", "link=void/agt,agt"));
        declarePrimitive(new Profile("link-neighbors", "turtles" , "[agt]agtset=void/void"));
        declarePrimitive(new Profile("link-with", "link=void/agt"));
        declarePrimitive(new Profile("my-in-links", "turtles" , "[agt]agtset(links)=void/void"));
        declarePrimitive(new Profile("my-links", "turtles" , "[agt]agtset(links)=void/void"));
        declarePrimitive(new Profile("my-out-links", "turtles" , "[agt]agtset(links)=void/void"));
        declarePrimitive(new Profile("no-links", "agtset(links)=void/void"));
        declarePrimitive(new Profile("other-end", "tandl" , "[agt]agt=void/void"));
        declarePrimitive(new Profile("out-link-neighbor?", "turtles" , "[agt]bool=void/agt"));
        declarePrimitive(new Profile("out-link-neighbors", "turtles" , "[agt]agtset=void/agt"));
        declarePrimitive(new Profile("out-link-to", "turtles" , "[agt]link=void/agt"));
        declarePrimitive(new Profile("show-link","links", "void=void/void"));
        declarePrimitive(new Profile("tie","links", "[link]void=void/void"));
        declarePrimitive(new Profile("untie","links", "[link]void=void/void"));

        //Movie 
        declarePrimitive(new Profile("movie-cancel", "[obs]void=void/void"));
        declarePrimitive(new Profile("movie-close", "[obs]void=void/void"));
        declarePrimitive(new Profile("movie-grab-view", "[obs]void=void/void"));
        declarePrimitive(new Profile("movie-grab-interface", "[obs]void=void/void"));
        declarePrimitive(new Profile("movie-set-frame-rate", "[obs]void=void/num"));
        declarePrimitive(new Profile("movie-start", "[obs]void=void/str"));
        declarePrimitive(new Profile("movie-status", "[obs]str=void/void"));

        //System dynamics
        declarePrimitive(new Profile("system-dynamics-setup", "void=void/void"));
        declarePrimitive(new Profile("system-dynamics-go", "void=void/void"));
        declarePrimitive(new Profile("system-dynamics-do-plot", "void=void/void"));

        //System 
        declarePrimitive(new Profile("netlogo-applet?", "bool=void/void"));
        declarePrimitive(new Profile("netlogo-version", "str=void/void"));
    /* */
    }

    /**
     * Special local variables are parameters passed to closures in certain
     * NetLogo commands.
     */
    protected void declareSpecialLocalVariables() {
        declareAttribute("*CLOSURE*", "?");
        declareAttribute("*CLOSURE*", "?1");
        declareAttribute("*CLOSURE*", "?2");
        declareAttribute("*CLOSURE*", "?3");
        declareAttribute("*CLOSURE*", "?4");
        declareAttribute("*CLOSURE*", "?5");
        declareAttribute("*CLOSURE*", "?6");
        declareAttribute("*CLOSURE*", "?7");
        declareAttribute("*CLOSURE*", "?8");
        declareAttribute("*CLOSURE*", "?9");
    }

    protected Attribute disambiguate(Object ref) {
        if (ref instanceof Attribute) {
            return (Attribute) ref;
        }
        if (ref instanceof LinkedList) {
            // ought to identify context in which we're acting.
            // but for now, just pick the first one, because I know that
            // for purposes of setter generation, the choice doesn't matter.
            ref = ((LinkedList) ref).getFirst();
            return disambiguate(ref);
        }
        return null;
    }
}
