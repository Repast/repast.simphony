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

import org.geotools.styling.Style;
import org.opengis.feature.type.FeatureType;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author User #1
 */
public class StyleDialog extends JDialog {
	public StyleDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public StyleDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

	private boolean completed;

//	public void setMapLayer(FeatureLayer layer) {
//		styleEditorPanel1.setMapLayer(layer);
//	}
	
	public void setData(FeatureType featureType, Style style){
		styleEditorPanel1.setData(featureType, style);
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		this.setVisible(false);
		this.dispose();
	}

	private void okButtonActionPerformed(ActionEvent e) {
		this.setVisible(false);
		this.dispose();
		completed = true;
	}

	public boolean display() {
		setModal(true);
		setVisible(true);
		return completed;
	}

	public Style getStyle() {
		return styleEditorPanel1.getStyle();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		styleEditorPanel1 = new StyleEditorPanel();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// ======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG);
			dialogPane.setLayout(new BorderLayout());

			// ======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
						FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT,
								FormSpec.DEFAULT_GROW) }, RowSpec
						.decodeSpecs("fill:default:grow")));
				contentPanel.add(styleEditorPanel1, cc.xywh(1, 1, 3, 1));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			// ======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_PAD);
				buttonBar.setLayout(new FormLayout(new ColumnSpec[] {
						FormSpecs.GLUE_COLSPEC, FormSpecs.BUTTON_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.BUTTON_COLSPEC }, RowSpec
						.decodeSpecs("pref")));

				// ---- okButton ----
				okButton.setText("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				buttonBar.add(okButton, cc.xy(2, 1));

				// ---- cancelButton ----
				cancelButton.setText("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				buttonBar.add(cancelButton, cc.xy(4, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;

	private JPanel contentPanel;

	private StyleEditorPanel styleEditorPanel1;

	private JPanel buttonBar;

	private JButton okButton;

	private JButton cancelButton;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
