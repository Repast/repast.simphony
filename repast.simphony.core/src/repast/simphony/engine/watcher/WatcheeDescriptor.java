package repast.simphony.engine.watcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes a watchee.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class WatcheeDescriptor {

	private String className;
	private List<FieldDescriptor> fields = new ArrayList<FieldDescriptor>();

	public WatcheeDescriptor(String className) {
		this.className = className;
	}

	public void addFieldDescriptor(String fieldName, String type) {
		fields.add(new FieldDescriptor(fieldName, type));
	}

	public Iterable<FieldDescriptor> getFields() {
		return fields;
	}

	public String getClassName() {
		return className;
	}
}
