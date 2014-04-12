package repast.simphony.gis.styleEditor;

import repast.simphony.gis.display.SquareIcon;

import javax.swing.*;
import java.awt.*;

/**
 * @author Nick Collier
 * 
 */
public class PaletteIcon implements Icon {

	public static enum Orientation {
		HORIZONTAL, VERTICAL
	}

	private SquareIcon[] icons;
	private Orientation orientation;
	private int width, height;
	private int iconSize;

	public PaletteIcon(Palette palette, Orientation orientation) {
		Color[] colors = palette.getColors();
		icons = new SquareIcon[colors.length];
		for (int i = 0; i < colors.length; i++) {
			icons[i] = new SquareIcon(colors[i], 12);
		}
		init(icons, orientation);
	}

	public PaletteIcon(SquareIcon[] icons, Orientation orientation) {
		init(icons, orientation);
	}

	public Color getColorAt(int x, int y) {
		int index = getColorIndexAt(x, y);
		if (index != -1) return icons[index].getColor();
		return null;
	}

	public int getColorIndexAt(int x, int y) {
		int index = 0;
		if (orientation == Orientation.HORIZONTAL) index = x / iconSize;
		else index = y / iconSize;
		if (index > -1 && index < icons.length) return index;
		else return -1;
	}

	private void init(SquareIcon[] icons, Orientation orientation) {
		this.icons = icons;
		this.orientation = orientation;
		iconSize = icons[0].getIconWidth();
		if (orientation == Orientation.HORIZONTAL) {

			width = icons.length * icons[0].getIconWidth();
			height = icons[0].getIconHeight();
		} else {
			height = icons.length * icons[0].getIconHeight();
			width = icons[0].getIconWidth();
		}
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
	 * Returns the icon's width.
	 *
	 * @return an int specifying the fixed width of the icon.
	 */
	public int getIconWidth() {
		return width;
	}

	/**
	 * Draw the icon at the specified location.  Icon implementations
	 * may use the Component argument to get properties useful for
	 * painting, e.g. the foreground or background color.
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (orientation == Orientation.HORIZONTAL) {
			for (Icon icon : icons) {
				icon.paintIcon(c, g, x, y);
				x += icon.getIconWidth();
			}
		} else {
			for (Icon icon : icons) {
				icon.paintIcon(c, g, x, y);
				y += icon.getIconHeight();
			}
		}
	}
}
