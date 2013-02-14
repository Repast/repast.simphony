/**
 * 
 */
package repast.simphony.ui.probe;

import static org.junit.Assert.*;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.junit.Test;

import repast.simphony.parameter.StringConverterFactory;
import repast.simphony.ui.probe.FieldPropertyDescriptor;
import repast.simphony.ui.probe.MethodPropertyDescriptor;
import repast.simphony.ui.probe.ProbeInfo;
import repast.simphony.ui.probe.ProbeIntrospector;
import repast.simphony.ui.probe.SampleObject;
import repast.simphony.ui.probe.SampleObject2;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;

/**
 * @author Nick Collier
 */
public class ProbeTests {
  
  private static class MyBeanAdapter extends BeanAdapter<Object> {

    private List<SimplePropertyAdapter> adapters = new ArrayList<SimplePropertyAdapter>();

    public MyBeanAdapter(ValueModel beanChannel) {
      super(beanChannel, false);
    }

    @Override
    protected SimplePropertyAdapter createPropertyAdapter(String propertyName, String getterName,
        String setterName) {
      SimplePropertyAdapter adapter = new SimplePropertyAdapter(propertyName, getterName, setterName);
      adapters.add(adapter);
      return adapter;
    }
    
    public void fireUpdate() {
      for (SimplePropertyAdapter spa : adapters) {
        spa.fireChanged();
      }
    }
    
    public class SimplePropertyAdapter extends BeanAdapter.SimplePropertyAdapter {

      protected SimplePropertyAdapter(String propertyName, String getterName, String setterName) {
        super(propertyName, getterName, setterName);
      }
      
      void fireChanged() {
        fireChange(getBean());
      }
     
    }

  }

  @SuppressWarnings("serial")
  public static class MyModel extends PresentationModel<Object> {

    private MyBeanAdapter adapter;

    public MyModel(Object bean) {
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
    protected BeanAdapter<Object> createBeanAdapter(ValueModel beanChannel) {
      adapter = new MyBeanAdapter(beanChannel);
      return adapter;
    }

    public void update() {
      adapter.fireUpdate();
    }
  }

  @Test
  public void testPresentationModel() {
    // this more to help figure out how it works than
    // testing to make sure it works.
    SampleObject obj = new SampleObject();
    MyModel model = new MyModel(obj);
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

    ValueModel vModel = model.getModel("code");
    JLabel label = BasicComponentFactory.createLabel(vModel);
    System.out.println(label.getText());
    obj.setCode("A");
    model.update();
    System.out.println(label.getText());
  }

  @Test
  public void testProbeIntrospector() throws IntrospectionException, IllegalArgumentException,
      IllegalAccessException {
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
