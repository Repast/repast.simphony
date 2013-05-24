package repast.simphony.systemdynamics.ode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import repast.simphony.systemdynamics.translator.Equation;
import repast.simphony.systemdynamics.translator.InformationManagers;
import repast.simphony.systemdynamics.translator.NativeDataTypeManager;
import repast.simphony.systemdynamics.translator.Node;

public class MethodCalculations {
	
//	private static class CircleODE implements FirstOrderDifferentialEquations {
//
//	    private double[] c;
//	    private double omega;
//
//	    public CircleODE(double[] c, double omega) {
//	        this.c     = c;
//	        this.omega = omega;
//	    }
//
//	    public int getDimension() {
//	        return 2;
//	    }
//
//	    public void computeDerivatives(double t, double[] y, double[] yDot) {
//	        yDot[0] = omega * (c[1] - y[1]);
//	        yDot[1] = omega * (y[0] - c[0]);
//	    }
//
//	}
	
	public static void generate(ODECodeGenerator odeCG, BufferedWriter code, ODEAnalyzer analyzer) {
		List<Equation> equations;
		
		// first generate any auxiliary variable assignments (not constants)
		
		equations = analyzer.getEquationIterator();
		
		
		try {
			
			code.append("\n\t// auxiliary assignments\n\n");
			
			for (Equation eqn : equations) {
				if (eqn.isStock())
					continue;
				if (eqn.isOneTime())
					continue;
				Node root = eqn.getTreeRoot();
				odeCG.makeLocal(root);
				odeCG.makeODESolverCompatible(root);
				code.append("\t// "+eqn.getVensimEquationOnly()+"\n");
				code.append("\t"+odeCG.generateExpression(root)+";\n\n");
			}
			
//			equations = analyzer.getEquationIterator();
			code.append("\n\t// \"stock\" delta assignments\n\n");
			for (Equation eqn : equations) {
				if (!eqn.isStock())
					continue;
				
				
				
				Node root = odeCG.alterEquationTreeForStock(eqn);
				odeCG.makeLocal(root);
				odeCG.makeODESolverCompatible(root);
				code.append("\t// "+eqn.getVensimEquationOnly()+"\n");
				code.append("\t"+odeCG.generateExpression(root)+";\n\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
