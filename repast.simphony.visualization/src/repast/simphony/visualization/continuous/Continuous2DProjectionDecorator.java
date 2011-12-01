package repast.simphony.visualization.continuous;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.visualization.decorator.AbstractProjectionDecorator;
import repast.simphony.visualization.decorator.ProjectionDecorator2D;
import repast.simphony.visualizationOGL2D.DisplayOGL2D;
import saf.v3d.BoxShape2D;
import saf.v3d.scene.VComposite;
import saf.v3d.scene.VShape;

/**
 * 2D decorator for a continuous space. This will add a bounding box around the
 * space.
 */
public class Continuous2DProjectionDecorator extends
    AbstractProjectionDecorator<ContinuousSpace<?>> implements
    ProjectionDecorator2D<ContinuousSpace<?>> {

  public static final String TYPE = "CONTINUOUS_DECORATOR";

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualization.decorator.ProjectionDecorator2D#init(repast
   * .simphony.visualizationOGL2D.DisplayOGL2D, saf.v3d.scene.VRoot)
   */
  public void init(DisplayOGL2D display, VComposite parent) {
    float width = (float) projection.getDimensions().getWidth() * unitSize;
    float height = (float) projection.getDimensions().getHeight() * unitSize;

    VShape shape;
    if (width == Float.NEGATIVE_INFINITY || height == Float.NEGATIVE_INFINITY) {
      // TODO make axes
      //shape = ShapeFactory2D.createAxes(width, height, 1000 * unitSize, color);
      //parent.addChild(shape);
    } else {
      BoxShape2D box = new BoxShape2D(width, height);
      shape = new VShape(box);
      parent.addChild(shape);
      shape.putProperty(AbstractProjectionDecorator.TYPE_KEY, TYPE);
    }
  }

  

  /**
   * Updates the decorator. The intention is that this would only do something
   * if the decoration has changed from that created in init.
   */
  public void update() {
  }
}
