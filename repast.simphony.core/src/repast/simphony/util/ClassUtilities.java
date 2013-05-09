/*CopyRightHere*/
package repast.simphony.util;

import org.apache.commons.lang3.StringUtils;
import simphony.util.messages.MessageCenter;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtilities {

  private static final MessageCenter msgCenter = MessageCenter
					.getMessageCenter(ClassUtilities.class);

  public static boolean doWarn = true;


  private static Set<Class> numberTypeSet = new HashSet<Class>();
	static {
		numberTypeSet.add(int.class);
		numberTypeSet.add(double.class);
		numberTypeSet.add(float.class);
		numberTypeSet.add(byte.class);
		numberTypeSet.add(short.class);
		numberTypeSet.add(long.class);
	}

	/**
	 * Search for and return the named field in the current class and in its super classes. If the
	 * field is not found, this throws a NoSuchFieldException
	 *
	 * @param clazz the clazz to begin the search for the field in.
	 * @param name  the name of the field to find
	 * @return the found field
	 * @throws NoSuchFieldException is the field is not found
	 */
	public static Field deepFindField(Class clazz, String name) throws NoSuchFieldException {
		NoSuchFieldException exp = null;
		while (clazz != null) {
			Field field = null;
			try {
				field = clazz.getDeclaredField(name);
				return field;
			} catch (NoSuchFieldException e) {
				if (exp == null)
					exp = e;
				clazz = clazz.getSuperclass();
			}
		}

		throw exp;
	}

	/**
	 *
	 * @param clazz the type to test
	 * @return true if the type is numeric, otherwise false
	 */
	public static boolean isNumericType(Class clazz) {
		if (Number.class.isAssignableFrom(clazz)) {
			return true;
		}
		return numberTypeSet.contains(clazz);
	}

	/**
	 * Inspects a class looking for methods marked with the specified annotation and returns all
	 * those methods that are marked.
	 *
	 * @param clazz                the class to look into
	 * @param annotationsToLookFor the annotation to check for on the class's methods
	 * @return an array of the methods with the specified annotation
	 */
	public static Method[] findMethods(Class<?> clazz,
	                                   Class<? extends Annotation>... annotationsToLookFor) {
		ArrayList<Method> methodsWithAnnotation = new ArrayList<Method>();

		for (Method method : clazz.getMethods()) {
			boolean add = true;
			for (Class<? extends Annotation> annotation : annotationsToLookFor) {
				if (!method.isAnnotationPresent(annotation)) {
					add = false;
				}
			}

			if (add) {
				methodsWithAnnotation.add(method);
			}

		}

		return methodsWithAnnotation.toArray(new Method[methodsWithAnnotation.size()]);
	}

	/**
	 * Inspects a class looking for fields marked with the specified annotation and returns all
	 * those fields that are marked.
	 *
	 * @param clazz                the class to look into
	 * @param annotationsToLookFor the annotation to check for on the class's fields
	 * @return an array of the fields with the specified annotation
	 */
	public static Field[] findFields(Class<?> clazz,
	                                 Class<? extends Annotation>... annotationsToLookFor) {
		ArrayList<Field> fieldsWithAnnotation = new ArrayList<Field>();

		for (Field field : clazz.getFields()) {
			boolean add = true;
			for (Class<? extends Annotation> annotation : annotationsToLookFor) {
				if (!field.isAnnotationPresent(annotation)) {
					add = false;
				}
			}

			if (add) {
				fieldsWithAnnotation.add(field);
			}

		}

		return fieldsWithAnnotation.toArray(new Field[fieldsWithAnnotation.size()]);
	}

	/**
	 * Finds the named method with the specified parameters in the specified class. This will search
	 * in superclasses and attempt to match parameters using Class.isAssignable.
	 * <p/>
	 * If this fails to find any matches then it will try to convert all the Object primitives in
	 * params, if any, to their primitive representations and then redo the search.
	 *
	 * @param clazz      the class to find the method in
	 * @param methodName the name of the method
	 * @param params     the parameters types of the method, if null then it is assumed the method takes no
	 *                   arguments
	 * @return the found method, or null if the method is not found.
	 */
	public static Method findMethod(Class<?> clazz, String methodName, Class<?>... params) {
		Class<?> origClass = clazz;
		while (clazz != null) {
			Method[] methods = clazz.getMethods();
			Method method = ClassUtilities.doFindMethod(methods, methodName, params);
			if (method != null) {
				return method;
			}
			clazz = clazz.getSuperclass();
		}

		// if we get here then we know that no method has been found
		// so see if we any of the params are Object versions of the primitives
		// and try to find the primitives
		Class<?> newParams[] = new Class<?>[params.length];
		boolean search = false;
		for (int i = 0; i < params.length; i++) {
			Class<?> param = params[i];
			Class<?> primitive = ClassUtilities.convertObjectPrimitive(param);
			if (primitive != null) {
				newParams[i] = primitive;
				search = true;
			} else {
				newParams[i] = param;
			}
		}

		if (search) {
			clazz = origClass;
			while (clazz != null) {
				Method[] methods = clazz.getMethods();
				Method method = ClassUtilities.doFindMethod(methods, methodName, newParams);
				if (method != null) {
					return method;
				}
				clazz = clazz.getSuperclass();
			}
		}

		warn("Utilities.findMethod: warning, could not find method '" + methodName + "("
						+ Arrays.deepToString(params) + ")' in class "
						+ (origClass == null ? "null" : origClass.getName()) + ", " + "returning null");
		return null;
	}

  private static void warn(String msg) {
    if (doWarn) msgCenter.warn(msg);
  }

  private static void warn(String msg, Throwable ex) {
    if (doWarn) msgCenter.warn(msg, ex);
  }

  public static Method findFirstMethodWithArgs(Class clazz, String name, int argCount) {
		Method[] methods = clazz.getMethods();

		for (Method method : methods) {
			if (method.getName().equals(name)) {
				if (method.getGenericParameterTypes().length == argCount)
					return method;
			}
		}

		return null;
	}

	public static String getPropertyName(String name) {
		if (name == null || name.equals("")) {
			warn("propertyName cannot be null or \"\" in getPropertyName, returning null");
			return null;
		}

		char firstChar = name.charAt(0);
		StringBuilder propertyName = new StringBuilder();
		propertyName.append(Character.toUpperCase(firstChar));
		propertyName.append(name.substring(1, name.length()));

		return propertyName.toString();
	}

	public static Method findSetter(Class<?> clazz, String propertyName, Class<?>... args) {
		propertyName = getPropertyName(propertyName);
		if (propertyName == null) {
			warn("Invalid propertyName(" + propertyName + ")");
			return null;
		}
		// TODO: take in Object... as the args and do .getClass() on them
		// TODO: try other names, assuming propName is blah try:
		// setBlah(...)
		// setblah(...)
		// blah(...)
		// setBLAH(...)

		return findMethod(clazz, "set" + propertyName, args);
	}

	// TODO: check if this does auto conversion Double->double and so forth
	public static boolean setValue(Object objToSetOn, String propertyName, Object value) {
		Method method = findSetter(objToSetOn.getClass(), propertyName, value.getClass());

		if (method == null) {
			return false;
		}
		try {
			method.invoke(objToSetOn, value);
			return true;
		} catch (Exception ex) {
			warn("Couldn't set value using method " + method.getName());
			return false;
		}
	}

	public static Object getValue(Object objToSetOn, String propertyName) {
		Method method = findGetter(objToSetOn.getClass(), propertyName);

		if (method == null) {
			return null;
		}
		try {
			return method.invoke(objToSetOn);
		} catch (Exception ex) {
			warn("Couldn't get value using method " + method.getName());
			return null;
		}
	}

	public static Method findGetter(Class<?> clazz, String propertyName, Class<?>... args) {
		propertyName = getPropertyName(propertyName);
		if (propertyName == null) {
			warn("Invalid propertyName(" + propertyName + ")");
			return null;
		}
		// TODO: take in Object... as the args and do .getClass() on them
		// TODO: try other names, assuming propName is blah try:
		// getBlah()
		// getblah()
		// blah()
		// getBLAH()...

		return findMethod(clazz, "get" + propertyName, args);
	}

	private static Class<?> convertObjectPrimitive(Class clazz) {
		if (clazz.equals(Double.class)) {
			return double.class;
		}
		if (clazz.equals(Long.class)) {
			return long.class;
		}
		if (clazz.equals(Integer.class)) {
			return int.class;
		}
		if (clazz.equals(Float.class)) {
			return float.class;
		}
		if (clazz.equals(Boolean.class)) {
			return boolean.class;
		}
		if (clazz.equals(Byte.class)) {
			return byte.class;
		}
		if (clazz.equals(Short.class)) {
			return short.class;
		}

		return null;
	}

	private static Method doFindMethod(Method[] methods, String methodName, Class<?>... params) {
		for (Method method : methods) {
			if (methodName.equals(method.getName())) {
				// found a matching name, check args
				Class<?>[] methodParams = method.getParameterTypes();

				// allow for params == null to mean the method takes 0 args
				if (methodParams.length == 0 && params == null) {
					return method;
				} else if (methodParams.length != 0 && params == null) {
					// skip this case
				} else if (methodParams.length == params.length) {
					boolean match = true;
					for (int i = 0; i < params.length; i++) {
						if (!methodParams[i].isAssignableFrom(params[i])) {
							match = false;
							break;
						}
					}
					if (match) {
						return method;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Checks a method for an annotation by checking the method itself and then any interfaces the
	 * method's class implements. This allows for finding annotations set on methods in an
	 * interface, as these annotations do not carry over to the classes that implement the
	 * interface. <p/> <p/> This works by grabbing the passed in method's declaring class and
	 * searching its declared interfaces for the method with the same name and parameters. It then
	 * will repeat this process on the class's superclass until the current class has no super
	 * class. <p/> An example of what this will find annotations (deeply) on is: <code>
	 * interface InterfaceWithAnnotation {
	 *
	 * @param method          the method to check for annotations
	 * @param annotationClass
	 * @return
	 * @throws SecurityException }
	 *                           <p/>
	 *                           class ClassImplementingInterface implements InterfaceWithAnnotation {
	 *                           public void methodWithAnnotation() { }
	 *                           }
	 *                           </code>
	 *                           <p/> <p/> Note: this checking only works on public methods.
	 */
	public static <T extends Annotation> T deepAnnotationCheck(Method method,
	                                                           Class<T> annotationClass) throws SecurityException {
		T annotationInstance = method.getAnnotation(annotationClass);

		if (annotationInstance != null) {
			return annotationInstance;
		}

		String methodName = method.getName();
		Class<?>[] parameterTypes = method.getParameterTypes();

		Class<?> currentClass = method.getDeclaringClass();

		while (currentClass != null) {
			Class[] declaringClassInterfaces = currentClass.getInterfaces();

			for (Class inter : declaringClassInterfaces) {
				Method interfaceMethod = findMethod(inter, methodName, parameterTypes);

				if (interfaceMethod != null) {
					T interfaceAnnotation = interfaceMethod.getAnnotation(annotationClass);

					if (interfaceAnnotation != null) {
						return interfaceAnnotation;
					}
				}
			}

			currentClass = currentClass.getSuperclass();
		}

		return null;
	}

	/**
	 * Gets a list of classes on the specified classpath.
	 *
	 * @param classpath the class path
	 * @return a list of classes on the specified classpath.
	 * @throws java.io.IOException
	 * @throws ClassNotFoundException
	 */
	public static List<Class> getClasses(String classpath) throws IOException, ClassNotFoundException {
		StringTokenizer tok = new StringTokenizer(classpath, File.pathSeparator);
		List<Class> classes = new ArrayList<Class>();
		while (tok.hasMoreTokens()) {
			String path = tok.nextToken();
			try {
				if (path.endsWith(".jar")) {
					classes.addAll(getJarClasses(path));
				} else {
					int index = path.length() + 1;
					if (path.endsWith("\\") || path.endsWith("/")) index = path.length();
					classes.addAll(getClasses(index, new File(path)));
				}
			} catch (NoClassDefFoundError e) {
				// TODO: fix this [SIM-112]
				e.printStackTrace();
			}
		}
		return classes;
	}

	private static List<? extends Class> getClasses(int index, File path) throws IOException {
		List<Class> classes = new ArrayList<Class>();
		if (path.isDirectory()) {
			for (File file : path.listFiles()) {
				classes.addAll(getClasses(index, file));
			}
		} else if (path.getName().endsWith(".class")) {
			String fname = path.getPath();
			fname = fname.substring(index, fname.length());
			fname = StringUtils.replace(fname, File.separator, ".");
			try {
				classes.add(Class.forName(fname.substring(0, fname.length() - 6), false, ClassUtilities.class.getClassLoader()));
			} catch (ClassNotFoundException e) {
				warn("Error creating Class from *.class file", e);
			} catch (NoClassDefFoundError e) {
				warn("Error creating Class from *.class file", e);
			}
		}

		return classes;
	}

	private static List<? extends Class> getJarClasses(String path) throws IOException, ClassNotFoundException {
		JarFile jar = new JarFile(path);
		List<Class> classes = new ArrayList<Class>();
		for (Enumeration entries = jar.entries(); entries.hasMoreElements();) {
			JarEntry entry = (JarEntry) entries.nextElement();
			String name = entry.getName();
			if (name.endsWith(".class")) {
				name = StringUtils.replace(name, "/", ".");
				try {
					Class clazz = Class.forName(name.substring(0, name.length() - 6), false, ClassUtilities.class.getClassLoader());
					classes.add(clazz);
				} catch (ClassNotFoundException e) {
					warn("Error creating Class from *.class file", e);
				} catch (NoClassDefFoundError e) {
					warn("Error creating Class from *.class file", e);
				}
			}

		}
		return classes;
	}

}
