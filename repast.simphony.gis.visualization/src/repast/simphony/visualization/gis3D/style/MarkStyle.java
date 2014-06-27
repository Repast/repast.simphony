package repast.simphony.visualization.gis3D.style;

import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.WWTexture;

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

	/**
	 * Get the WWTexture that will be applied to the PlaceMark.
	 * 
	 * @param object
	 * @param texture
	 * @return
	 */
	public WWTexture getTexture(T object, WWTexture texture);

	/**
	 * The PlaceMark is a WWJ PointPlacemark implementation with a different 
	 *   texture handling mechanism.  All other standard WWJ PointPlacemark 
	 *   attributes can be changed here.  PointPlacemark label attributes could be
	 *   set here, but are also available through the MarkStyle interface.
	 *   
	 *   @see gov.nasa.worldwind.render.PointPlacemark for more info.
	 * 
	 * @param object
	 * @param mark
	 * @return
	 */
	public PlaceMark getPlaceMark(T object, PlaceMark mark);

	/**
	 * Get the mark elevation in meters.  The elevation is used to visually offset 
	 *   the mark from the surface and is not an inherent property of the agent's 
	 *   location in the geography.
	 * 
	 * @return
	 */
	public double getElevation(T obj);
	
	/**
	 * Scale factor for the mark size. 
	 * 
	 * @param obj
	 * @return
	 */
	public double getScale(T obj);

	public double getHeading(T obj);

	/**
	 * The agent on-screen label.  Return null instead of empty string "" for better
	 *   performance.
	 * 
	 * @param obj
	 * @return
	 */
	public String getLabel(T obj);

	/**
	 * The mark label color.
	 * 
	 * @param obj
	 * @return
	 */
	public Color getLabelColor(T obj);

	/**
	 * The mark label font.
	 * 
	 * @param obj
	 * @return
	 */
	public Font getLabelFont(T obj);
	
	/**
	 * 
	 * Return an Offset that determines the label position relative to the mark 
	 * position.  @see gov.nasa.worldwind.render.Offset
	 * 
	 * The gov.nasa.worldwind.render.Offset is used to position the label from 
	 *   the mark point location.  The first two arguments in the Offset 
	 *   constructor are the x and y offset values.  The third and fourth 
	 *   arguments are the x and y units for the offset. AVKey.FRACTION 
	 *   represents units of the image texture size, with 1.0 being one image 
	 *   width/height.  AVKey.PIXELS can be used to specify the offset in pixels. 
	 * 
	 * @param obj
	 * @return
	 */
	public Offset getLabelOffset(T obj);
	
	/**
	 * Width of the line that connects an elevated mark with the surface.  Use
	 *   a value of 0 to disable line drawing.
	 *   
	 * @param obj
	 * @return
	 */
	public double getLineWidth(T obj);
	
	/**
	 * The material (color) of the line.
	 * 
	 * @param obj
	 * @param lineMaterial
	 * @return
	 */
	public Material getLineMaterial(T obj, Material lineMaterial);
}
