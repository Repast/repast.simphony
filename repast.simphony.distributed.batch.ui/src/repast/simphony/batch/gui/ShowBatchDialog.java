/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.event.ActionEvent;

import repast.simphony.ui.RSApplication;
import saf.core.ui.actions.AbstractSAFAction;

/**
 * SAF action for displaying the batch run dialog.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class ShowBatchDialog extends AbstractSAFAction<RSApplication> {

  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent evt) {
    System.out.println("show batch dialog");
    
  }
  
  

}
