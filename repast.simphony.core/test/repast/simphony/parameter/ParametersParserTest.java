package repast.simphony.parameter;

import junit.framework.TestCase;
import org.apache.velocity.app.Velocity;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

/**
 * Test of the ParametersParser.
 *
 * @author Nick Collier
 */
public class ParametersParserTest extends TestCase {

  private ParametersParser parser;

  public ParametersParserTest() {
    Properties props = new Properties();
    props.put("resource.loader", "class");
    props.put("class.resource.loader.description", "Velocity Classpath Resource Loader");
    props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    try {
      Velocity.init(props);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setUp() throws URISyntaxException, IOException, ParserConfigurationException, SAXException {
    parser = new ParametersParser(new File(getClass().getResource("test_params.xml").toURI()));
  }

  /*
    <parameter name="int" type="int" defaultValue="3"/>
  <parameter name="float" type="float" defaultValue="3.14f"/>
  <parameter name="long" type="long" defaultValue="334L"/>
  <parameter name="double" type="double" defaultValue="3.2343"/>
  <parameter name="string" type="string" defaultValue="Cormac"/>
  <parameter name="bool" type="boolean" defaultValue="true"/>
   */

  public void testParse() {
    Parameters params = parser.getParameters();
    assertEquals(7, params.getSchema().size());
    assertEquals(new Integer(3), params.getValue("int"));
    assertEquals(new Float(3.14), params.getValue("float"));
    assertEquals(new Long(334), params.getValue("long"));
    assertEquals(new Double(3.2343), params.getValue("double"));
    assertEquals("Cormac", params.getValue("string"));
    assertEquals(Boolean.TRUE, params.getValue("bool"));
    assertEquals(new Name("Nick", "Collier"), params.getValue("conv"));

    ParameterSchema details = params.getSchema().getDetails("string");
    List list = details.getConstrainingList();
    String[] vals = {"Cormac", "George", "Smith", "Bob", "Nick Collier"};
    assertEquals(vals.length, list.size());
    for (int i = 0; i < vals.length; i++) {
      assertEquals(vals[i], list.get(i));
    }

    params.setValue("conv", new Name("Cormac", "Lynch-Collier"));
    assertEquals(new Name("Cormac", "Lynch-Collier"), params.getValue("conv"));
  }

  public void testReadWriteValues() throws Exception {
    Parameters params = parser.getParameters();

    ParametersWriter writer = new ParametersWriter();
    File file = new File(getClass().getResource("test_params.xml").toURI());
    file = new File(file.getParent(), "out.xml");
    writer.writeValuesToFile(params, file);

    // change the values before reading the
    // old values in
    params.setValue("int", 4);
    params.setValue("float", 4.4f);
    params.setValue("long", 444L);
    params.setValue("double", 4.23423);
    params.setValue("string", "Bob");
    params.setValue("bool", false);
    assertEquals(new Integer(4), params.getValue("int"));

    ParametersValuesLoader parser = new ParametersValuesLoader(file);
    parser.loadValues(params);

    assertEquals(7, params.getSchema().size());
    assertEquals(new Integer(3), params.getValue("int"));
    assertEquals(new Float(3.14), params.getValue("float"));
    assertEquals(new Long(334), params.getValue("long"));
    assertEquals(new Double(3.2343), params.getValue("double"));
    assertEquals("Cormac", params.getValue("string"));
    assertEquals(Boolean.TRUE, params.getValue("bool"));
    assertEquals(new Name("Nick", "Collier"), params.getValue("conv"));

    ParameterSchema details = params.getSchema().getDetails("string");
    List list = details.getConstrainingList();
    String[] vals = {"Cormac", "George", "Smith", "Bob", "Nick Collier"};
    assertEquals(vals.length, list.size());
    for (int i = 0; i < vals.length; i++) {
      assertEquals(vals[i], list.get(i));
    }
  }
}
