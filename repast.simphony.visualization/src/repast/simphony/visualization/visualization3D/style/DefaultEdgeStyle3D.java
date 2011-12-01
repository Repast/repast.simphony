package repast.simphony.visualization.visualization3D.style;

import java.awt.Color;
import java.awt.Font;

import javax.media.j3d.Shape3D;

import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.ShapeFactory;

/**
 * @author Nick Collier
 * @version $Revision: 1.3 $ $Date: 2006/01/06 22:53:54 $
 */
public class DefaultEdgeStyle3D<T> implements EdgeStyle3D<T> {
  
  protected float radius = .01f;
  
  public TaggedBranchGroup getBranchGroup(T o, TaggedBranchGroup taggedGroup) {
    if (taggedGroup == null || taggedGroup.getTag() == null) {
      taggedGroup = new TaggedBranchGroup("DEFAULT");
      Shape3D shape = ShapeFactory.createCylinder(radius, 1f, "DEFAULT");
      taggedGroup.getBranchGroup().addChild(shape);
      return taggedGroup;
    }
    
    return null;
  }

  public float edgeRadius(T o) {
    return radius;
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
		return null;
	}

	public float[] getRotation(T t) {
		return null;
	}
  
  public TaggedAppearance getAppearance(T t, TaggedAppearance taggedAppearance, Object shapeID) {
    if (taggedAppearance == null || taggedAppearance.getTag() == null) {
      taggedAppearance = new TaggedAppearance("DEFAULT");
      AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), Color.GRAY);
			return taggedAppearance;
    }

		return null;
  }
}
