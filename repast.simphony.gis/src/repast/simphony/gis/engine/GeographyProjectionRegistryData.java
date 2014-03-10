package repast.simphony.gis.engine;

import repast.simphony.dataLoader.engine.DataLoaderProjectionRegistryData;
import repast.simphony.dataLoader.engine.ProjectionBuilderFactory;
import repast.simphony.engine.environment.AbstractProjectionRegistryData;
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