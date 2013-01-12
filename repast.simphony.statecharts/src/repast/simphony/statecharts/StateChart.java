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
	
	public boolean withinState(String id);
	
	public List<AbstractState<T>> getCurrentStates();
	
	public String getUuidForState(AbstractState<T> state);
	
	public void registerStateChartListener(StateChartListener scl);
	
	public void removeStateChartListener(StateChartListener scl);

}
