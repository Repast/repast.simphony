package repast.simphony.agents.designer.ui.editors;

import java.util.ArrayList;

import repast.simphony.agents.designer.ui.wizards.NewCodeWizardEntry;
import repast.simphony.agents.designer.ui.wizards.NewCodeWizardFormEntry;
import repast.simphony.agents.designer.ui.wizards.NewCodeWizardRadioButtonEntry;

/**
 * Generate the contents of the Loop block wizard for the agent editor.
 * 
 * @author Michael J. North
 * @author Eric Tatara
 *
 */
public class TaskWizardGenerator {

	public static ArrayList<NewCodeWizardEntry> createTaskWizard(){
		ArrayList<NewCodeWizardEntry> exampleListTaskWizard = 
			new ArrayList<NewCodeWizardEntry>();

		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryMain = 
			new NewCodeWizardRadioButtonEntry("Task Step", 
					"Please Select the Kind of Code to Create",
				new String[] { "Variable Assignment", 
						           "Contexts and Agents",
						           "Move an Agent to a New Location", 
						           "Move an Agent by an Offset", 
						           "Move an Agent with a Vector", 
						           "Statistics and Random Numbers",
						           "Mathematics",
						           "Queries",
						           "Networks",
						           "Schedule",
						           "Projections",
						           "Advanced Adaptation", 
						           "Miscellaneous",
						           "Model Initializer"}, 
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryMain);

		/* ---- BEGIN Variable Assignment ---- */
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryVariableAssignment = 
			new NewCodeWizardRadioButtonEntry("Variable Assignment",
				"Please Select the Kind of Variable Assignment to Perform",
				new String[] { "Assign a Variable",
								 "Assign a Variable from a Watched Agent", 
						         "Assign a Variable to a Watched Agent", 
					             "Assign a Behavior Step Return Value from a Calculation",
					             "Assign a Behavior Step Return Value from a Watched Agent"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryVariableAssignment);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryAssignAVariable = 
			new NewCodeWizardFormEntry("Assign a Variable",
				"Please Select the Variable Assignment Options",
				new String[] {"Variable Name", "Variable Value"},
				new String[] {"$1", "$2"}, 
				new String[] {"age", "24"}, 
				"$1 = $2");
		exampleListTaskWizard.add(newCodeWizardFormEntryAssignAVariable);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryAssignAVariableFromWatched = 
			new NewCodeWizardFormEntry("Assign a Variable from a Watched Agent",
				"Please Select the Variable Assignment Options",
				new String[] {"Variable Name", "Watched Agent Name", "Watched Agent Property"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"age", "watchedAgent", "ageProperty"}, 
				"$1 = $2.$3");
		exampleListTaskWizard.add(newCodeWizardFormEntryAssignAVariableFromWatched);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryAssignAVariableToWatched = 
			new NewCodeWizardFormEntry("Assign a Variable to a Watched Agent",
				"Please Select the Variable Assignment Options",
				new String[] {"Variable Name", "Watched Agent Name", "Watched Agent Property"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"age", "watchedAgent", "ageProperty"}, 
				"$2.$3 = $1");
		exampleListTaskWizard.add(newCodeWizardFormEntryAssignAVariableToWatched);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryAssignAVariableFromBehvaiorCalc = 
			new NewCodeWizardFormEntry("Assign a Behavior Step Return Value from a Calculation",
				"Please Select the Variable Assignment Options",
				new String[] {"Value to Return"},
				new String[] {"$1"}, 
				new String[] {"value"}, 
				"returnValue = $1");
		exampleListTaskWizard.add(newCodeWizardFormEntryAssignAVariableFromBehvaiorCalc);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryAssignAVariableFromReturnWatched = 
			new NewCodeWizardFormEntry("Assign a Behavior Step Return Value from a Watched Agent",
				"Please Select the Variable Assignment Options",
				new String[] {"Variable to Return", "Watched Agent Name"},
				new String[] {"$1", "$2"}, 
				new String[] {"age", "watchedAgent"}, 
				"returnValue = $2.$1");
		exampleListTaskWizard.add(newCodeWizardFormEntryAssignAVariableFromReturnWatched);
		/* ---- END Variable Assignment ---- */
		
		/* ---- BEGIN Contexts and Agents ---- */
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryCreateRemoveAdd = 
			new NewCodeWizardRadioButtonEntry("Contexts and Agents",
				"Please Select the Type of Activity to Perform",
				new String[] { "Create One or More Agents",
					             "Add an Agent to a Context", 
					             "Remove an Agent from a Context",
					             "Remove an Agent from the Entire Model",
					             "Get All Agents of a type from a Context",
					             "Get Random Agents of a type from a Context",
					             "Create a Context",
					             "Find a Context",
					             "Find a Parent Context",
					             "Remove a Context"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryCreateRemoveAdd);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryCreateRemoveAddCreate = 
			new NewCodeWizardFormEntry("Create One or More Agents",
				"Please Select the Agent Creation Options",
				new String[] {"Agent Type to Create", "Context in which to Place Agent", "Number of Agents to Create"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"anl.example.MyAgent", "root", "1"}, 
				"Object agent = CreateAgents(\"$2\", \"$1\", $3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryCreateRemoveAddCreate);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryCreateRemoveAddAdd = 
			new NewCodeWizardFormEntry("Add an Agent to a Context",
				"Please Select the Agent Add Options",
				new String[] {"Agent to Add", "Context in which to Place Agent"},
				new String[] {"$1", "$2"}, 
				new String[] {"myAgent", "root"}, 
				"Context context = AddAgentToContext(\"$2\", $1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryCreateRemoveAddAdd);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryCreateRemoveAddRemove = 
			new NewCodeWizardFormEntry("Remove an Agent from a Context",
				"Please Select the Agent Remove Options",
				new String[] {"Agent to Remove", "Context from which to Remove Agent"},
				new String[] {"$1", "$2"}, 
				new String[] {"myAgent", "root"}, 
				"Context context = RemoveAgentFromContext(\"$2\", $1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryCreateRemoveAddRemove);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryCreateRemoveAddRemoveModel = 
			new NewCodeWizardFormEntry("Remove an Agent from the Entire Model",
				"Please Select the Agent Remove Options",
				new String[] {"Agent to Remove"},
				new String[] {"$1"}, 
				new String[] {"myAgent"}, 
				"Context context = RemoveAgentFromModel($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryCreateRemoveAddRemoveModel);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryCreateRemoveAddGetAll = 
			new NewCodeWizardFormEntry("Get All Agents of a type from a Context",
				"Please Select the Agent Get Options",
				new String[] {"Agent Type to Get", "Context from which to Get Agents"},
				new String[] {"$1", "$2"}, 
				new String[] {"anl.example.MyAgent", "root"}, 
				"IndexedIterable iter = GetObjects(\"$2\", \"$1\")");
		exampleListTaskWizard.add(newCodeWizardFormEntryCreateRemoveAddGetAll);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryCreateRemoveAddGetRand = 
			new NewCodeWizardFormEntry("Get Random Agents of a type from a Context",
				"Please Select the Agent Get Options",
				new String[] {"Agent Type to Get", "Context from which to Get Agents", 
					"Number of Agents to Get"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"anl.example.MyAgent", "root", "10"}, 
				"Iterable iter = GetRandomObjects(\"$2\", \"$1\", $3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryCreateRemoveAddGetRand);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryCreateRemoveAddCreateContext = 
			new NewCodeWizardFormEntry("Create a Context",
				"Please Select the Create a Context Options",
				new String[] {"Parent Context Path", "New Context Name"},
				new String[] {"$1", "$2"}, 
				new String[] {"\"root\"", "NewContext"}, 
				"Context context = CreateContext($1,$2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryCreateRemoveAddCreateContext);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryCreateRemoveAddFindContext = 
			new NewCodeWizardFormEntry("Find a Context",
				"Please Select the Find a Context Options",
				new String[] {"Context Path"},
				new String[] {"$1"}, 
				new String[] {"\"root/subContext\""}, 
				"Context context = FindContext($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryCreateRemoveAddFindContext);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryCreateRemoveAddFindContextParent = 
			new NewCodeWizardFormEntry("Find a Parent Context",
				"Please Select the Find a Parent Context Options",
				new String[] {"Context Path"},
				new String[] {"$1"}, 
				new String[] {"\"root/subContext\""}, 
				"Context context = FindParentContext($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryCreateRemoveAddFindContextParent);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryCreateRemoveAddRemoveContext = 
			new NewCodeWizardFormEntry("Remove a Context",
				"Please Select the Remove a Context Options",
				new String[] {"Context Path"},
				new String[] {"$1"}, 
				new String[] {"\"root/subContext\""}, 
				"Context context = RemoveContext($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryCreateRemoveAddRemoveContext);
		/* ---- END Contexts and Agents ---- */
	 	
		/* ---- BEGIN Move an Agent to a New Location ---- */
		NewCodeWizardFormEntry newCodeWizardFormEntryMoveAgent = 
			new NewCodeWizardFormEntry("Move an Agent to a New Location",
				"Please Select the Agent Move Options",
				new String[] {"Agent to Move", "Space or Grid", "New Location"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myAgent", "root/aGrid", "10, 20"}, 
				"MoveAgent(\"$2\", $1, $3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMoveAgent);
		/* ---- END Move an Agent to a New Location ---- */

		/* ---- BEGIN Move an Agent by an Offset ---- */
		NewCodeWizardFormEntry newCodeWizardFormEntryMoveAgentByDisplacement = 
			new NewCodeWizardFormEntry("Move an Agent by an Offset",
				"Please Select the Agent Move Options",
				new String[] {"Agent to Move", "Space or Grid", "Offset List"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myAgent", "root/aGrid", "10, 20"}, 
				"MoveAgent(\"$2\", $1, $3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMoveAgentByDisplacement);
		/* ---- END Move an Agent by an Offset ---- */

		/* ---- BEGIN Move an Agent with a Vector ---- */
		NewCodeWizardFormEntry newCodeWizardFormEntryMoveAgentByVector = 
			new NewCodeWizardFormEntry("Move an Agent with a Vector",
				"Please Select the Agent Move Options",
				new String[] {"Agent to Move", "Space or Grid", "Distance to Move", "Angles to Move Along in Radians"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myAgent", "root/aGrid", "10, 20"}, 
				"MoveAgent(\"$2\", $1, $3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMoveAgentByVector);
		/* ---- END Move an Agent with a Vector ---- */

				/* ---- BEGIN Statistics and Random Numbers ---- */
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryStats = 
			new NewCodeWizardRadioButtonEntry("Statistics and Random Numbers",
				"Please Select the Type of Statistics Operation to Perform",
				new String[] { "Assign a Uniform Random Number",
					             "Initialize A Specific Distribution for Use", 
					             "Assign a Random Draw from a Specific Distribution"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryStats);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsUniform = 
			new NewCodeWizardFormEntry("Assign a Uniform Random Number",
				"Please Specify the Lower and Upper Bound Options",
				new String[] {"Variable Name", "Lower Bound", "Upper Bound"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"age", "0", "1"}, 
				"$1 = RandomDraw($2, $3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsUniform);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryStatsInit = 
			new NewCodeWizardRadioButtonEntry("Initialize A Specific Distribution for Use",
				"Please Select the Type of Distribution to Initialize",
				new String[] { "Beta",
					             "Binomial", 
					             "Chi Square",
					             "Exponential",
					             "Gamma",
					             "Logarithmic",
					             "Normal",
					             "Poisson",
					             "Student T",
					             "Von Mises",
					             "Zeta"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryStatsInit);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryStatsAssign = 
			new NewCodeWizardRadioButtonEntry("Assign a Random Draw from a Specific Distribution",
				"Please Select the Type of Distribution From Which to Sample",
				new String[] { "Beta ",
					             "Binomial ", 
					             "Chi Square ",
					             "Exponential ",
					             "Gamma ",
					             "Logarithmic ",
					             "Normal ",
					             "Poisson ",
					             "Student T ",
					             "Von Mises ",
					             "Zeta "},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryStatsAssign);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsInitBeta = 
			new NewCodeWizardFormEntry("Beta",
				"Please Specify the Beta Distribution Options",
				new String[] {"alpha", "beta"},
				new String[] {"$1", "$2"}, 
				new String[] {"2", "5"}, 
				"RandomHelper.createBeta($1, $2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsInitBeta);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsInitBinomial = 
			new NewCodeWizardFormEntry("Binomial",
				"Please Specify the Binomial Distribution Options",
				new String[] {"n", "p"},
				new String[] {"$1", "$2"}, 
				new String[] {"20", "0.5"}, 
				"RandomHelper.createBinomial($1, $2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsInitBinomial);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsInitChi = 
			new NewCodeWizardFormEntry("Chi Square",
				"Please Specify the Chi Square Distribution Options",
				new String[] {"degrees of freedom"},
				new String[] {"$1"}, 
				new String[] {"3"}, 
				"RandomHelper.createChiSquare($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsInitChi);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsInitExponential = 
			new NewCodeWizardFormEntry("Exponential",
				"Please Specify the Exponential Distribution Options",
				new String[] {"lambda"},
				new String[] {"$1"}, 
				new String[] {"1.0"}, 
				"RandomHelper.createExponential($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsInitExponential);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsInitGamma = 
			new NewCodeWizardFormEntry("Gamma",
				"Please Specify the Gamma Distribution Options",
				new String[] {"alpha", "lambda"},
				new String[] {"$1", "$2"}, 
				new String[] {"2", "2"}, 
				"RandomHelper.createGamma($1, $2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsInitGamma);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsInitLog = 
			new NewCodeWizardFormEntry("Logarithmic",
				"Please Specify the Logarithmic Distribution Options",
				new String[] {"p"},
				new String[] {"$1"}, 
				new String[] {"0.5"}, 
				"RandomHelper.createLogarithmic($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsInitLog);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsInitNormal = 
			new NewCodeWizardFormEntry("Normal",
				"Please Specify the Normal Distribution Options",
				new String[] {"mean", "standard deviation"},
				new String[] {"$1", "$2"}, 
				new String[] {"0", "1"}, 
				"RandomHelper.createNormal($1, $2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsInitNormal);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsInitPoisson = 
			new NewCodeWizardFormEntry("Poisson",
				"Please Specify the Poisson Distribution Options",
				new String[] {"mean"},
				new String[] {"$1"}, 
				new String[] {"10"}, 
				"RandomHelper.createPoisson($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsInitPoisson);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsInitStudentT = 
			new NewCodeWizardFormEntry("Student T",
				"Please Specify the Student T Distribution Options",
				new String[] {"degrees of freedom"},
				new String[] {"$1"}, 
				new String[] {"5"}, 
				"RandomHelper.createStudentT($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsInitStudentT);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsInitVonMises = 
			new NewCodeWizardFormEntry("Von Mises",
				"Please Specify the Von Mises Distribution Options",
				new String[] {"degrees of freedom"},
				new String[] {"$1"}, 
				new String[] {"4"}, 
				"RandomHelper.createVonMises($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsInitVonMises);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsInitZeta = 
			new NewCodeWizardFormEntry("Zeta",
				"Please Specify the Zeta Distribution Options",
				new String[] {"ro", "pk"},
				new String[] {"$1", "$2"}, 
				new String[] {"5", "10"}, 
				"RandomHelper.createZeta($1, $2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsInitZeta);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsAssignBeta = 
			new NewCodeWizardFormEntry("Beta ",
				"Please Specify the Beta Distribution Assignment Options",
				new String[] {"Variable"},
				new String[] {"$1"}, 
				new String[] {"myVar"}, 
				"$1 = RandomHelper.getBeta().nextDouble()");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsAssignBeta);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsAssignBinomial = 
			new NewCodeWizardFormEntry("Binomial ",
				"Please Specify the Binomial Distribution Assignment Options",
				new String[] {"Variable"},
				new String[] {"$1"}, 
				new String[] {"myVar"}, 
				"$1 = RandomHelper.getBinomial().nextInt()");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsAssignBinomial);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsAssignChiSquare = 
			new NewCodeWizardFormEntry("Chi Square ",
				"Please Specify the Chi Square Distribution Assignment Options",
				new String[] {"Variable"},
				new String[] {"$1"}, 
				new String[] {"myVar"}, 
				"$1 = RandomHelper.getChiSquare().nextDouble()");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsAssignChiSquare);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsAssignExponential = 
			new NewCodeWizardFormEntry("Exponential ",
				"Please Specify the Exponential Distribution Assignment Options",
				new String[] {"Variable"},
				new String[] {"$1"}, 
				new String[] {"myVar"}, 
				"$1 = RandomHelper.getExponential().nextDouble()");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsAssignExponential);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsAssignGamma = 
			new NewCodeWizardFormEntry("Gamma ",
				"Please Specify the Gamma Distribution Assignment Options",
				new String[] {"Variable"},
				new String[] {"$1"}, 
				new String[] {"myVar"}, 
				"$1 = RandomHelper.getGamma().nextDouble()");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsAssignGamma);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsAssignLogarithmic = 
			new NewCodeWizardFormEntry("Logarithmic ",
				"Please Specify the Logarithmic Distribution Assignment Options",
				new String[] {"Variable"},
				new String[] {"$1"}, 
				new String[] {"myVar"}, 
				"$1 = RandomHelper.getLogarithmic().nextDouble()");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsAssignLogarithmic);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsAssignNormal = 
			new NewCodeWizardFormEntry("Normal ",
				"Please Specify the Normal Distribution Assignment Options",
				new String[] {"Variable"},
				new String[] {"$1"}, 
				new String[] {"myVar"}, 
				"$1 = RandomHelper.getNormal().nextDouble()");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsAssignNormal);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsAssignPoisson = 
			new NewCodeWizardFormEntry("Poisson ",
				"Please Specify the Poisson Distribution Assignment Options",
				new String[] {"Variable"},
				new String[] {"$1"}, 
				new String[] {"myVar"}, 
				"$1 = RandomHelper.getPoisson().nextInt()");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsAssignPoisson);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsAssignStudentT = 
			new NewCodeWizardFormEntry("Student T ",
				"Please Specify the Student T Distribution Assignment Options",
				new String[] {"Variable"},
				new String[] {"$1"}, 
				new String[] {"myVar"}, 
				"$1 = RandomHelper.getStudentT().nextDouble()");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsAssignStudentT);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsAssignVonMises = 
			new NewCodeWizardFormEntry("Von Mises ",
				"Please Specify the Von Mises Distribution Assignment Options",
				new String[] {"Variable"},
				new String[] {"$1"}, 
				new String[] {"myVar"}, 
				"$1 = RandomHelper.getVonMises().nextDouble()");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsAssignVonMises);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryStatsAssignZeta = 
			new NewCodeWizardFormEntry("Zeta ",
				"Please Specify the Zeta Distribution Assignment Options",
				new String[] {"Variable"},
				new String[] {"$1"}, 
				new String[] {"myVar"}, 
				"$1 = RandomHelper.getZeta().nextInt()");
		exampleListTaskWizard.add(newCodeWizardFormEntryStatsAssignZeta);
		/* ---- END Statistics and Random Numbers ---- */
		
		/* ---- BEGIN Mathematics ---- */
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryMath = 
			new NewCodeWizardRadioButtonEntry("Mathematics",
				"Please Select the Type of Mathematics",
				new String[] {"Matrix Operations",
					            "Vector Operations",
					            "Calculus"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryMath);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryMathMatrix = 
			new NewCodeWizardRadioButtonEntry("Matrix Operations",
				"Please Select the Type of Matrix Operation",
				new String[] { "Create a Matrix",
					             "Create a Matrix from a 2D Array",
					             "Get a Row Vector from a Matrix",
					             "Get a Column Vector from a Matrix",
					             "Get a Single Element from a Matrix",
					             "Matrix Addition",
					             "Matrix Subtraction",
					             "Matrix-Matrix Multiplication",
					             "Matrix-Vector Multiplication",
					             "Matrix Inverse",
					             "Matrix Determinant",
					             "Matrix Transpose",
					             "Matrix Vectorization",
					             "Matrix Copy",
					             "Linear Solve Ax=y"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryMathMatrix);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryMathVector = 
			new NewCodeWizardRadioButtonEntry("Vector Operations",
				"Please Select the Type of Vector Operation",
				new String[] { "Create a Vector from Values",
					             "Create a Vector from a List",
					             "Get a Single Element from a Vector",
					             "Vector Addition",
					             "Vector Subtraction",
					             "Vector Cross Product",
					             "Vector Dot Product",
					             "Vector Scalar Multiplication",
					             "Vector Copy"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryMathVector);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryMathCalculus = 
			new NewCodeWizardRadioButtonEntry("Calculus",
				"Please Select the Type of Calculus",
				new String[] { "Compute a Derivative",
					             "Compute an Integral"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryMathCalculus);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixCreate = 
			new NewCodeWizardFormEntry("Create a Matrix",
				"Please Specify the Matrix Creation Options",
				new String[] {"Matrix name"},
				new String[] {"$1"}, 
				new String[] {"myMatrix"}, 
				"DenseMatrix $1 = new DenseMatrix()");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixCreate);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixCreateArray = 
			new NewCodeWizardFormEntry("Create a Matrix from a 2D Array",
				"Please Specify the Matrix Creation Options",
				new String[] {"Matrix name", "Array Variable"},
				new String[] {"$1", "$2"}, 
				new String[] {"myMatrix", "myArray2D"}, 
				"DenseMatrix $1 = DenseMatrix.valueOf($2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixCreateArray);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixGetRow = 
			new NewCodeWizardFormEntry("Get a Row Vector from a Matrix",
				"Please Specify the Matrix Get Row Options",
				new String[] {"Vector Variable", "Matrix Variable", "Row number"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myVector", "myMatrix", "2"}, 
				"DenseVector $1 = $2.getRow($3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixGetRow);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixGetCol = 
			new NewCodeWizardFormEntry("Get a Column Vector from a Matrix",
				"Please Specify the Matrix Get Column Options",
				new String[] {"Vector Variable", "Matrix Variable", "Column number"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myVector", "myMatrix", "2"}, 
				"DenseVector $1 = $2.getColumn($3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixGetCol);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixGetElement = 
			new NewCodeWizardFormEntry("Get a Single Element from a Matrix",
				"Please Specify the Matrix Get Element Options",
				new String[] {"Element Variable", "Matrix Variable", "Row Index", "Column Index"},
				new String[] {"$1", "$2", "$3", "$4"}, 
				new String[] {"myElement", "myMatrix", "2", "3"}, 
				"def $1 = $2.get($3,$4)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixGetElement);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixAdd = 
			new NewCodeWizardFormEntry("Matrix Addition",
				"Please Specify the Matrix Addition Options",
				new String[] {"Matrix 1", "Matrix 2", "Result Matrix"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myMatrix1", "myMatrix2", "resultMatrix"}, 
				"DenseMatrix $3 = $1.plus($2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixAdd);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixSub = 
			new NewCodeWizardFormEntry("Matrix Subtraction",
				"Please Specify the Matrix Subtraction Options",
				new String[] {"Matrix 1", "Matrix 2", "Result Matrix"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myMatrix1", "myMatrix2", "resultMatrix"}, 
				"DenseMatrix $3 = $1.minus($2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixSub);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixMultMatrix = 
			new NewCodeWizardFormEntry("Matrix-Matrix Multiplication",
				"Please Specify the Matrix-Matrix Multiplication Options",
				new String[] {"Matrix 1", "Matrix 2", "Result Matrix"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myMatrix1", "myMatrix2", "resultMatrix"}, 
				"DenseMatrix $3 = $1.times($2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixMultMatrix);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixMultVector = 
			new NewCodeWizardFormEntry("Matrix-Vector Multiplication",
				"Please Specify the Matrix-Vector Multiplication Options",
				new String[] {"Matrix", "Vector", "Result Vector"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myMatrix", "myVector", "resultVector"}, 
				"DenseVector $3 = $1.times($2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixMultVector);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixInverse = 
			new NewCodeWizardFormEntry("Matrix Inverse",
				"Please Specify the Matrix Inverse Options",
				new String[] {"Matrix", "Inverse Matrix"},
				new String[] {"$1", "$2"}, 
				new String[] {"myMatrix", "myInvMatrix"}, 
				"DenseMatrix $2 = $1.inverse()");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixInverse);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixDeterminant = 
			new NewCodeWizardFormEntry("Matrix Determinant",
				"Please Specify the Matrix Determinant Options",
				new String[] {"Matrix", "Determinant"},
				new String[] {"$1", "$2"}, 
				new String[] {"myMatrix", "myDeterminant"}, 
				"def $2 = $1.determinant()");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixDeterminant);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixTranspose = 
			new NewCodeWizardFormEntry("Matrix Transpose",
				"Please Specify the Matrix Transpose Options",
				new String[] {"Matrix", "Matrix Transpose"},
				new String[] {"$1", "$2"}, 
				new String[] {"myMatrix", "myTranspose"}, 
				"DenseMatrix $2 = $1.transpose()");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixTranspose);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixVect = 
			new NewCodeWizardFormEntry("Matrix Vectorization",
				"Please Specify the Matrix Vectorization Options",
				new String[] {"Matrix", "Vector"},
				new String[] {"$1", "$2"}, 
				new String[] {"myMatrix", "myVector"}, 
				"DenseVector $2 = $1.vectorization()");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixVect);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixCopy = 
			new NewCodeWizardFormEntry("Matrix Copy",
				"Please Specify the Matrix Copy Options",
				new String[] {"Matrix", "Matrix Copy"},
				new String[] {"$1", "$2"}, 
				new String[] {"myMatrix", "myMatrixCopy"}, 
				"DenseMatrix $2 = $1.copy()");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixCopy);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathMatrixSolve = 
			new NewCodeWizardFormEntry("Linear Solve Ax=y",
				"Please Specify the Matrix Linear Solve Options",
				new String[] {"A", "x", "y"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"A", "x", "y"}, 
				"DenseMatrix $2 = $1.solve($3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathMatrixSolve);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathVectorCreate = 
			new NewCodeWizardFormEntry("Create a Vector from Values",
				"Please Specify the Vector Create Options",
				new String[] {"Vector", "Values"},
				new String[] {"$1", "$2"}, 
				new String[] {"myVector", "1.pure,2.pure,3.pure,4.pure"}, 
				"DenseVector $1 = DenseVector.valueOf($2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathVectorCreate);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathVectorCreateList = 
			new NewCodeWizardFormEntry("Create a Vector from a List",
				"Please Specify the Vector Create Options",
				new String[] {"Vector", "List"},
				new String[] {"$1", "$2"}, 
				new String[] {"myVector", "myList"}, 
				"DenseVector $1 = DenseVector.valueOf($2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathVectorCreateList);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathVectorGetElement = 
			new NewCodeWizardFormEntry("Get a Single Element from a Vector",
				"Please Specify the Vector Get Element Options",
				new String[] {"Element", "Vector", "Index"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myElement", "myVector", "2"}, 
				"def $1 = $2.get($3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathVectorGetElement);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathVectorAdd = 
			new NewCodeWizardFormEntry("Vector Addition",
				"Please Specify the Vector Addition Options",
				new String[] {"Vector 1", "Vector 2", "Result Vector"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myVector1", "myVector2", "resultVector"}, 
				"DenseVector $3 = $1.plus($2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathVectorAdd);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathVectorSub = 
			new NewCodeWizardFormEntry("Vector Subtraction",
				"Please Specify the Vector Subtraction Options",
				new String[] {"Vector 1", "Vector 2", "Result Vector"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myVector1", "myVector2", "resultVector"}, 
				"DenseVector $3 = $1.minus($2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathVectorSub);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathVectorCross = 
			new NewCodeWizardFormEntry("Vector Cross Product",
				"Please Specify the Vector Cross Product Options",
				new String[] {"Vector 1", "Vector 2", "Result Vector"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myVector1", "myVector2", "resultVector"}, 
				"DenseVector $3 = $1.cross($2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathVectorCross);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathVectorDot = 
			new NewCodeWizardFormEntry("Vector Dot Product",
				"Please Specify the Vector Dot Product Options",
				new String[] {"Vector 1", "Vector 2", "Result"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myVector1", "myVector2", "result"}, 
				"def $3 = $1.times($2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathVectorDot);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathVectorScalar = 
			new NewCodeWizardFormEntry("Vector Scalar Multiplication",
				"Please Specify the Vector Scalar Multiplication Options",
				new String[] {"Vector", "Scalar", "Result Vector"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"myVector1", "5.0.pure", "resultVector"}, 
				"DenseVector $3 = $1.times($2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathVectorScalar);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathVectorCopy = 
			new NewCodeWizardFormEntry("Vector Copy",
				"Please Specify the Vector Copy Options",
				new String[] {"Vector", "Vector Copy"},
				new String[] {"$1", "$2"}, 
				new String[] {"myVector", "myVectorCopy"}, 
				"DenseVector $2 = $1.copy()");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathVectorCopy);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathDerivative = 
			new NewCodeWizardFormEntry("Compute a Derivative",
				"Please Specify the Compute a Derivative Options",
				new String[] {"Target Function", "Point"},
				new String[] {"$1", "$2"}, 
				new String[] {"2.meters / 1.0.seconds * it - 3.meters", "4.1.seconds"}, 
				"def result = derivative({$1}, $2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathDerivative);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryMathIntegral = 
			new NewCodeWizardFormEntry("Compute an Integral",
				"Please Specify the Compute an Integral Options",
				new String[] {"Integrand", "Lower Bound", "Upper Bound"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"(1.pure - it ** 2).sqrt()", "0.pure", "1.pure"}, 
				"def result = integral({$1}, $2, $3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryMathIntegral);	
		/* ---- END Mathematics ---- */
		
		/* ---- BEGIN Queries ---- */
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryQueries = 
			new NewCodeWizardRadioButtonEntry("Queries",
				"Please Select the Type of Query",
				new String[] {"Compound Queries",
					            "Queries for Networks",
					            "Queries for Grids and Spaces",
					            "Queries for Geographies",
					            "Queries for Properties",
					            "SQL Queries"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryQueries);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryQueriesCompound = 
			new NewCodeWizardRadioButtonEntry("Compound Queries",
				"Please Select the Type of Compound Query",
				new String[] {"Get the items from the union of the results of two queries",
					            "Get the items from the intersection of the results of two queries",
					            "Get the items in a context that are not returned by a query"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryQueriesCompound);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryQueriesNetwork = 
			new NewCodeWizardRadioButtonEntry("Queries for Networks",
				"Please Select the Type of Network Query",
				new String[] {"Get all nodes within some given path length of node",
					            "Get all nodes adjacent to a node",
					            "Get all nodes that are predeccesors of a node",
					            "Get all nodes that are successors of a node"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryQueriesNetwork);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryQueriesSpace = 
			new NewCodeWizardRadioButtonEntry("Queries for Grids and Spaces",
				"Please Select the Type of Grid or Space Query",
				new String[] {"Get all the items in a Grid within X distance of a given item",
					            "Get all the items in a Grid in the Moore neighborhood of a given item",
					            "Get all the items in a Grid in the Von Neumann neighborhood of a given item",
					            "Get all the items in a Space with X distance of a given item"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryQueriesSpace);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryQueriesGeo = 
			new NewCodeWizardRadioButtonEntry("Queries for Geographies",
				"Please Select the Type of Geography Query",
				new String[] {"Get the items contained by a Geometry",
					            "Get the items intersected by a Geometry",
					            "Get the items within X meters of a centroid",
					            "Get the items touched by a Geometry",
					            "Get the items a Geometry is within"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryQueriesGeo);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryQueriesProp = 
			new NewCodeWizardRadioButtonEntry("Queries for Properties",
				"Please Select the Type of Property Query",
				new String[] {"Get all items in a context with a property equal to some value",
					            "Get all items in a context with a property greater than some value",
					            "Get all itemsList in a context with a property less than some value"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryQueriesProp);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryQueriesSQL = 
			new NewCodeWizardRadioButtonEntry("SQL Queries",
				"Please Select the Type of SQL Query",
				new String[] {"Find Agents with an SQL Query",
					            "Find Agents with an SQL Query with a JoSQL Function Handler",
					            "Find Network Edges with an SQL Query",
					            "Find Network Edges with an SQL Query with a JoSQL Function Handle"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryQueriesSQL);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesCompoundUnion = 
			new NewCodeWizardFormEntry("Get the items from the union of the results of two queries",
				"Please Specify the Query Options",
				new String[] {"Query 1", "Query 2", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"new PropertyEquals(context, \"name\", \"David Healy\")", 
					            "new GridWithin(grid, agent, 3)", ""}, 
				"Iterator list = new OrQuery($1, $2).query($3).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesCompoundUnion);	
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesCompoundIntersect = 
			new NewCodeWizardFormEntry("Get the items from the intersection of the results of two queries",
				"Please Specify the Query Options",
				new String[] {"Query 1", "Query 2", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"new PropertyEquals(context, \"name\", \"David Healy\")", 
					            "new GridWithin(grid, agent, 3)", ""}, 
				"Iterator list = new AndQuery($1, $2).query($3).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesCompoundIntersect);	
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesCompoundNot = 
			new NewCodeWizardFormEntry("Get the items in a context that are not returned by a query",
				"Please Specify the Query Options",
				new String[] {"Query 1", "Query 2", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"new PropertyEquals(context, \"name\", \"David Healy\")", 
					            "new PropertyLessThan(context, \"wealth\", 3.14)", ""}, 
				"Iterator list = new NotQuery($1, $2).query($3).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesCompoundNot);	
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesNetPath = 
			new NewCodeWizardFormEntry("Get all nodes within some given path length of node",
				"Please Specify the Query Options",
				new String[] {"Network", "Node", "Path Length", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3", "$4"}, 
				new String[] {"network", "node", "3", ""}, 
				"Iterator list = new NetPathWithin($1, $2, $3).query($4).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesNetPath);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesNetAdjacent = 
			new NewCodeWizardFormEntry("Get all nodes adjacent to a node",
				"Please Specify the Query Options",
				new String[] {"Network", "Node", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"network", "node", ""}, 
				"Iterator list = new NetworkAdjacent($1, $2).query($3).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesNetAdjacent);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesNetPred = 
			new NewCodeWizardFormEntry("Get all nodes that are predeccesors of a node",
				"Please Specify the Query Options",
				new String[] {"Network", "Node", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"network", "node", ""}, 
				"Iterator list = new NetworkPredecessor($1, $2).query($3).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesNetPred);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesNetSucc = 
			new NewCodeWizardFormEntry("Get all nodes that are successors of a node",
				"Please Specify the Query Options",
				new String[] {"Network", "Node", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"network", "node", ""}, 
				"Iterator list = new NetworkSuccessor($1, $2).query($3).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesNetSucc);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesGridWithin = 
			new NewCodeWizardFormEntry("Get all the items in a Grid within X distance of a given item",
				"Please Specify the Query Options",
				new String[] {"Grid", "Agent", "Distance", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3", "$4"}, 
				new String[] {"grid", "agent", "3", ""}, 
				"Iterator list = new GridWithin($1, $2, $3).query($4).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesGridWithin);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesGridMoore = 
			new NewCodeWizardFormEntry("Get all the items in a Grid in the Moore neighborhood of a given item",
				"Please Specify the Query Options",
				new String[] {"Grid", "Agent", "Extent (x,y,z,...)", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3", "$4"}, 
				new String[] {"grid", "agent", "1, 1", ""}, 
				"Iterator list = new MooreQuery($1, $2, $3).query($4).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesGridMoore);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesGridVN = 
			new NewCodeWizardFormEntry("Get all the items in a Grid in the Von Neumann neighborhood of a given item",
				"Please Specify the Query Options",
				new String[] {"Grid", "Agent", "Extent (x,y,z,...)", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3", "$4"}, 
				new String[] {"grid", "agent", "1, 1", ""}, 
				"Iterator list = new VNQuery($1, $2, $3).query($4).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesGridVN);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesSpace = 
			new NewCodeWizardFormEntry("Get all the items in a Space with X distance of a given item",
				"Please Specify the Query Options",
				new String[] {"Space", "Agent", "Distance", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3", "$4"}, 
				new String[] {"space", "agent", "11.5", ""}, 
				"Iterator list = new ContinuousWithin($1, $2, $3).query($4).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesSpace);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesGeoContained = 
			new NewCodeWizardFormEntry("Get the items contained by a Geometry",
				"Please Specify the Query Options",
				new String[] {"Geography", "Geometry", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"geography", "geometry", ""}, 
				"Iterator list = new ContainsQuery($1, $2).query($3).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesGeoContained);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesGeoIntersect = 
			new NewCodeWizardFormEntry("Get the items intersected by a Geometry",
				"Please Specify the Query Options",
				new String[] {"Geography", "Geometry", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"geography", "geometry", ""}, 
				"Iterator list = new ItersectsQuery($1, $2).query($3).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesGeoIntersect);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesGeoWithin = 
			new NewCodeWizardFormEntry("Get the items within X meters of a centroid",
				"Please Specify the Query Options",
				new String[] {"Geography", "Geometry", "Distance", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3", "$4"}, 
				new String[] {"geography", "geometry", "10", ""}, 
				"Iterator list = new GeographyWithin($1, $3, $2).query($4).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesGeoWithin);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesGeoTouched = 
			new NewCodeWizardFormEntry("Get the items touched by a Geometry",
				"Please Specify the Query Options",
				new String[] {"Geography", "Geometry", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"geography", "geometry", ""}, 
				"Iterator list = new TouchesQuery($1, $2).query($3).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesGeoTouched);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesGeoWithin2 = 
			new NewCodeWizardFormEntry("Get the items a Geometry is within",
				"Please Specify the Query Options",
				new String[] {"Geography", "Geometry", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"geography", "geometry", ""}, 
				"Iterator list = new WithinQuery($1, $2).query($3).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesGeoWithin2);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesPropEq = 
			new NewCodeWizardFormEntry("Get all items in a context with a property equal to some value",
				"Please Specify the Query Options",
				new String[] {"Context", "Property", "Value", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3", "$4"}, 
				new String[] {"context", "\"name\"", "\"David Healy\"", ""}, 
				"Iterator list = new PropertyEquals($1, $2, $3).query($4).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesPropEq);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesPropGT = 
			new NewCodeWizardFormEntry("Get all items in a context with a property greater than some value",
				"Please Specify the Query Options",
				new String[] {"Context", "Property", "Value", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3", "$4"}, 
				new String[] {"context", "\"wealth\"", "3.14", ""}, 
				"Iterator list = new PropertyGreaterThan($1, $2, $3).query($4).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesPropGT);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesPropLT = 
			new NewCodeWizardFormEntry("Get all itemsList in a context with a property less than some value",
				"Please Specify the Query Options",
				new String[] {"Context", "Property", "Value", "Optional Nested Result"},
				new String[] {"$1", "$2", "$3", "$4"}, 
				new String[] {"context", "\"wealth\"", "3.14", ""}, 
				"Iterator list = new PropertyLessThan($1, $2, $3).query($4).iterator()");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesPropLT);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesSQLAgent = 
			new NewCodeWizardFormEntry("Find Agents with an SQL Query",
				"Please Specify the Query Options",
				new String[] {"Context Path", "SQL Query"},
				new String[] {"$1", "$2"}, 
				new String[] {"\"root\"", "\"SELECT * FROM java.lang.Object\""}, 
				"List resultList = FindAgentsInContext($1, $2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesSQLAgent);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesSQLAgentFunc = 
			new NewCodeWizardFormEntry("Find Agents with an SQL Query with a JoSQL Function Handler",
				"Please Specify the Query Options",
				new String[] {"Context Path", "SQL Query", "JoSQL Function Handler"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"\"root\"", "\"SELECT * FROM java.lang.Object\"", "new FunctionHandler()"}, 
				"List resultList = FindAgentsInContext($1, $2, $3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesSQLAgentFunc);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesSQLNet = 
			new NewCodeWizardFormEntry("Find Network Edges with an SQL Query",
				"Please Specify the Query Options",
				new String[] {"Network Path", "SQL Query"},
				new String[] {"$1", "$2"}, 
				new String[] {"\"root/friends\"", "\"SELECT toString Name FROM java.lang.Object\")"}, 
				"List resultList = FindAgentsInNetwork($1, $2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesSQLNet);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryQueriesSQLNetFunc = 
			new NewCodeWizardFormEntry("Find Network Edges with an SQL Query with a JoSQL Function Handle",
				"Please Specify the Query Options",
				new String[] {"Network Path", "SQL Query", "JoSQL Function Handler"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"\"root/friends\"", "\"SELECT toString Name FROM java.lang.Object\")", "new FunctionHandler()"}, 
				"List resultList = FindAgentsInNetwork($1, $2, $3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryQueriesSQLNetFunc);
		/* ---- END Queries ---- */
		
		/* ---- BEGIN Networks ---- */
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryNetworks = 
			new NewCodeWizardRadioButtonEntry("Networks",
				"Please Select the Network Operation",
				new String[] {"Create a Network or Edge",
					            "Find a Network",
					            "Find or Modify Edges",
					            "Find Nodes"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryNetworks);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryNetworksCreate = 
			new NewCodeWizardRadioButtonEntry("Create a Network or Edge",
				"Please Select the Network Create Option",
				new String[] {"Create a Network",
					            "Create a Random Density Network",
					            "Create a 1D Lattice Network",
					            "Create a 2D Lattice Network",
					            "Create a Watts Beta Small World Network",
					            "Create a Network from a File",
					            "Create an Edge"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryNetworksCreate);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryNetworksFindEdge = 
			new NewCodeWizardRadioButtonEntry("Find or Modify Edges",
				"Please Select the Find Edges Option",
				new String[] {"Get an Edge between Two Agents",
					            "Get the Weight of an Edge",
					            "Set the Weight of an Edge",
					            "Remove an Edge",
					            "Get all Edges Incoming to an Agent",
					            "Get all Edges Outgoing from an Agent",
					            "Get all Edges to or from an Agent"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryNetworksFindEdge);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryNetworksFindNode = 
			new NewCodeWizardRadioButtonEntry("Find Nodes",
				"Please Select the Find Nodes Option",
				new String[] {"Get the Predecessors of a Node",
					            "Get the Successors of a Node",
					            "Get all Nodes connected to a Node"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryNetworksFindNode);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetCreate = 
			new NewCodeWizardFormEntry("Create a Network",
				"Please Specify the Create Network Options",
				new String[] {"Context path", "Network Name", "Directed"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"\"mainContext\"", "\"MyNetwork\"", "true"}, 
				"Network newNetwork = CreateNetwork($1, $2, $3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetCreate);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetCreateRDN = 
			new NewCodeWizardFormEntry("Create a Random Density Network",
				"Please Specify the Create Random Density Network Options",
				new String[] {"Context path", "Network Name", "Directed", "Density", "Allow Self Loops", "Symmetric"},
				new String[] {"$1", "$2", "$3", "$4", "$5", "$6"}, 
				new String[] {"\"mainContext\"", "\"MyNetwork\"", "true", "0.25", "false", "false"}, 
				"Network newNetwork = CreateRandomDensityNetwork($1, $2, $3, $4, $5, $6)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetCreateRDN);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetCreate1DL = 
			new NewCodeWizardFormEntry("Create a 1D Lattice Network",
				"Please Specify the Create 1D Lattice Network Options",
				new String[] {"Context path", "Network Name", "Directed", "Toroidal", "Symmetric"},
				new String[] {"$1", "$2", "$3", "$4", "$5"}, 
				new String[] {"\"mainContext\"", "\"MyNetwork\"", "true", "false", "false"}, 
				"Network newNetwork = Create1DLatticeNetwork($1, $2, $3, $4, $5)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetCreate1DL);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetCreate2DL = 
			new NewCodeWizardFormEntry("Create a 2D Lattice Network",
				"Please Specify the Create 2D Lattice Network Options",
				new String[] {"Context path", "Network Name", "Directed", "Toroidal"},
				new String[] {"$1", "$2", "$3", "$4"}, 
				new String[] {"\"mainContext\"", "\"MyNetwork\"", "true", "false", "false"}, 
				"Network newNetwork = Create2DLatticeNetwork($1, $2, $3, $4)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetCreate2DL);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetCreateSWN = 
			new NewCodeWizardFormEntry("Create a Watts Beta Small World Network",
				"Please Specify the Create Watts Beta Small World Network Options",
				new String[] {"Context path", "Network Name", "Directed", "Beta", "Neighborhood Size", "Symmetric"},
				new String[] {"$1", "$2", "$3", "$4", "$5", "$6"}, 
				new String[] {"\"mainContext\"", "\"MyNetwork\"", "true", "0.1", "4", "true"}, 
				"Network newNetwork = CreateWattsBetaSmallWorldNetwork($1, $2, $3, $4, $5, $6)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetCreateSWN);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetCreateFile = 
			new NewCodeWizardFormEntry("Create a Network from a File",
				"Please Specify the Create Network from a File Options",
				new String[] {"Context path", "Network Name", "Directed", "Agent Class", "File Name", "Network File Format"},
				new String[] {"$1", "$2", "$3", "$4", "$5", "$6"}, 
				new String[] {"\"mainContext\"", "\"MyNetwork\"", "true", "\"boids.BoidAgent\"", "\"files\\boidNet.dl\"", "NetworkFileFormat.DL"}, 
				"Network newNetwork = CreateNetwork($1, $2, $3, $4, $5, $6)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetCreateFile);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetCreateEdge = 
			new NewCodeWizardFormEntry("Create an Edge",
				"Please Specify the Create an Edge Options",
				new String[] {"Network Path", "Source Node", "Target Node", "Weight"},
				new String[] {"$1", "$2", "$3", "$4"}, 
				new String[] {"\"root/Network\"", "source", "target", "1.0"}, 
				"RepastEdge newEdge = CreateEdge($1, $2, $3, $4)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetCreateEdge);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetFind = 
			new NewCodeWizardFormEntry("Find a Network",
				"Please Specify the Find Network Options",
				new String[] {"Network path"},
				new String[] {"$1"}, 
				new String[] {"\"root/Network\""}, 
				"Network network = FindNetwork($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetFind);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetGetEdge = 
			new NewCodeWizardFormEntry("Get an Edge between Two Agents",
				"Please Specify the Get Edge Options",
				new String[] {"Network Path", "Source Node", "Target Node"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"\"root/Network\"", "source", "target"}, 
				"RepastEdge edge = FindEdge($1, $2, $3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetGetEdge);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetGetEdgeWeight = 
			new NewCodeWizardFormEntry("Get the Weight of an Edge",
				"Please Specify the Get Edge Weight Options",
				new String[] {"Network Path", "Source Node", "Target Node"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"\"root/Network\"", "source", "target"}, 
				"double edgeWeight = GetEdgeWeight($1, $2, $3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetGetEdgeWeight);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetSetEdgeWeight = 
			new NewCodeWizardFormEntry("Set the Weight of an Edge",
				"Please Specify the Set Edge Weight Options",
				new String[] {"Network Path", "Source Node", "Target Node", "Weight"},
				new String[] {"$1", "$2", "$3", "$4"}, 
				new String[] {"\"root/Network\"", "source", "target", "2.5"}, 
				"RepastEdge edge = SetEdgeWeight($1, $2, $3, $4)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetSetEdgeWeight);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetEdgeRemove = 
			new NewCodeWizardFormEntry("Remove an Edge",
				"Please Specify the Remove Edge Options",
				new String[] {"Network Path", "Source Node", "Target Node"},
				new String[] {"$1", "$2", "$3"}, 
				new String[] {"\"root/Network\"", "source", "target"}, 
				"RepastEdge removedEdge = RemoveEdge($1, $2, $3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetEdgeRemove);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetGetInEdges = 
			new NewCodeWizardFormEntry("Get all Edges Incoming to an Agent",
				"Please Specify the Get In Edges Options",
				new String[] {"Network Path", "Node"},
				new String[] {"$1", "$2"}, 
				new String[] {"\"root/Network\"", "myNode"}, 
				"List inEdges = GetInEdges($1, $2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetGetInEdges);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetGetOutEdges = 
			new NewCodeWizardFormEntry("Get all Edges Outgoing from an Agent",
				"Please Specify the Get Out Edges Options",
				new String[] {"Network Path", "Node"},
				new String[] {"$1", "$2"}, 
				new String[] {"\"root/Network\"", "myNode"}, 
				"List outEdges = GetOutEdges($1, $2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetGetOutEdges);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetGetEdges = 
			new NewCodeWizardFormEntry("Get all Edges to or from an Agent",
				"Please Specify the Get All Edges Options",
				new String[] {"Network Path", "Node"},
				new String[] {"$1", "$2"}, 
				new String[] {"\"root/Network\"", "myNode"}, 
				"List edges = GetEdges($1, $2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetGetEdges);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetGetNodePred = 
			new NewCodeWizardFormEntry("Get the Predecessors of a Node",
				"Please Specify the Get the Predecessors of a Node Options",
				new String[] {"Network Path", "Node"},
				new String[] {"$1", "$2"}, 
				new String[] {"\"root/Network\"", "myNode"}, 
				"List predecessors = GetPredecessors($1, $2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetGetNodePred);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetGetNodeSucc = 
			new NewCodeWizardFormEntry("Get the Successors of a Node",
				"Please Specify the Get the Successors of a Node Options",
				new String[] {"Network Path", "Node"},
				new String[] {"$1", "$2"}, 
				new String[] {"\"root/Network\"", "myNode"}, 
				"List successors = GetSuccessors($1, $2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetGetNodeSucc);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryNetGetNodes = 
			new NewCodeWizardFormEntry("Get all Nodes connected to a Node",
				"Please Specify the Get all Nodes connected to a Node Options",
				new String[] {"Network Path", "Node"},
				new String[] {"$1", "$2"}, 
				new String[] {"\"root/Network\"", "myNode"}, 
				"List adjacent = GetAdjacent($1, $2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryNetGetNodes);
		/* ---- END Networks ---- */
		
		/* ---- BEGIN Schedule ---- */
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntrySchedule = 
			new NewCodeWizardRadioButtonEntry("Schedule",
				"Please Select the Schedule Operation",
				new String[] {"Get the Current Tick Count",
					            "Get the Current Tick Count as an Amount",
					            "End the Simulation Now",
					            "End the Simulation at a Specific Tick",
					            "Pause the Simulation Now",
					            "Pause the Simulation at a Specific Tick",
					            "Schedule a Non-Repeating Agent Method",
					            "Schedule a Repeating Agent Method",
					            "Cancel a Scheduled Action"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntrySchedule);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryScheduleGetTick = 
			new NewCodeWizardFormEntry("Get the Current Tick Count",
				"Please click finish",
				new String[] {},
				new String[] {}, 
				new String[] {}, 
				"double tick = GetTickCount()");
		exampleListTaskWizard.add(newCodeWizardFormEntryScheduleGetTick);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryScheduleGetTickAmount = 
			new NewCodeWizardFormEntry("Get the Current Tick Count as an Amount",
				"Please click finish",
				new String[] {},
				new String[] {}, 
				new String[] {}, 
				"Amount tick = GetTickCountInTimeUnits()");
		exampleListTaskWizard.add(newCodeWizardFormEntryScheduleGetTickAmount);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryScheduleEndNow = 
			new NewCodeWizardFormEntry("End the Simulation Now",
				"Please click finish",
				new String[] {},
				new String[] {}, 
				new String[] {}, 
				"EndSimulationRun()");
		exampleListTaskWizard.add(newCodeWizardFormEntryScheduleEndNow);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryScheduleEndAt = 
			new NewCodeWizardFormEntry("End the Simulation at a Specific Tick",
				"Please Specify the End the Simulation at a Specific Tick Options",
				new String[] {"End Tick"},
				new String[] {"$1"}, 
				new String[] {"100"}, 
				"EndSimulationRunAt($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryScheduleEndAt);
		
		NewCodeWizardFormEntry newCodeWizardFormEntrySchedulePauseNow = 
			new NewCodeWizardFormEntry("Pause the Simulation Now",
				"Please click finish",
				new String[] {},
				new String[] {}, 
				new String[] {}, 
				"PauseSimulationRun()");
		exampleListTaskWizard.add(newCodeWizardFormEntrySchedulePauseNow);
		
		NewCodeWizardFormEntry newCodeWizardFormEntrySchedulePauseAt = 
			new NewCodeWizardFormEntry("Pause the Simulation at a Specific Tick",
				"Please Specify the Pause the Simulation at a Specific Tick Options",
				new String[] {"Pause Tick"},
				new String[] {"$1"}, 
				new String[] {"100"}, 
				"PauseSimulationRunAt($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntrySchedulePauseAt);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryScheduleScheduleNonRepeat = 
			new NewCodeWizardFormEntry("Schedule a Non-Repeating Agent Method",
				"Please Specify the Schedule a Non-Repeating Agent Method Options",
				new String[] {"Agent", "Start Tick", "Method Name", "Parameters"},
				new String[] {"$1", "$2", "$3", "$4"}, 
				new String[] {"myAgent", "10", "\"calculate\"", "1.5"}, 
				"ISchedulableAction action = ScheduleAction($1, $2, $3, $4)");
		exampleListTaskWizard.add(newCodeWizardFormEntryScheduleScheduleNonRepeat);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryScheduleScheduleRepeat = 
			new NewCodeWizardFormEntry("Schedule a Repeating Agent Method",
				"Please Specify the Schedule a Repeating Agent Method Options",
				new String[] {"Agent", "Start Tick", "Repeat Interval", "Method Name", "Parameters"},
				new String[] {"$1", "$2", "$3", "$4", "$5"}, 
				new String[] {"myAgent", "10", "1", "\"sum\"", "2, 3"}, 
				"ISchedulableAction action = ScheduleAction($1, $2, $3, $4, $5)");
		exampleListTaskWizard.add(newCodeWizardFormEntryScheduleScheduleRepeat);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryScheduleCancel = 
			new NewCodeWizardFormEntry("Cancel a Scheduled Action",
				"Please click finish",
				new String[] {"Action to Cancel"},
				new String[] {"$1"}, 
				new String[] {"actionToCancel"}, 
				"CancelAction($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryScheduleCancel);
		/* ---- END Schedule ---- */
		
		/* ---- TODO BEGIN Projections ---- */
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryProjections = 
			new NewCodeWizardRadioButtonEntry("Projections",
				"Please Select the Projection Operation",
				new String[] {"Create a Projection",
					            "Find a Projection"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryProjections);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryProjectionsCreate = 
			new NewCodeWizardRadioButtonEntry("Create a Projection",
				"Please Select the Create Projection Operation",
				new String[] {"Create a Grid",
					            "Create a Space",
					            "Create a Geography",
					            "Create a Value Layer"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryProjectionsCreate);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryProjectionCreateGrid = 
			new NewCodeWizardFormEntry("Create a Grid",
				"Please Specify the Create Grid Options",
				new String[] {"Grid path", "Grid name", "Grid borders\n \t[WrapAroundBorders,\n\t" +
						" BouncyBorders,\n\t InfiniteBorders,\n\t StickyBorders,\n\t StrictBorders]", 
					"Grid dimensions \n\t[comma separated list]"},
				new String[] {"$1","$2","$3","$4"}, 
				new String[] {"\"root\"", "\"Grid\"", "\"WrapAroundBorders\"", "100,100"}, 
				"Grid grid = CreateGrid($1,$2,$3,$4)");
		exampleListTaskWizard.add(newCodeWizardFormEntryProjectionCreateGrid);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryProjectionCreateSpace = 
			new NewCodeWizardFormEntry("Create a Space",
				"Please Specify the Create Space Options",
				new String[] {"Space path", "Space name", "Space borders\n \t[WrapAroundBorders,\n\t" +
						" BouncyBorders,\n\t InfiniteBorders,\n\t StickyBorders,\n\t StrictBorders]", 
					"Space dimensions \n\t[comma separated list]"},
				new String[] {"$1","$2","$3","$4"}, 
				new String[] {"\"root\"", "\"Space\"", "\"WrapAroundBorders\"", "100,100"}, 
				"ContinuousSpace space = CreateContinuousSpace($1,$2,$3,$4)");
		exampleListTaskWizard.add(newCodeWizardFormEntryProjectionCreateSpace);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryProjectionCreateGeography = 
			new NewCodeWizardFormEntry("Create a Geography",
				"Please Specify the Create Geography Options",
				new String[] {"Geography path", "Geography name"},
				new String[] {"$1","$2"}, 
				new String[] {"\"root\"", "\"Geography\""}, 
				"Geography geography = CreateGeography($1,$2)");
		exampleListTaskWizard.add(newCodeWizardFormEntryProjectionCreateGeography);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryProjectionCreateValueLayer = 
			new NewCodeWizardFormEntry("Create a Value Layer",
				"Please Specify the Create Value Layer Options",
				new String[] {"Layer path", "Layer name",	"Layer dimensions \n\t[comma separated list]"},
				new String[] {"$1","$2","$3"}, 
				new String[] {"\"root\"", "\"Layer\"", "100,100"}, 
				"GridValueLayer = CreateGridValueLayer($1,$2,$3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryProjectionCreateValueLayer);
		
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryProjectionsFind = 
			new NewCodeWizardRadioButtonEntry("Find a Projection",
				"Please Select the Find Projection Operation",
				new String[] {"Find a Grid",
					            "Find a Space",
					            "Find a Geography",
					            "Find a Value Layer"},
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryProjectionsFind);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryProjectionFindGrid = 
			new NewCodeWizardFormEntry("Find a Grid",
				"Please Specify the Find Grid Options",
				new String[] {"Grid path"},
				new String[] {"$1"}, 
				new String[] {"\"root/Grid\""}, 
				"Grid grid = FindGrid($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryProjectionFindGrid);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryProjectionFindSpace = 
			new NewCodeWizardFormEntry("Find a Space",
				"Please Specify the Find Space Options",
				new String[] {"Space path"},
				new String[] {"$1"}, 
				new String[] {"\"root/Space\""}, 
				"ContinuousSpace space = FindContinuousSpace($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryProjectionFindSpace);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryProjectionFindGeo = 
			new NewCodeWizardFormEntry("Find a Geography",
				"Please Specify the Find Geography Options",
				new String[] {"Geography path"},
				new String[] {"$1"}, 
				new String[] {"\"root/Geography\""}, 
				"Geography geography = FindGeography($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryProjectionFindGeo);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryProjectionFindValue = 
			new NewCodeWizardFormEntry("Find a Value Layer",
				"Please Specify the Find Value Layer Options",
				new String[] {"Value Layer path"},
				new String[] {"$1"}, 
				new String[] {"\"root/ValueLayer\""}, 
				"ValueLayer layer = FindValueLayer($1)");
		exampleListTaskWizard.add(newCodeWizardFormEntryProjectionFindValue);
		
		/* ---- END Projections ---- */
		

		/* ---- BEGIN Adaptation ---- */
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryAdvancedAdaptation = new NewCodeWizardRadioButtonEntry(
				"Advanced Adaptation",
				"Please Select the Kind of Advanced Adaptation to Perform",
				new String[] { "Use a Regression Model",
						"Use a Neural Network", "Use a Genetic Algorithm" },
						null);
		exampleListTaskWizard
		.add(newCodeWizardRadioButtonEntryAdvancedAdaptation);

		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryRegression = new NewCodeWizardRadioButtonEntry(
				"Use a Regression Model",
				"Please Select the Kind of Regression Task to Perform",
				new String[] { "Forecast from a Regression Model",
				"Store Data for a Regression Model" }, null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryRegression);

		NewCodeWizardFormEntry newCodeWizardFormEntryRegressionForecast = new NewCodeWizardFormEntry(
				"Forecast from a Regression Model",
				"Please Select the Regression Options",
				new String[] { "Results Storage Variable",
						"Regression Model Variable",
				"Comma Separated List of Independent Variables or Values" },
				new String[] { "$1", "$2", "$3" }, new String[] { "x", "model",
				"y1, y2, y3" }, "double $1 = $2.forecast($3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryRegressionForecast);

		NewCodeWizardFormEntry newCodeWizardFormEntryRegressionStore = new NewCodeWizardFormEntry(
				"Store Data for a Regression Model",
				"Please Select the Regression Options",
				new String[] { "Regression Model Variable",
						"Dependent Variable or Value",
				"Comma Separated List of Independent Variables or Values" },
				new String[] { "$1", "$2", "$3" }, new String[] { "model", "y",
				"x1, x2, x3" }, "$1.add($2, $3)");
		exampleListTaskWizard.add(newCodeWizardFormEntryRegressionStore);

		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryNN = new NewCodeWizardRadioButtonEntry(
				"Use a Neural Network",
				"Please Select the Kind of Neural Network Task to Perform",
				new String[] { "Forecast from a Neural Network",
				"Train a Neural Network" }, null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryNN);

		NewCodeWizardFormEntry newCodeWizardRadioButtonEntryNNForecast = new NewCodeWizardFormEntry(
				"Forecast from a Neural Network",
				"Please Select the Neural Network Options",
				new String[] {
						"Results Storage Array (i.e., double[numberOfOutputNeurons])",
						"Neural Network Variable",
				"Input Data Array (i.e., double[numberOfInputNeurons])" },
				new String[] { "$1", "$2", "$3" }, new String[] { "outputData",
						"model", "inputData" },
		"double[] $1 = JooneTools.interrogate($2, $3)");
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryNNForecast);

		NewCodeWizardFormEntry newCodeWizardRadioButtonEntryNNTrain = new NewCodeWizardFormEntry(
				"Train a Neural Network",
				"Please Select the Neural Network Options",
				new String[] {
						"Neural Network Variable",
						"Input Array (i.e., double[numberOfTrainingExamples, numberOfInputNeurons])",
						"Target Output Array (i.e., double[numberOfTrainingExamples, numberOfOutputNeurons])",
						"Training Cycles (i.e., epochs)",
				"Threshold for Convergence (RMSE)" }, new String[] {
						"$1", "$2", "$3", "$4", "$5" }, new String[] { "model",
						"inputDataArray", "targetOutputDataArray", "5000",
				"0.01" },
		"JooneTools.train($1, $2, $3, $4, $5, 0, null, false)");
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryNNTrain);

		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryGA = new NewCodeWizardRadioButtonEntry(
				"Use a Genetic Algorithm",
				"Please Select the Kind of Genetic Algorithm Task to Perform",
				new String[] {
						"Get the Best Solution from a Genetic Algorithm",
				"Reset the Population of a Genetic Algorithm" }, null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryGA);

		NewCodeWizardFormEntry newCodeWizardRadioButtonEntryGAResult = new NewCodeWizardFormEntry(
				"Get the Best Solution from a Genetic Algorithm",
				"Please Select the Genetic Algorithm Options", new String[] {
						"Solution Storage Array (i.e., double[numberOfGenes])",
						"Genetic Algorithm Variable",
				"Number of Generations to Run (i.e., Cycles)" },
				new String[] { "$1", "$2", "$3" }, new String[] { "solution",
						"model", "100" },
		"double[] $1 = $2.getBestSolution($3)");
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryGAResult);

		NewCodeWizardFormEntry newCodeWizardRadioButtonEntryGAReset = new NewCodeWizardFormEntry(
				"Reset the Population of a Genetic Algorithm",
				"Please Select the Genetic Algorithm Options",
				new String[] { "Genetic Algorithm Variable" },
				new String[] { "$1" }, new String[] { "model" }, "$1.reset()");
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryGAReset);
		/* ---- END Adaptation ---- */
		
		/* ---- BEGIN Misc ---- */
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryMisc =
			new NewCodeWizardRadioButtonEntry("Miscellaneous", 
					"Please Select the Task",
				new String[] {"Print Statement",
						          "User Code Line"}, 
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryMisc);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryPrintLine = 
			new NewCodeWizardFormEntry("Print Statement", 
					"Please Select the Kind of Code to Create",
				new String[] {"Value to Print"}, 
				new String[] {"$1"},
				new String[] {"\"Hello!\""}, 
				"println \"$1\"");
		exampleListTaskWizard.add(newCodeWizardFormEntryPrintLine);

		NewCodeWizardFormEntry newCodeWizardFormEntryCodeToInclude = 
			new NewCodeWizardFormEntry("User Code Line", 
					"Please Select the Kind of Code to Create",
				new String[] {"Code to Include"}, 
				new String[] {"$1"},
				new String[] {"int a = 1"}, 
				"$1");
		exampleListTaskWizard.add(newCodeWizardFormEntryCodeToInclude);
		/* ---- END Misc ---- */
		
		/* ---- BEGIN Model Initializer ---- */
		NewCodeWizardRadioButtonEntry newCodeWizardRadioButtonEntryInitializer =
			new NewCodeWizardRadioButtonEntry("Model Initializer", 
					"Please Select the Model InitializerTask",
				new String[] {"Initialize Units, Matrices, and Calculus"}, 
			null);
		exampleListTaskWizard.add(newCodeWizardRadioButtonEntryInitializer);
		
		NewCodeWizardFormEntry newCodeWizardFormEntryInitializerRMC = 
			new NewCodeWizardFormEntry("Initialize Units, Matrices, and Calculus", 
					"Please click finish",
				new String[] {}, 
				new String[] {},
				new String[] {}, "RepastMathEMC.initAll()");
		exampleListTaskWizard.add(newCodeWizardFormEntryInitializerRMC);
		/* ---- BEGIN Model Initializer ---- */
		
		return exampleListTaskWizard;
	}
}