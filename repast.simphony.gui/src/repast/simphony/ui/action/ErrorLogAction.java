package repast.simphony.ui.action;

import java.awt.event.ActionEvent;

import repast.simphony.ui.RSApplication;
import repast.simphony.ui.widget.ErrorLog;
import saf.core.ui.actions.AbstractSAFAction;

/**
 * Action for viewing the error log.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
@SuppressWarnings("serial")
public class ErrorLogAction extends AbstractSAFAction<RSApplication> {

  public void actionPerformed(ActionEvent e) {
    ErrorLog log = this.workspace.getApplicationMediator().getErrorLog();
    log.show();
  }
}
