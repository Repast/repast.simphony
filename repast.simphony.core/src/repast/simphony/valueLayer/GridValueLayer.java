package repast.simphony.valueLayer;

import repast.simphony.space.Dimensions;
import repast.simphony.space.SpatialException;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.GridPointTranslator;
import repast.simphony.space.grid.StrictBorders;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;

/**
 * A grid value layer. Each cell in the grid is accessed via integer x, y, z,
 * ... n coordinates and contains some numeric value. Each cell in the grid will
 * contain a value regardless of whether or not it has been set yet. This
 * default value can be set by the user.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class GridValueLayer implements IGridValueLayer {

  protected String name;

  protected DoubleMatrix1D matrix;

  protected int[] stride;

  protected Dimensions dims;
  // this field is read directly by the
  // GridValueLayerConverter -- don't change it
  // without changing that!!!

  protected boolean dense;

  protected GridPointTranslator translator;

  /**
   * Creates a GridValueLayer with the specified name, density, and dimensions.
   * The default value of every cell in the grid will be 0. The default border
   * behavior is strict.
   * 
   * @param name
   *          the name of the value layer
   * @param dense
   *          if value layer will be densely populated
   * @param dimensions
   *          the dimension of the value layer
   */
  public GridValueLayer(String name, boolean dense, int... dimensions) {
    this(name, 0.0, dense, dimensions);

  }

  /**
   * Creates a GridValueLayer with the specified name, density, defaultValue and
   * dimensions. The default border behavior is strict.
   * 
   * @param name
   *          the name of the value layer
   * @param defaultValue
   *          the default value that each grid cell will contain if it has not
   *          been set yet
   * @param dense
   *          if the value layer will be densely populated
   * @param dimensions
   *          the dimension of the grid value layer
   */
  public GridValueLayer(String name, double defaultValue, boolean dense, int... dimensions) {
    this(name, defaultValue, dense, new StrictBorders(), dimensions);
  }

  /**
   * Creates a GridValueLayer with the specified name, density, translator, and
   * dimensions. The default border behavior is strict. The default value of
   * every cell in the grid will be 0.
   * 
   * @param name
   *          the name of the value layer
   * @param defaultValue
   *          the default value that each grid cell will contain if it has not
   *          been set yet
   * @param dense
   *          if the value layer will be densely populated
   * @param translator
   *          the translator used
   * @param dimensions
   *          the dimension of the value layer
   */
  public GridValueLayer(String name, boolean dense, GridPointTranslator translator,
      int... dimensions) {
    this(name, 0, dense, translator, dimensions);
  }

  /**
   * Creates a GridValueLayer with the specified name, defaultValue, density,
   * translator and dimensions.
   * 
   * @param name
   *          the name of the value layer
   * @param defaultValue
   *          the default value that each grid cell will contain if it has not
   *          been set yet
   * @param dense
   *          if value layer will be densely populated
   * @param translator
   *          the translator used
   * @param dimensions
   *          the dimension of the value layer
   */
  public GridValueLayer(String name, double defaultValue, boolean dense,
      GridPointTranslator translator, int... dimensions) {
    this(name, defaultValue, dense, translator, dimensions, new int[dimensions.length]);
  }

  /**
   * Creates a GridValueLayer with the specified name, defaultValue, density,
   * translator and dimensions.
   * 
   * @param name
   *          the name of the value layer
   * @param defaultValue
   *          the default value that each grid cell will contain if it has not
   *          been set yet
   * @param dense
   *          if value layer will be densely populated
   * @param translator
   *          the translator used
   * @param dimensions
   *          the dimension of the value layer
   */
  public GridValueLayer(String name, double defaultValue, boolean dense,
      GridPointTranslator translator, int[] dimensions, int[] origin) {
    this.name = name;
    this.dense = dense;
    this.translator = translator;

    int _size = 1;
    for (int dim : dimensions) {
      _size *= dim;
    }

    if (dense)
      matrix = new DenseDoubleMatrix1D(_size);
    else
      matrix = new SparseDoubleMatrix1D(_size);
    matrix.assign(defaultValue);

    int tmpStride = 1;
    stride = new int[dimensions.length];
    for (int i = dimensions.length - 1; i >= 0; i--) {
      stride[i] = tmpStride;
      tmpStride = tmpStride * dimensions[i];
    }

    translator.init(new GridDimensions(dimensions, origin));
    dims = new Dimensions(dimensions, origin);
  }

  /**
   * Apply the specified function to each cell in the extent around the
   * origin. This includes the origin itself.
   * 
   * @param function 
   * @param origin
   * @param extent
   */
  public void forEach(GridFunction function, GridPoint origin, int... extent) {
    int dimsSize = dims.size();
    if (extent.length != dimsSize)
      throw new IllegalArgumentException("Number of extents must be equal to the number of grid dimensions.");

    if (dimsSize > 3 || dimsSize < 1)
      throw new IllegalArgumentException("forEach is only "
          + "supported on 1D, 2D and 3D GridValueLayers");

    int[] mins = new int[dimsSize];
    int[] maxs = new int[dimsSize];
    if (translator.isToroidal()) {
      for (int i = 0; i < dimsSize; i++) {
        mins[i] = origin.getCoord(i) - extent[i];
        maxs[i] = origin.getCoord(i) + extent[i];
      }
    } else {
      for (int i = 0; i < dimsSize; i++) {
        int min = origin.getCoord(i) - extent[i];
        if (min < dims.getOrigin(i))
          min = (int) dims.getOrigin(i);
        mins[i] = min;

        int max = origin.getCoord(i) + extent[i];
        if (max >= dims.getOrigin(i) + dims.getDimension(i))
          max = (int) (dims.getOrigin(i) + dims.getDimension(i)) - 1;
        maxs[i] = max;
      }
    }

    if (dimsSize == 1) {
      for (int x = mins[0]; x <= maxs[0]; x++) {
        function.apply(get(x), x);
      }
    } else if (dimsSize == 2) {
      for (int x = mins[0]; x <= maxs[0]; x++) {
        for (int y = mins[1]; y <= maxs[1]; y++) {
          function.apply(get(x, y), x, y);
        }
      }
    } else {
      for (int x = mins[0]; x <= maxs[0]; x++) {
        for (int y = mins[1]; y <= maxs[1]; y++) {
          for (int z = mins[2]; z <= maxs[2]; z++) {
            function.apply(get(x, y, z), x, y, z);
          }
        }
      }
    }
  }

  /**
   * Gets a value given the specified coordinates. This assumes the coordinates
   * are int x, y, z, n values.
   * 
   * @param coordinates
   *          the coordinates used to return the value.
   * @return a value given the specified coordinates.
   */
  public double get(double... coordinates) {

    int[] coords = new int[coordinates.length];

    for (int i = 0; i < coordinates.length; i++)
      coords[i] = (int) coordinates[i];

    int index = getIndex(getTransformedLocation(coords));
    return matrix.get(index);
  }

  // returns the index into the matrix given a point
  // this casts the double to an int
  private int getIndex(int... point) {
    int[] matrixPoint = new int[point.length];
    int[] origin = this.dims.originToIntArray(null);
    for (int i = 0; i < point.length; i++) {
      matrixPoint[i] = point[i] + origin[i];
    }
    int index = 0;
    for (int i = 0; i < matrixPoint.length; i++) {
      index = index + (int) matrixPoint[i] * stride[i];
    }
    return index;
  }

  /**
   * Sets the specified cell to the specified value.
   * 
   * @param value
   *          the new value of the cell
   * @param coordinate
   *          the coordinate of the cell whose value we want to set
   */
  public void set(double value, int... coordinate) {
    if (coordinate.length != dims.size()) throw new SpatialException("Invalid number coordinates");
    int index = getIndex(getTransformedLocation(coordinate));
    matrix.set(index, value);
  }

  /**
   * Gets the name of this ValueLayer.
   * 
   * @return the name of this ValueLayer.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the dimensions of this GridValueLayer.
   * 
   * @return the dimensions of this GridValueLayer.
   */
  public Dimensions getDimensions() {
    return dims;
  }

  protected int[] getTransformedLocation(int... location) {
    int[] loc = new int[location.length];
    translator.transform(loc, location);
    return loc;
  }

  /**
   * Retrieves the rule being used for controlling what happens at or beyond the
   * borders of the value layer.
   * 
   * @return the rule for handling out of bounds coordinates
   */
  public GridPointTranslator getGridPointTranslator() {
    return translator;
  }

  /**
   * Sets the rule to use for controlling what happens at or beyond the borders
   * of the value layer.
   * 
   * @param rule
   *          the rule for handling out of bounds coordinates
   */
  public void setGridPointTranslator(GridPointTranslator rule) {
    this.translator = rule;
  }
}