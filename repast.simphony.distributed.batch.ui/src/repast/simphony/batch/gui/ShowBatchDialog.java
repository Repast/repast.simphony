/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

import repast.simphony.scenario.Scenario;
import repast.simphony.ui.RSApplication;
import saf.core.ui.actions.AbstractSAFAction;

/**
 * SAF action for displaying the batch run dialog.
 * 
 * @author Nick Collier
 */
@SuppressWarnings({ "serial" })
public class ShowBatchDialog extends AbstractSAFAction<RSApplication> {

  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent evt) {
    JFrame frame = workspace.getApplicationMediator().getGui().getFrame();
    JDialog dialog = new JDialog(frame);
    dialog.setModal(true);
    dialog.setLayout(new BorderLayout());
    Scenario scenario = workspace.getApplicationMediator().getCurrentScenario();
    MainPanel main = null;
    if (scenario != null) {
      main = new MainPanel(scenario.getScenarioDirectory().getParentFile());
    } else {
      main = new MainPanel();
    }
    dialog.add(main, BorderLayout.CENTER);
    dialog.addWindowListener(new OnClose(dialog, main));
  
    dialog.setSize(700, 500);
    dialog.setLocationRelativeTo(frame);
    dialog.setVisible(true);
  }
  
  private static class OnClose extends WindowAdapter {
    
    JDialog dialog;
    MainPanel main;
    
    public OnClose(JDialog dialog, MainPanel main) {
      this.main = main;
      this.dialog = dialog;
    }
    
    @Override
    public void windowClosing(WindowEvent evt) {
      main.onExit();
      dialog.dispose();
    }
  }
}
