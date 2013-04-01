package repast.simphony.statecharts.old;

/**
 * State abstract class.
 * @author jozik
 *
 */
public abstract class State {
	
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	private State parent;
	
	public State getParent() {
		return parent;
	}

	public void setParent(State parent) {
		this.parent = parent;
	}
	
	public boolean hasParent() {
		return parent != null;
	}

	/**
	 * 
	 */
	protected void enterStateFromChild(){
		if (hasParent()){
			getParent().enterStateFromChild();
		}
		initializeState();
	}
	
	protected void enterStateFromInitialState(){
		initializeState();
	}
	
	
	/**
	 * 
	 */
	public abstract void enterState();
	public abstract void exitState();
	public abstract void initializeState();

}
