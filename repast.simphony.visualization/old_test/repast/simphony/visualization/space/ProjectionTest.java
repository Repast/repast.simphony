package repast.visualization.space;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.RandomGridAdder;
import repast.visualization.editor.space.Projected3DGrid;
import repast.visualization.editor.space.Projected3DSpace;

/**
 * @author Nick Collier
 */
public class ProjectionTest {

  @Test
  public void grid3D() {
    Context context = new DefaultContext();
    GridBuilderParameters params = new GridBuilderParameters(new repast.simphony.space.grid.StrictBorders(), new RandomGridAdder(),
            false, 10, 20, 30);
    Grid grid = GridFactoryFinder.createGridFactory(null).createGrid("3D Grid", context, params);

    Object obj1 = new Object();
    Object obj2 = new Object();

    context.add(obj1);
    context.add(obj2);

    Projected3DGrid pGrid = new Projected3DGrid(grid, 1, 2);
    assertEquals(2, pGrid.getDimensions().size());
    assertEquals(20, pGrid.getDimensions().getWidth());
    assertEquals(20, pGrid.getDimensions().getDimension(0));
    assertEquals(30, pGrid.getDimensions().getHeight());
    assertEquals(30, pGrid.getDimensions().getDimension(1));

    GridPoint pt = grid.getLocation(obj1);
    GridPoint pt2 = pGrid.getLocation(obj1);
    assertEquals(pt.getY(), pt2.getX());
    assertEquals(pt.getZ(), pt2.getY());
    assertEquals(obj1, pGrid.getObjectAt(pt.getY(), pt.getZ()));

    pGrid.moveTo(obj1, 14, 10);
    assertEquals(obj1, grid.getObjectAt(pt.getX(), 14, 10));
    assertEquals(new GridPoint(14, 10), pGrid.getLocation(obj1));


    // 2D proj of x and Z where z is the x and x is the y
    pGrid = new Projected3DGrid(grid, 2, 0);
    assertEquals(2, pGrid.getDimensions().size());
    assertEquals(30, pGrid.getDimensions().getWidth());
    assertEquals(30, pGrid.getDimensions().getDimension(0));
    assertEquals(10, pGrid.getDimensions().getHeight());
    assertEquals(10, pGrid.getDimensions().getDimension(1));

    context.remove(obj1);
    context.add(obj1);

    pt = grid.getLocation(obj1);
    pt2 = pGrid.getLocation(obj1);
    assertEquals(pt.getZ(), pt2.getX());
    assertEquals(pt.getX(), pt2.getY());
    assertEquals(obj1, pGrid.getObjectAt(pt.getZ(), pt.getX()));

    pGrid.moveTo(obj1, 22, 5);
    assertEquals(obj1, grid.getObjectAt(5, pt.getY(), 22));
    assertEquals(new GridPoint(22, 5), pGrid.getLocation(obj1));
  }

  @Test
  public void space3D() {
    Context context = new DefaultContext();
    ContinuousSpace space = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null).
            createContinuousSpace("3D Space", context, new RandomCartesianAdder(),
                    new repast.simphony.space.continuous.StrictBorders(), 10, 20, 30);

    Object obj1 = new Object();
    Object obj2 = new Object();

    context.add(obj1);
    context.add(obj2);

    Projected3DSpace pSpace = new Projected3DSpace(space, 1, 2);
    assertEquals(2, pSpace.getDimensions().size());
    assertEquals(20, pSpace.getDimensions().getWidth());
    assertEquals(20, pSpace.getDimensions().getDimension(0));
    assertEquals(30, pSpace.getDimensions().getHeight());
    assertEquals(30, pSpace.getDimensions().getDimension(1));

    NdPoint pt = space.getLocation(obj1);
    NdPoint pt2 = pSpace.getLocation(obj1);
    assertEquals(pt.getY(), pt2.getX());
    assertEquals(pt.getZ(), pt2.getY());
    assertEquals(obj1, pSpace.getObjectAt(pt.getY(), pt.getZ()));

    pSpace.moveTo(obj1, 14, 10);
    assertEquals(obj1, space.getObjectAt(pt.getX(), 14, 10));
    assertEquals(new NdPoint(14.0, 10.0), pSpace.getLocation(obj1));


    // 2D proj of x and Z where z is the x and x is the y
    pSpace = new Projected3DSpace(space, 2, 0);
    assertEquals(2, pSpace.getDimensions().size());
    assertEquals(30, pSpace.getDimensions().getWidth());
    assertEquals(30, pSpace.getDimensions().getDimension(0));
    assertEquals(10, pSpace.getDimensions().getHeight());
    assertEquals(10, pSpace.getDimensions().getDimension(1));

    context.remove(obj1);
    context.add(obj1);

    pt = space.getLocation(obj1);
    pt2 = pSpace.getLocation(obj1);
    assertEquals(pt.getZ(), pt2.getX());
    assertEquals(pt.getX(), pt2.getY());
    assertEquals(obj1, pSpace.getObjectAt(pt.getZ(), pt.getX()));

    pSpace.moveTo(obj1, 22, 5);
    assertEquals(obj1, space.getObjectAt(5, pt.getY(), 22));
    assertEquals(new NdPoint(22.0, 5.0), pSpace.getLocation(obj1));

  }
}
