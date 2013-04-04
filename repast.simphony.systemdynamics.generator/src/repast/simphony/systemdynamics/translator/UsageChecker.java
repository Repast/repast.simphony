package repast.simphony.systemdynamics.translator;

import java.util.Map;

import repast.simphony.systemdynamics.support.MutableBoolean;
import repast.simphony.systemdynamics.support.MutableInteger;

public class UsageChecker {
	
	public static final String ARRAY = "array.";
	public static final String FUNCTION = "sdFunctions.";
	public static final String SCALAR = "memory.";
	public static final String LOOKUP = "lookup.";
	public static final String UNKNOWN = "unknown.";
	
	/**
	 * scans equation tokens and make sure that array references are correct, variable references are correct
	 * function calls are correct, etc
	 * 
	 * For this check, need access to all equations
	 */
	
	private Equation equation;
	private Map<String, Equation> equations;
	
	public UsageChecker(Map<String, Equation> equations, Equation equation) {
		this.equations = equations;
		this.equation = equation;
	}
	
	public OperationResult checkUsage() {
		OperationResult allResults = new OperationResult();
		
		MutableBoolean lhs = new MutableBoolean(true);
		MutableInteger pos = new MutableInteger(-1);
		
			for (String token : equation.getTokens()) {
				pos.add(1);
				if (token.equals("="));
					lhs.setValue(false);
				if (!isClassified(token)) 
					continue;
				String type = getType(token);
				OperationResult or = validateReference(pos, type, token, lhs);
				if (!or.isOk()) {
					allResults.setErrorMessage(or.getMessage());
				}
			}
		
		return allResults;
	}
	
	private OperationResult validateReference(MutableInteger pos, String type, String token, MutableBoolean lhs) {
		OperationResult or = new OperationResult();
		
		if (type.equals(ARRAY)) {
			or = InformationManagers.getInstance().getArrayManager().validateArrayReference(equations, equation, pos, lhs);
		} else if (type.equals(FUNCTION)) {
			or = InformationManagers.getInstance().getFunctionManager().validateFunctionReference(equations, equation, pos, lhs);
		} else if (type.equals(SCALAR)) {
			or = InformationManagers.getInstance().getNativeDataTypeManager().validateScalarReference(equations, equation, pos, lhs);
		} else if (type.equals(LOOKUP)) {
			or = InformationManagers.getInstance().getArrayManager().validateLookupReference(equations, equation, pos, lhs);
		} 
		
		
		return or;
		
	}
	
	private boolean isClassified(String token) {
		return
				token.startsWith(FUNCTION) ||
				token.startsWith(ARRAY) ||
				token.startsWith(SCALAR) ||
				token.startsWith(LOOKUP);
				
	}
	
	private String getType(String token) {
		if (token.startsWith(FUNCTION))
			return FUNCTION;
		if (token.startsWith(ARRAY))
			return ARRAY;
		if (token.startsWith(SCALAR))
			return SCALAR;
		if (token.startsWith(LOOKUP))
			return LOOKUP;
		return UNKNOWN;
		
	}

}
