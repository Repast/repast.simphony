package repast.simphony.dataLoader.engine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import repast.simphony.scenario.ActionSaver;

import com.thoughtworks.xstream.XStream;

/**
 * DataLoader action saver for ClassNameDataLoaderAction.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/10 17:02:31 $
 */
public class CNDataLoaderActionSaver implements ActionSaver<ClassNameDataLoaderAction> {

	public void save(XStream xstream, ClassNameDataLoaderAction controllerAction, String filename)
					throws IOException
	{
		String className = controllerAction.getBuilder().getClassName();
    FileWriter writer = new FileWriter(new File(filename));
    xstream.toXML(className, writer);
    writer.close();
  }
}
