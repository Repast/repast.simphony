package repast.simphony.ui.probe;

import java.awt.GridLayout;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
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
import com.jgoodies.common.format.EmptyNumberFormat;

/**
 * ProbedProperty for numeric types.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class NumericProbedProperty extends AbstractProbedProperty {

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

  private void setType(Class val) {
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

  public JComponent getComponent(PresentationModel model, boolean buffered) {
    AbstractValueModel valueModel = buffered ? model.getBufferedModel(name) : model.getModel(name);
    if (type == Type.READ) {
      return this.wrapWithSparkLineButton(model,
          BasicComponentFactory.createLabel(valueModel, Utils.getNumberFormatInstance()));
    }

    if (values != null) {
      return this.wrapWithSparkLineButton(model, new JComboBox(new ComboBoxAdapter(values,
          valueModel)));
    }

    switch (numType) {
    case BIGINTEGER: {
      EmptyNumberFormat formatter = new EmptyNumberFormat(Utils.getNumberFormatInstance(),
          new BigInteger("0"));
      // formatter.setValueClass(BigInteger.class);
      return this.wrapWithSparkLineButton(model,
          BasicComponentFactory.createFormattedTextField(valueModel, formatter));
    }
    case BIGDECIMAL: {
      EmptyNumberFormat formatter = new EmptyNumberFormat(Utils.getNumberFormatInstance(),
          new BigDecimal("0"));
      return this.wrapWithSparkLineButton(model,
          BasicComponentFactory.createFormattedTextField(valueModel, formatter));

    }
    case BYTE: {
      EmptyNumberFormat formatter = new EmptyNumberFormat(Utils.getNumberFormatInstance(),
          new Byte((byte) 0));
      return this.wrapWithSparkLineButton(model,
          BasicComponentFactory.createFormattedTextField(valueModel, formatter));
    }
    case SHORT: {
      EmptyNumberFormat formatter = new EmptyNumberFormat(Utils.getNumberFormatInstance(),
          new Short((short) 0));
      return this.wrapWithSparkLineButton(model,
          BasicComponentFactory.createFormattedTextField(valueModel, formatter));
    }
    case INT: {
      //EmptyNumberFormat formatter = new EmptyNumberFormat(NumberFormat.getIntegerInstance(),
       //   Integer.valueOf(0));
      return this.wrapWithSparkLineButton(model,
          BasicComponentFactory.createIntegerField(valueModel, 0));
    }
    case DOUBLE: {
      EmptyNumberFormat formatter = new EmptyNumberFormat(Utils.getNumberFormatInstance(),
          Double.valueOf(0));
      return this.wrapWithSparkLineButton(model,
          BasicComponentFactory.createFormattedTextField(valueModel, formatter));
    }
    case LONG: {
      return this.wrapWithSparkLineButton(model,
          BasicComponentFactory.createLongField(valueModel, 0L));
    }
    case FLOAT: {
      EmptyNumberFormat formatter = new EmptyNumberFormat(Utils.getNumberFormatInstance(),
          Float.valueOf(0));
      return this.wrapWithSparkLineButton(model,
          BasicComponentFactory.createFormattedTextField(valueModel, formatter));
    }
    default:
      return this.wrapWithSparkLineButton(model,
          BasicComponentFactory.createIntegerField(valueModel, 0));
    }
  }

  public JComponent wrapWithSparkLineButton(PresentationModel model, JComponent textComponent) {

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

