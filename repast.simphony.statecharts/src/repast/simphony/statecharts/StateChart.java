package repast.simphony.statecharts;

public interface StateChart {
	
	/**
	 * Receive a message into the statechart queue.
	 */
	public void receiveMessage(Object message);

	/**
	 * Begin state chart.
	 */
	public void begin();
	
	/**
	 * Add state to state chart.
	 * @param state
	 */
	public void addState(State state);
	
	/**
	 * Add state to state chart.
	 * @param state
	 */
	public void registerEntryState(State state);
	
	/**
	 * Add transition to state chart.
	 * @param transition
	 * @param source
	 * @param target
	 */
	public void addRegularTransition(Transition transition);
	
	/**
	 * Add self transition to state chart.
	 * @param transition
	 * @param source
	 * @param target
	 */
	void addSelfTransition(Transition transition);
	
	/**
	 * Retrieve current state.
	 * @return
	 */
	public State getCurrentState();

	public void resolve();
	public void scheduleResolveTime(double nextTime);
	public void removeResolveTime(double nextTime);

}
