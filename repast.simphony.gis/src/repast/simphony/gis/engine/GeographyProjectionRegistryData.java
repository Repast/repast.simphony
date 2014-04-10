package repast.simphony.gis.engine;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.dataLoader.engine.DataLoaderProjectionRegistryData;
import repast.simphony.dataLoader.engine.ProjectionBuilderFactory;
import repast.simphony.engine.environment.AbstractProjectionRegistryData;
import repast.simphony.freezedry.FreezeDryer;
import repast.simphony.gis.dataLoader.GeographyProjectionBuilder;
import repast.simphony.gis.freezedry.GeographyProjectionDryer;
import repast.simphony.gis.freezedry.GeometryFreezeDryer;
import repast.simphony.gis.xml.GeographyConverter;
import repast.simphony.space.gis.Geography;

/**
 * ProjectionRegistryData implementation for the Geography projection.
 * 
 * @author Eric Tatara
 *
 */
public class GeographyProjectionRegistryData extends AbstractProjectionRegistryData<Geography<?>> 
		implements DataLoaderProjectionRegistryData{

	public static final String NAME = "geography";
	public static final Class<?> CLASS = Geography.class;
	
	protected ProjectionBuilderFactory projectionBuilderFactory;
	
	public GeographyProjectionRegistryData(){
		super(NAME,CLASS);
		
		// TODO Projections: remove setters from interface and set internally?
		// TODO Projections: Test error handling in code that reference these classes
		//        by disabling one at a time and running the appropriate runtime 
		//        components.
		setProjectionDryer(new GeographyProjectionDryer());
		setProjectionXMLConverter(new GeographyConverter());
		setProjectionBuilderFactory(new GeographyProjectionBuilder());
		
		List<FreezeDryer<?>> freezedryers = new ArrayList<FreezeDryer<?>>();
		freezedryers.add(new GeometryFreezeDryer());
		setFreezeDryers(freezedryers);
	}
	
	@Override
	public ProjectionBuilderFactory getProjectionBuilderFactory() {
		return projectionBuilderFactory;
	}

	@Override
	public void setProjectionBuilderFactory(ProjectionBuilderFactory projectionBuilderFactory) {
		this.projectionBuilderFactory = projectionBuilderFactory;
	}

	@Override
	public boolean isProjectionAttribute(String attributeId) {
		return false;
	}
}