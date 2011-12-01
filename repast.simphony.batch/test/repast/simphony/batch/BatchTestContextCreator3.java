package repast.simphony.batch;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.graph.Network;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BatchTestContextCreator3 implements ContextBuilder<BatchTestAgent3> {

	public Context build(Context<BatchTestAgent3> context) {
    Network network = NetworkFactoryFinder.createNetworkFactory(null).createNetwork(
						"A Network", context, true);
		BatchTestAgent3 prev = null;
		BatchTestAgent3 first = null;
		for (int i = 0; i < BatchTest.NUM_AGENTS; i++) {
			BatchTestAgent3 agent = new BatchTestAgent3(String.valueOf(i),
					BatchTest.results);
			context.add(agent);
			if (first == null) {
				first = agent;
			}
			if (prev != null) {
				network.addEdge(prev, agent);
			}
			prev = agent;
		}
		network.addEdge(prev, first);

		repast.simphony.engine.environment.RunEnvironment.getInstance().endAt(BatchTest.END_AT);
		return context;
	}

	/**
	 * Loads the specified context with its data (subcontexts, objects,
	 * parameters and so forth). This should be used when the Context has
	 * already been created by its parent Context.
	 * 
	 * @param context
	 *            the context to load
	 */
	public void load(Context context) {
		// todo implement method
	}
}
