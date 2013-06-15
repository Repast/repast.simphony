package repast.simphony.userpanel.ui;

import java.io.File;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.scenario.AbstractDescriptorControllerActionIO;
import repast.simphony.scenario.ActionLoader;
import repast.simphony.scenario.DescriptorActionLoader;
import repast.simphony.scenario.Scenario;

public class UserPanelControllerActionIO
    extends
    AbstractDescriptorControllerActionIO<DefaultUserPanelDescriptorAction, DefaultUserPanelDescriptor> {

  public static class UserPanelActionLoader extends DescriptorActionLoader<UserPanelDescriptor> {

    public UserPanelActionLoader(File file, Object contextID) {
      super(file, contextID, UserPanelDescriptor.class, ControllerActionConstants.USER_PANEL_ROOT);
    }

    @Override
    protected ControllerAction createAction(UserPanelDescriptor descriptor, Scenario scenario) {
      descriptor.addScenarioChangedListener(scenario);
      return new DefaultUserPanelDescriptorAction(descriptor);
    }

    @Override
    protected ClassLoader getClassLoader() {
      return getClass().getClassLoader();
    }
  }

  public UserPanelControllerActionIO() {
    super(DefaultUserPanelDescriptorAction.class, DefaultUserPanelDescriptor.class);
  }

  @Override
  public ActionLoader getActionLoader(File actionFile, Object contextID) {
    return new UserPanelActionLoader(actionFile, contextID);
  }

}
