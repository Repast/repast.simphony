package repast.simphony.visualization.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import repast.simphony.ui.editor.ClassListItem;
import repast.simphony.util.collections.Pair;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public abstract class StyleTableModel extends AbstractTableModel {

	protected String[] COLUMN_NAMES;
	private static final Class[] COLUMN_CLASSES = {String.class, String.class};

	protected List<Pair<ClassListItem, ClassListItem>> items = new ArrayList<Pair<ClassListItem, ClassListItem>>();
	protected List<Pair<ClassListItem, ClassListItem>> oldItems = new ArrayList<Pair<ClassListItem, ClassListItem>>();

	public void addRow(ClassListItem className, ClassListItem styleName) {
		items.add(new Pair<ClassListItem, ClassListItem>(className, styleName));
		fireTableRowsInserted(items.size() - 1, items.size() - 1);
	}

	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	public Class<?> getColumnClass(int columnIndex) {
		return StyleTableModel.COLUMN_CLASSES[columnIndex];
	}

	public int getRowCount() {
		return items.size();
	}

	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Pair<ClassListItem, ClassListItem> pair = items.get(rowIndex);
		if (columnIndex == 0) return pair.getFirst();
		else return pair.getSecond();
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Pair<ClassListItem, ClassListItem> pair = items.get(rowIndex);
		if (aValue instanceof String) {
			aValue = new ClassListItem(aValue.toString());
		}
		if (columnIndex == 0) {
			pair.setFirst((ClassListItem) aValue);
		} else {
			pair.setSecond((ClassListItem) aValue);
		}
		fireTableRowsUpdated(rowIndex, rowIndex);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	public void deleteRow(int row) {
		items.remove(row);
		fireTableRowsDeleted(row, row);
	}

	public void swapItems() {
		List<Pair<ClassListItem, ClassListItem>> tmp = items;
		items = oldItems;
		oldItems = tmp;
		fireTableDataChanged();
	}
}
