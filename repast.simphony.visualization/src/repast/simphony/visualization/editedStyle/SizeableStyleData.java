package repast.simphony.visualization.editedStyle;

/**
 * Interface for edited styles that are scalable.
 * 
 * @author Eric Tatara
 *
 */
public interface SizeableStyleData {

	public float getSize();
	
	public float getSizeMax();
	
	public float getSizeMin();
	
	public float getSizeScale();
	
	public String getSizeMaxMethodName();
	
	public String getSizeMethodName();
	
	public String getSizeMinMethodName();
	
	public String getSizeScaleMethodName();
	
}
