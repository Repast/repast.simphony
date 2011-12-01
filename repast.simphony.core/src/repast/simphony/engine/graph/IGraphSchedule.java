/*CopyrightHere*/
package repast.simphony.engine.graph;

import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.graph.Network;

/**
 * A schedule that will schedule graphs. This currently is implemented on top of
 * the general Repast schedule, with the nodes being executed at times according
 * to their distance between each other.
 * 
 * @see repast.simphony.engine.graph.DefaultGraphSchedule
 * 
 * @author Jerry Vos
 */
public interface IGraphSchedule extends ISchedule {
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
	ISchedulableAction schedule(ScheduleParameters baseParams, Object rootNode,
			GraphExecutor<?> executor);

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
	ISchedulableAction schedule(ScheduleParameters baseParams, Object rootNode,
			Network topology, Executor<Object> nodeExecutor);
}
