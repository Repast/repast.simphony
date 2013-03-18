/**
 * 
 */
package repast.simphony.systemdynamics.support;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bragen
 * 
 */
public class MappedSubscriptManager {

    // manages named subscript mappings

    // subscriptNameMap: Map<String, Map<String, Map<String, String>>>
    // 1st level key is definitionLHS -> Map<String, Map<String, String>>
    // 2nd level key is definitionRHS -> Map<String, String>
    // 3rd level key is definitionRHS -> definitionLHS

    public static Map<String, Map<String, Map<String, String>>> subscriptNameMap = 
	new HashMap<String, Map<String, Map<String, String>>>();
    private static List<DelayedMapping> delayedMappings = new ArrayList<DelayedMapping>();
    
    
    public static boolean isMapBetween(String equationRHS, String equationLHS) {
	boolean map = false;
	if (subscriptNameMap.containsKey(equationRHS)) {
	    Map<String, Map<String, String>> level1 = subscriptNameMap.get(equationRHS);
	    return level1.containsKey(equationLHS);
	}
	
	
	return map;
    }
    

    // here, lhsSubscript refers to the LHS of the mapping statement i.e.
    // lhsSubscript: x, y, z -> mappedToSubscript

    public static void addSubscriptNameMapping(String definitionLHS,
	    String definitionRHS) {
	if (!subscriptNameMap.containsKey(definitionLHS))
	    subscriptNameMap.put(definitionLHS,
		    new HashMap<String, Map<String, String>>());
	Map<String, Map<String, String>> mappings = subscriptNameMap
		.get(definitionLHS);

	if (!mappings.containsKey(definitionRHS))
	    mappings.put(definitionRHS, new HashMap<String, String>());
    }

    // we can just save this as a delayed mapping since the subscript values are
    // full range
    public static void addSubscriptNameFullSubrangeMapping(
	    String definitionLHS, String definitionRHS) {
	addSubscriptNameMapping(definitionLHS, definitionRHS);
	delayedMappings.add(new DelayedMapping(definitionLHS, definitionRHS));
    }

    public static void addSubscriptValueMapping(String definitionLHS,
	    String definitionRHS, String definitionLHSValue,
	    String definitionRHSValue) {
	Map<String, Map<String, String>> mappings = subscriptNameMap
		.get(definitionLHS);
	Map<String, String> valueMap = mappings.get(definitionRHS);
	valueMap.put(definitionRHSValue, definitionLHSValue);
    }

    public static void addSubscriptValueMappingDelayed(String definitionLHS,
	    String definitionRHS, List<String> definitionLHSValues) {

	delayedMappings.add(new DelayedMapping(definitionLHS, definitionRHS,
		definitionLHSValues));

    }

    public static void addSubscriptValueMappingDelayed(String definitionLHS,
	    String definitionRHS, List<String> definitionLHSValues,
	    List<String> definitionRHSValues) {

	delayedMappings.add(new DelayedMapping(definitionLHS, definitionRHS,
		definitionLHSValues, definitionRHSValues));

    }

