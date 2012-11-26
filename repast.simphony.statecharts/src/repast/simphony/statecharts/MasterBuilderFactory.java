package repast.simphony.statecharts;

public class MasterBuilderFactory<T> {

	public StateChartBuilder<T> createStateChartBuilder(AbstractState<T> entryState){
		return new StateChartBuilder<T>(entryState);
	}
	
	public TransitionBuilder<T> createTransitionBuilder(AbstractState<T> source, AbstractState<T> target){
		return new TransitionBuilder<T>(source,target);
	}
	
	public SelfTransitionBuilder<T> createSelfTransitionBuilder(AbstractState<T> state){
		return new SelfTransitionBuilder<T>(state);
	}

	public OutOfBranchTransitionBuilder<T> createOutOfBranchTransitionBuilder(AbstractState<T> source, AbstractState<T> target){
		return new OutOfBranchTransitionBuilder<T>(source,target);
	}
	
	public DefaultOutOfBranchTransitionBuilder<T> createDefaultOutOfBranchTransitionBuilder(AbstractState<T> source, AbstractState<T> target){
		return new DefaultOutOfBranchTransitionBuilder<T>(source,target);
	}
	
	public SimpleStateBuilder<T> createSimpleStateBuilder(String id){
		return new SimpleStateBuilder<T>(id);
	}
	
	public CompositeStateBuilder<T> createCompositeStateBuilder(String id, AbstractState<T> entryState){
		return new CompositeStateBuilder<T>(id,entryState);
	}
	
	public BranchStateBuilder<T> createBranchStateBuilder(String id){
		return new BranchStateBuilder<T>(id);
	}
	
	public HistoryStateBuilder<T> createHistoryStateBuilder(String id,boolean shallow){
		return new HistoryStateBuilder<T>(id,shallow);
	}
	
	public FinalStateBuilder<T> createFinalStateBuilder(String id){
		return new FinalStateBuilder<T>(id);
	}
}
