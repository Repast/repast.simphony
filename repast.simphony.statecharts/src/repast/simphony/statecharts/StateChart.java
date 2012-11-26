package repast.simphony.statecharts;


public interface StateChart<T> {
	
	public T getAgent();
	
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
	
	public double getPriority();

	public TransitionResolutionStrategy getTransitionResolutionStrategy();

}
