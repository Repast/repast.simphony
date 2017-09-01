package repast.simphony.space.gis.valuelayer;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.awt.image.WritableRenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.measure.unit.SI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.iterator.WritableRandomIter;

import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.coverage.processing.Operations;
import org.geotools.factory.Hints;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
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
		
		GridCoverage2D  coverage = null;
 
		// TODO Category Color (rangees)
		
		Color[] temperatureColorScale = new Color[256];
		
		// white to red color scale
		for (int i=0; i<256; i++) {
			temperatureColorScale[i] = new Color(255, 255-i, 255-i); 
		}
		
    Category[] categories	= new Category[] {	
        new Category("No data",     null, 0),
        new Category("Land",        Color.decode("#91ce84"), 1),
        new Category("Cloud",       Color.decode("#e5f8fc"), 2),
        new Category("Temperature", temperatureColorScale, 3, 256)
    };
    
    // Single band
    GridSampleDimension[] bands = new GridSampleDimension[] {
    		new GridSampleDimension("Temperature", categories, SI.CELSIUS)
    };
    
    
//    BufferedImage image  = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);
//    
//    WritableRaster raster = image.getRaster();
    
    WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_BYTE,
        width, height, 1, null);
    
    for (int i=0; i<raster.getWidth(); i++) {
    	for (int j=0; j<raster.getHeight(); j++) {
    		raster.setSample(i,j,0, (i+j));
       }
    }
    
    //  DefaultGeographicCRS.WGS84 axis order is lon,lat (x,y) 
		ReferencedEnvelope envelope = new ReferencedEnvelope(lon1, lon2, lat1, lat2, 
				DefaultGeographicCRS.WGS84);
		
		Hints hints = new Hints(Hints.TILE_ENCODING, "raw");
    GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(hints);
    
    coverage = factory.create("Test", raster, envelope, bands);
    
//    coverage = factory.create("Test", image, envelope, bands, null, null);
    
    // TODO Test Read
    
    int[] val = null;
    val = coverage.evaluate(new Point2D.Double(coord1.x, coord1.y), val);
    
    Category cat = coverage.getSampleDimension(0).getCategory(val[0]);
    
    System.out.println(val[0] + " : " + cat.getName());
    
    // TODO Test Write
    
    WritableRenderedImage writableImage = (WritableRenderedImage)coverage.getRenderedImage();
    
    WritableRandomIter writeIter = RandomIterFactory.createWritable(writableImage, null);
    int dataType = writableImage.getSampleModel().getDataType();
    
    System.out.println(dataType);
    
    // Write directly to the grid
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
    
    double dval[] = null;
    dval = coverage.evaluate(new Point2D.Double(coord1.x, coord1.y), dval);
    cat = coverage.getSampleDimension(0).getCategory(dval[0]);
    
    System.out.println(dval[0] + " : " + cat.getName());
    
    // TODO serialize / image
    
		PlanarImage pi = (PlanarImage)coverage.getRenderedImage();
		try {
			ImageIO.write(pi.getAsBufferedImage(), "gif", new File("test/data/temperatureIndexed.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
