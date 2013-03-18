package repast.simphony.systemdynamics.translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class UnitExpression {
    
    private boolean valid;
    private boolean simpleExpression;  // otherwise a function invocation
    private String simplifiedUnits;
    private String lhs;
    private String operator;
    private List<String> rhs;
    private String function;
    Equation equation;
    private String inconsistentMessage;
    
    boolean unitParseFatalError = false;
    
    private Stack<Object> stack = new Stack<Object>();
    
    private List<Object> rhsSimplifiedUnit = new ArrayList<Object>();
//    private List<String> rhsOperators = new ArrayList<String>();
    private SimplifiedUnit lhsSimplifiedUnit;
    
    public UnitExpression(String lhs, String operator, List<String> rhs, Equation equation) {
	simpleExpression = true;
	this.lhs = lhs;
	this.rhs = rhs;
	this.operator = operator;
	this.equation = equation;
	lhsSimplifiedUnit = new SimplifiedUnit(this.lhs);
//	lhsSimplifiedUnit.print();
	if (equation.getVensimEquation().startsWith("ident agence[agence]=agence"))
	    System.out.println("ident agence[agence]=agence");
	simplifyRHS(this.rhs);
    }
    
    public String getEquationLHS() {
	return equation.getLhs().replace("array.", "");
    }
    
    public boolean isArrayInitialization() {
	    return equation.isArrayInitialization();
	}
    
    public String getVensimEquation() {
	return equation.getVensimEquation();
    }
    
    public String getLhsUnitsString() {
	return lhsSimplifiedUnit.formUnit();
    }
    
    public String getRhsUnitsString() {

	if (rhsSimplifiedUnit.size() == 1) {
	    return ((SimplifiedUnit) rhsSimplifiedUnit.get(0)).formUnit();
	} else {
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < rhsSimplifiedUnit.size(); i++) {
//		sb.append("(");
		Object obj = rhsSimplifiedUnit.get(i);
		if (obj instanceof SimplifiedUnit) {
		    sb.append(((SimplifiedUnit) obj).formUnit()+" ");
		} else {
		    String s = (String) obj;
		    if (Parser.isFunctionInvocation(s)) {
			s = s.split("<")[0].replace("sdFunctions.", "");
		    } else if (s.startsWith("constant")) {
			s = s.split("#")[0];
		    }
		    sb.append(s+" ");
		}
//		sb.append(")");
//		if (i < rhsOperators.size())
//		    sb.append(rhsOperators.get(i));
	    }
	    return sb.toString();
	}
    }
    
    
    public String getCompleteRhsUnitsString() {
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < rhs.size(); i++) {
		String s = rhs.get(i);
		if (s == null)
		    s = "null";
		    if (Parser.isFunctionInvocation(s)) {
			s = s.split("<")[0].replace("sdFunctions.", "");
		    } else if (s.startsWith("constant")) {
			s = s.split("#")[0];
		    }
		    sb.append(s+" ");
		}
	    return sb.toString();
	
    }

    private void simplifyRHS(List<String> tokens) {
	
	int lastGood = 0;
	int endBad = 0;
	int currentToken = -1;
	
	for (String token : tokens) {
	    currentToken++;
	    
	    if (token == null) {
		if (unitParseFatalError) {
		    rhsSimplifiedUnit.add("null");
		} else {
		    stack.push("null");
		}
	    } else if (Parser.isRelationalOperator(token)) {
		if (unitParseFatalError) {
		    rhsSimplifiedUnit.add(token);
		} else {
		    stack.push(token);
		}
	    } else if (Parser.isOperator(token)) {
		if (unitParseFatalError) {
		    rhsSimplifiedUnit.add(token);
		} else {
		    stack.push(token);
		}		
	    } else if (token.equals(",")) {
		if (unitParseFatalError) {
		    rhsSimplifiedUnit.add(token);
		}  else {
		    stack.push(token);
		}
		
		continue;
	    }else if (Parser.isFunctionInvocation(token)) {
		if (unitParseFatalError) {
		    rhsSimplifiedUnit.add(token);
		    
		} else {
		    stack.push(token);
		}
	    } else if (token.equals("(")) {
		if (unitParseFatalError) {
		    rhsSimplifiedUnit.add(token);
		    
		} else {
		    lastGood = currentToken;
		    stack.push(token);
		}
	    } else if (token.equals(")")) {
		if (unitParseFatalError) {
		    rhsSimplifiedUnit.add(token);
		} else {
		    processStackExpression();
		    if (unitParseFatalError) {
			endBad = currentToken;
//			rhsSimplifiedUnit.clear();
//			for (int i = 0; i <= lastGood; i++) 
//			    rhsSimplifiedUnit.add(tokens.get(i));
//			rhsSimplifiedUnit.add("ERROR >>>");
//			for (int i = lastGood+1; i < endBad; i++) 
//			    rhsSimplifiedUnit.add(tokens.get(i)); 
//			rhsSimplifiedUnit.add("<<< ERROR");
//			for (int i = endBad; i < tokens.size(); i++) 
//			    rhsSimplifiedUnit.add(tokens.get(i));

		    } else {
			lastGood = currentToken;
		    }
		}

	    } else {
		if (unitParseFatalError) {
		    rhsSimplifiedUnit.add(new SimplifiedUnit(token));
		} else {
		    stack.push(new SimplifiedUnit(token));
		}
	    }
	    
	}
	if (!unitParseFatalError)
	    processStackExpression();

	// recover what is left on the stack. When the units are consistent, only one
	// SimplifiedUnit remains on the stack. When the units are inconsistent, one or more
	// SimplifiedUnits remain on the stack. If more than one SU, then there must be 
	// binary operators on the stack also.

	while(!stack.isEmpty()) {
	    Object obj = stack.pop();
	    if (!unitParseFatalError)
		rhsSimplifiedUnit.add(obj);

	}
	//	System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

	private void processStackExpression() {
	    Stack<Object> tStack = new Stack<Object>();
	    while (true) {
		if (stack.isEmpty()) {
//		    System.out.println("processStackExpression - stack is empty");
		    break;
		}
		Object obj = stack.pop();
		Object peek = null;
		if (!stack.isEmpty())
		    peek = stack.peek();
		if (obj instanceof String) {
		    String token = (String) obj;
		    if (Parser.isOperator(token)) {
			tStack.push(token);
		    } else if (token.equals("(")) {
			if (peek != null) {
			    if (peek instanceof String) {
				String peeker = (String) peek;
				if (Parser.isFunctionInvocation(peeker)) {
				    stack.pop();
				    tStack.push(peeker);
//				    System.out.println("processStackExpression - break 1");
				    break;
				}
			    } 
			}
//			System.out.println("processStackExpression - break 2");
			break;
			
			// this is a special case were an object that does not carry a dimension
			// is referenced. Maybe I need to just set it to dmnl?
		    } else if (token.equals("null")) {
			tStack.push(token);
		    }

		} else {
		    tStack.push(obj);
		    
		}
	    }

	    // at this point tStack contains a expression that must be validated and
	    // simplified. if valid, the simplifiedUnit is placed back onto the 
	    // stack. If invalid, the entire expression is placed back onto the stack.
	    // if it is a valid function invocation, the simplifiedUnit of the return
	    // value is placed on the stack. If invalid, the entire function invocation
	    // is placed back on to the stack.
	    
	    // tStackRestore contains the original state of tStack. If an error, restore
	    // the stack from tStackRestore.

	    SimplifiedUnit aSU = null;
	    SimplifiedUnit otherSU = null;;
	    int countSU = 0;
	    String oper = "";

//	    System.out.println("##########################");
	    while (!tStack.isEmpty()) {
		Object obj = tStack.pop();
		if (obj instanceof SimplifiedUnit) {
		    countSU++;
		    if (aSU == null)
			aSU = (SimplifiedUnit) obj;
		    else
			otherSU = (SimplifiedUnit) obj;
		    if (countSU == 2) {
			if (executeOperator(oper, aSU, otherSU)) {
			    countSU = 1;
			} else {
			    System.out.println("Cannot execute operator "+oper);
			    // need to error out
			    Stack<Object> reverse = reverseOrder(stack);
			    while (!reverse.isEmpty()) {
				rhsSimplifiedUnit.add(reverse.pop());
			    }
			    unitParseFatalError = true;
			    rhsSimplifiedUnit.add("(");
			    rhsSimplifiedUnit.add("ERROR >>>");
			    rhsSimplifiedUnit.add(aSU);
			    rhsSimplifiedUnit.add(oper);
			    rhsSimplifiedUnit.add(otherSU);
			    rhsSimplifiedUnit.add(" <<< ERROR");
			    rhsSimplifiedUnit.add(")");
			    
			    
			    return;
			}
		    }
		} else {
		    String s = (String) obj;
		    if (Parser.isFunctionInvocation(s)) {

			// We've popped a function invocation off the stack
			// args are still on the stack
			//
			// Retrieve:
			// 	function name
			//	function return units (arg pointer)  note that we may need alternates
			//

			int[] funcReturnIndicies;
			
			// FUNCTIONS THAT RETURN UNITS OF DMNL -- HOW SHOULD WE HANDLE THIS?

			String funcName = FunctionDescription.getFunctionName(s);
			String funcReturn = FunctionDescription.getFunctionReturn(s);
			
			boolean returnsDmnl = funcReturn.equals("dmnl");
			if (returnsDmnl)
			    System.out.println("RETURNS DMNL");
			
			int funcReturnIndex = 0;
			if (funcReturn.contains("#")) {
			    // we have alternate args from which to grab units
			    String[] alts = funcReturn.split("#"); 
			    funcReturnIndicies = new int[alts.length];
			    for (String alt : alts) {
				for (int i = 0; i < alts.length; i++) {
				    funcReturnIndicies[i] = Integer.parseInt(alt.replace("arg", ""))-1;
				}
			    }
			    funcReturnIndex = funcReturnIndicies[0];
			} else {
			    funcReturnIndicies = new int[1];
			    funcReturnIndex = FunctionDescription.getFunctionReturnIndex(s);
			    funcReturnIndicies[0] = funcReturnIndex;
			}
//			System.out.println("Name "+funcName);
//			System.out.println("Return "+funcReturn);

			FunctionDescription fd = FunctionManager.getDescription(funcName);

			// if our return index is 0, then we need a unit expression
			// that will be either mult or div
			boolean requiresUnitExpression = fd.requiresUnitExpression();
			boolean somethingWrong = funcReturnIndex == -2;

			if (somethingWrong) {
			    System.out.println("!!! ERROR !!! Implemented Functions Data");
			}

			// there is a complication if a constant is used as a parameter then
			// there is an implied units. We can check to see if a return pos 
			// points to a 'constant'. If so, see if we can get unit from other
			// argument

			int numArgs = fd.getNumArgs();
			String[] argUnits = fd.getArgUnits();

			// check argument units against proper
			SimplifiedUnit[] sus = new SimplifiedUnit[numArgs];
			int i = 0;
			for (String u : argUnits) {
			    Object o = tStack.pop();
			    if (o instanceof String) {
				if (((String) o).equals(","))
				    o = tStack.pop(); 
			    }
			    if (o instanceof SimplifiedUnit) {
				sus[i] = (SimplifiedUnit) o;
//				System.out.println("su[i] = "+sus[i]);
				System.out.println("ArgUnit "+i+" = "+argUnits[i]+" "+sus[i].formUnit());
				// this needs work
				if (badFunctionArgumentUnits(argUnits[i], sus[i])) {
				    Stack<Object> reverse = reverseOrder(stack);
				    while (!reverse.isEmpty()) {
					rhsSimplifiedUnit.add(reverse.pop());
				    }
				    unitParseFatalError = true;
				    rhsSimplifiedUnit.add("ERROR >>>");
				    rhsSimplifiedUnit.add(sus[i]);
				    rhsSimplifiedUnit.add(" <<< ERROR");				    
				    
				    return;
				}
				i++;
			    } else {
				System.out.println("Expected SU found "+(String) o);
				Stack<Object> reverse = reverseOrder(stack);
				    while (!reverse.isEmpty()) {
					rhsSimplifiedUnit.add(reverse.pop());
				    }
				    unitParseFatalError = true;
				    rhsSimplifiedUnit.add("ERROR >>>");
				    rhsSimplifiedUnit.add((String) o);
				    rhsSimplifiedUnit.add(" <<< ERROR");				    
				    
				    return;
			    }
			}
			
			if (returnsDmnl) {
			    aSU = new SimplifiedUnit("dmnl");
			} else if (!requiresUnitExpression) {
			    SimplifiedUnit funcRetSU = sus[funcReturnIndex];
			    if (funcRetSU == null) {
				System.out.println("WTF!");
				Stack<Object> reverse = reverseOrder(stack);
				    while (!reverse.isEmpty()) {
					rhsSimplifiedUnit.add(reverse.pop());
				    }
				    unitParseFatalError = true;
				    rhsSimplifiedUnit.add("ERROR >>>");
				    rhsSimplifiedUnit.add(sus[funcReturnIndex]);
				    rhsSimplifiedUnit.add(" <<< ERROR");				    
				    
				    return;
			    }
			    if (funcRetSU.isConstant()) {
				for (int j : funcReturnIndicies) {
				    if (j != funcReturnIndex && !sus[j].isConstant()) {
					sus[funcReturnIndex] = new SimplifiedUnit(sus[j]);
					break;
				    }
				}
			    }


			    aSU = sus[funcReturnIndex];
			} else {
			    // make sure that we generate units that aren't constant
			    aSU = generateReturnUnits(funcReturn, sus);
			    
			    
			}
		    } else {
			oper = s;
		    }
		}
	    }
	    if (aSU == null)
		aSU = new SimplifiedUnit("null");
	    aSU.reduce();
	    stack.push(aSU);
//	    System.out.println("##########################");

	}
	
	private boolean badFunctionArgumentUnits(String correctUnits, SimplifiedUnit specifiedUnit) {
	    boolean bad = false;
	    if (!bad)
		return bad;
	    
	    // take care of specific units
	    if (specifiedUnit.isAny())
		return false;
	    
	    if (correctUnits.equals("units")) {
		if (lhsSimplifiedUnit.formUnit().equals(specifiedUnit.formUnit()))
		    return false;
		else
		    return true;
	    }
	    
	    
	    return bad;
	    
	}
	
	private Stack<Object> reverseOrder(Stack<Object> stack) {
	    Stack<Object> reverse = new Stack<Object>();
	    while (!stack.isEmpty()) {
		reverse.push(stack.pop());
	    }
	    
	    return reverse;
	}
	
	private SimplifiedUnit generateReturnUnits(String funcReturn, SimplifiedUnit[] sus) {
	    
	    String[] units;
	    
	    // special cases
	    if (funcReturn.equals("NA"))
		return new SimplifiedUnit("any");
	    if (funcReturn.equals("dmnl"))
		return new SimplifiedUnit("dmnl");
	    boolean division = funcReturn.contains("/");
	    boolean multiplication = !division;
	    
	    String operator = division ? "/" : "\\*";
	    
	    units = funcReturn.split(operator);
	    
	    int sub = Integer.parseInt(units[0].replace("arg", ""))-1;
	    
	    SimplifiedUnit su = new SimplifiedUnit(sus[sub]);
	    
	    sub = Integer.parseInt(units[1].replace("arg", ""))-1;
	    
	    if (division)
		su.dividedBy(sus[sub]);
	    else
		su.times(sus[sub]);
	    
	    return su;
	    
	}

	private boolean executeOperator(String oper, SimplifiedUnit aSU, SimplifiedUnit otherSU) {

	    if (aSU.validForOperation(oper, otherSU)) {
		if (oper.equals("+")) {
		    aSU.plus(otherSU);
		} else if (oper.equals("^")) {
		    aSU.exp(otherSU);
		} else if (oper.equals("-")) {
		    aSU.minus(otherSU);
		} else if (oper.equals("*")) {
		    aSU.times(otherSU);
		} else if (oper.equals("/")) {
		    aSU.dividedBy(otherSU);
		} else if (oper.equals("==")) {
		    aSU.equals(otherSU);
		} else if (Parser.isBooleanOperator(oper)) {
		    aSU.equals(otherSU);
		}
		aSU.reduce();
		return true;
	    } else {
		return false;
	    }

	}
	
	public void printUnitExpression() {
	    System.out.println("UNIT EXPRESSION");
	    System.out.println(getLhsUnitsString());
	    System.out.println(operator);
	    System.out.println(getRhsUnitsString());
	    
	}

	public void validate() {
//	    System.out.println("### VALIDATE ###");
	    
	    lhsSimplifiedUnit.reduce();
	    for (Object su : rhsSimplifiedUnit) {
		if (su instanceof SimplifiedUnit)
		((SimplifiedUnit) su).reduce();
	    }
	    
	    
	    printUnitExpression();
	    if (equation.isArrayInitialization()) {
		// is this really true?
		valid = true;
		return;
	    }
	    if (rhsSimplifiedUnit.size() == 1) {
		if (((SimplifiedUnit) rhsSimplifiedUnit.get(0)).hasSameUnits(lhsSimplifiedUnit)) {
		    	valid = true;
		} else {
		    	valid = false;
		    	UnitsManager.getUnitConsistencyXMLWriter().addInconsistent(this);
		    	inconsistentMessage = "1 RHS SU hasSameUnits false";
		}
	    } else {
		valid = false;
		UnitsManager.getUnitConsistencyXMLWriter().addInconsistent(this);
		inconsistentMessage = "!= 1 RHS SU "+rhsSimplifiedUnit.size();
	    }
	}

	public boolean isValid() {
	    validate();
	    return valid;
	}

	public void setValid(boolean valid) {
	    this.valid = valid;
	}

	public boolean isSimpleExpression() {
	    return simpleExpression;
	}

	public void setSimpleExpression(boolean simpleExpression) {
	    this.simpleExpression = simpleExpression;
	}

	public String getSimplifiedUnits() {
	    return simplifiedUnits;
	}

	public void setSimplifiedUnits(String simplifiedUnits) {
	    this.simplifiedUnits = simplifiedUnits;
	}

	public String getLhs() {
	    return lhs;
	}

	public void setLhs(String lhs) {
	    this.lhs = lhs;
	}


	public void setOperator(String operator) {
	    this.operator = operator;
	}


	public String getFunction() {
	    return function;
	}

	public void setFunction(String function) {
	    this.function = function;
	}

	public List<String> getRhs() {
	    return rhs;
	}

	public void setRhs(List<String> rhs) {
	    this.rhs = rhs;
	}

//	public List<SimplifiedUnit> getRhsSimplifiedUnit() {
//	    return rhsSimplifiedUnit;
//	}
//
//	public void setRhsSimplifiedUnit(List<SimplifiedUnit> rhsSimplifiedUnit) {
//	    this.rhsSimplifiedUnit = rhsSimplifiedUnit;
//	}

	public SimplifiedUnit getLhsSimplifiedUnit() {
	    return lhsSimplifiedUnit;
	}

	public void setLhsSimplifiedUnit(SimplifiedUnit lhsSimplifiedUnit) {
	    this.lhsSimplifiedUnit = lhsSimplifiedUnit;
	}

	public String getOperator() {
	    return operator;
	}

	public String getInconsistentMessage() {
	    return inconsistentMessage;
	}

	public void setInconsistentMessage(String inconsistentMessage) {
	    this.inconsistentMessage = inconsistentMessage;
	}

	public boolean isUnitParseFatalError() {
	    return unitParseFatalError;
	}

	public void setUnitParseFatalError(boolean unitParseFatalError) {
	    this.unitParseFatalError = unitParseFatalError;
	}
}
