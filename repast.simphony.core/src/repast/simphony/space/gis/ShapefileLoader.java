package repast.simphony.space.gis;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.*;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.FactoryFinder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.OperationNotFoundException;
import org.opengis.referencing.operation.TransformException;
import repast.simphony.context.Context;
import simphony.util.messages.MessageCenter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates and sets agents properties from a features in shapefile.
 *
 * @author Nick Collier
 */
public class ShapefileLoader<T> {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(ShapefileLoader.class);
  private static Map<Class, Class> primToObject = new HashMap<Class, Class>();

  static {
    primToObject.put(int.class, Integer.class);
    primToObject.put(long.class, Long.class);
    primToObject.put(double.class, Double.class);
    primToObject.put(float.class, Float.class);
    primToObject.put(boolean.class, Boolean.class);
    primToObject.put(byte.class, Byte.class);
    primToObject.put(char.class, Character.class);
  }

  private MathTransform transform;
  private Geography geography;
  private Context context;
  private Map<String, Method> attributeMethodMap = new HashMap<String, Method>();
  private Class agentClass;
  private FeatureIterator iter;

  /**
   * Creates a shapefile loader for agents of the specified
   * class and whose data source is the specified shapefile. The
   * agents will be placed into the specified Geography according to
   * the geometry specified in the shapefile and transformed according
   * to the geography's CRS.
   *
   * @param clazz     the agent class
   * @param shapefile the shapefile that serves as the datasource for the agent
   *                  properties
   * @param geography the geography to hold spatial locations of the agents
   * @param context   the context to add the agents to
   */
  public ShapefileLoader(Class<T> clazz, URL shapefile, Geography geography, Context context) {
    this.geography = geography;
    this.agentClass = clazz;
    this.context = context;
    try {
      BeanInfo info = Introspector.getBeanInfo(clazz, Object.class);
      Map<String, Method> methodMap = new HashMap<String, Method>();
      PropertyDescriptor[] pds = info.getPropertyDescriptors();
      for (PropertyDescriptor pd : pds) {
        if (pd.getWriteMethod() != null) {
          methodMap.put(pd.getName().toLowerCase(), pd.getWriteMethod());
        }
      }

      ShapefileDataStore store = new ShapefileDataStore(shapefile);
      FeatureType schema = store.getSchema(store.getTypeNames()[0]);
      for (int i = 0, n = schema.getAttributeCount(); i < n; i++) {
        AttributeType type = schema.getAttributeType(i);
        String name = type.getName();
        if (name.equals("the_geom")) {
          initTransform(geography, type);
        } else {
          Method method = methodMap.get(name.toLowerCase());
          if (method == null) method = methodMap.get(name.replace("_", "").toLowerCase());
          if (method != null && isCompatible(method.getParameterTypes()[0], (type.getType()))) {
            attributeMethodMap.put(name, method);
          }
        }
      }
      iter = store.getFeatureSource().getFeatures().features();
    } catch (IntrospectionException ex) {
      msg.error("Error while introspecting class", ex);
    } catch (IOException e) {
      msg.error(String.format("Error opening shapefile '%S'", shapefile), e);
    } catch (FactoryException e) {
      msg.error(String.format("Error creating transform between shapefile CRS and Geography CRS"), e);
    }
  }

  private boolean isCompatible(Class methodParam, Class attributeType) {
    if (methodParam.equals(attributeType)) return true;
    Class clazz = primToObject.get(methodParam);
    if (clazz != null) return clazz.equals(attributeType);
    return false;
  }

  private void initTransform(Geography geography, AttributeType type) throws FactoryException {
    GeometryAttributeType gType = (GeometryAttributeType) type;
    if (geography != null) {
      try {
        transform = FactoryFinder.getCoordinateOperationFactory(null).createOperation(gType.getCoordinateSystem(),
                geography.getCRS()).getMathTransform();
      } catch (OperationNotFoundException ex) {
        // bursa wolf params may be missing so try lenient.
        transform = CRS.transform(gType.getCoordinateSystem(), geography.getCRS(), true);
      }
    }
  }

