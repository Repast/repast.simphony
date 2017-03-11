package repast.simphony.gis.engine;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.dataLoader.engine.DataLoaderProjectionRegistryData;
import repast.simphony.dataLoader.engine.ProjectionBuilderFactory;
import repast.simphony.engine.environment.ProjectionRegistryData;
import repast.simphony.freezedry.FreezeDryer;
import repast.simphony.freezedry.freezedryers.proj.ProjectionDryer;
import repast.simphony.space.gis.GeoRasterLayer;
import repast.simphony.space.gis.Geography;
import repast.simphony.xml.AbstractConverter;

/**
 * ProjectionRegistryData implementation for the GeoRasterLayer projection.
 * 
 * @author Eric Tatara
 *
 */
public class GeoRasterLayerProjectionRegistryData 
	implements ProjectionRegistryData<Geography<?>>, DataLoaderProjectionRegistryData{

	public static final String NAME = "georaster layer";
	public static final Class<?> CLASS = GeoRasterLayer.class;
	
	public GeoRasterLayerProjectionRegistryData(){
		
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
//		return new GeographyProjectionBuilder();
		
		// TODO GIS Raster
		
		return null;
	}

	@Override
	public boolean isProjectionAttribute(String attributeId) {
		return false;
	}

	@Override
	public ProjectionDryer<Geography<?>> getProjectionDryer() {
//		return new GeographyProjectionDryer();
		
	// TODO GIS Raster
		
		return null;
	}

	@Override
	public AbstractConverter getProjectionXMLConverter() {
//		return new GeographyConverter();
		
	// TODO GIS Raster
		
		return null;
	}

	@Override
	public List<FreezeDryer<?>> getFreezeDryers() {
		List<FreezeDryer<?>> freezedryers = new ArrayList<FreezeDryer<?>>();
		
//		freezedryers.add(new GeometryFreezeDryer());
		
	// TODO GIS Raster
		
		return freezedryers;
	}
}