/**
 * 
 */
package repast.simphony.systemdynamics.translator;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.systemdynamics.support.MutableInteger;

/**
 * @author bragen
 *
 */
public class MacroManager {
    
    private static Map<String, Macro> macros = new HashMap<String, Macro>();
    
    public static void createNewMacro(String macroName, Macro macro) {
	macros.put(macroName, macro);
    }
    
    public static boolean isMacroName(String macroName) {
	return macros.containsKey(macroName);
    }
    
    public static String expand(String vensimEquation) {
	
	StringBuffer sb = new StringBuffer();
	String[] parts = vensimEquation.split("~", 2);
	
	List<String> tokens = Parser.tokenize(parts[0]);
	while (containsMacroInvocation(tokens)) {
	    tokens = applyMacro(tokens);
	}
	
	sb.append(makeEquation(tokens));
	sb.append("~");
	sb.append(parts[1]);
	return sb.toString();
	
    }
    
    private static List<String> applyMacro(List<String> tokens) {
	List<String> applied = new ArrayList<String>();
	int index = 0;
	// find the macro
	for (String token : tokens) {
	    if (isMacroName(token)) {
		Macro macro = macros.get(token);
		applied = macro.apply(tokens, index);
		break;
	    } else {
		index++;
	    }
	}
	
	
	return applied;
    }
    
    private static String makeEquation(List<String> tokens) {
	StringBuffer eqn = new StringBuffer();
	for (String token : tokens) {
	    eqn.append(token);
	}
	return eqn.toString();
    }
    
    public static boolean containsMacroInvocation(List<String> tokens) {
	int index = 0;
	for (String token : tokens) {
	    if (macros.containsKey(token)) {
		// is this an invocation or is it a reference to LHS?
		if (index+1 < tokens.size() && tokens.get(index+1).equals("("))
		    return true;
	    }
	    index++;
	}
	return false;
    }
    
    public static boolean containsMacroInvocation(String vensimEquation) {
	
//	System.out.println("cMI "+vensimEquation);
	
	String eqn = vensimEquation.split("~")[0];
	if (!eqn.contains("="))
	    return false;
	List<String> tokens = Parser.tokenize(Parser.extractRHS(eqn));
	for (String token : tokens) {
	    if (macros.containsKey(token))
		return true;
	}
	return false;
    }
    
    public static boolean readMacroAndProcess(String eqn, List<String> rawEquations, MutableInteger linePtr) {
	
	String macroName;
	List<String> arguments = new ArrayList<String>();
	String equation = new String(eqn).replace(":MACRO:", "").trim();
	macroName = equation.split("\\(")[0].trim();
	String[] argString = equation.split("\\(")[1].replace(")", "").trim().split(",");
	for (String anArg : argString)
	    arguments.add(anArg.trim());
	
	
	Macro aMacro = new Macro(macroName, arguments);
	createNewMacro(macroName, aMacro);
	
	String aLine = rawEquations.get(linePtr.valueAndInc());

	    while (!aLine.contains(":END OF MACRO:")) {
		String concat = "";
		// need to grab as many lines as necessary to get entire equation statement
		    // if the first line of the equation is continued onto additional lines
		    // we want to capture all lines and concatenate as a single line
		    if (aLine.endsWith("\\")) {
			concat = aLine.replace("\\", "");
			 aLine = rawEquations.get(linePtr.valueAndInc());
			while (aLine.endsWith("\\")) {
			    concat += aLine.replace("\\", "");
			    aLine = rawEquations.get(linePtr.valueAndInc());
			    
			}
//			aLine = concat;
		    } 
		    // now read until final "|"
		    
		    while(true) {
			if (aLine.endsWith("|")) { // used to be \t| the trim() on readLine() has thrown this off
			    concat += aLine;
			    break;
			} else {
			    concat += aLine;
			    aLine = rawEquations.get(linePtr.valueAndInc());
			}
		    }
		    
		    
		aMacro.addEquation(concat);
		
		aLine = rawEquations.get(linePtr.valueAndInc());
		while (aLine.length() == 0)
		    aLine = rawEquations.get(linePtr.valueAndInc());
	    }
	    
	    aMacro.expand();
	    System.out.println(aMacro.getExpandedMacroEquation());
	
	return true;
    }
    
    public static int getNumArgumentsFor(String macroName) {
	return macros.get(macroName).getArguments().size();
	
    }
    
    public static void printMacros() {
	for (String macroName : macros.keySet()) {
	    Macro macro = macros.get(macroName);
	    System.out.println("Macro Name: "+macroName);
	    System.out.println(macro.getExpandedMacroEquation());
	}
    }

}
