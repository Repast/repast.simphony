package repast.simphony.gis.visualization.engine;

import repast.simphony.context.Context;
import repast.simphony.visualization.engine.DisplayCreator;
import repast.simphony.visualization.engine.DisplayCreatorFactory;

public class DisplayCreatorFactory3DGIS implements DisplayCreatorFactory<GISDisplayDescriptor> {

	@Override
	public DisplayCreator createDisplayCreator(Context context,	GISDisplayDescriptor descriptor) {
		
		return new DisplayCreator3DGIS(context, descriptor);
	}
}