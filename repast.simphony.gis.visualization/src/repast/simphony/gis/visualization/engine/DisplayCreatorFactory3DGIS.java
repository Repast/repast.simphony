package repast.simphony.gis.visualization.engine;

import repast.simphony.context.Context;
import repast.simphony.visualization.engine.DisplayCreator;
import repast.simphony.visualization.engine.DisplayCreatorFactory;
import repast.simphony.visualization.engine.DisplayDescriptor;

public class DisplayCreatorFactory3DGIS implements DisplayCreatorFactory {

	@Override
	public DisplayCreator createDisplayCreator(Context<?> context,
			DisplayDescriptor descriptor) {
		
		return new DisplayCreator3DGIS(context, descriptor);
	}
}