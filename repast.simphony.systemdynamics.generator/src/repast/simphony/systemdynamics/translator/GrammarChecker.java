package repast.simphony.systemdynamics.translator;

import java.util.List;

import repast.simphony.systemdynamics.support.MutableBoolean;
import repast.simphony.systemdynamics.support.MutableInteger;

public class GrammarChecker {
	
	public static final String ASSIGNMENT = "Assignment";
	public static final String LOOKUP_DEFINITION = "LookupDefinition";
	public static final String SUBSCRIPT_DEFINITION = "SubscriptDefinition";
	
	public static final String COMMA = ",";
	public static final String LEFT_PAREN = "(";
	public static final String RIGHT_PAREN = ")";
	public static final String VARIABLE_REFERENCE = "VariableReference";
	public static final String FUNCTION_REFERENCE = "FunctionReference";
	public static final String LOOKUP_REFERENCE = "LookupReference";
	public static final String BINARY_OPERATOR = "BinaryOperator";
	public static final String UNARY_OPERATOR = "UnaryOperator";
	
	public static final String ARRAY = "array.";
	public static final String FUNCTION = "sdFunctions.";
	public static final String SCALAR = "memory.";
	public static final String LOOKUP = "lookup.";
	
	
	
	/*
	 * Tokens are one of
	 * 
	 * variables
	 * constants
	 * operators
	 * parens
	 * argument/subscript separator
	 * square brackets
	 * function calls
	 * lookup definitions
	 * subscript definition
	 * 
	 * Statement types are of
	 * lookup definition
	 * subscript definition
	 * assignment
	 */
	
	List<String> tokens;

	public GrammarChecker(List<String> tokens) {
		this.tokens = tokens;
	}
	
	// make sure proper number of parens
	// ends with appropriate token type
	
	public OperationResult checkGrammar() {
		
		System.out.println("######################## check Grammar #############################");
		
		OperationResult or = new OperationResult();
		String type = determineEquationType(or);
		if (!or.isOk()) 
			return or;
		if (type.equals(ASSIGNMENT)) {
			return checkAssignmentGrammar();			
		} if (type.equals(LOOKUP_DEFINITION)) {
			return checkLookupDefinitionGrammar();
		} if (type.equals(SUBSCRIPT_DEFINITION)) {
			return checkSubscriptDefinitionGrammar();
		}
		
		
		or.setErrorMessage("Cannot determine equation type for grammar check");
		return or;
	}
	
	private OperationResult checkAssignmentGrammar() {
		OperationResult or = new OperationResult();
		
		
		// modes: 
		//
		// looking for binary operator
		// looking for not binary operator
		//		unary -
		//		variable
		//		function reference
		//		lookup reference
		
		// just need to verify classification of tokens
		// usage checked elsewhere
		
		
		MutableBoolean lookingForBinaryOperator = new MutableBoolean(false);
		MutableBoolean lookingForArgumentSeparator = new MutableBoolean(false);
		MutableBoolean processingFunctionInvocation = new MutableBoolean(false);
		String previousToken = "";
		MutableInteger pos = new MutableInteger();
		MutableInteger openParens = new MutableInteger();
		
		int calls = 0;
		
		while (pos.value() < tokens.size()) {			
			String token = tokens.get(pos.value());
			System.out.println("GCin: "+token+" "+lookingForBinaryOperator.value()+" pos = "+pos.value());
			grammerCheck(token, lookingForBinaryOperator, lookingForArgumentSeparator, processingFunctionInvocation, previousToken, pos, openParens, or);
			System.out.println("GCout: "+token+" "+lookingForBinaryOperator.value()+" pos = "+pos.value());
			calls++;
			if (calls > 10) {
				or.setErrorMessage("Aborting loop");
				return or;
			}
		}
		if (openParens.value() != 0) {
			or.setErrorMessage("Mismatched parens");
		}
		if (!lookingForBinaryOperator.value()) {
			or.setErrorMessage("Illegal final token in equation");
		}
		return or;
	}
	
