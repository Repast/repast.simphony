package repast.simphony.ui.plugin.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/06 22:27:26 $
 */
public class DefaultEditorDialog extends JPanel implements Editor {

	private UISaver saver;
	private JDialog dialog;
	private boolean canceled = false;

	public DefaultEditorDialog(UISaver saver) {
		this.saver = saver;
		setLayout(new BorderLayout());
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		JPanel outerButtons = new JPanel(new BorderLayout());
		//SingleLineBorder border = new SingleLineBorder(Color.LIGHT_GRAY);
		//border.setDistanceFromEdge(6);
		//outerButtons.setBorder(border);

		buttons.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		buttons.add(Box.createHorizontalGlue());
		JButton save = new JButton("Save");
		buttons.add(save);
		buttons.add(Box.createRigidArea(new Dimension(6, 0)));
		JButton cancel = new JButton("Cancel");
		buttons.add(cancel);
		outerButtons.add(buttons, BorderLayout.CENTER);
		add(outerButtons, BorderLayout.SOUTH);

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				save();
			}
		});

		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				cancel();
			}
		});

		add(saver, BorderLayout.CENTER);
	}

	private void save() {
		boolean okToExit = true;
		okToExit = saver.save();
		canceled = false;
		if (okToExit)
			exit();
	}

	private void cancel() {
		boolean okToExit = true;
		okToExit = saver.cancel();
		canceled = true;
		if (okToExit)
			exit();
	}

	private void exit() {
		dialog.dispose();
	}

	public boolean wasCanceled() {
		return canceled;
	}

	public void display(JFrame parent) {
		dialog = new JDialog(parent, saver.getDialogTitle(), true);
		dialog.setLayout(new BorderLayout());
		dialog.add(this, BorderLayout.CENTER);
		dialog.pack();
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
	}

	public void display(JDialog parent) {
		dialog = new JDialog(parent, saver.getDialogTitle(), true);
		dialog.setLayout(new BorderLayout());
		dialog.add(this, BorderLayout.CENTER);
		dialog.pack();
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
	}
}
