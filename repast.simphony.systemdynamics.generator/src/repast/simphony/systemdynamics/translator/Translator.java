package repast.simphony.systemdynamics.translator;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;

import repast.simphony.systemdynamics.support.ArrayReference;
import repast.simphony.systemdynamics.support.MappedSubscriptManager;
import repast.simphony.systemdynamics.support.NamedSubscriptManager;
import repast.simphony.systemdynamics.support.SubscriptCombination;
import repast.simphony.systemdynamics.support.Utilities;

//import repast.simphony.engine.environment.RunEnvironment;
//import repast.simphony.parameter.Parameters;

public class Translator {



	protected Reader reader;

	//	private HashMap<String, Equation> equations;
	//    private List<String> evaluationOrder;

	public static String target = "UNDEFINED";
	public static Properties PROPERTIES = new Properties();
	public static Properties UNITS_PROPERTIES = new Properties();
	//    public static boolean useNativeDataTypes = true;

	protected String objectName;
	protected String dataType;
	protected String destinationDirectory;
	protected String miscDirectory;
	protected String packageName;
	protected String supportName;

	protected String unitsConsistencyCheckMdlFile = "";
	protected String unitsConsistencyCheckResultsFile = "./UnitsConsistencyCheckResults.txt";

	protected boolean unitsConsistency;

	protected SystemDynamicsObjectManager sdObjectManager;

	public SystemDynamicsObjectManager getSdObjectManager() {
		return sdObjectManager;
	}

	protected boolean generateC;
	protected boolean generateJava;

	protected static String DEFAULT_PROPERTIES_FILE = "DefaultConverter.properties";
	protected static String UNITS_PROPERTIES_FILE = "units.properties";

	public Translator() {
		//	this.loadProperties();
		//	this.loadUnitsProperties();
		//	ReaderConstants.OUTPUT_DIRECTORY = PROPERTIES.getProperty("outputDirectory");
		//	ReaderConstants.GENERATED_CODE_DIRECTORY = PROPERTIES.getProperty("generatedCodeDirectory");
		//	ReaderConstants.PACKAGE = PROPERTIES.getProperty("package");
		//	ReaderConstants.SUPPORT = PROPERTIES.getProperty("support");
		//	FunctionManager.load(PROPERTIES.getProperty("functionFile"));
		//	
		//	loadUnitsEquivalences();
		//	initialize();
	}

	public Translator(String mdlFile, String objectName, String target, String dataStructure) {
		this();
		reader = new Reader(mdlFile);
		this.objectName = objectName;

		if (target.equalsIgnoreCase(ReaderConstants.JAVA))
			Translator.target = ReaderConstants.JAVA;
		else if (target.equalsIgnoreCase(ReaderConstants.JAVASCRIPT))
			Translator.target = ReaderConstants.JAVASCRIPT;
		else if (target.equalsIgnoreCase(ReaderConstants.C))
			Translator.target = ReaderConstants.C;

	}

	public Translator(String mdlFile, String resultsFile) {
		// execute translator through units consistency
		// no code will be generated

		unitsConsistencyCheckMdlFile = mdlFile;
		unitsConsistencyCheckResultsFile = resultsFile;

		reader = new Reader(unitsConsistencyCheckMdlFile);

		this.loadProperties();
		this.loadUnitsProperties();
		ReaderConstants.OUTPUT_DIRECTORY = "DontCare";
		ReaderConstants.GENERATED_CODE_DIRECTORY = "DontCare";
//		ReaderConstants.PACKAGE = "DontCare";
//		ReaderConstants.SUPPORT = "DontCare";
		InformationManagers.getInstance().getFunctionManager().load(PROPERTIES.getProperty("functionFile"));
		loadUnitsEquivalences();
		initializeForConsistencyCheck();

	}

	//    public static void main(String[] args) {
	//
	//	Translator translator = new Translator(args[0], args[1], args[2], args[3]);
	//	translator.execute();
	//    }

	public void checkUnitsConsistency() {
		List<String> rawEquations = reader.readMDLFile();
		sdObjectManager = new SystemDynamicsObjectManager();
		HashMap<String, Equation> equations = new EquationProcessor().processRawEquations(sdObjectManager, rawEquations);
		processSubscriptDefinition(equations);
		processExponentiaion(equations);
		generateRPN(equations);
		generateTrees(equations);
		InformationManagers.getInstance().getArrayManager().populateArraySubscriptSpace();
		InformationManagers.getInstance().getUnitsManager().performUnitsConsistencyCheck(equations, unitsConsistencyCheckResultsFile);
	}

	public void initialize() {
	}

	public void initializeForConsistencyCheck() {

		this.objectName = "DontCare";
		Translator.target = "Java";
		this.dataType = "Arrays";
		this.destinationDirectory = "DontCare";
		this.miscDirectory = "DontCare";
		this.packageName = "DontCare";
		this.supportName = "DontCare";

		this.unitsConsistency = true;
		this.generateC = false;
		this.generateJava = false;

		Translator.target = ReaderConstants.JAVA;

		System.out.println("**** Units Consistency ****");
		System.out.println("MDL File: "+unitsConsistencyCheckMdlFile);
		System.out.println("Results File: "+unitsConsistencyCheckResultsFile);
		System.out.println("********");

	}

