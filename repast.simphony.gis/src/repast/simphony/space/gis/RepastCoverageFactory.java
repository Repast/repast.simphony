package repast.simphony.space.gis;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.measure.Unit;
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
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.NumberRange;
import org.geotools.util.factory.Hints;

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
	public static String NO_DATA_CATEGORY_LABEL = "No data";

	// Maximum valid color index for indexed colormodels of DataBuffer.TYPE_USHORT
	public static int MAX_SHORT_COLOR_INDEX = 2*Short.MAX_VALUE;
	
  // Maximum valid color index for indexed colormodels of DataBuffer.TYPE_BYTE
	public static int MAX_BYTE_COLOR_INDEX = 2*Byte.MAX_VALUE+1;
	
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
	 * Create a WritableGridCoverage2D suitable for in-memory read/write with 
	 *   double precision data.
	 * 
	 * @param name of the coverage 
	 * @param width the integer width of the grid
	 * @param height the integer height of the grid
	 * @param envelope the georeferenced envelope
	 * @param unit the optional {@link Unit} of the data
	 * @param defaultValue the optional default data value
	 * @param minValue the optional minimum data used for grayscale color range
	 * @param maxValue the optional maximum data used for grayscale color range
	 * @param noDataValue the optional "NO DATA" value
	 * 
	 * @return the WritableGridCoverage2D created with input arguments
	 */
	public static WritableGridCoverage2D createWritableCoverageDouble(String name, 
			int width, int height, ReferencedEnvelope envelope, Unit<?> unit, 
			double defaultValue, double minValue, double maxValue, double noDataValue) {
	
		return createWritableCoverage(name, width, height, envelope, 
				DataBuffer.TYPE_DOUBLE, unit, defaultValue, minValue, maxValue, noDataValue);
	}
	
	/**
	 * Create a WritableGridCoverage2D suitable for in-memory read/write with 
	 *   float precision data.
	 * 
	 * @param name of the coverage 
	 * @param width the integer width of the grid
	 * @param height the integer height of the grid
	 * @param envelope the georeferenced envelope
	 * @param unit the optional {@link Unit} of the data
	 * @param defaultValue the optional default data value
	 * @param minValue the optional minimum data used for grayscale color range
	 * @param maxValue the optional maximum data used for grayscale color range
	 * @param noDataValue the optional "NO DATA" value
	 * 
	 * @return the WritableGridCoverage2D created with input arguments
	 */
	public static WritableGridCoverage2D createWritableCoverageFloat(String name, 
			int width, int height, ReferencedEnvelope envelope, Unit<?> unit, 
			double defaultValue, double minValue, double maxValue, double noDataValue) {
	
		return createWritableCoverage(name, width, height, envelope, 
				DataBuffer.TYPE_FLOAT, unit, defaultValue, minValue, maxValue, noDataValue);
	}
	
	/**
	 * Create a WritableGridCoverage2D suitable for in-memory read/write.
	 * 
	 * @param name of the coverage 
	 * @param width the integer width of the grid
	 * @param height the integer height of the grid
	 * @param envelope the georeferenced envelope
	 * @param dataType the DataBuffer type, e.g. DataBuffer.TYPE_FLOAT or TYPE_DOUBLE
	 * @param unit the optional {@link Unit} of the data
	 * @param defaultValue the optional default data value
	 * @param minValue the optional minimum data used for grayscale color range
	 * @param maxValue the optional maximum data used for grayscale color range
	 * @param noDataValue the optional "NO DATA" value
	 * 
	 * @return the WritableGridCoverage2D created with input arguments
	 */
	public static WritableGridCoverage2D createWritableCoverage(String name, 
			int width, int height, ReferencedEnvelope envelope, int dataType, 
			Unit<?> unit, Double defaultValue, Double minValue, Double maxValue,
			Double noDataValue) {

		NumberRange<?> range =  NumberRange.create(minValue, maxValue);
	
		// Assume the single category name is the same as the coverage name.
		// Create a single gray-scale GridSampleDimension with specified min-max range
		// NOTE: ranges cannot overlap
		Category[] categories;
		
		if (noDataValue == null) {
			categories	= new Category[] {
					new Category(name, (Color)null, range)
			};
		}
		else {
			categories	= new Category[] {
					new Category(NO_DATA_CATEGORY_LABEL, (Color)null, noDataValue),
					new Category(name, (Color)null, range)
			};
		}
		// The single band GridSampleDimension has the same name as the coverage
		GridSampleDimension[] bands = new GridSampleDimension[] {
				new GridSampleDimension(name, categories, unit)
		};
		
    WritableRaster raster = RasterFactory.createBandedRaster(dataType, width, height, 1, null);
        
    // Set default data
    if (defaultValue != null) {
    	for (int i=0; i<raster.getWidth(); i++) {
    		for (int j=0; j<raster.getHeight(); j++) {
    			raster.setSample(i,j,0, defaultValue);    
    		}
    	}
    }
    GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
    GridCoverage2D coverage = factory.create(name, raster, envelope, bands); 
		
    return new WritableGridCoverage2D(coverage);
	}
	
	public static WritableGridCoverage2D createWritableByteIndexedCoverage(String name, int width, 
			int height, ReferencedEnvelope envelope, Category[] categories,
			Unit<?> unit, int defaultValue){
		
		return createWritableIndexedCoverage(name, width, height, envelope, 
				categories, DataBuffer.TYPE_BYTE, unit, defaultValue);
	}
	
	public static WritableGridCoverage2D createWritableShortIndexedCoverage(String name, int width, 
			int height, ReferencedEnvelope envelope, Category[] categories,
			Unit<?> unit, int defaultValue){
		
		return createWritableIndexedCoverage(name, width, height, envelope, 
				categories, DataBuffer.TYPE_USHORT, unit, defaultValue);
	}
	
	public static WritableGridCoverage2D createWritableIndexedCoverage(String name, int width, 
			int height, ReferencedEnvelope envelope, Category[] categories,
			int dataType, Unit<?> unit, Integer defaultValue){

		// Validate the categories against the maximum possible values for each data type
		for (Category cat : categories) {
			double maxVal = cat.getRange().getMaximum();
			
			if (dataType == DataBuffer.TYPE_BYTE && maxVal > MAX_BYTE_COLOR_INDEX) {
				String info = "The category " + cat.getName() + " has a maximum value of " 
						+ maxVal + " but the data type BYTE is only valid to " + MAX_BYTE_COLOR_INDEX;
				Exception ex = new Exception(info);
				msg.error(info, ex);
				ex.printStackTrace();
				return null;
			}
			else if (dataType == DataBuffer.TYPE_USHORT && maxVal > MAX_SHORT_COLOR_INDEX) {
				String info = "The category " + cat.getName() + " has a maximum value of " 
						+ maxVal + " but the data type SHORT is only valid to " + MAX_SHORT_COLOR_INDEX;
				Exception ex = new Exception(info);
				msg.error(info, ex);
				ex.printStackTrace();
				return null;
			}
		}

		// Single band with the same name as the coverage
		GridSampleDimension[] bands = new GridSampleDimension[] {
				new GridSampleDimension(name, categories, unit)
		};

		// Create the colormodel first since it is used to create a compatible
		// WritableRaster instance.  If the RasterFactory is used to create a raster
		// as in the GeoTools test code, the resulting raster may not be compatible
		// with the ColorModel, causing an Exception.

		// GridSampleDimension gets the ColorModel from the org.geotools.coverage.ColorModelFactory 
		// that provides indexed ColorModels for DataBuffer USHORT and BYTE, and
		// a scaled ColorModel for all others.

		ColorModel model = bands[0].getColorModel(0, bands.length, dataType);		

		WritableRaster raster = model.createCompatibleWritableRaster(width, height);

  // RasterFactory approach per the GeoTools unit tests.  Only works with BYTE 
//  WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_BYTE, width, height, 1, null);
  
		//Set default data
		if (defaultValue != null) {
			for (int i=0; i<raster.getWidth(); i++) {
				for (int j=0; j<raster.getHeight(); j++) {
					raster.setSample(i,j,0, defaultValue);    
				}
			}
		}
       
		RenderedImage image = new BufferedImage(model, raster, false, null);
		GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
  
		// GeoTools approach for creating a coverage from the raster directly.
//  GridCoverage2D coverage = factory.create("Test", raster, envelope, bands);
  
		// Create the coverage from the RenderedImage
		GridCoverage2D coverage = factory.create(name, image, envelope, bands, null, null);

    return new WritableGridCoverage2D(coverage);
	}
	
}