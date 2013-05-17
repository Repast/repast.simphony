package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.render.Renderable;

public interface SurfacePointShape extends Renderable{

	public LatLon getCenter();
	
	public void setCenter(LatLon latlon);
	
	public double getSize();
	
	public void setSize(double size);
}
