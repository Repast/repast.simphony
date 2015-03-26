package repast.simphony.gis.visualization.engine;

import repast.simphony.visualization.engine.BasicDisplayDescriptor;
import repast.simphony.visualization.engine.CartesianDisplayDescriptor;
import repast.simphony.visualization.engine.DisplayDescriptor;

/**
 * Display descriptor for GIS displays.
 * 
 * @author Eric Tatara
 *
 * TODO Projections: implement GIS-specific info like raster layers.
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
	private VIEW_TYPE viewType;
	
	/**
	 * If true, the display will zoom extent to always keep all agents in view.
	 */
	private boolean trackAgents = false;

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
    }
  }

  @Override
	public DisplayDescriptor makeCopy() {
  	return new GISDisplayDescriptor(this);
	}
  
	public VIEW_TYPE getViewType() {
		if (viewType == null)  // for backwards compatibility with older display descriptors
			return VIEW_TYPE.FLAT;
		
		else return viewType;
	}

	public void setViewType(VIEW_TYPE viewType) {
		this.viewType = viewType;
	}

	public boolean getTrackAgents() {
		return trackAgents;
	}

	public void setTrackAgents(boolean trackAgents) {
		this.trackAgents = trackAgents;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}