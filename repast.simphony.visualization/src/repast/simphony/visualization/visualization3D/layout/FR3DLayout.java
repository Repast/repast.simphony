package repast.simphony.visualization.visualization3D.layout;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.vecmath.Vector3f;

import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.Box;
import cern.colt.matrix.DoubleMatrix3D;
import cern.colt.matrix.impl.DenseDoubleMatrix3D;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * Modified from the Fruchterman-Reingold algorithm for node layouts from JUNG-
 * FRLayout for 2D...but modified to do 3D
 * 
 * @author M. Altaweel
 */

public class FR3DLayout<T> extends JungNetworkLayout<T> {

  private static final Object FR_KEY = "edu.uci.ics.jung.FR_Visualization_Key";

  private double forceConstant;

  private double temperature = 0;

  private int currentIteration = 0;

  private String status = null;

  private int mMaxIterations = 700;

  /*
   * public JungFRLayout(Network n) { super(n); }
   */
  private double attraction_multiplier = .75;

  private double attraction_constant;

  private double repulsion_multiplier = .75;

  private double repulsion_constant;

  public void setAttractionMultiplier(double attraction) {
    this.attraction_multiplier = attraction;
  }

  protected void initializeLocations() {
    try {
      for (Iterator iter = getVisibleGraph().getNodes().iterator(); iter.hasNext();) {
        Object o = iter.next();

        double[] coord = returnMatchingCoordinate(o);
        if (coord == null) {
          coord = new double[3];
          locationData.put(o, coord);
        }
        if (!dontmove.contains(o))
          initializeLocation(o, coord, currentSize);
        initialize_local_vertex(o);
      }
    } catch (ConcurrentModificationException cme) {
      initializeLocations();
    }

  }

  /**
   * Sets random locations for a vertex within the dimensions of the space. If
   * you want to initialize in some different way, override this method.
   * 
   * @param coord
   * @param o
   * @param d
   */
  protected void initializeLocation(Object o, double[] coord, DimensionLocal d) {
    locationData.put(o, vertex_locations.getLocation(o, true));
  }

  public void setRepulsionMultiplier(double repulsion) {
    this.repulsion_multiplier = repulsion;
  }

  public FR3DLayout() {
    super();
  }

  /*
   * new function for handling updates and changes to the network need some way
   * to hook into with xml data to change iteration count
   */

  public void update() {
    initialize_local();
    resetVisibleEdgesAndVertices();
    initializeLocations();
    while (currentIteration < 100) {
      advancePositions();
    }
    currentIteration = 0;
  }

  /**
   * Returns the current temperature and number of iterations elapsed, as a
   * string.
   */
  public String getStatus() {
    return status;
  }

  public void forceMove(Object picked, double x, double y, double z) {
    super.forceMove(picked, x, y, z);
  }

  protected void initialize_local() {
    temperature = getCurrentSize().getWidth() / 100;

    forceConstant = Math
        .sqrt((getCurrentSize().getHeight() * getCurrentSize().getWidth() * getCurrentSize().getZ())
            / getVisibleGraph().size());
    // / Math.max(getVisibleGraph().numEdges(),
    // getVisibleGraph().numVertices()));

    attraction_constant = attraction_multiplier * forceConstant;
    repulsion_constant = repulsion_multiplier * forceConstant;

    // forceConstant = 0.75 * Math
    // .sqrt(getCurrentSize().getHeight()
    // * getCurrentSize().getWidth()
    // / getVisibleGraph().numVertices());
  }

  private Object key = null;

  private double EPSILON = 0.000001D;

  private DimensionLocal currentSize;

  private int yD;

  private int xD;

  private int zD;

