package repast.simphony.data2;

import java.lang.reflect.Method;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import org.apache.commons.lang.ClassUtils;

/**
 * 
 */

/**
 * DataSource that retrieves data from a method call on an object.
 * 
 * @author Nick Collier
 */
public class MethodDataSource implements NonAggregateDataSource {

  private static Object[] NO_ARGS = {};

  private FastMethod fmethod;
  private String id;
  private Class<?> type, sourceType;

  /**
   * Creates a MethodDataSource that will call the named method on objects of
   * the specified type. The method must not return void and must take 0
   * arguments.
   * 
   * @param id
   *          a unique identifier for this DataSource.
   * @param clazz
   *          the object type
   * @param methodName
   *          the name of the method to call
   */
  public MethodDataSource(String id, Class<?> clazz, String methodName) {
    this.id = id;
    try {
      Method method = clazz.getMethod(methodName);
      if (method.getReturnType().equals(void.class))
        throw new DataException("Error creating MethodDataSource: method must not return void");
      fmethod = FastClass.create(clazz).getMethod(method);
      sourceType = clazz;
      Class<?> ret = fmethod.getReturnType();
      if (ret.isPrimitive())
        type = ClassUtils.primitiveToWrapper(ret);
      else
        type = (Class<?>) ret;
    } catch (Exception ex) {
      throw new DataException("Error creating MethodDataSource", ex);
    }

  }
  
  /**
   * Gets the FastMethod that this MethodDataSource will invoke on a "get".
   * 
   * @return the FastMethod that this MethodDataSource will invoke on a "get".
   */
  public FastMethod getMethod() {
    return fmethod;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSource#getDataType()
   */
  @Override
  public Class<?> getDataType() {
    return type;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSource#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  @Override
  public Object get(Object obj) {
    try {
      return fmethod.invoke(obj, NO_ARGS);
    } catch (Exception e) {
      throw new DataException("Error invoking method on object", e);
    }
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSource#getSourceType()
   */
  @Override
  public Class<?> getSourceType() {
    return sourceType;
  }
}
