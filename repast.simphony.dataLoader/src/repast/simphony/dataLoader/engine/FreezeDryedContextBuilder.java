/*CopyrightHere*/
package repast.simphony.dataLoader.engine;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.freezedry.FreezeDryedRegistry;
import repast.simphony.freezedry.FreezeDryingException;
import simphony.util.messages.MessageCenter;

public abstract class FreezeDryedContextBuilder implements ContextBuilder {
	private static final MessageCenter LOG = MessageCenter
			.getMessageCenter(FreezeDryedContextBuilder.class);

	private Iterable<Class<?>> classesToLoad;

	private Object freezeDryedContextId;

	private boolean createContextFromData;

	public FreezeDryedContextBuilder(boolean createContextFromData, Iterable<Class<?>> classesToLoad,
	                                 Object contextId) {
		super();
		this.createContextFromData = createContextFromData;
		this.classesToLoad = classesToLoad;
		this.freezeDryedContextId = contextId;
	}

	public Context build(Context context) {
		if (context.getId().equals(freezeDryedContextId)) {
			return create(context.getId());
		} else {
			load(context);
		}
		return context;
	}

	protected abstract void registerWriters(FreezeDryedRegistry registry)
			throws FreezeDryingException;

	public Context create(Object creatorID) {
		FreezeDryedRegistry registry = new FreezeDryedRegistry();
		try {
			registerWriters(registry);

			Context context;

			if (createContextFromData) {
				// TODO: what if it's not a DefaultContext
				return registry.rehydrate(DefaultContext.class, freezeDryedContextId.toString());
			} else {
				// context = runs.getMasterContext().getSubContext(contextId);
				// if (context == null) {
				context = new DefaultContext(freezeDryedContextId);
				load(registry, context);
				return context;
				// }
			}
		} catch (FreezeDryingException ex) {
			LOG.warn("Error attempting to create context with id '" + freezeDryedContextId + "'.", ex);
			return null;
		}
	}

	public void load(Context context) {
		FreezeDryedRegistry registry = new FreezeDryedRegistry();
		try {
			registerWriters(registry);
			load(registry, context);
		} catch (FreezeDryingException e) {
			LOG.warn("Error attempting to load context with id '" + freezeDryedContextId + "'.", e);
		}
	}
	
	public void load(FreezeDryedRegistry registry, Context context) {
		try {
			for (Class clazz : classesToLoad) {
				if (!DefaultContext.class.isAssignableFrom(clazz)) {
					context.addAll(registry.rehydrate(clazz));
				}
			}

			// runState.getMasterContext().addSubContext(context);
		} catch (FreezeDryingException ex) {
			LOG.warn("Error attempting to load context with id '" + context.getId() + "'.", ex);
		}
	}

	public final Iterable<Class<?>> getClassesToLoad() {
		return classesToLoad;
	}

	public final Object getFreezeDryedContextId() {
		return freezeDryedContextId;
	}

	public final boolean isCreateContextFromData() {
		return createContextFromData;
	}
}
