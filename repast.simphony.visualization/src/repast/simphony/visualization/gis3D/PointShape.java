package repast.simphony.visualization.gis3D;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.render.DrawContext;

/**
 * Superclass of all point shapes represented by a single coordinate.
 * 
 * @author Eric Tatara
 *
 */
public class PointShape extends RenderableShape {
	
	protected float size;
	public LatLon latlon;
	
	public PointShape(Geometry geometry){
		super();
		this.geometry=geometry;
	}
	
	public PointShape(){
		super();
	}
	
	@Override
	protected void doRender(DrawContext dc){
		
		if (geometry == null) return;
		
		LatLon latlon = WWUtils.CoordToLatLon(geometry.getCoordinate());
		
		// compute the elevation at the latlon.
		double elevation = dc.getGlobe().getElevation(latlon.getLatitude(), 
				latlon.getLongitude());
		
		// the point location in 3D is the lat/lon at elevation.
		Vec4 center = dc.getGlobe().computePointFromPosition(latlon.getLatitude(), 
				latlon.getLongitude(), elevation + RenderableShape.HEIGHT_OFFSET);

		// scale the shape based on the view position so that scaled objects are
		// the same size independent of zoom.
		Vec4 eye = dc.getView().getCurrentEyePoint();
		double scale = size * eye.distanceTo3(center) / ZOOM_SCALE_FACTOR;
		
		dc.getView().pushReferenceCenter(dc, center);
		dc.getGL().glScaled(scale, scale, scale);

		// rotate the shape in x,y so that "up" is normal to the globe surface.
		dc.getGL().glRotated(latlon.getLongitude().degrees, 0, 1, 0);
		dc.getGL().glRotated(-latlon.getLatitude().degrees, 1, 0, 0);
		
		// call the GenList defined in the subclass.
		dc.getGL().glCallList(glListId);
		dc.getView().popReferenceCenter(dc);
	}
}