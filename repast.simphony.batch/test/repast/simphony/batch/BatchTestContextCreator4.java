package repast.simphony.batch;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BatchTestContextCreator4 implements ContextBuilder<BatchTestAgent4> {


	public Context build(Context<BatchTestAgent4> context) {
		context.add(new BatchTestAgent4(BatchTest.paramResults));
		return context;
	}
}
