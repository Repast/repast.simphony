package repast.simphony.systemdynamics.translator;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.systemdynamics.support.ArrayReference;
import repast.simphony.systemdynamics.support.MappedSubscriptManager;
import repast.simphony.systemdynamics.support.NamedSubscriptManager;
import repast.simphony.systemdynamics.support.Utilities;

public class CodeGenerator {

    private static int EQUATION_LIMIT = 200;
    private int currentEquationNumber = 0;
    private int currentMethodNumber;

    private static Set<String> indexArraysPacked = new HashSet<String>();

    private static boolean flatten = true;

    private List<String> evaluationOrder;
    private Map<String, Equation> equations;

    private BufferedWriter sourceCode;

    private String start = "INITIAL_TIME";
    private String end = "FINAL_TIME";
    private String step = "TIME_STEP";
    
    private String tStep = "1.0";

    private int doubleUsed = 0;
    private int intUsed = 0;

    private int booleanUsed = 0;

    private int stringUsed = 0;

    private int maxTempInt = -1;
    private int maxTemp = -1;
    private int maxBoolean = -1;
    private int maxString = -1;

    private int nextValueVariable = 0;

    private String objectName;
    private String target;
    private String srcDir;
    
    private Translator translator;

    private Set<String> outerSubscripts = new HashSet<String>();

    private Equation currentGenerate;

    private boolean currentHasLhsArrayReference = false;

    public CodeGenerator(String srcDir, List<String> evaluationOrder, Map<String, Equation> equations, String objectName, String target,
	    Translator translator) {
	this.srcDir = srcDir;
	this.evaluationOrder = evaluationOrder;
	this.equations = equations;
	this.objectName = objectName;
	this.target = target;
	this.translator = translator;

	if (!equations.containsKey("INTITIAL_TIME")) {
	    if (equations.containsKey("INITIALTIME"))
		start = "INITIALTIME";
	    else
		start = "nonexistant";
	}
	if (!equations.containsKey("FINAL_TIME")) {
	    if (equations.containsKey("FINALTIME"))
		end = "FINALTIME";
	    else
		end = "nonexistant";
	}
	if (!equations.containsKey("TIME_STEP")) {
	    if (equations.containsKey("TIMESTEP"))
		step = "TIMESTEP";
	    else
		step = "TIME_STEP";
	}
    }

    public void generate() {
	openFile();
	generateCode();
	closeFile();
    }

    public void openFile() {
	if (Translator.target.equals(ReaderConstants.JAVA)) {
	sourceCode = Utilities.openFileForWriting(srcDir+objectName+".java");
	} else if (Translator.target.equals(ReaderConstants.C)) {
		sourceCode = Utilities.openFileForWriting(srcDir+objectName+".c");
	} else {
	    sourceCode = null;
	}
    }

