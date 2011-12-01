package repast.simphony.valueLayer;

import repast.simphony.space.continuous.WrapAroundBorders;

/**
 * This implements a diffusion algorithm on a grid value layer. By setting the
 * diffusion constant and the evaporation constant you can control the way the
 * diffusion occurs. Also, this allows you to specify whether the diffusion
 * should act as though the space is toroidal (meaning the edges are connected
 * to each other), or not. If the space is not toroidal then the edge of the
 * space is considered a "value sink", meaning any values that hit it are
 * evacuated out of the space (ie, if this space is diffusing heat, this acts as
 * though there is a perfect heat sink around the space).
 * <p/>
 * 
 * A diffusion constant of 1.0 means that all the values at a specific spot are
 * diffused away, so if the space looks like [0, 10, 0] diffusing (assuming no
 * evaporation) gives [5, 0, 5].
 * <p/>
 * 
 * An evaporation is used as a multiplier against the values in the space, this
 * means that a rate of 1.0 means no evaporation, and a rate of 0.0 means
 * instantaneous evaporation. So, going off the previous example, with a
 * diffusion constant of 1.0 and an evaporation rate of 0.0, [0, 10, 0] diffuses
 * to be [2.5, 0, 2.5].
 * 
 * @author Jerry Vos
 */
public class ValueLayerDiffuser {
  // TODO: Why these have these values versus others?
  public static final double DEFAULT_MAX = 0x7FFF;
  public static final double DEFAULT_MIN = -DEFAULT_MAX;

  public static final double DEFAULT_EVAP_CONST = 1.0;
  public static final double DEFAULT_DIFF_CONST = 1.0;

  private static final long serialVersionUID = 1L;

  private static final double OUT_OF_BOUNDS = 0.0;

  protected IGridValueLayer valueLayer;

  protected double maxValue = DEFAULT_MAX;

  protected double minValue = DEFAULT_MIN;

  protected double evaporationConst;

  protected double diffusionConst;

  protected boolean toroidal;

  protected transient Object computedVals;

  private transient WrapAroundBorders borders;

  /**
   * Constructs this with the default evaporation and diffusion constants.
   * Before this diffuser can be used, a value layer must be set through the
   * {@link #setValueLayer(GridValueLayer)} method.
   * <p/>
   * 
   * This is the same as
   * <code>new ValueLayer(null, DEFAULT_EVAP_CONST, DEFAULT_DIFF_CONST)</code>
   * 
   * @see #setValueLayer(GridValueLayer)
   */
  public ValueLayerDiffuser() {
    this(null, DEFAULT_EVAP_CONST, DEFAULT_DIFF_CONST);
  }

  /**
   * Constructs this with the specified evaporation and diffusion constants.
   * This also has the diffusion acting in a toroidal manner, so values from the
   * edges will be diffused to the other side of the space.
   * <p/>
   * 
   * This is the same as
   * <code>new ValueLayer(valueLayer, evaporationConst, diffusionConstant, true)</code>
   * 
   * @param valueLayer
   *          the layer this will be diffusing values on
   * @param evaporationConst
   *          the constant used for evaporating values off the layer
   * @param diffusionConst
   *          the constant used for diffusing values on the layer
   */
  public ValueLayerDiffuser(IGridValueLayer valueLayer, double evaporationConst,
      double diffusionConst) {
    this(valueLayer, evaporationConst, diffusionConst, true);
  }

  /**
   * Constructs this with the specified evaporation constant, diffusion
   * constant, and toroidal'ness.
   * 
   * @param valueLayer
   *          the layer this will be diffusing values on
   * @param evaporationConst
   *          the constant used for evaporating values off the layer
   * @param diffusionConst
   *          the constant used for diffusing values on the layer
   * @param toroidal
   *          if this should act as though the edges of the layer are connected
   */
  public ValueLayerDiffuser(IGridValueLayer valueLayer, double evaporationConst,
      double diffusionConst, boolean toroidal) {
    super();
    this.evaporationConst = evaporationConst;
    this.diffusionConst = diffusionConst;
    this.toroidal = toroidal;

    setValueLayer(valueLayer);
  }

