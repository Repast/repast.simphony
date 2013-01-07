package repast.simphony.statecharts.edit.parts;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.edit.policies.reparent.CreationEditPolicyWithCustomReparent;

import repast.simphony.statecharts.edit.policies.StateMachineCanonicalEditPolicy;
import repast.simphony.statecharts.edit.policies.StateMachineItemSemanticEditPolicy;
import repast.simphony.statecharts.part.StatechartVisualIDRegistry;

/**
 * @generated
 */
public class StateMachineEditPart extends DiagramEditPart {

  /**
   * @generated
   */
  public final static String MODEL_ID = "Statechart"; //$NON-NLS-1$

  /**
   * @generated
   */
  public static final int VISUAL_ID = 1000;

  /**
   * @generated
   */
  public StateMachineEditPart(View view) {
    super(view);
  }

  /**
   * @generated NOT
   */
  protected void createDefaultEditPolicies() {
    super.createDefaultEditPolicies();
    installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new StateMachineItemSemanticEditPolicy());
    installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new StateMachineCanonicalEditPolicy());
    installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicyWithCustomReparent(
        StatechartVisualIDRegistry.TYPED_INSTANCE));
    // removes the popup bar
    //removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.POPUPBAR_ROLE);

    // removes the prompt etc. when a connection is made with no end point
    removeEditPolicy(org.eclipse.gef.EditPolicy.GRAPHICAL_NODE_ROLE);
  }

  /**
   * @generated
   */
  /*package-local*/static class NodeLabelDragPolicy extends NonResizableEditPolicy {

    /**
     * @generated
     */
    @SuppressWarnings("rawtypes")
    protected List createSelectionHandles() {
      MoveHandle h = new MoveHandle((GraphicalEditPart) getHost());
      h.setBorder(null);
      return Collections.singletonList(h);
    }

    /**
     * @generated
     */
    public Command getCommand(Request request) {
      return null;
    }

    /**
     * @generated
     */
    public boolean understandsRequest(Request request) {
      return false;
    }
  }

}
