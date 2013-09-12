package repast.simphony.systemdynamics.translator;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import repast.simphony.systemdynamics.support.ArrayReference;
import repast.simphony.systemdynamics.support.MappedSubscriptManager;
import repast.simphony.systemdynamics.support.MutableInteger;
import repast.simphony.systemdynamics.support.NamedSubscriptManager;

public class Parser {

    private static char[] terminators = { '+', '-', '*', '/', '(', ')', '=', ',', '^', '`', '>', '<', ':', ';'};
    private static HashSet<Character> terminatorSet = new HashSet<Character>();
    
    public static final String INTERNAL_UNARY_MINUS = "_";

    static {

    	for (char c : terminators) {
    		terminatorSet.add(c);
    	}
    }
    
    public static String translateUnaryOperator(String u) {
    	if (u.equals(INTERNAL_UNARY_MINUS))
    		return "-";
    	else
    		return u;
    }

    public static boolean isOperator(String token) {
	return isArithmeticOperator(token) ||
	isLogicalOperator(token) || isRelationalOperator(token) ||
	token.equals("=");
    }

    public static boolean isArithmeticOperator(String token) {
	if (token.equals("+") ||
		token.equals("-") ||
		token.equals("*") ||
		token.equals("/") ||
		token.equals("_") ||
		token.equals("^") 
	)
	    return true;
	else
	    return false;
    }

    public static boolean isBinaryOperator(String token) {
	if (!token.equals("_") && !token.equals("!") && !token.equals(":NOT:"))
	    return true;
	else
	    return false;
    }
    
    public static boolean isEqualSign(String token) {
    	return token.equals("=");
        }

    public static boolean isUnaryOperator(String token) {
	return ! isBinaryOperator(token);
    }

    public static boolean isBooleanOperator(String token) {
	return isLogicalOperator(token) || isRelationalOperator(token);
    }
    public static boolean isLogicalOperator(String token) {
	if (
		token.equals(":OR:") ||
		token.equals(":NOT:") ||
		token.equals(":AND:") ||
		token.equals("||") ||
		token.equals("!") ||
		token.equals("&&")
	)
	    return true;
	else
	    return false;
    }
    public static boolean isRelationalOperator(String token) {
	if (token.equals("==") ||
		token.equals("<>") ||
		token.equals(">") ||
		token.equals(">=") ||
		token.equals("<") ||
		token.equals("<=")
	)
	    return true;
	else
	    return false;
    }

    public static boolean isFunctionInvocation(String token) {
	if (token == null)
	    return false;
	if (token.startsWith("sdFunction"))
	    return true;
	else
	    return false;
    }

    public static boolean isFunctionArgumentSeparator(String token) {
	if (token.equals(","))
	    return true;
	else
	    return false;
    }

    public static boolean isLeftParen(String token) {
	if (token.equals("("))
	    return true;
	else
	    return false;
    }

    public static boolean isRightParen(String token) {
	if (token.equals(")"))
	    return true;
	else
	    return false;
    }

    public static boolean isInteger( String input )
    {
	try
	{
	    Integer.parseInt( input );
	    return true;
	}
	catch( Exception e)
	{
	    return false;
	}
    }

    public static boolean isReal( String input )
    {
	if (input.equals(":NA:"))
	    return true;
	try
	{
	    Double.parseDouble( input );
	    return true;
	}
	catch( Exception e)
	{
	    return false;
	}
    }

    public static boolean isNumber(String token) {
	if (isInteger(token) || isReal(token))
	    return true;
	else
	    return false;
    }
    
    public static boolean isWord(String expression) {
    	
    	MutableInteger ptr = new MutableInteger(0);

    	while (inRange(expression, ptr)) {
    		String aChar = characterAt(expression, ptr);
    		

    		if (!Character.isLetterOrDigit(aChar.charAt(0)) &&  !Character.isWhitespace(aChar.charAt(0)) &&
    				!aChar.equals("_")) {
    			return false;
    		}
    		
    		ptr.add(1);

    	}

    
    	return true;
    }
    
    public static boolean isQuotedString(String s) {
    	return (s.startsWith("\"") && s.endsWith("\"")) ||
    			(s.startsWith("'") && s.endsWith("'"));
    }
    
    public static boolean isLocalVariable(String s) {	
    	return s.equalsIgnoreCase("time") || s.equalsIgnoreCase("timestep");
    }


