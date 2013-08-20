package repast.simphony.statecharts.edit.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Routing;
import org.eclipse.gmf.runtime.notation.View;

import repast.simphony.statecharts.edit.policies.TransitionItemSemanticEditPolicy;
import repast.simphony.statecharts.policies.ShowPropsEditPolicy;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;

/**
 * @generated
 */
public class TransitionEditPart extends ConnectionNodeEditPart implements ITreeBranchEditPart {
  
  private enum Side {TOP, BOTTOM, LEFT, RIGHT}; 

  /**
   * @generated
   */
  public static final int VISUAL_ID = 4001;

  /**
   * @generated
   */
  public TransitionEditPart(View view) {
    super(view);
  }

  /**
   * @generated
   */
  protected void createDefaultEditPolicies() {
    super.createDefaultEditPolicies();
    installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new TransitionItemSemanticEditPolicy());
    installEditPolicy(ShowPropsEditPolicy.EDIT_POLICY, new ShowPropsEditPolicy());
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
    return new TransitionFigure();
  }

  /**
   * @generated
   */
  public TransitionFigure getPrimaryShape() {
    return (TransitionFigure) getFigure();
  }
  
  /**
   * Handles switch betwen self link with self trans as true and
   * self linke with self trans as false.
   * 
   */
  @Override
  protected void handleNotificationEvent(Notification notification) {
    if (StatechartPackage.eINSTANCE.getTransition_SelfTransition().equals(notification.getFeature())) {
      // adding and removing seems to be the only way to get it
      // to redraw immediately
      TransitionFigure figure = (TransitionFigure) getFigure();
      Edge edge = (Edge) getModel();
      //Routing routing = (Routing) ViewUtil.getStructuralFeatureValue(edge, NotationPackage.eINSTANCE.getRoutingStyle_Routing());
      // set the routing style to be rectilinear as that makes it much easier to deal with the
      // self link
      ViewUtil.setStructuralFeatureValue(edge, NotationPackage.eINSTANCE.getRoutingStyle_Routing(),
          Routing.RECTILINEAR_LITERAL);
      
      IFigure parent = figure.getParent();
      parent.remove(figure);
      parent.add(figure);
      // set it back to the original routing
     // ViewUtil.setStructuralFeatureValue(edge, NotationPackage.eINSTANCE.getRoutingStyle_Routing(),
      //    routing);
    }

    super.handleNotificationEvent(notification);
  }

  /**
   * @generated NOT
   */
  public class TransitionFigure extends PolylineConnectionEx {

    /**
     * @generated
     */
    public TransitionFigure() {
      this.setForegroundColor(ColorConstants.black);

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

    /**
     * checks for self link and if so then draws as line across. 
     * 
     * @generated NOT
     */
    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx#setPoints(org.eclipse.draw2d.geometry.PointList)
     */
    @Override
    public void setPoints(PointList points) {
      ConnectionAnchor source = getSourceAnchor();
      ConnectionAnchor target = getTargetAnchor();
      Transition trans = (Transition)resolveSemanticElement();
     
      if (source != null && target != null && target.getOwner() != null && source.getOwner() != null &&
          target.getOwner().equals(source.getOwner()) && trans.isSelfTransition()) {
        
        PointList selfPts = new PointList();
        selfPts.addPoint(points.getFirstPoint());
        selfPts.addPoint(points.getLastPoint());
        
        super.setPoints(selfPts);
      } else {
        super.setPoints(points);
      }
    }
  }
}
