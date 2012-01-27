package repast.simphony.data2.wizard;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import repast.simphony.data2.AggregateOp;
import repast.simphony.data2.engine.CountSourceDefinition;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.MethodDataSourceDefinition;

@SuppressWarnings("serial")
public class AggMethodTableModel extends AbstractTableModel {
  
  static final int SOURCE_TYPE_COL = 1;
  static final int METHOD_COL = 2;
  static final int OP_COL = 3;
  
  static final String NO_METHOD_VAL = " N/A ";

  
  private static final String[] COLUMN_NAMES = {"Source Name", "Agent Type", "Method", "Aggregate Operation"};
  private static final Class<?>[] COLUMN_CLASSES = {String.class, String.class, String.class, AggregateOp.class};
  
  private List<MethodDataSourceDefinition> items = new ArrayList<MethodDataSourceDefinition>();
  
  public AggMethodTableModel(DataSetDescriptor descriptor) {
    for (MethodDataSourceDefinition def : descriptor.methodDataSources()) {
      items.add(def);
    }
    
    for (CountSourceDefinition def : descriptor.countDataSources()) {
      MethodDataSourceDefinition mds = new MethodDataSourceDefinition(def.getId(), def.getTypeName(), NO_METHOD_VAL);
      mds.setAggregateOp(AggregateOp.COUNT);
      items.add(mds);
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
    else if (col == 1) return def.getObjTargetClass();
    else if (col == 2) return def.getMethodName();
    else if (col == 3) return def.getAggregateOp();
    return null;
  }
  
  public void addMethodDataSource(MethodDataSourceDefinition def) {
    items.add(new MethodDataSourceDefinition(def));
    fireTableRowsInserted(items.size() - 1, items.size() - 1);
  }
  
  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return true;
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int col) {
    MethodDataSourceDefinition def = items.get(rowIndex);
    boolean changed = false;
    if (col == 0 && !def.getId().equals(aValue.toString())) {
      changed = true;
      def.setId(aValue.toString());
      
    } else if (col == 1 && !def.getObjTargetClass().equals(((Class<?>)aValue).getName())) {
      changed = true;
      def.setObjTargetClass(((Class<?>)aValue).getName());
      
    } else if (col == 2 && !def.getMethodName().equals(aValue.toString())) {
      changed = true;
      def.setMethodName(aValue.toString());
      
    } else if (col == 3 && !def.getAggregateOp().equals((AggregateOp)aValue)) {
      changed = true;
      def.setAggregateOp((AggregateOp)aValue);
    }
    
    if (changed) fireTableCellUpdated(rowIndex, col);
  }

  public void removeItem(int row) {
    if (row != -1) {
      items.remove(row);
      fireTableRowsDeleted(row, row);
    }
  }

  public void apply(DataSetDescriptor descriptor) {
    descriptor.clearMethodDataSources();
    descriptor.clearCountDataSources();
    for (MethodDataSourceDefinition def : items) {
      if (def.getId().trim().length() == 0) throw new IllegalArgumentException("Missing data source name. Each data source must be named.");
      if (def.getAggregateOp() == AggregateOp.COUNT) {
        descriptor.addCountDataSource(def.getId(), def.getObjTargetClass());
      } else {
        descriptor.addAggregateMethodDataSource(def.getId(), def.getObjTargetClass(), def.getMethodName(),
            def.getAggregateOp());
      }
    }
  }
}
