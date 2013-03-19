package repast.simphony.systemdynamics.translator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.systemdynamics.sdmodel.InfluenceLink;
import repast.simphony.systemdynamics.sdmodel.Stock;
import repast.simphony.systemdynamics.sdmodel.Subscript;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.sdmodel.VariableType;

public class TranslatorRepastSimphony extends Translator {
	
	public final static String EQUATIONS_TERMINATOR = "\\\\\\---/// Sketch information - do not modify anything except names";
	public final static String GRAPHICS_TERMINATOR = "---\\\\\\";
	public final static String MDL_HEADER = "{UTF-8}";
	public final static String FIELD_SEPARATOR = "\t~\t";
	public final static String EQUATION_TERMINATOR = "\t|";
	
	
	public TranslatorRepastSimphony() {
//		this.loadProperties();
//		this.loadUnitsProperties();
//		ReaderConstants.OUTPUT_DIRECTORY = PROPERTIES.getProperty("outputDirectory");
//		ReaderConstants.GENERATED_CODE_DIRECTORY = PROPERTIES.getProperty("generatedCodeDirectory");
//		ReaderConstants.PACKAGE = PROPERTIES.getProperty("package");
//		ReaderConstants.SUPPORT = PROPERTIES.getProperty("support");
//		FunctionManager.load(PROPERTIES.getProperty("functionFile"));
//		
//		loadUnitsEquivalences();
//		initialize();
	}

    @Override
    public void initialize() {
	//	String mdlFile, String objectName, String target, String dataStructure
//	private Reader reader;
//	private String objectName;
//	private String target = "";
//	public static boolean useNativeDataTypes = true;
	
	

	Parameters params = RunEnvironment.getInstance().getParameters();

	reader = new Reader(params.getValueAsString("MDL_FILE"));
	this.objectName = params.getValueAsString("OBJECT_NAME");
	Translator.target = params.getValueAsString("TARGET_LANGUAGE");
	this.dataType = params.getValueAsString("DATA_TYPE");
	this.destinationDirectory = params.getValueAsString("DESTINATION_DIRECTORY");
	this.miscDirectory = params.getValueAsString("MISC_DIRECTORY");
	this.packageName = params.getValueAsString("PACKAGE_NAME");
	this.supportName = ReaderConstants.SUPPORT;
	
	this.unitsConsistency = (Boolean) params.getValue("unitsConsistency");
	this.generateC = (Boolean) params.getValue("generateC");
	this.generateJava = (Boolean) params.getValue("generateJava");
	
	if (Translator.target.equalsIgnoreCase(ReaderConstants.JAVA))
	    Translator.target = ReaderConstants.JAVA;
	else if (Translator.target.equalsIgnoreCase(ReaderConstants.JAVASCRIPT))
	    Translator.target = ReaderConstants.JAVASCRIPT;
	else if (Translator.target.equalsIgnoreCase(ReaderConstants.C))
	    Translator.target = ReaderConstants.C;
	
	System.out.println("********");
	System.out.println("MDL File: "+params.getValueAsString("MDL_FILE"));
	System.out.println("Object Name: "+objectName);
	System.out.println("Target Language: "+target);
	System.out.println("Data Type: "+dataType);
	System.out.println("Dest Dir: "+destinationDirectory);
	System.out.println("Misc Dir: "+miscDirectory);
	System.out.println("Package: "+packageName);
	System.out.println("********");

	startProcess();

    }
    
