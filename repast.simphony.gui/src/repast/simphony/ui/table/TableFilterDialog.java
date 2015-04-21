package repast.simphony.ui.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;

import repast.simphony.ui.RSApplication;
import repast.simphony.ui.RSGUIConstants;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Dialog for editing filters on TablePanels
 * 
 * @author Eric Tatara
 *
 */
public class TableFilterDialog extends JDialog {

	protected TablePanel tablePanel;
	protected Map<String,Integer> columnNameMap;
	protected boolean cancelled = true;
	protected JList<RowFilter<Object,Object>> filterList;
	protected DefaultListModel<RowFilter<Object,Object>> filterListModel;
	protected boolean reordering = false;
	protected int currentIndex = -1;
	
	protected DefaultComboBoxModel<String> columnModel;
	protected JComboBox<String> columnBox;
	protected JComboBox<NamedRowFilter.Operator> operatorBox;
	protected DefaultComboBoxModel<NamedRowFilter.Operator> operatorModel;
	protected DefaultComboBoxModel<String> filterValueModel;
	protected JComboBox<String> filterValueBox;
	
	public TableFilterDialog(TablePanel tablePanel, Frame frame){
		super(frame,true);
		setTitle(tablePanel.getName() + " Table Filters");
		this.tablePanel = tablePanel;
		add(getPanel());
		pack();
		init();
		setLocationRelativeTo(frame);
	}
	
