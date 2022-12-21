package repast.simphony.visualization.gis3D;

import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfacePolygon;
import gov.nasa.worldwind.render.SurfacePolyline;
import gov.nasa.worldwind.render.SurfaceShape;
import repast.simphony.visualization.gis3D.style.SurfaceShapeStyle;

/**
 * Styled display layer for WorldWind display layers.
 * 
 * @author Eric Tatara
 *
 */
public class SurfaceShapeLayer extends AbstractRenderableLayer<SurfaceShapeStyle,SurfaceShape> {

  public SurfaceShapeLayer(String name, SurfaceShapeStyle<?> style) {
    super(name, style);
  }

  @Override
  protected void applyUpdatesToShape(Object obj) {
  	Geometry geom = geography.getGeometry(obj);
  	if (geom == null) return;
  	
  	SurfaceShape shape = getVisualItem(obj);

    // TODO GIS Test for code hot spots here
    
    // TODO GIS [blocker] do a check on points and only update when new
    if (shape instanceof SurfacePolygon){
    	SurfacePolygon polygonShape = (SurfacePolygon)shape;
    	
    	// TODO type checking
    	
    	// TODO if geography projection CRS is not WGS84, reproject the geoms
//    	Polygon p = (Polygon)WWUtils.projectGeometryToWGS84(geom, geography.getCRS());
    	
    	Polygon p = (Polygon)geom;
    	    	
    	List<LatLon> pts = WWUtils.CoordToLatLon(p.getExteriorRing().getCoordinates());
    	
    	// Set the outer polygon boundary
    	Iterable<? extends LatLon> currentLocations = polygonShape.getLocations();
    	if (currentLocations == null){
    		polygonShape.setLocations(pts);
    	}
    	else if (!currentLocations.equals(pts)){
    		polygonShape.setLocations(pts);
    	}
  		// Set inner polygon rings if any
  		int numInterRings = p.getNumInteriorRing();
  		for (int i=0; i<numInterRings; i++){
  			List<LatLon> internalPts = WWUtils.CoordToLatLon(p.getInteriorRingN(i).getCoordinates());
  			polygonShape.addInnerBoundary(internalPts);
  		}
    }
    else if (shape instanceof SurfacePolyline){
    	SurfacePolyline line = (SurfacePolyline)shape;
    	List<LatLon> pts = WWUtils.CoordToLatLon(geom.getCoordinates());
    	
    	Iterable<? extends LatLon> currentLocations = line.getLocations();
    	if (currentLocations == null){
    		line.setLocations(pts);
    	}
    	else if (!currentLocations.equals(pts)){
    		line.setLocations(pts);
    	}
    }
    else {  // TODO WWJ - How to handle other types?
    	
    }
    
    ShapeAttributes attrs = shape.getAttributes();
    
    if (attrs == null)
      attrs = new BasicShapeAttributes();
    
    if (style.getFillColor(obj) != null)
      attrs.setInteriorMaterial(new Material(style.getFillColor(obj)));
    attrs.setInteriorOpacity(style.getFillOpacity(obj));
    attrs.setOutlineMaterial(new Material(style.getLineColor(obj)));
    attrs.setOutlineOpacity(style.getLineOpacity(obj));
    attrs.setOutlineWidth(style.getLineWidth(obj));
    
    // TODO WWJ Labels - use a WWJ PointPlacemark to hold the label?
    
    shape.setAttributes(attrs);
  }
 
  @Override
  protected SurfaceShape createVisualItem(Object o) {
  	Geometry geom = geography.getGeometry(o);
  	if (geom == null) return null;
  	
  	SurfaceShape shape = style.getSurfaceShape(o,null);
  	 
    visualItemMap.put(o, shape);

    return shape;
  }
}