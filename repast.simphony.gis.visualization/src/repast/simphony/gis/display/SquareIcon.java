package repast.simphony.gis.display;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * A square-shaped icon for use on buttons like color pickers.
 * 
 *@deprecated 2D piccolo based code is being removed
 */
public class SquareIcon implements Icon {

	private Color color;
	private int dimension;

	public SquareIcon(Color color) {
		this(color, 10);
	}
	public SquareIcon(Color color, int dimension) {
		this.dimension = dimension;
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public int getIconHeight() {
		return dimension;
	}

	public int getIconWidth() {
		return dimension;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Color oldColor = g.getColor();
		g.setColor(color);
		g.fillRect(x, y, getIconWidth(), getIconWidth());
		g.setColor(Color.BLACK);
		g.drawRect(x, y, getIconWidth(), getIconHeight());
		g.setColor(oldColor);
	}
}