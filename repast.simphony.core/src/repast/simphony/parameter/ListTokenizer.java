package repast.simphony.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tokenizes lists and ranges of strings, numbers and booleans into
 * their individual elements.
 *
 * @author Nick Collier
 */
public class ListTokenizer {

  /**
   * Parses a space delimited list of strings into an array of boolean values. The individual
   * elements must be "true" or "false"
   *
   * @param values the values to tokenize
   * @param name   the name of the parameter
   * @return the tokenized list as an array of Booleans
   * @throws ParameterFormatException if there is an error in the tokenization or
   *                                  conversion.
   */
  public static Boolean[] parseBooleanValues(String values, String name) throws ParameterFormatException {
    try {
      StringTokenizer tok = new StringTokenizer(values, " ");
      List<Boolean> vals = new ArrayList<Boolean>();
      while (tok.hasMoreTokens()) {
        vals.add(Boolean.parseBoolean(tok.nextToken()));
      }
      return vals.toArray(new Boolean[0]);
    } catch (NumberFormatException ex) {
      throw new ParameterFormatException("Invalid format for parameter '" + name + "'", ex);
    }
  }

  /**
   * Parses a space delimited list of strings into an array of Float values. The
   * strings must be valid floats.
   *
   * @param values the values to tokenize
   * @param name   the name of the parameter
   * @return the tokenized list as an array of Floats
   * @throws ParameterFormatException if there is an error in the tokenization or
   *                                  conversion.
   */
  public static Float[] parseFloatValues(String values, String name) throws ParameterFormatException {
    try {
      StringTokenizer tok = new StringTokenizer(values, " ");
      List<Float> vals = new ArrayList<Float>();
      while (tok.hasMoreTokens()) {
        vals.add(Float.parseFloat(tok.nextToken()));
      }
      return vals.toArray(new Float[0]);
    } catch (NumberFormatException ex) {
      throw new ParameterFormatException("Invalid format for parameter '" + name + "'", ex);
    }
  }

  /**
   * Parses a space delimited list of strings into an array of Long values. The
   * strings must be valid longs.
   *
   * @param values the values to tokenize
   * @param name   the name of the parameter
   * @return the tokenized list as an array of Longs
   * @throws ParameterFormatException if there is an error in the tokenization or
   *                                  conversion.
   */
  public static Long[] parseLongValues(String values, String name) throws ParameterFormatException {
    try {
      StringTokenizer tok = new StringTokenizer(values, " ");
      List<Long> vals = new ArrayList<Long>();
      while (tok.hasMoreTokens()) {
        String val = tok.nextToken();
        if (val.endsWith("L") || val.endsWith("l")) val = val.substring(0, val.length() - 1);
        vals.add(Long.parseLong(val));
      }
      return vals.toArray(new Long[0]);
    } catch (NumberFormatException ex) {
      throw new ParameterFormatException("Invalid format for parameter '" + name + "'", ex);
    }
  }

  /**
   * Parses a space delimited list of strings into an array of Double values. The
   * strings must be valid doubles.
   *
   * @param values the values to tokenize
   * @param name   the name of the parameter
   * @return the tokenized list as an array of Doubles
   * @throws ParameterFormatException if there is an error in the tokenization or
   *                                  conversion.
   */
  public static Double[] parseDoubleValues(String values, String name) throws ParameterFormatException {
    try {
      StringTokenizer tok = new StringTokenizer(values, " ");
      List<Double> vals = new ArrayList<Double>();
      while (tok.hasMoreTokens()) {
        vals.add(Double.parseDouble(tok.nextToken()));
      }
      return vals.toArray(new Double[0]);
    } catch (NumberFormatException ex) {
      throw new ParameterFormatException("Invalid format for parameter '" + name + "'", ex);
    }
  }

  /**
   * Parses a space delimited list of strings into an array of Byte values. The
   * strings must be valid bytes.
   *
   * @param values the values to tokenize
   * @param name   the name of the parameter
   * @return the tokenized list as an array of Bytes
   * @throws ParameterFormatException if there is an error in the tokenization or
   *                                  conversion.
   */
  public static Byte[] parseByteValues(String values, String name) throws ParameterFormatException {
    try {
      StringTokenizer tok = new StringTokenizer(values, " ");
      List<Byte> vals = new ArrayList<Byte>();
      while (tok.hasMoreTokens()) {
        vals.add(Byte.parseByte(tok.nextToken()));
      }
      return vals.toArray(new Byte[0]);
    } catch (NumberFormatException ex) {
      throw new ParameterFormatException("Invalid format for parameter '" + name + "'", ex);
    }
  }

  /**
   * Parses a space delimited list of strings into an array of Short values. The
   * strings must be valid Shorts.
   *
   * @param values the values to tokenize
   * @param name   the name of the parameter
   * @return the tokenized list as an array of Shorts
   * @throws ParameterFormatException if there is an error in the tokenization or
   *                                  conversion.
   */
  public static Short[] parseShortValues(String values, String name) throws ParameterFormatException {
    try {
      StringTokenizer tok = new StringTokenizer(values, " ");
      List<Short> vals = new ArrayList<Short>();
      while (tok.hasMoreTokens()) {
        vals.add(Short.parseShort(tok.nextToken()));
      }
      return vals.toArray(new Short[0]);
    } catch (NumberFormatException ex) {
      throw new ParameterFormatException("Invalid format for parameter '" + name + "'", ex);
    }
  }

  /**
   * Parses a space delimited list of strings into an array of Integer values. The
   * strings must be valid ints.
   *
   * @param values the values to tokenize
   * @param name   the name of the parameter
   * @return the tokenized list as an array of Integers
   * @throws ParameterFormatException if there is an error in the tokenization or
   *                                  conversion.
   */
  public static Integer[] parseIntValues(String values, String name) throws ParameterFormatException {
    try {
      StringTokenizer tok = new StringTokenizer(values, " ");
      List<Integer> vals = new ArrayList<Integer>();
      while (tok.hasMoreTokens()) {
        vals.add(Integer.parseInt(tok.nextToken()));
      }
      return vals.toArray(new Integer[0]);
    } catch (NumberFormatException ex) {
      throw new ParameterFormatException("Invalid format for parameter '" + name + "'", ex);
    }
  }

  /**
   * Parses a space delimited list of strings into an array of individual String values. The
   * strings should be enclosed in single quotes.
   *
   * @param values the values to tokenize
   * @return the tokenized list as an array of Strings
   */
  public static String[] parseStringValues(String values) {
    List<String> list = new ArrayList<String>();
    if (values.trim().length() == 0) return new String[]{""};
    if (values.contains("'")) {
      // parse with regex
      Pattern p = Pattern.compile("'(?>\\\\.|.)*?'");
      Matcher m1 = p.matcher(values);
      while (m1.find()) {
        String val = m1.group();
        // strip off the '
        list.add(val.substring(1, val.length() - 1));
      }
    } else {
      StringTokenizer tok = new StringTokenizer(values, " ");
      while (tok.hasMoreTokens()) {
        list.add(tok.nextToken());
      }
    }

    return list.toArray(new String[list.size()]);
  }
}
