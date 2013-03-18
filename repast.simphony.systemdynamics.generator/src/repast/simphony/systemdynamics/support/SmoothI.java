package repast.simphony.systemdynamics.support;

public class SmoothI extends SmoothFunction {

	public SmoothI(SDFunctions sdFunctions, String name, double timeStep) {
		super(sdFunctions, name, timeStep);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getValue(double time, double timeStep, double... args) {
		
		rawValues.add(args[0]);
		if (rawValues.size() < 3) {
		    smoothedValues.add(args[2]);
			return smoothedValues.get(smoothedValues.size()-1);
		} else {
			// s(t) = a*x(i) + (1-a)*s(t-1)
		    	// where a = 1/(timestep/arg2)
		    	double a = 1.0/(timeStep/args[1]);
		    	double smooth = a*rawValues.get(rawValues.size()-2) + (1.0-a) * smoothedValues.get(smoothedValues.size()-1);
		    	smoothedValues.add(smooth);
		    	return smooth;
		}
	}

}
