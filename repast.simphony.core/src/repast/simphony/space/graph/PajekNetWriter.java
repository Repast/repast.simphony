package repast.simphony.space.graph;

import java.io.IOException;

import simphony.util.messages.MessageCenter;
import edu.uci.ics.jung.graph.Graph;

public class PajekNetWriter {

	private static MessageCenter LOG = MessageCenter
			.getMessageCenter(ORANetWriter.class);

	public void save(Graph network, String fileName) {
		
		edu.uci.ics.jung.io.PajekNetWriter writer = new edu.uci.ics.jung.io.PajekNetWriter();

		try {
			writer.save(network, fileName,
					new RepastPajekVertexTransformer(),
					new RepastPajekEdgeTransformer());
		} catch (IOException e) {
			LOG.error("Pajek export file \"" + fileName + "\" could not be written: ", e);
		}
		
	}

}
