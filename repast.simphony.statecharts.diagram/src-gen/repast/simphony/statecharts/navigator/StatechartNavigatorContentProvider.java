package repast.simphony.statecharts.navigator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentProvider;

import repast.simphony.statecharts.edit.parts.CompositeState2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateCompositeStateCompartment2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateCompositeStateCompartmentEditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateEditPart;
import repast.simphony.statecharts.edit.parts.FinalState2EditPart;
import repast.simphony.statecharts.edit.parts.FinalStateEditPart;
import repast.simphony.statecharts.edit.parts.History2EditPart;
import repast.simphony.statecharts.edit.parts.HistoryEditPart;
import repast.simphony.statecharts.edit.parts.PseudoState2EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState3EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState4EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState5EditPart;
import repast.simphony.statecharts.edit.parts.PseudoStateEditPart;
import repast.simphony.statecharts.edit.parts.State2EditPart;
import repast.simphony.statecharts.edit.parts.StateEditPart;
import repast.simphony.statecharts.edit.parts.StateMachineEditPart;
import repast.simphony.statecharts.edit.parts.TransitionEditPart;
import repast.simphony.statecharts.part.Messages;
import repast.simphony.statecharts.part.StatechartVisualIDRegistry;

/**
 * @generated
 */
public class StatechartNavigatorContentProvider implements ICommonContentProvider {

  /**
   * @generated
   */
  private static final Object[] EMPTY_ARRAY = new Object[0];

  /**
   * @generated
   */
  private Viewer myViewer;

  /**
   * @generated
   */
  private AdapterFactoryEditingDomain myEditingDomain;

  /**
   * @generated
   */
  private WorkspaceSynchronizer myWorkspaceSynchronizer;

  /**
   * @generated
   */
  private Runnable myViewerRefreshRunnable;

  /**
   * @generated
   */
  @SuppressWarnings({ "unchecked", "serial", "rawtypes" })
  public StatechartNavigatorContentProvider() {
    TransactionalEditingDomain editingDomain = GMFEditingDomainFactory.INSTANCE
        .createEditingDomain();
    myEditingDomain = (AdapterFactoryEditingDomain) editingDomain;
    myEditingDomain.setResourceToReadOnlyMap(new HashMap() {
      public Object get(Object key) {
        if (!containsKey(key)) {
          put(key, Boolean.TRUE);
        }
        return super.get(key);
      }
    });
    myViewerRefreshRunnable = new Runnable() {
      public void run() {
        if (myViewer != null) {
          myViewer.refresh();
        }
      }
    };
    myWorkspaceSynchronizer = new WorkspaceSynchronizer(editingDomain,
        new WorkspaceSynchronizer.Delegate() {
          public void dispose() {
          }

          public boolean handleResourceChanged(final Resource resource) {
            unloadAllResources();
            asyncRefresh();
            return true;
          }

          public boolean handleResourceDeleted(Resource resource) {
            unloadAllResources();
            asyncRefresh();
            return true;
          }

          public boolean handleResourceMoved(Resource resource, final URI newURI) {
            unloadAllResources();
            asyncRefresh();
            return true;
          }
        });
  }

  /**
   * @generated
   */
  public void dispose() {
    myWorkspaceSynchronizer.dispose();
    myWorkspaceSynchronizer = null;
    myViewerRefreshRunnable = null;
    myViewer = null;
    unloadAllResources();
    ((TransactionalEditingDomain) myEditingDomain).dispose();
    myEditingDomain = null;
  }

