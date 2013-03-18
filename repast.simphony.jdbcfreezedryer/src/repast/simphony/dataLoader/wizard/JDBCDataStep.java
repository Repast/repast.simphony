/*CopyrightHere*/
package repast.simphony.dataLoader.wizard;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.AutoCompleteDocument;
import org.jdesktop.swingx.autocomplete.ListAdaptor;
import org.pietschy.wizard.InvalidStateException;

import repast.simphony.freezedry.AbstractDataSource;
import repast.simphony.freezedry.datasource.DBUtils;
import repast.simphony.freezedry.datasource.JDBCConnectDialog;
import repast.simphony.freezedry.datasource.JDBCConnectPanel;
import repast.simphony.freezedry.datasource.JDBCDataSource;
import repast.simphony.freezedry.gui.ClassRetrievable;
import repast.simphony.util.Settings;
import repast.simphony.util.wizard.DynamicWizardModel;
import repast.simphony.util.wizard.ModelAwarePanelStep;
import saf.core.ui.util.UIUtilities;
import simphony.util.messages.MessageCenter;

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
public class JDBCDataStep<T extends DynamicWizardModel> extends ModelAwarePanelStep<T> implements ClassRetrievable {
	private static final long serialVersionUID = 8420028182447790071L;
	private static final MessageCenter LOG = MessageCenter
			.getMessageCenter(JDBCDataStep.class);

	class CompleteHandler implements DocumentListener, ItemListener {
		public void insertUpdate(DocumentEvent e) {
			update();
		}
		public void removeUpdate(DocumentEvent e) {
			update();
		}
		public void changedUpdate(DocumentEvent e) {
			update();
		}
		public void itemStateChanged(ItemEvent e) {
			update();
		}
		
		private void update() {
			setComplete(urlValid() && driverValid() && userInfoComplete());
		}
		private boolean driverValid() {
			try {
				Class.forName(driverField.getText());
				return true;
			} catch (ClassNotFoundException ex) {
				return false;
			}
		}
		private boolean urlValid() {
			return urlField.getText().indexOf(':') != urlField.getText().lastIndexOf(':');
			// url should be like: jdbc:<driver name>:<driver connection string>;
		}
		private boolean userInfoComplete() {
			if (loginBox.isSelected()) {
				if (userNameField.getText().equals("")) {
					return false;
				}
			}
			return true;
		}
		
	}
	
	private CompleteHandler completeHandler;
	
	private DefaultListModel urlListModel;
	
	private DefaultListModel driverListModel;
	
	public JDBCDataStep() {
		this("", "");
	}
	
	public JDBCDataStep(String title, String caption) {
		super(title, caption);
		completeHandler = new CompleteHandler();
		initComponents();
	}

	private void loginBoxItemStateChanged(ItemEvent e) {
		updateLoginFields();
	}

