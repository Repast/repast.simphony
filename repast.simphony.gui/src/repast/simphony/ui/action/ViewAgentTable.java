package repast.simphony.ui.action;

import java.awt.event.ActionEvent;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.ui.RSApplication;
import repast.simphony.ui.RSGui;
import saf.core.ui.actions.AbstractSAFAction;
import saf.core.ui.dock.DockableFrame;

/**
 * Action that creates a table of agents and their probe-able properties
 * 
 * @author Eric Tatara
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class ViewAgentTable extends AbstractSAFAction<RSApplication> {

	public static final String DISPLAY_NAME = "Agent Table";

	@Override
	public void actionPerformed(ActionEvent e) {
		RSGui gui = workspace.getApplicationMediator().getGui();
		double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		AgentTablePanel panel = new AgentTablePanel(workspace);
		
		DockableFrame dockable = gui.addVizualization(DISPLAY_NAME + " | tick: " + tick,	panel);
		dockable.toFront();}
}
