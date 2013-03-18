package repast.simphony.systemdynamics.translator;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import repast.simphony.systemdynamics.support.ArrayReference;
import repast.simphony.systemdynamics.support.MappedSubscriptManager;
import repast.simphony.systemdynamics.support.MutableInteger;
import repast.simphony.systemdynamics.support.NamedSubscriptManager;
import repast.simphony.systemdynamics.support.SubscriptCombination;

public class Equation {
	
	private String vensimEquation;
	private String equation;
	private List<String> multiEquations;
	private String cleanEquation = "";
	private String lhs;
	private String rhs;
	private HashSet<String> rhsTokens;
	private String units;
	private String comment;
	private String other;
	private HashSet<String> references;
	private HashSet<String> referencedBy;
	private Node treeRoot;
	
	private boolean stockVariable = false;
	
	private String btwnMode = "INTERPOLATE";
	
	private boolean arrayInitialization = false;
	
	private static int nextArray = 0;
	private static int nextInt = 0;
	private static int nextInteger = 0;
	private static int nextTimeSeries = 0;
	
	private boolean oneTime = false;
	private boolean vdmLookup = false;
	
	private List<String> tokens = new ArrayList<String>();
	private List<String> rpn = new ArrayList<String>();
	
	private boolean getXls = false;
	
	private boolean definesLookup = false;
	private boolean definesLookupGetXls = false;
	private boolean definesLookupWithRange = false;
	private boolean referencesLookup = false;
	private boolean hasInitialValue = false;
	private boolean assignment = false;
	private boolean typeString = false;
	private boolean definesSubscript = false;
	
	private boolean hasMacroInvocation = false;
	
	private boolean usesTimeSeries = false;
	
	private boolean hasException = false;
	private int leftBracketCount = 0;
	private List<String> exceptions;
	
	private boolean hasMultipleEquations = false;
	private String lhsArray;
	private List<String> lhsSubscripts = null;
	
	private boolean hasLHSArrayReference = false;
	private boolean hasRHSArrayReference = false;
	
	private EquationArrayReferenceStructure ears;
	
	private boolean orderedWithInitialValue = false;
	
	private boolean hasVectorSortOrder = false;
	private boolean hasVectorElmMap = false;
	
	
	private static HashSet<Character> terminatorSet = new HashSet<Character>(); 
	private static char[] terminators = { '+', '-', '*', '/', '(', ')', '=', ',', '^', '`', '>', '<', ':', ';'};
	private static boolean terminatorsInitialized = false;
	
//	private static String[] functions = { "INTEG", "IF THEN ELSE", "MAX", "ZIDZ", "INITIAL", "XIDZ", "GAME", "MIN", "RANDOM UNIFORM", "RANDOM POISSON",
//		"WITH LOOKUP", "SMOOTHI", "SIN", "PULSE", "STEP", "RAMP", "TIMEX", "SMOOTH", "SMOOTH3", "PULSE TRAIN", "SMOOTH N", "MODULO", "DELAY N", 
//		"GET XLS CONSTANTS", "GET XLS SUBSCRIPT", "GET XLS DATA", "SUM", "PROD", "VMIN", "VMAX", "DELAY FIXED", "GET DATA BETWEEN TIMES",
//		"LN", "VECTOR SELECT", "VECTOR ELM MAP", "VECTOR SORT ORDER", "VDMLOOKUP", "ALLOCATE BY PRIORITY", "SAMPLE IF TRUE", "ACTIVE INITIAL",
//		"ELMCOUNT",  "EXP", "TREND", "GET DATA AT TIME", "SQRT", "DELAY3", "GET DATA MAX", "NOP", "TAN", "ZIDZ", "ARCCOS", "ARCSIN", "ARCTAN",
//		"QUANTUM", "DELAY3I"};
//	private static HashSet<String> functionSet = new HashSet<String>();
	
//	public static HashMap<String, Integer> numberArguments = new HashMap<String, Integer>();
	
//	private static String[] functionsRequiringLHS = functions; // { "INTEG", "SMOOTH"};
//	private static HashSet<String> functionSetRequiringLHS = new HashSet<String>();
	private static HashSet<String> lookupTables = new HashSet<String>();
	
	    private static HashMap<String, Integer> precedence = new HashMap<String, Integer>();
		
	    private static HashMap<String, Boolean> leftAssociative = new HashMap<String, Boolean>();
	   
	    
	
	    static {
//		numberArguments.put("INTEG", 2 + 4);
//		numberArguments.put("IFTHENELSE", 3 + 4);
//		numberArguments.put("MAX", 2 + 4);
//		numberArguments.put("ZIDZ", 2 + 4);
//		numberArguments.put("INITIAL", 1 + 4);
//		numberArguments.put("XIDZ", 3 + 4);
//		numberArguments.put("GAME", 1 + 4);
//		numberArguments.put("MIN", 2 + 4);
//		numberArguments.put("RANDOMUNIFORM", 3 + 4);
//		numberArguments.put("RANDOMPOISSON", 6 + 4);
//		numberArguments.put("WITHLOOKUP", 2 + 4);
//		numberArguments.put("SMOOTHI", 3 + 4);
//		numberArguments.put("SIN", 1 + 4);
//		numberArguments.put("PULSE", 2 + 4);
//		numberArguments.put("STEP", 2 + 4);
//		numberArguments.put("RAMP", 3 + 4);
//		numberArguments.put("TIMEX", 3 + 4);
//		numberArguments.put("SMOOTH", 2 + 4);
//		numberArguments.put("SMOOTH3", 2 + 4);
//		numberArguments.put("LOOKUP", 2 + 0); // slightly difference calling convention
//		numberArguments.put("PULSTRAIN", 4 + 4);
//		numberArguments.put("SMOOTHN", 4 + 4); // why is this three?
//		numberArguments.put("MODULO", 2 + 4);
//		numberArguments.put("DELAYN", 4 + 4);
//		numberArguments.put("DELAYFIXED", 3 + 4);
//		numberArguments.put("GETXLSCONSTANTS", 3 + 0); // remove time dependent vars -- not needed
//		numberArguments.put("GETXLSSUBSCRIPT", 5 + 0);
//		numberArguments.put("GETXLSDATA", 4 + 0);
//		numberArguments.put("GETDATABETWEENTIMES", 3 + 4); // already passing the time series name
//		numberArguments.put("SUM", 1 + 4);
//		numberArguments.put("PROD", 1 + 4);
//		numberArguments.put("VMIN", 1 + 4);
//		numberArguments.put("VMAX", 1 + 4);
//		numberArguments.put("LN", 1 + 4);
//		numberArguments.put("VECTORELMMAP", 2 + 4);
//		numberArguments.put("VECTORSORTORDER", 2 + 4);
//		numberArguments.put("VECTORSELECT", 5 + 4);
//		numberArguments.put("VDMLOOKUP", 1 + 4);
//		numberArguments.put("ALLOCATEBYPRIORITY", 5 + 4);
//		numberArguments.put("SAMPLEIFTRUE", 3 + 4);
//		numberArguments.put("ACTIVEINITIAL", 2 + 4);
//		numberArguments.put("ELMCOUNT", 1 + 4);
//		numberArguments.put("EXP", 1 + 4);
//		numberArguments.put("TREND", 3 + 4);
//		numberArguments.put("GETDATAATTIME", 2 + 4);
//		numberArguments.put("SQRT", 1 + 4);
//		numberArguments.put("TAN", 1 + 4);
//		numberArguments.put("DELAY3", 2 + 4);
//		numberArguments.put("DELAY3I", 3 + 4);
//		numberArguments.put("GETDATAMAX", 3 + 4);
//		numberArguments.put("NOP", 0 + 4);
//		numberArguments.put("ZIDZ", 2 + 4);
//		numberArguments.put("ARCCOS", 1 + 4);
//		numberArguments.put("ARCSIN", 1 + 4);
//		numberArguments.put("ARCTAB", 1 + 4);
//		numberArguments.put("QUANTUM", 2 + 4);
		
		
		precedence.put("_", 8);
		precedence.put("^", 7);
		precedence.put("*", 6);
		precedence.put("/", 6);
		precedence.put("+", 5);
		precedence.put("-", 5);
		precedence.put("==", 4);
		precedence.put("<>", 4);
		precedence.put(">", 4);
		precedence.put(">=", 4);
		precedence.put("<", 4);
		precedence.put("<=", 4);
		precedence.put(":AND:", 2);
		precedence.put(":OR:", 1);
		precedence.put(":NOT:", 3);

		leftAssociative.put("_", false);
		leftAssociative.put("^", false);
		leftAssociative.put("*", true);
		leftAssociative.put("/", true);
		leftAssociative.put("+", true);
		leftAssociative.put("-", true);
		leftAssociative.put("==", true);
		leftAssociative.put("<>", true);
		leftAssociative.put(">", true);
		leftAssociative.put(">=", true);
		leftAssociative.put("<", true);
		leftAssociative.put("<=", true);
		leftAssociative.put(":AND:", true);
		leftAssociative.put(":OR:", true);
		leftAssociative.put(":NOT:", false);

	    }
	
	public Equation() {
		references = new HashSet<String>();
		referencedBy = new HashSet<String>();
		rhsTokens = new HashSet<String>();
		if (!terminatorsInitialized)
			initializeTerminators();
	}
	
	private static void initializeTerminators() {
		for (char c : terminators) {
			terminatorSet.add(c);
		}
//		for (String s : functions) {
//			functionSet.add(s.toUpperCase());
//		}
//		for (String s : functionsRequiringLHS) {
//			functionSetRequiringLHS.add(s.toUpperCase());
//		}
		terminatorsInitialized = true;
	}
	
	public Equation(String vensimEquation) {
	    this();

	    // need to check if this equation contains a macro invocation
	    // if so, we need to redefine the equation

	    if (MacroManager.containsMacroInvocation(vensimEquation)) {

		this.vensimEquation = (MacroManager.expand(vensimEquation)).replaceAll("\t", "");
		hasMacroInvocation = true;
	    } else {
		this.vensimEquation = vensimEquation.replaceAll("\t", "");
	    }
	    tokenize();
	    if (vensimEquation.contains("VDMLOOKUP"))
		setVdmLookup(true);
	    //		parse();
	}
	
	private String[] getPair(MutableInteger pos, char[] data) {
		String[] pair = new String[2];
		pair[0] = getNextNumber(pos, data);
		if (pair[0] == null)
			return null;
		pair[1] = getNextNumber(pos, data);
		if (pair[1] == null)
			return null;
		return pair;
		
	}
	
	private String getNextNumber(MutableInteger pos, char[] data) {
		
		String num = "";
		if (pos.value() >= data.length)
			return null;
		while(!(Character.isDigit(data[pos.value()]) || data[pos.value()] == '+' || data[pos.value()] == '-')) {
			pos.add(1);
			if (pos.value() >= data.length)
				return null;
		}
		
		num += data[pos.value()];
		pos.add(1);
		while(Character.isDigit(data[pos.value()]) || data[pos.value()] == '.') {
			num += data[pos.value()];
			pos.add(1);
		}
		
		return num;
	}
	
	private void createEars() {
	    ears = new EquationArrayReferenceStructure(this);
	}
	
	private String processPairs(String lookupData) {

	    String minX="", minXY="", maxX="", maxXY="";
	    StringBuffer x = new StringBuffer();
	    StringBuffer y = new StringBuffer();
	    MutableInteger pos = new MutableInteger(0);
	    String[] pair = null;
	    int numValues = 0;

	    // [(0,0)-(1,1)],(0,0),(0.1,0.2),(0.2,0.38),(0.3,0.55),(0.4,0.69),(0.5,0.8),(0.6,0.88),(0.7,0.94),(0.8,0.98),(0.9,0.995),(1,1),(1e+009,1)
	    // need to worry about pairs with with no range designation
	    // by this time, the blanks have been removed so we can check for ")-("

	    char[] lookupDataChars = lookupData.replace(")-(",")(").toCharArray();

	    if (definesLookupWithRange) {
		pair = getPair(pos, lookupDataChars);
		minX = Parser.forceDouble(pair[0]);
		minXY = Parser.forceDouble(pair[1]);

		pair = getPair(pos, lookupDataChars);
		maxX = Parser.forceDouble(pair[0]);
		maxXY = Parser.forceDouble(pair[1]);
	    }

	    while(pos.value() < lookupDataChars.length) {
		pair = getPair(pos, lookupDataChars);
		if (pair == null)
		    break;
		numValues++;
		x.append(","+Parser.forceDouble(pair[0]));
		y.append(","+Parser.forceDouble(pair[1]));
	    }
	    if (definesLookupWithRange) {
		return numValues+","+minX +"," +
		minXY +"," +
		maxX + "," +
		maxXY + x.toString()+y.toString();
	    } else {
		return numValues+","+"Double.MIN_VALUE" +"," +
		"Double.MIN_VALUE" +"," +
		"Double.MAX_VALUE" + "," +
		"Double.MAX_VALUE" + x.toString()+y.toString();
	    }

	}
	
	public void processExponentiation() {
		cleanEquation = processExponentiation(cleanEquation);
	}
	
	public String processExponentiation(String eqn) {
		if (eqn.contains("^")) {
//		    printTokens();
			
			int pos = eqn.indexOf("^");
			
			
			// find lhs
			// return l will be the first character position of the begining of the lhs
			MutableInteger l = new MutableInteger(pos-1);
			String lhs = getExpLHS(l, eqn);
			
			// returning r will be the position of the last position of the rhs
			MutableInteger r = new MutableInteger(pos+1);
			String rhs = getExpRHS(r, eqn);
			
			eqn = eqn.substring(0, l.value()) + "Math.pow("+lhs+","+rhs+")"+eqn.substring(r.value()+1);
			return processExponentiation(eqn);
			
		}
		return eqn;
	}
	
	private String getExpLHS(MutableInteger r, String eqn) {
		String c;
		c = eqn.substring(r.value(), r.value()+1);
		
		String rhs = "";
		// check if it is paren
		if (c.equals(")")) {
			rhs = toOpeningParen(r, eqn);
		} else {
			// not a paren
			// or a number
			// or a variable
			rhs = toBeginningOfLHS(r, eqn);
		}
		return reverse(rhs);
	}
	
	
	private String getExpRHS(MutableInteger r, String eqn) {
		
		String c;
		c = eqn.substring(r.value(), r.value()+1);
		
		String rhs = "";
		// check if it is paren
		if (c.equals("(")) {
			rhs = toClosingParen(r, eqn);
		} else {
			// not a paren
			// could be a function invocation
			// or a number
			// or a variable
			rhs = toEndOfRHS(r, eqn);
		}
		return rhs;
	}
	
	public String toBeginningOfLHS(MutableInteger r, String eqn) {
		String rhs = "";
		
		String c;
		c = eqn.substring(r.value(), r.value()+1);

		// is it number?
		// must be among: [0-9]+-.   // we will assume that that the vensim model compiles
		// don't need to worry about exponential notation

		if (Character.isDigit(c.toCharArray()[0])) {
			rhs = c;
			r.add(-1);
			c = eqn.substring(r.value(), r.value()+1);
			while(c.equals(".") || Character.isDigit(c.toCharArray()[0])) {
				rhs += c;
				r.add(-1);
				c = eqn.substring(r.value(), r.value()+1);
			}
			r.add(1);			
		} else {
			// must be either a variable or a function call
			rhs = c;
			r.add(-1);
			c = eqn.substring(r.value(), r.value()+1);
			while(c.equals("_") || Character.isLetterOrDigit(c.toCharArray()[0])) {
				rhs += c;
				r.add(-1);
				c = eqn.substring(r.value(), r.value()+1);
			}
			r.add(1);
		}		
		return rhs;
	}
	
