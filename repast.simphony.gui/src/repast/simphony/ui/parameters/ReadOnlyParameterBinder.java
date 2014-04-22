/**
 * 
 */
package repast.simphony.ui.parameters;

import javax.swing.JComponent;
import javax.swing.JLabel;

import repast.simphony.parameter.Parameters;

/**
 * ParameterBinder for read-only parameters
 * 
 * @author Nick Collier
 */
public class ReadOnlyParameterBinder extends AbstractParameterBinder {

  public ReadOnlyParameterBinder(String name, String displayName) {
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
    return new JLabel(params.getValueAsString(name));
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
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * projz.parameter.ParameterBinder#reset(repast.simphony.parameter.Parameters)
   */
  @Override
  public void resetToDefault() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * projz.parameter.ParameterBinder#toXML(repast.simphony.parameter.Parameters)
   */
  @Override
  public String toXML() {
    return toXML(params, params.getValueAsString(name), "");
  }
}