    public static String forceDouble(String expression) {
    	List<String> tokens = new ArrayList<String>();
    	MutableInteger ptr = new MutableInteger(0);

    	// Must not change quoted strings

    	boolean inQuote = false;

    	if (expression.contains("VDMLOOKUP"))
    		return expression;

    	while (inRange(expression, ptr)) {
    		String aChar = characterAt(expression, ptr);
    		if (aChar.equals("\""))
    			inQuote = !inQuote;
    		if (!inQuote && startMemoryReference(expression, ptr)) {
    			String memoryRef = getMemoryReferenceStartingAt(expression, ptr);
    			tokens.add(memoryRef);
    		} else {

    		if (!inQuote && (Character.isDigit(aChar.charAt(0)) || aChar.equals("."))) {
    			String number = getNumberStartingAt(expression, ptr);
    			if (!number.contains("."))
    				number += ".0";
    			tokens.add(number);
    		} else {
    			tokens.add(aChar);
    			ptr.add(1);
    		}
    		}

    	}

    	String forced = "";
    	for (String s : tokens) {
    		forced += s;
    	}

    	return forced;
    }
    
    private static boolean startMemoryReference(String expression, MutableInteger ptr) {
    	if (characterAt(ptr.value(), expression).equals("m") &&
    			characterAt(ptr.value()+1, expression).equals("e") &&
    			characterAt(ptr.value()+2, expression).equals("m") &&
    			characterAt(ptr.value()+3, expression).equals("o") &&
    			characterAt(ptr.value()+4, expression).equals("r") &&
    			characterAt(ptr.value()+5, expression).equals("y") &&
    			characterAt(ptr.value()+6, expression).equals(".") )
    		return true;
    	else
    		return false;
    }
    
    private static String getMemoryReferenceStartingAt(String equation, MutableInteger position) {
    	String memoryReference = "";

    	// gather characters until the , and return trim()
    	while (inRange(equation, position) && !characterAt(equation, position).equals(",")) {
    	    memoryReference += characterAt(equation, position);
    	    position.add(1);
    	}

    	return memoryReference;
        }

    public static String characterAt(String equation, MutableInteger position) {
	return equation.substring(position.value(), position.value()+1);
    }

    private static String getNumberStartingAt(String equation, MutableInteger position) {
	String number = "";

	// unary minus?

	if (characterAt(equation, position).equals("-")) {
	    number="-";
	    position.add(1);
	}
	while (inRange(equation, position) && (Character.isDigit(characterAt(equation, position).charAt(0)) ||
		characterAt(equation, position).equals("."))) {
	    number += characterAt(equation, position);
	    position.add(1);
	}

	return number;
    }

    public static boolean inRange(String equation, MutableInteger position) {
	if (position.value() <= equation.length()-1)
	    return true;
	else
	    return false;
    }

    public static  int getRowFromCellAddress(String cellAddress) {
	int row = 1;
	String rc = cellAddress;
	if (cellAddress.contains("!")) {
	    rc = cellAddress.split("!")[1];
	}
	int rowPos = getFirstNumberPosition(rc);
	row = Integer.parseInt(rc.substring(rowPos));
	return row;
    }

    public static  String getColumnLettersFromCellAddress(String cellAddress) {
	int column =  1;
	String rc = cellAddress;
	if (cellAddress.contains("!")) {
	    rc = cellAddress.split("!")[1];
	}
	int rowPos = getFirstNumberPosition(rc);
	return rc.substring(0, rowPos);
    }

    public static  int getColumnFromCellAddress(String cellAddress) {
	int column =  1;
	String rc = cellAddress;
	if (cellAddress.contains("!")) {
	    rc = cellAddress.split("!")[1];
	}
	int rowPos = getFirstNumberPosition(rc);
	column = convertColumnToNumber(rc.substring(0, rowPos));

	return column;
    }

    public static  int convertColumnToNumber(String column) {
	int num = 0;
	int pos = 0;

	char[] chars = column.toCharArray();
	for (int i = chars.length-1; i >=0; i--) {
	    num += (Character.getNumericValue(chars[i]) - 9) * Math.pow(26, pos++);
	}
	return num;
    }

    private static int getFirstNumberPosition(String address) {
	MutableInteger pos = new MutableInteger(0);
	while (inRange(address, pos) && Character.isLetter(characterAt(address, pos).toCharArray()[0]))
	    pos.add(1);
	return pos.value();
    }

    public static boolean containsNonquotedColon(String line) {
	boolean colon = false;
	boolean inQuote = false;
	MutableInteger position = new MutableInteger(0);
	while (inRange(line, position)) {
	    if (characterAt(line, position).equals("\"")) 
		inQuote = !inQuote;
	    if (characterAt(line, position).equals(":") && !colon) 
		colon = !inQuote;
	    position.add(1);
	}

	return colon;
    }


