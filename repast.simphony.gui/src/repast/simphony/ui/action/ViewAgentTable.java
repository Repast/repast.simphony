package repast.simphony.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JToolBar;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.ui.RSApplication;
import repast.simphony.ui.RSGui;
import repast.simphony.ui.table.AgentTablePanel;
import saf.core.ui.actions.AbstractSAFAction;
import saf.core.ui.dock.DockableFrame;

/**
 * Action that creates a table of agents and their probe-able properties
 * 
 * @author Eric Tatara
 */
@SuppressWarnings("serial")
public class ViewAgentTable extends AbstractSAFAction<RSApplication> {

	public static final String DISPLAY_NAME = "Agent Table";

	@Override
	public void actionPerformed(ActionEvent e) {
		RSGui gui = workspace.getApplicationMediator().getGui();
		double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		String displayName = DISPLAY_NAME + " @ tick " + tick;
		
		AgentTablePanel panel = new AgentTablePanel(workspace, displayName);	
		DockableFrame dockable = gui.addVizualization(displayName,	panel);
		gui.addViewListener(panel);
		
		// Used to check which table fires a DockableFrameEvent
		dockable.putClientProperty(AgentTablePanel.TABLE_OBJ_KEY, panel);
		
		JToolBar toolBar = gui.getToolBar(panel);
		
		for (JComponent comp : panel.getToolbarItems()){
			toolBar.add(comp);	
		}
		
		dockable.toFront();
	}
}