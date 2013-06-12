package repast.simphony.userpanel.ui;

import repast.simphony.scenario.AbstractDescriptor;

public class DefaultUserPanelDescriptor extends AbstractDescriptor implements UserPanelDescriptor {

  private Class userPanelCreator;

  public DefaultUserPanelDescriptor() {
    this("");
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
    if (this.userPanelCreator == null || !userPanelCreator.equals(clazz))
      this.userPanelCreator = clazz;
  }
}