	private void grammerCheck(String token, MutableBoolean lookingForBinaryOperator, MutableBoolean lookingForArgumentSeparator, 
			MutableBoolean processingFunctionInvocation, String previousToken, MutableInteger pos, MutableInteger openParens, OperationResult or) {
		or.clear();

		if (token.startsWith(ARRAY)) {
			System.out.println("Array "+ token);
			if (lookingForBinaryOperator.value()) {
				or.setErrorMessage("Looking for operator found "+token+" in pos "+pos.value());
				return;
			}
			if (lookingForArgumentSeparator.value()) {
				or.setErrorMessage("Looking for argument separator found "+token+" in pos "+pos.value());
				return;
			}
			previousToken = token;
			pos.add(1);
			lookingForBinaryOperator.setValue(true);
			return;
		} else if (token.startsWith(LOOKUP)) {
			System.out.println("Lookup "+ token);
			if (lookingForBinaryOperator.value()) {
				or.setErrorMessage("Looking for operator found "+token+" in pos "+pos.value());
				return;
			}
			if (lookingForArgumentSeparator.value()) {
				or.setErrorMessage("Looking for argument separator found "+token+" in pos "+pos.value());
				return;
			}
			consumeLookup(lookingForBinaryOperator, lookingForArgumentSeparator, processingFunctionInvocation, previousToken, pos, openParens, or);
			if (!or.isOk())
				return;
			pos.add(1);
			lookingForBinaryOperator.setValue(true);
			return;

		}  else if (token.startsWith(FUNCTION)) {
			System.out.println("Function "+ token);
			if (lookingForBinaryOperator.value()) {
				or.setErrorMessage("Looking for operator found "+token+" in pos "+pos.value());
				return;
			}
			if (lookingForArgumentSeparator.value()) {
				or.setErrorMessage("Looking for argument separator found "+token+" in pos "+pos.value());
				return;
			}
			consumeFunction(lookingForBinaryOperator, lookingForArgumentSeparator, processingFunctionInvocation, previousToken, pos, openParens, or);
			if (!or.isOk())
				return;
			pos.add(1);
			lookingForBinaryOperator.setValue(true);
			return;

		}  else if (token.startsWith(SCALAR)) {
			System.out.println("Scalar "+ token);
			if (lookingForBinaryOperator.value()) {
				or.setErrorMessage("Looking for operator found "+token+" in pos "+pos.value());
				return;
			}
			if (lookingForArgumentSeparator.value()) {
				or.setErrorMessage("Looking for argument separator found "+token+" in pos "+pos.value());
				return;
			}
			previousToken = token;
			pos.add(1);
			lookingForBinaryOperator.setValue(true);
			return;

		} else if (token.equals(LEFT_PAREN)) {
			System.out.println("LeftParen "+ token);
			if (lookingForBinaryOperator.value()) {
				or.setErrorMessage("Looking for operator found "+token+" in pos "+pos.value());
				return;
			}
			if (lookingForArgumentSeparator.value()) {
				or.setErrorMessage("Looking for argument separator found "+token+" in pos "+pos.value());
				return;
			}
			openParens.add(1);
			pos.add(1);
			return;

		} else if (token.equals(RIGHT_PAREN)) { // this is a grouping paren
			System.out.println("RightParen "+ token);
			if (lookingForBinaryOperator.value()) {
				
				// If this paren matches an open paren, we are OK
				
				if (openParens.value() <= 0) {
				or.setErrorMessage("Looking for operator found "+token+" in pos "+pos.value());
				return;
				}
			}
			if (lookingForArgumentSeparator.value()) {
				or.setErrorMessage("Looking for argument separator found "+token+" in pos "+pos.value());
				return;
			}
			openParens.add(-1);
			pos.add(1);
			return;
		} else if (token.equals(COMMA)) {
			System.out.println("Comma "+ token);
			if (lookingForBinaryOperator.value()) {
				or.setErrorMessage("Looking for operator found "+token+" in pos "+pos.value());
				return;
			}
		} else if (Parser.isOperator(token)) {
			if (!lookingForBinaryOperator.value()) {
				if (Parser.isBinaryOperator(token)) {
					or.setErrorMessage("not expecting binary operator but found "+token+" in pos "+pos.value());
					return;
				} else if (Parser.isUnaryOperator(token)) {
					previousToken = token;
					pos.add(1);
					lookingForBinaryOperator.setValue(false);
					return;
				}

			} else {
				previousToken = token;
				pos.add(1);
				lookingForBinaryOperator.setValue(false);
				return;
			}

		} else if (Parser.isNumber(token)) {
			System.out.println("Number "+ token);
			if (lookingForBinaryOperator.value()) {
				or.setErrorMessage("Looking for operator found "+token+" in pos "+pos.value());
				return;
			} 
			if (lookingForArgumentSeparator.value()) {
				or.setErrorMessage("Looking for argument separator found "+token+" in pos "+pos.value());
				return;
			}
			previousToken = token;
			pos.add(1);
			lookingForBinaryOperator.setValue(true);
			return;
		} else if (Parser.isQuotedString(token)) {
			System.out.println("Quoted String "+token);
			previousToken = token;
			pos.add(1);
			lookingForBinaryOperator.setValue(false);
		} else if (Parser.isLocalVariable(token)) {
			System.out.println("Local variable "+token);
			previousToken = token;
			pos.add(1);
			lookingForBinaryOperator.setValue(false);
		} else {
			System.out.println("Unknown token type"+ token);
			or.setErrorMessage("Unknown token type "+token);
		}
	}

