package repast.simphony.systemdynamics.diagram.navigator;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class SystemdynamicsNavigatorItem extends SystemdynamicsAbstractNavigatorItem {

  /**
   * @generated
   */
  static {
    final Class[] supportedTypes = new Class[] { View.class, EObject.class };
    Platform.getAdapterManager().registerAdapters(new IAdapterFactory() {

      public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adaptableObject instanceof repast.simphony.systemdynamics.diagram.navigator.SystemdynamicsNavigatorItem
            && (adapterType == View.class || adapterType == EObject.class)) {
          return ((repast.simphony.systemdynamics.diagram.navigator.SystemdynamicsNavigatorItem) adaptableObject)
              .getView();
        }
        return null;
      }

      public Class[] getAdapterList() {
        return supportedTypes;
      }
    }, repast.simphony.systemdynamics.diagram.navigator.SystemdynamicsNavigatorItem.class);
  }

  /**
   * @generated
   */
  private View myView;

  /**
   * @generated
   */
  private boolean myLeaf = false;

  /**
   * @generated
   */
  public SystemdynamicsNavigatorItem(View view, Object parent, boolean isLeaf) {
    super(parent);
    myView = view;
    myLeaf = isLeaf;
  }

  /**
   * @generated
   */
  public View getView() {
    return myView;
  }

  /**
   * @generated
   */
  public boolean isLeaf() {
    return myLeaf;
  }

  /**
   * @generated
   */
  public boolean equals(Object obj) {
    if (obj instanceof repast.simphony.systemdynamics.diagram.navigator.SystemdynamicsNavigatorItem) {
      return EcoreUtil
          .getURI(getView())
          .equals(
              EcoreUtil
                  .getURI(((repast.simphony.systemdynamics.diagram.navigator.SystemdynamicsNavigatorItem) obj)
                      .getView()));
    }
    return super.equals(obj);
  }

  /**
   * @generated
   */
  public int hashCode() {
    return EcoreUtil.getURI(getView()).hashCode();
  }

}
