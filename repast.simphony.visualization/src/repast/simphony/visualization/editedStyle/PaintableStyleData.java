package repast.simphony.visualization.editedStyle;

/**
 * Interface for edited styles that have a color.
 * 
 * @author Eric Tatara
 *
 */
public interface PaintableStyleData {

	public String getRedMethod();
	
	public String getGreenMethod(); 
	
	public String getBlueMethod();
	
	public float[] getColorMax();
	
	public float[] getColorMin();
	
	public float[] getColorScale();
	
	public float[] getColor();
}
