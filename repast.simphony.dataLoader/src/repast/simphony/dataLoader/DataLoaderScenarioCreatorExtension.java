package repast.simphony.dataLoader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XStream11XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
//import repast.score.SContext;
//import repast.score.SImplementation;
//import repast.score.SImplementationMode;
//import repast.score.impl.SImplementationImpl;
//import repast.score.xsd.ContextPersist;
import repast.simphony.dataLoader.engine.CNDataLoaderActionSaver;
import repast.simphony.dataLoader.engine.CNDataLoaderControllerActionIO;
import repast.simphony.dataLoader.engine.ClassNameContextBuilder;
import repast.simphony.dataLoader.engine.ClassNameDataLoaderAction;
import repast.simphony.scenario.FastMethodConvertor;
import repast.simphony.scenario.ScenarioCreatorExtension;
import repast.simphony.scenario.ScenarioEntry;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Automatically adds a class name data loader action entry to the created
 * scenario, if the implemention in the score file is a DataLoader.
 *
 * TODO determine what, if any, of this functionality is required -ert.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DataLoaderScenarioCreatorExtension implements ScenarioCreatorExtension {

  static class DLScenarioEntry implements ScenarioEntry {

    private String contextID, fileName, registryID;

    public DLScenarioEntry(String contextID, String fileName, String registryID) {
      this.contextID = contextID;
      this.fileName = fileName;
      this.registryID = registryID;
    }

    /**
     * Gets the context the action is associated with.
     *
     * @return the context the action is associated with.
     */
    public String getContextID() {
      return contextID;
    }

    /**
     * Gets the file name of the serialized action.
     *
     * @return the file name of the serialized action.
     */
    public String getFileName() {
      return fileName;
    }

    /**
     * Gets the id associated with the action itself. For example, repast.action.display.
     *
     * @return the id associated with the action itself. For example, repast.action.display.
     */
    public String getRegistryID() {
      return registryID;
    }
  }

  private List<ScenarioEntry> entries = new ArrayList<ScenarioEntry>();
  private int index = 0;

  /**
   * Automatically adds a class name data loader action entry to the created
   * scenario, if the implemention in the score file is a DataLoader.
   *
   * @param scenarioPath the path to the scenario dir
   * @return optional ScenarioEntry that can be put in the scenario.xml file. This can be null.
   */
  public List<ScenarioEntry> run(File scenarioPath) throws Exception {
    entries.clear();
    index = 0;
//    File modelFile = new File(scenarioPath, ContextPersist.CONTEXT_FILE_NAME);
//    SContext rootContext = ContextPersist.create(modelFile).load();
//    processContext(scenarioPath, rootContext);
    return new ArrayList<ScenarioEntry>(entries);
  }

//  private void processContext(File scenarioPath, SContext context) throws IOException, IllegalAccessException,
//          InstantiationException {
//    Class clazz = findClass(context);
//    if (clazz != null && ContextBuilder.class.isAssignableFrom(clazz)) {
//      // make the actual data loader itself
//      ContextBuilder contextBuilder = (ContextBuilder) clazz.newInstance();
//      ClassNameContextBuilder loader = new ClassNameContextBuilder(contextBuilder);
//      ClassNameDataLoaderAction action = new ClassNameDataLoaderAction(loader);
//      CNDataLoaderActionSaver saver = new CNDataLoaderActionSaver();
//
//      // create the xstream, and save the action
//      XStream xstream = new XStream(new XppDriver(new XStream11XmlFriendlyReplacer())) {
//        protected boolean useXStream11XmlFriendlyMapper() {
//          return true;
//        }
//      };
//      xstream.registerConverter(new FastMethodConvertor(xstream));
//      CNDataLoaderControllerActionIO io = new CNDataLoaderControllerActionIO();
//      File file = createFile(scenarioPath, io.getSerializationID());
//      saver.save(xstream, action, file.getCanonicalPath());
//
//      // create the ScenarioEntry
//      entries.add(new DLScenarioEntry(context.getLabel(), file.getName(), io.getSerializationID()));
//    }
//
//    for (Iterator iter = context.getSubContexts().iterator(); iter.hasNext();) {
//      SContext sub = (SContext) iter.next();
//      processContext(scenarioPath, sub);
//    }
//  }
//
//  private File createFile(File scenarioPath, String registryID) {
//    StringBuffer b = new StringBuffer(registryID);
//    b.append("_");
//    b.append(index++);
//    b.append(".xml");
//    return new File(scenarioPath, b.toString());
//  }
//
//  private Class findClass(SContext context) throws IOException {
//    SImplementation impl = context.getImplementation();
//    if (impl != null && !impl.getMode().equals(SImplementationMode.EXTERNAL_LITERAL)) {
//      String cp = SImplementationImpl.addDir(impl.getDerivedPath(), impl.getDerivedBinDir());
//
//      URL[] urls = new URL[1];
//      urls[0] = new File(cp).getCanonicalFile().toURL();
//      URLClassLoader classLoader = new URLClassLoader(urls, this.getClass().getClassLoader());
//      try {
//        return Class.forName(impl.getQualifiedName(), false, classLoader);
//      } catch (ClassNotFoundException e) {
//        // ok to return null because class problems will be
//        // sorted later.
//        return null;
//      }
//    }
//    return null;
//  }
}
