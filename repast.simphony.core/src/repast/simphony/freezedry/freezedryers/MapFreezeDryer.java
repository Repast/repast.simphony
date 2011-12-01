package repast.simphony.freezedry.freezedryers;

import repast.simphony.freezedry.*;
import repast.simphony.util.collections.Pair;
import simphony.util.messages.MessageCenter;

import java.util.Map;

public class MapFreezeDryer extends AbstractFreezeDryer<Map<?, ?>> {
	private static final MessageCenter LOG = MessageCenter.getMessageCenter(MapFreezeDryer.class);
	
	
	public MapFreezeDryer(FreezeDryedRegistry registry) {
		super(registry);
	}

	@SuppressWarnings("unchecked")
	public FreezeDryedObject freezeDry(String id, Map<?, ?> o)
			throws FreezeDryingException {
		FreezeDryedObject fdo = new FreezeDryedObject(id, o.getClass());
		for (Object key : o.keySet()) {
			Pair<Object, Object> aPair = new Pair<Object, Object>(key, o.get(key));
			FreezeDryedParentChild child = new FreezeDryedParentChild(o.getClass(), id, Pair.class,
					registry.getId(aPair));
			fdo.addChild(child);
		}
		return fdo;
	}

	@SuppressWarnings("unchecked")
	public Map<?, ?> rehydrate(FreezeDryedObject fdo)
			throws FreezeDryingException {
		Class type = fdo.getType();
		try {
			Map instance = (Map) type.newInstance();
			for (FreezeDryedParentChild child : fdo.getChildren()) {
				if (!(Pair.class == child.getChildClass())) {
					LOG.warn("Found an invalid child '" + child + "' in freeze dryed object (not a Pair), ignoring it and continuing.");
					continue;
				}
				Pair pair = (Pair) registry.getObject(child.getChildId(), Pair.class);
				instance.put(pair.getFirst(), pair.getSecond());
			}
			return instance;
		} catch (InstantiationException e) {
			throw new FreezeDryingException(e);
		} catch (IllegalAccessException e) {
			throw new FreezeDryingException(e);
		}
	}
	
	public boolean handles(Class<?> clazz) {
		return Map.class.isAssignableFrom(clazz);
	}
}
