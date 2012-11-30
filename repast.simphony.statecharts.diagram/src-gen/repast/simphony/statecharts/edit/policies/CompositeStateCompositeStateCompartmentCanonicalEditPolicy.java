package repast.simphony.statecharts.edit.policies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredLayoutCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetViewMutabilityCommand;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Location;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.Size;
import org.eclipse.gmf.runtime.notation.View;

import repast.simphony.statecharts.edit.parts.CompositeState2EditPart;
import repast.simphony.statecharts.edit.parts.FinalState2EditPart;
import repast.simphony.statecharts.edit.parts.History2EditPart;
import repast.simphony.statecharts.edit.parts.HistoryEditPart;
import repast.simphony.statecharts.edit.parts.PseudoState3EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState4EditPart;
import repast.simphony.statecharts.edit.parts.State2EditPart;
import repast.simphony.statecharts.part.StatechartDiagramUpdater;
import repast.simphony.statecharts.part.StatechartNodeDescriptor;
import repast.simphony.statecharts.part.StatechartVisualIDRegistry;
import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * @generated
 */
public class CompositeStateCompositeStateCompartmentCanonicalEditPolicy extends CanonicalEditPolicy {

  /**
   * @generated
   */
  protected void refreshOnActivate() {
    // Need to activate editpart children before invoking the canonical refresh for EditParts to add event listeners
    List<?> c = getHost().getChildren();
    for (int i = 0; i < c.size(); i++) {
      ((EditPart) c.get(i)).activate();
    }
    super.refreshOnActivate();
  }

  /**
   * @generated
   */
  protected EStructuralFeature getFeatureToSynchronize() {
    return StatechartPackage.eINSTANCE.getCompositeState_Children();
  }

  /**
   * @generated
   */
  @SuppressWarnings("rawtypes")
  protected List getSemanticChildrenList() {
    View viewObject = (View) getHost().getModel();
    LinkedList<EObject> result = new LinkedList<EObject>();
    List<StatechartNodeDescriptor> childDescriptors = StatechartDiagramUpdater
        .getCompositeStateCompositeStateCompartment_7001SemanticChildren(viewObject);
    for (StatechartNodeDescriptor d : childDescriptors) {
      result.add(d.getModelElement());
    }
    return result;
  }

  /**
   * @generated
   */
  protected boolean isOrphaned(Collection<EObject> semanticChildren, final View view) {
    return isMyDiagramElement(view) && !semanticChildren.contains(view.getElement());
  }

  /**
   * @generated
   */
  private boolean isMyDiagramElement(View view) {
    int visualID = StatechartVisualIDRegistry.getVisualID(view);
    switch (visualID) {
    case State2EditPart.VISUAL_ID:
    case CompositeState2EditPart.VISUAL_ID:
    case PseudoState3EditPart.VISUAL_ID:
    case PseudoState4EditPart.VISUAL_ID:
    case FinalState2EditPart.VISUAL_ID:
    case HistoryEditPart.VISUAL_ID:
    case History2EditPart.VISUAL_ID:
      return true;
    }
    return false;
  }

