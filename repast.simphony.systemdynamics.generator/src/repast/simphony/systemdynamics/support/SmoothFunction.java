package repast.simphony.systemdynamics.support;

import java.util.ArrayList;

abstract public class SmoothFunction {
	
	ArrayList<Double> rawValues;
	ArrayList<Double> changeInSmooth;
	ArrayList<Double> smoothedValues;
	protected String name;
	
	protected SDFunctions sdFunctions;
	
	public SmoothFunction(SDFunctions sdFunctions, String name, double timeStep) {

	    this.sdFunctions = sdFunctions;
	    this.name = name;
	    rawValues = new ArrayList<Double>();
	    changeInSmooth = new ArrayList<Double>();
	    smoothedValues = new ArrayList<Double>();
	    
	}
	
	public abstract double getValue(double time, double timeStep, double... arg2);

}
