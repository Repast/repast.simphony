package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.WWTexture;

/**
 * PlaceMark extends the WWJ PointPlacemark and only overrides the handling of
 *   the WWTexture such that PlaceMark does not store the texture in a map keyed
 *   on the image source.  
 * 
 * @author Eric Tatara
 *
 */
public class PlaceMark extends PointPlacemark {

	protected WWTexture texture;
	
	public PlaceMark(){
		this(Position.ZERO);
		
		setAttributes(new PointPlacemarkAttributes());
	}

	public PlaceMark(Position position) {
		super(position);
	}

	@Override
	/**
	 * Override PointPlacemark so we can handle texture storage more simply.  
	 *   The original behavior of PointPlacemark stores textures from an image
	 *   URL in a map, however here we want to just provide the texture. 
	 */
	protected WWTexture chooseTexture(PointPlacemarkAttributes attrs){
				
		return texture;
	}
	
	public WWTexture getTexture() {
		return texture;
	}

	public void setTexture(WWTexture texture) {
		this.texture = texture;
	}
}