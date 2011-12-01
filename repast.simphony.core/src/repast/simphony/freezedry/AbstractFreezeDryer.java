/*CopyrightHere*/
package repast.simphony.freezedry;

import simphony.util.messages.MessageCenter;

public abstract class AbstractFreezeDryer<U> implements FreezeDryer<U> {
	private static final MessageCenter LOG = MessageCenter
			.getMessageCenter(AbstractFreezeDryer.class);

	protected FreezeDryedRegistry registry;

	public AbstractFreezeDryer(FreezeDryedRegistry registry) {
		super();
		this.registry = registry;
	}

	protected <T> T getVal(FreezeDryedObject fdo, String key, Class<T> type)
			throws FreezeDryingException {
		Object obj = fdo.get(key);

		return getVal(obj, type, key);
	}

	protected <T> T getVal(Object obj, Class<T> type, String key) throws FreezeDryingException {
		T val;
		if (obj instanceof FreezeDryedParentChild) {
			FreezeDryedParentChild idPC = (FreezeDryedParentChild) obj;
			Object idObject = registry.getObject(idPC.getChildId(), idPC.getChildClass());
			if (!type.isAssignableFrom(idObject.getClass())) {
				LOG.warn("'" + key + " did not resolve to the correrct type (was '" + idObject
						+ "'), returning null for value.");
				val = null;
			} else {
				val = type.cast(idObject);
			}
		} else if (type.isAssignableFrom(obj.getClass())) {
			val = type.cast(obj);
		} else {
			LOG.warn("Unsupported type found for object (" + obj + ") " + key
					+ " value. Wanted type '" + type + "', returning null.");
			val = null;
		}
		return val;
	}

	protected String getString(FreezeDryedObject fdo, String key) throws FreezeDryingException {
		String val;
		if (fdo.get(key) instanceof FreezeDryedParentChild) {
			FreezeDryedParentChild idPC = (FreezeDryedParentChild) fdo.get(key);
			Object idObject = registry.getObject(idPC.getChildId(), idPC.getChildClass());
			if (!(idObject instanceof String)) {
				LOG.warn("'" + key + " did not resolve to a string (" + idObject
						+ "), using its toString value as the value for " + key + ".");
				val = idObject.toString();
			} else {
				val = (String) idObject;
			}
		} else if (fdo.get(key) instanceof String) {
			val = (String) fdo.get(key);
		} else {
			LOG.warn("Unsupported type found for context (" + fdo.get(key)
					+ "), using its toString as the context's " + key + ".");
			val = fdo.get(key).toString();
		}

		return val;
	}

	protected Object getChild(FreezeDryedParentChild cfdo) throws FreezeDryingException {
		String id = cfdo.getChildId();
		Class<?> childClass = cfdo.getChildClass();
		Object child = registry.getObject(id, childClass);
		return child;
	}

}
