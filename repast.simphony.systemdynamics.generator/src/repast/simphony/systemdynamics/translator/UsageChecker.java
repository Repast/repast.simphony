package repast.simphony.systemdynamics.translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UsageChecker {
	
	public static final String ARRAY = "array.";
	public static final String FUNCTION = "sdFunction.";
	public static final String SCALAR = "memory.";
	public static final String LOOKUP = "lookup.";
	public static final String UNKNOWN = "unknown.";
	
	/**
	 * scans equation tokens and make sure that array references are correct, variable references are correct
	 * function calls are correct, etc
	 * 
	 * For this check, need access to all equations
	 */
	
	private Map<String, Equation> equations;
	private List<Equation> usageErrors = new ArrayList<Equation>();
	
	public UsageChecker(Map<String, Equation> equations) {
		this.equations = equations;
	}
	
	public List<Equation> checkUsage() {
		
		for (Equation eqn : equations.values()) {
			for (String token : eqn.getTokens()) {
				if (!isClassified(token)) 
					continue;
				String type = getType(token);
				OperationResult or = validateReference(type, token);
			}
		}
		return usageErrors;
	}
	
	private OperationResult validateReference(String type, String token) {
		OperationResult or = new OperationResult();
		
		if (type.equals(ARRAY)) {
			or = ArrayManager.validateArrayReference(token);
		} else if (type.equals(FUNCTION)) {
			or = FunctionManager.validateFunctionRerference(token);
		} else if (type.equals(SCALAR)) {
			or = NativeDataTypeManager.validateScalarReference(token);
		} else if (type.equals(LOOKUP)) {
			or = ArrayManager.validateLookupReference(token);
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
