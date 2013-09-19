package repast.simphony.systemdynamics.translator;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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
	
	private boolean initializeScenarioDirectory = true;

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
		if (rawEquations == null)
			return;
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
	
//	try {
//	    FileInputStream fstream = new FileInputStream(filename);
//	    DataInputStream in = new DataInputStream(fstream);
//	    try {
//		fileReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
//	    } catch (UnsupportedEncodingException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    }
//	   
//	} catch (FileNotFoundException e) {
//	    // TODO Auto-generated catch block
//	    e.printStackTrace();
//	}

	protected void loadUnitsEquivalences() {

		BufferedReader unitsReader = null;
		FileInputStream fstream = null;
		
//			fstream = new FileInputStream("/" + UNITS_PROPERTIES.getProperty("unitsEquivalenceFile"));
			fstream = (FileInputStream) getClass().getResourceAsStream("/" + 
						UNITS_PROPERTIES.getProperty("unitsEquivalenceFile"));
			DataInputStream in = new DataInputStream(fstream);
			try {
				unitsReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		


		String aLine = null;
		try {
			aLine = unitsReader.readLine();
			while (aLine != null) {
				String[] equiv = aLine.split("=");
				InformationManagers.getInstance().getUnitsManager().addEquivalence(equiv[0], equiv[1]);
				aLine = unitsReader.readLine();
			}
			unitsReader.close();
			fstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
//	protected void loadUnitsEquivalences() {
//		BufferedReader unitsReader = openForRead(UNITS_PROPERTIES.getProperty("unitsEquivalenceFile"));
//
//
//		String aLine = null;
//		try {
//			aLine = unitsReader.readLine();
//			while (aLine != null) {
//				String[] equiv = aLine.split("=");
//				InformationManagers.getInstance().getUnitsManager().addEquivalence(equiv[0], equiv[1]);
//				aLine = unitsReader.readLine();
//			}
//			unitsReader.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//	}

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
		
//		sdObjectManager = new SystemDynamicsObjectManager();
		sdObjectManager = InformationManagers.getInstance().getSystemDynamicsObjectManager();

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
		
		Map<String, Equation> fatalErrors = equationProcessor.getFatalErrors(equations);
		errors = fatalErrors.size() > 0;
		if (errors) {
			printErrors(fatalErrors);
			generateErrorReport("Fatal Errors", fatalErrors, messages);
			return false;
		}
		
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
			// return false;
		}

		//	sdObjectManager.print();
		
		List<EquationGraphicValidation> addedScreenNames = sdObjectManager.validate(equations);
		
		sdObjectManager.createSystemDynamicsObjectForNonGraphic(addedScreenNames, equations);
		
//		System.out.println("############################");
//		System.out.println("############################");
//		System.out.println("############################");
//		System.out.println("############################");
//		sdObjectManager.print();
//		printGraphics(graphics);

//		sdObjectManager.print();
		if (!generateCode)
			return true;
		
		process(equations);
		
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
		if (mdlContents == null)
			return false;

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

//			sdObjectManager.print();
		
		

		List<EquationGraphicValidation> addedScreenNames = sdObjectManager.validate(equations);
		
		sdObjectManager.createSystemDynamicsObjectForNonGraphic(addedScreenNames, equations);

//		sdObjectManager.print();

		
		
		process(equations);
//		printGraphics(graphics);
		
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
//			System.out.println("IV: "+eqn.getVensimEquationOnly());
			eqn.generateTree();
//			System.out.println("IV: "+eqn.getVensimEquationOnly()+" <<"+eqn.getIntialValue()+">>");
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

				

				String ScenarioDirectory = getScenarioDirectory();

				if (this.isInitializeScenarioDirectory()) {
					RepastSimphonyEnvironment.generateScenarioXml(Translator.openReport(ScenarioDirectory+"scenario.xml"), objectName);
					RepastSimphonyEnvironment.generateUserPathXml(Translator.openReport(ScenarioDirectory+"user_path.xml"), objectName);
					RepastSimphonyEnvironment.generateClassLoaderXml(Translator.openReport(ScenarioDirectory+"repast.simphony.dataLoader.engine.ClassNameDataLoaderAction_1.xml"), objectName, this);
					RepastSimphonyEnvironment.generateContextXml(Translator.openReport(ScenarioDirectory+"context.xml"), objectName);
				}

				InformationManagers.getInstance().getNativeDataTypeManager().generateMemoryJava(Translator.openReport(SourceDirectory+"Memory"+objectName+".java"), objectName, this);
				RepastSimphonyEnvironment.generateContextBuilder(Translator.openReport(SourceDirectory+"ContextBuilder"+objectName+".java"), objectName, this);

			} else if (Translator.target.equals(ReaderConstants.JAVASCRIPT)) {

			} else if (Translator.target.equals(ReaderConstants.C)) {
				String SourceDirectory = getSourceDirectory() + asDirectoryPath(packageName)+ "/";
				InformationManagers.getInstance().getNativeDataTypeManager().dumpLegalNames(Translator.openReport(miscDirectory+"/"+"LegalNames_"+objectName+".csv"));
				InformationManagers.getInstance().getNativeDataTypeManager().generateMemoryC(Translator.openReport(SourceDirectory+"memory"+objectName+".h"), objectName, this);

			}

			InformationManagers.getInstance().getArrayManager().dumpSubscriptSpace(Translator.openReport(miscDirectory+"/"+"SubscriptSpace_"+objectName+".csv"));
		}

	}
	
	protected void analyzeDependencies(HashMap<String, HashSet<String>> requires, HashMap<String, HashSet<String>> requiresExpanded, 
			HashMap<String, HashSet<String>> lhsExpandedNotInitialized) {
		
		// make sure that every thing on the RHS of these maps have a corresponding LHS (i.e. key)
		
//		System.out.println("analyzeDependencies requires");
		
		// thse are scalars as key and anything on RHS
		
		for (String lhs : requires.keySet()) {
			HashSet<String> hs = requires.get(lhs);
			if (hs == null)
				continue;
			Iterator<String> iter = hs.iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				if (requires.containsKey(key) ||
						requiresExpanded.containsKey(key) ||
						lhsExpandedNotInitialized.containsKey(key)) {
					
				} else {
					System.out.println("No LHS for <"+key+"> used by "+lhs);
				}
			}
		}
		
