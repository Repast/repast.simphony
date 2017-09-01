package repast.simphony.gis.visualization.engine;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.visualization.DefaultDisplayData;

/**
 * Provides GIS Display used for initialization of the display.  The data here
 * is used primarily for information that is required in the display class 
 * constructor.  Other types of information can be set using the DisplayCreator. 
 * 
 * TODO GIS much of this can be moved to the DisplayCreatorGIS3D since it takes
 * data directly from the descriptor and sets it in the display.
 * 
 * @author Eric Tatara
 *
 */
public class GISDisplayData<T> extends DefaultDisplayData<T> {
	
	// TODO GIS probably dont need dynamic layers here if we set them via displaycreator style map
	// List of dynamic raster layers that will be stored in the Geography
	protected List<String> rasterLayers = new ArrayList<String>();
	
	// List of static raster layer files loaded directly to the display
	protected List<String> staticRasterLayers = new ArrayList<String>();
	
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
	
	public void addRasterLayer(String name) {
		rasterLayers.add(name);
  }
	
	public void adStaticdRasterLayer(String fileName) {
		staticRasterLayers.add(fileName);
  }

	public List<String> getRasterLayers() {
		return rasterLayers;
	}

	public List<String> getStaticRasterLayers() {
		return staticRasterLayers;
	}
}