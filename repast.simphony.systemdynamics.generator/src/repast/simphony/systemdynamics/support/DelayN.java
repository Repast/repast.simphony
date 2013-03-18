package repast.simphony.systemdynamics.support;

import java.util.ArrayList;
import java.util.List;

public class DelayN extends DelayFunction {
    
    protected List<Delay1> cascadedDelays = new ArrayList<Delay1>();

	public DelayN(SDFunctions sdFunctions, String name, double timeStep) {
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

	    inflow.add(input);
	    
	    if (cascadedDelays.size() == 0) {
		for (int i = 0; i < args[3]; i++) {
		    cascadedDelays.add(new Delay1(sdFunctions, "DelayN_"+i, timeStep));
		}
	    }
	    
	    double delayedValue = input;
	    
	    for (Delay1 d1 : cascadedDelays) {
		delayedValue = d1.getValue(time, timeStep, delayedValue, delayTime/n, initial);
	    }
	    
	    delayedInput.add(delayedValue);
	    
	    return delayedValue;
	}

}
