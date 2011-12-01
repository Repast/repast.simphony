package repast.simphony.freezedry.freezedryers;

import java.lang.reflect.Array;

import repast.simphony.freezedry.AbstractFreezeDryer;
import repast.simphony.freezedry.FieldUtilities;
import repast.simphony.freezedry.FreezeDryedObject;
import repast.simphony.freezedry.FreezeDryedParentChild;
import repast.simphony.freezedry.FreezeDryedRegistry;
import repast.simphony.freezedry.FreezeDryingException;

public class ArrayFreezeDryer extends AbstractFreezeDryer {
	protected FieldUtilities util = FieldUtilities.INSTANCE;

	public ArrayFreezeDryer(FreezeDryedRegistry registry) {
		super(registry);
	}

	@SuppressWarnings("unchecked")
	public Object rehydrate(FreezeDryedObject fdo) throws FreezeDryingException {
		String arrayType = fdo.getType().getComponentType().getName();
		int size = fdo.numChildren();
		Object array = null;
		try {
			array = Array.newInstance(FieldUtilities.INSTANCE.getClassFromString(arrayType), size);
		} catch (ClassNotFoundException cnfe) {
			throw new FreezeDryingException(cnfe);
		}

		try {
			for (FreezeDryedParentChild cfdo : fdo.getChildren()) {
				int index = cfdo.getIndex();
//				System.out.println("index");
				Object child = getChild(cfdo);
				Array.set(array, index, child);
			}
			return array;
		} catch (NegativeArraySizeException e) {
			throw new FreezeDryingException(e);
		}
	}
	
	public FreezeDryedObject freezeDry(String id, Object o)
			throws FreezeDryingException {
		FreezeDryedObject fdo = new FreezeDryedObject(id, o.getClass());
		int size = Array.getLength(o);
		for (int i = 0; i < size; i++) {
			Object child = Array.get(o, i);
			if (child != null) {
				FreezeDryedParentChild cfdo = new FreezeDryedParentChild(o
						.getClass(), fdo.getId(), child.getClass(), registry
						.getId(child), i);
				fdo.addChild(cfdo);
			}
		}
		return fdo;
	}

	public boolean handles(Class clazz) {
		return clazz.isArray();
	}
}
