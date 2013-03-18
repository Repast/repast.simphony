package repast.simphony.systemdynamics.translator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import repast.simphony.systemdynamics.support.MutableInteger;

public class SimplifiedUnit {
    
    private static final int NUMERATOR = 1;
    private static final int DENOMINATOR = 2;
    
    /*
     * Some sample Units:
     * 
     * percent
     * watt/meter/meter  -> watt/meter * 1/meter = watt/(meter*meter)
     * watt/(meter*meter) -> watt/(meter*meter)
     * mm/(year*DegreesC)
     * watt*year/DegreesC/(meter*meter) -> (watt*year)/DegreesC * 1/(meter*meter) = (watt*year)/(DegreesC*(meter*meter))
     * (watt/meter/meter)/DegreesC -> (watt/meter*1/meter)/DegreesC -> watt/(meter*meter) * 1/DegreesC = watt/((meter*meter)*DegreesC)
     * 
     */
    
    // Note that a unit of dmnl is represented by empty numerator and denominator hashmaps
    
    private String originalUnit;
    private Map<String, MutableInteger> numerator;
    private Map<String, MutableInteger> denominator;
    private String simplified;
    private int destination = NUMERATOR;
    
//    private UnitExpression unitExpression;
    
    public SimplifiedUnit(SimplifiedUnit other) {
	this.originalUnit = other.originalUnit;
	numerator = copyOf(other.getNumerator());
	denominator = copyOf(other.getDenominator());
	
//	simplify();
	reduce();
//	print();
    }
    
    public SimplifiedUnit(String originalUnit) {
	this.originalUnit = originalUnit;
	numerator = new HashMap<String, MutableInteger>();
	denominator = new HashMap<String, MutableInteger>();
	simplify();
	reduce();
//	print();
    }
    
    private void simplify() {
	// will need to parse the unit into a numerator and a demoninator
	// put appropriate values into hashmaps
	// () for grouping
	// * for multiply
	// / for division division should map into mult 1/unit
	// if denom is empty, there was none
	
	int parenCount = 0;
	Stack<Integer> stack = new Stack<Integer>();
	
	List<String> tokens = Parser.tokenize(originalUnit);
	for (String token : tokens) {
	    if (token.equals("(")) {
		parenCount++;
		stack.push(destination);
	    } else if (token.equals(")")) {
		parenCount--;
		destination = stack.pop();
	    } else if (token.equals("*")) {
		
	    } else if (token.equals("/")) {
		destination = DENOMINATOR;
	    } else {
		// this is some unit
		if (destination == NUMERATOR)
		    addToNumerator(token);
		else
		    addToDenominator(token);
	    }   
	}
    }
    
    public void print() {
	System.out.println("NUMERATOR:");
	print(numerator);
	System.out.println("DENOMINATOR:");
	print(denominator);
	System.out.println(" >> "+formUnit()+" <<");
	
    }
    
    public String formUnit() {
	StringBuffer sb = new StringBuffer();
	boolean hasDenom = hasDenominator();

//	if (hasDenom)
	    sb.append("(");

	sb.append(formUnit(numerator));

	if (hasDenom) {
	    sb.append("/");

	    sb.append(formUnit(denominator));

	    
	}
	sb.append(")");

	return sb.toString();
    }
    
    public String formUnit(Map<String, MutableInteger> aMap) {
	StringBuffer sb = new StringBuffer();
//	sb.append("(");
	int j = 0;
	for (String key : aMap.keySet()) {
	    MutableInteger i = new MutableInteger(aMap.get(key));
	    while (i.value() > 0) {
		if (j++ > 0)
		    sb.append("*");
		
		if (isConstant(key))
		    sb.append("constant");
		else
			sb.append(key);
		i.add(-1);
	    }
	}
//	sb.append(")");
	return sb.toString();
    }
    
    private boolean hasDenominator() {
	if (denominator.size() == 0)
	    return false;
	boolean hasDenom = false;
	for (String key : denominator.keySet()) {
	    if (!key.equals("dmnl") &&
		    !isConstant(key) && 
		    !key.equals("1") &&
		    !key.equals("fraction"))
		hasDenom = true;
	}
	
	return hasDenom;
    }
    
    private boolean isConstant(String key) {
	if (key.startsWith("constant#"))
	    return true;
	else
	    return false;
    }
    
    private int getConstantAsInt(String key) {
	return Integer.parseInt(key.split("#")[1]);
    }
    
    private String getConstantAsString(String key) {
	return key.split("#")[1];
    }
    
