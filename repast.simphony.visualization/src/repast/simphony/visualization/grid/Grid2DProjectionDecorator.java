package repast.simphony.visualization.grid;

import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.visualization.decorator.AbstractProjectionDecorator;
import repast.simphony.visualization.decorator.ProjectionDecorator2D;
import repast.simphony.visualizationOGL2D.DisplayOGL2D;
import saf.v3d.AppearanceFactory;
import saf.v3d.GridShape2D;
import saf.v3d.scene.VComposite;
import saf.v3d.scene.VShape;

/**
 * 2D grid decorator that decorates a grid projection with a 2D grid.
 */
public class Grid2DProjectionDecorator extends AbstractProjectionDecorator<Grid<?>> implements
    ProjectionDecorator2D<Grid<?>> {

  private static String TYPE = "GRID_DECORATOR_TYPE";

  /**
   * Initializes the decorator by adding a grid shape of the appropriate size to
   * the parent.
   * 
   * @param display
   * @param parent
   *          the parent to which the decoration should be added
   */
  public void init(DisplayOGL2D display, VComposite parent) {
    GridDimensions dims = projection.getDimensions();
    GridShape2D grid = new GridShape2D(unitSize, dims.getDimension(0), dims.getDimension(1));
    VShape shape = new VShape(grid);
    //shape.translate(-unitSize / 2, -unitSize / 2, 0);
    shape.setAppearance(AppearanceFactory.createColorAppearance(color));
    parent.addChild(shape);
    shape.putProperty(AbstractProjectionDecorator.TYPE_KEY, TYPE);
  }

  /**
   * Updates the decorator. The intention is that this would only do something
   * if the decoration has changed from that created in init.
   */
  public void update() {
  }
}
