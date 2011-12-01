package repast.simphony.dataLoader.engine;

import com.thoughtworks.xstream.XStream;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.freezedry.datasource.DFClassLister;
import repast.simphony.scenario.AbstractActionLoader;
import repast.simphony.scenario.Scenario;
import repast.simphony.util.collections.Pair;
import simphony.util.messages.MessageCenter;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * ActionLoader for delimited file based data loaders.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/10 17:02:31 $
 */
public class DFDataLoaderActionLoader extends AbstractActionLoader {

  private static final MessageCenter msgCenter = MessageCenter.getMessageCenter(DFDataLoaderActionLoader.class);

  public DFDataLoaderActionLoader(File file, Object contextID) {
    super(file, contextID);
  }

  // reads a pair from the xtream -- the first element of the pair is the directory of freezedried
  // content and the second is the delimiter.
  public void performLoad(Reader reader, XStream xstream, Scenario scenario, ControllerRegistry registry) {
    ClassLoader loader = xstream.getClassLoader();
    try {
      xstream.setClassLoader(Pair.class.getClassLoader());
      Pair<String, String> pair = (Pair<String, String>) xstream.fromXML(reader);
      xstream.setClassLoader(loader);
      File zipFile = new File(pair.getFirst());
      if (!zipFile.exists()) {
        msgCenter.error("Error loading freezedry zip file: file does not exist", new IOException());
        return;
      }

      DFClassLister lister = new DFClassLister(zipFile.getAbsolutePath());
      List<Class<?>> classList = lister.getClasses();

      DelimitedFileContextBuilder builder = new DelimitedFileContextBuilder(true, classList, contextID,
              zipFile.getAbsolutePath(), pair.getSecond().toCharArray()[0]);
      DFDataLoaderControllerAction action = new DFDataLoaderControllerAction("Foo", builder, scenario);
      ControllerAction parent = registry.findAction(contextID, ControllerActionConstants.DATA_LOADER_ROOT);
      registry.addAction(contextID, parent, action);
    } catch (ClassNotFoundException e) {
      msgCenter.error("Could not load class from file '" + file + "'", e);
    } catch (IOException e) {
      msgCenter.error("Error while reading zip file '" + file + "'", e);
    } finally {
      xstream.setClassLoader(loader);
    }
  }
}