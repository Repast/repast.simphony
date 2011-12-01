package repast.simphony.visualization.visualization3D.style;


/**
 * @author Nick Collier
 * @version $Revision: 1.3 $ $Date: 2006/01/06 22:53:54 $
 */
public interface EdgeStyle3D<T> extends Style3D<T> {
  
  public enum EdgeType { SHAPE, LINE};
  
  public EdgeType getEdgeType();
  
  public float edgeRadius(T obj); 
}
