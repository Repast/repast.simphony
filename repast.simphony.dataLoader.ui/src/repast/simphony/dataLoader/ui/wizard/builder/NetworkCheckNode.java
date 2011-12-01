package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.Icon;

public class NetworkCheckNode extends CheckNode {

	NetworkLayer layer;

	public NetworkCheckNode(String title, NetworkLayer layer, boolean isSelected) {
		super(title, layer, isSelected);
		this.layer = layer;
	}

	@Override
	public Icon getIcon() {
		return new LineIcon(layer.getStroke());
	}

	class LineIcon implements Icon {
		int width = 10, height = 10;

		Stroke stroke;

		public LineIcon(Stroke stroke) {
			this.stroke = stroke;
		}

		public int getIconHeight() {
			return height;
		}

		public int getIconWidth() {
			return width;
		}

		public void paintIcon(Component arg0, Graphics arg1, int arg2, int arg3) {
			Graphics2D g2d = (Graphics2D) arg1;
			Stroke stroke = g2d.getStroke();
			g2d.setStroke(this.stroke);
			g2d.drawLine(0, 0, width, height);
			g2d.setStroke(stroke);
		}

	}

}
