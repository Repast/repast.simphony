/**
 * 
 */
package repast.simphony.visualizationOGL2D;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import saf.v3d.ShapeFactory2D;

/**
 * Registers Spatials using an image (jpg, png, gif) as the source. The images
 * will be rendered to textures.
 * 
 * @author Nick Collier
 */
public class ImageSpatialSource implements SpatialSource {

  private String id;
  private String path;

  public ImageSpatialSource(String id, String path) throws IOException {
    this.id = id;
    this.path = path;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualizationOGL2D.SpatialSource#getID()
   */
  public String getID() {
    return id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.visualizationOGL2D.SpatialSource#registerSource(saf.v3d
   * .ShapeFactory2D, java.util.Map)
   */
  public void registerSource(ShapeFactory2D shapeFactory, Map<String, String> props)
      throws IOException {

    BufferedImage img = ImageIO.read(new File(path));
    int imgWidth = img.getWidth();
    int imgHeight = img.getHeight();
    int width = imgWidth;
    int height = imgHeight;
   
    float scaleX = 1f;
    float scaleY = 1f;

    if (props.containsKey(SpatialSource.KEY_BSQUARE_SIZE)) {
      int size = Integer.parseInt(props.get(SpatialSource.KEY_BSQUARE_SIZE));

      if (imgWidth > imgHeight) {
        width = size;
        scaleX = size / (float)imgWidth;
        height = (int)(imgHeight * scaleX);
        scaleY = height / (float)imgHeight;
      } else {
        height = size;
        scaleY = size / (float)imgHeight;
        width = (int)(imgWidth * scaleY);
        scaleX = width / (float)imgWidth;
      }
      
    } else {
     
      if (props.containsKey(KEY_WIDTH)) {
        width = (int)Double.parseDouble(props.get(KEY_WIDTH));
      }

      if (props.containsKey(KEY_HEIGHT)) {
        height = (int)Double.parseDouble(props.get(KEY_HEIGHT));
      }

      if (width != imgWidth) {
        scaleX = width / (float) imgWidth;
        if (height != imgHeight) {
          scaleY = height / (float) imgHeight;
        } else {
          scaleY = scaleX;
          height = (int) (imgHeight * scaleY);
        }
      } else {
        if (height != imgHeight) {
          scaleX = scaleY = height / (float) imgHeight;
          width = (int) (imgWidth * scaleX);
        }
      }
    }

    if (scaleX != 1f || scaleY != 1f) {
      // System.out.printf("width: %d, height: %d, scaleX: %f, scaleY: %f%n",
      // width, height, scaleX, scaleY);
      BufferedImage scaledImg = new BufferedImage(width, height, img.getType());
      AffineTransform at = AffineTransform.getScaleInstance(scaleX, scaleY);
      Graphics2D graphics = scaledImg.createGraphics();
      graphics.drawRenderedImage(img, at);
      graphics.dispose();
      img = scaledImg;
    }

    float scale = 1;
    if (props.containsKey(KEY_SCALE)) {
      scale = Float.parseFloat(props.get(KEY_SCALE));
    }

    shapeFactory.registerImage(id, img, scale);
  }
}
