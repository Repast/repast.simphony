package repast.simphony.batch;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BatchTestContextCreator implements ContextBuilder<BatchTestAgent> {

	public Context build(Context<BatchTestAgent> context) {
		for (int i = 0; i < BatchTest.NUM_AGENTS; i++) {
			context.add(new BatchTestAgent(String.valueOf(i), BatchTest.results));
		}

		repast.simphony.engine.environment.RunEnvironment.getInstance().endAt(BatchTest.END_AT);
		return context;
	}
}
