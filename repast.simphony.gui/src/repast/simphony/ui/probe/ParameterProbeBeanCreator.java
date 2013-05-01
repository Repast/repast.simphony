package repast.simphony.ui.probe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.apache.commons.lang3.StringUtils;

import repast.simphony.parameter.ParameterSchema;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.Schema;
import repast.simphony.util.ClassUtilities;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ParameterProbeBeanCreator {

  private static int count = 0;

  public ProbeableBeanInfo createProbeableBean(Parameters parameters) throws NotFoundException,
      CannotCompileException, NoSuchMethodException, IllegalAccessException,
      InvocationTargetException, InstantiationException {

    Map<String, String> displayNameMap = new HashMap<String, String>();

    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    ClassPool pool = new ClassPool();
    pool.appendSystemPath();
    pool.insertClassPath(new LoaderClassPath(OldProbeModel.class.getClassLoader()));
    pool.insertClassPath(new LoaderClassPath(Parameters.class.getClassLoader()));
    String className = "ParameterBean__" + count++;
    Schema schema = parameters.getSchema();

    Thread.currentThread().setContextClassLoader(OldProbeModel.class.getClassLoader());
    CtClass model = pool.get(OldProbeModel.class.getName());
    CtClass ct = pool.makeClass(className, model);
    ct.addField(CtField.make("private " + Parameters.class.getName() + " params;", ct));
    ct.addConstructor(CtNewConstructor.make(createConstructorSource(className), ct));

    for (String name : schema.parameterNames()) {
      ParameterSchema details = schema.getDetails(name);
      Class type = details.getType();
      String source;
      boolean useConv = false;
      if (isPrimOrString(type)) {
        source = createGetSource(name, type);
      } else if (details.getConverter() != null) {
        source = createGetConvertorSource(name);
        useConv = true;
      } else {
        throw new NotFoundException("Convertor not found for non-primitive parameter type");
      }
      CtMethod readMethod = CtNewMethod.make(source, ct);
      ct.addMethod(readMethod);
      CtMethod writeMethod;
      if (!parameters.isReadOnly(name)) {
        String setSource;
        if (useConv) {
          setSource = createSetSourceConvertor(name);
        } else {
          setSource = createSetSource(name, type);
        }
        writeMethod = CtNewMethod.make(setSource, ct);
        ct.addMethod(writeMethod);
      }
    }

    Class clazz = ct.toClass();
    Thread.currentThread().setContextClassLoader(clazz.getClassLoader());
    OldProbeModel m = (OldProbeModel) clazz.getConstructor(Parameters.class)
        .newInstance(parameters);

    for (String name : schema.parameterNames()) {
      displayNameMap.put(name, parameters.getDisplayName(name));
    }

    Thread.currentThread().setContextClassLoader(loader);
    return new ProbeableBeanInfo(m, displayNameMap);
  }

  private boolean isPrimOrString(Class type) {
    return ClassUtilities.isNumericType(type) || type.equals(Boolean.class)
        || type.equals(boolean.class) || type.equals(String.class);
  }

  private String createSetSourceConvertor(String name) {
    // public void setName(String val) { params.setValue(name, val); fireProp...
    // }
    StringBuffer buf = new StringBuffer("public void set");
    String methodPropName = createMethodPropName(name);
    buf.append(methodPropName);
    buf.append("(String val) {\n String __oldValue = params.getValue(\"");
    buf.append(name);
    buf.append("\");\n params.setValue(\"");
    buf.append(name);
    buf.append("\", val); \n");
    buf.append("firePropertyChange(\"");
    buf.append(methodPropName);
    buf.append("\", __oldValue, ");
    buf.append("val);}");
    // System.out.println("buf.toString() = " + buf.toString());

    return buf.toString();

  }

  private String createSetSource(String name, Class type) {
    // public void setName(Type val) { params.setValue(name, val); fireProp...}
    StringBuffer buf = new StringBuffer("public void set");
    String methodPropName = createMethodPropName(name);
    buf.append(methodPropName);
    buf.append("(");
    buf.append(type.getName());
    buf.append(" val) { ");
    buf.append(createSetCast(type, name));
    buf.append("params.setValue(\"");
    buf.append(name);
    buf.append("\", ");
    buf.append(createSetConversion(type));
    buf.append("); \n");
    buf.append("firePropertyChange(\"");
    buf.append(methodPropName);
    buf.append("\", __oldValue, ");
    buf.append("val);}");
    // System.out.println("buf.toString() = " + buf.toString());

    return buf.toString();
  }

  private String createSetCast(Class type, String name) {
    StringBuilder buf = new StringBuilder();
    buf.append(type.getName());
    buf.append(" __oldValue = ");
    String prefix = "";
    String suffix = "";
    if (type.equals(int.class)) {
      prefix = "((Integer)";
      suffix = ").intValue()";
    } else if (type.equals(long.class)) {
      prefix = "((Long)";
      suffix = ").longValue()";
    } else if (type.equals(float.class)) {
      prefix = "((Float)";
      suffix = ").floatValue()";
    } else if (type.equals(double.class)) {
      prefix = "((Double)";
      suffix = ").doubleValue()";
    } else if (type.equals(byte.class)) {
      prefix = "((Byte)";
      suffix = ").byteValue()";
    } else if (type.equals(short.class)) {
      prefix = "((Short)";
      suffix = ").shortValue()";
    } else if (type.equals(boolean.class)) {
      prefix = "((Boolean)";
      suffix = ").booleanValue()";
    }

    buf.append(prefix);
    buf.append("params.getValue(\"");
    buf.append(name);
    buf.append("\")");
    buf.append(suffix);
    buf.append(";\n");
    return buf.toString();
  }

  private String createSetConversion(Class type) {
    if (type.equals(int.class)) {
      return "Integer.valueOf(val)";
    } else if (type.equals(long.class)) {
      return "Long.valueOf(val)";
    } else if (type.equals(float.class)) {
      return "Float.valueOf(val)";
    } else if (type.equals(double.class)) {
      return "Double.valueOf(val)";
    } else if (type.equals(byte.class)) {
      return "Byte.valueOf(val)";
    } else if (type.equals(short.class)) {
      return "Short.valueOf(val)";
    } else if (type.equals(boolean.class)) {
      return "Boolean.valueOf(val)";
    } else {
      return "val";
    }
  }

  private String createGetConvertorSource(String name) {
    // public String getName() { return params.getValueAsString(name)); }
    StringBuffer buf = new StringBuffer("public String get");
    buf.append(createMethodPropName(name));
    buf.append("() { return params.getValueAsString(\"");
    buf.append(name);
    buf.append("\"); }");
    // System.out.println("buf = " + buf);
    return buf.toString();
  }

  private String createGetSource(String name, Class type) {
    // public type getName() { return (Type)params.get(name); }
    StringBuffer buf = new StringBuffer("public ");
    buf.append(type.getName());
    // only boolean NOT Boolean
    if (type.equals(boolean.class))
      buf.append(" is");
    else
      buf.append(" get");
    buf.append(createMethodPropName(name));
    buf.append("() { return ");
    buf.append(createGetCast(type, name));
    buf.append(" }");
    return buf.toString();
  }

  private String createGetCast(Class type, String name) {
    StringBuffer buf = new StringBuffer();
    buf.append("params.getValue(\"");
    buf.append(name);
    buf.append("\")");
    String paramGet = buf.toString();
    if (type.equals(int.class)) {
      return "((Integer)" + paramGet + ").intValue();";
    } else if (type.equals(long.class)) {
      return "((Long)" + paramGet + ").longValue();";
    } else if (type.equals(float.class)) {
      return "((Float)" + paramGet + ").floatValue();";
    } else if (type.equals(double.class)) {
      return "((Double)" + paramGet + ").doubleValue();";
    } else if (type.equals(byte.class)) {
      return "((Byte)" + paramGet + ").byteValue();";
    } else if (type.equals(short.class)) {
      return "((Short)" + paramGet + ").shortValue();";
    } else if (type.equals(boolean.class)) {
      return "((Boolean)" + paramGet + ").booleanValue();";
    } else {
      return "(" + type.getName() + ")" + paramGet + ";";
    }
  }

  private String createMethodPropName(String name) {
    String newName = StringUtils.replace(name, " ", "_");
    return StringUtils.capitalize(newName);
  }

  private String createConstructorSource(String className) {
    StringBuffer buf = new StringBuffer("public ");
    buf.append(className);
    buf.append("(");
    buf.append(Parameters.class.getName());
    buf.append(" p) { params = p;}");
    return buf.toString();
  }
}
