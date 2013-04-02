/*CopyrightHere*/
package repast.simphony.freezedry.datasource;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author Jerry Vos
 */
public class JDBCConnectPanel extends JPanel {
	public static final String DEFAULT_DRIVER_MSG = "Problem loading driver, check driver settings. See the log file for more info.";

	public static final String DEFAULT_CONNECTING_MSG = "Problem connecting to database, check connection settings. See the log file for more info.";
	
	private static final long serialVersionUID = -4967296557284748174L;
	
	public JDBCConnectPanel() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		separator1 = compFactory.createSeparator("Database Connection Properties");
		label1 = new JLabel();
		urlField = new JTextField();
		label2 = new JLabel();
		driverField = new JTextField();
		separator2 = compFactory.createSeparator("Database User Properties");
		label3 = new JLabel();
		userNameField = new JTextField();
		label4 = new JLabel();
		passwordField = new JPasswordField();
		separator3 = compFactory.createSeparator("Last Connection Messages");
		infoLabel = new JLabel();
		errorLabel = new JLabel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.PREFERRED, FormSpec.DEFAULT_GROW)
			},
			new RowSpec[] {
				new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW),
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC
			}));
		add(separator1, cc.xywh(1, 1, 3, 1));

		//---- label1 ----
		label1.setText("URL");
		add(label1, cc.xy(1, 3));
		add(urlField, cc.xy(3, 3));

		//---- label2 ----
		label2.setText("Driver");
		add(label2, cc.xy(1, 5));
		add(driverField, cc.xy(3, 5));
		add(separator2, cc.xywh(1, 7, 3, 1));

		//---- label3 ----
		label3.setText("User Name");
		add(label3, cc.xy(1, 9));
		add(userNameField, cc.xy(3, 9));

		//---- label4 ----
		label4.setText("Password");
		add(label4, cc.xy(1, 11));
		add(passwordField, cc.xy(3, 11));
		add(separator3, cc.xywh(1, 13, 3, 1));

		//---- infoLabel ----
		infoLabel.setText("info");
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(infoLabel, cc.xywh(1, 15, 3, 1));

		//---- errorLabel ----
		errorLabel.setText("error");
		errorLabel.setForeground(Color.red);
		add(errorLabel, cc.xywh(1, 17, 3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		
//		infoLabel.setPreferredSize(new Dimension(50, errorLabel.getPreferredSize().height));
		errorLabel.setPreferredSize(new Dimension(50, errorLabel.getPreferredSize().height));
		errorLabel.setText("");
		infoLabel.setText("");
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JComponent separator1;
	private JLabel label1;
	private JTextField urlField;
	private JLabel label2;
	private JTextField driverField;
	private JComponent separator2;
	private JLabel label3;
	private JTextField userNameField;
	private JLabel label4;
	private JPasswordField passwordField;
	private JComponent separator3;
	private JLabel infoLabel;
	private JLabel errorLabel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public void setErrorMessage(String msg) {
		errorLabel.setText(msg);
	}
	
	public void setInfoMessage(String msg) {
		infoLabel.setText(msg);
	}
	
	public String getURL() {
		return urlField.getText(); 
	}
	
	public String getUsername() {
		return userNameField.getText(); 
	}
	
	public String getPassword() {
		return passwordField.getText(); 
	}
	
	public String getDriverName() {
		return driverField.getText(); 
	}

	public void setUserName(String userName) {
		userNameField.setText(userName);
	}

	public void setPassword(String password) {
		passwordField.setText(password);
	}

	public void setURL(String dbURL) {
		urlField.setText(dbURL);
	}

	public void setDriverName(String driverName) {
		driverField.setText(driverName);
	}
}

