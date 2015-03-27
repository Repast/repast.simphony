package repast.simphony.ui.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * Auxiliary table that only provides numbered row headers for another main table.
 * 
 * @author Eric Tatara
 *
 */
public class RowTable extends JTable{

	protected JTable table;

	public RowTable(JTable mainTable){
		this.table = mainTable;

		DefaultTableModel model = new DefaultTableModel();
		setModel(model);
		model.setColumnCount(1);
		model.setColumnIdentifiers(new String[]{""});  // blank
		model.setRowCount(table.getRowCount());

		for (int i = 0; i < table.getRowCount(); i++) {
			setValueAt((i + 1), i, 0);
		}
		setShowGrid(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setPreferredScrollableViewportSize(new Dimension(50, 0));
		getColumnModel().getColumn(0).setPreferredWidth(50);
		getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable x, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				boolean selected = table.getSelectionModel().isSelectedIndex(row);
				Component component = table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, value, false, false, -1, -2);
				((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
				//		if (selected) {
				//		  component.setFont(component.getFont().deriveFont(Font.BOLD));
				//		  component.setForeground(Color.red);
				//		} else {
				component.setFont(component.getFont().deriveFont(Font.PLAIN));
				//		}
				return component;
			}
		});
	}
}