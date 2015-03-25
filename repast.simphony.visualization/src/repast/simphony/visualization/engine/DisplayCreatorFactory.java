package repast.simphony.visualization.engine;

import repast.simphony.context.Context;

public interface DisplayCreatorFactory<T> {
	
	public DisplayCreator createDisplayCreator(Context<?> context, T descriptor);

}