	private void updateLoginFields() {
//		boolean on = loginBox.isSelected();
//		userNameField.setEnabled(on);
//		passwordField.setEnabled(on);
		
		completeHandler.update();
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
		label8 = new JLabel();
		loginBox = new JCheckBox();
		label5 = new JLabel();
		label6 = new JLabel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
			},
			new RowSpec[] {
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
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC
			}));
		add(separator1, cc.xywh(1, 1, 3, 1));

		//---- label1 ----
		label1.setText("URL");
		add(label1, cc.xywh(1, 3, 1, 2));
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

		//---- label8 ----
		label8.setText("Store Login Details");
		add(label8, cc.xy(1, 13));

		//---- loginBox ----
		loginBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				loginBoxItemStateChanged(e);
			}
		});
		add(loginBox, cc.xy(3, 13));

		//---- label5 ----
		label5.setText("Note - If no user information is specified, you will be prompted when it is needed.");
		add(label5, cc.xywh(1, 15, 3, 1));

		//---- label6 ----
		label6.setText("Warning - Username and Passwords are saved in clear text and therefore not secure.");
		label6.setForeground(Color.red);
		add(label6, cc.xywh(1, 19, 3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		
		updateLoginFields();
		
		setupAutoComplete();
		
		urlField.getDocument().addDocumentListener(completeHandler);
		passwordField.getDocument().addDocumentListener(completeHandler);
		driverField.getDocument().addDocumentListener(completeHandler);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JComponent separator1;
	private JLabel label1;
	protected JTextField urlField;
	private JLabel label2;
	protected JTextField driverField;
	private JComponent separator2;
	private JLabel label3;
	protected JTextField userNameField;
	private JLabel label4;
	protected JPasswordField passwordField;
	private JLabel label8;
	protected JCheckBox loginBox;
	private JLabel label5;
	private JLabel label6;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	
	protected void setupAutoComplete() {
		JList urlList = new JList();
		urlListModel = new DefaultListModel();
		urlList.setModel(urlListModel);
		ListAdaptor adaptor = new ListAdaptor(urlList, urlField);		
		AutoCompleteDocument document = new AutoCompleteDocument(adaptor, false);
		AutoCompleteDecorator.decorate(urlField, document, adaptor);
		
		try {
			HashSet urlSet = (HashSet) Settings.getRegistry().get("JDBCDataStep.URLS");
			if (urlSet != null) {
				for (Object url : urlSet) {
					urlListModel.addElement(url.toString());
				}
			}
		} catch (Exception ex) {
			LOG.info("Error loading urls from registry", ex);
		}
		
		JList driverList = new JList();
		driverListModel = new DefaultListModel();
		driverList.setModel(driverListModel);
		adaptor = new ListAdaptor(driverList, driverField);		
		document = new AutoCompleteDocument(adaptor, false);
		AutoCompleteDecorator.decorate(driverField, document, adaptor);
		
		try {
			HashSet driverSet = (HashSet) Settings.getRegistry().get("JDBCDataStep.DRIVERS");
			if (driverSet != null) {
				for (Object driver : driverSet) {
					driverListModel.addElement(driver.toString());
				}
			}
		} catch (Exception ex) {
			LOG.info("Error loading drivers from registry", ex);
		}
	}
	
	protected boolean notNull(String string) {
		return string != null && !string.equals("");
	}
	
	public List<Class<?>> retrieveClasses() {
		ArrayList<Class<?>> classList = new ArrayList<Class<?>>();

		String userName = userNameField.getText();
		String password = passwordField.getText();
		
		while (true) {
			DataSource dataSource;
			Connection con = null;
			try {
				setBusy(true);
				// just in case, clear this out (if we started getting results then got an exception)
				classList.clear();
				dataSource = DBUtils.getDataSource(userName, password, urlField.getText(), driverField.getText());

				con = dataSource.getConnection();	
				
				String sql = "Select " + AbstractDataSource.ID_COL + " from "
				+ JDBCDataSource.DEFAULT_CLASS_MAPPING_TABLE;
				
				ResultSet results = con.createStatement().executeQuery(sql);
				
				while (results.next()) {
					String className = results.getString(AbstractDataSource.ID_COL.replaceAll("\"", ""));
					
					try {
						if (!className.endsWith(AbstractDataSource.CHILD_ID_MARKER)) {
							Class<?> clazz = Class.forName(className);
							classList.add(clazz);						
						}
					} catch (ClassNotFoundException ex) {
						LOG.info("Couldn't load class '" + className + "' found in classes table", ex);
					}
				}

				return classList;
				
			} catch (ClassNotFoundException ex) {
				LOG.info("Error loading specified driver '" + driverField.getText() + "'", ex);
				JDBCConnectDialog dialog = new JDBCConnectDialog((JFrame)null);
				JDBCConnectPanel panel = dialog.getPanel();
				panel.setErrorMessage(JDBCConnectPanel.DEFAULT_DRIVER_MSG);
				String loginInfo = "";
				if (!loginBox.isSelected()) {
					loginInfo = "Note - username and password will not be saved.";
				}

				panel.setInfoMessage(loginInfo);
				panel.setURL(urlField.getText());
				panel.setDriverName(driverField.getText());
				panel.setUserName(userName);
				panel.setPassword(password);
				
				UIUtilities.centerWindowOnScreen(dialog);
				dialog.setModal(true);
				dialog.pack();
				dialog.setVisible(true);
				if (dialog.wasCanceled()) {
					return classList;
				}
				
				userName = panel.getUsername();
				password = panel.getPassword();
				if (loginBox.isSelected()) {
					passwordField.setText(password);
					userNameField.setText(userName);
				}
				driverField.setText(panel.getDriverName());
				urlField.setText(panel.getURL());
			} catch (SQLException ex) {
				LOG.info("Error when executing database query.", ex);
				JDBCConnectDialog dialog = new JDBCConnectDialog((JFrame)null);
				JDBCConnectPanel panel = dialog.getPanel();
				panel.setErrorMessage(JDBCConnectPanel.DEFAULT_CONNECTING_MSG);
				String loginInfo = "";
				if (!loginBox.isSelected()) {
					loginInfo = "Note - username and password will not be saved.";
				}

				panel.setInfoMessage(loginInfo);
				panel.setURL(urlField.getText());
				panel.setDriverName(driverField.getText());
				panel.setUserName(userName);
				panel.setPassword(password);
				
				UIUtilities.centerWindowOnScreen(dialog);
				dialog.setModal(true);
				dialog.pack();
				dialog.setVisible(true);
				if (dialog.wasCanceled()) {
					return classList;
				}
				
				userName = panel.getUsername();
				password = panel.getPassword();
				if (loginBox.isSelected()) {
					passwordField.setText(password);
					userNameField.setText(userName);
				}
				driverField.setText(panel.getDriverName());
				urlField.setText(panel.getURL());
			} finally { 
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						// swallow exception
					}
				}
				setBusy(false);
			}
		}
	}
	
	@Override
	public void applyState() throws InvalidStateException {
		super.applyState();
		try {
			HashSet<String> urls = new HashSet<String>(urlListModel.getSize());
			for (int i = 0; i < Math.min(5, urlListModel.size()); i++) {
				urls.add(urlListModel.get(i).toString());
			}
			
			urls.add(urlField.getText());
			
			Settings.getRegistry().put("JDBCDataStep.URLS", urls);
		} catch (Exception ex) {
			LOG.info("Error storing urls.", ex);
		}
		try {
			HashSet<String> drivers = new HashSet<String>(driverListModel.getSize());
			for (int i = 0; i < Math.min(5, driverListModel.size()); i++) {
				drivers.add(driverListModel.get(i).toString());
			}
			
			drivers.add(driverField.getText());
			
			Settings.getRegistry().put("JDBCDataStep.DRIVERS", drivers);
		} catch (Exception ex) {
			LOG.info("Error storing drivers.", ex);
		}
	}
}
