package repast.simphony.gis.display;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.geotools.feature.*;
import simphony.util.messages.MessageCenter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Map;

/**
 * A hybrid feature that combines an object with a gis feature. The objects
 * properties become feature attributes. Setting the attribute values via the
 * feature or via the object itself will update this hybrid feature.
 * 
 * @author Nick Collier
 * @version $Revision: 1.3 $ $Date: 2007/04/18 19:25:53 $
 */
public class FeatureAgent implements Feature {

	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(FeatureAgent.class);

	private Object agentObj;

	private Feature feature;

	private Map<String, FeatureAgentAttribute> attributeMap = new HashMap<String, FeatureAgentAttribute>();

	private String id;

	// private static List<String> attributeNames;
	private FeatureType schema;

	/**
	 * Interface for getting and setting a FeatureAgent's attributes.
	 */
	static interface FeatureAgentAttribute {
		public Object getAttribute();

		public void setAttribute(Object obj) throws IllegalAttributeException;
	}

	static class FeatureFeatureAgentAttribute implements FeatureAgentAttribute {

		private Feature feature;

		private String name;

		public FeatureFeatureAgentAttribute(Feature feature, String name) {
			this.feature = feature;
			this.name = name;
		}

		public Object getAttribute() {
			return feature.getAttribute(name);
		}

		public void setAttribute(Object obj) throws IllegalAttributeException {
			feature.setAttribute(name, obj);
		}
	}

	static class ObjectFeatureAgentAttribute implements FeatureAgentAttribute {

		private static final Object[] EMPTY_ARRAY = new Object[0];

		private Object obj;

		private FastMethod readMethod;

		private FastMethod writeMethod;

		public ObjectFeatureAgentAttribute(Object obj, FastMethod readMethod, FastMethod writeMethod) {
			this.obj = obj;
			this.readMethod = readMethod;
			this.writeMethod = writeMethod;
		}

		public Object getAttribute() {
			try {
				return readMethod.invoke(obj, EMPTY_ARRAY);
			} catch (InvocationTargetException e) {
				MessageCenter.getMessageCenter(getClass()).error(
						"Unable to get feature attribute from object", e);
				// todo error center
			}
			return new Feature.NULL();
		}

		public void setAttribute(Object obj) throws IllegalAttributeException {
			if (writeMethod == null) {
				throw new IllegalAttributeException(
						"Unable to set attribute: write method does not exist");
			}
			try {
				writeMethod.invoke(this.obj, new Object[] { obj });
			} catch (InvocationTargetException e) {
				IllegalAttributeException ex = new IllegalAttributeException(
						"Unable to set attribute: " + e.getMessage());
				ex.initCause(e);
				throw ex;
			}
		}
	}

	/**
	 * Creates a FeatureAgent from the specified FeatureType, object, and
	 * feature.
	 * 
	 * @param type
	 * @param agent
	 * @param feature
	 */
	FeatureAgent(FeatureType type, Object agent, Feature feature) {
		this(type, agent, feature, null);
	}

	/**
	 * Creates a FeatureAgent from the specified FeatureType, object, feature,
	 * and map. The map is used to map between agent property names and feature
	 * attribute names. The key is the agent property name and the value is the
	 * feature attribute name. Propeties mapped in this way will show up in the
	 * gis feature with the attribute name.
	 * 
	 * @param type
	 * @param agent
	 * @param feature
	 */
	FeatureAgent(FeatureType type, Object agent, Feature feature,
			Map<String, String> agentFeatureMap) {
		this.agentObj = agent;
		this.feature = feature;
		createAttributes(agentFeatureMap);
		addFeatureAttributesToMap();
		// create the id as in geotools DefaultFeature.
		id = "fid-" + (new UID()).toString();
		this.schema = type;
	}

	// creates the feature attributes from object properties
	private void createAttributes(Map<String, String> agentFeatureMap) {
		try {
			BeanInfo info = Introspector.getBeanInfo(agentObj.getClass(), Object.class);
			PropertyDescriptor[] pds = info.getPropertyDescriptors();
			for (int i = 0; i < pds.length; i++) {
				PropertyDescriptor pd = pds[i];
				String featureName = getFeatureName(agentFeatureMap, pd.getName());
				if (featureName != null) {
					FastClass fastClass = FastClass.create(agentObj.getClass());
					FastMethod readMethod = fastClass.getMethod(pd.getReadMethod());
					FastMethod writeMethod = pd.getWriteMethod() == null ? null : fastClass
							.getMethod(pd.getWriteMethod());
					attributeMap.put(featureName, new ObjectFeatureAgentAttribute(agentObj,
							readMethod, writeMethod));
				}
			}
		} catch (IntrospectionException e) {
			msgCenter.error("Unable to create feature attributes from agent", e);
			// todo error center
		}
	}

	private String getFeatureName(Map<String, String> agentFeatureMap, String name) {
		if (agentFeatureMap == null || agentFeatureMap.size() == 0)
			return name;
		return agentFeatureMap.get(name);
	}

	// adds the feature attributes to the attribute map
	private void addFeatureAttributesToMap() {
		AttributeType[] types = feature.getFeatureType().getAttributeTypes();
		for (int i = 0; i < types.length; i++) {
			AttributeType type = types[i];
			String name = type.getName();
			attributeMap.put(name, new FeatureFeatureAgentAttribute(feature, name));
		}
	}

	/**
	 * Gets the feature collection this feature is stored in.
	 * 
	 * @return the collection that is the parent of this feature.
	 */
	public FeatureCollection getParent() {
		return feature.getParent();
	}

