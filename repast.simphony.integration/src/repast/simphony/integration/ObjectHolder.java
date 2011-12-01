/*CopyrightHere*/
package repast.simphony.integration;

/**
 * Class used by the {@link repast.simphony.integration.BeanBuilder} for keeping track of
 * stored values for detaching purposes.
 * 
 * @author Jerry Vos
 */
public class ObjectHolder {
	private Object parent;

	private String name;

	private Object value;

	private ObjectHolder.StorageType storageType;

	/**
	 * If the value was stored as a child of the parent or as a value in the parent. For instance,
	 * if it was stored as a new Record (CHILD) or as a value in a setter (VALUE).
	 */
	enum StorageType {
		CHILD, VALUE
	}

	/**
	 * Constructs an ObjectHolder with the specified field values.
	 * 
	 * @see StorageType
	 * 
	 * @param parent
	 *            the object to which the value was stored (or added under)
	 * @param name
	 *            the name the value was stored under
	 * @param value
	 *            the value that was stored
	 * @param storageType
	 *            how the value was stored
	 */
	public ObjectHolder(Object parent, String name, Object value,
			ObjectHolder.StorageType storageType) {
		super();
		this.parent = parent;
		this.name = name;
		this.value = value;
		this.storageType = storageType;
	}

	/**
	 * The name that value was stored under.
	 * 
	 * @return a string name
	 */
	public String getName() {
		return name;
	}

	/**
	 * The object in which the value was stored (or added under)
	 * 
	 * @return an object
	 */
	public Object getParent() {
		return parent;
	}

	/**
	 * The value that was stored.
	 * 
	 * @return an object
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * How the object was stored.
	 * 
	 * @see StorageType
	 * 
	 * @return a {@link StorageType} object
	 */
	public ObjectHolder.StorageType getStorageType() {
		return storageType;
	}
}