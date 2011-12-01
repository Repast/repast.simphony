package repast.simphony.parameter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Factory for ParameterTypes.
 *
 * @author Nick Collier
 */
public class ParameterTypeFactory {

  private static ParameterTypeFactory instance;

  public static ParameterTypeFactory instance() {
    if (instance == null) instance = new ParameterTypeFactory();
    return instance;
  }

  private Map<Class<?>, ParameterType> types = new HashMap<Class<?>, ParameterType>();

  private ParameterTypeFactory() {
    ParameterType type = new IntegerParameterType();
    types.put(int.class, type);
    types.put(Integer.class, type);

    type = new LongParameterType();
    types.put(long.class, type);
    types.put(Long.class, type);

    type = new StringParameterType();
    types.put(String.class, type);

    type = new FloatParameterType();
    types.put(float.class, type);
    types.put(Float.class, type);

    type = new DoubleParameterType();
    types.put(double.class, type);
    types.put(Double.class, type);

    type = new ByteParameterType();
    types.put(byte.class, type);
    types.put(Byte.class, type);

    type = new ShortParameterType();
    types.put(short.class, type);
    types.put(Short.class, type);

    type = new BooleanParameterType();
    types.put(boolean.class, type);
    types.put(Boolean.class, type);

    type = new FileParameterType();
    types.put(File.class, type);
  }

  /**
   * Gets a ParameterType for the specified class.
   *
   * @param clazz the class to get the type for
   * @param <T>   the type of the class
   * @return the ParameterType.
   */
  @SuppressWarnings({"unchecked"})
  public <T> ParameterType<T> getParameterType(Class<T> clazz) {
    return types.get(clazz);
  }

  /**
   * Adds a ParameterType for the specified class.
   *
   * @param clazz the class to add the type for
   * @param type  the type to add
   * @param <T>   the class type
   */
  public <T> void addParameterType(Class<T> clazz, ParameterType<T> type) {
    types.put(clazz, type);
  }

  private class StringParameterType implements ParameterType<String> {

    public Class<String> getJavaClass() {
      return String.class;
    }


    public String getValue(String val) throws ParameterFormatException {
      if (val == null || val.trim().length() == 0) {
        return "";
      }

      StringTokenizer tok = new StringTokenizer(val, " ");
      return tok.nextToken();
    }

    public StringConverter<String> getConverter() {
      return StringConverterFactory.instance().getConverter(String.class);
    }
  }

  private class BooleanParameterType implements ParameterType<Boolean> {

    public Class<Boolean> getJavaClass() {
      return Boolean.class;
    }


    public Boolean getValue(String val) throws ParameterFormatException {
      if (val == null || val.trim().length() == 0) {
        return false;
      }

      StringTokenizer tok = new StringTokenizer(val, " ");
      try {
        return Boolean.parseBoolean(tok.nextToken());
      } catch (NumberFormatException ex) {
        throw new ParameterFormatException("Invalid parameter format '" + val + "'");
      }
    }

    public StringConverter<Boolean> getConverter() {
      return StringConverterFactory.instance().getConverter(Boolean.class);
    }
  }

  private class IntegerParameterType implements ParameterType<Integer> {

    public Class<Integer> getJavaClass() {
      return Integer.class;
    }


    public Integer getValue(String val) throws ParameterFormatException {
      if (val == null || val.trim().length() == 0) {
        return 0;
      }

      StringTokenizer tok = new StringTokenizer(val, " ");
      try {
        return Integer.parseInt(tok.nextToken());
      } catch (NumberFormatException ex) {
        throw new ParameterFormatException("Invalid parameter format '" + val + "'");
      }
    }

    public StringConverter<Integer> getConverter() {
      return StringConverterFactory.instance().getConverter(Integer.class);
    }
  }


  private class LongParameterType implements ParameterType<Long> {

    public Class<Long> getJavaClass() {
      return Long.class;
    }


    public Long getValue(String val) throws ParameterFormatException {
      if (val == null || val.trim().length() == 0) {
        return 0L;
      }

      StringTokenizer tok = new StringTokenizer(val, " ");
      try {
        return Long.parseLong(tok.nextToken());
      } catch (NumberFormatException ex) {
        throw new ParameterFormatException("Invalid parameter format '" + val + "'");
      }
    }

    public StringConverter<Long> getConverter() {
      return StringConverterFactory.instance().getConverter(Long.class);
    }
  }

  private class DoubleParameterType implements ParameterType<Double> {

    public Class<Double> getJavaClass() {
      return Double.class;
    }


    public Double getValue(String val) throws ParameterFormatException {
      if (val == null || val.trim().length() == 0) {
        return 0d;
      }

      StringTokenizer tok = new StringTokenizer(val, " ");
      try {
        return Double.parseDouble(tok.nextToken());
      } catch (NumberFormatException ex) {
        throw new ParameterFormatException("Invalid parameter format '" + val + "'");
      }
    }

    public StringConverter<Double> getConverter() {
      return StringConverterFactory.instance().getConverter(Double.class);
    }
  }

  private class FloatParameterType implements ParameterType<Float> {

    public Class<Float> getJavaClass() {
      return Float.class;
    }


    public Float getValue(String val) throws ParameterFormatException {
      if (val == null || val.trim().length() == 0) {
        return 0f;
      }

      StringTokenizer tok = new StringTokenizer(val, " ");
      try {
        return Float.parseFloat(tok.nextToken());
      } catch (NumberFormatException ex) {
        throw new ParameterFormatException("Invalid parameter format '" + val + "'");
      }
    }

    public StringConverter<Float> getConverter() {
      return StringConverterFactory.instance().getConverter(Float.class);
    }
  }

  private class ShortParameterType implements ParameterType<Short> {

    public Class<Short> getJavaClass() {
      return Short.class;
    }


    public Short getValue(String val) throws ParameterFormatException {
      if (val == null || val.trim().length() == 0) {
        return 0;
      }

      StringTokenizer tok = new StringTokenizer(val, " ");
      try {
        return Short.parseShort(tok.nextToken());
      } catch (NumberFormatException ex) {
        throw new ParameterFormatException("Invalid parameter format '" + val + "'");
      }
    }

    public StringConverter<Short> getConverter() {
      return StringConverterFactory.instance().getConverter(Short.class);
    }
  }

  private class ByteParameterType implements ParameterType<Byte> {

    public Class<Byte> getJavaClass() {
      return Byte.class;
    }


    public Byte getValue(String val) throws ParameterFormatException {
      if (val == null || val.trim().length() == 0) {
        return 0;
      }

      StringTokenizer tok = new StringTokenizer(val, " ");
      try {
        return Byte.parseByte(tok.nextToken());
      } catch (NumberFormatException ex) {
        throw new ParameterFormatException("Invalid parameter format '" + val + "'");
      }
    }

    public StringConverter<Byte> getConverter() {
      return StringConverterFactory.instance().getConverter(Byte.class);
    }
  }

}
