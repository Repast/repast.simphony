package repast.simphony.statecharts.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.AnimatableScrollPane;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.edit.policies.reparent.CreationEditPolicyWithCustomReparent;

import repast.simphony.statecharts.edit.policies.CompositeStateCompositeStateCompartmentCanonicalEditPolicy;
import repast.simphony.statecharts.edit.policies.CompositeStateCompositeStateCompartmentItemSemanticEditPolicy;
import repast.simphony.statecharts.part.Messages;
import repast.simphony.statecharts.part.StatechartVisualIDRegistry;
import repast.simphony.statecharts.policies.ShowPropsEditPolicy;

/**
 * @generated
 */
public class CompositeStateCompositeStateCompartmentEditPart extends ShapeCompartmentEditPart {

  /**
   * @generated
   */
  public static final int VISUAL_ID = 7001;

  /**
   * @generated
   */
  public CompositeStateCompositeStateCompartmentEditPart(View view) {
    super(view);
  }

  /**
   * @generated
   */
  public String getCompartmentName() {
    return Messages.CompositeStateCompositeStateCompartmentEditPart_title;
  }

  /**
   * @generated NOT
   */
  public IFigure createFigure() {
    ResizableCompartmentFigure result = (ResizableCompartmentFigure) super.createFigure();
    result.setTitleVisibility(false);
    AnimatableScrollPane pane = (AnimatableScrollPane) result.getChildren().get(1);
    pane.setScrollBarVisibility(ScrollPane.NEVER);
    return result;
  }

  /**
   * @generated NOT
   */
  protected void createDefaultEditPolicies() {
    super.createDefaultEditPolicies();
    installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
        new CompositeStateCompositeStateCompartmentItemSemanticEditPolicy());
    installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicyWithCustomReparent(
        StatechartVisualIDRegistry.TYPED_INSTANCE));
    installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
    installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
        new CompositeStateCompositeStateCompartmentCanonicalEditPolicy());
    removeEditPolicy(EditPolicyRoles.POPUPBAR_ROLE);
    installEditPolicy(EditPolicyRoles.POPUPBAR_ROLE, new CompositeStatePopupBarEditPolicy());
    
    //installEditPolicy(ShowPropsEditPolicy.EDIT_POLICY, new ShowPropsEditPolicy());
  }

  /**
   * @generated
   */
  protected void setRatio(Double ratio) {
    if (getFigure().getParent().getLayoutManager() instanceof ConstrainedToolbarLayout) {
      super.setRatio(ratio);
    }
  }

}
