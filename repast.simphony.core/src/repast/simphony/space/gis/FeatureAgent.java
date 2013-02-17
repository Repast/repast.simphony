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

import org.geotools.feature.GeometryAttributeImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.BoundingBox;

import simphony.util.messages.MessageCenter;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Wraps a POJO in a GIS feature. The POJO's properties become feature
 * attributes. The feature's geometry is derived from the POJO and a Geography.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class FeatureAgent<T> implements SimpleFeature {

	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(FeatureAgent.class);

	protected FeatureId id;
  protected SimpleFeatureType featureType;
	private T agentObj;
  private Map<String, FeatureAttributeAdapter> attributeMap;
	protected String geometryName;
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
	 * Creates a FeatureAgent from the specified FeatureType, agent and
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
	 * Creates a FeatureAgent from the specified FeatureType, agent and
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
	public FeatureAgent(SimpleFeatureType type, T agent, Geography geog,
			List<FeatureAttributeAdapter> adapters) {
				
		id = new FeatureIdImpl("fid-" + new UID().toString().replace(':', '_'));
		featureType = type;
		agentObj = agent;
		geography = geog;
		geom = geography.getGeometry(agent);
//		geometryName = type.getGeometryDescriptor().getName().getLocalPart();
	  geometryName = FeatureAgentFactory.GEOM_ATTRIBUTE_NAME;
		
		createAttributes(adapters);
	}

	// creates the feature attributes from object properties
	private void createAttributes(List<FeatureAttributeAdapter> adapters) {
		attributeMap = new HashMap<String, FeatureAttributeAdapter>();
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

	private String getAttributeName(int i) {
		String name = null;
		AttributeType type = featureType.getType(i);
		if (type != null)
			name = type.getName().getLocalPart();
		return name;
	}
		
	/**
   * @see SimpleFeature#getID()
   */
	public String getID(){
		return id.getID();
	}

	/**
   * @see SimpleFeature#getType()
   */
	public SimpleFeatureType getType(){
		return getFeatureType();
	}
	
	/**
   * @see SimpleFeature#getFeatureType()
   */
	public SimpleFeatureType getFeatureType(){
		return featureType;
	}
	
	/**
   * @see Feature#getIdentifier()
   */
	public FeatureId getIdentifier(){
		return id;
	}
	
	/**
   * @see SimpleFeature#getAttributes()
   */
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
   * @see SimpleFeature#getAttribute(String)
   */
	public Object getAttribute(String name) {
		Object obj = null;
		if (name.equals(geometryName)) 
			obj = getDefaultGeometry();
		
		FeatureAttributeAdapter getter = attributeMap.get(name);
		if (getter != null)
			obj = getter.getAttribute(agentObj);
		
		return obj;
	}
	
	/**
   * @see SimpleFeature#setAttribute(String, Object)
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

	/**
   * @see SimpleFeature#getAttribute(Name)
   */
	public Object getAttribute(Name name) {
    return getAttribute( name.getLocalPart() );
  }
	
	/**
   * @see SimpleFeature#setAttribute(Name, Object)
   */
	public void setAttribute(Name name, Object value) {
		setAttribute( name.getLocalPart(), value );
	}
	
	/**
   * @see SimpleFeature#getAttribute(int)
   */
	public Object getAttribute(int index) {
		Object obj = null;	
		String name = getAttributeName(index);
		
		// index 0 always returns Geometry
		if (index == 0) 
			obj = getDefaultGeometry();
		
		if (name != null) {
			FeatureAttributeAdapter getter = attributeMap.get(name);
			if (getter != null)
				obj = getter.getAttribute(agentObj);
		}
		
		return obj;
	}
	
	/**
   * @see SimpleFeature#setAttribute(int, Object)
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
   * @see SimpleFeature#getAttributeCount()
   */
	public int getAttributeCount() {
	  return featureType.getAttributeCount();
	}
	
	/**
   * @see SimpleFeature#getDefaultGeometry()
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
   * @see SimpleFeature#setDefaultGeometry(Object)
   */
	public void setDefaultGeometry(Object geometry){
		geography.move(agentObj, (Geometry)geometry);
	}
	
	/**
	 * @see Feature#getBounds()
	 */
	public BoundingBox getBounds() {
		//TODO: cache this value
		ReferencedEnvelope bounds = new ReferencedEnvelope(
				featureType.getCoordinateReferenceSystem());

		if ( bounds.isNull() ) {
			bounds.init(geom.getEnvelopeInternal());
		}
		else {
			bounds.expandToInclude(geom.getEnvelopeInternal());
		}
		return bounds;
	}

	/**
	 * @see Feature#getDefaultGeometryProperty()
	 */
	public GeometryAttribute getDefaultGeometryProperty() {
		GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
		GeometryAttribute geometryAttribute = null;
		if(geometryDescriptor != null){
			Object defaultGeometry = getDefaultGeometry();
			geometryAttribute = new GeometryAttributeImpl(defaultGeometry, geometryDescriptor, null);            
		}
		return geometryAttribute;
	}

	/**
	 * @see Feature#setDefaultGeometryProperty(GeometryAttribute)
	 */
	public void setDefaultGeometryProperty(GeometryAttribute geometryAttribute) {
		if(geometryAttribute != null)
			setDefaultGeometry(geometryAttribute.getValue());
		else
			setDefaultGeometry(null);
	}

	/**
	 * @see Property#isNillable()
	 */
	public boolean isNillable() {
		return true;
	}
	
	/**
   * @see Property#getName()
   */
  public Name getName() {
      return featureType.getName();
  }
	
  /**
   * @see org.opengis.feature.Attribute#getDescriptor()
   */
  public AttributeDescriptor getDescriptor() {
      return new AttributeDescriptorImpl(featureType, featureType.getName(), 0,
              Integer.MAX_VALUE, true, null);
  }
	
	//**************************************************************************
	//
	// The method stubs are not used for the Repast implementation of SimpleFeature,
  //  however they may be necessary to implement if the FeatureAgent is used
  //  with any GeoTools API that requires them.
  //
	//  TODO determine what stubs are required to be implemented
  //
	//**************************************************************************

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

	public Map<Object, Object> getUserData() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setValue(Object arg0) {
		// TODO Auto-generated method stub
	}

	public void setAttributes(List<Object> arg0) {
		// TODO Auto-generated method stub
	}

	public void setAttributes(Object[] arg0) {
		// TODO Auto-generated method stub
	}
}