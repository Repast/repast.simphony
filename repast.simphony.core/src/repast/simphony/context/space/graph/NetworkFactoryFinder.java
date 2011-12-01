package repast.simphony.context.space.graph;

import java.util.Map;


public class NetworkFactoryFinder {

	public static NetworkFactory createNetworkFactory(Map<String, Object> hints) {
		return new DefaultNetworkFactory();
	}

}