    public static String getStringStartingAt(MutableInteger position, String equation) {
	String string = "";

	if (inRange(position, equation) && characterAt(position, equation).equals("\"")) {
	    return getQuotedStringStartingAt(position, equation);
	} else {
	    while (inRange(position, equation) && (Character.isLetter(characterAt(position, equation).charAt(0)) ||
		    Character.isDigit(characterAt(position, equation).charAt(0)) || 
		    characterAt(position, equation).equals(" ") || 
		    characterAt(position, equation).equals("\"") ||
		    characterAt(position, equation).equals("!"))) {
		string += characterAt(position, equation);
		position.add(1);
	    }

	    return string.trim();
	}
    }

    public static String getNumberStartingAt(MutableInteger position, String equation) {
	String number = "";

	// unary minus?

	if (characterAt(position, equation).equals("-")) {
	    number="-";
	    position.add(1);
	}
	while (inRange(position, equation) && (Character.isDigit(characterAt(position, equation).charAt(0)) ||
		characterAt(position, equation).equals("."))) {
	    number += characterAt(position, equation);
	    position.add(1);
	}

	return number;
    }
    
    public static String getNonQuotedStringStartingAtWithDelimitor(MutableInteger position, String equation, String delimitor) {
	// gobble up spaces too! (for functions)
	String nonQuotedString = "";
	while (inRange(position, equation) && !characterAt(position, equation).equals(delimitor)) {
	    nonQuotedString += characterAt(position, equation);
	    position.add(1);
	}
	// point past comma or end of line
//	position.add(1);
	return nonQuotedString.trim();
    }

    public static String getNonQuotedStringStartingAt(MutableInteger position, String equation, boolean allowColon) {
	// gobble up spaces too! (for functions)
	String nonQuotedString = "";
	while (inRange(position, equation) && (Character.isLetterOrDigit(characterAt(position, equation).charAt(0)) ||
		characterAt(position, equation).equals("#") ||
		characterAt(position, equation).equals(".") ||
		characterAt(position, equation).equals("_") ||
		characterAt(position, equation).equals("$") ||
		characterAt(position, equation).equals("!") || // here we allow the ! for subscript ranges normally it's handled elsewhere
		(allowColon && characterAt(position, equation).equals(":")) ||
		characterAt(position, equation).equals(" "))) {
	    nonQuotedString += characterAt(position, equation);
	    position.add(1);
	}

	return nonQuotedString.trim();
    }

    public static String getQuotedStringStartingAt(MutableInteger position, String equation) {
	String quoteChar = characterAt(position, equation);
	String quotedString = ""; // "\"";
	position.add(1);
	while (inRange(position, equation) && !characterAt(position, equation).equals(quoteChar)) {
	    quotedString += characterAt(position, equation);
	    position.add(1);
	}
	//	    quotedString += "\"";
	position.add(1);

	return quotedString;
    }

    public static String characterAt(int position, String equation) {
	if (position > equation.length()-1)
	    return null;
	String s = equation.substring(position, position+1);
	return equation.substring(position, position+1);
    }

    public static String characterAt(MutableInteger position, String equation) {
	String s =  equation.substring(position.value(), position.value()+1);
	return equation.substring(position.value(), position.value()+1);
    }

    public static String beyondPosition(MutableInteger position, String equation) {
	return equation.substring(position.value()+1);
    }


    public static void skipWhiteSpace(MutableInteger position, String equation) {
	while (inRange(position, equation) && Character.isWhitespace(characterAt(position, equation).charAt(0)))
	    position.add(1);
    }

    public static boolean inRange(MutableInteger position, String equation) {
	if (position.value() <= equation.length()-1)
	    return true;
	else
	    return false;
    }
    
    public static String[] splitQuoted(String record, String delimitor) {
	MutableInteger position = new MutableInteger(0);
	List<String> tokens = new ArrayList<String>();

	// break into tokens comma separated, but allow quoted strings
	// with commans
	String token = "";

	skipWhiteSpace(position, record);
	boolean done = false;
	while(!done) {
	    
	    // get to the beginning of the next token, making sure that
	    // we are still in range
	    skipWhiteSpace(position, record);
	    if (!inRange(position, record))
		break;

	    // get the first character of the token (note that it can be the delimiter ","
	    String theChar = characterAt(position, record);
	    if (characterAt(position, record).equals(delimitor)) {
		tokens.add("");
		position.add(1);
		// if we hit a quote, find it's corresponding close quote
	    } else if (characterAt(position, record).equals("\"") || characterAt(position, record).equals("'")) {
		token = getQuotedStringStartingAt(position, record);
		tokens.add(token);
		skipWhiteSpace(position, record);
		
		if (inRange(position, record) && !characterAt(position, record).equals(delimitor))
		    System.out.println("Not Delimiter(1)! "+characterAt(position, record));
		
		position.add(1);
	    } else {
		token = getNonQuotedStringStartingAtWithDelimitor(position, record, delimitor);
		tokens.add(token);
		skipWhiteSpace(position, record);
		
		if (inRange(position, record) && !characterAt(position, record).equals(delimitor))
		    System.out.println("Not Delimiter(2)! "+characterAt(position, record));
		
		position.add(1);
	    }
	   
	}
	
	String[] split = new String[tokens.size()];
	int i = 0;
	for (String s : tokens)
	    split[i++] = s;
	return split;
    }


