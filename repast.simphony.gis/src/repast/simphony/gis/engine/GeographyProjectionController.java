package repast.simphony.gis.engine;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ProjectionRegistry;
import repast.simphony.engine.environment.RunState;
import repast.simphony.freezedry.FreezeDryer;
import repast.simphony.gis.dataLoader.GeographyProjectionBuilder;
import repast.simphony.gis.freezedry.GeographyProjectionDryer;
import repast.simphony.gis.freezedry.GeometryFreezeDryer;
import repast.simphony.gis.xml.GeographyConverter;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.gis.FeatureAgentFactoryFinder;

public class GeographyProjectionController implements ControllerAction {
	
	public GeographyProjectionController(){
		System.out.println("GeographyProjectionController()");
		
		// TODO Projections: put all of this into the data constructor like with
		//       the viz and delete the setters from the interface.
		GeographyProjectionRegistryData data = new GeographyProjectionRegistryData();
		data.setProjectionDryer(new GeographyProjectionDryer());
		data.setProjectionXMLConverter(new GeographyConverter());
		data.setProjectionBuilderFactory(new GeographyProjectionBuilder());
		
		List<FreezeDryer<?>> freezedryers = new ArrayList<FreezeDryer<?>>();
		freezedryers.add(new GeometryFreezeDryer());
		data.setFreezeDryers(freezedryers);
		
		ProjectionRegistry.addRegistryData(data);
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