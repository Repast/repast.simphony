package repast.simphony.space.gis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.geotools.coverage.CoverageFactoryFinder;
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