	public String toEndOfRHS(MutableInteger r, String eqn) {
	    String rhs = "";

	    String c;
	    c = eqn.substring(r.value(), r.value()+1);

	    // is it number?
	    // must be among: [0-9]+-.   // we will assume that that the vensim model compiles
	    // don't need to worry about exponential notation

	    if (c.equals("-") ||
		    c.equals("+") ||
		    c.equals(".") ||
		    Character.isDigit(c.toCharArray()[0])) {
		rhs = c;
		r.add(1);
		// need to check if r points past end of eqn
		if (r.value() < eqn.length()) {
		    c = eqn.substring(r.value(), r.value()+1);
		    while((c.equals(".") || Character.isDigit(c.toCharArray()[0])) && r.value() < eqn.length()) {
			rhs += c;
			r.add(1);
			if (r.value() < eqn.length())
			    c = eqn.substring(r.value(), r.value()+1);
		    }
		}
		r.add(-1);			
	    } else {
		// must be either a variable or a function call
		rhs = c;
		r.add(1);
		c = eqn.substring(r.value(), r.value()+1);
		while((c.equals("_") || Character.isLetterOrDigit(c.toCharArray()[0])) && r.value() < eqn.length()) {
		    rhs += c;
		    r.add(1);
		    if (r.value() < eqn.length())
			c = eqn.substring(r.value(), r.value()+1);
		}
		if (c.equals("(")) {
		    rhs += toClosingParen(r, eqn);
		} else {
		    r.add(-1);
		}
	    }		
	    return rhs;
	}
	
	public String reverse(String s) {
		String rev = "";
		
		char[] chars = s.toCharArray();
		for (int i = chars.length-1; i >= 0; i--)
			rev += chars[i];
		
		return rev;
	}
	
	public String toClosingParen(MutableInteger r, String eqn) {
		String c;
		c = eqn.substring(r.value(), r.value()+1);
		String rhs = c;
		// find matching paren
		int paren = 1;
		while (paren > 0) {
			r.add(1);
			c = eqn.substring(r.value(), r.value()+1);
			rhs += c;
			if (c.equals(")"))
					paren--;
			else if (c.equals("("))
				paren++;
		}
		return rhs;
	}
	
	public String toOpeningParen(MutableInteger r, String eqn) {
		String c;
		c = eqn.substring(r.value(), r.value()+1);
		String rhs = c;
		// find matching parent
		int paren = 1;
		while (paren > 0) {
			r.add(-1);
			c = eqn.substring(r.value(), r.value()+1);
			rhs += c;
			if (c.equals(")"))
					paren++;
			else if (c.equals("("))
				paren--;
		}
		r.add(-1);
		c = eqn.substring(r.value(), r.value()+1);
		if (c.equals("_") || Character.isLetterOrDigit(c.toCharArray()[0])) {
			rhs += toFunctionStart(r, eqn);
		} else {
			r.add(1); // restore pointer
		}
		return rhs;
	}
	
	public String toFunctionStart(MutableInteger r, String eqn) {
		String rhs = "";
		String c;
		
		c = eqn.substring(r.value(), r.value()+1);
		while(c.equals("_") || Character.isLetterOrDigit(c.toCharArray()[0])) {
			rhs += c;
			r.add(-1);
			c = eqn.substring(r.value(), r.value()+1);
		}
		
		r.add(1);
		return rhs;
	}
	
	
//	// Not referenced?
//	public void processIfThenElse() {
//		if (cleanEquation.contains("IFTHENELSE")) {
//		    for (int i = 0; i < tokens.size(); i++) {
//			String t = tokens.get(i);
//			if (t.equals("="))
//			    tokens.set(i, "==");
//		    }
////		    printTokens();
//			String[] statement = cleanEquation.split("IFTHENELSE", 2);
//			if (statement[1].contains("=")) {
//				statement[1] = statement[1].replaceAll("=", "==");
//			}
//			cleanEquation = statement[0] + "IFTHENELSE" + statement[1];
//		}
//	}
	
	public void processSubscriptDefinition() {
	    if (definesSubscript) {
		// registerSubscript(subscript, String ...)
		StringBuffer sb = new StringBuffer("\tregisterSubscript(\""+tokens.get(0)+"\",");
		for (int i = 1; i < tokens.size(); i++) {
		    String token = tokens.get(i);
		    if (token.equals(","))
			sb.append(token);
		    else
			sb.append("\""+token+"\"");
		}
		sb.append(")");
		cleanEquation = sb.toString();
		setOneTime(true);
	    }
	}
	
	public void processLookup() {
		// first change the statement if it is a lookup table definition
		// sample: foodpollutionmulttab(0,10,20,30,40,50,60,1.02,0.9,0.65,0.35,0.2,0.1,0.05);
		// translate to VensimFunctions.addLookup("foodpollutionmulttab", 0,10,20,30,40,50,60,1.02,0.9,0.65,0.35,0.2,0.1,0.05);
		if (definesLookup) {

			
			    String[] lookup = cleanEquation.split("\\(", 2);
			    String definition = lookup[0];
			    if (definition.contains("]")) {
				definition = definition.replace("]", "") + ",holder1,holder2]";
			    } else {
				definition = definition + "[holder1,holder2]";
			    }
			    
			    NativeDataTypeManager.addVariable(this, definition, typeString ? "String" : "double");

			    // the specification of the lookup data can appear as x[0],x[n],y[0],,,y[n]

			    if (!lookup[1].contains("(")) {
				int numValues = lookup[1].split(",").length/2;
				cleanEquation = "sdFunctions.ADDLOOKUPNATIVE(\""+lookup[0]+
				"\", "+numValues+","+lookup[1];
			    } else {
				// or [(0,0)-(1,1)],(0,0),(0.1,0.2),(0.2,0.38),(0.3,0.55),(0.4,0.69),(0.5,0.8),(0.6,0.88),(0.7,0.94),(0.8,0.98),(0.9,0.995),(1,1),(1e+009,1)
				// where [] defines range and then pairs of data points
				cleanEquation = "sdFunctions.ADDLOOKUPPAIRSNATIVE(\""+lookup[0]+
				"\", "+processPairs(lookup[1]) + ")";
			    }
			
			setOneTime(true);
		    }
		
		// if not a definition, then a reference
		// sample:  birthscrowdingmultiplier=birthscrowdingmulttab(crowding);
		// translate to: birthscrowdingmultiplier=VensimFunctions.LOOKUP("birthscrowdingmulttab", crowding);
		// need to locate the lookup and then the 
		
		
		// Need to worry about subscripted lookup tables that are defined with static subscripts while access
		// is via named subscripts with multiple terminal values.
		// sample: x[y]( (1,2), (2,3)...)
		// ref via: x[named](q)  -> sdFunctions.LOOKUP("x"+"["+concatAsSubscript(outerSub.getSubscriptValue("named"))+"]", (q)
		// can also have x[named!](q) -> sdFunctions.LOOKUP("x"+"["+concatAsSubscript(rangeSub.getSubscriptValue("named"))+"]", (q)
		// y[z,named!] - sdFunctions.LOOKUP("y"+"["+concatAsSubscript(outerSub.getSubscriptValue("z"), rangeSub.getSubscriptValue("named"))+"]", (q)
		// we can detect this and generate appropriate subscript code since the we will know outer and range
		// subscripts
		
		// work directly with tokens
		if (referencesLookup)
			return;
		if (referencesLookup) {
			for (String tableReference : lookupTables) {
				if (cleanEquation.contains(tableReference)) {
					cleanEquation = cleanEquation.replace(tableReference+"(", "sdFunctions.LOOKUP(\""+tableReference+"\",");
					ArrayList<String> newTokens = new ArrayList<String>();
					int skipParenAt = -1;
					for (int i = 0; i < tokens.size(); i++) {
					    String t = tokens.get(i);
					    if (t.equals(tableReference.replaceAll("\"", ""))) {
						tokens.set(i, "sdFunctions.LOOKUP(\""+tableReference+"\",");
						newTokens.add("sdFunctions.LOOKUP");
						newTokens.add("(");
						newTokens.add("\""+tableReference+"\"");
						skipParenAt = i+1;
					    } else {
						if (!(t.equals("(") && i == skipParenAt))
						    newTokens.add(t);
					    }
					}
					tokens = newTokens;
//					printTokens();
				}
				
			}
			
			// code for array references -- none will be processed by above if named are used on lhs
			
			int tokNum = 0;
			boolean skipParen = false;
			ArrayList<String> newTokens = new ArrayList<String>();
			for (int i = 0; i < tokens.size(); i++) {
			    String token = tokens.get(i);
			    if (token.equals("(") && skipParen) {
				skipParen = false;
				continue;
			    }
//			    if (isArrayLookupWithSubscript(token)) {
//				
//				// the access is as defined -- subscript matches
//				
//				System.out.println("isArrayLookupWithSubscript "+token);
//				ArrayReference ar = new ArrayReference(token);
//				List<String> subscripts = ar.getSubscripts();
//				List<String> rangeSubscripts = ar.getRangeSubscriptsNames();
//				List<String> nonRangeSubscripts = ar.getNonRangeSubscripts();
//				// sample: x[y]( (1,2), (2,3)...)
//				// x[y] - sdFunctions.LOOKUP("x"+"["+concatAsSubscript(outerSub.getSubscriptValue("y"))+"]", (q)
//				StringBuffer sb = new StringBuffer();
//				sb.append("sdFunctions.LOOKUP(\""+token+"\"");
//			    }
			    if (isArrayLookup(token)) {
				System.out.println("isArrayLookup "+token);
				
				// the access is different that as defined i.e. terminal definition
				// named subscript access.
				
				ArrayReference ar = new ArrayReference(token);
				List<String> subscripts = ar.getSubscripts();
				List<String> rangeSubscripts = ar.getRangeSubscriptsNames();
				List<String> nonRangeSubscripts = ar.getNonRangeSubscripts();
				// sample: x[y]( (1,2), (2,3)...)
				// ref via: x[named](q)  -> sdFunctions.LOOKUP("x"+"["+concatAsSubscript(outerSub.getSubscriptValue("named"))+"]", (q)
				// y[z,named!] - sdFunctions.LOOKUP("y"+"["+concatAsSubscript(outerSub.getSubscriptValue("z"), rangeSub.getSubscriptValue("named"))+"]", (q)
				// Each array lookup can be different in equations
				
				newTokens.add("sdFunctions.LOOKUP");
				newTokens.add("(");
				
				skipParen = true;
				StringBuffer sb = new StringBuffer();
//				sb.append("sdFunctions.LOOKUP(\""+token+"\"");   // token -> is array name of lookup
				sb.append("\""+token+"+[\"+concatAsSubscriptEQUATION(");  // start of subscript builder
				int subr = 0;
				for (String sub : subscripts) {
				    if (subr++ > 0)
					sb.append(",");
				    if (nonRangeSubscripts.contains(sub)) {
					sb.append("outerSub.getSubscriptValue(\""+sub+"\")");
				    } else {
					sb.append("rangeSub.getSubscriptValue(\""+sub+"\")");
				    }
				}
				sb.append(")+\"]\"");
				
				
				newTokens.add(sb.toString());
				newTokens.add(",");
			
//				Here!
			    } else {
				newTokens.add(token);
			    }
			    tokNum++;
			}
			tokens = newTokens;
		}
		
	}
	
	private boolean isArrayLookupWithSubscript(String s) {
	    if (!ArrayReference.isArrayReference(s))
		return false;
	    // have an array reference, is it a lookup?
	    if (lookupTables.contains(s.replace("array.", "")))
		return true;
	    else
		return false;
	}
	
	private boolean isArrayLookup(String s) {
	    if (!ArrayReference.isArrayReference(s))
		return false;
	    // have an array reference, is it a lookup?
	    if (getLookupArrays().contains(new ArrayReference(s.replace("array.", "")).getArrayName()))
		return true;
	    else
		return false;
	}
	
	private Set<String> getLookupArrays() {
	    Set<String> lookupArrays = new HashSet<String>();
	    
	    for (String lookup : lookupTables) {
		if (ArrayReference.isArrayReference(lookup))
		    lookupArrays.add(new ArrayReference(lookup).getArrayName());
	    }
	    
	    
	    return lookupArrays;
	}
	
	private static String convertExponentialNotation(String equation) {

	    if (equation == null)
		System.out.println("equation is null");
	    String noExp = new String(equation);

	    String notation = "[-]?[0-9]?\\.?[0-9]+[Ee][-+]?[0-9]+";
	    Pattern p = Pattern.compile(notation);
	    Matcher m = p.matcher(equation);
	    while(m.find()) {
		String not = m.group();

		BigDecimal bd = BigDecimal.valueOf(Double.parseDouble(not));
		String bdString = bd.toPlainString();
		if (!bdString.contains("."))
		    bdString = bdString + ".0";
		noExp = noExp.replace(not, bdString);

	    }

	    return noExp;
	}
	
	private boolean canRemoveColonEqual() {
	    
	    String[] btwnModes = {":INTERPOLATE:", ":LOOK FORWARD:", ":HOLD BACKWORD:"};

	    boolean canRemove = true;
	    
	    boolean containsColonEqual = equation.contains(":=");
	    if (!containsColonEqual)
		return canRemove;

	    String tempEqn = new String(equation);
	    
	    for (String mode : btwnModes) {
		if (tempEqn.contains(mode)) {
		    tempEqn = tempEqn.replace(mode, "");
		    return tempEqn.contains(":="); 
		}
	    }

	    return canRemove;

	}
	
	private void tokenize() {
	    MutableInteger position = new MutableInteger(0);
	    
	    if (vensimEquation.contains(":IS:")) {
		vensimEquation = vensimEquation.replace(":IS:", "=");
		typeString = true;
	    }
	    
	    // an array equation with multiple equations can be detected by "~~|"
	    if (vensimEquation.contains("goober~~|")) {
		this.hasMultipleEquations = true;
		// TODO: handle multiple equations
		
	    } else {
//		if (vensimEquation.contains(":NOT:")) {
//			vensimEquation = vensimEquation.replaceAll(":NOT:", " "); // TODO: implement -- skip for now
//		    }
		if (vensimEquation.contains(":NA:")) {
			vensimEquation = vensimEquation.replaceAll(":NA:", "NAREPLACEMENT");
		    }
		
		// look for instance of variable on LHS and no RHS
		// convert into a data lookup function that currently doesn't exist
		
//		if (!vensimEquation.contains("=") &&
//			!vensimEquation.contains(":") &&
//			!vensimEquation.contains("(")) {
//		    String lhs = vensimEquation.split("~")[0];
//		    vensimEquation = vensimEquation.replace(lhs, lhs+" = VDMLOOKUP("+lhs+")");
//		}
		

		String[] eqn = vensimEquation.split("~");
		
		if (eqn[0].endsWith("=")) {
		    eqn[0] = eqn[0] + " VDMLOOKUP(\""+eqn[0].replace("=", "").trim()+"\")";
		    setVdmLookup(true);
		}
		
		// replace := with = and $ with
		equation = eqn[0];
		if (canRemoveColonEqual())
		    equation = eqn[0].replace(":=", "=").trim();
		if (eqn.length> 1)
		    units = eqn[1].replaceAll("  *", " ").trim();
		if (eqn.length> 2)
		    comment = eqn[2].replaceAll("  *", " ").trim();
	    }
	    
//	    if (equation.contains("Storm Event Generator"))
//		System.out.println("BRKPT (Storm Event Generator)");
//	    if (equation.contains("'"))
//		System.out.println("BRKPT (')");

	    
	    
	    // TODO: delay subscript processing until code generation -- 
		
		// convert scientific notation
		
	    equation = convertExponentialNotation(equation);
		
	    // first process the LHS
	    // if this is an assignment statement, then there will be a right had side.
	    // the only statement that will not have a RHS is the definition of a lookup table
		// or a subscript definition
	    
	    boolean hasRHS = tokenizeLHS(position);
	    
	    
	    // take care of the right hand side
	    if (hasRHS)
		tokenizeRHS(position);
	    
	    if (this.isAssignment()) {
		NativeDataTypeManager.addVariable(this, this.getLhs(), typeString ? "String" : "double");
	    }
	    if (this.isDefinesLookup()) {
		this.processLookup();
	    }
	    
//	    printTokens();
	}
	
