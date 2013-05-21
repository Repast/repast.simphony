package repast.simphony.systemdynamics.ode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import repast.simphony.systemdynamics.support.ArrayReference;
import repast.simphony.systemdynamics.translator.ArrayReferenceNative;
import repast.simphony.systemdynamics.translator.Equation;
import repast.simphony.systemdynamics.translator.InformationManagers;
import repast.simphony.systemdynamics.translator.NativeDataTypeManager;
import repast.simphony.systemdynamics.translator.Node;
import repast.simphony.systemdynamics.translator.Parser;
import repast.simphony.systemdynamics.translator.RepastSimphonyEnvironment;
import repast.simphony.systemdynamics.translator.Translator;
import repast.simphony.systemdynamics.translator.TreeTraversal;

public class ODECodeGenerator {

	private ODEAnalyzer analyzer;
	private String className;
	private String packageName;
	private String runnerName;
	private String contextName;

	public ODECodeGenerator(Map<String, Equation> equations, String packageName, String className) {
		this.packageName = packageName;
		this.className = className;
		analyzer = new ODEAnalyzer(equations);
		analyzer.analyze();
	}

	public void generateDerivativeClass(BufferedWriter code) {

		ObjectHeader.generate(this, code, analyzer, packageName, className);
		Constructor.generate(this, code, analyzer, packageName, className);
		MethodHeader.generate(this, code, analyzer);
		MethodCalculations.generate(this, code, analyzer);
		MethodFooter.generate(this, code, analyzer);
		SetterGetter.generate(this, code, analyzer);
		ObjectFooter.generate(this, code, analyzer);

	}

