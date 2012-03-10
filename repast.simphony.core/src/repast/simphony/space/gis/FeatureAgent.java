package repast.simphony.space.gis;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.BoundingBox;

import simphony.util.messages.MessageCenter;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Wraps a POJO in a gis feature. The POJO's properties become feature
 * attributes. The feature's geometry is derived from the POJO and a Geography.
 * 
 * @author Nick Collier
 */
public class FeatureAgent<T> implements SimpleFeature {

	private static MessageCenter msgCenter = MessageCenter
			.getMessageCenter(FeatureAgent.class);

	protected FeatureId fid;
	
	private T agentObj;

	private Map<String, FeatureAttributeAdapter> attributeMap = new HashMap<String, FeatureAttributeAdapter>();

	private SimpleFeatureType featureType;

	protected String geometryName;
	
	private Geometry geom;

	private Geography geography;
	
	/**
   * The set of user data attached to the feature (lazily created)
   */
  protected Map<Object, Object> userData;

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
			return null;
		}

		public void setAttribute(Object adaptee, Object value) {
			try {
				writeMethod.invoke(adaptee, new Object[] { value });
			} catch (InvocationTargetException e) {
				msgCenter.error("Unable to set attribute: ", e);
			}
		}

		public Class<?> getAttributeType() {
			return readMethod.getReturnType();
		}
	}

	/**
	 * Creates a FeatureAgent2 from the specified FeatureType, agent and
	 * geography. The geometry of the feature will be supplied by the geography.
	 * The agent's properties will become attributes of the feature.
	 * 
	 * @param type
	 * @param agent
	 * @param geography
	 */
	public FeatureAgent(SimpleFeatureType type, T agent, Geography geography) {
		this(type, agent, geography, new ArrayList<FeatureAttributeAdapter>());

	}

	/**
	 * Creates a FeatureAgent2 from the specified FeatureType, agent and
	 * geography. The geometry of the feature will be supplied by the geography.
	 * The agent's properties will become attributes of the feature. The list of
	 * adapters will become additional attributes for those that cannot be mapped
	 * directly to an agents properties.
	 * 
	 * @param type
	 * @param agent
	 * @param geography
	 * @param adapters
	 */
	public FeatureAgent(SimpleFeatureType type, T agent, Geography geography,
			List<FeatureAttributeAdapter> adapters) {
		this.agentObj = agent;
		this.geography = geography;
		geom = geography.getGeometry(agent);
		createAttributes(adapters);
		// create the id as in geotools DefaultFeature.
		String id = "fid-" + (new UID()).toString();
		fid = new FeatureIdImpl(id);
		this.featureType = type;
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
					attributeMap.put(featureName, new ObjectFeatureAgentAttribute(
							featureName, readMethod, writeMethod));
				}
			}

			for (FeatureAttributeAdapter adapter : adapters) {
				attributeMap.put(adapter.getAttributeName(), adapter);
			}
		} catch (IntrospectionException e) {
			msgCenter.error("Unable to create feature attributes from agent", e);
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
	 * Gets a reference to the feature type for this feature.
	 * 
	 * @return A copy of this feature's metadata in the form of a feature type
	 *         
	 */
	public SimpleFeatureType getFeatureType() {
		return featureType;
	}

	/**
	 * Gets the unique indentification string of this Feature.
	 * 
	 * @return The unique id.
	 */
	public String getID() {
		return fid.getID();
	}
	
	public SimpleFeatureType getType() {
		return featureType;
	}
	
	public FeatureId getIdentifier() {
		return fid;
	}
	
	public Name getName() {
		return featureType.getName();
	}

	public List<Object> getAttributes() {
		List<Object> objects = new ArrayList<Object>();
		
		// The first object in the list should be the Geometry
		objects.add(0, getDefaultGeometry());
		
		// Add remaining attributes
		for (int i = 1, n = featureType.getAttributeCount(); i < n; i++) {
			String name = getAttributeName(i);
			FeatureAttributeAdapter attribute = attributeMap.get(name);
		  objects.add(attribute.getAttribute(agentObj));
		}
		
		return objects;
	}

	/**
	 * Gets an attribute for this feature at the location specified by xPath.
	 * 
	 * @param name
	 *          name of attribute
	 * @return Attribute.
	 */
	public Object getAttribute(String name) {
		
		if (name.equals(geometryName)) 
			return getDefaultGeometry();
		
		FeatureAttributeAdapter getter = attributeMap.get(name);
		if (getter != null)
			return getter.getAttribute(agentObj);
		return null;
	}

	public int getAttributeCount() {
	  return featureType.getAttributeCount();
	}
	
	/**
	 * Gets an attribute by the given zero-based index.
	 * 
	 * @param index
	 *          the position of the attribute to retrieve.
	 * @return The attribute at the given index.
	 */
	public Object getAttribute(int index) {
		String name = getAttributeName(index);
		
		// index 0 always returns Geometry
		if (index == 0) 
			return getDefaultGeometry();
		
		if (name != null) {
			FeatureAttributeAdapter getter = attributeMap.get(name);
			if (getter != null)
				return getter.getAttribute(agentObj);
		}
		return null;
	}
	
	public Object getAttribute(Name name) {
    return getAttribute( name.getLocalPart() );
  }

	private String getAttributeName(int i) {
		String name = null;
		AttributeType type = featureType.getType(i);
		if (type != null)
			name = type.getName().getLocalPart();
		return name;
	}

	/**
	 * Sets the attribute at position to val.
	 * 
	 * @param index
	 *          the index of the attribute to set.
	 * @param val
	 *          the new value to give the attribute at position.
	 * @throws org.geotools.feature.IllegalAttributeException
	 *           if the passed in val does not validate against the AttributeType
	 *           at that position.
	 */
	public void setAttribute(int index, Object val) throws IndexOutOfBoundsException {
		String name = getAttributeName(index);
		
		// index 0 always is Geometry
		if (index == 0) {
			setDefaultGeometry((Geometry) val);
			return;
		}
		FeatureAttributeAdapter setter = attributeMap.get(name);
		if (setter != null)
			setter.setAttribute(agentObj, val);
		else
			throw new IndexOutOfBoundsException("Unable to set attribute '" + name
					+ "', attribute not found");
	}


	/**
	 * Sets a single attribute for this feature, passed as a complex object. If
	 * the attribute does not exist or the object does not conform to the internal
	 * feature type, an exception is thrown.
	 * 
	 * @param name
	 *          name of attribute 
	 * @param obj
	 *          the attribute value to set.
	 * @throws org.geotools.feature.IllegalAttributeException
	 *           if passed attribute does not match feature type
	 */
	public void setAttribute(String name, Object obj){
		if (obj instanceof Geometry) {
			geometryName = name;
			setDefaultGeometry((Geometry) obj);
			return;
		}
		FeatureAttributeAdapter setter = attributeMap.get(name);
		if (setter != null)
			setter.setAttribute(agentObj, obj);
	}

	public void setAttribute(Name name, Object value) {
		setAttribute( name.getLocalPart(), value );
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
		if (tmp == null)
			return (Geometry) geom.clone();
		else {
			geom = tmp;
		}
		return (Geometry) geom.clone();
	}

	/**
	 * Modifies the geometry.
	 * 
	 * @param geometry
	 *          for all feature attributes.
	 * @throws org.geotools.feature.IllegalAttributeException
	 *           if the feature does not have a geometry.
	 */
	public void setDefaultGeometry(Object geometry){
		geography.move(agentObj, (Geometry)geometry);
	}

	public BoundingBox getBounds() {
		ReferencedEnvelope bounds = new ReferencedEnvelope(
				featureType.getCoordinateReferenceSystem());

		if (bounds.isNull()) {
			bounds.init(geom.getEnvelopeInternal());
		} else {
			bounds.expandToInclude(geom.getEnvelopeInternal());
		}

		return bounds;
	}

	public int hashCode() {
		return fid.hashCode() * featureType.hashCode();
	}

	/**
	 * override of equals. Returns if the passed in object is equal to this.
	 * 
	 * @param obj
	 *          the Object to test for equality.
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
		if (!other.getFeatureType().equals(featureType)) {
			return false;
		}

		if (!fid.equals(other.getID())) {
			return false;
		}

		if (!other.agentObj.equals(agentObj))
			return false;

		return true;
	}
	
	public boolean isNillable() {
		return true;
	}

	// TODO Auto-generated method stubs from SimpleFeature interface inheritance

	public GeometryAttribute getDefaultGeometryProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDefaultGeometryProperty(GeometryAttribute arg0) {
		// TODO Auto-generated method stub
	}

	public Collection<Property> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Property> getProperties(Name arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Property> getProperties(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Property getProperty(Name arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Property getProperty(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<? extends Property> getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setValue(Collection<Property> arg0) {
		// TODO Auto-generated method stub
	}

	public void validate() throws IllegalAttributeException {
		// TODO Auto-generated method stub
	}

	public AttributeDescriptor getDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	 public Map<Object, Object> getUserData() {
     if(userData == null)
         userData = new HashMap<Object, Object>();
     return userData;
 }

	public void setValue(Object arg0) {
		// TODO Auto-generated method stub
	}

 public void setAttributes(List<Object> values) {
		// TODO Auto-generated method stub
 }

	public void setAttributes(Object[] values) {
		// TODO Auto-generated method stub
  }
}
