package repast.simphony.systemdynamics.ode;

import java.io.BufferedWriter;
import java.io.IOException;

public class ObjectHeader {
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
		try {
			code.append("package "+packageName+";\n\n");
			code.append("// import whatever is necessary;\n\n");
			code.append("private static class "+className+" implements FirstOrderDifferentialEquations {\n\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
