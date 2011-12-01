package repast.simphony.filter;

public class NotFilter<T> implements Filter<T> {

	Filter<T> filter;

	public NotFilter(Filter<T> filter) {
		this.filter = filter;
	}

	public boolean evaluate(T object) {
		return !filter.evaluate(object);
	}

}
