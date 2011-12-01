package repast.simphony.visualization.editedStyle;



public class DefaultEditedEdgeStyleData2D<T> extends EditedEdgeStyleData<T> {

	public DefaultEditedEdgeStyleData2D(){
		lineStyle = LineStyle.SOLID;
	  size = 1.0f;
	 
	  sizeMin = 0;
	  sizeMax = 1;
	  sizeScale = 1;
	  
	  color = new float[]{0,0,0};
	  
	  colorMax = new float[]{10,10,10};
	  colorMin = new float[]{0,0,0};
	  colorScale = new float[]{1,1,1};
	  
	}	
}
