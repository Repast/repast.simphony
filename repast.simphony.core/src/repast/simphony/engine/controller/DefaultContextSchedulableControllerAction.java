/*CopyrightHere*/
package repast.simphony.engine.controller;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.iterators.FilterIterator;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ContextSchedulableDescriptor;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;
import repast.simphony.util.collections.IterableAdaptor;

/**
 * An action that takes a {@link repast.simphony.engine.schedule.ContextSchedulableDescriptor}
 * and schedules it using
 * {@link ISchedule#scheduleIterable(ScheduleParameters, Iterable, String, boolean, Object...)}. The
 * scheduled Iterable will be a FilterIterable that takes in the context and the descriptor's
 * filter. The method name will be that returned by the descriptor.
 * 
 * @author Jerry Vos
 */
public class DefaultContextSchedulableControllerAction<T> extends NullAbstractControllerAction
		implements DescriptorControllerAction<ContextSchedulableDescriptor<T>> {
	private ContextSchedulableDescriptor<T> descriptor;

	public DefaultContextSchedulableControllerAction(ContextSchedulableDescriptor<T> descriptor) {
		super();

		this.descriptor = descriptor;
	}

	public ContextSchedulableDescriptor<T> getDescriptor() {
		return descriptor;
	}

	/**
	 * Schedules on the model schedule the descriptor's iterable with it executing the descriptor's
	 * method name.
	 * 
	 * @param runState
	 *            the RunState to fetch the model schedule from
	 * @param context
	 *            the context that will be used to filter agents from
	 */
	@Override
	public void runInitialize(RunState runState, Context context, Parameters runParams) {
		ISchedule schedule = runState.getScheduleRegistry().getModelSchedule();

		schedule.scheduleIterable(descriptor.getScheduleParameters(), getContextFilter(context,
				descriptor.getFilter()), descriptor.getMethodName(), descriptor.getShuffle());
	}

	protected Iterable getContextFilter(Context context,
	                                           Predicate filter) {
		return new IterableAdaptor(new FilterIterator(context.iterator(), filter));
	}
}
