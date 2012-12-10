package repast.simphony.parameter.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import repast.simphony.parameter.ParameterFormatException;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.ParameterTreeSweeper;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.util.collections.Pair;

/**
 * Parses a parameters xml file and creates ParameterTreeSweeper based on that.
 * Additional ParameterSetterCreators can be registered for dealing with
 * additional parameter types using the registerSetterCreator method.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ParameterSweepParser extends DefaultHandler2 {

  private static final String PARAMETER_NAME = "parameter";
  private static final String SWEEP = "sweep";
  private URL paramsURL;
  private InputStream inputStream;
  private Stack<ParameterSetter> stack = new Stack<ParameterSetter>();
  private ParameterTreeSweeper sweeper = new ParameterTreeSweeper();
  private Map<String, ParameterSetterCreator> creators = new HashMap<String, ParameterSetterCreator>();
  private Map<String, ParameterSetterCreator> constantCreators = new HashMap<String, ParameterSetterCreator>();
  private ParametersCreator creator = new ParametersCreator();

  /**
   * Creates a ParameterSweepParser to parse the specified URL.
   * 
   * @param paramsURL
   */
  public ParameterSweepParser(URL paramsURL) {
    this.paramsURL = paramsURL;
    init();
  }
  
  public ParameterSweepParser(InputStream in) {
    this.inputStream = in;
    init();
  }
  
  private void init() {
    creators.put("number", new NumberSetterCreator());
    creators.put("list", new ListSetterCreator());

    // constantCreators.put("number", new ConstantNumberSetterCreator());
    constantCreators.put("string", new ConstantStringSetterCreator());
    constantCreators.put("String", new ConstantStringSetterCreator());
    constantCreators.put("boolean", new ConstantBooleanSetterCreator());
    constantCreators.put("java.lang.Boolean", new ConstantBooleanSetterCreator());
    constantCreators.put("java.lang.String", new ConstantStringSetterCreator());
    constantCreators.put("java.lang.Integer", new ConstantNumberSetterCreator("java.lang.Integer"));
    constantCreators.put("java.lang.Double", new ConstantNumberSetterCreator("java.lang.Double"));
    constantCreators.put("java.lang.Float", new ConstantNumberSetterCreator("java.lang.Float"));
    constantCreators.put("java.lang.Long", new ConstantNumberSetterCreator("java.lang.Long"));
    constantCreators.put("java.lang.Short", new ConstantNumberSetterCreator("java.lang.Short"));
    constantCreators.put("java.lang.Byte", new ConstantNumberSetterCreator("java.lang.Byte"));

    constantCreators.put("int", new ConstantNumberSetterCreator("java.lang.Integer"));
    constantCreators.put("double", new ConstantNumberSetterCreator("java.lang.Double"));
    constantCreators.put("float", new ConstantNumberSetterCreator("java.lang.Float"));
    constantCreators.put("long", new ConstantNumberSetterCreator("java.lang.Long"));
    constantCreators.put("short", new ConstantNumberSetterCreator("java.lang.Short"));
    constantCreators.put("byte", new ConstantNumberSetterCreator("java.lang.Byte"));
  }

  /**
   * Creates a ParameterSweeper parser to use the specified sweeper and to parse
   * the specified URL.
   * 
   * @param sweeper
   * @param paramsURL
   */
  public ParameterSweepParser(ParameterTreeSweeper sweeper, URL paramsURL) {
    this(paramsURL);
    this.sweeper = sweeper;
  }

  /**
   * Parse the url and return the created ParameterTreeSweeper.
   * 
   * @return the created ParameterTreeSweeper.
   * @throws ParserConfigurationException
   * @throws SAXException
   * @throws IOException
   */
  public Pair<Parameters, ParameterTreeSweeper> parse() throws ParserConfigurationException,
      SAXException, IOException {
    SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
    if (this.inputStream == null) {
      inputStream = paramsURL.openStream();
    }
    
    parser.parse(inputStream, this);
    inputStream.close();
    return new Pair<Parameters, ParameterTreeSweeper>(creator.createParameters(), sweeper);
  }

  /**
   * Method writes out string content to a batch xml file used for temporary
   * data storage.
   * 
   * @param input
   *          the input string to write out
   * @throws IOException
   */
  public void inputFile(String input) throws IOException {
    boolean mjbSuggest = false;
    String file;
    if (!mjbSuggest) {
      file = "./batch.xml";
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      out.write(input);
      out.close();
      paramsURL = new File(paramsURL.getPath()).toURL();
    } else {
      // BufferedWriter out = new BufferedWriter(new FileWriter(file));
      File tFile = File.createTempFile("sweep", ".xml");
      System.out.println("TempFile: <" + tFile.getName() + ">");
      file = tFile.getName();
      file = tFile.getAbsolutePath();
      tFile.delete();
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      out.write(input);
      out.close();
      // paramsURL=new File(paramsURL.getPath()).toURL();
      paramsURL = new File(file).toURL();
    }
  }

  /**
   * Method reads file URL and size (based on content) and returns a string
   * representation of that content.
   * 
   * @param url
   *          the file url
   * @param size
   *          the size of the file
   * @return a string representation of the parameters
   * @throws IOException
   */
  public String readFile(URL url, int size) throws IOException {
    this.paramsURL = url;
    byte[] b = new byte[size];
    InputStream inputStream = this.paramsURL.openStream();
    inputStream.read(b);
    String params = new String(b);
    return params;
  }

  /**
   * Register a setter creator to be used during sweeper creation. When a
   * parameter element whose "type" attribute is equal to the specified typeID
   * is encountered, the specified creator will be used to create the
   * ParameterSetter. The attributes of that element will be passed to the
   * creator so that it can create the setter.
   * 
   * @param typeID
   * @param creator
   */
  public void registerStepperCreator(String typeID, ParameterSetterCreator creator) {
    creators.put(typeID, creator);
  }

  /**
   * Register a setter creator to be used for constant setters during sweeper
   * creation. When a parameter element whose "constant type" attribute is equal
   * to the specified typeID is encountered, the specified creator will be used
   * to create the ParameterSetter. The attributes of that element will be
   * passed to the creator so that it can create the constant setter.
   * 
   * @param constantTypeID
   *          matches agains the constant_type attribute.
   * @param creator
   */
  public void registerConstantCreator(String constantTypeID, ParameterSetterCreator creator) {
    constantCreators.put(constantTypeID, creator);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    if (qName.equals(SWEEP)) {
      sweeper.setRunCount(Integer.parseInt(attributes.getValue("runs")));
    } else if (qName.equals(PARAMETER_NAME)) {
      try {
        String type = attributes.getValue("type");
        ParameterSetterCreator setterCreator = null;
        if (type.equals(SetterConstants.CONSTANT_ID)) {
          String cType = attributes.getValue(SetterConstants.CONSTANT_TYPE_NAME);

          // for backward compatibility:
          if (cType.equals("number")) {
            // need to infer the data type by the specified values
            cType = inferDataTypeForConstant(attributes);
          }

          setterCreator = constantCreators.get(cType);
        } else {
          setterCreator = creators.get(type);
        }

        if (setterCreator == null) {
          throw new SAXException(new ParameterFormatException("Invalid parameter '"
              + attributesToString(attributes) + "'"));
        }

        setterCreator.init(attributes);
        setterCreator.addParameter(this.creator);
        ParameterSetter setter = setterCreator.createSetter();

        if (stack.isEmpty()) {
          ParameterSetter root = sweeper.getRootParameterSetter();
          sweeper.add(root, setter);
        } else {
          sweeper.add(stack.peek(), setter);
        }
        stack.push(setter);
      } catch (ParameterFormatException ex) {
        SAXException e = new SAXException(ex);
        e.initCause(ex);
        throw e;
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (qName.equals(PARAMETER_NAME))
      stack.pop();
  }

  private String inferDataTypeForConstant(Attributes attributes) {

    String value = attributes.getValue("value");
    return inferDataType(value);
  }

  private String inferDataType(String value) {

    // allowable data types: long, double, float, integer
    // rules:
    // long terminates with L
    // float terminates with f
    // double contains "." and one or more digits
    // int otherwise

    if (isLong(value))
      return "long";
    else if (isFloat(value))
      return "float";
    else if (isDouble(value))
      return "double";
    else
      return "int";
  }

  protected boolean isDouble(String val) {
    return Pattern.matches("\\d*\\.\\d+", val);
  }

  protected boolean isLong(String val) {
    String end = val.substring(val.length() - 1, val.length());
    return end.equalsIgnoreCase("l");
  }

  protected boolean isFloat(String val) {
    String end = val.substring(val.length() - 1, val.length());
    return end.equalsIgnoreCase("f");
  }

  protected boolean isInt(Double value) {
    return Math.rint(value) == value;
  }

  private String attributesToString(Attributes attributes) {
    StringBuilder builder = new StringBuilder("[");
    for (int i = 0; i < attributes.getLength(); i++) {
      if (i != 0)
        builder.append(", ");
      builder.append(attributes.getQName(i));
      builder.append("=\"");
      builder.append(attributes.getValue(i));
      builder.append("\"");
    }
    builder.append("]");
    return builder.toString();
  }
}