	private boolean tokenizeLHS(MutableInteger position) {

	    boolean hasRHS = true;

	    // this is used solely for detecting subscripted lookups and should
	    // not be used for any other purpose!

	    boolean hasEqualSign = equation.contains("=");
//	    if (equation.contains(":INTERP"))
//		System.out.println("INTERP");

	    String token = "";
	    skipWhiteSpace(position);
	    if (characterAt(position).equals("\"")) {
		// quoted string -- either a variable name or a lookup table name
		token = getQuotedStringStartingAt(position);
	    } else {
		// non quoted String
		boolean allowColon = false;
		token = getNonQuotedStringStartingAt(position, allowColon);
	    }
	    skipWhiteSpace(position);

	    cleanEquation = token;

	    lhs = token;
	    
	    

	    boolean ignore = true;

	    // need to determine what we have based on next token "(", "=", or "[" -- array processing for later
	    if (inRange(position) && characterAt(position).equals("=")) {

		// need to check if this is a "==" for an unchangeable variable
		// in the immediate future, this restriction will not be applied

		if (inRange(position.value()+1) && characterAt(position.value()+1).equals("=")) {
		    position.add(1);
		}
		tokens.add(token);
		assignment = true;
		tokens.add("=");
		cleanEquation += "=";
		ignore = false;
	    } else if (inRange(position) && characterAt(position).equals("(")) {
		// need to tokenize the remaining characters in the lookup definition
		ignore = false;
		tokens.add(token);
		ArrayList<String> tokenizedLookup;
		// what format of lookup are we defining?
		// need to detect "tokenizeWithLookup" case when there is no range "[()-()]" included
		// need to look ahead in the equation past position to see if a "(" exists
		if (equation.contains("[") || beyondPosition(position).contains("("))
		    tokenizedLookup = tokenizeWithLookup(position);
		else
		    tokenizedLookup = tokenizeLookup(position);
		tokens.addAll(tokenizedLookup);
		for (String s : tokenizedLookup)
		    cleanEquation += s;
		hasRHS = false;
		definesLookup = true;
		ArrayManager.setUsedAsLookup(token); // BAD BAD BAD can we trick code?
		lookupTables.add(token); // used to be clean(token)
		EquationProcessor.lookups.add(token); // used to be clean(token)
	    } else if (inRange(position) && characterAt(position).equals("[") && !hasEqualSign && equation.contains("(")) {
		// as of 8 Dec 2011, I believe that only a subscripted lookup definition can have this pattern

		// need to gobble up the square brackets and subscript name and make then part of the first
		// token as we try to fake out subscripted lookups without actually storing as arrays

		// something like abc[def]((22,22)...)
		String pastLeftBracket = equation.split("\\[",2)[1].trim();
		String subscript = pastLeftBracket.split("\\]",2)[0].trim();
		String origLHS = token;
		token += "[" + subscript +"]";
		
		// MJB 9/11/2012 Problem that lhs is defined improperly
		
		setLhs(token);

		// position to the opening left paren
		while(inRange(position) && !characterAt(position).equals("(")) {
		    position.add(1);
		}

		// need to tokenize the remaining characters in the lookup definition
		ignore = false;
		tokens.add(token);
		ArrayList<String> tokenizedLookup;

		// what format of lookup are we defining?
		// need to detect "tokenizeWithLookup" case when there is no range "[()-()]" included
		// need to look ahead in the equation past position to see if a "(" exists
		if (beyondPosition(position).contains("[") || beyondPosition(position).contains("("))
		    tokenizedLookup = tokenizeWithLookup(position);
		else
		    tokenizedLookup = tokenizeLookup(position);
		tokens.addAll(tokenizedLookup);
//		for (String s : tokenizedLookup)
//		    cleanEquation += s;
		hasRHS = false;
		definesLookup = true;
		lookupTables.add(token); // used to be clean(token)
		ArrayManager.setUsedAsLookup(token);
		EquationProcessor.lookups.add(token); // used to be clean(token)
//		List<String> s = new ArrayList<String>();
//		s.add(subscript);
		List<String> s = this.extractSubscripts(subscript);
		ArrayManager.arrayReference(origLHS, s);

		//	    } else if (inRange(position) && characterAt(position).equals("[") && !hasEqualSign && !equation.contains("(")) {

		// this is a construct of a variable on the LHS and nothing else
		// I think that this is a lookup out of a data file
		// this should be converted into a method call for a data lookup

	    } else if (inRange(position) && characterAt(position).equals(":")) {
		ignore = false;
		// need to tokenize the remaining characters in the subscript definition
		tokens.add(token);

		// this will be comma separated in tokens
		// tokenizeSubscripts stops if there is a mapping token "->"
		// we will need to check for <-> also.

		ArrayList<String> tokenizedSubscripts;
		tokenizedSubscripts = tokenizeSubscripts(position);

		tokens.addAll(tokenizedSubscripts);
		for (String s : tokenizedSubscripts)
		    cleanEquation += s;
		hasRHS = false;
		definesSubscript = true;
		EquationProcessor.subscripts.add(token);
		registerSubscripts(tokens.get(0), tokenizedSubscripts);
		NamedSubscriptManager.subscriptDefinition(tokens.get(0), extractSubscripts(tokenizedSubscripts));

		// check for -> 
		// if we find it, we can find multiple formats for the mapped subscripts
		// (1)   a: b, c, d -> q
		// (2)   a: b, c, d -> (q: x, y, z)
		// (3)   a: b, c, d -> (q: x, y, z), (q2: x2, y2, z2)

		MutableInteger lookAhead1 = new MutableInteger(position.value()+1);

		if (inRange(position) && characterAt(position).equals("-") &&
			inRange(lookAhead1) && characterAt(lookAhead1).equals(">")) {
		    processSubscriptMapping(tokens.get(0), extractSubscripts(tokenizedSubscripts), position);
		}
	    } else if (inRange(position) && characterAt(position).equals("<") && inRange(position.value()+1) && 
		    characterAt(position.value()+1).equals("-") && inRange(position.value()+2) &&
		    characterAt(position.value()+2).equals(">")) {
		ignore = false;
		// have a full subrange definition

		// NOTE: this does not directly generate code -- it is done indirectly by MappedSubscriptManager

		hasRHS = false;

		String rhsSubscript = equation.split("<->")[1].trim();
		MappedSubscriptManager.addSubscriptNameFullSubrangeMapping(lhs, rhsSubscript);

	    } else if (inRange(position) && characterAt(position).equals("[")) {
		leftBracketCount++;
		ignore = false;
		// todo: nested array References
		// cleanEquation

		lhsArray = token;

		// need to reconstruct the array reference into a single token
		StringBuffer sb = new StringBuffer("array."+token);
		//		tokens.add("array."+token);
		this.hasLHSArrayReference = true;

		hasRHS = true;

		lhsSubscripts = getSubscriptsStartingAt(position);
		ArrayManager.arrayReference(lhsArray, extractSubscripts(lhsSubscripts));
		skipWhiteSpace(position);

		// 2/22/13 - ADD PROCESSING
		// look ahead to see if the next character is the start of an interpolation mode definition
		// e.g. :INTERPOLATE:
		
		if (inRange(position) && characterAt(position).equals(":")) {
		    btwnMode = getStringDelimitedBy(position);
		    skipWhiteSpace(position);
		}
		if (inRange(position) && characterAt(position).equals("=")) {
		    String subs = "";
		    for (String s : lhsSubscripts)
			subs += s;
		    sb.append(subs);
		    //			tokens.addAll(lhsSubscripts);
		    lhs = sb.toString();

		    tokens.add(sb.toString());
		    assignment = true;
		    tokens.add("=");
		    
		    // need to skip past all "="
		    if (inRange(position.value()+1) && characterAt(position.value()+1).equals("="))
			position.add(1);

		} else if (inRange(position) && characterAt(position).equals("[")) {
		    leftBracketCount++;
		    // This is the start of the exception list
		    exceptions = extractExceptions(position);
		    this.setHasException(true);
		    // should be pointing at "=+
		    skipWhiteSpace(position);
		    if (inRange(position) && characterAt(position).equals("=")) {
			String subs = "";
			for (String s : lhsSubscripts)
			    subs += s;
			sb.append(subs);
			//			tokens.addAll(lhsSubscripts);
			lhs = sb.toString();

			tokens.add(sb.toString());
			assignment = true;
			tokens.add("=");

			// need to skip past all "="
			    if (inRange(position.value()+1) && characterAt(position.value()+1).equals("="))
				position.add(1);
		    } else {
			System.out.println("ERROR: Bad Array Reference (A)!");
			ignore = true;
		    }
		} else {
		    System.out.println("ERROR: Bad Array Reference (B)!");
		    ignore = true;
		}
		// Left Bracket Processing Ends
		
	    }
	    if (ignore) {
		System.out.println("IGNORING <"+equation+">");
		return false;
	    }
	    cleanEquation = "";
	    for (String s : tokens)
		cleanEquation += s;
	    position.add(1);
	    
//	    if (definesLookup)
//		System.out.println("defines lookup");

	    if (hasEqualSign || definesLookup) {
		UnitsManager.addLhsUnits(lhs, units);
	    }

	    return hasRHS;
	}
	
	private String getStringDelimitedBy(MutableInteger position) {
	    
	    StringBuffer sb = new StringBuffer();
	    // the character pointed at in position defines the delimiting character
	    String delim = characterAt(position);
	    position.add(1);
	    while (!characterAt(position).equals(delim)) {
		sb.append(characterAt(position));
		position.add(1);
	    }
	    position.add(1);
	    return sb.toString();
	    
	}
	
	public void removeExceptions(Set<String> arrayReferenceSet) {
	    List<String> removeList = new ArrayList<String>();
	    
	    for (String arrayReference : arrayReferenceSet) {
		if (containsException(arrayReference))
		    removeList.add(arrayReference);
	    }
	    arrayReferenceSet.removeAll(removeList);
	}
	
	public boolean containsException(String arrayReference) {
	    if (exceptions == null)
		return false;
	    boolean contains = false;
	    for (String except : exceptions) {
		if (arrayReference.contains(except))
		    contains = true;
	    }
	    
	    return contains;
	}
	
	private List<String> extractExceptions(MutableInteger position) {
	    List<String> exceptions = new ArrayList<String>();
	    while (getNextNonBlank(position).equals("[")) {
		exceptions.add(getExceptionSubscriptStartingAt(position));
		System.out.println("Detected Exception: "+exceptions.get(exceptions.size()-1));
	    }
	    return exceptions;
	}
	
	private void checkSpecialFunctions(String token) {
	    if (token.equalsIgnoreCase("Vector Sort Order")) {
		hasVectorSortOrder = true;
	    } else if (token.equalsIgnoreCase("Vector Elm Map")) {
		hasVectorElmMap = true;
	    }	    
	}
	
