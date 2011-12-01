package repast.simphony.userpanel.ui;

import java.awt.Component;

import javax.swing.JPanel;

import repast.simphony.engine.controller.DescriptorControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.Parameters;
import repast.simphony.ui.RSApplication;

public class DefaultUserPanelDescriptorAction extends DefaultControllerAction implements DescriptorControllerAction<UserPanelDescriptor>{

	private UserPanelDescriptor descriptor;
	
	public DefaultUserPanelDescriptorAction(UserPanelDescriptor descriptor){
		this.descriptor = descriptor;
	}
	@Override
	public UserPanelDescriptor getDescriptor() {
		return descriptor;
	}
	
	@Override
	public void runInitialize(RunState runState, Object contextId,
			Parameters runParams) {
		try {
			if (!RSApplication.getRSApplicationInstance().hasCustomUserPanelDefined()){
				UserPanelCreator cpc = (UserPanelCreator) descriptor.getUserPanelCreatorClass().newInstance();
				RSApplication.getRSApplicationInstance().addCustomUserPanel(cpc.createPanel());
			}
			else {
				RSApplication.getRSApplicationInstance().removeCustomUserPanel();
				UserPanelCreator cpc = (UserPanelCreator) descriptor.getUserPanelCreatorClass().newInstance();
				RSApplication.getRSApplicationInstance().addCustomUserPanel(cpc.createPanel());
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		

	}
	/* (non-Javadoc)
	 * @see repast.simphony.engine.environment.DefaultControllerAction#runCleanup(repast.simphony.engine.environment.RunState, java.lang.Object)
	 */
	@Override
	public void runCleanup(RunState runState, Object contextId) {
//		RSApplication.getRSApplicationInstance().removeCustomUserPanel(userPanel);
//		userPanel = null;
	}
	
	
	
	
	

}
