package repast.simphony.ui.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import repast.simphony.ui.probe.ProbedPropertiesFinder;

public class ProbePropertyTableModel extends DefaultTableModel {

	// Map of column (property) names and column index
	protected Map<String,Integer> columnMap = new HashMap<String,Integer>();
	
	// Map of column index and column class
	protected Map<Integer, Class<?>> colClassMap = new HashMap<Integer,Class<?>>();
	
	public ProbePropertyTableModel(List<List<ProbedPropertiesFinder.Property>> agentPropList) {
		
		// Setup columns
		// Use the first entry to create column names
		List<ProbedPropertiesFinder.Property> propListFirst = agentPropList.get(0);
		List<String> tableHeaders = new ArrayList<String>();
		
		for (ProbedPropertiesFinder.Property probe : propListFirst){
			String probeID = probe.getName();
			Integer col = columnMap.get(probeID);
			
			if (col == null){
				addColumn(probe.getDisplayName());
				tableHeaders.add(probe.getDisplayName());
				col = getColumnCount() - 1;  // note zero first index
				columnMap.put(probeID, col);  
				colClassMap.put(col, probe.getValue().getClass());
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
				
				setValueAt(probe.getValue(), row, col);
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
		return false;
	}
}