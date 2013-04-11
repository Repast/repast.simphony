package repast.simphony.systemdynamics.engine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.translator.InformationManagers;
import repast.simphony.systemdynamics.translator.SystemDynamicsObjectManager;
import repast.simphony.systemdynamics.translator.TranslatorRepastSimphony;

public class Engine {
	
	private List<String> messages = new ArrayList<String>();

	private String currentModel;
//	private SystemDynamicsObjectManager sdObjectManager;
	private TranslatorRepastSimphony translator;
	private IProject project;
	private IProgressMonitor progressMonitor;


	public TranslatorRepastSimphony getTranslator() {
		return translator;
	}

	/**
	 * This class provides the various methods to allow communication between the
	 * RSSD GUI and the RSSD Translator
	 */

	public Engine() {
	  // initializes the InformationManagers
		InformationManagers.getInstance();
		translator = new TranslatorRepastSimphony();
		System.out.println("Engine created");
	}
	
	public Engine(SystemModel systemModel, IProject project, IProgressMonitor progressMonitor ) {
		this();
		this.project = project;
		this.progressMonitor = progressMonitor;
		translator = new TranslatorRepastSimphony(project, progressMonitor);
		
	}
	
	public void generateCodeForRSD(SystemModel systemModel) {
		getTranslator().generateCodeForRSD(systemModel);
	}
	
	public boolean validateGenerateRSD(SystemModel systemModel, boolean generateCode) {
		boolean result = getTranslator().validateGenerateRSD(systemModel, generateCode, messages);
		if (result) {
			messages.add("Model syntax OK\n");
			messages.add("Model units consistent\n");
			if (generateCode)
				messages.add("Java source code generated");
			
		}
		InformationManagers.getInstance().getMessageManager().addToMessages(messages);
		return result;
	}
	
	public boolean validateGenerateMDL(String mdlFile, boolean generateCode) {
		System.out.println("vg MDL: "+mdlFile);
		boolean result = getTranslator().validateGenerateMDL(mdlFile, generateCode, messages);
		if (result) {
			messages.add("Model syntax OK\n");
			messages.add("Model units consistent\n");
			if (generateCode)
				messages.add("Java source code generated");
		}
		InformationManagers.getInstance().getMessageManager().addToMessages(messages);
		return result;
	}
	public SystemModel loadMDL(String filename) {
		return null;
	}

	public static void main(String[] args) {

//		Engine engine = new Engine();
//		SystemModel systemModel = engine.loadRSModel(args[0]);
//		engine.getSdObjectManager().ingest(systemModel);
//		engine.getSdObjectManager().processRSD();

	}

	/**
	 * Establish the model name. This name will be used in class names for the generated
	 * code. It will be validated for use in this context.
	 * 
	 * @param - modelName
	 * @return - Success or Failure with corresponding message
	 */
	public Response modelName(String modelName) {
		return new Response();
	}

	/**
	 * Used to create/update a variable within the System Dynamics model. The variable name
	 * is the complete Left Hand Side (LHS) of the equation (i.e. with subscript if a subscripted
	 * variable. All other arguments support null as a value (although this limits what processing
	 * can be done with the variable). Only comment is optional. All others must be specified before
	 * code generation or units consistency checks can occur. 
	 * 
	 * @param variableName - name of the variable. Internal and external forms of the name are maintained.
	 * 		The internal name is a valid variable name for use in generated Java code. This allows
	 * 		for blanks and other illegal characters to be used in the External name -- the name
	 * 		used by the GUI.
	 * @param type - supported Types are "double" and "String"
	 * @param equationRHS - the equation for computing the LHS value
	 * @param units - units for the LHS. Units can be anything. They are processed symbolically.
	 * @param comment - An optional comment for descriptive information of any kind.
	 * @return - Success or Failure with corresponding message
	 */
	public Response variable(String variableName, String type, String equationRHS, String units, String comment) {
		return new Response();
	}
	/**
	 * Used to create/update and equation for a specific variable in the System Dynamics model
	 * 
	 * @param variableName - equation LHS
	 * @param equation - equation RHS
	 * @return - Success or Failure with corresponding message
	 */
	public Response equation(String variableName, String equation)  {
		return new Response();
	}
	/**
	 * Used to create/update the type for a specific variable in the System Dynamics model
	 * 
	 * @param variableName - equation LHS
	 * @param type - double or String (subscripted variables are automatically processed).
	 * @return  - Success or Failure with corresponding message
	 */
	public Response type(String variableName, String type)  {
		return new Response();
	}

	/**
	 * Used to create/update the units for a specific variable in the System Dynamics model
	 * @param variableName
	 * @param units
	 * @return - Success or Failure with corresponding message
	 */
	public Response units(String variableName, String units)  {
		return new Response();
	}

