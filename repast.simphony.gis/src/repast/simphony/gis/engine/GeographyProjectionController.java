package repast.simphony.gis.engine;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ProjectionRegistry;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.gis.FeatureAgentFactoryFinder;

public class GeographyProjectionController implements ControllerAction {
	
	public GeographyProjectionController(){
		System.out.println("GeographyProjectionController()");
		
		ProjectionRegistry.addRegistryData(new GeographyProjectionRegistryData());
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