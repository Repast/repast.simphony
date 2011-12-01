package repast.simphony.userpanel.ui;

import repast.simphony.engine.schedule.DefaultDescriptor;

public class DefaultUserPanelDescriptor extends DefaultDescriptor implements UserPanelDescriptor{
	
	private Class userPanelCreator;
	
	public DefaultUserPanelDescriptor() {
		this(null);
	}

	public DefaultUserPanelDescriptor(String name) {
		super(name);
	}

	@Override
	public Class<?> getUserPanelCreatorClass() {
		return this.userPanelCreator;
	}

	@Override
	public void setUserPanelCreatorClass(Class<?> clazz) {
		this.userPanelCreator = clazz;
	}
}
