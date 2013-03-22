/**
 * 
 */
package repast.simphony.systemdynamics.translator;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import repast.simphony.systemdynamics.support.ArrayDefinition;
import repast.simphony.systemdynamics.support.ArrayReference;
import repast.simphony.systemdynamics.support.MutableInteger;
import repast.simphony.systemdynamics.support.NamedSubscriptManager;
import repast.simphony.systemdynamics.support.Subscript;
import repast.simphony.systemdynamics.support.SubscriptCombination;

/**
 * @author bragen
 *
 */
public class ArrayManager {

    private static Map<String, ArrayDefinition> arrays = new HashMap<String, ArrayDefinition>();
    private static Set<String> arraysUsedAsLookup = new HashSet<String>();
    
    // for each array, we need to keep a list of referenced subscripts by position
    // we'll need to this to eventually contain only terminal subscripts (non-named subscripts)
    
    private static Map<String, Map<Integer, Set<Subscript>>> subscriptSpace = 
	new HashMap<String, Map<Integer, Set<Subscript>>>();
    
    public static Map<String, Map<Integer, Map<String, Integer>>> allocatedIndicies = 
	new HashMap<String, Map<Integer, Map<String, Integer>>>();
    
    private static Map<String, Set<String>> uninitializedSubscriptCombinationsByArray =
	new HashMap<String, Set<String>>();
    
    public static List<String> getOrderedSubscriptNames(String array, int dimension, String subscript) {
	List<String> orderedSubscripts = new ArrayList<String>();
	
	for (String s : NamedSubscriptManager.getValuesFor(subscript)) {
	    orderedSubscripts.add(s);   
	}
	
	return orderedSubscripts;
    }
    
    public static OperationResult validateLookupReference(String token) {
    	OperationResult or = new OperationResult();
    	
    	ArrayReference ar = null;
    	if (ArrayReference.isArrayReference(token)) {
    	    ar = new ArrayReference(token);
    	    if (arrays.containsKey(ar.getArrayName()))
    			return or;
    	    
    	} 
    	
    	or.setErrorMessage("Invalid array reference "+token);
    	return or;
    }
    
    public static OperationResult validateArrayReference(String token) {
    	OperationResult or = new OperationResult();
    	
    	ArrayReference ar = null;
    	if (ArrayReference.isArrayReference(token)) {
    	    ar = new ArrayReference(token);
    	    if (arraysUsedAsLookup.contains(ar.getArrayName()))
    			return or;
    	    
    	} 
    	
    	or.setErrorMessage("Invalid lookup table reference "+token);
    	return or;
    }
    
    public static String getVensimSubscript(String array, int dimension, int index) {
	// inefficient change this to store as hash,ap
	Map<Integer, Map<String, Integer>> arrayMap = allocatedIndicies.get(array);
	    
	    Map<String, Integer> indexMap = arrayMap.get(dimension);
	    for (String subscript : indexMap.keySet()) {
		int storedIndex = indexMap.get(subscript);
		if (storedIndex == index)
		    return subscript;
	    }
	return "BOGUS$$$";
    }
    
    public static boolean isUsedAsLookup(String arrayName) {
	return arraysUsedAsLookup.contains(arrayName);
    }
    
    public static void setUsedAsLookup(String arrayName) {
	if (ArrayReference.isArrayReference(arrayName)) {
	    ArrayReference ar = new ArrayReference(arrayName);
	    arraysUsedAsLookup.add(ar.getArrayName());
	}
	arraysUsedAsLookup.add(arrayName);
    }
    
    
    
    public static int getNumDimensions(String array) {
	if (allocatedIndicies.get(array) == null) {
//	    printSubscriptSpace();
//	    System.out.println("No index info for array: "+array);
	    
	    return 0;
	}
	
	return allocatedIndicies.get(array).size();
    }
    
