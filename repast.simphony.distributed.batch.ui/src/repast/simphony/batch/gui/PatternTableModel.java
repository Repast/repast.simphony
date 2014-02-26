package repast.simphony.batch.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import repast.simphony.batch.ssh.OutputPattern;

/**
 * TableModel for editing custom file patterns.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class PatternTableModel extends AbstractTableModel {

  private static final Class<?>[] CLASSES = { String.class, String.class, Boolean.class,
      Boolean.class };
  private static final String[] NAMES = { "Pattern", "Local Path", "Aggregate", "Has Header" };

  private List<OutputPattern> patterns = new ArrayList<>();

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.table.AbstractTableModel#getColumnName(int)
   */
  @Override
  public String getColumnName(int column) {
    return NAMES[column];
  }
  
  public void addOutputPattern(OutputPattern pattern) {
    patterns.add(pattern);
    fireTableRowsInserted(patterns.size() - 1, patterns.size() - 1);
  }
  
  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
   */
  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    if (columnIndex == 3) {
      return patterns.get(rowIndex).isConcatenate();
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
   */
  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return CLASSES[columnIndex];
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.table.TableModel#getRowCount()
   */
  @Override
  public int getRowCount() {
    return patterns.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.table.TableModel#getColumnCount()
   */
  @Override
  public int getColumnCount() {
    return NAMES.length;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.table.TableModel#getValueAt(int, int)
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    OutputPattern pattern = patterns.get(rowIndex);
    switch (columnIndex) {
    case 0:
      return pattern.getPattern();
    case 1:
      return pattern.getPath();
    case 2:
      return pattern.isConcatenate();
    case 3:
      return pattern.isHeader();
    default:
      throw new IllegalArgumentException("Illegal column index " + columnIndex);
    }
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
   */
  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    OutputPattern pattern = patterns.get(rowIndex);
    switch (columnIndex) {
    case 0:
      pattern.setPattern(aValue.toString());
      fireTableCellUpdated(rowIndex, columnIndex);
      break;
    case 1:
      pattern.setPath(aValue.toString());
      fireTableCellUpdated(rowIndex, columnIndex);
      break;
    case 2:
      pattern.setConcatenate((Boolean)aValue);
      if (!pattern.isConcatenate()) pattern.setHeader(false);
      fireTableCellUpdated(rowIndex, columnIndex);
      break;
    case 3:
      pattern.setHeader((Boolean)aValue);
      fireTableCellUpdated(rowIndex, columnIndex);
      break;
    default:
      throw new IllegalArgumentException("Illegal column index " + columnIndex);
    }
  }
  
  public List<OutputPattern> getOutputPatterns() {
    return patterns;
  }

  public void setOutputPatterns(List<OutputPattern> outputPatterns) {
    patterns.clear();
    patterns.addAll(outputPatterns);
    fireTableDataChanged();
  }

  public void deleteRow(int row) {
    patterns.remove(row);
    fireTableRowsDeleted(row, row);
  }
}
