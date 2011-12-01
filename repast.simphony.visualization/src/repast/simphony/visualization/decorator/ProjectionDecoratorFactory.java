package repast.simphony.visualization.decorator;

import java.awt.Color;
import java.util.Map;

import repast.simphony.visualization.continuous.Continuous2DProjectionDecorator;
import repast.simphony.visualization.continuous.Continuous3DProjectionDecorator;
import repast.simphony.visualization.continuous.ContinuousDecorator;
import repast.simphony.visualization.grid.Grid2DProjectionDecorator;
import repast.simphony.visualization.grid.Grid3DProjectionDecorator;
import repast.simphony.visualization.grid.GridDecorator;

/**
 * Factory for creating projection decorators. A map is passed and based on the
 * contents of the map, a projection decorator is created. For example, passing
 * in a map with a group key of AbstractProjectionDecorator.GRID_DECORATOR would
 * create a grid decorator.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ProjectionDecoratorFactory {

  public ProjectionDecorator2D<?> create2DDecorator(String decoratorID, Map<String, Object> props) {
    if (decoratorID.equals(GridDecorator.GRID_DECORATOR)) {
      // create a 2D grid decorator from those properties.
      Grid2DProjectionDecorator deco = new Grid2DProjectionDecorator();
      if ((Boolean) props.get(Grid2DProjectionDecorator.SHOW_DECORATOR)) {
        deco.setUnitSize((Float) props.get(Grid2DProjectionDecorator.UNIT_SIZE));
        deco.setColor(new Color((Integer) props.get(Grid2DProjectionDecorator.COLOR)));
        return deco;
      }
    } else if (decoratorID.equals(ContinuousDecorator.CONTINUOUS_DECORATOR)) {
      Continuous2DProjectionDecorator deco = new Continuous2DProjectionDecorator();
      if ((Boolean) props.get(AbstractProjectionDecorator.SHOW_DECORATOR)) {
        deco.setUnitSize((Float) props.get(Continuous2DProjectionDecorator.UNIT_SIZE));
        deco.setColor(new Color((Integer) props.get(Continuous2DProjectionDecorator.COLOR)));
        return deco;
      }
    }
    return null;
  }

  public ProjectionDecorator3D<?> create3DDecorator(String decoratorID, Map<String, Object> props) {
    if (decoratorID.equals(GridDecorator.GRID_DECORATOR)) {
      // create a 3D grid decorator from those properties.
      Grid3DProjectionDecorator deco = new Grid3DProjectionDecorator();
      if ((Boolean) props.get(Grid3DProjectionDecorator.SHOW_DECORATOR)) {
        deco.setUnitSize((Float) props.get(AbstractProjectionDecorator.UNIT_SIZE));
        deco.setColor(new Color((Integer) props.get(AbstractProjectionDecorator.COLOR)));
        return deco;
      }
    } else if (decoratorID.equals(ContinuousDecorator.CONTINUOUS_DECORATOR)) {
      Continuous3DProjectionDecorator deco = new Continuous3DProjectionDecorator();
      if ((Boolean) props.get(Continuous3DProjectionDecorator.SHOW_DECORATOR)) {
        deco.setUnitSize((Float) props.get(Continuous3DProjectionDecorator.UNIT_SIZE));
        deco.setColor(new Color((Integer) props.get(Continuous3DProjectionDecorator.COLOR)));
        return deco;
      }
    }
    return null;
  }
}
