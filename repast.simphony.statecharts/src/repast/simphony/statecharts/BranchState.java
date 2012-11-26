package repast.simphony.statecharts;

public class BranchState<T> extends SimpleState<T> {

	protected BranchState(String id) {
		super(id);
	}

	private TransitionResolutionStrategy originalTRS;
	
	protected void initializeBranch(final DefaultStateChart<T> st) {
		registerOnEnter(new StateAction<T>(){
			@Override
			public void action(T agent, AbstractState<T> state)
					throws Exception {
				BranchState.this.originalTRS = st.getTransitionResolutionStrategy();
				st.setTransitionResolutionStrategy(TransitionResolutionStrategy.PRIORITY);
				
			}
		});
		registerOnExit(new StateAction<T>(){
			@Override
			public void action(T agent, AbstractState<T> state)
					throws Exception {
				st.setTransitionResolutionStrategy(BranchState.this.originalTRS);
				
			}
		});
	}

}
