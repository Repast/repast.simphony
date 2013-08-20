/**
 * 
 */
package repast.simphony.statecharts.edit.parts;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.geometry.PrecisionPoint;

/**
 * Factory for producing anchor locations.
 * 
 * @author Nick Collier
 */
public class AnchorLocationFactory {

  /**
   * Creates anchor locations at the cardinal points.
   * 
   * @return a map for anchor locations at the cardinal points. 
   */
  public static Map<String, PrecisionPoint> createNSEW() {
    Map<String, PrecisionPoint> anchorLocations = new HashMap<String, PrecisionPoint>();
    anchorLocations.put("WEST", new PrecisionPoint(0, 0.5d));
    anchorLocations.put("EAST", new PrecisionPoint(1d, 0.5d));
    anchorLocations.put("NORTH", new PrecisionPoint(0.5d, 0));
    anchorLocations.put("SOUTH", new PrecisionPoint(0.5d, 1d));
    return anchorLocations;
  }
  
  /**
   * Creates anchor locations at the cardinal points.
   * 
   * @return a map for anchor locations at the cardinal points. 
   */
  public static Map<String, PrecisionPoint> createCenter() {
    Map<String, PrecisionPoint> anchorLocations = new HashMap<String, PrecisionPoint>();
    anchorLocations.put("CENTER", new PrecisionPoint(0.5d, 0.5d));
    return anchorLocations;
  }

}
