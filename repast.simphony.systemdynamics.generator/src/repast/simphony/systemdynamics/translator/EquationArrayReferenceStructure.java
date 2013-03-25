package repast.simphony.systemdynamics.translator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import repast.simphony.systemdynamics.support.ArrayReference;
import repast.simphony.systemdynamics.support.MappedSubscriptManager;
import repast.simphony.systemdynamics.support.NamedSubscriptManager;

public class EquationArrayReferenceStructure {
    /**
     * Information that we need to capture for native data structure code generation
     * 
     * LHS arrayName
     * LHS subscripts -- named or terminal
     * RHS arrayName1
     * RMS array1 subscripts -- mapping to outerSub position range subscripts mapping to rangeSub position
     */
    
    private Equation equation;
    private ArrayReference lhsArrayReference;
    private List<ArrayReference> rhsArrayReferences;
    private List<String> rangeSubscriptNames;
    
    private Set<String> localIndexArrays = new HashSet<String>();
    
    private boolean arrayInitialization = false;
    private int numberArrayInitializers = 0;
    
    private boolean hasLHSarrayReference = false;
    private boolean hasRHSarrayReference = false;
    private boolean hasRHSrangeArrayReference = false;
    
    private boolean hasRHSmappedArrayReference = false;
    
    private boolean requiresOuterIndexArray = false;
    private boolean requiresRangeIndexArray = false;
    private boolean requiresMappedIndexArray = false;
    
    private List<String> arraysRequiringOuterIndexArray = new ArrayList<String>();
    private List<String> arraysRequiringRangeIndexArray = new ArrayList<String>();
    private List<String> arraysRequiringMappedIndexArray = new ArrayList<String>();
    
    private Map<String, String> mappedSubscriptIndexMap = new HashMap<String, String>();
    
    private Map<String, Integer> rangeIndexMap = new HashMap<String, Integer>();
    
    Map<String, List<String>> uniqueBySubscript = new HashMap<String, List<String>>();
    Map<String, List<String>> uniqueRangeBySubscript = new HashMap<String, List<String>>();
    
    public EquationArrayReferenceStructure(Equation equation) {
	this.equation = equation;
	rhsArrayReferences = new ArrayList<ArrayReference>();
	analyze();
    }
    
    public List<String> getOuterArraySubscriptDimensionPacked() {

	// given the subscripts on the LHS, return a pack info
	// List with all arrays that reference these subscripts

	List<String> al = new ArrayList<String>();

	// start with the LHS
	int dim = 0;
	for (String outerSubscript : lhsArrayReference.getSubscripts()) {
	    al.add(pack(lhsArrayReference.getArrayName(),outerSubscript,dim));
	    dim++;
	}

	// now RHS array references
	for (String outerSubscript : lhsArrayReference.getSubscripts()) {
	    for (ArrayReference ar : rhsArrayReferences) {
		List<String> subscripts = ar.getSubscripts();
		if (subscripts.contains(outerSubscript)) {
		    dim = subscripts.indexOf(outerSubscript);
		    String packed = pack(ar.getArrayName(),outerSubscript,dim);
		    if (!al.contains(packed))
			al.add(packed);
		}
	    }
	}


	return al;
    }
    
    public List<String> getMappedArraySubscriptDimensionPacked() {

	// given the subscripts on the LHS, return a pack info
	// List with all arrays that reference these subscripts

	List<String> al = new ArrayList<String>();
	if (!hasRHSmappedSubscripts())
	    return al;

	List<String> lhsSubscripts = lhsArrayReference.getSubscripts();

	// now for eachRHS array references
	for (ArrayReference ar : rhsArrayReferences) {
	    for (String rhsSubscript1 : ar.getSubscripts()) {
		// make sure that the range subscripts lose their "!"
		String rhsSubscript = rhsSubscript1.replace("!", "");
		// if this subscript is not found on the LHS, it may be a mapped
		// subscript (or something that is an artifcat of processing and
		// can be ignored.
		if (!lhsSubscripts.contains(rhsSubscript)) {
		    
		    // in what index position is the rhs subr
		    int dim = ar.getSubscripts().indexOf(rhsSubscript);
		    boolean foundMap = false;
		    
		    // build the shorthand map for mappings
		    // we know that there must/may be a mapping, so find it

		    for (int i = 0; i < lhsSubscripts.size(); i++) {
			String lhsSubr = lhsSubscripts.get(i);
			if (InformationManagers.getInstance().getMappedSubscriptManager().isMapBetween(rhsSubscript, lhsSubr)) {
			    foundMap = true;
			    String lhsPacked = pack(lhsArrayReference.getArrayName(), lhsSubr, i);
			    // this could be a terminal. Need to check
			    String packed = pack(ar.getArrayName(),rhsSubscript,dim);
			    if (!al.contains(packed))
				al.add(packed);
			    mappedSubscriptIndexMap.put(packed, lhsPacked);
			}

		    }
		    if (!foundMap) {
			if (!InformationManagers.getInstance().getArrayManager().isTerminalSubscript(ar.getArrayName(), rhsSubscript, dim)) {
//			    System.out.println("NOT TERMINAL: no map for "+rhsSubscript);
			}
		    }
		}
		
	    }

	}
	return al;
    }
    
