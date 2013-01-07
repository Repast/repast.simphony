/**
 * 
 */
package repast.simphony.statecharts.edit.parts;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.PopupBarEditPolicy;

import repast.simphony.statecharts.part.Messages;
import repast.simphony.statecharts.providers.StatechartElementTypes;

/**
 * Popup edit policy for the state machine itself.
 * 
 * @author Nick Collier
 */
public class CompositeStatePopupBarEditPolicy extends PopupBarEditPolicy {

  @Override
  protected void fillPopupBarDescriptors() {
    super.fillPopupBarDescriptors();
    addPopupBarDescriptor(new StateMachinePopupBarTool(this, StatechartElementTypes.State_3001, Messages.State3CreationTool_desc));
    addPopupBarDescriptor(new StateMachinePopupBarTool(this, StatechartElementTypes.CompositeState_3002, Messages.CompositeState4CreationTool_desc));
    addPopupBarDescriptor(new StateMachinePopupBarTool(this, StatechartElementTypes.History_3008, Messages.ShallowHistory6CreationTool_desc));
    addPopupBarDescriptor(new StateMachinePopupBarTool(this, StatechartElementTypes.History_3009, Messages.DeepHistory7CreationTool_desc));
  }

  public void addPopupBarDescriptor(StateMachinePopupBarTool tool) {
    super.addPopupBarDescriptor(null, tool.getImage(), tool, tool.getDescription());
  }

  @Override
  protected boolean shouldShowDiagramAssistant() {
    return true;
  }
}
