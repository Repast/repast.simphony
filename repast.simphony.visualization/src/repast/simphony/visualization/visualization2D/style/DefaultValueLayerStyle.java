package repast.simphony.visualization.visualization2D.style;

import java.awt.Color;
import java.awt.Paint;

import repast.simphony.valueLayer.ValueLayer;

/**
 * 
 * @author Eric Tatara
 * 
 *  @deprecated replaced by ogl 2D
 *
 */
public class DefaultValueLayerStyle implements ValueLayerStyle {

	protected ValueLayer layer;
	
	public DefaultValueLayerStyle(){
		
	}
	
	/**
	 * Adds a value layer to this ValueLayerStyle.
	 *
	 * @param layer the layer to add
	 */
	public void addValueLayer(ValueLayer layer) {
		this.layer = layer;
	}

	/**
	 * Gets the red component of the color for the specified coordinate.
	 *
	 * @param coordinates the coordinate to get the color for
	 * @return the red component of the color for the specified coordinate.
	 */
	public int getRed(double... coordinates) {
		return 0;
	}
	
	/**
	 * Gets the green component of the color for the specified coordinate.
	 *
	 * @param coordinates the coordinate to get the color for
	 * @return the green component of the color for the specified coordinate.
	 */
	public int getGreen(double... coordinates) {
		return layer.get(coordinates) > 0.0 ? 255 : 0;
	}
	
	/**
	 * Gets the blue component of the color for the specified coordinate.
	 *
	 * @param coordinates the coordinate to get the color for
	 * @return the blue component of the color for the specified coordinate.
	 */
	public int getBlue(double... coordinates) {
		return layer.get(coordinates) <= 0.0 ? 255 : 0;
	}
	
	/**
	 * Gets the size of one size of the cell value layer cell. The units are those appropriate to the
	 * display -- 2D or 3D.
	 *
	 * @return the width of the cell.
	 */
	public float getCellSize() {
		return 10f;
	}

	public Paint getPaint(double... coordinates) {
		return Color.BLUE;
	}
}