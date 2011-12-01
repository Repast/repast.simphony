package repast.simphony.ui;

import java.awt.image.BufferedImage;

/**
 * Interface for classes that can produce a BufferedImage. For example,
 * an Imageable JPanel would produce a BufferedImage of what that JPanel
 * contains.
 *
 * @author Nick Collier
 */
public interface Imageable {

  /**
   * Gets a BufferedImage.
   *
   * @return a BufferedImage.
   */
  public BufferedImage getImage();
}
