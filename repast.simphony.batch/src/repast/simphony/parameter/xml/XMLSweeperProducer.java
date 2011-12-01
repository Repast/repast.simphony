package repast.simphony.parameter.xml;

import java.net.URL;

/**
 * A SweeperProducer that produces a sweeper based on an XML
 * parameter file.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class XMLSweeperProducer extends AbstractXMLSweeperProducer  {

	/**
	 * Creates an XMLSweeperProducer from the specified parameter file
	 *
	 * @param paramsURL
	 */
	public XMLSweeperProducer(URL paramsURL) {
		parser = new ParameterSweepParser(paramsURL);
	}

	public ParameterSweepParser getParser() {
		return parser;
	}
}
