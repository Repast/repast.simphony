/**
 * 
 */
package repast.simphony.ui.parameters;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.JTextComponent;

import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.StringConverter;
import repast.simphony.ui.probe.StringConverterFormat;
import repast.simphony.ui.probe.Utils;
import repast.simphony.ui.probe.ZeroNumberFormat;

import com.jgoodies.binding.adapter.BasicComponentFactory;

/**
 * ParameterBinder for single unconstrained parameters.
 * 
 * @author Nick Collier
 */
public class DefaultParameterBinder extends AbstractParameterBinder {

  private Class<?> type;
  protected JTextComponent field;
  protected StringConverter<Object> converter;
  protected ParameterValueModel model;

  public DefaultParameterBinder(String name, String displayName, Class<?> type) {
    super(name, displayName);
    this.type = type;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * projz.parameter.CompCreator#create(repast.simphony.parameter.Parameters)
   */
  @SuppressWarnings("unchecked")
  @Override
  public JComponent getComponent(Parameters params) {
    this.params = params;
    if (field == null) {
      model = new ParameterValueModel(getName(), params);
      converter = params.getSchema().getDetails(getName()).getConverter();
      if (type.isPrimitive()) {
        NumberFormat format = getNumberFormat();
        if (type.equals(int.class) || type.equals(byte.class) || type.equals(short.class)) {
          field = BasicComponentFactory.createIntegerField(model, format);
        }

        else if (type.equals(long.class)) {
          field = BasicComponentFactory.createLongField(model, format);
        }

        else {
          field = BasicComponentFactory.createFormattedTextField(model, format);
        }

      } else {
        if (converter == null) {
          field = BasicComponentFactory.createTextField(model, true);
        } else {
          field = BasicComponentFactory.createFormattedTextField(model, new StringConverterFormat(
              converter));
        }
      }
    }

    return field;
  }

  private NumberFormat getNumberFormat() {
    if (type.equals(double.class) || type.equals(float.class)) {
      return new ZeroNumberFormat(Utils.getNumberFormatInstance());
    }

    return new ZeroNumberFormat(NumberFormat.getIntegerInstance());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * projz.parameter.ParameterBinder#reset(repast.simphony.parameter.Parameters)
   */
  @Override
  public void resetToDefault() {
    Object defaultValue = params.getSchema().getDetails(getName()).getDefaultValue();
    if (field instanceof JFormattedTextField) {
      AbstractFormatter formatter = ((JFormattedTextField) field).getFormatter();
      String val = null;
      try {
        val = formatter.valueToString(defaultValue);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      field.setText(val);
    } else {
      String val = converter.toString(defaultValue);
      field.setText(val);
    }
    toParameter();

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * projz.parameter.ParameterBinder#update(repast.simphony.parameter.Parameters
   * )
   */
  @Override
  public void toParameter() {
    if (field instanceof JFormattedTextField) {
      try {
        ((JFormattedTextField) field).commitEdit();
        // shouldn't happen
      } catch (ParseException e) {
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * projz.parameter.ParameterBinder#toXML(repast.simphony.parameter.Parameters)
   */
  @Override
  public String toXML() {
    toParameter();
    return toXML(params, params.getValueAsString(name), "");
  }
}
