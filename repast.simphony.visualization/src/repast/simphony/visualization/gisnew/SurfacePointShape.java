package repast.simphony.visualization.gisnew;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.render.Renderable;

public interface SurfacePointShape extends Renderable{

	public LatLon getLocation();
	
	public void setLocation(LatLon latlon);
	
}
