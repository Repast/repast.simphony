package repast.simphony.data2.wizard;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.MethodDataSourceDefinition;

@SuppressWarnings("serial")
public class SimpleMethodTableModel extends AbstractTableModel {
  
  private static final String[] COLUMN_NAMES = {"Data Source Name", "Method"};
  private static final Class<?>[] COLUMN_CLASSES = {String.class, MethodDataSourceDefinition.class};
  
  private List<MethodDataSourceDefinition> items = new ArrayList<MethodDataSourceDefinition>();
  
  public SimpleMethodTableModel(Iterable<MethodDataSourceDefinition> defs) {
    for (MethodDataSourceDefinition def : defs) {
      items.add(def);
    }
  }
  
  public String getColumnName(int column) {
    return COLUMN_NAMES[column];
  }

  public Class<?> getColumnClass(int columnIndex) {
    return COLUMN_CLASSES[columnIndex];
  }

  @Override
  public int getColumnCount() {
    return COLUMN_NAMES.length;
  }

  @Override
  public int getRowCount() {
    return items.size();
  }

  @Override
  public Object getValueAt(int row, int col) {
    MethodDataSourceDefinition def = items.get(row);
    if (col == 0) return def.getId();
    return def.getMethodName();
  }
  
  public void addMethodDataSource(MethodDataSourceDefinition def) {
    items.add(new MethodDataSourceDefinition(def));
    fireTableDataChanged();
  }
  
  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return true;
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    MethodDataSourceDefinition def = items.get(rowIndex);
    if (columnIndex == 0) def.setId(aValue.toString());
    else if (columnIndex == 1) {
      MethodDataSourceDefinition other = (MethodDataSourceDefinition)aValue;
      def.setMethodName(other.getMethodName());
      def.setId(other.getId());
    }
    fireTableDataChanged();
  }

  public void removeItem(int row) {
    if (row != -1) {
      items.remove(row);
      fireTableDataChanged();
    }
  }

  public void apply(DataSetDescriptor descriptor) {
    descriptor.clearMethodDataSources();
    for (MethodDataSourceDefinition def : items) {
      descriptor.addMethodDataSource(def.getId(), def.getObjTargetClass(), def.getMethodName());
    }
  }
}
