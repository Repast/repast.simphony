package repast.simphony.visualization.gis3D;

import java.awt.image.BufferedImage;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.SurfaceImage;

/**
 * Customized SurfaceImage implementation for Repast GIS display that provides
 *   finer control over texture rendering such as anti-alias modes.
 *   
 * @author Eric Tatara
 *
 */
public class RepastSurfaceImage extends SurfaceImage {

	public RepastSurfaceImage (BufferedImage image, Sector sector) {
		super(image, sector);
	}

	public RepastSurfaceImage(Object imageSource, Iterable<? extends LatLon> corners) {
		super (imageSource, corners);
	}
	
	
	@Override
	protected void initializeSourceTexture(DrawContext dc){
		
		// Use a RepastTexture instead of the default WWJ texture implementation.
		
		this.sourceTexture = new RepastTexture(this.getImageSource(), false);
		
//      this.sourceTexture = new LazilyLoadedTexture(this.getImageSource(), false);
		
	}
}
