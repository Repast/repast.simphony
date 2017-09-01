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
	
	/**
	 * Map of tatic raster layer FILES loaded directly to the display 
	 *   <file name, style> for static coverages
	 *
	 *  style can be null
	 */
	protected Map<String,String> staticCoverageMap = new HashMap<String,String>();

	
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
}