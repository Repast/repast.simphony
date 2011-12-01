/*CopyrightHere*/
package repast.simphony.scenario;

import java.io.FileWriter;
import java.io.IOException;

import repast.simphony.engine.controller.DescriptorControllerAction;
import repast.simphony.engine.schedule.Descriptor;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * 
 * @author Jerry Vos
 */
public class DescriptorActionSaver implements ActionSaver<DescriptorControllerAction> {
	public void save(XStream xstream, DescriptorControllerAction action, String filename) throws IOException {
		Descriptor descriptor = action.getDescriptor();
		xstream.toXML(descriptor, new FileWriter(filename));
	}
}
