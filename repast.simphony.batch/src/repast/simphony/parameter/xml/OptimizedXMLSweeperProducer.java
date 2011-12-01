package repast.simphony.parameter.xml;

import java.net.URL;

import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.parameter.optimizer.*;
import repast.simphony.parameter.RunResultProducer;

/**
 * A SweeperProducer that produces an optimizing sweeper based on an XML
 * parameter file and a RunResultProducer.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class OptimizedXMLSweeperProducer extends AbstractXMLSweeperProducer {

	private URL paramsURL;
	private RunResultProducer producer;
	private AdvancementChooser chooser = new OptimizedParameterSweeper.DefaultAdvanceChooser();

	/**
	 * Creates an OptimizedXMLSweeperProducer from the specified parameter file
	 * and RunResultProducer.
	 *
	 * @param producer
	 * @param paramsURL
	 */
	public OptimizedXMLSweeperProducer(RunResultProducer producer, URL paramsURL) {
		this.paramsURL = paramsURL;
		this.producer = producer;
	}

	/**
	 * Creates an OptimizedXMLSweeperProducer from the specified parameter file,
	 * RunResultProducer, and AdvancementChooser.
	 *
	 * @param producer
	 * @param chooser
	 * @param paramsURL
	 */
	public OptimizedXMLSweeperProducer(RunResultProducer producer, AdvancementChooser chooser,
	                                   URL paramsURL) {
		this.paramsURL = paramsURL;
		this.producer = producer;
		this.chooser = chooser;
	}

	/**
	 * Initializes the producer with the controller registry and
	 * master context id.
	 *
	 * @param registry
	 * @param masterContextId
	 */
	public void init(ControllerRegistry registry, Object masterContextId) {
		OptimizedParameterSweeper sweeper = new OptimizedParameterSweeper(registry, masterContextId);
		sweeper.setAdvancementChooser(chooser);
		sweeper.setResultProducer(producer);
		parser = new ParameterSweepParser(sweeper, paramsURL);
	}
}
