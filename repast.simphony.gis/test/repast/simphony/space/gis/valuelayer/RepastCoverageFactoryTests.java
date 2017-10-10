package repast.simphony.space.gis.valuelayer;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;

import junit.framework.TestCase;
import repast.simphony.space.gis.RasterLayer;
import repast.simphony.space.gis.RepastCoverageFactory;

/**
 * Tests for the RepastCoverageFactory.  Considers all general GridCoverage
 * operations like creating in memory, creating from file, read, write, etc.
 * 
 * @author Eric Tatara
 *
 * TODO GIS test creating different data types and writing different types (eg float, 
 *       double, int) to each to make sure we dont have any cast Exception in the
 *       WritableCoverage class.  Better yet, put those in a separate Test
 */
public class RepastCoverageFactoryTests extends TestCase {

	public static final String TEST_CRS_CODE = "EPSG:4326";  // WGS84
	
	// RGB world file checker pattern Black - Red - Green - Blue - White
	public static final String SAMPLE_PNG_GRB = "test/data/RGBTestPattern.png";
	
	// Grayscale sample GeoTiff
	public static final String SAMPLE_GEOTIFF_GRAY = "test/data/sample_gray.tif";

	GridCoverage2D coverage1;  // grayscale (single band) coverage
	
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
		try {
			generateSampleGeoTiff();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	/**
	 * Create a sample grayscale GeoTiff file we can use for testing
	 * @throws Exception
	 */
	private void generateSampleGeoTiff() throws Exception {

		File file = new File(SAMPLE_GEOTIFF_GRAY);

		GridCoverageFactory factory = new GridCoverageFactory( new Hints(Hints.TILE_ENCODING, "raw"));

		// Generate an image with data such that the 
		int height = 400;
		int width = 200;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);

		// Alt method for creating single band raster / image
//		WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_BYTE, width, height, 1, null);
		
		WritableRaster raster =(WritableRaster) image.getData();
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
			// Create a black and white checker board pattern image
				raster.setSample(x, y, 0, (int)( 1+(Math.sin(x/10)*Math.cos(y/10))*256));
//				raster.setSample(x, y, 1, (int)( 1+(Math.sin(x)*Math.cos(y))*256));
//				raster.setSample(x, y, 2, (int)( 1+(Math.sin(x)*Math.cos(y))*256));
				// Just set to zero
//				raster.setSample(x, y, 0, 0);
			}
		}
		image.setData(raster); // Assign the raster back to the image
		
		// Use the EPSG version of WGS84 since DefaultGeographicCRS.WGS84 is missing
		// some meta-data used by GeoTiff
		CoordinateReferenceSystem crs = CRS.decode(TEST_CRS_CODE);
		
		ReferencedEnvelope envelope = new ReferencedEnvelope(lat1, lat2, lon1, lon2, crs);
		
		// Alt method using raster directly
//	  GridCoverage gc = factory.create("test", raster, envelope);
				
		// Coverage is stored for later tests
		coverage1 = factory.create(file.getName(), image, envelope);
		
		GeoTiffWriter writer = new GeoTiffWriter(file);
		writer.write(coverage1, null);
		writer.dispose();
		
		// Output the image as PNG for visual inspection
		PlanarImage pi = (PlanarImage)coverage1.getRenderedImage();
		ImageIO.write(pi.getAsBufferedImage(), "png", new File("test/data/sample.png"));
	}
	
	/**
	 * Create a RasterLayer from a GeoTiff file and test various API calls.
	 */
	public void testCreateFromFile() throws Exception{
		File file = new File(SAMPLE_GEOTIFF_GRAY);
			
		RasterLayer layer = new RasterLayer(file.getName(), file, true);
		
		assertEquals(file.getName(), layer.getName());
		assertEquals(1, layer.getNumBands());
		
		assertTrue(layer.getGridCoverage().isDataEditable());
		
		// Test layer CRS
		assertTrue(CRS.equalsIgnoreMetadata(CRS.decode(TEST_CRS_CODE), layer.getCRS()));
		
		// Set the layer to DefaultGeographicCRS.WGS84 
		layer.setCRS(DefaultGeographicCRS.WGS84);
		
		// Test layer CRS
		assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, layer.getCRS()));
		
		// Test layer CRS re-project to NAD83
		CoordinateReferenceSystem crsNAD83 = CRS.decode("EPSG:4269", false);;
		layer.setCRS(crsNAD83);	
		
		assertTrue(CRS.equalsIgnoreMetadata(crsNAD83, layer.getCRS()));
		
//		layer.setCRS(DefaultGeographicCRS.WGS84);
		
		
		// TODO other operations besides re-project to make sure wrapping with
		//      WritableGridCoverage2D is robust to various processing steps
		
		// TODO read data from known point inside envelope
		
		// Crater lake center lon = -122.1, lat = 42.94 degrees
		double lat = 42.94;
		double lon = -122.1; 
		
