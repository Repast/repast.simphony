package repast.simphony.visualization.visualization3D.style;

import java.awt.Color;
import java.awt.Paint;

import repast.simphony.valueLayer.ValueLayer;

/**
 * @author Nick Collier
 * @author Eric Tatara
 */
public class DefaultValueLayerStyle3D implements ValueLayerStyle3D {

	private ValueLayer layer;

	public DefaultValueLayerStyle3D(){
		
	}
	
	/**
	 * Gets the Y (height) value for the specified coordinates.
	 *
	 * @param coordinates the coordinates to get the Y value for.
	 * @return the Y (height) value for the specified coordinates.
	 */
	public float getY(double... coordinates) {
		return (float)layer.get(coordinates);
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
		return .06f;
	}

	public Paint getPaint(double... coordinates) {
		return Color.BLUE;
	}
}