    public void ingest(SystemModel systemModel) {
    	
    	Map<String, String> linksTo = new HashMap<String, String>();
    	Map<String, String> linksFrom = new HashMap<String, String>();
    	Map<String, String> uuidToName = new HashMap<String, String>();
    	Map<String, String> nameToUuid = new HashMap<String, String>();
    	
    	for (Variable variable : systemModel.getVariables()) {
    		System.out.println("Name: "+variable.getName());
    		System.out.println("Type: "+variable.getType());
    		System.out.println("Units: "+variable.getUnits());
    		System.out.println("Equation: "+variable.getEquation());
    		System.out.println("Subscripts: "+variable.getSubscripts());
    		System.out.println("Comment: "+variable.getComment());
    		System.out.println("Uuid: "+variable.getUuid());
    		System.out.println("#####\n");
    		
    		uuidToName.put(variable.getUuid(), variable.getName());
    		nameToUuid.put(variable.getName(), variable.getUuid());
    	}
    	
    	// need to handle edges
    	
    	for (InfluenceLink link : systemModel.getLinks()) {
    		String id = link.getUuid();
    		String to = link.getTo().getUuid();
    		String from = link.getFrom().getUuid();
    		
    		linksTo.put(id, to);
    		linksFrom.put(id, from);
    		
    		System.out.println("Link From "+uuidToName.get(from)+" to "+uuidToName.get(to));
    		
    	}
    	
    	// subscripts
    	
    	for (Subscript subscript : systemModel.getSubscripts()) {
    		System.out.println("Subscript: "+subscript.getName());
    		for (String element : subscript.getElements()) {
    			System.out.println("   Element: "+element);
    		}
    	}
    }
    
    public void generateCodeForRSD(SystemModel systemModel) {

    	reader = null;
    	this.objectName = systemModel.getClassName();
    	Translator.target = "JAVA";
    	this.dataType = "Arrays";
    	this.destinationDirectory = ".";
    	this.miscDirectory = ".";
    	this.packageName = systemModel.getPackage();
    	this.supportName = "support";
    	
    	this.unitsConsistency = true;
    	this.generateC = false;
    	this.generateJava = true;
    	
    	this.loadProperties();
    	
    	FunctionManager.load(PROPERTIES.getProperty("functionFile"));
    	
    	Translator.target = ReaderConstants.JAVA;
    	
    	
    	System.out.println("********");
    	System.out.println("RSD File: "+systemModel.getClassName());
    	System.out.println("Object Name: "+objectName);
    	System.out.println("Target Language: "+target);
    	System.out.println("Data Type: "+dataType);
    	System.out.println("Dest Dir: "+destinationDirectory);
    	System.out.println("Misc Dir: "+miscDirectory);
    	System.out.println("Package: "+packageName);
    	System.out.println("********");
    	
    	
    	
    	List<String> mdlContents = convertToMDL(systemModel);
    	execute(mdlContents);
    }
    
