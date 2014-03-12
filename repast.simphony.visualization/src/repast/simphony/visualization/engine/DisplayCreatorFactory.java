package repast.simphony.visualization.engine;

import repast.simphony.context.Context;

public interface DisplayCreatorFactory {
	
	public DisplayCreator createDisplayCreator(Context<?> context, DisplayDescriptor descriptor);

}
