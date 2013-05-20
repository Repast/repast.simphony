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

  protected void applyUpdatesToShape(Object obj) {
  	SurfaceShape shape = getVisualItem(obj);

    // TODO WWJ Test for code hot spots here
    // TODO WWJ Refactor this method and createVisualItem since they are similar

    // update location
   
    
    // TODO WWJ [blocker] do a check on points and only update when new
    if (shape instanceof SurfacePolygon){
    	SurfacePolygon polygon = (SurfacePolygon)shape;
    	List<LatLon> pts = WWUtils.CoordToLatLon(geography.getGeometry(obj).getCoordinates());
    	
    	if (!polygon.getLocations().equals(pts)){
    	  polygon.setLocations(pts);
    	  System.out.println("points dont match.");
    	}
    }
    else if (shape instanceof SurfacePolyline){
    	SurfacePolyline line = (SurfacePolyline)shape;
    	List<LatLon> pts = WWUtils.CoordToLatLon(geography.getGeometry(obj).getCoordinates());
    	
    	if (!line.getLocations().equals(pts)){
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

  protected SurfaceShape createVisualItem(Object o) {
  	SurfaceShape shape = style.getSurfaceShape(o,null);
  	 
  	// TODO WWJ - any need to refactor this further to separate polygon and line layers?
    if (shape instanceof SurfacePolyline) {
    	SurfacePolyline line = (SurfacePolyline)shape;
    	List<LatLon> pts = WWUtils.CoordToLatLon(geography.getGeometry(o).getCoordinates());
      line.setLocations(pts);
    } 
    else if (shape instanceof SurfacePolygon){
    	SurfacePolygon polygon = (SurfacePolygon)shape;
      List<LatLon> pts = WWUtils.CoordToLatLon(geography.getGeometry(o).getCoordinates());
    	polygon.setLocations(pts);
    } else {
      // TODO WWJ - Do we need a special NULL object for this case?
    }
    
    ShapeAttributes attrs = shape.getAttributes();

    if (attrs == null)
      attrs = new BasicShapeAttributes();

    if (style.getFillColor(o) != null)
      attrs.setInteriorMaterial(new Material(style.getFillColor(o)));
    attrs.setInteriorOpacity(style.getFillOpacity(o));
    attrs.setOutlineMaterial(new Material(style.getLineColor(o)));
    attrs.setOutlineOpacity(style.getLineOpacity(o));
    attrs.setOutlineWidth(style.getLineWidth(o));

    shape.setAttributes(attrs);
   
    visualItemMap.put(o, shape);

    return shape;
  }
  
  protected void updateExistingObjects(LayoutUpdater updater){
  	for (Object o : visualItemMap.keySet()){
  		applyUpdatesToShape(o);
  	}
  }
  
  protected void processAddedObjects() {
    for (Object o : addedObjects) {
    	SurfaceShape shape = createVisualItem(o);
    	renderableToObjectMap.put(shape, o);
    	addRenderable(shape);
    }
    addedObjects.clear();
  }

  protected void processRemovedObjects() {
    for (Object o : removeObjects) {
      SurfaceShape shape = visualItemMap.remove(o);
      if (shape != null) {
        removeRenderable(shape);
        renderableToObjectMap.remove(shape);
      }
    }
    removeObjects.clear();
  }

  
  /**
   * Updates the displayed nodes by applying styles etc. The display is not
   * updated to reflect these changes.
   */
  public void update(LayoutUpdater updater) {
    // remove what needs to be removed
    processRemovedObjects();
    updateExistingObjects(updater);
    processAddedObjects();
    
    firePropertyChange(AVKey.LAYER, null, this);
  }
}