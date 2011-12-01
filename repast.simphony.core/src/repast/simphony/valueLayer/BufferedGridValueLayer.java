/**
 * 
 */
package repast.simphony.valueLayer;

import repast.simphony.space.Dimensions;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.GridPointTranslator;
import repast.simphony.space.grid.StrictBorders;

/**
 * Implements a grid value layer with read and write buffers. 
 * All setting is done on the write buffer and all reading is done on
 * the read buffer. The {@link #swap() swap} method swaps the read
 * and write buffers.
 * 
 * @author Nick Collier
 */
public class BufferedGridValueLayer implements IGridValueLayer {
  
  public enum Buffer {READ, WRITE};
  
  private GridValueLayer read, write;
  
  /**
   * Creates a BufferedGridValueLayer with the specified name, density, and dimensions.
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
  public BufferedGridValueLayer(String name, boolean dense, int... dimensions) {
    this(name, 0.0, dense, dimensions);

  }

  /**
   * Creates a BufferedGridValueLayer with the specified name, density, defaultValue and
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
  public BufferedGridValueLayer(String name, double defaultValue, boolean dense, int... dimensions) {
    this(name, defaultValue, dense, new StrictBorders(), dimensions);
  }

  /**
   * Creates a BufferedGridValueLayer with the specified name, density, translator, and
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
  public BufferedGridValueLayer(String name, boolean dense, GridPointTranslator translator,
      int... dimensions) {
    this(name, 0, dense, translator, dimensions);
  }

  /**
   * Creates a BufferedGridValueLayer with the specified name, defaultValue, density,
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
  public BufferedGridValueLayer(String name, double defaultValue, boolean dense,
      GridPointTranslator translator, int... dimensions) {
    this(name, defaultValue, dense, translator, dimensions, new int[dimensions.length]);
  }

  /**
   * Creates a BufferedGridValueLayer with the specified name, defaultValue, density,
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
  public BufferedGridValueLayer(String name, double defaultValue, boolean dense,
      GridPointTranslator translator, int[] dimensions, int[] origin) {
    read = new GridValueLayer(name, defaultValue, dense, translator, dimensions, origin);
    write = new GridValueLayer(name, defaultValue, dense, translator, dimensions, origin);
  }

  /* (non-Javadoc)
   * @see repast.simphony.valueLayer.ValueLayer#get(double[])
   */
  public double get(double... coordinate) {
    return read.get(coordinate);
  }
  
  /**
   * Apply the specified function to each cell in the extent around the
   * origin. This includes the origin itself.
   * 
   * @param function 
   * @param origin
   * @param buffer specifies which buffer the function operates on
   * @param extent
   */
  public void forEach(GridFunction function, GridPoint origin, Buffer buffer, int... extent) {
    if (buffer == Buffer.READ) read.forEach(function, origin, extent);
    else if (buffer == Buffer.WRITE) write.forEach(function, origin, extent);
    
  }

  /* (non-Javadoc)
   * @see repast.simphony.valueLayer.ValueLayer#getDimensions()
   */
  public Dimensions getDimensions() {
    return read.getDimensions();
  }

  /* (non-Javadoc)
   * @see repast.simphony.valueLayer.ValueLayer#getName()
   */
  public String getName() {
    return read.getName();
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
    write.set(value, coordinate);
  }
  
  /**
   * Swaps the read and write buffers.
   */
  public void swap() {
    GridValueLayer tmp = read;
    read = write;
    write = tmp;
  }
}
