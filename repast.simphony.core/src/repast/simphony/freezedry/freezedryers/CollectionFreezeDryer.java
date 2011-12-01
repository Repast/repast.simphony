package repast.simphony.freezedry.freezedryers;

import java.util.Collection;

import repast.simphony.context.Context;
import repast.simphony.freezedry.AbstractFreezeDryer;
import repast.simphony.freezedry.FreezeDryedObject;
import repast.simphony.freezedry.FreezeDryedParentChild;
import repast.simphony.freezedry.FreezeDryedRegistry;
import repast.simphony.freezedry.FreezeDryingException;

public class CollectionFreezeDryer extends AbstractFreezeDryer<Collection> {
	public CollectionFreezeDryer(FreezeDryedRegistry registry) {
		super(registry);
	}

	public FreezeDryedObject freezeDry(String id, Collection o)
			throws FreezeDryingException {
		FreezeDryedObject fdo = new FreezeDryedObject(id, o.getClass());
		for (Object child : (Collection<?>) o) {
			FreezeDryedParentChild cfdo = new FreezeDryedParentChild(fdo
					.getClass(), fdo.getId(), child.getClass(), registry
					.getId(child));
			fdo.addChild(cfdo);
		}
		return fdo;
	}

	@SuppressWarnings("unchecked")
	public Collection rehydrate(FreezeDryedObject fdo)
			throws FreezeDryingException {
		Class clazz = fdo.getType();
		Collection instance = null;
		try {
			instance = (Collection) clazz.newInstance();
		} catch (InstantiationException e) {
			throw new FreezeDryingException(e);
		} catch (IllegalAccessException e) {
			throw new FreezeDryingException(e);
		}
		for (FreezeDryedParentChild cfdo : fdo.getChildren()) {
			instance.add(getChild(cfdo));
		}
		return instance;
	}

	public boolean handles(Class<?> clazz) {
		return Collection.class.isAssignableFrom(clazz) && !Context.class.isAssignableFrom(clazz);
	}
}
