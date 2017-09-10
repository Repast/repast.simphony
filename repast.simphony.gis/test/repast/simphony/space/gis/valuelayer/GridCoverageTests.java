package repast.simphony.space.gis.valuelayer;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.image.WritableRenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.iterator.WritableRandomIter;

import org.apache.commons.lang3.ArrayUtils;
import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.coverage.processing.Operations;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.NumberRange;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;

import it.geosolutions.jaiext.iterators.RandomIterFactory;
import junit.framework.TestCase;

/**
 * Test GeoTools GridCoverage factory methods.  These tests serve several purpose:
 * 
 *  1) Test that the GeoTools libraries are available on the classpath
 *  2) Test for any changes to GeoTools API
 *  3) General examples for developers using the GridCoverage API
 * 
 * @author Eric Tatara
 *
 */
public class GridCoverageTests extends TestCase {

	GridCoverageFactory coverageFactory;

	// Test lat/lon pair describe a rectangle over downtown Chicago.  (x1,y) is the
	//   lower left corner and (x2,y2) is the upper left corner.
	double lon1 = -87.668;  // x1
	double lon2 = -87.582;  // x2
	
	double lat1 = 41.834;  // y1
	double lat2 = 41.916;  // y2
	
	// Some test lon,lat coord test points that should be within the above rectangle
	Coordinate coord1 = new Coordinate(-87.6560, 41.9066);
	Coordinate coord2 = new Coordinate(-87.5899, 41.8768);
	Coordinate coord3 = new Coordinate(-87.6428, 41.8413);
	
	@Override
	public void setUp() {
		coverageFactory = CoverageFactoryFinder.getGridCoverageFactory(null);
	}

	/**
	 * Create a grid coverage and test that its properties are as expected.
	 */
	public void testCoverageBasicProperties(){

		String path;
		Category[] categories;
		Rectangle2D bounds;
		GridSampleDimension[] bands;
		
		// Envelope over North America roughly bounding the United States
		// from lower left corner (-124, 25) to upper right corner (-64, 49)
		// Note that WGS84 axis order is x,y = lon,lat
		
		// The image data index (0,0) is upper left
		
		GridCoverage2D coverage = createSampleCoverageFromBufferedImage();

		// Check the CRS of the coverage
		CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
			
		assertEquals(crs, coverage.getCoordinateReferenceSystem());
		
		// Check the coverage envelope
		Envelope env = coverage.getEnvelope();
		assertEquals(env.getLowerCorner().getCoordinate()[0], -124.0);
		assertEquals(env.getLowerCorner().getCoordinate()[1], 25.0);
		assertEquals(env.getUpperCorner().getCoordinate()[0], -64.0);
		assertEquals(env.getUpperCorner().getCoordinate()[1], 49.0);		
		
		// Check the coverage name
		assertEquals("test coverage", coverage.getName().toString());
		
		// TODO check the coverage sample dimensions
		GridSampleDimension[] gsd = coverage.getSampleDimensions();	
		
		// Read some of the coverage values and compare to expected
		// Roughly middle of coverage envelope, should be around 100.0
		int[] val = (int[])coverage.evaluate(new DirectPosition2D(crs, -95, 39));
		assertEquals(89, val[0]);
		
		val = new int[1];
		// Read using Point2D should be the same
		coverage.evaluate(new Point2D.Double(-95, 39), val);  // writes to val
		assertEquals(89, val[0]);
		
		// Roughly upper left corner of coverage envelope, should be around 10.0
		val = (int[])coverage.evaluate(new DirectPosition2D(crs, -122.87, 47.01));
		assertEquals(9, val[0]);
		
		// Roughly bottom right corner of coverage envelope, should be around 200.0
		val = (int[])coverage.evaluate(new DirectPosition2D(crs, -65.65, 25.27));
		assertEquals(195, val[0]);
		
		// Test if coverage is writable
		assertTrue(coverage.isDataEditable());
		
	}
	