	public void generateRunnerClass(BufferedWriter code, String myClassName) {

		NativeDataTypeManager ndtm = InformationManagers.getInstance().getNativeDataTypeManager();
		runnerName = myClassName;

		try {
			code.append("package "+packageName+";\n\n");
			code.append("import org.apache.commons.math3.ode.FirstOrderIntegrator;\n");
			code.append("import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;\n\n");
			code.append("import repast.simphony.engine.environment.RunEnvironment;\n");
			code.append("import repast.simphony.engine.schedule.ScheduledMethod;\n");
			code.append("import repast.simphony.parameter.Parameters;\n\n");

			code.append("public class "+myClassName+" {\n\n");

			// declare variables

			code.append("private double y[] = new double["+analyzer.getNumberODE()+"];\n");
			code.append("FirstOrderIntegrator integrator;\n");
			code.append(""+className+" ode;\n");
			code.append("double timeDelta;\n\n");

			// constructor

			code.append("public "+myClassName+"() {\n\n");
			code.append("\tParameters params = RunEnvironment.getInstance().getParameters();\n");
			code.append("\ttimeDelta = (Double) params.getValue(\"SAVEPER\");\n");
			code.append("\tintegrator = new EulerIntegrator((Double) params.getValue(\"TIME_STEP\"));\n");

			code.append("\tode = new "+className+"(\n");
			int i = 0;
			for (Equation eqn : analyzer.getAuxiliariesForConstructor()) {
				if (i++ > 0)
					code.append(",\n");
				code.append("\t\t(Double) params.getValue(\""+ndtm.makeLegal(eqn.getLhs())+"\")");
			}

			code.append("\n\t);\n");

			// initial state

			for (int n = 0; n < analyzer.getNumberODE(); n++) {
				String lhs = analyzer.getStockFor(Integer.toString(n));
				Equation eqn = analyzer.getEquationForLHS(lhs);
				String initVal = eqn.getIntialValue();
				if (Parser.isNumber(initVal)) {
					code.append("\ty["+n+"] = "+initVal+";\n");
				} else {
					code.append("\ty["+n+"] = (Double) params.getValue(\""+ndtm.makeLegal(initVal)+"\");\n");
				}
			}
			code.append("}\n\n");

			// step method

			code.append("@ScheduledMethod(start = 1,interval = 1,shuffle = true)\n");
			code.append("public void step() {\n");
			code.append("\tintegrator.integrate(ode, 0.0, y, timeDelta, y);\n");
			code.append("}\n\n");


			// standard getters
			code.append("public String getID() {\n");
			code.append("\treturn \""+myClassName+"\";\n");
			code.append("}\n");
			code.append("public "+className+" getOde() {\n");
			code.append("\treturn ode;\n");
			code.append("}\n");

			// getters for stocks

			for (int n = 0; n < analyzer.getNumberODE(); n++) {
				String lhs = analyzer.getStockFor(Integer.toString(n));
				Equation eqn = analyzer.getEquationForLHS(lhs);

				code.append("public double getY"+n+"() {\n");
				code.append("\treturn y["+n+"];\n");
				code.append("}\n\n");

			}
			code.append("}\n");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void generateContextBuilderClass(BufferedWriter code, String myClassName, String runnerName) {
		
		contextName = myClassName;

		NativeDataTypeManager ndtm = InformationManagers.getInstance().getNativeDataTypeManager();

		try {
			code.append("package "+packageName+";\n\n");
			code.append("import repast.simphony.context.Context;\n");
			code.append("import repast.simphony.dataLoader.ContextBuilder;\n");
			code.append("import repast.simphony.engine.environment.RunEnvironment;\n");
			code.append("import repast.simphony.parameter.Parameters;\n\n");

			code.append("public class "+myClassName+" implements ContextBuilder<Object> {\n");
			code.append("\t@Override\n");
			code.append("\tpublic Context<Object> build(Context<Object> context) {\n");
			code.append("\t\t"+runnerName+" ode = new "+runnerName+"();\n");
			code.append("\t\tcontext.setId(\""+className+"\");\n");
			code.append("\t\tcontext.add(ode);\n");
			code.append("\t\tcontext.add(ode.getOde());\n\n");
			code.append("\t\tParameters params = RunEnvironment.getInstance().getParameters();\n");
			code.append("\t\tRunEnvironment.getInstance().endAt((Double) params.getValue(\"FINAL_TIME\"));\n");
			code.append("\t\treturn context;\n");
			code.append("\t}\n");
			code.append("}\n");


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String generateExpression(Node node) {
		StringBuffer sb = new StringBuffer();

		if (node != null) {

			// node is the root node of an expression
			// if an operator:
			// left child
			// operator
			// right child
			if (Parser.isArithmeticOperator(node.getToken()) || Parser.isEqualSign(node.getToken())) {
				if (!Parser.isUnaryOperator(node.getToken())) {
					sb.append(generateExpression(TreeTraversal.getLhs(node))); // node.getChild()
					sb.append(node.getToken());
					sb.append(generateExpression(TreeTraversal.getRhs(node))); // node.getChild().getNext()
				} else {
					sb.append(Parser.translateUnaryOperator(node.getToken()));
					sb.append(generateExpression(node.getChild())); // this is really a LHS location, but RHS of unary
				}
				return sb.toString();

			} else {
				sb.append(node.getToken());
			}

		}
		return sb.toString();
	}

	public void makeLocal(Node node) {
		if (node == null)
			return;
		node.setToken(NativeDataTypeManager.getAsJavaLocalVariable(node.getToken()));
		makeLocal(node.getChild());
		makeLocal(node.getNext());
	}

	public void makeODESolverCompatible(Node node) {
		makeLHSCompatible(TreeTraversal.getLhs(node)); // node.getChild()
		makeRHSCompatible(TreeTraversal.getRhs(node)); // node.getChild().getNext()

	}

	public void makeLHSCompatible(Node node) {
		if (node == null)
			return;
		if (analyzer.isStock(node.getToken())) {
			String index = analyzer.getIndexFor(node.getToken());
			node.setToken("yDot["+index+"]");
		}
	}

	public void makeRHSCompatible(Node node) {
		if (node == null)
			return;
		if (analyzer.isStock(node.getToken())) {
			String index = analyzer.getIndexFor(node.getToken());
			node.setToken("y["+index+"]");
		}
		makeRHSCompatible(node.getChild());
		makeRHSCompatible(node.getNext());
	}

	public Node alterEquationTreeForStock(Equation stockEqn) {

		String stock = stockEqn.getLhs();


		Node stockRoot = stockEqn.getCopyOfTree();   		// root of tree (=)
		Node stockLhs = stockRoot.getChild();
		Node functionNode = stockLhs.getNext();   	// stock rhs -> INTEG		

		Node rateNode = TreeTraversal.getFunctionArgument(functionNode, 1); 	// this is the rate. Assume that it is not an expression

		String origRateName = InformationManagers.getInstance().getNativeDataTypeManager().getOriginalName(rateNode.getToken());
//		System.out.println("I think rate variable is: "+origRateName);

		// now just change some links to get rid of function call and args

		stockLhs.setNext(rateNode);
		rateNode.setPrevious(stockLhs);
		rateNode.setNext(null);

		return stockRoot;


	}

	public void generateScenarioDirectoryFiles(String scenarioDirectory) {
		
		String odeScenarioDirectory = scenarioDirectory.replace(".rs/", "") + "ODE.rs/";
		
		generateContextXml(Translator.openReport(odeScenarioDirectory+"context.xml"), className);
		generateUserPathXml(Translator.openReport(odeScenarioDirectory+"user_path.xml"), className);
		generateClassLoaderXml(Translator.openReport(odeScenarioDirectory+"repast.simphony.dataLoader.engine.ClassNameDataLoaderAction_1.xml"), className);
		generateScenarioXml(Translator.openReport(odeScenarioDirectory+"scenario.xml"), className);
		generateParametersXml(Translator.openReport(odeScenarioDirectory+"parameters.xml"));
		
		// parameters
		
	}
	
	public void generateScenarioXml(BufferedWriter source, String name) {
	     
//	     <?xml version="1.0" encoding="UTF-8" ?>
//	     <Scenario>
//	     <repast.simphony.action.data_set context="EnergySecurity5_0" file="repast.simphony.action.data_set_0.xml" />
//	     <repast.simphony.action.data_set context="EnergySecurity5_0" file="repast.simphony.action.data_set_1.xml" />
//	     <repast.simphony.dataLoader.engine.ClassNameDataLoaderAction context="EnergySecurity5_0" file="repast.simphony.dataLoader.engine.ClassNameDataLoaderAction_2.xml" />
//	     </Scenario>

	     try {
		 source.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		 source.append("<Scenario>\n");
//		 source.append("<repast.simphony.action.data_set context=\""+name+"\" file=\"repast.simphony.action.data_set_0.xml\" />\n");
		 source.append("<repast.simphony.dataLoader.engine.ClassNameDataLoaderAction context=\""+name+"\" " +
			 "file=\"repast.simphony.dataLoader.engine.ClassNameDataLoaderAction_1.xml\" />\n");
		 source.append("</Scenario>\n");
		 source.close();
	     } catch (IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
	     }
	 }
	 
	 public void generateContextXml(BufferedWriter source, String name) {

	     try {

		 source.append("<context id=\""+name+"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		 		"xsi:noNamespaceSchemaLocation=\"http://repast.org/scenario/context\">\n");
		 source.append("</context>\n");


		 source.close();
	     } catch (IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
	     }
	}
	 public void generateClassLoaderXml(BufferedWriter source, String name) {

	     try {

		 source.append("<string>"+packageName+".ContextBuilder"+name+"</string>\n");

		 source.close();
	     } catch (IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
	     }
	}
	 
	 
	 public void generateUserPathXml(BufferedWriter source, String name) {

	     try {
		 source.append("<model name=\""+name+"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		 "xsi:noNamespaceSchemaLocation=\"http://repast.org/scenario/user_path\">\n");
		 source.append("<classpath>\n");
		 source.append("<agents path=\"../bin\" />\n");
		 source.append("<entry path=\"../lib\" />\n");
		 source.append("</classpath>\n");
		 source.append("</model>\n");


		 source.close();
	     } catch (IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
	     }
	}
	 
	 public  void generateParametersXml(BufferedWriter source) {
		 
		 Map<String, String> initialValues = new HashMap<String, String>();
		 
		 Iterator<Equation> iter = analyzer.getEquationIterator();
		 while (iter.hasNext()) {
			 Equation eqn = iter.next();
			 if (eqn.isAssignment() && eqn.isOneTime()) {
				 String[] bothSides = eqn.getEquation().split("=", 2);
				 initialValues.put(InformationManagers.getInstance().getNativeDataTypeManager().getLegalName(bothSides[0].replace("\"", "").trim()), 
						 bothSides[1].replace("\"", "").trim());
			 }
		 }
		 
		 

	     try {

		 source.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		 source.append("<parameters>\n");
		 source.append("<parameter name=\"randomSeed\" displayName=\"Default Random Seed\" type=\"int\"\n");
		 source.append("\tdefaultValue=\"__NULL__\"\n");
		 source.append("\tisReadOnly=\"false\" \n");
		 source.append("\tconverter=\"repast.simphony.parameter.StringConverterFactory$IntConverter\"\n");
		 source.append("/>\n");
		 
		 for (String var : initialValues.keySet()) {
		     String value = initialValues.get(var);
		     String legalVar = InformationManagers.getInstance().getNativeDataTypeManager().makeLegal(var.replace("memory.", ""));
		     
		     source.append("<parameter name=\""+legalVar+"\" displayName=\""+InformationManagers.getInstance().getNativeDataTypeManager().getOriginalName(var)+"\" type=\"double\" \n");
		     source.append("\tdefaultValue=\""+value+"\" \n");
		     source.append("\tisReadOnly=\"false\" \n");
		     source.append("\tconverter=\"repast.simphony.parameter.StringConverterFactory$DoubleConverter\"\n");
		     source.append("/>\n");
		 }

		 
		 source.append("</parameters>\n");
		 source.close();
	     } catch (IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
	     }
	}


}
