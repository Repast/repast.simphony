package repast.simphony.context.space.continuous;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousAdder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.DefaultContinuousSpace;
import repast.simphony.space.continuous.PointTranslator;

public class DefaultContinuousSpaceFactory implements ContinuousSpaceFactory {

	public <T> ContinuousSpace<T> createContinuousSpace(String name,
			Context<T> context, ContinuousAdder<T> adder,
			PointTranslator translator, double... size) {
		DefaultContinuousSpace<T> space = new ContextSpace<T>(name,
				adder, translator, size);
		context.addProjection(space);
		return space;
	}
	
	public <T> ContinuousSpace<T> createContinuousSpace(String name,
			Context<T> context, ContinuousAdder<T> adder,
			PointTranslator translator, double[] size, double[] origin) {
		DefaultContinuousSpace<T> space = new ContextSpace<T>(name,
				adder, translator, size, origin);
		context.addProjection(space);
		return space;
	}
}
