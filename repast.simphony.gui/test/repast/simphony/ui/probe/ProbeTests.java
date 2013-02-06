/**
 * 
 */
package repast.simphony.ui.probe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import org.junit.Test;

import repast.simphony.parameter.StringConverterFactory;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;

/**
 * @author Nick Collier
 */
public class ProbeTests {

  @SuppressWarnings("serial")
  public static class MyModel<B> extends PresentationModel<B> {

    public MyModel(B bean) {
      super(bean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.jgoodies.binding.PresentationModel#createBeanAdapter(com.jgoodies
     * .binding.value.ValueModel)
     */
    @Override
    protected BeanAdapter<B> createBeanAdapter(ValueModel beanChannel) {
      return new BeanAdapter<B>(beanChannel, false);
    }

    public void update() {
      fireMultiplePropertiesChanged();
    }
  }

  @Test
  public void testPresentationModel() {
    // this more to help figure out how it works than
    // testing to make sure it works.
    SampleObject obj = new SampleObject();
    MyModel<SampleObject> model = new MyModel<SampleObject>(obj);
    ValueModel bModel = model.getModel("intVal");
    assertEquals(new Integer(obj.getIntVal()), bModel.getValue());

    bModel.setValue(5);
    assertEquals(5, obj.getIntVal());

    obj.setIntVal(10);
    // not equals because we don't listen for changes from the
    // obj side, we have to force them.
    assertTrue(new Integer(10).equals(bModel.getValue()));
    // force the change with an update call
    model.update();
    assertEquals(new Integer(obj.getIntVal()), bModel.getValue());

    /*
     * JTextField fld = new JTextField(); NumberFormat nf =
     * NumberFormat.getIntegerInstance(); Bindings.bind(fld,
     * ConverterFactory.createStringConverter(bModel, nf)); assertEquals("10",
     * fld.getText());
     * 
     * fld.setText("12"); assertEquals(12, obj.getIntVal());
     */
  }

  @Test
  public void testProbeIntrospector() throws IntrospectionException, IllegalArgumentException, IllegalAccessException {
    ProbeIntrospector introspector = ProbeIntrospector.getInstance();
    ProbeInfo pi = introspector.getProbeInfo(SampleObject.class);

    PropertyDescriptor pd = pi.getIDProperty();
    assertNotNull(pd);
    assertEquals("id", pd.getReadMethod().getName());
    int count = 0;
    for (MethodPropertyDescriptor ppd : pi.methodPropertyDescriptors()) {
      if (ppd.getName().equals("intVal")) {
        assertEquals("Integer Value", ppd.getDisplayName());
        assertEquals(StringConverterFactory.IntConverter.class, ppd.getStringConverter().getClass());
        assertEquals("getIntVal", ppd.getReadMethod().getName());
        assertEquals("setIntVal", ppd.getWriteMethod().getName());
        ++count;
      } else {
        assertEquals("code", ppd.getName());
        assertEquals("Code", ppd.getDisplayName());
        assertTrue(ppd.getStringConverter() == null);
        assertEquals("getCode", ppd.getReadMethod().getName());
        assertEquals("setCode", ppd.getWriteMethod().getName());
        ++count;
      }
    }
    assertEquals(2, count);

    // test the cache
    assertEquals(pi, introspector.getProbeInfo(SampleObject.class));

    pi = introspector.getProbeInfo(SampleObject2.class);
    pd = pi.getIDProperty();
    assertTrue(pd == null);
    count = 0;
    for (MethodPropertyDescriptor ppd : pi.methodPropertyDescriptors()) {
      if (ppd.getName().equals("intVal")) {
        assertEquals("intVal", ppd.getDisplayName());
        assertTrue(ppd.getStringConverter() == null);
        assertEquals("getIntVal", ppd.getReadMethod().getName());
        assertEquals("setIntVal", ppd.getWriteMethod().getName());
        ++count;
      } else {
        assertEquals("code", ppd.getName());
        assertEquals("code", ppd.getDisplayName());
        assertTrue(ppd.getStringConverter() == null);
        assertEquals("getCode", ppd.getReadMethod().getName());
        assertEquals("setCode", ppd.getWriteMethod().getName());
        ++count;
      }
    }

    count = 0;
    for (FieldPropertyDescriptor fd : pi.fieldPropertyDescriptor()) {
      ++count;
      assertEquals("foo", fd.getName());
      assertEquals("foo", fd.getField().getName());
      assertEquals("My Foo", fd.getDisplayName());
      assertTrue(fd.getStringConverter() == null);

      assertEquals(3, fd.getField().getLong(new SampleObject2()));
    }
    assertEquals(1, count);
  }
}