  /**
   * @generated
   */
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    myViewer = viewer;
  }

  /**
   * @generated
   */
  void unloadAllResources() {
    for (Resource nextResource : myEditingDomain.getResourceSet().getResources()) {
      nextResource.unload();
    }
  }

  /**
   * @generated
   */
  void asyncRefresh() {
    if (myViewer != null && !myViewer.getControl().isDisposed()) {
      myViewer.getControl().getDisplay().asyncExec(myViewerRefreshRunnable);
    }
  }

  /**
   * @generated
   */
  public Object[] getElements(Object inputElement) {
    return getChildren(inputElement);
  }

  /**
   * @generated
   */
  public void restoreState(IMemento aMemento) {
  }

  /**
   * @generated
   */
  public void saveState(IMemento aMemento) {
  }

  /**
   * @generated
   */
  public void init(ICommonContentExtensionSite aConfig) {
  }

  /**
   * @generated
   */
  public Object[] getChildren(Object parentElement) {
    if (parentElement instanceof IFile) {
      IFile file = (IFile) parentElement;
      URI fileURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
      Resource resource = myEditingDomain.getResourceSet().getResource(fileURI, true);
      ArrayList<StatechartNavigatorItem> result = new ArrayList<StatechartNavigatorItem>();
      ArrayList<View> topViews = new ArrayList<View>(resource.getContents().size());
      for (EObject o : resource.getContents()) {
        if (o instanceof View) {
          topViews.add((View) o);
        }
      }
      result.addAll(createNavigatorItems(
          selectViewsByType(topViews, StateMachineEditPart.MODEL_ID), file, false));
      return result.toArray();
    }

    if (parentElement instanceof StatechartNavigatorGroup) {
      StatechartNavigatorGroup group = (StatechartNavigatorGroup) parentElement;
      return group.getChildren();
    }

    if (parentElement instanceof StatechartNavigatorItem) {
      StatechartNavigatorItem navigatorItem = (StatechartNavigatorItem) parentElement;
      if (navigatorItem.isLeaf() || !isOwnView(navigatorItem.getView())) {
        return EMPTY_ARRAY;
      }
      return getViewChildren(navigatorItem.getView(), parentElement);
    }

    return EMPTY_ARRAY;
  }

  /**
   * @generated
   */
  private Object[] getViewChildren(View view, Object parentElement) {
    switch (StatechartVisualIDRegistry.getVisualID(view)) {

    case PseudoState2EditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Node sv = (Node) view;
      StatechartNavigatorGroup incominglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_PseudoState_2006_incominglinks,
          "icons/incomingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      StatechartNavigatorGroup outgoinglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_PseudoState_2006_outgoinglinks,
          "icons/outgoingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getIncomingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
      connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
      if (!incominglinks.isEmpty()) {
        result.add(incominglinks);
      }
      if (!outgoinglinks.isEmpty()) {
        result.add(outgoinglinks);
      }
      return result.toArray();
    }

    case TransitionEditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Edge sv = (Edge) view;
      StatechartNavigatorGroup target = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_Transition_4001_target,
          "icons/linkTargetNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      StatechartNavigatorGroup source = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_Transition_4001_source,
          "icons/linkSourceNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getLinksTargetByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(StateEditPart.VISUAL_ID));
      target.addChildren(createNavigatorItems(connectedViews, target, true));
      connectedViews = getLinksTargetByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(CompositeStateEditPart.VISUAL_ID));
      target.addChildren(createNavigatorItems(connectedViews, target, true));
      connectedViews = getLinksTargetByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(PseudoStateEditPart.VISUAL_ID));
      target.addChildren(createNavigatorItems(connectedViews, target, true));
      connectedViews = getLinksTargetByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(PseudoState2EditPart.VISUAL_ID));
      target.addChildren(createNavigatorItems(connectedViews, target, true));
      connectedViews = getLinksTargetByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(PseudoState5EditPart.VISUAL_ID));
      target.addChildren(createNavigatorItems(connectedViews, target, true));
      connectedViews = getLinksTargetByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(FinalStateEditPart.VISUAL_ID));
      target.addChildren(createNavigatorItems(connectedViews, target, true));
      connectedViews = getLinksTargetByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(State2EditPart.VISUAL_ID));
      target.addChildren(createNavigatorItems(connectedViews, target, true));
      connectedViews = getLinksTargetByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(CompositeState2EditPart.VISUAL_ID));
      target.addChildren(createNavigatorItems(connectedViews, target, true));
      connectedViews = getLinksTargetByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(PseudoState3EditPart.VISUAL_ID));
      target.addChildren(createNavigatorItems(connectedViews, target, true));
      connectedViews = getLinksTargetByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(PseudoState4EditPart.VISUAL_ID));
      target.addChildren(createNavigatorItems(connectedViews, target, true));
      connectedViews = getLinksTargetByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(FinalState2EditPart.VISUAL_ID));
      target.addChildren(createNavigatorItems(connectedViews, target, true));
      connectedViews = getLinksTargetByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(HistoryEditPart.VISUAL_ID));
      target.addChildren(createNavigatorItems(connectedViews, target, true));
      connectedViews = getLinksTargetByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(History2EditPart.VISUAL_ID));
      target.addChildren(createNavigatorItems(connectedViews, target, true));
      connectedViews = getLinksSourceByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(StateEditPart.VISUAL_ID));
      source.addChildren(createNavigatorItems(connectedViews, source, true));
      connectedViews = getLinksSourceByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(CompositeStateEditPart.VISUAL_ID));
      source.addChildren(createNavigatorItems(connectedViews, source, true));
      connectedViews = getLinksSourceByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(PseudoStateEditPart.VISUAL_ID));
      source.addChildren(createNavigatorItems(connectedViews, source, true));
      connectedViews = getLinksSourceByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(PseudoState2EditPart.VISUAL_ID));
      source.addChildren(createNavigatorItems(connectedViews, source, true));
      connectedViews = getLinksSourceByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(PseudoState5EditPart.VISUAL_ID));
      source.addChildren(createNavigatorItems(connectedViews, source, true));
      connectedViews = getLinksSourceByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(FinalStateEditPart.VISUAL_ID));
      source.addChildren(createNavigatorItems(connectedViews, source, true));
      connectedViews = getLinksSourceByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(State2EditPart.VISUAL_ID));
      source.addChildren(createNavigatorItems(connectedViews, source, true));
      connectedViews = getLinksSourceByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(CompositeState2EditPart.VISUAL_ID));
      source.addChildren(createNavigatorItems(connectedViews, source, true));
      connectedViews = getLinksSourceByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(PseudoState3EditPart.VISUAL_ID));
      source.addChildren(createNavigatorItems(connectedViews, source, true));
      connectedViews = getLinksSourceByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(PseudoState4EditPart.VISUAL_ID));
      source.addChildren(createNavigatorItems(connectedViews, source, true));
      connectedViews = getLinksSourceByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(FinalState2EditPart.VISUAL_ID));
      source.addChildren(createNavigatorItems(connectedViews, source, true));
      connectedViews = getLinksSourceByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(HistoryEditPart.VISUAL_ID));
      source.addChildren(createNavigatorItems(connectedViews, source, true));
      connectedViews = getLinksSourceByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(History2EditPart.VISUAL_ID));
      source.addChildren(createNavigatorItems(connectedViews, source, true));
      if (!target.isEmpty()) {
        result.add(target);
      }
      if (!source.isEmpty()) {
        result.add(source);
      }
      return result.toArray();
    }

    case PseudoState3EditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Node sv = (Node) view;
      StatechartNavigatorGroup incominglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_PseudoState_3003_incominglinks,
          "icons/incomingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      StatechartNavigatorGroup outgoinglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_PseudoState_3003_outgoinglinks,
          "icons/outgoingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getIncomingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
      connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
      if (!incominglinks.isEmpty()) {
        result.add(incominglinks);
      }
      if (!outgoinglinks.isEmpty()) {
        result.add(outgoinglinks);
      }
      return result.toArray();
    }

    case State2EditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Node sv = (Node) view;
      StatechartNavigatorGroup incominglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_State_3001_incominglinks,
          "icons/incomingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      StatechartNavigatorGroup outgoinglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_State_3001_outgoinglinks,
          "icons/outgoingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getIncomingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
      connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
      if (!incominglinks.isEmpty()) {
        result.add(incominglinks);
      }
      if (!outgoinglinks.isEmpty()) {
        result.add(outgoinglinks);
      }
      return result.toArray();
    }

    case StateMachineEditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Diagram sv = (Diagram) view;
      StatechartNavigatorGroup links = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_StateMachine_1000_links,
          "icons/linksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(StateEditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(CompositeStateEditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(PseudoStateEditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(PseudoState2EditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(PseudoState5EditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(FinalStateEditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getDiagramLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      links.addChildren(createNavigatorItems(connectedViews, links, false));
      if (!links.isEmpty()) {
        result.add(links);
      }
      return result.toArray();
    }

    case PseudoStateEditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Node sv = (Node) view;
      StatechartNavigatorGroup incominglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_PseudoState_2005_incominglinks,
          "icons/incomingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      StatechartNavigatorGroup outgoinglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_PseudoState_2005_outgoinglinks,
          "icons/outgoingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getIncomingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
      connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
      if (!incominglinks.isEmpty()) {
        result.add(incominglinks);
      }
      if (!outgoinglinks.isEmpty()) {
        result.add(outgoinglinks);
      }
      return result.toArray();
    }

    case FinalStateEditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Node sv = (Node) view;
      StatechartNavigatorGroup incominglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_FinalState_2008_incominglinks,
          "icons/incomingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      StatechartNavigatorGroup outgoinglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_FinalState_2008_outgoinglinks,
          "icons/outgoingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getIncomingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
      connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
      if (!incominglinks.isEmpty()) {
        result.add(incominglinks);
      }
      if (!outgoinglinks.isEmpty()) {
        result.add(outgoinglinks);
      }
      return result.toArray();
    }

    case PseudoState5EditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Node sv = (Node) view;
      StatechartNavigatorGroup incominglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_PseudoState_2007_incominglinks,
          "icons/incomingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      StatechartNavigatorGroup outgoinglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_PseudoState_2007_outgoinglinks,
          "icons/outgoingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getIncomingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
      connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
      if (!incominglinks.isEmpty()) {
        result.add(incominglinks);
      }
      if (!outgoinglinks.isEmpty()) {
        result.add(outgoinglinks);
      }
      return result.toArray();
    }

    case History2EditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Node sv = (Node) view;
      StatechartNavigatorGroup incominglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_History_3009_incominglinks,
          "icons/incomingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      StatechartNavigatorGroup outgoinglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_History_3009_outgoinglinks,
          "icons/outgoingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getIncomingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
      connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
      if (!incominglinks.isEmpty()) {
        result.add(incominglinks);
      }
      if (!outgoinglinks.isEmpty()) {
        result.add(outgoinglinks);
      }
      return result.toArray();
    }

    case HistoryEditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Node sv = (Node) view;
      StatechartNavigatorGroup incominglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_History_3008_incominglinks,
          "icons/incomingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      StatechartNavigatorGroup outgoinglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_History_3008_outgoinglinks,
          "icons/outgoingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getIncomingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
      connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
      if (!incominglinks.isEmpty()) {
        result.add(incominglinks);
      }
      if (!outgoinglinks.isEmpty()) {
        result.add(outgoinglinks);
      }
      return result.toArray();
    }

    case CompositeState2EditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Node sv = (Node) view;
      StatechartNavigatorGroup incominglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_CompositeState_3002_incominglinks,
          "icons/incomingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      StatechartNavigatorGroup outgoinglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_CompositeState_3002_outgoinglinks,
          "icons/outgoingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry
              .getType(CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID));
      connectedViews = getChildrenByType(connectedViews,
          StatechartVisualIDRegistry.getType(State2EditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry
              .getType(CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID));
      connectedViews = getChildrenByType(connectedViews,
          StatechartVisualIDRegistry.getType(CompositeState2EditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry
              .getType(CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID));
      connectedViews = getChildrenByType(connectedViews,
          StatechartVisualIDRegistry.getType(PseudoState3EditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry
              .getType(CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID));
      connectedViews = getChildrenByType(connectedViews,
          StatechartVisualIDRegistry.getType(PseudoState4EditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry
              .getType(CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID));
      connectedViews = getChildrenByType(connectedViews,
          StatechartVisualIDRegistry.getType(FinalState2EditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry
              .getType(CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID));
      connectedViews = getChildrenByType(connectedViews,
          StatechartVisualIDRegistry.getType(HistoryEditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry
              .getType(CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID));
      connectedViews = getChildrenByType(connectedViews,
          StatechartVisualIDRegistry.getType(History2EditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getIncomingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
      connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
      if (!incominglinks.isEmpty()) {
        result.add(incominglinks);
      }
      if (!outgoinglinks.isEmpty()) {
        result.add(outgoinglinks);
      }
      return result.toArray();
    }

    case StateEditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Node sv = (Node) view;
      StatechartNavigatorGroup incominglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_State_2003_incominglinks,
          "icons/incomingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      StatechartNavigatorGroup outgoinglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_State_2003_outgoinglinks,
          "icons/outgoingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getIncomingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
      connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
      if (!incominglinks.isEmpty()) {
        result.add(incominglinks);
      }
      if (!outgoinglinks.isEmpty()) {
        result.add(outgoinglinks);
      }
      return result.toArray();
    }

    case PseudoState4EditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Node sv = (Node) view;
      StatechartNavigatorGroup incominglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_PseudoState_3006_incominglinks,
          "icons/incomingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      StatechartNavigatorGroup outgoinglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_PseudoState_3006_outgoinglinks,
          "icons/outgoingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getIncomingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
      connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
      if (!incominglinks.isEmpty()) {
        result.add(incominglinks);
      }
      if (!outgoinglinks.isEmpty()) {
        result.add(outgoinglinks);
      }
      return result.toArray();
    }

    case FinalState2EditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Node sv = (Node) view;
      StatechartNavigatorGroup incominglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_FinalState_3007_incominglinks,
          "icons/incomingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      StatechartNavigatorGroup outgoinglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_FinalState_3007_outgoinglinks,
          "icons/outgoingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getIncomingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
      connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
      if (!incominglinks.isEmpty()) {
        result.add(incominglinks);
      }
      if (!outgoinglinks.isEmpty()) {
        result.add(outgoinglinks);
      }
      return result.toArray();
    }

    case CompositeStateEditPart.VISUAL_ID: {
      LinkedList<StatechartAbstractNavigatorItem> result = new LinkedList<StatechartAbstractNavigatorItem>();
      Node sv = (Node) view;
      StatechartNavigatorGroup incominglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_CompositeState_2004_incominglinks,
          "icons/incomingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      StatechartNavigatorGroup outgoinglinks = new StatechartNavigatorGroup(
          Messages.NavigatorGroupName_CompositeState_2004_outgoinglinks,
          "icons/outgoingLinksNavigatorGroup.gif", parentElement); //$NON-NLS-1$
      Collection<View> connectedViews;
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry
              .getType(CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID));
      connectedViews = getChildrenByType(connectedViews,
          StatechartVisualIDRegistry.getType(State2EditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry
              .getType(CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID));
      connectedViews = getChildrenByType(connectedViews,
          StatechartVisualIDRegistry.getType(CompositeState2EditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry
              .getType(CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID));
      connectedViews = getChildrenByType(connectedViews,
          StatechartVisualIDRegistry.getType(PseudoState3EditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry
              .getType(CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID));
      connectedViews = getChildrenByType(connectedViews,
          StatechartVisualIDRegistry.getType(PseudoState4EditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry
              .getType(CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID));
      connectedViews = getChildrenByType(connectedViews,
          StatechartVisualIDRegistry.getType(FinalState2EditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry
              .getType(CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID));
      connectedViews = getChildrenByType(connectedViews,
          StatechartVisualIDRegistry.getType(HistoryEditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getChildrenByType(Collections.singleton(sv),
          StatechartVisualIDRegistry
              .getType(CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID));
      connectedViews = getChildrenByType(connectedViews,
          StatechartVisualIDRegistry.getType(History2EditPart.VISUAL_ID));
      result.addAll(createNavigatorItems(connectedViews, parentElement, false));
      connectedViews = getIncomingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      incominglinks.addChildren(createNavigatorItems(connectedViews, incominglinks, true));
      connectedViews = getOutgoingLinksByType(Collections.singleton(sv),
          StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
      outgoinglinks.addChildren(createNavigatorItems(connectedViews, outgoinglinks, true));
      if (!incominglinks.isEmpty()) {
        result.add(incominglinks);
      }
      if (!outgoinglinks.isEmpty()) {
        result.add(outgoinglinks);
      }
      return result.toArray();
    }
    }
    return EMPTY_ARRAY;
  }

  /**
   * @generated
   */
  private Collection<View> getLinksSourceByType(Collection<Edge> edges, String type) {
    LinkedList<View> result = new LinkedList<View>();
    for (Edge nextEdge : edges) {
      View nextEdgeSource = nextEdge.getSource();
      if (type.equals(nextEdgeSource.getType()) && isOwnView(nextEdgeSource)) {
        result.add(nextEdgeSource);
      }
    }
    return result;
  }

  /**
   * @generated
   */
  private Collection<View> getLinksTargetByType(Collection<Edge> edges, String type) {
    LinkedList<View> result = new LinkedList<View>();
    for (Edge nextEdge : edges) {
      View nextEdgeTarget = nextEdge.getTarget();
      if (type.equals(nextEdgeTarget.getType()) && isOwnView(nextEdgeTarget)) {
        result.add(nextEdgeTarget);
      }
    }
    return result;
  }

  /**
   * @generated
   */
  private Collection<View> getOutgoingLinksByType(Collection<? extends View> nodes, String type) {
    LinkedList<View> result = new LinkedList<View>();
    for (View nextNode : nodes) {
      result.addAll(selectViewsByType(nextNode.getSourceEdges(), type));
    }
    return result;
  }

  /**
   * @generated
   */
  private Collection<View> getIncomingLinksByType(Collection<? extends View> nodes, String type) {
    LinkedList<View> result = new LinkedList<View>();
    for (View nextNode : nodes) {
      result.addAll(selectViewsByType(nextNode.getTargetEdges(), type));
    }
    return result;
  }

  /**
   * @generated
   */
  private Collection<View> getChildrenByType(Collection<? extends View> nodes, String type) {
    LinkedList<View> result = new LinkedList<View>();
    for (View nextNode : nodes) {
      result.addAll(selectViewsByType(nextNode.getChildren(), type));
    }
    return result;
  }

  /**
   * @generated
   */
  private Collection<View> getDiagramLinksByType(Collection<Diagram> diagrams, String type) {
    ArrayList<View> result = new ArrayList<View>();
    for (Diagram nextDiagram : diagrams) {
      result.addAll(selectViewsByType(nextDiagram.getEdges(), type));
    }
    return result;
  }

  // TODO refactor as static method
  /**
   * @generated
   */
  private Collection<View> selectViewsByType(Collection<View> views, String type) {
    ArrayList<View> result = new ArrayList<View>();
    for (View nextView : views) {
      if (type.equals(nextView.getType()) && isOwnView(nextView)) {
        result.add(nextView);
      }
    }
    return result;
  }

  /**
   * @generated
   */
  private boolean isOwnView(View view) {
    return StateMachineEditPart.MODEL_ID.equals(StatechartVisualIDRegistry.getModelID(view));
  }

  /**
   * @generated
   */
  private Collection<StatechartNavigatorItem> createNavigatorItems(Collection<View> views,
      Object parent, boolean isLeafs) {
    ArrayList<StatechartNavigatorItem> result = new ArrayList<StatechartNavigatorItem>(views.size());
    for (View nextView : views) {
      result.add(new StatechartNavigatorItem(nextView, parent, isLeafs));
    }
    return result;
  }

  /**
   * @generated
   */
  public Object getParent(Object element) {
    if (element instanceof StatechartAbstractNavigatorItem) {
      StatechartAbstractNavigatorItem abstractNavigatorItem = (StatechartAbstractNavigatorItem) element;
      return abstractNavigatorItem.getParent();
    }
    return null;
  }

  /**
   * @generated
   */
  public boolean hasChildren(Object element) {
    return element instanceof IFile || getChildren(element).length > 0;
  }

}