    private String getConstantAsString(Map<String, MutableInteger> map) {
	String constant = null;
	for (String key : map.keySet()) {
	    if (isConstant(key))
		return getConstantAsString(key);
	}
	return constant;
    }
    
    private String getConstantKey(Map<String, MutableInteger> map) {
	String constant = null;
	for (String key : map.keySet()) {
	    if (isConstant(key))
		return key;
	}
	return constant;
    }
    
    private double getConstantAsDouble(String key) {
	return Double.parseDouble(key.split("#")[1]);
    }
    
    private boolean containsConstant(Map<String, MutableInteger> map) {
	for (String key : map.keySet()) {
	    if (isConstant(key))
		return true;
	}
	return false;
    }
    
    private void removeConstant(Map<String, MutableInteger> map) {
	String keyToRemove = null;
	for (String key : map.keySet()) {
	    if (isConstant(key)) {
		keyToRemove = key;
		break;
	    }
	}
	if (keyToRemove != null)
	    map.remove(keyToRemove);
	return;
    }
    
    private void removeEquivalent(Map<String, MutableInteger> map) {
	String keyToRemove = null;
	for (String key : map.keySet()) {
	    if (isMultiMatch(key) || key.equals("dmnl")) {
		keyToRemove = key;
	    }
	}
	if (keyToRemove != null)
	    map.remove(keyToRemove);
	return;
    }
    
    private void print(Map<String, MutableInteger> map) {
	for (String key : map.keySet()) {
	    System.out.println(key+" "+map.get(key).value());
	}
    }
    
    private void addToNumerator(String tokenIn) {
	String token = UnitsManager.getEffectiveUnits(tokenIn);
	if (numerator.containsKey(token)) {
	    MutableInteger mi = numerator.get(token);
	    mi.add(1);
	} else {
	    numerator.put(token, new MutableInteger(1));
	}
	
    }
    
    private void addToDenominator(String tokenIn) {
	String token = UnitsManager.getEffectiveUnits(tokenIn);
	if (denominator.containsKey(token)) {
	    MutableInteger mi = denominator.get(token);
	    mi.add(1);
	} else {
	    denominator.put(token, new MutableInteger(1));
	}
    }
    
    public void reduce() {
	
	if (numerator.size() == 0)
	    return;
	

	List<String> removeFromNumerator = new ArrayList<String>();
	List<String> removeFromDenominator = new ArrayList<String>();
	
	for (String key : numerator.keySet()) {
	    if (isMultiMatch(key))
		continue;
	    if (denominator.containsKey(key)) {
		MutableInteger numer = numerator.get(key);
		MutableInteger denom = denominator.get(key);
		while (numer.value() > 0 && denom.value() > 0) {
		    numer.add(-1);
		    denom.add(-1);
		}
		if (numer.value() == 0)
		    removeFromNumerator.add(key);
		if (denom.value() == 0)
		    removeFromDenominator.add(key);
	    }
	}
	
	for (String key : removeFromNumerator)
	    numerator.remove(key);
	for (String key : removeFromDenominator)
	    denominator.remove(key);
	
	removeFromNumerator.clear();
	removeFromDenominator.clear();
	
	for (String key : numerator.keySet()) {
	    if (!isMultiMatch(key))
		continue;
	    if (numerator.size() > 1)
		removeFromNumerator.add(key);
	}
	
	for (String key : denominator.keySet()) {
	    if (!isMultiMatch(key))
		continue;
	    if (denominator.size() > 1)
		removeFromNumerator.add(key);
	}
	
	for (String key : removeFromNumerator)
	    numerator.remove(key);
	for (String key : removeFromDenominator)
	    denominator.remove(key);
	
	if (numerator.containsKey("dmnl") && numerator.size() > 1)
	    numerator.remove("dmnl");
	
	if (numerator.containsKey("dmnl") && numerator.size() == 1) {
	    if (denominator.size() == 0) {
		numerator.get("dmnl").setValue(1);
	    } else {
		numerator.put("1", new MutableInteger(1));
		numerator.remove("dmnl");
	    }
	}
	
	if (denominator.containsKey("dmnl"))
	    denominator.remove("dmnl");
	
	if (containsConstant(denominator))
	    removeConstant(denominator);
	
	// if we have removed all units, generate a dmnl
	if (numerator.size() == 0 && denominator.size() == 0)
	    numerator.put("dmnl", new MutableInteger(1));
	
	// if there is just a denominator, insert a "1"
	if (numerator.size() == 0 && denominator.size() > 0)
	    numerator.put("1", new MutableInteger(1));
	
	
    }
    
