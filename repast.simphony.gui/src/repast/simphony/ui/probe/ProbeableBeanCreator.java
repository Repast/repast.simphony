package repast.simphony.ui.probe;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.Quantity;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;
import repast.simphony.parameter.Parameter;
import repast.simphony.parameter.StringConverter;
import repast.simphony.util.ClassUtilities;
import repast.simphony.util.collections.Pair;
import simphony.util.messages.MessageCenter;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ProbeableBeanCreator {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(ProbeableBeanCreator.class);


  static class Property {
    Method read;
    Method write;
    String propName;
    String displayName;
    StringConverter converter;

    public Property(String propName, Method read, Method write) {
      this.propName = propName;
      this.displayName = propName;
      this.read = read;
      this.write = write;
    }
  }

  private static ProbeableBeanCreator instance = new ProbeableBeanCreator();

  public static ProbeableBeanCreator getInstance() {
    return instance;
  }

  private Map<String, Pair<List<Property>, Class>> beanPool = new HashMap<String, Pair<List<Property>, Class>>();
  private Map<Class, StringConverter> converterMap = new HashMap<Class, StringConverter>();

  private ProbeableBeanCreator() {
    converterMap.put(Quantity.class, new QuantityConverter());
  }

  public ProbeableBeanInfo createProbeableBean(Object target) throws IntrospectionException, NoSuchMethodException,
          IllegalAccessException, InvocationTargetException, InstantiationException, CannotCompileException, NotFoundException, ClassNotFoundException {
    String targetClassName = target.getClass().getName();
    Pair<List<Property>, Class> pair = beanPool.get(targetClassName);
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    if (pair == null) {
      List<Property> props = getProperties(target);
//      ClassPool pool = new ClassPool();
      ClassPool pool = ClassPool.getDefault();
      pool.appendSystemPath();
      pool.insertClassPath(new LoaderClassPath(ProbeModel.class.getClassLoader()));
      String className = "BindableBeanZZ" + target.getClass().getSimpleName();
      Thread.currentThread().setContextClassLoader(ProbeModel.class.getClassLoader());
      CtClass model = pool.get(ProbeModel.class.getName());

      CtClass ct = pool.makeClass(className, model);
      ct.addField(CtField.make("private " + targetClassName + " bean;", ct));
      ct.addField(CtField.make("private java.util.Map convMap;", ct));
      ct.addConstructor(CtNewConstructor.make(createConstructorSource(className, targetClassName), ct));
      for (Property prop : props) {
        processProperty(prop, ct);
      }
      Class clazz = ct.toClass();
      pair = new Pair<List<Property>, Class>(props, clazz);
      beanPool.put(targetClassName, pair);
    }

    Class clazz = pair.getSecond();
    Map convMap = new HashMap();
    HashMap<String, String> nameMap = new HashMap<String, String>();
    for (Property prop : pair.getFirst()) {
      String displayName = prop.displayName;
      if (displayName != null) nameMap.put(prop.propName, displayName);
      if (prop.converter != null) convMap.put(prop.propName, prop.converter);
    }
    Thread.currentThread().setContextClassLoader(clazz.getClassLoader());
    OldProbeModel m = (OldProbeModel) clazz.getConstructor(target.getClass(), Map.class).
            newInstance(target, convMap);
    Thread.currentThread().setContextClassLoader(loader);


    return new ProbeableBeanInfo(m, nameMap);

  }

  private void processProperty(Property prop, CtClass ct) throws CannotCompileException {
    // for getters : public retval methodName()
    if (prop.read != null && !Modifier.isFinal(prop.read.getModifiers()))
      ct.addMethod(CtNewMethod.make(createGetMethodSource(prop), ct));
    if (prop.write != null && !Modifier.isFinal(prop.write.getModifiers())) {
      ct.addMethod(CtNewMethod.make(createSetMethodSource(prop), ct));
    }
  }

  private String createSetMethodSource(Property prop) {
    Method write = prop.write;
    Method read = prop.read;
    StringBuffer buf = new StringBuffer("public void ");
    buf.append(write.getName());
    buf.append("(");
    if (prop.converter == null) {
      Class<?> paramType = write.getParameterTypes()[0];
      String paramTypeName = paramType.getName();
      if (paramType.isArray()) {
        paramTypeName = paramType.getComponentType().getName() + "[]";
      }
      buf.append(paramTypeName);
      buf.append(" val) { ");
      if (read != null) {
        buf.append(paramTypeName);
        buf.append(" oldValue = bean.");
        buf.append(read.getName());
        buf.append("();\n");
      }
      buf.append("bean.");
      buf.append(write.getName());
      buf.append("(val);\n");
      buf.append("firePropertyChange(\"");
      buf.append(prop.propName);
      buf.append("\", ");
      if (read == null) buf.append("null, ");
      else buf.append("oldValue, ");
      buf.append("val);}");
    } else {
      buf.append("String val) {");
      buf.append("repast.simphony.parameter.StringConverter conv = (repast.simphony.parameter.StringConverter)convMap.get(\"");
      buf.append(prop.propName);
      buf.append("\"); ");
      if (read != null) {
        buf.append("String oldValue = this.");
        buf.append(read.getName());
        buf.append("();\n");
      }
      String wrappedType = write.getParameterTypes()[0].getName();
      buf.append(wrappedType);
      buf.append(" trueVal = (");
      buf.append(wrappedType);
      buf.append(")conv.fromString(val); bean.");
      buf.append(write.getName());
      buf.append("(trueVal); ");
      buf.append("firePropertyChange(\"");
      buf.append(prop.propName);
      buf.append("\", ");
      if (read == null) buf.append("null, ");
      else buf.append("oldValue, ");
      buf.append("val);}");
    }
    return buf.toString();
  }

  private String createGetMethodSource(Property prop) {
    Method method = prop.read;
    StringBuffer buf = new StringBuffer("public ");
    if (prop.converter == null) {
      String retType = method.getReturnType().getName();
      if (method.getReturnType().isArray()) {
        retType = method.getReturnType().getComponentType().getName() + "[]";
      }
      buf.append(retType);
      buf.append(" ");
      buf.append(method.getName());
      buf.append("() { return bean.");
      buf.append(method.getName());
      buf.append("();}");
    } else {
      buf.append("String ");
      buf.append(method.getName());
      buf.append("() { repast.simphony.parameter.StringConverter conv = (repast.simphony.parameter.StringConverter)convMap.get(\"");
      buf.append(prop.propName);
      buf.append("\"); return conv.toString(bean.");
      buf.append(method.getName());
      buf.append("());}");
    }
    return buf.toString();
  }

  private String createConstructorSource(String className, String targetClassName) {
    StringBuffer buf = new StringBuffer("public ");
    buf.append(className);
    buf.append("(");
    buf.append(targetClassName);
    buf.append(" target, java.util.Map map) { bean = target; convMap = map;}");
    return buf.toString();
  }

  private List<Property> getProperties(Object target) throws IntrospectionException {
    Method[] methods = ClassUtilities.findMethods(target.getClass(), Parameter.class);
    BeanInfo info = Introspector.getBeanInfo(target.getClass(), Object.class);
    PropertyDescriptor[] pds = info.getPropertyDescriptors();
    List<Property> props = new ArrayList<Property>();
    for (PropertyDescriptor pd : pds) {
    	
    	// TODO Note that this class isn't used anymore!
    	
      if (methods.length > 0) {
        // only add those properties that have a Parameter annotation
        Method readMethod = pd.getReadMethod();
        Method writeMethod = pd.getWriteMethod();
        Parameter param = readMethod != null ? readMethod.getAnnotation(Parameter.class) : null;
        if (param == null) param = writeMethod != null ? writeMethod.getAnnotation(Parameter.class) : null;
        if (param != null) {
          String conv = param.converter();
          StringConverter converter = null;
          if (conv.trim().length() != 0) {
            try {
              Class clazz = Class.forName(conv);
              converter = (StringConverter) clazz.newInstance();
            } catch (IllegalAccessException e) {
              msg.warn("Error while creating converter for agent parameter '" + param.usageName() + "'", e);
            } catch (InstantiationException e) {
              msg.warn("Error while creating converter for agent parameter '" + param.usageName() + "'", e);
            } catch (ClassNotFoundException e) {
              msg.warn("Error while creating converter for agent parameter '" + param.usageName() + "'", e);
            }
          } else {
            converter = converterMap.get(readMethod.getReturnType());
          }
          Property prop = new Property(pd.getName(), readMethod, writeMethod);
          prop.displayName = param.displayName();
          prop.converter = converter;
          props.add(prop);
        }
      } else {
        // otherwise add them all
        Property prop = new Property(pd.getName(), pd.getReadMethod(), pd.getWriteMethod());
        
        if (pd.getReadMethod() != null){
          prop.converter = converterMap.get(pd.getReadMethod().getReturnType());
          props.add(prop);
        }
      }
    }
    return props;
  }
}
