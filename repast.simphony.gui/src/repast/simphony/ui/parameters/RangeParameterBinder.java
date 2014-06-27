/**
 * 
 */
package repast.simphony.ui.parameters;

import javax.swing.JComponent;
import javax.swing.JSlider;

import repast.simphony.parameter.Parameters;

import com.jgoodies.binding.adapter.BoundedRangeAdapter;

/**
 * ParameterBinder for ranged parameters.
 * 
 * @author Nick Collier
 */
public class RangeParameterBinder extends AbstractParameterBinder {

  private int min, max, spacing;

  private JSlider slider;

  public RangeParameterBinder(String name, String displayName, int min, int max, int spacing) {
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
    if (slider == null) {
      ParameterValueModel model = new ParameterValueModel(getName(), params);
      slider = new JSlider(new BoundedRangeAdapter(model, 0, min, max));
      slider.setPaintLabels(true);
      slider.setPaintTicks(true);
      slider.setMajorTickSpacing(spacing);
      slider.setSnapToTicks(true);
    }
    return slider;
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
    int val = ((Integer) defaultValue).intValue();
    slider.setValue(val);
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
    params.setValue(name, slider.getValue());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * projz.parameter.ParameterBinder#toXML(repast.simphony.parameter.Parameters)
   */
  @Override
  public String toXML() {
    return toXML(params, String.valueOf(slider.getValue()),
        String.format("range=\"%d %d %d\"", min, max, spacing));
  }
}
