/**
 * 
 */
package repast.simphony.chart2.wizard;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import repast.simphony.ui.plugin.editor.SquareIcon;

/**
 * TableCellEditor for colors.
 * 
 * @author from java tutorial.
 */
@SuppressWarnings("serial")
public class ColorEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

  Color currentColor;
  JLabel label = new JLabel();
  JColorChooser colorChooser;
  JDialog dialog;
  protected static final String EDIT = "edit";

  public ColorEditor() {

    // Set up the dialog that the button brings up.
    colorChooser = new JColorChooser();
    dialog = JColorChooser.createDialog(label, "Pick a Color", true, // modal
        colorChooser, this, // OK button handler
        null); // no CANCEL button handler
    label.setHorizontalAlignment(JLabel.CENTER);
  }
  
  public boolean isCellEditable(EventObject evt) {
    if (evt instanceof MouseEvent) {
      return ((MouseEvent)evt).getClickCount() >= 2;
    }
    return true;
  }

  public void actionPerformed(ActionEvent e) {
    currentColor = colorChooser.getColor();
  }

  // Implement the one CellEditor method that AbstractCellEditor doesn't.
  public Object getCellEditorValue() {
    return currentColor;
  }

  // Implement the one method defined by TableCellEditor.
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
      int row, int column) {
    currentColor = (Color) value;
    colorChooser.setColor(currentColor);
    dialog.setVisible(true);

    label.setIcon(new SquareIcon(10, 10, currentColor));
    return label;
  }

}
