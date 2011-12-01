package repast.simphony.context.space.physics;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousAdder;
import repast.simphony.space.continuous.PointTranslator;
import repast.simphony.space.physics.PhysicsSpace;

public interface PhysicsSpaceFactory {
	public <T> PhysicsSpace<T> createPhysicsSpace(String name, Context<T> context, 
			ContinuousAdder<T> adder, PointTranslator translator, double... size);
	
	public <T> PhysicsSpace<T> createPhysicsSpace(String name, Context<T> context, 
			ContinuousAdder<T> adder, PointTranslator translator, double[] size, 
			double[] origin);
}
