package repast.simphony.scenario;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XStream11XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.plugin.ControllerActionIOExtensions;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses a scenario.xml file and loads the serialized controller actions into a
 * ControllerRegistry. This also reads the other info in a scenario file as well.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ScenarioFileLoader extends DefaultHandler2 {

  public static final String MODEL_INITIALIZER_XML = "model.initializer";
  public static final String MODEL_PLUGIN_XML = "model.plugin_jpf";

  private List<ActionLoader> loaders = new ArrayList<ActionLoader>();
  private ControllerActionIOExtensions ext;
  private File scenarioDir;
  private String modelInitClass = null;
  private File modelPluginPath = null;

  /**
   * Creates a ScenarioFileLoader that will load actions using
   * the specified ControllerActionIOExtensions to do the loading.
   *
   * @param ext the extensions used to the loading
   */
  public ScenarioFileLoader(ControllerActionIOExtensions ext) {
    this.ext = ext;
  }

  /**
   * Loads serialized actions into the specified ControllerRegistry and returns
   * that registry.
   *
   * @param scenarioDir
   * @param scenario
   * @param registry
   * @return the ControllerRegistry containing the loaded actions.
   * @throws ParserConfigurationException
   * @throws SAXException
   * @throws IOException
   */
  public ControllerRegistry load(File scenarioDir, Scenario scenario, ControllerRegistry registry) throws
          ParserConfigurationException, SAXException, IOException {
    this.scenarioDir = scenarioDir;
    SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
    parser.parse(new File(scenarioDir, "scenario.xml"), this);
    createActions(scenario, registry);
    if (modelInitClass != null) {
      scenario.setModelInitName(modelInitClass);
    }

    if (modelPluginPath != null) scenario.setModelPluginPath(modelPluginPath);
    return registry;
  }

  private void createActions(Scenario scenario, ControllerRegistry registry) throws FileNotFoundException {
    XStream xstream = new XStream(new XppDriver(new XStream11XmlFriendlyReplacer())) {
      protected boolean useXStream11XmlFriendlyMapper() {
        return true;
      }
    };
    xstream.registerConverter(new FastMethodConvertor(xstream));
//        xstream.alias("type", SProjectionType.class);
//        xstream.registerConverter(new SProjectionTypeConverter());
//    xstream.registerConverter(new SGridConverter());
//    xstream.registerConverter(new SContinuousSpaceConverter());
//    xstream.registerConverter(new SNetworkConverter());
//    xstream.registerConverter(new SGeographyConverter());

    for (ActionLoader loader : loaders) {
      loader.loadAction(xstream, scenario, registry);
    }
  }


  /**
   * Receive notification of the start of an element.
   * <p/>
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to take specific actions at the start of
   * each element (such as allocating a new tree node or writing
   * output to a file).</p>
   *
   * @param uri        The Namespace URI, or the empty string if the
   *                   element has no Namespace URI or if Namespace
   *                   processing is not being performed.
   * @param localName  The local name (without prefix), or the
   *                   empty string if Namespace processing is not being
   *                   performed.
   * @param qName      The qualified name (with prefix), or the
   *                   empty string if qualified names are not available.
   * @param attributes The attributes attached to the element.  If
   *                   there are no attributes, it shall be an empty
   *                   Attributes object.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *                                  wrapping another exception.
   * @see org.xml.sax.ContentHandler#startElement
   */
  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equals(MODEL_INITIALIZER_XML)) {
      modelInitClass = attributes.getValue("class");
    } else if (qName.equals(MODEL_PLUGIN_XML)) {
      String path = attributes.getValue("file").trim();
      if (path.equals("plugin_jpf.xml")) {
        modelPluginPath = new File(scenarioDir, path);
      }
    } else {
      ControllerActionIO io = ext.getControllerActionIO(qName);

      if (io != null) {
        // todo does null check now for testing, eventually should not return null
        String fileName = attributes.getValue("file");
        String contextID = attributes.getValue("context");
        ActionLoader loader = io.getActionLoader(new File(scenarioDir, fileName), contextID);
        loaders.add(loader);
      }
    }
  }
}
