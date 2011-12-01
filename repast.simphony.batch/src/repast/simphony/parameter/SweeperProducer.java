package repast.simphony.parameter;

import java.io.IOException;

import repast.simphony.engine.environment.ControllerRegistry;

/**
 * Interface for classes that produce a ParameterTreeSweeper and its
 * concommitant Parameters.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface SweeperProducer {

	/**
	 * Gets the ParameterTreeSweeper produced by this SweeperProducer.
	 *
	 * @return the ParameterTreeSweeper produced by this SweeperProducer.
	 */
	ParameterTreeSweeper getParameterSweeper() throws IOException;

	/**
	 * Gets the Parameters produced by this SweeperProducer.
	 *
	 * @return the ParameterTreeSweeper produced by this SweeperProducer.
	 */
	Parameters getParameters() throws IOException;

	/**
	 * Initializes the producer with the controller registry and
	 * master context id. 
	 *
	 * @param registry
	 * @param masterContextId
	 */
	void init(ControllerRegistry registry, Object masterContextId);
}
