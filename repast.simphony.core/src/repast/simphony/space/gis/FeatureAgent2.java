package repast.simphony.space.gis;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wraps a POJO in a gis feature. The POJO's properties become
 * feature attributes. The feature's geometry is derived from
 * the POJO and a Geography.
 *
 * @author Nick Collier
 */
public class FeatureAgent2<T> implements Feature {

  private static MessageCenter msgCenter = MessageCenter.getMessageCenter(FeatureAgent2.class);
  private T agentObj;

  private Map<String, FeatureAttributeAdapter> attributeMap = new HashMap<String, FeatureAttributeAdapter>();
  private String id;
  private FeatureType schema;
  private FeatureCollection parent;
  private Geometry geom;
  private Geography geography;

  static class ObjectFeatureAgentAttribute implements FeatureAttributeAdapter {

    private static final Object[] EMPTY_ARRAY = new Object[0];

    private String name;
    private FastMethod readMethod;
    private FastMethod writeMethod;

    public ObjectFeatureAgentAttribute(String name, FastMethod readMethod,
                                       FastMethod writeMethod) {
      this.name = name;
      this.readMethod = readMethod;
      this.writeMethod = writeMethod;
    }

    public String getAttributeName() {
      return name;
    }

    public Object getAttribute(Object adaptee) {
      try {
        return readMethod.invoke(adaptee, EMPTY_ARRAY);
      } catch (InvocationTargetException e) {
        MessageCenter.getMessageCenter(getClass()).error(
                "Unable to get feature attribute from object", e);
        // todo error center
      }
      return new NULL();
    }

    public void setAttribute(Object adaptee, Object value) throws IllegalAttributeException {
      if (writeMethod == null) {
        throw new IllegalAttributeException(
                "Unable to set attribute: write method does not exist");
      }
      try {
        writeMethod.invoke(adaptee, new Object[]{value});
      } catch (InvocationTargetException e) {
        IllegalAttributeException ex = new IllegalAttributeException(
                "Unable to set attribute: " + e.getMessage());
        ex.initCause(e);
        throw ex;
      }
    }

    public Class<?> getAttributeType() {
      return readMethod.getReturnType();
    }
  }

  /**
   * Creates a FeatureAgent2 from the specified FeatureType, agent and geography.
   * The geometry of the feature will be supplied by the geography. The agent's properties
   * will become attributes of the feature.
   *
   * @param type
   * @param agent
   * @param geography
   */
  public FeatureAgent2(FeatureType type, T agent, Geography geography) {
    this(type, agent, geography,  new ArrayList<FeatureAttributeAdapter>());

  }

  /**
   * Creates a FeatureAgent2 from the specified FeatureType, agent and geography.
   * The geometry of the feature will be supplied by the geography. The agent's properties
   * will become attributes of the feature. The list of adapters will become additional
   * attributes for those that cannot be mapped directly to an agents properties.
   *
   * @param type
   * @param agent
   * @param geography
   * @param adapters
   */
  public FeatureAgent2(FeatureType type, T agent, Geography geography, List<FeatureAttributeAdapter> adapters) {
    this.agentObj = agent;
    this.geography = geography;
    geom = geography.getGeometry(agent);
    createAttributes(adapters);
    // create the id as in geotools DefaultFeature.
    id = "fid-" + (new UID()).toString();
    this.schema = type;
  }

  // creates the feature attributes from object properties
  private void createAttributes(List<FeatureAttributeAdapter> adapters) {
    try {
      BeanInfo info = Introspector.getBeanInfo(agentObj.getClass(),
              Object.class);
      PropertyDescriptor[] pds = info.getPropertyDescriptors();
      for (int i = 0; i < pds.length; i++) {
        PropertyDescriptor pd = pds[i];
        String featureName = pd.getName();
        FastClass fastClass = FastClass.create(agentObj.getClass());
        if (pd.getReadMethod() != null) {
          FastMethod readMethod = fastClass.getMethod(pd.getReadMethod());
          FastMethod writeMethod = pd.getWriteMethod() == null ? null
                  : fastClass.getMethod(pd.getWriteMethod());
          attributeMap.put(featureName, new ObjectFeatureAgentAttribute(featureName, readMethod, writeMethod));
        }
      }

      for (FeatureAttributeAdapter adapter : adapters) {
        attributeMap.put(adapter.getAttributeName(), adapter);
      }
    } catch (IntrospectionException e) {
      msgCenter
              .error("Unable to create feature attributes from agent", e);
      // todo error center
    }
  }

