package repast.simphony.statecharts.navigator;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;

/**
 * @generated
 */
public abstract class StatechartAbstractNavigatorItem extends PlatformObject {

  /**
   * @generated
   */
  static {
    final Class[] supportedTypes = new Class[] { ITabbedPropertySheetPageContributor.class };
    final ITabbedPropertySheetPageContributor propertySheetPageContributor = new ITabbedPropertySheetPageContributor() {
      public String getContributorId() {
        return "repast.simphony.statecharts.diagram"; //$NON-NLS-1$
      }
    };
    Platform.getAdapterManager().registerAdapters(new IAdapterFactory() {

      public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adaptableObject instanceof repast.simphony.statecharts.navigator.StatechartAbstractNavigatorItem
            && adapterType == ITabbedPropertySheetPageContributor.class) {
          return propertySheetPageContributor;
        }
        return null;
      }

      public Class[] getAdapterList() {
        return supportedTypes;
      }
    }, repast.simphony.statecharts.navigator.StatechartAbstractNavigatorItem.class);
  }

  /**
   * @generated
   */
  private Object myParent;

  /**
   * @generated
   */
  protected StatechartAbstractNavigatorItem(Object parent) {
    myParent = parent;
  }

  /**
   * @generated
   */
  public Object getParent() {
    return myParent;
  }

}
