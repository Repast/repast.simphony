package repast.simphony.visualization.visualization3D;


import java.awt.Color;
import java.awt.Font;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.vecmath.Point3f;

import repast.simphony.visualization.visualization3D.style.Style3D;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public interface Label {
  String getLabel();

  void setLabel(String label);

  Style3D.LabelPosition getPosition();

  void setColor(Color color);

  Color getColor();

  void setFont(Font font);

  Font getFont();

  void setPosition(Style3D.LabelPosition position);
  
  void setOffset(float offset);
  
  float getOffset();

  BranchGroup getBranchGroup(Point3f center, Group object);
}
