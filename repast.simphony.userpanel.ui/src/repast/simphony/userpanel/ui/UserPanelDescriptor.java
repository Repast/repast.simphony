package repast.simphony.userpanel.ui;

import repast.simphony.engine.schedule.Descriptor;

public interface UserPanelDescriptor extends Descriptor{
	
	public void setUserPanelCreatorClass(Class<?> clazz);
	
	public Class<?> getUserPanelCreatorClass();
	
}
