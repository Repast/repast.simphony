package repast.simphony.context.space.physics;

import java.util.Map;

public class PhysicsSpaceFactoryFinder {

	public static PhysicsSpaceFactory createPhysicsSpaceFactory(
			Map<String, Object> hints) {
		return new DefaultPhysicsSpaceFactory();
	}

}
