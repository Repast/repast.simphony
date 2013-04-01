package repast.simphony.statecharts.navigator;

import org.eclipse.jface.viewers.ViewerSorter;

import repast.simphony.statecharts.part.StatechartVisualIDRegistry;

/**
 * @generated
 */
public class StatechartNavigatorSorter extends ViewerSorter {

  /**
   * @generated
   */
  private static final int GROUP_CATEGORY = 7004;

  /**
   * @generated
   */
  public int category(Object element) {
    if (element instanceof StatechartNavigatorItem) {
      StatechartNavigatorItem item = (StatechartNavigatorItem) element;
      return StatechartVisualIDRegistry.getVisualID(item.getView());
    }
    return GROUP_CATEGORY;
  }

}
