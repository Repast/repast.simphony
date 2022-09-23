package repast.simphony.space.gis;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.processing.Operations;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.Coverage;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * RasterLayer provides Repast ValueLayer like functionality using geographic 
 * coordinates and can be displayed when part of a Geography instance.  Contains
 * an instance of a  WritableGridCoverage2D that allows read and write capabilities
 * on an underlying GridGoverage2D.  
 * 
 * Current write capability is limited to a single band.  The WritableGridCoverage2D
 * needs to be updated to allow multi-band writes.
 * 
 * TODO GIS provide multiple band write capability.
 * 
 * @author Eric Tatara
 *
 */
public class RasterLayer {

	protected String name;
	
	protected WritableGridCoverage2D gridCoverage;
	
	/**
	 * Create the RasterLayer from the provided GridCoverage2D.  This is useful
	 * when creating GridCoverage2D from other readers or data types.
	 * 
	 * @param name
	 * @param gridCoverage
	 * 
	 */
	public RasterLayer(String name, GridCoverage2D gridCoverage){
		
		this.gridCoverage = new WritableGridCoverage2D(gridCoverage);
		
		if (name != null){
			this.name = name;
		}
		else {
			this.name = gridCoverage.getName().toString(Locale.getDefault());
		}
		
	}
	
	public RasterLayer(GridCoverage2D gridCoverage){
		this(null, gridCoverage);
	}
	
	public RasterLayer(String name, File rasterfile) {
		this(name, rasterfile, false);
	}
	
	/**
	 * Create the RasterLayer using a raster file.  The coverage returned by
	 * the file reader will be wrapped with a WritableGridCoverage instance so
	 * that this layer can be written to.
	 * 
	 * @param name the name of the GeoValueLayer
	 * @param rasterfile a georeferenced raster file such as GeoTif
	 * @param forceLonLatAxisOrder forces lon,lat coordinate order on read
	 */
	public RasterLayer(String name, File rasterfile, boolean forceLonLatAxisOrder){
		
		if (name != null)
			this.name = name;
		else
			name = rasterfile.getName();
		
		Hints hints = null;
		
		if (forceLonLatAxisOrder)
			hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
				
		AbstractGridFormat format = GridFormatFinder.findFormat(rasterfile, hints);
		GridCoverage2DReader reader = format.getReader(rasterfile);
		
		GridCoverage2D readCoverage = null;
		try {
			readCoverage = reader.read(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Wrap the read coverage in a writable object so that this layer can be
		// written to.
		gridCoverage = new WritableGridCoverage2D(readCoverage);
	}
	
	
	public RasterLayer(String name, int width, int height, ReferencedEnvelope envelope){
		
		this.name = name;
		
		GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
		
		// Alt method for creating single band raster / image
//	WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_BYTE, width, height, 1, null);
		
		// raster
//		gridCoverage =  new WritableGridCoverage2D(factory.create(name, raster, envelope));

		// image
		gridCoverage =  new WritableGridCoverage2D(factory.create(name, image, envelope));

	}
	
	public CoordinateReferenceSystem getCRS(){
		return gridCoverage.getCoordinateReferenceSystem();
	}
	
	/**
	 * Setting the layer CRS will project the coverage to the new CRS.
	 * 
	 * @param crs target CoordinateReferenceSystem
	 */
	public void setCRS(CoordinateReferenceSystem crs){
		
		// Don't resample if CRS is same as current.  Also avoids an exception
		//  in Geotools that happens when CRS is DefaultGeographicCRS.WGS84
		if (CRS.equalsIgnoreMetadata(crs, gridCoverage.getCoordinateReferenceSystem())) {
			return;
		}
		
		Coverage cov = Operations.DEFAULT.resample(gridCoverage,crs);
		
		gridCoverage = new WritableGridCoverage2D((GridCoverage2D)cov);
	}
	

	public String getName() {
		return name;
	}

	public GridCoverage2D getGridCoverage() {
		return gridCoverage;
	}

	public int getNumBands() {
		return gridCoverage.getNumSampleDimensions();
	}
	
	public Object getValue(double x, double y){
		return gridCoverage.evaluate(new DirectPosition2D(
				gridCoverage.getCoordinateReferenceSystem(), x, y));
	}

	public int getIntegerGridValue(int x, int y) {
		return gridCoverage.evaluate(new GridCoordinates2D(x, y), 
				new int[getNumBands()])[0];
	}
	
	public int getFloatGridValue(int x, int y) {
		return gridCoverage.evaluate(new GridCoordinates2D(x, y), 
				new int[getNumBands()])[0];
	}
	
	public int getDoubleGridValue(int x, int y) {
		return gridCoverage.evaluate(new GridCoordinates2D(x, y), 
				new int[getNumBands()])[0];
	}
	
	public int getIntegerWorldValue(double x, double y) {
		return gridCoverage.evaluate(new Point2D.Double(x,y), new int[getNumBands()])[0];
	}
	
	public float getFloatWorldValue(double x, double y) {
		return gridCoverage.evaluate(new Point2D.Double(x,y), new float[getNumBands()])[0];
	}
	
	public double getDoubleWorldValue(double x, double y) {
		return gridCoverage.evaluate(new Point2D.Double(x,y), new double[getNumBands()])[0];
	}
	
	 
  public void setWorldValue(double x, double y, int value) {
  	gridCoverage.setValue(new DirectPosition2D(
  			gridCoverage.getCoordinateReferenceSystem(), x, y), value);
  }
  
  public void setWorldValue(double x, double y, float value) {
  	gridCoverage.setValue(new DirectPosition2D(
  			gridCoverage.getCoordinateReferenceSystem(), x, y), value);
  }
  
  public void setWorldValue(double x, double y, double value) {
  	gridCoverage.setValue(new DirectPosition2D(
  			gridCoverage.getCoordinateReferenceSystem(), x, y), value);
  }

  public void setGridValue(int x, int y, int value) {
  	gridCoverage.setValue(new GridCoordinates2D(x,y), value);
  }
  
  public void setGridValue(int x, int y, float value) {
  	gridCoverage.setValue(new GridCoordinates2D(x,y), value);
  }
  
  public void setGridValue(int x, int y, double value) {
  	gridCoverage.setValue(new GridCoordinates2D(x,y), value);
  }
}
