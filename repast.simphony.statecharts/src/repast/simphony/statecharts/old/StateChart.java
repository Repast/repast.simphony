package repast.simphony.statecharts.old;

import java.util.ArrayList;
import java.util.List;

public class StateChart {

	private State entryState;
	private List<State> topLevelStates = new ArrayList<State>();
	
	public StateChart(){
		
	}
	
	public void addState(){
		
	}
	
	public void addTransition(Transition transition, State source, State target){
		
	}
	
	public State getTopLevelActiveState(){
		return null;
	}
	
	public List<State> getAllActiveStates(){
		return null;
	}
	
	
	public State getActiveSimpleState(){
		return null;
	}
	
	public void update(){
		
	}

}
