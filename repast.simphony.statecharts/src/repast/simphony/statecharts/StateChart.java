package repast.simphony.statecharts;

import java.util.List;


public interface StateChart<T> {
	
	/**
	 * Receive a message into the statechart queue.
	 */
	public void receiveMessage(Object message);
	
	/**
	 * Gets the agent associated with this StateChart.
	 * 
	 * @return the agent associated with this StateChart.
	 */
	T getAgent();

	/**
	 * Begin state chart.
	 */
	public void begin(StateChartSimIntegrator integrator);
	
	/**
	 * Stops the state chart.
	 */
	public void stop();
	
	/**
	 * Retrieve current state.
	 * @return
	 */
	public AbstractState<T> getCurrentSimpleState();
	
	public boolean withinState(String id);
	
	public List<AbstractState<T>> getCurrentStates();
	
	public String getUuidForState(AbstractState<T> state);
	public AbstractState<T> getStateForUuid(String uuid);
	
	public void registerStateChartListener(StateChartListener scl);
	
	public void removeStateChartListener(StateChartListener scl);

}
