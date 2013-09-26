/**
 * 
 */
package repast.simphony.statecharts.policies;

import java.util.List;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.actions.ShowPropertiesViewAction;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenEditPolicy;

/**
 * EditPolicy that shows the properties view on double mouse click. 
 * 
 * @author Nick Collier
 */
public class ShowPropsEditPolicy extends OpenEditPolicy {
  
  public static final String EDIT_POLICY = ShowPropsEditPolicy.class.getName();
  
  /* (non-Javadoc)
   * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenEditPolicy#getOpenCommand(org.eclipse.gef.Request)
   */
  @Override
  protected Command getOpenCommand(Request request) {
    final ShowPropertiesViewAction action = 
        new ShowPropertiesViewAction(((DefaultEditDomain)getHost().getViewer().getEditDomain()).getEditorPart());
    return new Command() {
      public void execute() {
        action.run();
        
        @SuppressWarnings("rawtypes")
        List parts = getHost().getViewer().getSelectedEditParts();
        if (parts.size() > 0) {
          EditPart part = (EditPart)parts.get(0);
          getHost().getViewer().deselect(part);
          getHost().getViewer().select(part);
        }
      }
    };
  }
}
