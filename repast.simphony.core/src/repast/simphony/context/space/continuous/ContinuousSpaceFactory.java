package repast.simphony.context.space.continuous;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousAdder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.PointTranslator;

public interface ContinuousSpaceFactory {
	public <T> ContinuousSpace<T> createContinuousSpace(String name,
	                                                    Context<T> context, ContinuousAdder<T> adder,
	                                                    PointTranslator translator, double... size);
	
	public <T> ContinuousSpace<T> createContinuousSpace(String name,
            Context<T> context, ContinuousAdder<T> adder,
            PointTranslator translator, double[] size, double[] origin);
}
