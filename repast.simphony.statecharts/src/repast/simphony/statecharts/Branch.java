package repast.simphony.statecharts;

import java.util.List;

public class Branch<T> extends SimpleState<T> {

	private Branch(String id) {
		super(id);
	}

	private TransitionResolutionStrategy originalTRS;
	
	private List<OutOfBranchTransition<T>> tos;
	private IntoBranchTransition<T> intoBranchTransition;
	


	public void initializeBranch(final StateChart<T> st) {
		registerOnEnter(new StateAction<T>(){
			@Override
			public void action(T agent, AbstractState<T> state)
					throws Exception {
				Branch.this.originalTRS = st.getTransitionResolutionStrategy();
				st.setTransitionResolutionStrategy(TransitionResolutionStrategy.PRIORITY);
				
			}
		});
		registerOnExit(new StateAction<T>(){
			@Override
			public void action(T agent, AbstractState<T> state)
					throws Exception {
				st.setTransitionResolutionStrategy(Branch.this.originalTRS);
				
			}
		});
	}


	public static <U> Branch<U> createBranch(String id, IntoBranchTransition<U> ibt, List<OutOfBranchTransition<U>> tos) {
		Branch<U> branch = new Branch<U>(id);
		branch.intoBranchTransition = ibt;
		branch.tos = tos;
		return branch;
	}

	public IntoBranchTransition<T> getIntoBranchTransition() {
		return intoBranchTransition;
	}

	public List<OutOfBranchTransition<T>> getTos() {
		return tos;
	}

}
