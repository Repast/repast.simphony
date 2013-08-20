/**
 * 
 */
package repast.simphony.systemdynamics.support;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author bragen
 *
 */
public class DelayFixed extends DelayFunction {
    
    Queue<Double> queue;
    
    public DelayFixed(SDFunctions sdFunctions, String name, double timeStep, double delay, double initialValue) {
	super(sdFunctions, name, timeStep);
	queue = new LinkedList<Double>();
	int delayTime = (int) (delay/timeStep);
	for (int t = 0; t < delayTime; t++)
	    queue.add(initialValue);
    }

    @Override
    public double getValue(double time, double timeStep, double... args) {
	
	queue.add(args[0]);

	return queue.remove();
    }
}
