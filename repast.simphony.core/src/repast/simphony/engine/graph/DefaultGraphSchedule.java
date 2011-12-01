/*CopyrightHere*/
package repast.simphony.engine.graph;

import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedulableActionFactory;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.graph.Network;

/**
 * Base implementation of a graph schedule running on top of the default Repast
 * schedule.<p/>
 * 
 * This class delegates the scheduling to {@link repast.simphony.engine.graph.GraphScheduleUtilities}.
 * The ScheduleUtilities class actually performs the scheduling of the root node
 * execution.
 * 
 * @see repast.simphony.engine.graph.IGraphSchedule
 * @see repast.simphony.engine.schedule.Schedule
 * 
 * @author Jerry Vos
 */
public class DefaultGraphSchedule extends Schedule implements IGraphSchedule {
	private static final long serialVersionUID = -3655829174664815464L;

	public DefaultGraphSchedule() {
		super();
	}

	public DefaultGraphSchedule(ISchedulableActionFactory factory) {
		super(factory);
	}

	/**
	 * Schedules the execution of a graph starting with the specified root node.
	 * The base parameters object is used to schedule the execution of the
	 * primary node, other nodes' execution should be based on these parameters
	 * but should not be scheduled as repeating actions. The base node itself
	 * can be scheduled as a repeating action, allowing for repeated execution
	 * of the graph.<p/>
	 * 
	 * @param baseParams
	 *            the parameters for scheduling the root node and a basis for
	 *            the scheduling of the following nodes
	 * @param rootNode
	 *            the node to start execution on
	 * @param executor
	 *            the action that will execute the graph
	 * @return the action scheduled for the root node
	 */
	public ISchedulableAction schedule(ScheduleParameters baseParams,
			Object rootNode, GraphExecutor<?> executor) {
		return GraphScheduleUtilities.scheduleGraph(this, baseParams, rootNode,
				executor);
	}

	/**
	 * Schedules a Repast network to be executed. This is functions the same as
	 * {@link #schedule(ScheduleParameters, Object, GraphExecutor)} but
	 * automatically builds the GraphExecutor.
	 * 
	 * @see #schedule(ScheduleParameters, Object, GraphExecutor)
	 * 
	 * @param baseParams
	 *            the parameters for scheduling the root node and a basis for
	 *            the scheduling of the following nodes
	 * @param rootNode
	 *            the node to start execution on
	 * @param topology
	 *            the topology to execute
	 * @param nodeExecutor
	 *            the action that will execute nodes in the graph
	 * @return the action scheduled for executing the root node
	 */
	public ISchedulableAction schedule(ScheduleParameters baseParams,
			Object rootNode, Network topology,
			Executor<Object> nodeExecutor) {
		return GraphScheduleUtilities.scheduleGraph(this, baseParams, rootNode,
				topology, nodeExecutor);
	}
}
