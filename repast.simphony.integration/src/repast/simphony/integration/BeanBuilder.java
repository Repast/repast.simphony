/*CopyrighHere*/
package repast.simphony.integration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Stack;

import org.apache.commons.jxpath.JXPathContext;

import repast.simphony.integration.ObjectHolder.StorageType;
import repast.simphony.util.ClassUtilities;
import simphony.util.messages.MessageCenter;

/**
 * Writes data to a Bean. This doesn't actually "build" a bean class, but will load a given class
 * with data.
 * 
 * @author Jerry Vos
 */
public class BeanBuilder implements OutputBuilder<ObjectHolder, Object> {
	// TODO: throw when not initialized
	private MessageCenter msgCenter = MessageCenter.getMessageCenter(BeanBuilder.class);

	private Class<?> rootBeanClass;

	private Object root;

	private Object curObject;

	private Stack<ObjectHolder> objectPath;

	private JXPathContext curContext;

	private JXPathContext rootContext;

	/**
	 * Instantiates this object without a class to create a bean from. Before an instance created
	 * with this method can be used, {@link #setRootBeanClass(Class)} must be called. This is the
	 * same as <code>BeanBuilder(null)}</code>.
	 * 
	 * @see #BeanBuilder(Class)
	 */
	public BeanBuilder() {
		this(null);
	}

	/**
	 * Instantiates this object without a class to create a bean from. Before an instance created
	 * with this method can be used, {@link #setRootBeanClass(Class)} must be called. This is the
	 * same as <code>BeanBuilder(null)}</code>.
	 * 
	 * @see #BeanBuilder(Class)
	 */
	public BeanBuilder(Object target) {
		this(null);
		this.root = target;
	}
	
	/**
	 * Instantiates this with the specified class to use as the root to set values on. Classes
	 * 
	 * @see #setRootBeanClass(Class)
	 * 
	 * @param rootBeanClass
	 *            the class that will be the root of the value "tree"
	 */
	public BeanBuilder(Class<?> rootBeanClass) {
		this.rootBeanClass = rootBeanClass;

		this.objectPath = new Stack<ObjectHolder>();
	}

	/**
	 * Sets the class to use as the root for setting values. This will be the class instantiated and
	 * that will have top-level values set on it.
	 * 
	 * @param rootBeanClass
	 */
	public void setRootBeanClass(Class<?> rootBeanClass) {
		this.rootBeanClass = rootBeanClass;
	}

	/**
	 * Retrieves the class that will act as the base for setting values.
	 * 
	 * @return the specified root bean class
	 */
	public Class getRootBeanClass() {
		return rootBeanClass;
	}

	private Object instantiate(Class<?> toInstantiate) {
		try {
			return toInstantiate.newInstance();
		} catch (InstantiationException ex) {
			msgCenter.warn("Error instantiating class(" + toInstantiate + ").", ex);
			return null;
		} catch (IllegalAccessException ex) {
			msgCenter.warn("Error instantiating class(" + toInstantiate + ").", ex);
			return null;
		}
	}

	private Method findAddMethod(Class clazz, String name) {
		return ClassUtilities.findFirstMethodWithArgs(clazz, "add"
				+ ClassUtilities.getPropertyName(name), 1);
	}

	private Method findRemoveMethod(Class clazz, String name) {
		return ClassUtilities.findFirstMethodWithArgs(clazz, "remove"
				+ ClassUtilities.getPropertyName(name), 1);
	}

