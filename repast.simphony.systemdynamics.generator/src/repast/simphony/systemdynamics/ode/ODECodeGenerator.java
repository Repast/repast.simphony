package repast.simphony.systemdynamics.ode;

import java.io.BufferedWriter;
import java.util.Map;

import repast.simphony.systemdynamics.support.ArrayReference;
import repast.simphony.systemdynamics.translator.ArrayReferenceNative;
import repast.simphony.systemdynamics.translator.Equation;
import repast.simphony.systemdynamics.translator.InformationManagers;
import repast.simphony.systemdynamics.translator.NativeDataTypeManager;
import repast.simphony.systemdynamics.translator.Node;
import repast.simphony.systemdynamics.translator.Parser;

public class ODECodeGenerator {
	
	private ODEAnalyzer analyzer;
	private String className;
	private String packageName;
	
	public ODECodeGenerator(Map<String, Equation> equations, String packageName, String className) {
		this.packageName = packageName;
		this.className = className;
		analyzer = new ODEAnalyzer(equations);
		analyzer.analyze();
	}
	
	public void generate(BufferedWriter code) {
		
		ObjectHeader.generate(this, code, analyzer, packageName, className);
		Constructor.generate(this, code, analyzer, packageName, className);
		MethodHeader.generate(this, code, analyzer);
		MethodCalculations.generate(this, code, analyzer);
		MethodFooter.generate(this, code, analyzer);
		SetterGetter.generate(this, code, analyzer);
		ObjectFooter.generate(this, code, analyzer);
		
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
				sb.append(generateExpression(node.getChild()));
				sb.append(node.getToken());
				sb.append(generateExpression(node.getChild().getNext()));
				} else {
					sb.append(Parser.translateUnaryOperator(node.getToken()));
					sb.append(generateExpression(node.getChild()));
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
		makeLHSCompatible(node.getChild());
		makeRHSCompatible(node.getChild().getNext());
		
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
	
	public void alterEquationTreeForStock(Equation stockEqn) {
		
		String stock = stockEqn.getLhs();
		System.out.println("##### Experiment "+stock+" #####");
		
//		Equation stockEqn = analyzer.getEquations().get(stock);
		Node stockRoot = stockEqn.getTreeRoot();   		// root of tree (=)
		Node stockLhs = stockRoot.getChild();
		Node stockRhs = stockLhs.getNext();   	// stock rhs -> INTEG
		Node arg3 = stockRhs.getChild().getNext().getNext().getNext();
		Node arg4 = arg3.getNext(); 	// this is the rate. Assume that it is not an expression
		Node arg5 = arg4.getNext();
		String origRateName = InformationManagers.getInstance().getNativeDataTypeManager().getOriginalName(arg4.getToken());
		System.out.println("I think rate variable is: "+origRateName);
		
		// now just change some links to get rid of function call and args
		
		stockLhs.setNext(arg4);
		arg4.setPrevious(stockLhs);
		arg4.setNext(null);

		
}




}
