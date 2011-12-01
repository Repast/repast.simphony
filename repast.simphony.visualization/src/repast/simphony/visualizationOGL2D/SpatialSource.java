/**
 * 
 */
package repast.simphony.visualizationOGL2D;

import java.io.IOException;
import java.util.Map;

import saf.v3d.ShapeFactory2D;

/**
 * Interface for classes that can act as sources for VSpatials. 
 * 
 * @author Nick Collier
 */
public interface SpatialSource {
  
  /**
   * Key for the map passed to registerSource whose
   * value specifies the width, if appicable, of the created Spatial.
   */
  String KEY_WIDTH = "width";
  
  /**
   * Key for the map passed to registerSource whose
   * value specifies the height, if appicable, of the created Spatial.
   */
  String KEY_HEIGHT = "height";
  
  /**
   * Key for the map passed to registerSource whose
   * value specifies the scale, if appicable, of the created Spatial.
   */
  String KEY_SCALE = "scale";
  
  /**
   * Key for the map passed to registerSource whose
   * value specifies the size of a bounding square
   * side, if applicable, of the created Spatial.
   */
  String KEY_BSQUARE_SIZE = "bsq_size";
  
  /**
   * Gets the unique name for this source.
   * 
   * @return the unique name for this source.
   */
  String getID();
  
  /**
   * Registers this source with ShapeFactory2D. After registering the source
   * VSpatials can be retreived by name from the ShapeFactory2D.
   * 
   * @param props a map containing properties (SpatialSource.WIDTH, etc.) that can be used in registering the source
   */
  void registerSource(ShapeFactory2D shapeFactory, Map<String, String> props) throws IOException;
  
}
