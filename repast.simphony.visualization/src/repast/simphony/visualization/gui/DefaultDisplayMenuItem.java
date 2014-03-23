package repast.simphony.visualization.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.SwingUtilities;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.ui.plugin.AbstractEditorMenuItem;
import repast.simphony.ui.tree.ScenarioTreeEvent;
import repast.simphony.visualization.engine.DefaultDisplayDescriptor;
import repast.simphony.visualization.engine.DisplayComponentControllerAction;
import repast.simphony.visualization.engine.DisplayDescriptor;

/**
 * EditorMenuItem for creating displays. This is attached to a display composite action.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class DefaultDisplayMenuItem extends AbstractEditorMenuItem {


  public DefaultDisplayMenuItem() {
    super("Add Display");
  }

  public void actionPerformed(ActionEvent e) {
    DisplayComponentControllerAction action = createAction(SwingUtilities.getWindowAncestor(evt.getTree()),
            evt);
    if (action != null) {
      ControllerRegistry registry = evt.getRegistry();
      Object contextID = evt.getContextID();
      ControllerAction parent = registry.findAction(contextID, ControllerActionConstants.VIZ_ROOT);
      registry.addAction(contextID, parent, action);
      evt.addActionToTree(action);
      action.getDescriptor().addScenarioChangedListener(evt.getScenario());
      evt.getScenario().setDirty(true);
    }
  }

  private DisplayComponentControllerAction createAction(Component parent, ScenarioTreeEvent evt) {
    
  	// TODO Projections: should be null for an empty desciptor since we don't know the type yet.
//  	DisplayDescriptor descriptor = new DefaultDisplayDescriptor("A Display");
  	
  	// TODO Projections: make sure the descriptor is set for the action later once its created.
  	//         ... might need need to since DisplayWizardCreator.getAction() does this...need to check
    DisplayComponentControllerAction action = new DisplayComponentControllerAction(null);
    
    ContextData context = null;
    List<ContextData> allContexts = evt.getScenario().getContext().getAllContexts();

    for (ContextData con : allContexts) {
      if (con.getId().equals(evt.getContextID())) {
        context = con;
        break;
      }
    }

    DisplayWizardCreator config = new DisplayWizardCreator(evt.getContextID(), context, action);

    config.showDialog(parent);
    return config.getAction();
  }
}