    public List<String> getRangeArraySubscriptDimensionPacked() {

	// given the subscripts on the LHS, return a pack info
	// List with all arrays that do not reference these subscripts

	List<String> al = new ArrayList<String>();
	if (!hasRHSrangeSubscripts())
	    return al;

	// now RHS array references
	for (String rangeSubscript : getRHSrangeSubscripts()) {
	    for (ArrayReference ar : rhsArrayReferences) {
		List<String> subscripts = ar.getSubscripts();
		if (subscripts.contains(rangeSubscript)) {
		    int dim = subscripts.indexOf(rangeSubscript);
		    String packed = pack(ar.getArrayName(),rangeSubscript,dim);
		    if (!al.contains(packed))
			al.add(packed);
		}
	    }
	}
	


	return al;
    }
    
    public String getOuterLoops() {

	StringBuffer code = new StringBuffer();
	List<Integer> dimensionsRequiringIndexArray = new ArrayList<Integer>();

	// first create any index arrays if required
	if (requiresOuterIndexArray) {
	    dimensionsRequiringIndexArray = retrieveDimensions(arraysRequiringOuterIndexArray);
	    for (String packed : arraysRequiringOuterIndexArray) {
		String[] unpacked = unpack(packed);
		unpacked[0] = unpacked[0].replace("lookup.", "");
		if (!CodeGenerator.indexDefined(packed)) {
		    if (!Translator.target.equals(ReaderConstants.C))
			code.append("int[] ");
		    else
			code.append("int *");
		    code.append("indexArray_"+CodeGenerator.getLegalName(unpacked[0]+"_"+unpacked[1]+"_"+unpacked[2])+"_ = newIntArray("+
		    		InformationManagers.getInstance().getArrayManager().getNumIndicies(unpacked[0], Integer.parseInt(unpacked[2]))+","+
		    		InformationManagers.getInstance().getArrayManager().getIndicies(unpacked[0], Integer.parseInt(unpacked[2]), unpacked[1])+");\n");
		    CodeGenerator.defineIndex(packed);
		}
	    }
	}

	if (requiresMappedIndexArray) {
	    code.append(generateMappedIndexArrays());
	    //	    generateMappedIndexForLHS(String eqLHSarray, String eqLHSsubscript, int eqLHSdimension)
	    //	    generateMappedIndexForRHS(String eqLHSarray, String eqLHSsubscript, int eqLHSdimension, 
	    //		    String eqRHSarray, String eqRHSsubscript, int eqRHSdimension)
	}

	// for each dimension, generate the appropriate loop construct

	for (int dim = 0; dim < lhsArrayReference.getSubscripts().size(); dim++) {
	    String sub = lhsArrayReference.getSubscripts().get(dim);
	    if (dimensionsRequiringIndexArray.contains(dim)) {
		code.append("/* getOuterLoops 1 */\n");
//		code.append("for (int outer"+dim+" = 0; outer"+dim+" < "+NamedSubscriptManager.getNumIndexFor(sub)+"; outer"+dim+"++) {\n");
		
		code.append("int outer"+dim+";\n");
		code.append("for (outer"+dim+" = 0; outer"+dim+" < "+InformationManagers.getInstance().getNamedSubscriptManager().getNumIndexFor(sub)+"; outer"+dim+"++) {\n");
		
	    } else {
//		code.append("/* getOuterLoops 2 */\n");
//		code.append("for (int outer"+dim+" :  new int[] {"+
//			ArrayManager.getIndiciesSorted(lhsArrayReference.getArrayName(), dim, sub)+"}) {\n");
		String data = InformationManagers.getInstance().getArrayManager().getIndiciesSorted(lhsArrayReference.getArrayName(), dim, sub);
		int num = data.split(",").length;
		if (!Translator.target.equals(ReaderConstants.C))
		    code.append("int[] ");
		else
		    code.append("int* ");

		code.append("outer"+dim+"_index = newIntArray("+num+","+data+");\n");
		
		code.append("int outer"+dim+";\n");
		code.append("int touter"+dim+";\n");
		code.append("for (touter"+dim+" = 0; touter"+dim+" < "+InformationManagers.getInstance().getNamedSubscriptManager().getNumIndexFor(sub)+"; touter"+dim+"++) {\n");
		code.append("outer"+dim+" = outer"+dim+"_index[touter"+dim+"];\n");
	    }
	}


	return code.toString();
    }
    
    public String generateMappedIndexForRHS(String eqLHSarray, String eqLHSsubscript, int eqLHSdimension, 
	    String eqRHSarray, String eqRHSsubscript, int eqRHSdimension) {
	StringBuffer sb = new StringBuffer();
	
	List<String> lhs = new ArrayList<String>();
	List<String> rhs = new ArrayList<String>();
	
	// need to generate the sequence of lhs subscripts
	for (String s : InformationManagers.getInstance().getNamedSubscriptManager().getValuesFor(eqLHSsubscript)) {
	    lhs.add(s);
	}
	// for each of these, get the mapping
	for (String lh : lhs) {
	    rhs.add(InformationManagers.getInstance().getMappedSubscriptManager().getSubscriptMapping(eqLHSsubscript, lh, eqRHSsubscript));
	}
	
	    String index = "indexMapArray_"+CodeGenerator.getLegalName(eqRHSarray+"_"+eqRHSsubscript+"_"+eqRHSdimension) +
	    		"_[outer"+eqLHSdimension+"]";
	    sb.append(index);
	   
	
	return sb.toString();
    }
    
    public String generateMappedIndexArrays() {
	StringBuffer sb = new StringBuffer();
	
	for (String packed : arraysRequiringMappedIndexArray) {
		String[] unpacked = unpack(packed);
		if (!CodeGenerator.indexDefined(packed)) {
		    if (!Translator.target.equals(ReaderConstants.C))
			sb.append("int[] ");
		    else
			sb.append("int *");
		    int num = getMappedIndicies(packed).split(",").length;
		    sb.append("indexMapArray_"+CodeGenerator.getLegalName(unpacked[0]+"_"+unpacked[1]+"_"+unpacked[2])+"_ = newIntArray("+
			    num+","+
			    getMappedIndicies(packed)+");\n");
		    CodeGenerator.defineIndex(packed);

		}
	}
	
	return sb.toString();
    }
    