	/**
	 * Sets the parent collection this feature is stored in, if it is not
	 * already set. If it is set then this method does nothing.
	 * 
	 * @param featureCollection
	 *            the collection to be set as parent.
	 */
	public void setParent(FeatureCollection featureCollection) {
		feature.setParent(featureCollection);
	}

	/**
	 * Gets a reference to the feature type schema for this feature.
	 * 
	 * @return A copy of this feature's metadata in the form of a feature type
	 *         schema.
	 */
	public FeatureType getFeatureType() {
		return schema;
	}

	/**
	 * Gets the unique indentification string of this Feature.
	 * 
	 * @return The unique id.
	 */
	public String getID() {
		return id;
	}

	/**
	 * Copy all the attributes of this Feature into the given array. If the
	 * argument array is null, a new one will be created. Gets all attributes
	 * from this feature, returned as a complex object array. This array comes
	 * with no metadata, so to interpret this collection the caller class should
	 * ask for the schema as well.
	 * 
	 * @param objects
	 *            The array to copy the attributes into.
	 * @return The array passed in, or a new one if null.
	 */
	public Object[] getAttributes(Object[] objects) {
		if (objects == null) {
			objects = new Object[schema.getAttributeCount()];
		}

		// need to do in schema order
		for (int i = 0, n = schema.getAttributeCount(); i < n; i++) {
			String name = getAttributeName(i);
			FeatureAgentAttribute attribute = attributeMap.get(name);
			objects[i] = attribute.getAttribute();
		}
		return objects;
	}

	/**
	 * Gets an attribute for this feature at the location specified by xPath.
	 * 
	 * @param xPath
	 *            XPath representation of attribute location.
	 * @return Attribute.
	 */
	public Object getAttribute(String xPath) {
		FeatureAgentAttribute getter = attributeMap.get(xPath);
		if (getter != null)
			return getter.getAttribute();
		return new Feature.NULL();
	}

	/**
	 * Gets an attribute by the given zero-based index.
	 * 
	 * @param index
	 *            the position of the attribute to retrieve.
	 * @return The attribute at the given index.
	 */
	public Object getAttribute(int index) {
		String name = getAttributeName(index);
		if (name != null) {
			FeatureAgentAttribute getter = attributeMap.get(name);
			if (getter != null)
				return getter.getAttribute();
		}
		return new Feature.NULL();
	}

	private String getAttributeName(int i) {
		String name = null;
		AttributeType type = schema.getAttributeType(i);
		if (type != null)
			name = type.getName();
		return name;
	}

	/**
	 * Sets the attribute at position to val.
	 * 
	 * @param index
	 *            the index of the attribute to set.
	 * @param val
	 *            the new value to give the attribute at position.
	 * @throws IllegalAttributeException
	 *             if the passed in val does not validate against the
	 *             AttributeType at that position.
	 */
	public void setAttribute(int index, Object val) throws IllegalAttributeException,
			ArrayIndexOutOfBoundsException {
		String name = getAttributeName(index);
		FeatureAgentAttribute setter = attributeMap.get(name);
		if (setter != null)
			setter.setAttribute(val);
		else
			throw new IllegalAttributeException("Unable to set attribute '" + name
					+ "', attribute not found");
	}

	public int getNumberOfAttributes() {
		return schema.getAttributeCount();
	}

	/**
	 * Sets a single attribute for this feature, passed as a complex object. If
	 * the attribute does not exist or the object does not conform to the
	 * internal feature type, an exception is thrown.
	 * 
	 * @param xPath
	 *            XPath representation of attribute location.
	 * @param obj
	 *            the attribute value to set.
	 * @throws IllegalAttributeException
	 *             if passed attribute does not match feature type
	 */
	public void setAttribute(String xPath, Object obj) throws IllegalAttributeException {
		FeatureAgentAttribute setter = attributeMap.get(xPath);
		if (setter != null)
			setter.setAttribute(obj);
		else
			throw new IllegalAttributeException("Unable to set attribute via xPath '" + xPath + "'");
	}

	/**
	 * Gets the geometry for this feature.
	 * 
	 * @return Geometry for this feature.
	 */
	public Geometry getDefaultGeometry() {
		return feature.getDefaultGeometry();
	}

	/**
	 * Modifies the geometry.
	 * 
	 * @param geometry
	 *            for all feature attributes.
	 * @throws IllegalAttributeException
	 *             if the feature does not have a geometry.
	 */
	public void setDefaultGeometry(Geometry geometry) throws IllegalAttributeException {
		feature.setDefaultGeometry(geometry);
	}

	/**
	 * Get the total bounds of this feature which is calculated by doing a union
	 * of the bounds of each geometry this feature is associated with.
	 * 
	 * @return An Envelope containing the total bounds of this Feature.
	 */
	public Envelope getBounds() {
		return feature.getBounds();
	}

	public int hashCode() {
		return id.hashCode() * schema.hashCode();
	}

	/**
	 * override of equals. Returns if the passed in object is equal to this.
	 * 
	 * @param obj
	 *            the Object to test for equality.
	 * @return <code>true</code> if the object is equal, <code>false</code>
	 *         otherwise.
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj == this) {
			return true;
		}

		if (!(obj instanceof FeatureAgent)) {
			return false;
		}

		FeatureAgent other = (FeatureAgent) obj;

		if (!other.getFeatureType().equals(schema)) {
			return false;
		}

		if (!id.equals(other.getID())) {
			return false;
		}

		if (!other.feature.equals(feature))
			return false;
		if (!other.agentObj.equals(agentObj))
			return false;

		return true;
	}

}
