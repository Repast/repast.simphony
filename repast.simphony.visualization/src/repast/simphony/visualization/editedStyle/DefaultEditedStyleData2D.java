package repast.simphony.visualization.editedStyle;

import java.awt.Font;

public class DefaultEditedStyleData2D<T> extends EditedStyleData<T> {

	public DefaultEditedStyleData2D(){
	  
	  shapeWkt = "circle";
	  // -1 indicates the default icon size
	  size = -1f;
	  
	  label = "";
	  labelOffset = 5.0f;
	  labelPosition = "bottom";
	  labelPrecision = 3;
	  
	  labelFontFamily = "Arial";
	  labelFontType = Font.PLAIN;
	  labelFontSize = 14;
	  labelColor = new float[]{0,0,0};
	  
	  sizeMin = 1;
	  sizeMax = 15;
	  sizeScale = 1;
	  
	  color = new float[]{0,0,1};
	  
	  colorMax = new float[]{10,10,10};
	  colorMin = new float[]{0,0,0};
	  colorScale = new float[]{1,1,1};
	  
	}	
}
