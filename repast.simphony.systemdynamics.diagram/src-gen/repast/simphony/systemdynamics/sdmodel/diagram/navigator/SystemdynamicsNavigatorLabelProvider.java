package repast.simphony.systemdynamics.sdmodel.diagram.navigator;

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

import repast.simphony.systemdynamics.sdmodel.CausalLink;
import repast.simphony.systemdynamics.sdmodel.Cloud;
import repast.simphony.systemdynamics.sdmodel.Rate;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.CausalLinkEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.CloudEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.RateEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.RateNameEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.StockEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.StockNameEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.SystemModelEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.VariableEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.edit.parts.VariableNameEditPart;
import repast.simphony.systemdynamics.sdmodel.diagram.part.SystemdynamicsDiagramEditorPlugin;
import repast.simphony.systemdynamics.sdmodel.diagram.part.SystemdynamicsVisualIDRegistry;
import repast.simphony.systemdynamics.sdmodel.diagram.providers.SystemdynamicsElementTypes;
import repast.simphony.systemdynamics.sdmodel.diagram.providers.SystemdynamicsParserProvider;

/**
 * @generated
 */
public class SystemdynamicsNavigatorLabelProvider extends LabelProvider implements
    ICommonLabelProvider, ITreePathLabelProvider {

  /**
   * @generated
   */
  static {
    SystemdynamicsDiagramEditorPlugin.getInstance().getImageRegistry()
        .put("Navigator?UnknownElement", ImageDescriptor.getMissingImageDescriptor()); //$NON-NLS-1$
    SystemdynamicsDiagramEditorPlugin.getInstance().getImageRegistry()
        .put("Navigator?ImageNotFound", ImageDescriptor.getMissingImageDescriptor()); //$NON-NLS-1$
  }

  /**
   * @generated
   */
  public void updateLabel(ViewerLabel label, TreePath elementPath) {
    Object element = elementPath.getLastSegment();
    if (element instanceof SystemdynamicsNavigatorItem
        && !isOwnView(((SystemdynamicsNavigatorItem) element).getView())) {
      return;
    }
    label.setText(getText(element));
    label.setImage(getImage(element));
  }

  /**
   * @generated
   */
  public Image getImage(Object element) {
    if (element instanceof SystemdynamicsNavigatorGroup) {
      SystemdynamicsNavigatorGroup group = (SystemdynamicsNavigatorGroup) element;
      return SystemdynamicsDiagramEditorPlugin.getInstance().getBundledImage(group.getIcon());
    }

    if (element instanceof SystemdynamicsNavigatorItem) {
      SystemdynamicsNavigatorItem navigatorItem = (SystemdynamicsNavigatorItem) element;
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
    switch (SystemdynamicsVisualIDRegistry.getVisualID(view)) {
    case CausalLinkEditPart.VISUAL_ID:
      return getImage(
          "Navigator?Link?http://repast.sf.net/systemdynamics?CausalLink", SystemdynamicsElementTypes.CausalLink_4002); //$NON-NLS-1$
    case StockEditPart.VISUAL_ID:
      return getImage(
          "Navigator?TopLevelNode?http://repast.sf.net/systemdynamics?Stock", SystemdynamicsElementTypes.Stock_2003); //$NON-NLS-1$
    case SystemModelEditPart.VISUAL_ID:
      return getImage(
          "Navigator?Diagram?http://repast.sf.net/systemdynamics?SystemModel", SystemdynamicsElementTypes.SystemModel_1000); //$NON-NLS-1$
    case RateEditPart.VISUAL_ID:
      return getImage(
          "Navigator?Link?http://repast.sf.net/systemdynamics?Rate", SystemdynamicsElementTypes.Rate_4003); //$NON-NLS-1$
    case CloudEditPart.VISUAL_ID:
      return getImage(
          "Navigator?TopLevelNode?http://repast.sf.net/systemdynamics?Cloud", SystemdynamicsElementTypes.Cloud_2002); //$NON-NLS-1$
    case VariableEditPart.VISUAL_ID:
      return getImage(
          "Navigator?TopLevelNode?http://repast.sf.net/systemdynamics?Variable", SystemdynamicsElementTypes.Variable_2001); //$NON-NLS-1$
    }
    return getImage("Navigator?UnknownElement", null); //$NON-NLS-1$
  }

  /**
   * @generated
   */
  private Image getImage(String key, IElementType elementType) {
    ImageRegistry imageRegistry = SystemdynamicsDiagramEditorPlugin.getInstance()
        .getImageRegistry();
    Image image = imageRegistry.get(key);
    if (image == null && elementType != null
        && SystemdynamicsElementTypes.isKnownElementType(elementType)) {
      image = SystemdynamicsElementTypes.getImage(elementType);
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
    if (element instanceof SystemdynamicsNavigatorGroup) {
      SystemdynamicsNavigatorGroup group = (SystemdynamicsNavigatorGroup) element;
      return group.getGroupName();
    }

    if (element instanceof SystemdynamicsNavigatorItem) {
      SystemdynamicsNavigatorItem navigatorItem = (SystemdynamicsNavigatorItem) element;
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
    switch (SystemdynamicsVisualIDRegistry.getVisualID(view)) {
    case CausalLinkEditPart.VISUAL_ID:
      return getCausalLink_4002Text(view);
    case StockEditPart.VISUAL_ID:
      return getStock_2003Text(view);
    case SystemModelEditPart.VISUAL_ID:
      return getSystemModel_1000Text(view);
    case RateEditPart.VISUAL_ID:
      return getRate_4003Text(view);
    case CloudEditPart.VISUAL_ID:
      return getCloud_2002Text(view);
    case VariableEditPart.VISUAL_ID:
      return getVariable_2001Text(view);
    }
    return getUnknownElementText(view);
  }

  /**
   * @generated
   */
  private String getStock_2003Text(View view) {
    IParser parser = SystemdynamicsParserProvider.getParser(SystemdynamicsElementTypes.Stock_2003,
        view.getElement() != null ? view.getElement() : view,
        SystemdynamicsVisualIDRegistry.getType(StockNameEditPart.VISUAL_ID));
    if (parser != null) {
      return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement()
          : view), ParserOptions.NONE.intValue());
    } else {
      SystemdynamicsDiagramEditorPlugin.getInstance().logError(
          "Parser was not found for label " + 5002); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getVariable_2001Text(View view) {
    IParser parser = SystemdynamicsParserProvider.getParser(
        SystemdynamicsElementTypes.Variable_2001, view.getElement() != null ? view.getElement()
            : view, SystemdynamicsVisualIDRegistry.getType(VariableNameEditPart.VISUAL_ID));
    if (parser != null) {
      return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement()
          : view), ParserOptions.NONE.intValue());
    } else {
      SystemdynamicsDiagramEditorPlugin.getInstance().logError(
          "Parser was not found for label " + 5001); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getRate_4003Text(View view) {
    IParser parser = SystemdynamicsParserProvider.getParser(SystemdynamicsElementTypes.Rate_4003,
        view.getElement() != null ? view.getElement() : view,
        SystemdynamicsVisualIDRegistry.getType(RateNameEditPart.VISUAL_ID));
    if (parser != null) {
      return parser.getPrintString(new EObjectAdapter(view.getElement() != null ? view.getElement()
          : view), ParserOptions.NONE.intValue());
    } else {
      SystemdynamicsDiagramEditorPlugin.getInstance().logError(
          "Parser was not found for label " + 6001); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getSystemModel_1000Text(View view) {
    return ""; //$NON-NLS-1$
  }

  /**
   * @generated
   */
  private String getCausalLink_4002Text(View view) {
    CausalLink domainModelElement = (CausalLink) view.getElement();
    if (domainModelElement != null) {
      return domainModelElement.getId();
    } else {
      SystemdynamicsDiagramEditorPlugin.getInstance().logError(
          "No domain element for view with visualID = " + 4002); //$NON-NLS-1$
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * @generated
   */
  private String getCloud_2002Text(View view) {
    Cloud domainModelElement = (Cloud) view.getElement();
    if (domainModelElement != null) {
      return domainModelElement.getName();
    } else {
      SystemdynamicsDiagramEditorPlugin.getInstance().logError(
          "No domain element for view with visualID = " + 2002); //$NON-NLS-1$
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
    return SystemModelEditPart.MODEL_ID.equals(SystemdynamicsVisualIDRegistry.getModelID(view));
  }

}
