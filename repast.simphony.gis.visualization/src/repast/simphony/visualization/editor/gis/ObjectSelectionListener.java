package repast.simphony.visualization.editor.gis;

import java.util.List;

/**
 * Interface for classes that want to be notified of object selection.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public interface ObjectSelectionListener {

  /**
   * Invoked when objects have been selected.
   *
   * @param objs the selected nodes.
   */
  void objectsSelected(List<Object> objs);
}