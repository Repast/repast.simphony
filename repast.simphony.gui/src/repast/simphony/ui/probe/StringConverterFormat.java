/**
 * 
 */
package repast.simphony.ui.probe;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

import repast.simphony.parameter.StringConverter;

/**
 * Format subclass that uses a StringConverter to perform the formatting.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class StringConverterFormat extends Format {
  
  private StringConverter<Object> converter;

  public StringConverterFormat(StringConverter<Object> converter) {
    this.converter = converter;
  }
  
  @Override
  public Object parseObject(String source) throws ParseException {
    return parseObject(source, null);
  }

  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
    toAppendTo.append(converter.toString(obj));
    return toAppendTo;
  }

  @Override
  public Object parseObject(String source, ParsePosition pos) {
    Object val =  converter.fromString(source);
    return val;
  }
}
