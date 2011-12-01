package repast.simphony.parameter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Parser for parsing parameter defs and values in xml format.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ParametersParser extends DefaultHandler2 {

  private static final String PARAMETER = "parameter";

  private ParametersCreator creator;

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

  public ParametersParser(Parameters params, File file) throws IOException, SAXException,
      ParserConfigurationException {
    creator = new ParametersCreator();
    if (params != null) creator.addParameters(params);
    Parameters p = creator.createParameters();
    SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
    parser.parse(file, this);
  }

  public ParametersParser(File file) throws ParserConfigurationException, SAXException, IOException {
    this(null, file);
  }

  public Parameters getParameters() {
    Parameters params = creator.createParameters();
    ParameterSchema details = params.getSchema().getDetails(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME);
    if (details != null && params.getValue(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME) == Parameters.NULL) {
      int val = (int) System.currentTimeMillis();
      // per JIRA 76 - "Use positive default random seeds"
      if (val < 0) val = Math.abs(val);
      params.setValue(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME, val);
    }
    
    return params;
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    try {
      if (qName.equals(PARAMETER)) {
	String name = attributes.getValue("name");
	String sType = attributes.getValue("type");
	String defaultValue = attributes.getValue("defaultValue");

	if (name == null || sType == null || defaultValue == null)
	  throw new ParameterFormatException("Parameter element is missing required attributes");

	String strConvertor = attributes.getValue("converter");
	StringConverter converter = null;
	if (strConvertor != null) {
	  Class clazz = Class.forName(strConvertor);
	  converter = (StringConverter) clazz.newInstance();
	}

	String readOnly = attributes.getValue("isReadOnly");
	Class<?> type = typeMap.get(sType);
	if (type == null)
	  type = Class.forName(sType);

	Object defVal;
	if (defaultValue.equals("__NULL__")) {
	  defVal = null;
	} else {
	  if (converter != null)
	    defVal = converter.fromString(defaultValue);
	  else
	    defVal = ParameterUtils.parseDefaultValue(type, name, defaultValue)[0];
	}
	
	if (name.equals(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME) && 
	    defVal == null && creator.contains(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME)) return;
	

	String displayName = attributes.getValue("displayName");
	if (displayName == null)
	  displayName = name;
	creator.addParameter(name, displayName, type, defVal, Boolean.parseBoolean(readOnly));
	if (converter != null)
	  creator.addConvertor(name, converter);

	String list = attributes.getValue("values");
	if (list != null) {
	  if (converter == null) {
	    Object[] vals = ParameterUtils.parseDefaultValue(type, name, list);
	    creator.addConstraint(name, Arrays.asList(vals));
	  } else {
	    List objs = new ArrayList();
	    Object[] vals = ParameterUtils.parseDefaultValue(String.class, name, list);
	    for (Object val : vals) {
	      objs.add(converter.fromString(val.toString()));
	    }
	    creator.addConstraint(name, objs);
	  }
	}
      }
    } catch (ClassNotFoundException e) {
      throw new SAXException("Unable to creates parameters", e);
    } catch (ParameterFormatException ex) {
      throw new SAXException("Unable to convert parameter type. Please use a convertor.", ex);
    } catch (IllegalAccessException e) {
      throw new SAXException("Unable to creates parameters. Bad convertor value", e);
    } catch (InstantiationException e) {
      throw new SAXException("Unable to creates parameters. Bad convertor value", e);
    } catch (ClassCastException ex) {
      throw new SAXException(
	  "Unable to creates parameters. Convertor must implement StringConverter");
    }
  }
}
