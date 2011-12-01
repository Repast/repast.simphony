package repast.simphony.ui.probe;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import com.jgoodies.binding.beans.Model;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BindableBeanCreator {


	static class Property {
		Method read;
		Method write;
		String propName;

		public Property(String propName, Method read, Method write) {
			this.propName = propName;
			this.read = read;
			this.write = write;
		}
	}

	private static BindableBeanCreator instance = new BindableBeanCreator();

	public static BindableBeanCreator getInstance() {
		return instance;
	}

	private Map<String, Class> beanPool = new HashMap<String, Class>();

	private BindableBeanCreator() {}

	public Model createBindableBean(Object target) throws IntrospectionException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException,
			InstantiationException, CannotCompileException, NotFoundException,
			ClassNotFoundException {
		return createBindableBean(target, "BindableBeanZZ" + target.getClass().getSimpleName());
	}
	
	public Model createBindableBean(Object target, String className) throws IntrospectionException, NoSuchMethodException,
					IllegalAccessException, InvocationTargetException, InstantiationException, CannotCompileException, NotFoundException, ClassNotFoundException {
		String targetClassName = target.getClass().getName();
		Class clazz = beanPool.get(targetClassName);
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (clazz == null) {
			List<Property> props = getProperties(target);
			Class modelClass = Class.forName("com.jgoodies.binding.beans.Model");

			ClassPool pool = new ClassPool();
			pool.appendSystemPath();
			pool.insertClassPath(new LoaderClassPath(modelClass.getClassLoader()));
			Thread.currentThread().setContextClassLoader(modelClass.getClassLoader());
			CtClass model = pool.get(modelClass.getName());

			CtClass ct = pool.makeClass(className, model);
			ct.addField(CtField.make("private " + targetClassName + " bean;", ct));
			ct.addConstructor(CtNewConstructor.make(createConstructorSource(className, targetClassName), ct));
			for (Property prop : props) {
				processProperty(prop, ct);
			}
			clazz = ct.toClass();
			beanPool.put(targetClassName, clazz);
		}
		Thread.currentThread().setContextClassLoader(clazz.getClassLoader());
		Model m = (Model)clazz.getConstructor(target.getClass()).newInstance(target);
		Thread.currentThread().setContextClassLoader(loader);
		return m;

	}

	private void processProperty(Property prop, CtClass ct) throws CannotCompileException {
		// for getters : public retval methodName()
		if (prop.read != null)
			ct.addMethod(CtNewMethod.make(createGetMethodSource(prop.read), ct));
		if (prop.write != null) {
			ct.addMethod(CtNewMethod.make(createSetMethodSource(prop), ct));
		}
	}

	private String createSetMethodSource(Property prop) {
		Method write = prop.write;
		Method read = prop.read;
		StringBuffer buf = new StringBuffer("public void ");
		buf.append(write.getName());
		buf.append("(");
		String paramTypeName = write.getParameterTypes()[0].getName();
		buf.append(paramTypeName);
		buf.append(" val) { ");
		if (read != null) {
			buf.append(paramTypeName);
			buf.append(" __oldValue = bean.");
			buf.append(read.getName());
			buf.append("();\n");
		}
		buf.append("bean.");
		buf.append(write.getName());
		buf.append("(val);\n");
		buf.append(getFireLine(prop.propName, paramTypeName, read == null));
		return buf.toString();
	}

	private String getFireLine(String propName, String paramTypeName, boolean readIsNull) {
		StringBuffer buf = new StringBuffer();
		buf.append("firePropertyChange(\"");
		buf.append(propName);
		buf.append("\", ");

		if (paramTypeName.equals("long")) {
			if (readIsNull) buf.append("null,");
			else buf.append("new Long(__oldValue,");
			buf.append("new Long(val));}");

		} else if (paramTypeName.equals("float")) {
			if (readIsNull) buf.append("null,");
			else buf.append("new Float(__oldValue),");
			buf.append("new Float(val));}");
		}	else if (paramTypeName.equals("double")) {
			if (readIsNull) buf.append("null,");
			else buf.append("new Double(__oldValue),");
			buf.append("new Double(val));}");
		} else {
			if (readIsNull) buf.append("null, ");
			else buf.append("__oldValue, ");
			buf.append("val);}");
		}

		return buf.toString();
	}

	private String createGetMethodSource(Method method) {
		StringBuffer buf = new StringBuffer("public ");
		buf.append(method.getReturnType().getName());
		buf.append(" ");
		buf.append(method.getName());
		buf.append("() { return bean.");
		buf.append(method.getName());
		buf.append("();}");
		return buf.toString();
	}

	private String createConstructorSource(String className, String targetClassName) {
		StringBuffer buf = new StringBuffer("public ");
		buf.append(className);
		buf.append("(");
		buf.append(targetClassName);
		buf.append(" target) { bean = target;}");
		return buf.toString();
	}

	private List<Property> getProperties(Object target) throws IntrospectionException {
		BeanInfo info = Introspector.getBeanInfo(target.getClass(), Object.class);
		PropertyDescriptor[] pds = info.getPropertyDescriptors();
		List<Property> props = new ArrayList<Property>();
		for (PropertyDescriptor pd : pds) {
			props.add(new Property(pd.getName(), pd.getReadMethod(), pd.getWriteMethod()));
		}
		return props;
	}
}
