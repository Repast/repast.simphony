package repast.simphony.gis.visualization.engine;

import repast.simphony.visualization.engine.StyleRegistrar;
import repast.simphony.visualization.gis3D.style.StyleGIS;

/**
 * Style registrar for 3D GIS displays.
 * 
 * @author Eric Tatara
 */
public class StyleRegistrarGIS3D extends StyleRegistrar<StyleGIS<?>> {


  @Override
  protected StyleGIS<?> createdEditedStyle(String editedStyleName) {

  	// TODO GIS provided editid style 
  	
  	return null;
  }
}