	/**
	 * Project a coverage to a different CRS and test that sampling the coverage
	 * works as expected.
	 * 
	 * Project WGS84 lon/lat to NAD 83 lat/lon degree
	 */
	public void testCoverageReproject(){
		GridCoverage2D coverage = createSampleCoverageFromBufferedImage();
		
		// Should be WGS84 to start
		CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();
		
		assertEquals(DefaultGeographicCRS.WGS84, crs);
		
		double lat = 25.27;
		double lon = -65.65;
		
		// Read a value from the WGS84 coverage with axis order lon, lat
		int[] val = (int[])coverage.evaluate(new DirectPosition2D(crs, lon, lat));
		
		int compareValue = val[0];
		 
		// Create a NAD 1983 CRS, unit = degree, axis order = lat,lon (NORTH_EAST)
		CoordinateReferenceSystem crsNAD83 = null;
		try {
			crsNAD83 = CRS.decode("EPSG:4269", false);
		} catch (FactoryException e) {
			e.printStackTrace();
		}
		
		// Project the coverage to the new CRS
		coverage = (GridCoverage2D)Operations.DEFAULT.resample(coverage,crsNAD83);
		assertEquals(coverage.getCoordinateReferenceSystem(), crsNAD83);
		
		// Now test read samples from the coverage using the NAD83 axis order lat, lon, 
		// which is swapped from the WGS84 axis order
		val = (int[])coverage.evaluate(new DirectPosition2D(crsNAD83, lat, lon));
	  assertEquals(val[0], compareValue);
	}
	

