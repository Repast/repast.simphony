package repast.simphony.gis.styleEditor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Map;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;

import repast.simphony.space.gis.FeatureAgent;
import repast.simphony.util.ClassUtilities;
import simphony.util.messages.MessageCenter;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Adapts a java Class to a GIS feature. The resulting
 * feature is "hollow" and has no attribute values, but
 * does provide a schema that can be used in the
 * StyleEditor.
 *
 * @author Nick Collier
 */
public class HollowFeature implements SimpleFeature {

	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(HollowFeature.class);

  private Map<String, FeatureAgentAttribute> attributeMap = new HashMap<String, FeatureAgentAttribute>();
	private String id;
	private SimpleFeatureType schema;
	private FeatureCollection parent;
	private Geometry geom;

  /**
	 * Interface for getting and setting a FeatureAgent's attributes.
	 */
	static class FeatureAgentAttribute {
    Class type;

    FeatureAgentAttribute(Class type) {
      this.type = type;
    }

    public Object getAttribute() {
      if (ClassUtilities.isNumericType(type)) return 0;
      else return "";
    }

		public void setAttribute(Object obj) {

    }
	}


	/**
	 * Creates a HollowFeature from the specified FeatureType, object, feature,
	 * and map. The map is used to map between agent property names and feature
	 * attribute names. The key is the agent property name and the value is the
	 * feature attribute name. Propeties mapped in this way will show up in the
	 * gis feature with the attribute name.
	 *
	 * @param type
	 */
	public HollowFeature(FeatureType type, Class objClass, Geometry geom) {
    this.geom = geom;
		createAttributes(objClass);
		// create the id as in geotools DefaultFeature.
		id = "fid-" + (new UID()).toString();
		this.schema = type;
	}

	// creates the feature attributes from object properties
	private void createAttributes(Class objClass) {
		try {
			BeanInfo info = Introspector.getBeanInfo(objClass, Object.class);
			PropertyDescriptor[] pds = info.getPropertyDescriptors();
			for (int i = 0; i < pds.length; i++) {
				PropertyDescriptor pd = pds[i];
				String featureName = pd.getName();
        if (pd.getReadMethod() != null) {
          attributeMap.put(featureName, new FeatureAgentAttribute(pd.getReadMethod().getReturnType()));
        }
      }
		} catch (IntrospectionException e) {
			msgCenter.error("Unable to create feature attributes from agent", e);
		}
	}

	/**
	 * Gets the feature collection this feature is stored in.
	 *
	 * @return the collection that is the parent of this feature.
	 */
	public FeatureCollection getParent() {
		return parent;
	}

	/**
	 * Sets the parent collection this feature is stored in, if it is not
	 * already set. If it is set then this method does nothing.
	 *
	 * @param featureCollection
	 *            the collection to be set as parent.
	 */
	public void setParent(FeatureCollection featureCollection) {
		this.parent = featureCollection;
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
    if (xPath.equals("the_geom")) {
			return getDefaultGeometry();
		}
    FeatureAgentAttribute getter = attributeMap.get(xPath);
		if (getter != null)
			return getter.getAttribute();
		return new NULL();
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
    if (name.equals("the_geom")) {
			return getDefaultGeometry();
		}
    if (name != null) {
			FeatureAgentAttribute getter = attributeMap.get(name);
			if (getter != null)
				return getter.getAttribute();
		}
		return new NULL();
	}

	private String getAttributeName(int i) {
		String name = null;
		AttributeType type = schema.getAttributeType(i);
		if (type != null) name = type.getName();
		return name;
	}

	/**
	 * Sets the attribute at position to val.
	 *
	 * @param index
	 *            the index of the attribute to set.
	 * @param val
	 *            the new value to give the attribute at position.
	 * @throws org.geotools.feature.IllegalAttributeException
	 *             if the passed in val does not validate against the
	 *             AttributeType at that position.
	 */
	public void setAttribute(int index, Object val)
			throws IllegalAttributeException, ArrayIndexOutOfBoundsException {
		String name = getAttributeName(index);
		FeatureAgentAttribute setter = attributeMap.get(name);
		if (setter != null)
			setter.setAttribute(val);
		else
			throw new IllegalAttributeException("Unable to set attribute '"
					+ name + "', attribute not found");
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
	 * @throws org.geotools.feature.IllegalAttributeException
	 *             if passed attribute does not match feature type
	 */
	public void setAttribute(String xPath, Object obj)
			throws IllegalAttributeException {
		FeatureAgentAttribute setter = attributeMap.get(xPath);
		if (setter != null)
			setter.setAttribute(obj);
		else
			throw new IllegalAttributeException(
					"Unable to set attribute via xPath '" + xPath + "'");
	}

	/**
	 * Gets the geometry for this feature.
	 *
	 * @return Geometry for this feature.
	 */
	public Geometry getDefaultGeometry() {
		return geom;
	}

	/**
	 * Modifies the geometry.
	 *
	 * @param geometry
	 *            for all feature attributes.
	 * @throws org.geotools.feature.IllegalAttributeException
	 *             if the feature does not have a geometry.
	 */
	public void setDefaultGeometry(Geometry geometry)
			throws IllegalAttributeException {
	  this.geom = geometry;
	}

	/**
	 * Get the total bounds of this feature which is calculated by doing a union
	 * of the bounds of each geometry this feature is associated with.
	 *
	 * @return An Envelope containing the total bounds of this Feature.
	 */
	public Envelope getBounds() {
		return geom.getEnvelopeInternal();
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

		return true;
	}
}