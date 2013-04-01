package repast.simphony.statecharts.navigator;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class StatechartNavigatorItem extends StatechartAbstractNavigatorItem {

  /**
   * @generated
   */
  static {
    final Class[] supportedTypes = new Class[] { View.class, EObject.class };
    Platform.getAdapterManager().registerAdapters(new IAdapterFactory() {

      public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adaptableObject instanceof repast.simphony.statecharts.navigator.StatechartNavigatorItem
            && (adapterType == View.class || adapterType == EObject.class)) {
          return ((repast.simphony.statecharts.navigator.StatechartNavigatorItem) adaptableObject).getView();
        }
        return null;
      }

      public Class[] getAdapterList() {
        return supportedTypes;
      }
    }, repast.simphony.statecharts.navigator.StatechartNavigatorItem.class);
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
  public StatechartNavigatorItem(View view, Object parent, boolean isLeaf) {
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
    if (obj instanceof repast.simphony.statecharts.navigator.StatechartNavigatorItem) {
      return EcoreUtil.getURI(getView()).equals(
          EcoreUtil.getURI(((repast.simphony.statecharts.navigator.StatechartNavigatorItem) obj)
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
