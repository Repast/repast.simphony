package repast.simphony.valueLayer;

/**
 * Interface for value layer data stores.
 * 
 * @author Eric Tatara
 *
 */
public interface ValueLayerStore{
	
	/**
	 * Get the double value for the specified index.
	 * 
	 * @param index
	 * @return
	 */
	public double get(long index);
	
	/**
	 * Set the double value for the specified index.
	 * 
	 * @param index
	 * @param value
	 */
	public void set(long index, double value);

	public int size();
}