	// Create a sample grid coverage from a BufferedImage
	private GridCoverage2D createSampleCoverageFromBufferedImage(){

		int width  = 100;
		int height = 100;
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
		WritableRaster raster = (WritableRaster) image.getData();

		// Assign some values to the raster data band 0
		int band = 0;
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				raster.setSample(x, y, band,(int)(x+y));
			}
		}
		image.setData(raster);

		CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
		
		// Create an envelope over North America roughly bounding the United States
		// from lower left corner (-124, 25) to upper right corner (-64, 49)
		// Note that WGS84 axis order is x,y = lon,lat
		
		// The image data index (0,0) is upper left
		
		return coverageFactory.create("test coverage", image,
				new Envelope2D(crs, -124, 25, 60, 24));
	}
	
	public void testCreateIndexedCoverage(){
		int width = 100;
		int height = 200;
		
		int type = DataBuffer.TYPE_USHORT;
		int maxColorIndex = 2*Short.MAX_VALUE;
		
//		int type = DataBuffer.TYPE_BYTE;
//		int maxColorIndex = 2*Byte.MAX_VALUE;
		
		Color[] temperatureColorScale = new Color[maxColorIndex+1];
		
		// white to red color scale
		for (int i=0; i<=maxColorIndex; i++) {
//			temperatureColorScale[i] = new Color(maxColorIndex, maxColorIndex-i, maxColorIndex-i); 
			
			float a = ((float)i/(float)maxColorIndex);
			temperatureColorScale[i] = new Color(1.0f, 1-a, 1-a);
		}
	
    Category[] categories	= new Category[] {	
        new Category("No data",     Color.BLACK, 0),
        new Category("Land",        Color.GREEN, 1),
        new Category("Ocean",       Color.BLUE, 2),
        new Category("Temperature", temperatureColorScale, 3, maxColorIndex)
    };
    
    // Single band
    GridSampleDimension[] bands = new GridSampleDimension[] {
    		new GridSampleDimension("Temperature", categories, SI.CELSIUS)
    };

    // Need to create the colormodel first, since it is used to create a compatible
    // WritableRaster instance.  If the RasterFactory is used to create a raster
    // as in the GeoTools test code, the resulting raster may not be compatible
    // with the ColorModel, causing a severe Exception.
    
    // GridSampleDimension gets the ColorModel from the org.geotools.coverage.ColorModelFactory 
    // that provides indexed ColorModels only for DataBuffer USHORT and BYTE, and
    // a scaled ColorModel for all others.
    
  	ColorModel model = bands[0].getColorModel(0, bands.length, type);		
		
  	WritableRaster raster = model.createCompatibleWritableRaster(width, height);
  	
    // RasterFactory approach per the GeoTools unit tests.  Only works with BYTE 
//    WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_BYTE, width, height, 1, null);
    
  	// Fill the raster with simulated data
    int aval = 0;
    for (int i=0; i<raster.getWidth(); i++) {
    	for (int j=0; j<raster.getHeight(); j++) {
    		
    		if (i < 3 && j < 3)         // simulate NODATA
    			aval = 0;
    		else if (i < 10 && j < 10)  // simulate land
    			aval = 1;
    		else if (i < 30 && j < 30)  // simulate ocean
    			aval = 2;
    		else
    			aval = (maxColorIndex/254)*(i+j);               // simulate temperature
    		
    		raster.setSample(i,j,0, aval);
       }
    }
          
    //  DefaultGeographicCRS.WGS84 axis order is lon,lat (x,y) 
		ReferencedEnvelope envelope = new ReferencedEnvelope(lon1, lon2, lat1, lat2, 
				DefaultGeographicCRS.WGS84);
		
		RenderedImage image = new BufferedImage(model, raster, false, null);
				
    GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
    
    // GeoTools approach for creating a coverage from the raster directly.
//    GridCoverage2D coverage = factory.create("Test", raster, envelope, bands);
    
    // Create the coverage from the RenderedImage
    GridCoverage2D coverage = factory.create("Test", image, envelope, bands, null, null);
    
 
    // TODO Test Read
    
    int[] val = null;
    val = coverage.evaluate(new Point2D.Double(coord1.x, coord1.y), val);
    
    Category cat = coverage.getSampleDimension(0).getCategory(val[0]);
    Unit<?> unit = coverage.getSampleDimension(0).getUnits();

    String categoryName = null;
//    String unitName = UnitFormat.getInstance(Locale.getDefault()).format(unit);
    if (cat != null) {
    	categoryName = cat.getName().toString(Locale.getDefault()); 	
    }
    
    System.out.println("Type: Indexed " + categoryName + " " + val[0]);
    
    // TODO Test Write
    
    WritableRenderedImage writableImage = (WritableRenderedImage)coverage.getRenderedImage();
    
    WritableRandomIter writeIter = RandomIterFactory.createWritable(writableImage, null);
    
    // Write directly to the grid coord
    writeIter.setSample(1, 1, 0, 250);
    
    // Write using the geographic coordinates
    // First transform the geo coords to grid coords
    GridCoordinates2D gridPos = null;
    try {
			gridPos = coverage.getGridGeometry().worldToGrid(new DirectPosition2D(coord1.x, coord1.y));
		} catch (InvalidGridGeometryException e1) {
			e1.printStackTrace();
		} catch (TransformException e1) {
			e1.printStackTrace();
		}
    
    writeIter.setSample(gridPos.x, gridPos.y, 0, 250.1);
    
    val = null;
    cat = null;
    val = coverage.evaluate(new Point2D.Double(coord1.x, coord1.y), val);
    cat = coverage.getSampleDimension(0).getCategory(val[0]);
    
    if (cat != null) {
    	categoryName = cat.getName().toString(Locale.getDefault()); 	
    }
    
    System.out.println(val[0] + " : " + categoryName);
    
    // TODO serialize / image
    
  	PlanarImage pi = (PlanarImage)coverage.getRenderedImage();
		BufferedImage image2 = pi.getAsBufferedImage();
		
		System.out.println(image2.toString() + "\n");
		
		// Write an output image for visual inspection.  
		try {
			ImageIO.write(image2, "gif", new File("test/data/temperatureIndexed.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Create test coverages for various Numeric data types
	 */
	public void testCreateCoverages() {
		createCoverage(Float.class);
		createCoverage(Double.class);
		
	// TODO Int color space doesnt seem to work in generating the BufferedImage
//		createCoverage(Integer.class);  
		
		// Byte and Short technically works although the use in Repast isn't clear
		createCoverage(Byte.class);
		createCoverage(Short.class);
		
		// TODO transfer the coverage creation to the factory class
		
	}
	
	/**
	 * Create a single band coverage based on the data type class argument.
	 * 
	 * @param clazz
	 */
	private void createCoverage(Class<? extends Number> clazz){
		int width = 100;
		int height = 200;
		
		int dataType = 0;
		
	  // max raster value is needed for colormodel scaling
		// max = 61892 for 100*200*PI
		double maxValue = (width*height)*Math.PI;   
		NumberRange<?> range = null;
		
		if (clazz == Byte.class) {
			dataType = DataBuffer.TYPE_BYTE;  // 0
			range = NumberRange.create(1, 128);  
		}
		else if (clazz == Short.class) {
			dataType = DataBuffer.TYPE_SHORT;  // 2
			range = NumberRange.create(1, (short)Math.min(maxValue, Short.MAX_VALUE));  
		}
		else if (clazz == Integer.class) {
			dataType = DataBuffer.TYPE_INT;  // 3
			range = NumberRange.create(1, (int)maxValue);
		}
		else if (clazz == Float.class) {
			dataType = DataBuffer.TYPE_FLOAT;  // 4
			range = NumberRange.create(1, (float)maxValue);
		}
		else if (clazz == Double.class) {
			dataType = DataBuffer.TYPE_DOUBLE;  // 5
			range = NumberRange.create(1, (double)maxValue);
		}
		
		// Create a single grayscale GridSampleDimension with specified min-max range
		Category[] categories	= new Category[] {	   
				new Category("No data", (Color)null, 0),   // NOTE: ranges cannot overlap
				new Category("Temperature", (Color)null, range)
		};

		// Single band
		GridSampleDimension[] bands = new GridSampleDimension[] {
				new GridSampleDimension("Temperature", categories, SI.CELSIUS)
		};
		
    WritableRaster raster = RasterFactory.createBandedRaster(dataType, width, height, 1, null);
        
    // Set sample data
    for (int i=0; i<raster.getWidth(); i++) {
    	for (int j=0; j<raster.getHeight(); j++) {
    		if (clazz == Byte.class) {
    			raster.setSample(i,j,0, (byte)(1 + (i*j)/200));
    		}
    		else
    			raster.setSample(i,j,0, Math.PI*(i*j));    
       }
    }
    
    //  DefaultGeographicCRS.WGS84 axis order is lon,lat (x,y) 
		ReferencedEnvelope envelope = new ReferencedEnvelope(lon1, lon2, lat1, lat2, DefaultGeographicCRS.WGS84);
    GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
   
    GridCoverage2D coverage = factory.create("Test", raster, envelope, bands);    
    
    // Test Read
    Object[] objval = null;
    double dval = 0;
    if (clazz == Float.class) {
    	float[] val = null;
    	val = coverage.evaluate(new Point2D.Double(coord1.x, coord1.y), val);
    	objval = ArrayUtils.toObject(val);
    	dval = val[0];
    }
    else if (clazz == Double.class) {
    	double[] val = null;
    	val = coverage.evaluate(new Point2D.Double(coord1.x, coord1.y), val);
    	objval = ArrayUtils.toObject(val);
    	dval = val[0];
    }
    else if (clazz == Integer.class) {
    	int[] val = null;
    	val = coverage.evaluate(new Point2D.Double(coord1.x, coord1.y), val);
    	objval = ArrayUtils.toObject(val);
    	dval = val[0];
    }
    else if (clazz == Byte.class) {
    	byte[] val = null;
    	// Note byte API call requires DirectPosition2D
    	val = coverage.evaluate(new DirectPosition2D(coord1.x, coord1.y), val);
    	objval = ArrayUtils.toObject(val);
    	dval = val[0];
    }
    else if (clazz == Short.class) {
    	int[] val = null;
    	// Note API doesn't support short or ushort, so use int
    	val = coverage.evaluate(new Point2D.Double(coord1.x, coord1.y), val);
    	objval = ArrayUtils.toObject(val);
    	dval = val[0];
    }
    
   
    
    Category cat = coverage.getSampleDimension(0).getCategory(dval);
    Unit<?> unit = coverage.getSampleDimension(0).getUnits();

    String categoryName = null;
//    String unitName = UnitFormat.getInstance(Locale.getDefault()).format(unit);
    if (cat != null) {
    	categoryName = cat.getName().toString(Locale.getDefault()); 	
    }
    
    System.out.println("Type: " + clazz.getSimpleName() + " " + categoryName + " " + objval[0]);
    
    // TODO Test Write
    
		PlanarImage pi = (PlanarImage)coverage.getRenderedImage();
		BufferedImage image = pi.getAsBufferedImage();
		
		System.out.println(image.toString() + "\n");
		
		// Write an output image for visual inspection.  
		try {
			ImageIO.write(image, "gif", new File("test/data/generatedCoverage_" + clazz.getSimpleName() + ".gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
