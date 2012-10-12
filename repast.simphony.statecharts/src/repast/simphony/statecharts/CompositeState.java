package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.List;

public class CompositeState extends State {

	private List<State> children = new ArrayList<State>();
	private final State initialState;
	private State parent;
	private State currentState;
	
	public CompositeState(State initialState){
		this.initialState = initialState;
		addState(initialState);
	}
	
//	public static CompositeState createState(State initialState){
//		return new CompositeState(initialState);
//	}
	
	@Override
	public void enterState() {
		if (hasParent()){
			getParent().enterStateFromChild();
		}
		// go to initial state
		currentState = initialState;
		currentState.enterStateFromInitialState();
	}

	@Override
	public void exitState() {
		// TODO Auto-generated method stub
		
	}
	
	public void addState(State state){
		children.add(state);
		state.setParent(this);
	}

	@Override
	public State getParent() {
		return parent;
	}



	@Override
	public void setParent(State state) {
		this.parent = state;
	}

	@Override
	public void initializeState() {
		System.out.println("Composite state: " + getId() + "initialized");
		
	}



}
