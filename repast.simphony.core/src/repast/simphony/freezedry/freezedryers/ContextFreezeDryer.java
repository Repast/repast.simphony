package repast.simphony.freezedry.freezedryers;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.freezedry.*;
import repast.simphony.freezedry.freezedryers.proj.ProjectionDryer;
import repast.simphony.space.projection.Projection;
import repast.simphony.valueLayer.ValueLayer;
import simphony.util.messages.MessageCenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ContextFreezeDryer extends AbstractFreezeDryer<DefaultContext<?>> {
	public static final String VALUE_LAYERS_KEY = "valueLayers";

	public static final String CONTEXT_TYPE_ID_KEY = "contextTypeId";

	public static final String CONTEXT_ID_KEY = "contextId";

	public static final String PROPERTY_PROJECTIONS_KEY = "projectionsFromProperties";

	public static final String NORMAL_PROJECTIONS_KEY = "projectionsFromDefault";

	public static final String PROJECTION_TYPE_KEY = "projectionType";

	private static final MessageCenter LOG = MessageCenter
			.getMessageCenter(ContextFreezeDryer.class);

	public ContextFreezeDryer(FreezeDryedRegistry registry) {
		super(registry);
	}

	public FreezeDryedObject freezeDry(String id, DefaultContext<?> context)
			throws FreezeDryingException {
    FreezeDryedObject fdo = new FreezeDryedObject(context.getId().toString(), context.getClass());

		storeMetaData(fdo, context);
		storeChildren(fdo, context);
		storeProjections(fdo, context);
		storeValueLayers(fdo, context);
		storeSubContexts(fdo, context);

		return fdo;
	}

	private void storeSubContexts(FreezeDryedObject fdo, DefaultContext<?> context)
			throws FreezeDryingException {
		ArrayList<Context> subContexts = new ArrayList<Context>();
		for (Context<?> subContext : context.getSubContexts()) {
			subContexts.add(subContext);
		}

		FreezeDryUtils.addComplex(registry, fdo, "subContexts", subContexts);
	}

	private void storeValueLayers(FreezeDryedObject fdo, DefaultContext<?> context)
			throws FreezeDryingException {
		ArrayList<ValueLayer> list = new ArrayList<ValueLayer>(context.getValueLayers());
		FreezeDryUtils.addComplex(registry, fdo, VALUE_LAYERS_KEY, list);
	}

	protected void storeMetaData(FreezeDryedObject fdo, DefaultContext<?> context) {
		fdo.put(CONTEXT_ID_KEY, context.getId());
		fdo.put(CONTEXT_TYPE_ID_KEY, context.getTypeID());
	}

	protected void storeChildren(FreezeDryedObject fdo, Context context)
			throws FreezeDryingException {
		for (Object child : (Collection<?>) context) {
			FreezeDryedParentChild cfdo = new FreezeDryedParentChild(context.getClass(), fdo
					.getId(), child.getClass(), registry.getId(child));
			fdo.addChild(cfdo);
		}
	}

	protected void storeProjections(FreezeDryedObject fdo, Context<?> context)
			throws FreezeDryingException {
		ArrayList<Map<String, Object>> projectionProps = new ArrayList<Map<String, Object>>();
		ArrayList<Projection> normalProjections = new ArrayList<Projection>();
		for (Projection proj : context.getProjections()) {

			ProjectionDryer dryer = ProjectionDryer.getDryer(proj.getClass());
			if (dryer == null) {
				normalProjections.add(proj);
				LOG.info("Could not find projection dryer for freeze drying projection '" + proj
						+ "' in context '" + context + "', using the default freeze dryer.");
			} else {
				Map<String, Object> map = dryer.getProperties(context, proj);
				map.put(PROJECTION_TYPE_KEY, proj.getClass());
				projectionProps.add(map);
			}
		}
		FreezeDryUtils.addComplex(registry, fdo, PROPERTY_PROJECTIONS_KEY, projectionProps);
		FreezeDryUtils.addComplex(registry, fdo, NORMAL_PROJECTIONS_KEY, normalProjections);

	}

	@SuppressWarnings("unchecked")
	public DefaultContext rehydrate(FreezeDryedObject fdo) throws FreezeDryingException {
		DefaultContext context = instantiate(fdo);

		loadMetaData(fdo, context);
		loadChildren(fdo, context);
		loadProjections(fdo, context);
		loadValueLayers(fdo, context);
		loadSubContexts(fdo, context);

		return context;
	}

	private void loadSubContexts(FreezeDryedObject fdo, DefaultContext context)
			throws FreezeDryingException {
		Iterable subContexts = getIterable(fdo, "subContexts");
		if (subContexts != null) {
			for (Object obj : subContexts) {
				if (obj instanceof Context) {
					context.addSubContext((Context) obj);
				} else {
					LOG.warn("Found a non-context '" + obj
							+ "' when loading sub contexts for context '" + context + "'.");
				}
			}
		}
	}

	private void loadValueLayers(FreezeDryedObject fdo, DefaultContext context)
			throws FreezeDryingException {
		Iterable layers = getIterable(fdo, VALUE_LAYERS_KEY);
		if (layers == null) {
			LOG.info("Did not find any layers for context '" + context + "'.");
			return;
		}
		for (Object obj : layers) {
			if (obj instanceof ValueLayer) {
				ValueLayer valLayer = (ValueLayer) obj;
				context.addValueLayer(valLayer);
			} else {
				LOG.warn("Found a non-value layer '" + obj
						+ "' when loading value layers for context '" + context + "'.");
			}
		}
	}

	private void loadMetaData(FreezeDryedObject fdo, DefaultContext context)
			throws FreezeDryingException {
		String typeId = getString(fdo, CONTEXT_TYPE_ID_KEY);
		context.setTypeID(typeId);
	}

	protected Iterable getIterable(FreezeDryedObject fdo, String key) throws FreezeDryingException {
		return getVal(fdo, key, Iterable.class);
	}
	
	protected Projection getProjection(Object obj, String key) throws FreezeDryingException {
		return getVal(obj, Projection.class, key);
	}

	protected DefaultContext instantiate(FreezeDryedObject fdo) throws FreezeDryingException {
		if (fdo.get(CONTEXT_ID_KEY) == null) {
			FreezeDryingException ex = new FreezeDryingException(
					"Could not find context id in freeze dryed object '" + fdo + "'.");
			LOG.error(ex.getMessage(), ex);
			throw ex;
		}
		String contextId = getString(fdo, CONTEXT_ID_KEY);
		return new DefaultContext(contextId);
	}

	protected void loadChildren(FreezeDryedObject fdo, DefaultContext context)
			throws FreezeDryingException {
		for (FreezeDryedParentChild child : fdo.getChildren()) {
			context.add(getChild(child));
		}
	}

	protected void loadPropertyProjections(FreezeDryedObject fdo, Context context) throws FreezeDryingException {
		Iterable propertyProjections = getIterable(fdo, PROPERTY_PROJECTIONS_KEY);
		if (propertyProjections == null) {
			return;
		}

		for (Object obj : propertyProjections) {
			if (obj instanceof Map) {
				Map props = (Map) obj;
				ProjectionDryer dryer = ProjectionDryer.getDryer((Class) props
						.get(PROJECTION_TYPE_KEY));
				if (dryer == null) {
					LOG
							.warn("Could not find projection dryer for rehydrating projection with class '"
									+ props.get(PROJECTION_TYPE_KEY)
									+ "' in context '"
									+ context
									+ "'");
				} else {
					dryer.buildAndAddProjection(context, props);
				}
			} else {
				LOG.warn("Found a non-Map '" + obj
						+ "' when attempting to load up a property projection for context '"
						+ context + "', ignoring and continuing.");
			}
		}
	}
	
	protected void loadNormalProjections(FreezeDryedObject fdo, Context context) throws FreezeDryingException {
		Iterable projections = getIterable(fdo, NORMAL_PROJECTIONS_KEY);
		if (projections == null) {
			return;
		}

		for (Object obj : projections) {
			Projection proj = getProjection(obj, NORMAL_PROJECTIONS_KEY);
			if (proj != null) {
				context.addProjection(proj);
			}
		}
	}

	protected void loadProjections(FreezeDryedObject fdo, Context context)
			throws FreezeDryingException {
		loadPropertyProjections(fdo, context);
		loadNormalProjections(fdo, context);
	}

	public boolean handles(Class<?> clazz) {
		return DefaultContext.class.isAssignableFrom(clazz);
	}
}
