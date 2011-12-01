package repast.simphony.ui.action;

import java.awt.event.ActionEvent;

import repast.simphony.ui.RSApplication;
import saf.core.ui.actions.AbstractSAFAction;

/**
 * Action for loading an existing frame layout.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
@SuppressWarnings("serial")
public class LoadLayout extends AbstractSAFAction<RSApplication> {

  public void actionPerformed(ActionEvent e) {
    workspace.getApplicationMediator().loadLayout();
  }
}
