package repast.simphony.visualization.editedStyle;

/**
 * 
 * @author Eric Tatara
 *
 */
public class EditedValueLayerStyleData {

	protected float[] color;
	protected float[] colorMax;
	protected float[] colorMin;
	protected float[] colorScale;
	protected boolean[] colorValue;
	
	protected float y;
	protected float yMin;
	protected float yMax;
	protected float yScale;
	protected boolean yValue;
	
	protected float cellSize;
		
	public float[] getColorMax() {
		return colorMax;
	}
	
	public void setColorMax(float[] colorMax) {
		this.colorMax = colorMax;
	}
	
	public float[] getColorMin() {
		return colorMin;
	}
	
	public void setColorMin(float[] colorMin) {
		this.colorMin = colorMin;
	}
	
	public float[] getColorScale() {
		return colorScale;
	}
	
	public void setColorScale(float[] colorScale) {
		this.colorScale = colorScale;
	}
	
	public float[] getColor() {
		return color;
	}
	
	public void setColor(float[] color) {
		this.color = color;
	}

	public float getCellSize() {
		return cellSize;
	}

	public void setCellSize(float cellSize) {
		this.cellSize = cellSize;
	}

	public boolean[] getColorValue() {
		return colorValue;
	}

	public void setColorValue(boolean[] colorValue) {
		this.colorValue = colorValue;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getYMin() {
		return yMin;
	}

	public void setYMin(float min) {
		yMin = min;
	}

	public float getYMax() {
		return yMax;
	}

	public void setYMax(float max) {
		yMax = max;
	}

	public float getYScale() {
		return yScale;
	}

	public void setYScale(float scale) {
		yScale = scale;
	}

	public boolean isYValue() {
		return yValue;
	}

	public void setYValue(boolean value) {
		yValue = value;
	}
}