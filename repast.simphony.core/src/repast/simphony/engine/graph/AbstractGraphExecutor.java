/*CopyrightHere*/
package repast.simphony.engine.graph;

import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.graph.Traverser;
import repast.simphony.util.SimUtilities;

import java.util.Iterator;

/**
 * Base implementation of a GraphExecutor. Subclasses of this executor should
 * provide a validation method and this class will perform the execution of the
 * graph.<p/>
 * 
 * @see repast.simphony.engine.graph.GraphExecutor
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public abstract class AbstractGraphExecutor<E> implements GraphExecutor<E> {
	private Traverser<E> traverser;

	private Executor<E> nodeExecutor;

	private ISchedule schedule;

	private ScheduleParameters baseParams;

	/**
	 * Constructs this graph executor to work with the specified parameters.
	 * This GraphExecutor will visit the nodes with the specified visitor,
	 * execute them with the given node executor and reschedule itself according
	 * to baseParams.
	 * 
	 * @param schedule
	 *            the schedule this graph executor will use to reschedule itself
	 * @param baseParams
	 *            the parameters to base its scheduling on
	 * @param traverser
	 *            the visitor for finding successors to nodes
	 * @param nodeExecutor
	 *            the executor for executing the nodes
	 */
	public AbstractGraphExecutor(ISchedule schedule,
			ScheduleParameters baseParams, Traverser<E> traverser,
			Executor<E> nodeExecutor) {
		super();

		this.schedule = schedule;
		this.baseParams = baseParams;
		this.traverser = traverser;
		this.nodeExecutor = nodeExecutor;
	}

	/**
	 * Performs execution of the graph. This involves validating the params for
	 * execution, executing the current node with the given node executor, then
	 * scheduling this to perform execution on the current node's successors.
	 * 
	 * @param params
	 *            parameters on the current node to be executed
	 */
	@ScheduledMethod
	public void execute(GraphParams<E> params) {
		// if we're at the start (root node) we do it,
		// otherwise validate it
		if (params.getPreviousNode() == null || validateForExecution(params)) {
			nodeExecutor.execute(params.getCurrentNode());

			Iterator<E> iter = traverser.getSuccessors(
					params.getPreviousNode(), params.getCurrentNode());
			for (; iter.hasNext();) {
				E successor = iter.next();

				double totalDist = getNextTime(params, successor);

				ScheduleParameters scheduleParams = getScheduleParameters(totalDist);

				GraphParams<E> graphParams = new GraphParams<E>(params
						.getCurrentNode(), successor, totalDist - getRootTime());

				schedule.schedule(scheduleParams, this, graphParams);
			}
		}
	}

	protected ScheduleParameters getScheduleParameters(double time) {
		return GraphScheduleUtilities.getSimilarOneTimeParams(baseParams, SimUtilities
				.scale(time, 7));
	}

	protected double getNextTime(GraphParams<E> params, E nextNode) {
		return getRootTime()
				+ params.getPreviousDistance()
				+ traverser.getDistance(params.getCurrentNode(), nextNode);
	}

	protected double getRootTime() {
		return baseParams.getStart();
	}
}
