package repast.simphony.freezedry;

import repast.simphony.util.collections.IterableAdaptor;

import java.util.*;

/**
 * This represents a flattened object. The properties stored in this object can
 * be one of four types: String, Number, byte[] or FreezeDryedParentChild. As
 * such, when working with this object, one should take care to neither put
 * another object type in as a value, or expect anything else out. These were
 * chosen as the base primitives for this class because for any object it should
 * be possible to decompose the properties into these types.
 * FreezeDryedParentChild represents a flattened view of another object, so that
 * should account for all possibilites.
 * 
 * @version $revision$
 * @author Howe
 */
public class FreezeDryedObject {

	Map<String, Object> values;

	List<FreezeDryedParentChild> children;

	String id;

	Class type;

	boolean isMulti = false;

	public FreezeDryedObject(String id, Class type) {
		this.id = id;
		this.type = type;
		values = new LinkedHashMap<String, Object>();
	}

	public FreezeDryedObject(Class type) {
		this(null, type);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Class getType() {
		return type;
	}

	public Object get(String key) {
		return values.get(key);
	}

	public void put(String key, Object value) {
		values.put(key, value);
	}

	public Set<String> keySet() {
		return values.keySet();
	}

	public void addChild(FreezeDryedParentChild o) {
		if (children == null) {
			children = new ArrayList<FreezeDryedParentChild>();
		}
		o.setIndex(children.size());
		children.add(o);
	}

	public Iterable<FreezeDryedParentChild> getChildren() {
		if (children == null) {
			return new IterableAdaptor<FreezeDryedParentChild>(
					new ArrayList<FreezeDryedParentChild>().iterator());
		}
		return children;
	}

	public boolean hasChildren() {
		return children != null;
	}

	public int numChildren() {
		if (!hasChildren()) {
			return 0;
		}
		return children.size();
	}

	public Map<String, Object> getProperties() {
		return values;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FreezeDryed " + getType() + ": " + getId() + "\n");
		for (String key : keySet()) {
			builder.append(key + ": " + get(key) + " | ");
		}
		builder.append("\n");
		for (Object child : getChildren()) {
			builder.append(child.toString() + "\n");
		}
		return builder.toString();
	}
}
