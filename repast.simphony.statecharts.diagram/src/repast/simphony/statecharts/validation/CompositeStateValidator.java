/**
 * 
 */
package repast.simphony.statecharts.validation;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.swt.graphics.Point;

import repast.simphony.statecharts.edit.parts.CompositeState2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateCompositeStateCompartment2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateCompositeStateCompartmentEditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateEditPart;
import repast.simphony.statecharts.edit.parts.FinalState2EditPart;
import repast.simphony.statecharts.edit.parts.History2EditPart;
import repast.simphony.statecharts.edit.parts.HistoryEditPart;
import repast.simphony.statecharts.edit.parts.PseudoState3EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState4EditPart;
import repast.simphony.statecharts.edit.parts.State2EditPart;
import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.CompositeState;
import repast.simphony.statecharts.scmodel.PseudoState;
import repast.simphony.statecharts.scmodel.PseudoStateTypes;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.util.StatechartsModelUtil;

/**
 * Validates a CompositeState.
 * 
 * @author Nick Collier
 */
public class CompositeStateValidator {
  private Map<String, Point> defaultSizes = new HashMap<String, Point>();

 public CompositeStateValidator() {
    defaultSizes.put(String.valueOf(State2EditPart.VISUAL_ID), new Point(40, 40));
    defaultSizes.put(String.valueOf(CompositeState2EditPart.VISUAL_ID), new Point(200, 200));
    defaultSizes.put(String.valueOf(CompositeStateEditPart.VISUAL_ID), new Point(200, 200));
    defaultSizes.put(String.valueOf(FinalState2EditPart.VISUAL_ID), new Point(15, 15));
    defaultSizes.put(String.valueOf(HistoryEditPart.VISUAL_ID), new Point(15, 15));
    defaultSizes.put(String.valueOf(History2EditPart.VISUAL_ID), new Point(15, 15));
    defaultSizes.put(String.valueOf(PseudoState3EditPart.VISUAL_ID), new Point(15, 15));
    defaultSizes.put(String.valueOf(PseudoState4EditPart.VISUAL_ID), new Point(19, 19));
  }

  private int getWidth(int width, String type) {
    if (width == -1) {
      return defaultSizes.get(type).x;
    }
    return width;
  }

  private int getHeight(int height, String type) {
    if (height == -1) {
      return defaultSizes.get(type).y;
    }
    return height;
  }
  
  private boolean hasIncoming(AbstractState state) {
    if (StatechartsModelUtil.hasIncoming(state)) return true;
    if (state.eClass().equals(StatechartPackage.Literals.COMPOSITE_STATE)) {
      for (AbstractState child : ((CompositeState)state).getChildren()) {
        if (hasIncoming(child)) return true;
      }
    } 
    return false;
  }
  
  
  // checks if the composite state is connected to anything
  // outside it. This is a warning.
  private IStatus validateConnected(IValidationContext ctx) {
    Node node = (Node)ctx.getTarget();
    CompositeState state = (CompositeState) node.getElement();
    if (hasIncoming(state)) return ctx.createSuccessStatus();
    return ctx.createFailureStatus("Composite State has no incoming transitions");
  }

  private IStatus validateVisibility(IValidationContext ctx) {
    Node context = (Node) ctx.getTarget();
    Shape shape = (Shape) context;
    Bounds bounds = (Bounds) shape.getLayoutConstraint();
    int fHeight = shape.getFontHeight();
    int width = getWidth(bounds.getWidth(), shape.getType()) - 6;
    int height = getHeight(bounds.getHeight(), shape.getType()) - (fHeight + 16);

    for (Object obj : context.getChildren()) {
      Node child = (Node) obj;
      String type = child.getType();
      if (type.equals(String.valueOf(CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID))
          || type.equals(String
              .valueOf(CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID))) {

        for (Object gc : child.getChildren()) {
          LayoutConstraint lc = ((Node) gc).getLayoutConstraint();
          if (lc != null) {
            Bounds cBounds = (Bounds) lc;
            int x = cBounds.getX();
            int y = cBounds.getY();
            String gcType = ((Node) gc).getType();
            if (x < 0 || y < 0 || x + getWidth(cBounds.getWidth(), gcType) > width
                || y + getHeight(cBounds.getHeight(), gcType) > height) {
              //System.out.println(cBounds);
              //System.out.println("failure: " +  (x + getWidth(cBounds.getWidth(), gcType)) + ", " +
              //    (y + getHeight(cBounds.getHeight(), gcType)) + "; " + width + ", " + height);
              return ctx.createFailureStatus("Composite State contains hidden or partially hidden elements: runtime may render this Composite State incompletely.");
            }
          }
        }
      }
    }
    return ctx.createSuccessStatus();
  }
  
  public IStatus checkForWarnings(IValidationContext ctx) {
    IStatus status = validateConnected(ctx);
    if (!status.isOK()) return status;
    return validateVisibility(ctx);
  }
  
  // check that the composite state has an ID
  private IStatus validateID(IValidationContext ctx) {
    Node node = (Node)ctx.getTarget();
    CompositeState state = (CompositeState) node.getElement();
    if (state.getId().trim().length() == 0) return ctx.createFailureStatus("Composite State is missing required ID");
    return ctx.createSuccessStatus();
  }
  
  // checks that the composite state has an initial state
  // and that it is connected to state, if there is an
  // incoming transition to the composite state.
  private IStatus validateInitialState(IValidationContext ctx) {
    Node node = (Node)ctx.getTarget();
    CompositeState state = (CompositeState) node.getElement();
    if (StatechartsModelUtil.getIncoming(state).size() > 0) {
      EObject initState = null;
      for (AbstractState obj : state.getChildren()) {
        if (obj.eClass().equals(StatechartPackage.Literals.PSEUDO_STATE) &&
            ((PseudoState)obj).getType().equals(PseudoStateTypes.INITIAL)) {
          initState = obj;
          break;
        }
      }
      
      if (initState == null) {
        return ctx.createFailureStatus("Composite State is missing required Initial State Marker");
      }
      if (StatechartsModelUtil.getOutgoing(initState).size() == 0) {
        return ctx.createFailureStatus("Composite State's required Initial State Marker is unconnected");
      }
    }
    
    return ctx.createSuccessStatus();
  }
  
  private IStatus validateCode(IValidationContext ctx) {
    Node node = (Node)ctx.getTarget();
    CompositeState state = (CompositeState) node.getElement();
    if (!Validator.isCodeValid(state)) {
      return ctx.createFailureStatus("Composite State has invalid onEnter or onExit code.");
    }
    return ctx.createSuccessStatus();
  }
  
  public IStatus checkForErrors(IValidationContext ctx) {
    IStatus status = validateInitialState(ctx);
    if (!status.isOK()) return status;
    
    status = validateCode(ctx); 
    if (!status.isOK()) return status;
    
    return validateID(ctx);
  }
}
