package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;

/**
 * Tree cell renderer for CheckNodes. These nodes have a selected property that
 * is represented via a checkbox by the renderer.
 * 
 * Based on code from jguru (?)
 * 
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:24:11 $
 * @author Nick Collier
 */
public class CheckNodeRenderer extends JLabel implements TreeCellRenderer {

	protected TreeCheckBox box = new TreeCheckBox();

	protected TreeLabel label = new TreeLabel();

	protected TreeLabel rootLabel = new TreeLabel();

	protected Color defaultCheckBoxInterior;

	public CheckNodeRenderer() {
		setLayout(null);
		add(box);
		add(label);
		box.setBackground(UIManager.getColor("Tree.textBackground"));
		box.setBorderPaintedFlat(true);
		defaultCheckBoxInterior = UIManager
				.getColor("CheckBox.interiorBackground");
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		String str = tree.convertValueToText(value, selected, expanded, leaf,
				row, hasFocus);
		setEnabled(tree.isEnabled());
		if (value instanceof CheckNode) {
			CheckNode checkNode = ((CheckNode) value);
			box.setSelected(checkNode.isSelected());
			box.setInteriorBackground(checkNode.getTreeState().getColor());
		}
		label.setFont(tree.getFont());
		label.setText(str);
		label.setSelected(selected);
		label.setHasFocus(hasFocus);
		if (leaf && value instanceof CheckNode)
			label.setIcon(((CheckNode) value).getIcon());
		else {
			rootLabel.setText(str);
			rootLabel.setFont(tree.getFont());
			rootLabel.setSelected(selected);
			rootLabel.setHasFocus(hasFocus);
			rootLabel.setIcon(UIManager.getIcon("Tree.closedIcon"));
			return rootLabel;
		}

		return this;
	}

	@Override
	public void doLayout() {
		Dimension dBox = box.getPreferredSize();
		Dimension dLabel = label.getPreferredSize();

		int yBox = 0;
		int yLabel = 0;

		if (dBox.height < dLabel.height)
			yBox = (dLabel.height - dBox.height) / 2;
		else
			yLabel = (dBox.height - dLabel.height) / 2;

		box.setLocation(0, yBox);
		box.setBounds(0, yBox, dBox.width, dBox.height);
		label.setLocation(dBox.width, yLabel);
		label.setBounds(dBox.width, yLabel, dLabel.width, dLabel.height);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dBox = box.getPreferredSize();
		Dimension dLabel = label.getPreferredSize();
		return new Dimension(dBox.width + dLabel.width, Math.max(dBox.height,
				dLabel.height));
	}

	@Override
	public void setBackground(Color color) {
		if (color instanceof ColorUIResource)
			color = null;
		super.setBackground(color);
	}

	static class TreeCheckBox extends JCheckBox {

		private Color interiorBackground;

		private Color defaultInteriorBackground;

		public TreeCheckBox() {
			defaultInteriorBackground = UIManager
					.getColor("CheckBox.interiorBackground");
		}

		public void setInteriorBackground(Color color) {
			interiorBackground = color;
		}

		@Override
		public void paint(Graphics g) {
			UIManager.put("CheckBox.interiorBackground", interiorBackground);
			super.paint(g);
			UIManager.put("CheckBox.interiorBackground",
					defaultInteriorBackground);
		}
	}

	static class TreeLabel extends JLabel {
		boolean isSelected, hasFocus;

		public TreeLabel() {
		}

		@Override
		public void setBackground(Color color) {
			if (color instanceof ColorUIResource)
				color = null;
			super.setBackground(color);
		}

		@Override
		public void paint(Graphics g) {
			String str = getText();
			if (str != null) {
				if (str.length() > 0) {
					if (isSelected) {
						g.setColor(UIManager
								.getColor("Tree.selectionBackground"));
						setForeground(Color.WHITE);
					} else {
						g.setColor(UIManager.getColor("Tree.textBackground"));
						setForeground(Color.BLACK);
					}

					Dimension d = getPreferredSize();
					int imageOffset = 0;
					Icon icon = getIcon();
					if (icon != null) {
						imageOffset = icon.getIconWidth()
								+ Math.max(0, getIconTextGap() - 1);
					}
					g.fillRect(imageOffset, 0, d.width - 1 - imageOffset,
							d.height);
					if (hasFocus) {
						g.setColor(UIManager
								.getColor("Tree.selectionBorderColor"));
						g.drawRect(imageOffset, 0, d.width - 1 - imageOffset,
								d.height - 1);
					}
				}
			}
			super.paint(g);
		}

		@Override
		public Dimension getPreferredSize() {
			Dimension d = super.getPreferredSize();
			if (d != null) {
				d = new Dimension(d.width + 3, d.height);
			}

			return d;
		}

		public void setHasFocus(boolean hasFocus) {
			this.hasFocus = hasFocus;
		}

		public void setSelected(boolean selected) {
			isSelected = selected;
		}
	}
}
