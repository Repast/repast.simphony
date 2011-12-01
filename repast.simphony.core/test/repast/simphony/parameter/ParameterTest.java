package repast.simphony.parameter;

import junit.framework.TestCase;
import repast.simphony.parameter.*;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ParameterTest extends TestCase {

	static class SimpleBean {

		int intVal;
		double doubleVal;
		String stringVal = "foo";

		@Parameter(displayName = "Double Value", usageName = "doubleVal", defaultValue = "20.032")
		public double getDoubleVal() {
			return doubleVal;
		}

		public void setDoubleVal(double doubleVal) {
			this.doubleVal = doubleVal;
		}

		@Parameter(displayName = "Int Value", usageName = "intVal", defaultValue = "3")
		public int getIntVal() {
			return intVal;
		}

		public void setIntVal(int intVal) {
			this.intVal = intVal;
		}

		@Parameter(displayName = "String Value", usageName = "stringVal", defaultValue = "foo")
		public String getStringVal() {
			return stringVal;
		}

		public void setStringVal(String stringVal) {
			this.stringVal = stringVal;
		}
	}

	static class PropListener implements PropertyChangeListener {

		Object oldVal;
		Object newVal;

		public void propertyChange(PropertyChangeEvent evt) {
			oldVal = evt.getOldValue();
			newVal = evt.getNewValue();
		}
	}

	private BeanParameters parameters;
	private SimpleBean bean;

	public void setUp() {
		bean = new SimpleBean();
		parameters = new BeanParameters(bean);
	}

	public void testGet() {
		int val = 3;
		double dVal = 3.14;
		String sVal = "hello";

		bean.setIntVal(val);
		bean.setDoubleVal(dVal);
		bean.setStringVal(sVal);

		assertEquals(val, parameters.getValue("intVal"));
		assertEquals(dVal, parameters.getValue("doubleVal"));
		assertEquals(sVal, parameters.getValue("stringVal"));
		try {
			parameters.getValue("foo");
			// should never get here, ex should be thrown
			assertTrue(false);
		} catch (IllegalParameterException ex) {}
	}

	public void testSet() {
		int val = 3;
		double dVal = 3.14;
		String sVal = "hello";

		parameters.setValue("intVal", val);
		parameters.setValue("doubleVal", dVal);
		parameters.setValue("stringVal", sVal);

		assertEquals(val, parameters.getValue("intVal"));
		assertEquals(dVal, parameters.getValue("doubleVal"));
		assertEquals(sVal, parameters.getValue("stringVal"));
	}

	public void testPropertyListener() {
		PropListener pl = new PropListener();
		parameters.addPropertyChangeListener(pl);

		String sVal = "hello";
		parameters.setValue("stringVal", sVal);
		assertEquals("foo", pl.oldVal);
		assertEquals(sVal, pl.newVal);
	}

	public void testSchema() {
		Schema schema = parameters.getSchema();
		assertEquals(String.class, schema.getDetails("stringVal").getType());
		assertEquals(int.class, schema.getDetails("intVal").getType());
		assertEquals(double.class, schema.getDetails("doubleVal").getType());

		Set<String> set = new HashSet<String>();
		set.add("stringVal");
		set.add("intVal");
		set.add("doubleVal");

		int count = 0;
		for (String item : schema.parameterNames()) {
			count++;
			set.remove(item);
		}

		assertTrue(count == 3);
		assertTrue(set.size() == 0);
	}

	public void testBoundParameters() throws IntrospectionException {
		ParametersCreator creator = new ParametersCreator();
		BoundParameters parameters = creator.createBoundParameters(SimpleBean.class);
		Schema schema = parameters.getSchema();

		assertEquals(String.class, schema.getDetails("stringVal").getType());
		assertEquals(int.class, schema.getDetails("intVal").getType());
		assertEquals(double.class, schema.getDetails("doubleVal").getType());

		Set<String> set = new HashSet<String>();
		set.add("stringVal");
		set.add("intVal");
		set.add("doubleVal");

		int count = 0;
		for (String item : schema.parameterNames()) {
			count++;
			set.remove(item);
		}

		assertTrue(count == 3);
		assertTrue(set.size() == 0);

		assertEquals(3, parameters.getValue("intVal"));
		assertEquals(20.032, parameters.getValue("doubleVal"));
		assertEquals("foo", parameters.getValue("stringVal"));

		SimpleBean bean = new SimpleBean();
		parameters.setBean(bean);

		assertEquals(3, bean.getIntVal());
		assertEquals(20.032, bean.getDoubleVal());
		assertEquals("foo", bean.getStringVal());

		parameters.setValue("intVal", 10);
		parameters.setValue("doubleVal", .32);
		parameters.setValue("stringVal", "bar");

		assertEquals(10, parameters.getValue("intVal"));
		assertEquals(.32, parameters.getValue("doubleVal"));
		assertEquals("bar", parameters.getValue("stringVal"));

		assertEquals(10, bean.getIntVal());
		assertEquals(.32, bean.getDoubleVal());
		assertEquals("bar", bean.getStringVal());
	}
}