    public static void makeConsistent() {
	// check for full Subrange that may not have a set of values equated
	// will need to reach out to the NamedSubscriptManager for subscript
	// values

	for (DelayedMapping delayedMapping : delayedMappings) {
	    List<String> definitionLHSValues;
	    List<String> definitionRHSValues;
	    definitionLHSValues = delayedMapping.getDefinitionLHSValues();
	    definitionRHSValues = delayedMapping.getDefinitionRHSValues();

	    if (definitionLHSValues == null) {
		// this assumes that the for x <-> y means that y is defined and
		// x is defined by this (at the time of reading this statement

		if (NamedSubscriptManager.isNamedSubscript(delayedMapping.getDefinitionLHS()))  {
		    definitionLHSValues = NamedSubscriptManager.getValuesFor(delayedMapping.getDefinitionLHS());
		} else {
		    definitionLHSValues = NamedSubscriptManager.getValuesFor(delayedMapping.getDefinitionRHS());
		}
	    } 
	    if (definitionRHSValues == null) {
		if (NamedSubscriptManager.isNamedSubscript(delayedMapping.getDefinitionRHS()))  {
		    definitionRHSValues = NamedSubscriptManager.getValuesFor(delayedMapping.getDefinitionRHS());
		} else {
		    definitionRHSValues = NamedSubscriptManager.getValuesFor(delayedMapping.getDefinitionLHS());
		}
	    }

	    // List<String> definitionRHSValues =
	    // NamedSubscriptManager.getValuesFor(dm.getDefinitionRHS());

	    if (definitionLHSValues.size() > 0) {
		for (int i = 0; i < definitionLHSValues.size(); i++)
		    MappedSubscriptManager.addSubscriptValueMapping(
			    delayedMapping.getDefinitionLHS(),
			    delayedMapping.getDefinitionRHS(),
			    definitionLHSValues.get(i),
			    definitionRHSValues.get(i));
	    }
	}

	delayedMappings.clear();

	// for each mapping
	for (String definitionLHS : subscriptNameMap.keySet()) {
	    Map<String, Map<String, String>> mappings = subscriptNameMap
	    .get(definitionLHS);
	    // get other subscripts to which this is mapped
	    for (String definitionRHS : mappings.keySet()) {
		Map<String, String> valueMap = mappings.get(definitionRHS);
		// if there are values that are mapped, we are OK
		if (valueMap.size() > 0)
		    continue;
		// otherwise, we need to populate
		for (String value : NamedSubscriptManager
			.getValuesFor(definitionRHS)) {
		    addSubscriptValueMapping(definitionLHS, definitionRHS,
			    value, value); 
		}
	    }
	}

	List<String> level1Key = new ArrayList<String>();
	List<String> level2Key;
	List<String> level3Key;

	for (String key : subscriptNameMap.keySet()) {
	    level1Key.add(key);
	}
	Collections.sort(level1Key);
	for (String key1 : level1Key) {
	    Map<String, Map<String, String>> level2Map = subscriptNameMap
	    .get(key1);
	    level2Key = new ArrayList<String>();
	    for (String key : level2Map.keySet())
		level2Key.add(key);
	    Collections.sort(level2Key);
	    for (String key2 : level2Key) {
		Map<String, String> level3Map = level2Map.get(key2);
		level3Key = new ArrayList<String>();
		for (String key : level3Map.keySet())
		    level3Key.add(key);
		Collections.sort(level3Key);
		for (String key3 : level3Key) {
		    if (NamedSubscriptManager.isNamedSubscript(key3)) {
			for (String terminal : NamedSubscriptManager
				.getValuesFor(key3)) {
			    addSubscriptValueMapping(key1, key2, 
				    level3Map.get(key3), terminal);
			    //terminal); // this terminal is wrong, needs to point
			}
		    }

		}
	    }
	}
    }
    
    
    public static String getSubscriptMapping(String eqLHSnamed, String eqLHSterminal, String eqRHSnamed) {
	Map<String, Map<String, String>> level2Map = subscriptNameMap.get(eqRHSnamed);
	Map<String, String> level3Map = level2Map.get(eqLHSnamed);
	String mapped = level3Map.get(eqLHSterminal);
	if (mapped == null) {
	    mapped = "MissingMap!";
	}
	return mapped;
    }

    // here rhsSubscript means the rhs of an assignment equation
    // lhsSubscript is the subscript referenced on the lhs of equation

    public static String getMappedValue(String definitionLHS,
	    String definitionRHS, SubscriptCombination subscriptCombination) {
	if (!subscriptNameMap.containsKey(definitionLHS))
	    return null;
	Map<String, Map<String, String>> mappings = subscriptNameMap
		.get(definitionLHS);
	Map<String, String> valueMap = mappings.get(definitionRHS);
	if (valueMap == null)
	    return null;
	if (valueMap.containsKey(subscriptCombination
		.getSubscriptValue(definitionRHS)))
	    return valueMap.get(subscriptCombination
		    .getSubscriptValue(definitionRHS));
	return null;
    }

