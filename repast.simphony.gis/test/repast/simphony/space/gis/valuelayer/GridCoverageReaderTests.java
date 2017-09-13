package repast.simphony.space.gis.valuelayer;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.DataSourceException;
import org.geotools.gce.arcgrid.ArcGridFormatFactory;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffFormatFactorySpi;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.image.WorldImageFormatFactory;
import org.opengis.coverage.grid.GridCoverageReader;

import junit.framework.TestCase;

/**
 * Test if various coverage format (e.g. GeoTiff) readers are available on the
 * classpath and can read test files correctly.  Does not test for data correctness,
 * only if the reader returns a non-null coverage object from the file.
 * 
 * TODO additional file formats
 * 
 * @author Eric Tatara
 *
 */
public class GridCoverageReaderTests extends TestCase {

	File geoTiffFile1, geoTiffFile2, geoTiffFile3;
	File arcGridFile, arcGridFileZip;
	File jpegWorldFile, tiffWorldFile, pngWorldFile;
	
	@Override
	public void setUp() {
		geoTiffFile1 = new File("test/data/UTM2GTIF.TIF");
		geoTiffFile2 = new File("test/data/SP27GTIF.TIF");
		geoTiffFile3 = new File("test/data/craterlake-imagery-30m.tif");
		
		arcGridFile = new File("test/data/ArcGrid.asc");
		arcGridFileZip = new File("test/data/spearfish.asc.gz");
		
		jpegWorldFile = new File("test/data/RGBTestPattern.jpg");
		pngWorldFile = new File("test/data/RGBTestPattern.png");
		tiffWorldFile = new File("test/data/RGBTestPattern.tif");
	}

	/**
	 * Test the GeoTools GeoTiff reader directly to verify the classpath is 
	 * configured with the correct GeoTool jars.
	 * 
	 */
	public void testGTGeoTiffReader(){
		readGTGeoTiff(geoTiffFile1);
		readGTGeoTiff(geoTiffFile2);
		readGTGeoTiff(geoTiffFile3);
	}
	
	/**
	 * Test the GeoTools ArcGrid reader directly to verify the classpath is 
	 * configured with the correct GeoTool jars.
	 * 
	 */
	public void testGTArcGridReader() {
		readGTArcGrid(arcGridFile);
		readGTArcGrid(arcGridFileZip);
	}
	
	/**
	 * Test the GeoTools GridFormatFinder to read a files file to verify the 
	 * classpath is configured with the correct GeoTool jars.
	 * 
	 */
	public void testGTFactoryFinder() {
		readGTFactoryFinder(geoTiffFile1);
		readGTFactoryFinder(geoTiffFile2);
		readGTFactoryFinder(geoTiffFile3);
		
		readGTFactoryFinder(arcGridFile);
//		readGTFactoryFinder(arcGridFileZip); // Finder doesnt support zip
		
		readGTFactoryFinder(jpegWorldFile);
		readGTFactoryFinder(pngWorldFile);
		readGTFactoryFinder(tiffWorldFile);
	}

	public void readGTGeoTiff(File file){
		
		GeoTiffReader reader = null;
		try {
			reader = new GeoTiffReader(file);
		} catch (DataSourceException e1) {
			e1.printStackTrace();
		}
	
		GridCoverage2D coverage = null;
		try {
			coverage = reader.read(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull(coverage);
	}
	
	public void readGTArcGrid(File file){
		
		GridCoverage2D coverage = null;
		try {
			GridCoverageReader reader = new ArcGridReader(file);
			coverage = (GridCoverage2D) reader.read(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(coverage);
	}
	
	public void readGTFactoryFinder(File file){

		AbstractGridFormat format = GridFormatFinder.findFormat(file);
		GridCoverage2DReader reader = format.getReader(file);
		GridCoverage2D coverage = null;
		try {
			coverage = reader.read(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull(coverage);
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
		boolean worldFileAvailable = false;
		
		for (GridFormatFactorySpi f : formats){
			
			System.out.println("Found GridFormatFinder on classpath: " + f.toString());
			
			if (f instanceof GeoTiffFormatFactorySpi){
				geoTiffAvailable = true;
			}
			
			else if (f instanceof ArcGridFormatFactory){
				arcGridAvailable = true;
			}
			
			else if (f instanceof WorldImageFormatFactory) {
				worldFileAvailable = true;
			}
		}

		assertTrue(geoTiffAvailable);
		assertTrue(arcGridAvailable);
		assertTrue(worldFileAvailable);
	}
}