    public static List<String> tokenize(String equation) {

	MutableInteger position = new MutableInteger(0);
	List<String> tokens = new ArrayList<String>();

	// break into tokens and determine as much as possible
	// about the equation as we pass through it.
	String token = "";
	String unaryMinus = "";

	skipWhiteSpace(position, equation);
	boolean done = false;
	while(!done) {
	    skipWhiteSpace(position, equation);
	    if (!inRange(position, equation))
		break;

	    String theChar = characterAt(position, equation);

	    if (characterAt(position, equation).equals("\"") || characterAt(position, equation).equals("'")) {


		token = getQuotedStringStartingAt(position, equation);


	    } else if (Character.isLetter(characterAt(position, equation).charAt(0)) || 
		    characterAt(position, equation).equals("$")) {
		boolean allowColon = false;
		token = unaryMinus+ getNonQuotedStringStartingAt(position, equation, allowColon);

	    } else if (Character.isDigit(characterAt(position, equation).charAt(0))) {
		token = unaryMinus+ getNumberStartingAt(position, equation);
	    } else if (characterAt(position, equation).equals(":")) {
		token = getBooleanOperatorStartingAt(position, equation);
	    } else if (isTerminator(characterAt(position, equation).charAt(0))) {
		// special cases:
		// if "-" need to check if unary and attach to next token
		// if "=" need to change to "=="
		// if "<" need to check to "<>"
		// if "+" need to check if this is a unary + (previous token = "("
		if (characterAt(position, equation).equals("-")) {
		    // there may not be a previous token need to check
		    if (tokens.size()-1 >= 0) {
			String previousToken = tokens.get(tokens.size()-1);
			if (previousToken.equals("(") || previousToken.equals("=") || Parser.isOperator(previousToken) || 
				isFunctionArgumentSeparator(previousToken)) {
			    token = "_"; // unary minus
			} else {
			    token = "-";
			}
		    } else {
			token = "_"; // unary minus
		    }

		} else if (characterAt(position, equation).equals("+")) {
		    String previousToken = tokens.get(tokens.size()-1);
		    if (previousToken.equals("(") || previousToken.equals("=") || Parser.isOperator(previousToken) || 
			    isFunctionArgumentSeparator(previousToken)) {
			token = " "; // discard unary plus will this work?
		    } else {
			token = "+";
		    }
		} else if (characterAt(position, equation).equals("=")) {
		    token = "="; // used to be "==" with no add

		} else if (characterAt(position, equation).equals("<")) {  // look ahead for ">" i.e. != "<>"
		    if (characterAt(position.value()+1, equation).equals(">")) {
			token = "<>";
			position.add(1);
		    } if (characterAt(position.value()+1, equation).equals("=")) {
			token = "<=";
			position.add(1);
		    } else {
			token = characterAt(position, equation);
		    }
		}  else if (characterAt(position, equation).equals(">")) {  // look ahead for "=" i.e. != "<>"
		    if (characterAt(position.value()+1, equation).equals("=")){
			token = ">=";
			position.add(1);
		    } else {
			token = characterAt(position, equation);
		    }  
		} else {
		    token = characterAt(position, equation);
		}
		position.add(1);
	    } else if (characterAt(position, equation).equals("[") || characterAt(position, equation).equals("]")) {
		token = characterAt(position, equation);
		position.add(1);
	    }

	    tokens.add(token);

	}



	return tokens;
    }

    private static String getBooleanOperatorStartingAt(MutableInteger position, String equation) {
	String booleanOperator = ":";
	position.add(1);
	while (inRange(position, equation) && !characterAt(position, equation).equals(":")) {
	    booleanOperator += characterAt(position, equation);
	    position.add(1);
	}
	booleanOperator += ":";
	position.add(1);

	return booleanOperator;
    }

    public static  boolean isTerminator(char c) {
	if (terminatorSet.contains(c))
	    return true;
	else
	    return false;
    }

    public static String extractLHS(String equation) {
	return equation.split("=")[0].trim();
    }

    public static String extractRHS(String equation) {
	return equation.split("=",2)[1].trim();
    }


    

}
