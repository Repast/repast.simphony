/*CopyrightHere*/
package repast.simphony.freezedry.freezedryers.proj;

import java.util.ArrayList;
import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.DefaultNetworkFactory;
import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.space.graph.DirectedJungNetwork;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.graph.UndirectedJungNetwork;
import simphony.util.messages.MessageCenter;

/**
 * A class that handles Networks. This uses the
 * {@link repast.simphony.context.space.graph.DefaultNetworkFactory} to create directed
 * or undirected networks (based on the network's type). This stores the
 * directedness of the network and its edges (including the edge weights).
 * 
 * @author Jerry Vos
 */
public class NetworkProjectionDryer extends ProjectionDryer<Network<?>> {
	public static final String DIRECTED_KEY = "directed";

	public static final String EDGES_KEY = "edges";

	private static final MessageCenter LOG = MessageCenter
	.getMessageCenter(NetworkProjectionDryer.class);

	/**
	 * Stores the network's directedness and edges (each edges as a tuple of
	 * (source, target, weight)).
	 * 
	 * @param context
	 *            the context the network is a member of
	 * @param net
	 *            the network itself
	 * @param map
	 *            the destination for the properties
	 */
	@Override
	protected void addProperties(Context<?> context, Network<?> net,
			Map<String, Object> map) {
		map.put(DIRECTED_KEY, net.isDirected());

		ArrayList<Object[]> edges = new ArrayList<Object[]>();
		for (RepastEdge edge : net.getEdges()) {
			edges.add(getEdgeProps(edge));
		}
		map.put(EDGES_KEY, edges.toArray());
	}

	protected Object[] getEdgeProps(RepastEdge edge) {
		Object[] edgeProps = new Object[3];
		edgeProps[0] = edge.getSource();
		edgeProps[1] = edge.getTarget();
		edgeProps[2] = edge.getWeight();

		return edgeProps;
	}

	/**
	 * This instantiates the network using the {@link DefaultNetworkFactory},
	 * based on the network's name, and directedness (determined from the
	 * properties).
	 * 
	 * @param context
	 *            the context the network is a part of
	 * @param properties
	 *            the properties of the network
	 */
	@Override
	public Network<?> instantiate(Context<?> context,
			Map<String, Object> properties) {

    Network net;
		if ((Boolean) properties.get(DIRECTED_KEY)) {
			net = new ContextJungNetwork(new DirectedJungNetwork(properties.get(NAME_KEY).toString()), context);
		} else {
			net = new ContextJungNetwork(new UndirectedJungNetwork(properties.get(NAME_KEY).toString()), context);
		}
    return net;
	}

	/**
	 * Loads in the network's edges.
	 * 
	 * @param context
	 *            ignored
	 * @param proj
	 *            the network edges will be loaded into
	 * @param properties
	 *            the properties for the network
	 */
	@Override
	protected void loadProperties(Context<?> context, Network<?> proj,
			Map<String, Object> properties) {
		super.loadProperties(context, proj, properties);

		Object[] edges = (Object[]) properties.get(EDGES_KEY);
		if (edges == null) {
			LOG.info("The list of edges for context '" + context
					+ "' resolved to null, not adding any edges");
			return;
		}

		loadEdges(edges, proj);
	}

	protected void loadEdges(Object[] edges, Network proj){
		for (Object o : edges) {
			if (o instanceof Object[]) {
				Object[] edgeProps = (Object[]) o;
				proj.addEdge(edgeProps[0], edgeProps[1], (Double) edgeProps[2]);
			} else {
				LOG.warn("Edge properties '" + o
						+ "' did not resolve to an Object[], edge skipped.");
			}
		}

	}

	/**
	 * Returns true when the type could be casted to {@link Network}.
	 * 
	 * @see Class#isAssignableFrom(java.lang.Class)
	 */
	@Override
	public boolean handles(Class<?> type) {
		return Network.class.isAssignableFrom(type);
	}
}