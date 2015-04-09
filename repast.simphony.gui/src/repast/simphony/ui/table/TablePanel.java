package repast.simphony.ui.table;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

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
	
	public TablePanel(){
		this(null, null);
	}
	
	public TablePanel(TableModel model, String tableName){
		super(new BorderLayout());
		setName(tableName);
		setOpaque(true);

		if (model == null)
			model = new DefaultTableModel();

		table = new JTable(model);

		// Set Table behaviors and styles
		
		table.setDefaultRenderer(JComponent.class, new ComponentTableCellEditorRenderer());
		table.setDefaultEditor(JComponent.class, new ComponentTableCellEditorRenderer());
		table.setCellSelectionEnabled(true);
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(true);
		table.setAutoCreateRowSorter(true);
//		table.setRowSorter(new TableSorter<TableModel>());
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
}