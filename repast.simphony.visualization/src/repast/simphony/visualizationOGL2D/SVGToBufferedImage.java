/**
 * 
 */
package repast.simphony.visualizationOGL2D;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

/**
 * Reads an SVG file into a buffered image.
 * 
 * @author Nick Collier
 */
public class SVGToBufferedImage {

  static class BITranscoder extends ImageTranscoder {

    private BufferedImage image;

    public BufferedImage createImage(int w, int h) {
      image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      return image;
    }

    public void writeImage(BufferedImage img, TranscoderOutput out) {
    }

    public BufferedImage getImage() {
      return image;
    }
  }

  /**
   * 
   * @param path
   * @param width -1 means use default width
   * @param height -1 means use default height
   * @return
   * @throws IOException
   * @throws TranscoderException
   */
  public static BufferedImage createImage(String path, int width, int height) throws IOException,
      TranscoderException {
    BITranscoder transcoder = new BITranscoder();
    if (width != -1) transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, new Float(width));
    if (height != -1) transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, new Float(height));
    
    String uri = new File(path).toURI().toString();
    transcoder.transcode(new TranscoderInput(uri), null);
    return transcoder.getImage();
  }
}
