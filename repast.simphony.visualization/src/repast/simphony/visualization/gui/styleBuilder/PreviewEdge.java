package repast.simphony.visualization.gui.styleBuilder;

import java.awt.Color;
import java.awt.Stroke;

public interface PreviewEdge {

	public void setColor(Color color);
	public void setStroke(Stroke stroke);
		
	public void updatePreview();
}
