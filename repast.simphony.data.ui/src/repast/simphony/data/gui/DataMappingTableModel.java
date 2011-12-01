/*CopyrightHere*/
package repast.simphony.data.gui;

import repast.simphony.data.logging.gather.TimeDataMapping;
import repast.simphony.util.collections.Pair;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick Collier
 */
public class DataMappingTableModel extends AbstractTableModel {
  private static final long serialVersionUID = -4328607927468260943L;

  private static final String[] COLUMN_NAMES = {"Column Name", "Source"};
  private static final Class[] COLUMN_CLASSES = {String.class, MappingSourceRepresentation.class};

  private List<Pair<String, MappingSourceRepresentation>> items = new ArrayList<Pair<String, MappingSourceRepresentation>>();

  public void addMapping(String columnName, MappingSourceRepresentation rep) {
    if (rep instanceof TimeSourceRepresentation) columnName = TimeDataMapping.TICK_COLUMN;
    items.add(new Pair<String, MappingSourceRepresentation>(columnName, rep));
    fireTableRowsInserted(items.size() - 1, items.size() - 1);
  }

  public void clearMappings() {
    int initialSize = items.size() - 1;
    items.clear();
    if (initialSize > 0)
      fireTableRowsDeleted(0, initialSize);
  }

  public String getColumnName(int column) {
    return DataMappingTableModel.COLUMN_NAMES[column];
  }

  public Class<?> getColumnClass(int columnIndex) {
    return DataMappingTableModel.COLUMN_CLASSES[columnIndex];
  }

  public int getRowCount() {
    return items.size();
  }

  public int getColumnCount() {
    return DataMappingTableModel.COLUMN_NAMES.length;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    Pair<String, MappingSourceRepresentation> pair = items.get(rowIndex);
    if (columnIndex == 0) return pair.getFirst();
    else if (columnIndex == 1) return pair.getSecond();
    else return true;
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    Pair<String, MappingSourceRepresentation> pair = items.get(rowIndex);
    if (columnIndex == 0) {
      if (aValue.toString().trim().length() == 0) return;
      pair.setFirst(aValue.toString());
    } else {
      if (aValue instanceof TimeSourceRepresentation) pair.setFirst(TimeDataMapping.TICK_COLUMN);
      pair.setSecond((MappingSourceRepresentation) aValue);
    }
    fireTableRowsUpdated(rowIndex, rowIndex);
  }


  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    Object value = getValueAt(rowIndex, columnIndex);
    if (value instanceof MappingSourceRepresentation) {
      MappingSourceRepresentation rep = (MappingSourceRepresentation) value;
      return rep.isMappingEditable();
    } else {
      if (columnIndex == 0) {
        Pair<String, MappingSourceRepresentation> pair = items.get(rowIndex);
        return !(pair.getSecond() instanceof TimeSourceRepresentation);
      }
      return true;
    }
  }

  public void deleteRow(int row) {
    items.remove(row);
    fireTableRowsDeleted(row, row);
  }

  public Iterable<Pair<String, MappingSourceRepresentation>> rows() {
    return items;
  }
}
