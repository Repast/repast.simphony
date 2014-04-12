package repast.simphony.visualization.gis3D.style;

import gov.nasa.worldwind.render.BasicWWTexture;
import gov.nasa.worldwind.render.Offset;

import java.awt.Color;
import java.awt.Font;

import repast.simphony.visualization.gis3D.PlaceMark;

/**
 * Interface for Place Marks in the GIS 3D Display.
 * 
 * @author Eric Tatara
 *
 * @param <T> the agent type
 */
public interface MarkStyle<T> extends StyleGIS<T> {

	// TODO WWJ - use BufferedImage instead?
	public BasicWWTexture getTexture(T object, BasicWWTexture texture);

	public PlaceMark getPlaceMark(T object, PlaceMark mark);

	public double getScale(T obj);

	public double getHeading(T obj);

	public String getLabel(T obj);

	public Color getLabelColor(T obj);

	public Font getLabelFont(T obj);
	
	public Offset getLabelOffset(T obj);
}
