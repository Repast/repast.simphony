
package repast.simphony.ui.probe;

import javax.swing.JComponent;

import com.jgoodies.binding.PresentationModel;

/**
 * Interface for classes that can create the UI for a Probed property.
 * 
 * @author Nick Collier
 */
public interface ProbedPropertyUICreator {
  
  /**
   * Gets the JComponent used to display or interact with this property.
   * 
   * @param model the model to bind the property to a GUI widget if required.
   * @param buffered
   * @return
   */
  JComponent getComponent(PresentationModel<Object> model);


  /**
   * Gets the display name to use a label the probe panel.
   * 
   * @return the display name to use a label in the UI.
   */
  public String getDisplayName();

}
