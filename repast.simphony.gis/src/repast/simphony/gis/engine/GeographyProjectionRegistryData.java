package repast.simphony.gis.engine;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.dataLoader.engine.DataLoaderProjectionRegistryData;
import repast.simphony.dataLoader.engine.ProjectionBuilderFactory;
import repast.simphony.engine.environment.ProjectionRegistryData;
import repast.simphony.freezedry.FreezeDryer;
import repast.simphony.freezedry.freezedryers.proj.ProjectionDryer;
import repast.simphony.gis.dataLoader.GeographyProjectionBuilder;
import repast.simphony.gis.freezedry.GeographyProjectionDryer;
import repast.simphony.gis.freezedry.GeometryFreezeDryer;
import repast.simphony.gis.xml.GeographyConverter;
import repast.simphony.space.gis.Geography;
import repast.simphony.xml.AbstractConverter;

/**
 * ProjectionRegistryData implementation for the Geography projection.
 * 
 * @author Eric Tatara
 *
 */
public class GeographyProjectionRegistryData 
	implements ProjectionRegistryData<Geography<?>>, DataLoaderProjectionRegistryData{

	public static final String NAME = "geography";
	public static final String COVERAGE_NAME = "coverage layer";
	public static final Class<?> CLASS = Geography.class;
	
	public GeographyProjectionRegistryData(){
		
	}
	
	@Override
	public String getTypeName() {
		return NAME;
	}

	@Override
	public Class getInterface() {
		return CLASS;
	}
	
	@Override
	public ProjectionBuilderFactory getProjectionBuilderFactory() {
		return new GeographyProjectionBuilder();
	}

	@Override
	public boolean isProjectionAttribute(String attributeId) {
		return false;
	}

	@Override
	public ProjectionDryer<Geography<?>> getProjectionDryer() {
		return new GeographyProjectionDryer();
	}

	@Override
	public AbstractConverter getProjectionXMLConverter() {
		return new GeographyConverter();
	}

	@Override
	public List<FreezeDryer<?>> getFreezeDryers() {
		List<FreezeDryer<?>> freezedryers = new ArrayList<FreezeDryer<?>>();
		freezedryers.add(new GeometryFreezeDryer());
		
		return freezedryers;
	}
}