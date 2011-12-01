package repast.simphony.ui.action;

import java.awt.event.ActionEvent;

import repast.simphony.ui.RSApplication;
import saf.core.ui.actions.AbstractSAFAction;

/**
 * Action for saving a model with save as semantics.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/03 18:23:19 $
 */
public class SaveAsModel extends AbstractSAFAction<RSApplication> {

	public void actionPerformed(ActionEvent e) {
		workspace.getApplicationMediator().saveAs();
	}
}
