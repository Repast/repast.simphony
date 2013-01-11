package repast.simphony.systemdynamics.diagram.navigator;

import org.eclipse.jface.viewers.ViewerSorter;

import repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry;

/**
 * @generated
 */
public class SystemdynamicsNavigatorSorter extends ViewerSorter {

  /**
   * @generated
   */
  private static final int GROUP_CATEGORY = 4006;

  /**
   * @generated
   */
  public int category(Object element) {
    if (element instanceof SystemdynamicsNavigatorItem) {
      SystemdynamicsNavigatorItem item = (SystemdynamicsNavigatorItem) element;
      return SystemdynamicsVisualIDRegistry.getVisualID(item.getView());
    }
    return GROUP_CATEGORY;
  }

}
