/*CopyrightHere*/
package repast.simphony.engine.graph;

import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.Traverser;

/**
 * Executor for NetworkTopologies. This validates nodes' execution by checking
 * to make sure they are still connected.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class NetworkTopologyExecutor extends AbstractGraphExecutor<Object> {
	private Network topology;

	/**
	 * Constructs this executor to execute using the given schedule and node
	 * execution parameters. This class primarily is interested in the topology
	 * as it uses it for validation, the rest of the parameters are used by the
	 * super class.
	 * 
	 * @see AbstractGraphExecutor#AbstractGraphExecutor(ISchedule,
	 *      ScheduleParameters, Traverser, Executor)
	 * @see #validateForExecution(GraphParams)
	 * 
	 * @param schedule
	 *            the schedule to schedule itself with
	 * @param baseParams
	 *            the parameters for the scheduling
	 * @param traverser
	 *            the traverser for traversing the graph
	 * @param nodeExecutor
	 *            the executor for executing nodes
	 * @param topology
	 *            the topology to use to determine if nodes are connected
	 */
	public NetworkTopologyExecutor(ISchedule schedule,
			ScheduleParameters baseParams, Traverser<Object> traverser,
			Executor<Object> nodeExecutor, Network topology) {
		super(schedule, baseParams, traverser, nodeExecutor);

		this.topology = topology;
	}

	/**
	 * This is the same as NetworkTopologyExecutor(ISchedule,
	 * ScheduleParameters, new TopologyVisitor(topology), Executor,
	 * NetworkTopology).
	 * 
	 * @see #NetworkTopologyExecutor(ISchedule, ScheduleParameters, Traverser,
	 *      Executor, NetworkTopology)
	 * @see AbstractGraphExecutor#AbstractGraphExecutor(ISchedule,
	 *      ScheduleParameters, Traverser, Executor)
	 * @see #validateForExecution(GraphParams)
	 * 
	 * @param schedule
	 *            the schedule to schedule itself with
	 * @param baseParams
	 *            the parameters for the scheduling
	 * @param nodeExecutor
	 *            the executor for executing nodes
	 * @param topology
	 *            the topology to use to determine if nodes are connected
	 */
	public NetworkTopologyExecutor(ISchedule schedule,
			ScheduleParameters scheduleParams, Executor<Object> nodeExecutor,
			Network topology) {
		this(schedule, scheduleParams, new NetworkTraverser(topology),
				nodeExecutor, topology);
	}

	/**
	 * Checks if two nodes are still connected with
	 * {@link NetworkTopology#areIncident(Object, Object).
	 * 
	 * @param params
	 *            the parameters used to get the current node and previous one
	 * @return if the two nodes are still connected according to the topology
	 */
	public boolean validateForExecution(GraphParams<Object> params) {
		// make sure the nodes are still connected
		return topology.isAdjacent(params.getPreviousNode(), params
				.getCurrentNode());
	}
}