	private void tokenizeRHS(MutableInteger position) {
	    
	   

	    // break into tokens and determine as much as possible
	    // about the equation as we pass through it.
	    String token = "";
	    String unaryMinus = "";

	    boolean function = false;
	    boolean variable = false;
	    boolean array = false;
	    boolean macro = false;
	    boolean stringConstant = false;
	    boolean lookupRef = false;
	    
//	    if (lhs.contains("Storm Event Generator"))
//		System.out.println("BRKPT");

	    skipWhiteSpace(position);
	    boolean done = false;
	    while(!done) {

		function = false;
		variable = false;
		array = false;
		stringConstant = false;
		macro = false;
		lookupRef = false;
		
		if (position.value() >= equation.length())
		    break;
		
		String theChar = characterAt(position);
		if (characterAt(position).equals("\"") || characterAt(position).equals("'")) {
		    if (characterAt(position).equals("'"))
			stringConstant = true;
		    
		    if (typeString)
			token = getQuotedStringWithQuotesStartingAt(position);
		    else
			token = getQuotedStringStartingAt(position);
		    
		    // we have a quoted string. Her are possible situations:
		    // (1) a function reference
		    // (2) a lookup reference -- Can be an reference to an array Lookup table
		    // (3) an array reference
		    // (4) a simple variable reference
		    // (5) String for variable assignment (typeString = "true")
		    
//		    ArrayLookupTable   MARK HERE

		    skipWhiteSpace(position);
		    if (inRange(position)) {
			theChar = characterAt(position);

			// if the char == "(", we have either a function reference
			// or a lookup reference
			if (characterAt(position).equals("(")) {
			    if (FunctionManager.isFunction(token)) { // HERE~~~
				checkSpecialFunctions(token);
				function = true;
				if (isGetXlsDataFunction(token))
				    usesTimeSeries = true;
				
				if (isGetXlsLookupsFunction(token))
				    	definesLookupGetXls = true;
				
				if (token.toUpperCase().equals("INTEG"))
				    stockVariable = true;
				
				// move past "(" as code below will be adding it back
				position.add(1);
			    } else if (isMacroName(token)) {
				macro = true;
				// move past "(" as code below will be adding it back
				position.add(1);
			    } else {
				referencesLookup = true;
				lookupRef = true;
				rhsTokens.add(token);
			    }
			    // if the char == "[", we have either an array reference
			    // or a array lookup reference
			} else if (characterAt(position).equals("[")) {
			    array = true;
			    hasRHSArrayReference = true;
			    token=tokenizeArrayReference(token, position);
			    position.add(1);
			    skipWhiteSpace(position);
			    if (inRange(position.value()) && characterAt(position.value()).equals("(")) {
				referencesLookup = true;
				lookupRef = true;
				token = token.replace("array.", "");
				rhsTokens.add(token);

			    } else {
				rhsTokens.add(token);
				//					position.add(1);
			    }

			} else {
			    if (!stringConstant)
				variable = true;
			    rhsTokens.add(token);
			}
		    } else {
			// don't look ahead since it is beyond the end of statement
			// so this must be a variable
			variable = true;
			rhsTokens.add(token);
		    }
		    // this is where we grab either a funtion, a lookup, or variable
		    // need to detect arrays
		    // or special variable :NA:
		} else if (Character.isLetter(characterAt(position).charAt(0))) {
		    boolean allowColon = false;
		    token = unaryMinus+ getNonQuotedStringStartingAt(position, allowColon);

		    skipWhiteSpace(position);
		    if (inRange(position)) {
			if (characterAt(position).equals("(")) {   // checking for function invocation 
			    if (FunctionManager.isFunction(token)) {  // Here
				checkSpecialFunctions(token);
				function = true;
				if (isGetXlsDataFunction(token)) {
				    usesTimeSeries = true;
				}
				if (isGetXlsLookupsFunction(token)) {
//				    usesTimeSeries = true;
				    definesLookupGetXls = true;
				}
				

				if (token.toUpperCase().equals("INTEG"))
				    stockVariable = true;
				
				token = token.toUpperCase();
				// move past "(" as code below will be adding it back
				position.add(1);
			    } else if (isMacroName(token)) {
				    macro = true;
				    // move past "(" as code below will be adding it back
				    position.add(1);
			    } else {
				lookupRef = true;
				referencesLookup = true;
				rhsTokens.add(token);
			    }
			    // array reference?
			} else if (characterAt(position).equals("[")) {
			    array = true;
			    hasRHSArrayReference = true;
			    token=tokenizeArrayReference(token, position);
			    position.add(1);
			    skipWhiteSpace(position);
			    theChar = characterAt(position.value());
			    if (inRange(position.value()) && characterAt(position.value()).equals("(")) { // MARK HERE
				referencesLookup = true;
				lookupRef = true;
				token = token.replace("array.", "");
				rhsTokens.add(token);
				
			    } else {
				rhsTokens.add(token);
//				position.add(1);
			    }
			    

			} else {
			    variable = true;
			    rhsTokens.add(token);
			}
		    } else {
			// don't look ahead since it is beyond the end of statement
			// so this must be a variable
			variable = true;
			rhsTokens.add(token);
		    }
		} else if (Character.isDigit(characterAt(position).charAt(0))) {
		    token = unaryMinus+ getNumberStartingAt(position);
		} else if (characterAt(position).equals(":")) {
		    token = getBooleanOperatorStartingAt(position);
		} else if (isTerminator(characterAt(position).charAt(0))) {
		    // special cases:
		    // if "-" need to check if unary and attach to next token
		    // if "=" need to change to "=="
		    // if "<" need to check to "<>"
		    // if "+" need to check if this is a unary + (previous token = "("
		    if (characterAt(position).equals("-")) {
			String previousToken = tokens.get(tokens.size()-1);
			if (previousToken.equals("(") || previousToken.equals("=") || Parser.isOperator(previousToken) || 
				isFunctionArgumentSeparator(previousToken)) {
			    token = "_"; // unary minus
			} else {
			    token = "-";
			}
		    } else if (characterAt(position).equals("+")) {
			String previousToken = tokens.get(tokens.size()-1);
			if (previousToken.equals("(") || previousToken.equals("=") || Parser.isOperator(previousToken) || 
				isFunctionArgumentSeparator(previousToken)) {
			    token = " "; // discard unary plus will this work?
			} else {
			    token = "+";
			}
		    } else if (characterAt(position).equals("=")) {
			token = "==";
		    } else if (characterAt(position).equals("<")) {  // look ahead for ">" i.e. != "<>"
			if (characterAt(position.value()+1).equals(">")) {
			    token = "<>";
			    position.add(1);
			} if (characterAt(position.value()+1).equals("=")) {
			    token = "<=";
			    position.add(1);
			} else {
			    token = characterAt(position);
			}
		    }  else if (characterAt(position).equals(">")) {  // look ahead for "=" i.e. != "<>"
			if (characterAt(position.value()+1).equals("=")){
			    token = ">=";
			    position.add(1);
			} else {
			    token = characterAt(position);
			}  
		    } else {
			token = characterAt(position);
		    }
		    position.add(1);
		}


		// for functions 
		if (function) {
		    if (clean(token).startsWith("GETXLSXXX")) {
			tokens.add("sdFunctions."+clean(token));
			tokens.add("(");
			cleanEquation += "sdFunctions."+clean(token)+"(";
		    } else {
			tokens.add("sdFunctions."+clean(token));
			tokens.add("(");
			FunctionDescription fd = FunctionManager.getDescription(token);
			if (fd == null)
			    System.out.println("NULL FD");
			if (fd.isRequiresName()) {
			tokens.add("\""+lhs+"\"");
			tokens.add(",");
			}

			String valueOf = "";
			 valueOf = NativeDataTypeManager.getLegalName(this, lhs);
			 
			 System.out.println("TokRHS: valueof ="+valueOf+" lhs = "+lhs);
			 
			 int goober;
			if (ArrayReference.isArrayReference(lhs)) {
			    goober = 1;
			    valueOf = lhs;
//			    valueOf = new ArrayReferenceNative(lhs, this).generateRHSImplementation(); // I added native 1/17
			} else {
			    goober = 2;
//			    valueOf = NativeDataTypeManager.getLegalName(lhs);
			}
			
			if (fd.isRequiresValue()) {
			tokens.add(valueOf);
			tokens.add(",");
			}
			if (fd.isRequiresTime()) {
			tokens.add("time");
			tokens.add(",");
			}
			if (fd.isRequiresTimeStep()) {
			tokens.add("timeStep");
			tokens.add(",");
			}

			cleanEquation += "sdFunctions."+clean(token)+"(\""+lhs+"\","+valueOf+", time, timeStep, ";
		    }
		} else if (macro) {
		    tokens.add(token);
//			tokens.add(clean(MacroManager.makeLegal(token)));
			tokens.add("(");
//			cleanEquation += clean(MacroManager.makeLegal(token))+"(";
			cleanEquation += clean(token)+"(";
		}else if (lookupRef) {
		    // token = lookup name
		    	tokens.add("sdFunctions.LOOKUP");
		    	tokens.add("(");
		    	
		    	tokens.add("lookup."+token);
		    	
		    	tokens.add(",");
		    	// move past the "(", we've already processed it
		    	position.add(1);
		    	skipWhiteSpace(position);
		    	
			cleanEquation += "sdFunctions.LOOKUP"+"("+"lookup."+token+",";
		}else if (array) {
		    	tokens.add(token);
			cleanEquation += token;
			
		} else if (variable) {
		    
			tokens.add(NativeDataTypeManager.getLegalName(token));
			cleanEquation += NativeDataTypeManager.getLegalName(token);
		   
		} else if (stringConstant) {
			tokens.add("\""+token+"\"");
			cleanEquation += "\""+token+"\"";
		    } else {
			tokens.add(token);
			cleanEquation += token.equals("_") ? "-" : token;
		    }
		    skipWhiteSpace(position);
		}
	    // if this rhs contains an invocation of GETXLS...
	    // we do not want for it to have RHS tokens!
	    
//	    private boolean definesLookup = false;
//		private boolean definesLookupWithRange = false;
//		private boolean referencesLookup = false;
//		private boolean hasInitialValue = false;
//		private boolean assignment = false;
//		private boolean definesSubscript = false;
//	    if (equation.contains("RANDOM"))
//		System.out.println("RANDOM");
	    if (rhsTokens.size() == 0 || equation.contains("GET XLS") || equation.contains("VDMLOOKUP") ||
		    definesLookup || hasInitialValue || definesSubscript ||
		    definesLookupWithRange)
		setOneTime(true);
	    
	    // need to call this multiple times even though it meets our "one-time" criteria
	    if (equation.contains("RANDOM"))
		setOneTime(false);
	    
	
	}
	
	private boolean isArrayReference(MutableInteger position) {
	    
	    MutableInteger pos = new MutableInteger(position);
	    
	    // look ahead to determine if this is an array reference
	    // skip to ending ]
	    // find next now-white space
	    // ( indicates a lookup
	    while (!characterAt(pos).equals("]"))
		pos.add(1);
	    pos.add(1);
	    skipWhiteSpace(pos);
	    if (!characterAt(pos).equals("("))
		return true;
	    else
		return false;
	}
	
	private void validateIFTHENELSEInTree(Node root) {
	    // NOTE: we do not support boolean variables. Need to adjust the code to check for a 
	    // non-zero value rather than just a variable
	    
	    List<Node> iteNodes = new ArrayList<Node>();
	    findITE(root, iteNodes);
	    for (Node node : iteNodes) {
		validateIFTHENELSE(node);
	    }	
	}
	
	public void findITE(Node node, List<Node> iteNodes) {
	    if (node == null)
		return;
	    if (isITENode(node))
		iteNodes.add(node);
	    findITE(node.getChild(), iteNodes);
	    findITE(node.getNext(), iteNodes);
	}
	
	public static boolean isITENode(Node node) {
	    if (node.getToken().equals("sdFunctions.IFTHENELSE"))
		return true;
	    else
		return false;
	}
	
	private void validateIFTHENELSE(Node iteNode) {
	    // our condition node is the fifth argument -- based on standard calling convention
	    // of 4 informational arguments appearing first
	    
	    Node c = iteNode.getChild();
	    if (c == null)
		System.out.println("MISSING ARG");
	    c = c.getNext();
	    if (c == null)
		System.out.println("MISSING ARG");
	    c = c.getNext();
	    if (c == null)
		System.out.println("MISSING ARG");
	    c = c.getNext();
	    if (c == null)
		System.out.println("MISSING ARG");
	    c = c.getNext();
	    if (c == null)
		System.out.println("MISSING ARG");
	    
	    Node conditionNode = iteNode.getChild().getNext().getNext().getNext().getNext();
	    if (conditionNode.isTerminal()) {
		replaceBooleanVariable(conditionNode);
	    } else {
		List<Node> terminals = new ArrayList<Node>();
		findTerminal(conditionNode.getChild(), terminals);
		for (Node node : terminals) {
		    if (!Parser.isRelationalOperator(node.getParent().getToken()) &&
			    !Parser.isArithmeticOperator(node.getParent().getToken()) &&
			    !Parser.isFunctionInvocation(node.getParent().getToken()))
			replaceBooleanVariable(node);
		}
	    }
	    
	}
	
	private void findTerminal(Node node, List<Node> terminals) {
	    if (node == null)
		return;
	    if (node.isTerminal())
		terminals.add(node);
	    findTerminal(node.getChild(), terminals);
	    findTerminal(node.getNext(), terminals);
	}
	
	private void replaceBooleanVariable(Node node) {
	    // need two new nodes
	    Node equalsNode = new Node("==");
	    Node one = new Node("1.0");
	    Node previous = node.getPrevious();
	    Node next = node.getNext();
	    Node parent = node.getParent();
	    
	    // reset previous, and next nodes to point to new node
	    if (previous != null) {
		previous.setNext(equalsNode);
	    } else {
		parent.setChild(equalsNode);
	    }
	    if (next != null) {
		next.setPrevious(equalsNode);
	    }
	    
	    // point equalsNode to conditionNodes pointers
	    equalsNode.setParent(node.getParent());
	    equalsNode.setNext(node.getNext());
	    equalsNode.setPrevious(node.getPrevious());
	    
	    // create the condition expression
	    equalsNode.setChild(node);
	    node.setPrevious(null);
	    node.setNext(one);
	}
	
	private void processSubscriptMapping(String rhsSubscriptName, List<String> rhsSubscriptValues, MutableInteger position) {
	    
	    // position points to "-" of "->"
	    
	    // found -> 
	    // we can find multiple formats for the mapped subscripts
	    // (1)   a: b, c, d -> q
	    // (2)   a: b, c, d -> (q: x, y, z)
	    // (3)   a: b, c, d -> (q: x, y, z), (q2: x2, y2, z2)

	    // position points to the "->"
	    String mappedTo = equation.split("->")[1].trim();

	    // format (1)
	    if (!mappedTo.contains("(")) {
		MappedSubscriptManager.addSubscriptNameMapping(rhsSubscriptName, mappedTo);

		if (NamedSubscriptManager.isNamedSubscript(mappedTo)) {
		    List<String> mappedToValues = NamedSubscriptManager.getValuesFor(mappedTo);
		    for (int i = 0; i < mappedToValues.size(); i++)
			MappedSubscriptManager.addSubscriptValueMapping(rhsSubscriptName, mappedTo, rhsSubscriptValues.get(i), mappedToValues.get(i));
		} else {
		    MappedSubscriptManager.addSubscriptValueMappingDelayed(rhsSubscriptName, mappedTo, rhsSubscriptValues);
		}
		return;
	    } else {
		// formats (2) & (3)
		String s = characterAt(position);
		position.add(2); // skip "->"
		s = characterAt(position);
		skipWhiteSpace(position);
		s = characterAt(position);
		// should be pointing at a "("
		// if not, we are done
		while (inRange(position) && characterAt(position).equals("(")) {
		    List<String> tokens = tokenizeMappedSubscriptInParens(position);
		    
		    // first token is the mappedTo name, the rest are subscript values
		    
		    mappedTo = tokens.get(0);
		    tokens.remove(0);
		    
		    MappedSubscriptManager.addSubscriptNameMapping(rhsSubscriptName, mappedTo);
		    
		    if (NamedSubscriptManager.isNamedSubscript(mappedTo)) {
		    for (int i = 0; i < tokens.size(); i++)
			MappedSubscriptManager.addSubscriptValueMapping(rhsSubscriptName, mappedTo, rhsSubscriptValues.get(i), tokens.get(i));
		    } else {
			MappedSubscriptManager.addSubscriptValueMappingDelayed(rhsSubscriptName, mappedTo, rhsSubscriptValues, tokens);
		    }
		}
		// at this point, we are either pointing to another "(" defining another mapping, or we are done.
	    }


	}
	
	private List<String> tokenizeMappedSubscriptInParens(MutableInteger position) {
	    // come in pointing to "("
	    List<String> tokens = new ArrayList<String>();
	    int matchingParen = equation.indexOf(")", position.value());

	    String s = equation.substring(position.value()+1, matchingParen);
	    String[] t = s.split(",");
	    String name = t[0].split(":")[0].trim();
	    String first = t[0].split(":")[1].trim();
	    tokens.add(name);
	    tokens.add(first);
	    for (int i = 1; i < t.length; i++)
		tokens.add(t[i].trim());

	    position.setValue(matchingParen+1);
	    
	    if (!inRange(position))
		return tokens;
	    
	    String s1 = characterAt(position);
	    skipWhiteSpace(position);
	    s1 = characterAt(position);
	    if (inRange(position.value()) && characterAt(position.value()).equals(",")) {
		
		position.add(1); // point to ( or whitespace
		s1 = characterAt(position);
		skipWhiteSpace(position);
		s1 = characterAt(position);
	    }

	    return tokens;

	}
	
	private String tokenizeArrayReference(String arrayName, MutableInteger position) {
	    StringBuffer sb = new StringBuffer();
	    
	    if (vensimEquation.startsWith("proba durée[d1,sim]"))
		System.out.println("proba durée[d1,sim]");

	    sb.append("array."+arrayName);

	    sb.append("[");
	    List<String> tokenList = tokenizeSubscripts(position);
	    for (String token : tokenList) {

		sb.append(token);
	    }
	    
	    ArrayManager.arrayReference(arrayName, extractSubscripts(tokenList));
	    // position should be correct
	    return sb.toString();
	}
	
//	private boolean isArrayConstantInitialization() {
//	    // scan the equation
//	    if (!this.isHasLHSArrayReference())
//		return false;
//	    for (String value : getRHStokens(tokens)) {
//		if (!Parser.isNumber(value))
//		    return false;
//	    }
//	    return true;
//	}
	
