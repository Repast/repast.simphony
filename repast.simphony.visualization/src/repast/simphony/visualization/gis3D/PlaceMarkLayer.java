package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.BasicWWTexture;
import gov.nasa.worldwind.render.Material;

import java.awt.Color;
import java.awt.Font;

import repast.simphony.visualization.LayoutUpdater;
import repast.simphony.visualization.gis3D.style.MarkStyle;

/**
 * Styled display layer for WorldWind display layers.
 * 
 * @author Eric Tatara
 *
 */
public class PlaceMarkLayer extends AbstractRenderableLayer<MarkStyle,PlaceMark> {

  public PlaceMarkLayer(String name, MarkStyle<?> style) {
    super(name, style);
  }

  protected void applyUpdatesToShape(Object obj) {
  	LatLon pt = WWUtils.CoordToLatLon(geography.getGeometry(obj).getCoordinate());
  	
  	PlaceMark mark = getVisualItem(obj);
  	
  	BasicWWTexture texture = style.getTexture(obj, mark.getTexture());
    
  	if (texture != null){
  		mark.setTexture(texture);
  	}
        
  	// Update the center lat/lon if the shape has moved
    if (!(pt.getLatitude().degrees == mark.getPosition().getLatitude().degrees &&
    		pt.getLongitude().degrees == mark.getPosition().getLongitude().degrees)){
    
      mark.setPosition(new Position(pt,0));
    }

    // TODO WWJ - 
    
    // Note that there is no performance gain here by checking if attributes have
    //  changed before setting, since the mark attributes just hold the values,
    //  which are updated when WWJ PointPlacemark class renders itself.
    mark.getAttributes().setScale( style.getScale(obj));
    mark.getAttributes().setHeading(style.getHeading(obj));
    mark.setLabelText(style.getLabel(obj));
    mark.getAttributes().setLabelFont(style.getLabelFont(obj));

    Color labelColor = style.getLabelColor(obj); 
    Color currentColor = mark.getAttributes().getLabelColor();
    
    // But do a check on color, since a new Material needs to be created
    if (labelColor != null && !(labelColor.equals(currentColor))){
    	mark.getAttributes().setLabelMaterial(new Material (labelColor));
    }
    
    mark.getAttributes().setLabelOffset(style.getLabelOffset(obj));        
  }
  
  protected PlaceMark createVisualItem(Object o) {
  	LatLon pt = WWUtils.CoordToLatLon(geography.getGeometry(o).getCoordinate());
  	PlaceMark mark = style.getPlaceMark(o, null);
  	
  	// TODO WWJ - make sure set in style
//  	mark.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
//  	mark.setLineEnabled(false);
//  	 mark.getAttributes().setScale(10d);

  	
  	//  	 mark.setLabelText("Hi!");
  	 
  	 visualItemMap.put(o, mark);

     return mark;
  }
  
 
  
  protected void updateExistingObjects(LayoutUpdater updater){
  	for (Object o : visualItemMap.keySet()){
  		applyUpdatesToShape(o);
  	}
  }
  
  protected void processAddedObjects() {
    for (Object o : addedObjects) {
    	PlaceMark mark  = createVisualItem(o);
    	renderableToObjectMap.put(mark, o);
    	addRenderable(mark);
    }
    addedObjects.clear();
  }

  protected void processRemovedObjects() {
    for (Object o : removeObjects) {
    	PlaceMark mark  = visualItemMap.remove(o);
      if (mark != null) {
        removeRenderable(mark);
        renderableToObjectMap.remove(mark);
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