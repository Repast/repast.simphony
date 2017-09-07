package repast.simphony.gis.visualization.engine;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.visualization.engine.BasicDisplayDescriptor;
import repast.simphony.visualization.engine.DisplayDescriptor;

/**
 * Display descriptor for GIS displays.
 * 
 * @author Eric Tatara
 *
 * TODO GIS Network style (included with BasicDisplayDescriptor ?)
 */
public class GISDisplayDescriptor extends BasicDisplayDescriptor {

	public static enum VIEW_TYPE {FLAT("Flat"), GLOBE("Globe");
		String name;

		VIEW_TYPE(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
		}
		
		@Override
		public String toString(){
			return name;
		}
	}

	/**
	 * The view type determines how the map is displayed, eg flat or round globe.
	 */
	private VIEW_TYPE viewType = VIEW_TYPE.FLAT;
	
	/**
	 * If true, the display will zoom extent to always keep all agents in view.
	 */
	private boolean trackAgents = false;

	/**
	 * Map of <file name, style> for static coverages.  Style can be null.
	 *
	 */
	protected Map<String,String> staticCoverages = new HashMap<String,String>();
	
	/**
	 * Map of <coverage name, style> for dynamic coverages.  Style can be null.
	 *
	 */
	protected Map<String,String> coverageLayers = new HashMap<String,String>();
	
  // TODO WWJ - handle multiple styles
//  private static Class<?>[] stylesGIS3D = new Class<?>[] { DefaultMarkStyle.class,
//      DefaultSurfaceShapeStyle.class };
	
	private static final long serialVersionUID = 8349240641245552328L;
	
	public GISDisplayDescriptor(DisplayDescriptor descriptor) {
    super(descriptor.getName());
    set(descriptor);    
  }

  public GISDisplayDescriptor(String name) {
    super(name);
  }

  @Override
  public void set(DisplayDescriptor descriptor) {
  	super.set(descriptor);
  	
  	getLayerOrders().clear();
  	 	
  	if (descriptor.agentClassLayerOrders() != null) {
  		for (String name : descriptor.agentClassLayerOrders()) {
  			addLayerOrder(name, descriptor.getLayerOrder(name));
  		}
  	}
  	
     // For backwards compatibility with DefaultDisplayDescriptor, check if setting
    //  new GIS specific settings is appropriate.
    if (descriptor instanceof GISDisplayDescriptor){
    	setViewType(((GISDisplayDescriptor)descriptor).getViewType());
    	setTrackAgents(((GISDisplayDescriptor)descriptor).getTrackAgents());
    	
    	Map<String,String> coverageMap = ((GISDisplayDescriptor)descriptor).getStaticCoverageMap();
    	
    	staticCoverages.clear();
    	for (String fileName : coverageMap.keySet()) {
    		addStaticCoverage(fileName, coverageMap.get(fileName));
    	}
    	
    	coverageLayers.clear();
    	for (String name : coverageLayers.keySet()) {
    		addCoverageLayer(name, coverageLayers.get(name));
    	}
    }
  }

  @Override
	public DisplayDescriptor makeCopy() {
  	return new GISDisplayDescriptor(this);
	}
  
	public VIEW_TYPE getViewType() {
		return viewType;
	}

	public void setViewType(VIEW_TYPE viewType) {
		this.viewType = viewType;
		scs.fireScenarioChanged(this, "viewType");
	}

	public boolean getTrackAgents() {
		return trackAgents;
	}

	public void setTrackAgents(boolean trackAgents) {
		this.trackAgents = trackAgents;
		scs.fireScenarioChanged(this, "trackAgents");
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 * @return a Map of static coverages <filename, style>
	 */
	public Map<String, String> getStaticCoverageMap() {
		if (staticCoverages == null)
			staticCoverages = new HashMap<String,String>();
		
		return staticCoverages;
	}

	public void addStaticCoverage(String fileName, String style) {
		if (staticCoverages == null)
			staticCoverages = new HashMap<String,String>();
		
		staticCoverages.put(fileName, style);
		scs.fireScenarioChanged(this, "staticCoverage");
	}
	
	/**
	 * 
	 * @return a Map of dynamic coverages <name, style>
	 */
	public Map<String, String> getCoverageLayers() {
		if (coverageLayers == null)
			coverageLayers = new HashMap<String,String>();
		
		return coverageLayers;
	}

	public void addCoverageLayer(String name, String style) {
		if (coverageLayers == null)
			coverageLayers = new HashMap<String,String>();
		
		coverageLayers.put(name, style);
		scs.fireScenarioChanged(this, "coverageLayers");
	}
}