    private boolean isMultiMatch(String token) {
	if (token.equalsIgnoreCase("any") ||
		isConstant(token) ||
		token.equalsIgnoreCase("fraction") ||
		token.equalsIgnoreCase("percent") ||
		token.equalsIgnoreCase("1") )
	    return true;
	else
	    return false;
    }
    
    // TODO: Note: we need to worry about multimatch units: constant, any
    
    public boolean hasSameUnitsOriginal(SimplifiedUnit other) {
	// equality: numerator and denominator contain same information
	// in hashmaps
	
	// check for same number of entries
	if (this.numerator.size() != other.getNumerator().size() ||
		this.denominator.size() != other.getDenominator().size())
	    return false;
	
	// check same units
	for (String unit : numerator.keySet()) {
	    if (other.getNumerator().containsKey(unit)) {
		MutableInteger countThis = numerator.get(unit);
		MutableInteger countOther = other.getNumerator().get(unit);
		if (countThis.value() != countOther.value())
		    return false;
	    } else {
		return false;
	    }
	}
	
	for (String unit : denominator.keySet()) {
	    if (other.getDenominator().containsKey(unit)) {
		MutableInteger countThis = denominator.get(unit);
		MutableInteger countOther = other.getDenominator().get(unit);
		if (countThis != countOther)
		    return false;
	    } else {
		return false;
	    }
	}
	
	
	return true;
    }
    
    public boolean isConstant() {
	if (numerator.size() == 1 &&
		containsConstant(numerator)
		) {
	    if (denominator.size() > 1) {
		System.out.println("??? constant with denominator ???");
	    }
	    return true;
	} else {
	    return false;
	}
    }
    
    public boolean isDmnl() {
	if (numerator.size() == 1 &&
		numerator.containsKey("dmnl")) {
	    if (denominator.size() > 1) {
		System.out.println("??? constant with denominator ???");
	    }
	    return true;
	} else {
	    return false;
	}
    }
    
    public boolean isArrayInitialization() {
	return isArrayInitialization();
    }
    
    public boolean isAny() {
	if (numerator.containsKey("any"))
	    return true;
	else
	    return false;
    }
    
    public boolean hasSameUnits(SimplifiedUnit other) {
	// equality: numerator and denominator contain same information
	// in hashmaps
	
	// do a quick check for "constant" on RHS (which is this)
	if (isConstant() || other.isConstant())
	    return true;
	
	if (isAny() || other.isAny())
	    return true;
	
//	// check for same number of entries
//	if (!hasSameCount(this.numerator, other.getNumerator()) ||
//		!hasSameCount(this.denominator,other.getDenominator()))
//	    return false;
	
	// make copies of the structures
	
	Map<String, MutableInteger> thisNumerator = copyOf(numerator);
	Map<String, MutableInteger> thisDenominator = copyOf(denominator);
	Map<String, MutableInteger> otherNumerator = copyOf(other.getNumerator());
	Map<String, MutableInteger> otherDenominator = copyOf(other.getDenominator());
	
	// remove the units in common
	
	removeSame(thisNumerator, otherNumerator);
	removeSame(thisDenominator, otherDenominator);
	
	// if the sizes of the maps are 0, then they have same units
	
	if (thisNumerator.size() == 0 &&
		otherNumerator.size() == 0 &&
		thisDenominator.size() == 0 &&
		otherDenominator.size() == 0 )
	    return true;
	
	// at this point we have the same number of units, but not identical
	// we need to remove the "multiMatch" units i.e. any, constant
	
	removeMulti(thisNumerator, otherNumerator, thisDenominator, otherDenominator);
	
	// if not the same size, different
	
	if (!hasSameCount(thisNumerator, otherNumerator) ||
		!hasSameCount(thisDenominator,otherDenominator))
	    return false;
	

	
//	System.out.println(">>> sameUnits <<<");
//	System.out.println("This: "+this.formUnit());
//	System.out.println("Other: "+other.formUnit());
	
// if the sizes of the maps are 0, then they have same units
	
	if (thisNumerator.size() == 0 &&
		otherNumerator.size() == 0 &&
		thisDenominator.size() == 0 &&
		otherDenominator.size() == 0 )
	    return true;
	else
	    return false;
    }
    
    private boolean hasSameCount(Map<String, MutableInteger> thisOne, Map<String, MutableInteger> otherOne) {
	
	return getCount(thisOne) == getCount(otherOne);
	
    }
    
    private int getCount(Map<String, MutableInteger> thisOne) {
	int thisCount = 0;
	for (String key : thisOne.keySet())
	    thisCount += thisOne.get(key).value();
	return thisCount;
    }
    