    private String getMappedIndicies(String rhsPacked) {
	StringBuffer sb = new StringBuffer();
	String lhsPacked = mappedSubscriptIndexMap.get(rhsPacked);
	String[] rhsUnpacked = unpack(rhsPacked);
	String[] lhsUnpacked = unpack(lhsPacked);
	List<String> orderedLHS = InformationManagers.getInstance().getArrayManager().getOrderedSubscriptNames(lhsUnpacked[0], Integer.parseInt(lhsUnpacked[2]), lhsUnpacked[1]);
	List<String> orderedRHS = new ArrayList<String>();
	for (String lhsSubr : orderedLHS) {
	    orderedRHS.add(InformationManagers.getInstance().getMappedSubscriptManager().getSubscriptMapping(lhsUnpacked[1], lhsSubr, rhsUnpacked[1]));
	}
	int index = 0;
	for (String rhsSubr : orderedRHS) {
	    if (index++ > 0)
		sb.append(", ");
	    sb.append(InformationManagers.getInstance().getArrayManager().getTerminalValue(rhsUnpacked[0], rhsSubr, Integer.parseInt(rhsUnpacked[2])));
	}
	
	
	return sb.toString();
	
    }
    
    public String generateMappedIndexForLHS(String eqLHSarray, String eqLHSsubscript, int eqLHSdimension) {
	StringBuffer sb = new StringBuffer();
	
	List<String> lhs = new ArrayList<String>();
	
	// need to generate the sequence of lhs subscripts
	for (String s : InformationManagers.getInstance().getNamedSubscriptManager().getValuesFor(eqLHSsubscript)) {
	    lhs.add(s);
	}
	
	sb.append("// This will be a mapped range index array\n// ");
	for (String s : lhs)
	    sb.append(","+s);
	sb.append("\n");
	
	
	return sb.toString();
    }
    
    public boolean requiresIndirection(String arrayName, String subscript, int dimension) {
	String packed = pack(arrayName, subscript, dimension);
	if (arraysRequiringOuterIndexArray.contains(packed)) {
	    return true;
	} else {
	    return false;
	}
    }
    
    public String getSubscriptForImplementation(String arrayName, String subscript, int dimension) {

	// Handle only outer s here!!!
	// will return one of four possiblities:
	//
	// outer#
	// range#
	// indexArray_Embodied_cost_of_energy_supply_capacity_EnergyPath_0_[outer#]
	// indexArray_Embodied_cost_of_energy_supply_capacity_EnergyPath_0_[range#]2
	
	String subr = "";
	String packed = pack(arrayName, subscript, dimension);
	int index = getOuterIndex(arrayName, subscript, dimension);
	if (isOuterSubscript(arrayName, subscript, dimension)) {
	    // is it in our list of arrays requiring an index array?
	    if (arraysRequiringOuterIndexArray.contains(packed)) {
		
		    subr = "indexArray_"+CodeGenerator.getLegalName(arrayName+"_"+subscript+"_"+dimension)+"_[outer"+index+"]";
		   
	    } else {
		subr = "outer"+index;
	    }
	} else {
	    // if the subscripts don't match, then it could map to any of the lhs subscripts
	    int lhsSubrIndex = 0;
	    for (String lhsSubr : lhsArrayReference.getSubscripts()) {
		if (InformationManagers.getInstance().getMappedSubscriptManager().isMapBetween(subscript, lhsSubr)) {
		    subr = generateMappedIndexForRHS(this.lhsArrayReference.getArrayName(), lhsSubr, lhsSubrIndex, 
			    arrayName, subscript, dimension);
		    

		}
		lhsSubrIndex++;
	    }
	}
	return subr;

    }
    
    public String getRangeSubscriptForImplementation(String arrayName, String subscript, int dimension) {
	
	// Handle only outer s here!!!
	// will return one of four possiblities:
	//
	// outer#
	// range#
	// indexArray_Embodied_cost_of_energy_supply_capacity_EnergyPath_0_[outer#]
	// indexArray_Embodied_cost_of_energy_supply_capacity_EnergyPath_0_[range#]2
	
	String subr = "";
	String noBang = subscript.replace("!", "");
	String packed = pack(arrayName, subscript, dimension);
	int index = getRangeIndexFor(subscript); //getRangeIndex(arrayName, noBang, dimension);
//	if (isRangeSubscript(arrayName, subscript, dimension)) {
	    // is it in our list of arrays requiring an index array?
	if (arraysRequiringRangeIndexArray.contains(packed)) {
	    
		subr = "indexArray_"+CodeGenerator.getLegalName(arrayName+"_"+noBang+"_"+dimension)+"_[range"+index+"]";
		CodeGenerator.defineIndex(packed);
	  
	} else {
		subr = "range"+index;
	    }
//	}
	return subr;
	
    }
    
    public int getRangeSubscriptIndexForImplementation(String arrayName, String subscript, int dimension) {
	
	// Handle only outer s here!!!
	// will return one of four possiblities:
	//
	// outer#
	// range#
	// indexArray_Embodied_cost_of_energy_supply_capacity_EnergyPath_0_[outer#]
	// indexArray_Embodied_cost_of_energy_supply_capacity_EnergyPath_0_[range#]2
	
	String subr = "";
	String noBang = subscript.replace("!", "");
	String packed = pack(arrayName, subscript, dimension);
	int index = getRangeIndex(arrayName, noBang, dimension);
	return index;

    }
    
