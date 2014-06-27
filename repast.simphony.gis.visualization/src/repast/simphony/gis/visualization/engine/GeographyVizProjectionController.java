package repast.simphony.gis.visualization.engine;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.gis.FeatureAgentFactoryFinder;
import repast.simphony.visualization.engine.VisualizationRegistry;

public class GeographyVizProjectionController implements ControllerAction {
	
	public GeographyVizProjectionController(){
		VisualizationRegistry.addRegistryData(new GISVisualizationRegistryData());
		VisualizationRegistry.addRegistryData(new GIS3DVisualizationRegistryData());
	}
	
	@Override
	public void batchInitialize(RunState runState, Object contextId) {
	}

	@Override
	public void runInitialize(RunState runState, Object contextId,
			Parameters runParams) {
	}

	@Override
	public void runCleanup(RunState runState, Object contextId) {
		FeatureAgentFactoryFinder.getInstance().clearAdapters();
	}

	@Override
	public void batchCleanup(RunState runState, Object contextId) {
	}
}