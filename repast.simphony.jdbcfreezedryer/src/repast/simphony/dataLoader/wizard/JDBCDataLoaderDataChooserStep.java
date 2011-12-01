/*CopyrightHere*/
package repast.simphony.dataLoader.wizard;

import org.pietschy.wizard.InvalidStateException;

import repast.simphony.dataLoader.ui.wizard.DataLoaderWizardModel;
import repast.simphony.freezedry.gui.ClassRetrievable;

/**
 * @author Jerry Vos
 */
public class JDBCDataLoaderDataChooserStep extends JDBCDataStep<DataLoaderWizardModel> implements ClassRetrievable {
	private static final long serialVersionUID = 8420028182447790071L;
	
	public JDBCDataLoaderDataChooserStep() {
		super("Database Connection Information",
				"Enter the necessary information for connecting to the database");
	}

	@Override
	public void prepare() {
		super.prepare();
		
		if (model != null && model.getBuilder() != null) {
			JDBCContextActionBuilder loader = (JDBCContextActionBuilder) model.getBuilder();
			
			if (notNull(loader.getDriverName())) {
				driverField.setText(loader.getDriverName());
			}
			if (notNull(loader.getDbURL())) {
				urlField.setText(loader.getDbURL());
			}
			if (notNull(loader.getUserName())) {
				userNameField.setText(loader.getUserName());
			}
			if (notNull(loader.getPassword())) {
				passwordField.setText(loader.getPassword());
			}
			loginBox.setSelected(loader.getStoreLogin());
		}
	}
	
	@Override
	public void applyState() throws InvalidStateException {
		super.applyState();
		JDBCContextActionBuilder loader = (JDBCContextActionBuilder) model.getBuilder();
		loader.setStoreLogin(super.loginBox.isSelected());
		loader.setDriverName(driverField.getText());
		loader.setDbURL(urlField.getText());
		loader.setUserName(userNameField.getText());
		loader.setPassword(passwordField.getText());
	}
}
