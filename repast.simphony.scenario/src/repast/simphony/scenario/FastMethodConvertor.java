package repast.simphony.scenario;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.extended.JavaMethodConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * XStream converter for a FastMethod. Serializes the Method and then turns it into a FastMethod on
 * deserialization.
 *
 * @author Nick Collier
 */
public class FastMethodConvertor implements Converter {

  private JavaMethodConverter jConverter = new JavaMethodConverter();
  private XStream xstream;

  public FastMethodConvertor(XStream xstream) {
    this.xstream = xstream;
  }

  public boolean canConvert(Class aClass) {
    return aClass != null && aClass.equals(FastMethod.class);
  }

  public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
    FastMethod fMethod = (FastMethod) o;
    jConverter.marshal(fMethod.getJavaMethod(), hierarchicalStreamWriter, marshallingContext);
  }

  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    try {
      //boolean isMethodNotConstructor = context.getRequiredType().equals(Method.class);

      reader.moveDown();
      String declaringClassName = reader.getValue();
      Class declaringClass = loadClass(declaringClassName);
      reader.moveUp();

      String methodName = null;
      //if (isMethodNotConstructor) {
      reader.moveDown();
      methodName = reader.getValue();
      reader.moveUp();
      //}

      reader.moveDown();
      List parameterTypeList = new ArrayList();
      while (reader.hasMoreChildren()) {
        reader.moveDown();
        String parameterTypeName = reader.getValue();
        parameterTypeList.add(loadClass(parameterTypeName));
        reader.moveUp();
      }
      Class[] parameterTypes = (Class[]) parameterTypeList.toArray(new Class[parameterTypeList.size()]);
      reader.moveUp();

      //if (isMethodNotConstructor) {
      Method method = declaringClass.getDeclaredMethod(methodName, parameterTypes);
      return FastClass.create(method.getDeclaringClass()).getMethod(method);
      //} else {
      //	return declaringClass.getDeclaredConstructor(parameterTypes);
      //}
    } catch (ClassNotFoundException e) {
      throw new ConversionException(e);
    } catch (NoSuchMethodException e) {
      throw new ConversionException(e);
    }
  }

  private Class loadClass(String className) throws ClassNotFoundException {
    Class primitiveClass = primitiveClassForName(className);
    if (primitiveClass != null) {
      return primitiveClass;
    }
    if (xstream != null) {
      return xstream.getClassLoader().loadClass(className);
    }
    return jConverter.getClass().getClassLoader().loadClass(className);
  }

  /**
   * Lookup table for primitive types.
   */
  private Class primitiveClassForName(String name) {
    return name.equals("void") ? Void.TYPE :
            name.equals("boolean") ? Boolean.TYPE :
                    name.equals("byte") ? Byte.TYPE :
                            name.equals("char") ? Character.TYPE :
                                    name.equals("short") ? Short.TYPE :
                                            name.equals("int") ? Integer.TYPE :
                                                    name.equals("long") ? Long.TYPE :
                                                            name.equals("float") ? Float.TYPE :
                                                                    name.equals("double") ? Double.TYPE : null;
  }
}
