package repast.simphony.filter;


public interface Filter<T> {
	public boolean evaluate(T object);
}
