package repast.simphony.visualization.editedStyle;

import java.awt.Paint;

import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualization.visualization2D.style.ValueLayerStyle;

/**
 * 
 * @author Eric Tatara
 *
 */
public class EditedValueLayerStyle2D implements ValueLayerStyle {
	protected ValueLayer layer;
	protected EditedValueLayerStyleData innerStyle;

	public EditedValueLayerStyle2D(String userStyleFile) {
		innerStyle = EditedStyleUtils.getValueLayerStyle(userStyleFile);
		if (innerStyle == null)
			innerStyle = new DefaultEditedValueLayerStyleData2D();
	}
	
	public void addValueLayer(ValueLayer layer) {
		this.layer = layer;
	}

	public int getRed(double... coordinates) {
		return 0;
	}
	
	public int getGreen(double... coordinates) {
		return 0;
	}
	
	public int getBlue(double... coordinates) {
		return 0;
	}

	public Paint getPaint(double... coordinates) {
		return EditedStyleUtils.getValueLayerColor(innerStyle, layer.get(coordinates));
	}

	public float getCellSize() {
		return innerStyle.getCellSize();
	}
}