	protected void loadUnitsEquivalences() {
		BufferedReader unitsReader = openForRead(UNITS_PROPERTIES.getProperty("unitsEquivalenceFile"));


		String aLine = null;
		try {
			aLine = unitsReader.readLine();
			while (aLine != null) {
				String[] equiv = aLine.split("=");
				InformationManagers.getInstance().getUnitsManager().addEquivalence(equiv[0], equiv[1]);
				aLine = unitsReader.readLine();
			}
			unitsReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public void startProcess() {
		List<String> mdlContents = reader.readMDLFile();
		boolean success = execute(mdlContents);
		if (!success)
    		System.out.println("Errors Prevent completion of operation");
	}
	
	private void printErrors(Map<String, Equation> errors) {
		Iterator<String> iter = errors.keySet().iterator();
		while (iter.hasNext()) {
			String lhs = iter.next();
			Equation eqn = errors.get(lhs);
			System.out.println("ERROR: "+eqn.getVensimEquation());
			System.out.println("ERROR: "+eqn.getTokensOneLine());
			for (String msg : eqn.getSyntaxMessages()) {
				System.out.println(msg);
			}
			for (String msg : eqn.getSemanticMessages()) {
				System.out.println(msg);
			}

		}
	}
	
	protected boolean validateGenerate(List<String> mdlContents, boolean generateCode, List<String> messages) {
		sdObjectManager = new SystemDynamicsObjectManager();

		// process graphics first since we get screen name information from this portion of the file
		Map<String, View> graphics = new GraphicsProcessor().processGraphics(sdObjectManager, mdlContents);
		EquationProcessor equationProcessor = new EquationProcessor();
		
		Map<String, Equation> equations = equationProcessor.processRawEquations(sdObjectManager, mdlContents);
		
		boolean errors = equationProcessor.getDuplicateLHS().size() > 0;
		if (errors) {
			messages.add("+++ Duplicate LHS +++");
			for (String lhs : equationProcessor.getDuplicateLHS()) {
				messages.add(lhs);
			}
			return false;
		}
		
		// Should these be done here?
		
		processSubscriptDefinition(equations);
		processExponentiaion(equations);
		
		Map<String, Equation> syntaxErrors = equationProcessor.getSyntaxErrors(equations);
		errors = syntaxErrors.size() > 0;
		if (errors) {
			printErrors(syntaxErrors);
			generateErrorReport("Syntax Errors", syntaxErrors, messages);
			return false;
		}
		
		InformationManagers.getInstance().getArrayManager().populateArraySubscriptSpace();
		equationProcessor.checkUsage(equations);
		
		Map<String, Equation> usageErrors = equationProcessor.getUsageErrors(equations);
		errors = usageErrors.size() > 0;
		if (errors) {
			printErrors(usageErrors);
			generateErrorReport("Usage Errors", usageErrors, messages);
			return false;
		}
		
//		processSubscriptDefinition(equations);
//		processExponentiaion(equations);
		generateRPN(equations);
		generateTrees(equations);
//		generateCausalTrees(sdObjectManager);
//		InformationManagers.getInstance().getArrayManager().populateArraySubscriptSpace();
		
		Map<String, Equation> unitsErrors = InformationManagers.getInstance().getUnitsManager().
				performUnitsConsistencyCheck(equations, unitsConsistencyCheckResultsFile);
		errors = unitsErrors.size() > 0;
		if (errors) {
			printErrors(unitsErrors);
			generateErrorReport("Units Errors", unitsErrors, messages);
			return false;
		}

		//	sdObjectManager.print();
		
		List<String> addedScreenNames = sdObjectManager.validate(equations);
		
		sdObjectManager.createSystemDynamicsObjectForNonGraphic(addedScreenNames, equations);

		sdObjectManager.print();
		if (!generateCode)
			return true;
		
		process(equations);
		printGraphics(graphics);
		return true;
	}
	
	
	protected void generateErrorReport(String title, Map<String, Equation> errors, List<String> messages) {
		Iterator<String> iter = errors.keySet().iterator();
		messages.add("+++ "+title+" +++");
		while (iter.hasNext()) {
			String lhs = iter.next();

			Equation eqn = errors.get(lhs);

			messages.add("Equation:"); // "\n"
			messages.add("\t"+StringEscapeUtils.escapeHtml(eqn.getVensimEquation().split("~")[0]));

			for (String msg : eqn.getSyntaxMessages()) {
				messages.add(msg);
			}
			for (String msg : eqn.getSemanticMessages()) {
				messages.add(msg);
			}
			for (String msg : eqn.getUnitsInconsistencyReport()) {
				messages.add(msg);
			}
			messages.add("----------");
		}
	}

	protected boolean execute(List<String> mdlContents) {

		sdObjectManager = new SystemDynamicsObjectManager();

		// process graphics first since we get screen name information from this portion of the file
		Map<String, View> graphics = new GraphicsProcessor().processGraphics(sdObjectManager, mdlContents);
		EquationProcessor equationProcessor = new EquationProcessor();
		
		Map<String, Equation> equations = equationProcessor.processRawEquations(sdObjectManager, mdlContents);
		
		
		
		Map<String, Equation> syntaxErrors = equationProcessor.getSyntaxErrors(equations);
		boolean errors = syntaxErrors.size() > 0;
		if (errors) {
			printErrors(syntaxErrors);
			return false;
		}
		
		
		
		
		Map<String, Equation> usageErrors = equationProcessor.getUsageErrors(equations);
		errors = usageErrors.size() > 0;
		if (errors) {
			printErrors(usageErrors);
			return false;
		}
		
		InformationManagers.getInstance().getUnitsManager().performUnitsConsistencyCheck(equations, unitsConsistencyCheckResultsFile);

		//	sdObjectManager.print();
		
		

		List<String> addedScreenNames = sdObjectManager.validate(equations);
		
		
		sdObjectManager.createSystemDynamicsObjectForNonGraphic(addedScreenNames, equations);

		sdObjectManager.print();

		
		
		process(equations);
		printGraphics(graphics);
		
		return true;
	}

	protected void printGraphics(Map<String, View> graphics) {

		for (View view : graphics.values()) {
			view.print();
		}

	}

	protected void process(Map<String, Equation> equations) {
	}

	protected String asDirectoryPath(String packageName) {
		return packageName.replace(".", "/");
	}

	// perform any misc transformations that are required
	protected void processSubscriptDefinition(Map<String, Equation> equations) {
		for (String lhs : equations.keySet()) {
			Equation eqn = equations.get(lhs);
			eqn.processSubscriptDefinition();

		}
	}
	// perform any misc transformations that are required
	protected void processExponentiaion(Map<String, Equation> equations) {
		for (String lhs : equations.keySet()) {

			Equation eqn = equations.get(lhs);
			eqn.processExponentiation();

		}
	}

	// perform any misc transformations that are required
	protected void generateRPN(Map<String, Equation> equations) {
		for (String lhs : equations.keySet()) {

			Equation eqn = equations.get(lhs);

			try {
				eqn.generateRpn();
				//		eqn.printRpnUnits();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}

	protected void generateTrees(Map<String, Equation> equations) {
		for (String lhs : equations.keySet()) {
			Equation eqn = equations.get(lhs);		
			eqn.generateTree();
		}
	}

	protected void generateMemory() {
		InformationManagers.getInstance().getMappedSubscriptManager().makeConsistent();

		if (isGenerateC() || isGenerateJava()) {
			InformationManagers.getInstance().getMappedSubscriptManager().dumpMappings(Translator.openReport(miscDirectory+"/"+"SubscriptMappings_"+objectName+".csv"));
			InformationManagers.getInstance().getNamedSubscriptManager().dumpMappings(Translator.openReport(miscDirectory+"/"+"SubscriptNameValues_"+objectName+".csv"));

			InformationManagers.getInstance().getUnitsManager().dumpLhsUnits(Translator.openReport(miscDirectory+"/"+"Units_"+objectName+".csv"));
			InformationManagers.getInstance().getUnitsManager().dumpLhsUnitsRaw(Translator.openReport(miscDirectory+"/"+"UnitsRaw_"+objectName+".csv"));


			if (Translator.target.equals(ReaderConstants.JAVA)) {

				String SourceDirectory = getSourceDirectory() + asDirectoryPath(packageName)+ "/";
				InformationManagers.getInstance().getNativeDataTypeManager().dumpLegalNames(Translator.openReport(miscDirectory+"/"+"LegalNames_"+objectName+".csv"));

				RepastSimphonyEnvironment.generateContextBuilder(Translator.openReport(SourceDirectory+"ContextBuilder"+objectName+".java"), objectName, this);

				String ScenarioDirectory = getScenarioDirectory();

				RepastSimphonyEnvironment.generateScenarioXml(Translator.openReport(ScenarioDirectory+"scenario.xml"), objectName);
				RepastSimphonyEnvironment.generateUserPathXml(Translator.openReport(ScenarioDirectory+"user_path.xml"), objectName);
				RepastSimphonyEnvironment.generateClassLoaderXml(Translator.openReport(ScenarioDirectory+"repast.simphony.dataLoader.engine.ClassNameDataLoaderAction_1.xml"), objectName, this);
				RepastSimphonyEnvironment.generateContextXml(Translator.openReport(ScenarioDirectory+"context.xml"), objectName);

				InformationManagers.getInstance().getNativeDataTypeManager().generateMemoryJava(Translator.openReport(SourceDirectory+"Memory"+objectName+".java"), objectName, this);

			} else if (Translator.target.equals(ReaderConstants.JAVASCRIPT)) {

			} else if (Translator.target.equals(ReaderConstants.C)) {
				String SourceDirectory = getSourceDirectory() + asDirectoryPath(packageName)+ "/";
				InformationManagers.getInstance().getNativeDataTypeManager().dumpLegalNames(Translator.openReport(miscDirectory+"/"+"LegalNames_"+objectName+".csv"));
				InformationManagers.getInstance().getNativeDataTypeManager().generateMemoryC(Translator.openReport(SourceDirectory+"memory"+objectName+".h"), objectName, this);

			}

			InformationManagers.getInstance().getArrayManager().dumpSubscriptSpace(Translator.openReport(miscDirectory+"/"+"SubscriptSpace_"+objectName+".csv"));
		}

	}

	protected ArrayList<String> determineEvaluationOrder(Map<String, Equation> equations) {

		// requires maps a non-array realLHS to all rhs variables expanded. This map
		// is modified in the algorithm. The key is removed when the rhs hashset is
		// empty. This signifies that realLHS can be inserted into the evaluation order list.
		HashMap<String, HashSet<String>> requires = new HashMap<String, HashSet<String>>();

		// expandedLhsMap maps an expandedLHS to its higher level lhs subscript from which
		// it is expanded. This map remains intact in the algorithm.
		HashMap<String, String> expandedLhsMap = new HashMap<String, String>();

		// lhsExpandedMap maps a realLHS to its expandedLHSs. This map remains intact in the
		// algorithm
		HashMap<String, HashSet<String>> lhsExpandedMap = new HashMap<String, HashSet<String>>();

		// lhsExpandedNotInitialized starts as a copy of lhsExpandedMap. As expandedLHSs become available
		// for reference, they are removed from the HashSet. Once this HashSet becomes empty, we can remove
		// the key which means that we do not need to consider this realLHS for processing. IS THIS CORRECT?
		HashMap<String, HashSet<String>> lhsExpandedNotInitialized = new HashMap<String, HashSet<String>>();

		// requiresExpanded maps expandedLHS to set of rhs expanded variables. Set elements are removed
		// as they become available for evaluation. The expandedLHS is removed as key when the HashSet
		// becomes empty.
		HashMap<String, HashSet<String>> requiresExpanded = new HashMap<String, HashSet<String>>();


		// all realLHS that still require processing.
		List<String> allRealLHS = new ArrayList<String>();

		// for each equation:
		// get the set of variables that are required
		// then walk through the set repeatedly removing those with no requirements after they have been met

		// first make sure that all mapped subscripts have been defined

		for (String realLHS : equations.keySet()) {

			// reference to the equations currently being processed
			Equation currentEquation = equations.get(realLHS);

			allRealLHS.add(realLHS);		
			if (ArrayReference.isArrayReference(realLHS)) {

				// initial structures that will hold LHS -> Expanded LHS mapping
				// lhsExpandedMap will stay intact
				// lhsExpandedNotInitialized will be modified as we determine when LHS has been assigned a value

				lhsExpandedMap.put(realLHS, new HashSet<String>());
				lhsExpandedNotInitialized.put(realLHS, new HashSet<String>());

				ArrayReference lhsArrayReference = new ArrayReference(realLHS);

				// if a realLHS is getting its data via VDMLOOKUP, there are no RHS vars in which we are
				// interested so we can move to the next equation
				//		    if (equations.get(realLHS).getEquation().contains("VDMLOOKUP")) {
				//			requiresExpanded.put(realLHS, new HashSet<String>());
				//			continue;
				//		    }

				// need to check if there are exceptions for this assignment
				// if there are, we need to have access to them so that they can
				// be discard from processing
				List<String> exceptions = new ArrayList<String>();

				if (currentEquation.isHasException()) {
					exceptions = currentEquation.getExceptions();
				}

				// Generate all combinations of realLHS expanded
				for (SubscriptCombination outerSub : InformationManagers.getInstance().getArrayManager().getSubscriptValueCombinations(lhsArrayReference.getSubscriptsAsArray())) {

					// Here we will skip any subscript combo values that are defined in another equation within a multi-equation definition
					if (exceptions.contains(outerSub.getSubscriptValue())) {
						System.out.println("Skipping "+"[" + outerSub.getSubscriptValue() + "] for "+realLHS);
						continue;
					}

					// Build an array reference based on the subscript value
					String expandedLHS = "array."+lhsArrayReference.getArrayName()+ "[" + outerSub.getSubscriptValue() + "]";

					// allocate data structures for holding rhs references and mapping from expandedLHS -> realLHS
					requiresExpanded.put(expandedLHS, new HashSet<String>());
					//		    System.out.println("expandedLhsMap: <"+expandedLHS+"> <"+realLHS+">");
					expandedLhsMap.put(expandedLHS, realLHS);

					lhsExpandedMap.get(realLHS).add(expandedLHS);
					if (!realLHS.equals(expandedLHS))
						lhsExpandedNotInitialized.get(realLHS).add(expandedLHS);

					// get the set of all rhsVariable reference
					// these are the tokens stored during parsing. Note that we have the ! still there
					for (String rhsVar : equations.get(realLHS).getRHSVariables()) {
						if (equations.get(realLHS).getsExternalData())
							continue;

						// if this is not an array reference, simply put the variable into the requiresExpanded set
						// there is nothing to expand
						if (!ArrayReference.isArrayReference(rhsVar)) {
							requiresExpanded.get(expandedLHS).add(rhsVar);
						} else {
							// if an array access, we must rip apart the array reference and 
							// insert all subscript values based on outerSub current value

							//create an arrayReference for the RHS 
							// recall that we have a LHS arrayReference already defined
							ArrayReference rhsArrayReference = new ArrayReference(rhsVar);

							// some array references may take on multiple values for the eqauation (e.g. SUM(xyz!)
							// get a list of these combinations
							String arrayName = null;
							if (rhsArrayReference.hasRangeSubscript() || InformationManagers.getInstance().getNamedSubscriptManager().hasNamedSubscript(rhsArrayReference)) {
								String[] rhsExpanded;
								if (rhsArrayReference.hasRangeSubscript())
									rhsExpanded = removeBang(rhsArrayReference.getRangeSubscriptsAsArray());
								else
									rhsExpanded = InformationManagers.getInstance().getNamedSubscriptManager().getExpandedSubscripts(rhsArrayReference);

								List<SubscriptCombination> combos = InformationManagers.getInstance().getArrayManager().getSubscriptValueCombinations(rhsExpanded);
								for (SubscriptCombination rangeSub : combos){

									// we now construct an appropriate arrayReference for each combination
									arrayName = "array."+rhsArrayReference.getArrayName()+"[";
									int subNum = 0;

									// for each subscript that appears in this rhsArrayReference
									// we need to determine if it is a range subscript or not.
									// If it is, 
									for (String subscript : rhsArrayReference.getSubscriptsAsArray()) {
										if (subNum++ > 0)
											arrayName += ",";
										if (ArrayReference.isRangeSubscript(subscript)) {
											arrayName += rangeSub.getSubscriptValue(subscript.replace("!", ""));
										} else {
											arrayName += outerSub.getSubscriptValue(subscript);
										}

									}
									arrayName += "]";
									if (!expandedLHS.equals(arrayName))
										requiresExpanded.get(expandedLHS).add(arrayName);

								}
							} else {
								// With no range subscript, we just copy over the subscript values that appear in the LHS
								arrayName = "array."+rhsArrayReference.getArrayName()+"[";
								int subNum = 0;

								// for each subscript that appears in this rhsArrayReference
								// we need to determine if it is a range subscript or not.
								// If it is, 
								for (String subscript : rhsArrayReference.getSubscriptsAsArray()) {
									if (subNum++ > 0)
										arrayName += ",";

									arrayName += outerSub.getSubscriptValue(subscript);

								}
								arrayName += "]";
								requiresExpanded.get(expandedLHS).add(arrayName);
							}
						}

					}
				}
			} else {

				if (equations.get(realLHS).isRepeated())
					requires.put(realLHS, equations.get(realLHS).getRHSVariablesExpanded());
				else
					requires.put(realLHS, null);
			}

		}

		dumpRequires("lhsExpandedMap.txt", lhsExpandedMap);
		dumpRequires("initialRequires.txt", requires);
		boolean startChain = false; // want this to be false for production
		dumpRequiresExpanded("initialRequiresExpanded.txt", requiresExpanded, startChain); // HerelhsExpandedNotInitialized
		dumpRequiresExpanded("initiallhsExpandedNotInitialized.txt", lhsExpandedNotInitialized, startChain);
		
		// determine if any equations are not referenced in the model
		// Note that we will disregard any autogenerated equations since there is no requirement that they be referenced

		List<String> notReferenced = new ArrayList<String>();
		for (String s : equations.keySet()) {
			if (!equations.get(s).isAutoGenerated())
				notReferenced.add(s);
		}
		
		int initialCount = notReferenced.size();
		
		
		for (HashSet<String> hs : requires.values()) {
			if (hs == null)
				continue;
			Iterator<String> iter = hs.iterator();
			while(iter.hasNext()) {
				String rhs = iter.next();
				if (notReferenced.contains(rhs))
					notReferenced.remove(rhs);
			}
		}
		
		if (notReferenced.size() > 0) {
			MessageManager mm = InformationManagers.getInstance().getMessageManager();
			mm.addWarningMessage("+++ Unused Equations +++");
			mm.addWarningMessage("Requires "+requires.size()+" Total Recognized: "+equations.keySet().size()+" Initial "+initialCount+" Not Referenced: "+notReferenced.size());
			for (String lhs : notReferenced) {
				mm.addWarningMessage(equations.get(lhs).getVensimEquationOnly()+"\n");
			}
			
		}


		// this is the order in which we will evaluate the equations
		ArrayList<String> evaluationOrder = new ArrayList<String>();

		int lastCount = 0;

		// allLHS needs to be reset
		allRealLHS.clear();

		// thse are all the scalars
		allRealLHS.addAll(requires.keySet());
		allRealLHS.addAll(lhsExpandedNotInitialized.keySet());

		// processing is slightly different after first pass through the equations
		boolean pass1 = true;
		boolean done = false;
		int loopCount = 0;
		while (!done) {

			// this will contain LHS's that have been assigned a value
			ArrayList<String> toRemoveFromRequires = new ArrayList<String>();
			for (String realLHS : allRealLHS) { // requires.keySet()

				// If LHS not in lhsExpandedMap (which is static) keyset, then we know that this is
				// a non-array LHS
				if (!lhsExpandedMap.containsKey(realLHS)) {
					// this section deals with non-array lhs
					// if the key has been removed or the set associated with the key is empty, then
					// we have all values required for the calculation and it can be added to the evaluation list
					if (pass1) {
						if (requires.get(realLHS) == null || (requires.get(realLHS) != null && requires.get(realLHS).size() == 0)) { 
							toRemoveFromRequires.add(realLHS);
						} else {

						}
					} else {
						//  check for constants or those with no remaining requirements
						if (requires.get(realLHS) == null || (requires.get(realLHS) != null && (requires.get(realLHS).size() == 0)) 
								|| hasInitialValue(realLHS, requires, equations)) { 
							toRemoveFromRequires.add(realLHS);

						} else {

						}
					}
				} else {

					// this section deals with array lhs
					// we don't check for null as the allLHS list is generated from lhsExpandedNotInitialized keyset
					// lhsExpandedNotInitialized does get modified as things progresses


					// Special case:
					//  if an array subscript value is read from file (VDMLOOKUP), then it will have no RHS dependancy
					//  and we can just add it to toRemoveFromRequires. This means that there is no members of the 
					//  lhsExpandedNotInitialized HashSet
					//
					// Note that under this condition, the for loop will not be executed.

					if (lhsExpandedNotInitialized.containsKey(realLHS) &&
							lhsExpandedNotInitialized.get(realLHS).size() == 0) {
						toRemoveFromRequires.add(realLHS);

					}

					// now check if the expandedLHS has been completely defined

					for (String expandedLHS : lhsExpandedNotInitialized.get(realLHS)) { // this was just lhsExpandedMap

						if (pass1) {
							// if the expanded LHS is no longer in requires expanded
							// or if it is in requires expanded, but has no RHS left to define
							// we can remove this expandedLHS
							if (requiresExpanded.get(expandedLHS) == null || (requiresExpanded.get(expandedLHS) != null && requiresExpanded.get(expandedLHS).size() == 0)) { 
								toRemoveFromRequires.add(expandedLHS);

							} else {

							}
						} else {
							Equation eqn = equations.get(realLHS);
							//			    if (eqn.getVensimEquation().contains("INTEG")) {
							//				System.out.println("Try this one: "+eqn);
							//			    }
							//  check for constants or those with no remaining requirements
							// as above, but also check if an initial value is available
							if (requiresExpanded.get(expandedLHS) == null || (requiresExpanded.get(expandedLHS) != null && (requiresExpanded.get(expandedLHS).size() == 0)) 
									|| hasInitialValue(realLHS, expandedLHS, requires, requiresExpanded, lhsExpandedNotInitialized, equations)) {  // was requires (requiresExpanded) MJB 9/12
								toRemoveFromRequires.add(expandedLHS);

							} else {

							}
						}
					}
				}
			} // end of loop through all real LHS

			pass1 = false;

			System.out.println("To Remove Count = "+toRemoveFromRequires.size());
			if (toRemoveFromRequires.size() > 0) {
				if (lastCount == toRemoveFromRequires.size()) {
					loopCount++;
					System.out.println("LoopCount: "+loopCount);
					if (loopCount > 3) {
						break;
					}
				} else {
					loopCount = 0;
				}

				lastCount = toRemoveFromRequires.size();
				removeRequirement(toRemoveFromRequires, requires, requiresExpanded, lhsExpandedNotInitialized, expandedLhsMap, evaluationOrder);
				System.out.println("Remaining Equations: "+(requires.size()+lhsExpandedNotInitialized.size()));
				// allLHS needs to be reset
				allRealLHS.clear();
				allRealLHS.addAll(requires.keySet());
				allRealLHS.addAll(lhsExpandedNotInitialized.keySet());
			} else {
				if (requires.size() == 0 && lhsExpandedNotInitialized.size() == 0) {
					System.out.println("Successfully ordered all equations");
					done  = true;
					// allLHS needs to be reset from requires and lhsExpandedMap
				} else {
					System.out.println("Cannot order these equations:");
					for (String lhs : requires.keySet()) {
						System.out.println("   >>> lhs "+lhs+"  "+requires.get(lhs).size());
						System.out.println(equations.get(lhs).getEquation());
						System.out.println(equations.get(lhs).getCleanEquation());
						printRequires(requires.get(lhs));
						done = true;
					}

					for (String lhs : lhsExpandedNotInitialized.keySet()) {
						//			String lhs = expandedLhsMap.get(lhse);
						System.out.println("   >>> lhs <"+lhs+"> "+lhsExpandedNotInitialized.get(lhs).size());
						System.out.println(equations.get(lhs).getEquation());
						System.out.println(equations.get(lhs).getCleanEquation());
						printRequires(lhsExpandedNotInitialized.get(lhs));
						done = true;
					}

				}
			}
		}
		dumpRequires("postOrderRequires.txt", requires);
		boolean endChain = true;
		dumpRequiresExpanded("postOrderRequiresExpanded.txt", requiresExpanded, endChain); // Here
		dumpRequiresExpanded("postlhsExpandedNotInitialized.txt", lhsExpandedNotInitialized, endChain);

		int requiresSize = requires.keySet().size();
		int notInitSize = lhsExpandedNotInitialized.keySet().size();

		System.out.println("%%%%%%%%%% "+requiresSize+" "+notInitSize);
		evaluationOrder.addAll(requires.keySet());
		evaluationOrder.addAll(lhsExpandedNotInitialized.keySet());

		analyzeEvaluationOrder(evaluationOrder, equations, lhsExpandedMap, expandedLhsMap);
		return evaluationOrder;
	}

	protected String[] removeBang(String[] inList) {
		String[] outList = new String[inList.length];
		for (int i = 0; i < inList.length; i++) {
			outList[i] = inList[i].replace("!", "");
		}
		return outList;
	}

	protected boolean containsInitialValueFunction(String equation) {
		if (containsFunctionINTEG(equation) || 
				containsFunctionSMOOTHI(equation) ||
				containsFunctionDELAY3I(equation) ||
				containsFunctionACTIVEINITIAL(equation)) {
			return true;
		} else {
			return false;
		}

	}

	protected boolean containsFunctionINTEG(String equation) {
		if (equation.contains("INTEG"))
			return true;
		else
			return false;
	}

	protected boolean containsFunctionSMOOTHI(String equation) {
		if (equation.contains("SMOOTHI"))
			return true;
		else
			return false;
	}

	protected boolean containsFunctionDELAY3I(String equation) {
		if (equation.contains("DELAY3I"))
			return true;
		else
			return false;
	}

	protected boolean containsFunctionACTIVEINITIAL(String equation) {
		if (equation.contains("ACTIVEINITIAL"))
			return true;
		else
			return false;
	}

	protected boolean hasInitialValue(String lhs, String expandedLHS, HashMap<String, HashSet<String>> requires, HashMap<String, 
			HashSet<String>> requiresExpanded, HashMap<String, HashSet<String>> lhsExpandedNotInitialized, Map<String, Equation> equations) {
		boolean initialized = false;

		Equation eqn = equations.get(lhs);
		// functions that support initial values
		if (containsInitialValueFunction(equations.get(lhs).getCleanEquation())) {

			initialized = true;
			ArrayList<String> initializationVariables = equations.get(lhs).getFunctionInitialVariables();

			for (String var : initializationVariables) {

				String v = removeValueOf(var).replaceAll("\"", "");
				// the requires
				v = InformationManagers.getInstance().getNativeDataTypeManager().getOriginalName(v);

				if (Parser.isNumber(v))
					continue;
				if (ArrayReference.isArrayReference(v)) {
					ArrayReference lhsAR = new ArrayReference(lhs);
					ArrayReference lhsExpandedAR = new ArrayReference(expandedLHS);
					ArrayReference rhsAR = new ArrayReference(v);
					if (lhsExpandedNotInitialized.containsKey(v) && lhsExpandedNotInitialized.get(v).size() == 0 )
						continue;

				} else /* scalar */ {
					if (requires.containsKey(v) && requires.get(v).size() > 0) {
						initialized = false;
					}
				}
				//		else if (requires.containsKey(v)) {
				//		    initialized = false;
				//		} else {
				//		    System.out.println("Not in requires: "+v);
				//		}
			}
		} 

		if (!equations.get(lhs).isOrderedWithInitialValue()) {	
			equations.get(lhs).setOrderedWithInitialValue(initialized);
		}

		return initialized;
	}

	protected boolean hasInitialValue(String lhs, HashMap<String, HashSet<String>> requires, Map<String, Equation> equations) {
		boolean initialized = false;

		Equation eqn = equations.get(lhs);

		//	if (equations.get(lhs).getVensimEquation().contains("IF THEN ELSE")) {
		//	    return true;
		//	}
		// functions that support initial values
		if (containsInitialValueFunction(equations.get(lhs).getCleanEquation())) {

			// HERE -- get a list of the initialization variables in the statement and check each of them

			//		if (Equation.isNumber(var))
			//			return true;
			//		if (requires.containsKey(var))
			//			return false;

			initialized = true;
			ArrayList<String> initializationVariables = equations.get(lhs).getFunctionInitialVariables();



			for (String var : initializationVariables) {

				String v = removeValueOf(var).replaceAll("\"", "");
				// the requires
				v = InformationManagers.getInstance().getNativeDataTypeManager().getOriginalName(v);

				if (Parser.isNumber(v))
					continue;
				else if (requires.containsKey(v)) {
					initialized = false;
				} else {
					System.out.println("Not in requires: "+v);
				}
			}
		}
		if (!equations.get(lhs).isOrderedWithInitialValue()) {	
			equations.get(lhs).setOrderedWithInitialValue(initialized);
		}
		return initialized;
	}

	protected String removeValueOf(String var) {
		String retVar = var.replace("valueOf(", "").replaceAll("\"", "").replaceAll("\\)", "");
		return retVar;
	}

	protected void printRequires(HashSet<String> set) {

		Iterator<String> iter = set.iterator();
		while (iter.hasNext()) {
			System.out.println("           "+iter.next());
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	protected void removeRequirement(ArrayList<String> toRemoveFromRequires, HashMap<String, HashSet<String>> requires,
			HashMap<String, HashSet<String>> requiresExpanded,
			HashMap<String, HashSet<String>> lhsExpandedNotInitialized,
			HashMap<String, String> expandedLhsMap,
			ArrayList<String> evaluationOrder) {



		System.out.println("removeRequirement # "+toRemoveFromRequires.size());

		for (String toRemove : toRemoveFromRequires) {

			if (ArrayReference.isArrayReference(toRemove)) {

				// we've determined that this array reference can take place
				// remove it from requiresExpanded
				// this does not added to evaluation order until we know that all other references to this array have been processed

				if (requiresExpanded.remove(toRemove) == null) { // "LHS" of equation
					System.out.println("Bad Remove from requiresExpanded: "+toRemove);

				}

				if (lhsExpandedNotInitialized.remove(toRemove) == null) { // "LHS" of equation
					//			System.out.println("Bad Remove from lhsExpandedNotInitialized: "+toRemove);

				}
				removeFromRHS(toRemove, requires, requiresExpanded); // "RHS" of equation

				// at this point, we need to see if all array subscripts have been achieved. Then we can add to evaluationOrder

				String realLHS = expandedLhsMap.get(toRemove);
				HashSet<String> ehs = lhsExpandedNotInitialized.get(realLHS);
				if (ehs != null && ehs.contains(toRemove)) {
					ehs.remove(toRemove);

					if (ehs.size() == 0) {
						evaluationOrder.add(realLHS);
						lhsExpandedNotInitialized.remove(realLHS);
						removeFromRHS(realLHS, requires, requiresExpanded);

					} else {

					}
				} else {

				}


			} else {
				// we remove from requires and then check requires and requiresExpanded
				// this will always be a non array LHS
				requires.remove(toRemove);
				evaluationOrder.add(toRemove);
				removeFromRHS(toRemove, requires, requiresExpanded);
			}
		}
	}

	protected void removeFromRHS(String lhs, HashMap<String, HashSet<String>> requires, 
			HashMap<String, HashSet<String>> requiresExpanded) {

		removeFromRHS(lhs, requires, "requires");
		removeFromRHS(lhs, requiresExpanded, "requiresExpanded");
	}

	protected void removeFromRHS(String lhs, HashMap<String, HashSet<String>> requires, String tag) {

		for (String req : requires.keySet()) {

			HashSet<String> hs = requires.get(req);
			if (hs != null && hs.contains(lhs)) {
				hs.remove(lhs);

			}
		}
	}

	protected void dumpRequires(String fileName, HashMap<String, HashSet<String>> requires) {

		BufferedWriter bw = Utilities.openFileForWriting(miscDirectory+"/"+fileName);
		ArrayList<String> allLHS = new ArrayList<String>();

		for (String lhs : requires.keySet()) {
			if (lhs == null) {
				System.out.println("Skipping null in requires");
			} else {
				allLHS.add(lhs);
			}
		}

		Collections.sort(allLHS);

		try {
			for (String lhs : allLHS) {
				bw.append("LHS: <"+lhs+">\n");
				for (String r : sortSet(requires.get(lhs))) {
					bw.append("     RHS <"+r+">\n");
				}
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void dumpRequiresExpanded(String fileName, HashMap<String, HashSet<String>> requires, boolean printChain) {

		//	boolean printChain = true;

		BufferedWriter bw = Utilities.openFileForWriting(miscDirectory+"/"+fileName);
		ArrayList<String> allLHS = new ArrayList<String>();

		for (String lhs : requires.keySet()) {
			if (lhs == null) {
				System.out.println("Skipping null in requires");
			} else {
				allLHS.add(lhs);
			}
		}

		Collections.sort(allLHS);


		try {
			for (String lhs : allLHS) {
				bw.append("LHS: <"+lhs+">\n");
				int level = 1;
				if (printChain) {
					printChain(bw, level, lhs, requires, new HashSet<String>());
				} else {
					for (String r : sortSet(requires.get(lhs))) {
						bw.append("     RHS <"+r+">\n");
					}
				}
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void printChain(BufferedWriter bw, int level, String lhs, HashMap<String, HashSet<String>> requires, HashSet<String> alreadyPrinted) {

		if (level > 50) {
			try {
				bw.append("Terminating recursion\n");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		StringBuffer blanks = new StringBuffer();
		for (int i = 0; i < level; i++) {
			blanks.append("  ");
		}

		for (String r : sortSet(requires.get(lhs))) {
			if (alreadyPrinted.contains(r)) {
				try {
					bw.append("                      ### looping on "+r+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			try {
				bw.append(blanks.toString()+"RHS <"+r+"> \n");
				alreadyPrinted.add(r);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			printChain(bw, level+1, r, requires, alreadyPrinted);
		}

	}


	protected List<String> sortSet(HashSet<String> set) {
		ArrayList<String> al = new ArrayList<String>();
		if (set == null)
			return al;
		Iterator<String> iter = set.iterator();
		while (iter.hasNext())
			al.add(iter.next());

		Collections.sort(al);

		return al;
	}

	protected void analyzeEvaluationOrder(List<String> evaluationOrder, Map<String, Equation> equations,
			HashMap<String, HashSet<String>> lhsExpandedMap, HashMap<String, String> expandedLhsMap) {

		boolean orderProblem = false;
		BufferedWriter bw = openReport(miscDirectory+"/"+"evaluationOrder.txt");
		HashMap<String, Integer> initializationLine = new HashMap<String, Integer>();
		for (int i = 0; i < evaluationOrder.size(); i++) {
			initializationLine.put(evaluationOrder.get(i), i);
			String lhs = evaluationOrder.get(i);
			//	    System.out.println("<"+lhs+">");

			if (expandedLhsMap.containsKey(lhs)) {
				String real = expandedLhsMap.get(lhs);
				initializationLine.put(real, i);
			}

			if (lhsExpandedMap.containsKey(lhs)) {
				for (String exp : lhsExpandedMap.get(lhs)) {
					initializationLine.put(exp, i);
				}
			}
		}

		try {
			for (int i = 0; i < evaluationOrder.size(); i++) {
				String lhs = evaluationOrder.get(i);

				bw.append("\n"+i);
				bw.append(" EQN: "+equations.get(lhs).getCleanEquation()+"\n");
				bw.append(" LHS: "+lhs+"\n");
				bw.flush();
				HashSet<String> rhsVars = null;
				if (equations.get(lhs).isRepeated())
					rhsVars = equations.get(lhs).getRHSVariables();

				boolean problem = false;
				boolean orderingProblem = false;
				boolean missingProblem = false;

				if (rhsVars == null || rhsVars.size() == 0) {
					bw.append("     None\n");
					problem = false;
					orderingProblem = false;
					missingProblem = false;
				} else {
					int r = 0;
					for (String rhs : rhsVars) {

						// need to contend with array references  30Sep

						if (initializationLine.get(rhs) != null) {
							r = initializationLine.get(rhs);
							bw.append("     <"+rhs+"> "+r + (i < r ? " <<<<<<<<" : "")+"\n");
							if (i < r && !equations.get(lhs).isOrderedWithInitialValue()) 
								orderingProblem = true;
						} else {
							// this might be a array assignment and we alooking at an expanded LHS
							// convert back to the realLHS
							String rLHS = expandedLhsMap.get(rhs);
							if (initializationLine.containsKey(rLHS)) {
								r = initializationLine.get(rLHS);
								bw.append("     <"+rLHS+"> "+r + (i < r ? " <<<<<<<<" : "")+"\n");
								if (i < r && !equations.get(rLHS).isOrderedWithInitialValue()) 
									orderingProblem = true;
							} else {
								bw.append(" >>>> initializtion line missing for <"+rhs+">\n");
								missingProblem = true;
							}
						}
						if (i > r /*&& (equations.get(lhs).getCleanEquation().contains("INTEG") ||
				equations.get(lhs).getCleanEquation().contains("SMOOTHI")) */) {
							problem = false;
						}
					}
				}
				if (problem) {
					bw.append("######### P R O B L E M #########\n");
					orderProblem = true;
				}

				if (orderingProblem) {
					bw.append("######### ORDERING P R O B L E M #########\n");
					orderProblem = true;
				}

				if (missingProblem) {
					bw.append("######### MISSING P R O B L E M #########\n");
					orderProblem = true;
				}
			}
			bw.flush();
			bw.close();


			if (orderProblem) {
				System.out.println("!!!Evaluation Order PROBLEMS!!!");
			} else {
				System.out.println("Clean Evaluation Order");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//    public void performUnitsConsistencyCheck() {
	//	UnitsManager.performUnitsConsistencyCheck(evaluationOrder, equations, "./ConsistencyResults.xml");
	//    }


	public static  BufferedWriter openReport(String filename) {
		BufferedWriter report = null;

		try {
			File aFile = new File(filename);

			if (aFile.getParentFile() != null)
				aFile.getParentFile().mkdirs();

			report = new BufferedWriter(new OutputStreamWriter(
					(new FileOutputStream(aFile, false))));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     


		return report;
	}

	public static BufferedReader openForRead(String filename) {

		BufferedReader fileReader = null;
		
		System.out.println("Open for Read: "+filename);

		// open the file for reading
		try {
			fileReader = new BufferedReader (new FileReader(new File(filename)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileReader;
	}

	public boolean loadProperties(String file) {
		File props = new File(file);
		if (props.exists()) {
			try {
				PROPERTIES.load(new FileInputStream(props));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean loadProperties() {
		return loadProperties(DEFAULT_PROPERTIES_FILE);
	}

	public boolean loadUnitsProperties(String file) {
		File props = new File(file);
		if (props.exists()) {
			try {
				UNITS_PROPERTIES.load(new FileInputStream(props));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean loadUnitsProperties() {
		return loadUnitsProperties(UNITS_PROPERTIES_FILE);
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getTarget() {
		return Translator.target;
	}

	public void setTarget(String target) {
		Translator.target = target;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDestinationDirectory() {
		return destinationDirectory;
	}

	public void setDestinationDirectory(String destinationDirectory) {
		this.destinationDirectory = destinationDirectory;
	}

	public String getMiscDirectory() {
		return miscDirectory;
	}

	public void setMiscDirectory(String miscDirectory) {
		this.miscDirectory = miscDirectory;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getSupportName() {
		return supportName;
	}

	public void setSupportName(String supportName) {
		this.supportName = supportName;
	}

	public boolean isUnitsConsistency() {
		return unitsConsistency;
	}

	public boolean isGenerateC() {
		return generateC;
	}

	public boolean isGenerateJava() {
		return generateJava;
	}

	public String getSourceDirectory() {
		return destinationDirectory+"/"+ "src" + "/";
	}
	
	public String getScenarioDirectory() {
		return destinationDirectory +"/" + objectName + ".rs/";
	}
}
