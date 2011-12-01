package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class LegendLabel {
	String displayName;

	Color color;

	Rectangle2D rect;

	AgentLayer layer;

	SquareIcon icon;

	JLabel label;

	public LegendLabel(String displayName, Color color, AgentLayer layer) {
		this.displayName = displayName;
		this.color = color;
		this.layer = layer;
		rect = new Rectangle2D.Float(10, 10, 20, 20);
		label = new JLabel();
		label.setText(displayName);
		icon = new SquareIcon(color);
		label.setIcon(icon);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				toggle();
			}
		});
		label.setAlignmentX(0.0f);
	}

	public void toggle() {
		icon.toggle();
		label.repaint();
		layer.setVisible(!layer.getVisible());
		layer.repaint();
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color);
		g2d.fill(rect);
		g2d.setColor(Color.BLACK);
		g2d.drawString(displayName, 40, 10);
	}

	public JComponent getComponent() {
		return label;
	}

	class SquareIcon implements Icon {
		private Color color;

		boolean xed = false;

		private int width = 10;

		private int height = 10;

		public SquareIcon(Color color) {
			this.color = color;
		}

		public void toggle() {
			xed = !xed;
		}

		public int getIconWidth() {
			return width;
		}

		public int getIconHeight() {
			return height;
		}

		public void paintIcon(Component comp, Graphics g, int x, int y) {
			Graphics2D g2d = (Graphics2D) g;
			Color c = g2d.getColor();
			Stroke stroke = g2d.getStroke();
			g2d.setColor(color);
			g2d.fillRect(x, y, width, width);
			if (xed) {
				g2d.setColor(Color.BLACK);
				g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2d.drawLine(x, y, x + width - 1, y + height - 1);
			}
			g2d.setStroke(stroke);
			g2d.setColor(c);

		}
	}
}