		private void consumeFunction(MutableBoolean lookingForBinaryOperator, MutableBoolean lookingForArgumentSeparator, 
				MutableBoolean processingFunctionInvocation, String previousToken, MutableInteger pos, MutableInteger openParens, OperationResult or) {
			
			or.clear();
			processingFunctionInvocation.setValue(true);
			
			// func is func_name ( notB , notB, ..., notB)
			
			boolean done = false;
			int localOpenParen = 0;
			
			pos.add(1);
			String token = tokens.get(pos.value());
			if (!token.equals(LEFT_PAREN)) {
				or.setErrorMessage("expecting open paren for function found "+token+" in pos "+pos.value());
				return;
			}
			localOpenParen++;
			// point to first argument
			pos.add(1);
			
			while(!done) {
				token = tokens.get(pos.value());
				grammerCheck(token, lookingForBinaryOperator, lookingForArgumentSeparator, processingFunctionInvocation, previousToken, pos, openParens, or);
				if (!or.isOk())
					return;
				if (tokens.get(pos.value()).equals(RIGHT_PAREN)) {
					System.out.println("Right Paren ");
					localOpenParen--;
					// found closing paren
					if (localOpenParen == 0) {
						done = true;
						System.out.println("ends function");
					}
					pos.add(1);
				} else if (tokens.get(pos.value()).equals(COMMA)) {
					System.out.println("Right Paren ");
					// need to grab another function argument
					pos.add(1);
					lookingForArgumentSeparator.setValue(false);
					lookingForBinaryOperator.setValue(false);
				}
			}
			

		}

		private void consumeLookup(MutableBoolean lookingForBinaryOperator, MutableBoolean lookingForArgumentSeparator, 
				MutableBoolean processingFunctionInvocation, String previousToken, MutableInteger pos, MutableInteger openParens, OperationResult or) {
			
			// for now...
			consumeFunction(lookingForBinaryOperator, lookingForArgumentSeparator, processingFunctionInvocation, previousToken, pos, openParens, or);
		}

