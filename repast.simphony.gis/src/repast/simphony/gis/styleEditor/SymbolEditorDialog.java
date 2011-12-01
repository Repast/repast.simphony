/*
 * Created by JFormDesigner on Tue Apr 17 10:36:47 EDT 2007
 */

package repast.simphony.gis.styleEditor;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.geotools.feature.FeatureType;
import org.geotools.styling.Rule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author User #2
 */
public class SymbolEditorDialog extends JDialog {

	private Rule rule = null;

	public SymbolEditorDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public SymbolEditorDialog(Dialog owner) {
		super(owner);
		initComponents();

		okButton.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent evt) {
			  rule = ruleEditPanel1.getRule();
		    dispose();
		  }
		});

		cancelButton.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent evt) {
			  dispose();
		  }
		});
	}

	public void init(FeatureType type, Rule rule) {
		ruleEditPanel1.init(type, rule);
	}

	public Rule display() {
		setModal(true);
		setVisible(true);
		return rule;
	}



	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		ruleEditPanel1 = new RuleEditPanel();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle("Edit Symbol");
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new BorderLayout());
				contentPanel.add(ruleEditPanel1, BorderLayout.CENTER);
			}
			dialogPane.add(contentPanel, BorderLayout.NORTH);

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.GLUE_COLSPEC,
						FormFactory.BUTTON_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.BUTTON_COLSPEC
					},
					RowSpec.decodeSpecs("pref")));

				//---- okButton ----
				okButton.setText("OK");
				buttonBar.add(okButton, cc.xy(2, 1));

				//---- cancelButton ----
				cancelButton.setText("Cancel");
				buttonBar.add(cancelButton, cc.xy(4, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;
	private JPanel contentPanel;
	private RuleEditPanel ruleEditPanel1;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
