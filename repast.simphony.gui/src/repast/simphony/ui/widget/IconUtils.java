package repast.simphony.ui.widget;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconUtils {

  /**
   * Attemps to load an icon as resource and if that fails then
   * directly from the "icons" directory.
   * 
   * @param iconFile the icon file name
   * @return the loaded icon
   */
  public static Icon loadIcon(String iconFile) {
    // a specific classloader that may not be there in testing.
    Icon icon;
    try {
     icon = new ImageIcon(IconUtils.class.getClassLoader().getResource(iconFile));
    } catch (Exception ex) {
      icon = new ImageIcon("./icons/" + iconFile);
    }
    
    return icon;
  }

}