	protected JPanel getPanel(){	
		setSize(new Dimension(600,400));
		FormLayout layout = new FormLayout(
				"pref:grow, 3dlu, pref:grow, 3dlu, pref, 3dlu, 50dlu, 3dlu, pref, pref:grow",  // columns
				"pref, 4dlu, pref, 3dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref:grow, 4dlu, pref, 4dlu"); //rows
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("New Filter", cc.xyw(1, 1, 10));
		builder.add(new JLabel("Column to filter"), cc.xy(1,3));
		builder.add(new JLabel("Condition"), cc.xy(3,3));
		builder.add(new JLabel("Value"), cc.xy(5,3));
		
		columnModel = new DefaultComboBoxModel<String>();
		columnBox = new JComboBox<String>(columnModel);
		columnBox.setToolTipText("Select the column to filter");
		columnBox.setEditable(false);
		builder.add(columnBox, cc.xy(1,5));
		
		operatorModel = new DefaultComboBoxModel<NamedRowFilter.Operator>();
		operatorBox = new JComboBox<NamedRowFilter.Operator>(operatorModel);
		operatorBox.setToolTipText("Select the filter condition");
		operatorBox.setEditable(false);
		builder.add(operatorBox, cc.xy(3,5));
		
		filterValueModel = new DefaultComboBoxModel<String>();
		filterValueBox = new JComboBox<String>(filterValueModel);
		filterValueBox.setToolTipText("Enter the filter condition value");
		filterValueBox.setEditable(true);
		builder.add(filterValueBox, cc.xyw(5,5,3));
		
		JButton addButton = new JButton(RSGUIConstants.ADD_ICON);
		addButton.setToolTipText("Add a new filter with the specified parameters");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addFilter();
			}
		});
		
		builder.add(addButton, cc.xy(9, 5));
		
		builder.addSeparator("Current Filters", cc.xyw(1, 7, 10));
		
		filterListModel = new DefaultListModel<RowFilter<Object,Object>>();
		filterList = new JList<RowFilter<Object,Object>>(filterListModel);
		filterList.setPreferredSize(new Dimension(100,300));
		filterList.setToolTipText("List of current filters");
		builder.add(new JScrollPane(filterList), cc.xywh(1, 9, 3, 3));
		filterList.setVisibleRowCount(14);
		filterList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		JButton removeFilterButton = new JButton(RSGUIConstants.DELETE_ICON);
		removeFilterButton.setToolTipText("Delete selected filters");
		removeFilterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List <RowFilter<Object,Object>> selected = filterList.getSelectedValuesList();
				
				for (RowFilter<Object,Object> filter : selected){
					filterListModel.removeElement(filter);
				}
			}
		});
		
		builder.add(removeFilterButton, cc.xy(5, 9));
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ok();
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		
		builder.add(okButton, cc.xy(1, 13));
		builder.add(cancelButton, cc.xy(3, 13));
		
		return builder.getPanel();
	}
	
	protected void init(){
		columnNameMap = new HashMap<String,Integer>();
		
		for (RowFilter<Object, Object> filter : tablePanel.getRowFilterList()){
			filterListModel.addElement(filter);
		}
		
		TableModel model = tablePanel.getTable().getModel();
		
		for (int col=0; col<model.getColumnCount(); col++){
			columnModel.addElement(model.getColumnName(col));
			columnNameMap.put(model.getColumnName(col), col);
		}
		
		columnBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectColumn();
			}
		});
		
		columnBox.setSelectedIndex(0);
	}

	public Set<RowFilter<Object, Object>> getRowFilterSet() {
		Set<RowFilter<Object,Object>> rowFilterList = 
				new HashSet<RowFilter<Object,Object>>();
		
		for (int i=0; i<filterListModel.size(); i++){
			rowFilterList.add(filterListModel.get(i));
		}
					
		return rowFilterList;
	}
	
	/**
	 * OK button is pressed
	 */
	protected void ok(){
		cancelled = false;
		setVisible(false);
		dispose();
	}
	
	/**
	 * Cancel button is pressed
	 */
	protected void cancel(){
		cancelled = true;
		setVisible(false);
		dispose();
	}
	
	public boolean wasCanceled(){
		return cancelled;
	}
	
	/**
	 * Sets dialog options based on selected column.
	 */
	protected void selectColumn(){
		int col = columnNameMap.get(columnBox.getSelectedItem());
		TableModel model = tablePanel.getTable().getModel();
		Class<?> clazz = model.getColumnClass(col);
		
		// Set appropriate logical operators depending on column class
		operatorModel.removeAllElements();
		if (Number.class.isAssignableFrom(clazz)){
			EnumSet<NamedRowFilter.Operator> set = EnumSet.allOf(NamedRowFilter.Operator.class);
			
			for (NamedRowFilter.Operator e : set){
				operatorModel.addElement(e);
			}
		}
		else {
			operatorModel.addElement(NamedRowFilter.Operator.EQUALS);
		}
	
		// Set filter value elements based on available values from selected column
		Set<Object> values = new TreeSet<Object>();
		for (int row=0; row < model.getRowCount(); row++){
			Object obj = model.getValueAt(row, col);
			String stringVal = null;
			
			// Component values are based on getName(), all other are Object.toString()
			if (obj != null){
				if (obj instanceof Component){
					stringVal = ((Component)obj).getName();
				}
				else{
					stringVal = obj.toString();
				}
			}
			if (stringVal != null)
				values.add(stringVal);
		}
		
		filterValueModel.removeAllElements();
		for (Object o : values){
			filterValueModel.addElement(o.toString());
		}
	}
	
	protected void addFilter(){
		String colName = columnBox.getItemAt(columnBox.getSelectedIndex());
		int col = columnNameMap.get(colName);
		Class<?> clazz = tablePanel.getTable().getModel().getColumnClass(col);
		NamedRowFilter.Operator op = operatorBox.getItemAt(operatorBox.getSelectedIndex());
		String filtVal = (String)filterValueBox.getSelectedItem();
		
		if (validateFilterValue(clazz, filtVal)){
			TableFilterFactory<Object, Object> fac = new TableFilterFactory<>();
			RowFilter<Object,Object> filter = fac.createFilter(colName, col, clazz, filtVal, op);
			filterListModel.addElement(filter);
		}
		else{
			JOptionPane.showMessageDialog(
					RSApplication.getRSApplicationInstance().getGui().getFrame(),
			    "Invald filter value",
			    "Filter error",
			    JOptionPane.ERROR_MESSAGE);
		}
	}

	protected boolean validateFilterValue(Class<?> clazz, String filtVal){
		if (Number.class.isAssignableFrom(clazz)){
			try {
				Double.parseDouble(filtVal);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		if (Boolean.class.isAssignableFrom(clazz)){
			if (filtVal != null && (
					filtVal.equalsIgnoreCase("true") || filtVal.equalsIgnoreCase("false"))) {
			
				return true;
			}
		}
		
		else if (filtVal != null)
			return true;

		return false;
	}
}
