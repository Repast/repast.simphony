package repast.simphony.statecharts.navigator;

import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITreePathLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.ViewerLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;

import repast.simphony.statecharts.edit.parts.CompositeState2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateEditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateName2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateNameEditPart;
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
import repast.simphony.statecharts.edit.parts.StateName2EditPart;
import repast.simphony.statecharts.edit.parts.StateNameEditPart;
import repast.simphony.statecharts.edit.parts.TransitionEditPart;
import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;
import repast.simphony.statecharts.part.StatechartVisualIDRegistry;
import repast.simphony.statecharts.providers.StatechartElementTypes;
import repast.simphony.statecharts.providers.StatechartParserProvider;
import repast.simphony.statecharts.scmodel.FinalState;
import repast.simphony.statecharts.scmodel.History;
import repast.simphony.statecharts.scmodel.PseudoState;
import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.Transition;

/**
 * @generated
 */
public class StatechartNavigatorLabelProvider extends LabelProvider implements
    ICommonLabelProvider, ITreePathLabelProvider {

  /**
   * @generated
   */
  static {
    StatechartDiagramEditorPlugin.getInstance().getImageRegistry()
        .put("Navigator?UnknownElement", ImageDescriptor.getMissingImageDescriptor()); //$NON-NLS-1$
    StatechartDiagramEditorPlugin.getInstance().getImageRegistry()
        .put("Navigator?ImageNotFound", ImageDescriptor.getMissingImageDescriptor()); //$NON-NLS-1$
  }

  /**
   * @generated
   */
  public void updateLabel(ViewerLabel label, TreePath elementPath) {
    Object element = elementPath.getLastSegment();
    if (element instanceof StatechartNavigatorItem
        && !isOwnView(((StatechartNavigatorItem) element).getView())) {
      return;
    }
    label.setText(getText(element));
    label.setImage(getImage(element));
  }

  /**
   * @generated
   */
  public Image getImage(Object element) {
    if (element instanceof StatechartNavigatorGroup) {
      StatechartNavigatorGroup group = (StatechartNavigatorGroup) element;
      return StatechartDiagramEditorPlugin.getInstance().getBundledImage(group.getIcon());
    }

    if (element instanceof StatechartNavigatorItem) {
      StatechartNavigatorItem navigatorItem = (StatechartNavigatorItem) element;
      if (!isOwnView(navigatorItem.getView())) {
        return super.getImage(element);
      }
      return getImage(navigatorItem.getView());
    }

    return super.getImage(element);
  }

  /**
   * @generated
   */
  public Image getImage(View view) {
    switch (StatechartVisualIDRegistry.getVisualID(view)) {
    case TransitionEditPart.VISUAL_ID:
      return getImage(
          "Navigator?Link?http://repast.sf.net/statecharts?Transition", StatechartElementTypes.Transition_4001); //$NON-NLS-1$
    case FinalStateEditPart.VISUAL_ID:
      return getImage(
          "Navigator?TopLevelNode?http://repast.sf.net/statecharts?FinalState", StatechartElementTypes.FinalState_2008); //$NON-NLS-1$
    case CompositeState2EditPart.VISUAL_ID:
      return getImage(
          "Navigator?Node?http://repast.sf.net/statecharts?CompositeState", StatechartElementTypes.CompositeState_3002); //$NON-NLS-1$
    case FinalState2EditPart.VISUAL_ID:
      return getImage(
          "Navigator?Node?http://repast.sf.net/statecharts?FinalState", StatechartElementTypes.FinalState_3007); //$NON-NLS-1$
    case PseudoState4EditPart.VISUAL_ID:
      return getImage(
          "Navigator?Node?http://repast.sf.net/statecharts?PseudoState", StatechartElementTypes.PseudoState_3006); //$NON-NLS-1$
    case CompositeStateEditPart.VISUAL_ID:
      return getImage(
          "Navigator?TopLevelNode?http://repast.sf.net/statecharts?CompositeState", StatechartElementTypes.CompositeState_2004); //$NON-NLS-1$
    case PseudoState3EditPart.VISUAL_ID:
      return getImage(
          "Navigator?Node?http://repast.sf.net/statecharts?PseudoState", StatechartElementTypes.PseudoState_3003); //$NON-NLS-1$
    case PseudoState5EditPart.VISUAL_ID:
      return getImage(
          "Navigator?TopLevelNode?http://repast.sf.net/statecharts?PseudoState", StatechartElementTypes.PseudoState_2007); //$NON-NLS-1$
    case State2EditPart.VISUAL_ID:
      return getImage(
          "Navigator?Node?http://repast.sf.net/statecharts?State", StatechartElementTypes.State_3001); //$NON-NLS-1$
    case StateMachineEditPart.VISUAL_ID:
      return getImage(
          "Navigator?Diagram?http://repast.sf.net/statecharts?StateMachine", StatechartElementTypes.StateMachine_1000); //$NON-NLS-1$
    case HistoryEditPart.VISUAL_ID:
      return getImage(
          "Navigator?Node?http://repast.sf.net/statecharts?History", StatechartElementTypes.History_3008); //$NON-NLS-1$
    case PseudoState2EditPart.VISUAL_ID:
      return getImage(
          "Navigator?TopLevelNode?http://repast.sf.net/statecharts?PseudoState", StatechartElementTypes.PseudoState_2006); //$NON-NLS-1$
    case PseudoStateEditPart.VISUAL_ID:
      return getImage(
          "Navigator?TopLevelNode?http://repast.sf.net/statecharts?PseudoState", StatechartElementTypes.PseudoState_2005); //$NON-NLS-1$
    case History2EditPart.VISUAL_ID:
      return getImage(
          "Navigator?Node?http://repast.sf.net/statecharts?History", StatechartElementTypes.History_3009); //$NON-NLS-1$
    case StateEditPart.VISUAL_ID:
      return getImage(
          "Navigator?TopLevelNode?http://repast.sf.net/statecharts?State", StatechartElementTypes.State_2003); //$NON-NLS-1$
    }
    return getImage("Navigator?UnknownElement", null); //$NON-NLS-1$
  }

  /**
   * @generated
   */
  private Image getImage(String key, IElementType elementType) {
    ImageRegistry imageRegistry = StatechartDiagramEditorPlugin.getInstance().getImageRegistry();
    Image image = imageRegistry.get(key);
    if (image == null && elementType != null
        && StatechartElementTypes.isKnownElementType(elementType)) {
      image = StatechartElementTypes.getImage(elementType);
      imageRegistry.put(key, image);
    }

    if (image == null) {
      image = imageRegistry.get("Navigator?ImageNotFound"); //$NON-NLS-1$
      imageRegistry.put(key, image);
    }
    return image;
  }

  /**
   * @generated
   */
  public String getText(Object element) {
    if (element instanceof StatechartNavigatorGroup) {
      StatechartNavigatorGroup group = (StatechartNavigatorGroup) element;
      return group.getGroupName();
    }

    if (element instanceof StatechartNavigatorItem) {
      StatechartNavigatorItem navigatorItem = (StatechartNavigatorItem) element;
      if (!isOwnView(navigatorItem.getView())) {
        return null;
      }
      return getText(navigatorItem.getView());
    }

    return super.getText(element);
  }

  /**
   * @generated
   */
  public String getText(View view) {
    if (view.getElement() != null && view.getElement().eIsProxy()) {
      return getUnresolvedDomainElementProxyText(view);
    }
    switch (StatechartVisualIDRegistry.getVisualID(view)) {
    case TransitionEditPart.VISUAL_ID:
      return getTransition_4001Text(view);
    case FinalStateEditPart.VISUAL_ID:
      return getFinalState_2008Text(view);
    case CompositeState2EditPart.VISUAL_ID:
      return getCompositeState_3002Text(view);
    case FinalState2EditPart.VISUAL_ID:
      return getFinalState_3007Text(view);
    case PseudoState4EditPart.VISUAL_ID:
      return getPseudoState_3006Text(view);
    case CompositeStateEditPart.VISUAL_ID:
      return getCompositeState_2004Text(view);
    case PseudoState3EditPart.VISUAL_ID:
      return getPseudoState_3003Text(view);
    case PseudoState5EditPart.VISUAL_ID:
      return getPseudoState_2007Text(view);
    case State2EditPart.VISUAL_ID:
      return getState_3001Text(view);
    case StateMachineEditPart.VISUAL_ID:
      return getStateMachine_1000Text(view);
    case HistoryEditPart.VISUAL_ID:
      return getHistory_3008Text(view);
    case PseudoState2EditPart.VISUAL_ID:
      return getPseudoState_2006Text(view);
    case PseudoStateEditPart.VISUAL_ID:
      return getPseudoState_2005Text(view);
    case History2EditPart.VISUAL_ID:
      return getHistory_3009Text(view);
    case StateEditPart.VISUAL_ID:
      return getState_2003Text(view);
    }
    return getUnknownElementText(view);
  }

  /**
   * @generated
   */
  private String getPseudoState_3006Text(View view) {
    PseudoState domainModelElement = (PseudoState) view.getElement();
    if (domainModelElement != null) {
      return domainModelElement.getId();
    } else {
      StatechartDiagramEditorPlugin.getInstance().logError(
          "No domain element for view with visualID = " + 3006); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getTransition_4001Text(View view) {
    Transition domainModelElement = (Transition) view.getElement();
    if (domainModelElement != null) {
      return String.valueOf(domainModelElement.getPriority());
    } else {
      StatechartDiagramEditorPlugin.getInstance().logError(
          "No domain element for view with visualID = " + 4001); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getCompositeState_3002Text(View view) {
    IParser parser = StatechartParserProvider.getParser(StatechartElementTypes.CompositeState_3002,
        view.getElement() != null ? view.getElement() : view,
        StatechartVisualIDRegistry.getType(CompositeStateName2EditPart.VISUAL_ID));
    if (parser != null) {
      return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement()
          : view), ParserOptions.NONE.intValue());
    } else {
      StatechartDiagramEditorPlugin.getInstance()
          .logError("Parser was not found for label " + 5003); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getHistory_3008Text(View view) {
    History domainModelElement = (History) view.getElement();
    if (domainModelElement != null) {
      return domainModelElement.getId();
    } else {
      StatechartDiagramEditorPlugin.getInstance().logError(
          "No domain element for view with visualID = " + 3008); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getState_2003Text(View view) {
    IParser parser = StatechartParserProvider.getParser(StatechartElementTypes.State_2003,
        view.getElement() != null ? view.getElement() : view,
        StatechartVisualIDRegistry.getType(StateNameEditPart.VISUAL_ID));
    if (parser != null) {
      return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement()
          : view), ParserOptions.NONE.intValue());
    } else {
      StatechartDiagramEditorPlugin.getInstance()
          .logError("Parser was not found for label " + 5001); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getPseudoState_3003Text(View view) {
    PseudoState domainModelElement = (PseudoState) view.getElement();
    if (domainModelElement != null) {
      return domainModelElement.getId();
    } else {
      StatechartDiagramEditorPlugin.getInstance().logError(
          "No domain element for view with visualID = " + 3003); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getPseudoState_2007Text(View view) {
    PseudoState domainModelElement = (PseudoState) view.getElement();
    if (domainModelElement != null) {
      return domainModelElement.getId();
    } else {
      StatechartDiagramEditorPlugin.getInstance().logError(
          "No domain element for view with visualID = " + 2007); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getState_3001Text(View view) {
    IParser parser = StatechartParserProvider.getParser(StatechartElementTypes.State_3001,
        view.getElement() != null ? view.getElement() : view,
        StatechartVisualIDRegistry.getType(StateName2EditPart.VISUAL_ID));
    if (parser != null) {
      return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement()
          : view), ParserOptions.NONE.intValue());
    } else {
      StatechartDiagramEditorPlugin.getInstance()
          .logError("Parser was not found for label " + 5002); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getFinalState_2008Text(View view) {
    FinalState domainModelElement = (FinalState) view.getElement();
    if (domainModelElement != null) {
      return domainModelElement.getId();
    } else {
      StatechartDiagramEditorPlugin.getInstance().logError(
          "No domain element for view with visualID = " + 2008); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getPseudoState_2005Text(View view) {
    PseudoState domainModelElement = (PseudoState) view.getElement();
    if (domainModelElement != null) {
      return domainModelElement.getId();
    } else {
      StatechartDiagramEditorPlugin.getInstance().logError(
          "No domain element for view with visualID = " + 2005); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getStateMachine_1000Text(View view) {
    StateMachine domainModelElement = (StateMachine) view.getElement();
    if (domainModelElement != null) {
      return domainModelElement.getClassName();
    } else {
      StatechartDiagramEditorPlugin.getInstance().logError(
          "No domain element for view with visualID = " + 1000); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getHistory_3009Text(View view) {
    History domainModelElement = (History) view.getElement();
    if (domainModelElement != null) {
      return domainModelElement.getId();
    } else {
      StatechartDiagramEditorPlugin.getInstance().logError(
          "No domain element for view with visualID = " + 3009); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getPseudoState_2006Text(View view) {
    PseudoState domainModelElement = (PseudoState) view.getElement();
    if (domainModelElement != null) {
      return domainModelElement.getId();
    } else {
      StatechartDiagramEditorPlugin.getInstance().logError(
          "No domain element for view with visualID = " + 2006); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getCompositeState_2004Text(View view) {
    IParser parser = StatechartParserProvider.getParser(StatechartElementTypes.CompositeState_2004,
        view.getElement() != null ? view.getElement() : view,
        StatechartVisualIDRegistry.getType(CompositeStateNameEditPart.VISUAL_ID));
    if (parser != null) {
      return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement()
          : view), ParserOptions.NONE.intValue());
    } else {
      StatechartDiagramEditorPlugin.getInstance()
          .logError("Parser was not found for label " + 5004); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getFinalState_3007Text(View view) {
    FinalState domainModelElement = (FinalState) view.getElement();
    if (domainModelElement != null) {
      return domainModelElement.getId();
    } else {
      StatechartDiagramEditorPlugin.getInstance().logError(
          "No domain element for view with visualID = " + 3007); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getUnknownElementText(View view) {
    return "<UnknownElement Visual_ID = " + view.getType() + ">"; //$NON-NLS-1$  //$NON-NLS-2$
  }

  /**
   * @generated
   */
  private String getUnresolvedDomainElementProxyText(View view) {
    return "<Unresolved domain element Visual_ID = " + view.getType() + ">"; //$NON-NLS-1$  //$NON-NLS-2$
  }

  /**
   * @generated
   */
  public void init(ICommonContentExtensionSite aConfig) {
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
  public String getDescription(Object anElement) {
    return null;
  }

  /**
   * @generated
   */
  private boolean isOwnView(View view) {
    return StateMachineEditPart.MODEL_ID.equals(StatechartVisualIDRegistry.getModelID(view));
  }

}
