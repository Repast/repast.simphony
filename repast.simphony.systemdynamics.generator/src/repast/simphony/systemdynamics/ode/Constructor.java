package repast.simphony.systemdynamics.ode;

import java.io.BufferedWriter;
import java.io.IOException;

import repast.simphony.systemdynamics.translator.Equation;
import repast.simphony.systemdynamics.translator.InformationManagers;
import repast.simphony.systemdynamics.translator.NativeDataTypeManager;

public class Constructor {
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
	
	public static void generate(ODECodeGenerator odeCG, BufferedWriter code, ODEAnalyzer analyzer, String packageName, String className) {
		NativeDataTypeManager ndtm = InformationManagers.getInstance().getNativeDataTypeManager();
		try {
			// declare all auxiliaries
			// if this is a constant, put it in
			for (Equation eqn : analyzer.getAuxiliaries()) {
				if (eqn.isOneTime()) {
					code.append("\tprivate double "+ndtm.makeLegal(eqn.getLhs())+" = "+eqn.getRhs()+";\n");
				} else {
					code.append("\tprivate double "+ndtm.makeLegal(eqn.getLhs())+";\n");
				}
			}
			
			code.append("\tprivate ODEFunctionSupport sdFunctions = new ODEFunctionSupport();\n\n");
			
			// generate empty constructor
			code.append("\n\tpublic "+className+"() {\n\t}");

			// generate constructor 
			code.append("\n\tpublic "+className+"(");
			int i = 0;
			for (Equation eqn : analyzer.getAuxiliariesForConstructor()) {
				if (i++ > 0)
					code.append(", ");
				code.append("double "+ndtm.makeLegal(eqn.getLhs()));
			}
			code.append(") {\n\n");
			for (Equation eqn : analyzer.getAuxiliariesForConstructor()) {
				code.append("\tthis."+ndtm.makeLegal(eqn.getLhs())+" = "+ndtm.makeLegal(eqn.getLhs())+";\n");
			}
			code.append("\t}\n\n");

			code.append("\t public int getDimension() {\n\t\treturn "+analyzer.getNumberODE()+";\n\t}\n\n");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