	/**
	 * Finds an "add" method with the specified name associated with it (ie addName) and
	 * instantiates an object of its argument type. It then will add the instantiated object to the
	 * current object (or the parent set in the name path) and set the current object to be the
	 * newly instantiated object.
	 * 
	 * @param name
	 *            a string that represents the name of the property to create an object for; if the
	 *            name is an xpath expression then this will attempt to grab an object using the
	 *            xpath expression and use the string from the last slash on as the name of the add
	 *            method. For example if the name is /fileDef/bob/prop, it will find /fileDef/Bob
	 *            and look for it's "addProp" method.
	 */
	public ObjectHolder createAndGoInto(String name) {
		Object parentObj = DataFileUtils.findExplicitTargetParent(this, name);
		if (parentObj == null) {
			parentObj = curObject;
		}
		name = DataFileUtils.getName(name);

		Method addMethod = findAddMethod(parentObj.getClass(), name);
		if (addMethod == null) {
			RuntimeException ex = new RuntimeException("Couldn't find add method for name(" + name
					+ ").");
			msgCenter.error("Couldn't find add method", ex);
			throw ex;
		}

		Class argType = addMethod.getParameterTypes()[0];
		Object arg = instantiate(argType);

		try {
			addMethod.invoke(curObject, arg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		ObjectHolder holder = new ObjectHolder(curObject, name, arg, StorageType.CHILD);

		curObject = arg;

		// update the context so that we're pointing at the nested one
		curContext = getContext(curObject);

		objectPath.push(holder);

		return holder;
	}

	private JXPathContext getContext(Object obj) {
		JXPathContext context;
		if (curContext != null) {
			context = JXPathContext.newContext(curContext, obj);
		} else {
			context = JXPathContext.newContext(obj);
		}
		context.setLenient(true);

		return context;
	}

	/**
	 * Sets the current object up a level in the hierarchy. If we're at the root object the behavior
	 * is unspecified.
	 */
	public void goUp() {
		curObject = objectPath.pop().getParent();
		curContext = curContext.getParentContext();
	}

	/**
	 * Sets the current object to be the root object.
	 */
	public void goRoot() {
		this.curObject = root;
		this.curContext = rootContext;
		objectPath.clear();
	}

	/**
	 * Writes a value to the specified object
	 */
	public ObjectHolder writeValue(String name, Object value) {
		Object parentObj = DataFileUtils.findExplicitTargetParent(this, name);

		if (parentObj == null) {
			parentObj = curObject;
		}

		name = DataFileUtils.getName(name);

		ObjectHolder holder = new ObjectHolder(parentObj, name, value, StorageType.VALUE);

		if (!ClassUtilities.setValue(parentObj, name, value)) {
			msgCenter.warn("There was an error setting value(" + value + ") on object(" + curObject
					+ ") + with value name(" + name + ")");
		}

		return holder;
	}

	/**
	 * Retrieves the object (an instance of the root bean class) that was written to by this
	 * instance.
	 * 
	 * @return the root object that had values written to it
	 */
	public Object getWrittenObject() {
		return root;
	}

	/**
	 * Detaches the current object from it's parent. This will either set the current object to null
	 * in the parent (if it was set with a set method) or remove it from the parent (if it was
	 * added).
	 */
	public void detach() {
		if (curObject == root) {
			RuntimeException ex = new RuntimeException("Cannot detach root object");
			msgCenter.error("Cannot detach root object", ex);
			throw ex;
		}
		ObjectHolder top = objectPath.pop();

		curObject = top.getParent();
		remove(curObject, top.getName(), top.getValue(), top.getStorageType());
	}

	private void remove(Object parent, String name, Object value, StorageType storageType) {
		switch (storageType) {
		case CHILD:
			removeChild(parent, name, value);
			break;
		case VALUE:
			removeValue(parent, name);
			break;
		default:
		// TODO: throw runtimeexception
		}
	}

	private void removeValue(Object parent, String name) {
		ClassUtilities.setValue(parent, name, null);
	}

	private void removeChild(Object parent, String name, Object value) {
		try {
			findRemoveMethod(parent.getClass(), name).invoke(parent, value);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Detaches each of the given objects from their parents. This will occur in the way as
	 * specified in {@link #detach()}, but for each object.
	 * 
	 * @see #detach()
	 */
	public void detach(Iterable<ObjectHolder> objsToDetach) {
		// set all the objects to null in their parent and/or remove them
		for (ObjectHolder holder : objsToDetach) {
			remove(holder.getParent(), holder.getName(), holder.getValue(), holder.getStorageType());
		}
	}

	/**
	 * Instantiates an instance of the root bean class and starts using it.
	 */
	public void initialize() {
		try {
			if (root == null) {
				root = instantiate(rootBeanClass);
			}
			buildRootContext();
			goRoot();
		} catch (Exception ex) {
			throw new RuntimeException("Couldn't instantiate root class", ex);
		}

	}

	private void buildRootContext() {
		// have to do this because otherwise getContext will make the old context be
		// the parent of the root
		curContext = null;
		rootContext = JXPathUtils.createRootContext(root, true);
		curContext = rootContext;
	}

	public Object selectNode(String path) {
		return selectNode(curContext, path);
	}

	public Object selectNode(Object curContext, String path) {
		if (path == null || curContext == null) {
			return null;
		}

		JXPathContext jxPathContext;
		if (curContext instanceof JXPathContext) {
			jxPathContext = (JXPathContext) curContext;
		} else {
			jxPathContext = JXPathContext.newContext(curContext);
		}

		try {
			return JXPathUtils.getXPathNode(rootContext, jxPathContext, path);
		} catch (Exception ex) {
			msgCenter.info("Error evaluating path(" + path + ")");
			return null;
		}
	}

	public List<?> selectNodes(String path) {
		return selectNodes(curContext, path);
	}

	public List<?> selectNodes(Object curContext, String path) {
		if (path == null || curContext == null) {
			return null;
		}

		JXPathContext jxPathContext;
		if (curContext instanceof JXPathContext) {
			jxPathContext = (JXPathContext) curContext;
		} else {
			jxPathContext = JXPathContext.newContext(curContext);
		}

		try {
			return JXPathUtils.getXPathNodes(rootContext, jxPathContext, path);
		} catch (Exception ex) {
			msgCenter.info("Error evaluating path(" + path + ")");
			return null;
		}
	}

	public Object getRoot() {
		return root;
	}

	public Object getValue(Object o) {
		return o;
	}

}
