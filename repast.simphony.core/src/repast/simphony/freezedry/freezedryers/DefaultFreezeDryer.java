package repast.simphony.freezedry.freezedryers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

import repast.simphony.freezedry.AbstractFreezeDryer;
import repast.simphony.freezedry.FieldUtilities;
import repast.simphony.freezedry.FreezeDryedObject;
import repast.simphony.freezedry.FreezeDryedParentChild;
import repast.simphony.freezedry.FreezeDryedRegistry;
import repast.simphony.freezedry.FreezeDryer;
import repast.simphony.freezedry.FreezeDryingException;
import simphony.util.messages.MessageCenter;

public class DefaultFreezeDryer extends AbstractFreezeDryer<Object> {
	private static final MessageCenter LOG = MessageCenter
			.getMessageCenter(DefaultFreezeDryer.class);

	private FieldUtilities util = FieldUtilities.INSTANCE;

	public DefaultFreezeDryer(FreezeDryedRegistry registry) {
		super(registry);
	}

	@SuppressWarnings("unchecked")
	public FreezeDryedObject freezeDry(String id, Object o) throws FreezeDryingException {
		Class type = o.getClass();
		FreezeDryedObject fdo = new FreezeDryedObject(id, type);
		type = type.getSuperclass();

		boolean accessibility;

		Stack<Class> superStack = new Stack<Class>();
		while (!type.equals(Object.class)) {
			superStack.push(type);
			if (!registry.getFreezeDryer(type).equals(this)) {
				Class parentType = superStack.pop();
				FreezeDryer dryer = registry.getFreezeDryer(parentType);
				FreezeDryedObject pfdo = dryer.freezeDry(UUID.randomUUID().toString(), o);
				Map<String, Object> parentProps = pfdo.getProperties();
				for (String key : parentProps.keySet()) {
					fdo.put(type.getName() + "_" + key, parentProps.get(key));
				}
				for (FreezeDryedParentChild fdpc : pfdo.getChildren()) {
					fdo.addChild(fdpc);
				}
				break;
			}
			type = type.getSuperclass();
		}
		while (!superStack.isEmpty()) {
			type = superStack.pop();
			for (Field field : type.getDeclaredFields()) {
				accessibility = field.isAccessible();
				try {
					field.setAccessible(true);
					if (Modifier.isTransient(field.getModifiers())
							|| Modifier.isFinal(field.getModifiers())
							|| Modifier.isStatic(field.getModifiers())) {
						continue;
					}
					if (util.isPrimitive(field)) {
						fdo.put(type.getName() + "_" + field.getName(), field.get(o));
					} else if (util.isPrimitiveArray(field)) {
						fdo.put(type.getName() + "_" + field.getName(), field.get(o));
					} else {
						Object child = field.get(o);
						if (child != null) {
							String childKey = registry.getId(child);
							fdo.put(field.getName() + FreezeDryer.CLASS_MARKER, util
									.getTypeAsString(child));
							fdo.put(field.getName() + FreezeDryer.ID_MARKER, childKey);
						} else {
							fdo.put(field.getName() + FreezeDryer.CLASS_MARKER, null);
							fdo.put(field.getName() + FreezeDryer.ID_MARKER, null);
						}
					}
				} catch (Exception ex) {
					LOG.error("Error writing child field '" + field + "' for '" + o + "'.", ex);
					throw new FreezeDryingException(ex);
				} finally {
					field.setAccessible(accessibility);
				}
			}
		}
		for (Field field : o.getClass().getDeclaredFields()) {
			accessibility = field.isAccessible();

			try {
				field.setAccessible(true);
				if (Modifier.isTransient(field.getModifiers())
						|| Modifier.isFinal(field.getModifiers())
						|| Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				if (util.isPrimitive(field)) {
					fdo.put(field.getName(), field.get(o));
				} else if (util.isPrimitiveArray(field)) {
					fdo.put(field.getName(), field.get(o));
				} else {
					Object child = field.get(o);
					if (child != null) {
						String childKey = registry.getId(child);
						fdo.put(field.getName() + FreezeDryer.CLASS_MARKER, util
								.getTypeAsString(child));
						fdo.put(field.getName() + FreezeDryer.ID_MARKER, childKey);
					} else {
						fdo.put(field.getName() + FreezeDryer.CLASS_MARKER, null);
						fdo.put(field.getName() + FreezeDryer.ID_MARKER, null);
					}
				}
			} catch (Exception ex) {
				LOG.error("Error writing child field '" + field + "' for '" + o + "'.", ex);
				throw new FreezeDryingException(ex);
			} finally {
				field.setAccessible(accessibility);
			}
		}
		return fdo;
	} 
	
	

	@SuppressWarnings("unchecked")
	public Object rehydrate(FreezeDryedObject fdo) throws FreezeDryingException {
		try {
			Class type = fdo.getType();

			Constructor cons = null;
			Object instance = null;
			try {
				cons = type.getConstructor(new Class[0]);
				instance = cons.newInstance(new Object[0]);
			} catch (NoSuchMethodException nsme) {
				cons = type.getConstructors()[0];
				Class[] params = cons.getParameterTypes();
				Object[] paramInst = new Object[params.length];
				for (int i = 0; i < params.length; i++) {
          if (params[i].equals(String.class)) {
            paramInst[i] = "";
          } else if (!params[i].isPrimitive()) {
						paramInst[i] = null;
					} else {
						paramInst[i] = 0;
					}
				}
				instance = cons.newInstance(paramInst);	
			}
			boolean accessibility;
			for (Field field : getFields(type)) {
				accessibility = field.isAccessible();
				try {
					field.setAccessible(true);
					if (Modifier.isTransient(field.getModifiers())
							|| Modifier.isFinal(field.getModifiers())) {
						continue;
					}
					if (util.isPrimitive(field)) {
						// registry.getPrimitiveDryer().rehydrate()
						// registry.rehydrateObject(field.getType(), fdo.get(field.getName())));
						Object val = fdo.get(field.getName());
						if (val == null) {
							continue;
						}
						if (field.getType().equals(Double.TYPE)) {
							field.set(instance, ((Number) fdo.get(field.getName())).doubleValue());
						} else if (field.getType().equals(Integer.TYPE)) {
							field.set(instance, ((Number) fdo.get(field.getName())).intValue());
						} else if (field.getType().equals(Short.TYPE)) {
							field.set(instance, ((Number) fdo.get(field.getName())).shortValue());
						} else if (field.getType().equals(Float.TYPE)) {
							field.set(instance, ((Number) fdo.get(field.getName())).floatValue());
						} else if (field.getType().equals(Byte.TYPE)) {
							field.set(instance, ((Number) fdo.get(field.getName())).byteValue());
						} else if (field.getType().equals(Long.TYPE)) {
							field.set(instance, ((Number) fdo.get(field.getName())).longValue());
						} else if (field.getType().equals(String.class)) {
							field.set(instance, fdo.get(field.getName()));
						} else if (field.getType().equals(Boolean.TYPE)) {
							field.set(instance, Boolean.valueOf(fdo.get(field.getName()).toString()));
						} else if (field.getType().equals(Character.TYPE)) {
							Object fdoVal = fdo.get(field.getName());
							if (fdoVal instanceof String) {
								if (((String) fdoVal).length() > 0) {
									fdoVal = ((String) fdoVal).charAt(0);
								}
							}
							field.set(instance, (Character) fdoVal);
						}
					} else {
						String typeName = (String) fdo.get(field.getName()
								+ FreezeDryer.CLASS_MARKER);
						String id = (String) fdo.get(field.getName() + FreezeDryer.ID_MARKER);
						if (typeName == null || id == null) {
							continue;
						}
						Class childType = util.getClassFromString(typeName);
						Object child = registry.getObject(id, childType);
						field.set(instance, child);
					}
				} finally {
					field.setAccessible(accessibility);
				}

			}
			return instance;
		} catch (SecurityException e) {
			throw new FreezeDryingException(e);
		} catch (IllegalAccessException e) {
			throw new FreezeDryingException(e);
		} catch (ClassNotFoundException e) {
			throw new FreezeDryingException(e);
		} catch (InstantiationException ex) {
			LOG.error("Error instantiating " + fdo + ".", ex);
			throw new FreezeDryingException("Error instantiating " + fdo + ".", ex);
		} catch (InvocationTargetException ex) {
			LOG.error("Error instantiating " + fdo + ".", ex);
			throw new FreezeDryingException("Error instantiating " + fdo + ".", ex);
		}
	}

	private List<Field> getFields(Class type) {
		List<Field> fields = new ArrayList<Field>();

		while (type != Object.class) {
			for (Field field : type.getDeclaredFields()) {
				fields.add(field);
			}

			type = type.getSuperclass();
		}
		return fields;
	}

	public boolean handles(Class<?> clazz) {
		return true;
	}
}
