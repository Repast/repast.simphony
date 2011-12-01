/**
 * 
 */
package repast.simphony.engine.watcher;

import repast.simphony.filter.Filter;

/**
 * Filter that evaluates to true if the path
 * contained by this filter is part of the evaluated path.
 * 
 * @author Nick Collier
 */
public class WatcherPathFilter implements Filter<String> {
  
  private String path;
  
  public WatcherPathFilter(String path) {
    this.path = path;
  }

  /* (non-Javadoc)
   * @see repast.simphony.filter.Filter#evaluate(java.lang.Object)
   */
  public boolean evaluate(String dataPath) {
    return dataPath.contains(path);
  }
}
