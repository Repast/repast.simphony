package repast.simphony.ui.table;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
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
		this(null);
	}
	
	public TablePanel(TableModel model){
		super(new BorderLayout());
		setOpaque(true);

		if (model == null)
			model = new DefaultTableModel();

		table = new JTable(model);

		// Set Table behaviors and styles
		
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
	
	
}