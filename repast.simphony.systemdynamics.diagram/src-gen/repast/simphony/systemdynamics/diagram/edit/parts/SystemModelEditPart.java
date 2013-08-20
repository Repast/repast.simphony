package repast.simphony.systemdynamics.diagram.edit.parts;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableLabelEditPolicy;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.edit.policies.reparent.CreationEditPolicyWithCustomReparent;

import repast.simphony.systemdynamics.diagram.edit.policies.SystemModelCanonicalEditPolicy;
import repast.simphony.systemdynamics.diagram.edit.policies.SystemModelItemSemanticEditPolicy;
import repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry;

/**
 * @generated
 */
public class SystemModelEditPart extends DiagramEditPart {

  /**
   * @generated
   */
  public final static String MODEL_ID = "Systemdynamics"; //$NON-NLS-1$

  /**
   * @generated
   */
  public static final int VISUAL_ID = 1000;

  /**
   * @generated
   */
  public SystemModelEditPart(View view) {
    super(view);
  }

  /**
   * @generated NOT
   */
  protected void createDefaultEditPolicies() {
    super.createDefaultEditPolicies();
    installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new SystemModelItemSemanticEditPolicy());
    installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new SystemModelCanonicalEditPolicy());
    installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicyWithCustomReparent(
        SystemdynamicsVisualIDRegistry.TYPED_INSTANCE));
    removeEditPolicy(EditPolicyRoles.POPUPBAR_ROLE);
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

  /**
   * @generated
   */
  /*package-local*/static class LinkLabelDragPolicy extends NonResizableLabelEditPolicy {

    /**
     * @generated
     */
    @SuppressWarnings("rawtypes")
    protected List createSelectionHandles() {
      MoveHandle mh = new MoveHandle((GraphicalEditPart) getHost());
      mh.setBorder(null);
      return Collections.singletonList(mh);
    }
  }

}
