/**
 * 
 */
package repast.simphony.relogo.styles;

import repast.simphony.visualizationOGL2D.SpatialSource;

/**
 * Interface for Relogo SpatialSources.
 * 
 * @author Nick Collier
 */
public interface ReLogoSpatialSource extends SpatialSource {
  
  /**
   * Gets whether or not to rotate spatials produced from
   * this source in response to a heading. 
   * 
   * @return
   */
  boolean doRotate();
  
  /**
   * Gets whether or not this source is simple enough to be represented as
   * geometry rather than a strict image. 
   * 
   * @return whether or not this source is simple enough to be represented as
   * geometry rather than a strict image. 
   */
  boolean isSimple();
  
  /**
   * Gets a default offset for spatials produced from this source.
   * 
   * @return a default offset for spatials produced from this source.
   */
  float getOffset();

}
