/**
 * 
 */
package repast.simphony.eclipse.util;

import org.eclipse.core.resources.IFile;

/**
 * Interface for classes that determine whether or not a file should be deleted.
 * 
 * @author Nick Collier
 */
public interface ToDeleteFilter {
  
  /**
   * Returns true if the specified file should be deleted. 
   * 
   * @param file
   * 
   * @return true if the specified file should be deleted, otherwise false.
   */
  boolean delete(IFile file);

}
