package repast.simphony.systemdynamics.ode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;

import repast.simphony.systemdynamics.translator.Equation;

public class MethodHeader {
	
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
	
		try {
			code.append("\tpublic void computeDerivatives(double t, double[] y, double[] yDot) {\n");			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateRunner(ODECodeGenerator odeCG, BufferedWriter code, ODEAnalyzer analyzer) {
		
	}

}
