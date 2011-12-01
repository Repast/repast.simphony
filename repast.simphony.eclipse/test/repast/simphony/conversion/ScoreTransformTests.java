/**
 * 
 */
package repast.simphony.conversion;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * @author Nick Collier
 */
public class ScoreTransformTests {

  @Test
  public void testScoreTransform() throws TransformerException, IOException, XMLStreamException,
      SAXException {
    ScoreToCM transformer = new ScoreToCM();
    transformer.run(new File("./test/scoreTransforms/model.score"));

    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = factory.newSchema(new File("./test/scoreTransforms/context.xsd"));
    Validator validator = schema.newValidator();
    validator.validate(new StreamSource(new File("./test/scoreTransforms/context.xml")));
  }

  @Test
  public void testModelSchema() throws Exception {
    try {
      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = factory.newSchema(new File("./test/scoreTransforms//user_path.xsd"));
      Validator validator = schema.newValidator();
      validator.validate(new StreamSource(new File("./test/data/model.xml")));

    } catch (Exception ex) {
      assertTrue(ex.getMessage(), false);
      throw ex;
    }

  }
  
  @Test
  public void testScenario() throws IOException, XMLStreamException {
    StringWriter writer = new StringWriter();
    ScenarioXMLConverter converter = new ScenarioXMLConverter();
    converter.run(new File("./test/data/scenario.xml"), writer);
    String xml = writer.toString();
    assertTrue(xml.contains("<Scenario>"));
    assertTrue(xml.contains("</Scenario>"));
    assertTrue(!xml.contains("repast.simphony.action.display"));
  }

  @Test
  public void testScoreToModel() throws IOException, XMLStreamException, SAXException {
    ScoreToUserPath trans = new ScoreToUserPath();
    StringWriter writer = new StringWriter();
    trans.run(new File("./test/scoreTransforms/model2.score"), writer);
    //System.out.println(writer.toString());

    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = factory.newSchema(new File("./test/scoreTransforms/user_path.xsd"));
    Validator validator = schema.newValidator();
    validator.validate(new StreamSource(new StringReader(writer.toString())));

    writer = new StringWriter();
    trans.run(new File("./test/scoreTransforms/model.score"), writer);
    //System.out.println(writer.toString());

    factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    schema = factory.newSchema(new File("./test/scoreTransforms/user_path.xsd"));
    validator = schema.newValidator();
    validator.validate(new StreamSource(new StringReader(writer.toString())));

  }

}
