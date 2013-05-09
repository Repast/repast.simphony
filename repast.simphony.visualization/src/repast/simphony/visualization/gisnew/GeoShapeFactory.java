package repast.simphony.visualization.gisnew;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.render.SurfaceCircle;
import gov.nasa.worldwind.render.SurfacePolygon;
import gov.nasa.worldwind.render.SurfaceSquare;


public class GeoShapeFactory {

//	public static GeoShape createCircle(){
//		GeoShape shape = new GeoShape();
//		
//		Renderable r;
//		
//		// test if the desired WWJ objects can be cast to Renderable
//		// SurfaceShapes
//		r = new SurfaceCircle();
//		r = new SurfaceEllipse();
//		r = new SurfaceSquare();
//		r = new SurfaceQuad();
//		r = new SurfacePolygon();
//		r = new SurfacePolyline();
//		
//		// Airspaces
//		r = new Curtain();
//		r = new Polygon();
//		r = new CappedCylinder();
//		r = new PartialCappedCylinder();
//		r = new Cake();
//		r = new Orbit();
//		r = new PolyArc();
//		r = new Route();
//		r = new TrackAirspace();
//		r = new SphereAirspace();
//		r = new Box();
//		
//		// misc
//		r = new ExtrudedPolygon();
//		r = new SurfaceImage(null,null);
//		
//		// TODO markers
//		Marker m;
//		m = new BasicMarker(null,null);
//		
//		shape.setRenderable(r);
//		
//		return shape;
//	}
	
	public static GeoShape createCircle(){
		GeoShape shape = new GeoShape();
		SurfacePointShape c = new SurfaceCircleShape();
		shape.setRenderable(c);
		return shape;
	}
	
	public static GeoShape createSquare(){
		GeoShape shape = new GeoShape();
		SurfacePointShape c = new SurfaceSquareShape();
		shape.setRenderable(c);
		return shape;
	}
	
	public static GeoShape createPolygon(){
		GeoShape shape = new GeoShape();	
		shape.setRenderable(new SurfacePolygon());
		return shape;
	}
	
	// TODO WWJ - add other types (square, cross, star, ...).
	
	public static class SurfaceCircleShape extends SurfaceCircle implements SurfacePointShape {

		public SurfaceCircleShape(){
			super();
			setRadius(1);
		}
		
		public LatLon getLocation() {
			return getCenter();
		}
		
		public void setLocation(LatLon latlon) {
			setCenter(latlon);		
		}

		public double getSize() {
			return 2*getRadius();
		}
		
		public void setSize(double size) {
			setRadius(size/2);
		}
	}
	
	public static class SurfaceSquareShape extends SurfaceSquare implements SurfacePointShape {

		public SurfaceSquareShape(){
			super();
			super.setSize(1);
		}
		
		public LatLon getLocation() {
			return getCenter();
		}
		
		public void setLocation(LatLon latlon) {
			setCenter(latlon);		
		}

		public double getSize() {
			return super.getSize();
		}
		
		public void setSize(double size) {
			super.setSize(size);
		}
	}
}
