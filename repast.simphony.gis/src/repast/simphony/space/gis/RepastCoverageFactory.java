package repast.simphony.space.gis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Factory that provides support for creating GridCoverage2D instances that are
 * writable.
 * 
 * Current write capability of WritableGridCoverage2D is limited to a single band.  
 * 
 * TODO GIS provide multiple band write capability.
 * 
 * @author Eric Tatara
 *
 */
public class RepastCoverageFactory {
	
	/**
	 * Create a writable coverage using a raster file.  
	 * 
	 * @param rasterfile a georeferenced raster file such as GeoTif
	 * @param forceLonLatAxisOrder forces lon,lat coordinate order on read
	 * @return WritableGridCoverage2D a writable GridCoverage2D
	 */
	public static WritableGridCoverage2D createWritableCoverageFromFile(File rasterfile, 
			boolean forceLonLatAxisOrder){
		
		Hints hints = null;
		
		if (forceLonLatAxisOrder)
			hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
				
		AbstractGridFormat format = GridFormatFinder.findFormat(rasterfile, hints);
		GridCoverage2DReader reader = format.getReader(rasterfile);
		
		GridCoverage2D readCoverage = null;
		try {
			readCoverage = reader.read(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new WritableGridCoverage2D(readCoverage);
	}
	
	/**
	 * Create a writable raster instance using the specified parameters.
	 * 
	 * @param name
	 * @param width
	 * @param height
	 * @param envelope
	 * @return WritableGridCoverage2D a writable GridCoverage2D
	 * 
	 * TODO image data type (int, float, double, etc).
	 */
	public static WritableGridCoverage2D createWritableCoverage(String name, int width, 
			int height, ReferencedEnvelope envelope){
		
		GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
		
		// Alt method for creating single band raster / image
//	WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_BYTE, width, height, 1, null);
		
		// raster
//		return  new WritableGridCoverage2D(factory.create(name, raster, envelope));

		// image
		return new WritableGridCoverage2D(factory.create(name, image, envelope));
	}
	
	
}