  /**
   * Retrieves a value from the layer, taking into account the toroidal'ness.
   * 
   * @param coords
   *          the coordinates
   * @return the value at the specified coordinate
   */
  protected double getValue(double... coords) {
    if (toroidal) {
      if (borders == null) {
        borders = new WrapAroundBorders();
        borders.init(valueLayer.getDimensions());
      }
      // use the wrap around borders class to set this up
      borders.transform(coords, coords);
    } else if (inBounds(coords) == 0.0) {
      return 0.0;
    }
    return valueLayer.get(coords);
  }

  /**
   * Returns 1 if the coordinates are in the layer's bounds, otherwise returns
   * {@value #OUT_OF_BOUNDS}.
   * 
   * @param coords
   *          the coordinates to check
   * @return 1 or {@value #OUT_OF_BOUNDS}
   */
  protected double inBounds(double... coords) {
    if (toroidal) {
      return 1;
    }
    for (int i = 0; i < coords.length; i++) {
      if (coords[i] < 0 || coords[i] > valueLayer.getDimensions().getDimension(i) - 1) {
        return OUT_OF_BOUNDS;
      }
    }
    return 1;
  }

  /**
   * Massages the value into the range specified by [minValue, maxValue].
   * 
   * @param val
   *          the value to bring into range (if necessary)
   * @return the value
   */
  protected double constrainByMinMax(double val) {
    if (val > maxValue) {
      return maxValue;
    } else if (val < minValue) {
      return minValue;
    }
    return val;
  }

