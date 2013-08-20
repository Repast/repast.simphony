package repast.simphony.systemdynamics.support;

import java.util.ArrayList;
import java.util.List;

public class SmoothN extends SmoothFunction {

    protected List<Smooth> cascadedSmooths = new ArrayList<Smooth>();

	public SmoothN(SDFunctions sdFunctions, String name, double timeStep) {
		super(sdFunctions, name, timeStep);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getValue(double time, double timeStep, double... args) {
	    // if this is the first time this is called, must initialized the cascaded
	    // delay1s
	    
	    double input = args[0];
	    double delayTime = args[1];
	    double initial = args[2];
	    double n = args[3];
	    
	    rawValues.add(input);
	    
	    if (cascadedSmooths.size() == 0) {
		for (int i = 0; i < args[3]; i++) {
		    cascadedSmooths.add(new Smooth(sdFunctions, "", timeStep));
		}
	    }
	    
	    double delayedValue = input;
	    
	    for (Smooth d1 : cascadedSmooths) {
		delayedValue = d1.getValue(time, timeStep, delayedValue, delayTime/n, initial);
	    }
	    
	    smoothedValues.add(delayedValue);
	    
	    return delayedValue;
	}

}
