package repast.simphony.batch;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.RunResultProducer;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class TestRunResultProducer implements RunResultProducer {

	public static final int xCenter = 20;
	public static final int yCenter = 25;

	public double getRunValue(RunState runState) {
		Parameters params = RunEnvironment.getInstance().getParameters();

		double x = (Integer) params.getValue("x") - xCenter;
		double y = (Integer) params.getValue("y") - yCenter;

		return -(x * x + y * y);
	}
}
