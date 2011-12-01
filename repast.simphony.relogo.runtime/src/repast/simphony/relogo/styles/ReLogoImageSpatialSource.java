/**
 * 
 */
package repast.simphony.relogo.styles;

import java.io.IOException;

import repast.simphony.visualizationOGL2D.ImageSpatialSource;

/**
 * @author Nick Collier
 */
public class ReLogoImageSpatialSource extends ImageSpatialSource implements ReLogoSpatialSource{
  
  public ReLogoImageSpatialSource(String id, String path) throws IOException {
    super(id, path);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualizationOGL2D.SpatialSource#doRotate()
   */
  public boolean doRotate() {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualizationOGL2D.SpatialSource#getOffset()
   */
  public float getOffset() {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualizationOGL2D.SpatialSource#isSimple()
   */
  public boolean isSimple() {
    return false;
  }

}
