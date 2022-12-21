package repast.simphony.gis.legend;

import java.awt.Color;

import javax.swing.BorderFactory;


/**
 * Renderer for a LegendEntry. This by the default renderer used to draw
 * LegendEntries. This displays the checkbox and the name of the legend entry.
 *
 * @author $Author: howe $
 * @version $Revision: 1.4 $
 * @date Oct 24, 2006
 * 
 * @deprecated 2D piccolo based code is being removed
 */
public class LegendDefaultCellRenderer extends javax.swing.JPanel implements
		javax.swing.tree.TreeCellRenderer {
	private static final long serialVersionUID = -5052912655237566673L;

	/** if the note is a layer */
	private javax.swing.JCheckBox legendNoteCheckBox;

	private javax.swing.JLabel treeNoteIconJLabel;

	/**
	 * Creates a new instance of LegendTreeCellRender
	 */
	public LegendDefaultCellRenderer() {
		super();
		initComponents();
	}

	private void initComponents() {
		treeNoteIconJLabel = new javax.swing.JLabel();
		legendNoteCheckBox = new javax.swing.JCheckBox();
		legendNoteCheckBox.setBorder(BorderFactory.createEmptyBorder());

		setLayout(new java.awt.BorderLayout());

		treeNoteIconJLabel.setBackground(new java.awt.Color(255, 255, 255));
		treeNoteIconJLabel.setForeground(new java.awt.Color(255, 255, 255));
		treeNoteIconJLabel
				.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		add(treeNoteIconJLabel, java.awt.BorderLayout.WEST);

		legendNoteCheckBox.setBackground(new java.awt.Color(255, 255, 255));
		legendNoteCheckBox.setSelected(true);
		add(legendNoteCheckBox, java.awt.BorderLayout.CENTER);
	}

	private void setLayerSelected(boolean sel) {
		this.legendNoteCheckBox.setSelected(sel);
	}

	private void setText(String name) {
		remove(legendNoteCheckBox);
		legendNoteCheckBox.setText(name);
		add(legendNoteCheckBox, java.awt.BorderLayout.CENTER);
	}

	public java.awt.Component getTreeCellRendererComponent(
			javax.swing.JTree tree, Object value, boolean selected,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		LegendEntry userObject = (LegendEntry) value;

		setText(userObject.toString());

		setLayerSelected(userObject.isDataVisible());

		setBackground(userObject.getBackground(selected));
		treeNoteIconJLabel.setIcon(userObject.getIcon(expanded));
		Color c;
		if (selected) {
			c = new Color(204, 204, 255);
		} else {
			c = new Color(255, 255, 255);
		}
		legendNoteCheckBox.setBackground(c);

		return this;
	}
}
