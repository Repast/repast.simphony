package repast.simphony.visualization.editedStyle;


public class EditedEdgeStyleData<T> implements PaintableStyleData, SizeableStyleData {

	protected LineStyle lineStyle;
	
	protected float size;
	protected float sizeMax;
	protected float sizeMin;
	protected float sizeScale;
	
	protected String sizeMethodName;
	protected String sizeMaxMethodName;
	protected String sizeMinMethodName;
	protected String sizeScaleMethodName;
		
	protected float[] color;
	
	protected String redMethodName;
	protected String greenMethodName;
	protected String blueMethodName;
	
	protected float[] colorMax;
	protected float[] colorMin;
	protected float[] colorScale;
	
	public float getSize() {
	  return size;
	}
	public float getSizeMax() {
		return sizeMax;
	}
	public float getSizeMin() {
		return sizeMin;
	}
	public float getSizeScale() {
		return sizeScale;
	}
	public String getSizeMaxMethodName() {
		return sizeMaxMethodName;
	}
	public String getSizeMethodName() {	
		return sizeMethodName;
	}
	public String getSizeMinMethodName() {
		return sizeMinMethodName;
	}
	public String getSizeScaleMethodName() {
		return sizeScaleMethodName;
	}
	public String getRedMethod() {
		return redMethodName;
	}
	public String getGreenMethod() {
    return greenMethodName;	
	}
	public String getBlueMethod() {
		return blueMethodName;
	}
	
	public void setSize(float size) {
		this.size = size;
	}
	public void setSizeMax(float sizeMax) {
	  this.sizeMax = sizeMax;
	}
	public void setSizeMin(float sizeMin) {
	  this.sizeMin = sizeMin;
	}
	public void setSizeScale(float sizeScale) {
	  this.sizeScale = sizeScale;
	}
	public void setSizeMaxMethodName(String sizeMaxMethodName) {
	  this.sizeMaxMethodName = sizeMaxMethodName;
	}
	public void setSizeMethodName(String sizeMethodName) {
	  this.sizeMethodName = sizeMethodName;
	}
	public void setSizeMinMethodName(String sizeMinMethodName) {
	  this.sizeMinMethodName = sizeMinMethodName;
	}
	public void setSizeScaleMethodName(String sizeScaleMethodName) {
    this.sizeScaleMethodName = sizeScaleMethodName;
	}
		public void setRedMethod(String redMethod) {
	  this.redMethodName = redMethod;
	}
	public void setGreenMethod(String greenMethod) {
	  this.greenMethodName = greenMethod;
	}
	public void setBlueMethod(String blueMethod) {
	  this.blueMethodName = blueMethod;
	}
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
	public LineStyle getLineStyle() {
		return lineStyle;
	}
	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}
}
