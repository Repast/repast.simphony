package repast.simphony.ws.gis;

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
public class ServerNetStyleGIS {
	
	private EditedEdgeStyleData styleData;
	
	public ServerNetStyleGIS(EditedEdgeStyleData styleData) {
		this.styleData = styleData;
	}
	
	
	public Color getColor(Object o) {
    return EditedStyleUtils.getColor(styleData, o);
  }
	
	public float getSize(Object o) {
		return EditedStyleUtils.getSize(styleData, o);
	}
	
	
	

}
