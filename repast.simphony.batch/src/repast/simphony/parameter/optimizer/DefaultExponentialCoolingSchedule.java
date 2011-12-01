/*CopyrightHere*/
/**
 * 
 */
package repast.simphony.parameter.optimizer;

public class DefaultExponentialCoolingSchedule implements CoolingSchedule {
	public static final double DEFAULT_COOLING_RATE = .9;
	
	protected double temp;
	
	protected double coolingRate;

	public double init(double initialTemp) {
		coolingRate = DEFAULT_COOLING_RATE;
		return temp = initialTemp;
	}

	public double cool() {
		temp = Math.exp(-coolingRate) * temp;
		return temp;
	}
	
}