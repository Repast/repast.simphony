package repast.simphony.systemdynamics.diagram.edit.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polygon;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.notation.View;

import repast.simphony.systemdynamics.diagram.edit.policies.RateItemSemanticEditPolicy;
import repast.simphony.systemdynamics.figure.HourglassDecoration;

/**
 * @generated
 */
public class RateEditPart extends ConnectionNodeEditPart implements ITreeBranchEditPart {

  /**
   * @generated
   */
  public static final int VISUAL_ID = 4003;

  /**
   * @generated
   */
  public RateEditPart(View view) {
    super(view);
  }

  /**
   * @generated
   */
  protected void createDefaultEditPolicies() {
    super.createDefaultEditPolicies();
    installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new RateItemSemanticEditPolicy());
  }

  /**
   * @generated
   */
  protected boolean addFixedChild(EditPart childEditPart) {
    if (childEditPart instanceof RateNameEditPart) {
      ((RateNameEditPart) childEditPart).setLabel(getPrimaryShape().getFigureRateName());
      return true;
    }
    return false;
  }

  /**
   * @generated
   */
  protected void addChildVisual(EditPart childEditPart, int index) {
    if (addFixedChild(childEditPart)) {
      return;
    }
    super.addChildVisual(childEditPart, index);
  }

  /**
   * @generated
   */
  protected boolean removeFixedChild(EditPart childEditPart) {
    if (childEditPart instanceof RateNameEditPart) {
      return true;
    }
    return false;
  }

  /**
   * @generated
   */
  protected void removeChildVisual(EditPart childEditPart) {
    if (removeFixedChild(childEditPart)) {
      return;
    }
    super.removeChildVisual(childEditPart);
  }

  /**
   * Creates figure for this edit part.
   * 
   * Body of this method does not depend on settings in generation model so you
   * may safely remove <i>generated</i> tag and modify it.
   * 
   * @generated
   */

  protected Connection createConnectionFigure() {
    return new RateLinkFigure();
  }

  /**
   * @generated
   */
  public RateLinkFigure getPrimaryShape() {
    return (RateLinkFigure) getFigure();
  }

  /**
   * @generated
   */
  public class RateLinkFigure extends PolylineConnectionEx {

    /**
     * @generated
     */
    private WrappingLabel fFigureRateName;

    /**
     * @generated NOT
     */
    public RateLinkFigure() {
      this.setLineWidth(4);
      this.setForegroundColor(ColorConstants.black);

      createContents();
      setTargetDecoration(createTargetDecoration());
      add(new HourglassDecoration(), new ConnectionLocator(this) {
        public void relocate(IFigure target) {
          PointList points = getConnection().getPoints();
          target.setLocation(getLocation(points));
        }
      });
    }

    /**
     * @generated
     */
    private void createContents() {

      fFigureRateName = new WrappingLabel();

      fFigureRateName.setText("<É>");

      this.add(fFigureRateName);

    }

    /**
     * @generated
     */
    private RotatableDecoration createTargetDecoration() {
      PolygonDecoration df = new PolygonDecoration();
      df.setFill(true);
      df.setLineWidth(4);
      return df;
    }

    /**
     * @generated
     */
    public WrappingLabel getFigureRateName() {
      return fFigureRateName;
    }

  }

}
