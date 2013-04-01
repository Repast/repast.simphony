package repast.simphony.statecharts;

public class FinalStateBuilder<T> extends SimpleStateBuilder<T>{
	
	public FinalStateBuilder(String id) {
		super(id);
	}

	@Override
	public void registerOnExit(StateAction<T> onExit) {
		throw new UnsupportedOperationException("FinalStates are never exited, only entered.");
	}
	
	public FinalState<T> build(){
		FinalState<T> result = new FinalState<T>(id);
		result.registerOnEnter(onEnter);// Since register on exit is not supported.
		return result;
	}
		
}
