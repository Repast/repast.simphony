package repast.simphony.gis.visualization.engine;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.visualization.DefaultDisplayData;

/**
 * Provides GIS Display used for initialization of the display.  The data here
 * is used primarily during the display.init().  
 * 
 * Other types of information can be set using the DisplayCreator. 
 *
 * @author Eric Tatara
 *
 */
public class GISDisplayData<T> extends DefaultDisplayData<T> {
	
	protected Map<String,Integer> layerOrders = new HashMap<String,Integer>();
	
	/**
	 * Map of static raster layer FILES loaded directly to the display 
	 *   <file name, style> for static coverages
	 *
	 *  style can be null
	 */
	protected Map<String,String> staticCoverageMap = new HashMap<String,String>();

	/**
	 * Globe layers are the default WWJ layers like the WMS background, stars,
	 * etc that can be optionally added to displays.
	 */
	protected Map<String,Boolean> globeLayers = new HashMap<String,Boolean>();
	
	// View type: FLAT or GLOBE
	protected GISDisplayDescriptor.VIEW_TYPE viewType;
	
	public GISDisplayData(Context<T> context) {
		super(context);
	}

	public GISDisplayDescriptor.VIEW_TYPE getViewType() {
		return viewType;
	}

	public void setViewType(GISDisplayDescriptor.VIEW_TYPE viewType) {
		this.viewType = viewType;
	}

	public Map<String, String> getStaticCoverageMap() {
		return staticCoverageMap;
	}

	public void addStaticCoverage(String fileName, String style) {
		staticCoverageMap.put(fileName, style);
	}

	public Map<String, Boolean> getGlobeLayers() {
		return globeLayers;
	}
	
	public void addGlobeLayer(String layerName, boolean enabled) {
		globeLayers.put(layerName, enabled);
	}

	public Map<String, Integer> getLayerOrders() {
		return layerOrders;
	}

	public void setLayerOrders(Map<String, Integer> layerOrders) {
		this.layerOrders = layerOrders;
	}

}