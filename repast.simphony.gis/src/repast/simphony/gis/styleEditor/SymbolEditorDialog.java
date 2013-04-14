package repast.simphony.gis.styleEditor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.geotools.styling.Rule;
import org.opengis.feature.type.FeatureType;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Dialog that wraps a RuleEditPanel.  Used in GIS style editor panels when
 *  the Rule editor is needed.
 * 
 * @author Nick Collier
 * @author Eric Tatara
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
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		ruleEditPanel1 = new RuleEditPanel();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		CellConstraints cc = new CellConstraints();

		setTitle("Edit Symbol");
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		dialogPane.setBorder(Borders.DIALOG);
		dialogPane.setLayout(new BorderLayout());

		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(ruleEditPanel1, BorderLayout.CENTER);

		dialogPane.add(contentPanel, BorderLayout.NORTH);

		buttonBar.setBorder(Borders.BUTTON_BAR_PAD);
		buttonBar.setLayout(new FormLayout(
				new ColumnSpec[] {
						FormSpecs.GLUE_COLSPEC,
						FormSpecs.BUTTON_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.BUTTON_COLSPEC
				},
				RowSpec.decodeSpecs("pref")));


		okButton.setText("OK");
		buttonBar.add(okButton, cc.xy(2, 1));

		cancelButton.setText("Cancel");
		buttonBar.add(cancelButton, cc.xy(4, 1));

		dialogPane.add(buttonBar, BorderLayout.SOUTH);

		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
	}

	private JPanel dialogPane;
	private JPanel contentPanel;
	private RuleEditPanel ruleEditPanel1;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
}