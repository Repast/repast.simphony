package repast.simphony.visualization.gisnew;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfaceEllipse;
import gov.nasa.worldwind.render.SurfacePolygon;
import gov.nasa.worldwind.render.SurfacePolyline;
import gov.nasa.worldwind.render.SurfaceShape;

import java.awt.Paint;
import java.util.List;

import repast.simphony.visualization.gis3D.WWUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class StyledSurfaceShapeLayer extends AbstractSurfaceLayer {

	StyleGIS style;

	public StyledSurfaceShapeLayer(String name, StyleGIS style, WorldWindowGLCanvas wwglCanvas) {

		if (style == null) 
			this.style = new DefaultStyleGIS();
		else
			this.style = style;
		
		init(name, wwglCanvas);
	}

	protected void applyUpdatesToShape(Object o) {
		GeoShape shape = getVisualItem(o);
	
		// update location
		// TODO update polygons and lines?
		
		LatLon pt = WWUtils.CoordToLatLon(geography.getGeometry(o).getCoordinate());
		
		Renderable renderable = shape.getRenderable();
	
		
		if (renderable instanceof SurfaceEllipse){
			if (!pt.equals(((SurfaceEllipse)renderable).getCenter())){
				((SurfaceEllipse)renderable).setCenter(pt);
			}
		}
		
		
//		Paint fill = style.getFillColor(o);
//		shape.setFill(fill);
		
		// TODO update the rest of the shape properties
		
		// TODO use shape attributes
	}
	
	protected GeoShape createVisualItem(Object o) {
		GeoShape shape = style.getShape(o);

		System.out.print("Creating visual item for " + o + " ");
		
		Renderable renderable = shape.getRenderable();
		
		SurfaceShape s = (SurfaceShape)renderable;

		if (style.getFeatureType(o) == GeoShape.FeatureType.POINT){
			LatLon pt = WWUtils.CoordToLatLon(geography.getGeometry(o).getCoordinate());	
		  ((SurfacePointShape)s).setLocation(pt);
		  
		  System.out.print(" POINT\n");
		}
		else if (style.getFeatureType(o) == GeoShape.FeatureType.LINE){
			List<LatLon> pts = WWUtils.CoordToLatLon(geography.getGeometry(o).getCoordinates());	
			((SurfacePolyline)s).setLocations(pts);
			System.out.print(" LINE\n");
		}
		else if (style.getFeatureType(o) == GeoShape.FeatureType.POLYGON){
			List<LatLon> pts = WWUtils.CoordToLatLon(geography.getGeometry(o).getCoordinates());	
			((SurfacePolygon)s).setLocations(pts);
			System.out.print(" POLYGON\n");
		}
		else{
			// TODO nothing?
		}
			

		ShapeAttributes attrs = s.getAttributes();

		attrs.setInteriorMaterial(new Material(style.getFillColor(o)));
		attrs.setInteriorOpacity(style.getFillOpacity(o));
		attrs.setOutlineMaterial(new Material(style.getBorderColor(o)));
		attrs.setOutlineOpacity(style.getBorderOpacity(o));
		
		// TODO
		attrs.setOutlineWidth(3);

		s.setAttributes(attrs);
		// TODO all of this
//		shape.setGeometry(geography.getGeometry(o));
//		shape.setIsScaled(style.isScaled(o));
//		renderable.setScale(style.getScale(o));

		visualItemMap.put(o, shape);

		return shape;
	}
	
	public void setStyle(StyleGIS style) {
		this.style = style;
	}

//	public Map<GeoShape, Object> getShapeToObjectMap() {
//		return this.shapeToObjectMap;
//	}
}