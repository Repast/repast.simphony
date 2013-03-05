package repast.simphony.statecharts;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import simphony.util.messages.MessageCenter;

public abstract class AbstractState<T> {
	
	private DefaultStateChart<T> stateChart;
	private T agent;
	
	
	protected T getAgent(){
		if (agent == null){
			if (stateChart == null){
				if (getParent() == null){
					throw new IllegalStateException("The state: " + this + " was not added to the statechart");
				}
				else {
					agent = getParent().getAgent();
				}
			}
			else{
				agent = stateChart.getAgent();
			}
		}
		return agent;
	}
		
	protected void setStateChart(DefaultStateChart<T> stateChart){
		this.stateChart = stateChart;
	}
	
	private CompositeState<T> parent;
	
	public CompositeState<T> getParent() {
		return parent;
	}

	
	public void setParent(CompositeState<T> parent) {
		this.parent = parent;
	}

	private String id;
	private StateAction<T> onEnter;
	private StateAction<T> onExit;
	
	public AbstractState(String id){
		this.id = id;
	}
	
	@Override
	public String toString(){
		return getId();
	}
	
	public String getId() {
		return id;
	}

	private Parameters params;
	
	protected Parameters getParams() {
		if (params == null) {
			RunEnvironment re = RunEnvironment.getInstance();
			if (re != null)
				params = re.getParameters();
		}
		return params;
	}
	
	protected void onEnter() {
		try {
			onEnter.action(getAgent(),this,getParams());
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when calling onEnter in state: " + id, e);
			throw new RuntimeException(e);
		}
	}

	
	protected void onExit() {
		try {
			onExit.action(getAgent(),this,getParams());
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when calling onExit in state: " + id, e);
			throw new RuntimeException(e);
		}
	}

	
	protected void registerOnEnter(StateAction<T> onEnter) {
		this.onEnter = onEnter;
	}

	
	protected void registerOnExit(StateAction<T> onExit) {
		this.onExit = onExit;
	}

	
	public AbstractState<T> calculateLowestCommonAncestor(AbstractState<T> other){
		// If the source and target are the same, the lca is the parent
		if (this.equals(other)){
			return getParent();
		}
		Iterator<AbstractState<T>> myAncestors = getAncestors().iterator();
		Iterator<AbstractState<T>> otherAncestors = other.getAncestors().iterator();
		AbstractState<T> currentCandidate = null;
		while(myAncestors.hasNext() && otherAncestors.hasNext()){
			AbstractState<T> myNext = myAncestors.next(); 
			if (myNext == otherAncestors.next()) currentCandidate = myNext;
		}
		return currentCandidate;
	}
	
	/**
	 * Returns a list with the highest node in front.
	 */
	protected List<AbstractState<T>> getAncestors(){
		LinkedList<AbstractState<T>> ancestors = new LinkedList<AbstractState<T>>();
		AbstractState<T> s = this;
		ancestors.addFirst(s);
		while (s.getParent() != null){
			s = s.getParent();
			ancestors.addFirst(s);
		}
		return ancestors;
	}
}
