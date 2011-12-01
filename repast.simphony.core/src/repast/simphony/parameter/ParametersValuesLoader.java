package repast.simphony.parameter;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Parser for loading stored parameter values into a Parameters object.
 * This assumes that the schema is appropriate for the loaded
 * values. The format consists
 * of a name value pair where the value is a string representation of parameter value.
 * For example,
 * <pre>
 * <parameters type="valuesOnly">
 * 				<parameter name="double" value="3.2343"/>
 * </parameters>
 * </pre>
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ParametersValuesLoader extends DefaultHandler2 {

  private static final String PARAMETER = "parameter";
  public static Map<String, Class> typeMap = new HashMap<String, Class>();

  static {
    typeMap.put("int", int.class);
    typeMap.put("double", double.class);
    typeMap.put("long", long.class);
    typeMap.put("float", float.class);
    typeMap.put("boolean", boolean.class);
    typeMap.put("byte", byte.class);
    typeMap.put("short", short.class);
    typeMap.put("String", String.class);
    typeMap.put("string", String.class);
  }

  private Map<String, String> nameValMap = new HashMap<String, String>();


  public ParametersValuesLoader(File file) throws IOException, SAXException, ParserConfigurationException {
    SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
    parser.parse(file, this);
  }

  public ParametersValuesLoader(String str) throws IOException, SAXException, ParserConfigurationException {
    SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
    parser.parse(new InputSource(new StringReader(str)), this);
  }

  /**
   * Loads the stored values into the specified Parameters. This
   * assumes that the Parameters' schema is appropriate for the
   * loaded parameter values.
   *
   * @param params the Parameters to load the stored values into
   * @throws ParameterFormatException if there is an error loading the parameters
   */
  public void loadValues(Parameters params) throws ParameterFormatException {
    for (String name : nameValMap.keySet()) {
      String val = nameValMap.get(name);
      Schema schema = params.getSchema();
      ParameterSchema details = schema.getDetails(name);
      if (details.getConverter() != null) {
        params.setValue(name, val);
      } else {
        Class type = details.getType();
        Object[] vals = ParameterUtils.parseDefaultValue(type, name, val);
        params.setValue(name, vals[0]);
      }

    }
  }


  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

    if (qName.equals(PARAMETER)) {
      String name = attributes.getValue("name");
      String defaultValue = attributes.getValue("value");

      if (name == null || defaultValue == null)
        throw new SAXException("Parameter element is missing required attributes");

      nameValMap.put(name, defaultValue);
    }
  }


  @Override
  public void endDocument() throws SAXException {
    if (nameValMap.size() == 0) {
      throw new SAXException("Error in parameter file format: no parameters found");
    }
  }
}
