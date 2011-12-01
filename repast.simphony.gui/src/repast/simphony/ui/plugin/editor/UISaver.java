package repast.simphony.ui.plugin.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/09 15:55:03 $
 */
public abstract class UISaver extends JPanel {

	public abstract String getDialogTitle();

	public abstract boolean save();

	public abstract boolean cancel();

	protected JPanel createTitlePanel(String title) {
		JPanel outerLabelPanel = new JPanel(new BorderLayout());
		outerLabelPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));
		JPanel innerLabelPanel = new JPanel();
		innerLabelPanel.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));
		innerLabelPanel.setBackground(Color.BLUE);
		JLabel label = new JLabel(title);
		label.setForeground(Color.WHITE);
		label.setFont(label.getFont().deriveFont(Font.BOLD, 11f));
		innerLabelPanel.add(label, BorderLayout.CENTER);
		outerLabelPanel.add(innerLabelPanel, BorderLayout.CENTER);
		// outerLabelPanel.setSize(outerLabelPanel.getPreferredSize());
		// outerLabelPanel.setMaximumSize(outerLabelPanel.getPreferredSize());
		return outerLabelPanel;
	}
}
