package repast.simphony.context.space.physics;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousAdder;
import repast.simphony.space.continuous.PointTranslator;
import repast.simphony.space.physics.DefaultPhysicsSpace;
import repast.simphony.space.physics.PhysicsSpace;

public class DefaultPhysicsSpaceFactory implements PhysicsSpaceFactory {

	public <T> PhysicsSpace<T> createPhysicsSpace(String name,
			Context<T> context, ContinuousAdder<T> adder,
			PointTranslator translator, double xdim, double ydim, double zdim) {
		DefaultPhysicsSpace<T> space = new ContextPhysics<T>(name,
				adder, translator, xdim, ydim, zdim);
		context.addProjection(space);
		return space;
	}
	
	public <T> PhysicsSpace<T> createPhysicsSpace(String name,
			Context<T> context, ContinuousAdder<T> adder,
			PointTranslator translator, double... size) {
		DefaultPhysicsSpace<T> space = new ContextPhysics<T>(name,
				adder, translator, size[0], size[1], size[2]);
		context.addProjection(space);
		return space;
	}
	
	public <T> PhysicsSpace<T> createPhysicsSpace(String name,
			Context<T> context, ContinuousAdder<T> adder,
			PointTranslator translator, double[] size, double[] origin) {
		DefaultPhysicsSpace<T> space = new ContextPhysics<T>(name,
				adder, translator, size, origin);
		context.addProjection(space);
		return space;
	}
}
