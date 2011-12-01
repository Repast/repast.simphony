package repast.simphony.visualization.editedStyle;

import repast.simphony.visualization.visualization3D.style.ValueLayerStyle3D;

public class EditedValueLayerStyle3D extends EditedValueLayerStyle2D 
  implements ValueLayerStyle3D {

	public EditedValueLayerStyle3D(String userStyleFile) {
		super(userStyleFile);
	}

	public float getY(double... coordinates) {
		float y = innerStyle.getY();

		if (!innerStyle.isYValue())
			return y;

		y = (float)layer.get(coordinates);
		
		// scaling
		float yMax = innerStyle.getYMax();
		float yMin = innerStyle.getYMin();
		float yScale = innerStyle.getYScale();

		float div = yMax - yMin;

		if (div <= 0)
			div = 1;

		y = ((y - yMin) / div) * yScale;
		
		return y;
	}
}