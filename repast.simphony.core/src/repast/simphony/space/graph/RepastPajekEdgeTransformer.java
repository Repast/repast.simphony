package repast.simphony.space.graph;

import org.apache.commons.collections15.Transformer;

public class RepastPajekEdgeTransformer implements Transformer {

	public Object transform(Object obj) {
		if (obj instanceof RepastEdge) {
			RepastEdge repastEdge = (RepastEdge) obj;
			return repastEdge.getWeight();
		} else {
			return 0.0;
		}
	}

}
