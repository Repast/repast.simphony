package repast.simphony.visualization.editedStyle;


/**
 * 
 * @author Eric Tatara
 *
 * @param <T>
 */
public class EditedStyleData<T> implements PaintableStyleData, SizeableStyleData {

	protected String shapeWkt;
	
	protected float size;
	protected float sizeMax;
	protected float sizeMin;
	protected float sizeScale;
	
	protected String sizeMethodName;
	protected String sizeMaxMethodName;
	protected String sizeMinMethodName;
	protected String sizeScaleMethodName;
	
	protected String label;
	protected String labelMethod;
	protected String labelPosition;
	protected int labelPrecision;
	protected float labelOffset;
	protected int labelFontSize;
	protected int labelFontType;
	protected String labelFontFamily;
	protected float[] labelColor;
	
	protected float[] color;
	
	protected String redMethodName;
	protected String greenMethodName;
	protected String blueMethodName;
	
	protected float[] colorMax;
	protected float[] colorMin;
	protected float[] colorScale;
	
	protected String iconFile2D;
	protected String modelFile3D;
	protected String textureFile3D;
	
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
	public String getLabel() {
		return label;
	}
	public String getLabelPosition() {
		return labelPosition;
	}
	public String getLabelMethod() {
		return labelMethod;
	}
	public float getLabelOffset() {
		return labelOffset;
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
	public void setLabel(String label) {
		this.label = label;
	}
	public void setLabelPosition(String angle) {
	  this.labelPosition = angle;
	}
	public void setLabelMethod(String labelMethod) {
	  this.labelMethod = labelMethod;
	}
	public void setLabelOffset(float offset) {
	  this.labelOffset = offset;
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
	public int getLabelFontSize() {
		return labelFontSize;
	}
	public void setLabelFontSize(int labelFontSize) {
		this.labelFontSize = labelFontSize;
	}
	public int getLabelFontType() {
		return labelFontType;
	}
	public void setLabelFontType(int labelFontType) {
		this.labelFontType = labelFontType;
	}
	public String getLabelFontFamily() {
		return labelFontFamily;
	}
	public void setLabelFontFamily(String labelFontFamily) {
		this.labelFontFamily = labelFontFamily;
	}
	public float[] getLabelColor() {
		return labelColor;
	}
	public void setLabelColor(float[] labelColor) {
		this.labelColor = labelColor;
	}	
	public String getShapeWkt() {
		return shapeWkt;
	}
	public void setShapeWkT(String shapeWkt) {
		this.shapeWkt = shapeWkt;
	}
	public String getIconFile2D() {
		return iconFile2D;
	}
	public void setIconFile2D(String iconFile2D) {
		this.iconFile2D = iconFile2D;
	}
	public String getModelFile3D() {
		return modelFile3D;
	}
	public void setModelFile3D(String modelFile3D) {
		this.modelFile3D = modelFile3D;
	}
	public String getTextureFile3D() {
		return textureFile3D;
	}
	public void setTextureFile3D(String textureFile3D) {
		this.textureFile3D = textureFile3D;
	}
	public int getLabelPrecision() {
		return labelPrecision;
	}
	public void setLabelPrecision(int labelPrecision) {
		this.labelPrecision = labelPrecision;
	}
}
