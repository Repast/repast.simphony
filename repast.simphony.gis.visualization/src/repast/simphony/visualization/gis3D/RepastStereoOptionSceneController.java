package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.StereoOptionSceneController;

/**
 * Custom WWJ SceneController subclass for Repast GIS display
 * 
 * @author Eric Tatara
 *
 */
public class RepastStereoOptionSceneController extends StereoOptionSceneController {

	/**
	 * RenderQuality determines the WWJ AbstractSceneController split scale value.  
	 * The value determines the number of tiles per surface area, thus affecting 
	 * the image quality of surface shapes.  Higher values produce sharper surface 
	 * shapes but with a performance penalty.  
	 * 
	 * Values less than about 2.5 result in extreme blur and values over 5.0 will 
	 * produce unusable canvas refresh rates.
	 *
	 */
	public enum RenderQuality{
		LOW (2.7),
		MEDIUM (2.9),
		HIGH (3.2),
		VERYHIGH (3.5),
		ULTRAHIGH (3.7);
		
		double splitScale = 3.0;
		
		RenderQuality(double splitScale){
			this.splitScale = splitScale;
		}
		
		public double getSplitScale() {return splitScale;}
	}

	protected RenderQuality renderQuality = RenderQuality.MEDIUM;
	
	public RepastStereoOptionSceneController(){
		super();
		
		setRenderQuality(RenderQuality.MEDIUM);
	}
	
	public RenderQuality getRenderQuality() {
		return renderQuality;
	}

	public void setRenderQuality(RenderQuality renderQuality) {
		this.renderQuality = renderQuality;
	
		surfaceObjectTileBuilder.setSplitScale(renderQuality.getSplitScale());
	}
}