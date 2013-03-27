package repast.simphony.systemdynamics.translator;

/**
 * Used only for code generation of Native Data Types
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.systemdynamics.support.ArrayReference;
import repast.simphony.systemdynamics.support.NamedSubscriptManager;

/**
 * @author bragen
 *
 */
public class ArrayReferenceNative {

    private String arrayName;
    private List<String> subscripts;
    private Equation equation;
    
    
    public ArrayReferenceNative(String reference, Equation equation) {
	arrayName = extractArrayName(reference);
	subscripts = extractSubscripts(reference);
	this.equation = equation;
	
    }
    
    public ArrayReferenceNative(String reference) {
	arrayName = extractArrayName(reference);
	subscripts = extractSubscriptsNative(reference);	
    }
    
    public ArrayReferenceNative(ArrayReference ar, Equation equation) {
	arrayName = new String(ar.getArrayName());
	subscripts = new ArrayList<String> (ar.getSubscripts());
	this.equation = equation;
    }
    
    public String generateLHSHeader(String valueVariable) {
	
	StringBuffer code = new StringBuffer();
	EquationArrayReferenceStructure ears = equation.getEars();
//	code.append("// getOuter ArrayReferenceNative 50\n");
	code.append(ears.getOuterLoops());
	return code.toString();
	
//	StringBuffer code = new StringBuffer();
//	
//	// define the temporary arrays that we'll need
//	EquationArrayReferenceStructure ears = equation.getEars();
//	
//	
//	// move this code to ears.
//	// need to maintain info between ArrayRefs -> this is tied to ears within equation?
//	List<String> packedInfo = ears.getOuterArraySubscriptDimensionPacked();
//	
//	
//	Map<String, List<String>> uniqueBySubscript = new HashMap<String, List<String>>();
//	
//	// first determine if we need multiple index arrays
//	for (String packed : packedInfo) {
//	    String[] info = packed.split("###");
//	    if (!uniqueBySubscript.containsKey(info[1])) {
//		uniqueBySubscript.put(info[1], new ArrayList<String>());
//	    }
//	    List<String> bySybscript = uniqueBySubscript.get(info[1]);
//	    String indicies = ArrayManager.getIndicies(info[0], Integer.parseInt(info[2]), info[1]);
//	    if (!bySybscript.contains(indicies))
//		bySybscript.add(indicies);
//	}
//	
//	// create index arrays only when needed
//	for (String packed : packedInfo) {
//	    String[] info = packed.split("###");
//	    if (uniqueBySubscript.get(info[1]).size() > 1) {
//	    code.append("int[] indexArray_"+CodeGenerator.getLegalName(info[0]+"_"+info[1]+"_"+info[2])+"_ = new int[] {"+
//		    ArrayManager.getIndicies(info[0], Integer.parseInt(info[2]), info[1])+"};\n");
//	    }
//	}
//	
//
//	    int subNum = 0;
//	    for (String sub : subscripts) {
//		List<String> unique = uniqueBySubscript.get(sub);
//		if (unique.size() > 1) {
//		    code.append("for (int outer"+subNum+" = 0; outer"+subNum+" < "+NamedSubscriptManager.getNumIndexFor(sub)+"; outer"+subNum+"++) {\n");
//		} else {
//		    code.append("for (int outer"+subNum+" :  new int[] {"+
//		    ArrayManager.getIndicies(arrayName, subNum, sub)+"} {\n");
//		}
//		subNum++;
//	    }
//	
//	return code.toString(); 

    }
    
    public boolean hasRangeSubscript() {
	
	for (String sub : subscripts)
	    if (isRangeSubscript(sub))
		return true;
	return false;
    }
    
