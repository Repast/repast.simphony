package repast.simphony.statecharts.old;

public class SimpleState extends State {

	private State parent;
	
	@Override
	public void enterState() {
		if (hasParent()){
			getParent().enterStateFromChild();
		}
		initializeState();
	}

	@Override
	public void exitState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public State getParent() {
		return parent;
	}

	@Override
	public boolean hasParent() {
		return parent != null;
	}

	@Override
	public void initializeState() {
		System.out.println("Simple state: " + getId() + "initialized");
	}


}
