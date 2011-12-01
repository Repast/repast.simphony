package repast.simphony.ui.probe;

import junit.framework.TestCase;
import org.jscience.physics.amount.Amount;
import repast.simphony.parameter.BeanParameters;
import repast.simphony.parameter.Parameter;
import repast.simphony.parameter.Parameters;
import repast.simphony.util.collections.Pair;

import javax.measure.unit.SI;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ProbeableBeanTests extends TestCase {

  static class SimpleBean {

		int intVal = 1;
		double doubleVal = 3.14;
		long longVal = 30;
		float floatVal = .12f;
		boolean booleanVal = false;
		String stringVal = "foo";
    MutableNumber mutableNumber = new MutableNumber(4.0);

    public double getDoubleVal() {
			return doubleVal;
		}

		public void setDoubleVal(double doubleVal) {
			this.doubleVal = doubleVal;
		}

    public int getIntVal() {
			return intVal;
		}

		public void setIntVal(int intVal) {
			this.intVal = intVal;
		}

		public String getStringVal() {
			return stringVal;
		}

		public void setStringVal(String stringVal) {
			this.stringVal = stringVal;
		}

		public float getFloatVal() {
			return floatVal;
		}

		public void setFloatVal(float floatVal) {
			this.floatVal = floatVal;
		}

		public long getLongVal() {
			return longVal;
		}

		public void setLongVal(long longVal) {
			this.longVal = longVal;
		}

		public boolean isBooleanVal() {
			return booleanVal;
		}

		public void setBooleanVal(boolean booleanVal) {
			this.booleanVal = booleanVal;
		}
  }

  public static class AnotherBean {

    int intVal = 1;
    MutableNumber mutableNumber = new MutableNumber(4.0);

    public int getIntVal() {
			return intVal;
		}

		public void setIntVal(int intVal) {
			this.intVal = intVal;
		}

    @Parameter(usageName = "mutableNumber", displayName = "Mutable Number",
            converter="repast.simphony.ui.probe.MutableNumberConverter")
    public MutableNumber getMutableNumber() {
      return mutableNumber;
    }

    public void setMutableNumber(MutableNumber mutableNumber) {
      this.mutableNumber = mutableNumber;
    }
  }

  private Parameters params;
	private SimpleBean simpleBean;

  class PListener implements PropertyChangeListener {
    Map<String, Pair> nameVals = new HashMap<String, Pair>();

    public void propertyChange(PropertyChangeEvent evt) {
      nameVals.put(evt.getPropertyName(), new Pair(evt.getOldValue(), evt.getNewValue()));
    }
  }

  public void setUp() {
		simpleBean = new SimpleBean();
		params = new BeanParameters(simpleBean);
	}

  public void testProbeBean() throws Exception {
   AnotherBean anotherBean = new AnotherBean();
    ProbeableBeanInfo info = ProbeableBeanCreator.getInstance().createProbeableBean(anotherBean);
    assertEquals("Mutable Number", info.getDisplayName("mutableNumber"));

    MutableNumberConverter conv = new MutableNumberConverter();

    ProbeableBean bean = info.getBean();
    PListener listener = new PListener();
    bean.addPropertyChangeListener(listener);

    Class clazz = bean.getClass();
    Method m = clazz.getMethod("getMutableNumber");
		assertEquals(conv.toString(anotherBean.getMutableNumber()), m.invoke(bean));

    Method set = clazz.getMethod("setMutableNumber", String.class);
    set.invoke(bean, "23.234");
    assertEquals(23.234, anotherBean.getMutableNumber().getVal());
    assertEquals("23.234", m.invoke(bean));
    assertEquals(1, listener.nameVals.size());
    assertTrue(listener.nameVals.keySet().contains("mutableNumber"));
    assertEquals("4.0", listener.nameVals.get("mutableNumber").getFirst());
    assertEquals("23.234", listener.nameVals.get("mutableNumber").getSecond());
  }

  public void testGet() {
		try {
			ProbeableBean bean = new ParameterProbeBeanCreator().createProbeableBean(params).getBean();
			Class clazz = bean.getClass();

			Method m = clazz.getMethod("getIntVal");
			assertEquals(simpleBean.getIntVal(), m.invoke(bean));

			m = clazz.getMethod("getDoubleVal");
			assertEquals(simpleBean.getDoubleVal(), m.invoke(bean));

			m = clazz.getMethod("getLongVal");
			assertEquals(simpleBean.getLongVal(), m.invoke(bean));

			m = clazz.getMethod("getFloatVal");
			assertEquals(simpleBean.getFloatVal(), m.invoke(bean));

			m = clazz.getMethod("isBooleanVal");
			assertEquals(simpleBean.isBooleanVal(), m.invoke(bean));

			m = clazz.getMethod("getStringVal");
			assertEquals(simpleBean.getStringVal(), m.invoke(bean));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void testSet() {
		try {
			ProbeableBean bean = new ParameterProbeBeanCreator().createProbeableBean(params).getBean();
			Class clazz = bean.getClass();
			// System.out.println("bean.getClass().getMethods() = " + Arrays.asList(bean.getClass().getMethods()));

			Method m = clazz.getMethod("setIntVal", int.class);
			m.invoke(bean, new Integer(12));

			m = clazz.getMethod("setDoubleVal", double.class);
			m.invoke(bean, new Double(1.1223));

			m = clazz.getMethod("setLongVal", long.class);
			m.invoke(bean, new Long(2343443));

			m = clazz.getMethod("setFloatVal", float.class);
			m.invoke(bean, new Float(3.2342f));

			m = clazz.getMethod("setBooleanVal", boolean.class);
			m.invoke(bean, Boolean.TRUE);

			m = clazz.getMethod("setStringVal", String.class);
			m.invoke(bean, "Cormac");

			m = clazz.getMethod("getIntVal");
			assertEquals(simpleBean.getIntVal(), m.invoke(bean));
			assertEquals(12, m.invoke(bean));

			m = clazz.getMethod("getDoubleVal");
			assertEquals(simpleBean.getDoubleVal(), m.invoke(bean));
			assertEquals(1.1223, m.invoke(bean));

			m = clazz.getMethod("getLongVal");
			assertEquals(simpleBean.getLongVal(), m.invoke(bean));
			assertEquals(2343443L, m.invoke(bean));

			m = clazz.getMethod("getFloatVal");
			assertEquals(simpleBean.getFloatVal(), m.invoke(bean));
			assertEquals(3.2342f, m.invoke(bean));

			m = clazz.getMethod("isBooleanVal");
			assertEquals(simpleBean.isBooleanVal(), m.invoke(bean));
			assertEquals(true, m.invoke(bean));

			m = clazz.getMethod("getStringVal");
			assertEquals(simpleBean.getStringVal(), m.invoke(bean));
			assertEquals("Cormac", m.invoke(bean));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

  public void testAmountConverter() {
    Amount amount = Amount.valueOf(1, SI.METER);
    AmountConverter conv = new AmountConverter();
    //System.out.println(conv.toString(amount));
    assertEquals("1.0 m", conv.toString(amount));
    amount = conv.fromString("3 m");
    assertEquals(3.0, amount.doubleValue(SI.METER));
    //System.out.println(conv.toString(amount));
  }
}