    public static String getMappedValue(String definitionLHS,
	    List<String> allDefinitionRHS,
	    SubscriptCombination subscriptCombination) {
	// there can only be one mapping

	for (String definitionRHS : allDefinitionRHS) {
	    String mappedValue = getMappedValue(definitionLHS, definitionRHS,
		    subscriptCombination);
	    if (mappedValue != null) {
		return mappedValue;
	    }
	}

	// this code is strictly for debugging purposes. It needs to be deleted
	for (String lhsSubscript : allDefinitionRHS) {
	    String mappedValue = getMappedValue(definitionLHS, lhsSubscript,
		    subscriptCombination);
	    if (mappedValue != null) {
		return mappedValue;
	    }
	}
	return null;

    }

    public static void generateCode(BufferedWriter bw) {
	// this method takes the information determined while parsing the System
	// Dynamics equations
	// and writes it back out in the forms of method calls to reload the
	// data.

	// addSubscriptNameMapping

	// makeConsistent();
	// dumpMappings(VensimReader.openReport("SubscriptMappings.csv"));

	try {
	    for (String definitionLHS : subscriptNameMap.keySet()) {
		Map<String, Map<String, String>> mappings = subscriptNameMap
			.get(definitionLHS);
		for (String definitionRHS : mappings.keySet()) {
		    bw.append("MappedSubscriptManager.addSubscriptNameMapping( \""
			    + definitionLHS
			    + "\", \""
			    + definitionRHS
			    + "\");\n");
		    Map<String, String> valueMap = mappings.get(definitionRHS);
		    for (String definitionRHSVal : valueMap.keySet()) {
			bw.append("MappedSubscriptManager.addSubscriptValueMapping( \""
				+ definitionLHS
				+ "\", \""
				+ definitionRHS
				+ "\", \""
				+ valueMap.get(definitionRHSVal)
				+ "\", \"" + definitionRHSVal + "\");\n");
		    }
		}
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public static void dumpMappings(BufferedWriter bw) {
	List<String> level1Key = new ArrayList<String>();
	List<String> level2Key;
	List<String> level3Key;
	
	try {
	    bw.append("EQ RHS,EQ LHS,EQ LHS Sub,EQ RHS Sub,EQ LHS Sub Named,EQ RHS Sub Named,LHS Index,RHS Index\n");
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	for (String key : subscriptNameMap.keySet()) {
	    level1Key.add(key);
	}
	Collections.sort(level1Key);
	for (String key1 : level1Key) {
	    Map<String, Map<String, String>> level2Map = subscriptNameMap
		    .get(key1);
	    level2Key = new ArrayList<String>();
	    for (String key : level2Map.keySet())
		level2Key.add(key);
	    Collections.sort(level2Key);
	    for (String key2 : level2Key) {
		Map<String, String> level3Map = level2Map.get(key2);
		level3Key = new ArrayList<String>();
		for (String key : level3Map.keySet())
		    level3Key.add(key);
		Collections.sort(level3Key);
		for (String key3 : level3Key) {
		    try {
			bw.append(key1
				+ ","
				+ key2
				+ ","
				+ key3
				+ ","
				+ level3Map.get(key3)
				+ ","
				+ NamedSubscriptManager.isNamedSubscript(key3)
				+ ","
				+ NamedSubscriptManager
				.isNamedSubscript(level3Map.get(key3))
				+ ","
				+NamedSubscriptManager.getIndex(key2, key3)
				+ ","
				+NamedSubscriptManager.getIndex(key1, level3Map.get(key3))
				+ "\n");
		    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }

		}
	    }
	}
	try {
	    bw.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
