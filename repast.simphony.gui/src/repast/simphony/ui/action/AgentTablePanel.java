package repast.simphony.ui.action;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunState;
import repast.simphony.ui.RSApplication;
import repast.simphony.ui.probe.ProbeManager;
import repast.simphony.ui.table.AgentTableFactory;
import saf.core.ui.Workspace;

/**
 * Panel than holds the agent table and other GUI elements.
 * 
 * @author Eric Tatara
 *
 */
public class AgentTablePanel extends JPanel {
	private static final long serialVersionUID = 1904514116491789302L;

	public AgentTablePanel(Workspace<RSApplication> workspace){
		super(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		add(tabbedPane);

		// TODO add support toolbar / buttons on top.

		ProbeManager probeManager = workspace.getApplicationMediator().getProbeManager();

		RunState currentRunState = workspace.getApplicationMediator().getController().getCurrentRunState();
		Context context = currentRunState.getMasterContext();
		
		for (Object agentType : context.getAgentTypes()){
			Class agentClass = (Class)agentType;

			JPanel agentPanel = AgentTableFactory.createTablePanel(context.getAgentLayer(agentClass));

			tabbedPane.addTab(agentClass.getSimpleName(), agentPanel);
		
		}
	}
}