  /**
   * @generated
   */
  protected void refreshSemantic() {
    if (resolveSemanticElement() == null) {
      return;
    }
    LinkedList<IAdaptable> createdViews = new LinkedList<IAdaptable>();
    List<StatechartNodeDescriptor> childDescriptors = StatechartDiagramUpdater
        .getCompositeStateCompositeStateCompartment_7001SemanticChildren((View) getHost()
            .getModel());
    LinkedList<View> orphaned = new LinkedList<View>();
    // we care to check only views we recognize as ours
    LinkedList<View> knownViewChildren = new LinkedList<View>();
    for (View v : getViewChildren()) {
      if (isMyDiagramElement(v)) {
        knownViewChildren.add(v);
      }
    }
    // alternative to #cleanCanonicalSemanticChildren(getViewChildren(), semanticChildren)
    HashMap<StatechartNodeDescriptor, LinkedList<View>> potentialViews = new HashMap<StatechartNodeDescriptor, LinkedList<View>>();
    //
    // iteration happens over list of desired semantic elements, trying to find best matching View, while original CEP
    // iterates views, potentially losing view (size/bounds) information - i.e. if there are few views to reference same EObject, only last one 
    // to answer isOrphaned == true will be used for the domain element representation, see #cleanCanonicalSemanticChildren()
    for (Iterator<StatechartNodeDescriptor> descriptorsIterator = childDescriptors.iterator(); descriptorsIterator
        .hasNext();) {
      StatechartNodeDescriptor next = descriptorsIterator.next();
      String hint = StatechartVisualIDRegistry.getType(next.getVisualID());
      LinkedList<View> perfectMatch = new LinkedList<View>(); // both semanticElement and hint match that of NodeDescriptor
      LinkedList<View> potentialMatch = new LinkedList<View>(); // semanticElement matches, hint does not
      for (View childView : getViewChildren()) {
        EObject semanticElement = childView.getElement();
        if (next.getModelElement().equals(semanticElement)) {
          if (hint.equals(childView.getType())) {
            perfectMatch.add(childView);
            // actually, can stop iteration over view children here, but
            // may want to use not the first view but last one as a 'real' match (the way original CEP does
            // with its trick with viewToSemanticMap inside #cleanCanonicalSemanticChildren
          } else {
            potentialMatch.add(childView);
          }
        }
      }
      if (perfectMatch.size() > 0) {
        descriptorsIterator.remove(); // precise match found no need to create anything for the NodeDescriptor
        // use only one view (first or last?), keep rest as orphaned for further consideration
        knownViewChildren.remove(perfectMatch.getFirst());
      } else if (potentialMatch.size() > 0) {
        potentialViews.put(next, potentialMatch);
      }
    }
    // those left in knownViewChildren are subject to removal - they are our diagram elements we didn't find match to,
    // or those we have potential matches to, and thus need to be recreated, preserving size/location information.
    orphaned.addAll(knownViewChildren);
    //
    CompositeTransactionalCommand boundsCommand = new CompositeTransactionalCommand(host()
        .getEditingDomain(), DiagramUIMessages.SetLocationCommand_Label_Resize);
    ArrayList<CreateViewRequest.ViewDescriptor> viewDescriptors = new ArrayList<CreateViewRequest.ViewDescriptor>(
        childDescriptors.size());
    for (StatechartNodeDescriptor next : childDescriptors) {
      String hint = StatechartVisualIDRegistry.getType(next.getVisualID());
      IAdaptable elementAdapter = new CanonicalElementAdapter(next.getModelElement(), hint);
      CreateViewRequest.ViewDescriptor descriptor = new CreateViewRequest.ViewDescriptor(
          elementAdapter, Node.class, hint, ViewUtil.APPEND, false, host()
              .getDiagramPreferencesHint());
      viewDescriptors.add(descriptor);

      LinkedList<View> possibleMatches = potentialViews.get(next);
      if (possibleMatches != null) {
        // from potential matches, leave those that were not eventually used for some other NodeDescriptor (i.e. those left as orphaned)
        possibleMatches.retainAll(knownViewChildren);
      }
      if (possibleMatches != null && !possibleMatches.isEmpty()) {
        View originalView = possibleMatches.getFirst();
        knownViewChildren.remove(originalView); // remove not to copy properties of the same view again and again
        // add command to copy properties
        if (originalView instanceof Node) {
          if (((Node) originalView).getLayoutConstraint() instanceof Bounds) {
            Bounds b = (Bounds) ((Node) originalView).getLayoutConstraint();
            boundsCommand.add(new SetBoundsCommand(boundsCommand.getEditingDomain(), boundsCommand
                .getLabel(), descriptor, new Rectangle(b.getX(), b.getY(), b.getWidth(), b
                .getHeight())));
          } else if (((Node) originalView).getLayoutConstraint() instanceof Location) {
            Location l = (Location) ((Node) originalView).getLayoutConstraint();
            boundsCommand.add(new SetBoundsCommand(boundsCommand.getEditingDomain(), boundsCommand
                .getLabel(), descriptor, new Point(l.getX(), l.getY())));
          } else if (((Node) originalView).getLayoutConstraint() instanceof Size) {
            Size s = (Size) ((Node) originalView).getLayoutConstraint();
            boundsCommand.add(new SetBoundsCommand(boundsCommand.getEditingDomain(), boundsCommand
                .getLabel(), descriptor, new Dimension(s.getWidth(), s.getHeight())));
          }
        }
      }
    }

    boolean changed = deleteViews(orphaned.iterator());
    //
    CreateViewRequest request = getCreateViewRequest(viewDescriptors);
    Command cmd = getCreateViewCommand(request);
    if (cmd != null && cmd.canExecute()) {
      SetViewMutabilityCommand.makeMutable(new EObjectAdapter(host().getNotationView())).execute();
      executeCommand(cmd);
      if (boundsCommand.canExecute()) {
        executeCommand(new ICommandProxy(boundsCommand.reduce()));
      }
      @SuppressWarnings("unchecked")
      List<IAdaptable> nl = (List<IAdaptable>) request.getNewObject();
      createdViews.addAll(nl);
    }
    if (changed || createdViews.size() > 0) {
      postProcessRefreshSemantic(createdViews);
    }
    if (createdViews.size() > 1) {
      // perform a layout of the container
      DeferredLayoutCommand layoutCmd = new DeferredLayoutCommand(host().getEditingDomain(),
          createdViews, host());
      executeCommand(new ICommandProxy(layoutCmd));
    }

    makeViewsImmutable(createdViews);
  }
}
