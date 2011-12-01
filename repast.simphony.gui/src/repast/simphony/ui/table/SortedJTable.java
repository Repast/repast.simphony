package repast.simphony.ui.table;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/*
 * @author Michael J. North
 *
 */
public class SortedJTable extends JTable {

	public SortedJTable() {
		super();
		init();
	}

	public SortedJTable(int numRows, int numColumns) {
		super(numRows, numColumns);
		init();
	}

	public SortedJTable(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
		init();
	}

	public SortedJTable(TableModel dm, TableColumnModel cm,
			ListSelectionModel sm) {
		super(dm, cm, sm);
		init();
	}

	public SortedJTable(TableModel dm, TableColumnModel cm) {
		super(dm, cm);
		init();
	}

	public SortedJTable(TableModel dm) {
		super(dm);
		init();
	}

	public SortedJTable(Vector rowData, Vector columnNames) {
		super(rowData, columnNames);
		init();
	}

	public void init() {
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		((DefaultTableCellRenderer) this.getTableHeader().getDefaultRenderer())
				.setHorizontalAlignment(SwingConstants.CENTER);
		this.setRowSelectionAllowed(true);
		this.setColumnSelectionAllowed(true);
		this.setCellSelectionEnabled(true);
	}

	@Override
	public void setModel(TableModel newModel) {
		super.setModel(new TableSorter(newModel, this.getTableHeader()));
	}

}
