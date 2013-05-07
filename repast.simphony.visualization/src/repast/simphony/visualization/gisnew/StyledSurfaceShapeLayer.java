package repast.simphony.visualization.gisnew;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfacePolygon;
import gov.nasa.worldwind.render.SurfacePolyline;
import gov.nasa.worldwind.render.SurfaceShape;

import java.awt.BasicStroke;
import java.util.List;

import repast.simphony.visualization.LayoutUpdater;
import repast.simphony.visualization.gis3D.WWUtils;

/**
 * Styled display layer for WorldWind display layers.
 * 
 * @author Eric Tatara
 *
 */
public class StyledSurfaceShapeLayer extends AbstractSurfaceLayer<StyleGIS> {

  public StyledSurfaceShapeLayer(String name, StyleGIS<?> style) {
    super(name, style);
  }

  protected void applyUpdatesToShape(Object obj) {
    GeoShape shape = getVisualItem(obj);

    // TODO WWJ Test for code hot spots here
    // TODO WWJ Refactor this method and createVisualItem since they are similar

    // update location
    SurfaceShape renderable = (SurfaceShape)shape.getRenderable();

    // For the special case of surface point shapes, we update the shape center
    //  location and shape implementation
    if (renderable instanceof SurfacePointShape) {
    	SurfacePointShape rend = (SurfacePointShape)renderable;
      LatLon pt = WWUtils.CoordToLatLon(geography.getGeometry(obj).getCoordinate());
    	// Update the renderable if a new type is returned from the style
    	GeoShape styleShape = style.getShape(obj);
    	if (!styleShape.getRenderable().getClass().equals(rend.getClass())){
    		shape.setRenderable(styleShape.getRenderable());
    	}
    	
    	// Update the center lat/lon if the shape has moved
    	if (!pt.equals(((SurfacePointShape) renderable).getCenter())) {
        ((SurfacePointShape) renderable).setCenter(pt);
      }
    	
    	rend.setSize(style.getScale(obj));
    }
    else if (renderable instanceof SurfacePolygon){
    	SurfacePolygon polygon = (SurfacePolygon)renderable;
    	List<LatLon> pts = WWUtils.CoordToLatLon(geography.getGeometry(obj).getCoordinates());
    	polygon.setLocations(pts);
    }
    else if (renderable instanceof SurfacePolyline){
    	SurfacePolyline line = (SurfacePolyline)renderable;
    	List<LatLon> pts = WWUtils.CoordToLatLon(geography.getGeometry(obj).getCoordinates());
    	line.setLocations(pts);
    }
    else {  // TODO WWJ - How to handle other types?
    	
    }
    
    ShapeAttributes attrs = renderable.getAttributes();
    
    if (attrs == null)
      attrs = new BasicShapeAttributes();
    
    attrs.setInteriorMaterial(new Material(style.getFillColor(obj)));
    attrs.setInteriorOpacity(style.getFillOpacity(obj));
    attrs.setOutlineMaterial(new Material(style.getBorderColor(obj)));
    attrs.setOutlineOpacity(style.getBorderOpacity(obj));
    attrs.setOutlineWidth(style.getBorderWidth(obj));
    
    renderable.setAttributes(attrs);
  }

  protected GeoShape createVisualItem(Object o) {
    GeoShape shape = style.getShape(o);
    SurfaceShape renderable = (SurfaceShape)shape.getRenderable();
    
    if (renderable instanceof SurfacePointShape){
    	SurfacePointShape rend = (SurfacePointShape)renderable;
      LatLon pt = WWUtils.CoordToLatLon(geography.getGeometry(o).getCoordinate());
      rend.setCenter(pt);
      rend.setSize(style.getScale(o));
    } 
    else if (renderable instanceof SurfacePolyline) {
    	SurfacePolyline line = (SurfacePolyline)renderable;
    	List<LatLon> pts = WWUtils.CoordToLatLon(geography.getGeometry(o).getCoordinates());
      line.setLocations(pts);
    } 
    else if (renderable instanceof SurfacePolygon){
    	SurfacePolygon polygon = (SurfacePolygon)renderable;
      List<LatLon> pts = WWUtils.CoordToLatLon(geography.getGeometry(o).getCoordinates());
    	polygon.setLocations(pts);
    } else {
      // TODO WWJ - Do we need a special NULL object for this case?
    }
    
    ShapeAttributes attrs = renderable.getAttributes();

    if (attrs == null)
      attrs = new BasicShapeAttributes();

    attrs.setInteriorMaterial(new Material(style.getFillColor(o)));
    attrs.setInteriorOpacity(style.getFillOpacity(o));
    attrs.setOutlineMaterial(new Material(style.getBorderColor(o)));
    attrs.setOutlineOpacity(style.getBorderOpacity(o));
    attrs.setOutlineWidth(style.getBorderWidth(o));

    renderable.setAttributes(attrs);
    // TODO WWJ - all of this
    // shape.setGeometry(geography.getGeometry(o));
    // shape.setIsScaled(style.isScaled(o));
    // renderable.setScale(style.getScale(o));

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
    	GeoShape shape = createVisualItem(o);
    	renderableToObjectMap.put(shape.getRenderable(), o);
    	addRenderable(shape.getRenderable());
    }
    addedObjects.clear();
  }

  protected void processRemovedObjects() {
    for (Object o : removeObjects) {
      GeoShape shape = visualItemMap.remove(o);
      if (shape != null) {
        removeRenderable(shape.getRenderable());
        renderableToObjectMap.remove(shape.getRenderable());
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