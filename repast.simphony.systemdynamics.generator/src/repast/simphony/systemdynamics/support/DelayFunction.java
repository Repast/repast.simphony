package repast.simphony.systemdynamics.support;

import java.util.ArrayList;

abstract public class DelayFunction {
	
	ArrayList<Double> inflow;
	ArrayList<Double> stock;
	ArrayList<Double> delayedInput;
	protected String name;
	
	protected SDFunctions sdFunctions;
	
	
	public DelayFunction(SDFunctions sdFunctions, String name, double timeStep) {
		this.sdFunctions = sdFunctions;
	    this.name = name;
		inflow = new ArrayList<Double>();
		stock = new ArrayList<Double>();
		delayedInput = new ArrayList<Double>();
		
	}
	
	public abstract double getValue(double time, double timeStep, double... arg1);

}