    public String getMappedSubscriptForImplementation(String arrayName, String subscript, int dimension) {
	
	// Handle only outer s here!!!
	// will return one of four possiblities:
	//
	// outer#
	// range#
	// indexArray_Embodied_cost_of_energy_supply_capacity_EnergyPath_0_[outer#]
	// indexArray_Embodied_cost_of_energy_supply_capacity_EnergyPath_0_[range#]2
	
	String subr = "";
	String noBang = subscript.replace("!", "");
	String packed = pack(arrayName, subscript, dimension);
	int index = getRangeIndex(arrayName, noBang, dimension);
//	if (isRangeSubscript(arrayName, subscript, dimension)) {
	    // is it in our list of arrays requiring an index array?
	    if (arraysRequiringRangeIndexArray.contains(packed)) {
		subr = "indexMapArray_"+CodeGenerator.getLegalName(arrayName+"_"+noBang+"_"+dimension)+"_[range"+index+"]";
	    } else {
		subr = "range"+index;
	    }
//	}
	return subr;
	
    }

    public boolean isOuterSubscript(String arrayName, String subscript, int dimension) {
	// just see if this is int the subscript list for LHS
	if (lhsArrayReference == null)
	    return false;
	return lhsArrayReference.getSubscripts().contains(subscript);

    }
    
//    public boolean isRangeSubscript(String arrayName, String subscript, int dimension) {
//	// just see if this is int the subscript list for LHS
//	if (rhsArrayReferences.size() == 0)
//	    return false;
//	return rhsArrayReferences.contains(subscript);
//
//    }
    
    public int getRangeIndex(String arrayName, String subscript, int dimension) {
	
	int index = 0;
	for (String sub : getUniqueRHSarrayRangeSubscripts()) {
	    if (sub.equals(subscript))
		return index;
	    index++;
	}
	// should never happend
	return -1;
	
    }
    public int getOuterIndex(String arrayName, String subscript, int dimension) {
	
	int index = 0;
	
	if (lhsArrayReference == null) {
		System.out.println("LHS Reference is null");
		System.out.println("ArrayName: "+arrayName);
		System.out.println("subscript: "+subscript);
		System.out.println("dimension: "+dimension);
	}
	
	for (String sub : lhsArrayReference.getSubscripts()) {
	    if (sub.equals(subscript))
		return index;
	    index++;
	}
	// should never happend
	return -1;
	
    }
    
    public String getLHSsubscript() {
	StringBuffer code = new StringBuffer();
	 
	 
	    for (int subNum = 0; subNum < lhsArrayReference.getSubscripts().size(); subNum++) {
//		code.append("[outer"+subNum+"]");
		if (subNum > 0)
		    code.append("+");
		code.append("\"[\"+"+"outer"+subNum+"+\"]\"");
	    }
	    
	    return code.toString();
    }
    /**
     * used only for logging purposes
     * @return
     */
    public String getLHSassignmentName() {
	StringBuffer code = new StringBuffer();
	
//	stringConcat("memory.SLR_in_2000","[",intToString(outer0),"]")
	
	code.append("stringConcat(");
	code.append("\"");
	code.append(InformationManagers.getInstance().getNativeDataTypeManager().getOriginalName(InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(lhsArrayReference.getArrayName())));
	code.append("\"");

	List<Integer> dimensionsRequiringIndexArray = new ArrayList<Integer>();

	// first create any index arrays if required
	if (requiresOuterIndexArray) {
	    dimensionsRequiringIndexArray = retrieveDimensions(arraysRequiringOuterIndexArray);
	}
	for (int dim = 0; dim < lhsArrayReference.getSubscripts().size(); dim++) {
	    String sub = lhsArrayReference.getSubscripts().get(dim);
	    if (!dimensionsRequiringIndexArray.contains(dim)) {
		code.append(",\"[\",intToString(outer"+dim+"),\"]\"");
	    } else {
		code.append(",\"[\"+"+getSubscriptForImplementation(lhsArrayReference.getArrayName(), sub, dim)+",\"]\"");

	    }
	}
	code.append(")");
	return code.toString();
    }
    
    public String getLHSassignment() {
	StringBuffer code = new StringBuffer();
	code.append(InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(lhsArrayReference.getArrayName()));
	// need to check for index array usage
	//	    for (int subNum = 0; subNum < lhsArrayReference.getSubscripts().size(); subNum++) {
	//		code.append("[outer"+subNum+"]");
	//	    }

	List<Integer> dimensionsRequiringIndexArray = new ArrayList<Integer>();

	// first create any index arrays if required
	if (requiresOuterIndexArray) {
	    dimensionsRequiringIndexArray = retrieveDimensions(arraysRequiringOuterIndexArray);
	}
	for (int dim = 0; dim < lhsArrayReference.getSubscripts().size(); dim++) {
	    String sub = lhsArrayReference.getSubscripts().get(dim);
	    if (!dimensionsRequiringIndexArray.contains(dim)) {
		code.append("[outer"+dim+"]");
	    } else {
		code.append("["+getSubscriptForImplementation(lhsArrayReference.getArrayName(), sub, dim)+"]");

	    }
	}
	return code.toString();
    }

//    public boolean isRangeSubscript(String arrayName, String subscript, int dimension) {
//
//    }
//
//    public int getRangeIndex(String arrayName, String subscript, int dimension) {
//
//    }
    
