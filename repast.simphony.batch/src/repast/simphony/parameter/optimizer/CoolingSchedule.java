/*CopyrightHere*/
/**
 * 
 */
package repast.simphony.parameter.optimizer;

public interface CoolingSchedule {
	double init(double initialTemp);
	double cool();
}