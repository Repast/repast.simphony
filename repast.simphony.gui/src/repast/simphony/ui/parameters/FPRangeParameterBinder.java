/**
 * 
 */
package repast.simphony.ui.parameters;

import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import repast.simphony.parameter.Parameters;

import com.jgoodies.binding.adapter.SpinnerAdapterFactory;

/**
 * ParameterBinder for floating point ranged parameters. The
 * parameter can be edited with a number spinner.
 * 
 * @author Nick Collier
 */
public class FPRangeParameterBinder extends AbstractParameterBinder {

  private double min, max, spacing;

  private JSpinner spinner;

  public FPRangeParameterBinder(String name, String displayName, double min, double max, double spacing) {
    super(name, displayName);
    this.min = min;
    this.max = max;
    this.spacing = spacing;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * projz.parameter.CompCreator#create(repast.simphony.parameter.Parameters)
   */
  @Override
  public JComponent getComponent(Parameters params) {
    this.params = params;
    if (spinner == null) {
      double val = ((Number) params.getValue(name)).doubleValue();
      ParameterValueModel model = new ParameterValueModel(getName(), params);
      SpinnerModel spinModel = new SpinnerNumberModel(val, min, max, spacing);
      SpinnerAdapterFactory.connect(spinModel, model, val);
      spinner = new JSpinner(spinModel);
    }
    return spinner;
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
    double val = ((Double) defaultValue).doubleValue();
    spinner.setValue(val);
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
    try {
      spinner.commitEdit();
    } catch (ParseException e) {
      e.printStackTrace();
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
    return toXML(params, String.valueOf(spinner.getValue()),
        String.format("range=\"%d %d %d\"", min, max, spacing));
  }
}