    // need one of these based on a list of array references
    public String getRangeLoops(List<Node> arrayReferences) {
	StringBuffer code = new StringBuffer();
	
	if (!hasRHSrangeArrayReference)
	    return code.toString();
	
	if (arrayReferences.size() == 0)
	    return code.toString();

	// have range loops

	List<Integer> dimensionsRequiringIndexArray = new ArrayList<Integer>();
	Set<String> arSet = new HashSet<String>();
	for (Node node : arrayReferences) {
	    ArrayReference ar = new ArrayReference(node.getToken());
	    String arrayName = ar.getArrayName();
	    for (int i = 0; i < ar.getSubscripts().size(); i++) {
		String subr = ar.getSubscripts().get(i);
		if (ArrayReference.isRangeSubscript(subr))
		    arSet.add(pack(arrayName, subr, i));
	    }
	}

	// first create any index arrays if required
	if (requiresRangeIndexArray) {
	    dimensionsRequiringIndexArray = retrieveDimensions(arraysRequiringRangeIndexArray);
	    for (String packed : arraysRequiringRangeIndexArray) {
		if (!arSet.contains(packed))
		    continue;
		String[] unpacked = unpack(packed);
		unpacked[1] = unpacked[1].replace("!", "");
		
		//
		
		if (!CodeGenerator.indexDefined(pack(unpacked[0],unpacked[1],unpacked[2])) && !localIndexArrays.contains(pack(unpacked[0],unpacked[1],unpacked[2]))) {
//		    code.append("int[] indexArray_"+CodeGenerator.getLegalName(unpacked[0]+"_"+unpacked[1]+"_"+unpacked[2])+"_ = new int[] {"+
//			    ArrayManager.getIndicies(unpacked[0], Integer.parseInt(unpacked[2]), unpacked[1])+"};\n");
		    if (!Translator.target.equals(ReaderConstants.C))
			code.append("int[] ");
		    else
			code.append("int *");
		    code.append("indexArray_"+CodeGenerator.getLegalName(unpacked[0]+"_"+unpacked[1]+"_"+unpacked[2])+"_ = newIntArray("+
		    		InformationManagers.getInstance().getArrayManager().getNumIndicies(unpacked[0], Integer.parseInt(unpacked[2]))+","+
		    		InformationManagers.getInstance().getArrayManager().getIndicies(unpacked[0], Integer.parseInt(unpacked[2]), unpacked[1])+");\n");
		    localIndexArrays.add(pack(unpacked[0],unpacked[1],unpacked[2]));
		    
		    // If the lhs ar not null, we know that the index array is defined within an outer loop so this
		    // index array cannot be reused by others
		    // is null, then it can be reused
		    
		    if (lhsArrayReference == null)
			CodeGenerator.defineIndex(pack(unpacked[0],unpacked[1],unpacked[2]));
		}
	    }
	}
	
	// MJB 10/2/2012
	code.append("{ /* MJB Range Issues*/ \n");

	// for each dimension, generate the appropriate loop construct
	// subrs are packed -- we are not properly handling this!

	//	Here!

	List<String> subrs = getUniqueRHSarrayRangeSubscripts();
//	for (int subIndex = 0; subIndex < subrs.size(); subIndex++) {
	for (String sub : subrs) {
	    String noBang = sub;
	String subr = sub + "!";
	    
	    // does this subscript exist ibn arSet?
	    if (!subscriptExistsIn(subr, arSet)) {
		continue;
	    }
	    int subIndex = rangeIndexMap.get(subr);
	    code.append("int range"+subIndex+";\n");
	    if (!dimensionsRequiringIndexArray.contains(subIndex)) {
		
		code.append("for (range"+subIndex+" = 0; range"+subIndex+" < "+InformationManagers.getInstance().getNamedSubscriptManager().getNumIndexFor(subr.replace("!", ""))+"; range"+subIndex+"++) {\n");
	    } else {
//		/* EARS 598 */ for (range0 :  new int[] {0, 1, 2, 3, 4, 5, 6}) {
//		code.append("for (range"+subIndex+" :  new int[] {"+
//			ArrayManager.getIndiciesSorted(getArrayWithSubscriptDimension(arraysRequiringRangeIndexArray, subr, subIndex), subIndex, subr)+"}) {\n");
		if (!Translator.target.equals(ReaderConstants.C))
			code.append("int[] ");
		    else
			code.append("int *");
		String numbers = InformationManagers.getInstance().getArrayManager().getIndiciesSorted(getArrayWithSubscriptDimension(arraysRequiringRangeIndexArray, subr, subIndex), subIndex, subr);
		    int num = numbers.split(",").length;
		    
		    code.append("range"+subIndex+"_index = newIntArray("+num+","+numbers+");\n");
		    code.append("int trange"+subIndex+";\n");
		    code.append("for (trange"+subIndex+" = 0; trange"+subIndex+" < "+num+"; trange"+subIndex+"++) {\n");
		    code.append("range"+subIndex+" = "+"range"+subIndex+"_index["+"trange"+subIndex+"];\n");
		    
	    }
	}

	return code.toString();
    }
    
    private boolean subscriptExistsIn(String sub, Set<String> arSet) {
	
	for (String packed : arSet) {
	    String subr = unpack(packed)[1].replace("!", "");
	    if (sub.replace("!", "").equals(subr))
		return true;
	}
	
	return false;
    }
    
