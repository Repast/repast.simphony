package repast.simphony.statecharts;

import repast.simphony.parameter.IllegalParameterException;

public class OutOfBranchTransitionBuilder<T> extends TransitionBuilder<T>{
	
	public OutOfBranchTransitionBuilder(AbstractState<T> source,
			AbstractState<T> target) {
		this("",source,target);
	}
	
	public OutOfBranchTransitionBuilder(String id, AbstractState<T> source,
			AbstractState<T> target) {
		super(id, source, target);
		if (!(source instanceof BranchState)){
			throw new IllegalParameterException("Out of branch transitions require a BranchState as a source.");
		}
	}
	
	@Override
	public void addTrigger(Trigger trigger){
		if (trigger instanceof ConditionTrigger){
			super.addTrigger(trigger);
		}
		else {
			throw new IllegalParameterException("Out of branch transitions require a ConditionTrigger as a trigger.");
		}
	}
	
}