package repast.simphony.gis.engine;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ProjectionRegistry;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.Parameters;
import repast.simphony.plugin.ExtendablePluginClassLoader;
import repast.simphony.space.gis.FeatureAgentFactoryFinder;

/**
 * Geography Projection Data Controller used to init the Geography projection.
 * 
 * @author Eric Tatara
 *
 */
public class GeographyProjectionController implements ControllerAction {
	
	public GeographyProjectionController(){
		
		// Geography Projection
		ProjectionRegistry.addRegistryData(new GeographyProjectionRegistryData());
		
		// Initialize the JAI registry for JAI plugins used by GeoRasterLayer
		// Only init JAI when using the Repast ClassLoader, since the normal Java
		// ClassLoader with batch runs will pick them up automatically.
		if (this.getClass().getClassLoader() instanceof ExtendablePluginClassLoader){
			new JAIInitializer().initJAI();	
		}
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
		
		// TODO GIS Legacy - remove when legacy GIS displays no longer needed
		// Rest the FeatureAgent factory between runs
		FeatureAgentFactoryFinder.getInstance().clearAdapters();
	}

	@Override
	public void batchCleanup(RunState runState, Object contextId) {
	}
}