	private List<String> getRHStokens(List<String> tokens) {
	    List<String> al = new ArrayList<String>();
	    boolean afterEquals = false;
	    for (String token : tokens) {
		if (afterEquals) {
		    if (!token.equals(",") && !token.equals(";"))
			al.add(token);
		} else if (token.equals("="))
		    afterEquals = true;
	    }
	    return al;
	}
	
	private void registerSubscripts(String subscript, List<String> tokenizedSubscripts) {
	    List<String> al = new ArrayList<String>();
	    for (String token : tokenizedSubscripts)
		if (!token.equals(","))
		    al.add(token);
	    EquationProcessor.subscriptValues.put(subscript, al);
	}
	
	private String getOffset(SubscriptCombination combo1, SubscriptCombination combo2) {
	    // only need to find offset between last two subscripts
	    return "";
	    
	}
	
	public List<SubscriptCombination> getSubscriptValueCombinations(String ...strings) {
	    // given a list of subscripts return all combinations of values
	    List<List<String>> bySubscriptValue = new ArrayList<List<String>>();

	    List<SubscriptCombination> values = new ArrayList<SubscriptCombination>();
	    List<String> order = new ArrayList<String>();

	    // generate list of lists of values
	    for (String subscriptName : strings) {
		bySubscriptValue.add(extractSubscripts(getSubscriptValues(subscriptName)));
		order.add(subscriptName);
	    }
	    int numPermutations = 1;
	    for (int i = 0; i < bySubscriptValue.size(); i++) {
		numPermutations *= bySubscriptValue.get(i).size();
	    }
	    // create all permutations
	    SubscriptCombination[] combinations = new SubscriptCombination[numPermutations];

	    // initialize the empty buffers
	    for (int i = 0; i < numPermutations; i++)
		combinations[i] = new SubscriptCombination(order);

	    // for each of the subscripts
	    for (int subscriptNumber = 0; subscriptNumber < strings.length; subscriptNumber++) {

		// determine the number of times that the same value should be outputed
		int numPerValue = 1;
		for (int i = subscriptNumber+1; i < bySubscriptValue.size(); i++) {
		    numPerValue *= bySubscriptValue.get(i).size();
		}
		int row = 0;
		while(row < numPermutations) {
		    for (String value : bySubscriptValue.get(subscriptNumber)) {

			for (int i = 0; i < numPerValue; i++) {
			    combinations[row].addSubscriptValue(strings[subscriptNumber], value);
			    row++;
			}
		    }
		}
	    }

	    for (SubscriptCombination combo : combinations) {
		values.add(combo);
	    }

	    return values;
	}
	
	public List<String> getSubscriptValues(String subscriptName) {
	    List<String> al = new ArrayList<String>();
	    if (isNamedSubscript(subscriptName)) {
		for (String v : EquationProcessor.subscriptValues.get(subscriptName)) {
		    if (isNamedSubscript(v))
			al.addAll(getSubscriptValues(v));
		    else
			al.add(v);
		}
	    } else {
		al.add(subscriptName);
	    }
	    return al;
	}
	
	public boolean isNamedSubscript(String subscriptName) {
	    if (EquationProcessor.subscriptValues.containsKey(subscriptName))
		return true;
	    else
		return false;
	}
	
	private List<String> extractSubscripts(List<String> subscriptList) {
	    List<String> al = new ArrayList<String>();
	    for (String token : subscriptList)
		if (!token.equals("[") && !token.equals(",") && !token.equals("]"))
		    al.add(token);
	    return al;
	}
	
	private List<String> extractSubscripts(String subscript) {
	    List<String> al = new ArrayList<String>();
	    String[] subrs = subscript.split(",");
	    for (String token : subrs)
		al.add(token.trim());
	    return al;
	}
	
	public String generateArrayConstantsInitialization() {
	    StringBuffer sb = new StringBuffer();
	    String[] values = asArray(getRHStokens(tokens));

	    if (!equation.contains("GET XLS")) {
		if (cleanEquation.contains("sdFunction")) {
		    // or of the form X[a,b] = SDFuntion...

		    String funcCallHead = "";
		    String funcCallTail = "";
		    funcCallHead = tokens.get(2) + "("; // actual function call

		    // 4 ,
		    // 5 var name
		    // 6 ,
		    // 7 var value

		    for (int t = 7; t < tokens.size(); t++)
			funcCallTail += tokens.get(t);
		    
			funcCallTail += ";\n";
		   
		    sb.append("// funcCallTail = "+funcCallTail);

		   

			// generate the entire group of code.

			sb.append("// getOuter Equation 1691\n");
			sb.append(getEars().getOuterLoops());
			sb.append(ears.getLHSassignment()+ " = "+
				funcCallHead+"\""+ears.getLHSassignment()+"\","+ears.getLHSassignment()+funcCallTail);
			for (int i = 0; i < ears.getOuterClosingCount(); i++)
			    sb.append("} // close outer \n");

		} else {

		    // need to create a new array
		    String vArray = getNextArray();
		    if (!Translator.target.equals(ReaderConstants.C))
			sb.append("double[] ");
		    else
			sb.append("double* ");
		    sb.append(vArray+" = newDoubleArray( ");
		    boolean needsIncrement = false;
		    if (values.length > 1)
			needsIncrement = true;
		    sb.append(values.length);
		    for (int j = 0; j < values.length; j++) {
			
			    sb.append(",");
			sb.append(values[j]);
		    }
		    sb.append(");\n");

		    String integer = getNextInt();
		    sb.append("int "+integer+" = 0;\n");
		    
		   
			sb.append("// getOuter Equation 1742\n");
			    sb.append(getEars().getOuterLoops());
			    sb.append(getEars().getLHSassignment());
			    sb.append(" = "+vArray+"["+integer+"];\n"); 
			 // log
			    sb.append("/* generateArrayConstantsInitialization 1 */\n");
			    sb.append("logit(stringConcat(\""+getEars().getLhsArrayReference().getArrayName()+"\"");
			    
			    for (int i = 0; i < getEars().getOuterClosingCount(); i++) {
				sb.append(",\"[\",intToString(outer"+i+"),\"]\"");
			    }
			    sb.append("),0.0,");
			    sb.append(vArray+"["+integer+"]);\n");
			    // inc pointer
			    if (needsIncrement)
				sb.append(integer+"++;\n");
			    for (int i = 0; i < getEars().getOuterClosingCount(); i++)
				sb.append("}\n");

		}
	    } else {
		if (equation.contains("CONSTANTS")) {
		// or of the form X[a,b] = GETXLSCONSTANTS...
		int numTokens = tokens.size();
		int cellLocation = numTokens-2; // 0 index and next to last token (last arg to function)
		String funcCallHead = "";
		String funcCallCellStart = "";
		String funcCallCell = "";
		String funcCallTail = ")";
		for (int t = 2; t < cellLocation; t++)
		    funcCallHead += tokens.get(t);
		funcCallCellStart = tokens.get(cellLocation).replace("\"", "");;
		int numDimensions = asArray(extractSubscripts(lhsSubscripts)).length;
		String columnStart = Parser.getColumnLettersFromCellAddress(funcCallCellStart);
		int rowStart = Parser.getRowFromCellAddress(funcCallCellStart);

		int numRows = 1;
		int numCols = -1;

		List<String> subs = extractSubscripts(lhsSubscripts);
		for (int i = subs.size()-1; i >= 0; i--) {
		    if (NamedSubscriptManager.isNamedSubscript(subs.get(i))) {
			if (numCols == -1) {
			    numCols = NamedSubscriptManager.getValuesFor(subs.get(i)).size();
			} else  {
			    numRows *= NamedSubscriptManager.getValuesFor(subs.get(i)).size();
			}
		    }
		}


		List<SubscriptCombination> combos = getSubscriptValueCombinations(asArray(extractSubscripts(lhsSubscripts)));
		//		    String r = getNextInteger();
		//		    String c = getNextInteger();
		//		    
		//		    sb.append("Integer "+r+" = new MutableInteger();\n");
		//		    sb.append("Integer "+r+" = new MutableInteger();\n");
		////		    getNumRowsAndCols(String arrayName, String subscript, MutableInteger numRows, MutableInteger numCols)
		//		    sb.append("ArrayManager.getNumRowsAndCols(\""+lhsArray+"\", subscript, numRows, numCols);\n");
		//		    String v = funcCallHead + "\"" + funcCallCellStart + "\"" + ","+ r + ".intValue(),"+ c+ ".intValue()" +funcCallTail;
		String v = funcCallHead + "\"" + funcCallCellStart + "\"" + ","+ numRows + ","+ numCols +funcCallTail;
		String array = getNextArray();
		sb.append("double[] "+array+" = "+v+";\n");
		String integer = getNextInt();
		sb.append("int "+integer+" = 0;\n");
		

		    sb.append("// getOuter Equation 1811\n");
		    sb.append(getEars().getOuterLoops());
		    sb.append(getEars().getLHSassignment());
		    sb.append(" = "+array+"["+integer+"];\n");
		    // log
		    sb.append("/* generateArrayConstantsInitialization 2 */\n");
		    sb.append("logit(\""+getEars().getLhsArrayReference().getArrayName()+"\"");
		    for (int i = 0; i < getEars().getOuterClosingCount(); i++) {
			sb.append("+\"[\"+outer"+i+"+\"]\"");
		    }
		    sb.append(",0.0,");
		    sb.append(array+"["+integer+"]);\n");
		    // inc pointer
		    sb.append(integer+"++;\n");
		    for (int i = 0; i < getEars().getOuterClosingCount(); i++)
			sb.append("}\n");

	    } else {
		// or of the form X[a,b] = GETXLSDATA...
		int numTokens = tokens.size();
		int cellLocation = numTokens-2; // 0 index and next to last token (last arg to function)
		String funcCallHead = "";
		String funcCallCellStart = "";
		
		String funcCallTail = ")";
		for (int t = 2; t < cellLocation; t++)
		    funcCallHead += tokens.get(t);
		funcCallCellStart = tokens.get(cellLocation).replace("\"", "");

		int numRows = 1;
		int numCols = -1;

		List<String> subs = extractSubscripts(lhsSubscripts);
		for (int i = subs.size()-1; i >= 0; i--) {
		    if (NamedSubscriptManager.isNamedSubscript(subs.get(i))) {
			if (numCols == -1) {
			    numCols = NamedSubscriptManager.getValuesFor(subs.get(i)).size();
			} else  {
			    numRows *= NamedSubscriptManager.getValuesFor(subs.get(i)).size();
			}
		    }
		}


		List<SubscriptCombination> combos = getSubscriptValueCombinations(asArray(extractSubscripts(lhsSubscripts)));
		//		    String r = getNextInteger();
		//		    String c = getNextInteger();
		//		    
		//		    sb.append("Integer "+r+" = new MutableInteger();\n");
		//		    sb.append("Integer "+r+" = new MutableInteger();\n");
		////		    getNumRowsAndCols(String arrayName, String subscript, MutableInteger numRows, MutableInteger numCols)
		//		    sb.append("ArrayManager.getNumRowsAndCols(\""+lhsArray+"\", subscript, numRows, numCols);\n");
		//		    String v = funcCallHead + "\"" + funcCallCellStart + "\"" + ","+ r + ".intValue(),"+ c+ ".intValue()" +funcCallTail;
		String timeSeries = getNextTimeSeries();
		
		String v = funcCallHead + "\"" + funcCallCellStart + "\"" + ","+ numRows + ","+ numCols+funcCallTail;
		sb.append("TimeSeriesInstance "+timeSeries+" = "+v+";\n");
		String integer = getNextInt();
		sb.append("int "+integer+" = 0;\n");
		
		    sb.append("// getOuter Equation 1877\n");
		    sb.append(getEars().getOuterLoops());
		    
		    // special case: using GET XLS LOOKUPS rather than GET XLS DATA
		    if (this.isDefinesLookupGetXls()) {
			sb.append(getEars().getLHSassignment()+" = ");
			sb.append("sdFunctions.ADDLOOKUPNATIVE(\""+lhsArray+"\"+ "+ getEars().getLHSsubscript()+", "+timeSeries+".getTime(),"+timeSeries+".valuesForIndex("+integer+"++));");
		    } else {
		    sb.append("\ttimeSeriesData.addTimeSeries(data, \""+lhsArray+"\", "+ getEars().getLHSsubscript()+", "+timeSeries+".getTime(),"+timeSeries+".valuesForIndex("+integer+"++));");
		    }
		    for (int i = 0; i < getEars().getOuterClosingCount(); i++)
			sb.append("}\n");
		
	    }
	    }

	    return sb.toString();
	}
	
//	private String getCellLocation( List<SubscriptCombination> combos, SubscriptCombination sub, 
//		String funcCallCellStart, int numDimensions, String columnStart, int rowStart) {
//	    StringBuffer sb = new StringBuffer();
//	    
//	    // vectors and scalars are easy
//	    if (numDimensions == 1) {
//		int columnOffset = 0;
//		for (columnOffset = 0; columnOffset < combos.size(); columnOffset++)
//		    if (combos.get(columnOffset).equals(sub)) {
//			sb.append(getColumn(columnStart, columnOffset) + rowStart);
//		    }
//	    } else {
//		int columnOffset = 0;
//		for (columnOffset = 0; columnOffset < combos.size(); columnOffset++)
//		    if (combos.get(columnOffset).equals(sub)) {
//			sb.append(getColumn(columnStart, columnOffset) + rowStart);
//		    }
//	    }
//	    
//	    
//	    return sb.toString();
//	    
//	}
	
//	private String getColumn(String column, int offset) {
//	    
//	    String allColumns = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//	    String[] asArray = toStringArray(column);
//	    String lastChar = asArray[asArray.length-1];
//	    int pos = allColumns.indexOf(lastChar);
//	    
//	    if (pos + offset < allColumns.length()) {
//		asArray[asArray.length-1] = allColumns.substring(pos+offset, pos+offset+1);
//		return asString(asArray);
//	    } else {
//	    }
//	    return asString(asArray);
//	}
	
	private String asString(String[] asArray) {
	    StringBuffer sb = new StringBuffer();
	    for (String s : asArray)
		sb.append(s);
	    return sb.toString();
	}
	
//	private String[] toStringArray(String column) {
//	    String[] newCol = new String[column.length()];
//	    for (int i = 0; i < newCol.length; i++)
//		newCol[i] = column.substring(i, i+1);
//	    return newCol;
//	}
	
	private String[] asArray(List<String> items) {
	    String[] array = new String[items.size()];
	    for (int i = 0; i < items.size(); i++) {
		array[i] = items.get(i);
	    }
	    return array;
	}
	
