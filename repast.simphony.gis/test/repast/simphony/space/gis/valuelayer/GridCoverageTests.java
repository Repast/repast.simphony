package repast.simphony.space.gis.valuelayer;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.processing.Operations;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

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
		// Roughly middle of coverage envelope, should be around 10.0
		int[] val = (int[])coverage.evaluate(new DirectPosition2D(crs, -95, 39));
		assertEquals(val[0], 8);
		
		// Roughly upper left corner of coverage envelope, should be around 0.0
		val = (int[])coverage.evaluate(new DirectPosition2D(crs, -122.87, 47.01));
		assertEquals(val[0], 0);
		
		// Roughly bottom right corner of coverage envelope, should be around 20.0
		val = (int[])coverage.evaluate(new DirectPosition2D(crs, -65.65, 25.27));
		assertEquals(val[0], 18);
		
		// Test if coverage is writable
		assertTrue(coverage.isDataEditable());
		
		System.out.println(coverage.getRenderedImage().getClass().getName());
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

		int width  = 10;
		int height = 10;
		
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
}
