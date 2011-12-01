package repast.simphony.visualization.visualization3D.style;


import java.awt.Color;
import java.awt.Font;

/**
 * @author Nick Collier
 * @version $Revision: 1.3 $ $Date: 2006/01/06 22:53:54 $
 */
public interface Style3D<T> {
  
  enum LabelPosition {NORTH, SOUTH, EAST, WEST, SOUTH_WEST, NORTH_WEST, SOUTH_EAST, NORTH_EAST, ON_TOP_OF};

  // can return null, or the passed-in representation meaning don't change anything
  public TaggedBranchGroup getBranchGroup(T obj, TaggedBranchGroup group);

  // return null to indicate no change?
  public float[] getScale(T obj);

  public float[] getRotation(T obj);
  
  public String getLabel(T obj, String currentLabel);
  
  public Color getLabelColor(T obj, Color currentColor);
  
  public Font getLabelFont(T obj, Font currentFont);

  public LabelPosition getLabelPosition(T obj, LabelPosition curentPosition);

  // return null or current appearance meaning don't change anything.
  public TaggedAppearance getAppearance(T obj, TaggedAppearance appearance, Object shapeID);
  
  // return 0 means system figures it out
  float getLabelOffset(T obj);
}
