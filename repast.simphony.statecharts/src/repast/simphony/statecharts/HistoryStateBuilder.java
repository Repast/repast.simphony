package repast.simphony.statecharts;

public class HistoryStateBuilder<T> extends AbstractStateBuilder<T> {

	final private boolean shallow;
	
	public HistoryStateBuilder(String id, boolean shallow) {
		super(id);
		this.shallow = shallow;
	}
	
	public HistoryStateBuilder(String id) {
		this(id,true);
	}
	
	@Override
	public void registerOnExit(StateAction<T> onExit) {
		throw new UnsupportedOperationException("HistoryStates are never exited, only entered. Use registerOnEnter instead.");
	}
	
	public HistoryState<T> build(){
		HistoryState<T> result = new HistoryState<T>(id, shallow);
		result.registerOnEnter(onEnter);// Since register on exit is not supported.
		return result;
	}
	
}
