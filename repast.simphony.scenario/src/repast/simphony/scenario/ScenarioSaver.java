package repast.simphony.scenario;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.plugin.ActionExtensions;
import repast.simphony.plugin.ControllerActionIOExtensions;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.scenario.data.ContextFileWriter;
import repast.simphony.util.collections.Tree;
import simphony.util.messages.MessageCenter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XStream11XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Saves a scenario, that is, this serializes the scenario action tree.
 * 
 * @author Nick Collier
 */
public class ScenarioSaver {
  private static final MessageCenter LOG = MessageCenter.getMessageCenter(ScenarioSaver.class);

  private ControllerRegistry registry;
  private File scenarioDir;
  private ContextData rootContext;
  private Object currentContextID;
  private ControllerActionIOExtensions ioExts;


  class SaverScenarioEntry implements ScenarioEntry {

    private File file;
    private Object contextID;
    private ControllerAction action;
    private ControllerActionIO actionIO;

    public SaverScenarioEntry(ControllerAction action, ControllerActionIO actionIO, File file, Object contextID) {
      this.action = action;
      this.actionIO = actionIO;
      this.file = file;
      this.contextID = contextID;
    }

    public String getFileName() {
      return file.getName();
    }

    public String getContextID() {
      return contextID.toString();
    }

    public String getRegistryID() {
      return actionIO.getSerializationID();
    }

    @SuppressWarnings("unchecked")
    public void save(XStream xstream) throws IOException {
      actionIO.getActionSaver().save(xstream, action, file.getAbsolutePath());
    }

    @Override
    public String toString() {
      return "id: " + getRegistryID() + ", action's class: " + action.getClass()
              + ", action: " + action;
    }
  }

  private List<SaverScenarioEntry> entries = new ArrayList<SaverScenarioEntry>();

  public ScenarioSaver(ControllerRegistry registry, File scenarioDir, ContextData rootContext) {
    this.registry = registry;
    this.scenarioDir = scenarioDir;
    this.rootContext = rootContext;
  }

  public void save(ActionExtensions ext, Scenario scenario, boolean saveModelSpec) throws Exception, ParseErrorException {
    ioExts = ext.getIoExts();
    Tree<Object> contextGraph = registry.getContextIdTree();
    Object contextRoot = contextGraph.getRoot();
    saveContext(contextRoot, contextGraph);

    saveScenario(scenario.getModelInitName(), scenario.getModelPluginPath(), saveModelSpec);
  }

  private void saveContext(Object contextID, Tree<Object> graph) {
    currentContextID = contextID;
    Tree<ControllerAction> actionGraph = registry.getActionTree(contextID);
    ControllerAction rootAction = actionGraph.getRoot();
    saveAction(rootAction, actionGraph);

    for (Object subContextID : graph.getChildren(contextID)) {
      saveContext(subContextID, graph);
    }
  }

  private void saveAction(ControllerAction action, Tree<ControllerAction> graph) {
    ControllerActionIO io = ioExts.getControllerActionIO(action.getClass());
    if (io != null) {
      // todo this check is for testing. We should throw an error if we can't find the io
      SaverScenarioEntry entry = new SaverScenarioEntry(action, io, createFile(io.getSerializationID()), currentContextID);
      entries.add(entry);
    }
    for (ControllerAction childAction : graph.getChildren(action)) {
      saveAction(childAction, graph);
    }
  }

  private File createFile(String registryID) {
    StringBuffer b = new StringBuffer(registryID);
    b.append("_");
    b.append(entries.size());
    b.append(".xml");
    return new File(scenarioDir, b.toString());
  }

  private void saveScenario(String modelInitName, File modelPluginPath, boolean saveModelSpec) throws Exception, ParseErrorException {
    VelocityContext context = new VelocityContext();
    String template = getClass().getPackage().getName();
    template = template.replace('.', '/');
    template = template + "/Scenario.vt";
    context.put("entries", entries);
    if (modelInitName != null && modelInitName.length() > 0) context.put("modelInit", modelInitName);
    if (modelPluginPath != null) context.put("modelPlugin", modelPluginPath);

    XStream xstream = new XStream(new XppDriver(new XStream11XmlFriendlyReplacer())) {
      protected boolean useXStream11XmlFriendlyMapper() {
        return true;
      }
    };
    xstream.registerConverter(new FastMethodConvertor(xstream));

    /*
    NC: these are obsolete?
    xstream.registerConverter(new SGridConverter());
    xstream.registerConverter(new SContinuousSpaceConverter());
    xstream.registerConverter(new SNetworkConverter());
    xstream.registerConverter(new SGeographyConverter());
    */
    for (SaverScenarioEntry entry : entries) {
      try {
        entry.save(xstream);
      } catch (Throwable ex) {
        LOG.warn("Error saving entry '" + entry + "'. Continuing saving.", ex);
      }
    }

    if (saveModelSpec) {
      File ctxFile = new File(scenarioDir, ScenarioConstants.LEGACY_SCORE_FILE_NAME);
      ContextFileWriter writer = new ContextFileWriter();
      writer.write(ctxFile, rootContext);
    }

    Writer writer = new FileWriter(new File(scenarioDir, Scenario.SCENARIO_FILE_NAME));
    Velocity.mergeTemplate(template, "UTF-8", context, writer);
    writer.close();
  }
}
