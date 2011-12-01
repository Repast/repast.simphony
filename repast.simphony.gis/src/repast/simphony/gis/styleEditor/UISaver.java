package repast.simphony.gis.styleEditor;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2007/04/18 19:25:53 $
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
