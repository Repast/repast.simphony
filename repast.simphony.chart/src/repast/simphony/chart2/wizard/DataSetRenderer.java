package repast.simphony.chart2.wizard;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import repast.simphony.data2.engine.DataSetDescriptor;

/**
 * ListCellRenderer that renders a DataSet by its name.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class DataSetRenderer extends DefaultListCellRenderer {
  @Override
  public Component getListCellRendererComponent(JList list, Object value, int index,
      boolean isSelected, boolean cellHasFocus) {
    
    if (value != null) {
      value = ((DataSetDescriptor)value).getName();
    }
    return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
  }
  
}
