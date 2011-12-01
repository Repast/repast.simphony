package repast.simphony.context.space.continuous;

import java.util.Map;


public class ContinuousSpaceFactoryFinder {

	public static ContinuousSpaceFactory createContinuousSpaceFactory(
			Map<String, Object> hints) {
		return new DefaultContinuousSpaceFactory();
	}

}
