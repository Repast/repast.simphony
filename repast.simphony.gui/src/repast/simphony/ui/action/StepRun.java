package repast.simphony.ui.action;

import java.awt.event.ActionEvent;

import repast.simphony.ui.RSApplication;
import saf.core.ui.actions.AbstractSAFAction;

/**
 * Action for stepping a run
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class StepRun extends AbstractSAFAction<RSApplication> {

	public void actionPerformed(ActionEvent e) {
		workspace.getApplicationMediator().step();
	}
}
