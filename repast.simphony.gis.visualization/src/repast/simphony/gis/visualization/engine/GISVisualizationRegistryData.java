package repast.simphony.gis.visualization.engine;

import repast.simphony.visualization.engine.AbstractVisualizationRegistryData;

public class GISVisualizationRegistryData extends AbstractVisualizationRegistryData {

	public static final String TYPE = "GIS";
	
	public GISVisualizationRegistryData() {
		super(TYPE, new DisplayCreatorFactoryGIS());
	}
}
