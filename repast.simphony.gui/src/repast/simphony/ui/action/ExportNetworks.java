/**
 * 
 */
package repast.simphony.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

import repast.simphony.engine.environment.RunState;
import repast.simphony.ui.RSApplication;
import repast.simphony.ui.widget.NetworkExportDialog;
import saf.core.ui.actions.AbstractSAFAction;

/**
 * Action that launches the export network dialog.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class ExportNetworks extends AbstractSAFAction<RSApplication> {

  @SuppressWarnings("unchecked")
  @Override
  public void actionPerformed(ActionEvent e) {
    JFrame frame = workspace.getApplicationMediator().getGui().getFrame();
    NetworkExportDialog dialog = new NetworkExportDialog(frame);
    dialog.setModal(true);
    RunState currentRunState = workspace.getApplicationMediator().getController().getCurrentRunState();
    dialog.init(currentRunState.getMasterContext());
    dialog.pack();
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dialog.setLocationRelativeTo(frame);
    dialog.setVisible(true);
  }
}
