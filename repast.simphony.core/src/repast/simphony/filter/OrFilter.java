/**
 * 
 */
package repast.simphony.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter that evalutes to true if any of its component filters evaluate to true.
 * If the filter is empty, then this also returns true.
 * 
 * @author Nick Collier
 */
public class OrFilter<T> implements Filter<T> {
  
  private List<Filter<T>> filters = new ArrayList<Filter<T>>();
  
  /**
   * Creates an empty OrFilter.
   * 
   */
  public OrFilter() {}
  
  /**
   * Creates an OrFilter composed of the specified filters.
   * 
   * @param filters the filters to add
   */
  public OrFilter(Filter<T> ...filters) {
    for (Filter<T> filter : filters) {
      this.filters.add(filter);
    }
  }
  
  /**
   * Add the specified filter to this OrFilter.
   * 
   * @param filter the filter to add
   */
  public void addFilter(Filter<T> filter) {
    filters.add(filter);
  }
  
  /**
   * Gets an iterable over the filters that compose this OrFilter.
   * 
   * @return an iterable over the filters that compose this OrFilter.
   */
  public Iterable<Filter<T>> filters() {
    return filters;
  }

  /* (non-Javadoc)
   * @see repast.simphony.filter.Filter#evaluate(java.lang.Object)
   */
  public boolean evaluate(T object) {
    if (filters.size() == 0) return true;
    
    for (Filter<T> filter : filters) {
      if (filter.evaluate(object)) return true;
    }
    return false;
  }
}
