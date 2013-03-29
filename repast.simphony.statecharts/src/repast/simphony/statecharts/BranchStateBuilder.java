package repast.simphony.statecharts;

public class BranchStateBuilder<T> extends SimpleStateBuilder<T>{
	
	public BranchStateBuilder(String id) {
		super(id);
	}

	public BranchState<T> build(){
		BranchState<T> result = new BranchState<T>(id);
		setAbstractProperties(result); // This is not strictly necessary.
		return result;
	}
	
	@Override
	public void registerOnEnter(StateAction<T> onEnter) {
		throw new UnsupportedOperationException("BranchStates cannot have onEnter actions.");
	}

	@Override
	public void registerOnExit(StateAction<T> onExit) {
		throw new UnsupportedOperationException("BranchStates cannot have onExit actions.");
	}
	
}
