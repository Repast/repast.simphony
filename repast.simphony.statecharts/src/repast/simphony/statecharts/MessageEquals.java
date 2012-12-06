package repast.simphony.statecharts;


public interface MessageEquals<T>{
	public Object messageValue(T agent, Transition<T> transition) throws Exception;
}