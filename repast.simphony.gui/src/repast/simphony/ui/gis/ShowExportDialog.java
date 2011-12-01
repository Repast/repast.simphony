package repast.simphony.ui.gis;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import repast.simphony.engine.environment.RunState;
import repast.simphony.ui.RSApplication;
import saf.core.ui.actions.AbstractSAFAction;

/**
 * @author Nick Collier
 */
public class ShowExportDialog extends AbstractSAFAction<RSApplication> {


  public void actionPerformed(ActionEvent e) {
    if (workspace.getApplicationMediator().getController() == null
				|| workspace.getApplicationMediator().getController()
						.getCurrentRunState() == null) {
			JOptionPane.showMessageDialog(null,
					"Please load a model and initialize the simulation before attempting to export " +
                  "a geography layer");
			return;
		}

		execute(workspace.getApplicationMediator().getController().getCurrentRunState());
  }

  private void execute(RunState currentRunState) {
    GeographyExportDialog dialog = new GeographyExportDialog(workspace.
            getApplicationMediator().getGui().getFrame());
    dialog.setModal(true);
    dialog.init(currentRunState.getMasterContext());
    dialog.setVisible(true);
  }
}
