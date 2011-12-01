package repast.simphony.dataLoader.engine;

import com.thoughtworks.xstream.XStream;
import repast.simphony.scenario.ActionSaver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * DataLoader action saver for XMLDataLoaderAction.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/10 17:02:31 $
 */
public class XMLDataLoaderActionSaver implements ActionSaver<XMLDataLoaderAction> {

  public void save(XStream xstream, XMLDataLoaderAction controllerAction, String filename)
          throws IOException {
    String className = controllerAction.getBuilder().getXMLFile().getCanonicalPath();
    FileWriter writer = new FileWriter(new File(filename));
    xstream.toXML(className, writer);
    writer.close();
  }
}