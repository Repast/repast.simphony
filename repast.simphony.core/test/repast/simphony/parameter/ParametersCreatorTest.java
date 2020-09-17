package repast.simphony.parameter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Range;

import junit.framework.TestCase;

/**
 * @author Nick Collier
 */
public class ParametersCreatorTest extends TestCase {

  class Name {
    private String name;


    public Name(String name) {
      this.name = name;
    }


    public String getName() {
      return name;
    }

    public boolean equals(Object obj) {
      if (obj instanceof Name) {
        return ((Name)obj).name.equals(name);
      }
      return false;
    }
  }

  class NameConverter implements StringConverter {


    public String toString(Object obj) {
      return ((Name)obj).name;
    }

    public Object fromString(String strRep) {
      return new Name(strRep);
    }
  }

  public void testConvertor() {
    ParametersCreator creator = new ParametersCreator();
    NameConverter convertor = new NameConverter();
    creator.addParameter("familyName", Name.class, new Name("Nick"), false);
    creator.addConvertor("familyName", convertor);
    List<Name> list = new ArrayList<Name>();
    list.add(new Name("Caitrin"));
    list.add(new Name("Nicola"));
    list.add(new Name("Cormac"));
    list.add(new Name("Nick"));
    creator.addConstraint("familyName", list);

    Parameters params = creator.createParameters();
    assertEquals(new Name("Nick"), params.getValue("familyName"));

    params.setValue("familyName", "Nicola");
    assertEquals(new Name("Nicola"), params.getValue("familyName"));

  }


  public void testConstraints() {
    ParametersCreator creator = new ParametersCreator();
    creator.addParameter("int", "My Int", int.class, 3, false);
    creator.addConstraint("int", new SteppedRange(0.0, 11.2, 1.0));
    List<String> list = new ArrayList<String>();
    list.add("foo");
    list.add("bar");
    list.add("boo");
    creator.addParameter("string", "My String", String.class, "foo", false);
    creator.addConstraint("string", list);

    Parameters params = creator.createParameters();
    assertEquals(2, params.getSchema().size());
    ParameterSchema details = params.getSchema().getDetails("int");
    assertEquals("int", details.getName());
    assertEquals(int.class, details.getType());
    assertEquals(3, details.getDefaultValue());
    assertEquals(new SteppedRange(0d, 11.2d, 1.0), details.getConstrainingRange());

    assertEquals(3, params.getValue("int"));
    assertEquals("foo", params.getValue("string"));

    params.setValue("int", 10);
    assertEquals(10, params.getValue("int"));

    params.setValue("string", "bar");
    assertEquals("bar", params.getValue("string"));
    params.setValue("string", "boo");
    assertEquals("boo", params.getValue("string"));

    try {
      params.setValue("int", -3);
      fail("should throw exeception");
    } catch (IllegalParameterException ex) {
      assertTrue(true);
    }

    try {
      params.setValue("int", 12);
      fail("should throw exeception");
    } catch (IllegalParameterException ex) {
      assertTrue(true);
    }

    try {
      params.setValue("string", "hello");
      fail("should throw exeception");
    } catch (IllegalParameterException ex) {
      assertTrue(true);
    }
  }

  public void testBadConstraint() {
    ParametersCreator creator = new ParametersCreator();
    creator.addParameter("int", "My Int", int.class, 3, false);
    try {
      creator.addConstraint("int", new SteppedRange(-3.0, 0.0, 1.0));
      fail("should throw exception");
    } catch (IllegalParameterException ex) {
      //ex.printStackTrace();
      assertTrue(true);
    }

  }
}
