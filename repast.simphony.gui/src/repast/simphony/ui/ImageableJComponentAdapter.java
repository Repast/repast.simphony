package repast.simphony.ui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * Adapts a JComponent to an Imageable.
 *
 * @author Nick Collier
 */
public class ImageableJComponentAdapter implements Imageable {

  private JComponent comp;

  /**
   * Constructs a ImageableJComponentAdapter that adapts the
   * specified JComponent.
   *
   * @param comp the component to adapt.
   */
  public ImageableJComponentAdapter(JComponent comp) {
    this.comp = comp;
  }

  /**
   * Gets a BufferedImage that displays the contents of the JComponent.
   *
   * @return a BufferedImage that displays the contents of the JComponent.
   */
  public BufferedImage getImage() {
    Rectangle d = comp.getBounds();
    BufferedImage img = comp.getGraphicsConfiguration().createCompatibleImage(d.width, d.height);
    Graphics2D g2d = img.createGraphics();
    comp.paint(g2d);
    g2d.dispose();
    return img;
  }
}
