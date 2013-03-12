/*CopyrightHere*/
package repast.simphony.freezedry.datasource;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

/**
 * @author Jerry Vos
 */
public class JDBCConnectDialog extends JDialog {
	private static final long serialVersionUID = 2832861029004959959L;
	private boolean cancelled;
	
	public JDBCConnectDialog(Frame owner) {
		super(owner);
		initComponents();
		cancelled = true;
	}

	public JDBCConnectDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void okButtonActionPerformed(ActionEvent e) {
		cancelled = false;
		dispose();
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		this.dispose();
		cancelled = true;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		dBCConnectPanel1 = new JDBCConnectPanel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle("Connection Settings");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

		//======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG);
			dialogPane.setLayout(new BorderLayout());

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_PAD);
				buttonBar.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormSpecs.GLUE_COLSPEC,
						FormSpecs.BUTTON_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.BUTTON_COLSPEC
					},
					RowSpec.decodeSpecs("pref")));

				//---- okButton ----
				okButton.setText("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				buttonBar.add(okButton, cc.xy(2, 1));

				//---- cancelButton ----
				cancelButton.setText("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				buttonBar.add(cancelButton, cc.xy(4, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
			dialogPane.add(dBCConnectPanel1, BorderLayout.CENTER);
		}
		contentPane.add(dialogPane);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		
		getRootPane().setDefaultButton(okButton);
		
		
		ActionListener actionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent actionEvent)
			{
				cancelButton.doClick();
			}
		};
		
		KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		getRootPane().registerKeyboardAction(actionListener, escape, JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	private JDBCConnectPanel dBCConnectPanel1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public JDBCConnectPanel getPanel() {
		return this.dBCConnectPanel1;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JDBCConnectDialog dialog = new JDBCConnectDialog((JFrame) null);
		dialog.pack();
		dialog.setVisible(true);
	}
	
	public boolean wasCanceled() {
		return cancelled;
	}
}
