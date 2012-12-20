package repast.simphony.systemdynamics.sdmodel.diagram.navigator;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;

import repast.simphony.systemdynamics.sdmodel.diagram.part.SystemdynamicsDiagramEditorPlugin;

/**
 * @generated
 */
public class SystemdynamicsDomainNavigatorLabelProvider implements ICommonLabelProvider {

  /**
   * @generated
   */
  private AdapterFactoryLabelProvider myAdapterFactoryLabelProvider = new AdapterFactoryLabelProvider(
      SystemdynamicsDiagramEditorPlugin.getInstance().getItemProvidersAdapterFactory());

  /**
   * @generated
   */
  public void init(ICommonContentExtensionSite aConfig) {
  }

  /**
   * @generated
   */
  public Image getImage(Object element) {
    if (element instanceof SystemdynamicsDomainNavigatorItem) {
      return myAdapterFactoryLabelProvider.getImage(((SystemdynamicsDomainNavigatorItem) element)
          .getEObject());
    }
    return null;
  }

  /**
   * @generated
   */
  public String getText(Object element) {
    if (element instanceof SystemdynamicsDomainNavigatorItem) {
      return myAdapterFactoryLabelProvider.getText(((SystemdynamicsDomainNavigatorItem) element)
          .getEObject());
    }
    return null;
  }

  /**
   * @generated
   */
  public void addListener(ILabelProviderListener listener) {
    myAdapterFactoryLabelProvider.addListener(listener);
  }

  /**
   * @generated
   */
  public void dispose() {
    myAdapterFactoryLabelProvider.dispose();
  }

  /**
   * @generated
   */
  public boolean isLabelProperty(Object element, String property) {
    return myAdapterFactoryLabelProvider.isLabelProperty(element, property);
  }

  /**
   * @generated
   */
  public void removeListener(ILabelProviderListener listener) {
    myAdapterFactoryLabelProvider.removeListener(listener);
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

}
