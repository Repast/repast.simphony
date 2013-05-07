package repast.simphony.visualization.gisnew;

import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

/**
 * 
 * @author Eric Tatara
 *
 */
public interface StyleGIS<T> {
	
  public GeoShape getShape(T obj);

  public Color getFillColor(T obj);
  
  public double getFillOpacity(T obj);
  
  public Color getBorderColor(T obj);
  
  public double getBorderOpacity(T obj);
  
  public double getBorderWidth(T obj);
  
  public boolean isScaled(T object);
  
  public float getScale(T obj);

  public float getRotation(T obj);
  
  public String getLabel(T obj);
  
  public Color getLabelColor(T obj);
  
  public Font getLabelFont(T obj);
	
}
