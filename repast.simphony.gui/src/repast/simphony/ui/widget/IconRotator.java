package repast.simphony.ui.widget;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.Timer;

/**
 * Rotates an icon on a JButton.
 *
 * @author Nick Collier
 */
public class IconRotator {

  private JButton button;
  private ImageIcon icon;
  private int width, height;
  private Timer timer;

  public IconRotator(JButton button, ImageIcon icon) {
    this.button = button;
    this.icon = icon;
    width = icon.getIconWidth();
    height = icon.getIconHeight();
    timer = new Timer(250, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        rotate();
      }
    });
  }

  public void setEnabled(boolean enabled) {
    if (enabled) {
      button.setEnabled(true);
      if (!timer.isRunning()) timer.start();
    } else {
      if (timer.isRunning()) timer.stop();
      button.setEnabled(false);
    }
  }

  private void rotate() {
    width = icon.getIconWidth();
    height = icon.getIconHeight();
    int type = BufferedImage.TYPE_INT_RGB;
    BufferedImage image = new BufferedImage(width, height, type);
    Graphics2D g2 = image.createGraphics();
    AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(45), width / 2.0, height / 2.0);
    g2.drawImage(icon.getImage(), at, button);
    g2.dispose();
    icon.setImage(image);
    button.revalidate();
    button.repaint();
  }
}
