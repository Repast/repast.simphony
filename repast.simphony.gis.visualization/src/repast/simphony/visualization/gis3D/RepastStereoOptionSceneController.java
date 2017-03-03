package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.StereoOptionSceneController;

/**
 * Custom WWJ SceneController subclass for Repast GIS display
 * 
 * @author Eric Tatara
 *
 */
public class RepastStereoOptionSceneController extends StereoOptionSceneController {

	// Preset split scale values to include in the Repast display descriptor
	public static double SPLIT_SCALE_LOW_QUALITY = 2.7;
	public static double SPLIT_SCALE_MEDIUM_QUALITY = 2.9;
	public static double SPLIT_SCALE_HIGH_QUALITY = 3.2;
	public static double SPLIT_SCALE_VERY_HIGH_QUALITY = 3.5;
	public static double SPLIT_SCALE_ULTRA_HIGH_QUALITY = 3.7;
	
	public RepastStereoOptionSceneController(){
		super();
		
		setSplitScape(SPLIT_SCALE_MEDIUM_QUALITY);
	}
	
	/**
	 * Set the AbstractSceneController split scale value.  The value determines
	 * the number of tiles per surface area, thus affecting the image quality of
	 * surface shapes.  Higher values produce sharper surface shapes but with a
	 * performance penalty.  Values less than about 2.5 result in extreme blur
	 * and values over 5 will produce unusable canvas refresh rates.
	 * 
	 * @param splitScale
	 */
	protected void setSplitScape(double splitScale){
		surfaceObjectTileBuilder.setSplitScale(splitScale);
	}
	
}
