/**
 * 
 */
package repast.simphony.data2.util;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import repast.simphony.data2.DataSource;
import repast.simphony.data2.engine.CustomDataSourceDefinition;
import repast.simphony.data2.engine.MethodDataSourceDefinition;

/**
 * Filter for class types that are allowed on aggregate data sets.
 * 
 * @author Nick Collier
 */
public class AggregateFilter {
  
  private static Set<Class<?>> allowable = new HashSet<Class<?>>();
  
  static {
    // for ReLogo def
    allowable.add(Object.class);
    allowable.add(int.class);
    allowable.add(long.class);
    allowable.add(float.class);
    allowable.add(byte.class);
    allowable.add(short.class);
    allowable.add(double.class);
    allowable.add(boolean.class);
    allowable.add(Boolean.class);
  }
  
  /**
   * Checks whether or not the specified class passes this filter.
   * 
   * @param clazz
   * @return true if the class passes, otherwise false.
   */
  public boolean check(Class<?> clazz) {
    if (allowable.contains(clazz)) return true;
    return Number.class.isAssignableFrom(clazz);
  }
  
  public boolean check(CustomDataSourceDefinition def) throws ClassNotFoundException,
      InstantiationException, IllegalAccessException {

    String cname = def.getDataSourceClassName();
    Class<?> clazz = Class.forName(cname);
    DataSource source = (DataSource) clazz.newInstance();
    Class<?> dataType = source.getDataType();
    return check(dataType);
  }
  
  public boolean check(MethodDataSourceDefinition def) throws ClassNotFoundException {
    String cname = def.getObjTargetClass();
    Class<?> clazz = Class.forName(cname);
    for (Method method : clazz.getMethods()) {
      if (method.getParameterTypes().length == 0 && method.getName().equals(def.getMethodName())) {
        Class<?> ret = method.getReturnType();
        return check(ret);
      }
    }
    return false;
  }
}
