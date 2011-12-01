package repast.simphony.ui.action;

import repast.simphony.ui.RSApplication;
import repast.simphony.ui.widget.ErrorLog;
import saf.core.ui.actions.AbstractSAFAction;

import java.awt.event.ActionEvent;

/**
 * Action for reseting the layout.
 *
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class ResetLayout extends AbstractSAFAction<RSApplication> {

  public void actionPerformed(ActionEvent e) {
    this.workspace.getApplicationMediator().resetLayout();
  }
}