	private ArrayList<String> tokenizeWithLookup(MutableInteger position) {
	    ArrayList<String> al = new ArrayList<String>();
//	    "Op: Backlog Saturation Effect"= WITH LOOKUP (
//		        "Op: Checkpoint Service Backlog"/"Op: Initial Service Backlog",
//		                ([(0,0)-(100,10)],(0,0),(0.001,1),(1,1),(100,1) ))
	    
	    al.add("(");
	    position.add(1);
	    skipWhiteSpace(position);
	    
	    // process min and max
	    // if they exist
	    
	    if (characterAt(position).equals("[")) {

		definesLookupWithRange = true;
		
		checkIf(characterAt(position),"[");
		al.add("[");
		position.add(1);
		skipWhiteSpace(position);

		checkIf(characterAt(position),"(");
		al.add("(");
		position.add(1);
		skipWhiteSpace(position);

		al.add(getNumberStartingAt(position));
		//	    position.add(1);
		skipWhiteSpace(position);

		checkIf(characterAt(position),",");
		al.add(",");
		position.add(1);
		skipWhiteSpace(position);

		al.add(getNumberStartingAt(position));
		//	    position.add(1);
		skipWhiteSpace(position);

		checkIf(characterAt(position),")");
		al.add(")");
		position.add(1);
		skipWhiteSpace(position);

		checkIf(characterAt(position),"-");
		al.add("-");
		position.add(1);
		skipWhiteSpace(position);

		al.add("(");
		position.add(1);
		skipWhiteSpace(position);

		al.add(getNumberStartingAt(position));
		//	    position.add(1);
		skipWhiteSpace(position);

		al.add(",");
		position.add(1);
		skipWhiteSpace(position);

		al.add(getNumberStartingAt(position));
		//	    position.add(1);
		skipWhiteSpace(position);

		al.add(")");
		position.add(1);
		skipWhiteSpace(position);

		al.add("]");
		position.add(1);
		skipWhiteSpace(position);
	    }
	    
	    
	    boolean done = false;
	    int openParens = 1;
	    
	    while(inRange(position) && !done) {
		if (Character.isDigit(characterAt(position).charAt(0)) || characterAt(position).equals("-")) {
		    al.add(getNumberStartingAt(position));
		} else {
		    al.add(characterAt(position));
		    if (characterAt(position).equals("(")) {
			openParens++;
		    } else if (characterAt(position).equals("(")) {
			openParens--;
			if (openParens == 0)
			    done = true;
		    }
		    position.add(1);
		}
	    }
	    
	    al.add(")");
	    
	    return al;
	}
	
	private void checkIf(String s1, String s2) {
	    if (!s1.equals(s2)) {
		System.out.println("Expecting <"+s1+"> found <"+s2+">");
	    }
	}
	
	private ArrayList<String>  tokenizeSubscripts(MutableInteger position) {
	    // position is pointing to "["
	    ArrayList<String> al = new ArrayList<String>();
	    position.add(1);
	    skipWhiteSpace(position);
	    
	    // need to check for numeric ranges of the form "(X1-X12)"
	    // expand these out
	    
	    if (characterAt(position).equals("(")) {
		expandNumericRange(position, al);
	    } else {
		al.add(getStringStartingAt(position));
	    }
	    skipWhiteSpace(position);
	    while (inRange(position)) {
		
		MutableInteger lookAhead1 = new MutableInteger(position);
		lookAhead1.add(1);
		
		skipWhiteSpace(position);
		if (characterAt(position).equals("]")) {
		    al.add("]");
		    break;
		}
		
		if (characterAt(position).equals("-") && characterAt(lookAhead1).equals(">")) {
		    return al;
		}
		al.add(characterAt(position)); // comma
		position.add(1);
		skipWhiteSpace(position);
		
		 // need to check for numeric ranges of the form "(X1-X12)"
		    // expand these out
		if (characterAt(position).equals("(")) {
			expandNumericRange(position, al);
		    } else {
		al.add(getStringStartingAt(position));
		    }
		// number
		skipWhiteSpace(position);
	    }
	    
	    
	    return al;
	    
	}
	
	private void expandNumericRange(MutableInteger position, ArrayList<String> tokenList) {
	    
	    // we have a string of the form: ( x1-x23 )
	    // we process up to the closing ) and expand to the appropriate number of entries adding them
	    // and a separating comma to the token list
	    
	    String str = equation.substring(position.value());
	    str = str.substring(1, str.indexOf(")")).trim();
	    
	    // str now contains x1-x23
	    
	    String l = str.split("-")[0].trim();
	    String r = str.split("-")[1].trim();
	    String start = extractNumberFromEndOfSubscript(l);
	    String end = extractNumberFromEndOfSubscript(r);
	    String chars = l.replaceAll(start, "");
	    
	    int numIndex = 0;
	    
	    for (int i = Integer.parseInt(start); i <= Integer.parseInt(end); i++) {
		if (numIndex++ > 0)
		    tokenList.add(",");
		tokenList.add(chars + Integer.toString(i));
	    }
	    
	    // need to reposition position
	    while (!characterAt(position).equals(")")) {
		position.add(1);
	    }
	    position.add(1);
	    skipWhiteSpace(position);
	    
	}
	
	private String extractNumberFromEndOfSubscript(String s) {
	    String n = "";
	    for (int i = s.length()-1; i >= 0; i--) {
		if (Character.isDigit(s.charAt(i))) {
		    n = s.charAt(i) + n;
		} else {
		    return n;
		}
	    }
	    return "";
	}
	
	private ArrayList<String>  tokenizeLookup(MutableInteger position) {
	    // position is pointing to "("
	    ArrayList<String> al = new ArrayList<String>();
	    al.add("(");
	    position.add(1);
	    skipWhiteSpace(position);
	    al.add(getNumberStartingAt(position));
	    skipWhiteSpace(position);
	    while (!characterAt(position).equals(")")) {
		skipWhiteSpace(position);
		al.add(characterAt(position)); // comma
		position.add(1);
		skipWhiteSpace(position);
		al.add(getNumberStartingAt(position)); // number
		skipWhiteSpace(position);
	    }
	    al.add(")");
	    
	    return al;
	    
	}
	
	private boolean inRange(int position) {
	    if (position <= equation.length()-1)
		return true;
	    else
		return false;
	}
	
	private boolean inRange(MutableInteger position) {
	    if (position.value() <= equation.length()-1)
		return true;
	    else
		return false;
	}
	
	private String getBooleanOperatorStartingAt(MutableInteger position) {
	    String booleanOperator = ":";
	    position.add(1);
	    while (inRange(position) && !characterAt(position).equals(":")) {
		booleanOperator += characterAt(position);
		position.add(1);
	    }
	    booleanOperator += ":";
	    position.add(1);
	    
	    return booleanOperator;
	}
	
	private List<String> getSubscriptsStartingAt(MutableInteger position) {
	 // position is pointing to "("
	    ArrayList<String> al = new ArrayList<String>();
	   al.add("[");
	    position.add(1);
	    skipWhiteSpace(position);
	    al.add(getStringStartingAt(position));
	    skipWhiteSpace(position);
	    while (!characterAt(position).equals("]")) {
		skipWhiteSpace(position);
		al.add(characterAt(position)); // comma
		position.add(1);
		skipWhiteSpace(position);
		al.add(getStringStartingAt(position)); // number
		skipWhiteSpace(position);
	    }
	    position.add(1);
	    al.add("]");
	    return al;
	}
	
	private String getExceptionSubscriptStartingAt(MutableInteger position) {
		 // position is pointing to "["
		    String exceptionSubscript = "";
		   // skip over the "["
		    position.add(1);
		    
		    // grab what ever's nexr
		    skipWhiteSpace(position);
		    exceptionSubscript += getStringStartingAt(position);
		    skipWhiteSpace(position);
		    // until we reach the final
		    while (!characterAt(position).equals("]")) {
			skipWhiteSpace(position);
			exceptionSubscript += characterAt(position); // comma
			position.add(1);
			skipWhiteSpace(position);
			exceptionSubscript += getStringStartingAt(position); // number
			skipWhiteSpace(position);
		    }
		    position.add(1);
		    return exceptionSubscript;
		}
	
	private String getNextNonBlank(MutableInteger position) {
	    String string = "";
	    MutableInteger pos = new MutableInteger(position);
	    skipWhiteSpace(pos);
	    return characterAt(position);
	}
	
	private String getStringStartingAt(MutableInteger position) {
	    String string = "";
	    
	    if (inRange(position) && characterAt(position).equals("\"")) {
		return getQuotedStringStartingAt(position);
	    } else {
	    while (inRange(position) && (Character.isLetter(characterAt(position).charAt(0)) ||
		    Character.isDigit(characterAt(position).charAt(0)) || 
		    characterAt(position).equals(" ") || 
		    characterAt(position).equals("\"") ||
		    characterAt(position).equals("!"))) {
		string += characterAt(position);
		position.add(1);
	    }
	    
	    return string.trim();
	    }
	}
	
	private String getNumberStartingAt(MutableInteger position) {
	    String number = "";
	    
	    // unary minus?
	    
	    if (characterAt(position).equals("-")) {
		number="-";
		position.add(1);
	    }
	    while (inRange(position) && (Character.isDigit(characterAt(position).charAt(0)) ||
		    characterAt(position).equals("."))) {
		number += characterAt(position);
		position.add(1);
	    }
	    
	    return number;
	}
	
	private String getNonQuotedStringStartingAt(MutableInteger position, boolean allowColon) {
	    // gobble up spaces too! (for functions)
	    String nonQuotedString = "";
	    while (inRange(position) && (Character.isLetterOrDigit(characterAt(position).charAt(0)) ||
		    characterAt(position).equals("_") ||
		    characterAt(position).equals("$") ||
		    characterAt(position).equals("'") ||   // allow single quote if in a non-quoted string
		    (allowColon && characterAt(position).equals(":")) ||
		    characterAt(position).equals(" "))) {
		nonQuotedString += characterAt(position);
		position.add(1);
	    }
	    
	    return nonQuotedString.trim();
	}
	
	private String getQuotedStringStartingAt(MutableInteger position) {
	    String quoteChar = characterAt(position);
	    String quotedString = ""; // "\"";
	    position.add(1);
	    while (inRange(position) && !characterAt(position).equals(quoteChar)) {
		quotedString += characterAt(position);
		position.add(1);
	    }
//	    quotedString += "\"";
	    position.add(1);
	    
	    return quotedString;
	}
	
	private String getQuotedStringWithQuotesStartingAt(MutableInteger position) {
	    String quoteChar = characterAt(position);
	    String quotedString = "\\\"";
	    position.add(1);
	    while (inRange(position) && !characterAt(position).equals(quoteChar)) {
		quotedString += characterAt(position);
		position.add(1);
	    }
	    quotedString += "\\\"";
	    position.add(1);
	    
	    return quotedString;
	}
	
	private String characterAt(int position) {
	    if (position > equation.length()-1)
		return null;
	    String s = equation.substring(position, position+1);
	    return equation.substring(position, position+1);
	}
	
	private String characterAt(MutableInteger position) {
	    String s =  equation.substring(position.value(), position.value()+1);
	    return equation.substring(position.value(), position.value()+1);
	}
	
	private String beyondPosition(MutableInteger position) {
	    return equation.substring(position.value()+1);
	}
	
	private void skipWhiteSpace(MutableInteger position) {
	    while (inRange(position) && Character.isWhitespace(characterAt(position).charAt(0)))
		position.add(1);
	}
	
//	private void parse() {
//	    
//	    tokens.clear();
//		
//		String[] eqn = vensimEquation.split("~");
//		equation = eqn[0].trim();
//		
//		// first let's deal with any logical ORs
//		
//		equation = equation.replace(":OR:", "`");
//		
//		// next convert scientific notation
//		
//		equation = convertExponentialNotation(equation);
//		
//	
//		char[] chars = equation.toCharArray();
//		boolean inQuote = false;
//		boolean leftHandSide = true;
//		String token = "";
//		int cPos = -1;
//		
//		boolean processForTerminator;
//
//		for (char c : chars) {
//			processForTerminator = true;
//			cPos++;
//			
////			if (c == '-')
////				System.out.println("Stop Here!");
//		
//			// if we are in a quoted string, just gobble up characters into the token
//			if (inQuote) {
//				processForTerminator = false;
//				if (c == '"') {
//					//terminating quote
//					inQuote = false;
//					if (cPos == chars.length-1) {
//						processForTerminator = true;
//					} else {
//						token = token + c;
//					}
//				} else {
//					token = token + c;
//				}
//			} else if (c == '"'){
//				// starting quote
//				inQuote = true;
//				token = token + c;
//				processForTerminator = false;
//			} 
//			
//			
//			// need to rethink this whole last character thing
//
//			if (processForTerminator) {
//			    
//				if (cPos == chars.length-1 || isTerminator(c)) {
//					// if this is the last character, we need to add it to the token
//					if (cPos == chars.length-1 && !isTerminator(c)) {
//						token = token + c;
//					}
//
//					// do we have a variable (or function) or a number?
//					if ((token.replace(" ", "").length() == 0 && c!= '-')|| Parser.isNumber(token)) {
//						// need to discard
//						if (Parser.isNumber(token)) {
//							if (cPos == chars.length-1 && !isTerminator(c)) {
//								cleanEquation = cleanEquation + clean(token);
//								tokens.add(clean(token));
//							} else {
//								cleanEquation = cleanEquation + clean(token) + c;
//								tokens.add(clean(token));
//								tokens.add(new String(new char[]{c}));
//							}
//						}
//						else {
//						    // a unary minus came here!
//							cleanEquation = cleanEquation + c;
//							tokens.add(new String(new char[]{c}));
//							
//						}
//						token = "";
//					} else {
//					    // we either have a function or a variable
//					    // if function, ignore for now
//					    String clean = clean(token);
//					    if (isFunction(token.trim())) {
//						if (requiresLHS(clean)) {
//						    cleanEquation = cleanEquation + "sdFunctions."+clean+"(\""+lhs+"\",valueOf(\""+lhs+"\"), time, timeStep, ";
//						    tokens.add("sdFunctions."+clean);
//						    tokens.add("(");
//						    tokens.add("\""+lhs+"\"");
//						    tokens.add(",");
//						    tokens.add("valueOf(\""+lhs+"\")");
//						    tokens.add(",");
//						    tokens.add("time");
//						    tokens.add(",");
//						    tokens.add("timeStep");
//						    tokens.add(",");
//						} else { 
//						    cleanEquation = cleanEquation + "sdFunctions."+clean+"(\""+lhs+"\", time, timeStep, ";
//
//						    tokens.add("sdFunctions."+clean);
//						    tokens.add("(");
//						    tokens.add("\""+lhs+"\"");
//						    tokens.add(",");
//						    tokens.add("time");
//						    tokens.add(",");
//						    tokens.add("timeStep");
//						    tokens.add(",");
//						}
//						//							if (cPos < chars.length-1 || isTerminator(c))
//						//								cleanEquation = cleanEquation + clean + c;
//						//							else
//						//								cleanEquation = cleanEquation + clean;
//					    } else { // variable
//						if (leftHandSide) {
//						    lhs = clean;
//						    leftHandSide = false;
//
//						    // a '(' terminator on left side indicates a lookup 
//						    if (c == '(') {
//							definesLookup = true;
//							lookupTables.add(token); // used to be clean
//							EquationProcessorlookups.add(token); // used to be clean
//						    }
//
//						} else {
//						    if (!isSeparator(token.trim()) && clean != null) {
//							rhsTokens.add(clean);
//							//									System.out.println("RHS Add: "+clean);
//
//							EquationProcessorrhsvariables.add(clean);
//
//
//
//							if (c == '(') {
//							    referencesLookup = true;
//							} else {
//							    clean = "valueOf(\"" + clean + "\")";
//							}
//						    }
//						}
//
//
//						if (cPos < chars.length-1 || isTerminator(c)) {
//						    cleanEquation = cleanEquation + clean + c;
//						    if (clean.trim().length() > 0)
//							tokens.add(clean);
//
//						    if (c == '=')
//							assignment = true;
//
//						    // check for a unary -
//						    String lastToken = tokens.get(tokens.size()-1);
//						    if (c == '-' &&
//							    (tokens.get(tokens.size()-1).equals("(") ||
//								    tokens.get(tokens.size()-1).equals("=") ||
//								    isOperator(tokens.get(tokens.size()-1)))) {
//							// use '_' as unary - in token stream
//							tokens.add(new String(new char[]{'_'}));
//
//						    } else {
//							tokens.add(new String(new char[]{c}));
//						    }
//
//						} else {
//						    cleanEquation = cleanEquation + clean;
//						    tokens.add(clean);
//						}
//					    }
//					    token = "";
//
//					}
//					// need to do something with the terminating character
//
//				} else {
//					if (cPos < chars.length-1)
//					token = token + c;
//				}
//			}
//
//
//		}
//		
//		// what if this is a lookup function or references one? (identify this by no "=" sign;
//		
//		// if lhs contains a '(' -> a lookup definition
//		
//		// contains a lookup reference -> form: abc()  ->  where abc is not a function! but
//		// need to know that we are in the state to find a function or lookup
//		// states: looking at a terminator or var/number
//		
//		// restore logical OR java style
//		equation = equation.replace("`", "||");
//		cleanEquation = cleanEquation.replace("`", "||");
//		for (String t : tokens)
//		    t = t.replace("`", "||");
//		
//		if (cleanEquation.contains("INTEG") && cleanEquation.contains(","))
//			hasInitialValue = true;
//		
//		if (cleanEquation.contains("IFTHEN") && cleanEquation.contains(">"))
//		    printTokens();
//	}
	
