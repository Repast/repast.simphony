package repast.simphony.space.gis.valuelayer;

import java.io.File;
import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import junit.framework.TestCase;
import repast.simphony.space.gis.GeoRasterLayer;

/**
 * Tests for GeoRasterLayer API
 * 
 * @author Eric Tatara
 *
 */
public class GeoRasterLayerTests extends TestCase {

	@Override
	public void setUp() {

	}

	/**
	 * Create a GeoRasterLayer from a GeoTiff file and test various API calls.
	 */
	public void testCreateFromFile(){
		File file = new File("test/data/craterlake-imagery-30m.tif");
		
		GeoRasterLayer layer = new GeoRasterLayer("GRL Test 1", file);
		
		System.out.println(layer.getGridCoverage().getEnvelope2D().getLowerCorner());
		System.out.println(layer.getGridCoverage().getEnvelope2D().getUpperCorner());
		
		
		System.out.println("Layer source CRS: " + layer.getCRS());
		
		// Test if the layer has a valid CRS
		ReferenceIdentifier ref = layer.getCRS().getIdentifiers().iterator().next();
		CoordinateReferenceSystem crs1 = null;
		try {
			crs1 = CRS.decode(ref.toString(), false);
		} catch (FactoryException e) {
			e.printStackTrace();
		}
		assertNotNull(crs1);
		assertTrue(layer.isWritable());
		
		// Test layer CRS re-project
//		layer.setCRS(DefaultGeographicCRS.WGS84);	
//		System.out.println("Layer re-project CRS: " + layer.getCRS());
//		
//		assertEquals(DefaultGeographicCRS.WGS84, layer.getCRS());
//		assertTrue(layer.isWritable());
//		assertEquals("GRL Test 1", layer.getName());
		
		
		// TODO other operations besides re-project to make sure wrapping with
		//      WritableGridCoverage2D is robust to various processing steps
		
		// TODO read data
		
		// Crater lake center lon = -122.1, lat = 42.94 degrees
		double lon = -122.1; 
		double lat = 42.94;
		
//		System.out.println(layer.getValue(lon, lat).getClass().getName());
		
		// TODO write data
		
		// TODO test read/write after re-project
		
		// TODO serialize
	}
	
	/**
	 * Create a GeoRasterLayer from a GridCoverage2D and test various API calls.
	 */
	public void testCreateFromCoverage(){
		
		File file = new File("test/data/craterlake-imagery-30m.tif");

		AbstractGridFormat format = GridFormatFinder.findFormat(file);
		GridCoverage2DReader reader = format.getReader(file);
		GridCoverage2D coverage = null;
		try {
			coverage = reader.read(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		GeoRasterLayer layer = new GeoRasterLayer(coverage);
		
		// Test if the layer has a valid CRS
		ReferenceIdentifier ref = layer.getCRS().getIdentifiers().iterator().next();
		CoordinateReferenceSystem crs1 = null;
		try {
			crs1 = CRS.decode(ref.toString(), false);
		} catch (FactoryException e) {
			e.printStackTrace();
		}
		assertNotNull(crs1);
		assertEquals("craterlake-imagery-30m", layer.getName());
		
		// Should not be writable
		assertFalse(layer.isWritable());
		
		// Now create writable layer
		layer = new GeoRasterLayer("A Layer", coverage, true);
		assertEquals("A Layer", layer.getName());
		assertTrue(layer.isWritable());
		
		// Test layer CRS re-project
		layer.setCRS(DefaultGeographicCRS.WGS84);	
		assertEquals(DefaultGeographicCRS.WGS84, layer.getCRS());
		assertTrue(layer.isWritable());
		
		
		// TODO read data
		
		// TODO write data
		
		// TODO test read/write after re-project
		
		// TODO serialize
	}
}
