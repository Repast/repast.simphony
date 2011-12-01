package repast.simphony.visualization;

import java.util.Iterator;

import repast.simphony.context.Context;

/**
 * Default implementation of DisplayData that wraps a Context. The implemented
 * methods are then forwarded to the wrapped Context.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DefaultDisplayData<T> extends AbstractDisplayData<T> {

  private Context<T> context;

  /**
   * Gets the container that contains the objects we want to display.
   * 
   * @return the container that contains the objects we want to display.
   */
  public VisualizedObjectContainer<T> getContainer() {
    return new VisualizedObjectContainer<T>() {
      public void add(T obj) {
	context.add(obj);
      }

      public void remove(T obj) {
	context.remove(obj);
      }

      public Iterator<T> iterator() {
	return context.iterator();
      }

      public boolean contains(T obj) {
	return context.contains(obj);
      }
    };
  }

  /**
   * Creates a DefaultDisplayData that delegates its methods to the specified
   * context.
   * 
   * @param context
   */
  public DefaultDisplayData(Context<T> context) {
    this.context = context;
  }

  /**
   * Adds the named projection to the list of projections to display.
   * 
   * @param name
   *          the name of the projection to add.
   */
  public void addProjection(String name) {
    projs.add(context.getProjection(name));
  }

  /**
   * Adds the named ValueLayer to the list of ValueLayers to display.
   * 
   * @param name
   *          the name of the ValueLayer to add.
   */
  public void addValueLayer(String name) {
    valueLayers.add(context.getValueLayer(name));
  }

  /**
   * Gets the objects to be initially displayed.
   * 
   * @return an iterable over the objects to be initially displayed.
   */
  public Iterable<T> objects() {
    return context;
  }

}
