/*
 * Created by JFormDesigner on Thu May 25 11:34:35 CDT 2006
 */

package repast.simphony.gis.id;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 * @author User #1
 */
public class IdTableDialog extends JDialog {
	private FeatureTableModel idModel;

	public IdTableDialog(Frame owner, FeatureTableModel idModel) {
		super(owner);
		setTitle(idModel.getFeatureType().getTypeName());
		this.idModel = idModel;
		initComponents();
		table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < idModel.getColumnCount(); i++) {
			String title = idModel.getColumnName(i);
			TableColumn column = table1.getColumnModel().getColumn(i);
			column = table1.getColumnModel().getColumn(i);
			Component comp = table1.getDefaultRenderer(
					idModel.getColumnClass(i)).getTableCellRendererComponent(
					table1, title, false, false, 0, i);
			int cellWidth = comp.getPreferredSize().width + 5;
			column.setPreferredWidth(cellWidth);
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		panel1 = new JPanel();
		scrollPane1 = new JScrollPane();
		table1 = new JTable(idModel);

		// ======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// ======== panel1 ========
		{
			panel1.setLayout(new BorderLayout());

			// ======== scrollPane1 ========
			{
				scrollPane1.setViewportView(table1);
			}
			panel1.add(scrollPane1, BorderLayout.CENTER);
		}
		contentPane.add(panel1, BorderLayout.CENTER);
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel panel1;

	private JScrollPane scrollPane1;

	private JTable table1;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
