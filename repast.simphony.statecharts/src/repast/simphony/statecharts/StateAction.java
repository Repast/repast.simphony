package repast.simphony.statecharts;

public interface StateAction<T> {
	
	public void action(T agent, AbstractState<T> state) throws Exception;

}
