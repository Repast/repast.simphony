package repast.simphony.ui.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Creates a Table of agents and their properties
 * 
 * @author Eric Tatara
 *
 */
public class AgentTableFactory {
	
	public static JPanel createAgentTablePanel(Iterable agents){
		DefaultTableModel model = new DefaultTableModel();
		TablePanel tablePanel = new TablePanel(model);
			
		Map<Object, Map<String,Object>> agentMap = new HashMap<Object, Map<String,Object>>();
		
		for (Object agent : agents){
					
			// TODO get the properties via probe
			Map<String,Object> properties = new HashMap();
			
			agentMap.put(agent, properties);
		}
		
		
		// Set the table headers from the agent property descriptions
		List<String> tableHeaders = new ArrayList<String>();
		
		// Insert ID at front of list
		tableHeaders.add(0, "ID");
		
		// TODO add the properties
		tableHeaders.add(1, "Zip");
		tableHeaders.add(2, "Zop");
		tableHeaders.add(3, "Zup");;

		Object [][] tableData = new Object[agentMap.keySet().size()][tableHeaders.size()];
		
		model.setDataVector(tableData, tableHeaders.toArray());

		// Now that the TableModel has been setup, set the actual values.
		
		int row = 0;
		for (Object agent : agentMap.keySet()){
			model.setValueAt(agent.toString(), row, 0);
			
			Map<String,Object> props = agentMap.get(agent);
			
			// TODO use a map for property name -> col index 
			int col = 1;
			for (String propName : props.keySet()){
				Object val = props.get(propName);
				
				model.setValueAt(val, row, col);
				col++;
			}
			row++;
		}
		
		tablePanel.insertRowLabels();
	
		// Automatically sort the first column of Agent IDs
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		
		TableRowSorter sorter = (TableRowSorter)tablePanel.getTable().getRowSorter();

		sorter.setSortKeys(sortKeys);
		sorter.sort();
		
		// TODO add filtering
//		 sorter.setRowFilter(RowFilter.regexFilter(".*foo.*"));
	
		return tablePanel;
	}
}