	private boolean requiresLHS(String func) {
		if (func.equals("LOOKUP)"))
			return false;
		else
			return true;
	}
	
	public static boolean isSeparator(String token) {
		if (token.trim().length() == 0)
			return true;
		if (token.equals(")") ||
				token.equals("(") ||  
				token.equals("[") ||
				token.equals("]"))
			return true;
		else
			return false;
	}
	
	private String clean(String s) {
		return s.replace(" ", "")
			.replace(":", "_")
			.replace("\t", "")
			.replace("&", "_")
			.replace("-", "_")
			.replace("\"", "")
			.replace("(", "_")
			.replace(")", "_")
			.replace("'", "");
	}
	

	
	public static  boolean isTerminator(char c) {
	    if (terminatorSet.contains(c))
		return true;
	    else
		return false;
	}
	
	public static boolean isGetXlsDataFunction(String s) {
	    return s.equals("GET XLS DATA");
	}
	
	public static boolean isGetXlsLookupsFunction(String s) {
	    return s.equals("GET XLS LOOKUPS");
	}
	
	public static boolean isVectorSortOrder(String s) {
	    return s.equals("VECTOR SORT ORDER");
	}
	
	public static boolean isVectorElmMap(String s) {
	    return s.equals("VECTOR ELM MAP");
	}

//	public static boolean isFunction(String s) {
//	    if (functionSet.contains(s.toUpperCase()))
//		return true;
//	    else
//		return false;
//	}
	
	public static boolean isMacroName(String s) {
	    return MacroManager.isMacroName(s);
	}

	public String getEquation() {
		return equation;
	}

	public void setEquation(String equation) {
		this.equation = equation;
	}

	public String getLhs() {
		return lhs;
	}

	public void setLhs(String lhs) {
		this.lhs = lhs;
	}

	public String getRhs() {
		return rhs;
	}

	public void setRhs(String rhs) {
		this.rhs = rhs;
	}
	
	public void addReferencedBy(String equation) {
		referencedBy.add(equation);
	}
	
	public List<String> getReferences() {
		List<String> refs = new ArrayList<String>();
		
		return refs;
	}

	public String getVensimEquation() {
		return vensimEquation;
	}

