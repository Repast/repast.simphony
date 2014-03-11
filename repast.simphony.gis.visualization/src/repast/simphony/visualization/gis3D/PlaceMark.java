package repast.simphony.visualization.gis3D;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.BasicWWTexture;
import gov.nasa.worldwind.render.PatternFactory;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.WWTexture;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * 
 * @author Eric Tatara
 *
 */
public class PlaceMark extends PointPlacemark {

	protected BasicWWTexture texture;
	
	public PlaceMark(){
		this(Position.ZERO);
		
		setAttributes(new PointPlacemarkAttributes());
	}

	public PlaceMark(Position position) {
		super(position);
	}

	@Override
	/**
	 * Override PointPlaceMark so we can handle texture storage more simply. 
	 */
	protected WWTexture chooseTexture(PointPlacemarkAttributes attrs){
				
		return texture;
	}
	
	public BasicWWTexture getTexture() {
		return texture;
	}

	public void setTexture(BasicWWTexture texture) {
		this.texture = texture;
	}
}