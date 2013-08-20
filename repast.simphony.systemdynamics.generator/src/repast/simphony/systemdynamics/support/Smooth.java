package repast.simphony.systemdynamics.support;

public class Smooth extends SmoothFunction {

	public Smooth(SDFunctions sdFunctions, String name, double timeStep) {
		super(sdFunctions, name, timeStep);
		// TODO Auto-generated constructor stub
	}

	public double getValue(double time, double timeStep, double... args) {
	    
	   rawValues.add(args[0]);
	    
	    double smoothOfInput;
	    double input = args[0];
	    double averagingTime = args[1];
	    double initial = rawValues.get(0);
	    if (args.length > 2) {
		initial = args[2];
	    }
		
		 
	    if (smoothedValues.size() == 0) {
		// we need to define the initial value of the stock
		
		smoothedValues.add(initial);
//		changeInSmooth.add((input - initial)/averagingTime);
		changeInSmooth.add(0.0);
		
		return smoothedValues.get(smoothedValues.size()-1);
	    } else {
		smoothOfInput = smoothedValues.get(smoothedValues.size()-1);
		
		smoothOfInput = smoothOfInput + changeInSmooth.get(changeInSmooth.size()-1) * timeStep;
		changeInSmooth.add((input - smoothOfInput)/averagingTime);
		smoothedValues.add(smoothOfInput);
		return smoothOfInput;
	    }
	}

}
