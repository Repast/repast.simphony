package repast.simphony.visualization.editedStyle;

/**
 * Default data for 3D edited value layer styles
 * 
 * @author Eric Tatara
 *
 */
public class DefaultEditedValueLayerStyleData3D extends EditedValueLayerStyleData {

	public DefaultEditedValueLayerStyleData3D(){
	  	  
	  color = new float[]{0,0,1};
	  
	  colorMax = new float[]{10,10,10};
	  colorMin = new float[]{0,0,0};
	  colorScale = new float[]{1,1,1};
	  colorValue = new boolean[]{false,false,false};
	  
	  cellSize = 0.06f;
	  
	  y = 0.0f;
	  yMin = 0.0f;
	  yMax = 0.1f;
	  yScale = 1.0f;
	  yValue = false;
	}	
}
