package repast.simphony.visualization.engine;

import repast.simphony.engine.controller.DescriptorControllerAction;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.GUIRegistryType;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.NonModelAction;
import repast.simphony.parameter.Parameters;
import repast.simphony.visualization.IDisplay;
import simphony.util.messages.MessageCenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Component action for a display. This action when initalized creates a single display from a
 * descriptor.
 *
 * @author Nick Collier
 */
public class DisplayComponentControllerAction extends DefaultControllerAction implements
        DescriptorControllerAction<DisplayDescriptor> {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(DisplayComponentControllerAction.class);

  private List<DisplayUpdater> displays = new ArrayList<DisplayUpdater>();

  private DisplayDescriptor descriptor;
  private ISchedulableAction displayUpdate;

  @NonModelAction
  static class DisplayUpdater implements IAction {

    IDisplay display;

    public DisplayUpdater(IDisplay display) {
      this.display = display;
    }

    public void execute() {
      display.update();
    }
  }

  public DisplayComponentControllerAction(DisplayDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  @Override
  public void runCleanup(RunState runState, Object contextId) {
    if (displayUpdate != null) runState.getScheduleRegistry().getModelSchedule().removeAction(displayUpdate);
    for (DisplayUpdater updater : displays) {
      updater.display.destroy();
      updater.display = null;
    }
    displays.clear();
    displayUpdate = null;
  }

  @Override
  public void runInitialize(RunState runState, Object contextId, Parameters params) {
    if (!runState.getRunInfo().isBatch()) {
      if (descriptor.getDisplayType() == DisplayType.THREE_D) {
        // try to load a 3D class to make sure that the user has J3D installed
        try {
          Class.forName("com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom");
        } catch (ClassNotFoundException e) {
          JOptionPane.showMessageDialog(null, "Java3D 1.5 (java3d.dev.java.net) is required for 3D displays." +
                  "\nAborting 3D display creation of '" + descriptor.getName() + "'.");
          return;
        }
      }
      else if (descriptor.getDisplayType() == DisplayType.GIS3D) {
        // try to load a JOGL class to make sure that the user has JOGL installed
        try {
          Class.forName("javax.media.opengl.glu.GLU");
        } catch (ClassNotFoundException e) {
          JOptionPane.showMessageDialog(null, "JOGL (jogl.dev.java.net) is required for 3D GIS displays." +
                  "\nAborting display creation of '" + descriptor.getName() + "'.");
          return;
        }
      }

      DisplayProducer creator = new DisplayProducer(contextId, runState, descriptor);

      try {
        IDisplay display = creator.createDisplay();
        display.init();
        String contextName = contextId.toString();
        // Change to shorten the display name for "default_observer_context" displays
        if (contextName.equals("default_observer_context")){
        	contextName = "ReLogo";
        }
        runState.getGUIRegistry().addDisplay(contextName+": "+descriptor.getName(), GUIRegistryType.DISPLAY, display);
        DisplayUpdater updater = new DisplayUpdater(display);
        displayUpdate = runState.getScheduleRegistry().getModelSchedule().schedule(descriptor.getScheduleParameters(),
                updater);
        displays.add(updater);
      } catch (Exception ex) {
        msg.error("Error while creating displays", ex);
      }
    }
  }

  public DisplayDescriptor getDescriptor() {
    return descriptor;
  }

  public void setDescriptor(DisplayDescriptor descriptor) {
    this.descriptor = descriptor;
  }
}
