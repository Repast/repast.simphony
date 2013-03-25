/**
 * 
 */
package repast.simphony.eclipse;

import org.eclipse.ui.IPageLayout;

/**
 * Interface for classes that can help to configure the Repast Simphony Perspective.
 * 
 * @author Nick Collier
 */
public interface IRepastPerspectiveConfigurator {
  
  /**
   * Configures the Perspective using the specified layout.
   * 
   * @param layout
   */
  void configurePerspective(IPageLayout layout);

}
