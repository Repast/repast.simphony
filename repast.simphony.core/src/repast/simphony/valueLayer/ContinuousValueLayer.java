package repast.simphony.valueLayer;

import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.PointTranslator;
import repast.simphony.space.continuous.StrictBorders;

import java.util.HashMap;
import java.util.Map;

/**
 * A continuous value layer. Each point in the space is accessed via double x, y,
 * z, ... n coordinates and contains some numeric value.
 *
 * @author Eric Tatara
 */
public class ContinuousValueLayer implements ValueLayer {

  protected String name;

  protected Dimensions dims;

  // this field is accessed directly by
  // ContinuousValueLayerConverter. Don't
  // change it withough changing that as well!
  protected double defaultValue;
  // this field is accessed directly by
  // ContinuousValueLayerConverter. Don't
  // change it withough changing that as well!
  protected Map<NdPoint, Double> locationMap;

  protected boolean dense;

  protected PointTranslator translator;

  /**
   * Creates a continuous value layer withe specified name and dimensions.
   *
   * @param name       the name of the value layer
   * @param dimensions the dimensions of the value layer
   */
  public ContinuousValueLayer(String name, double... dimensions) {
    this(name, 0.0, true, dimensions);
  }

  /**
   * Creates a ContinuousValueLayer with the specified name, density, and
   * dimensions. The default value of every cell in the grid will be 0.  The
   * default border behavior is strict.
   *
   * @param name       the name of the value layer
   * @param dense      dummy argument
   * @param dimensions the dimension of the value layer
   */
  public ContinuousValueLayer(String name, boolean dense, double... dimensions) {
    this(name, 0.0, dense, dimensions);

  }

  /**
   * Creates a ContinuousValueLayer with the specified name, density,
   * defaultValue and dimensions. The default border behavior is strict.
   *
   * @param name         the name of the value layer
   * @param defaultValue the default value that a location will contain if it
   *                     has not been set yet
   * @param dense        dummy argument
   * @param dimensions   the dimension of the value layer
   */
  public ContinuousValueLayer(String name, double defaultValue, boolean dense,
                              double... dimensions) {
    this(name, defaultValue, dense, new StrictBorders(), dimensions);
  }

  /**
   * Creates a ContinuousValueLayer with the specified name, density,
   * translator, and dimensions. The default value of every cell in the
   * grid will be 0.
   *
   * @param name       the name of the value layer
   * @param dense      dummy argument
   * @param translator the translator used
   * @param dimensions the dimension of the value layer
   */
  public ContinuousValueLayer(String name, boolean dense,
                              PointTranslator translator, double... dimensions) {
    this(name, 0.0, dense, translator, dimensions);
  }

  /**
   * Creates a ContinuousValueLayer with the specified name, density,
   * defaultValue, translator, and dimensions.
   *
   * @param name       the name of the value layer
   * @param defaultVal the default value that each cell will contain if it has
   *                   not been set yet
   * @param dense      dummy argument
   * @param translator the translator used
   * @param dimensions the dimension of the value layer
   */
  public ContinuousValueLayer(String name, double defaultVal, boolean dense,
                              PointTranslator translator, double... dimensions) {
    this.name = name;
    this.defaultValue = defaultVal;
    this.dense = dense;
    this.translator = translator;

    dims = new Dimensions(dimensions);

    locationMap = new HashMap<NdPoint, Double>();
    translator.init(dims);
  }

  /**
   * Gets a value given the specified coordinates. This assumes the coordinates
   * are double x, y, z, n values.
   *
   * @param coordinates the coordinates used to return the value.
   * @return a value given the specified coordinates.
   */
  public double get(double... coordinates) {
    NdPoint point = new NdPoint(coordinates);

    Double value = locationMap.get(point);

    return value == null ? defaultValue : value;
  }

  /**
   * Sets the specified point to the specified value.
   *
   * @param value      the new value of the cell
   * @param coordinate the coordinate of the cell whose value we want to set
   */
  public void set(double value, double... coordinate) {

    NdPoint point = new NdPoint(coordinate);

    locationMap.put(point, value);
  }

  protected double[] getLocation(double... location) {
    double[] loc = new double[location.length];
    translator.transform(loc, location);
    return loc;
  }

  /**
   * Gets the name of this value layer.
   *
   * @return the name of this value layer.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the dimensions of this value layer.
   *
   * @return the dimensions of this value layer.
   */
  public Dimensions getDimensions() {
    return dims;
  }

  /**
   * Retrieves the rule being used for controlling what happens at or beyond
   * the borders of the value layer.
   *
   * @return the rule for handling out of bounds coordinates
   */
  public PointTranslator getPointTranslator() {
    return translator;
  }

  /**
   * Sets the rule to use for controlling what happens at or beyond the
   * borders of the value layer.
   *
   * @param rule the rule for handling out of bounds coordinates
   */
  public void setPointTranslator(PointTranslator rule) {
    this.translator = rule;
	}
}