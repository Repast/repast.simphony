package repast.simphony.visualization.gis3D.style;

import repast.simphony.visualization.gis3D.Material;
import repast.simphony.visualization.gis3D.RenderableShape;

/**
 * 
 * @author Eric Tatara
 *
 */
public interface StyleGIS3D<T> {

//	enum LabelPosition {NORTH, SOUTH, EAST, WEST, SOUTH_WEST, NORTH_WEST, SOUTH_EAST, NORTH_EAST, ON_TOP_OF};

  public RenderableShape getShape(T obj);

  public Material getMaterial(T obj, Material material);
  
  public boolean isScaled(T object);
  
  public float[] getScale(T obj);

  
//  public float[] getRotation(T obj);
//  
//  public String getLabel(T obj, String currentLabel);
//  
//  public Color getLabelColor(T obj, Color currentColor);
//  
//  public Font getLabelFont(T obj, Font currentFont);
//
//  public LabelPosition getLabelPosition(T obj, LabelPosition curentPosition);
//  
//  // return 0 means system figures it out
//  float getLabelOffset(T obj);
	
}
