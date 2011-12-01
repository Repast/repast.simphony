package repast.simphony.sql;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import repast.simphony.ui.RSApplication;
import saf.core.ui.Workspace;
import saf.core.ui.actions.ISAFAction;
import saf.core.ui.dock.DockableFrame;

/**
 * A plugin for executing SQL on agents.
 * 
 * @author Michael J. North
 */

public class RunSQLModel extends AbstractAction implements
		ISAFAction<RSApplication> {

	private Workspace<RSApplication> workspace;
	private int sqlWindowCounter = 0;

	public void initialize(Workspace<RSApplication> arg0) {
		workspace = arg0;
	}

	public void actionPerformed(ActionEvent arg0) {
		DockableFrame frame = workspace.getApplicationMediator().getGui()
				.addVizualization("SQL Panel (" + (++sqlWindowCounter) + ")",
						new SQLWindow());
		frame.toFront();
	}

	public RunSQLModel() {
		super();
	}
}
