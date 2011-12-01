/**
 * 
 */
package repast.simphony.data2.wizard;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import repast.simphony.data2.engine.DataSetComponentControllerAction;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;

/**
 * Utility methods for creating data wizards.
 * 
 * @author Nick Collier
 */
public class WizardUtils {
  
  public static  Border createDefaultEmptyBorder() {
    return BorderFactory.createEmptyBorder(4, 4, 4, 4);
  }
}
