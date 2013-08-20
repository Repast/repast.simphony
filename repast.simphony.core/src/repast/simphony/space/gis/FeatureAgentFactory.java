package repast.simphony.space.gis;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import simphony.util.messages.MessageCenter;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Abstract factory for adapting agents to features. This creates a FeatureType
 * given an agent class.
 * 
 * TODO Geotools [minor] - this class would benefit from cleanup since there's lots of
 *      duplicate code, along with subclasses: 
 *      		ShapefileFeatureAgentFactory
 *      		DefaultFeatureAgentFactory
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public abstract class FeatureAgentFactory<T> {

	/*
	 * The default name for the geometry attribute - an arbitrary string.  Note
	 *  that Feature attribute names may not contain spaces or periods. 
	 */
	public static final String GEOM_ATTRIBUTE_NAME = "Location";
	MessageCenter msg = MessageCenter.getMessageCenter(getClass());	
	private Map<Class, SimpleFeatureType> types = new HashMap<Class, SimpleFeatureType>();
	private Map<Class, Class> primitiveMap = new HashMap<Class, Class>();
	private Set<Class> legalShapefileAttribs = new HashSet<Class>();
	protected List<ObjectClassAttributeData> classAttributeList;
	
	public abstract FeatureAgent getFeature(T agent, Geography geography);
	
	public FeatureAgentFactory() {
		primitiveMap.put(double.class, Double.class);
		primitiveMap.put(int.class, Integer.class);
		primitiveMap.put(float.class, Float.class);
		primitiveMap.put(long.class, Long.class);
		primitiveMap.put(short.class, Short.class);
		primitiveMap.put(byte.class, Byte.class);
		primitiveMap.put(boolean.class, Boolean.class);

		legalShapefileAttribs = new HashSet<Class>();
		legalShapefileAttribs.addAll(primitiveMap.keySet());
		legalShapefileAttribs.addAll(primitiveMap.values());
		legalShapefileAttribs.add(String.class);
	}

	/**
	 * Gets a feature type appropriate for writing to shapefile.
	 * 
	 * @param agentClass
	 * @param coordRefSystem
	 * @param geomClass
	 * @return
	 * @throws IntrospectionException
	 * @throws SchemaException
	 */
	protected SimpleFeatureType getShapefileFeatureType(Class agentClass,
			CoordinateReferenceSystem coordRefSystem,
			Class<? extends Geometry> geomClass) throws IntrospectionException,
			SchemaException {
		return getShapefileFeatureType(agentClass, coordRefSystem, geomClass,
				new ArrayList());
	}

	// returns true if the types list contains an attribute with the
	// specified name.
	private boolean containsTypeName(List<AttributeType> types, String name) {
		for (AttributeType type : types) {
			if (type.getName().equals(name))
				return true;
		}

		return false;
	}

	/**
	 * Gets a feature type appropriate for writing to shapefile.
	 * 
	 * @param agentClass
	 * @param coordRefSystem
	 * @param geomClass
	 * @return
	 * @throws IntrospectionException
	 * @throws SchemaException
	 */
	protected SimpleFeatureType getShapefileFeatureType(Class agentClass,
			CoordinateReferenceSystem coordRefSystem,
			Class<? extends Geometry> geomClass,
			List<FeatureAttributeAdapter> adapters) throws IntrospectionException,
			SchemaException {
		BeanInfo info = Introspector.getBeanInfo(agentClass, Object.class);
		PropertyDescriptor[] pds = info.getPropertyDescriptors();
		List<AttributeDescriptor> ats = new ArrayList<AttributeDescriptor>();

		AttributeTypeBuilder builder = new AttributeTypeBuilder();
		for (int i = 0; i < pds.length; i++) {
			String fieldName = pds[i].getName();

			Method method = pds[i].getReadMethod();
			if (method != null) {
				Class<?> returnType = method.getReturnType();
				Class retType = primitiveMap.get(returnType);
				if (retType == null)
					retType = returnType;
				if (legalShapefileAttribs.contains(retType)) {
					builder.setBinding(retType);
					builder.setNillable(true);
					ats.add(builder.buildDescriptor(fieldName));
				}
			}
		}

		for (FeatureAttributeAdapter adapter : adapters) {
			Class<?> returnType = adapter.getAttributeType();
			Class retType = primitiveMap.get(returnType);
			if (retType == null)
				retType = returnType;
			if (legalShapefileAttribs.contains(retType)) {
				builder.setBinding(retType);
				builder.setNillable(true);
				ats.add(builder.buildDescriptor(adapter.getAttributeName()));
			}
		}
			
		SimpleFeatureTypeBuilder ftBuilder = new SimpleFeatureTypeBuilder();
		String featureTypeName = agentClass.getName() + ".FeatureType";
		ftBuilder.setName(featureTypeName);
		ftBuilder.setCRS(coordRefSystem);
		
		// The FeatureType Geometry class is the first attribute added
		ftBuilder.add(GEOM_ATTRIBUTE_NAME, geomClass);
		
		for (AttributeDescriptor ad : ats){
		  ftBuilder.add(ad);
		}
				
		return ftBuilder.buildFeatureType();
	}

	protected SimpleFeatureType getFeatureType(Class agentClass,
			CoordinateReferenceSystem coordRefSystem,
			Class<? extends Geometry> geomClass) throws IntrospectionException,
			SchemaException {
		return getFeatureType(agentClass, coordRefSystem, geomClass,
				new ArrayList());
	}

	protected SimpleFeatureType getFeatureType(Class agentClass,
			CoordinateReferenceSystem coordRefSystem,
			Class<? extends Geometry> geomClass,
			List<FeatureAttributeAdapter> adapters) throws IntrospectionException,
			SchemaException {
		SimpleFeatureType type = types.get(agentClass);
		if (type != null)
			return type;
		
		// if get here then we need to create our own type
		BeanInfo info = Introspector.getBeanInfo(agentClass, Object.class);
		PropertyDescriptor[] pds = info.getPropertyDescriptors();
		List<AttributeDescriptor> ats = new ArrayList<AttributeDescriptor>();
		AttributeTypeBuilder builder = new AttributeTypeBuilder();

		for (int i = 0; i < pds.length; i++) {
			String fieldName = pds[i].getName();
			
			Method method = pds[i].getReadMethod();
			if (method != null) {

				Class<?> returnType = method.getReturnType();
				Class retType = primitiveMap.get(returnType);
				if (retType == null)
					retType = returnType;
				builder.setBinding(retType);
				builder.setNillable(true);
				ats.add(builder.buildDescriptor(fieldName));
			}
		}

		for (FeatureAttributeAdapter adapter : adapters) {
			Class<?> returnType = adapter.getAttributeType();
			Class retType = primitiveMap.get(returnType);
			if (retType == null)
				retType = returnType;
			builder.setBinding(retType);
			builder.setNillable(true);
			ats.add(builder.buildDescriptor(adapter.getAttributeName()));
		}

		SimpleFeatureTypeBuilder ftBuilder = new SimpleFeatureTypeBuilder();
		String featureTypeName = agentClass.getName() + ".FeatureType";
		ftBuilder.setName(featureTypeName);
		ftBuilder.setCRS(coordRefSystem);
		
		// !! IMPORTANT !!  The default Geometry class must be the first attribute added
		ftBuilder.add(GEOM_ATTRIBUTE_NAME, geomClass);
		
		for (AttributeDescriptor ad : ats){
			ftBuilder.add(ad);
		}
		
		type = ftBuilder.buildFeatureType();
		types.put(agentClass, type);
		
		return type;
	}
	
	/**
   * Hold method read / write info for agent class.
   * 
   *
   */
  static class ObjectClassAttributeData {
		
  	private String name;
		private FastMethod readMethod;
		private FastMethod writeMethod;
  	
  	public ObjectClassAttributeData(String name, FastMethod readMethod, 
  			FastMethod writeMethod){
  		
  		this.name = name;
  		this.readMethod = readMethod;
  		this.writeMethod = writeMethod;
  	}
  	
  	public String getName() {
			return name;
		}

		public FastMethod getReadMethod() {
			return readMethod;
		}

		public FastMethod getWriteMethod() {
			return writeMethod;
		}
  }
  
  /**
   * Create a list of class attributes for the FeatureAgent
   * 
   * @param agentClass
   */
	protected void createClassAttributes(Class<T> agentClass) {
		classAttributeList = new ArrayList<ObjectClassAttributeData>();
		try {
			BeanInfo info = Introspector.getBeanInfo(agentClass,	Object.class);
			PropertyDescriptor[] pds = info.getPropertyDescriptors();
			FastClass fastClass = FastClass.create(agentClass);
			
			for (int i = 0; i < pds.length; i++) {
				PropertyDescriptor pd = pds[i];
				String featureName = pd.getName();
				
				if (pd.getReadMethod() != null) {
					FastMethod readMethod = fastClass.getMethod(pd.getReadMethod());
					FastMethod writeMethod = pd.getWriteMethod() == null ? null
							: fastClass.getMethod(pd.getWriteMethod());
					
					classAttributeList.add(new ObjectClassAttributeData(featureName, 
							readMethod, writeMethod));
				}
			}
		} catch (IntrospectionException e) {
			msg.error("Unable to create feature attributes from agent", e);
		}
	}
}
