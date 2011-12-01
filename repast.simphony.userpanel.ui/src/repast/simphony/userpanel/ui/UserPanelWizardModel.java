package repast.simphony.userpanel.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.pietschy.wizard.models.StaticModel;

import repast.simphony.engine.schedule.ScheduleParameters;

public class UserPanelWizardModel extends StaticModel {
	private Collection<Class<?>> clazzes;

	private UserPanelDescriptor userPanelDescriptor;

	public UserPanelWizardModel(Collection<Class<?>> clazzes) {
		this(clazzes, new DefaultUserPanelDescriptor("Custom Panel"));
	}

	public UserPanelWizardModel(Collection<Class<?>> clazzes,
			UserPanelDescriptor descriptor) {
		this.clazzes = clazzes;
		this.userPanelDescriptor = descriptor;
	}

	public String getActionName() {
		return userPanelDescriptor.getName();
	}

	public void setActionName(String actionName) {
		userPanelDescriptor.setName(actionName);
	}

	public Collection<Class<?>> getClasses() {
		return clazzes;
	}

	public void setClasses(Collection<Class<?>> clazzes) {
		this.clazzes = clazzes;
	}

	public void setUserPanelCreatorClass(Class<?> clazz) {
		userPanelDescriptor.setUserPanelCreatorClass(clazz);
	}
	
	public Class<?> getUserPanelCreatorClass(){
		return userPanelDescriptor.getUserPanelCreatorClass();
	}

	public void setDescriptor(UserPanelDescriptor descriptor) {
		this.userPanelDescriptor = descriptor;
	}

	public UserPanelDescriptor getDescriptor() {
		return userPanelDescriptor;
	}


}
