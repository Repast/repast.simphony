package repast.simphony.ws;

import java.awt.Color;

import repast.simphony.visualization.editedStyle.EditedEdgeStyleData;
import repast.simphony.visualization.editedStyle.EditedStyleUtils;

/**
 * Style for server-based display updates.  Does not contain any references
 *   to display library classes.
 * 
 * @author Eric Tatara
 *
 * @param <T> The style object (agent) type.
 */
public class ServerNetStyle2D {
	
	private EditedEdgeStyleData styleData;
	
	public ServerNetStyle2D(EditedEdgeStyleData styleData) {
		this.styleData = styleData;
	}
	
	
	public Color getColor(Object o) {
    return EditedStyleUtils.getColor(styleData, o);
  }
	
	public float getSize(Object o) {
		return EditedStyleUtils.getSize(styleData, o);
	}
	
	
	

}
