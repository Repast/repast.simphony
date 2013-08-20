package repast.simphony.chart2.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.SwingUtilities;

import repast.simphony.chart2.engine.HistogramComponentControllerAction;
import repast.simphony.chart2.wizard.HistogramEditorWizard;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.util.DataUtilities;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.ui.plugin.AbstractEditorMenuItem;

/**
 * Implements the "Add Time Series Chart" menu item.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class HistogramMenuItem extends AbstractEditorMenuItem {

  public HistogramMenuItem() {
    super("Add Histogram Chart");
  }

  private HistogramComponentControllerAction createAction(Component parent, List<DataSetDescriptor> dataSets) {
    HistogramEditorWizard wizard = new HistogramEditorWizard(dataSets);
    wizard.showDialog(parent, "Histogram Editor");
    if (wizard.wasCancelled())
      return null;

    HistogramComponentControllerAction action = new HistogramComponentControllerAction(wizard
        .getModel().getDescriptor());
    return action;
  }

  /**
   * Invoked when an action occurs.
   */
  public void actionPerformed(ActionEvent e) {
    // evt is the scenario tree event that triggered this event
    Object contextID = evt.getContextID();
    HistogramComponentControllerAction action = createAction(
        SwingUtilities.getWindowAncestor(evt.getTree()), 
        DataUtilities.getDataSetDescriptors(evt.getRegistry(), evt.getContextID()));
    if (action != null) {
      ControllerRegistry registry = evt.getRegistry();

      ControllerAction parent = registry.findAction(contextID,
          ControllerActionConstants.CHART_ROOT);
      registry.addAction(contextID, parent, action);
      evt.addActionToTree(action);
      action.getDescriptor().addScenarioChangedListener(evt.getScenario());
      evt.getScenario().setDirty(true);
    }
  }
}
