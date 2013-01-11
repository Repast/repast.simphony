package repast.simphony.systemdynamics.diagram.edit.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.View;

import repast.simphony.systemdynamics.diagram.edit.policies.InfluenceLinkItemSemanticEditPolicy;

/**
 * @generated
 */
public class InfluenceLinkEditPart extends ConnectionNodeEditPart implements ITreeBranchEditPart {

  /**
   * @generated
   */
  public static final int VISUAL_ID = 4004;

  /**
   * @generated
   */
  public InfluenceLinkEditPart(View view) {
    super(view);
  }

  /**
   * @generated
   */
  protected void createDefaultEditPolicies() {
    super.createDefaultEditPolicies();
    installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new InfluenceLinkItemSemanticEditPolicy());
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
    return new InfluenceLinkFigure();
  }

  /**
   * @generated
   */
  public InfluenceLinkFigure getPrimaryShape() {
    return (InfluenceLinkFigure) getFigure();
  }

  /**
   * @generated
   */
  public class InfluenceLinkFigure extends PolylineConnectionEx {

    /**
     * @generated
     */
    public InfluenceLinkFigure() {
      this.setForegroundColor(ColorConstants.blue);

      setTargetDecoration(createTargetDecoration());
    }

    /**
     * @generated
     */
    private RotatableDecoration createTargetDecoration() {
      PolygonDecoration df = new PolygonDecoration();
      df.setFill(true);
      return df;
    }

  }

}
