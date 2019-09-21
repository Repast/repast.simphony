package repast.simphony.ws;

import java.awt.Color;

import repast.simphony.visualization.editedStyle.EditedStyleData;
import repast.simphony.visualization.editedStyle.EditedStyleUtils;

/**
 * Style for server-based display updates.  Does not contain any references
 *   to display library classes.
 * 
 * @author Eric Tatara
 *
 * @param <T> The style object (agent) type.
 */
public class ServerStyle2D {
	
	private EditedStyleData styleData;
	
	public ServerStyle2D(EditedStyleData styleData) {
		this.styleData = styleData;
	}
	
	public String getWellKnownText() {
		return styleData.getShapeWkt();
	}
	
	public String getIconFile() {
		return styleData.getIconFile2D();
	}
	
	public Color getColor(Object o) {
    return EditedStyleUtils.getColor(styleData, o);
  }
	
	public float getSize(Object o) {
		return EditedStyleUtils.getSize(styleData, o);
	}
	
	public String getLabel(Object o) {
    return EditedStyleUtils.getLabel(styleData, o);
  }
	
	public Color getLabelColor(Object o, Color currentColor) {
		float colorRGB[] = styleData.getLabelColor();
		return new Color(colorRGB[0],colorRGB[1],colorRGB[2]);
	}
	

}
