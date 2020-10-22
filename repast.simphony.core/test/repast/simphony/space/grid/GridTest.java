package repast.simphony.space.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jogamp.vecmath.Matrix3d;
import org.jogamp.vecmath.Vector3d;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.engine.environment.RunState;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.query.space.grid.GridWithin;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.query.space.grid.VNQuery;
import repast.simphony.query.space.projection.Within;
import repast.simphony.query.space.projection.WithinMoore;
import repast.simphony.query.space.projection.WithinVN;
import repast.simphony.space.Direction;
import repast.simphony.space.SpatialException;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;

/**
 * Tests for grids.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class GridTest extends TestCase {

  Integer one = new Integer(1);

  Integer two = new Integer(2);

  Integer three = new Integer(3);

  Integer four = new Integer(4);

  Context<Integer> context;

  public void setUp() {
    context = new DefaultContext<Integer>();
    context.add(one);
    context.add(two);
    context.add(three);
    context.add(four);

    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy2D(
        new SimpleGridAdder<Integer>(), new StrictBorders(), 10, 30);
    GridFactoryFinder.createGridFactory(null).createGrid("2D Grid", context, params);

    params = GridBuilderParameters.singleOccupancy2DTorus(new SimpleGridAdder<Integer>(), 10, 30);
    GridFactoryFinder.createGridFactory(null).createGrid("2D Torus", context, params);

    params = new GridBuilderParameters<Integer>(new StrictBorders(),
        new SimpleGridAdder<Integer>(), true, 10, 30);
    GridFactoryFinder.createGridFactory(null).createGrid("2D Multi Grid", context, params);

    params = new GridBuilderParameters<Integer>(new StrictBorders(),
        new SimpleGridAdder<Integer>(), false, 15, 8, 14, 12);
    GridFactoryFinder.createGridFactory(null).createGrid("4D Grid", context, params);

    params = new GridBuilderParameters<Integer>(new StrictBorders(),
        new SimpleGridAdder<Integer>(), false, 15, 8, 14);
    GridFactoryFinder.createGridFactory(null).createGrid("3D Grid", context, params);

    // enable the random stuff
    RunState.init(null, null, null);
  }

  public void testSimpleAsParkingLot() {
    Grid<Integer> grid = context.getProjection(Grid.class, "2D Grid");
    assertEquals(0, grid.size());
    context.remove(one);
    assertEquals(0, grid.size());
    grid.moveTo(two, 1, 1);
  }

  public void testGridCellNgh() {
    context.clear();
    // 10 x 30
    Grid<Integer> grid = context.getProjection(Grid.class, "2D Multi Grid");
    for (int i = 0; i < 18; i++) {
      context.add(i);
    }

    Map<GridPoint, Set<Integer>> vals = new HashMap<GridPoint, Set<Integer>>();
    int agent = 0;
    for (int x = 3; x < 6; x++) {
      for (int y = 2; y < 5; y++) {
        Set<Integer> agents = new HashSet<Integer>();
        int a1 = agent++;
        int a2 = agent++;
        agents.add(a1);
        agents.add(a2);
        vals.put(new GridPoint(x, y), agents);
        grid.moveTo(a1, x, y);
        grid.moveTo(a2, x, y);
      }
    }

    GridCellNgh<Integer> gcn = new GridCellNgh<Integer>(grid, new GridPoint(4, 3), Integer.class,
        1, 1);
    List<GridCell<Integer>> ngh = gcn.getNeighborhood(true);
    assertEquals(9, ngh.size());
    for (GridCell<Integer> cell : ngh) {
      Set<Integer> agents = vals.get(cell.getPoint());
      assertNotNull(agents);
      assertEquals(agents.size(), cell.size());
      for (Integer val : cell.items()) {
        assertTrue(agents.remove(val));
      }
      assertEquals(0, agents.size());
    }

    context.clear();
    for (int i = 0; i < 18; i++) {
      context.add(i);
    }

    agent = 0;
    for (int x = 3; x < 6; x++) {
      for (int y = 2; y < 5; y++) {
        if (x != 4 && y != 3) {
          Set<Integer> agents = new HashSet<Integer>();
          int a1 = agent++;
          int a2 = agent++;
          agents.add(a1);
          agents.add(a2);
          vals.put(new GridPoint(x, y), agents);
          grid.moveTo(a1, x, y);
          grid.moveTo(a2, x, y);
        }
      }
    }

    gcn = new GridCellNgh<Integer>(grid, new GridPoint(4, 3), Integer.class, 1, 1);
    ngh = gcn.getNeighborhood(false);
    assertEquals(8, ngh.size());
    for (GridCell<Integer> cell : ngh) {
      Set<Integer> agents = vals.get(cell.getPoint());
      assertNotNull(agents);
      assertEquals(agents.size(), cell.size());
      for (Integer val : cell.items()) {
        assertTrue(agents.remove(val));
      }
      assertEquals(0, agents.size());
    }

    context.clear();
    grid = context.getProjection(Grid.class, "2D Torus");
    for (int i = 0; i < 9; i++)
      context.add(i);

    Map<GridPoint, Integer> expected = new HashMap<GridPoint, Integer>();
    expected.put(new GridPoint(0, 0), 0);
    expected.put(new GridPoint(0, 1), 1);
    expected.put(new GridPoint(1, 1), 2);
    expected.put(new GridPoint(1, 0), 3);
    expected.put(new GridPoint(1, 29), 4);
    expected.put(new GridPoint(0, 29), 5);
    expected.put(new GridPoint(9, 29), 6);
    expected.put(new GridPoint(9, 0), 7);
    expected.put(new GridPoint(9, 1), 8);

    for (Map.Entry<GridPoint, Integer> entry : expected.entrySet()) {
      grid.moveTo(entry.getValue(), entry.getKey().getX(), entry.getKey().getY());
    }

    gcn = new GridCellNgh<Integer>(grid, new GridPoint(0, 0), Integer.class, 1, 1);
    ngh = gcn.getNeighborhood(true);
    assertEquals(9, ngh.size());
    for (GridCell<Integer> cell : ngh) {
      System.out.println(cell.getPoint());
      Integer a = expected.get(cell.getPoint());
      assertNotNull(a);
      assertEquals(1, cell.size());
      assertEquals(a, cell.items().iterator().next());
    }

  }

  /*
   * 
   * Commented out because this tests for a hang message by causing a hang.
   * 
   * public void testFullGridWarning() { BasicConfigurator.configure(); Context
   * aContext = new DefaultContext<Integer>();
   * 
   * GridBuilderParameters<Integer> params = GridBuilderParameters
   * .singleOccupancy2D(new RandomGridAdder<Integer>(), new StrictBorders(), 3,
   * 3); GridFactoryFinder.createGridFactory(null).createGrid("2D Grid",
   * aContext, params);
   * 
   * for (int i = 0; i < 50; i++) { aContext.add(i); } }
   */

  public void testDimensions() {
    Grid<Integer> grid = context.getProjection(Grid.class, "2D Grid");
    GridDimensions dims = grid.getDimensions();
    assertEquals(10, dims.getDimension(0));
    assertEquals(30, dims.getDimension(1));

    grid = context.getProjection(Grid.class, "4D Grid");
    dims = grid.getDimensions();
    assertEquals(15, dims.getDimension(0));
    assertEquals(8, dims.getDimension(1));
    assertEquals(14, dims.getDimension(2));
    assertEquals(12, dims.getDimension(3));
  }

  public void testSize() {
    Grid<Integer> grid = context.getProjection(Grid.class, "2D Grid");
    assertEquals(0, grid.size());
  }

  public void testGridPoint() {
    GridPoint a = new GridPoint(new int[] { 3, 3 });
    GridPoint b = new GridPoint(new int[] { 3, 3 });
    assertTrue(a.equals(b));
    GridPoint c = new GridPoint(new int[] { 10, 3 });
    assertTrue(!a.equals(c));
  }

  public void testBounceGrid() {
    GridBuilderParameters<Integer> params = new GridBuilderParameters<Integer>(new BouncyBorders(7,
        10, 10), new SimpleGridAdder<Integer>(), true, 7, 10, 10);
    GridFactoryFinder.createGridFactory(null).createGrid("Bounce Grid", context, params);
    Grid<Integer> grid = context.getProjection(Grid.class, "Bounce Grid");
    grid.moveTo(one, 3, 4, 2);
    grid.moveByDisplacement(one, -4, -6, -4);
    GridPoint point = grid.getLocation(one);
    assertEquals(1, point.getX());
    assertEquals(2, point.getY());
    assertEquals(2, point.getZ());
  }

  public void testSimpleMove() {
    Grid<Integer> grid = context.getProjection(Grid.class, "2D Grid");
    boolean result = grid.moveTo(one, 3, 10);
    assertTrue(result);

    GridPoint point = grid.getLocation(one);
    assertTrue(point.getX() == 3 && point.getY() == 10);

    Integer val = grid.getObjectAt(point.getX(), point.getY());
    assertEquals(one, val);

    result = grid.moveTo(one, 5, 10);
    assertTrue(result);

    // "one" is no longer at 3, 10
    point = new GridPoint(3, 10);
    val = grid.getObjectAt(point.getX(), point.getY());
    assertTrue(val == null);

    result = grid.moveTo(two, 5, 10);
    // single occupancy so two can't be in same
    // cell as one
    assertTrue(!result);
  }

  public void testMoveAndLocation() {
    Grid<Integer> grid = context.getProjection(Grid.class, "2D Grid");
    boolean result = grid.moveTo(one, 3, 10);
    assertTrue(result);
    assertEquals(1, grid.size());
    GridPoint point = grid.getLocation(one);
    assertTrue(point.getX() == 3 && point.getY() == 10);

    Integer val = grid.getObjectAt(point.getX(), point.getY());
    assertEquals(one, val);

    val = grid.getRandomObjectAt(point.getX(), point.getY());
    assertEquals(one, val);

    int count = 0;
    val = null;

    for (Iterator<Integer> iter = grid.getObjectsAt(point.getX(), point.getY()).iterator(); iter
        .hasNext();) {
      count++;
      val = iter.next();
    }
    assertEquals(1, count);
    assertEquals(one, val);

    // single occupancy grid so this should return false.
    result = grid.moveTo(two, 3, 10);
    assertTrue(!result);
    assertEquals(1, grid.size());
    assertTrue(grid.getLocation(two) == null);

    // try {
    // grid.move(two, 15, 28);
    // fail();
    // } catch (IndexOutOfBoundsException ex) {
    // }
  }

  public void testMoveAndLocationTorus() {
    Grid<Integer> grid = context.getProjection(Grid.class, "2D Torus");
    boolean result = grid.moveTo(one, 3, 10);
    assertTrue(result);
    assertEquals(1, grid.size());
    GridPoint point = grid.getLocation(one);
    assertTrue(point.getX() == 3 && point.getY() == 10);

    // single occupancy grid so this should return false.
    result = grid.moveTo(two, 3, 10);
    assertTrue(!result);
    assertEquals(1, grid.size());
    assertTrue(grid.getLocation(two) == null);

    grid.moveTo(two, 15, -2);
    assertEquals(2, grid.size());
    point = grid.getLocation(two);
    assertTrue(point.getX() == 5 && point.getY() == 28);
  }

  public void testDisplacementMove() {
    // grid dims: 15, 8, 14, 12
    Grid<Integer> grid = context.getProjection(Grid.class, "4D Grid");
    boolean result = grid.moveTo(one, 3, 4, 5, 6);
    assertTrue(result);
    assertEquals(1, grid.size());
    GridPoint point = grid.getLocation(one);
    assertTrue(point.getCoord(0) == 3 && point.getCoord(1) == 4 && point.getCoord(2) == 5
        && point.getCoord(3) == 6);

    // test full args
    point = grid.moveByDisplacement(one, 2, -1, 3, -3);
    assertTrue(point.getCoord(0) == 5 && point.getCoord(1) == 3 && point.getCoord(2) == 8
        && point.getCoord(3) == 3);

    // test only "x" arg
    point = grid.moveByDisplacement(one, 2);
    assertTrue(point.getCoord(0) == 7 && point.getCoord(1) == 3 && point.getCoord(2) == 8
        && point.getCoord(3) == 3);

    try {
      // test too many args
      grid.moveByDisplacement(one, 2, -1, 3, -3, 10);
      fail();
    } catch (SpatialException ex) {
    }
  }

  public void testVectorMoveConstants() {
    // 10 by 30
    Grid<Integer> grid = context.getProjection(Grid.class, "2D Grid");
    boolean thrown = false;
    try {
      grid.moveTo(one, 3, 4, 5);
    } catch (SpatialException ex) {
      thrown = true;
    }
    assertTrue("Exception thrown", thrown);

    grid.moveTo(one, 5, 5);

    GridPoint point = grid.moveByVector(one, 1, Direction.NORTH);
    assertEquals(5, point.getX());
    assertEquals(6, point.getY());

    point = grid.moveByVector(one, 1, Direction.SOUTH);
    assertEquals(5, point.getX());
    assertEquals(5, point.getY());

    point = grid.moveByVector(one, 1, Direction.WEST);
    assertEquals(4, point.getX());
    assertEquals(5, point.getY());

    point = grid.moveByVector(one, 1, Direction.EAST);
    assertEquals(5, point.getX());
    assertEquals(5, point.getY());
  }

  public void testVectorMove() {
    // grid dims: 15, 8, 14, 12
    Grid<Integer> grid = context.getProjection(Grid.class, "3D Grid");
    boolean result = grid.moveTo(one, 3, 4, 5);
    assertTrue(result);
    assertEquals(1, grid.size());
    GridPoint point = grid.getLocation(one);
    assertTrue(point.getCoord(0) == 3 && point.getCoord(1) == 4 && point.getCoord(2) == 5);

    int[] oldVals = new int[3];
    for (int i = 0; i < 3; i++) {
      oldVals[i] = point.getCoord(i);
    }

    // test truncated version
    point = grid.moveByVector(one, 2, 0);
    int[] vals = vecTrans(0, 0, 0, 2);
    for (int i = 0; i < 3; i++) {
      assertEquals("i: -" + i, vals[i] + oldVals[i], point.getCoord(i));
    }

    grid.moveTo(one, 3, 4, 5);
    // test when supplying complete angle list
    point = grid.moveByVector(one, 2, 0, Math.toRadians(0), 0);
    vals = vecTrans(0, 0, 0, 2);
    for (int i = 0; i < 3; i++) {
      assertEquals("i: -" + i, vals[i] + oldVals[i], point.getCoord(i));
    }

    for (int i = 0; i < 3; i++) {
      oldVals[i] = point.getCoord(i);
    }

    point = grid.moveByVector(one, 2, 0, Math.toRadians(90), 0);
    vals = vecTrans(0, 90, 0, 2);
    for (int i = 0; i < 3; i++) {
      assertEquals("i: " + i, vals[i] + oldVals[i], point.getCoord(i));
    }
  }

  private int[] vecTrans(int x, int y, int z, int scale) {
    Vector3d vec3d = new Vector3d(1, 0, 0);
    Matrix3d matrixX = new Matrix3d();
    matrixX.rotX(Math.toRadians(x));
    Matrix3d matrixY = new Matrix3d();
    matrixY.rotY(Math.toRadians(y));
    Matrix3d matrixZ = new Matrix3d();
    matrixZ.rotZ(Math.toRadians(z));
    matrixX.mul(matrixY);
    matrixX.mul(matrixZ);
    matrixX.transform(vec3d);
    vec3d.scale(scale);
    int[] vals = new int[3];
    vals[0] = (int) Math.rint(vec3d.x);
    vals[1] = (int) Math.rint(vec3d.y);
    // change z's sign to match the vector stuff.
    vals[2] = (int) Math.rint(-vec3d.z);
    return vals;
  }

  public void testMoveAndLocationMulti() {
    Grid<Integer> grid = context.getProjection(Grid.class, "2D Multi Grid");
    boolean result = grid.moveTo(one, 3, 10);
    assertTrue(result);
    assertEquals(1, grid.size());
    GridPoint point = grid.getLocation(one);
    assertTrue(point.getX() == 3 && point.getY() == 10);

    // mult occupancy grid so this should return true.
    result = grid.moveTo(two, 3, 10);
    assertTrue(result);
    assertEquals(2, grid.size());
    assertTrue(grid.getLocation(two).equals(point));

    Integer val = grid.getObjectAt(point.getX(), point.getY());
    assertEquals(one, val);

    val = grid.getRandomObjectAt(point.getX(), point.getY());
    assertTrue(val.equals(one) || val.equals(two));

    Set<Integer> set = new HashSet<Integer>();

    for (Iterator<Integer> iter = grid.getObjectsAt(point.getX(), point.getY()).iterator(); iter
        .hasNext();) {
      set.add(iter.next());
    }
    assertEquals(2, set.size());
    assertTrue(set.contains(one));
    assertTrue(set.contains(two));
  }

  // public void testToroidalTranslatorX() {
  // ToriodalMatrixIndexTranslator trans = new
  // ToriodalMatrixIndexTranslator(20);
  // int[] x = {10};
  // trans.getIndex(x);
  // assertEquals(10, x[0]);
  //
  // x[0] = 25;
  // trans.getIndex(x);
  // assertEquals(5, x[0]);
  //
  // x[0] = -5;
  // trans.getIndex(x);
  // assertEquals(15, x[0]);
  //
  // x[0] = 45;
  // trans.getIndex(x);
  // assertEquals(5, x[0]);
  // }
  //
  // public void testToroidalTranslatorXY() {
  // ToriodalMatrixIndexTranslator trans = new
  // ToriodalMatrixIndexTranslator(20, 40);
  // int[] xy = {10, 15};
  // trans.getIndex(xy);
  // assertEquals(10, xy[0]);
  // assertEquals(15, xy[1]);
  //
  // xy[0] = 25;
  // xy[1] = 41;
  // trans.getIndex(xy);
  // assertEquals(5, xy[0]);
  // assertEquals(1, xy[1]);
  //
  // xy[0] = -5;
  // xy[1] = -52;
  // trans.getIndex(xy);
  // assertEquals(15, xy[0]);
  // assertEquals(28, xy[1]);
  // }
  //
  // public void testToroidalTranslatorXYZ() {
  // ToriodalMatrixIndexTranslator trans = new
  // ToriodalMatrixIndexTranslator(20, 40, 10);
  // int[] xyz = {10, 15, 3};
  // trans.getIndex(xyz);
  // assertEquals(10, xyz[0]);
  // assertEquals(15, xyz[1]);
  // assertEquals(3, xyz[2]);
  //
  // xyz[0] = 25;
  // xyz[1] = 41;
  // xyz[2] = 50;
  // trans.getIndex(xyz);
  // assertEquals(5, xyz[0]);
  // assertEquals(1, xyz[1]);
  // assertEquals(0, xyz[2]);
  //
  // xyz[0] = -5;
  // xyz[1] = -52;
  // xyz[2] = -2;
  // trans.getIndex(xyz);
  // assertEquals(15, xyz[0]);
  // assertEquals(28, xyz[1]);
  // assertEquals(8, xyz[2]);
  // }
  //
  // public void testToroidalTranslatorXYZN() {
  // ToriodalMatrixIndexTranslator trans = new
  // ToriodalMatrixIndexTranslator(20, 40, 10, 15);
  // int[] xyzn = {10, 15, 3, 7};
  // trans.getIndex(xyzn);
  // assertEquals(10, xyzn[0]);
  // assertEquals(15, xyzn[1]);
  // assertEquals(3, xyzn[2]);
  // assertEquals(7, xyzn[3]);
  //
  // xyzn[0] = 25;
  // xyzn[1] = 41;
  // xyzn[2] = 50;
  // xyzn[3] = 31;
  // trans.getIndex(xyzn);
  // assertEquals(5, xyzn[0]);
  // assertEquals(1, xyzn[1]);
  // assertEquals(0, xyzn[2]);
  // assertEquals(1, xyzn[3]);
  //
  // xyzn[0] = -5;
  // xyzn[1] = -52;
  // xyzn[2] = -2;
  // xyzn[3] = -32;
  // trans.getIndex(xyzn);
  // assertEquals(15, xyzn[0]);
  // assertEquals(28, xyzn[1]);
  // assertEquals(8, xyzn[2]);
  // assertEquals(13, xyzn[3]);
  // }

  public void testSimpleBounce3D() {
    BouncyBorders bouncer = new BouncyBorders(12, 10, 14);
    int[] loc = { 3, 4, 5 };

    bouncer.translate(loc, -2, 3, 1);
    assertEquals(1, loc[0]);
    assertEquals(7, loc[1]);
    assertEquals(6, loc[2]);

    bouncer.translate(loc, 8, -2, 3);
    assertEquals(9, loc[0]);
    assertEquals(5, loc[1]);
    assertEquals(9, loc[2]);
  }

  public void testSimpleBounce2D() {
    BouncyBorders bouncer = new BouncyBorders(7, 10);
    int[] gp = new int[] { 2, 3 };

    // simple non-reflective movement
    bouncer.translate(gp, 1, 2);
    assertEquals(3, gp[0]);
    assertEquals(5, gp[1]);

    bouncer.translate(gp, -1, -5);
    assertEquals(2, gp[0]);
    assertEquals(0, gp[1]);

    bouncer.translate(gp, 0, 1);
    assertEquals(2, gp[0]);
    assertEquals(1, gp[1]);
  }

  public void testStraightBounce3D() {
    BouncyBorders bouncer = new BouncyBorders(12, 10, 14);

    // simple "straight" bounce off back wall
    int[] loc = { 10, 8, 12 };
    bouncer.translate(loc, 0, 0, 5);
    assertEquals(10, loc[0]);
    assertEquals(8, loc[1]);
    assertEquals(9, loc[2]);

    // simple "straight" bounce off front wall
    loc = new int[] { 10, 8, 3 };
    bouncer.translate(loc, 0, 0, -5);
    assertEquals(10, loc[0]);
    assertEquals(8, loc[1]);
    assertEquals(2, loc[2]);

    // simple "straight" bounce off left wall
    loc = new int[] { 3, 8, 3 };
    bouncer.translate(loc, -4, 0, 0);
    assertEquals(1, loc[0]);
    assertEquals(8, loc[1]);
    assertEquals(3, loc[2]);

    // simple "straight" bounce off right wall
    loc = new int[] { 10, 8, 3 };
    bouncer.translate(loc, 5, 0, 0);
    assertEquals(7, loc[0]);
    assertEquals(8, loc[1]);
    assertEquals(3, loc[2]);

    // simple "straight" bounce off bottom wall
    loc = new int[] { 10, 2, 3 };
    bouncer.translate(loc, 0, -4, 0);
    assertEquals(10, loc[0]);
    assertEquals(2, loc[1]);
    assertEquals(3, loc[2]);

    // simple "straight" bounce off top wall
    loc = new int[] { 10, 8, 3 };
    bouncer.translate(loc, 0, 5, 0);
    assertEquals(10, loc[0]);
    assertEquals(5, loc[1]);
    assertEquals(3, loc[2]);
  }

  // bounce in all 3 dims
  public void testComplexBounce3D() {
    BouncyBorders bouncer = new BouncyBorders(12, 10, 14);

    // bounce diag down and to left off back wall
    int[] loc = { 1, 7, 11 };
    bouncer.translate(loc, 3, -3, 3);
    assertEquals(4, loc[0]);
    assertEquals(4, loc[1]);
    assertEquals(12, loc[2]);

    loc = new int[] { 1, 7, 11 };
    bouncer.translate(loc, 3, -6, 4);
    assertEquals(4, loc[0]);
    assertEquals(1, loc[1]);
    assertEquals(11, loc[2]);

    // GridPoint gp = new GridPoint(new int[] { 1, 7, 11 });
    int[] iLoc = new int[] { 1, 7, 11 };
    bouncer.translate(/* gp, */iLoc, 3, -6, 4);
    assertEquals(4, iLoc[0]);
    assertEquals(1, iLoc[1]);
    assertEquals(11, iLoc[2]);

    // todo more tests!!!

  }

  // mult intersect is when the line from inside the
  // box to the new location intersects mult. boundaries
  // and we need to choose the first one intersected and
  // bounce off that.
  public void testMultIntersect3D() {
    BouncyBorders bouncer = new BouncyBorders(7, 10, 10);

    // through back wall far enough that appears to
    // intersect right wall
    int[] loc = { 4, 4, 8 };
    // bouncer.translate(loc, 3, 0, 3);
    // assertEquals(5.0, loc[0]);
    // assertEquals(4.0, loc[1]);
    // assertEquals(7.0, loc[2]);

    // through front wall, moving downwards so hits
    // front wall then floor then up
    loc = new int[] { 3, 4, 2 };
    bouncer.translate(loc, -4, -6, -4);
    assertEquals(1, loc[0]);
    assertEquals(2, loc[1], 1);
    assertEquals(2, loc[2], 1);

    // GridPoint gp = new GridPoint(new int[] { 3, 4, 2 });
    int[] iLoc = new int[] { 3, 4, 2 };
    bouncer.translate(/* gp, */iLoc, -4, -6, -4);
    assertEquals(1, iLoc[0]);
    assertEquals(2, iLoc[1]);
    assertEquals(2, iLoc[2]);
  }

  public void testBounce2D() {
    BouncyBorders bouncer = new BouncyBorders(7, 10);
    int[] gp = new int[] { 2, 3 };

    // a simple bounce
    bouncer.translate(gp, -3, 0);
    assertEquals(1, gp[0]);
    assertEquals(3, gp[1]);

    bouncer.translate(gp, 6, 0);
    assertEquals(5, gp[0]);
    assertEquals(3, gp[1]);

    // a simple bounce
    bouncer.translate(gp, 0, -7);
    assertEquals(5, gp[0]);
    assertEquals(4, gp[1]);

    bouncer.translate(gp, 0, 7);
    assertEquals(5, gp[0]);
    assertEquals(7, gp[1]);

    gp = new int[] { 1, 3 };
    bouncer.translate(gp, -2, 5);
    assertEquals(1, gp[0]);
    assertEquals(8, gp[1]);

    gp = new int[] { 5, 1 };
    bouncer.translate(gp, 4, 5);
    assertEquals(3, gp[0]);
    assertEquals(6, gp[1]);

    gp = new int[] { 1, 7 };
    bouncer.translate(gp, 1, 3);
    assertEquals(2, gp[0]);
    assertEquals(8, gp[1]);

    gp = new int[] { 1, 2 };
    bouncer.translate(gp, 1, -3);
    assertEquals(2, gp[0]);
    assertEquals(1, gp[1]);

    // int bounce
    gp = new int[] { 4, 1 };
    bouncer.translate(gp, 3, -3);
    assertEquals(5, gp[0]);
    assertEquals(2, gp[1]);

    gp = new int[] { 4, 8 };
    bouncer.translate(gp, 3, 3);
    assertEquals(5, gp[0]);
    assertEquals(7, gp[1]);

    gp = new int[] { 2, 8 };
    bouncer.translate(gp, -3, 3);
    assertEquals(1, gp[0]);
    assertEquals(7, gp[1]);

    gp = new int[] { 1, 2 };
    bouncer.translate(gp, -3, -3);
    assertEquals(2, gp[0]);
    assertEquals(1, gp[1]);

    // corner bounce
    gp = new int[] { 2, 2 };
    bouncer.translate(gp, -4, -4);
    assertEquals(2, gp[0]);
    assertEquals(2, gp[1]);
  }

  public void test4D() {
    context.clear();
    Grid<Integer> grid = context.getProjection(Grid.class, "4D Grid");
    GridDimensions dims = grid.getDimensions();
    List<Integer> list = new ArrayList<Integer>();
    for (int i = 0; i < dims.getDimension(0) * dims.getDimension(1) * dims.getDimension(2)
        * dims.getDimension(3); i++) {
      Integer o = new Integer(i);
      list.add(o);
      context.add(o);
    }

    int i = 0;
    for (int x = 0; x < dims.getDimension(0); x++) {
      for (int y = 0; y < dims.getDimension(1); y++) {
        for (int z = 0; z < dims.getDimension(2); z++) {
          for (int wally = 0; wally < dims.getDimension(3); wally++) {
            boolean rv = grid.moveTo(list.get(i++), x, y, z, wally);
            assertTrue(rv);
          }
        }
      }
    }

    i = 0;
    for (int x = 0; x < dims.getDimension(0); x++) {
      for (int y = 0; y < dims.getDimension(1); y++) {
        for (int z = 0; z < dims.getDimension(2); z++) {
          for (int wally = 0; wally < dims.getDimension(3); wally++) {
            Integer o = grid.getObjectAt(x, y, z, wally);
            assertEquals(o, list.get(i++));
          }
        }
      }
    }
  }

  class Listener implements ProjectionListener {

    boolean added, removed, moved;

    Object obj;

    public void projectionEventOccurred(ProjectionEvent evt) {
      added = evt.getType() == ProjectionEvent.OBJECT_ADDED;
      removed = evt.getType() == ProjectionEvent.OBJECT_REMOVED;
      moved = evt.getType() == ProjectionEvent.OBJECT_MOVED;
      obj = evt.getSubject();
    }

    public void reset() {
      added = removed = moved = false;
      obj = null;
    }
  }

  public void testListener() {
    Grid<Integer> grid = context.getProjection(Grid.class, "2D Grid");
    Listener listener = new Listener();
    grid.addProjectionListener(listener);
    Integer ten = new Integer(10);

    context.add(ten);
    assertTrue(listener.added);
    assertTrue(!listener.removed);
    assertTrue(!listener.moved);
    assertEquals(ten, listener.obj);
    listener.reset();

    grid.moveTo(ten, 3, 3);
    assertTrue(!listener.added);
    assertTrue(!listener.removed);
    assertTrue(listener.moved);
    assertEquals(ten, listener.obj);
    listener.reset();

    grid.moveTo(ten, 3, 3);
    assertTrue(!listener.added);
    assertTrue(!listener.removed);
    assertTrue(!listener.moved);
    assertEquals(null, listener.obj);
    listener.reset();

    context.remove(ten);
    assertTrue(!listener.added);
    assertTrue(listener.removed);
    assertTrue(!listener.moved);
    assertEquals(ten, listener.obj);
  }

  public void test2DVNGridQuery() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy2D(
        new SimpleGridAdder<Integer>(), new StrictBorders(), 10, 30);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Grid", context,
        params);

    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        Integer val = new Integer(i++);
        context.add(val);
        grid.moveTo(val, x, y);
      }
    }

    nghs.add(new Integer(3));
    nghs.add(new Integer(33));
    nghs.add(new Integer(93));
    nghs.add(new Integer(123));
    nghs.add(new Integer(62));
    nghs.add(new Integer(64));
    nghs.add(new Integer(64));

    VNQuery<Integer> query = new VNQuery<Integer>(grid, 63, 2, 1);
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());

    nghs.clear();
    nghs.add(new Integer(1));
    nghs.add(new Integer(2));
    nghs.add(new Integer(30));

    query.reset(0, 1, 2);
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());

    nghs.clear();
    nghs.add(181);
    nghs.add(211);
    nghs.add(271);
    nghs.add(240);
    nghs.add(242);
    nghs.add(243);

    query.reset(241, 2, 2);
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());

    nghs.clear();
    nghs.add(28);
    nghs.add(59);

    query.reset(29);
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());
  }

  public void test2DVNContains() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy2D(
        new SimpleGridAdder<Integer>(), new StrictBorders(), 10, 30);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Grid", context,
        params);

    Set<Integer> all = new HashSet<Integer>();
    Set<Integer> working = new HashSet<Integer>();
    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        Integer val = new Integer(i++);
        context.add(val);
        all.add(val);
        grid.moveTo(val, x, y);
      }
    }

    VNContains<Integer> contains = new VNContains<Integer>(grid);

    nghs.add(new Integer(3));
    nghs.add(new Integer(33));
    nghs.add(new Integer(93));
    nghs.add(new Integer(123));
    nghs.add(new Integer(62));
    nghs.add(new Integer(64));
    nghs.add(new Integer(64));

    working.addAll(all);
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(63, integer, 2, 1));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(63, integer, 2, 1));
    }

    nghs.clear();
    working.clear();
    working.addAll(all);
    nghs.add(new Integer(1));
    nghs.add(new Integer(2));
    nghs.add(new Integer(30));
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(0, integer, 1, 2));
    }

    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(0, integer, 1, 2));
    }

    nghs.clear();
    working.clear();
    working.addAll(all);
    nghs.add(181);
    nghs.add(211);
    nghs.add(271);
    nghs.add(240);
    nghs.add(242);
    nghs.add(243);
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(241, integer, 2, 2));
    }

    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(241, integer, 2, 2));
    }

    nghs.clear();
    working.clear();
    working.addAll(all);
    nghs.add(28);
    nghs.add(59);
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(29, integer, 1, 1));
    }

    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(29, integer, 1, 1));
    }
  }

  public void test1DTorusVNQuery() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy1DTorus(
        new SimpleGridAdder<Integer>(), 10);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("1D Torus", context,
        params);

    Set<Integer> all = new HashSet<Integer>();
    Set<Integer> working = new HashSet<Integer>();
    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      Integer val = new Integer(i++);
      context.add(val);
      grid.moveTo(val, x);
    }

    nghs.add(new Integer(0));
    nghs.add(new Integer(2));

    working.addAll(all);
    working.removeAll(nghs);

    VNQuery<Integer> query = new VNQuery<Integer>(grid, 1, 1);
    // remove all the vals returned from the query
    // checking to see that those vals are in nghs.
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());

    nghs.clear();
    working.clear();
    working.addAll(all);
    working.removeAll(nghs);

    nghs.add(new Integer(0));
    nghs.add(new Integer(2));
    nghs.add(new Integer(9));
    nghs.add(new Integer(3));

    query.reset(1, 2);
    // remove all the vals returned from the query
    // checking to see that those vals are in nghs.
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());
  }

  public void test2DTorusVNQuery() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy2DTorus(
        new SimpleGridAdder<Integer>(), 10, 30);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Torus", context,
        params);

    Set<Integer> nghs = new HashSet<Integer>();

    // this creates a grid like:
    /*
     * 0 |30|..|270 1 |31|..|.. 2 |32|..|.. 3 |33|..|.. ..|..|..|.. 29|59|..|299
     */
    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        Integer val = new Integer(i++);
        context.add(val);
        grid.moveTo(val, x, y);
      }
    }

    nghs.add(3);
    nghs.add(33);
    nghs.add(93);
    nghs.add(123);
    nghs.add(62);
    nghs.add(64);

    VNQuery<Integer> query = new VNQuery<Integer>(grid, 63, 2, 1);
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());

    nghs.clear();
    nghs.add(1);
    nghs.add(2);
    nghs.add(29);
    nghs.add(28);
    nghs.add(30);
    nghs.add(270);

    query.reset(0, 1, 2);
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());
  }

  public void test1DVNContains() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy1D(
        new SimpleGridAdder<Integer>(), new StrictBorders(), 10);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("1D Grid", context,
        params);

    Set<Integer> all = new HashSet<Integer>();
    Set<Integer> working = new HashSet<Integer>();
    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      Integer val = new Integer(i++);
      context.add(val);
      grid.moveTo(val, x);
    }

    nghs.add(new Integer(4));
    nghs.add(new Integer(6));

    working.addAll(all);
    working.removeAll(nghs);

    VNContains<Integer> contains = new VNContains<Integer>(grid);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(5, integer, 1));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(5, integer, 1));
    }

    nghs.clear();
    working.clear();
    working.addAll(all);
    nghs.add(5);
    nghs.add(6);
    nghs.add(8);
    nghs.add(9);
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(7, integer, 2));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(7, integer, 2));
    }
  }

  public void test1DTorusVNContains() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy1DTorus(
        new SimpleGridAdder<Integer>(), 10);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("1D Torus", context,
        params);

    Set<Integer> all = new HashSet<Integer>();
    Set<Integer> working = new HashSet<Integer>();
    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      Integer val = new Integer(i++);
      context.add(val);
      grid.moveTo(val, x);
    }

    nghs.add(new Integer(0));
    nghs.add(new Integer(2));

    working.addAll(all);
    working.removeAll(nghs);

    VNContains<Integer> contains = new VNContains<Integer>(grid);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(1, integer, 1));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(1, integer, 1));
    }

    nghs.clear();
    working.clear();
    working.addAll(all);
    working.removeAll(nghs);

    nghs.add(new Integer(0));
    nghs.add(new Integer(2));
    nghs.add(new Integer(9));
    nghs.add(new Integer(3));

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(1, integer, 2));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(1, integer, 2));
    }
  }

  public void test2DTorusVNContains() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy2DTorus(
        new SimpleGridAdder<Integer>(), 10, 30);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Torus", context,
        params);

    Set<Integer> all = new HashSet<Integer>();
    Set<Integer> working = new HashSet<Integer>();
    Set<Integer> nghs = new HashSet<Integer>();

    // this creates a grid like:
    /*
     * 0 |30|..|270 1 |31|..|.. 2 |32|..|.. 3 |33|..|.. ..|..|..|.. 29|59|..|299
     */
    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        Integer val = new Integer(i++);
        all.add(val);
        context.add(val);
        grid.moveTo(val, x, y);
      }
    }

    VNContains<Integer> contains = new VNContains<Integer>(grid);

    nghs.add(3);
    nghs.add(33);
    nghs.add(93);
    nghs.add(123);
    nghs.add(62);
    nghs.add(64);

    working.addAll(all);
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(63, integer, 2, 1));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(63, integer, 2, 1));
    }

    nghs.clear();
    working.clear();
    working.addAll(all);
    nghs.add(1);
    nghs.add(2);
    nghs.add(29);
    nghs.add(28);
    nghs.add(30);
    nghs.add(270);
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(0, integer, 1, 2));
    }

    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(0, integer, 1, 2));
    }
  }

  public void test1DMooreContains() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy1D(
        new SimpleGridAdder<Integer>(), new StrictBorders(), 10);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("1D Grid", context,
        params);

    Set<Integer> all = new HashSet<Integer>();
    Set<Integer> working = new HashSet<Integer>();
    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      Integer val = new Integer(i++);
      context.add(val);
      grid.moveTo(val, x);
    }

    nghs.add(new Integer(4));
    nghs.add(new Integer(6));

    working.addAll(all);
    working.removeAll(nghs);

    MooreContains<Integer> contains = new MooreContains<Integer>(grid);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(5, integer, 1));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(5, integer, 1));
    }

    nghs.clear();
    working.clear();
    working.addAll(all);
    nghs.add(5);
    nghs.add(6);
    nghs.add(8);
    nghs.add(9);
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(7, integer, 2));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(7, integer, 2));
    }
  }

  public void test1DTorusMooreContains() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy1DTorus(
        new SimpleGridAdder<Integer>(), 10);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("1D Torus", context,
        params);

    Set<Integer> all = new HashSet<Integer>();
    Set<Integer> working = new HashSet<Integer>();
    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      Integer val = new Integer(i++);
      context.add(val);
      grid.moveTo(val, x);
    }

    nghs.add(new Integer(0));
    nghs.add(new Integer(2));

    working.addAll(all);
    working.removeAll(nghs);

    MooreContains<Integer> contains = new MooreContains<Integer>(grid);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(1, integer, 1));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(1, integer, 1));
    }

    nghs.clear();
    working.clear();
    working.addAll(all);
    working.removeAll(nghs);

    nghs.add(new Integer(0));
    nghs.add(new Integer(2));
    nghs.add(new Integer(9));
    nghs.add(new Integer(3));

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(1, integer, 2));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(1, integer, 2));
    }
  }

  public void test2DMooreContains() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy2D(
        new SimpleGridAdder<Integer>(), new StrictBorders(), 10, 30);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Grid", context,
        params);

    Set<Integer> all = new HashSet<Integer>();
    Set<Integer> working = new HashSet<Integer>();
    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        Integer val = new Integer(i++);
        context.add(val);
        grid.moveTo(val, x, y);
      }
    }

    nghs.add(new Integer(3));
    nghs.add(new Integer(33));
    nghs.add(new Integer(93));
    nghs.add(new Integer(123));
    nghs.add(new Integer(62));
    nghs.add(new Integer(64));
    nghs.add(new Integer(2));
    nghs.add(new Integer(32));
    nghs.add(new Integer(92));
    nghs.add(new Integer(122));
    nghs.add(new Integer(4));
    nghs.add(new Integer(34));
    nghs.add(new Integer(94));
    nghs.add(new Integer(124));

    working.addAll(all);
    working.removeAll(nghs);

    MooreContains<Integer> contains = new MooreContains<Integer>(grid);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(63, integer, 2, 1));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(63, integer, 2, 1));
    }

    nghs.clear();
    working.clear();
    working.addAll(all);
    nghs.add(1);
    nghs.add(2);
    nghs.add(30);
    nghs.add(31);
    nghs.add(32);
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(0, integer, 1, 2));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(0, integer, 1, 2));
    }

    nghs.clear();
    working.clear();
    working.addAll(all);
    nghs.add(181);
    nghs.add(211);
    nghs.add(271);
    nghs.add(240);
    nghs.add(242);
    nghs.add(243);
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(241, integer, 2, 2));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(241, integer, 2, 2));
    }

    nghs.clear();
    working.clear();
    working.addAll(all);
    nghs.add(28);
    nghs.add(59);
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(29, integer, 1, 1));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(29, integer, 1, 1));
    }
  }

  public void test2DTorusMooreContains() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy2DTorus(
        new SimpleGridAdder<Integer>(), 10, 30);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Torus", context,
        params);

    Set<Integer> all = new HashSet<Integer>();
    Set<Integer> working = new HashSet<Integer>();
    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        Integer val = new Integer(i++);
        all.add(val);
        context.add(val);
        grid.moveTo(val, x, y);
      }
    }

    nghs.add(new Integer(3));
    nghs.add(new Integer(33));
    nghs.add(new Integer(93));
    nghs.add(new Integer(123));
    nghs.add(new Integer(62));
    nghs.add(new Integer(64));
    nghs.add(new Integer(2));
    nghs.add(new Integer(32));
    nghs.add(new Integer(92));
    nghs.add(new Integer(122));
    nghs.add(new Integer(4));
    nghs.add(new Integer(34));
    nghs.add(new Integer(94));
    nghs.add(new Integer(124));

    working.addAll(all);
    working.removeAll(nghs);

    MooreContains<Integer> contains = new MooreContains<Integer>(grid);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(63, integer, 2, 1));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(63, integer, 2, 1));
    }

    nghs.clear();
    working.clear();
    working.addAll(all);
    nghs.add(1);
    nghs.add(2);
    nghs.add(30);
    nghs.add(31);
    nghs.add(32);
    nghs.add(270);
    nghs.add(271);
    nghs.add(272);
    nghs.add(298);
    nghs.add(299);
    nghs.add(28);
    nghs.add(29);
    nghs.add(58);
    nghs.add(59);
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(0, integer, 1, 2));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(0, integer, 1, 2));
    }
  }

  private int getVal(int x, int y, int z) {
    return x * 150 + y * 5 + z;
  }

  public void test3DVNGridQuery() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancyND(
        new SimpleGridAdder<Integer>(), new StrictBorders(), 10, 30, 5);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Grid", context,
        params);

    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        for (int z = 0; z < 5; z++) {
          Integer val = new Integer(i++);
          context.add(val);
          grid.moveTo(val, x, y, z);
        }
      }
    }

    // 527 at 3, 15, 2
    nghs.add(getVal(1, 15, 2));
    nghs.add(getVal(2, 15, 2));
    nghs.add(getVal(4, 15, 2));
    nghs.add(getVal(5, 15, 2));
    nghs.add(getVal(3, 13, 2));
    nghs.add(getVal(3, 14, 2));
    nghs.add(getVal(3, 16, 2));
    nghs.add(getVal(3, 17, 2));
    nghs.add(getVal(3, 15, 1));
    nghs.add(getVal(3, 15, 3));

    VNQuery<Integer> query = new VNQuery<Integer>(grid, 527, 2, 2, 1);
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());
  }

  public void test3DVNGridQueryWithSet() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancyND(
        new SimpleGridAdder<Integer>(), new StrictBorders(), 10, 30, 5);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Grid", context,
        params);

    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        for (int z = 0; z < 5; z++) {
          Integer val = new Integer(i++);
          context.add(val);
          grid.moveTo(val, x, y, z);
        }
      }
    }

    // 527 at 3, 15, 2
    // only add a subset of the nghs, not all of them.
    nghs.add(getVal(1, 15, 2));
    nghs.add(getVal(2, 15, 2));
    nghs.add(getVal(4, 15, 2));
    nghs.add(getVal(5, 15, 2));

    VNQuery<Integer> query = new VNQuery<Integer>(grid, 527, 2, 2, 1);
    for (Integer val : query.query(nghs)) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());
  }

  public void test3DVNContains() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancyND(
        new SimpleGridAdder<Integer>(), new StrictBorders(), 10, 30, 5);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Grid", context,
        params);

    Set<Integer> all = new HashSet<Integer>();
    Set<Integer> working = new HashSet<Integer>();
    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        for (int z = 0; z < 5; z++) {
          Integer val = new Integer(i++);
          context.add(val);
          all.add(val);
          grid.moveTo(val, x, y, z);
        }
      }
    }

    VNContains<Integer> contains = new VNContains<Integer>(grid);

    // 527 at 3, 15, 2
    nghs.add(getVal(1, 15, 2));
    nghs.add(getVal(2, 15, 2));
    nghs.add(getVal(4, 15, 2));
    nghs.add(getVal(5, 15, 2));
    nghs.add(getVal(3, 13, 2));
    nghs.add(getVal(3, 14, 2));
    nghs.add(getVal(3, 16, 2));
    nghs.add(getVal(3, 17, 2));
    nghs.add(getVal(3, 15, 1));
    nghs.add(getVal(3, 15, 3));

    working.addAll(all);
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(527, integer, 2, 2, 1));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(527, integer, 2, 2, 1));
    }
  }

  public void test3DMooreContains() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancyND(
        new SimpleGridAdder<Integer>(), new StrictBorders(), 10, 30, 5);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Grid", context,
        params);

    Set<Integer> all = new HashSet<Integer>();
    Set<Integer> working = new HashSet<Integer>();
    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        for (int z = 0; z < 5; z++) {
          Integer val = new Integer(i++);
          context.add(val);
          all.add(val);
          grid.moveTo(val, x, y, z);
        }
      }
    }

    MooreContains<Integer> contains = new MooreContains<Integer>(grid);

    // 527 at 3, 15, 2
    for (int x = 1; x < 6; x++) {
      for (int y = 13; y < 18; y++) {
        for (int z = 1; z < 4; z++) {
          if (!(x == 3 && y == 15 && z == 2)) {
            nghs.add(getVal(x, y, z));
          }
        }
      }
    }
    working.addAll(all);
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer, contains.isNeighbor(527, integer, 2, 2, 1));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(527, integer, 2, 2, 1));
    }
  }

  public void test1DTorusMooreQuery() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy1DTorus(
        new SimpleGridAdder<Integer>(), 10);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("1D Torus", context,
        params);

    Set<Integer> all = new HashSet<Integer>();
    Set<Integer> working = new HashSet<Integer>();
    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      Integer val = new Integer(i++);
      context.add(val);
      grid.moveTo(val, x);
    }

    nghs.add(new Integer(0));
    nghs.add(new Integer(2));

    working.addAll(all);
    working.removeAll(nghs);

    MooreQuery<Integer> query = new MooreQuery<Integer>(grid, 1, 1);
    // remove all the vals returned from the query
    // checking to see that those vals are in nghs.
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());

    nghs.clear();
    working.clear();
    working.addAll(all);
    working.removeAll(nghs);

    nghs.add(new Integer(0));
    nghs.add(new Integer(2));
    nghs.add(new Integer(9));
    nghs.add(new Integer(3));

    query.reset(1, 2);
    // remove all the vals returned from the query
    // checking to see that those vals are in nghs.
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());
  }

  // this specifically tests for a problem where the queried object
  // is at y == 0 and x > 0.
  public void test2DStrictMooreQuery() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy2D(
        new SimpleGridAdder<Integer>(), new StrictBorders(), 3, 3);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Grid", context,
        params);

    Set<Integer> nghs = new HashSet<Integer>();
    
    int i = 0;
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        Integer val = new Integer(i);
        i++;
        context.add(val);
        grid.moveTo(val, x, y);
      }
    }
    
    nghs.add(new Integer(0));
    nghs.add(new Integer(2));
    nghs.add(new Integer(3));
    nghs.add(new Integer(4));
    nghs.add(new Integer(5));

    // get nghs of (1, 0)
    MooreQuery<Integer> query = new MooreQuery<Integer>(grid, new Integer(1), 1, 1);
    // remove all the vals returned from the query
    // checking to see that those vals are in nghs.
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());
  }

  public void test2DTorusMooreQuery() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancy2DTorus(
        new SimpleGridAdder<Integer>(), 10, 30);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Torus", context,
        params);

    Set<Integer> all = new HashSet<Integer>();
    Set<Integer> working = new HashSet<Integer>();
    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        Integer val = new Integer(i++);
        all.add(val);
        context.add(val);
        grid.moveTo(val, x, y);
      }
    }

    nghs.add(new Integer(3));
    nghs.add(new Integer(33));
    nghs.add(new Integer(93));
    nghs.add(new Integer(123));
    nghs.add(new Integer(62));
    nghs.add(new Integer(64));
    nghs.add(new Integer(2));
    nghs.add(new Integer(32));
    nghs.add(new Integer(92));
    nghs.add(new Integer(122));
    nghs.add(new Integer(4));
    nghs.add(new Integer(34));
    nghs.add(new Integer(94));
    nghs.add(new Integer(124));

    working.addAll(all);
    working.removeAll(nghs);

    MooreQuery<Integer> query = new MooreQuery<Integer>(grid, 63, 2, 1);
    // remove all the vals returned from the query
    // checking to see that those vals are in nghs.
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());

    nghs.clear();
    working.clear();
    working.addAll(all);
    nghs.add(1);
    nghs.add(2);
    nghs.add(30);
    nghs.add(31);
    nghs.add(32);
    nghs.add(270);
    nghs.add(271);
    nghs.add(272);
    nghs.add(298);
    nghs.add(299);
    nghs.add(28);
    nghs.add(29);
    nghs.add(58);
    nghs.add(59);
    working.removeAll(nghs);

    query.reset(0, 1, 2);
    // remove all the vals returned from the query
    // checking to see that those vals are in nghs.
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());
  }

  public void test3DMooreQuery() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancyNDTorus(
        new SimpleGridAdder<Integer>(), 10, 30, 5);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("3D Grid", context,
        params);

    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        for (int z = 0; z < 5; z++) {
          Integer val = new Integer(i++);
          context.add(val);
          grid.moveTo(val, x, y, z);
        }
      }
    }

    // calc the nghs of 1354
    // 1354 at 9, 0, 4 -- extent 2, 2, 2
    int rx, ry, rz;
    for (int x = 7; x < 12; x++) {
      rx = x;
      if (x > 9)
        rx = x % 10;
      for (int y = -2; y < 3; y++) {
        ry = y;
        if (y < 0)
          ry = 30 + y;
        for (int z = 3; z < 8; z++) {
          rz = z;
          if (z > 4)
            rz = z % 5;
          if (!(x == 9 && y == 0 && z == 4)) {
            nghs.add(getVal(rx, ry, rz));
          }
        }
      }
    }

    MooreQuery<Integer> query = new MooreQuery<Integer>(grid, 1354, 2, 2, 2);
    // remove all the vals returned from the query
    // checking to see that those vals are in nghs.
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());

    // calc the nghs of 1200
    // at 8, 0, 0 -- extent 2, 2, 2
    for (int x = 6; x < 11; x++) {
      rx = x;
      if (x > 9)
        rx = x % 10;
      for (int y = -2; y < 3; y++) {
        ry = y;
        if (y < 0)
          ry = 30 + y;
        for (int z = -2; z < 3; z++) {
          rz = z;
          if (z < 0)
            rz = 5 + z;
          if (!(x == 8 && y == 0 && z == 0)) {
            nghs.add(getVal(rx, ry, rz));
          }
        }
      }
    }

    query.reset(1200, 2, 2, 2);
    // remove all the vals returned from the query
    // checking to see that those vals are in nghs.
    for (Integer val : query.query()) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());

  }

  public void test3DMooreQueryWithSet() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancyNDTorus(
        new SimpleGridAdder<Integer>(), 10, 30, 5);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("3D Grid", context,
        params);

    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        for (int z = 0; z < 5; z++) {
          Integer val = new Integer(i++);
          context.add(val);
          grid.moveTo(val, x, y, z);
        }
      }
    }

    // calc the nghs of 1354
    // 1354 at 9, 0, 4 -- extent 2, 2, 2
    int rx, ry, rz;
    int count = 0;
    for (int x = 7; x < 12; x++) {
      rx = x;
      if (x > 9)
        rx = x % 10;
      for (int y = -2; y < 3; y++) {
        ry = y;
        if (y < 0)
          ry = 30 + y;
        for (int z = 3; z < 8; z++) {
          rz = z;
          if (z > 4)
            rz = z % 5;
          if (!(x == 9 && y == 0 && z == 4)) {
            // only add a few and query with that set
            // passed in

            if (count++ < 4)
              nghs.add(getVal(rx, ry, rz));
          }
        }
      }
    }

    MooreQuery<Integer> query = new MooreQuery<Integer>(grid, 1354, 2, 2, 2);
    // remove all the vals returned from the query
    // checking to see that those vals are in nghs.
    for (Integer val : query.query(nghs)) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());

    // calc the nghs of 1200
    // at 8, 0, 0 -- extent 2, 2, 2
    count = 0;
    for (int x = 6; x < 11; x++) {
      rx = x;
      if (x > 9)
        rx = x % 10;
      for (int y = -2; y < 3; y++) {
        ry = y;
        if (y < 0)
          ry = 30 + y;
        for (int z = -2; z < 3; z++) {
          rz = z;
          if (z < 0)
            rz = 5 + z;
          if (!(x == 8 && y == 0 && z == 0)) {
            if (count++ < 4)
              nghs.add(getVal(rx, ry, rz));
          }
        }
      }
    }

    query.reset(1200, 2, 2, 2);
    // remove all the vals returned from the query
    // checking to see that those vals are in nghs.
    for (Integer val : query.query(nghs)) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());

  }

  public void test3DMooreGridQueryWithSet() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancyND(
        new SimpleGridAdder<Integer>(), new StrictBorders(), 10, 30, 5);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Grid", context,
        params);

    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        for (int z = 0; z < 5; z++) {
          Integer val = new Integer(i++);
          context.add(val);
          grid.moveTo(val, x, y, z);
        }
      }
    }

    int count = 0;
    // 527 at 3, 15, 2
    for (int x = 1; x < 6; x++) {
      for (int y = 13; y < 18; y++) {
        for (int z = 1; z < 4; z++) {
          if (!(x == 3 && y == 15 && z == 2)) {
            if (count++ < 4)
              nghs.add(getVal(x, y, z));
          }
        }
      }
    }

    MooreQuery<Integer> query = new MooreQuery<Integer>(grid, 527, 2, 2, 1);
    // remove all the vals returned from the query
    // checking to see that those vals are in nghs.
    for (Integer val : query.query(nghs)) {
      assertTrue(nghs.remove(val));
    }
    assertEquals(0, nghs.size());

  }

  public void test3DTorusMooreContains() {
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancyNDTorus(
        new SimpleGridAdder<Integer>(), 10, 30, 5);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("2D Grid", context,
        params);

    Set<Integer> all = new HashSet<Integer>();
    Set<Integer> working = new HashSet<Integer>();
    Set<Integer> nghs = new HashSet<Integer>();

    int i = 0;
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 30; y++) {
        for (int z = 0; z < 5; z++) {
          Integer val = new Integer(i++);
          context.add(val);
          all.add(val);
          grid.moveTo(val, x, y, z);
        }
      }
    }

    MooreContains<Integer> contains = new MooreContains<Integer>(grid);

    // 1354 at 9, 0, 4 -- extent 2, 2, 2
    int rx, ry, rz;
    for (int x = 7; x < 12; x++) {
      rx = x;
      if (x > 9)
        rx = x % 10;
      for (int y = -2; y < 3; y++) {
        ry = y;
        if (y < 0)
          ry = 30 + y;
        for (int z = 3; z < 8; z++) {
          rz = z;
          if (z > 4)
            rz = z % 5;
          if (!(x == 9 && y == 0 && z == 4)) {
            nghs.add(getVal(rx, ry, rz));
          }
        }
      }
    }
    working.addAll(all);
    working.removeAll(nghs);

    for (Integer integer : nghs) {
      assertTrue("Val: " + integer + ", " + grid.getLocation(integer),
          contains.isNeighbor(1354, integer, 2, 2, 2));
    }
    for (Integer integer : working) {
      assertTrue("Val: " + integer, !contains.isNeighbor(1354, integer, 2, 2, 2));
    }
  }

  public void testWithinPredicate() {
    Context<Integer> context = new DefaultContext<Integer>();
    for (int i = 0; i < 10; i++) {
      context.add(i);
    }

    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("grid", context,
        GridBuilderParameters.singleOccupancy2DTorus(new SimpleGridAdder<Integer>(), 30, 30));
    grid.moveTo(1, 10, 10);
    grid.moveTo(2, 10, 11);

    Within within = new Within(1, 2, 3);
    assertTrue(grid.evaluate(within));

    grid.moveTo(1, 29, 29);
    assertFalse(grid.evaluate(within));
  }

  public void testWithinVNPredicate() {
    Context<Integer> context = new DefaultContext<Integer>();
    for (int i = 0; i < 10; i++) {
      context.add(i);
    }

    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("grid", context,
        GridBuilderParameters.singleOccupancy2DTorus(new SimpleGridAdder<Integer>(), 30, 30));
    grid.moveTo(1, 10, 10);
    grid.moveTo(2, 10, 12);

    WithinVN within = new WithinVN(1, 2, 3);
    assertTrue(grid.evaluate(within));

    grid.moveTo(2, 11, 11);
    assertFalse(grid.evaluate(within));

    grid.moveTo(2, 6, 10);
    assertFalse(grid.evaluate(within));
  }

  public void testWithinMoorePredicate() {
    Context<Integer> context = new DefaultContext<Integer>();
    for (int i = 0; i < 10; i++) {
      context.add(i);
    }

    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("grid", context,
        GridBuilderParameters.singleOccupancy2DTorus(new SimpleGridAdder<Integer>(), 30, 30));
    grid.moveTo(1, 10, 10);
    grid.moveTo(2, 12, 12);

    WithinMoore within = new WithinMoore(1, 2, 3);
    assertTrue(grid.evaluate(within));

    grid.moveTo(2, 14, 11);
    assertFalse(grid.evaluate(within));

    grid.moveTo(2, 6, 10);
    assertFalse(grid.evaluate(within));
  }

  public void testWithin3() {
    Context<String> context = new DefaultContext<String>();
    context.add("A");
    context.add("B");
    context.add("C");
    context.add("D");

    context.add("X");
    context.add("Y");
    context.add("Z");

    Grid<String> grid = GridFactoryFinder.createGridFactory(null).createGrid(
        "grid",
        context,
        new GridBuilderParameters<String>(new StrictBorders(), new RandomGridAdder<String>(), true,
            50, 50));
    grid.moveTo("A", 0, 49);
    grid.moveTo("B", 49, 0);
    grid.moveTo("C", 0, 0);
    grid.moveTo("D", 49, 49);

    grid.moveTo("X", 1, 48);
    grid.moveTo("Y", 3, 49);
    grid.moveTo("Z", 0, 45);

    Set<String> expected = new HashSet<String>();
    expected.add("X");
    expected.add("Y");

    GridWithin<String> query = new GridWithin<String>(context, "A", 3);
    for (String val : query.query()) {
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());

    grid.moveTo("X", 49, 2);
    grid.moveTo("Y", 47, 1);
    grid.moveTo("Z", 46, 3);

    expected.add("X");
    expected.add("Y");

    query = new GridWithin<String>(context, "B", 3);
    for (String val : query.query()) {
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());

    grid.moveTo("X", 0, 2);
    grid.moveTo("Y", 1, 0);
    grid.moveTo("Z", 5, 0);

    expected.add("X");
    expected.add("Y");

    query = new GridWithin<String>(context, "C", 3);
    for (String val : query.query()) {
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());

    grid.moveTo("X", 48, 48);
    grid.moveTo("Y", 48, 49);
    grid.moveTo("Z", 45, 49);

    expected.add("X");
    expected.add("Y");

    query = new GridWithin<String>(context, "D", 3);
    for (String val : query.query()) {
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());

  }

  public void testWithin2() {
    Context<Integer> context = new DefaultContext<Integer>();
    for (int i = 0; i < 4; i++) {
      context.add(i);
    }

    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid(
        "grid",
        context,
        GridBuilderParameters.singleOccupancy1D(new SimpleGridAdder<Integer>(),
            new StrictBorders(), 20));

    grid.moveTo(0, 1);
    grid.moveTo(1, 0);
    grid.moveTo(3, 2);
    grid.moveTo(2, 10);

    Set<Integer> expected = new HashSet<Integer>();
    expected.add(0);
    expected.add(3);

    GridWithin<Integer> query = new GridWithin<Integer>(context, 1, 2);
    for (Integer val : query.query()) {
      assertTrue("val: " + val, expected.remove(val));
    }
    assertEquals(0, expected.size());

    grid = GridFactoryFinder.createGridFactory(null).createGrid("gridA", context,
        GridBuilderParameters.singleOccupancy1DTorus(new SimpleGridAdder<Integer>(), 20));

    grid.moveTo(0, 1);
    grid.moveTo(1, 0);
    grid.moveTo(3, 2);
    grid.moveTo(2, 19);

    expected = new HashSet<Integer>();
    expected.add(0);
    expected.add(3);
    expected.add(2);

    query = new GridWithin<Integer>(grid, 1, 2);
    for (Integer val : query.query()) {
      assertTrue("val: " + val, expected.remove(val));
    }
    assertEquals(0, expected.size());
  }

  public void testWithin() {
    Context<Integer> context = new DefaultContext<Integer>();
    for (int i = 0; i < 10; i++) {
      context.add(i);
    }

    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("gridB", context,
        GridBuilderParameters.singleOccupancy2DTorus(new SimpleGridAdder<Integer>(), 30, 30));
    grid.moveTo(0, 3, 3);
    grid.moveTo(1, 4, 4);
    grid.moveTo(2, 4, 3);
    grid.moveTo(3, 10, 10);
    for (int i = 4; i < 10; i++) {
      assertTrue(grid.moveTo(i, 15 + i, 20));
    }

    Set<Integer> expected = new HashSet<Integer>();
    double dist = 10;
    GridPoint origin = grid.getLocation(1);
    for (int i = 0; i < 10; i++) {
      GridPoint point = grid.getLocation(i);
      if (grid.getDistanceSq(origin, point) <= dist * dist) {
        expected.add(i);
      }
    }

    // remove the source object itself.
    expected.remove(1);

    GridWithin<Integer> query = new GridWithin<Integer>(context, 1, 10);
    for (Integer val : query.query()) {
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());

    expected = new HashSet<Integer>();
    for (int i = 0; i < 10; i++) {
      GridPoint point = grid.getLocation(i);
      if (grid.getDistanceSq(origin, point) <= dist * dist) {
        expected.add(i);
      }
    }

    // remove the source object itself.
    expected.remove(1);

    query = new GridWithin<Integer>(grid, 1, 10);
    for (Integer val : query.query()) {
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());

    expected.add(3);
    for (Integer val : query.query(expected)) {
      assertTrue(expected.remove(val));
    }
    assertEquals(0, expected.size());
  }

  public void testEdges() {
    Context<Integer> context = new DefaultContext<Integer>();
    context.add(1);

    // defaults to strict borders
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancyND(
        new SimpleGridAdder<Integer>(), new StrictBorders(), 10, 30);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("gridA", context,
        params);

    boolean thrown = false;
    try {
      grid.moveTo(1, 10, 30);
    } catch (SpatialException ex) {
      thrown = true;
    }
    assertTrue("Exception thrown", thrown);

    assertTrue(grid.moveTo(1, 8, 28));
    GridPoint point = grid.moveByDisplacement(1, 1, 1);
    assertEquals(point.getX(), 9);
    assertEquals(point.getY(), 29);

    grid.moveTo(1, 2, 2);
    thrown = false;
    try {
      point = grid.moveByDisplacement(1, -3, -3);
    } catch (SpatialException ex) {
      thrown = true;
    }
    assertTrue("Exception thrown", thrown);

    params = new GridBuilderParameters<Integer>(new StickyBorders(),
        new SimpleGridAdder<Integer>(), false, 10, 30);
    grid = GridFactoryFinder.createGridFactory(null).createGrid("gridB", context, params);

    thrown = false;
    try {
      grid.moveTo(1, 10, 30);
    } catch (SpatialException ex) {
      thrown = true;
    }
    assertTrue("Exception thrown", thrown);

    grid.moveTo(1, 8, 28);
    point = grid.moveByDisplacement(1, 2, 1);
    assertEquals(point.getX(), 9);
    assertEquals(point.getY(), 29);

    grid.moveTo(1, 2, 2);
    point = grid.moveByDisplacement(1, -3, -3);
    assertEquals(point.getX(), 0);
    assertEquals(point.getY(), 0);

  }

  public void testBadMove() {
    Context<Integer> context = new DefaultContext<Integer>();
    context.add(1);

    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancyND(
        new SimpleGridAdder<Integer>(), new StickyBorders(), 10, 30);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("Grid", context,
        params);

    boolean thrown = false;
    try {
      grid.moveTo(1, 100, 212);
    } catch (SpatialException ex) {
      thrown = true;
    }
    assertTrue("Exception thrown", thrown);
  }

  public void testPredicates() {
    // this is less to see if the predicate queries are working than
    // if the predicate itself dispatches correctly, so we can do
    // some simple tests.
    Context<Integer> context = new DefaultContext<Integer>();
    GridBuilderParameters<Integer> params = GridBuilderParameters.singleOccupancyND(
        new SimpleGridAdder<Integer>(), new StrictBorders(), 10, 30, 5);
    Grid<Integer> grid = GridFactoryFinder.createGridFactory(null).createGrid("Grid", context,
        params);

    for (int i = 0; i < 5; i++) {
      context.add(i);
    }

    // grid.move(1, 5, 5, 5);
    // grid.move(2, 4, 5, 6);

    // WithinVN withinVN = new WithinVN(1, 2, 1);
    // assertFalse(grid.evaluate(withinVN));

  }

  public static junit.framework.Test suite() {
    return new TestSuite(GridTest.class);
  }

  public void testWrappedBorders() {
    Grid<Integer> grid1 = GridFactoryFinder.createGridFactory(null).createGrid("grid1", context,
        GridBuilderParameters.singleOccupancy2DTorus(new SimpleGridAdder<Integer>(), 50, 50));
    grid1.moveTo(1, 50, 50);
    GridPoint point = grid1.getLocation(1);
    assertEquals(0, point.getX());
    assertEquals(0, point.getY());

    grid1.moveTo(1, 0, 49);

    point = grid1.moveByDisplacement(1, -1, 0);
    assertEquals(49, point.getX());
    assertEquals(49, point.getY());

    point = grid1.moveByDisplacement(1, 0, 1);
    assertEquals(49, point.getX());
    assertEquals(0, point.getY());

    grid1.moveTo(1, -1, -1);
    point = grid1.getLocation(1);
    assertEquals(49, point.getX());
    assertEquals(49, point.getY());

    grid1.moveTo(1, -103, -152);
    point = grid1.getLocation(1);
    assertEquals(47, point.getX());
    assertEquals(48, point.getY());
  }
}
