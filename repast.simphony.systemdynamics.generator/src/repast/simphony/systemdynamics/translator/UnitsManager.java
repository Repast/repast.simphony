package repast.simphony.systemdynamics.translator;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.systemdynamics.support.ArrayReference;
import repast.simphony.systemdynamics.support.MutableInteger;

public class UnitsManager {
    
    private static Map<String, String> lhsUnits = new HashMap<String, String>();
    private static Map<String, String> lhsUnitsRaw = new HashMap<String, String>();
    private static Map<String, String> unitsEquivalence = new HashMap<String, String>();
    private static UnitConsistencyXMLWriter unitConsistencyXMLWriter;
    
    public UnitsManager() {
	
    }
    
    public static void performUnitsConsistencyCheck(Map<String, Equation> equations, String file) {
	List<String> evaluationOrder = new ArrayList<String>();
	for (String key : equations.keySet()) {
	    evaluationOrder.add(key);
	}
	performUnitsConsistencyCheck(evaluationOrder, equations, file);
	
    }
    
    public static void performUnitsConsistencyCheck(List<String> evaluationOrder, Map<String, Equation> equations, String file) {
	unitConsistencyXMLWriter = new UnitConsistencyXMLWriter();
	int equationCount = 0;
	for (String lhs : evaluationOrder) {

	    Equation eqn = equations.get(lhs);
	    if (!eqn.isAssignment())
		continue;
	    
	    if (!FunctionManager.canCheckUnitsConsistency(eqn.getVensimEquation())) {
		continue;
	    }
	    
	    equationCount++;
	    System.out.println("===================================");
	    
	    List<String> units = eqn.getEquationUnits();

	    if (!eqn.isArrayInitialization() && !isConsistent(eqn, units)) {
		System.out.println("INCONSISTENT UNITS: "+eqn.getCleanEquation());
	    } else {
		System.out.println("!!!YES!!! CONSISTENT UNITS: "+eqn.getCleanEquation());
	    }
	}
	unitConsistencyXMLWriter.setEquationCount(equationCount);
	unitConsistencyXMLWriter.write(file);
	unitConsistencyXMLWriter.writeReport(file.replace(".xml", ".txt"));
    }
    
    public static void addEquivalence(String alternateUnit, String effectiveUnit) {
	unitsEquivalence.put(alternateUnit, effectiveUnit);
    }
    
    public static String getEffectiveUnits(String units) {
	if (unitsEquivalence.containsKey(units))
	    return unitsEquivalence.get(units);
	else
	    return units;
    }
    
    public static String getUnits(String lhs) {
	
	String units = lhsUnits.get(cleanseLHS(lhs));
	return getEffectiveUnits(units);
    }
    
    public static void addLhsUnits(String lhs, String units) {
//	if (lhs.startsWith("array."))
//	    System.out.println("Proc array");
	lhsUnitsRaw.put(lhs, units);
	lhsUnits.put(cleanseLHS(lhs), cleanseUnits(units));
    }
    
    public static boolean hasUnits(String lhs) {
	return lhsUnits.containsKey(cleanseLHS(lhs));
    }
    
    public static String cleanseLHS(String lhs) {
	String lhside = lhs;
	
	if (ArrayReference.isArrayReference(lhs)) {
	    lhside = new ArrayReference(lhs).getArrayName();
	}
	
	lhside = NativeDataTypeManager.getOriginalName(lhside);
	
	lhside = lhside.replace("memory.", "").replace("lookup.", "").replace("sdFunctions.", "").replace("array.", "");
	
	return lhside.replace("memory.", "").replace("lookup.", "").replace("sdFunctions.", "");
    }
    public static String cleanseUnits(String units) {
	if (units == null)
	    return null;
	String u = units;
	
	if (units.contains("[")) {
	    u = units.split("\\[")[0].trim();
	}
	
	return u;
    }
    
