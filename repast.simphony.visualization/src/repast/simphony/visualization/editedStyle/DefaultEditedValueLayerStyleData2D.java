package repast.simphony.visualization.editedStyle;

/**
 * Default data for 2D edited value layer styles
 * 
 * @author Eric Tatara
 *
 */
public class DefaultEditedValueLayerStyleData2D extends EditedValueLayerStyleData {

	public DefaultEditedValueLayerStyleData2D(){
	  	  
	  color = new float[]{0,0,1};
	  
	  colorMax = new float[]{10,10,10};
	  colorMin = new float[]{0,0,0};
	  colorScale = new float[]{1,1,1};
	  colorValue = new boolean[]{false,false,false};
	  cellSize = 15.0f;
	}	
}
