package repast.simphony.relogo.factories;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JToggleButton;

public class ToggleButtonIcon implements Icon {

	private static final int SIZE = 10;

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
		if (c instanceof JToggleButton && ((JToggleButton) c).isSelected()) {
			g.setColor(new Color(26,123,202));
		} else {
			g.setColor(Color.GRAY.brighter().brighter());
		}

		g.fillOval(x, y+2, SIZE, SIZE-4);
		g.setColor(Color.GRAY);
		g.drawOval(x, y+2, SIZE, SIZE-4);

	}

}
