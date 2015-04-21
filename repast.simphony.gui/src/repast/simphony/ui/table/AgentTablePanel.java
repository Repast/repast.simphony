package repast.simphony.ui.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.RowFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunState;
import repast.simphony.ui.RSApplication;
import repast.simphony.ui.RSGUIConstants;
import saf.core.ui.Workspace;
import saf.core.ui.dock.DockableFrame;
import saf.core.ui.event.DockableFrameEvent;
import saf.core.ui.event.DockableFrameListener;

/**
 * Panel than holds the agent table and other GUI elements.
 * 
 * @author Eric Tatara
 *
 */
public class AgentTablePanel extends JPanel implements DockableFrameListener{
	private static final long serialVersionUID = 1904514116491789302L;

	public static final String TABLE_OBJ_KEY = "repast.simphony.ui.table.TABLE_OBJ_KEY";
	
	// List of toolbar contents that will be added to the parent dockable toolbar
	protected List<JComponent> toolbarItems = new ArrayList<JComponent>();
	protected List<AgentTableListener> listeners = new ArrayList<AgentTableListener>();
	protected JTabbedPane tabbedPane;
	
	public AgentTablePanel(Workspace<RSApplication> workspace, 	String displayName){
		super(new BorderLayout());
		setName(displayName);
		tabbedPane = new JTabbedPane();
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
				TableModel model = ((TablePanel)agentPanel).getTable().getModel();
				models.put(agentClass.getSimpleName(), model);
				
				// Initialize the table listeners from the model
				if (model instanceof ProbePropertyTableModel){
					listeners.addAll(((ProbePropertyTableModel)model).getListeners());
				}
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
			
		// Row filter dialog button
		JButton filterButton = new JButton(RSGUIConstants.SM_FILTER_ICON);
		filterButton.setToolTipText("Filter table");
		filterButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				Component panel = tabbedPane.getSelectedComponent();
				Frame frame = RSApplication.getRSApplicationInstance().getGui().getFrame();
				if (panel instanceof TablePanel){
					TableFilterDialog dialog = new TableFilterDialog((TablePanel)panel, frame);
					dialog.setVisible(true);
					
					if (!dialog.wasCanceled()){
						Set<RowFilter<Object, Object>> rowFilters = dialog.getRowFilterSet();
						((TablePanel)panel).setRowFilters(rowFilters);
					}
				}
				
			}
		});
		toolbarItems.add(filterButton);
	}

	public List<JComponent> getToolbarItems() {
		return toolbarItems;
	}
	
	public void addAgentTableListener(AgentTableListener listener){
		listeners.add(listener);
	}
	
	public boolean removeAgentTableListener(AgentTableListener listener){
		return listeners.remove(listener);
	}

	@Override
	public void dockableClosed(DockableFrameEvent evt) {
		DockableFrame view = evt.getDockable();
		
		// If this table panel caused the dock closed event
		if (this == view.getClientProperty(TABLE_OBJ_KEY)){
			for (AgentTableListener listener : listeners){
				listener.tableClosed();
			}
		}
	}

	@Override
	public void dockableClosing(DockableFrameEvent arg0) {
	}

	@Override
	public void dockableFloated(DockableFrameEvent arg0) {
	}

	@Override
	public void dockableFloating(DockableFrameEvent arg0) {
	}

	@Override
	public void dockableMaximized(DockableFrameEvent arg0) {
	}

	@Override
	public void dockableMaximizing(DockableFrameEvent arg0) {
	}

	@Override
	public void dockableMinimized(DockableFrameEvent arg0) {
	}

	@Override
	public void dockableMinimizing(DockableFrameEvent arg0) {
	}

	@Override
	public void dockableRestored(DockableFrameEvent arg0) {
	}

	@Override
	public void dockableRestoring(DockableFrameEvent arg0) {
	}
}