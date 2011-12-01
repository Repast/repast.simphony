package repast.simphony.batch;

import javassist.Loader;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BatchClassLoader extends Loader {

	public void initBatch(String classname, String[] args) throws Throwable {
		Class c = loadClass(classname);
		try {
			Object obj = c.newInstance();
			c.getDeclaredMethod("createScenario").invoke(obj);
		} catch (java.lang.reflect.InvocationTargetException e) {
			throw e.getTargetException();
		}
	}
}