		private OperationResult checkLookupDefinitionGrammar() {

			OperationResult or = new OperationResult();

			// token stream:
			//  <thisisalookup> <(> <[> <(> <0> <,> <0> <)> <-> <(> <100> <,> <100> <)> <]> <,> <(> <12> <,> <12> <)> <,> <(> <12> <,> <12> <)> <)> 
			//       0           1   2   3   4   5   6   7   8   9    10   11   12   13  14  15  16  17   18  19   20  21  22  23   24  25   26  27 

			MutableInteger pos = new MutableInteger();
			String[] pattern = {"(", "#", ",", "#", ")"};

			String token = tokens.get(pos.value());
			if (!Parser.isWord(token)) {

				or.setErrorMessage("Invalid Lookup Name \""+token+"\" in position "+pos.value());
				return or;
			}

			pos.add(1);
			token = tokens.get(pos.value());
			if (!tokens.get(pos.value()).equals("(")) {
				or.setErrorMessage("Missing opening (");
				return or;
			}

			pos.add(1);
			token = tokens.get(pos.value());
			if (token.equals("[")) {
				pos.add(1);
				if (!patternMatch(pattern, tokens, pos, or))
					return or;
				//			pos.add(1);
				token = tokens.get(pos.value());
				if (!token.equals("-")) {

					or.setErrorMessage("Invalid range separator(1) \""+token+"\" in position "+pos.value());
					return or;
				}
				pos.add(1);
				if (!patternMatch(pattern, tokens, pos, or))
					return or;
				//			pos.add(1);
				token = tokens.get(pos.value());
				if (!token.equals("]")) {

					or.setErrorMessage("Invalid range terminator  \""+token+"\" in position "+pos.value());
					return or;
				}
				pos.add(1);
				token = tokens.get(pos.value());
				if (!token.equals(",")) {

					or.setErrorMessage("Invalid range separator(2) \""+token+"\" in position "+pos.value());;
					return or;
				}
				pos.add(1);

			}
			// all that's left should be comma-separated pairs of numbers


			while (pos.value() < tokens.size() - pattern.length) {

				if (!patternMatch(pattern, tokens, pos, or))
					return or;
				//			pos.add(1);
				token = tokens.get(pos.value());
				if (pos.value() != tokens.size()-1 && !token.equals(",")) {

					or.setErrorMessage("Invalid range separator(3) \""+token+"\" in position "+pos.value());
					return or;
				}
				// if we are end of lookup table data
				if (pos.value() != tokens.size()-1)
					pos.add(1);
			}
			//		pos.add(1);
			token = tokens.get(pos.value());
			if (!token.equals(")")) {

				or.setErrorMessage("Missing closing paren \""+token+"\" in position "+pos.value());
				return or;
			}


			return or;
		}

		private boolean patternMatch(String[] pattern, List<String> tokens, MutableInteger pos, OperationResult or) {

			System.out.println("patternMatch: pos = "+pos.value());

			for (int i = 0; i < pattern.length; i++) {
				if (pattern[i].equals("#")) {
					if (!Parser.isNumber(tokens.get(pos.value()))) {
						or.setErrorMessage("Invalid numeric value "+tokens.get(pos.value()));
						System.out.println("Invalid numeric value "+tokens.get(pos.value()));
						return false;
					}
				} else {
					if (!pattern[i].equals((tokens.get(pos.value())))) {
						or.setErrorMessage("Invalid token "+tokens.get(pos.value())+" expecting "+pattern[i]);
						System.out.println("Invalid token "+tokens.get(pos.value())+" expecting "+pattern[i]);
						return false;
					}
				}
				pos.add(1);
			}

			return true;

		}

		private OperationResult checkSubscriptDefinitionGrammar() {
			OperationResult or = new OperationResult();

			// note that some preprocessing of the subscript definition has
			// already taken place and the resulting definition is actually quite
			// simple

			String token = tokens.get(0);
			if (!Parser.isWord(token)) {

				or.setErrorMessage("Invalid LHS "+token);
			}

			token = tokens.get(1);
			if (!token.equals(":")) {

				or.setErrorMessage("Invalid operator "+token);
				return or;
			}

			for (int pos = 2; pos < tokens.size(); pos++) {
				token = tokens.get(pos);
				if (pos % 2 == 0) {
					if (!token.equals(",")) {

						or.setErrorMessage("Invalid separator "+token);
						return or;
					}
				} else {
					if (!Parser.isWord(token)) {

						or.setErrorMessage("Invalid subscript value "+token);
						return or;
					}
				}
			}


			return or;
		}

		private String determineEquationType(OperationResult or) {
			String type = "UNKNOWN";
			or.clear();

			for (String token : tokens) {
				if (token.equals(":")) {
					return SUBSCRIPT_DEFINITION;				
				} else if (token.equals("=")) {
					return ASSIGNMENT;
				} else if (token.equals("(")) {
					return LOOKUP_DEFINITION;
				}
			}


			or.setErrorMessage("Cannot determine equation type for grammar check");
			return type;

		}
}
