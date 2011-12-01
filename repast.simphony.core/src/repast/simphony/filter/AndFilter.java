package repast.simphony.filter;

public class AndFilter<T> implements Filter<T> {

	Filter<T> filter1;

	Filter<T> filter2;

	public AndFilter(Filter<T> filter1, Filter<T> filter2) {
		this.filter1 = filter1;
		this.filter2 = filter2;
	}

	public boolean evaluate(T object) {
		return filter1.evaluate(object) && filter2.evaluate(object);
	}

}
