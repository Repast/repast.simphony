package repast.simphony.statecharts;

import repast.simphony.parameter.IllegalParameterException;

public class DefaultOutOfBranchTransitionBuilder<T> extends
		TransitionBuilder<T> {

	public DefaultOutOfBranchTransitionBuilder(AbstractState<T> source,
			AbstractState<T> target) {
		super(source, target);
	}

	@Override
	public void addTrigger(Trigger trigger) {
		throw new IllegalParameterException(
				"Default out of branch transitions do not have triggers.");
	}

}