package repast.simphony.ui.table;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunState;
import repast.simphony.ui.RSApplication;
import repast.simphony.ui.RSGUIConstants;
import saf.core.ui.Workspace;

/**
 * Panel than holds the agent table and other GUI elements.
 * 
 * @author Eric Tatara
 *
 */
public class AgentTablePanel extends JPanel {
	private static final long serialVersionUID = 1904514116491789302L;

	// List of toolbar contents that will be added to the parent dockable toolbar
	protected List<JComponent> toolbarItems = new ArrayList<JComponent>();
	
	public AgentTablePanel(Workspace<RSApplication> workspace, 	String displayName){
		super(new BorderLayout());
		setName(displayName);
		JTabbedPane tabbedPane = new JTabbedPane();
		add(tabbedPane);

		// TODO add support toolbar / buttons on top.

		RunState currentRunState = 
				workspace.getApplicationMediator().getController().getCurrentRunState();
		Context context = currentRunState.getMasterContext();
		
		Map<String,TableModel> models = new HashMap<String,TableModel>();
		
		// Create a tab panel for each agent layer
		for (Object agentType : context.getAgentTypes()){
			Class agentClass = (Class)agentType;

			JPanel agentPanel = AgentTableFactory.createAgentTablePanel(
					context.getAgentLayer(agentClass), agentClass.getSimpleName());

			tabbedPane.addTab(agentClass.getSimpleName(), agentPanel);
			
			if (agentPanel instanceof TablePanel){
				models.put(agentClass.getSimpleName(), 
						((TablePanel)agentPanel).getTable().getModel());
			}
		}
		
		initToolBar(models);
	}
	
	protected void initToolBar(Map<String,TableModel> models){
		
		// Export table to Excel button
		JButton excelButton = new JButton(RSGUIConstants.SM_SAVE_ICON);
		excelButton.setToolTipText("Export table to Excel");
		excelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter("Excel File","xlsx"));
				fc.setCurrentDirectory(new File("."));
				fc.setSelectedFile(new File(getName()+".xlsx"));
				fc.setMultiSelectionEnabled(true);
				int returnVal = fc.showSaveDialog(AgentTablePanel.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					SpreadsheetUtils.saveTablesAsExcel(models, file);
				}
			}
		});
		toolbarItems.add(excelButton);
	}

	public List<JComponent> getToolbarItems() {
		return toolbarItems;
	}
}