    public String generateLHSFooter(String valueVariable) {
	StringBuffer code = new StringBuffer();

	// Assignment
	EquationArrayReferenceStructure ears = equation.getEars();
	code.append(ears.getLHSassignment());
	code.append(" = "+valueVariable+";\n");
	
	// Logging
//	code.append("/* generateLHSFooter */\n");
	code.append("logit(");
	code.append(ears.getLHSassignmentName());
	code.append(",");
	code.append("time,");
	code.append(valueVariable+");\n");
	
	// closing braces
	for (String sub : subscripts) {
	    code.append("}\n");
	}


	return code.toString();
    }
    
    
    public String generateRHSImplementation() {
	StringBuffer code = new StringBuffer();
	// pop[race,state!]
	// generates arrayValueOf("pop",concatAsSubscript(outerSub.getSubscriptValue("race"), rangeSub.getSubscriptValue("state")))
	// generates range loops
	// generates pop[outer0][range0]
	
	EquationArrayReferenceStructure ears = equation.getEars();
	

	    code.append(InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(arrayName));
	    
		int subNum = 0;
		for (String sub : subscripts) {
		    if (isRangeSubscript(sub)) {
			code.append("["+equation.getEars().getRangeSubscriptForImplementation(arrayName, sub, subNum)+"]");
		    } else if (InformationManagers.getInstance().getNamedSubscriptManager().isNamedSubscript(sub)) { // if not a named subscript
			code.append("["+equation.getEars().getSubscriptForImplementation(arrayName, sub, subNum)+"]");
		    } else {
			// This may to be referenced through an index array even if a terminal
			// this needs to be checked first. If not then check if terminal subscript

			if (equation.getEars().requiresIndirection(arrayName, sub, subNum)) {
			    code.append("["+equation.getEars().getSubscriptForImplementation(arrayName, sub, subNum)+"]");
			} else {
			    if (InformationManagers.getInstance().getArrayManager().isTerminalSubscript(arrayName, sub, subNum)) {
				code.append("["+InformationManagers.getInstance().getArrayManager().getTerminalValue(arrayName, sub, subNum)+"]"); 
			    } else {
				code.append("\n// PROBLEM: "+arrayName+" "+sub+" "+subNum+"\n");
				code.append("["+sub.replace("!", "")+"]"); 
//				String makeError = null;
//				System.out.println("// goober "+makeError.toLowerCase());
			    }
			}
		    }
		    subNum++;
		   
		}
	
	return code.toString();
}
    
    public String generateRHSName() {
	
	
	StringBuffer code = new StringBuffer();
	// pop[race,state!]
	// generates arrayValueOf("pop",concatAsSubscript(outerSub.getSubscriptValue("race"), rangeSub.getSubscriptValue("state")))
	// generates range loops
	// generates pop[outer0][range0]
//	code.append("/* generateRHSName  */\n");
	code.append("stringConcat(");
	code.append("\"");
	    code.append(InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(arrayName));
	    
		
		for (String sub : subscripts) {
		    if (isRangeSubscript(sub)) {
			code.append("[\",intToString(range"+equation.getEars().getRangeSubscriptNumber(sub)+"),\"]");
		    } else if (InformationManagers.getInstance().getNamedSubscriptManager().isNamedSubscript(sub)) { // if not a named subscript
			code.append("[\",intToString(outer"+equation.getEars().getOuterSubscriptNumber(sub)+"),\"]");
		    } else {
			code.append("["+sub.replace("!", "")+"]");   // need a lookup to determine terminal value!
		    }
		   
		}
		code.append("\")");
	
		System.out.println("RHS Name: "+code.toString());
	return code.toString();
    }
    
    public static boolean isArrayReference(String token) {
	if (token.startsWith("array.") && token.contains("[") && token.contains("]") ||
		token.contains("[") && token.contains("]"))	
	    return true;
	else
	    return false;
    }
    
    public static boolean isRangeSubscript(String subscript) {
	if (subscript.endsWith("!"))
	    return true;
	else
	    return false;
    }
    