  /**
   * Gets the agent that this FeatureAgent wraps.
   *
   * @return the agent that this FeatureAgent wraps.
   */
  public T getAgent() {
    return agentObj;
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
   * @param featureCollection the collection to be set as parent.
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
   * @param objects The array to copy the attributes into.
   * @return The array passed in, or a new one if null.
   */
  public Object[] getAttributes(Object[] objects) {
    if (objects == null) {
      objects = new Object[schema.getAttributeCount()];
    }

    // need to do in schema order
    // - 1 because the geometry is not in the attribute map
    for (int i = 0, n = schema.getAttributeCount(); i < n; i++) {
      String name = getAttributeName(i);
      FeatureAttributeAdapter attribute = attributeMap.get(name);
      if (name.equals("the_geom")) objects[i] = getDefaultGeometry();
      else objects[i] = attribute.getAttribute(agentObj);
    }
    return objects;
  }

  /**
   * Gets an attribute for this feature at the location specified by xPath.
   *
   * @param xPath XPath representation of attribute location.
   * @return Attribute.
   */
  public Object getAttribute(String xPath) {
    if (xPath.equals("the_geom")) {
      return getDefaultGeometry();
    }
    FeatureAttributeAdapter getter = attributeMap.get(xPath);
    if (getter != null)
      return getter.getAttribute(agentObj);
    return new NULL();
  }

  /**
   * Gets an attribute by the given zero-based index.
   *
   * @param index the position of the attribute to retrieve.
   * @return The attribute at the given index.
   */
  public Object getAttribute(int index) {
    String name = getAttributeName(index);
    if (name.equals("the_geom")) {
      return getDefaultGeometry();
    }
    if (name != null) {
      FeatureAttributeAdapter getter = attributeMap.get(name);
      if (getter != null)
        return getter.getAttribute(agentObj);
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
   * @param index the index of the attribute to set.
   * @param val   the new value to give the attribute at position.
   * @throws org.geotools.feature.IllegalAttributeException
   *          if the passed in val does not validate against the
   *          AttributeType at that position.
   */
  public void setAttribute(int index, Object val)
          throws IllegalAttributeException, ArrayIndexOutOfBoundsException {
    String name = getAttributeName(index);
    if (name.equals("the_geom")) {
      setDefaultGeometry((Geometry) val);
      return;
    }
    FeatureAttributeAdapter setter = attributeMap.get(name);
    if (setter != null)
      setter.setAttribute(agentObj, val);
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
   * @param xPath XPath representation of attribute location.
   * @param obj   the attribute value to set.
   * @throws org.geotools.feature.IllegalAttributeException
   *          if passed attribute does not match feature type
   */
  public void setAttribute(String xPath, Object obj)
          throws IllegalAttributeException {
    if (xPath.equals("the_geom")) {
      setDefaultGeometry((Geometry) obj);
      return;
    }
    FeatureAttributeAdapter setter = attributeMap.get(xPath);
    if (setter != null)
      setter.setAttribute(agentObj, obj);
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
    // this might be called after the agent is removed
    // from a geography so we need to go through these
    // hoops
    Geometry tmp = geography.getGeometry(agentObj);
    if (tmp == null) return (Geometry)geom.clone();
    else {
      geom = tmp;
    }
    return (Geometry)geom.clone();
  }

  /**
   * Modifies the geometry.
   *
   * @param geometry for all feature attributes.
   * @throws org.geotools.feature.IllegalAttributeException
   *          if the feature does not have a geometry.
   */
  public void setDefaultGeometry(Geometry geometry)
          throws IllegalAttributeException {
    geography.move(agentObj, geometry);
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
   * @param obj the Object to test for equality.
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

    if (!(obj instanceof FeatureAgent2)) {
      return false;
    }

    FeatureAgent2 other = (FeatureAgent2) obj;
    if (!other.getFeatureType().equals(schema)) {
      return false;
    }

    if (!id.equals(other.getID())) {
      return false;
    }

    if (!other.agentObj.equals(agentObj))
			return false;

		return true;
	}
}

