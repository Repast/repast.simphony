package repast.simphony.data2.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.SwingUtilities;

import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.FileSinkComponentControllerAction;
import repast.simphony.data2.util.DataUtilities;
import repast.simphony.data2.wizard.FileSinkEditorWizard;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.ui.plugin.AbstractEditorMenuItem;

/**
 * Implements the "Add Data Set" menu item.
 * 
 * @author Nick Collier
 */
public class FileSinkMenuItem extends AbstractEditorMenuItem {
  private static final long serialVersionUID = 5878701522312055100L;

  public FileSinkMenuItem() {
    super("Add File Sink");
  }

  private FileSinkComponentControllerAction createAction(Component parent, List<Class<?>> agentClasses, 
      List<DataSetDescriptor> dataSets) {
    FileSinkEditorWizard wizard = new FileSinkEditorWizard(dataSets);
    wizard.showDialog(parent, "File Sink Editor");
    if (wizard.wasCancelled()) return null;

    FileSinkComponentControllerAction action = new FileSinkComponentControllerAction(wizard.getModel().getDescriptor());
    return action;
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed(ActionEvent e) {
    // evt is the scenario tree event that triggered this event
    Object contextID = evt.getContextID();
    List<Class<?>> clazzes = evt.getScenario().getContext().getAgentClasses(true);
    FileSinkComponentControllerAction action = createAction(SwingUtilities.getWindowAncestor(evt.getTree()),
            clazzes, DataUtilities.getDataSetDescriptors(evt.getRegistry(), evt.getContextID()));
    if (action != null) {
      ControllerRegistry registry = evt.getRegistry();

      ControllerAction parent = registry.findAction(contextID, ControllerActionConstants.OUTPUTTER_ROOT);
      registry.addAction(contextID, parent, action);
      evt.addActionToTree(action);
    }
  }
}
