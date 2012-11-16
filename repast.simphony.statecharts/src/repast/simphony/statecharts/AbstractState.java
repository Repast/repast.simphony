package repast.simphony.statecharts;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import simphony.util.messages.MessageCenter;

public abstract class AbstractState {

	private CompositeState parent;
	
	public CompositeState getParent() {
		return parent;
	}

	
	public void setParent(CompositeState parent) {
		this.parent = parent;
	}

	private String id;
	private Callable<Void> onEnter = new Callable<Void>(){
		@Override
		public Void call() throws Exception {
			return null;
		}
	};
	private Callable<Void> onExit = new Callable<Void>(){
		@Override
		public Void call() throws Exception {
			return null;
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
			onEnter.call();
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when calling onEnter in state: " + id, e);
			throw new RuntimeException(e);
		}
	}

	
	public void onExit() {
		try {
			onExit.call();
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when calling onExit in state: " + id, e);
			throw new RuntimeException(e);
		}
	}

	
	public void registerOnEnter(Callable<Void> onEnter) {
		this.onEnter = onEnter;
	}

	
	public void registerOnExit(Callable<Void> onExit) {
		this.onExit = onExit;
	}

	
	public AbstractState calculateLowestCommonAncestor(AbstractState other){
		Iterator<AbstractState> myAncestors = getAncestors().iterator();
		Iterator<AbstractState> otherAncestors = other.getAncestors().iterator();
		AbstractState currentCandidate = null;
		while(myAncestors.hasNext() && otherAncestors.hasNext()){
			AbstractState myNext = myAncestors.next(); 
			if (myNext == otherAncestors.next()) currentCandidate = myNext;
		}
		return currentCandidate;
	}
	
	/**
	 * Returns a list with the highest node in front.
	 */
	protected List<AbstractState> getAncestors(){
		LinkedList<AbstractState> ancestors = new LinkedList<AbstractState>();
		AbstractState s = this;
		ancestors.addFirst(s);
		while (s.getParent() != null){
			s = s.getParent();
			ancestors.addFirst(s);
		}
		return ancestors;
	}
}