    private void removeMulti(Map<String, MutableInteger> thisNumerator, Map<String, MutableInteger> otherNumerator,
	    Map<String, MutableInteger> thisDenominator, Map<String, MutableInteger> otherDenominator) {
	// simply remove all these things from each of the four maps and see what happend
	
	
	    removeEquivalent(thisNumerator);
	    removeEquivalent(otherNumerator);
	    removeEquivalent(thisDenominator);
	    removeEquivalent(otherDenominator);
    }
    
    private void removeSame(Map<String, MutableInteger> thisOne, Map<String, MutableInteger> otherOne) {
	List<String> toRemove = new ArrayList<String>();
	for (String key : thisOne.keySet()) {
	    if (otherOne.containsKey(key)) {
		MutableInteger miThis = thisOne.get(key);
		MutableInteger miOther = otherOne.get(key);
		if (miThis.value() == miOther.value()) {
		    toRemove.add(key);
		} else if (miThis.value() > miOther.value()) {
		    miThis.add(-miOther.value());
		} else if (miThis.value() < miOther.value()) {
		    miOther.add(-miThis.value());
		}
	    } 
	}
	for (String key : toRemove) {
	    thisOne.remove(key);
	    otherOne.remove(key);
	}
    }
    
    public boolean validForOperation(String operator, SimplifiedUnit otherSU) {
	if (operator == null) {
	    System.out.println("Operator is null!");
	}
	if (operator.equals("+") || operator.equals("-") || operator.equals("==") || 
		Parser.isBooleanOperator(operator)) {
	    if(this.hasSameUnits(otherSU))
		return true;
	    else
		return false;
	    
	} else if (operator.equals("*") || operator.equals("/")) {
	    return true;
	} 
	else if (operator.equals("^")) {
	    if (otherSU.isConstant() || otherSU.isDmnl())
		return true;
	    else
		return false;
	} 
	return false;
    }
    
    public void equals(SimplifiedUnit su) {
	numerator.clear();
	numerator.put("dmnl", new MutableInteger(1));
	denominator.clear();
    }
    
    public void plus(SimplifiedUnit su) {
	// nop: no changes to units
    }
    
    public void minus(SimplifiedUnit su) {
	// nop: no changes to units
    }
    
    public void exp(SimplifiedUnit su) {
	
	if (su.isConstant()) {
	    String key = su.getConstantKey(su.getNumerator());
	    String constant = su.getConstantAsString(key);
	    SimplifiedUnit copy = new SimplifiedUnit(this);
	    if (Parser.isInteger(constant)) {
		int con = Integer.parseInt(constant);
		for (int i = 0; i < con-1; i++) {
		    this.times(copy);
		}
	    }
	}
    }
    
    public void times(SimplifiedUnit su) {
	for (String key : su.getNumerator().keySet()) {
	    if (numerator.containsKey(key))
		numerator.get(key).add(1);
	    else
		numerator.put(key, new MutableInteger(su.getNumerator().get(key).value()));
	}
	for (String key : su.getDenominator().keySet()) {
	    if (denominator.containsKey(key))
		denominator.get(key).add(1);
	    else
		denominator.put(key, new MutableInteger(su.getDenominator().get(key).value()));
	}
//	this.reduce();
    }
    
    public void dividedBy(SimplifiedUnit su) {
	times(su.invert());
//	this.reduce();
    }
    
    public SimplifiedUnit invert() {
	
	SimplifiedUnit su = new SimplifiedUnit(this);
	su.setDenominator(numerator);
	su.setNumerator(denominator);
	return su;
//	return (new SimplifiedUnit("1/"+getOriginalUnit()));
    }

    public String getOriginalUnit() {
        return originalUnit;
    }

    public void setOriginalUnit(String originalUnit) {
        this.originalUnit = originalUnit;
    }

    public Map<String, MutableInteger> getNumerator() {
        return numerator;
    }

    public void setNumerator(Map<String, MutableInteger> numerator) {
        this.numerator = numerator;
    }

    public Map<String, MutableInteger> getDenominator() {
        return denominator;
    }

    public void setDenominator(Map<String, MutableInteger> denominator) {
        this.denominator = denominator;
    }

    public String getSimplified() {
        return simplified;
    }

    public void setSimplified(String simplified) {
        this.simplified = simplified;
    }
    
    private Map<String, MutableInteger> copyOf(Map<String, MutableInteger> map) {
	Map<String, MutableInteger> newMap = new HashMap<String, MutableInteger>();
	for (String key : map.keySet()) {
	    MutableInteger mi = map.get(key);
	    newMap.put(key, new MutableInteger(mi.value()));
	}
	
	return newMap;
    }

}
