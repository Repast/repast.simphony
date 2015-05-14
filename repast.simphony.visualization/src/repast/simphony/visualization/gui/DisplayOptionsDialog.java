package repast.simphony.visualization.gui;

import java.awt.Component;
import java.awt.Dimension;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.models.SimplePath;

import repast.simphony.scenario.data.ContextData;
import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.ui.plugin.editor.OptionsEditorDialog;
import repast.simphony.visualization.engine.DisplayComponentControllerAction;
import repast.simphony.visualization.engine.DisplayDescriptor;

/**
 * Options dialog for editing an existing display. This takes the steps used by 
 * the dialog creation wizard and makes them each an option pane.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class DisplayOptionsDialog extends OptionsEditorDialog implements Editor {
  private static final long serialVersionUID = -5983444933550736114L;

  private DisplayComponentControllerAction action;

  private DisplayWizardModel model;

  // note that the contextId may not be the id of the root context.
  public DisplayOptionsDialog(String contextId, ContextData rootContext, DisplayComponentControllerAction action) {
    super();
    // create the model and the steps.
    this.action = action;
        
    // Make a copy of the existing DisplayDescriptor for the Wizard to work with.
    DisplayDescriptor descriptor = action.getDescriptor().makeCopy();
    
    DisplayConfigurationWizard displayWizard = new DisplayConfigurationWizard(contextId, descriptor, rootContext);
  	
    model = displayWizard.getModel();
    Wizard wizard = displayWizard.getWizard();
    
    // TODO Projections: use the regular GeneralStep here to allow modifying projections in the wizard. 
    NameOnlyGeneralStep gStep = new NameOnlyGeneralStep();  
    addContent("General", null, gStep, wizard);  
    
    // Skip the normal General step.
    model.nextStep(); 
    
    // Add remaining step.
    while (model.isNextAvailable()) {	
      addContent(model.getActiveStep().getName(), null, (PanelWizardStep) model.getActiveStep(), wizard);
    	model.nextStep();
    }
    // Last step
    addContent(model.getActiveStep().getName(), null, (PanelWizardStep) model.getActiveStep(), wizard);
  }
  
  @Override
  protected void ok() {
    super.ok();
    DisplayDescriptor descriptor = model.getDescriptor();
    
    // TODO Projections: check what is going on here.  Why did the old 
    //        DefaultDispayDescriptor.set() need to be called?
//    ((DefaultDisplayDescriptor)action.getDescriptor()).set(descriptor);
    
    action.setDescriptor(descriptor);
  }
}
