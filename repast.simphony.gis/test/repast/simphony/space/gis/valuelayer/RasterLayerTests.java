package repast.simphony.space.gis.valuelayer;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import junit.framework.TestCase;
import repast.simphony.space.gis.RasterLayer;

/**
 * Tests for RasterLayer API
 * 
 * @author Eric Tatara
 *
 */
public class RasterLayerTests extends TestCase {

	public static final String SAMPLE_GEOTIFF = "test/data/sample.tiff";

	GridCoverage2D coverage;
	
	// Test lat/lon pair describe a rectable over downtown Chicago.  (x1,y) is the
	//   lower left corner and (x2,y2) is the upper left corner.
	double lon1 = -87.668;  // x1
	double lon2 = -87.582;  // x2
	
	double lat1 = 41.834;  // y1
	double lat2 = 41.916;  // y2
	
	
	@Override
	public void setUp() {
		try {
			generateSampleGeoTiff();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private void generateSampleGeoTiff() throws Exception {

		File file = new File(SAMPLE_GEOTIFF);

		GridCoverageFactory factory = new GridCoverageFactory( new Hints(Hints.TILE_ENCODING, "raw"));

		// Generate an image with data such that the 
		int height = 200;
		int width = 100;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);

		// Alt method for creating single band raster / image
//		WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_BYTE, width, height, 1, null);
		
		WritableRaster raster =(WritableRaster) image.getData();
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
			// Create a black and white checker board pattern image
				raster.setSample(x, y, 0, (int)( 1+(Math.sin(x)*Math.cos(y))*256));
//				raster.setSample(x, y, 1, (int)( 1+(Math.sin(x)*Math.cos(y))*256));
//				raster.setSample(x, y, 2, (int)( 1+(Math.sin(x)*Math.cos(y))*256));
				// Just set to zero
//				raster.setSample(x, y, 0, 0);
			}
		}
		image.setData(raster); // Assign the raster back to the image
		
		CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
		
		//  DefaultGeographicCRS.WGS84 axis order is lon,lat (x,y) 
		ReferencedEnvelope envelope = new ReferencedEnvelope(lat1, lat2, lon1, lon2,  crs);
		
		// Alt method using raster directly
//	  GridCoverage gc = factory.create("test", raster, envelope);
		
		
		
//		File file2 = new File("test/data/UTM2GTIF.TIF");
//		
//		AbstractGridFormat format = GridFormatFinder.findFormat(file2);
//		GridCoverage2DReader reader = format.getReader(file2);
//		GridCoverage2D coverage2 = null;
//		try {
//			coverage2 = reader.read(null);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		// TODO check the sample dimensions if causing issue in display
		
		coverage = factory.create("Test Coverage 123", image, envelope);
		
		GeoTiffWriter writer = new GeoTiffWriter(file);
		writer.write(coverage, null);
		writer.dispose();
		
		// Check the raster image
		PlanarImage pi = (PlanarImage)coverage.getRenderedImage();
		ImageIO.write(pi.getAsBufferedImage(), "png", new File("test/data/sample.png"));
		
	}
	
	/**
	 * Create a GeoRasterLayer from a GeoTiff file and test various API calls.
	 */
	public void testCreateFromFile(){
//		File file = new File("test/data/craterlake-imagery-30m.tif");
//		File file = new File("test/data/UTM2GTIF.TIF");
		File file = new File(SAMPLE_GEOTIFF);
			
		RasterLayer layer = new RasterLayer("GRL Test 1", file);
	
		// Test layer CRS
		assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, layer.getCRS()));
		
		assertTrue(layer.isWritable());	
		
		// Try to set the layer to WGS84 which it should already be set to.  The 
		//   layer.setCRS() should catch this otherwise it can cause an exception.
		layer.setCRS(DefaultGeographicCRS.WGS84);
		
		// Test layer CRS
			assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, layer.getCRS()));
		
		CoordinateReferenceSystem crsNAD83 = null;
		try {
			crsNAD83 = CRS.decode("EPSG:4269", false);
		} catch (FactoryException e) {
			e.printStackTrace();
		}
		
		// Test layer CRS re-project
		layer.setCRS(crsNAD83);	
		
		assertTrue(CRS.equalsIgnoreMetadata(crsNAD83, layer.getCRS()));
//		assertEquals(DefaultGeographicCRS.WGS84, layer.getCRS());
//		assertTrue(layer.isWritable());
		
		
		layer.setCRS(DefaultGeographicCRS.WGS84);
		
		
		assertEquals("GRL Test 1", layer.getName());
		
		
		// TODO other operations besides re-project to make sure wrapping with
		//      WritableGridCoverage2D is robust to various processing steps
		
		// TODO read data
		
		// Crater lake center lon = -122.1, lat = 42.94 degrees
		double lat = 42.94;
		double lon = -122.1; 
		
//		System.out.println(layer.getValue(lon, lat).getClass().getName());
		
		// TODO write data
		
		// TODO test read/write after re-project
		
		// TODO serialize
	}
	
	/**
	 * Create a GeoRasterLayer from a GridCoverage2D and test various API calls.
	 */
	public void testCreateFromCoverage(){
				
		assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, coverage.getCoordinateReferenceSystem()));
		
		RasterLayer layer = new RasterLayer(coverage);
		
		assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, layer.getCRS()));
		
		assertNotNull(layer.getCRS());
		
		// TODO check what layer/coverage name should be
		assertEquals("Test Coverage 123", layer.getName());
		
		// Should not be writable
//		assertFalse(layer.isWritable());
		
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
}
