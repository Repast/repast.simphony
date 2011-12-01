package repast.simphony.relogo.factories;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class RegularButtonIcon implements Icon {

	private static final int SIZE = 1;

	@Override
	public int getIconHeight() {
		return SIZE;
	}

	@Override
	public int getIconWidth() {
		return SIZE;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		

	}

}
