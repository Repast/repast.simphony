package repast.simphony.systemdynamics.translator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.systemdynamics.support.MutableInteger;

public class Macro {
    
    private String macroName; // LHS of macro definition
    private List<String> arguments = new ArrayList<String>();
    private String originalMacroEquation; // RHS of macro equation
    private List<String> expandedMacroEquation; // result of repeated substitutions
    
    
    private Map<String, String> localVariables = new HashMap<String, String>();
    
    public Macro(String macroName, List<String> arguments) {
	this.macroName = macroName;
	this.arguments.addAll(arguments);
    }
    
    public void addEquation(String equation) {
	
	String eqn = equation.split("~")[0];
	String lhs = extractLHS(eqn);
	String rhs = extractRHS(eqn);
	if (lhs.equals(macroName)) {
	    originalMacroEquation = rhs;
	} else {
	    localVariables.put(lhs, rhs);
	}
	
    }
    
    public void expand() {
	// expand originalMacroEquation into expandedMacroEquation
	List<String> tokens = Parser.tokenize(originalMacroEquation);
	while (needsLocalVariableSubstitution(tokens)) {
	    tokens = substituteLocalVariables(tokens);
	}
	tokens = substituteArguments(tokens);
	expandedMacroEquation = tokens;
	
    }
    
    public List<String> apply(List<String> tokens, int index) {
	List<String> applied = new ArrayList<String>();
	// this will perform only a single application of a macro
	// index points to the macro invocation
	String lhs = "";
	int it = 0;
	while (it < tokens.size()) {
	    String t = tokens.get(it++);
	    if (t.equals("="))
		break;
	    lhs += t;
	}
	
	MutableInteger pos = new MutableInteger(index);
	String macro = tokens.get(pos.valueAndInc());
	String paren = tokens.get(pos.valueAndInc());
	List<String> args = extractArguments(tokens, pos, arguments.size());
	// index points to macro invocation
	// pos points past closing ")"
	for (int i = 0; i < index; i++) {
	    applied.add(tokens.get(i));
	}
	applied.addAll(applyArguments(args, lhs));
	
	for (int i = pos.value(); i < tokens.size(); i++) {
	    applied.add(tokens.get(i));
	}
	
	
	return applied;
    }
    
    private List<String> applyArguments(List<String> args, String lhs) {
	List<String> applied = new ArrayList<String>();
	for (String token : expandedMacroEquation) {
	
	    if (token.startsWith("_$arg")) {
		int index = Integer.parseInt(token.replace("_$arg", "").replace("$_", ""));
		applied.addAll(Parser.tokenize(args.get(index)));
	    } else if (token.startsWith("_$macro")) {
		applied.add(lhs);
	    } else if (token.endsWith("$")) {
		applied.add(token.substring(0, token.length()-1));
	    } else {
		applied.add(token);
	    }
	}
	
	return applied;
	
    }
    
    private List<String> extractArguments(List<String> tokens, MutableInteger pos, int numArgs) {
	List<String> args = new ArrayList<String>();
	for (int i = 0; i < numArgs; i++) {
	    args.add(extractArgument(tokens, pos));
	}
	return args;
    }
    
    private String extractArgument(List<String> tokens, MutableInteger pos) {
	StringBuffer sb = new StringBuffer();

	// pos is pointing to the beginning of the argument
	// grab until we hit our "," Note that we need to worry about parens
	int parenCount = 0;
	int blockCount = 0;
	while(true) {
	   
	    String token = tokens.get(pos.valueAndInc());
	    if (token.equals("(")) {
		parenCount++;
	    } else if (token.equals(")")) {
		parenCount--;
		if (parenCount < 0)
		    break;
	    } else if (token.equals("[")) {
		blockCount++;
	    } else if (token.equals("]")) {
		blockCount--;
	    } else if (token.equals(",")) {
		if (parenCount == 0 && blockCount == 0) {
		    break;
		} else {

		}
	    } 
	    sb.append(token);
	}
	return sb.toString();
    }
    
    private String makeEquation(List<String> tokens) {
	StringBuffer sb = new StringBuffer();
	for (String token : tokens) {
	    if (token.equals(macroName)) {
		sb.append("_$macro$_");
	    } else if (token.endsWith("$")) {
		sb.append(token.substring(0, token.length()-1));
	    } else {
		sb.append(token);
	    }
	}
	return sb.toString();
    }
    
    private List<String> substituteArguments(List<String> tokens) {
	List<String> args = new ArrayList<String>();
	for (String token : tokens) {
	    if (arguments.contains(token)) {
		int pos = arguments.indexOf(token);
		args.add("_$arg"+pos+"$_");
	    } else if (token.equals(macroName)){
		args.add("_$macro$_");
	    } else {
		args.add(token);
	    }
	}
	return args;
    }
    
    private List<String> substituteLocalVariables(List<String> tokens) {
	List<String> expanded = new ArrayList<String>();
	for (String token : tokens) {
	    if (localVariables.containsKey(token)) {
		expanded.addAll(Parser.tokenize(localVariables.get(token)));
	    } else {
		expanded.add(token);
	    }
	}
	return expanded;
    }
    

    private boolean needsLocalVariableSubstitution(List<String> tokens) {
	// determine if there are any local variable references in the token list
	for (String localVariable : localVariables.keySet()) {
	    if (tokens.contains(localVariable))
		return true;
	}
	return false;
	
    }
    
    public String getExpandedMacroEquation() {
	return makeEquation(expandedMacroEquation);
    }
    

    
    private String extractLHS(String equation) {
	return equation.split("=")[0].trim();
    }
    
    private String extractRHS(String equation) {
	return equation.split("=",2)[1].trim();
    }
    

    public String getMacroName() {
        return macroName;
    }

    public void setMacroName(String macroName) {
        this.macroName = macroName;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public String getOriginalMacroEquation() {
        return originalMacroEquation;
    }

    public void setOriginalMacroEquation(String originalMacroEquation) {
        this.originalMacroEquation = originalMacroEquation;
    }

    public Map<String, String> getLocalVariables() {
        return localVariables;
    }

    public void setLocalVariables(Map<String, String> localVariables) {
        this.localVariables = localVariables;
    }

}
