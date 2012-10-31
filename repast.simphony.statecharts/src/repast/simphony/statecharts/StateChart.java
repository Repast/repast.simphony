package repast.simphony.statecharts;

public interface StateChart extends StateChartResolveActionListener {

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
	public void addTransition(Trigger transition, State source, State target);
	
	/**
	 * Add self transition to state chart.
	 * @param transition
	 * @param source
	 * @param target
	 */
	void addSelfTransition(Trigger trigger, State state);
	
	/**
	 * Retrieve current state.
	 * @return
	 */
	public State getCurrentState();

	public void scheduleResolveTime(double nextTime);

	

}
