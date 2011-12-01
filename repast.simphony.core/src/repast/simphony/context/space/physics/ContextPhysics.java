package repast.simphony.context.space.physics;

import repast.simphony.context.Context;
import repast.simphony.context.ContextEvent;
import repast.simphony.context.ContextListener;
import repast.simphony.space.continuous.ContinuousAdder;
import repast.simphony.space.continuous.PointTranslator;
import repast.simphony.space.physics.DefaultPhysicsSpace;
import repast.simphony.space.projection.ProjectionEvent;

/**
 * Default implementation of an n-dimensional continuous space.
 */
public class ContextPhysics<T> extends DefaultPhysicsSpace<T> implements
        ContextListener<T> {


  /**
   * Constructs this space with the specified name, adder, translator,
   * accessor and size. The size is the size of the space meaning [3, 3] is a
   * 3x3 space.
   *
   * @param name the name of the space
   * @param size the dimensions of the space
   */
  public ContextPhysics(String name, ContinuousAdder<T> adder,
                      PointTranslator translator, double xdim, double ydim, double zdim) {
    super(name, adder, translator, xdim, ydim, zdim);
  }

  /**
   * Constructs this space with the specified name, adder, translator,
   * accessor and size. The size is the size of the space meaning [3, 3] is a
   * 3x3 space.
   *
   * @param name   the name of the space
   * @param size   the dimensions of the space
   * @param origin the origin of the space
   */
  public ContextPhysics(String name, ContinuousAdder<T> adder,
                      PointTranslator translator, double[] size, double[] origin) {
    super(name, adder, translator, size, origin);
  }

  /**
   * If the event is an add this will put the object at the (0, 0, ..., 0)
   * location. If it is a remove it will remove the object from its location
   * map.
   *
   * @param evt a {@link ContextEvent} that occurred to the context this is a
   *            part of
   */
  public void eventOccured(ContextEvent<T> evt) {
    T obj = evt.getTarget();
    ContextEvent.EventType type = evt.getType();
    if (type == ContextEvent.ADDED) {
      addFromContext(evt.getTarget());
    } else if (type == ContextEvent.REMOVED) {
      remove((T) obj);
    } else if (type == ContextEvent.EventType.PROJECTION_ADDED
            && evt.getProjection().equals(this)) {
      addAll(evt.getContext());
    } else if (type == ContextEvent.EventType.PROJECTION_REMOVED
            && evt.getProjection().equals(this)) {
      removeAll();
    }
  }

  protected void addFromContext(T t) {
    // this.accessor.put(t, locationCoordinatesMap,
    agentLocationMap.put(t, new PointHolder());
    adder.add(this, t);
    fireProjectionEvent(new ProjectionEvent(this, t,
            ProjectionEvent.OBJECT_ADDED));
  }

  protected void addAll(Context<T> context) {
    for (T t : context) {
      addFromContext(t);
		}
	}
}