  /**
   * Returns a visualization-specific key (that is, specific both to this
   * instance and AbstractLayout that can be used to access
   */
  public Object getKey() {
    if (key == null)
      try {
        key = new Pair(this, FR_KEY);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    return key;
  }

  public void initialize(DimensionLocal size) {
    initialize(size, new JungRandomVertexLocationDecorator(size, true));
  }

  public void initialize(DimensionLocal size, JungVertexLocationFunction v_locations) {
    this.currentSize = size;
    this.vertex_locations = v_locations;
    initialize_local();
    initializeLocations();
  }

  protected void initialize_local_vertex(Object o) {
    if (!objectData.containsKey(o)) {
      objectData.put(o, new FRVertexData());
    }
  }

  /**
   * Moves the iteration forward one notch, calculation attraction and repulsion
   * between vertices and edges and cooling the temperature.
   */
  public synchronized void advancePositions() {
    currentIteration++;
    status = "VV: " + getVisibleVertices().size() + " IT: " + currentIteration + " temp: "
        + temperature;
    /**
     * Calculate repulsion
     */
    while (true) {

      try {
        for (Iterator iter = getVisibleVertices().iterator(); iter.hasNext();) {
          Object v1 = iter.next();
          if (dontMove(v1))
            continue;
          calcRepulsion(v1);
        }
        break;
      } catch (ConcurrentModificationException cme) {
      }
    }

    /**
     * Calculate attraction
     */
    while (true) {
      try {
        for (Iterator iter = getVisibleEdges().iterator(); iter.hasNext();) {
          RepastEdge e = (RepastEdge) iter.next();

          calcAttraction(e);
        }
        break;
      } catch (ConcurrentModificationException cme) {
      }
    }

    // double cumulativeChange = 0;

    while (true) {
      try {

        for (Iterator iter = getVisibleVertices().iterator(); iter.hasNext();) {
          Object v = iter.next();
          if (dontMove(v))
            continue;
          calcPositions(v);
        }
        break;
      } catch (ConcurrentModificationException cme) {
      }
    }
    cool();
  }

  public synchronized void calcPositions(Object v) {
    FRVertexData fvd = getFRData(v);
    if (fvd == null)
      return;
    double[] xyz = getCoordinates(v);
    double deltaLength = Math.max(
        EPSILON,
        Math.sqrt((fvd.disp.get(0, 0, 0) * fvd.disp.get(0, 0, 0))
            + (fvd.disp.get(1, 0, 0) * fvd.disp.get(1, 0, 0) + (fvd.disp.get(2, 0, 0) * fvd.disp
                .get(2, 0, 0)))));

    // double deltaLength = Math.max(EPSILON,
    // Math.sqrt((fvd.disp.get(0,0,0)*fvd.disp.get(1,0,0)*fvd.disp.get(2,0,0))));

    double newXDisp = fvd.getXDisp() / deltaLength * Math.min(deltaLength, temperature);

    if (Double.isNaN(newXDisp)) {
      try {
        throw new Exception("Unexpected mathematical result in FRLayout:calcPositions [xdisp]");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    double newYDisp = fvd.getYDisp() / deltaLength * Math.min(deltaLength, temperature);

    double newZDisp = fvd.getZDisp() / deltaLength * Math.min(deltaLength, temperature);

    xyz[0] = xyz[0] + newXDisp;
    xyz[1] = xyz[1] + newYDisp;
    xyz[2] = xyz[2] + newZDisp;

    double borderWidth = getCurrentSize().getWidth() / 50.0;
    double newXPos = xyz[0];
    if (newXPos < borderWidth) {
      newXPos = borderWidth + Math.random() * borderWidth * 2.0;
    } else if (newXPos > (getCurrentSize().getWidth() - borderWidth)) {
      newXPos = getCurrentSize().getWidth() - borderWidth - Math.random() * borderWidth * 2.0;
    }
    // double newXPos = Math.min(getCurrentSize().getWidth() - 20.0,
    // Math.max(20.0, xyd.getX()));

    double newYPos = xyz[1];
    if (newYPos < borderWidth) {
      newYPos = borderWidth + Math.random() * borderWidth * 2.0;
    } else if (newYPos > (getCurrentSize().getHeight() - borderWidth)) {
      newYPos = getCurrentSize().getHeight() - borderWidth - Math.random() * borderWidth * 2.0;
    }
    // double newYPos = Math.min(getCurrentSize().getHeight() - 20.0,
    // Math.max(20.0, xyd.getY()));

    double newZPos = xyz[2];
    if (newZPos < borderWidth) {
      newZPos = borderWidth + Math.random() * borderWidth * 2.0;
    } else if (newZPos > (getCurrentSize().getZ() - borderWidth)) {
      newZPos = getCurrentSize().getZ() - borderWidth - Math.random() * borderWidth * 2.0;
    }

    xyz[0] = newXPos;
    xyz[1] = newYPos;
    xyz[2] = newZPos;
  }

  public void calcAttraction(RepastEdge e) {
    Object v1 = e.getSource();
    Object v2 = e.getTarget();

    double[] p1 = getCoordinates(v1);
    double[] p2 = getCoordinates(v2);
    if (p1 == null || p2 == null)
      return;
    double xDelta = p1[0] - p2[0];
    double yDelta = p1[1] - p2[1];
    double zDelta = p1[2] - p2[2];

    double deltaLength = Math.max(EPSILON,
        Math.sqrt((xDelta * xDelta) + (yDelta * yDelta) + (zDelta * zDelta)));

    // double force = (deltaLength * deltaLength) / forceConstant;

    initialize_local();
    double force = (deltaLength * deltaLength) / attraction_constant;

    if (Double.isNaN(force)) {
      try {
        throw new Exception("Unexpected mathematical result in FRLayout:calcPositions [force]");
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }

    FRVertexData fvd1 = getFRData(v1);
    FRVertexData fvd2 = getFRData(v2);

    fvd1.decrementDisp((xDelta / deltaLength) * force, (yDelta / deltaLength) * force,
        (zDelta / deltaLength) * force);
    fvd2.incrementDisp((xDelta / deltaLength) * force, (yDelta / deltaLength) * force,
        (zDelta / deltaLength) * force);
  }

  public void calcRepulsion(Object v1) {
    FRVertexData fvd1 = getFRData(v1);
    if (fvd1 == null)
      return;
    fvd1.setDisp(0, 0, 0);

    try {
      for (Iterator iter2 = getVisibleVertices().iterator(); iter2.hasNext();) {
        Object v2 = iter2.next();
        if (dontMove(v2))
          continue;
        if (v1 != v2) {
          double[] p1 = getCoordinates(v1);
          double[] p2 = getCoordinates(v2);
          if (p1 == null || p2 == null)
            continue;
          double xDelta = p1[0] - p2[0];
          double yDelta = p1[1] - p2[1];
          double zDelta = p1[2] - p2[2];

          double deltaLength = Math.max(EPSILON,
              Math.sqrt((xDelta * xDelta) + (yDelta * yDelta) + (zDelta * zDelta)));

          // double force = (forceConstant * forceConstant) /
          // deltaLength;
          double force = (repulsion_constant * repulsion_constant) / deltaLength;

          if (Double.isNaN(force)) {
            try {
              throw new Exception(
                  "Unexpected mathematical result in FRLayout:calcPositions [repulsion]");
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          fvd1.incrementDisp((xDelta / deltaLength) * force, (yDelta / deltaLength) * force,
              (zDelta / deltaLength) * force);
        }
      }
    } catch (ConcurrentModificationException cme) {
      calcRepulsion(v1);
    }
  }

  private void cool() {
    temperature *= (1.0 - currentIteration / (double) mMaxIterations);
  }

  public void setMaxIterations(int maxIterations) {
    mMaxIterations = maxIterations;
  }

  public FRVertexData getFRData(Object o) {
    return (FRVertexData) objectData.get(o);
  }

  /**
   * This one is an incremental visualization.
   */
  public boolean isIncremental() {
    return true;
  }

  /**
   * Returns true once the current iteration has passed the maximum count,
   * <tt>MAX_ITERATIONS</tt>.
   */
  public boolean incrementsAreDone() {
    if (currentIteration > mMaxIterations) {
      // System.out.println("Reached currentIteration =" +
      // currentIteration + ", maxIterations=" + mMaxIterations);
      return true;
    }
    return false;
  }

  public static class FRVertexData {

    private DoubleMatrix3D disp;

    public FRVertexData() {
      initialize();
    }

    public void initialize() {
      disp = new DenseDoubleMatrix3D(3, 1, 1);
    }

    public double getXDisp() {
      return disp.get(0, 0, 0);
    }

    public double getYDisp() {
      return disp.get(1, 0, 0);
    }

    public double getZDisp() {
      return disp.get(2, 0, 0);
    }

    public void setDisp(double x, double y, double z) {
      disp.set(0, 0, 0, x);
      disp.set(1, 0, 0, y);
      disp.set(2, 0, 0, z);
    }

    public void incrementDisp(double x, double y, double z) {
      disp.set(0, 0, 0, disp.get(0, 0, 0) + x);
      disp.set(1, 0, 0, disp.get(1, 0, 0) + y);
      disp.set(2, 0, 0, disp.get(2, 0, 0) + z);
    }

    public void decrementDisp(double x, double y, double z) {
      disp.set(0, 0, 0, disp.get(0, 0, 0) - x);
      disp.set(1, 0, 0, disp.get(1, 0, 0) - y);
      disp.set(2, 0, 0, disp.get(2, 0, 0) - z);
    }

  }

  public float[] getLocation(Object obj) {
    float[] coords = getLocation(obj, true);
    Vector3f v3f = new Vector3f();
    v3f.set(coords);
    return coords;
  }

  @Override
  public void setProjection(Network projection) {
    this.baseGraph = projection;
    this.visibleGraph = projection;

    Iterable edges = projection.getEdges();
    this.visibleEdges = addEdgesOrVertices(edges);
    Iterable nodes = projection.getNodes();
    this.visibleVertices = addEdgesOrVertices(nodes);
    this.dontmove = new HashSet();
    this.locationData = new HashMap<Object, double[]>();
    this.objectData = new HashMap<Object, Object>(); // added map for
    // extra
    this.currentSize = new DimensionLocal(1, 1, 1);
    initialize(currentSize);
  }

  public DimensionLocal getCurrentSize() {
    return this.currentSize;
  }

  public int getX() {
    return xD;
  }

  public void setX(int x) {
    this.xD = x;
  }

  public int getY() {
    return yD;
  }

  public void setY(int y) {
    this.yD = y;
  }

  public int getZ() {
    return zD;
  }

  public void setZ(int z) {
    this.zD = z;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualization.Layout#getBoundingBox()
   */
  @Override
  public Box getBoundingBox() {
    return new Box();
  }

}
