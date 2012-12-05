package repast.simphony.statecharts;

import repast.simphony.parameter.IllegalParameterException;

public class DefaultOutOfBranchTransitionBuilder<T> extends
		TransitionBuilder<T> {

	public DefaultOutOfBranchTransitionBuilder(AbstractState<T> source,
			AbstractState<T> target) {
		this("",source,target);
	}
	
	public DefaultOutOfBranchTransitionBuilder(String id, AbstractState<T> source,
			AbstractState<T> target) {
		super(id, source, target);
		if (!(source instanceof BranchState)){
			throw new IllegalParameterException("Default out of branch transitions require a BranchState as a source.");
		}
	}

	@Override
	public void addTrigger(Trigger trigger) {
		throw new IllegalParameterException(
				"Default out of branch transitions do not have triggers.");
	}

}