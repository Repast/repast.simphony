package repast.simphony.jung;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import repast.simphony.ui.RSApplication;
import saf.core.ui.Workspace;
import saf.core.ui.actions.ISAFAction;
import saf.core.ui.dock.DockableFrame;

/**
 * A plugin for executing JUNG on networks.
 * 
 * @author Michael J. North
 */
public class RunJungModel extends AbstractAction implements
		ISAFAction<RSApplication> {

	private Workspace<RSApplication> workspace;
	private int jungWindowCounter = 0;

	public void initialize(Workspace<RSApplication> arg0) {
		workspace = arg0;
	}

	public void actionPerformed(ActionEvent arg0) {
		DockableFrame frame = workspace.getApplicationMediator().getGui()
				.addVizualization("JUNG Panel (" + (++jungWindowCounter) + ")",
						new JungWindow());
		frame.toFront();
	}

	public RunJungModel() {
		super();
	}
}
