package repast.simphony.freezedry.freezedryers;

import repast.simphony.freezedry.FreezeDryedObject;
import repast.simphony.freezedry.FreezeDryedRegistry;
import repast.simphony.freezedry.FreezeDryer;
import repast.simphony.freezedry.FreezeDryingException;

public class ClassFreezeDryer implements FreezeDryer<Class> {
	FreezeDryedRegistry registry;

	public ClassFreezeDryer(FreezeDryedRegistry registry) {
		this.registry = registry;
	}

	public Class rehydrate(FreezeDryedObject fdo) throws FreezeDryingException {
		String className = (String) fdo.get("className".toUpperCase());
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new FreezeDryingException(e);
		}
	}

	public FreezeDryedObject freezeDry(String id, Class o)
			throws FreezeDryingException {
		FreezeDryedObject fdo = new FreezeDryedObject(id, Class.class);
		fdo.put("className".toUpperCase(), o.getName());
		return fdo;
	}
	
	public boolean handles(Class<?> clazz) {
		return clazz.equals(Class.class);
	}
}
