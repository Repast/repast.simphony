package repast.simphony.ui.probe;

import java.awt.GridLayout;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import repast.simphony.ui.sparkline.PropertySourcedSparklineJComponent;
import repast.simphony.ui.sparkline.SparklineJComponent;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.value.AbstractValueModel;

/**
 * ProbedProperty for numeric types.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NumericProbedProperty extends DefaultProbedPropertyUICreator {

  private enum NumType {
    BYTE, INT, DOUBLE, LONG, FLOAT, BIGINTEGER, BIGDECIMAL, SHORT
  };

  private NumType numType;
  private List values;
  private boolean wrap = true;

  public NumericProbedProperty(PropertyDescriptor desc, boolean wrap) {
    super(desc);
    this.wrap = wrap;
    if (type == Type.READ || type == Type.READ_WRITE)
      setType(desc.getReadMethod().getReturnType());
    else
      setType(desc.getWriteMethod().getParameterTypes()[0]);
  }

  public NumericProbedProperty(PropertyDescriptor desc, List possibleValues, boolean wrap) {
    this(desc, wrap);
    values = possibleValues;
  }

  private void setType(Class<?> val) {
    if (val.equals(BigDecimal.class))
      numType = NumType.BIGDECIMAL;
    if (val.equals(BigInteger.class))
      numType = NumType.BIGINTEGER;
    if (val.equals(byte.class) || val.equals(Byte.class))
      numType = NumType.BYTE;
    if (val.equals(int.class) || val.equals(Integer.class))
      numType = NumType.INT;
    if (val.equals(double.class) || val.equals(Double.class))
      numType = NumType.DOUBLE;
    if (val.equals(float.class) || val.equals(Float.class))
      numType = NumType.FLOAT;
    if (val.equals(long.class) || val.equals(Long.class))
      numType = NumType.LONG;
    if (val.equals(short.class) || val.equals(Short.class))
      numType = NumType.SHORT;
  }

  public JComponent getComponent(PresentationModel<Object> model) {
    AbstractValueModel valueModel = model.getModel(name,getterName,setterName);
    if (type == Type.READ) {
      return this.wrapWithSparkLineButton(valueModel,
          BasicComponentFactory.createLabel(valueModel, Utils.getNumberFormatInstance()));
    }

    if (values != null) {
      return this.wrapWithSparkLineButton(valueModel, new JComboBox(new ComboBoxAdapter(values,
          valueModel)));
    }

    switch (numType) {
    case BIGINTEGER: {
      return this.wrapWithSparkLineButton(valueModel,
          BasicComponentFactory.createIntegerField(valueModel, new ZeroNumberFormat(NumberFormat.getIntegerInstance())));
    }
    case BIGDECIMAL: {
      return this.wrapWithSparkLineButton(valueModel,
          BasicComponentFactory.createFormattedTextField(valueModel, new ZeroNumberFormat(Utils.getNumberFormatInstance())));

    }
    case BYTE: {
      return this.wrapWithSparkLineButton(valueModel,
          BasicComponentFactory.createIntegerField(valueModel, new ZeroNumberFormat(NumberFormat.getIntegerInstance())));
    }
    case SHORT: {
      return this.wrapWithSparkLineButton(valueModel,
          BasicComponentFactory.createIntegerField(valueModel, new ZeroNumberFormat(NumberFormat.getIntegerInstance())));
    }
    case INT: {
      return this.wrapWithSparkLineButton(valueModel,
          BasicComponentFactory.createIntegerField(valueModel, new ZeroNumberFormat(NumberFormat.getIntegerInstance())));
    }
    case DOUBLE: {
      return this.wrapWithSparkLineButton(valueModel,
          BasicComponentFactory.createFormattedTextField(valueModel, new ZeroNumberFormat(Utils.getNumberFormatInstance())));
    }
    case LONG: {
      return this.wrapWithSparkLineButton(valueModel,
          BasicComponentFactory.createLongField(valueModel, new ZeroNumberFormat(NumberFormat.getIntegerInstance())));
    }
    case FLOAT: {
      return this.wrapWithSparkLineButton(valueModel,
          BasicComponentFactory.createFormattedTextField(valueModel, new ZeroNumberFormat(Utils.getNumberFormatInstance())));
    }
    default:
      return this.wrapWithSparkLineButton(valueModel,
          BasicComponentFactory.createIntegerField(valueModel, new ZeroNumberFormat(NumberFormat.getIntegerInstance())));
    }
  }

  public JComponent wrapWithSparkLineButton(AbstractValueModel model, JComponent textComponent) {

    if (this.wrap) {

      JPanel combinedPanel = new JPanel();
      combinedPanel.setLayout(new GridLayout(1, 2));

      SparklineJComponent sparklineButton = new PropertySourcedSparklineJComponent(model, this);

      combinedPanel.add(textComponent);
      combinedPanel.add(sparklineButton);

      return combinedPanel;

    } else {

      return textComponent;

    }

  }

}

class ZeroNumberFormat extends NumberFormat {
  
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

/*
 * class ZeroNumberFormatter extends EmptyNumberFormat {
 * 
 * public ZeroNumberFormatter() { super(); }
 * 
 * public ZeroNumberFormatter(NumberFormat numberFormat) { super(numberFormat,
 * 0); }
 * 
 * @Override public Object stringToValue(String string) throws ParseException {
 * if (string == null || string.length() == 0) { Class clazz =
 * this.getValueClass(); if (clazz.equals(BigInteger.class)) return new
 * BigInteger("0"); if (clazz.equals(BigDecimal.class)) return new
 * BigDecimal(0.0); if (clazz.equals(Byte.class)) return new Byte((byte) 0); if
 * (clazz.equals(Double.class)) return new Double(0.0); if
 * (clazz.equals(Long.class)) return new Long(0); if (clazz.equals(Float.class))
 * return new Float(0); return new Integer(0); } return
 * super.stringToValue(string); }
 * 
 * @Override public String valueToString(Object object) throws ParseException {
 * if (object instanceof Number && ((Number) object).doubleValue() == 0.0)
 * return "0"; return super.valueToString(object); }
 */

