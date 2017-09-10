package repast.simphony.space.gis;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.media.jai.RasterFactory;

import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;

import simphony.util.messages.MessageCenter;

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
	private static MessageCenter msg = MessageCenter.getMessageCenter(RepastCoverageFactory.class);
	
	/**
	 * Create a writable coverage using a coverage file.  
	 * 
	 * @param file a georeferenced coverage file such as GeoTif
	 * @param forceLonLatAxisOrder forces lon,lat coordinate order on read
	 * @return WritableGridCoverage2D a writable GridCoverage2D
	 */
	public static WritableGridCoverage2D createWritableCoverageFromFile(File file, 
			boolean forceLonLatAxisOrder){
		
		GridCoverage2D readCoverage = null;
		
		readCoverage = createCoverageFromFile(file, forceLonLatAxisOrder);
		if (readCoverage == null)
			return null;
		
		return new WritableGridCoverage2D(readCoverage);
	}
	
	/**
	 * Create a coverage using a coverage file.  
	 * 
	 * @param file a georeferenced coverage file such as GeoTif
	 * @param forceLonLatAxisOrder forces lon,lat coordinate order on read
	 * @return GridCoverage2D 
	 */
	public static GridCoverage2D createCoverageFromFile(File file, boolean forceLonLatAxisOrder) {
		Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, forceLonLatAxisOrder);

		if (!file.exists()) { 
			String info = "Cannot find coverage file: " + file.getPath();
			Exception ex = new FileNotFoundException(info);
			msg.error(info, ex);
			ex.printStackTrace();
			return null;
		}
		
		AbstractGridFormat format = GridFormatFinder.findFormat(file);
		
		if (format == null || format instanceof UnknownFormat) {
			String info = "Cannot find coverage format for file: " + file.getPath();
			Exception ex = new Exception(info);
			msg.error(info, ex);
			ex.printStackTrace();
			return null;
		}
		
		
		GridCoverage2DReader reader = null;
		
		try {
			reader = format.getReader(file,hints);
		} 
		catch(Exception ex) {
			String info = "Cannot find coverage reader for format: " + format.toString() + 
					" for file " + file.getPath();
			msg.error(info, ex);
			ex.printStackTrace();
			return null;
		}
		
		if (reader == null) {
			String info = "Cannot find coverage reader for format: " + format.toString() + 
					" for file " + file.getPath();
			Exception ex = new Exception(info);
			msg.error(info, ex);
			ex.printStackTrace();
			return null;
		}
		
		GridCoverage2D readCoverage = null;
		try {
			readCoverage = reader.read(null);
		} catch (IOException ex) {
			String info = "Error reading coverage from file: " + file.getPath();
			msg.error(info, ex);
			ex.printStackTrace();
		}
		return readCoverage;
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
	 * TODO default value
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
	
	public static WritableGridCoverage2D createWritableFloatCoverage(String name, int width, 
			int height, int defaultValue, ReferencedEnvelope envelope) {
		
		WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT,
				width, height, 1, null);
		
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				raster.setSample(x, y, 0, defaultValue);
			}
		}
		
		Hints hints = new Hints(Hints.TILE_ENCODING, "raw");
    GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(hints);  
    GridCoverage2D  coverage = factory.create("Test", raster, envelope);

    return new WritableGridCoverage2D(coverage);
	}
	
	public static WritableGridCoverage2D createWritableIndexedCoverage(String name, int width, 
			int height, int defaultValue, ReferencedEnvelope envelope, Category[] categories,
			GridSampleDimension[] bands ){
	
    BufferedImage image  = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);    
    WritableRaster raster = image.getRaster();
        
    for (int i=0; i<raster.getWidth(); i++) {
    	for (int j=0; j<raster.getHeight(); j++) {
    		raster.setSample(i,j,0,defaultValue);
       }
    }
		
		Hints hints = new Hints(Hints.TILE_ENCODING, "raw");
    GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(hints);  
    GridCoverage2D  coverage = factory.create("Test", raster, envelope, bands);

    return new WritableGridCoverage2D(coverage);
	}
}