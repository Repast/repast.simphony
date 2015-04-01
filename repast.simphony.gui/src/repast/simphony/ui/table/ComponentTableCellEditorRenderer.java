package repast.simphony.ui.table;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * TableCell editor and renderer for JComponents
 * 
 * @author Eric Tatara
 *
 */
public class ComponentTableCellEditorRenderer extends AbstractCellEditor 
implements TableCellEditor, TableCellRenderer {

	JComponent component;
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {

		component = (JComponent)value;
		return component;
	}

	@Override
	public Object getCellEditorValue() {
		return component;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		
		component = (JComponent)value;
		return component;
	}
}
