package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.Icon;

public class SquareIcon implements Icon {
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
			g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND));
			g2d.drawLine(x, y, x + width - 1, y + height - 1);
		}
		g2d.setStroke(stroke);
		g2d.setColor(c);

	}
}