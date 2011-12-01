package repast.simphony.filter;

public class EqualsToFilter<T> implements Filter<T> {
	T object;

	public EqualsToFilter(T object) {
		this.object = object;
	}

	public boolean evaluate(T object) {
		return this.object.equals(object);
	}

}
