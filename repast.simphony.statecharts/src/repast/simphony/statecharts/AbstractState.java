package repast.simphony.statecharts;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import simphony.util.messages.MessageCenter;

public abstract class AbstractState<T> {
	
	private T agent;
	
	public T getAgent(){
		return agent;
	}
	
	public void setAgent(T agent){
		this.agent = agent;
	}

	private CompositeState<T> parent;
	
	public CompositeState<T> getParent() {
		return parent;
	}

	
	public void setParent(CompositeState<T> parent) {
		this.parent = parent;
	}

	private String id;
	private StateAction<T> onEnter = new StateAction<T>(){
		@Override
		public void action(T agent, AbstractState<T> state) throws Exception {
		}
	};
	private StateAction<T> onExit = new StateAction<T>(){
		@Override
		public void action(T agent, AbstractState<T> state) throws Exception {
		}
	};
	
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

	
	public void onEnter() {
		try {
			onEnter.action(getAgent(),this);
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when calling onEnter in state: " + id, e);
			throw new RuntimeException(e);
		}
	}

	
	public void onExit() {
		try {
			onExit.action(getAgent(),this);
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when calling onExit in state: " + id, e);
			throw new RuntimeException(e);
		}
	}

	
	public void registerOnEnter(StateAction<T> onEnter) {
		this.onEnter = onEnter;
	}

	
	public void registerOnExit(StateAction<T> onExit) {
		this.onExit = onExit;
	}

	
	public AbstractState<T> calculateLowestCommonAncestor(AbstractState<T> other){
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
