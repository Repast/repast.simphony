package repast.simphony.ui.probe;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

@SuppressWarnings("serial")
public class ZeroNumberFormat extends NumberFormat {
  
  private NumberFormat delegate;
  
  public ZeroNumberFormat(NumberFormat delegate) {
    this.delegate = delegate;
  }

  /**
   * @param number
   * @param toAppendTo
   * @param pos
   * @return
   * @see java.text.NumberFormat#format(java.lang.Object, java.lang.StringBuffer, java.text.FieldPosition)
   */
  public StringBuffer format(Object number, StringBuffer toAppendTo, FieldPosition pos) {
    return delegate.format(number, toAppendTo, pos);
  }

  /**
   * @param source
   * @return
   * @throws ParseException
   * @see java.text.Format#parseObject(java.lang.String)
   */
  public Object parseObject(String source) throws ParseException {
    if (source == null || source.trim().length() == 0) source = "0";
    return delegate.parseObject(source);
  }

  /**
   * @param number
   * @param toAppendTo
   * @param pos
   * @return
   * @see java.text.NumberFormat#format(double, java.lang.StringBuffer, java.text.FieldPosition)
   */
  public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
    return delegate.format(number, toAppendTo, pos);
  }

  /**
   * @param number
   * @param toAppendTo
   * @param pos
   * @return
   * @see java.text.NumberFormat#format(long, java.lang.StringBuffer, java.text.FieldPosition)
   */
  public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
    return delegate.format(number, toAppendTo, pos);
  }

  /**
   * @param source
   * @param parsePosition
   * @return
   * @see java.text.NumberFormat#parse(java.lang.String, java.text.ParsePosition)
   */
  public Number parse(String source, ParsePosition parsePosition) {
    return delegate.parse(source, parsePosition);
  }

  /**
   * @param source
   * @return
   * @throws ParseException
   * @see java.text.NumberFormat#parse(java.lang.String)
   */
  public Number parse(String source) throws ParseException {
    return delegate.parse(source);
  }
  
  
  
}