package repast.simphony.visualization.gisnew;

import gov.nasa.worldwind.render.SurfacePolygon;


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
	
	public static GeoShape createCircle(double radius){
		GeoShape shape = new GeoShape();
		
		SurfacePointShape c = new SurfaceCircleShape(radius);
				
		shape.setRenderable(c);
		
		return shape;
	}
	
	public static GeoShape createPolygon(){
		GeoShape shape = new GeoShape();
				
		shape.setRenderable(new SurfacePolygon());
		
		return shape;
	}
}
