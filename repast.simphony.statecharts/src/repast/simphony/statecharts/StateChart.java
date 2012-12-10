package repast.simphony.statecharts;

import java.util.List;


public interface StateChart<T> {
	
	/**
	 * Receive a message into the statechart queue.
	 */
	public void receiveMessage(Object message);

	/**
	 * Begin state chart.
	 */
	public void begin();
	
	/**
	 * Retrieve current state.
	 * @return
	 */
	public AbstractState<T> getCurrentSimpleState();
	
//	public double getPriority();
	
	public boolean withinState(String id);
	
	public List<AbstractState<T>> getCurrentStates();

}
