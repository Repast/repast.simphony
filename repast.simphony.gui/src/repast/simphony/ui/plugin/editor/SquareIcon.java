package repast.simphony.ui.plugin.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * Draws a square colored icon.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class SquareIcon implements Icon {

	private int width, height;
	private Color color;

	public SquareIcon(int width, int height, Color color) {
		this.width = width;
		this.height = height;
		this.color = color;
	}

	/**
	 * Draw the icon at the specified location.  Icon implementations
	 * may use the Component argument to get properties useful for
	 * painting, e.g. the foreground or background color.
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Color curColor = g.getColor();
		g.setColor(color);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
		g.setColor(curColor);
	}

	/**
	 * Returns the icon's width.
	 *
	 * @return an int specifying the fixed width of the icon.
	 */
	public int getIconWidth() {
		return width;
	}

	/**
	 * Returns the icon's height.
	 *
	 * @return an int specifying the fixed height of the icon.
	 */
	public int getIconHeight() {
		return height;
	}
	
	/**
	 * Gets the color of this Icon.
	 * @return
	 */
	public Color getColor() {
	  return color;
	}
}
