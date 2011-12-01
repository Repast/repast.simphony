package repast.simphony.random;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;
import junit.framework.TestCase;
import repast.simphony.random.RandomHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class RandomTest extends TestCase {

	public void setUp() {
		RandomHelper.init();
	}

	// creates all the distributions via reflection and then
	// calls getX to make sure they are the same
	public void testAllCreation() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class clazz = RandomHelper.class;
		Method[] allMethods = clazz.getMethods();
		List<Method> methods = new ArrayList<Method>();
		for (Method method : allMethods) {
			if (method.getName().startsWith("create")) {
				methods.add(method);
			}
		}

		// want to create it and then get it and compare with that
		// returned by getting from RandomHelper by name.
		for (Method method : methods) {
			Class types[] = method.getParameterTypes();
			Object[] obj = new Object[types.length];
			int i = 0;
			for (Class type : types) {
				if (type.equals(double.class)) {
					if (method.getName().equals("createExponentialPower")) obj[i++] = new Double(1);
					else if (method.getName().equals("createChiSquare")) obj[i++] = new Double(1);
					else obj[i++] = new Double(.5);
				} else if (type.equals(double[].class)) {
					obj[i++] = new double[]{2.2, 3.4};
				} else {
					obj[i++] = new Integer(1);
				}
			}
			AbstractDistribution dist = (AbstractDistribution) method.invoke(null, obj);
			assertTrue(dist != null);

			// find the corresponding getMethod
			String name = method.getName().substring(6, method.getName().length());
			method = clazz.getMethod("get" + name);
			assertEquals(dist, method.invoke(null));
		}
	}

	public void testNonDefaultDistribution() {
		try {
			Normal firstNormal = RandomHelper.createNormal(2, 1);
			Normal normal = RandomHelper.getNormal();
			assertEquals(firstNormal, normal);

			RandomEngine engine = RandomHelper.registerGenerator("my gen", 0);
			Normal aNormal = new Normal(0, 1, engine);
			RandomHelper.registerDistribution("Another Normal", aNormal);
			assertEquals(aNormal, RandomHelper.getDistribution("Another Normal"));

		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	public void testGenerator() {
		RandomEngine engine = RandomHelper.registerGenerator("my gen", 0);
		assertEquals(engine, RandomHelper.getGenerator("my gen"));
		assertEquals(0, RandomHelper.getSeed("my gen"));
	}

	public void testSetSeed() {
		RandomEngine engine = RandomHelper.getGenerator();
		Uniform uniform  = RandomHelper.getUniform();
		assertTrue(uniform != null);
		int seed = RandomHelper.getSeed();

		RandomHelper.setSeed(3);
		assertNotSame(engine, RandomHelper.getGenerator());
		assertNotSame(uniform, RandomHelper.getUniform());
		assertNotSame(seed, RandomHelper.getSeed());
	}
}