//		System.out.println("analyzeDependencies requiresExpanded");
		
		// thse are scalars as key and anything on RHS
		
		for (String lhs : requiresExpanded.keySet()) {
			HashSet<String> hs = requiresExpanded.get(lhs);
			if (hs == null)
				continue;
			Iterator<String> iter = hs.iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				if (requires.containsKey(key) ||
						requiresExpanded.containsKey(key) ||
						lhsExpandedNotInitialized.containsKey(key)) {
					
				} else {
					System.out.println("No LHS for <"+key+"> used by "+lhs);
				}
			}
		}
		
//		System.out.println("analyzeDependencies lhsExpandedNotInitialized");
		
		// thse are scalars as key and anything on RHS
		
		for (String lhs : lhsExpandedNotInitialized.keySet()) {
			HashSet<String> hs = lhsExpandedNotInitialized.get(lhs);
			Iterator<String> iter = hs.iterator();
			if (hs == null)
				continue;
			while (iter.hasNext()) {
				String key = iter.next();
				if (requires.containsKey(key) ||
						requiresExpanded.containsKey(key) ||
						lhsExpandedNotInitialized.containsKey(key)) {
					
				} else {
					System.out.println("No LHS for <"+key+"> used by "+lhs);
				}
			}
		}
		
	}

	protected ArrayList<String> determineEvaluationOrder(Map<String, Equation> equations) {

		// requiresScalarExpandedRhs maps a non-array realLHS to all rhs variables expanded. This map
		// is modified in the algorithm. The key is removed when the rhs hashset is
		// empty. This signifies that realLHS can be inserted into the evaluation order list.
		HashMap<String, HashSet<String>> requiresLhsScalarExpandedRhs = new HashMap<String, HashSet<String>>();
		HashMap<String, HashSet<String>> references = new HashMap<String, HashSet<String>>();

		// expandedLhsToRealLhsMap maps an expandedLHS to its higher level lhs subscript from which
		// it is expanded. This map remains intact in the algorithm.
		HashMap<String, String> expandedLhsToRealLhsMap = new HashMap<String, String>();

		// realLhsToExpandedLhsMap maps a realLHS to its expandedLHSs. This map remains intact in the
		// algorithm
		HashMap<String, HashSet<String>> realLhsToExpandedLhsMap = new HashMap<String, HashSet<String>>();

		// realLhsToExpandedLhsNotInitialized starts as a copy of realLhsToExpandedLhsMap. As expandedLHSs become available
		// for reference, they are removed from the HashSet. Once this HashSet becomes empty, we can remove
		// the key which means that we do not need to consider this realLHS for processing. IS THIS CORRECT?
		HashMap<String, HashSet<String>> realLhsToExpandedLhsNotInitialized = new HashMap<String, HashSet<String>>();

		// requiresExpandedLhsExpandedRHS maps expandedLHS to set of rhs expanded variables. Set elements are removed
		// as they become available for evaluation. The expandedLHS is removed as key when the HashSet
		// becomes empty.
		HashMap<String, HashSet<String>> requiresExpandedLhsExpandedRHS = new HashMap<String, HashSet<String>>();


		// all realLHS that still require processing.
		List<String> allRealLHS = new ArrayList<String>();

		// for each equation:
		// get the set of variables that are required
		// then walk through the set repeatedly removing those with no requirements after they have been met

		// first make sure that all mapped subscripts have been defined

		for (String realLHS : equations.keySet()) {
			
//			System.out.println("GenOrder: "+realLHS);

			// reference to the equations currently being processed
			Equation currentEquation = equations.get(realLHS);
			allRealLHS.add(realLHS);
			
			
			if (ArrayReference.isArrayReference(realLHS)) {
				
				// initial structures that will hold LHS -> Expanded LHS mapping
				// lhsExpandedMap will stay intact
				// lhsExpandedNotInitialized will be modified as we determine when LHS has been assigned a value

				realLhsToExpandedLhsMap.put(realLHS, new HashSet<String>());
				realLhsToExpandedLhsNotInitialized.put(realLHS, new HashSet<String>());

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
					requiresExpandedLhsExpandedRHS.put(expandedLHS, new HashSet<String>());
//							    System.out.println("expandedLhsMap: <"+expandedLHS+"> <"+realLHS+">");
					expandedLhsToRealLhsMap.put(expandedLHS, realLHS);

					realLhsToExpandedLhsMap.get(realLHS).add(expandedLHS);
					realLhsToExpandedLhsNotInitialized.get(realLHS).add(expandedLHS);

					// get the set of all rhsVariable reference
					// these are the tokens stored during parsing. Note that we have the ! still there
					for (String rhsVar : equations.get(realLHS).getRHSVariables()) {
						if (equations.get(realLHS).isGetExternalData())
							continue;

						// if this is not an array reference, simply put the variable into the requiresExpanded set
						// there is nothing to expand
						if (!ArrayReference.isArrayReference(rhsVar)) {
							requiresExpandedLhsExpandedRHS.get(expandedLHS).add(rhsVar);
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
									if (!expandedLHS.equals(arrayName)) {
										requiresExpandedLhsExpandedRHS.get(expandedLHS).add(arrayName);
										// try this
//										if (expandedLhsToRealLhsMap.containsKey(arrayName)) {
//											String myRealLHS = expandedLhsToRealLhsMap.get(arrayName);
////											System.out.println("adding "+myRealLHS+" because of "+arrayName);
//											requiresExpandedLhsExpandedRHS.get(expandedLHS).add(myRealLHS);
//										}
									}

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
								requiresExpandedLhsExpandedRHS.get(expandedLHS).add(arrayName);
							}
						}

					}
				}
			} else {

				if (equations.get(realLHS).isRepeated()) {
					requiresLhsScalarExpandedRhs.put(realLHS, equations.get(realLHS).getRHSVariablesExpanded());
//					requiresExpanded.put(realLHS, equations.get(realLHS).getRHSVariablesExpanded()); // added 4/5/13
				}
				else {
					requiresLhsScalarExpandedRhs.put(realLHS, null);
				}
			}

		}

		dumpRequires("realLhsToExpandedLhsMap.txt", realLhsToExpandedLhsMap);
		dumpRequires("initialrequiresLhsScalarExpandedRhs .txt", requiresLhsScalarExpandedRhs);
		boolean startChain = false; // want this to be false for production
		dumpRequiresExpanded("initialrequiresExpandedLhsExpandedRHS.txt", requiresExpandedLhsExpandedRHS, startChain); // HerelhsExpandedNotInitialized
		dumpRequiresExpanded("initialrealLhsToExpandedLhsNotInitialized.txt", realLhsToExpandedLhsNotInitialized, startChain);
		
		analyzeDependencies(requiresLhsScalarExpandedRhs, requiresExpandedLhsExpandedRHS, realLhsToExpandedLhsNotInitialized);

		// this is the order in which we will evaluate the equations
		ArrayList<String> evaluationOrder = new ArrayList<String>();

		int lastCount = 0;
		int terminationLoopCount = 0;

		// allLHS needs to be reset
		allRealLHS.clear();

		// thse are all the scalars
		allRealLHS.addAll(requiresLhsScalarExpandedRhs.keySet());
		// these are all the arrays
		allRealLHS.addAll(realLhsToExpandedLhsNotInitialized.keySet());

		// processing is slightly different after first pass through the equations
		boolean pass1 = true;
		boolean done = false;
		int loopCount = 0;
		while (!done) {

			// this will contain LHS's that have been assigned a value
			ArrayList<String> toRemoveFromRequires = new ArrayList<String>();
			
			for (String realLHS : allRealLHS) { // all RealLHS that still need 

				// If LHS not in realLhsToExpandedLhsMap (which is static) keyset, then we know that this is
				// a non-array LHS
				if (!realLhsToExpandedLhsMap.containsKey(realLHS)) {
					// this section deals with non-array lhs
					// if the key has been removed or the set associated with the key is empty, then
					// we have all values required for the calculation and it can be added to the evaluation list
					if (pass1) {
						if (requiresLhsScalarExpandedRhs.get(realLHS) == null || (requiresLhsScalarExpandedRhs.get(realLHS) != null && requiresLhsScalarExpandedRhs.get(realLHS).size() == 0)) { 
							toRemoveFromRequires.add(realLHS);
						} else {

						}
					} else {
						//  check for constants or those with no remaining requirements
						if (requiresLhsScalarExpandedRhs.get(realLHS) == null || (requiresLhsScalarExpandedRhs.get(realLHS) != null && (requiresLhsScalarExpandedRhs.get(realLHS).size() == 0)) 
								|| hasInitialValue(realLHS, requiresLhsScalarExpandedRhs, equations)) { 
							toRemoveFromRequires.add(realLHS);

						} else {

						}
					}
				} else {

					// this section deals with array lhs

					// if realLHS -> expLHS shows that all expLHS have been processed, add to the list of values that are available
					if (realLhsToExpandedLhsNotInitialized.containsKey(realLHS) &&
							realLhsToExpandedLhsNotInitialized.get(realLHS).size() == 0) {
						toRemoveFromRequires.add(realLHS);
					}

					// now check if the expandedLHS has been completely defined
					// expLHS -> expRHS is empty

					for (String expandedLHS : realLhsToExpandedLhsNotInitialized.get(realLHS)) {

						if (pass1) {
							// if the expanded LHS is no longer in requires expanded
							// or if it is in requires expanded, but has no RHS left to define
							// we can remove this expandedLHS
							if (requiresExpandedLhsExpandedRHS.get(expandedLHS) == null || (requiresExpandedLhsExpandedRHS.get(expandedLHS) != null && requiresExpandedLhsExpandedRHS.get(expandedLHS).size() == 0)) { 
								toRemoveFromRequires.add(expandedLHS);

							} 
						} else {
							Equation eqn = equations.get(realLHS);
							//  check for constants or those with no remaining requirements
							// as above, but also check if an initial value is available
							if (requiresExpandedLhsExpandedRHS.get(expandedLHS) == null || (requiresExpandedLhsExpandedRHS.get(expandedLHS) != null && (requiresExpandedLhsExpandedRHS.get(expandedLHS).size() == 0)) 
									|| hasInitialValue(realLHS, expandedLHS, requiresLhsScalarExpandedRhs, requiresExpandedLhsExpandedRHS, realLhsToExpandedLhsNotInitialized, equations)) {  // was requires (requiresExpanded) MJB 9/12
								toRemoveFromRequires.add(expandedLHS);
							} 
						}
					}
				}
			} // end of loop through all real LHS

			pass1 = false;

//			System.out.println("To Remove Count = "+toRemoveFromRequires.size());
			if (toRemoveFromRequires.size() > 0) {
				terminationLoopCount = 0;
				if (lastCount == toRemoveFromRequires.size()) {
					loopCount++;
//					System.out.println("LoopCount: "+loopCount);
					if (loopCount > 5) {
						System.out.println("Break on loop count "+loopCount);
						break;
					}
				} else {
					loopCount = 0;
				}

				lastCount = toRemoveFromRequires.size();
				
				// this is were the work really takes place manipulating the data structures
				removeRequirement(toRemoveFromRequires, requiresLhsScalarExpandedRhs, requiresExpandedLhsExpandedRHS, realLhsToExpandedLhsNotInitialized, expandedLhsToRealLhsMap, evaluationOrder);
				
				System.out.println("Remaining Equations: scalar "+requiresLhsScalarExpandedRhs.size()+" arrays "+realLhsToExpandedLhsNotInitialized.size());
				// allLHS needs to be reset
				allRealLHS.clear();
				allRealLHS.addAll(requiresLhsScalarExpandedRhs.keySet());
				allRealLHS.addAll(realLhsToExpandedLhsNotInitialized.keySet());
				
			} else {
				terminationLoopCount++;
				if (terminationLoopCount > 1) {
					if (requiresLhsScalarExpandedRhs.size() == 0 && realLhsToExpandedLhsNotInitialized.size() == 0) {
						System.out.println("Successfully ordered all equations");
						done  = true;
						// allLHS needs to be reset from requires and lhsExpandedMap
					} else {
						System.out.println("Cannot order these equations:");
						for (String lhs : requiresLhsScalarExpandedRhs.keySet()) {
							System.out.println("   >>> lhs "+lhs+"  "+requiresLhsScalarExpandedRhs.get(lhs).size());
							System.out.println(equations.get(lhs).getEquation());
							System.out.println(equations.get(lhs).getCleanEquation());
							printRequires(requiresLhsScalarExpandedRhs.get(lhs));
							done = true;
						}

						for (String lhs : realLhsToExpandedLhsNotInitialized.keySet()) {
							
							System.out.println("   >>> lhs <"+lhs+"> "+realLhsToExpandedLhsNotInitialized.get(lhs).size());
							System.out.println(equations.get(lhs).getEquation());
							System.out.println(equations.get(lhs).getCleanEquation());
							printRequires(realLhsToExpandedLhsNotInitialized.get(lhs));

							done = true;
						}

					}
				}
			}
		}
		dumpRequires("postOrderrequiresLhsScalarExpandedRhs.txt", requiresLhsScalarExpandedRhs);
		boolean endChain = true;
		dumpRequiresExpanded("postOrderrequiresExpandedLhsExpandedRHS.txt", requiresExpandedLhsExpandedRHS, endChain); // Here
		dumpRequiresExpanded("postrealLhsToExpandedLhsNotInitialized.txt", realLhsToExpandedLhsNotInitialized, endChain);

		int requiresSize = requiresLhsScalarExpandedRhs.keySet().size();
		int notInitSize = realLhsToExpandedLhsNotInitialized.keySet().size();

