package repast.simphony.ui.table;

import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
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
	
	protected List<AgentTableListener> listeners = new ArrayList<AgentTableListener>();
	
	public ProbePropertyTableModel(List<List<ProbedPropertiesFinder.Property>> agentPropList) {
	
		initColumns(agentPropList);
		initColumnData(agentPropList);
	}
	
	/**
	 * Configure the columns from data in the agent properties list.
	 * 
	 * @param agentPropList
	 */
	protected void initColumns(List<List<ProbedPropertiesFinder.Property>> agentPropList){
		// Setup columns - use the first entry to create column names
		List<ProbedPropertiesFinder.Property> propListFirst = agentPropList.get(0);
		List<String> tableHeaders = new ArrayList<String>();
		
		ProbedPropertiesFinder.Property agentIDProp = propListFirst.remove(0);
		Collections.sort(propListFirst);
		propListFirst.add(0, agentIDProp);
		
		for (ProbedPropertiesFinder.Property probe : propListFirst){
			String probeID = probe.getName();
			Integer col = columnMap.get(probeID);
			
			if (col == null){
					addColumn(probe.getDisplayName());
					tableHeaders.add(probe.getDisplayName());
					col = getColumnCount() - 1; 
					columnMap.put(probeID, col);
					
					if (probe.getUiCreator() != null){
						colClassMap.put(col, JComponent.class);
						columEditable.put(col,true);
					}
					else
						colClassMap.put(col, probe.getType());
			}
			else {
				// TODO handle duplicate probe names?
			}
		}
		
//		// Create a sorted map of property names, so columns appear alphabetically
//		Map<String,ProbedPropertiesFinder.Property> probeIDMap = 
//				new TreeMap<String,ProbedPropertiesFinder.Property>();
//		
//		for (ProbedPropertiesFinder.Property probe : propListFirst){
//			probeIDMap.put(probe.getName(), probe);
//		}
//		
//		for (String probeID : probeIDMap.keySet()){
//			ProbedPropertiesFinder.Property probe = probeIDMap.get(probeID);
//
//			addColumn(probe.getDisplayName());
//			tableHeaders.add(probe.getDisplayName());
//			int col = getColumnCount() - 1; 
//			columnMap.put(probeID, col);
//
//			if (probe.getUiCreator() != null){
//				colClassMap.put(col, JComponent.class);
//				columEditable.put(col,true);
//			}
//			else
//				colClassMap.put(col, probe.getType());
//		}
//		
		Object [][] tableData = new Object[agentPropList.size()][tableHeaders.size()];
		setDataVector(tableData, tableHeaders.toArray());
	}
		
	/**
	 * Sets the cell values in each row, column from data in the agent properties list.
	 * 
	 * @param agentPropList
	 */
	protected void initColumnData(List<List<ProbedPropertiesFinder.Property>> agentPropList){
		int row = 0;
		for (List<ProbedPropertiesFinder.Property> propList : agentPropList){
			Integer myrow = row;
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
				
				if (value != null){
					setValueAt(value, row, col);}
				
				// For JComponents, setup listeners to sync with table / component events
				if (value instanceof JComponent){
					JComponent comp = (JComponent)value;
					
					List<Component> components = getAllComponents(comp);
					
					for (Component child : components){
						// Agent table listeners respond to table events
						if (child instanceof AgentTableListener){
							listeners.add((AgentTableListener)child);
						}
						// Some Jbuttons might be colored or changed text, so when this happens,
						//  we need to let the table know so that it can repaint.
						if (child instanceof JButton){
							((JButton)child).addPropertyChangeListener(new PropertyChangeListener() {
								
								@Override
								public void propertyChange(PropertyChangeEvent evt) {
									String propName = evt.getPropertyName();
									
									if (propName.equals("background") || propName.equals("text")){
										fireTableCellUpdated(myrow, col);
									}
								}
							});
						}
					}
				}
			}
			row++;
		}
	}
		
	@Override
	public Class<?> getColumnClass(int col) {
		return colClassMap.get(col);
	}

	/**
	 * Recursively find all Components in the provided container.
	 * 
	 * @param c the container
	 * @return a list of components in the container
	 */
	public static List<Component> getAllComponents(final Container c) {
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		
		// Currently allows column-based editing
		if (columEditable.get(col) != null && columEditable.get(col) == true)
			return true;
		
		return false;
	}

	public List<AgentTableListener> getListeners() {
		return listeners;
	}
}