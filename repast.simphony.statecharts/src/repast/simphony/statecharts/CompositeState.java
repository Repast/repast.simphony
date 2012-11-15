package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.List;

public class CompositeState extends AbstractState {

	private AbstractState entryState;
	
	public AbstractState getEntryState() {
		return entryState;
	}

	private List<AbstractState> children = new ArrayList<AbstractState>();
	
	public void registerEntryState(AbstractState state) {
		entryState = state;
		add(state);
	}
	
	public CompositeState(String id) {
		super(id);
	}
	
	public void add(AbstractState state){
		state.setParent(this);
		if (!children.contains(state)){
			children.add(state);
		}
	}

}