//		System.out.println(layer.getValue(lon, lat).getClass().getName());
		
		// TODO write data
		
		// TODO test read/write after re-project
		
		// TODO serialize
	}
	
	/**
	 * Create a RasterLayer from a PNG World file and test various API calls.
	 */
	public void testCreateFromFile2() throws Exception{
		File file = new File(SAMPLE_PNG_GRB);
			
		GridCoverage2D layer = RepastCoverageFactory.createCoverageFromFile(file, true);
		
//		RasterLayer layer = new RasterLayer(file.getName(), file, true);
		
//		assertEquals(file.getName(), layer.getName());
		
		// Three band RGB
		assertEquals(3, layer.getNumSampleDimensions());
		
		// Test layer CRS
		assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, layer.getCoordinateReferenceSystem()));
		
		// Test layer CRS re-project to NAD83
//		CoordinateReferenceSystem crsNAD83 = CRS.decode("EPSG:4269", false);;
//		layer.setCRS(crsNAD83);	
//		
//		assertTrue(CRS.equalsIgnoreMetadata(crsNAD83, layer.getCRS()));
		
		
		// TODO other operations besides re-project to make sure wrapping with
		//      WritableGridCoverage2D is robust to various processing steps
		
		// TODO read data from known point inside envelope
		
		// Red point should have large Red pixel content and low B,G pixel content
		double lat = 41.8686;
		double lon = -87.8197;

		int[] val = null;
		val = layer.evaluate(new Point2D.Double(lon,lat), val);

		assertTrue(val[0] > 200);  // R
		assertTrue(val[1] < 50);   // G
		assertTrue(val[2] < 50);   // B

		// Black point should have low R,G,B pixel content
		lat = 41.8706;
		lon = -87.8544;

		val = null;
		val = layer.evaluate(new Point2D.Double(lon,lat), val);

		assertTrue(val[0] < 50);   // R
		assertTrue(val[1] < 50);   // G
		assertTrue(val[2] < 50);   // B
		
//		System.out.println(layer.getValue(lon, lat).getClass().getName());
		
		// TODO write data
		
		// TODO test read/write after re-project
		
		// TODO serialize
	}
	
	/**
	 * Create a RasterLayer from a GridCoverage2D and test various API calls.
	 */
	public void testCreateFromCoverage() throws Exception{
		
		File file = new File(SAMPLE_GEOTIFF_GRAY);  // only need for the name
		
		RasterLayer layer = new RasterLayer(coverage1);
		
		assertEquals(file.getName(), layer.getName());
		assertTrue(CRS.equalsIgnoreMetadata(CRS.decode(TEST_CRS_CODE), layer.getCRS()));
		assertEquals(1, layer.getNumBands());
		
		// Now create writable layer
//		layer = new RasterLayer("A Layer", coverage, true);
//		assertEquals("A Layer", layer.getName());
//		assertTrue(layer.isWritable());
		
		// Test layer CRS re-project
		// Create a NAD 1983 CRS, unit = degree, axis order = lat,lon (NORTH_EAST)
		CoordinateReferenceSystem crsNAD83 = null;
		try {
			crsNAD83 = CRS.decode("EPSG:4269", false);
		} catch (FactoryException e) {
			e.printStackTrace();
		}
		
		layer.setCRS(crsNAD83);	
		assertTrue(CRS.equalsIgnoreMetadata(crsNAD83, layer.getCRS()));

//		assertTrue(layer.isWritable());
		
		// TODO read data before and after CRS transforms
		
//		int[] val = (int [])layer.getValue(lon1+0.01, lat1+0.01);
//		
//		System.out.println(val[0]);
//		
//    val = (int [])layer.getValue(lon2-0.01, lat2-0.01);
//		
//		System.out.println(val[0]);
		
//		assertEquals(0d, val[0]);
	
		// TODO write data
		
		// TODO test read/write after re-project
		
		// TODO serialize
	}
	
	// Create a RasterLayer programmatically
	public void testCreateFromCode() throws Exception {
		CoordinateReferenceSystem crs = CRS.decode(TEST_CRS_CODE);
		
		//  DefaultGeographicCRS.WGS84 axis order is lon,lat (x,y) 
		ReferencedEnvelope envelope = new ReferencedEnvelope(lon1, lon2, lat1, lat2,   crs);
		RasterLayer layer = new RasterLayer("A Layer", 200, 100, envelope);
		
		assertEquals("A Layer", layer.getName());
		assertEquals(1, layer.getNumBands());
		
		assertTrue(CRS.equalsIgnoreMetadata(CRS.decode(TEST_CRS_CODE), layer.getCRS()));
		
		// TODO read data
		// TODO write data
		
		// Note data type of gridcoverage is USHORT
		layer.setWorldValue(coord1.x, coord1.y, 10);
		
		assertEquals(10.0, layer.getDoubleWorldValue(coord1.x, coord1.y));
		assertEquals(10, layer.getIntegerWorldValue(coord1.x, coord1.y));
		assertEquals(10.0f, layer.getFloatWorldValue(coord1.x, coord1.y));
		
		// TODO transform CRS
		// TODO serialize
	}
}
