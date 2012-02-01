/**
 * 
 */
package repast.simphony.xml;

import repast.simphony.space.graph.DefaultEdgeCreator;
import repast.simphony.space.graph.EdgeCreator;
import repast.simphony.space.graph.RepastEdge;

/**
 * Edge creator that delegates the actual edge creation to 
 * a specified delegate. 
 * 
 * @author Nick Collier
 */
@SuppressWarnings("rawtypes")
public class DelegatingEdgeCreator implements EdgeCreator {
  
  private EdgeCreator delegate;
  
  public DelegatingEdgeCreator() {
    delegate = new DefaultEdgeCreator();
  }
  
  /**
   * Sets the delegate to the specified EdgeCreator.
   * 
   * @param delegate
   */
  public void initDelegate(EdgeCreator delegate) {
    this.delegate = delegate;
  }

  /* (non-Javadoc)
   * @see repast.simphony.space.graph.EdgeCreator#getEdgeType()
   */
  public Class<?> getEdgeType() {
    return delegate.getClass();
  }

  /* (non-Javadoc)
   * @see repast.simphony.space.graph.EdgeCreator#createEdge(java.lang.Object, java.lang.Object, boolean, double)
   */
  @SuppressWarnings("unchecked")
  public RepastEdge<?> createEdge(Object source, Object target, boolean isDirected, double weight) {
    return delegate.createEdge(source, target, isDirected, weight);
  }
}
