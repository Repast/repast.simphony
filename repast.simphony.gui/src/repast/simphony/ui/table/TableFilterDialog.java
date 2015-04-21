package repast.simphony.ui.table;

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

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;

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
	protected JComboBox<Enum<NamedRowFilter.Operator>> operatorBox;
	protected DefaultComboBoxModel<Enum<NamedRowFilter.Operator>> operatorModel;
	protected JTextField filterValueField;
	
	public TableFilterDialog(TablePanel tablePanel, Frame frame){
		super(frame,true);
		setTitle(tablePanel.getName() + " Table Filters");
		this.tablePanel = tablePanel;
		
		add(getPanel());
		init();
	}
	
	protected JPanel getPanel(){	
		setSize(new Dimension(600,400));
		FormLayout layout = new FormLayout(
				"pref:grow, 3dlu, pref:grow, 3dlu, pref, 3dlu, pref, pref:grow",  // columns
				"pref, 4dlu, pref, 3dlu, pref, 4dlu, pref, 4dlu, pref:grow, 4dlu, pref, 4dlu, pref, 4dlu"); //rows
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("New Filter", cc.xyw(1, 1, 8));
		builder.add(new JLabel("Column to filter"), cc.xy(1,3));
		builder.add(new JLabel("Condition"), cc.xy(3,3));
		builder.add(new JLabel("Value"), cc.xy(5,3));
		
		columnModel = new DefaultComboBoxModel<String>();
		columnBox = new JComboBox<String>(columnModel);
		columnBox.setToolTipText("Select the column to filter");
		columnBox.setEditable(false);
		builder.add(columnBox, cc.xy(1,5));
		
		operatorModel = new DefaultComboBoxModel<Enum<NamedRowFilter.Operator>>();
		operatorBox = new JComboBox<Enum<NamedRowFilter.Operator>>(operatorModel);
		operatorBox.setToolTipText("Select the filter condition");
		operatorBox.setEditable(false);
		builder.add(operatorBox, cc.xy(3,5));
		
		filterValueField = new JTextField();
		filterValueField.setEditable(true);
		builder.add(filterValueField, cc.xy(5,5));
		
		JButton addButton = new JButton(RSGUIConstants.ADD_ICON);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addFilter();
			}
		});
		
		builder.add(addButton, cc.xy(7, 5));
		
		builder.addSeparator("Current Filters", cc.xyw(1, 7, 8));
		
		filterListModel = new DefaultListModel<RowFilter<Object,Object>>();
		filterList = new JList<RowFilter<Object,Object>>(filterListModel);
		filterList.setPreferredSize(new Dimension(100,300));
		filterList.setToolTipText("List of current filters");
		builder.add(new JScrollPane(filterList), cc.xywh(1, 9, 3, 4));
		filterList.setVisibleRowCount(14);
		filterList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		JButton removeFilterButton = new JButton(RSGUIConstants.DELETE_ICON);
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
	
	protected void ok(){
		cancelled = false;
		setVisible(false);
		dispose();
	}
	
	protected void cancel(){
		cancelled = true;
		setVisible(false);
		dispose();
	}
	
	public boolean wasCanceled(){
		return cancelled;
	}
	
	protected void selectColumn(){
		int col = columnNameMap.get(columnBox.getSelectedItem());
		
		Class<?> clazz = tablePanel.getTable().getModel().getColumnClass(col);
		
		operatorModel.removeAllElements();
		
		if (Number.class.isAssignableFrom(clazz)){
			EnumSet<NamedRowFilter.Operator> set = EnumSet.allOf(NamedRowFilter.Operator.class);
			
			for (Enum<NamedRowFilter.Operator> e : set){
				operatorModel.addElement(e);
			}
		}
		else if (String.class.isAssignableFrom(clazz)){
			operatorModel.addElement(NamedRowFilter.Operator.EQUALS);
		}
	}
	
	protected void addFilter(){
		// TODO
	}
}
