package repast.simphony.scenario;

import java.io.IOException;

import repast.simphony.engine.environment.ControllerAction;

import com.thoughtworks.xstream.XStream;

/**
 * Interface for classes that can persist ControllerActions.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:11:55 $
 */
public interface ActionSaver<T extends ControllerAction> {

	void save(XStream xstream, T action, String filename) throws IOException;

}
