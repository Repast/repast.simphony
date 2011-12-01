/*$$
 * Copyright (c) 2007, Argonne National Laboratory
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with 
 * or without modification, are permitted provided that the following 
 * conditions are met:
 *
 *	 Redistributions of source code must retain the above copyright notice,
 *	 this list of conditions and the following disclaimer.
 *
 *	 Redistributions in binary form must reproduce the above copyright notice,
 *	 this list of conditions and the following disclaimer in the documentation
 *	 and/or other materials provided with the distribution.
 *
 * Neither the name of the Repast project nor the names the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *$$*/
package repast.simphony.engine.graph;


import repast.simphony.engine.schedule.Frequency;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.graph.Network;
import simphony.util.messages.MessageCenter;

/**
 * Utilities for working with the schedule.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public class GraphScheduleUtilities {
	private static final MessageCenter l4jLogger = MessageCenter
			.getMessageCenter(GraphScheduleUtilities.class);

	/**
	 * Schedules the execution of a graph starting with the specified root node.
	 * The base parameters object is used to schedule the execution of the
	 * primary node, other nodes' execution should be based on these parameters
	 * but should not be scheduled as repeating actions. The base node itself
	 * can be scheduled as a repeating action, allowing for repeated execution
	 * of the graph.<p/>
	 * 
	 * @param schedule
	 *            the schedule to schedule the graph to
	 * @param baseParams
	 *            the parameters for scheduling the root node and a basis for
	 *            the scheduling of the following nodes
	 * @param rootNode
	 *            the node to start execution on
	 * @param executor
	 *            the action that will execute the graph
	 * @return the action scheduled for the root node
	 */
	public static <E> ISchedulableAction scheduleGraph(ISchedule schedule,
			ScheduleParameters baseParams, Object rootNode,
			Executor<GraphParams<E>> graphExecutor) {

		return schedule.schedule(baseParams, graphExecutor,
				new GraphParams<Object>(null, rootNode, 0));
	}

	/**
	 * Schedules a Repast network to be executed. This is functions the same as
	 * {@link #schedule(ScheduleParameters, Object, GraphExecutor)} but
	 * automatically builds the GraphExecutor.
	 * 
	 * @see #schedule(ScheduleParameters, Object, GraphExecutor)
	 * 
	 * @param schedule
	 *            the schedule to schedule the graph to
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
	public static ISchedulableAction scheduleGraph(ISchedule schedule,
			ScheduleParameters baseParams, Object rootNode,
			Network topology, Executor<Object> nodeExecutor) {

		return scheduleGraph(
				schedule,
				baseParams,
				rootNode,
				new NetworkTopologyExecutor(schedule, baseParams,
						new NetworkTraverser(topology), nodeExecutor, topology));
	}

	/**
	 * Creates a similar ScheduleParameters object with the same properties as
	 * the given base, but with a different time.
	 * 
	 * @param baseParameters
	 *            the base for finding the next parameters
	 * @param newTime
	 *            the time for the returned object
	 * @return a ScheduleParameters object with time, newTime
	 */
	public static ScheduleParameters getSimilarParams(
			ScheduleParameters baseParameters, double newTime) {
		return getSimilarParams(baseParameters, newTime, baseParameters
				.getFrequency(), baseParameters.getInterval());
	}

	/**
	 * Creates a similar ScheduleParameters object with the same properties as
	 * the given base, but with a different time.
	 * 
	 * @param baseParameters
	 *            the base for finding the next parameters
	 * @param newTime
	 *            the time for the returned object
	 * @return a ScheduleParameters object with time, newTime
	 */
	public static ScheduleParameters getSimilarOneTimeParams(
			ScheduleParameters baseParameters, double newTime) {
		return getSimilarParams(baseParameters, newTime, Frequency.ONE_TIME, 0);
	}

	/**
	 * Creates a similar ScheduleParameters object with the same properties as
	 * the given base, but with a different time.
	 * 
	 * @param baseParameters
	 *            the base for finding the next parameters
	 * @param newTime
	 *            the time for the returned object
	 * @param newFrequency
	 *            the Frequency for the returned object
	 * @param newInterval
	 *            the interval for the returned object
	 * @return a ScheduleParameters object with time, newTime, Frequency,
	 *         newFrequency, and interval, newInterval
	 */
	public static ScheduleParameters getSimilarParams(
			ScheduleParameters baseParameters, double newTime,
			Frequency frequency, double interval) {
		switch (frequency) {
		case ONE_TIME:
			return ScheduleParameters.createOneTime(newTime, baseParameters
					.getPriority(), baseParameters.getDuration());
		case REPEAT:
			return ScheduleParameters.createRepeating(baseParameters.getStart()
					+ newTime, interval, baseParameters.getPriority(),
					baseParameters.getDuration());

		default:
			l4jLogger.warn("Utilities.getNextScheduleParameters: Unknown "
					+ "Frequency type found when attempting to get "
					+ "next schedule parameters.");
			return null;
		}
	}
}
