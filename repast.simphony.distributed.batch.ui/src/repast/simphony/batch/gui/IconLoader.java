/**
 * 
 */
package repast.simphony.batch.gui;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author Nick Collier
 */
public class IconLoader {
  
  /**
   * Gets the named icon.
   * 
   * @param name
   * 
   * @return the named icon.
   */
  public static Icon loadIcon(String name) {
    return new ImageIcon(IconLoader.class.getResource("/icons/" + name));
  }

}