    public String getRangeLoopsXXX(/* Node node */) {
	StringBuffer code = new StringBuffer();
	
	if (!hasRHSrangeArrayReference)
	    return code.toString();

	// have range loops

	List<Integer> dimensionsRequiringIndexArray = new ArrayList<Integer>();

	// first create any index arrays if required
	if (requiresRangeIndexArray) {
	    dimensionsRequiringIndexArray = retrieveDimensions(arraysRequiringRangeIndexArray);
	    for (String packed : arraysRequiringRangeIndexArray) {
		String[] unpacked = unpack(packed);
		unpacked[1] = unpacked[1].replace("!", "");
		
		//
		
		if (!CodeGenerator.indexDefined(pack(unpacked[0],unpacked[1],unpacked[2])) && !localIndexArrays.contains(pack(unpacked[0],unpacked[1],unpacked[2]))) {
//		    code.append("int[] indexArray_"+CodeGenerator.getLegalName(unpacked[0]+"_"+unpacked[1]+"_"+unpacked[2])+"_ = new int[] {"+
//			    ArrayManager.getIndicies(unpacked[0], Integer.parseInt(unpacked[2]), unpacked[1])+"};\n");
		    
		    if (!Translator.target.equals(ReaderConstants.C))
			code.append("int[] ");
		    else
			code.append("int *");
		    code.append("indexArray_"+CodeGenerator.getLegalName(unpacked[0]+"_"+unpacked[1]+"_"+unpacked[2])+"_ = newIntArray("+
		    		InformationManagers.getInstance().getArrayManager().getNumIndicies(unpacked[0], Integer.parseInt(unpacked[2]))+","+
		    		InformationManagers.getInstance().getArrayManager().getIndicies(unpacked[0], Integer.parseInt(unpacked[2]), unpacked[1])+");\n");
		    
		    localIndexArrays.add(pack(unpacked[0],unpacked[1],unpacked[2]));
		    
		    // If the lhs ar not null, we know that the index array is defined within an outer loop so this
		    // index array cannot be reused by others
		    // is null, then it can be reused
		    
		    if (lhsArrayReference == null)
			CodeGenerator.defineIndex(pack(unpacked[0],unpacked[1],unpacked[2]));
		}
	    }
	}

	// for each dimension, generate the appropriate loop construct
	// subrs are packed -- we are not properly handling this!

	//	Here!

	List<String> subrs = getUniqueRHSarrayRangeSubscripts();
	for (int subIndex = 0; subIndex < subrs.size(); subIndex++) {
	    String sub = subrs.get(subIndex);
	    code.append("int range"+subIndex+";\n");
	    if (!dimensionsRequiringIndexArray.contains(subIndex)) {
		code.append("for (range"+subIndex+" = 0; range"+subIndex+" < "+InformationManagers.getInstance().getNamedSubscriptManager().getNumIndexFor(sub)+"; range"+subIndex+"++) {\n");
	    } else {
		code.append("/* EARS 662 */ for (range"+subIndex+" :  new int[] {"+
				InformationManagers.getInstance().getArrayManager().getIndiciesSorted(getArrayWithSubscriptDimension(arraysRequiringRangeIndexArray, sub, subIndex), subIndex, sub)+"}) {\n");
	    }
	}

	return code.toString();
    }
    
    public String getArrayWithSubscriptDimension(List<String> packed, String sub, int dimension) {
	// we can retrieve any array that matched
	for (String s : packed) {
	    String[] unpacked = unpack(s);
//	    unpacked[1] = unpacked[1].replace("!", "");
	    if (unpacked[1].equals(sub) && Integer.parseInt(unpacked[2]) == dimension)
		    return  unpacked[0];
	}
	return "*ERROR*";
    }
    
    public List<Integer> retrieveDimensions(List<String> packed) {
	List<Integer> al = new ArrayList<Integer>();
	for (String p : packed) {
	    String[] unpacked = unpack(p);
	    Integer dim = Integer.parseInt(unpacked[2]);
	    if (!al.contains(dim))
		al.add(dim);
	}
	return al;
    }
    
//    public String getRangeLoops() {
//	StringBuffer sb = new StringBuffer();
//	
//	
//	
//	return sb.toString();
//    }
    
    public int getOuterClosingCount() {
	return lhsArrayReference.getSubscripts().size();
    }
    
    public int getRangeClosingCount() {
	return getRHSrangeSubscripts().size();
    }
    
    public int getRangeClosingCount(List<Node> arrayReferences) {
	int closingParens = 0;
	
	Set<String> arSet = new HashSet<String>();
	for (Node node : arrayReferences) {
	    ArrayReference ar = new ArrayReference(node.getToken());
	    String arrayName = ar.getArrayName();
	    for (int i = 0; i < ar.getSubscripts().size(); i++) {
		String subr = ar.getSubscripts().get(i);
		if (ArrayReference.isRangeSubscript(subr))
		    arSet.add(pack(arrayName, subr, i));
	    }
	}
	
	List<String> subrs = getUniqueRHSarrayRangeSubscripts();
	for (int subIndex = 0; subIndex < subrs.size(); subIndex++) {
	    String sub = subrs.get(subIndex);
	    // does this subscript exist ibn arSet?
	    if (!subscriptExistsIn(sub, arSet)) {
		continue;
	    }
	    closingParens++;
	}
	return closingParens;
    }
    
    
    
    private String pack(String arrayName, String subscript, int dimension) {
	return arrayName+"###"+subscript+"###"+dimension;
    }
    
    private String pack(String arrayName, String subscript, String dimension) {
	return arrayName+"###"+subscript+"###"+dimension;
    }
    
