package repast.simphony.visualization.editedStyle;

import java.awt.Color;
import java.awt.Font;

import javax.media.j3d.Shape3D;

import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.ShapeFactory;
import repast.simphony.visualization.visualization3D.style.EdgeStyle3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;

/**
 * 
 * @author Eric Tatara
 *
 * @param <T>
 */
public class EditedEdgeStyle3D<T> implements EdgeStyle3D<T> {
  
  EditedEdgeStyleData<Object> innerStyle;
  private float scale3D2D = 100; // scale to match 2D widths in editor GUI
//  protected float constantRadius = 0.01f;
	public EditedEdgeStyle3D(String userStyleFile) {
		innerStyle = EditedStyleUtils.getEdgeStyle(userStyleFile);
		if (innerStyle == null)
			innerStyle = new DefaultEditedEdgeStyleData3D<Object>();
	}
  
  public TaggedBranchGroup getBranchGroup(T o, TaggedBranchGroup taggedGroup) {
    if (taggedGroup == null || taggedGroup.getTag() == null) {
      taggedGroup = new TaggedBranchGroup("DEFAULT");
      
      float radius = EditedStyleUtils.getSize(innerStyle, o)/scale3D2D;
      
      Shape3D shape = ShapeFactory.createCylinder(radius, 1f, "DEFAULT");
      
      taggedGroup.getBranchGroup().addChild(shape);
      
      return taggedGroup;
    }
    return null;
  }

  public float edgeRadius(T o) {
    return EditedStyleUtils.getSize(innerStyle, o)/scale3D2D;
//  	return constantRadius;  
  	}

  public EdgeType getEdgeType() {
    return EdgeType.SHAPE;
  }

	public String getLabel(T o, String currentLabel) {
		return null;
	}

	public Color getLabelColor(T t, Color currentColor) {
		return null; // To change body of implemented methods use File |
		// Settings | File Templates.
	}

	public Font getLabelFont(T t, Font currentFont) {
		return null; // To change body of implemented methods use File |
		// Settings | File Templates.
	}

	public EdgeStyle3D.LabelPosition getLabelPosition(T o, LabelPosition curentPosition) {
		return LabelPosition.NORTH;
	}

	public float getLabelOffset(T t) {
		return .015f;
	}

	public float[] getScale(T o) {
		float radius = EditedStyleUtils.getSize(innerStyle, o);
		
		return new float[]{radius,1.0f,radius};
	}

	public float[] getRotation(T t) {
		return null;
	}
  
  public TaggedAppearance getAppearance(T t, TaggedAppearance taggedAppearance, Object shapeID) {
    
  	if (taggedAppearance == null || taggedAppearance.getTag() == null) 
      taggedAppearance = new TaggedAppearance("DEFAULT");
    
  	 Color color = EditedStyleUtils.getColor(innerStyle, t);
     
     AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), color);
	
     // TODO add option in gui for drawing wireframe 
//     AppearanceFactory.setPolygonAppearance(taggedAppearance.getAppearance(), PolygonDraw.LINE);
 
     return taggedAppearance;

  }
}
