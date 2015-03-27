package repast.simphony.ui.table;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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

	public static JPanel createTablePanel(Iterable agents){
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(true);
		DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);
			
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
			table.setValueAt(agent.toString(), row, 0);
			
			Map<String,Object> props = agentMap.get(agent);
			
			// TODO use a map for property name -> col index 
			int col = 1;
			for (String propName : props.keySet()){
				Object val = props.get(propName);
				
				table.setValueAt(val, row, col);
				col++;
			}
			row++;
		}
		
		// Set Table behaviors and styles
		TableRowSorter sorter = new TableRowSorter(model);
		table.setRowSorter(sorter);
	
		// Automatically sort the first column of Agent IDs
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();
		
		// TODO add filtering
//		 sorter.setRowFilter(RowFilter.regexFilter(".*foo.*"));
		
		table.setCellSelectionEnabled(true);
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(true);
		//	table.setAutoCreateRowSorter(true);   // TODO add a toggle button to enable
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane scrollPane = new JScrollPane(table);
		
		JTable rowTable = new RowTable(table);
		scrollPane.setRowHeaderView(rowTable);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,rowTable.getTableHeader());
		
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}
}