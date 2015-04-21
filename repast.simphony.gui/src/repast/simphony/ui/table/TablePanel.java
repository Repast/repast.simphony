package repast.simphony.ui.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import repast.simphony.ui.table.NamedRowFilter.Operator;

/**
 * Basic table panel with some customization over standard JTable. 
 * 
 * @author Eric Tatara
 *
 * TODO filtering
 */
public class TablePanel extends JPanel {

	protected JTable table; 
	protected JScrollPane scrollPane;
	protected JToolBar toolbar;
	protected Set<RowFilter<Object,Object>> rowFilterSet;
	
	public TablePanel(){
		this(null, null);
	}
	
	public TablePanel(TableModel model, String tableName){
		super(new BorderLayout());
		setName(tableName);
		setOpaque(true);
		rowFilterSet = new HashSet<RowFilter<Object,Object>>();
		
		if (model == null)
			model = new DefaultTableModel();

		table = new JTable(model);

		// Set Table behaviors and styles
		
		table.setDefaultRenderer(JComponent.class, new ComponentTableCellEditorRenderer());
		table.setDefaultEditor(JComponent.class, new ComponentTableCellEditorRenderer());
		table.setDefaultRenderer(Double.class, new DoubleTableCellRenderer(5));
		table.setCellSelectionEnabled(true);
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(true);
		table.setAutoCreateRowSorter(true);
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void addToolBar(JToolBar toolbar){
		this.toolbar = toolbar;
		add(toolbar, BorderLayout.NORTH);
	}
	
	public JToolBar getToolBar(){
		return toolbar;
	}
	
	/**
	 * Adds numbered row labels.  Should be called after table is populated.
	 * 
	 * TODO add a listener that updates for row add / remove events.
	 */
	public void insertRowLabels(){
		JTable rowTable = new RowTable(table);
		scrollPane.setRowHeaderView(rowTable);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,rowTable.getTableHeader());
	}

	public JTable getTable() {
		return table;
	}
	
	/**
	 * Automatically resizes column widths to fit header and contents
	 */
	public void autoResizeColumnWidth() {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 50; // Min width
			
			// Check header size by using a mock jbutton
			JButton button = new JButton(table.getColumnName(column));
			width = button.getPreferredSize().width;
			
			// Check each row contents and use the largest found width for the column
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width, width);
			}
			int pad = 5;  // sometimes the found size is just a hair too narrow
			
			columnModel.getColumn(column).setPreferredWidth(width+pad);
		}
	}
	
	public void clearRowFilters(){
		rowFilterSet.clear();
		setRowFilters(rowFilterSet);
	}
	
	protected void addRowFilter(RowFilter<Object,Object> filter){
		rowFilterSet.add(filter);
		setRowFilters(rowFilterSet);
	}
	
	protected void setRowFilters(Set<RowFilter<Object,Object>> filterList){
		RowFilter<Object,Object> filter = RowFilter.andFilter(filterList);
		
		((TableRowSorter)table.getRowSorter()).setRowFilter(filter);
		
		rowFilterSet.clear();
		rowFilterSet.addAll(filterList);
	}

	public Set<RowFilter<Object, Object>> getRowFilterList() {
		return rowFilterSet;
	}
	
}