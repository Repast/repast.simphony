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

import repast.simphony.statecharts.edit.policies.CompositeStateCompositeStateCompartment2CanonicalEditPolicy;
import repast.simphony.statecharts.edit.policies.CompositeStateCompositeStateCompartment2ItemSemanticEditPolicy;
import repast.simphony.statecharts.part.Messages;
import repast.simphony.statecharts.part.StatechartVisualIDRegistry;
import repast.simphony.statecharts.policies.ShowPropsEditPolicy;

/**
 * @generated
 */
public class CompositeStateCompositeStateCompartment2EditPart extends ShapeCompartmentEditPart {

  /**
   * @generated
   */
  public static final int VISUAL_ID = 7002;

  /**
   * @generated
   */
  public CompositeStateCompositeStateCompartment2EditPart(View view) {
    super(view);
  }

  /**
   * @generated
   */
  public String getCompartmentName() {
    return Messages.CompositeStateCompositeStateCompartment2EditPart_title;
  }

  /**
   * @generated NOT
   */
  public IFigure createFigure() {
    ResizableCompartmentFigure result = (ResizableCompartmentFigure) super.createFigure();
    AnimatableScrollPane pane = (AnimatableScrollPane) result.getChildren().get(1);
    pane.setScrollBarVisibility(ScrollPane.NEVER);
    result.setTitleVisibility(false);
    return result;
  }

  /**
   * @generated NOT
   */
  protected void createDefaultEditPolicies() {
    super.createDefaultEditPolicies();
    installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
        new CompositeStateCompositeStateCompartment2ItemSemanticEditPolicy());
    installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicyWithCustomReparent(
        StatechartVisualIDRegistry.TYPED_INSTANCE));
    installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
    installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
        new CompositeStateCompositeStateCompartment2CanonicalEditPolicy());
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
