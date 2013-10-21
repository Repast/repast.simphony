/**
 * 
 */
package repast.simphony.systemdynamics.support;


//import gov.anl.rssd.translator.NativeDataTypeManager;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.systemdynamics.translator.InformationManagers;

/**
 * @author bragen
 *
 */
public class ArrayReference {

	private String arrayName;
	private List<String> subscripts;


	public ArrayReference(String reference) {
		arrayName = extractArrayName(reference);
		subscripts = extractSubscripts(reference);
	}

	public String getVensimReference() {
		StringBuffer sb = new StringBuffer();

		sb.append(arrayName);
		sb.append("[");
		int i = 0;
		for (String s : subscripts) {
			if (i++ > 0)
				sb.append(",");
			sb.append(s);
		}
		sb.append("]");
		return sb.toString();
	}

	public String generateLHSHeader(String valueVariable) {

		StringBuffer code = new StringBuffer();
		//	if (!Translator.useNativeDataTypes) {
		// X[ns1,ns2, external]
		// generates
		//for (SubscriptCombination outerSub : getSubscriptValueCombinations("ns1", "ns2", "external")) {
		code.append("for (SubscriptCombination outerSub : getSubscriptValueCombinations(");
		int s = 0;
		for (String sub : subscripts) {
			if (s++ > 0)
				code.append(",");
			code.append("\""+sub+"\"");
			//	    }
		}
		code.append(")) {\n");

		//	} else {
		//	   
		//	    int subNum = 0;
		//	    for (String sub : subscripts) {
		//		code.append("for (int outer"+subNum+" = 0; outer"+subNum+" < "+NamedSubscriptManager.getNumIndexFor(sub)+"; outer"+subNum+"++) {\n");
		//		subNum++;
		//	    }
		//	}
		return code.toString(); 

	}

	public boolean hasRangeSubscript() {

		for (String sub : subscripts)
			if (isRangeSubscript(sub))
				return true;
		return false;
	}

	public String generateLHSFooter(String valueVariable) {
		StringBuffer code = new StringBuffer();

		//	if (!Translator.useNativeDataTypes) {
		code.append("arraySetValue(\""+arrayName+"\", outerSub.getSubscriptValue(), "+valueVariable+");\n");
		code.append("}\n");
		//		} else {
		//		    code .append(NativeDataTypeManager.getLegalName(arrayName));
		//		 
		//		    for (int subNum = 0; subNum < subscripts.size(); subNum++) {
		//			code.append("[outer"+subNum+"]");
		//		    }
		//		    code.append(" = "+valueVariable+";\n");
		//		    for (String sub : subscripts) {
		//			code.append("}\n");
		//		    }
		//		}

		return code.toString();
	}

	public String generateRHSImplementation() {
		StringBuffer code = new StringBuffer();
		// represents pop[race,state!]
		// generates arrayValueOf("pop",concatAsSubscript(outerSub.getSubscriptValue("race"), rangeSub.getSubscriptValue("state")))
		//	if (!Translator.useNativeDataTypes) {
		code.append("arrayValueOf(\""+arrayName+"\",concatAsSubscript(");
		int subNum = 0;
		for (String sub : subscripts) {
			if (subNum++ > 0)
				code.append(",");
			if (isRangeSubscript(sub)) {
				code.append("rangeSub.getSubscriptValue(\""+sub.replace("!", "")+"\")");
			} else if (InformationManagers.getInstance().getNamedSubscriptManager().isNamedSubscript(sub)) { // if not a named subscript
				code.append("outerSub.getSubscriptValue(\""+sub.replace("!", "")+"\")");
			} else {
				code.append("\""+sub.replace("!", "")+"\"");
			}

		}

		code.append("))");
		//	} else {
		//	    code.append(NativeDataTypeManager.getLegalName(arrayName));
		//		int subNum = 0;
		//		for (String sub : subscripts) {
		//		    if (isRangeSubscript(sub)) {
		//			code.append("rangeSub.getSubscriptValue(\""+sub.replace("!", "")+"\")");
		//		    } else if (NamedSubscriptManager.isNamedSubscript(sub)) { // if not a named subscript
		//			code.append("outerSub.getSubscriptValue(\""+sub.replace("!", "")+"\")");
		//		    } else {
		//			code.append("\""+sub.replace("!", "")+"\"");
		//		    }
		//		   
		//		}
		//		
		//		code.append("))");
		//	}
		return code.toString();
	}

	public String generateRHSName() {
		StringBuffer code = new StringBuffer();
		// represents pop[race,state!]
		// generates arrayValueOf("pop",concatAsSubscript(outerSub.getSubscriptValue("race"), rangeSub.getSubscriptValue("state")))

		//	code.append("\""+arrayName+"[\"+concatAsSubscript{AR.generateRHSName}(");
		code.append("\""+arrayName+"[\"+concatAsSubscript(");
		int subNum = 0;
		for (String sub : subscripts) {
			if (subNum++ > 0)
				code.append(",");
			if (isRangeSubscript(sub)) {
				code.append("rangeSub.getSubscriptValue(\""+sub.replace("!", "")+"\")");
			} else if (InformationManagers.getInstance().getNamedSubscriptManager().isNamedSubscript(sub)) { // if not a named subscript
				code.append("outerSub.getSubscriptValue(\""+sub.replace("!", "")+"\")");
			} else {
				code.append("\""+sub.replace("!", "")+"\"");
			}

		}

		code.append("+\"]\")");
		return code.toString();
	}

	public static boolean isArrayReference(String token) {
		// treat this as a literal, what about actual
		// variables have " in them?
		if ((token.startsWith("\"array.") && token.endsWith("\"")) || (token.startsWith("stringConcat(\"memory.") && token.endsWith("\")")))
			return false;
		if ((token.startsWith("array.") || token.startsWith("lookup.")  ) && token.contains("[") && token.contains("]") ||
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
		return reference.split("\\[")[0].replace("array.","").replace("lookup.","").trim();
	}

	private List<String> extractSubscripts(String reference) {
		
		String[] verify = reference.split("\\[");
		if (verify.length < 2) {
			System.out.println("ArrayReference: bad extractSubscripts: "+reference);
		}

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

	public List<String> getRangeSubscriptsWithDimension() {
		List<String> ranges = new ArrayList<String>();
		int dim = 0;
		for (String sub : subscripts)
			if (sub.contains("!"))
				ranges.add(sub.replace("!","")+"###"+dim);
		dim++;

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

	public List<String> getUniqueRangeSubscriptsNames() {
		List<String> ranges = new ArrayList<String>();
		for (String sub : subscripts)
			if (sub.contains("!")) {
				String newSub = sub.replace("[", "").replace("]", "").replace("!", "");
				if (!ranges.contains(newSub))
					ranges.add(newSub);
			}

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