	public void setVensimEquation(String vensimEquation) {
		this.vensimEquation = vensimEquation;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getCleanEquation() {
		return cleanEquation;
	}

	public void setCleanEquation(String cleanEquation) {
		this.cleanEquation = cleanEquation;
	}
	
	public HashSet<String> getRHSVariables() {
		return new HashSet<String>(rhsTokens);
	}
	
	public HashSet<String> getRHSVariablesExpanded() {
	    HashSet<String> s = new HashSet<String>();
	    if (rhsTokens == null)
		return s;
		for (String t : rhsTokens) {
		    String tNoBang = t.replaceAll("!", "");
		    if (ArrayReference.isArrayReference(tNoBang)) {
			s.addAll(ArrayManager.expand(new ArrayReference(tNoBang)));
		    } else {
			s.add(t);
		    }
		}
		removeExceptions(s);
		return s;
	}

	public boolean isDefinesLookup() {
		return definesLookup;
	}

	public void setDefinesLookup(boolean definesLookup) {
		this.definesLookup = definesLookup;
	}

	public boolean isReferencesLookup() {
		return referencesLookup;
	}

	public void setReferencesLookup(boolean referencesLookup) {
		this.referencesLookup = referencesLookup;
	}

	public boolean isHasInitialValue() {
		return hasInitialValue;
	}

	public void setHasInitialValue(boolean hasInitialValue) {
		this.hasInitialValue = hasInitialValue;
	}

	public List<String> getTokens() {
	    return tokens;
	}

	public void setTokens(ArrayList<String> tokens) {
	    this.tokens = tokens;
	}
	
	public void printTokens() {
	    System.out.println("EQ: "+getVensimEquation());
	    System.out.println("EQ: "+getCleanEquation());
	    for (String t : getTokens()) {
		System.out.println("   <"+t+">");
	    }
	}
	
	public void printTokensOneLine() {
	    
	    for (String t : getTokens()) {
		System.out.print(" <"+t+">");
	    }
	    System.out.print("\n");
	}
	
	public void printRpn() {
	    System.out.println("EQ: "+getCleanEquation());
	    for (String t : getRpn()) {
		System.out.println("   <"+t+">");
	    }
	}
	
	public void printRpnUnits() {
	    System.out.println("EQ: "+getCleanEquation());
	    for (String t : getRpn()) {
		String u = "";
		if (UnitsManager.hasUnits(t))
		    u = UnitsManager.getUnits(t);
		System.out.println("   <"+t+"> <"+u+">");
	    }
	}
	
	public void generateRpn() {
	    boolean printRPN = false;
	    if (!isAssignment())
		return;
//	    System.out.println(cleanEquation);
	    
	    Stack<String> stack = new Stack<String>();

	    int tPtr = 0;
	    
	    boolean locatedEquals = false;
	    
	    // work with a temporary version of tokens in which arrays and subscripts are combined
	    // into a single token for each occurance
	    
	    int functionsActive = 0;
	    int activeParens = 0;
	    
	    List<String> combinedTokens = new ArrayList<String>(tokens);
	    
	    int j = 0;
	    
	    for (String tokenIn : combinedTokens) {
		
		String token = tokenIn; // .replace("lookup.", "memory.");
		
		if (token.length() == 0) {
		    continue;
		}
//		System.out.println("### "+token);
		// skip the first two tokens (varname and =)
		if (token.equals("=")) {
		    locatedEquals = true;
		    continue;
		}
		if (!locatedEquals)
		    continue;
		if (Parser.isNumber(token) || isVariableReference(token) || isLookupReference(token)) {
		    rpn.add(token);
		} else if (isFunctionInvocation(token) || isMacroInvocation(token)) {
		    functionsActive++;
		    stack.push(token);
		} else if (isFunctionArgumentSeparator(token) && functionsActive > 0) {
		    while (!stack.peek().equals("(")) {
			rpn.add(stack.pop());
		    }
		} else if (Parser.isOperator(token) || Parser.isBooleanOperator(token)) {
		    while (stack.size() > 0 && (Parser.isOperator(stack.peek())|| Parser.isBooleanOperator(stack.peek())) && (
			    (leftAssociative.get(token) && precedence.get(token).intValue() <= precedence.get(stack.peek()).intValue()) ||
			    (!leftAssociative.get(token) && precedence.get(token).intValue() < precedence.get(stack.peek()).intValue())
			    )) {
			rpn.add(stack.pop());
		    }
		    stack.push(token);
		    
		} else if (isLeftParen(token)) {
		    activeParens++;
		    stack.push(token);
		} else if (isRightParen(token)) {
		    try {
			while (!stack.peek().equals("(")) {
			rpn.add(stack.pop());
			}
		    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    stack.pop();
		    if (activeParens == functionsActive)
			functionsActive--; // where does this belong?
		    activeParens--;
		    if (stack.size() > 0 && (isFunctionInvocation(stack.peek()) || isMacroInvocation(stack.peek())) )
			rpn.add(stack.pop());
		}
	    }
	    
	    while (stack.size() > 0)
		rpn.add(stack.pop());
	    
	    ArrayList<String> al = new ArrayList<String>();
	    al.add(combinedTokens.get(0));
	    al.addAll(rpn);
	    al.add("=");
	    rpn = al;
	    
	}
	
	private boolean isLookupReference(String token) {
	    if (token.startsWith("lookup."))
		return true;
	    else
		return false;
	}
	
	private boolean isVariableReference(String token) {
	    if (token.startsWith("\"") || token.startsWith("valueOf") || token.startsWith("memory.") ||
		    token.startsWith("time") || token.startsWith("array.") || token.startsWith("arrayValueOf"))
		return true;
	    else
		return false;
	}
	private boolean isFunctionInvocation(String token) {
	    if (token.startsWith("sdFunction"))
		return true;
	    else
		return false;
	}
	
	private boolean isMacroInvocation(String token) {
	    // always return false as we have already processed
	    return false;
//	    if (MacroManager.isMacroName(token))
//		return true;
//	    else
//		return false;
	}
	
	private boolean isFunctionArgumentSeparator(String token) {
	    if (token.equals(","))
		return true;
	    else
		return false;
	}
	
	private boolean isLeftParen(String token) {
	    if (token.equals("("))
		return true;
	    else
		return false;
	}
	
	private boolean isRightParen(String token) {
	    if (token.equals(")"))
		return true;
	    else
		return false;
	}
	
	
	// MJB 4 Oct 2011 commented these out for some reason the rpn not being generated correctly
//	private boolean isOperator(String token) {
//	    if (token.equals("+") ||
//		    token.equals("-") ||
//		    token.equals("*") ||
//		    token.equals("/") ||
//		    token.equals("_") ||
//		    token.equals("^") ||
//		    token.equals(":OR:") ||
//		    token.equals(":AND:") ||
//		    token.equals(":NOT:") ||
//		    token.equals("==") ||
//		    token.equals("=")
//	    )
//		return true;
//	    else
//		return false;
//	}
	
//	private  boolean isBooleanOperator(String token) {
//	    if (token.equals(":AND:"))
//		System.out.println(":AND:");
//		if (token.equals("==") ||
//				token.equals("<>") ||
//				token.equals(">") ||
//				token.equals(">=") ||
//				token.equals("<") ||
//				token.equals("<=") ||
//				token.equals(":OR:") ||
//				token.equals(":NOT:") ||
//				token.equals(":AND:")
//		)
//			return true;
//		else
//			return false;
//	}
	

	public List<String> getRpn() {
	    return rpn;
	}

	public void setRpn(ArrayList<String> rpn) {
	    this.rpn = rpn;
	}

	public boolean isAssignment() {
	    return assignment;
	}

	public void setAssignment(boolean assignment) {
	    this.assignment = assignment;
	}
	
	
	public void generateTree() {
	    treeRoot = generateEquationTree();
	    validateIFTHENELSEInTree(treeRoot);
	}
	
	public Node generateEquationTree() {
	    boolean printit = false;
	    
	    System.out.println("TREE: "+vensimEquation);
	    this.printRpn();

	    Node start = null;
	    Node end = null;

	    int nPtr = 0;

	    for (String token : rpn) {
//		System.out.println("Token: "+token);
		if (start!= null && printit) {
//		    this.printTokens();
//		    this.printRpn();
		    printTree(start);
		    
		}

		Node aNode = new Node(rpn.get(nPtr));
		if (nPtr == 0) {
		    start = aNode;
		    end = aNode;
		} else if (Parser.isOperator(token) || Parser.isBooleanOperator(token))  {
			
		    Node p1;
		    Node p2;
		    Node p3;
		    
		 // if binary take 2
		   
			if (Parser.isBinaryOperator(token)) {

			p1 = end;
			p2 = end.getPrevious();
			if (p2 == null) {
			    System.out.println("HERE!");
			}
			p3 = end.getPrevious().getPrevious();
			
			// make required changes to structure
			aNode.setChild(p2);
			aNode.setPrevious(p3);
			if (p2 != null) {
			    p2.setPrevious(null);
			    p2.setParent(aNode);
			    p2.setNext(p1);
			}
			p1.setParent(aNode);
			end = aNode;

			if (p3 != null) {
			    p3.setNext(aNode);
			    aNode.setPrevious(p3);
			} else {
			    start = aNode;
			}
		    } else {
			// unary operator
			p1 = end;
			p2 = end.getPrevious();
			
			aNode.setChild(p1);
			aNode.setPrevious(p2);
			if (p2 != null) {
			p2.setNext(aNode);
			} else {
			    start = aNode;
			}
			if (p1 != null) {
			    p1.setPrevious(null);
			    p1.setParent(aNode);
			    p1.setNext(null);
			   
			}
			p1.setParent(aNode);
			end = aNode;
		    } 
		    
		} else if (isFunctionInvocation(token)) {
		 // if a functionInvocation
//		    int numArgs = numberArguments.get(getFunctionName(token).toUpperCase());
		    FunctionDescription fd = FunctionManager.getDescription(token); 
		    if (fd == null)
			System.out.println("Null Description");
		    
		    // need to worry about functions that have no arguments
		    
		    int numArgs = fd.getNumArgsAll();
		    
		    if (numArgs > 0) {

			Node[] args = new Node[numArgs];
			args[numArgs-1] = end;
			for (int i = numArgs-2; i >= 0; i--) {
			    args[i] = end.getPrevious();
			    end = end.getPrevious();
			}

			if (end != null)
			    end = end.getPrevious();

			if (end != null) {
			    aNode.setPrevious(end);
			    end.setNext(aNode);
			} else {
			    start = aNode;
			}
			end = aNode;
			for (int i = 0; i < numArgs; i++) {
			    args[i].setParent(aNode);
			    //			System.out.println("    Funtion arg "+i+" "+args[i].getToken());
			}
			aNode.setChild(args[0]);
			args[0].setPrevious(null);
		    } else {
			// if there are no args, add to end
			if (end != null) {
			    aNode.setPrevious(end);
			    end.setNext(aNode);
			} else {
			    start = aNode;
			}
			end = aNode;
			
		    }
		} else if (isMacroInvocation(token)) {
			 // if a macro Invocation
			    int numArgs = MacroManager.getNumArgumentsFor(token);
			    
			    Node[] args = new Node[numArgs];
			    args[numArgs-1] = end;
			    for (int i = numArgs-2; i >= 0; i--) {
				args[i] = end.getPrevious();
				end = end.getPrevious();
			    }
			    
			    if (end != null)
				end = end.getPrevious();
			    
			    if (end != null) {
				aNode.setPrevious(end);
				end.setNext(aNode);
			    } else {
				start = aNode;
			    }
			    end = aNode;
			    for (int i = 0; i < numArgs; i++) {
				args[i].setParent(aNode);
			    }
			    aNode.setChild(args[0]);
			    args[0].setPrevious(null);
		} else {
		    end.setNext(aNode);
		    aNode.setPrevious(end);
		    end = aNode;
		}
		
//		printTreeLevel1(start);
		nPtr++;
	    }
		if (start!= null && printit) {
		    this.printTokens();
		    this.printRpn();
		    printTree(start);
		    
		}
	    printit = false;
	    
	    // catch array initializtion?

	    if (start == null) {
//		System.out.println("What is this with no tree?");
	    } else {
		if (!Parser.isOperator(start.getToken())) {
//		    System.out.println("HERE");
		    
		    // if end is "=", promote it to root and make start it's child
		    if (end.getToken().equals("=")) {
			arrayInitialization = true;
//			end.setChild(start);
//			end.setNext(null);
//			start.setParent(end);
//			start = end;
		    }
		    
		}
	    }

	    return start;

	}
	
	public void printTreeLevel1(Node node) {
	    if (node == null) {
		System.out.println("End of List");
		return;
	    }
	    System.out.println("NODE: "+node.getToken());
	    printTreeLevel1(node.getNext());
	}
	
	private String getFunctionName(String token) {
	    return token.replace("sdFunctions.", "");
	}
	
	public void printTree(Node node) {
	    System.out.println(cleanEquation);
	    printTree(node, 0);
	}
	
	public void printTree() {
	    System.out.println(cleanEquation);
	    printTree(treeRoot, 0);
	}
	
	private void printTree(Node node, int level) {
	    if (node == null)
		return;
	    String blanks="";
	    for (int i = 0; i < level; i++)
		blanks += "    ";
	    System.out.println(blanks+"Level "+level + " token "+node.getToken() + "\n"+
		    blanks+"   parent: "+(node.getParent() != null ? node.getParent().getToken() : "NULL") +
		    "\n"+blanks+"   next: " +(node.getNext() != null ? node.getNext().getToken() : "NULL"));
	    printTree(node.getChild(), level+1);
	    printTree(node.getNext(), level);
	}
	
	public void printTreeTerse() {
	    System.out.println(cleanEquation);
	    printTreeTerse(treeRoot, 0);
	}
	
	public static void printTreeCausal(Node node, int level, Map<String, Equation> equations) {
	    if (node == null)
		return;
	    String blanks="";
	    for (int i = 0; i < level; i++)
		blanks += "    ";
	    if (node.getToken() == null) {
		System.out.println(blanks+"Level "+level + " token "+node.getToken()+"????? NULL TOKEN");
	    } else if (equations.get(node.getToken()) == null) {
		System.out.println(blanks+"Level "+level + " token "+node.getToken()+"????? NULL EQUATION");
	    } else {
		System.out.println(blanks+"Level "+level + " token "+node.getToken()+(equations.get(node.getToken()).isStock() ? "<STOCK>" : ""));
	    }
	    printTreeCausal(node.getChild(), level+1, equations);
	    printTreeCausal(node.getNext(), level, equations);
	}
	
	private void printTreeTerse(Node node, int level) {
	    if (node == null)
		return;
	    String blanks="";
	    for (int i = 0; i < level; i++)
		blanks += "    ";
	    System.out.println(blanks+"Level "+level + " token "+node.getToken() + "\n");
	    printTreeTerse(node.getChild(), level+1);
	    printTreeTerse(node.getNext(), level);
	}
	
	public void printTreeCode() {
	    System.out.println("######################################################");
	    System.out.println(cleanEquation);
	    printTreeCode(treeRoot, 0);
	}
	
	private void printTreeCode(Node node, int level) {
	    if (node == null)
		return;
	    String blanks="";
	    for (int i = 0; i < level; i++)
		blanks += "    ";
	    System.out.println(blanks+"Level "+level + "\n"+node.getInfo(blanks));
	    printTreeCode(node.getChild(), level+1);
	    printTreeCode(node.getNext(), level);
	}
	
	public List<String> getEquationUnits() {
	    List<String> eqn = new ArrayList<String>();
//	    System.out.println("=====================================================");
//	    System.out.println(cleanEquation);
	    printTreeCodeUnits(eqn, treeRoot, 0);
	    // need to get rid of first and last entries in eqn (uneeded parens)
	    if (eqn.size() > 1) {
		eqn.remove(eqn.size()-1);
		eqn.remove(0);
	    }
	    return eqn;
	}
	
	private void printTreeCodeUnits(List<String> eqn, Node node, int level) {
	    if (node == null)
		return;
	    if (node.isTerminal()) {
		if (Parser.isNumber(node.getToken())) {
//		    System.out.println(node.getToken()+"<"+"constant"+">");
		    eqn.add("constant"+"#"+node.getToken());
		} else {
//		    System.out.println(node.getToken()+"<"+UnitsManager.getUnits(node.getToken())+">");
		    String v = UnitsManager.getUnits(node.getToken());
//		    if (v == null)
//			System.out.println("V is null");
		    eqn.add(UnitsManager.getUnits(node.getToken()));
		}
	    }
	    
	    if (Parser.isOperator(node.getToken())) {
		Node leftChild = node.getChild();
		Node rightChild = leftChild != null ? leftChild.getNext() : null;
		eqn.add("(");
		printTreeCodeUnits(eqn, leftChild, level+1);
//		System.out.println(level+" "+node.getToken()+"<"+node.getUnits()+">");
		eqn.add(node.getToken());
//		System.out.println(node.getToken());
		printTreeCodeUnits(eqn, rightChild, level+1);
		eqn.add(")");
	    }
	    
	    if (Parser.isFunctionInvocation(node.getToken())) {
		eqn.add(node.getToken()+"<"+UnitsManager.getUnits(node.getToken())+">");
		eqn.add("(");
//		System.out.println(node.getToken()+"<"+UnitsManager.getUnits(node.getToken())+">");
		Node child = node.getChild();
		int c = 0;
		while (child != null) {
		    if (c++ > 0)
			eqn.add(",");
		    printTreeCodeUnits(eqn, child, level+1);
		    child = child.getNext();
		}
		eqn.add(")");
	    }
	}
	
	
	
	public ArrayList<String> getFunctionInitialVariables() {
	    ArrayList<String> al = new ArrayList<String>();
	    getFunctionInitialVariables(treeRoot, al);
	    return al;
	}
	
	public void getFunctionInitialVariables(Node node, ArrayList<String> al) {
	    if (node == null)
		return;
	    
	    if (node.getToken().equals("sdFunctions.INTEG") ||
		    node.getToken().equals("sdFunctions.SMOOTHI") || 
		    node.getToken().equals("sdFunctions.ACTIVEINITIAL")) {
		Node child = node.getChild();
		while (child.getNext() != null)
		    child = child.getNext();
		al.add(child.getToken());
	    }
	    
	    getFunctionInitialVariables(node.getNext(), al);
	    getFunctionInitialVariables(node.getChild(), al);
	    
	    
	    
	    return;
	    
	}
	
	public Node getCopyOfTree() {
	    return generateEquationTree();
	}

	public Node getTreeRoot() {
	    return treeRoot;
	}

	public void setTreeRoot(Node treeRoot) {
	    this.treeRoot = treeRoot;
	}

	public boolean isDefinesSubscript() {
	    return definesSubscript;
	}

	public void setDefinesSubscript(boolean definesSubscript) {
	    this.definesSubscript = definesSubscript;
	}

	public boolean isHasLHSArrayReference() {
	    return hasLHSArrayReference;
	}

	public void setHasLHSArrayReference(boolean hasLHSArrayReference) {
	    this.hasLHSArrayReference = hasLHSArrayReference;
	}

	public boolean isHasRHSArrayReference() {
	    return hasRHSArrayReference;
	}

	public void setHasRHSArrayReference(boolean hasRHSArrayReference) {
	    this.hasRHSArrayReference = hasRHSArrayReference;
	}

	public boolean isHasMultipleEquations() {
	    return hasMultipleEquations;
	}

	public void setHasMultipleEquations(boolean hasMultipleEquations) {
	    this.hasMultipleEquations = hasMultipleEquations;
	}
	
	public String getUnitsAndComment() {
	    StringBuffer sb = new StringBuffer();
	    sb.append("/*\n");
	    sb.append("\tEquation: "+equation+"\n\n");
	    if (units != null || comment != null) {
		sb.append("\tUnits:"+(units == null ? "None Provided" : units)+"\n\n");
		sb.append("\tComment: "+(comment == null ? "None Provided" : comment)+"\n\n");
	    } 
	    sb.append("*/\n");
		return sb.toString();
	}

	public boolean isOneTime() {
	    return oneTime;
	}

	public void setOneTime(boolean oneTime) {
	    this.oneTime = oneTime;
	}
	
	public boolean isRepeated() {
	    return !isOneTime();
	}
	
	private String getNextArray() {
	    String next = "_a" + nextArray++;
	    return next;
	}
	private String getNextInt() {
	    String next = "_i" + nextInt++;
	    return next;
	}
//	private String getNextInteger() {
//	    String next = "_I" + nextInt++;
//	    return next;
//	}
	
	private String getNextTimeSeries() {
	    String next = "_ts" + nextTimeSeries++;
	    return next;
	}

	public boolean isHasException() {
	    return hasException;
	}

	public void setHasException(boolean hasException) {
	    this.hasException = hasException;
	}

	public int getLeftBracketCount() {
	    return leftBracketCount;
	}

	public void setLeftBracketCount(int leftBracketCount) {
	    this.leftBracketCount = leftBracketCount;
	}

	public List<String> getExceptions() {
	    
	    // Note that we expand the subscript to terminal values
	    
	    List<String> al = new ArrayList<String>();
	    for (String subscript : exceptions)
		al.addAll(NamedSubscriptManager.getValuesFor(subscript));
	    return al;
	}
	
	public boolean getsExternalData() {
	    if (equation.contains("VDMLOOKUP") || 
		    equation.contains("GET XLS DATA") ||
		    equation.contains("GET XLS LOOKUP") ||
		    equation.contains("GET XLS CONSTANT"))
		return true;
	    else
		return false;
	}

	public boolean isVdmLookup() {
	    return vdmLookup;
	}

	public void setVdmLookup(boolean vdmLookup) {
	    this.vdmLookup = vdmLookup;
	}
	
	public String toString() {
	    return vensimEquation;
	}
	
//	public void convertToNativeDataTypes() {
////	    if (vensimEquation.startsWith("Production Capacity")) {
////		System.out.println("Problem");
////	    }
//	    List<String> nativeTypes = new ArrayList<String>();
//	    for (String token : tokens) {
//		if (token.startsWith("valueOf")) {
//		    String tmp = token.replace("valueOf(\"", "");
//		    tmp = tmp.replace("\")", "");
//		    nativeTypes.add(NativeDataTypeManager.getLegalName(tmp));
//		} else if (token.startsWith("arrayValueOf")) {
//		    // rip appart the invocation
//		    // arrayValueOf("Production Capacity",concatAsSubscript("Production Mode","Country"))
//		    // becomes
//		    // Memory.Production_Capacity[Production_Mode][Country]
//		    String tmp = token.replace("arrayValueOf(", "");
//		    String[] t = tmp.split(",", 2);
//		    StringBuffer sb = new StringBuffer();
//		    sb.append(NativeDataTypeManager.getLegalName(t[0].replace("\"", "")));
//		    sb.append(t[1].replace("concatAsSubscript(\"","[").replace("\",\"",	"][").replace("\"", "]").replace("))", ""));
//		    nativeTypes.add(sb.toString());
//		} else if (token.startsWith("array.")) {
//		    // rip appart the invocation
//		    // array.Initial Production Capacity[Production Mode,Country]
//		    // becomes
//		    // Memory.Initial_Production_Capacity[Production_Mode][Country]
//		    ArrayReference ar = new ArrayReference(token);
//		    StringBuffer sb = new StringBuffer();
//		    sb.append(NativeDataTypeManager.getLegalName(ar.getArrayName()));
//		    for (String subr : ar.getSubscripts())
//			sb.append("["+subr+"]");
//		    nativeTypes.add(sb.toString());
//		} else {
//		    nativeTypes.add(token);
//		}
//	    }
//	    
//	    tokens = nativeTypes;
//	    
//	}
	
	public List<Node> getTreeAsList() {
	    if (treeRoot == null) {
//		generateRpn();
//		generateTree();
		return null;
	    }
	    List<Node> al = new ArrayList<Node>();

	    Node lhs = treeRoot.getChild();
	    
	    // hack!
	    // need to worry about array initialization, since its syntax violates normal
	    // binary operator use ("=")
	    
	    if (arrayInitialization) {
		traverse(treeRoot, al);
		al.remove(al.size()-1);
	    } else {

	    al.add(lhs);
	    	traverse(lhs.getNext(), al);
	    }

	    return al;
	}
	
	public void traverse(Node node, List<Node> al) {
	     if (node == null)
		 return;
	     al.add(node);
	     traverse(node.getChild(), al);
	     traverse(node.getNext(), al);
	   
	    
	}

	public EquationArrayReferenceStructure getEars() {
//	    if (ears == null)
		createEars();
	    return ears;
	}

	public void setEars(EquationArrayReferenceStructure ears) {
	    this.ears = ears;
	}

	public boolean isArrayInitialization() {
	    return arrayInitialization;
	}

	public void setArrayInitialization(boolean arrayInitialization) {
	    this.arrayInitialization = arrayInitialization;
	}

	public boolean isUsesTimeSeries() {
	    return usesTimeSeries;
	}

	public void setUsesTimeSeries(boolean usesTimeSeries) {
	    this.usesTimeSeries = usesTimeSeries;
	}
	
	public boolean isHasMacroInvocation() {
	    return hasMacroInvocation;
	}

	public void setHasMacroInvocation(boolean hasMacroInvocation) {
	    this.hasMacroInvocation = hasMacroInvocation;
	}

	public boolean isOrderedWithInitialValue() {
	    return orderedWithInitialValue;
	}

	public void setOrderedWithInitialValue(boolean orderedWithInitialValue) {
	    this.orderedWithInitialValue = orderedWithInitialValue;
	}

	public boolean isHasVectorSortOrder() {
	    return hasVectorSortOrder;
	}

	public void setHasVectorSortOrder(boolean hasVectorSortOrder) {
	    this.hasVectorSortOrder = hasVectorSortOrder;
	}

	public boolean isHasVectorElmMap() {
	    return hasVectorElmMap;
	}

	public void setHasVectorElmMap(boolean hasVectorElmMap) {
	    this.hasVectorElmMap = hasVectorElmMap;
	}
	
	public boolean requiresPostGenerationProcessing() {
	    return hasVectorElmMap || hasVectorSortOrder;
	}

	public String getBtwnMode() {
	    return btwnMode;
	}

	public void setBtwnMode(String btwnMode) {
	    this.btwnMode = btwnMode;
	}

	public boolean isDefinesLookupGetXls() {
	    
	    return vensimEquation.toUpperCase().contains("GET XLS LOOKUPS");
//	    return definesLookupGetXls;
	}
	
	public boolean isStock() {
	    return stockVariable;
	}
}