    private void analyze() {

    	equation.printTokensOneLine();
	List<Node> al = equation.getTreeAsList();
	if (al == null) {
		
		String lhs = equation.getTokens().get(0);
		if (lhs != null && ArrayReference.isArrayReference(lhs)) {
			hasLHSarrayReference = true;
			lhsArrayReference = new ArrayReference(lhs);
		}
		
		return;
	}

	if (equation.isArrayInitialization()) {
		
		arrayInitialization = true;
		// 
		Node lhs = al.get(0);
		numberArrayInitializers = al.size()-1;
		if (lhs != null && ArrayReference.isArrayReference(lhs.getToken())) {
			hasLHSarrayReference = true;
			lhsArrayReference = new ArrayReference(lhs.getToken());
			
		}
	} else {
		

		// LHS
		Node lhs = al.get(0);
		if (lhs != null && ArrayReference.isArrayReference(lhs.getToken())) {
			hasLHSarrayReference = true;
			lhsArrayReference = new ArrayReference(lhs.getToken());
			
		}
		for (int i = 1; i < al.size(); i++) {
			Node n = al.get(i);
			if (n != null && ArrayReference.isArrayReference(n.getToken())) {

				hasRHSarrayReference = true;
				rhsArrayReferences.add(new ArrayReference(n.getToken()));
				
			}
		}

		// Step 1 -- determine if we need outer looping separate index arrays for the array references
		requiresOuterIndexArray = this.needOuterIndexArray();
		// Step 2 -- determine if we need range looping and separate indexes
		// note that there can be multiple RHS references to the same subscript
		// we currently save the name of the array -- think we need to not store arrayName
		if (hasRHSrangeSubscripts())
			hasRHSrangeArrayReference = true;

		if (hasRHSarray() && hasRHSrangeSubscripts())
			requiresRangeIndexArray = this.needRangeIndexArray();

		// Step 3 -- determine if we need subscript mapping
		requiresMappedIndexArray = this.needMappedIndexArray();
		hasRHSmappedArrayReference = requiresMappedIndexArray;
	}
    }
    
    private String getIndicies(String packed) {
	String[] unpacked = unpack(packed);
	 return InformationManagers.getInstance().getArrayManager().getIndicies(unpacked[0], Integer.parseInt(unpacked[2]), unpacked[1]);
    }
    
    private String[] unpack(String packed) {
	String[] unpacked = packed.split("###");
	return unpacked;
    }
    
    public boolean hasLHSarray() {
	if (lhsArrayReference == null) {
	    return false;
	} else {
	    return true;
	}
    }
    
    public boolean hasRHSarray() {
	return rhsArrayReferences.size() > 0 ? true : false;
    }
    
    public boolean hasRHSrangeSubscripts() {
	
	boolean hasRangeSubscript = false;
	int index = 0;

	for (ArrayReference ar : rhsArrayReferences) {
	    if (ar.hasRangeSubscript()) {
		hasRangeSubscript = true;
		for (String subscript : ar.getRangeSubscripts()) {
		    if (!rangeIndexMap.containsKey(subscript)) {
			rangeIndexMap.put(subscript, index++);
		    }
		}
	    }
	}
	return hasRangeSubscript;

    }
    
    public int getRangeIndexFor(String rangeSubscript) {
	if (rangeIndexMap == null || rangeSubscript == null) {
	    System.out.println("WTF!");
	}
	return rangeIndexMap.get(rangeSubscript);
    }
    
    public boolean hasRHSmappedSubscripts() {
	
	if (lhsArrayReference == null)
	    return false;

	List<String> lhsSubscripts = lhsArrayReference.getSubscripts();

	for (ArrayReference ar : rhsArrayReferences) {
	    for (String rhsSubscripts : ar.getSubscripts()) {
		if (!lhsSubscripts.contains(rhsSubscripts))
		    return true;
	    }
	}
	return false;

    }
    
    public List<String> getUniqueRHSarrayRangeSubscripts() {
	rangeSubscriptNames = new ArrayList<String>();
	for (ArrayReference ar : rhsArrayReferences) {
	    if (ar.hasRangeSubscript()) {
		for (String rSub : ar.getUniqueRangeSubscriptsNames()) {
		    if (!rangeSubscriptNames.contains(rSub))
			rangeSubscriptNames.add(rSub);
		}

	    }
	}
	
	return rangeSubscriptNames;
    }
    
    public List<String> getRHSarrayRangeSubscripts() {
	rangeSubscriptNames = new ArrayList<String>();
	for (ArrayReference ar : rhsArrayReferences) {
	    if (ar.hasRangeSubscript()) {
		for (String r : ar.getRangeSubscriptsWithDimension()) {
		    String pair = ar.getArrayName() +"###"+r;
		    if (!rangeSubscriptNames.contains(pair))
			rangeSubscriptNames.add(pair);
		}
	    }
	}
	
	return rangeSubscriptNames;
    }
    
    public List<String> getRHSrangeSubscripts() {
	rangeSubscriptNames = new ArrayList<String>();
	for (ArrayReference ar : rhsArrayReferences) {
	    if (ar.hasRangeSubscript()) {
		for (String r : ar.getRangeSubscripts())
		    if (!rangeSubscriptNames.contains(r))
			rangeSubscriptNames.add(r);
	    }
	}
	
	return rangeSubscriptNames;
    }
    
    public int getOuterSubscriptNumber(String subscript) {
	int pos = 0;
	for (String sub : lhsArrayReference.getSubscripts()) {
	    if (sub.equals(subscript))
		return pos;
	    pos++;
	}
	return -1;
    }
    
    public int getRangeSubscriptNumber(String subscript) {
	int pos = 0;
	if (rangeSubscriptNames == null)
	    getRHSrangeSubscripts();
	for (String sub : rangeSubscriptNames) {
	    if (sub.equals(subscript))
		return pos;
	    pos++;
	}
	return -1;
    }
    
