package repast.simphony.visualization.gisnew;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.render.SurfaceCircle;

public class SurfaceCircleShape extends SurfaceCircle implements SurfacePointShape {

	public SurfaceCircleShape(double radius){
		super();
		setRadius(radius);
	}
	
	public LatLon getLocation() {
		return getCenter();
	}
	
	public void setLocation(LatLon latlon) {
		setCenter(latlon);		
	}
}