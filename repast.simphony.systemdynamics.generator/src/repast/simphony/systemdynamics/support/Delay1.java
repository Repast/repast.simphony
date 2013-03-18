package repast.simphony.systemdynamics.support;

public class Delay1 extends DelayFunction {
    public Delay1(SDFunctions sdFunctions, String name, double timeStep) {
	super(sdFunctions, name, timeStep);
	// TODO Auto-generated constructor stub
    }

    @Override
    public double getValue(double time, double timeStep, double... args) {
	
	// take our input value
	inflow.add(args[0]);
	
	double input = args[0];
	double delayDuration = args[1];
	double initial = input;
	if (args.length > 2)
	    initial = args[2];
	
	if (stock.size() == 0) {
	    stock.add(initial * delayDuration);
	    inflow.add(input);
	    delayedInput.add(stock.get(stock.size()-1)/delayDuration);
	} else {
	    stock.add(stock.get(stock.size()-1) +timeStep*(inflow.get(inflow.size()-1) - delayedInput.get(delayedInput.size()-1)));
	    inflow.add(input);
	    delayedInput.add(stock.get(stock.size()-1)/delayDuration);
	}

	
	return delayedInput.get(delayedInput.size()-1);
    }
}