    public static int getNumIndicies(String array, int dimension) {
	if (allocatedIndicies.get(array) == null) {
//	    printSubscriptSpace();
//	    System.out.println("No index info for array: "+array);
	    return 0;
	}
	if (allocatedIndicies.get(array).get(dimension) == null) {
//	    printSubscriptSpace();
//	    System.out.println("No index info for array/dimension:"+array+"/"+dimension);
	    return 0;
	}
	Map<String, Integer> indicies = allocatedIndicies.get(array).get(dimension);
	Set<Integer> indSet = new HashSet<Integer>();
	for (String key : indicies.keySet()) {
	    indSet.add(indicies.get(key));
	}
	
	return indSet.size();
    }
    
    public static String getIndicies(String arrayIn, int dimension, String subscript) {
	String array = new String(arrayIn);
	if (array.startsWith("lookup.")) {
	    
	    array = array.replace("lookup.", "");
	}
	Map<String, Integer> indicies = allocatedIndicies.get(array).get(dimension);
	int pos = 0;
	StringBuffer sb = new StringBuffer();
	for (String s : NamedSubscriptManager.getValuesFor(subscript)) {
	    if (pos++ > 0)
		sb.append(", ");
	    sb.append(indicies.get(s));   
	}
	
	return sb.toString();
    }
    
    public static String getIndiciesSorted(String array, int dimension, String subscript) {
	Map<String, Integer> indicies = allocatedIndicies.get(array).get(dimension);
	List<Integer> ind = new ArrayList<Integer>();
	for (String s : NamedSubscriptManager.getValuesFor(subscript.replace("!", ""))) {
	    ind.add(indicies.get(s));
	}
	Collections.sort(ind);
	int pos = 0;
	StringBuffer sb = new StringBuffer();
	for (Integer s : ind) {
	    if (pos++ > 0)
		sb.append(", ");
	    sb.append(s);   
	}
	
	return sb.toString();
    }

    public static void arrayReference(String arrayName, String...subscripts) {

	// Is this the first time we have seen arrays?
	if (!arrays.containsKey(arrayName)) {
	    arrays.put(arrayName, new ArrayDefinition(arrayName, subscripts.length));
	}
	// record the subscript values that have been applied
	ArrayDefinition ad = arrays.get(arrayName);
	for (int i = 0; i < subscripts.length; i++)
	    ad.addReference(i, subscripts[i]);
    }

    public static void arrayReference(String arrayName, List<String> subscripts) {

	// Is this the first time we have seen arrays?
	if (!arrays.containsKey(arrayName)) {
	    arrays.put(arrayName, new ArrayDefinition(arrayName, subscripts.size()));
	}
	// record the subscript values that have been applied
	ArrayDefinition ad = arrays.get(arrayName);
	for (int i = 0; i < subscripts.size(); i++)
	    ad.addReference(i, subscripts.get(i).replace("!", ""));
    }
    
