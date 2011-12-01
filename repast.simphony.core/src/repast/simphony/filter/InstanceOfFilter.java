package repast.simphony.filter;

public class InstanceOfFilter<T> implements Filter<T> {
	Class<T> clazz;

	public InstanceOfFilter(Class<T> clazz) {
		this.clazz = clazz;
	}

	public boolean evaluate(T object) {
		return clazz.isAssignableFrom(object.getClass());
	}

}
