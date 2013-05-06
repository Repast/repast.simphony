package repast.simphony.visualization.gisnew;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfaceEllipse;
import gov.nasa.worldwind.render.SurfacePolygon;
import gov.nasa.worldwind.render.SurfacePolyline;
import gov.nasa.worldwind.render.SurfaceShape;

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

  protected void applyUpdatesToShape(Object o) {
    GeoShape shape = getVisualItem(o);

    // update location
    // TODO update polygons and lines?

    LatLon pt = WWUtils.CoordToLatLon(geography.getGeometry(o).getCoordinate());

    Renderable renderable = shape.getRenderable();

    if (renderable instanceof SurfaceEllipse) {
      if (!pt.equals(((SurfaceEllipse) renderable).getCenter())) {
        ((SurfaceEllipse) renderable).setCenter(pt);
      }
    }

    // Paint fill = style.getFillColor(o);
    // shape.setFill(fill);

    // TODO WWJ - update the rest of the shape properties

    // TODO WWJ - use shape attributes
  }

  protected GeoShape createVisualItem(Object o) {
    GeoShape shape = style.getShape(o);

    Renderable renderable = shape.getRenderable();

    SurfaceShape s = (SurfaceShape) renderable;

    if (style.getFeatureType(o) == GeoShape.FeatureType.POINT) {
      LatLon pt = WWUtils.CoordToLatLon(geography.getGeometry(o).getCoordinate());
      ((SurfacePointShape) s).setLocation(pt);
    } 
    else if (style.getFeatureType(o) == GeoShape.FeatureType.LINE) {
      List<LatLon> pts = WWUtils.CoordToLatLon(geography.getGeometry(o).getCoordinates());
      ((SurfacePolyline) s).setLocations(pts);
    } 
    else if (style.getFeatureType(o) == GeoShape.FeatureType.POLYGON) {
      List<LatLon> pts = WWUtils.CoordToLatLon(geography.getGeometry(o).getCoordinates());
      ((SurfacePolygon) s).setLocations(pts);
    } else {
      // TODO WWJ - Do we need a special NULL object for this case?
    }

    ShapeAttributes attrs = s.getAttributes();

    if (attrs == null)
      attrs = new BasicShapeAttributes();

    attrs.setInteriorMaterial(new Material(style.getFillColor(o)));
    attrs.setInteriorOpacity(style.getFillOpacity(o));
    attrs.setOutlineMaterial(new Material(style.getBorderColor(o)));
    attrs.setOutlineOpacity(style.getBorderOpacity(o));

    // TODO WWJ
    attrs.setOutlineWidth(3);

    s.setAttributes(attrs);
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