package repast.simphony.statecharts;

import java.util.List;


public interface StateChart<T> {
	
	/**
	 * Receive a message into the statechart queue.
	 */
	void receiveMessage(Object message);
	
	/**
	 * Gets the agent associated with this StateChart.
	 * 
	 * @return the agent associated with this StateChart.
	 */
	T getAgent();

	/**
	 * Begin state chart.
	 */
	void begin(StateChartSimIntegrator integrator);
	
	/**
	 * Stops the state chart.
	 */
	void stop();
	
	/**
	 * Retrieve current state.
	 * @return
	 */
	AbstractState<T> getCurrentSimpleState();
	
	boolean withinState(String id);
	
	List<AbstractState<T>> getCurrentStates();
	
	String getUuidForState(AbstractState<T> state);
	
	/**
	 * Returns the state associated with the uuid or
	 * null if not found.
	 * @param uuid
	 * @return
	 */
	AbstractState<T> getStateForUuid(String uuid);
	
	void registerStateChartListener(StateChartListener scl);
	
	void removeStateChartListener(StateChartListener scl);

	/**
	 * Returns the transition associated with the uuid or
	 * null if not found.
	 * @param uuid
	 * @return
	 */
	Transition<T> getTransitionForUuid(String uuid);
	
	/**
	 * Activates the state if it exists. Should be called when simulation is paused.
	 * @param state
	 * 			the state to activate
	 */
	void activateState(AbstractState<T> state);
	
	/**
	 * Follow the transition if valid. Should be called when simulation is paused.
	 * @param transition
	 * 			the transition to follow
	 */
	void followTransition(Transition<T> transition);
	
	/**
	 * Activates the state (identified by a potentially non-unique ID) if it exists.
	 * @param stateID
	 * 			the potentially non-unique ID of the state to activate
	 */
	void activateState(String stateID);
	
	
	/**
	 * Follow the transition (identified by a potentially non-unique ID) if valid.
	 * @param transitionID
	 * 			the potentially non-unique ID of the transition to follow
	 */
	void followTransition(String transitionID);
	
	

	double getPriority();

	void resolve();

}
