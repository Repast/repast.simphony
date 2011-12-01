/**
 * 
 */
package repast.simphony.chart2.wizard;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import repast.simphony.chart2.engine.TimeSeriesChartDescriptor;
import repast.simphony.data2.engine.CountSourceDefinition;
import repast.simphony.data2.engine.CustomDataSourceDefinition;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.MethodDataSourceDefinition;
import repast.simphony.data2.util.AggregateFilter;
import repast.simphony.data2.util.DataUtilities;
import simphony.util.messages.MessageCenter;

/**
 * TableModel for configuring series in a time series chart.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class SeriesConfigTableModel extends AbstractTableModel {

  private static MessageCenter msgCenter = MessageCenter
      .getMessageCenter(SeriesConfigTableModel.class);

  private static class RowItem {
    Boolean use;
    String id, label;
    Color color;

    /**
     * @param use
     * @param id
     * @param label
     * @param color
     */
    public RowItem(Boolean use, String id, String label, Color color) {
      this.use = use;
      this.id = id;
      this.label = label;
      this.color = color;
    }
  }

  private static final String[] COLUMN_NAMES = { " ", "ID", "Label", "Color" };
  private static final Class<?>[] COLUMN_CLASSES = { Boolean.class, String.class, String.class,
      Color.class };
  public static final int INCLUDE_COL = 0;

  private List<RowItem> items = new ArrayList<RowItem>();
  private AggregateFilter filter = new AggregateFilter();

  public SeriesConfigTableModel(TimeSeriesChartDescriptor descriptor, DataSetDescriptor data) {
    List<Color> colors = new ArrayList<Color>();
    colors.add(Color.BLUE);
    colors.add(Color.RED);
    colors.add(Color.GREEN);
    colors.add(Color.MAGENTA);
    colors.add(Color.ORANGE);
    colors.add(Color.CYAN);
    colors.add(Color.YELLOW);

    for (CountSourceDefinition def : data.countDataSources()) {
      items.add(new RowItem(false, def.getId(), def.getId(), getNextColor(colors)));
    }

    for (MethodDataSourceDefinition def : data.methodDataSources()) {
      try {
        if (filter.check(def)) {
          items.add(new RowItem(false, def.getId(), def.getId(), getNextColor(colors)));
        }
      } catch (Exception ex) {
        msgCenter.warn("Error while creating method data source", ex);
      }
    }

    for (CustomDataSourceDefinition def : data.customAggDataSources()) {
      try {
        if (filter.check(def)) {
          items.add(new RowItem(false, def.getId(), def.getId(), getNextColor(colors)));
        }
      } catch (Exception ex) {
        msgCenter.warn("Error while creating custom data source", ex);
      }
    }

    for (String id : descriptor.getSeriesIds()) {
      RowItem item = findItem(id);
      item.use = true;
      item.color = descriptor.getSeriesColor(id);
      item.label = descriptor.getSeriesLabel(id);
    }

    Collections.sort(items, new Comparator<RowItem>() {
      @Override
      public int compare(RowItem item1, RowItem item2) {
        return item1.id.compareTo(item2.id);
      }
    });
  }

  private RowItem findItem(String id) {
    for (RowItem item : items) {
      if (item.id.equals(id))
        return item;
    }
    return null;
  }

  private Color getNextColor(List<Color> colors) {
    if (colors.size() == 0)
      return Color.BLUE;
    else
      return colors.remove(0);
  }

  @Override
  public Class<?> getColumnClass(int col) {
    return COLUMN_CLASSES[col];
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    return col != 1;
  }

  @Override
  public int getColumnCount() {
    return COLUMN_NAMES.length;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.table.AbstractTableModel#getColumnName(int)
   */
  @Override
  public String getColumnName(int column) {
    return COLUMN_NAMES[column];
  }

  @Override
  public int getRowCount() {
    return items.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int,
   * int)
   */
  @Override
  public void setValueAt(Object val, int row, int col) {
    RowItem item = items.get(row);
    if (col == 0)
      item.use = (Boolean) val;
    else if (col == 2)
      item.label = val.toString();
    else
      item.color = (Color) val;

    fireTableRowsUpdated(row, row);
  }

  @Override
  public Object getValueAt(int row, int col) {
    RowItem item = items.get(row);
    if (col == 0)
      return item.use;
    else if (col == 1)
      return item.id;
    else if (col == 2)
      return item.label;
    else
      return item.color;
  }

  /**
   * Gets whether or not any series has been checked to included.
   * 
   * @return true if any have been checked, otherwise false.
   */
  public boolean anySeriesIncluded() {
    for (RowItem item : items) {
      if (item.use)
        return true;
    }
    return false;
  }

  public void apply(TimeSeriesChartDescriptor descriptor) {
    descriptor.clearSeriesIds();
    for (RowItem item : items) {
      if (item.use)
        descriptor.addSeriesId(item.id, item.label, item.color);
    }
  }
}
