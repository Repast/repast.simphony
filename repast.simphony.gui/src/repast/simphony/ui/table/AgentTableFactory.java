package repast.simphony.ui.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.IntrospectionException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableRowSorter;

import repast.simphony.ui.RSGUIConstants;
import repast.simphony.ui.probe.ProbedPropertiesFinder;

/**
 * Creates a Table of agents and their properties
 * 
 * @author Eric Tatara
 *
 */
public class AgentTableFactory {
	
	
	
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
		
		ProbePropertyTableModel model = new ProbePropertyTableModel(agentPropList);
		
		TablePanel tablePanel = new TablePanel(model, tableName);
		initToolBar(tablePanel);
		
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
	
	protected static void initToolBar(TablePanel tablePanel){
		JToolBar toolbar = new JToolBar();
		tablePanel.addToolBar(toolbar);
		
		JButton excelButton = new JButton(RSGUIConstants.SM_SAVE_ICON);
		excelButton.setToolTipText("Export table to Excel");
		excelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter("Excel File","xlsx"));
				fc.setCurrentDirectory(new File("."));
				fc.setMultiSelectionEnabled(true);
				int returnVal = fc.showSaveDialog(tablePanel);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					SpreadsheetUtils.saveSingleTableAsExcel(
							tablePanel.getTable().getModel(), tablePanel.getName(), file);
				}
			}
		});
		
		toolbar.add(excelButton);
	}
}