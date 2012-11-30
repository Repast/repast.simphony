package repast.simphony.statecharts;

public interface TriggerDoubleFunction<T> {
	public double value(T agent, Transition<T> transition) throws Exception;
}