  /**
   * Creates all the agents for the shapefile features,
   * setting each agent's properteis to the value of a
   * feature's relevant attributes.
   */
  public void load() {
    while (hasNext()) next();
  }

  /**
   * Creates the next agent from the next feature
   * in the shapefile, setting that agent's properties to the value of
   * that feature's relevant attributes.
   *
   * @return the created agent
   */
  public T next() {
    T obj = null;
    try {
      obj = (T) agentClass.newInstance();
      obj = processNext(obj);
    } catch (InstantiationException e) {
      msg.error("Error creating agent instance from class", e);
    } catch (IllegalAccessException e) {
      msg.error("Error creating agent instance from class", e);
    }

    return obj;
  }

  private T processNext(T obj) {
    try {
      Feature feature = iter.next();
      obj = fillAgent(feature, obj);
      if (!context.contains(obj)) context.add(obj);
      if (geography != null) geography.move(obj, JTS.transform(feature.getDefaultGeometry(), transform));
      return obj;
    } catch (IllegalAccessException e) {
      msg.error("Error setting agent property from feature attribute", e);
    } catch (InvocationTargetException e) {
      msg.error("Error setting agent property from feature attribute", e);
    } catch (TransformException e) {
      msg.error("Error transforming feature geometry to geography's CRS", e);
    }

    return null;
  }

  /**
   * Creates the next agent from the next feature
   * in the shapefile, setting that agent's properties to the value of
   * that feature's relevant attributes. Note that the constructor
   * matching is somewhat naive and looks for exact matches.
   *
   * @param constructorArgs parameters to pass to the constructor when
   *                        creating the agent.
   * @return the created agent
   * @throws IllegalArgumentException if the constructor args don't match a
   *                                  constructor.
   */
  public T nextWithArgs(Object... constructorArgs) throws IllegalArgumentException {
    Constructor constructor = findConstructor(constructorArgs);
    if (constructor == null) throw new IllegalArgumentException("Unable to find matching constructor for arguments");
    try {
      T obj = (T) constructor.newInstance(constructorArgs);
      return processNext(obj);
    } catch (InstantiationException e) {
      msg.error("Error creating agent instance from class", e);
    } catch (IllegalAccessException e) {
      msg.error("Error creating agent instance from class", e);
    } catch (InvocationTargetException e) {
      msg.error("Error creating agent instance from class", e);
    }

    return null;
  }

  /**
   * Sets the specified object's properties to the relevant
   * attributes values of the next feature.
   *
   * @param obj the object whose properties we want to set.
   * @return the object with is properties now set.
   */
  public T next(T obj) {
    return processNext(obj);
  }

  private Constructor findConstructor(Object[] constructorArgs) {
    Class[] args = new Class[constructorArgs.length];
    int i = 0;
    for (Object obj : constructorArgs) {
      args[i++] = obj.getClass();
    }

    for (Constructor constructor : agentClass.getConstructors()) {
      Class[] params = constructor.getParameterTypes();
      if (params.length == args.length) {
        boolean pass = true;
        for (i = 0; i < args.length; i++) {
          if (!isCompatible(params[i], args[i])) {
            pass = false;
            break;
          }
        }
        if (pass) return constructor;
      }
    }

    return null;
  }

  /**
   * Returns true if there are more features left to process,
   * otherwise false.
   *
   * @return true if there are more features left to process,
   *         otherwise false.
   */
  public boolean hasNext() {
    return iter.hasNext();
  }

  private T fillAgent(Feature feature, T agent) throws IllegalAccessException, InvocationTargetException {
    for (String attribName : attributeMethodMap.keySet()) {
      Object val = feature.getAttribute(attribName);
      Method write = attributeMethodMap.get(attribName);
      write.invoke(agent, val);
    }
    return agent;
  }
}
