package repast.simphony.ui.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

import repast.simphony.ui.probe.ProbedPropertiesFinder;
import simphony.util.messages.MessageCenter;

/**
 * TableModel for Agent tables
 * 
 * @author Eric Tatara
 *
 */
public class ProbePropertyTableModel extends DefaultTableModel {

	private MessageCenter msgCenter = MessageCenter.getMessageCenter(ProbePropertyTableModel.class);
	
	// Map of column (property) names and column index
	protected Map<String,Integer> columnMap = new HashMap<String,Integer>();
	
	// Map of column index and column class
	protected Map<Integer, Class<?>> colClassMap = new HashMap<Integer,Class<?>>();
	
	// Map of booleans that determine if a colum is editable
	protected Map<Integer,Boolean> columEditable = new HashMap<Integer, Boolean>();
	
	public ProbePropertyTableModel(List<List<ProbedPropertiesFinder.Property>> agentPropList) {
		
		// Setup columns - use the first entry to create column names
		List<ProbedPropertiesFinder.Property> propListFirst = agentPropList.get(0);
		List<String> tableHeaders = new ArrayList<String>();
		
		for (ProbedPropertiesFinder.Property probe : propListFirst){
			String probeID = probe.getName();
			Integer col = columnMap.get(probeID);
			
			if (col == null){
				Object value = probe.getValue();
				
				if (value != null){
					addColumn(probe.getDisplayName());
					tableHeaders.add(probe.getDisplayName());
					col = getColumnCount() - 1;  // note zero first index
					columnMap.put(probeID, col);
					
					if (probe.getUiCreator() != null){
						colClassMap.put(col, JComponent.class);
						columEditable.put(col,true);
					}
					else
						colClassMap.put(col, value.getClass());
				}
			}
			else {
				// TODO handle duplicate probe names?
			}
		}
		
		Object [][] tableData = new Object[agentPropList.size()][tableHeaders.size()];
		setDataVector(tableData, tableHeaders.toArray());
		
		// Now that the TableModel has been setup, set the actual values.
		int row = 0;
		for (List<ProbedPropertiesFinder.Property> propList : agentPropList){		
			for (ProbedPropertiesFinder.Property probe : propList){
				String probeID = probe.getName();
				Integer col = columnMap.get(probeID);
				
				// If no column found, then skip this probe
				if (col == null) continue;  
				
				Object value = null;
				if (probe.getUiCreator() != null){
					try {
						value = probe.getUiCreator().getComponent(null);
					}
					catch(Exception e){
						msgCenter.warn("Error creating probe for " + probeID, e);
					}
				}
				else
					value = probe.getValue();
				
				if (value != null)
					setValueAt(value, row, col);
			}
			row++;
		}
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		return colClassMap.get(col);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		
		// Currently allows column-based editing
		if (columEditable.get(col) != null && columEditable.get(col) == true)
			return true;
		
		return false;
	}
}