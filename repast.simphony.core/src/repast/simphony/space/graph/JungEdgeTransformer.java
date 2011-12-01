package repast.simphony.space.graph;

import org.apache.commons.collections15.Transformer;

/**
 * @author Eric Tatara
 *
 * Transforms a RepastEdge into a weight  
 *
 * @param <T>
 */
public class JungEdgeTransformer<T> implements Transformer<RepastEdge<T>,Double> {

	public Double transform(RepastEdge<T> edge) {

		return edge.getWeight();
	}

}