//		System.out.println("%%%%%%%%%% "+requiresSize+" "+notInitSize);
		
		System.out.println("Requires data structure contains "+requiresSize+" records");
		for (String req : requiresLhsScalarExpandedRhs.keySet()) {
			if (requiresLhsScalarExpandedRhs.get(req) == null)
				System.out.println("     "+req+" is null");
			else
				System.out.println("     "+req+" "+requiresLhsScalarExpandedRhs.get(req).size());
		}
		
		System.out.println("Not initialized structure contains "+notInitSize+" records");
		for (String req : realLhsToExpandedLhsNotInitialized.keySet()) {
			if (realLhsToExpandedLhsNotInitialized.get(req) == null)
			System.out.println("     "+req+" is null");
			else
			System.out.println("     "+req+" "+realLhsToExpandedLhsNotInitialized.get(req).size());
		}
		
		evaluationOrder.addAll(requiresLhsScalarExpandedRhs.keySet());
		evaluationOrder.addAll(realLhsToExpandedLhsNotInitialized.keySet());

		analyzeEvaluationOrder(evaluationOrder, equations, realLhsToExpandedLhsMap, expandedLhsToRealLhsMap);
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
	
	//hasInitialValue(realLHS, expandedLHS, requiresLhsScalarExpandedRhs, requiresExpandedLhsExpandedRHS, realLhsToExpandedLhsNotInitialized, equations)

	protected boolean hasInitialValue(String realLHS, String expLHS, HashMap<String, HashSet<String>> requiresLhsScalarExpandedRhs, HashMap<String, 
			HashSet<String>> requiresExpandedLhsExpandedRHS, HashMap<String, HashSet<String>> realLhsToExpandedLhsNotInitialized, Map<String, Equation> equations) {
		boolean initialized = false;

		Equation eqn = equations.get(realLHS);
		// functions that support initial values
		if (containsInitialValueFunction(equations.get(realLHS).getVensimEquationOnly())) {

			initialized = true;
			ArrayList<String> initializationVariables = equations.get(realLHS).getFunctionInitialVariables();

			for (String var : initializationVariables) {

				String v = removeValueOf(var).replaceAll("\"", "");
				// the requires
				String vOrig = InformationManagers.getInstance().getNativeDataTypeManager().getOriginalName(v);

				if (Parser.isNumber(v))
					continue;
				if (ArrayReference.isArrayReference(v) || ArrayReference.isArrayReference(vOrig)) {
					if ((realLhsToExpandedLhsNotInitialized.containsKey(v) && realLhsToExpandedLhsNotInitialized.get(v).size() > 0 ) ||
							(realLhsToExpandedLhsNotInitialized.containsKey(vOrig) && realLhsToExpandedLhsNotInitialized.get(vOrig).size() > 0 ))
						initialized = false;
				} else /* scalar */ {
					if ((requiresLhsScalarExpandedRhs.containsKey(v) && requiresLhsScalarExpandedRhs.get(v).size() > 0)  ||
							(requiresLhsScalarExpandedRhs.containsKey(vOrig) && requiresLhsScalarExpandedRhs.get(vOrig).size() > 0)) {
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

		if (!equations.get(realLHS).isOrderedWithInitialValue()) {	
			equations.get(realLHS).setOrderedWithInitialValue(initialized);
		}

		return initialized;
	}

	//  hasInitialValue(realLHS, requiresLhsScalarExpandedRhs, equations))
	protected boolean hasInitialValue(String realLHS, HashMap<String, HashSet<String>> requiresLhsScalarExpandedRhs, Map<String, Equation> equations) {
		boolean initialized = false;

		Equation eqn = equations.get(realLHS);

		//	if (equations.get(lhs).getVensimEquation().contains("IF THEN ELSE")) {
		//	    return true;
		//	}
		// functions that support initial values
		if (containsInitialValueFunction(equations.get(realLHS).getCleanEquation())) {

			// HERE -- get a list of the initialization variables in the statement and check each of them

			//		if (Equation.isNumber(var))
			//			return true;
			//		if (requires.containsKey(var))
			//			return false;

			initialized = true;
			ArrayList<String> initializationVariables = equations.get(realLHS).getFunctionInitialVariables();



			for (String var : initializationVariables) {

				String v = removeValueOf(var).replaceAll("\"", "");
				// the requires
				v = InformationManagers.getInstance().getNativeDataTypeManager().getOriginalName(v);

				if (Parser.isNumber(v))
					continue;
				else if (requiresLhsScalarExpandedRhs.containsKey(v)) {
					initialized = false;
				} else {
//					System.out.println("Not in requires: "+v);
//					System.out.println("Not in requires: "+eqn.getVensimEquationOnly());
				}
			}
		}
		if (!equations.get(realLHS).isOrderedWithInitialValue()) {	
			equations.get(realLHS).setOrderedWithInitialValue(initialized);
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

	protected void removeRequirement(ArrayList<String> toRemoveFromRequires, 
			HashMap<String, HashSet<String>> requiresLhsScalarExpandedRhs,
			HashMap<String, HashSet<String>> requiresExpandedLhsExpandedRHS,
			HashMap<String, HashSet<String>> realLhsToExpandedLhsNotInitialized,
			HashMap<String, String> expandedLhsToRealLhsMap,
			ArrayList<String> evaluationOrder) {

		for (String toRemove : toRemoveFromRequires) {
			
			// toRemove will be either a realLHS or expLHS

			if (ArrayReference.isArrayReference(toRemove)) {

				// we've determined that this array reference can take place
				// remove it from requiresExpanded
				// this does not added to evaluation order until we know that all other references to this array have been processed

				// this will remove expLHS
				if (requiresExpandedLhsExpandedRHS.remove(toRemove) == null) { // "LHS" of equation
//					System.out.println("Bad Remove from requiresExpanded: "+toRemove);
				} else {
//					System.out.println("Successful Remove from requiresExpanded: "+toRemove);
				}

				// this will remove realLHS
				if (realLhsToExpandedLhsNotInitialized.remove(toRemove) == null) { // "LHS" of equation
//					System.out.println("Bad Remove from lhsExpandedNotInitialized: "+toRemove);

				} else {
//					System.out.println("Successful Remove from lhsExpandedNotInitialized: "+toRemove);
				}
				
				// now check the RHS data structures, removing all occurrences
				removeFromRHS(toRemove, requiresLhsScalarExpandedRhs, requiresExpandedLhsExpandedRHS); // "RHS" of equation

				// at this point, we need to see if all array subscripts have been achieved. Then we can add to evaluationOrder

				// once again, to remove can be a realLHS or expLHS
				// cast to its parent realLHS and get the set of expLHS 
				String realLHS = expandedLhsToRealLhsMap.get(toRemove);
				HashSet<String> realLHSToExpLHS = realLhsToExpandedLhsNotInitialized.get(realLHS);
				
				// the the exp/real LHS exists in the set 
				if (realLHSToExpLHS != null && realLHSToExpLHS.contains(toRemove)) {
					// if it's there, get rid of it
					realLHSToExpLHS.remove(toRemove);

					// if the size is now 0, we can add the realLHS to the evaluation order list
					if (realLHSToExpLHS.size() == 0 /* || (realLHSToExpLHS.size() == 1) && realLHSToExpLHS.contains(toRemove) */ ) {  // try this for multi def
//						System.out.println("add to EO: <"+toRemove+"> "+realLHS);
//						validateEO(realLHS, 
//								 requiresLhsScalarExpandedRhs,
//								 requiresExpandedLhsExpandedRHS,
//								 realLhsToExpandedLhsNotInitialized);
						evaluationOrder.add(realLHS);
						
						// never need to process this again
						realLhsToExpandedLhsNotInitialized.remove(realLHS);
						// so we need to remove it from RHS datastructures as this is the last chance we will have
						removeFromRHS(realLHS, requiresLhsScalarExpandedRhs, requiresExpandedLhsExpandedRHS);

					} else {
//						System.out.println(" stillExpLHSMap "+toRemove);
//						System.out.println(" stillExpLHSMap size "+ehs.size());
					}
				} else {
					// real realLHSToExpLHS either was null or toRemove wasn't in there
//					System.out.println("EHS IS "+(ehs==null ? "NULL" : "NOT NULL"));
					if (realLHSToExpLHS!=null) {
//						System.out.println("EHS does not contain "+toRemove+" for "+realLHS);
					} else {
//						// here when the "toRemove" actually was a realLHS originally
//						System.out.println("add to EO: <"+toRemove+"> "+realLHS);
//						validateEO(realLHS, 
//								 requiresLhsScalarExpandedRhs,
//								 requiresExpandedLhsExpandedRHS,
//								 realLhsToExpandedLhsNotInitialized);
						evaluationOrder.add(realLHS);
						// and again remove from to process set and from rhs data structures
						realLhsToExpandedLhsNotInitialized.remove(realLHS);
						removeFromRHS(realLHS, requiresLhsScalarExpandedRhs, requiresExpandedLhsExpandedRHS);
					}
				}


			} else {
				// we remove from requiresLhsScalarExpandedRhs and then check requires and requiresExpanded
				// this will always be a non array LHS
//				validateEO(toRemove, 
//						 requiresLhsScalarExpandedRhs,
//						 requiresExpandedLhsExpandedRHS,
//						 realLhsToExpandedLhsNotInitialized);
				requiresLhsScalarExpandedRhs.remove(toRemove);
				evaluationOrder.add(toRemove);
//				System.out.println("add to EO: "+toRemove);
				removeFromRHS(toRemove, requiresLhsScalarExpandedRhs, requiresExpandedLhsExpandedRHS);
			}
		}
	}
	
	protected boolean validateEO(String eoLHS, 
			HashMap<String, HashSet<String>> requiresLhsScalarExpandedRhs,
			HashMap<String, HashSet<String>> requiresExpandedLhsExpandedRHS,
			HashMap<String, HashSet<String>> realLhsToExpandedLhsNotInitialized) {
		boolean valid = true;
		
		System.out.println("validateEO: "+eoLHS);
		
		if (inMap(eoLHS, requiresLhsScalarExpandedRhs, "requiresLhsScalarExpandedRhs"))
			valid = false;
		if (inMap(eoLHS, requiresExpandedLhsExpandedRHS, "requiresExpandedLhsExpandedRHS"))
			valid = false;
		if (inMap(eoLHS, realLhsToExpandedLhsNotInitialized, "realLhsToExpandedLhsNotInitialized"))
			valid = false;
		
		return valid;
		
	}
	
	protected boolean inMap(String eoLHS, Map<String, HashSet<String>> map, String name) {
		boolean in = false;

		if (map.containsKey(eoLHS)) {
			if (map.get(eoLHS) != null && map.get(eoLHS).size() > 0) {
				in = true;
				System.out.println("inMap: "+name+" "+eoLHS);
				return in;
			}
		}
		for (String k : map.keySet()) {
			if (inSet(eoLHS, map.get(k), name))
				in = true;
		}
		return in;
	}
	
	protected boolean inSet(String eoLHS, HashSet<String> set, String name) {
		boolean in = false;
		if (set == null)
			return in;
		
		if (set.contains(eoLHS)) {
			in = true;
			System.out.println("inSet: "+name+" "+eoLHS);
		}
		return in;
	}

	protected void removeFromRHS(String lhs, HashMap<String, HashSet<String>> requires, 
			HashMap<String, HashSet<String>> requiresExpanded) {

		removeFromRHS(lhs, requires, "requires");
		removeFromRHS(lhs, requiresExpanded, "requiresExpanded");
	}

	protected void removeFromRHS(String lhs, HashMap<String, HashSet<String>> requires, String tag) {
		
		// these will remove realLHS and expLHS where appropriate

		for (String req : requires.keySet()) {

			HashSet<String> hs = requires.get(req);
			if (hs != null && hs.contains(lhs)) {
//				System.out.println(tag+ " RemoveRHS "+req+" from LHS "+lhs);
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
					rhsVars = equations.get(lhs).getRHSVariablesExpanded();

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
							if (i < r && !equations.get(lhs).isOrderedWithInitialValue()) {
								orderingProblem = true;
								bw.append("     <"+rhs+"> "+r + (i < r ? " <<<<<<<<" : "")+"\n");
							} else {
								bw.append("     <"+rhs+"> "+r + "\n");
							}
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
								bw.append(" >>>> initializtion line missing for <"+rhs+"> try individuals\n");
								missingProblem = false;
								// if the rhs is an array reference to a "group"
								// see if the individual array refers have initialization lines
								if (rhs.contains("!") || !ArrayReference.isArrayReference(lhs)) {


									ArrayReference rhsArrayReference = new ArrayReference(rhs.replace("!",""));
									
									for (SubscriptCombination subrCombo : InformationManagers.getInstance().getArrayManager().
											getSubscriptValueCombinations(rhsArrayReference.getSubscriptsAsArray())) {
										// Build an array reference based on the subscript value
										String expandedRHS = "array."+rhsArrayReference.getArrayName()+ "[";
										int subn = 0;
										for (String subscript : rhsArrayReference.getSubscriptsAsArray()) {
											if (subn++ > 0) {
												expandedRHS += ",";
											}
											expandedRHS += subrCombo.getSubscriptValue(subscript);										 
										}
										expandedRHS += "]";
										if (!initializationLine.containsKey(expandedRHS)) {
											bw.append(" >>>> initializtion line missing for individual <"+expandedRHS+">\n");
											missingProblem = true;
										} else {
											r = initializationLine.get(expandedRHS);
											bw.append(" >>>> initializtion line FOUND for individual <"+expandedRHS+"> "+r+"\n");
										}
									}
								
								
								} else 	if (ArrayReference.isArrayReference(lhs)) {
									if (!ArrayReference.isArrayReference(rhs)) {
//										System.out.println("Why do I think that this should be an AR");
										continue;
									}
									ArrayReference rhsArrayReference = new ArrayReference(rhs.replace("!",""));
									ArrayReference lhsArrayReference = new ArrayReference(lhs);
									for (SubscriptCombination subrCombo : InformationManagers.getInstance().getArrayManager().
											getSubscriptValueCombinations(lhsArrayReference.getSubscriptsAsArray())) {
										// Build an array reference based on the subscript value
										String expandedRHS = "array."+rhsArrayReference.getArrayName()+ "[";
										int subn = 0;
										for (String subscript : rhsArrayReference.getSubscriptsAsArray()) {
											if (subn++ > 0) {
												expandedRHS += ",";
											}
											expandedRHS += subrCombo.getSubscriptValue(subscript);										 
										}
										expandedRHS += "]";
										if (!initializationLine.containsKey(expandedRHS)) {
											bw.append(" >>>> initializtion line missing for individual <"+expandedRHS+">\n");
											missingProblem = true;
										} else {
											r = initializationLine.get(expandedRHS);
											bw.append(" >>>> initializtion line FOUND for individual <"+expandedRHS+"> "+r+"\n");
										}
									}
								} else {}
							}
						}
//						if (i > r /*&& (equations.get(lhs).getCleanEquation().contains("INTEG") ||
//				equations.get(lhs).getCleanEquation().contains("SMOOTHI")) */) {
//							problem = false;
//						}
					}
				}
				if (problem) {
					bw.append("######### P R O B L E M ######### \n");
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
//				System.out.println("!!!Evaluation Order PROBLEMS!!! -- see evaluationOrder.txt");
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

//	public static BufferedReader openForRead(String filename) {
//
//		BufferedReader fileReader = null;
//		
//		System.out.println("Open for Read: "+filename);
//
//		// open the file for reading
//		try {
//			fileReader = new BufferedReader (new FileReader(new File(filename)));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return fileReader;
//	}
	
	// getClass().getResourceAsStream("/"+file)
	
	public boolean loadProperties(String file) {
		
			try {
				PROPERTIES.load(getClass().getResourceAsStream("/"+file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		
	}

//	public boolean loadProperties(String file) {
//		File props = new File(file);
//		if (props.exists()) {
//			try {
//				PROPERTIES.load(new FileInputStream(props));
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}

	public boolean loadProperties() {
		return loadProperties(DEFAULT_PROPERTIES_FILE);
	}
	
	public boolean loadUnitsProperties(String file) {
	
			try {
				UNITS_PROPERTIES.load(getClass().getResourceAsStream("/"+file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
	}

//	public boolean loadUnitsProperties(String file) {
//		File props = new File(file);
//		if (props.exists()) {
//			try {
//				getClass().getResourceAsStream("/"+props));
//				UNITS_PROPERTIES.load(new FileInputStream(props));
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}

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
		if (miscDirectory == null) {
			miscDirectory = "miscOutput";
		}
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

	public boolean isInitializeScenarioDirectory() {
		return initializeScenarioDirectory;
	}

	public void setInitializeScenarioDirectory(boolean initializeScenarioDirectory) {
		this.initializeScenarioDirectory = initializeScenarioDirectory;
	}
}
