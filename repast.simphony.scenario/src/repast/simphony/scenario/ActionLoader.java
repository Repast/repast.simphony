package repast.simphony.scenario;

import java.io.FileNotFoundException;

import repast.simphony.engine.environment.ControllerRegistry;

import com.thoughtworks.xstream.XStream;

/**
 * Creates an action from a serialized descriptor and adds the action to a registry.
 *
 * @author Nick Collier
 */
public interface ActionLoader {

	public void loadAction(XStream xstream, Scenario scenario, ControllerRegistry registry) throws FileNotFoundException;
}