    public void closeFile() {
	try {
	    sourceCode.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void generateCode() {

//	if (translator.isUnitsConsistency())
//	    performUnitsConsistencyCheck();

	if (translator.isGenerateC() || translator.isGenerateJava()) {
	    openFile();


	    // create the "runner" class for this model
	    if (Translator.target.equals(ReaderConstants.JAVA)) {
		runner(srcDir);
	    }

	    // create the header code for the model
	    // includes imports and constructor
	    if (Translator.target.equals(ReaderConstants.JAVA)) {
		startObject(sourceCode);
	    } else {
		startObjectC(sourceCode);
		InformationManagers.getInstance().getNativeDataTypeManager().generateArrayDeclarationC(sourceCode);	
	    }



	    // write the oneTime method that is used to initial the model components
	    onetime(sourceCode);

	    // generate the methods that are called repeatedly as time progresses
	    repeated(sourceCode);

	    // update any time series values
	    if (Translator.target.equals(ReaderConstants.JAVA)) {
		
		    timeSeriesReferences(sourceCode);
	    }

	    // finally close out the object
	    if (Translator.target.equals(ReaderConstants.JAVA)) 
		endObject(sourceCode);
	    else
		endObjectC(sourceCode);

	    closeFile();
	}
    }

    private void startObject(BufferedWriter bw) {

	// create the header information for the object

	try {
	   
		bw.append("package "+translator.getPackageName()+";\n\n");
	  

	    // imports
	    bw.append("// imports\n\n");

	    bw.append("import "+ReaderConstants.SUPPORT+".MappedSubscriptManager"+";\n");
	    bw.append("import "+ReaderConstants.SUPPORT+".SubscriptCombination"+";\n\n");
	    bw.append("import "+ReaderConstants.SUPPORT+".TimeSeriesData"+";\n");
	    bw.append("import "+ReaderConstants.SUPPORT+".TimeSeriesInstance"+";\n");

	    // to facilitate switching between JavaScript and Java implementations, we have
	    // refactored the code to contain language specific classes

	    if (target.equals(ReaderConstants.JAVA)) {
		
		bw.append("import "+ReaderConstants.SUPPORT+".SDModelWithPropertiesVDM_Native"+";\n");
		bw.append("import repast.simphony.engine.schedule.ISchedule;\n");
		bw.append("import repast.simphony.engine.schedule.ScheduledMethod;\n");
		bw.append("import repast.simphony.engine.environment.RunEnvironment;\n");
		bw.append("import repast.simphony.parameter.Parameters;\n");
		bw.append("import "+ReaderConstants.SUPPORT+".MessageJava"+";\n");
		bw.append("import "+ReaderConstants.SUPPORT+".ResultsReporterJava"+";\n");
		bw.append("import "+ReaderConstants.SUPPORT+".SDFunctionsWithXLSColt"+";\n\n\n");
	    } else {

		bw.append("import "+ReaderConstants.SUPPORT+".SDModel"+";\n");
		bw.append("import "+ReaderConstants.SUPPORT+".MessageJS"+";\n");
		bw.append("import "+ReaderConstants.SUPPORT+".ResultsReporterJS"+";\n");
		bw.append("import "+ReaderConstants.SUPPORT+".SDFunctions"+";\n\n\n");
	    }

	    //	    bw.append("import java.io.BufferedWriter;\n\n");


	    // constructor
	    if (target.equals(ReaderConstants.JAVA)) {
		
		    bw.append("public class "+objectName+" extends SDModelWithPropertiesVDM_Native {\n\n");
		    bw.append("private Memory"+objectName+" memory;\n");

		
	    } else {
		bw.append("public class "+objectName+" extends SDModel {\n\n");
	    }
	    bw.append("public "+objectName+"(String name) {\n");
	    bw.append("this(name, null);\n");
	    bw.append("}\n\n");
	    bw.append("public "+objectName+"(String name, String[] args) {\n");
	    bw.append("super(name, args);\n\n");

	    // perform some initialization based on language

	    if (target.equals(ReaderConstants.JAVA)) {
		bw.append("sdFunctions = new SDFunctionsWithXLSColt(this);\n");
		bw.append("message = new MessageJava();\n");
		bw.append("results = new ResultsReporterJava();\n");

		    bw.append("memory = new Memory"+objectName+"();\n\n\n");
		    bw.append("timeSeriesData.setNativeDataTypes(true);\n");

	    } else {
		bw.append("sdFunctions = new SDFunctions(this);\n");
		bw.append("message = new MessageJS();\n");
		bw.append("results = new ResultsReporterJS();\n");
	    }

	    bw.append("}\n\n");
	    

		bw.append("public Memory"+objectName+" getMemory() {\n");
		bw.append("return memory;\n");
		bw.append("}\n\n");

	    if (target.equals(ReaderConstants.JAVA)) {
		bw.append("@Override\n");
		bw.append("public double getINITIALTIME() {\n");
		
		    bw.append("return memory.getINITIALTIME();\n");
		
		bw.append("}\n");
		bw.append("@Override\n");
		bw.append("public double getFINALTIME() {\n");
		
		    bw.append("return memory.getFINALTIME();\n");
		
		bw.append("}\n");
		bw.append("@Override\n");
		bw.append("public double getTIMESTEP() {\n");
		
		    bw.append("return memory.getTIMESTEP();\n");
		
		bw.append("}\n");
	    }

	    bw.flush();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    private void startObjectC(BufferedWriter bw) {

	// create the header information for the object

	try {
	    bw.append("#include <stdio.h>\n");
	    bw.append("#include <stdlib.h>\n");
	    bw.append("#include <stdarg.h>\n");
	    bw.append("#include <stdbool.h>\n");
	    bw.append("#include <string.h>\n\n");
	    bw.append("#include <float.h>\n\n");
	    bw.append("#include <math.h>\n\n");
	    
	   
	    bw.append("#include \"vensimSupport.h\"\n\n");
	    bw.append("#include \"memory"+objectName+".h\"\n");
	    
	    bw.append("void repeated(double time, double timeStep);\n");
	    
	    bw.append("int main(void) {\n");
	    bw.append("}\n\n");
	    
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private boolean initialValueInitialized(String lhs) {
	String clean = equations.get(lhs).getCleanEquation();
	String[] s = clean.split(",");
	String var = s[s.length-1].replace(")", "");
	if (Parser.isNumber(var))
	    return true;
	if (evaluationOrder.contains(var))
	    return true;
	return false;
    }

    private String getIntialValueVariable(String lhs) {
	String clean = equations.get(lhs).getCleanEquation();
	String[] s = clean.split(",");
	String var = s[s.length-1].replace(")", "");
	return var;
    }



    private void onetime(BufferedWriter bw) {
	
	Map<String, String> initialValues = new HashMap<String, String>();
	String indent = "    ";
	String statement = null;
	try {
	    if (!Translator.target.equals(ReaderConstants.C)) 
		bw.append("protected ");
	    bw.append("void oneTime0() {\n\n");

	    bw.append("double time = 0.0;\n");
	    bw.append("double timeStep = getTIMESTEP();\n");
	    
	    if (!Translator.target.equals(ReaderConstants.C))
		bw.append("Parameters params = RunEnvironment.getInstance().getParameters();\n");


	    // NOTE: only "2" and "3" seem to be used, Need to figure out this "GAME" thing
	    for (String lhs : evaluationOrder) {
		
		Equation eqn = equations.get(lhs);

		// skip function calls for which there is no lhs
		if (equations.get(lhs).getCleanEquation().contains("GAME???")) {
		    String[] bothSides = equations.get(lhs).getCleanEquation().split("=", 2);
		    statement = indent + "setValue(\""+bothSides[0]+"\","+bothSides[1]+"); // 1\n";
		    bw.append(eqn.getUnitsAndComment());
		    bw.append("{\n");
		    bw.append(scrub(statement));
		    bw.append("}\n");
		} else	if (equations.get(lhs).isOneTime()) { 
		    currentEquationNumber++;
		    if (currentEquationNumber > EQUATION_LIMIT) {
			currentEquationNumber = 0;
			currentMethodNumber++;
			resetLimits();
			bw.append("}\n\n");
			if (!Translator.target.equals(ReaderConstants.C))
			    bw.append("protected ");
			bw.append("void oneTime"+currentMethodNumber+"() {\n\n");
			 bw.append("double time = 0.0;\n");
			 bw.append("double timeStep = getTIMESTEP();\n");
			 if (!Translator.target.equals(ReaderConstants.C))
			     bw.append("Parameters params = RunEnvironment.getInstance().getParameters();\n");
		    }
		    if (equations.get(lhs).getCleanEquation().contains("=")) {

			// Need to deal with arrays
			if (equations.get(lhs).isHasLHSArrayReference()) {
			    // TODO: test that this is indeed a initializer 
			    bw.append(eqn.getUnitsAndComment());
			    bw.append("{\n");
			    bw.append(scrub(equations.get(lhs).generateArrayConstantsInitialization()));
			    bw.append("}\n");
			} else {
			    bw.append(eqn.getUnitsAndComment());
			    String[] bothSides = equations.get(lhs).getCleanEquation().split("=", 2);
			    if (InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(bothSides[0]).contains(step))
				tStep = forceDouble(bothSides[1]);
			 
				//				 = (Double) params.getValue("THRESHHOLD_ADJUSTMENT");
				if (Translator.target.equals(ReaderConstants.C)) {
				    statement = indent+InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(bothSides[0])+" = "+forceDouble(bothSides[1])+"; // 2;\n" +
				    "logit(\""+bothSides[0]+"\", 0.0,"+forceDouble(bothSides[1])+");\n"; // 2\n";
				} else {
//				    bw.append("/* oneTime */\n");
				    statement = indent+InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(bothSides[0])+
				    	" = (Double) params.getValue(\""+InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(bothSides[0]).replace("memory.", "")+
				    	"\"); // 2;\n" +
				    	"logit(\""+bothSides[0]+"\", getINITIALTIME(), (Double) params.getValue(\""+
				    	InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(bothSides[0]).replace("memory.", "")+"\"));\n"; // 2\n";

				    // none Repast Execution
				    //				statement = indent+NativeDataTypeManager.getLegalName(bothSides[0])+" = "+forceDouble(bothSides[1])+"; // 2;\n" +
				    //				"log(\""+bothSides[0]+"\", 0.0,"+forceDouble(bothSides[1])+");\n"; // 2\n";
				}

				initialValues.put(InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(bothSides[0]), forceDouble(bothSides[1]));
			  
			    //						if (eqn.isVdmLookup())
			    //						    bw.append("/*\n"); // leave code in, but commented out
			    bw.append("{\n");
			    bw.append(scrub(statement));
			    bw.append("}\n");
			    //						if (eqn.isVdmLookup())
			    //						    bw.append("*/\n"); // leave code in, but commented out
			}
		    } else if (eqn.isDefinesLookup()){
			
			    String lhSide = eqn.getLhs();
			    if (ArrayReference.isArrayReference(lhSide)) {
				// need to get the assigned index for this
				ArrayReference ar = new ArrayReference(lhSide);
//				getTerminalValue(String arrayName, String subscriptName, int dimension)
				statement = InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(lhSide) + 
				"["+InformationManagers.getInstance().getArrayManager().getTerminalValue(ar.getArrayName(), ar.getSubscripts().get(0), 0)+ "]" 
				+ " = " + eqn.getCleanEquation()+"; // 3\n";
			    } else {
				statement = InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(lhSide) + " = " + eqn.getCleanEquation()+"; // 3\n";
			    }
			    bw.append(eqn.getUnitsAndComment());
			    bw.append("{\n");
			    bw.append(scrub(statement));
			    bw.append("}\n");
			
		    } else {
			if (!eqn.isDefinesSubscript()) {
			    statement = indent + equations.get(lhs).getCleanEquation()+"; // 3\n";
			    bw.append(eqn.getUnitsAndComment());
			    bw.append("{\n");
			    bw.append(scrub(statement));
			    bw.append("}\n");
			}
		    }
		} else if (equations.get(lhs).isHasInitialValue() && 
			initialValueInitialized(lhs)) {
		    if (equations.get(lhs).isHasLHSArrayReference()) {

		    } else {

			//Need to deal wirh arrays

			statement = indent + "setValue(\""+lhs+"\","+getIntialValueVariable(lhs)+"); // 4\n";
			bw.append(eqn.getUnitsAndComment());
			bw.append("{\n");
			bw.append(scrub(statement));
			bw.append("}\n");
		    }
		}
		
	    }

	    bw.append("}\n\n");
	    bw.flush();
	    
	    if (!Translator.target.equals(ReaderConstants.C))
		bw.append("protected ");
	    bw.append("void oneTime() {\n\n");
	    bw.append(scrub("memory.TIME_STEP = "+tStep+";\n\n"));

	    for (int i = 0; i <= currentMethodNumber; i++) {
		
		bw.append("   oneTime"+i+"();\n");
	    }


	    bw.append("}\n\n");
	    bw.flush();
	    
	   
		String ScenarioDirectory = translator.getDestinationDirectory() +"/" + objectName + ".rs/";
		RepastSimphonyEnvironment.generateParametersXml(Translator.openReport(ScenarioDirectory+"parameters.xml"), objectName, translator, initialValues);
	   

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    private String forceDouble(String rhs) {

	// this is a constants or an expression consisting of only numbers and operators
	// we need to make sure that the expression will be recognized as a double in java

	return Parser.forceDouble(rhs);
    }
    private void repeated(BufferedWriter bw) {
	try {

	    if (!Translator.target.equals(ReaderConstants.C)){ 
		bw.append("@ScheduledMethod(");
		bw.append("start = 1,");
		bw.append("interval = 1,");
		bw.append("shuffle = true)\n");
		bw.append("public ");
	    }
	    bw.append("void step() {\n");
	    if (!Translator.target.equals(ReaderConstants.C)){ 
		bw.append("ISchedule schedule = repast.simphony.engine.environment.RunEnvironment\n");
		bw.append(".getInstance().getCurrentSchedule();\n");
	    }
	    bw.append(scrub("double timeStep = memory.getTIMESTEP();\n"));

	    bw.append(scrub("double time = memory.getINITIALTIME() + (schedule.getTickCount() - 1.0) * timeStep;\n"));
	    bw.append(scrub("memory.Time = time;\n"));
	    if (!Translator.target.equals(ReaderConstants.C))
		bw.append(scrub("currentTime = time;\n"));
	    bw.append(scrub("repeated(time, timeStep);\n"));
	    bw.append("}\n\n");

	    if (!Translator.target.equals(ReaderConstants.C))
		bw.append("protected ");
	    bw.append("void repeated0(double time, double timeStep) {\n\n");

	    currentMethodNumber = 0;

	    for (String lhs : evaluationOrder) {

		Equation equation = equations.get(lhs);
		if (equation.isRepeated()) {

		    currentEquationNumber++;
		    if (currentEquationNumber > EQUATION_LIMIT) {
			currentEquationNumber = 0;
			currentMethodNumber++;
			resetLimits();
			bw.append("}\n\n");
			if (!Translator.target.equals(ReaderConstants.C))
			    bw.append("protected ");
			bw.append("void repeated"+currentMethodNumber+"(double time, double timeStep) {\n\n");
		    }

		    if (equation.isVdmLookup()) {
			bw.append("/*\n");
			bw.append(" * This is automatically processed\n");
			bw.append(" * Included for documentation purposes\n");
		    }
		    bw.append("{\n");
		    resetCounters();
		    generateCode(equation, bw);
		    bw.append("}\n");
		    if (equation.isVdmLookup()) {
			bw.append("*/\n");
		    }

		}
	    }
	    // will always have an open method
	    bw.append("}\n\n");


	    if (!Translator.target.equals(ReaderConstants.C))
		bw.append("protected ");
	    bw.append("void repeated(double time, double timeStep) {\n\n");
	    if (!Translator.target.equals(ReaderConstants.C)) {
		bw.append("  message.println(\"repeated: \"+time+\" \"+timeStep);\n");
		bw.append("  data.setCurrentTime(time);\n");
		bw.append("  setValue(\"Time\", time);\n");
		bw.append("  timeSeriesData.advanceTime(data, time);\n");
		    bw.append("  updateTimeSeriesReferences(time);\n");
	    }

	    for (int i = 0; i <= currentMethodNumber; i++) {
		bw.append("   repeated"+i+"(time, timeStep);\n");
	    }


	    bw.append("}\n\n");
	    bw.flush();

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
    
    private void timeSeriesReferences(BufferedWriter bw) {
	try {

	    bw.append("protected void updateTimeSeriesReferences(double time) {\n\n");

	    for (String lhs : evaluationOrder) {

		Equation equation = equations.get(lhs);
		if (equation.isUsesTimeSeries()) {
		    bw.append("{\n");
		    bw.append(equation.getEars().getOuterLoops());
		    bw.append(equation.getEars().getLHSassignment());
		    bw.append(" = data.arrayValueOf(\""+equation.getEars().getLhsArrayReference().getArrayName()+"\", ");
		    for (int i = 0; i < equation.getEars().getOuterClosingCount(); i++) {
			if (i > 0)
			    bw.append("+");
			bw.append("\"[\"+outer"+i+"+\"]\"");
		    }
		    bw.append(");\n");
//		    bw.append("/* timeSeriesReferences */\n");
		    bw.append("logit("+equation.getEars().getLHSassignmentName()+",time,"+equation.getEars().getLHSassignment()+");\n");
		    
		    
		    for (int i = 0; i < equation.getEars().getOuterClosingCount(); i++) {
			bw.append("}\n");
		    }
		    bw.append("}\n");
		}
	    }
	    // will always have an open method
	    bw.append("}\n\n");

	    bw.flush();

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public void flattenTree(Node treeRoot) {
	int level = 0;
	if (treeRoot == null)
	    return;
	flattenTree(treeRoot.getChild(), level);
    }

    private void flattenTree(Node node, int level) {
	if (node == null)
	    return;

	//	    System.out.println(">>>>> "+level+"\n"+node.getInfo());

	flattenTree(node.getChild(), level+1);
	// check all children one level down, if any of them are terminal and not placeholders
	// pass their equation tail up to this level and terminate (although we may
	// recursively work our way back up but that will require some more thought)
	//	    if (node.getToken().startsWith(">"))
	//		System.out.println(">");

	Node nextLevel = node.getChild();
	while (nextLevel != null) {

	    // added isDeleted and removed isPlaceHolder

	    if ( /*isLeaf(nextLevel) &&*/ !nextLevel.isPlaceHolder()  && isEligibleToFlatten(nextLevel)) {

		flatten(node, nextLevel);

		nextLevel.setPlaceHolder(true);
		nextLevel.setDeleted(true); // simulated delete
	    }

	    nextLevel = nextLevel.getNext();
	}

	//	    if (!endThisBranch)
	//		flattenTree(node.getChild(), level+1);
	flattenTree(node.getNext(), level);
    }

    private boolean isEligibleToFlatten(Node node) {
	if (node.getGeneratedCodeHead().length() == 0 &&
		node.getGeneratedCodeElse().length() == 0 &&
		node.getGeneratedCodeTail().length() > 0)
	    return true;
	else
	    return false;
    }

    private void flatten(Node node, Node child) {
	String childVar = child.getResultsVariable();

	String childEqn = "("+   child.getGeneratedCodeTail().toString().split("=",2)[1].replace(";", "").trim()+")";

	String flattened = "";
	if (containsChildVar(childVar, node.getGeneratedCodeTail().toString())) {
	    flattened = replaceChildVar(childVar, childEqn, node.getGeneratedCodeTail().toString());
	    node.setGeneratedCodeTail(new StringBuffer(flattened));
	} else if (containsChildVar(childVar, node.getGeneratedCodeHead().toString())) {
	    flattened = replaceChildVar(childVar, childEqn, node.getGeneratedCodeHead().toString());
	    node.setGeneratedCodeHead(new StringBuffer(flattened));
	} else if (containsChildVar(childVar, node.getGeneratedCodeElse().toString())) {
	    flattened = replaceChildVar(childVar, childEqn, node.getGeneratedCodeElse().toString());
	    node.setGeneratedCodeElse(new StringBuffer(flattened));
	} 

    }

    private String replaceChildVar(String childVar, String childEqn, String code) {

	// the occurance of this variable can be as a argument to a method or as
	// a stand-alone variable in an expression
	String replaced = code.replace(childVar+",", childEqn+",").
	replace(childVar+" ", childEqn+" ").
	replace(childVar+")", childEqn+")").
	replace(childVar+";", childEqn+";").
	replace(childVar+")", childEqn+")");
	return replaced;
    }

    private boolean containsChildVar(String childVar, String code) {

	String[] terminators = new String[] {" ", ",", ";", ")"};
	for (String term : terminators) {
	    if (code.contains(childVar+term))
		return true;
	}
	return false;
    }

    public void printTree(Node treeRoot) {
	int level = 0;
	if (treeRoot == null)
	    return;
	System.out.println("Level "+level + " token "+treeRoot.getToken() + "\n["+treeRoot.getInfo()+"]");
	printTree(treeRoot.getChild(), 1);
    }
    
    public void generateTemps(Node treeRoot) {
	StringBuffer sb = new StringBuffer();
	if (treeRoot != null) 
	    sb.append(generateTemps(treeRoot.getChild(), 1));
	if (sb.length() > 0) {
	    addTempsToLHS(treeRoot, sb.toString());
	}
	return;
    }
    
    private void addTempsToLHS(Node treeRoot, String temps) {
	
	Node lhs = treeRoot.getChild();
	
	StringBuffer newHeader = new StringBuffer();
	newHeader.append(temps);
	newHeader.append(lhs.getGeneratedCodeHead());
	lhs.setGeneratedCodeHead(newHeader);
	
	
    }
    
    public String generateTemps(Node node, int level) {
	StringBuffer sb = new StringBuffer();
	if (node != null) {
	    if (!node.isPlaceHolder())
		sb.append(printTemp(node));
	    sb.append(generateTemps(node.getChild(), level+1));
	    sb.append(generateTemps(node.getNext(), level));
	}
	return sb.toString();
    }
    
    public String printTemp(Node node) {

	StringBuffer sb = new StringBuffer();

	if (node.getResultsVariable() != null) {

	    if (node.getResultsVariable().startsWith("_b")) {

		if (Translator.target.equals(ReaderConstants.C))
		    sb.append("bool "+node.getResultsVariable()+" = false;\n");
		else
		    sb.append("boolean "+node.getResultsVariable()+" = false;\n");
	    } else if (node.getResultsVariable().startsWith("_t")) {
		sb.append("double "+node.getResultsVariable()+" = 0.0;\n");
	    } else if (node.getResultsVariable().startsWith("_s")) {
		if (Translator.target.equals(ReaderConstants.C))
		    sb.append("char* "+node.getResultsVariable()+" = (char *) malloc(100);\n");
		else
		    sb.append("String "+node.getResultsVariable()+" = \"\";\n");
	    }
	}

	return sb.toString();
    }

    private void printTree(Node node, int level) {
	if (node == null)
	    return;
	System.out.println("Level "+level + " token "+node.getToken() + "["+node.getInfo()+"]");
	printTree(node.getChild(), level+1);
	printTree(node.getNext(), level);
    }

    private String convertRangeFunction(Node node, Map<Node, String> rhsStatements, String valueVariable) {
	//	    this is where I need to make vector select work, I think
	StringBuffer code = new StringBuffer();
	// code gets translated into a single value
	
	EquationArrayReferenceStructure ears = currentGenerate.getEars();

	String type = getRangeFunctionType(node);
	
	Map<String, Integer> rangeIndex = new HashMap<String, Integer>();

	// first determine the subscripts that range in the function call
	// it is possible that there are none!
	List<Node> arrayReferenceNodes = getArrayReferences(node);
	List<String> rangeSubscripts = new ArrayList<String>();
	int index = 0;
	for (Node arn : arrayReferenceNodes) {
	    ArrayReference ar = new ArrayReference(arn.getToken());
	    for (String rangeSub : ar.getRangeSubscripts()) {
		String subscript = rangeSub;
		if (!rangeIndex.containsKey(subscript)) {
		    rangeIndex.put(subscript, index++);
		}
		rangeSubscripts.add(rangeSub.replace("!", ""));
	    }
	}
	
	// now alter range index ??????????????
	for (Node arn : arrayReferenceNodes) {
	    ArrayReference ar = new ArrayReference(arn.getToken());
	}

	// define the loop
	//	    for (SubscriptCombination sub : getSubscriptValueCombinations("race", "state")) {
	//		value += arrayValueOf("population", concatAsSubscript(sub.getSubscriptValue("race"),sub.getSubscriptValue("state"));
	//	}
	String tVar = getNextDouble();
	if (type.equals("VMAX")) { 
	    code.append("double "+tVar+" = -Double.MAX_VALUE;\n");
	}
	if (type.equals("VMIN")) {
	    code.append("double "+tVar+" = Double.MAX_VALUE;\n");
	}
	
	boolean addRangeBracket = false;

	
//	    code.append("/* CG 868 */\n");
	    code.append(ears.getRangeLoops(arrayReferenceNodes));
	    addRangeBracket = true;

	    //		// this is the number of unique range subscripts that appear on the RHS
	    //		int numRange = ears.getRHSrangeSubscripts().size();
	    //		List<String> rangeSubs = ears.getRHSrangeSubscripts();
	    //		// these are array + "###" + range subscript + "###" + dimension triples 
	    //		int numIndexArrays = ears.getRHSarrayRangeSubscripts().size();
	    //		
	    //		// define the temporary arrays that we'll need
	    //		for (String packedInfo : ears.getRHSarrayRangeSubscripts()) {
	    //		    String[] info = packedInfo.split("###");
	    //		    code.append("int[] indexArray_"+getLegalName(info[0]+"_"+info[1]+"_"+info[2])+"_ = new int[] {"+
	    //			    ArrayManager.getIndicies(info[0], Integer.parseInt(info[2]), info[1])+"};\n");
	    //		}
	    //		
	    //		
	    //		for ( int pos = 0; pos < numRange; pos++) {
	    //		    code.append("// loop for range variable "+rangeSubs.get(pos)+"\n");
	    //		    code.append("for (int range"+pos+" = 0; range"+pos+" < "+
	    //			    NamedSubscriptManager.getValuesFor(rangeSubs.get(pos).replace("!", "")).size()+"; range"+pos+"++) { // need to figure out closing }\n");
	    //		}

	

	if (type.equals("VMAX")) {
	    code.append("if ("+tVar+" < ");

	    Node expressionRoot = node.getChild().getNext().getNext().getNext().getNext();

	    String exp = generateExpression(expressionRoot,  rhsStatements);
	    code.append(exp);
	    code.append(") {\n");
	    code.append(tVar+" = "+exp+";");
	    code.append("\n}\n");
	    code.append("\n}\n");
	    if (addRangeBracket)
		code.append("} /* addRangeBracket */\n");
	    code.append(valueVariable+" = "+tVar+";\n");

	} else if (type.equals("VMIN")) {
	    code.append("if ("+tVar+" > ");

	    Node expressionRoot = node.getChild().getNext().getNext().getNext().getNext();

	    String exp = generateExpression(expressionRoot,  rhsStatements);
	    code.append(exp);
	    code.append(") {\n");
	    code.append(tVar+" = "+exp+";");
	    code.append("\n}\n");
	    code.append("\n}\n");
	    if (addRangeBracket)
		code.append("} /* addRangeBracket */\n");
	    code.append(valueVariable+" = "+tVar+";\n");
	} else {

	    code.append(valueVariable);
	    code.append(" "+(type.equals("SUM") ? "+" : "*")+"= (\n");

	    // generate the code as specified in arg # 5 of the range function
	    // note that this can be a complex tree for the computation

	    Node expressionRoot = node.getChild().getNext().getNext().getNext().getNext();
	    code.append(generateExpression(expressionRoot,  rhsStatements));
	    code.append(");\n");
	    
		for (int i = 0; i < ears.getRangeClosingCount(arrayReferenceNodes); i++)
		code.append("}\n");
	    
	    if (addRangeBracket)
		code.append("} /* addRangeBracket */\n");
	    
	}



	// now that we've generated code. alter the tree

	node.setToken(valueVariable);
	node.setChild(null);

	return code.toString();
    }

    public static String getLegalName(String s ) {
	// cleanup the string for use as a portion of a variable/array name
	String legal = InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(s.replace("!", ""));
	return legal.replace("memory.", "");
    }

    private String generateExpression(Node node, Map<Node, String> rhsStatements) {

	StringBuffer sb = new StringBuffer();

	if (node == null)
	    return "";

	if (isLeaf(node)) {
	    if (rhsStatements.containsKey(node)) {

		return rhsStatements.get(node);
	    } else {
		return node.getToken();
	    }
	}
	if (node.getChild() != null) {
	    sb.append(generateExpression(node.getChild(), rhsStatements));
	}
	sb.append(node.getToken());
	if (node.getChild() != null && node.getChild().getNext() != null) {
	    sb.append(generateExpression(node.getChild().getNext(), rhsStatements));
	}

	return sb.toString();

    }

    private String getValueVariable() {
	if (nextValueVariable == 52)
	    System.out.println("_vv 52");
	String vv = "_vv"+nextValueVariable;
	nextValueVariable++;
	return vv;
    }
    private List<Node> getArrayReferences(Node node) {
	List<Node> al = new ArrayList<Node>();

	if (node == null)
	    return al;

	if (node.getToken().startsWith("array.")) {
	    al.add(node);
	}

	// assume for now that there are no nested references

	List<Node> more = getArrayReferencesChild(node.getChild());
	if (more.size() > 0)
	    al.addAll(more);

//	more = getArrayReferences(node.getNext());
//	if (more.size() > 0)
//	    al.addAll(more);

	return al;
    }
    
    private List<Node> getArrayReferencesChild(Node node) {
	List<Node> al = new ArrayList<Node>();

	if (node == null)
	    return al;

	if (node.getToken().startsWith("array.")) {
	    al.add(node);
	}

	// assume for now that there are no nested references

	List<Node> more = getArrayReferencesChild(node.getChild());
	if (more.size() > 0)
	    al.addAll(more);
	
	more = getArrayReferencesChild(node.getNext());
	if (more.size() > 0)
	    al.addAll(more);

	return al;
    }
    private Node getLHS(Node node) {
	return node.getChild();
    }

    private boolean isArrayReference(Node node) {
	return ArrayReference.isArrayReference(node.getToken());
    }

    private boolean isRangeFunctionNode(Node node) {
	if (node.getToken().startsWith("sdFunctions.SUM") || 
		node.getToken().startsWith("sdFunctions.PROD") ||
		node.getToken().startsWith("sdFunctions.VMIN") ||
		node.getToken().startsWith("sdFunctions.VMAX"))
	    return true;
	else
	    return false;
    }

    private String getRangeFunctionType(Node node) {
	if (node.getToken().startsWith("sdFunctions.SUM"))
	    return "SUM";
	else if (node.getToken().startsWith("sdFunctions.PROD"))
	    return "PROD";
	else if (node.getToken().startsWith("sdFunctions.VMIN"))
	    return "VMIN";
	else if (node.getToken().startsWith("sdFunctions.VMAX"))
	    return "VMAX";
	else 
	    return "???";
    }

    private List<Node> getRangeFunctionReferences(Node node) {
	List<Node> al = new ArrayList<Node>();

	if (node == null)
	    return al;

	if (isRangeFunctionNode(node)) {
	    al.add(node);
	}

	// assume for now that there are no nested references

	List<Node> more = getRangeFunctionReferences(node.getChild());
	if (more.size() > 0)
	    al.addAll(more);

	more = getRangeFunctionReferences(node.getNext());
	if (more.size() > 0)
	    al.addAll(more);

	return al;
    }


    private String generateModifiedCode(Node node) {
	StringBuffer sb = new StringBuffer();

	if (node == null)
	    return "";

	if (!node.getInfo().equals("@"))
	    return node.getInfo();

	if (Parser.isOperator(node.getToken()) || Parser.isBooleanOperator(node.getToken())){
	    if (node.getToken().equals("^")) {
		sb.append("Math.pow(");
		sb.append(generateModifiedCode(node.getChild()));
		sb.append(",");
		sb.append(generateModifiedCode(node.getChild().getNext()));
		sb.append(");\n");
	    } else {
		// boolean operator
		sb.append(generateModifiedCode(node.getChild()));
		sb.append(node.getToken());
		sb.append(generateModifiedCode(node.getChild().getNext()));
	    }

	} else if (Parser.isFunctionInvocation(node.getToken())){
	    // function invocation

	    sb.append(node.getToken()+"(");
	    Node n = node.getChild();
	    sb.append(generateModifiedCode(n));
	    n = n.getNext();
	    while(n != null) {
		sb.append(",");
		sb.append(generateModifiedCode(n));
		n = n.getNext();
	    }

	    sb.append(")");
	} else {
	    // the leaf node
	    sb.append(node.getToken());
	}


	return sb.toString();
    }

    private void generateCode(Equation equation, BufferedWriter bw) {


	resetCounters();

	currentGenerate = equation;
	// This is intended only for "repeated" statements

	EquationArrayReferenceStructure ears = null;
	if((equation.isHasLHSArrayReference() || equation.isHasRHSArrayReference())) {
	    ears = equation.getEars();
	}

	
	System.out.println("GenerateCode: <"+equation.getVensimEquation()+">");
	equation.printTokensOneLine();
	
	generateLHScode(equation, ears);
	generateRHScode(equation, ears);
	

	Node root = equation.getTreeRoot();



	//	    writeGeneratedCode(equation, bw);
	//		    printTree(root);

	if (flatten)
	    flattenTree(root);
	
	generateTemps(root);

	// There are a couple of instances in which we actually need to rewrite
	// some of the code that we have already written
	if (equation.requiresPostGenerationProcessing()) {
	    postGenerationProcessing(equation);
	}


	//		    printTree(root);
	
	writeGeneratedCode(equation, bw);
    }
    
    private void postGenerationProcessing(Equation equation) {
//	System.out.println("##### Going In #####");
//	printTree(equation.getTreeRoot());
	if (equation.isHasVectorSortOrder()) {
	    postGenerationProcessingVectorSortOrder(equation);
	} else if (equation.isHasVectorElmMap()) {
	    postGenerationProcessingVectorElmMap(equation);
	}
	
//	System.out.println("##### Going Out #####");
//	printTree(equation.getTreeRoot());
	
    }
    
    private void postGenerationProcessingVectorSortOrder(Equation equation) {
	
	
	// THIS NEEDS TO CHANGE. WE CANNOT PASS THE ORIGINAL VARIABLE TO VSO
	// MUST CREATE A NEW TEMP ARRAY
	
	Node root = equation.getTreeRoot();
	Node lhs = root.getChild();
	Node rhs = lhs.getNext();
	
	String resultsVariable = rhs.getResultsVariable();
	
	// gather each line of code in the head into an array
	String[] linesOfCode = lhs.getGeneratedCodeHead().toString().split("\n");
	int vectorLength = 0;
	int keepToLine = 0;
	String removedLoopVar = "";
	
	// back up to the line that creates the last "outer" loop
	for (int i = linesOfCode.length-1; i >= 0; i--) {
	    String code = linesOfCode[i];
	    if (code.contains("newIntArray(")) {
		String vLen = code.split("Array\\(")[1].split(",")[0];
		vectorLength = Integer.parseInt(vLen);
		keepToLine = i;
		removedLoopVar = code.split("=")[0].replace("int* ", "")
			.replace("int[] ", "").replace("_index ", "");
		break;
	    }
	}
	
	String scalarDeclaration = "double "+resultsVariable+" = 0.0";
	String vectorDeclaration = "";
	if (Translator.target.equals(ReaderConstants.C)){
//	    vectorDeclaration = "double "+resultsVariable+"["+vectorLength+"];";
	    vectorDeclaration = "double *"+resultsVariable+" = newDoubleArray("+vectorLength+");";
	} else {
	    vectorDeclaration = "double[] "+resultsVariable+" = new double["+vectorLength+"];";
	}
	
	StringBuffer newHeader = new StringBuffer();
	
	for (int i = 0; i < keepToLine; i++) {
	    if (linesOfCode[i].contains(scalarDeclaration)) {
		newHeader.append(vectorDeclaration);
	    } else {
		newHeader.append(linesOfCode[i]);
	    }
	    newHeader.append("\n");
	}
	
	lhs.setGeneratedCodeHead(newHeader);
	
	// need to remove the "}" associated with the removed outer loop
	
	StringBuffer newTail = new StringBuffer();
	linesOfCode = lhs.getGeneratedCodeTail().toString().split("\n");
	
	for (int i = 0; i < linesOfCode.length-1; i++) {
	    if (linesOfCode[i].contains("["+removedLoopVar+"]")) {
		newTail.append(linesOfCode[i].replace("["+removedLoopVar+"]", ""));
	    } else if (linesOfCode[i].contains("logit"))  {
		String newLog = linesOfCode[i].replace("logit", "logitVector").replace(resultsVariable, vectorLength+","+resultsVariable).replace(removedLoopVar, "0");
		newTail.append(newLog);
	    } else {
		newTail.append(linesOfCode[i]);
	    }
	    newTail.append("\n");
	}
	
	lhs.setGeneratedCodeTail(newTail);
	
	// need to remove the removeLoopVar passed in to Vector Sort Order funtion
	// next to last argument
	// editing RHS tail
	
	StringBuffer rhsNewTail = new StringBuffer();
	linesOfCode = rhs.getGeneratedCodeTail().toString().split("\n");
	
	for (int i = 0; i < linesOfCode.length; i++) {
	    if (linesOfCode[i].contains("["+removedLoopVar+"]")) {
		int occurrence = 0;
		String[] stuff = linesOfCode[i].split(",");
		for (int s = 0; s < stuff.length; s++) {
		    if (s > 0){
			rhsNewTail.append(",");
		    }
		    if (stuff[s].contains("["+removedLoopVar+"]")) {
			occurrence++;
			if (occurrence == 1) {
			    // specifying the "value" to func
			    rhsNewTail.append(stuff[s].replace(removedLoopVar, "0"));
			} else if (occurrence == 2) {
			    
			    rhsNewTail.append(stuff[s].replace("["+removedLoopVar+"]", ""));
			} else {
			    rhsNewTail.append(stuff[s]);
			}
		    } else if (stuff[s].contains("intToString("+removedLoopVar+")")) {
			
			    rhsNewTail.append(stuff[s].replace(removedLoopVar, "0"));
			
		    } else {
			rhsNewTail.append(stuff[s]);
		    }
		}

	    } else {
		rhsNewTail.append(linesOfCode[i]);
	    }
	    rhsNewTail.append("\n");
	}
	    rhs.setGeneratedCodeTail(rhsNewTail);
	}
    
    private void postGenerationProcessingVectorElmMap(Equation equation) {
	
	/*
	_t0 = VECTORELMMAP(
		stringConcat("sorted_target_active[",intToString(outer0),"][",intToString(outer1),"]"),
		sorted_target_active[outer0][outer1],
		time,
		timeStep,
		(Target_is_Active[outer0][0]),    <<< needs to be (&Target_is_Active[outer0][0]),
		(Target_Order[outer0][outer1]));
		
		
		
	*/
	
	// if (Translator.target.equals(ReaderConstants.JAVA))
	
	Node root = equation.getTreeRoot();
	Node lhs = root.getChild();
	Node rhs = lhs.getNext();
	
	String zeroOffset = "0";
	String[] invocation = rhs.getGeneratedCodeTail().toString().split(",");
	int index = invocation.length-2;
	if (Translator.target.equals(ReaderConstants.JAVA)) {
	    // NOTE! This [0] doesn't need to be 0!
	    // 
	    zeroOffset = getZeroOffset(invocation[index]);
	    invocation[index] = invocation[index].replace("["+zeroOffset+"]", "");
	} else {
	    invocation[index] = "&" + invocation[index];
	}
	
	index = invocation.length-1;
	invocation[index] = invocation[index].replace("(", "("+zeroOffset+"+");
	
	StringBuffer sb = new StringBuffer();
	int i = 0;
	for (String s : invocation) {
	    if (i++ > 0)
		sb.append(",");
	    sb.append(s);
	}
	rhs.setGeneratedCodeTail(sb);

    }
    
    private String getZeroOffset(String invocation) {
	String zeroOffset = "";
	String notation = "\\[[0-9]+\\]\\)";
	Pattern p = Pattern.compile(notation);
	Matcher m = p.matcher(invocation);
	boolean found = m.find();
	String fnd = m.group();
	zeroOffset = fnd.replace("[", "").replace("])", "");
	return zeroOffset;
    }

    private void generateLHScode(Equation equation, EquationArrayReferenceStructure ears) {
	// generate the code for the LHS
	// nodes will tell child nodes into which variable
	// to place their result

	Node lhs = null;
	String resultsVariable;
	
	    Node root = equation.getTreeRoot();
//	    if (root == null) {
//		System.out.println("BRKPT (root)");
//	    }
	    lhs = root.getChild();
	    if (lhs == null) {
		System.out.println("BRKPT (rhs)");
	    }
	    Node rhs = lhs.getNext();
//	    if (rhs == null) {
//		System.out.println("BRKPT (lhs)");
//	    }
	    resultsVariable = getNextDouble();
	    rhs.setResultsVariable(resultsVariable);
	


	if (!isArrayReference(lhs)) {

	    currentHasLhsArrayReference = false;
	    outerSubscripts.clear();

	    // one of two forms:
	    // A = B
	    // generates:
	    // _d = RHS head

	    // setValue(A, _d) tail

	  
		lhs.getGeneratedCodeTail().append(InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(lhs.getToken())+" = "+resultsVariable+";\n");
//		lhs.getGeneratedCodeTail().append("/* generateLHScode */\n");
		lhs.getGeneratedCodeTail().append("logit(\""+lhs.getToken()+"\",time,"+resultsVariable+");\n");
	   
	} else {
	    
		ArrayReferenceNative ar = new ArrayReferenceNative(lhs.getToken(), equation);
		currentHasLhsArrayReference = true;
		outerSubscripts.addAll(ar.getSubscripts());
		lhs.getGeneratedCodeHead().append(ar.generateLHSHeader(resultsVariable));
		lhs.getGeneratedCodeTail().append(ar.generateLHSFooter(resultsVariable));
	    }
	
    }

    private void generateRHScode(Equation equation, EquationArrayReferenceStructure ears) {
	// generate the code for the RHS

	Node node = equation.getTreeRoot().getChild().getNext(); // -> this is RHS

	generateRHSCode(node);

    }

    private void printNode(Node node, boolean traverseChildSiblings) {
	if (node == null) {
	    return;
	}

	if (Equation.isITENode(node)) {

	    // skip placeholders
	    Node condition = node.getChild().getNext().getNext().getNext().getNext();
	    Node thenExpression = condition.getNext();
	    Node elseExpression = thenExpression.getNext();

	    // print the condition Expression

	    printNode(condition, false);
	    printNodeCodeHead(node);
	    printNode(thenExpression, false);
	    printNodeCodeElse(node);
	    printNode(elseExpression, false);
	    printNodeCodeTail(node);


	} else {
	    printNode(node.getChild(), true);
	    if (traverseChildSiblings) 
		printNode(node.getNext(), true);
	    printNodeCode(node);
	}
    }

    private void printNodeCode(Node node) {
	try {
	    if (!node.isPlaceHolder()) {
		if (node.getGeneratedCodeHead().length() > 0) {
		    sourceCode.append(scrub(node.getGeneratedCodeHead().toString()));
		}

		if (node.getGeneratedCodeElse().length() > 0) {
		    sourceCode.append(scrub(node.getGeneratedCodeElse().toString()));
		}

		if (node.getGeneratedCodeTail().length() > 0) {
		    sourceCode.append(scrub(node.getGeneratedCodeTail().toString()));
		}
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    private void printNodeCodeHead(Node node) {
	try {
	    if (!node.isPlaceHolder()) {
		if (node.getGeneratedCodeHead().length() > 0) {
		    sourceCode.append(scrub(node.getGeneratedCodeHead().toString()));
		}
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    private void printNodeCodeElse(Node node) {
	try {
	    if (!node.isPlaceHolder()) {

		if (node.getGeneratedCodeElse().length() > 0) {
		    sourceCode.append(scrub(node.getGeneratedCodeElse().toString()));
		}
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    private void printNodeCodeTail(Node node) {
	try {
	    if (!node.isPlaceHolder()) {
		if (node.getGeneratedCodeTail().length() > 0) {
		    sourceCode.append(scrub(node.getGeneratedCodeTail().toString()));
		}
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void writeGeneratedCode(Equation equation, BufferedWriter bw) {
	// traverse the tree and write the code to file

	try {
	    Node root = equation.getTreeRoot();

	    //		System.out.println("T R E E *********************************************************************");
	    //		    equation.printTree();
	    //		    System.out.println("T R E E   C O D E ===========================================================");
	    //		    equation.printTreeCode();


	    Node lhs = root.getChild();
	    Node rhs = lhs.getNext();
	    bw.append(equation.getUnitsAndComment());
	    bw.append(scrub(lhs.getGeneratedCodeHead().toString()));
	    boolean traverseChildSiblings = true;
	    printNode(rhs, traverseChildSiblings);  // print the right hand side
	    bw.append(scrub(lhs.getGeneratedCodeTail().toString()));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }


    // this seems to work OK without Array References -- need to incorporate and then pass all
    // equations through this.
    // code will be stored in the nodes for later printing -- where we will need to worry about ordering
    // 

    private void generateRHSCode(Node node) {
	StringBuffer sb = new StringBuffer();

	StringBuffer theHead;
	StringBuffer theTail;
	StringBuffer theElse;

	theHead = node.getGeneratedCodeHead();
	theElse = node.getGeneratedCodeElse();
	theTail = node.getGeneratedCodeTail();

	String tempVariable = "";
	String booleanVariable = "";

	if (node == null)
	    return;

	if (!node.isPlaceHolder()) {

	    // this node has already been assigned the variable into which to store it's value;

	    if (isRangeFunctionNode(node)) {
		// ignore the place holders
		Node n = node.getChild();
		for (int i = 0; i < 4; i++) {
		    n.setPlaceHolder(true);
		    n = n.getNext();
		}
		// we will in fact ignore the fifth as well as we are inlining this code
		// make sure our result varaible is initialized
		theHead.append(node.getResultsVariable()+" = 0.0;\n");
		theHead.append(convertRangeFunction(node,genRHSStatements(node), node.getResultsVariable()));

		n.setPlaceHolder(true);
		// create the loop over range variables
		// sum/prod of values into results
	    } else  if (node.getToken().equals("sdFunctions.IFTHENELSE")) {
		// need to specify the dest var for cond, true, and false
		Node condition = getCONDITION(node);
		String conditionVariable = getNextBoolean();
		condition.setResultsVariable(conditionVariable);

		Node thenNode = getTHEN(node);
		String thenVariable = getNextDouble();
		thenNode.setResultsVariable(thenVariable);		

		Node elseNode = getELSE(node);
		String elseVariable = getNextDouble();
		elseNode.setResultsVariable(elseVariable);

		// the first four arguments are placeholders and should not be processed
		Node n = node.getChild();
		for (int i = 0; i < 4; i++) {
		    n.setPlaceHolder(true);
		    n = n.getNext();
		}

		//generate head and tail code

		theHead.append("if ("+conditionVariable+") {\n");

		boolean rvDouble = node.getResultsVariable().startsWith("_t");
		boolean thenVariableDouble = thenVariable.startsWith("_t");
		boolean elseVariableDouble = thenVariable.startsWith("_t");
		boolean rvBoolean = !rvDouble;
		boolean thenVariableBoolean = !thenVariableDouble;
		boolean elseVariableBoolean = !elseVariableDouble;

		// we may need to cast double values to boolean
		String cast = "";
		if (rvBoolean && thenVariableDouble) {
		    cast = " == 1 ";
		}

		theElse.append(node.getResultsVariable()+" = "+thenVariable+cast+";\n");
		theElse.append("} else {\n");

		cast = "";
		if (rvBoolean && elseVariableDouble) {
		    cast = " == 1 ";
		}

		theTail.append(node.getResultsVariable()+" = "+elseVariable+cast+";\n");
		theTail.append("}\n");

	    } else if (Parser.isOperator(node.getToken()) || Parser.isBooleanOperator(node.getToken())){

		String variable1 = "";
		String variable2 = "";
		// the operator is either unary or binary if not boolean
		// when working with boolean operators, will need to look ahead to determine if child
		// nodes are boolean or doubles
		if (!Parser.isBooleanOperator(node.getToken())) {
		    variable1 = getNextDouble();
		    Node left = node.getChild();
		    left.setResultsVariable(variable1);

		    if (left.getNext() == null) {
			// unary operator 
			theTail.append(node.getResultsVariable()+" = "+node.getToken().replace("_", "-")+" "+variable1+";\n");
		    } else {
			// binary operator
			variable2 = getNextDouble();
			Node right = left.getNext();
			right.setResultsVariable(variable2);
			if (node.getToken().equals("^")) {
			    theTail.append(node.getResultsVariable()+" = Math.pow("+variable1+","+variable2+");\n");
			} else {
			    theTail.append(node.getResultsVariable()+" = "+variable1+" "+node.getToken()+" "+variable2+";\n");
			}
		    }
		} else {
		    // working with either a logical operator or a relational operator as root
		    // convert from Vensim operator to its Java equivalent
		    if (node.getToken().equals(":AND:"))
			node.setToken("&&");
		    if (node.getToken().equals(":OR:"))
			node.setToken("||");
		    if (node.getToken().equals(":NOT:"))
			node.setToken("!");

		    if (Parser.isLogicalOperator(node.getToken())) {
			variable1 = getNextBoolean();
			variable2 = getNextBoolean();
		    } else { // relational operator
			variable1 = getNextDouble();
			variable2 = getNextDouble();
		    }

		    if (Parser.isBinaryOperator(node.getToken())) { 
			node.getChild().setResultsVariable(variable1);
			node.getChild().getNext().setResultsVariable(variable2);
			theTail.append(node.getResultsVariable()+" = "+variable1+" "+node.getToken()+" "+variable2+";\n");
		    } else {
			node.getChild().setResultsVariable(variable1);
			theTail.append(node.getResultsVariable()+" = "+node.getToken()+" ("+variable1+");\n");
		    }
		}
	    } else if (node.getToken().equals("sdFunctions.VECTORSELECT")){
		String suffix1 = getNextInt();
		String suffix2 = getNextInt();
		ArrayReference ar1 = null;
		ArrayReference ar2 = null;
		Node arg1 = node.getChild().getNext().getNext().getNext().getNext();
		Node arg2 = arg1.getNext();

		// these should be arrays and their range subscripts show be the same
		if (ArrayReference.isArrayReference(arg1.getToken())) {
		    ar1 = new ArrayReference(arg1.getToken());
		}
		if (ArrayReference.isArrayReference(arg2.getToken())) {
		    ar2 = new ArrayReference(arg2.getToken());
		}

		List<String> range = ar1.getRangeSubscriptsNames();





		if (Translator.target.equals(ReaderConstants.C))
		    theHead.append("double _da"+suffix1+"["+InformationManagers.getInstance().getNamedSubscriptManager().getNumIndexFor(range.get(0))+"];\n");
		else
		    theHead.append("double[] _da"+suffix1+" = new double"+"["+InformationManagers.getInstance().getNamedSubscriptManager().getNumIndexFor(range.get(0))+"];\n");

		theHead.append("int _i"+suffix1+" = 0;\n");
		// need a range loop
		ArrayReferenceNative ar1n = new ArrayReferenceNative(arg1.getToken(), currentGenerate);
		EquationArrayReferenceStructure ears = currentGenerate.getEars();
		List<Node> arrayReferenceNodes = getArrayReferences(node);
		//		    theHead.append("/* CG 1571 */\n");
		//		    theHead.append("{\n");
		theHead.append(ears.getRangeLoops(arrayReferenceNodes));
		theHead.append("_da"+suffix1+"[_i"+suffix1+"++] = "+ar1n.generateRHSImplementation()+";\n");
		for (int i = 0; i < ears.getRangeClosingCount(); i++)
		    theHead.append("}\n");
		theHead.append("}\n");

		if (ar2 != null) {
		    range = ar2.getRangeSubscriptsNames();


		    if (Translator.target.equals(ReaderConstants.C))
			theHead.append("double _da"+suffix2+"["+InformationManagers.getInstance().getNamedSubscriptManager().getNumIndexFor(range.get(0))+"];\n");
		    else
			theHead.append("double[] _da"+suffix2+" = new double"+"["+InformationManagers.getInstance().getNamedSubscriptManager().getNumIndexFor(range.get(0))+"];\n");

		    theHead.append("int _i"+suffix2+" = 0;\n");
		    // need a range loop
		    ar1n = new ArrayReferenceNative(arg2.getToken(), currentGenerate);

		    ears = currentGenerate.getEars();
		    arrayReferenceNodes = getArrayReferences(node);
		    theHead.append(ears.getRangeLoops(arrayReferenceNodes));

		    theHead.append("_da"+suffix2+"[_i"+suffix2+"++] = "+ar1n.generateRHSImplementation()+";\n");
		    for (int i = 0; i < ears.getRangeClosingCount(); i++)
			theHead.append("}\n");
		    theHead.append("}\n");

		} else {
		    // here we have an arithmetic expression that evaluates to an array of the same length
		    // as the _da1 array
		    // for example:

		    //		        VECTOR SELECT(
		    //		        	Path resource definition[EnergyPath!,Resource],
		    //		        	Energy to meet demand[EnergyPath!]/Efficiency[EnergyPath!],
		    //		        	0,
		    //		        	VSSUM,
		    //		        	VSERRATLEASTONE)

		    // We want to generate the child, the node, the childs next repeatedly

		    if (Translator.target.equals(ReaderConstants.C))
			theHead.append("double _da"+suffix2+"["+InformationManagers.getInstance().getNamedSubscriptManager().getNumIndexFor(range.get(0))+"];\n");
		    else
			theHead.append("double[] _da"+suffix2+" = new double"+"["+InformationManagers.getInstance().getNamedSubscriptManager().getNumIndexFor(range.get(0))+"];\n");

		    theHead.append("int _i"+suffix2+" = 0;\n");
		    // need a range loop
		    // arg2 is not an array reference -- that is why we are in this portion of the code.
		    // that means it is some form of expression

		    ears = currentGenerate.getEars();
		    arrayReferenceNodes = getArrayReferences(node);
		    theHead.append(ears.getRangeLoops(arrayReferenceNodes));

		    // we need to generate the code based on a tree with this argument as the root.
		    //			ArrayReferenceNative ar1n = new ArrayReferenceNative(arg2.getToken(), currentGenerate);

		    // generateExpression (currentGenerate, node);
		    theHead.append("_da"+suffix2+"[_i"+suffix2+"++] = "+generateExpression(arg2 /*, genRHSStatements(node) */)+";\n");   // here no genRHSS

		    for (int i = 0; i < ears.getRangeClosingCount(); i++)
			theHead.append("}\n");
		    theHead.append("}\n");

		    arg2.setPlaceHolder(false);
		}


		theTail.append(node.getResultsVariable()+" = ");
		theTail.append(node.getToken()+"(");

		Node n = node.getChild();

		// 1st and second arguments need to be updated if they refer to arrays as subscript
		// definition is created as we go and may referenced them before defined

		String lhs = currentGenerate.getLhs();
		String valueArg;
		String nameArg;
		if (ArrayReference.isArrayReference(lhs)) {

		    valueArg = new ArrayReferenceNative(lhs, currentGenerate).generateRHSImplementation();
		    nameArg = new ArrayReferenceNative(lhs, currentGenerate).generateRHSName();


		} else {
		    valueArg = InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(lhs);

		    nameArg = "\""+lhs+"\"";
		}


		// the first four arguments are placeholders and should not be processed
		// 1st and second arguments need to be updated if they refer to arrays as subscript
		// definition is created as we go and may referenced them before defined

		int numSkip = 4;

		for (int i = 0; i < numSkip; i++) {
		    if (i > 0)
			theTail.append(",");
		    if (i == 1)
		    {
			n.setToken(valueArg);
		    } else if (i == 0) {
			n.setToken(nameArg);
		    }
		    theTail.append(n.getToken());
		    n.setPlaceHolder(true);
		    n = n.getNext();
		}

		// These two parameters need to be arrays that are generated in separate equations.
		// model this after how we generate code for it then else

		theTail.append(",");

		String name = "_da1";
		//			n.setResultsVariable(name);
		//			theTail.append(name);


		nameArg = "_da"+suffix1;
		n.setToken(nameArg);
		n.setPlaceHolder(true);
		theTail.append(nameArg);
		n = n.getNext();
		theTail.append(",");
		nameArg = "_da"+suffix2;
		n.setPlaceHolder(true);
		n.setToken(nameArg);
		theTail.append(nameArg);
		n = n.getNext();

		while(n != null) {

		    theTail.append(",");

		    String t = getNextDouble();
		    n.setResultsVariable(t);
		    theTail.append(t);
		    n = n.getNext();
		}


		theTail.append(");\n");


	    } else if (node.getToken().equals("sdFunctions.SAMPLEIFTRUE")){
		theTail.append(node.getResultsVariable()+" = ");
		theTail.append(node.getToken()+"(");

		Node n = node.getChild();

		// 1st and second arguments need to be updated if they refer to arrays as subscript
		// definition is created as we go and may referenced them before defined

		String lhs = currentGenerate.getLhs();
		String valueArg;
		String nameArg;
		if (ArrayReference.isArrayReference(lhs)) {

		    valueArg = new ArrayReferenceNative(lhs, currentGenerate).generateRHSImplementation();
		    nameArg = new ArrayReferenceNative(lhs, currentGenerate).generateRHSName();


		} else {

		    valueArg = InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(lhs);

		    nameArg = "\""+lhs+"\"";
		}


		// the first four arguments are placeholders and should not be processed
		// 1st and second arguments need to be updated if they refer to arrays as subscript
		// definition is created as we go and may referenced them before defined

		for (int i = 0; i < 4; i++) {
		    if (i > 0)
			theTail.append(",");
		    if (i == 1)
		    {
			n.setToken(valueArg);
		    } else if (i == 0) {
			n.setToken(nameArg);
		    }
		    theTail.append(n.getToken());
		    n.setPlaceHolder(true);
		    n = n.getNext();
		}

		// need to deal with GET DATA BETWEEN TIMES as one of its parameters to be
		// passed is a string rather than a double!

		theTail.append(",");

		String b = getNextBoolean();
		n.setResultsVariable(b);
		theTail.append(b);
		n = n.getNext();

		while(n != null) {

		    theTail.append(",");

		    String t = getNextDouble();
		    n.setResultsVariable(t);
		    theTail.append(t);
		    n = n.getNext();
		}


		theTail.append(");\n");
	    } else if (node.getToken().equals("sdFunctions.GETDATABETWEENTIMES")){

		//		    /*
		//			Equation: RS PFC at start= INITIAL(GET DATA BETWEEN TIMES(RS PFC, Other emissions cut start time , 0))
		//			Units:tons/Year
		//			Comment: None Provided
		//		*/
		//		_t0 = sdFunctions.INITIAL(
		//			"RS PFC at start",
		//			valueOf("RS PFC at start"),
		//			time,
		//			timeStep,
		//				(sdFunctions.GETDATABETWEENTIMES(
		//					"RS PFC at start",
		//					valueOf("RS PFC at start"),
		//					time,
		//					timeStep,
		//					("RS PFC"),
		//					(valueOf("Other emissions cut start time")),
		//					(0))));
		//		setValue("RS PFC at start", _t0);

		theTail.append(node.getResultsVariable()+" = ");
		theTail.append(node.getToken()+"(");

		Node n = node.getChild();

		// 1st and second arguments need to be updated if they refer to arrays as subscript
		// definition is created as we go and may referenced them before defined

		String lhs = currentGenerate.getLhs();
		String valueArg;
		String nameArg;
		
		valueArg = InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(lhs);
		    nameArg = "\""+lhs+"\"";
		    
		    
//		if (ArrayReference.isArrayReference(lhs)) {
//		    valueArg = new ArrayReferenceNative(lhs, currentGenerate).generateRHSImplementation();
//		    nameArg = new ArrayReferenceNative(lhs, currentGenerate).generateRHSName();
//		} else {
//		    valueArg = NativeDataTypeManager.getLegalName(lhs);
//		    nameArg = "\""+lhs+"\"";
//		}


		// the first four arguments are placeholders and should not be processed
		// 1st and second arguments need to be updated if they refer to arrays as subscript
		// definition is created as we go and may referenced them before defined

//		for (int i = 0; i < 4; i++) {
//		    if (i > 0)
//			theTail.append(",");
//		    if (i == 1)
//		    {
//			n.setToken(valueArg);
//		    } else if (i == 0) {
//			n.setToken(nameArg);
//		    }
//		    theTail.append(n.getToken());
//		    n.setPlaceHolder(true);
//		    n = n.getNext();
//		}

		// need to deal with GET DATA BETWEEN TIMES as one of its parameters to be
		// passed is a string rather than a double!

//		theTail.append(",");

		String name = getNextString();
		n.setResultsVariable(name);
		theTail.append(name);

		String aRef = n.getToken();

//		if (ArrayReference.isArrayReference(aRef)) {
//		    nameArg = new ArrayReference(aRef).generateRHSName();
//		} else {
//		    nameArg = generateRHSName(aRef);
//		}

		//		}   NEED TO CHECK THIS!


		n.setToken(nameArg);
		n = n.getNext();

		while(n != null) {

		    theTail.append(",");

		    String t = getNextDouble();
		    n.setResultsVariable(t);
		    theTail.append(t);
		    n = n.getNext();
		}


		theTail.append(");\n");
	    } else if (Parser.isFunctionInvocation(node.getToken())){



		theTail.append(node.getResultsVariable()+" = ");
		theTail.append(node.getToken()+"(");

		Node n = node.getChild();

		// 1st and second arguments need to be updated if they refer to arrays as subscript
		// definition is created as we go and may referenced them before defined

		String lhs = currentGenerate.getLhs();
		String valueArg;
		String nameArg;
		if (ArrayReference.isArrayReference(lhs)) {

		    valueArg = new ArrayReferenceNative(lhs, currentGenerate).generateRHSImplementation();
		    nameArg = new ArrayReferenceNative(lhs, currentGenerate).generateRHSName();


		} else {

		    valueArg = InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(lhs);
		    nameArg = "\""+lhs+"\"";
		}


		// the first four arguments are placeholders and should not be processed
		// 1st and second arguments need to be updated if they refer to arrays as subscript
		// definition is created as we go and may referenced them before defined

		int numTransfer = 4;

		if (node.getToken().startsWith("sdFunctions.LOOKUP"))
		    numTransfer = 0;

		for (int i = 0; i < numTransfer; i++) {
		    if (i > 0)
			theTail.append(",");
		    if (i == 1)
		    {
			n.setToken(valueArg);
		    } else if (i == 0) {
			n.setToken(nameArg);
		    }
		    theTail.append(n.getToken());
		    n.setPlaceHolder(true);
		    n = n.getNext();
		}

		// need to deal with GET DATA BETWEEN TIMES as one of its parameters to be
		// passed is a string rather than a double!

		int cnt = 0;

		while(n != null) {
		    if (numTransfer == 4 || (numTransfer == 0 && cnt > 0))
			theTail.append(",");
		    cnt++;

		    String t = getNextDouble();
		    n.setResultsVariable(t);
		    theTail.append(t);
		    n = n.getNext();
		}


		theTail.append(");\n");

	    } else {
		// Either SCALAR or ARRAY Reference
		// the leaf node
		if (isArrayReference(node) ) { // those that will evaluate to a double
		    if (node.getResultsVariable() != null) {
			if (node.getResultsVariable().startsWith("_s")) {
			    theTail.append(node.getResultsVariable()+" = "+node.getToken()+";\n");
			} else {

			    theTail.append(node.getResultsVariable()+" = "+new ArrayReferenceNative(node.getToken(), currentGenerate).generateRHSImplementation()+";\n");

			}
		    } else {

			theTail.append(node.getResultsVariable()+" = "+new ArrayReferenceNative(node.getToken(), currentGenerate).generateRHSImplementation()+";\n");

		    }
		} else {
		    // Note: we may actually attempt to assign a double to a boolean (nested ITE)
		    // check the results variable -- it will be of the proper type

		    if (node.getResultsVariable() == null) {
			System.out.println("HUH?");

		    }

		    boolean resultTypeDouble = node.getResultsVariable().startsWith("_t");
		    boolean resultTypeBoolean = node.getResultsVariable().startsWith("_b");
		    boolean resultString = node.getResultsVariable().startsWith("_s");

		    String castToBoolean = "";
		    if (resultTypeBoolean) {
			castToBoolean = " == 1";
		    }
		    if (resultTypeDouble || resultTypeBoolean) {

			theTail.append(node.getResultsVariable()+" = "+InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(node.getToken())+castToBoolean+";\n");

		    } else {

			theTail.append(node.getResultsVariable()+" = "+node.getToken()+";\n");

		    }
		}
	    }
	}

	if (node.getChild() != null) 
	    generateRHSCode(node.getChild());
	if (node.getNext() != null) 
	    generateRHSCode(node.getNext());
    }

    private String generateExpression(Node node) {
	StringBuffer sb = new StringBuffer();
	node.setPlaceHolder(true);

	// node is the root node of an expression
	// if an operator:
	// left child
	// operator
	// right child
	if (Parser.isArithmeticOperator(node.getToken())) {
	    sb.append(generateExpression(node.getChild()));
	    sb.append(node.getToken());
	    sb.append(generateExpression(node.getChild().getNext()));
	    return sb.toString();
	    // if an array reference, generate rhs
	} else if (ArrayReference.isArrayReference(node.getToken())) {
	    
		sb.append(new ArrayReferenceNative(node.getToken(), currentGenerate).generateRHSImplementation());
	    
	    return sb.toString();
	} else if (node.getToken().startsWith("sdFunctions.LOOKUP")) {
	    sb.append("/* MJB Requires dealing with LOOKUP access!*/");
	    
	    sb.append("   ");
	    sb.append(generateLOOKUP(node));
//	    if (Translator.useNativeDataTypes) 
//		sb.append(new ArrayReferenceNative(node.getToken(), currentGenerate).generateRHSImplementation());
//	    else
//		sb.append(new ArrayReference(node.getToken()).generateRHSImplementation());
	    return sb.toString();
	    // else this should just be variable
	} else {
	    sb.append(node.getToken());
	}


	return sb.toString();
    }
    
    private String generateLOOKUP(Node node) {
	StringBuffer theTail = new StringBuffer();
	theTail.append(node.getToken()+"(");

	Node n = node.getChild();

	// 1st and second arguments need to be updated if they refer to arrays as subscript
	// definition is created as we go and may referenced them before defined

	String lhs = currentGenerate.getLhs();
	String valueArg;
	String nameArg;
	if (ArrayReference.isArrayReference(lhs)) {
	    
		valueArg = new ArrayReferenceNative(lhs, currentGenerate).generateRHSImplementation();
		nameArg = new ArrayReferenceNative(lhs, currentGenerate).generateRHSName();
	   

	} else {
	   
		valueArg = InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(lhs);
	   
	    nameArg = "\""+lhs+"\"";
	}


	// the first four arguments are placeholders and should not be processed
	// 1st and second arguments need to be updated if they refer to arrays as subscript
	// definition is created as we go and may referenced them before defined

	int numTransfer = 4;
	
	if (node.getToken().startsWith("sdFunctions.LOOKUP"))
	    numTransfer = 0;

	int cnt = 0;

	while(n != null) {
	    if (numTransfer == 4 || (numTransfer == 0 && cnt > 0))
		theTail.append(",");
	    cnt++;

	    String t = getNextDouble();
	    n.setResultsVariable(t);
	    theTail.append(generateExpression(n));
	    n = n.getNext();
	}


	theTail.append(")\n");

    
	
	return theTail.toString();
    }

    private String generateRHSName(String valueOfRef) {
	return valueOfRef.replace("valueOf(", "").replace(")", "");
    }

    private Node getCONDITION(Node node) {

	// skip the first four and return # 5
	return node.getChild().getNext().getNext().getNext().getNext();
    }

    private Node getTHEN(Node node) {
	// skip the first four and return # 6
	return node.getChild().getNext().getNext().getNext().getNext().getNext();

    }

    private Node getELSE(Node node) {
	// skip the first four and return # 7
	return node.getChild().getNext().getNext().getNext().getNext().getNext().getNext();

    }

    //	private List<Node> getIFTHENELSE(Node node) {
    //	    List<Node> al = new ArrayList<Node>();
    //	    
    //	    if (node == null)
    //		return al;
    //	    
    //	    if (node.getToken().startsWith("sdFunctions.IFTHENELSE")) {
    //		al.add(node);
    //	    }
    //	    
    //	    List<Node> more = getIFTHENELSE(node.getChild());
    //	    if (more.size() > 0)
    //		al.addAll(more);
    //	    
    //	    more = getIFTHENELSE(node.getNext());
    //	    if (more.size() > 0)
    //		al.addAll(more);
    //	    
    //	    return al;
    //	}

    private boolean isLeaf(Node node) {
	// this should never happen, I think
	if (node == null)
	    return true;

	if (node.getChild() == null)
	    return true;
	//	    else if (node.getChild().isDeleted())
	//		return true;
	//	    else
	return false;
    }

    private boolean isTerminal(Node node) {

	if (node == null)
	    return true;

	if (node.getToken().startsWith("sdFunctions.IFTHENELSE"))
	    return false;

	return isTerminal(node.getChild()) && isTerminal(node.getNext());
    }


    //	private void execute(BufferedWriter bw) {
    //		
    //		try {
    //			bw.append("public void execute(BufferedWriter bw) {\n\n");
    //			bw.append("    oneTime();\n");
    //			
    //			bw.append("    for (double time = getINITIALTIME(); time <= getFINALTIME(); time += getTIMESTEP()) {\n" +
    //				"         Data.setCurrentTime(time);\n" +
    //				"         currentTime = time;\n" +
    //				"         repeated(time, getTIMESTEP());\n" +
    //				"         reportTimeStep(bw, time);\n");
    //			
    //			bw.append("    }\n");
    //			
    //			bw.append("}\n\n");
    //			bw.flush();
    //			
    //		} catch (IOException e) {
    //			// TODO Auto-generated catch block
    //			e.printStackTrace();
    //		}
    //		
    //	}

    private void report(BufferedWriter bw) {
	try {

	    bw.append("public BufferedWriter report(String filename) {\n\n");
	    bw.append("    if (!trace.isTrace() || report == null)\n");
	    bw.append("       return null;\n");
	    bw.append("  BufferedWriter bw = null;\n");
	    bw.append("  try {\n");
	    bw.append("    bw = Utilities.openFileForWriting(filename);\n\n");

	    ArrayList<String> sortOrder = new ArrayList<String>(evaluationOrder);
	    Collections.sort(sortOrder);

	    // generate the column headers
	    bw.append("    bw.append(\"time\");");
	    for (String lhs : sortOrder) {
		if (!equations.get(lhs).getCleanEquation().contains("="))
		    continue;
		if (equations.get(lhs).isHasLHSArrayReference()) {

		} else {
		    if (equations.get(lhs).isRepeated()) {
			//					bw.append("    bw.append(\",\");\n");
			bw.append("    bw.append(\","+lhs+"\");\n");
		    } else {
			//					bw.append("    bw.append(\",\");\n");
			bw.append("    bw.append(\","+lhs+"\");\n");
		    }
		}
	    }
	    bw.append("    bw.append(\"\\n\");\n");

	    bw.append("    bw.flush();\n");


	    bw.append("  } catch (IOException e) {\n");
	    bw.append("    e.printStackTrace();\n");
	    bw.append("  }\n");
	    bw.append("    return bw;\n\n");
	    bw.append("  }\n");
	    // generate the data

	    bw.append("public void reportTimeStep(double time) {\n");
	    bw.append("    if (!trace.isTrace() || report == null)\n");
	    bw.append("       return;\n");
	    bw.append("    int i = (int) ((currentTime - getINITIALTIME())/getTIMESTEP());\n");

	    // this should be based on SAVPER

	    bw.append("BufferedWriter bw = report;\n");
	    bw.append("  try {\n");
	    bw.append("    bw.append(Double.toString(currentTime));\n");
	    for (String lhs : sortOrder) {

		if (!equations.get(lhs).getCleanEquation().contains("="))
		    continue; if (equations.get(lhs).isHasLHSArrayReference()) {

		    } else {
			bw.append("    bw.append(\",\"+Double.toString(valueOf(\""+lhs+"\", i)));\n");    // get "lhs"_history.get(i);
		    }

	    }
	    bw.append("    bw.append(\"\\n\");\n");
	    bw.append("    i++;\n");

	    //			bw.append("}\n\n");
	    bw.append("    bw.flush();\n");
	    //			bw.append("    bw.close();\n");
	    bw.append("  } catch (IOException e) {\n");
	    bw.append("    e.printStackTrace();\n");
	    bw.append("  }\n");
	    bw.append("}\n\n");
	    bw.flush();

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public void runner(String srcDir) {

	BufferedWriter bw = Utilities.openFileForWriting(srcDir+objectName+"_runner.java");

	try {
	   

		bw.append("package "+translator.getPackageName()+";\n\n");
	   

	    bw.append("public class "+objectName+"_runner  {\n");
	    bw.append("public "+objectName+"_runner(String name, String[] args) {}\n");
	    bw.append("public static void main(String[] args) {\n");
	    bw.append("new "+objectName+"(\""+objectName+"\", args).execute();\n");
	    bw.append("}\n");

	    bw.append("}\n");
	    bw.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void endObject(BufferedWriter bw) {
	try {
	    bw.append("}\n\n");
	    bw.flush();
	    //			bw.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
    
    private void endObjectC(BufferedWriter bw) {
	try {
	    
	    // don't think we need this.
//	    bw.append("}\n\n");
	    bw.flush();
	    //			bw.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    private String getNextBoolean() {
	String next = "_b"+ booleanUsed;
	if (booleanUsed > this.maxBoolean) {
	    maxBoolean = booleanUsed;
//	    try {
//		if (Translator.target.equals(ReaderConstants.C))
//		    sourceCode.append("bool "+next+" = false;\n");
//		else
//		    sourceCode.append("boolean "+next+" = false;\n");
//	    } catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    }
	}
	booleanUsed++;
	return  next;
    }

    // write temps later not npw.
    // traverse tree and if not placeholder, generate the code for results variable.
    private String getNextDouble() {
	String next = "_t"+ doubleUsed;
	if (doubleUsed > this.maxTemp) {
	    maxTemp = doubleUsed;
//	    try {
//		sourceCode.append("double "+next+" = 0.0;\n");
//	    } catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    }
	}
	doubleUsed++;
	return next;
    }
    
    private String getNextInt() {
	
	String next = /* "_i"+ */ Integer.toString(intUsed);
	if (intUsed > this.maxTempInt) {
	    maxTempInt = intUsed;
//	    try {
//		sourceCode.append("int "+next+" = 0;\n");
//	    } catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    }
	}
	intUsed++;
	return next;
    }

    private String getNextString() {
	String next = "_s"+ stringUsed;
	if (stringUsed > this.maxString) {
	    maxString = stringUsed;
//	    try {
//		sourceCode.append("String "+next+" = \"\";\n");
//	    } catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	    }
	}
	stringUsed++;
	return next;
    }

    private void resetCounters() {
	resetLimits();
	booleanUsed = 0;
	doubleUsed = 0;
	stringUsed = 0;
	intUsed = 0;


    }

    private void resetLimits() {
	maxTemp = -1;
	maxTempInt = -1;
	maxBoolean = -1;
	maxString = -1;
	indexArraysPacked.clear();
    }

    public static boolean indexDefined(String pack) {
	return indexArraysPacked.contains(pack);
    }

    public static void defineIndex(String pack) {
	indexArraysPacked.add(pack);
    }


    private Map<Node, String> genRHSStatements(Node node) {
	List<Node> arrayReferenceNodes = getArrayReferences(node);

	Map<Node, String> rhsStatements = new HashMap<Node, String>();

	Node lhsNode = null;;
	lhsNode = getLHS(node);

	for (Node n : arrayReferenceNodes) {
	    if (!n.equals(lhsNode)) {
	
		    rhsStatements.put(n, new ArrayReferenceNative(n.getToken(), currentGenerate).generateRHSImplementation());
		
	    }
	}

	return rhsStatements;
    }

    public BufferedWriter getSourceCode() {
	return sourceCode;
    }

    public void setSourceCode(BufferedWriter sourceCode) {
	this.sourceCode = sourceCode;
    }
    
    public static String scrub(String lineOfCode) {
	if (Translator.target.equals(ReaderConstants.JAVA))
	    return lineOfCode;
	else if (Translator.target.equals(ReaderConstants.JAVASCRIPT))
	    return lineOfCode;
	else if (Translator.target.equals(ReaderConstants.C))
	    return lineOfCode.replace("memory.", "").
	    	replace("sdFunctions.", "").
	    	replace("Double.MAX_VALUE", "DBL_MAX").
		replace("Double.MIN_VALUE", "DBL_MIN").
		replace("/* holder1 */", "1").
		replace("/* holder2 */", "1").
		replace("schedule.", "").
		replace("Math.pow", "pow");
	else
	    return lineOfCode;
	
    }
    
    public void performUnitsConsistencyCheck() {
//	UnitsManager.performUnitsConsistencyCheck(evaluationOrder, equations, "./ConsistencyResults.xml");
    }


}
