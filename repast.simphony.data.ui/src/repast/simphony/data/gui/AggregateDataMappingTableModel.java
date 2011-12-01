/*CopyrightHere*/
package repast.simphony.data.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import repast.simphony.data.logging.gather.AggregateDataMapping;
import repast.simphony.data.logging.gather.TimeDataMapping;
import repast.simphony.util.collections.Pair;

/**
 * @author Nick Collier
 */
public class AggregateDataMappingTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -4328607927468260943L;
	
	private static final Class[] COLUMN_CLASSES = {String.class, MappingSourceRepresentation.class};

	private List<Pair<String, AggregateMappingSourceRepresentation>> items = new ArrayList<Pair<String, AggregateMappingSourceRepresentation>>();

	private String sourceColTitle;

	private String colNameTitle;

	public AggregateDataMappingTableModel(String colNameTitle, String sourceColTitle) {
		this.colNameTitle = colNameTitle;
		this.sourceColTitle = sourceColTitle;
	}
	
	public void addMapping(String name, AggregateMappingSourceRepresentation rep) {
		if (rep instanceof TimeSourceRepresentation) name = TimeDataMapping.TICK_COLUMN;
		items.add(new Pair<String, AggregateMappingSourceRepresentation>(name, rep));
		fireTableRowsInserted(items.size() - 1, items.size() - 1);
	}

	public void clearMappings() {
		int initialSize = items.size() - 1;
		items.clear();
		if (initialSize > 0)
			fireTableRowsDeleted(0, initialSize);
	}
	
	public String getColumnName(int column) {
		if (column == 0) {
			return colNameTitle;
		}
		return sourceColTitle;
	}

	public Class<?> getColumnClass(int columnIndex) {
		return AggregateDataMappingTableModel.COLUMN_CLASSES[columnIndex];
	}

	public int getRowCount() {
		return items.size();
	}

	public int getColumnCount() {
		return AggregateDataMappingTableModel.COLUMN_CLASSES.length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Pair<String, AggregateMappingSourceRepresentation> pair = items.get(rowIndex);
		if (columnIndex == 0) return pair.getFirst();
		else return pair.getSecond();
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Pair<String, AggregateMappingSourceRepresentation> pair = items.get(rowIndex);
		if (columnIndex == 0) {
			if (aValue.toString().trim().length() == 0) return;
			pair.setFirst(aValue.toString());
		} else {
			if (aValue instanceof TimeSourceRepresentation) pair.setFirst(TimeDataMapping.TICK_COLUMN);
			pair.setSecond((AggregateMappingSourceRepresentation) aValue);
		}
		fireTableRowsUpdated(rowIndex, rowIndex);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			Pair<String, AggregateMappingSourceRepresentation> pair = items.get(rowIndex);
			return !(pair.getSecond() instanceof TimeSourceRepresentation);
		}
		return true;
	}

	public void deleteRow(int row) {
		items.remove(row);
		fireTableRowsDeleted(row, row);
	}

	public Map<String, AggregateDataMapping> createMappingsMap() {
		HashMap<String, AggregateDataMapping> mappingsMap = new HashMap<String, AggregateDataMapping>();
		for (Pair<String, AggregateMappingSourceRepresentation> pair : items) {
			mappingsMap.put(pair.getFirst(), pair.getSecond().createMapping());
		}
		return mappingsMap;
	}
}
