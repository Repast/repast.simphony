/**
 * 
 */
package repast.simphony.ui.parameters;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import repast.simphony.parameter.Parameters;

import com.jgoodies.binding.adapter.BasicComponentFactory;

/**
 * ParameterBinder for boolean parameters.
 * 
 * @author Nick Collier
 */
public class BooleanParameterBinder extends AbstractParameterBinder {

  private JCheckBox box;

  public BooleanParameterBinder(String name, String displayName) {
    super(name, displayName);
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
    if (box == null) {
      ParameterValueModel model = new ParameterValueModel(getName(), params);
      box = BasicComponentFactory.createCheckBox(model, "");
    }
    return box;
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
    params.setValue(name, box.isSelected());
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
    boolean val = ((Boolean) defaultValue).booleanValue();
    box.setSelected(val);
  }


  
  /*
   * (non-Javadoc)
   * 
   * @see
   * projz.parameter.ParameterBinder#toXML(repast.simphony.parameter.Parameters)
   */
  @Override
  public String toXML() {
    return toXML(params, String.valueOf(box.isSelected()), "");
  }
}
