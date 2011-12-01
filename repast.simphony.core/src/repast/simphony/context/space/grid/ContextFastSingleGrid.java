/*CopyrightHere*/
package repast.simphony.context.space.grid;

import repast.simphony.context.Context;
import repast.simphony.context.ContextEvent;
import repast.simphony.context.ContextListener;
import repast.simphony.space.grid.FastDenseSingleOccuGrid;
import repast.simphony.space.grid.GridAdder;
import repast.simphony.space.grid.GridPointTranslator;
import repast.simphony.space.projection.ProjectionEvent;

/**
 * Default implementation of an n-dimensional grid.
 */
public class ContextFastSingleGrid<T> extends FastDenseSingleOccuGrid<T> implements
		ContextListener<T> {

	/**
	 * Constructs this space with the specified name, adder, translator,
	 * accessor and size. The size is the size of the space meaning [3, 3] is a
	 * 3x3 space.
	 * 
	 * @param name
	 *            the name of the space
	 * @param size
	 *            the dimensions of the space
	 */
	public ContextFastSingleGrid(String name, GridAdder<T> adder,
			GridPointTranslator translator,
			int... size) {
		super(name, adder, translator, size);
	}
	
	/**
	 * If the event is an add this will put the object at the (0, 0, ..., 0)
	 * location. If it is a remove it will remove the object from its location
	 * map.
	 * 
	 * @param evt
	 *            a {@link ContextEvent} that occurred to the context this is a
	 *            part of
	 */
	public void eventOccured(ContextEvent<T> evt) {
		T obj = evt.getTarget();
		ContextEvent.EventType type = evt.getType();
		if (type == ContextEvent.ADDED) {
			addFromContext(evt.getTarget());
		} else if (type == ContextEvent.REMOVED) {
			remove(obj);
		} else if (type == ContextEvent.EventType.PROJECTION_ADDED
				&& evt.getProjection() == this) {
			addAll(evt.getContext());
		} else if (type == ContextEvent.EventType.PROJECTION_REMOVED
				&& evt.getProjection() == this) {
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
