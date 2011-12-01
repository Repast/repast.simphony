package repast.simphony.visualization.editor;

import repast.simphony.space.projection.Projection;
import repast.simphony.visualization.AbstractDisplayData;
import repast.simphony.visualization.VisualizedObjectContainer;

/**
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class SyntheticDisplayData extends AbstractDisplayData {

  private VisualizedObjectContainer container;

  public SyntheticDisplayData(VisualizedObjectContainer container) {
    this.container = container;
  }

  public void addProjection(Projection proj) {
    projs.add(proj);
  }

  /**
   * Gets the container that contains the objects we want to display.
   *
   * @return the container that contains the objects we want to display.
   */
  public VisualizedObjectContainer getContainer() {
    return container;
  }

  /**
   * Gets the objects to be initially displayed.
   *
   * @return an iterable over the objects to be initially displayed.
   */
  public Iterable objects() {
    return container;
  }
}
