package repast.simphony.space.gis.valuelayer;

import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.gce.arcgrid.ArcGridFormatFactory;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffFormatFactorySpi;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.opengis.coverage.grid.GridCoverageReader;

import junit.framework.TestCase;

public class GridCoverageReaderTests extends TestCase {

	@Override
	public void setUp() {

	}

	/**
	 * Test the GeoTools GeoTiff reader directly to verify the classpath is 
	 * configured with the correct GeoTool jars.
	 * 
	 */
	public void testGTGeoTiffReader(){
//		File file = new File("test/data/bogota.tif");
		File file = new File("test/data/sst_io.bin.20170305.tif");
		
		
		GeoTiffReader reader = null;
		try {
			reader = new GeoTiffReader(file, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
		} catch (DataSourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		GridCoverage2D coverage = null;
		try {
			coverage = reader.read(null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertNotNull(coverage);
		
		System.out.println(file.getName() + " editable? " +coverage.isDataEditable());
	}
	
	/**
	 * Test the GeoTools ArcGrid reader directly to verify the classpath is 
	 * configured with the correct GeoTool jars.
	 * 
	 */
	public void testGTArcGridReader(){
		File file = new File("test/data/ArcGrid.asc");
	
		GridCoverage2D coverage = null;
		try {
			GridCoverageReader reader = new ArcGridReader(file);
			coverage = (GridCoverage2D) reader.read(null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertNotNull(coverage);
		
		System.out.println(file.getName() + " editable? " +coverage.isDataEditable());
	}
	
	/**
	 * Test the GeoTools ArcGrid reader using a gzip file directly to verify the 
	 * classpath is configured with the correct GeoTool jars.
	 * 
	 */
	public void testGTArcGridReaderGZip(){
		File file = new File("test/data/spearfish.asc.gz");
	
		GridCoverage2D coverage = null;
		try {
			GridCoverageReader reader = new ArcGridReader(file);
			coverage = (GridCoverage2D) reader.read(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertNotNull(coverage);
		
		System.out.println(file.getName() + " editable? " +coverage.isDataEditable());
	}

	/**
	 * Test the GeoTools GridFormatFinder to read a GeoTiff file to verify the 
	 * classpath is configured with the correct GeoTool jars.
	 * 
	 */
	public void testGTFactoryFinderGeoTiff(){
		File file = new File("test/data/craterlake-imagery-30m.tif");

		AbstractGridFormat format = GridFormatFinder.findFormat(file);
		GridCoverage2DReader reader = format.getReader(file);
		GridCoverage2D coverage = null;
		try {
			coverage = reader.read(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertNotNull(coverage);
	
		System.out.println(file.getName() + " editable? " +coverage.isDataEditable());
	}
	
	/**
	 * Test the GeoTools GridFormatFinder to read available expected Factory 
	 * implementations to verify classpath is configured with the correct GeoTool jars.
	 * 
	 */
	public void testGTGridFormatFinder(){
	
		GridFormatFinder.scanForPlugins();

		Set<GridFormatFactorySpi> formats = GridFormatFinder.getAvailableFormats();

		boolean geoTiffAvailable = false;
		boolean arcGridAvailable = false;
		
		for (GridFormatFactorySpi f : formats){
			if (f instanceof GeoTiffFormatFactorySpi){
				geoTiffAvailable = true;
			}
			
			else if (f instanceof ArcGridFormatFactory){
				arcGridAvailable = true;
			}
		}

		assertTrue(geoTiffAvailable);
		assertTrue(arcGridAvailable);
	}
}
