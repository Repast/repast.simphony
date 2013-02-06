/**
 * 
 */
package repast.simphony.ui.probe;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.parameter.StringConverter;
import repast.simphony.util.ClassUtilities;
import simphony.util.messages.MessageCenter;

/**
 * Introspect objects and creates ProbeInfo out of them.
 * 
 * @author Nick Collier
 */
public class ProbeIntrospector {

  private static MessageCenter msg = MessageCenter.getMessageCenter(ProbeIntrospector.class);

  private enum MethodType {
    READ, WRITE, INVALID
  };

  private static ProbeIntrospector instance;

  /**
   * Gets the instance of the ProbeIntrospector.
   * 
   * @return the instance of the ProbeIntrospector.
   */
  public static ProbeIntrospector getInstance() {
    if (instance == null)
      instance = new ProbeIntrospector();
    return instance;
  }

  private Map<Class<?>, ProbeInfo> infoMap = new HashMap<Class<?>, ProbeInfo>();

  /**
   * Gets the ProbeInfo for the specified class.
   * 
   * @param probedObjectClass
   *          the class to get ProbeInfo for.
   * @return the ProbeInfo for the specified class.
   * @throws IntrospectionException
   *           if there is getting the ProbeInfo.
   */
  public ProbeInfo getProbeInfo(Class<?> probedObjectClass) throws IntrospectionException {
    ProbeInfo info = infoMap.get(probedObjectClass);
    if (info == null) {
      info = createInfo(probedObjectClass);
      infoMap.put(probedObjectClass, info);
    }
    return info;
  }

  @SuppressWarnings("unchecked")
  private void findID(PBI info) throws IntrospectionException {
    Method[] methods = ClassUtilities.findMethods(info.getProbedClass(), ProbeID.class);
    PropertyDescriptor pd = null;

    for (Method method : methods) {
      if (method.getParameterTypes().length == 0 && method.getReturnType() != null) {
        pd = new PropertyDescriptor(method.getName(), method, null);
        break;
      }
    }

    info.idDescriptor = pd;
  }

  private void gatherFields(List<Field> fields, Class<?> clazz) {
    while (!clazz.equals(Object.class)) {
      Field[] fs = clazz.getDeclaredFields();
      for (Field field : fs) {
        if (field.isAnnotationPresent(ProbeProperty.class))
          fields.add(field);
      }
      clazz = clazz.getSuperclass();
    }
  }

  private void findFieldProperties(PBI info) throws IntrospectionException {
    List<Field> fields = new ArrayList<Field>();
    gatherFields(fields, info.getProbedClass());
    for (Field field : fields) {
      field.setAccessible(true);
      ProbeProperty pprop = field.getAnnotation(ProbeProperty.class);
      FieldPropertyDescriptor fp = new FieldPropertyDescriptor(field, pprop.usageName().trim());
      if (pprop.displayName().trim().length() > 0)
        fp.setDisplayName(pprop.displayName().trim());
      fp.setStringConverter(createStringConverter(pprop));
      info.fpds.add(fp);
    }
  }

  private MethodType getMethodType(Method method) {
    if (!method.getReturnType().equals(void.class) && method.getParameterTypes().length == 0)
      return MethodType.READ;
    if (method.getReturnType().equals(void.class) && method.getParameterTypes().length == 1)
      return MethodType.WRITE;
    return MethodType.INVALID;
  }

  private StringConverter<?> createStringConverter(ProbeProperty param) {
    if (param.converter().trim().length() > 0) {
      try {
        Class<?> clazz = Class.forName(param.converter().trim());
        StringConverter<?> converter = (StringConverter<?>) clazz.newInstance();
        return converter;
      } catch (IllegalAccessException e) {
        msg.warn("Error while creating converter for agent parameter '" + param.usageName() + "'",
            e);
      } catch (InstantiationException e) {
        msg.warn("Error while creating converter for agent parameter '" + param.usageName() + "'",
            e);
      } catch (ClassNotFoundException e) {
        msg.warn("Error while creating converter for agent parameter '" + param.usageName() + "'",
            e);
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private void findProperties(PBI info) throws IntrospectionException {
    Method[] methods = ClassUtilities.findMethods(info.getProbedClass(), ProbeProperty.class);
    if (methods.length > 0) {
      Map<String, MethodPropertyDescriptor> pdMap = new HashMap<String, MethodPropertyDescriptor>();
      for (Method method : methods) {
        MethodType type = getMethodType(method);
        if (type == MethodType.INVALID) {
          msg.warn(String.format(
              "%s does not meet the requirements for the @ProbeParameter annotation",
              method.getName()));
          continue;
        }

        ProbeProperty pprop = method.getAnnotation(ProbeProperty.class);
        MethodPropertyDescriptor pd = pdMap.get(pprop.usageName().trim());
        if (pd == null) {
          pd = new MethodPropertyDescriptor(pprop.usageName().trim(), info.getProbedClass());
          pd.setDisplayName(pprop.usageName().trim());
          info.pds.add(pd);
          pdMap.put(pprop.usageName().trim(), pd);
        }

        if (pd.getDisplayName().equals(pd.getName()) && pprop.displayName().trim().length() > 0)
          pd.setDisplayName(pprop.displayName());
        if (pd.getStringConverter() == null)
          pd.setStringConverter(createStringConverter(pprop));

        if (type == MethodType.READ)
          pd.setReadMethod(method);
        else if (type == MethodType.WRITE)
          pd.setWriteMethod(method);
      }

    } else {
      // add the bean property descriptors
      BeanInfo bInfo = Introspector.getBeanInfo(info.getProbedClass(), Object.class);
      PropertyDescriptor[] pds = bInfo.getPropertyDescriptors();
      for (PropertyDescriptor pd : pds) {
        info.pds.add(new MethodPropertyDescriptor(pd.getName(), pd.getReadMethod(), pd
            .getWriteMethod()));
      }
    }
  }

  private ProbeInfo createInfo(Class<?> clazz) throws IntrospectionException {
    PBI info = new PBI(clazz);
    findID(info);
    findProperties(info);
    findFieldProperties(info);
    return info;
  }

  private static class PBI implements ProbeInfo {

    private Class<?> clazz;
    private List<MethodPropertyDescriptor> pds = new ArrayList<MethodPropertyDescriptor>();
    private List<FieldPropertyDescriptor> fpds = new ArrayList<FieldPropertyDescriptor>();
    private PropertyDescriptor idDescriptor;

    public PBI(Class<?> clazz) {
      this.clazz = clazz;
    }

    @Override
    public Class<?> getProbedClass() {
      return clazz;
    }

    @Override
    public PropertyDescriptor getIDProperty() {
      return idDescriptor;
    }

    @Override
    public Iterable<MethodPropertyDescriptor> methodPropertyDescriptors() {
      return pds;
    }

    @Override
    public Iterable<FieldPropertyDescriptor> fieldPropertyDescriptor() {
      return fpds;
    }
  }
}
