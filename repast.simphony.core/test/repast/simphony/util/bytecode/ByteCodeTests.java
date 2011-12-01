package repast.simphony.util.bytecode;

import junit.framework.TestCase;

import java.lang.reflect.Method;

/**
 * @author Nick Collier
 *         Date: Aug 5, 2008 2:02:00 PM
 */
public class ByteCodeTests extends TestCase {

  public class TestObject {
    public int getInt() {
      return 3;
    }

    public double getDouble() {
      return 3.14;
    }

    public boolean getBoolean() {
      return false;
    }

    public String getString() {
      return "Hello";
    }
  }


  public void testDataSource() throws Exception {
    ByteCodeUtilities bcUtils = ByteCodeUtilities.getInstance();

    TestObject obj = new TestObject();
    Method method = obj.getClass().getMethod("getInt");

    DataSource source = bcUtils.createMethodCall(method);
    Object data = source.getData(obj);
    assertTrue(data instanceof Integer);
    assertEquals(new Integer(3), data);

    method = obj.getClass().getMethod("getDouble");
    source = bcUtils.createMethodCall(method);
    data = source.getData(obj);
    assertTrue(data instanceof Double);
    assertEquals(new Double(3.14), data);

    method = obj.getClass().getMethod("getBoolean");
    source = bcUtils.createMethodCall(method);
    data = source.getData(obj);
    assertTrue(data instanceof Boolean);
    assertEquals(Boolean.FALSE, data);

    method = obj.getClass().getMethod("getString");
    source = bcUtils.createMethodCall(method);
    data = source.getData(obj);
    assertTrue(data instanceof String);
    assertEquals("Hello", data);
  }


}
