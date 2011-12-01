package repast.simphony.space.gis;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.feature.*;
import org.geotools.feature.type.GeometricAttributeType;
import org.geotools.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Abstract factory for adapting agents to features. This
 * creates a FeatureType given an agent class.
 *
 * @author Nick Collier
 * @version $Revision: 1.4 $ $Date: 2006/06/09 19:08:02 $
 */
public abstract class FeatureAgentFactory {

  private Map<Class, FeatureType> types = new HashMap<Class, FeatureType>();

  private Map<Class, Class> primitiveMap = new HashMap<Class, Class>();
  private Set<Class> legalShapefileAttribs = new HashSet<Class>();

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
  protected FeatureType getShapefileFeatureType(Class agentClass,
                                                CoordinateReferenceSystem coordRefSystem,
                                                Class<? extends Geometry> geomClass) throws IntrospectionException,
          SchemaException {
    return getShapefileFeatureType(agentClass, coordRefSystem, geomClass, new ArrayList());
  }

  // returns true if the types list contains an attribute with the
  // specified name.
  private boolean containsTypeName(List<AttributeType> types, String name) {
    for (AttributeType type : types) {
      if (type.getName().equals(name)) return true;
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
  protected FeatureType getShapefileFeatureType(Class agentClass,
                                                CoordinateReferenceSystem coordRefSystem,
                                                Class<? extends Geometry> geomClass,
                                                List<FeatureAttributeAdapter> adapters) throws IntrospectionException,
          SchemaException {
    BeanInfo info = Introspector.getBeanInfo(agentClass, Object.class);
    PropertyDescriptor[] pds = info.getPropertyDescriptors();
    List<AttributeType> types = new ArrayList<AttributeType>();


    for (int i = 0; i < pds.length; i++) {
      String featureName = pds[i].getName();

      Method method = pds[i].getReadMethod();
      if (method != null) {
        Class<?> returnType = method.getReturnType();
        Class retType = primitiveMap.get(returnType);
        if (retType == null) retType = returnType;
        if (legalShapefileAttribs.contains(retType)) {
          types.add(AttributeTypeFactory.newAttributeType(featureName,
                  retType, true));
        }
      }
    }

    for (FeatureAttributeAdapter adapter : adapters) {
      Class<?> returnType = adapter.getAttributeType();
      Class retType = primitiveMap.get(returnType);
      if (retType == null) retType = returnType;
      if (legalShapefileAttribs.contains(retType)) {
        types.add(AttributeTypeFactory.newAttributeType(adapter.getAttributeName(),
                retType, true));
      }
    }


    GeometricAttributeType geomType = new GeometricAttributeType("the_geom", geomClass,
            true, null, coordRefSystem, Filter.NONE);
    types.add(geomType);
    AttributeType[] ats = types.toArray(new AttributeType[types.size()]);
    FeatureTypeBuilder builder = FeatureTypeFactory.newInstance(agentClass.getName()
            + ".FeatureType");
    builder.addTypes(ats);
    builder.setDefaultGeometry(geomType);
    return builder.getFeatureType();
  }

  protected FeatureType getFeatureType(Class agentClass,
                                       CoordinateReferenceSystem coordRefSystem,
                                       Class<? extends Geometry> geomClass) throws IntrospectionException,
          SchemaException {
    return getFeatureType(agentClass, coordRefSystem, geomClass, new ArrayList());
  }

  protected FeatureType getFeatureType(Class agentClass,
                                       CoordinateReferenceSystem coordRefSystem,
                                       Class<? extends Geometry> geomClass, List<FeatureAttributeAdapter> adapters) throws IntrospectionException,
          SchemaException {
    FeatureType type = types.get(agentClass);
    if (type != null)
      return type;

    // if get here then we need to create our own type
    BeanInfo info = Introspector.getBeanInfo(agentClass, Object.class);
    PropertyDescriptor[] pds = info.getPropertyDescriptors();
    List<AttributeType> ats = new ArrayList<AttributeType>();
    //AttributeType[] ats = new AttributeType[pds.length + 1];

    for (int i = 0; i < pds.length; i++) {
      String featureName = pds[i].getName();

      Method method = pds[i].getReadMethod();
      if (method != null) {


        Class<?> returnType = method.getReturnType();
        Class retType = primitiveMap.get(returnType);
        if (retType == null)
          retType = returnType;
        ats.add(AttributeTypeFactory.newAttributeType(featureName,
                retType, true));
      }
    }

    for (FeatureAttributeAdapter adapter : adapters) {
      Class<?> returnType = adapter.getAttributeType();
      Class retType = primitiveMap.get(returnType);
      if (retType == null) retType = returnType;
      ats.add(AttributeTypeFactory.newAttributeType(adapter.getAttributeName(),
              retType, true));
    }

    GeometricAttributeType geomType = new GeometricAttributeType("the_geom", geomClass,
            true, null, coordRefSystem, Filter.NONE);
    ats.add(geomType);
    FeatureTypeBuilder builder = FeatureTypeFactory.newInstance(agentClass.getName()
            + ".FeatureType");
    builder.addTypes(ats.toArray(new AttributeType[ats.size()]));
    builder.setDefaultGeometry(geomType);
    //type = FeatureTypeBuilder.newFeatureType(ats.toArray(new AttributeType[ats.size()]), agentClass.getName()
    //       + ".FeatureType");
    type = builder.getFeatureType();
    types.put(agentClass, type);
    return type;
  }
}