    public static void dumpSubscriptSpaceOrig(BufferedWriter bw) {


	try {
	    bw.append("array,subNum,value,type,start,end\n");
	    for (String array : subscriptSpace.keySet()) {
		Map<Integer, Set<Subscript>> map = subscriptSpace.get(array);
		
		for (int i = 0; i < map.keySet().size(); i++) {
		    
		    Set<Subscript> subs = map.get(i);
		    for (Subscript s : subs) {
			bw.append(array+","+i+","+s.getValue()+","+getIndicies(array, i, s.getValue())+"\n");
		    }
		}
	    }
	    bw.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public static void dumpSubscriptSpace(BufferedWriter bw) {


	try {
	    bw.append("array,subNum,alphabetic,numeric\n");
	    for (String array : allocatedIndicies.keySet()) {
		Map<Integer, Map<String, Integer>> arrayMap = allocatedIndicies.get(array);
		
		for (int dimension = 0; dimension < arrayMap.keySet().size(); dimension++) {
		    
		    Map<String, Integer> indexMap = arrayMap.get(dimension);
		    for (String subscript : indexMap.keySet()) {
			bw.append(array+","+dimension+","+subscript+","+indexMap.get(subscript)+"\n");
		    }
		}
	    }
	    bw.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public static void printSubscriptSpace() {

	    System.out.println("array,subNum,alphabetic,numeric");
	    for (String array : allocatedIndicies.keySet()) {
		Map<Integer, Map<String, Integer>> arrayMap = allocatedIndicies.get(array);
		
		for (int dimension = 0; dimension < arrayMap.keySet().size(); dimension++) {
		    
		    Map<String, Integer> indexMap = arrayMap.get(dimension);
		    for (String subscript : indexMap.keySet()) {
			System.out.println(array+","+dimension+","+subscript+","+indexMap.get(subscript));
		    }
		}
	    }
    }
    
    public static void populateArraySubscriptSpace() {

	// for all arrays that have been discovered
	for (String array : arrays.keySet()) {
	    // create the subscript space for the array
	    subscriptSpace.put(array, new HashMap<Integer, Set<Subscript>>());
	    Map<Integer, Set<Subscript>> arraySubscriptSpace = subscriptSpace.get(array);
	    allocatedIndicies.put(array, new HashMap<Integer, Map<String, Integer>>());
	    
	    
	    ArrayDefinition ad = arrays.get(array);
	    for (int dim = 0; dim < ad.getNumDimensions(); dim++) {
//		System.out.println("Array: "+ad.getName()+" Dimension: "+dim);
		arraySubscriptSpace.put(dim, new HashSet<Subscript>());
		allocatedIndicies.get(array).put(dim, new HashMap<String, Integer>());
		Set<Subscript> subscripts = arraySubscriptSpace.get(dim);
		// for each dimension, capture the values used as subscripts in array references
		
		for (String sub : ad.getSubscriptReferencesForPos(dim)) {
		    sub = sub.replace("!","");
		    Subscript s = new Subscript(sub, 
			    NamedSubscriptManager.isNamedSubscript(sub) ? Subscript.NAMED : Subscript.TERMINAL);
		    subscripts.add(s);
		    expand(s);
		    s.computeInitialStartEnd();
		}
		
		Map<String, Integer> allocatedIndiciesForArrayDimension = allocatedIndicies.get(array).get(dim);
		allocateIndicies(subscripts, allocatedIndiciesForArrayDimension);
	    }
	}
	    
	// generate all possible array subscript combinations
	for (String array : arrays.keySet()) {
	    uninitializedSubscriptCombinationsByArray.put(array, generateCombinationsByPos(array));
	}

    }
    
    private static void allocateIndicies(Set<Subscript> subscripts, 
	    Map<String, Integer> allocatedIndiciesForArrayDimension) {
	
	List<Subscript> subscriptsSortedByNumChildren = new ArrayList<Subscript>();
	subscriptsSortedByNumChildren.addAll(subscripts);
	
	Collections.sort(subscriptsSortedByNumChildren, new Comparator<Subscript>() {
		@Override
		public int compare(Subscript o1, Subscript o2) {
		    
		    if (o1.getChildren() == null && o2.getChildren() == null)
			return 0;
		    if (o1.getChildren() != null && o2.getChildren() == null)
			return -1;
		    if (o1.getChildren() == null && o2.getChildren() != null)
			return 1;
		    
			return o1.getChildren().size() < o2.getChildren().size() ? 
				1 : o1.getChildren().size() > o2.getChildren().size() ? -1 : 0;
		}
	});
	
	// allTerminals must be a sorted list of terminals
	List<String> allTerminals = new ArrayList<String>();
	// create The set of all terminals
	for (Subscript s : subscriptsSortedByNumChildren) {
	    
//	    s.printDetail();
	    
	    List<String> allTerm = NamedSubscriptManager.getValuesFor(s.getValue());
	    for (String t : allTerm) {
		if (!allTerminals.contains(t))
		    allTerminals.add(t);
	    }
	}
	
	Iterator<String> iter = allTerminals.iterator();
	int index = 0;
	while (iter.hasNext()) {
	    allocatedIndiciesForArrayDimension.put(iter.next(), index++);
	}
	
    }
    
    private static List<Subscript> getNamed(Set<Subscript> subscripts) {
	List<Subscript> list = new ArrayList<Subscript>();
	
	for(Subscript subscript : subscripts) {
	    if (subscript.isNamed())
		list.add(subscript);
	}
	
	return list;
    }
    
    private static List<Subscript> getTerminal(Set<Subscript> subscripts) {
	List<Subscript> list = new ArrayList<Subscript>();
	
	for(Subscript subscript : subscripts) {
	    if (subscript.isTerminal())
		list.add(subscript);
	}
	
	return list;
    }
    
    private static void expand(Subscript subscript) {
	if (subscript.isTerminal())
	    return;
	List<String> values = NamedSubscriptManager.getValuesFor(subscript.getValue());
	for (String sub : values) {
	    sub = sub.replace("!","");
	    Subscript s = new Subscript(sub, 
		    NamedSubscriptManager.isNamedSubscript(sub) ? Subscript.NAMED : Subscript.TERMINAL);
	    subscript.addChild(s);
	    expand(s);
	}
    }
    
    private static Set<String> generateCombinationsByPos(String array) {
	Set<String> combos = new HashSet<String>();
	Map<Integer, Set<Subscript>> arraySubscriptSpace = subscriptSpace.get(array);
	    ArrayDefinition ad = arrays.get(array);
	    Integer[] sizeByPos = new Integer[ad.getNumDimensions()];
	    for (int pos = 0; pos < ad.getNumDimensions(); pos++) {
		sizeByPos[pos] = arraySubscriptSpace.get(pos).size();
	    }
	    
	    List<String> posNames = new ArrayList<String>();
	    
	    int numPermutations = 1;
		for (int pos = 0; pos < ad.getNumDimensions(); pos++) {
		    numPermutations *= sizeByPos[pos].intValue();
		    posNames.add(Integer.toString(pos));
		}
		
		// create all permutations
		SubscriptCombination[] combinations = new SubscriptCombination[numPermutations];

		// initialize the empty buffers
		for (int i = 0; i < numPermutations; i++)
		    combinations[i] = new SubscriptCombination(posNames);

		// for each of the subscripts
		for (int subscriptNumber = 0; subscriptNumber < ad.getNumDimensions(); subscriptNumber++) {
		    
		    String[] subValues = new String[arraySubscriptSpace.get(subscriptNumber).size()];
		    int s = 0;
		    for (Subscript subr : arraySubscriptSpace.get(subscriptNumber)) {
			subValues[s++] = subr.getValue();
		    }

		    // determine the number of times that the same value should be output
		    int numPerValue = 1;
		    for (int i = subscriptNumber+1; i < sizeByPos.length; i++) {
			numPerValue *= sizeByPos[i];
		    }
		    int row = 0;
		    while(row < numPermutations) {
			for (String value : subValues) {

			    for (int i = 0; i < numPerValue; i++) {
				// String subscript, String value
				combinations[row].addSubscriptValue(posNames.get(subscriptNumber), value);
				row++;
			    }
			}
		    }
		}
		for (int i = 0; i < combinations.length; i++) {
		    combos.add(combinations[i].getSubscriptValue());
		}
	
	return combos;
    }
    
    public static void initialized(String arrayReference) {
	ArrayReference ar = new ArrayReference(arrayReference);
	String arrayName = ar.getArrayName();
	Set<String> uninitialized = uninitializedSubscriptCombinationsByArray.get(arrayName);
	String[] subscripts = ar.getSubscriptsAsArray();
	
	for (SubscriptCombination sc : getSubscriptValueCombinations(subscripts)) {
	    uninitialized.remove(sc.getSubscriptValue());
	}
    }
    
    public static boolean isInitialized(String arrayReference) {
	ArrayReference ar = new ArrayReference(arrayReference);
	String arrayName = ar.getArrayName();
//	String subscript = ar.get
	Set<String> uninitialized = uninitializedSubscriptCombinationsByArray.get(arrayName);
	
	String[] subscripts = ar.getSubscriptsAsArray();
	
	for (SubscriptCombination sc : getSubscriptValueCombinations(subscripts)) {
	    if (uninitialized.contains(sc.getSubscriptValue()))
		    return false;
	}
	
	    return true;
    }

    public static List<SubscriptCombination> getSubscriptValueCombinations(String ...strings) {
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

    public static List<String> getSubscriptValues(String subscriptName) {
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

    public static boolean isNamedSubscript(String subscriptName) {
	if (EquationProcessor.subscriptValues.containsKey(subscriptName))
	    return true;
	else
	    return false;
    }
    
    public static boolean isTerminalSubscript(String arrayName, String subscriptName, int dimension) {
	
//	    private static Map<String, Map<Integer, Map<String, Integer>>> allocatedIndicies = 
//		new HashMap<String, Map<Integer, Map<String, Integer>>>();
	
	
	Map<Integer, Map<String, Integer>> alloc = allocatedIndicies.get(arrayName);
	if (alloc == null) {
//	    System.out.println("ISTERMINAL? bad arrayName <"+arrayName+">");
	    return false;
	}
	Map<String, Integer> indicies = alloc.get(dimension);
	if (indicies == null) {
//	    System.out.println("ISTERMINAL? bad dimension <"+dimension+">");
	    return false;
	}
	if (indicies.containsKey(subscriptName))
	    return true;
	else
	    return false;
    }
    
    public static int getTerminalValue(String arrayName, String subscriptName, int dimension) {
	Map<Integer, Map<String, Integer>> alloc = allocatedIndicies.get(arrayName);
	if (alloc == null) {
//	    System.out.println("getTerminalValue? bad arrayName <"+arrayName+">");
	    return -1;
	}
	Map<String, Integer> indicies = alloc.get(dimension);
	if (indicies == null) {
//	    System.out.println("getTerminalValue? bad dimension <"+dimension+">");
	    return -1;
	}
	if (indicies.containsKey(subscriptName))
	    return indicies.get(subscriptName);
	else
	    return -1;
    }

   
    private static List<String> extractSubscripts(List<String> subscriptList) {
	List<String> al = new ArrayList<String>();
	for (String token : subscriptList)
	    if (!token.equals("[") && !token.equals(",") && !token.equals("]"))
		al.add(token);
	return al;
    }
    
//    public static void printSubscriptSpace() {
////	 private static Map<String, Map<Integer, Set<String>>> subscriptSpace = 
////		new HashMap<String, Map<Integer, Set<String>>>();
//	
//	for (String array : subscriptSpace.keySet()) {
//	    System.out.println("##### "+array);
//	    Map<Integer, Set<Subscript>> map = subscriptSpace.get(array);
//	    for (int i = 0; i < map.keySet().size(); i++) {
//		System.out.println("   >>>>> "+i);
//		Set<Subscript> subs = map.get(i);
//		for (Subscript s : subs) {
//		    System.out.println("          <"+s.toString()+">");
//		}
//	    }
//	}
//    }
    
    public static void getNumRowsAndCols(String arrayName, String subscript, MutableInteger numRows, MutableInteger numCols) {
	int numCol = -1;
	int numRow = 1;
	List<String> subs = extractSubscripts(subscript);
	for (int i = subs.size()-1; i >= 0; i--) {
	    if (NamedSubscriptManager.isNamedSubscript(subs.get(i))) {
		if (numCol == -1) {
		    numCol = NamedSubscriptManager.getValuesFor(subs.get(i)).size();
		} else  {
		    numRow *= NamedSubscriptManager.getValuesFor(subs.get(i)).size();
		}
	    }
	}
	numRows.setValue(numRow);
	numCols.setValue(numCol);
    }
    
    private static List<String> extractSubscripts(String subscriptList) {
	String[] subs = subscriptList.split(",");
	List<String> al = new ArrayList<String>();
	for (String token : subs)
	    al.add(token);
	return al;
    }
    
    public static Set<String> expand(ArrayReference arrRef) {
	Set<String> al = new HashSet<String>();
	
	for (SubscriptCombination sc : getSubscriptValueCombinations(arrRef.getSubscriptsAsArray())) {
//	    al.add(arrayName+ "["+sc.getSubscriptValue()+"]");
	    
	    al.add("array."+arrRef.getArrayName()+ "["+sc.getSubscriptValue()+"]");
	}
	
	return al;
    }
    
    public static Set<String> expandRawSubscript(ArrayReference arrRef) {
	Set<String> al = new HashSet<String>();
	
	for (SubscriptCombination sc : ArrayManager.getSubscriptValueCombinations(arrRef.getSubscriptsAsArray())) {
//	    al.add(arrayName+ "["+sc.getSubscriptValue()+"]");
	    
	    al.add("["+sc.getSubscriptValue()+"]");
	}
	
	return al;
    }

}
