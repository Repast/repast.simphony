package repast.simphony.statecharts;

import java.util.List;
import java.util.concurrent.Callable;

public class Branch extends AbstractState {

	private TransitionResolutionStrategy originalTRS;
	
	private List<OutOfBranchTransition> tos;
	private IntoBranchTransition intoBranchTransition;
	


	public void initializeBranch(final StateChart st) {
		registerOnEnter(new Callable<Void>(){

			@Override
			public Void call() throws Exception {
				Branch.this.originalTRS = st.getTransitionResolutionStrategy();
				st.setTransitionResolutionStrategy(TransitionResolutionStrategy.PRIORITY);
				return null;
			}
			
		});
		registerOnExit(new Callable<Void>(){

			@Override
			public Void call() throws Exception {
				st.setTransitionResolutionStrategy(Branch.this.originalTRS);
				return null;
			}
			
		});
	}


	public static Branch createBranch(String id, IntoBranchTransition ibt, List<OutOfBranchTransition> tos) {
		Branch branch = new Branch(id);
		branch.intoBranchTransition = ibt;
		branch.tos = tos;
		return branch;
	}

	public IntoBranchTransition getIntoBranchTransition() {
		return intoBranchTransition;
	}

	public List<OutOfBranchTransition> getTos() {
		return tos;
	}

	private Branch(String id) {
		super(id);
	}

}
