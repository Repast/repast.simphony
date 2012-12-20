package repast.simphony.systemdynamics.sdmodel.diagram.edit.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.notation.View;

import repast.simphony.systemdynamics.sdmodel.diagram.edit.policies.CausalLinkItemSemanticEditPolicy;

/**
 * @generated
 */
public class CausalLinkEditPart extends ConnectionNodeEditPart implements ITreeBranchEditPart {

  /**
   * @generated
   */
  public static final int VISUAL_ID = 4002;

  /**
   * @generated
   */
  public CausalLinkEditPart(View view) {
    super(view);
  }

  /**
   * @generated
   */
  protected void createDefaultEditPolicies() {
    super.createDefaultEditPolicies();
    installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new CausalLinkItemSemanticEditPolicy());
  }

  /**
   * Creates figure for this edit part.
   * 
   * Body of this method does not depend on settings in generation model
   * so you may safely remove <i>generated</i> tag and modify it.
   * 
   * @generated
   */

  protected Connection createConnectionFigure() {
    return new CausalLinkFigure();
  }

  /**
   * @generated
   */
  public CausalLinkFigure getPrimaryShape() {
    return (CausalLinkFigure) getFigure();
  }

  /**
   * @generated
   */
  public class CausalLinkFigure extends PolylineConnectionEx {

    /**
     * @generated
     */
    public CausalLinkFigure() {
      this.setLineWidth(2);
      this.setForegroundColor(ColorConstants.blue);

      setTargetDecoration(createTargetDecoration());
    }

    /**
     * @generated
     */
    private RotatableDecoration createTargetDecoration() {
      PolygonDecoration df = new PolygonDecoration();
      df.setFill(true);
      df.setLineWidth(2);
      return df;
    }

  }

}
