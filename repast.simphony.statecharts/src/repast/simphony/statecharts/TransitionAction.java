package repast.simphony.statecharts;

public interface TransitionAction<T> {
	
	public void action(T agent, Transition<T> transition) throws Exception;

}
