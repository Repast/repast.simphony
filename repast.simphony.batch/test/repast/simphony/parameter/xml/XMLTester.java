package repast.simphony.parameter.xml;

import junit.framework.TestCase;
import org.xml.sax.helpers.AttributesImpl;
import repast.simphony.parameter.*;
import repast.simphony.util.collections.Pair;

import java.util.*;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class XMLTester extends TestCase {

  public void testParse() {
    try {
      ParameterSweepParser parser = new ParameterSweepParser(this.getClass().getResource(
          "params.xml"));
      Pair<Parameters, ParameterTreeSweeper> out = parser.parse();
      ParameterTreeSweeper sweeper = out.getSecond();

      assertEquals(2, sweeper.getRunCount());
      ParameterSetter root = sweeper.getRootParameterSetter();
      Collection<ParameterSetter> children = sweeper.getChildren(root);
      assertEquals(7, children.size());

      Set<String> names = new HashSet<String>();
      LongSteppedSetter iSetter = null;
      for (ParameterSetter setter : children) {
        if (setter instanceof LongSteppedSetter) {
          iSetter = (LongSteppedSetter) setter;
          names.add(((LongSteppedSetter) setter).getParameterName());
        }
        if (setter instanceof ConstantSetter)
          names.add(((ConstantSetter) setter).getParameterName());
      }
      assertTrue(names.contains("num_1"));
      assertTrue(names.contains("const_1"));
      assertTrue(names.contains("const_2"));
      assertTrue(names.contains("const_3"));
      assertTrue(names.contains("const_4"));
      assertTrue(names.contains("const_5"));
      assertTrue(names.contains("const_6"));

      assertEquals("num_1", iSetter.getParameterName());

      children = sweeper.getChildren(iSetter);
      assertEquals(3, children.size());
      Set<Class> types = new HashSet<Class>();
      types.add(DoubleSteppedSetter.class);
      types.add(FloatSteppedSetter.class);
      types.add(IntSteppedSetter.class);
      Map<String, ParameterSetter> map = new HashMap<String, ParameterSetter>();
      for (ParameterSetter ps : children) {
        assertTrue(types.remove(ps.getClass()));
        if (ps instanceof DoubleSteppedSetter)
          map.put(((DoubleSteppedSetter) ps).getParameterName(), ps);
        if (ps instanceof FloatSteppedSetter)
          map.put(((FloatSteppedSetter) ps).getParameterName(), ps);
        if (ps instanceof IntSteppedSetter)
          map.put(((IntSteppedSetter) ps).getParameterName(), ps);
      }
      assertEquals(0, types.size());
      assertTrue(map.containsKey("num_2"));
      assertTrue(map.containsKey("num_3"));
      assertTrue(map.containsKey("num_4"));

      children = sweeper.getChildren(map.get("num_2"));
      assertEquals(1, children.size());
      ListParameterSetter<String> lSetter = (ListParameterSetter<String>) children.iterator()
          .next();
      assertEquals("list_val", lSetter.getParameterName());
      children = sweeper.getChildren(lSetter);
      assertEquals(0, children.size());

      children = sweeper.getChildren(map.get("num_3"));
      assertEquals(0, children.size());
      children = sweeper.getChildren(map.get("num_4"));
      assertEquals(0, children.size());

      Parameters params = out.getFirst();
      names = new HashSet<String>();
      names.add("num_1");
      names.add("num_2");
      names.add("num_3");
      names.add("num_4");
      names.add("list_val");
      names.add("const_1");
      names.add("const_2");
      names.add("const_3");
      names.add("const_4");
      names.add("const_5");
      names.add("const_6");
      for (String name : params.getSchema().parameterNames()) {
        assertTrue(names.remove(name));
      }

      assertEquals(new Long(1), params.getValue("num_1"));
      assertEquals(new Float(.3), params.getValue("num_2"));
      assertEquals(new Double(.9), params.getValue("num_3"));
      assertEquals(new Integer(2), params.getValue("num_4"));
      assertEquals(new Float(.3f), params.getValue("const_1"));
      assertEquals(new Double(.2), params.getValue("const_2"));
      assertEquals(new Integer(11), params.getValue("const_3"));
      assertEquals(new Long(11), params.getValue("const_4"));
      assertEquals("hello cormac", params.getValue("const_5"));
      assertEquals(Boolean.FALSE, params.getValue("const_6"));

    } catch (Exception ex) {
      ex.printStackTrace();
      assertTrue(false);
    }
  }

  public void testSimpleStringListSetterCreator() {
    try {
      // <parameter name="list_value" type="list" value_type="String"
      // values="foo bar baz" />
      AttributesImpl attributes = new AttributesImpl();
      attributes.addAttribute("name", "name", "name", "", "list_value");
      attributes.addAttribute("value_type", "value_type", "value_type", "", "string");
      attributes.addAttribute("values", "values", "values", "", "foo bar baz");

      ListSetterCreator creator = new ListSetterCreator();
      creator.init(attributes);
      ListParameterSetter<String> setter = (ListParameterSetter<String>) creator.createSetter();
      ParametersCreator pc = new ParametersCreator();
      creator.addParameter(pc);
      Parameters params = pc.createParameters();
      setter.reset(params);
      assertEquals("foo", params.getValue("list_value"));
      setter.next(params);
      assertEquals("bar", params.getValue("list_value"));
      setter.next(params);
      assertEquals("baz", params.getValue("list_value"));
      assertTrue(setter.atEnd());
    } catch (ParameterFormatException e) {
      e.printStackTrace();
    }
  }

  public void testIntListSetterCreator() {
    try {
      // <parameter name="list_value" type="list" value_type="String"
      // values="foo bar baz" />
      AttributesImpl attributes = new AttributesImpl();
      attributes.addAttribute("name", "name", "name", "", "list_value");
      attributes.addAttribute("value_type", "value_type", "value_type", "", "int");
      attributes.addAttribute("values", "values", "values", "", "32 12 -4");

      ListSetterCreator creator = new ListSetterCreator();
      creator.init(attributes);
      ListParameterSetter<String> setter = (ListParameterSetter<String>) creator.createSetter();
      ParametersCreator pc = new ParametersCreator();
      creator.addParameter(pc);
      Parameters params = pc.createParameters();
      setter.reset(params);
      assertEquals(32, params.getValue("list_value"));
      setter.next(params);
      assertEquals(12, params.getValue("list_value"));
      setter.next(params);
      assertEquals(-4, params.getValue("list_value"));
      assertTrue(setter.atEnd());
    } catch (ParameterFormatException e) {
      e.printStackTrace();
    }
  }

  public void testLongListSetterCreator() {
    try {
      // <parameter name="list_value" type="list" value_type="String"
      // values="foo bar baz" />
      AttributesImpl attributes = new AttributesImpl();
      attributes.addAttribute("name", "name", "name", "", "list_value");
      attributes.addAttribute("value_type", "value_type", "value_type", "", "long");
      attributes.addAttribute("values", "values", "values", "", "32 12 -4");

      ListSetterCreator creator = new ListSetterCreator();
      creator.init(attributes);
      ListParameterSetter<String> setter = (ListParameterSetter<String>) creator.createSetter();
      ParametersCreator pc = new ParametersCreator();
      creator.addParameter(pc);
      Parameters params = pc.createParameters();
      setter.reset(params);
      assertEquals(32L, params.getValue("list_value"));
      setter.next(params);
      assertEquals(12L, params.getValue("list_value"));
      setter.next(params);
      assertEquals(-4L, params.getValue("list_value"));
      assertTrue(setter.atEnd());
    } catch (ParameterFormatException e) {
      e.printStackTrace();
    }
  }

  public void testBooleanListSetterCreator() {
    try {
      // <parameter name="list_value" type="list" value_type="String"
      // values="foo bar baz" />
      AttributesImpl attributes = new AttributesImpl();
      attributes.addAttribute("name", "name", "name", "", "list_value");
      attributes.addAttribute("value_type", "value_type", "value_type", "", "boolean");
      attributes.addAttribute("values", "values", "values", "", "true false true");

      ListSetterCreator creator = new ListSetterCreator();
      creator.init(attributes);
      ListParameterSetter<String> setter = (ListParameterSetter<String>) creator.createSetter();
      ParametersCreator pc = new ParametersCreator();
      creator.addParameter(pc);
      Parameters params = pc.createParameters();
      setter.reset(params);
      assertEquals(true, params.getValue("list_value"));
      setter.next(params);
      assertEquals(false, params.getValue("list_value"));
      setter.next(params);
      assertEquals(true, params.getValue("list_value"));
      assertTrue(setter.atEnd());
    } catch (ParameterFormatException e) {
      e.printStackTrace();
    }
  }

  public void testDoubleListSetterCreator() {
    try {
      // <parameter name="list_value" type="list" value_type="String"
      // values="foo bar baz" />
      AttributesImpl attributes = new AttributesImpl();
      attributes.addAttribute("name", "name", "name", "", "list_value");
      attributes.addAttribute("value_type", "value_type", "value_type", "", "double");
      attributes.addAttribute("values", "values", "values", "", ".12343 132342.23423 1.23");

      ListSetterCreator creator = new ListSetterCreator();
      creator.init(attributes);
      ListParameterSetter<String> setter = (ListParameterSetter<String>) creator.createSetter();
      ParametersCreator pc = new ParametersCreator();
      creator.addParameter(pc);
      Parameters params = pc.createParameters();
      setter.reset(params);
      assertEquals(.12343, params.getValue("list_value"));
      setter.next(params);
      assertEquals(132342.23423, params.getValue("list_value"));
      setter.next(params);
      assertEquals(1.23, params.getValue("list_value"));
      assertTrue(setter.atEnd());
    } catch (ParameterFormatException e) {
      e.printStackTrace();
    }
  }

  public void testFloatListSetterCreator() {
    try {
      // <parameter name="list_value" type="list" value_type="String"
      // values="foo bar baz" />
      AttributesImpl attributes = new AttributesImpl();
      attributes.addAttribute("name", "name", "name", "", "list_value");
      attributes.addAttribute("value_type", "value_type", "value_type", "", "float");
      attributes.addAttribute("values", "values", "values", "", ".12343 132342.23423 1.23");

      ListSetterCreator creator = new ListSetterCreator();
      creator.init(attributes);
      ListParameterSetter<String> setter = (ListParameterSetter<String>) creator.createSetter();
      ParametersCreator pc = new ParametersCreator();
      creator.addParameter(pc);
      Parameters params = pc.createParameters();
      setter.reset(params);
      assertEquals(.12343f, params.getValue("list_value"));
      setter.next(params);
      assertEquals(132342.23423f, params.getValue("list_value"));
      setter.next(params);
      assertEquals(1.23f, params.getValue("list_value"));
      assertTrue(setter.atEnd());
    } catch (ParameterFormatException e) {
      e.printStackTrace();
    }
  }

  public void testStringListSetterCreator() {
    try {
      // <parameter name="list_value" type="list" value_type="String"
      // values="foo bar baz" />
      AttributesImpl attributes = new AttributesImpl();
      attributes.addAttribute("name", "name", "name", "", "list_value");
      attributes.addAttribute("value_type", "value_type", "value_type", "", "string");
      attributes.addAttribute("values", "values", "values", "", "'fo\"o . ' 'bar' 'b az'");

      ListSetterCreator creator = new ListSetterCreator();
      creator.init(attributes);
      ListParameterSetter<String> setter = (ListParameterSetter<String>) creator.createSetter();
      ParametersCreator pc = new ParametersCreator();
      creator.addParameter(pc);
      Parameters params = pc.createParameters();
      setter.reset(params);
      assertEquals("fo\"o . ", params.getValue("list_value"));
      setter.next(params);
      assertEquals("bar", params.getValue("list_value"));
      setter.next(params);
      assertEquals("b az", params.getValue("list_value"));
      assertTrue(setter.atEnd());
    } catch (ParameterFormatException e) {
      e.printStackTrace();
    }
  }

  public void testIntSetterCreator() {
    try {
      // <parameter name="num_1" type="number" start="1" end="4" step="1">
      AttributesImpl attributes = new AttributesImpl();
      attributes.addAttribute("name", "name", "name", "", "aParam");
      attributes.addAttribute("start", "start", "start", "", "1");
      attributes.addAttribute("end", "end", "end", "", "4");
      attributes.addAttribute("step", "step", "step", "", "1");

      NumberSetterCreator creator = new NumberSetterCreator();
      creator.init(attributes);
      ParameterSetter setter = creator.createSetter();
      assertTrue(setter instanceof IntSteppedSetter);
      IntSteppedSetter iSetter = (IntSteppedSetter) setter;
      ParametersCreator pc = new ParametersCreator();
      creator.addParameter(pc);
      Parameters params = pc.createParameters();
      iSetter.reset(params);
      assertEquals(1, params.getValue("aParam"));
      iSetter.next(params);
      assertEquals(2, params.getValue("aParam"));
      iSetter.next(params);
      assertEquals(3, params.getValue("aParam"));
      iSetter.next(params);
      assertEquals(4, params.getValue("aParam"));
      assertTrue(iSetter.atEnd());
    } catch (ParameterFormatException e) {
      e.printStackTrace();
    }
  }

  public void testDoubleSetterCreator() {
    try {
      AttributesImpl attributes = new AttributesImpl();
      attributes.addAttribute("name", "name", "name", "", "aParam");
      attributes.addAttribute("start", "start", "start", "", "1");
      attributes.addAttribute("end", "end", "end", "", "2");
      attributes.addAttribute("step", "step", "step", "", ".1");

      NumberSetterCreator creator = new NumberSetterCreator();
      creator.init(attributes);
      ParameterSetter setter = creator.createSetter();
      assertTrue(setter instanceof DoubleSteppedSetter);
      DoubleSteppedSetter iSetter = (DoubleSteppedSetter) setter;
      ParametersCreator pc = new ParametersCreator();
      creator.addParameter(pc);
      Parameters params = pc.createParameters();
      iSetter.reset(params);
      for (double i = 1; i < 2; i += .1) {
        assertEquals(i, (Double) params.getValue("aParam"), .0001);
        iSetter.next(params);
      }
      assertTrue(iSetter.atEnd());
    } catch (ParameterFormatException e) {
      e.printStackTrace();
    }
  }
}
