package repast.simphony.systemdynamics.analysis;

/*
 * This is what we will actually generate
 */

public class SamplePolarityCalculator implements PolarityCalculator {
	
	public double compute(double[] rhs) {
		
		double lhs = rhs[0]+rhs[1];
		
		return lhs;
	}

}
