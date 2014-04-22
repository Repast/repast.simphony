package repast.simphony.visualization.gis3D.style;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PatternFactory;
import gov.nasa.worldwind.render.WWTexture;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;

import repast.simphony.visualization.gis3D.BufferedImageTexture;
import repast.simphony.visualization.gis3D.PlaceMark;

/**
 * Default style for place marks.
 * 
 * @author Eric Tatara
 *
 * @param <T> the agent type
 */
public class DefaultMarkStyle<T> implements MarkStyle<T>{
	
	@Override
	public WWTexture getTexture(T object, WWTexture texture) {

		if (texture != null)
			return texture;
		
		Color color = Color.red;
		
		BufferedImage image = PatternFactory.createPattern(PatternFactory.PATTERN_SQUARE, 
				new Dimension(10, 10), 0.7f,  color, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0));
		
		return new BufferedImageTexture(image);	
		
	}

	@Override
	public PlaceMark getPlaceMark(T object, PlaceMark mark) {
		
		if (mark == null)
			mark = new PlaceMark();
		
		mark.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
		mark.setLineEnabled(false);
		
		return mark;
	}

	@Override
	public double getScale(T obj) {
		return 1;
	}

	@Override
	public double getHeading(T obj) {
		return 0;
	}

	@Override
	public String getLabel(T obj) {
		return null;
	}

	@Override
	public Color getLabelColor(T obj) {
		return null;
	}

	@Override
	public Font getLabelFont(T obj) {
		return null;
	}

	@Override
	public Offset getLabelOffset(T obj) {
		return null;
	}

	@Override
	public double getElevation(T obj) {
		return 0;
	}

	@Override
	public double getLineWidth(T obj) {
		return 0;
	}

	@Override
	public Material getLineMaterial(T obj, Material lineMaterial) {
		if (lineMaterial == null){
			lineMaterial = new Material(Color.RED);
		}
		
		return lineMaterial;
	}
}