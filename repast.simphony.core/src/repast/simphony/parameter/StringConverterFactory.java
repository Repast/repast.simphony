package repast.simphony.parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nick Collier Date: Dec 22, 2008 3:50:56 PM
 */
public class StringConverterFactory {

  private static StringConverterFactory instance;

  /**
   * Gets the singleton instance.
   * 
   * @return the singleton instance.
   */
  public static StringConverterFactory instance() {
    if (instance == null) {
      instance = new StringConverterFactory();
    }
    return instance;
  }

  private StringConverterFactory() {
    converters.put(int.class, new IntConverter());
    converters.put(Integer.class, new IntConverter());
    converters.put(float.class, new FloatConverter());
    converters.put(Float.class, new FloatConverter());
    converters.put(Long.class, new LongConverter());
    converters.put(long.class, new LongConverter());
    converters.put(Double.class, new DoubleConverter());
    converters.put(double.class, new DoubleConverter());
    converters.put(Byte.class, new ByteConverter());
    converters.put(byte.class, new ByteConverter());
    converters.put(boolean.class, new BooleanConverter());
    converters.put(Boolean.class, new BooleanConverter());
    converters.put(short.class, new ShortConverter());
    converters.put(Short.class, new ShortConverter());

    converters.put(String.class, new StringStringConverter());

  }

  private Map<Class, StringConverter> converters = new HashMap<Class, StringConverter>();

  /**
   * Get StringConverter appropriate for the specified class.
   * 
   * @param clazz
   *          the class to get the StringConverter for
   * @param <T>
   *          the type of the class
   * @return the StringConverter. This can return NULL, if no StringConverter
   *         has been registered.
   */
  @SuppressWarnings( { "unchecked" })
  public <T> StringConverter<T> getConverter(Class<T> clazz) {
    return converters.get(clazz);
  }

  /**
   * Adds a converter to the factory for the specified class.
   * 
   * @param clazz
   *          the class the converter works with
   * @param converter
   *          the converter
   * @param <T>
   *          the type the converter works on
   */
  public <T> void addConverter(Class<T> clazz, StringConverter<T> converter) {
    converters.put(clazz, converter);
  }

  private abstract static class PrimConverter implements StringConverter {
    public String toString(Object obj) {
      return obj.toString();
    }
  }

  public static class StringStringConverter implements StringConverter {
  
    public String toString(Object obj) {
      return obj.toString();
    }

    public Object fromString(String strRep) {
      return strRep;
    }

  }

  public static class IntConverter extends PrimConverter {
    public Object fromString(String strRep) {
      return Integer.valueOf(strRep);
    }
  }

  public static class DoubleConverter extends PrimConverter {
    public Object fromString(String strRep) {
      return Double.valueOf(strRep);
    }
  }

  public static class LongConverter extends PrimConverter {
    public Object fromString(String strRep) {
      return Long.valueOf(strRep);
    }
  }

  public static class FloatConverter extends PrimConverter {
    public Object fromString(String strRep) {
      return Float.valueOf(strRep);
    }
  }

  public static class ByteConverter extends PrimConverter {
    public Object fromString(String strRep) {
      return Byte.valueOf(strRep);
    }
  }

  public static class ShortConverter extends PrimConverter {
    public Object fromString(String strRep) {
      return Short.valueOf(strRep);
    }
  }

  public static class BooleanConverter extends PrimConverter {
    public Object fromString(String strRep) {
      return Boolean.valueOf(strRep);
    }
  }
}
