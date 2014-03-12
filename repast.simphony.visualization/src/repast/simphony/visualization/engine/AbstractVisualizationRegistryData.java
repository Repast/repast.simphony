package repast.simphony.visualization.engine;

public abstract class AbstractVisualizationRegistryData implements
		VisualizationRegistryData {

	protected String type;
	protected DisplayCreatorFactory displayCreatorFactory;
	
	public AbstractVisualizationRegistryData(String type, DisplayCreatorFactory displayCreatorFactory){
		this.type = type;
		this.displayCreatorFactory = displayCreatorFactory;
	}
	
	@Override
	public String getVisualizationType() {
		return type;
	}

	@Override
	public DisplayCreatorFactory getDisplayCreatorFactory() {
		return displayCreatorFactory;
	}
}