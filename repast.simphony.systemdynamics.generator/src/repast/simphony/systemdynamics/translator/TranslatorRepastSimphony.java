package repast.simphony.systemdynamics.translator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.systemdynamics.analysis.PolarityCodeBuilder;
import repast.simphony.systemdynamics.engine.Engine;
import repast.simphony.systemdynamics.generator.DirectoryCleaner;
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
	
	public final static String CLOUD_IDENTIFIER = "CLOUD_";
	
	private static final String SRC_GEN = "src-gen";
	private static final String OUTPUT = "output";

	
	private IProject project;
	private IProgressMonitor progressMonitor;
	
	private Engine engine;
	
	public TranslatorRepastSimphony(IProject project, IProgressMonitor progressMonitor, Engine engine) {
		this(engine);
		this.project = project;
		this.progressMonitor = progressMonitor;
	}
	
	
	public TranslatorRepastSimphony(Engine engine) {
		
		this.packageName = "Package";
    	this.supportName = "support";
    	
    	this.unitsConsistency = true;
    	this.generateC = false;
    	this.generateJava = true;
    	
    	this.engine = engine;
    	
    	this.loadProperties();
    	this.loadUnitsProperties();
    	
    	InformationManagers.getInstance().getFunctionManager().load(PROPERTIES.getProperty("functionFile"));
    	loadUnitsEquivalences();
    	
    	Translator.target = ReaderConstants.JAVA;
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
    
    public boolean validateGenerateMDL(String mdlFile, boolean generateCode, List<String> messages) {
    	reader = null;
    	this.objectName = "Object";
    	Translator.target = "JAVA";
    	this.dataType = "Arrays";
    	
    	try {
			this.destinationDirectory = addSrcPath(project, progressMonitor, SRC_GEN);
			this.miscDirectory = addPath(project, progressMonitor, OUTPUT);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	this.packageName = "Package";
    	this.supportName = "support";
    	
    	this.unitsConsistency = true;
    	this.generateC = false;
    	this.generateJava = true;
    	
    	this.loadProperties();
    	this.loadUnitsProperties();
    	
    	InformationManagers.getInstance().getFunctionManager().load(PROPERTIES.getProperty("functionFile"));
    	loadUnitsEquivalences();
    	
    	Translator.target = ReaderConstants.JAVA;
    	
    	
    	System.out.println("********");
    	System.out.println("MFL File: "+mdlFile);
    	System.out.println("Object Name: "+objectName);
    	System.out.println("Target Language: "+target);
    	System.out.println("Data Type: "+dataType);
    	System.out.println("Dest Dir: "+destinationDirectory);
    	System.out.println("Misc Dir: "+miscDirectory);
    	System.out.println("Package: "+packageName);
    	System.out.println("********");
    	
    	messages.add("********");
    	messages.add("MDL File: "+mdlFile);
    	messages.add("Object Name: "+objectName);
//    	messages.add("Target Language: "+target);
//    	messages.add("Data Type: "+dataType);
//    	messages.add("Dest Dir: "+destinationDirectory);
//    	messages.add("Misc Dir: "+miscDirectory);
//    	messages.add("Package: "+packageName);
    	messages.add("********");
    	
    	List<String> mdlContents = new Reader(mdlFile).readMDLFile();
    	if (mdlContents == null) {
    		messages.add("MDL Read failure");
    		return false;
    	}
    	boolean success = validateGenerate(mdlContents, generateCode, messages);
    	return success;
    }
    
    public boolean validateGenerateRSD(SystemModel systemModel, boolean generateCode, List<String> messages) {
    	reader = null;
    	this.objectName = systemModel.getClassName();
    	Translator.target = "JAVA";
    	this.dataType = "Arrays";
    	
    	
    	
//    	addPath(IProject project, IProgressMonitor monitor, String dirName)
    	
    	try {
			this.destinationDirectory = addSrcPath(project, progressMonitor, SRC_GEN);
			this.miscDirectory = addPath(project, progressMonitor, OUTPUT);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	this.packageName = systemModel.getPackage();
    	this.supportName = "support";
    	
    	this.unitsConsistency = true;
    	this.generateC = false;
    	this.generateJava = true;
    	
    	this.loadProperties();
    	this.loadUnitsProperties();
    	
    	InformationManagers.getInstance().getFunctionManager().load(PROPERTIES.getProperty("functionFile"));
    	loadUnitsEquivalences();
    	
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
    	
    	boolean success = true;
    	
    	if (systemModel.getPackage() == null) {
    		messages.add("Package must be non-null on Properties tab");
    		success = false;
    	}
    	
    	if (systemModel.getClassName() == null) {
    		messages.add("Class name must be non-null on Properties tab");
    		success = false;
    	}
    	
    	if (!success)
    		return success;
    	
    	messages.add("********");
    	messages.add("Package: "+systemModel.getPackage());
    	messages.add("Class Name: "+systemModel.getClassName());
    	messages.add("********");
    	
    	List<String> mdlContents = convertToMDL(systemModel);
    	success = validateGenerate(mdlContents, generateCode, messages);
    	return success;
    }
    
    public void generateCodeForRSD(SystemModel systemModel) {

    	reader = null;
    	this.objectName = systemModel.getClassName();
    	Translator.target = "JAVA";
    	this.dataType = "Arrays";
    	
    	
    	try {
			this.destinationDirectory = addSrcPath(project, progressMonitor, SRC_GEN);
			this.miscDirectory = addPath(project, progressMonitor, OUTPUT);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	this.packageName = systemModel.getPackage();
    	this.supportName = "support";
    	
    	this.unitsConsistency = true;
    	this.generateC = false;
    	this.generateJava = true;
    	
    	this.loadProperties();
    	
    	InformationManagers.getInstance().getFunctionManager().load(PROPERTIES.getProperty("functionFile"));
    	
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
    	boolean success = execute(mdlContents);
    	if (!success)
    		System.out.println("Errors Prevent completion of operation");
    }
    
    private List<String> convertToMDL(SystemModel systemModel) {
    	List<String> mdlContents = new ArrayList<String>();
    	
    	Map<String, String> linksTo = new HashMap<String, String>();
    	Map<String, String> linksFrom = new HashMap<String, String>();
    	Map<String, String> uuidToName = new HashMap<String, String>();
    	Map<String, String> nameToUuid = new HashMap<String, String>();
    	
//    	mdlContents.add(MDL_HEADER);
    	
    	
    	// set the various time parameters
    	
    	mdlContents.add(TranslatorConstants.INITIAL_TIME+" = "+systemModel.getStartTime());
    	mdlContents.add(FIELD_SEPARATOR+ systemModel.getUnits());
    	mdlContents.add(FIELD_SEPARATOR);
    	mdlContents.add(EQUATION_TERMINATOR);
    	
    	mdlContents.add(TranslatorConstants.FINAL_TIME+" = "+systemModel.getEndTime());
    	mdlContents.add(FIELD_SEPARATOR+ systemModel.getUnits());
    	mdlContents.add(FIELD_SEPARATOR);
    	mdlContents.add(EQUATION_TERMINATOR);
    	
    	
    	mdlContents.add(TranslatorConstants.TIME_STEP+" = "+systemModel.getTimeStep());
    	mdlContents.add(FIELD_SEPARATOR+ systemModel.getUnits());
    	mdlContents.add(FIELD_SEPARATOR);
    	mdlContents.add(EQUATION_TERMINATOR);
    	
    	
    	mdlContents.add(TranslatorConstants.SAVEPER+" = "+systemModel.getReportingInterval());
    	mdlContents.add(FIELD_SEPARATOR+ systemModel.getUnits());
    	mdlContents.add(FIELD_SEPARATOR);
    	mdlContents.add(EQUATION_TERMINATOR);
    		
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
    		
    		// clouds are just graphic objects, but treated as Variable within the SystemModel
    		// we can safely ignore then...
    		
    		if (variable.getType().equals(VariableType.CONSTANT) &&
    				variable.getName().startsWith(CLOUD_IDENTIFIER))
    			continue;
    		
    		System.out.println("Name: "+variable.getName());
    		System.out.println("Type: "+variable.getType());
    		
    		if (variable.getType().equals(VariableType.STOCK)) {
    			Stock stk = (Stock) variable;
    			stk.getInitialValue();
    		}
    		
    		System.out.println("Units: "+variable.getUnits());
    		System.out.println("LHS: "+variable.getLhs());
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
    			
    			if (variable.getType().equals(VariableType.STOCK)) {
        			Stock stk = (Stock) variable;
        			
        			mdlContents.add(variable.getLhs()+ "= INTEG(");
//    				mdlContents.add(variable.getEquation()+","+stk.getInitialValue()+")");
    				mdlContents.add(variable.getEquation()+")");
    				
        		} else if (variable.getType().equals(VariableType.RATE) || 
        				variable.getType().equals(VariableType.CONSTANT )|| 
        				variable.getType().equals(VariableType.AUXILIARY )) {
    				mdlContents.add(variable.getLhs()+ "=");
    				mdlContents.add(variable.getEquation());
    			} else if (variable.getType().equals(VariableType.LOOKUP)){
//    				mdlContents.add(variable.getLhs()+ "(");
//    				mdlContents.add(variable.getEquation()+")");
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
    	//	processSubscriptDefinition(equations);
    	//	processExponentiaion(equations);
    	//	generateRPN(equations);
    	//	generateTrees(equations);
    	//	generateCausalTrees(sdObjectManager);
    	//	ArrayManager.populateArraySubscriptSpace();
    	//	UnitsManager.performUnitsConsistencyCheck(equations, "./ConsistencyResults.xml");

//    	generateCausalTrees(sdObjectManager);
//    	generatePolarityCode(equations);


    	if (equations != null) {
    		generateMemory();
    		List<String> evaluationOrder = determineEvaluationOrder(equations);
    		if (evaluationOrder != null) {
    			String dir = getSourceDirectory() + "/" + asDirectoryPath(packageName)+ "/";
    			new File(dir).mkdirs();
    			cg = new CodeGenerator(dir, evaluationOrder, equations, objectName, Translator.target, this);
    			cg.generateCode();
    		}
    		try {
    			project.refreshLocal(IResource.DEPTH_INFINITE, null);
    		} catch (CoreException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

    	}
    }

    
    private void generateCausalTrees(SystemDynamicsObjectManager sdObjectManager) {
    	CausalAnalyzer analyzer = new CausalAnalyzer();
    	analyzer.generateCausalTrees(sdObjectManager);
    }
    
    private void  generatePolarityCode(Map<String, Equation> equations) {
    	for (Equation eqn : equations.values()) {
    		if (!eqn.isAssignment())
    			continue;
    		if (eqn.isDefinesLookup() || eqn.isUsesTimeSeries() || eqn.isDefinesLookupGetXls())
    			continue;
    		if (eqn.getVensimEquation().contains("INTEG")) {
    			System.out.println("INTEG");
    		}
    		PolarityCodeBuilder polarityCodeBuilder = new PolarityCodeBuilder(eqn);
    		System.out.println(eqn.getVensimEquation());
    		System.out.println(polarityCodeBuilder.getGeneratedCode());
    	}
    }
    
    private String addPath(IProject project, IProgressMonitor monitor, String dirName) throws CoreException {
        IJavaProject javaProject = JavaCore.create(project);
        
        // workspace relative
        IPath srcPath = javaProject.getPath().append(dirName + "/");
        // project relative
        IFolder folder = project.getFolder(dirName);
     
        if (!folder.exists()) {
          // creates within the project
          folder.create(true, true, monitor);
        }
        
        return project.getLocation().append(srcPath.lastSegment()).toPortableString();
      }
    
    private String addSrcPath(IProject project, IProgressMonitor monitor, String dirName) throws CoreException {
        IJavaProject javaProject = JavaCore.create(project);
        
        // workspace relative
        IPath srcPath = javaProject.getPath().append(dirName + "/");
        // project relative
        IFolder folder = project.getFolder(dirName);
     
        if (!folder.exists()) {
          // creates within the project
          folder.create(true, true, monitor);
          IClasspathEntry[] entries = javaProject.getRawClasspath();
          boolean found = false;
          for (IClasspathEntry entry : entries) {
            if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE && entry.getPath().equals(srcPath)) {
              found = true;
              break;
            }
          }

          if (!found) {
            IClasspathEntry[] newEntries = new IClasspathEntry[entries.length + 1];
            System.arraycopy(entries, 0, newEntries, 0, entries.length);
            IClasspathEntry srcEntry = JavaCore.newSourceEntry(srcPath, null);
            newEntries[entries.length] = srcEntry;
            javaProject.setRawClasspath(newEntries, null);
          }

        }
        
        return project.getLocation().append(srcPath.lastSegment()).toPortableString();
      }
    
    public String getProjectLocation() {
    	return project.getLocation().toPortableString();
    }
    
    public String getProjectName() {
    	return project.getName();
    }

    @Override
	public String getSourceDirectory() {
		return destinationDirectory +"/";
	}
	
    @Override
	public String getScenarioDirectory() {
		 return getProjectLocation() + "/" + getProjectName() + ".rs/";
	}


	public Engine getEngine() {
		return engine;
	}
}
