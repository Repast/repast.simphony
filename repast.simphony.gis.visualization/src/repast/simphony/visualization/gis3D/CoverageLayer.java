package repast.simphony.visualization.gis3D;

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
  
  // TODO GIS get smoothing param from descriptor
  protected boolean smoothing = false;
  
  public CoverageLayer(String coverageName, CoverageStyle style){
  	setName(coverageName);
  	this.coverageName = coverageName;
  	this.style = style;
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
  	
  	// TODO GIS can we handle this of coverage added to Geography after init?
  	// TODO GIS removing the renderable each time handles null coverage but 
  	//      might cause display issues so check.
  	removeRenderable(surfaceImage);
  	
  	if (coverage == null) return;  // Nothing to do if null coverage
  	
  	// TODO GIS Coverage styling that overrides the internal buffered image.
  	
  	// The slowest part is calling .getBufferedImage(), so we might be able
		//  to speed up this code by comparing the underlying raster or PlanarImage
		//  which is relatively fast, and only updating if the data has changed.
		
		PlanarImage pi = (PlanarImage)coverage.getRenderedImage();
		
		// TODO GIS these probably wont change so think about storing
		ReferencedEnvelope envelope = new ReferencedEnvelope(coverage.getEnvelope());
		Sector sector = WWUtils.envelopeToSectorWGS84(envelope);
		
		// slow step
		surfaceImage.setImageSource(pi.getAsBufferedImage(), sector);
		addRenderable(surfaceImage);
  }
  
  /**
   * 
   * Override dispose() to prevent losing renderables on frame resize/dock.
   */
  @Override   // TODO WWJ - find out if this can be handled better
  public void dispose() {}

}