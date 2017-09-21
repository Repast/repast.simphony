package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfacePolygon;
import gov.nasa.worldwind.render.SurfacePolyline;
import gov.nasa.worldwind.render.SurfaceShape;

import java.util.List;

import repast.simphony.visualization.LayoutUpdater;
import repast.simphony.visualization.gis3D.style.SurfaceShapeStyle;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

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

    // TODO WWJ Test for code hot spots here
    // TODO WWJ Refactor this method and createVisualItem since they are similar

    
    // TODO WWJ [blocker] do a check on points and only update when new
    if (shape instanceof SurfacePolygon){
    	SurfacePolygon polygonShape = (SurfacePolygon)shape;
    	
    	// TODO type checking
    	
    	// TODO if geography projection CRS is not WGS84, reproject the geoms
//    	Polygon p = (Polygon)WWUtils.projectGeometryToWGS84(geom, geography.getCRS());
    	
    	Polygon p = (Polygon)geom;
    	    	
    	List<LatLon> pts = WWUtils.CoordToLatLon(p.getExteriorRing().getCoordinates());
    	
    	Iterable<? extends LatLon> currentLocations = polygonShape.getLocations();
    	if (currentLocations == null){
      	// Set the outer polygon boundary
    		polygonShape.setLocations(pts);
    	}
    	else if (!currentLocations.equals(pts)){
      	// Set the outer polygon boundary
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

  // TODO GIS do we even need to do most of the location and styling in create?
  
  @Override
  protected SurfaceShape createVisualItem(Object o) {
  	Geometry geom = geography.getGeometry(o);
  	if (geom == null) return null;
  	
  	SurfaceShape shape = style.getSurfaceShape(o,null);
  	 
  	// TODO WWJ - any need to refactor this further to separate polygon and line layers?
//    if (shape instanceof SurfacePolyline) {
//    	SurfacePolyline line = (SurfacePolyline)shape;
//    	List<LatLon> pts = WWUtils.CoordToLatLon(geom.getCoordinates());
//      line.setLocations(pts);
//    } 
//    else if (shape instanceof SurfacePolygon){
//    	// TODO interior holes as above
//    	
//    	SurfacePolygon polygon = (SurfacePolygon)shape;
//      List<LatLon> pts = WWUtils.CoordToLatLon(geom.getCoordinates());
//    	polygon.setLocations(pts);
//    } else {
//      // TODO WWJ - Do we need a special NULL object for this case?
//    }
    
//    ShapeAttributes attrs = shape.getAttributes();
//
//    if (attrs == null)
//      attrs = new BasicShapeAttributes();
//
//    if (style.getFillColor(o) != null)
//      attrs.setInteriorMaterial(new Material(style.getFillColor(o)));
//    attrs.setInteriorOpacity(style.getFillOpacity(o));
//    attrs.setOutlineMaterial(new Material(style.getLineColor(o)));
//    attrs.setOutlineOpacity(style.getLineOpacity(o));
//    attrs.setOutlineWidth(style.getLineWidth(o));
//
//    shape.setAttributes(attrs);
   
    visualItemMap.put(o, shape);

    return shape;
  }
}