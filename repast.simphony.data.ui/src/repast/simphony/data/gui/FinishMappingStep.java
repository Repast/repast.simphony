/*CopyrightHere*/
package repast.simphony.data.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.pietschy.wizard.PanelWizardStep;

/**
 * @author Jerry Vos
 */
public class FinishMappingStep extends PanelWizardStep {
	private static final long serialVersionUID = 6470535900106598051L;

	public FinishMappingStep() {
		super("Data Mapping Creation Complete", "Data Mapping Creation Completed");
		initComponents();
		
		setComplete(true);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label1 = new JLabel();
		hSpacer1 = new JPanel(null);
		hSpacer2 = new JPanel(null);

		//======== this ========
		setLayout(new BorderLayout());

		//---- label1 ----
		label1.setText("Data mapping creation complete.  Press finish to add the created mapping.");
		add(label1, BorderLayout.CENTER);
		add(hSpacer1, BorderLayout.WEST);
		add(hSpacer2, BorderLayout.EAST);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	private JPanel hSpacer1;
	private JPanel hSpacer2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
