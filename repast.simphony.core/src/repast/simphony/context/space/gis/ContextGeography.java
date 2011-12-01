package repast.simphony.context.space.gis;

import repast.simphony.context.Context;
import repast.simphony.context.ContextEvent;
import repast.simphony.context.ContextListener;
import repast.simphony.space.gis.DefaultGeography;
import repast.simphony.space.projection.ProjectionEvent;

public class ContextGeography<T> extends DefaultGeography<T> implements
		ContextListener<T> {

	public ContextGeography(String name) {
		super(name);
	}

	public ContextGeography(String name, String crsCode) {
		super(name, crsCode);
	}

	void addFromContext(Context<T> context) {
		for (T object : context) {
      addFromContext(object);
		}
	}

  void addFromContext(T object) {
    add(object);
		adder.add(this, object);
    fireProjectionEvent(new ProjectionEvent(this, object, ProjectionEvent.Type.OBJECT_ADDED));
  }

  public void eventOccured(ContextEvent<T> evt) {
		ContextEvent.EventType type = evt.getType();
		if (type == ContextEvent.ADDED) {
			addFromContext(evt.getTarget());
		} else if (type == ContextEvent.REMOVED) {
			remove(evt.getTarget());
		} else if (type == ContextEvent.EventType.PROJECTION_ADDED
				&& evt.getProjection().equals(this)) {
			addFromContext(evt.getContext());
		} else if (type == ContextEvent.EventType.PROJECTION_REMOVED
				&& evt.getProjection().equals(this)) {
			removeAll(evt.getContext());
		}
	}

  void removeAll(Context<T> context) {
    for (T object : context) {
      remove(object);
    }
  }
}
