package repast.simphony.ui.table;

import java.awt.BorderLayout;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableRowSorter;

import repast.simphony.ui.probe.ProbedPropertiesFinder;

/**
 * Factory for creating tables of agents and their properties
 * 
 * @author Eric Tatara
 *
 */
public class AgentTableFactory {

	/**
	 * Create an TablePanel with agent properties such that each agent is a row
	 *   in the table, and each column is an agent property.
	 *   
	 * @param agents the Iterable of agents to add to the table
	 * @param tableName
	 * @return the table panel
	 */
	public static JPanel createAgentTablePanel(Iterable agents, String tableName){

		List<List<ProbedPropertiesFinder.Property>> agentPropList = 
				new ArrayList<List<ProbedPropertiesFinder.Property>>();

		ProbedPropertiesFinder finder = new ProbedPropertiesFinder();

		// Create and store the probed properties for each agent
		for (Object agent : agents){
			try {
				agentPropList.add(finder.findProperties(agent));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | IntrospectionException e) {
				e.printStackTrace();
			}
		}

		if (agentPropList.size() > 0){
			ProbePropertyTableModel model = new ProbePropertyTableModel(agentPropList);
			TablePanel tablePanel = new TablePanel(model, tableName);
			tablePanel.insertRowLabels();

			configureSort(model, tablePanel);
			tablePanel.autoResizeColumnWidth();
			
			return tablePanel;
		}
		else{
			JPanel panel = new JPanel(new BorderLayout());
			panel.setBorder(new EmptyBorder(10, 20, 10, 10));
			panel.add(new JLabel("No instances currently exist in simulation."), BorderLayout.NORTH);
			return panel;
		}
	}
	
	/**
	 * Configures sorting on table rows
	 * 
	 * @param model
	 * @param tablePanel
	 */
	protected static void configureSort(ProbePropertyTableModel model, TablePanel tablePanel){
		// Automatically sort the first column of Agent IDs
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));

		TableRowSorter<ProbePropertyTableModel> sorter = 
				(TableRowSorter)tablePanel.getTable().getRowSorter();

		sorter.setSortKeys(sortKeys);
		sorter.sort();

		// Provide a comparator for JComponent table cells that sorts based on the
		//  JComponent name
		for (int col=0; col<model.getColumnCount(); col++){
			if (model.getColumnClass(col) != null && 
					JComponent.class.isAssignableFrom(model.getColumnClass(col))){

				sorter.setComparator(col, new Comparator<JComponent>(){
					@Override
					public int compare(JComponent obj1, JComponent obj2){
						if (obj1 == null) {
							return -1;
						}
						if (obj2 == null) {
							return 1;
						}

						String t1 = obj1.getName();
						String t2 = obj2.getName();

						if (t1 == null) {
							return -1;
						}
						if (t2 == null) {
							return 1;
						}
						if (t1.equals(t2)) {
							return 0;
						}
						return t1.compareTo(t2);
					}
				});
			}
		}
	}
}