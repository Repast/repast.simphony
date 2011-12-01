package repast.simphony.scenario;

import java.io.FileWriter;
import java.io.IOException;

import repast.simphony.engine.environment.ControllerAction;

import com.thoughtworks.xstream.XStream;

public class DefaultActionSaver<T extends ControllerAction> implements ActionSaver<T> {

	public DefaultActionSaver() {
	}

	public void save(XStream xstream, T action, String filename) throws IOException {
		xstream.toXML(action, new FileWriter(filename));
	}

}