	/**
	 * Used to create/update the comment for a specific variable in the System Dynamics model
	 * @param variableName
	 * @param comment
	 * @return - Success or Failure with corresponding message
	 */
	public Response comment(String variableName, String comment)  {
		return new Response();
	}

	/**
	 * Checks the syntax of the specified equation. This will be the complete equation i.e.
	 * LHS = RHS.
	 * @param equation
	 * @return - Success or Failure with corresponding message
	 */
	public Response checkSyntax(String equation) {
		return new Response();
	}

	/**
	 * Checks the syntax of the equation associated with the variable (LHS);
	 * @param variableName
	 * @return - Success or Failure with corresponding message
	 */
	public Response checkSyntaxForVariable(String variableName) {
		return new Response();
	}

	/**
	 * Checks the unit consistency of the entire System Dynamics model
	 * @return - Success or Failure with corresponding message
	 */
	public Response validateUnits() {
		return new Response();
	}

	/**
	 * Used to specify the four model run time parameters
	 * 
	 * @param startTime
	 * @param endTime
	 * @param timeStep
	 * @param savper
	 * @return - Success or Failure with corresponding message
	 */
	public Response setModelParameters(String startTime, String endTime, String timeStep, String savper) {
		return new Response();
	}

	/**
	 * Used to define named subscripts and mappings
	 * 
	 * @param subrDefinition
	 * @return - Success or Failure with corresponding message
	 */
	public Response defineSubscript(String subrDefinition) {
		return new Response();
	}

	/**
	 * Used to define units equivalents (e.g. People = person = persons)
	 * @param unitsEquivalence
	 * @return - Success or Failure with corresponding message
	 */
	public Response defineUnitsEquivalance(String unitsEquivalence) {
		return new Response();
	}

	/**
	 * Loads a Vensim .mdl file from disk and returns contents to the GUI.
	 * Note that the information will also be loaded into the Engine.
	 * @param filename
	 * @return - Success or Failure with corresponding message
	 */
	public Response loadVensimModel(String filename) {
		return new Response();
	}

	/**
	 * Loads a Repast Simphony .rsd file from disk and returns contents to the GUI.
	 * Note that the information will also be loaded into the Engine.
	 * @param filename
	 * @return - Success or Failure with corresponding message
	 */
	public SystemModel loadRSModel(String filename) {
		SystemModel systemModel = null;
		try {
			XMIResourceImpl resource = new XMIResourceImpl();
			resource.load(new FileInputStream(filename), new HashMap<Object, Object>());


			for (EObject obj : resource.getContents()) {
				if (obj.eClass().equals(SDModelPackage.Literals.SYSTEM_MODEL)) {
					systemModel = (SystemModel)obj;
					return systemModel;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		return null;
	}

	/**
	 * Creates the set of classes that implements the System Dynamics model.
	 * @param directory
	 * @return - Success or Failure with corresponding message
	 */
	public Response generateCode(String directory) {
		return new Response();
	}

	/**
	 * Returns a list of all the function categories supported by RS.
	 * @return - Success or Failure with corresponding message
	 */
	public Response getFunctionCategories() {
		return new Response();
	}

	/**
	 * Returns the set of functions and their interfaces that comprise the category
	 * @param category
	 * @return - Success or Failure with corresponding message
	 */
	public Response getFunctions(String category) {
		return new Response();
	}

	/**
	 * Used to define a user function that can be called from the generate code.
	 * @param object - The class name
	 * @param name - The method name
	 * @param args - descriptive single word to identify argument
	 * @param argTypes - double or String
	 * @param argUnits - the units
	 * @param returnType - double or String
	 * @param returnUnits - units returned
	 * @return - Success or Failure with corresponding message
	 */
	public Response registerUserFunction(String object, String name, String[] args, String[] argTypes, String[] argUnits, String returnType, String returnUnits) {
		return new Response();
	}

	public Response getLookupTableDefinitionSyntax() {
		return new Response();
	}

	public Response getSubscriptDefinitionSyntax() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("Subscript Definition Syntax\n");
		sb.append("NamedSubscript : sub1, sub2, ..., subN\n");
		sb.append("where subn is another named subscript or a terminal value\n\n");
		sb.append("NamedSubscript : (sub1-sub4)\n");
		sb.append("is equivalent to:\n");
		sb.append("NamedSubscript : sub1, sun2, sub3, sub4\n");
		Response response = new Response();
		response.setMessage(sb.toString());
		return response;
	}

	public Response getEquivalenceDefinitionSyntax() {
		return new Response();
	}

	public Response getReservedWords() {
		return new Response();
	}

	public Response getOperators() {
		return new Response();
	}

	public SystemDynamicsObjectManager getSdObjectManager() {
		return translator.getSdObjectManager();
	}

	public String getMessages() {
		
		StringBuffer sb = new StringBuffer();
		
		for (String s : messages) {
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
	}


}