    private List<String> convertToMDL(SystemModel systemModel) {
    	List<String> mdlContents = new ArrayList<String>();
    	
    	Map<String, String> linksTo = new HashMap<String, String>();
    	Map<String, String> linksFrom = new HashMap<String, String>();
    	Map<String, String> uuidToName = new HashMap<String, String>();
    	Map<String, String> nameToUuid = new HashMap<String, String>();
    	
//    	mdlContents.add(MDL_HEADER);
    	
    	
    	// set the various time parameters
    	
    	mdlContents.add("Initial Time = "+systemModel.getStartTime()+ FIELD_SEPARATOR+ systemModel.getUnits()+FIELD_SEPARATOR+EQUATION_TERMINATOR);
    	mdlContents.add("Final Time = "+systemModel.getEndTime()+ FIELD_SEPARATOR+ systemModel.getUnits()+ FIELD_SEPARATOR+EQUATION_TERMINATOR);
    	mdlContents.add("Time Step = "+systemModel.getTimeStep()+ FIELD_SEPARATOR+ systemModel.getUnits()+ FIELD_SEPARATOR+EQUATION_TERMINATOR);
    	mdlContents.add("Savper = "+systemModel.getReportingInterval()+ FIELD_SEPARATOR+ systemModel.getUnits()+ FIELD_SEPARATOR+EQUATION_TERMINATOR);
    		
// subscripts
    	
    	for (Subscript subscript : systemModel.getSubscripts()) {
    		
    		System.out.println("Subscript: "+subscript.getName());
    		mdlContents.add(subscript.getName()+":");
    		int i = 0;
    		StringBuffer sb = new StringBuffer("\t");
    		for (String element : subscript.getElements()) {
    			if (i++ > 0)
    				sb.append(",");
    			sb.append(element.replace("\n", ""));
    			System.out.println("   Element: "+element);
    		}
    		mdlContents.add(sb.toString());
    		mdlContents.add(FIELD_SEPARATOR);
    		mdlContents.add(FIELD_SEPARATOR);
    		mdlContents.add(EQUATION_TERMINATOR);
    	}
    	
    	
    	
    	
    	
    	for (Variable variable : systemModel.getVariables()) {
    		System.out.println("Name: "+variable.getName());
    		System.out.println("Type: "+variable.getType());
    		
    		if (variable.getType().equals(VariableType.STOCK)) {
    			Stock stk = (Stock) variable;
    			stk.getInitialValue();
    		}
    		
    		System.out.println("Units: "+variable.getUnits());
    		System.out.println("Equation: "+variable.getEquation());
    		System.out.println("Subscripts: "+variable.getSubscripts());
    		for (String s : variable.getSubscripts()) {
    			System.out.println("<"+s+">");
    		}
    		System.out.println("Comment: "+variable.getComment());
    		System.out.println("Uuid: "+variable.getUuid());
    		System.out.println("#####\n");
    		
    		uuidToName.put(variable.getUuid(), variable.getName());
    		nameToUuid.put(variable.getName(), variable.getUuid());
    		
    		if (variable.getName() != null) {
    			// construct the equation
    			String name = variable.getName();
    			StringBuffer sb = new StringBuffer();
    			if (variable.getSubscripts() != null && variable.getSubscripts().size() > 0) {
    				int i = 0;
    				sb.append("[");
    				for (String s : variable.getSubscripts()) {
    					if (i++ > 0)
    						sb.append(",");
    					sb.append(s);
    				}
    				
    				sb.append("]");
    			}
    			
    			String lhs = name+ sb.toString();
    			
//    			mdlContents.add(lhs+" =");
    			
    			
    			if (variable.getEquation().contains("=")) {
    				mdlContents.add(variable.getEquation().split("=")[0]);
    				mdlContents.add(variable.getEquation().split("=")[1]);
    			} else {
    				mdlContents.add(variable.getEquation());
    			}
    			mdlContents.add(FIELD_SEPARATOR+(variable.getUnits() != null ? variable.getUnits() : ""));
    			mdlContents.add(FIELD_SEPARATOR+(variable.getComment() != null ? variable.getComment() : ""));
    			mdlContents.add(EQUATION_TERMINATOR);
    		}
    		
    	}
    	
//    	// need to handle edges
//    	
//    	for (InfluenceLink link : systemModel.getLinks()) {
//    		String id = link.getUuid();
//    		String to = link.getTo().getUuid();
//    		String from = link.getFrom().getUuid();
//    		
//    		linksTo.put(id, to);
//    		linksFrom.put(id, from);
//    		
//    		System.out.println("Link From "+uuidToName.get(from)+" to "+uuidToName.get(to));
//    		
//    	}
//    	
//    	// subscripts
//    	
//    	for (Subscript subscript : systemModel.getSubscripts()) {
//    		System.out.println("Subscript: "+subscript.getName());
//    		for (String element : subscript.getElements()) {
//    			System.out.println("   Element: "+element);
//    		}
//    	}
    	
    	mdlContents.add(EQUATIONS_TERMINATOR);
    	mdlContents.add(GRAPHICS_TERMINATOR);
    	
    	for (String s : mdlContents)
    		System.out.println("<"+s+">");
    	
    	
    	return mdlContents;
    }
    
    @Override
    protected void process(Map<String, Equation> equations) {
	CodeGenerator cg = null;
	processSubscriptDefinition(equations);
	processExponentiaion(equations);
	generateRPN(equations);
	generateTrees(equations);
	generateCausalTrees(sdObjectManager);
	ArrayManager.populateArraySubscriptSpace();
//	UnitsManager.performUnitsConsistencyCheck(equations, "./ConsistencyResults.xml");
	
	
	if (equations != null) {
	    generateMemory();
	    List<String> evaluationOrder = determineEvaluationOrder(equations);
	    if (evaluationOrder != null) {
		new File(destinationDirectory+"/"+"src" + "/" + asDirectoryPath(packageName)+ "/").mkdirs();
		cg = new CodeGenerator(destinationDirectory+ "/"+"src" + "/" + asDirectoryPath(packageName)+ "/", evaluationOrder, equations, objectName, Translator.target, this);
		cg.generateCode();
	    }
	}
    }

    
    private void generateCausalTrees(SystemDynamicsObjectManager sdObjectManager) {
	CausalAnalyzer analyzer = new CausalAnalyzer();
	analyzer.generateCausalTrees(sdObjectManager);
    }
}
