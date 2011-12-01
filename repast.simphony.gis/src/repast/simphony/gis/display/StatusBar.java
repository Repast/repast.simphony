package repast.simphony.gis.display;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
	private static final long serialVersionUID = -6005582629829739723L;

	JLabel label = new JLabel();

	boolean isLabeled = false;

	private Component currentComponent;

	public StatusBar() {
		super();
		setLayout(new BorderLayout());
		super.setPreferredSize(new Dimension(100, 16));
		add(label, BorderLayout.CENTER);
		currentComponent = label;
		isLabeled = true;
		setMessage("");
	}

	public void setMessage(String message) {
		if (!isLabeled) {
			remove(currentComponent);
			add(label, BorderLayout.CENTER);
			invalidate();
			repaint();
		}
		label.setText(message);
		currentComponent = label;
		isLabeled = true;
	}

	public void setComponent(Component comp) {
		this.remove(currentComponent);
		isLabeled = false;
		this.currentComponent = comp;
		add(comp, BorderLayout.CENTER);
		invalidate();
		repaint();
	}
}
