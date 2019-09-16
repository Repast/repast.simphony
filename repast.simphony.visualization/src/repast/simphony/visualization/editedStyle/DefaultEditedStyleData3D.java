package repast.simphony.visualization.editedStyle;

import java.awt.Font;


public class DefaultEditedStyleData3D<T> extends EditedStyleData<T> {

	public DefaultEditedStyleData3D(){
	  shapeWkt = "sphere";
	  size = 0.03f;
	  
	  label = "";
	  labelOffset = 0.03f;
	  labelPosition = "bottom";
	  labelPrecision = 3;
	  
	  labelFontFamily = "Arial";
	  labelFontType = Font.PLAIN;
	  labelFontSize = 14;
	  labelColor = new float[]{1,1,1};
	  
	  sizeMin = 0;
	  sizeMax = 0.1f;
	  sizeScale = 1;
	  
	  color = new float[]{1,0,0};   // Red
	  
	  colorMax = new float[]{10,10,10};
	  colorMin = new float[]{0,0,0};
	  colorScale = new float[]{1,1,1};
	  
	}	
}
