package repast.simphony.gis.visualization.engine;

import repast.simphony.visualization.engine.AbstractVisualizationRegistryData;

public class GIS3DVisualizationRegistryData extends AbstractVisualizationRegistryData {

	public static final String TYPE = "GIS3D";
	
	public GIS3DVisualizationRegistryData() {
		super(TYPE, new DisplayCreatorFactory3DGIS());
	}
}