    public static void dumpLhsUnits(BufferedWriter bw) {
	List<String> lhs = new ArrayList<String>();
	lhs.addAll(lhsUnits.keySet());
	Collections.sort(lhs);
	for (String l : lhs) {
	    try {
		bw.append("\"");
		bw.append(l);
		bw.append("\"");
		bw.append(",");
		bw.append("\"");
		bw.append(lhsUnits.get(l));
		bw.append("\"");
		bw.append("\n");
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	try {
	    bw.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public static void dumpLhsUnitsRaw(BufferedWriter bw) {
	List<String> lhs = new ArrayList<String>();
	lhs.addAll(lhsUnitsRaw.keySet());
	Collections.sort(lhs);
	for (String l : lhs) {
	    try {
		bw.append("\"");
		bw.append(l);
		bw.append("\"");
		bw.append(",");
		bw.append("\"");
		bw.append(lhsUnitsRaw.get(l));
		bw.append("\"");
		bw.append("\n");
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	try {
	    bw.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public static boolean isConsistent(Equation eqn, List<String> units) {
	
	List<String> unitsExpanded = expandUnits(units);
	unitsExpanded = addFunctionUnits(unitsExpanded);
	eqn.printTokensOneLine();
//	printUnits(units);
//	System.out.println("++++++++++++++++++++++++++++++++");
	boolean isValid = valid(unitsExpanded, eqn);
	if (!isValid)
	    printUnitsIndented(unitsExpanded);
	return isValid;
    }
    
    private static boolean valid(List<String> units, Equation eqn) {
	// stack based evaluation of units consistency
	if (units.size() < 3) {
	    return false;
	}
	
	// by definition:	
	String lhs = units.get(0);
	
	if (lhs == null)
	    return false;
	String equal = units.get(1);
	List<String> rhs = new ArrayList<String>(units);
	rhs.remove(0);
	rhs.remove(0);
	UnitExpression unitExpression = new UnitExpression(lhs, equal, rhs, eqn);
	return unitExpression.isValid();
    }
    
    private static List<String> addFunctionUnits(List<String> units) {
	List<String> unitsExpanded = new ArrayList<String>();
	MutableInteger i = new MutableInteger(0);
	while (i.value() < units.size()) {
	    String s = units.get(i.value());
	    if (Parser.isFunctionInvocation(s)) {
		s = s.split("<")[0];
		FunctionDescription fd = FunctionManager.getDescription(s);
		if (fd == null)
		    unitsExpanded.add(s+"<NULL>");
		else
		    unitsExpanded.add(s+"<"+fd.getReturnUnits()+">");
	    } else {
		unitsExpanded.add(s);
	    }
	    i.add(1);
	}
	return unitsExpanded;
    }
    
    private static List<String> expandUnits(List<String> units) {
	List<String> unitsExpanded = new ArrayList<String>();
	MutableInteger i = new MutableInteger(0);
	while (i.value() < units.size()) {
	    String s = units.get(i.value());
	    if (Parser.isFunctionInvocation(s)) {
		s = s.split("<")[0];
		i.add(1);
		FunctionDescription fd = FunctionManager.getDescription(s);
		unitsExpanded.addAll(expandFunctionUnits(units, i, s));
		
		// should be pointing to the next argument
		for (int p = 0; p < fd.getNumArgs(); p++) {
		    if (i.value() >= units.size())
			System.out.println("WTF!");
		    String s1 = units.get(i.value());
		    
		    // if null, there is no dimension for this thing
		    // set to "dmnl"
		    
		    if (s1 == null)
			s1 = "dmnl";
		    
		    if (Parser.isFunctionInvocation(s1)) {
			s1 = s1.split("<")[0];
			i.add(1);
			FunctionDescription fd1 = FunctionManager.getDescription(s1);
			unitsExpanded.addAll(expandFunctionUnits(units, i, s1));
		    } else {
			unitsExpanded.add(s1);
			i.add(1);
		    }
		}
		
	    } else {
		unitsExpanded.add(s);
		i.add(1);
	    }
	}
	
	return unitsExpanded;
    }
    
    private static List<String> expandFunctionUnits(List<String> units, MutableInteger i, String functionName) {
	List<String> unitsExpanded = new ArrayList<String>();
	FunctionDescription fd = FunctionManager.getDescription(functionName);
	if (fd == null)
	    System.out.println("fd is null for "+functionName);
	unitsExpanded.add(functionName);  // function name
	unitsExpanded.add(units.get(i.valueAndInc()));   // left paren
	if (fd.isRequiresName()) {
//	    unitsExpanded.add("NA");
	    i.add(1);
//	    unitsExpanded.add(units.get(i.valueAndInc()));  // separator
	    i.add(1);
	} 
	if (fd.isRequiresValue()) {
//	    unitsExpanded.add(units.get(i.valueAndInc()));
//	    unitsExpanded.add(units.get(i.valueAndInc()));  // separator
	    i.add(2);
	}
	if (fd.isRequiresTime()) {
//	    unitsExpanded.add(Translator.UNITS_PROPERTIES.getProperty("timeUnits"));
	    i.add(1);
//	    unitsExpanded.add(units.get(i.valueAndInc()));  // separator
	    i.add(1);
	}
	if (fd.isRequiresTimeStep()) {
//	    unitsExpanded.add(Translator.UNITS_PROPERTIES.getProperty("timeUnits"));
	    i.add(1);
//	    unitsExpanded.add(units.get(i.valueAndInc()));  // separator
	    i.add(1);
	}
	
	return unitsExpanded;
    }
    
    private static void printUnits(List<String> units) {
	for (String s : units) {
	    System.out.print(" \""+s+"\"");
	}
	System.out.print("\n");
    }
    
    private static void printUnitsIndented(List<String> units) {
	
	int indent = 0;
	String indentation = getIndentation(indent);
	for (String s : units) {
	    if (s == null)
		    s = "null";
	    if (s.equals(")")) {
		indent--;
	    	indentation = getIndentation(indent);
	    }
	    System.out.println(indentation+"\""+s+"\"");
	    if (s.equals("(")) {
		indent++;
	    	indentation = getIndentation(indent);
	    }
	}
	
    }
    
    private static String getIndentation(int indent) {
	StringBuffer sb = new StringBuffer();
	
	for (int i = 0; i < indent; i++)
	    sb.append("   ");
	
	return sb.toString();
	
	
	
    }

    public static UnitConsistencyXMLWriter getUnitConsistencyXMLWriter() {
        return unitConsistencyXMLWriter;
    }

}
