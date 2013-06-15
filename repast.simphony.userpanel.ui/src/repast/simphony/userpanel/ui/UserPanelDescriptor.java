package repast.simphony.userpanel.ui;

import repast.simphony.engine.schedule.Descriptor;
import repast.simphony.scenario.ScenarioChangedListener;

public interface UserPanelDescriptor extends Descriptor{
	
	public void setUserPanelCreatorClass(Class<?> clazz);
	
	public Class<?> getUserPanelCreatorClass();
	
	void addScenarioChangedListener(ScenarioChangedListener listener);
	
}
