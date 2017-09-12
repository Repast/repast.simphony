package repast.simphony.visualization.gis3D;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.WritableRaster;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.jts.ReferencedEnvelope;

import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.SurfaceImage;
import repast.simphony.space.gis.Geography;
import repast.simphony.visualization.gis3D.style.CoverageStyle;

/**
 * Layer class for displaying grid coverage (rasters) as WorldWind RenderableLayer.
 * Each layer instance can hold a single coverage object.
 * 
 * @author Eric Tatara
 * 
 */
public class CoverageLayer extends RenderableLayer{

	protected String coverageName;
	protected Geography geography;
  protected CoverageStyle style;
  protected SurfaceImage surfaceImage;  // Only one SurfaceImage in the layer
  
  // Image to set when the coverage is null
  protected BufferedImage noImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
  
  // TODO GIS get smoothing param from descriptor
  protected boolean smoothing = false;
  
  public CoverageLayer(String coverageName, CoverageStyle style){
  	setName(coverageName);
  	this.coverageName = coverageName;
  	this.style = style;
  	
  	// Create a single SurfaceImage once with dummy data that will be update later.
  	// Needed here since the coverage can be null during init or any time later
  	surfaceImage = new RepastSurfaceImage(noImage, Sector.EMPTY_SECTOR);
  	addRenderable(surfaceImage);
  }

  /**
   * Gets the style used by this display layer.
   * 
   * @return the style used by this display layer.
   */
  public CoverageStyle getStyle() {
    return style;
  }

  /**
   * Sets the style used by this display layer.
   * 
   * @param style the new style
   */
  public void setStyle(CoverageStyle style) {
    this.style = style;
  }
  
  /**
   * Set the geography for this display.
   * 
   * @param geography
   */
  public void setGeography(Geography geography) {
    this.geography = geography;
  }
  
  /**
   * Update the coverage with raster data an styling
   */
  public void update() {
  	
  	// Get the coverage each update in case it changed or was created/destroyed
  	GridCoverage2D coverage = geography.getCoverage(coverageName);
  	
  	if (coverage == null) {  		
  		surfaceImage.setImageSource(noImage, Sector.EMPTY_SECTOR);
  		
  		return;  // Nothing left to do if null coverage
  	}
  
  	
  	// TODO GIS Coverage styling that overrides the internal buffered image.
  	
  	// The slowest part is calling .getBufferedImage(), so we might be able
		//  to speed up this code by comparing the underlying raster or PlanarImage
		//  which is relatively fast, and only updating if the data has changed.
		
 		ReferencedEnvelope envelope = new ReferencedEnvelope(coverage.getEnvelope());
 		Sector sector = WWUtils.envelopeToSectorWGS84(envelope);
  	
		PlanarImage pi = (PlanarImage)coverage.getRenderedImage();
		BufferedImage image = pi.getAsBufferedImage();
		
		// TODO GIS check all other potential data types
		// If the raster is backed with a Double DataBuffer, we need to convert it
		// to a Float DataBuffer because double is not supported by OpenGL.  The
		// easiest approach seems to just create a new image and draw on it rather
		// than creating new buffers and rasters.
		if (pi.getData().getDataBuffer().getDataType() == DataBuffer.TYPE_DOUBLE){
			BufferedImage original = pi.getAsBufferedImage();

			image = new BufferedImage(original.getWidth(), original.getHeight(), 
					BufferedImage.TYPE_INT_ARGB);
			
			image.getGraphics().drawImage(original, 0, 0, null);
		}
	
		// slow step
		surfaceImage.setImageSource(image, sector);
  }
  
  /**
   * 
   * Override dispose() to prevent losing renderables on frame resize/dock.
   */
  @Override   // TODO WWJ - find out if this can be handled better
  public void dispose() {}

}