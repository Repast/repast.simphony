package repast.simphony.jung.tablemodels;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/*
 * @author Michael J. North
 *
 */
public abstract class DefaultTableModel implements TableModel {

	protected List<TableModelListener> listeners = new ArrayList<TableModelListener>();

	public void addTableModelListener(TableModelListener listener) {
		this.listeners.add(listener);
	}

	public Class<String> getColumnClass(int col) {
		return String.class;
	}

	public abstract int getColumnCount();

	public abstract int getRowCount();

	public abstract String getColumnName(int col);

	public abstract Object getValueAt(int row, int col);

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void setValueAt(Object value, int row, int col) {
	}

	public void removeTableModelListener(TableModelListener listener) {
		this.listeners.remove(listener);
	}

}
