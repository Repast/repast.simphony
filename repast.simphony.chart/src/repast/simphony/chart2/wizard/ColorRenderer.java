/**
 * 
 */
package repast.simphony.chart2.wizard;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import repast.simphony.ui.widget.SquareIcon;

/**
 * TableCell renderer for a Color
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class ColorRenderer extends DefaultTableCellRenderer {

  /* (non-Javadoc)
   * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
   */
  @Override
  public Component getTableCellRendererComponent(JTable arg0, Object obj, boolean arg2,
      boolean arg3, int arg4, int arg5) {
    
    JLabel label = (JLabel)super.getTableCellRendererComponent(arg0, obj, arg2, arg3, arg4, arg5);
    if (obj != null) {
      label.setText("");
      label.setIcon(new SquareIcon(10, 10, (Color)obj));
      label.setHorizontalAlignment(JLabel.CENTER);
    }
    
    return label; 
  }
}