    public boolean needRangeIndexArray() {
	
	boolean need = false;
    
	List<String> packedInfo = getRangeArraySubscriptDimensionPacked();
	
	// first determine if we need multiple index arrays
	for (String packed : packedInfo) {
	    String[] info = packed.split("###");
	    info[1] = info[1].replace("!", "");
	    if (!uniqueBySubscript.containsKey(info[1])) {
		uniqueBySubscript.put(info[1], new ArrayList<String>());
	    }
	    List<String> bySubscript = uniqueBySubscript.get(info[1]);
	    String indicies = InformationManagers.getInstance().getArrayManager().getIndicies(info[0], Integer.parseInt(info[2]), info[1]);
	    if (!bySubscript.contains(indicies))
		bySubscript.add(indicies);
	}
	
	// create index arrays only when needed
	for (String packed : packedInfo) {
	    String[] info = packed.split("###");
	    info[1] = info[1].replace("!", "");
	    List<String> al = uniqueBySubscript.get(info[1]);
	    if (al.size() > 1) {
		arraysRequiringRangeIndexArray.add(packed);
		need = true;
	    }
	}
	return need;

}
    
    public boolean needMappedIndexArray() {
	
	boolean need = false;
    
	List<String> packedInfo = getMappedArraySubscriptDimensionPacked();
	
	// create index arrays only when needed
	for (String packed : packedInfo) {
		arraysRequiringMappedIndexArray.add(packed);
		need = true;
	}
	return need;

}
    
    public boolean needOuterIndexArray() {
	
	
	boolean need = false;
	if (lhsArrayReference == null)
	    return need;
    
	// move this code to ears.
	// need to maintain info between ArrayRefs -> this is tied to ears within equation?
	List<String> packedInfo = getOuterArraySubscriptDimensionPacked();
	
	// first determine if we need multiple index arrays
	for (String packed : packedInfo) {
	    String[] info = packed.split("###");
	    if (!uniqueBySubscript.containsKey(info[1])) {
		uniqueBySubscript.put(info[1], new ArrayList<String>());
	    }
	    List<String> bySybscript = uniqueBySubscript.get(info[1]);
	    String indicies = InformationManagers.getInstance().getArrayManager().getIndicies(info[0], Integer.parseInt(info[2]), info[1]);
	    if (!bySybscript.contains(indicies))
		bySybscript.add(indicies);
	}
	
	// create index arrays only when needed
	for (String packed : packedInfo) {
	    String[] info = packed.split("###");
	    if (uniqueBySubscript.get(info[1]).size() > 1) {
		arraysRequiringOuterIndexArray.add(packed);
		need = true;
	    }
	}
	

//	    int subNum = 0;
//	    for (String sub : subscripts) {
//		List<String> unique = uniqueBySubscript.get(sub);
//		if (unique.size() > 1) {
//		    code.append("for (int outer"+subNum+" = 0; outer"+subNum+" < "+NamedSubscriptManager.getNumIndexFor(sub)+"; outer"+subNum+"++) {\n");
//		} else {
//		    code.append("for (int outer"+subNum+" :  new int[] {"+
//		    ArrayManager.getIndicies(arrayName, subNum, sub)+"} {\n");
//		}
//		subNum++;
//	    }
	return need;

}

    public boolean isHasLHSarrayReference() {
        return hasLHSarrayReference;
    }

    public void setHasLHSarrayReference(boolean hasLHSarrayReference) {
        this.hasLHSarrayReference = hasLHSarrayReference;
    }

    public boolean isHasRHSarrayReference() {
        return hasRHSarrayReference;
    }

    public void setHasRHSarrayReference(boolean hasRHSarrayReference) {
        this.hasRHSarrayReference = hasRHSarrayReference;
    }

    public boolean isHasRHSrangeArrayReference() {
        return hasRHSrangeArrayReference;
    }

    public void setHasRHSrangeArrayReference(boolean hasRHSrangeArrayReference) {
        this.hasRHSrangeArrayReference = hasRHSrangeArrayReference;
    }

    public boolean isHasRHSmappedArrayReference() {
        return hasRHSmappedArrayReference;
    }

    public void setHasRHSmappedArrayReference(boolean hasRHSmappedArrayReference) {
        this.hasRHSmappedArrayReference = hasRHSmappedArrayReference;
    }

    public ArrayReference getLhsArrayReference() {
        return lhsArrayReference;
    }

    public void setLhsArrayReference(ArrayReference lhsArrayReference) {
        this.lhsArrayReference = lhsArrayReference;
    }

    public List<ArrayReference> getRhsArrayReferences() {
        return rhsArrayReferences;
    }

    public void setRhsArrayReferences(List<ArrayReference> rhsArrayReferences) {
        this.rhsArrayReferences = rhsArrayReferences;
    }

    public boolean isRequiresOuterIndexArray() {
        return requiresOuterIndexArray;
    }

    public void setRequiresOuterIndexArray(boolean requiresOuterIndexArray) {
        this.requiresOuterIndexArray = requiresOuterIndexArray;
    }

    public boolean isRequiresRangeIndexArray() {
        return requiresRangeIndexArray;
    }

    public void setRequiresRangeIndexArray(boolean requiresRangeIndexArray) {
        this.requiresRangeIndexArray = requiresRangeIndexArray;
    }

    public boolean isRequiresMappedIndexArray() {
        return requiresMappedIndexArray;
    }

    public void setRequiresMappedIndexArray(boolean requiresMappedIndexArray) {
        this.requiresMappedIndexArray = requiresMappedIndexArray;
    }
}
