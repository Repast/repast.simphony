package repast.simphony.context;

public interface SpatialContext<T, V> extends Context<T> {
	public boolean putObjectAt(T object, V coordinates);

	public boolean putObjectAt(T object, double... coordinates);

	public Iterable<T> getObjectsAt(V coordinates);

	public V moveByVector(T object, double distance, double... anglesInRadians);

	public V moveByDisplacement(T object, double... displacement);

	public V getLocation(Object object);

}
