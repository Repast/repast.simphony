package repast.simphony.gis.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

/**
 * This is a simple Component that implements an animated clock. This can be
 * used to show the user that activity is going on as an alternative to the
 * Indeterminate ProgressBar. To start the animation call start(), and call
 * stop() when you are done to avoid having a lost thread.
 * 
 * @author Howe
 * 
 */
public class TimerIcon extends JLabel {

	private static final long serialVersionUID = -4996937649039393053L;

	private Icon[] images = new Icon[8];

	private Thread animateThread;

	private boolean running;

	private String[] imageNames = new String[] { "ClockN.gif", "ClockNE.gif",
			"ClockE.gif", "ClockSE.gif", "ClockS.gif", "ClockSW.gif",
			"ClockW.gif", "ClockNW.gif" };

	public TimerIcon() {
		initImages();
	}

	public TimerIcon(Dimension size) {
		initImages(size);
		// setPreferredSize(size);
	}

	public void showClock(boolean show) {
		if (show) {
			if (!running) {
				running = true;
				animateThread = new ClockThread();
				animateThread.start();
			}
		} else {
			running = false;
			setIcon(null);
		}
	}

	public void setMessage(String message) {
		setText(message);
	}

	private void initImages() {
		int i = 0;
		for (String imageName : imageNames) {
			try {
				URL imageFile = TimerIcon.class.getResource(imageName);
				images[i] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						imageFile));
				i++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void initImages(Dimension size) {
		int i = 0;
		for (String imageName : imageNames) {
			try {
				URL imageFile = TimerIcon.class.getResource(imageName);
				images[i] = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						imageFile).getScaledInstance(size.width, size.height,
						Image.SCALE_SMOOTH));
				i++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(200, 200);
		frame.setLayout(new BorderLayout());
		final TimerIcon icon = new TimerIcon(new Dimension(16, 16));
		frame.add(icon, BorderLayout.CENTER);
		icon.setMessage("Loading shapefile");
		Action action = new AbstractAction() {
			boolean toggled = false;

			public void actionPerformed(ActionEvent e) {
				toggled = !toggled;
				icon.showClock(toggled);
			}
		};
		action.putValue(Action.NAME, "Start/Stop");
		frame.add(new JButton(action), BorderLayout.PAGE_END);
		frame.setVisible(true);

	}

	class ClockThread extends Thread {
		public void run() {
			int i = 1;
			while (running) {
				int index = i % 8;
				setIcon(images[index]);
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				i++;
			}
		}
	}
}
