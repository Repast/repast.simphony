package repast.simphony.parameter.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParameterTreeSweeper;
import repast.simphony.parameter.SweeperProducer;
import repast.simphony.util.collections.Pair;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class AbstractXMLSweeperProducer implements SweeperProducer {
	
        // NC: why is this now public?
	public ParameterSweepParser parser;
	protected ParameterTreeSweeper sweeper;
	protected Parameters params;

	/**
	 * Initializes the producer with the controller registry and
	 * master context id. This will set the parser and the sweeper
	 * back to null.
	 *
	 * @param registry
	 * @param masterContextId
	 */
	public void init(ControllerRegistry registry, Object masterContextId) {
	  params = null;
	  sweeper = null;
	}

	/**
	 * Gets the Parameters produced by this SweeperProducer.
	 *
	 * @return the ParameterTreeSweeper produced by this SweeperProducer.
	 */
	public Parameters getParameters() throws IOException {
		if (params == null) {
			parse();
		}
		return params;
	}

	protected void parse() throws IOException {
		try {
			Pair<Parameters, ParameterTreeSweeper> pair = parser.parse();
			sweeper = pair.getSecond();
			params = pair.getFirst();
		} catch (ParserConfigurationException e) {
			IOException ex = new IOException("Error reading parameter file");
			ex.initCause(e);
			throw ex;
		} catch (SAXException e) {
			IOException ex = new IOException("Error reading parameter file");
			ex.initCause(e);
			throw ex;
		}
	}

	/**
	 * Gets the ParameterTreeSweeper produced by this SweeperProducer.
	 *
	 * @return the ParameterTreeSweeper produced by this SweeperProducer.
	 */
	public ParameterTreeSweeper getParameterSweeper() throws IOException {
		if (sweeper == null) parse();
		return sweeper;
	}
}
