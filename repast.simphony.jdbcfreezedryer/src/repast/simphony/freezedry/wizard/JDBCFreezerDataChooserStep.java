/*CopyrightHere*/
package repast.simphony.freezedry.wizard;

import org.pietschy.wizard.InvalidStateException;

import repast.simphony.dataLoader.wizard.JDBCDataStep;
import repast.simphony.freezedry.wizard.FreezeDryWizardModel;

/**
 * @author Jerry Vos
 */
public class JDBCFreezerDataChooserStep extends JDBCDataStep<FreezeDryWizardModel> {
	private static final long serialVersionUID = 8420028182447790071L;
	
	public JDBCFreezerDataChooserStep() {
		super("Database Connection Information",
				"Enter the necessary information for connecting to the database");
	}

	@Override
	public void prepare() {
		super.prepare();
		
		if (model != null && model.getBuilder() != null) {
			JDBCDSBuilder loader = (JDBCDSBuilder) model.getBuilder();
			
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
		JDBCDSBuilder builder = (JDBCDSBuilder) model.getBuilder();
		builder.setStoreLogin(super.loginBox.isSelected());
		builder.setDriverName(driverField.getText());
		builder.setDbURL(urlField.getText());
		builder.setUserName(userNameField.getText());
		builder.setPassword(passwordField.getText());
	}
}