    private String extractArrayName(String reference) {
	return reference.split("\\[")[0].replace("array.","").replace("lookup.", "").trim();
    }
    
    private List<String> extractSubscriptsNative(String reference) {
	
	List<String> subscripts = new ArrayList<String>();
	String[] subs = reference.split("\\[");
	String sub = "";
	for (int i = 1; i < subs.length; i++) {
	    sub += "["+subs[i];
	}
	subscripts.add(sub.trim());
	return subscripts;
    }
    
    private List<String> extractSubscripts(String reference) {
	
	List<String> subscripts = new ArrayList<String>();
	for (String sub : reference.split("\\[")[1].split("]")[0].split(","))
	    subscripts.add(sub.trim());
	return subscripts;
    }
    
    public String[] getNonRangeSubscriptsAsArray() {
	List<String> al = getNonRangeSubscripts();
	return al.toArray(new String[al.size()]);
    }
    
    public List<String> getNonRangeSubscripts() {
	List<String> ranges = new ArrayList<String>();
	for (String sub : subscripts)
	    if (!sub.contains("!"))
		ranges.add(sub);
	
	return ranges;
    }
    
    public String[] getRangeSubscriptsAsArray() {
	List<String> al = getRangeSubscripts();
	return al.toArray(new String[al.size()]);
    }
    
    public List<String> getRangeSubscripts() {
	List<String> ranges = new ArrayList<String>();
	for (String sub : subscripts)
	    if (sub.contains("!"))
		ranges.add(sub);
	
	return ranges;
    }
    
    public String[] getRangeSubscriptsNamesAsArray() {
	List<String> al = getRangeSubscriptsNames();
	return al.toArray(new String[al.size()]);
    }
    
    public List<String> getRangeSubscriptsNames() {
	List<String> ranges = new ArrayList<String>();
	for (String sub : subscripts)
	    if (sub.contains("!"))
		ranges.add(sub.replace("[", "").replace("]", "").replace("!", ""));
	
	return ranges;
    }
    
    public String getSubscriptsAsMethodParameters() {
	StringBuffer sb = new StringBuffer();
	for (String sub : subscripts) {
	    if (sb.length() > 0)
		sb.append(",");
	    sb.append("\""+sub+"\"");
	}
	return sb.toString();
    }

    public String getArrayName() {
        return arrayName;
    }

    public void setArrayName(String arrayName) {
        this.arrayName = arrayName;
    }

    public List<String> getSubscripts() {
        return subscripts;
    }
    
    public String[] getSubscriptsAsArray() {
	String[] s = new String[subscripts.size()];
	int i = 0;
	for (String sub : subscripts)
	    s[i++] = sub;
        return s;
    }

    public void setSubscripts(List<String> subscripts) {
        this.subscripts = subscripts;
    }
    
//    public Set<String> expand() {
//	Set<String> al = new HashSet<String>();
//	
//	for (SubscriptCombination sc : ArrayManager.getSubscriptValueCombinations(this.getSubscriptsAsArray())) {
////	    al.add(arrayName+ "["+sc.getSubscriptValue()+"]");
//	    
//	    al.add("array."+arrayName+ "["+sc.getSubscriptValue()+"]");
//	}
//	
//	return al;
//    }
//    
//    public Set<String> expandRawSubscript() {
//	Set<String> al = new HashSet<String>();
//	
//	for (SubscriptCombination sc : ArrayManager.getSubscriptValueCombinations(this.getSubscriptsAsArray())) {
////	    al.add(arrayName+ "["+sc.getSubscriptValue()+"]");
//	    
//	    al.add("["+sc.getSubscriptValue()+"]");
//	}
//	
//	return al;
//    }
    
    public String getSubscript() {
	StringBuffer sb = new StringBuffer();
	int i = 0;
	for (String s : subscripts) {
	    if (i++ > 0)
		sb.append(",");
	    sb.append(s);
	}
	
	
	return sb.toString();
    }
}
