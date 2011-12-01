package repast.simphony.dataLoader.engine;

import com.thoughtworks.xstream.XStream;
import repast.simphony.scenario.ActionSaver;
import repast.simphony.util.collections.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * DataLoader action saver for DFDataLoaderControllerAction
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/10 17:02:31 $
 */
public class DFDataLoaderActionSaver implements ActionSaver<DFDataLoaderControllerAction> {

  // saves the directory and delimiter to a file as a Pair
  public void save(XStream xstream, DFDataLoaderControllerAction action, String filename) throws IOException {
    DelimitedFileContextBuilder builder = action.getBuilder();
    Pair<String, String> pair = new Pair<String, String>(builder.getPathName(),
            String.valueOf(builder.getDelimiter()));
    FileWriter writer = new FileWriter(new File(filename));
    xstream.toXML(pair, writer);
    writer.close();
  }
}