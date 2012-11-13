package repast.simphony.statecharts;

import java.util.Queue;
import java.util.concurrent.Callable;

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
	 */
	public void addRegularTransition(Transition transition);
	
	/**
	 * Add branch to state chart. 
	 * @param branch
	 */
	public void addBranch(Branch branch);
	
	/**
	 * Add self transition to state chart.
	 * @param transition
	 * @param source
	 * @param target
	 */
	public void addSelfTransition(Trigger trigger, State state);
	public void addSelfTransition(Trigger trigger, Callable<Void> onTransition,
			Callable<Boolean> guard, State state);

	
	/**
	 * Retrieve current state.
	 * @return
	 */
	public State getCurrentState();
	
	

	public void resolve();
	public void scheduleResolveTime(double nextTime);
	public void removeResolveTime(double nextTime);

	public double getPriority();
	void setPriority(double priority);

	public Queue<Object> getQueue();

	public void setTransitionResolutionStrategy(TransitionResolutionStrategy transitionResolutionStrategy);

	public TransitionResolutionStrategy getTransitionResolutionStrategy();


}