  /**
   * Computes all the values for the space.
   */
  protected void computeVals() {
    // this is being based on
    // http://www.mathcs.sjsu.edu/faculty/rucker/capow/santafe.html
    int size = valueLayer.getDimensions().size();

    if (size == 1) {
      int width = (int) valueLayer.getDimensions().getWidth();

      double sum;
      double[] newVals = new double[width];
      for (int x = 0; x < width; x++) {
        // sum the cell to the left and the right of the given one
        sum = getValue(x - 1);
        sum += getValue(x + 1);

        double weightedAvg = sum / 2.0;

        // apply the diffusion and evaporation constants
        double oldVal = getValue(x);
        double delta = weightedAvg - oldVal;

        double newVal = (oldVal + delta * diffusionConst) * evaporationConst;
        // bring the value into range [min, max]
        newVals[x] = constrainByMinMax(newVal);
      }
      computedVals = newVals;
    } else if (size == 2) {
      int width = (int) valueLayer.getDimensions().getWidth();
      int height = (int) valueLayer.getDimensions().getHeight();
      double[][] newVals = new double[width][height];
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          // these are the neighbors that are directly north/south/east/west to
          // the given cell 4 times those that are diagonal to the cell
          double uE = getValue(x + 1, y);
          double uN = getValue(x, y + 1);
          double uW = getValue(x - 1, y);
          double uS = getValue(x, y - 1);

          // these are the neighbors that are diagonal to the given cell
          // they are only weighted 1/4 of the ones that are
          // north/south/east/west
          // of the cell
          double uNE = getValue(x + 1, y + 1);
          double uNW = getValue(x - 1, y + 1);
          double uSW = getValue(x - 1, y - 1);
          double uSE = getValue(x + 1, y - 1);

          // compute the weighted avg, those directly north/south/east/west
          // are given 4 times the weight of those on a diagonal
          double weightedAvg = ((uE + uN + uW + uS) * 4 + (uNE + uNW + uSW + uSE)) / 20.0;

          // apply the diffusion and evaporation constants
          double oldVal = getValue(x, y);
          double delta = weightedAvg - oldVal;

          double newVal = (oldVal + delta * diffusionConst) * evaporationConst;

          // bring the value into [min, max]
          newVals[x][y] = constrainByMinMax(newVal);

          // System.out.println("x: " + x + " y: " + y + "val: " + oldVal +
          // " delta: "
          // + delta + " d: " + newVals[x][y]);
        }
      }
      computedVals = newVals;
    } else if (size == 3) {
      int width = (int) valueLayer.getDimensions().getWidth();
      int height = (int) valueLayer.getDimensions().getHeight();
      int depth = (int) valueLayer.getDimensions().getDepth();
      double[][][] newVals = new double[width][height][depth];
      for (int z = 0; z < depth; z++) {
        for (int y = 0; y < height; y++) {
          for (int x = 0; x < width; x++) {
            newVals[x][y][z] = compute3dVal(x, y, z);
          }
        }
      }
      computedVals = newVals;
    }
  }

  private double compute3dVal(double xCoord, double yCoord, double zCoord) {
    double weightedSum = 0;
    int numberEqual = 0;
    // this is 4 if we're dealing with a direct (von-neuman) neighbor of the
    // cell
    // otherwise (we're on a diagonal) it's 1
    double multiplier = 1;
    // this keeps the count of the number of neighbors we found that are valid.
    // this also takes into account that some of the cells are weighted more
    // than others
    // and will have them counted accordingly (4 versus 1)
    int count = 0;
    for (double z = zCoord - 1; z <= zCoord + 1; z++) {
      for (double y = yCoord - 1; y <= yCoord + 1; y++) {
        for (double x = xCoord - 1; x <= xCoord + 1; x++) {
          numberEqual = 0;
          // find out if we're directly north/south/east/west/above/below the
          // given
          // cell (a von-neuman neighbor)
          if (xCoord == x) {
            numberEqual++;
          }
          if (yCoord == y) {
            numberEqual++;
          }
          if (zCoord == z) {
            numberEqual++;
          }
          if (numberEqual == 3) {
            // we're at the source
            continue;
          } else if (numberEqual == 2) {
            // we're a direct neighbor so we get weighted more
            multiplier = 4;
          } else {
            multiplier = 1;
          }
          // System.out.println(x + "," + y + "," + z);
          weightedSum += multiplier * getValue(x, y, z);
          count += multiplier;
        }
      }
    }

    double weightedAvg = weightedSum / count;
    double oldVal = getValue(xCoord, yCoord, zCoord);
    double delta = weightedAvg - oldVal;

    return constrainByMinMax((oldVal + delta * diffusionConst) * evaporationConst);
  }

  /**
   * Runs the diffusion with the current rates and values. Following the Swarm
   * class, it is roughly newValue = evap(ownValue + diffusionConstant * (nghAvg
   * - ownValue)) where nghAvg is the weighted average of a cells neighbors, and
   * ownValue is the current value for the current cell.
   * <p>
   * 
   * Values from the value layer are used to calculate diffusion. This value is
   * then written to a buffer. When this has been done for every cell in the
   * grid, the buffer is copied to the value layer.
   */
  public void diffuse() {
    computeVals();
    int size = valueLayer.getDimensions().size();

    if (size == 1) {
      double[] newVals = (double[]) computedVals;
      for (int x = 0; x < newVals.length; x++) {
        valueLayer.set(newVals[x], x);
      }
    } else if (size == 2) {
      double[][] newVals = (double[][]) computedVals;
      for (int x = 0; x < newVals.length; x++) {
        for (int y = 0; y < newVals[0].length; y++) {
          valueLayer.set(newVals[x][y], x, y);
        }
      }
    } else {
      double[][][] newVals = (double[][][]) computedVals;
      for (int x = 0; x < newVals[0].length; x++) {
        for (int y = 0; y < newVals[0][0].length; y++) {
          for (int z = 0; z < newVals.length; z++) {
            valueLayer.set(newVals[x][y][z], x, y, z);
          }
        }
      }
    }
  }

  public double getDiffusionConst() {
    return diffusionConst;
  }

  public void setDiffusionConst(double diffusionConst) {
    this.diffusionConst = diffusionConst;
  }

  public double getEvaporationConst() {
    return evaporationConst;
  }

  public void setEvaporationConst(double evapRate) {
    this.evaporationConst = evapRate;
  }

  public double getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(double max) {
    this.maxValue = max;
  }

  public double getMinValue() {
    return minValue;
  }

  public void setMinValue(double min) {
    this.minValue = min;
  }

  public boolean isToroidal() {
    return toroidal;
  }

  public void setToroidal(boolean toroidal) {
    this.toroidal = toroidal;
  }

  public ValueLayer getValueLayer() {
    return valueLayer;
  }

  public void setValueLayer(IGridValueLayer valueLayer) {
    if (valueLayer.getDimensions().size() > 3) {
      throw new RuntimeException("Value layer diffuser only works for 1d, 2d or 3d value layers.");
    }
    this.valueLayer = valueLayer;
  }
}
