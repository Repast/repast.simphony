package repast.simphony.space.gis;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.processing.Operations;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.coverage.Coverage;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class RasterLayer {

	protected String name;
	
	protected GridCoverage2D gridCoverage;
	
	/**
	 * Create the RasterLayer from the provided GridCoverage2D.  This is useful
	 * when creating GridCoverage2D from other readers or data types.  If the
	 * GridCoverage2D is not writable, attempt to write to this GeoRasterLayer
	 * will cause an error.
	 * 
	 * @param name
	 * @param gridCoverage
	 * @param writable
	 */
	public RasterLayer(String name, GridCoverage2D gridCoverage, boolean writable){
		
		if (writable){
			// Wrap the read coverage in a writable object 
			gridCoverage = new WritableGridCoverage2D(gridCoverage);
		}
		
		this.gridCoverage = gridCoverage;
		
		if (name != null){
			this.name = name;
		}
		else {
			this.name = gridCoverage.getName().toString(Locale.getDefault());
		}
		
	}
	
	public RasterLayer(GridCoverage2D gridCoverage){
		this(null, gridCoverage, false);
	}
	
	/**
	 * Create the RasterLayer using a raster file.  The coverage returned by
	 * the file reader will be wrapped with a WritableGridCoverage instance so
	 * that this layer can be written to.
	 * 
	 * @param name the name of the GeoValueLayer
	 * @param rasterfile a georeferenced raster file such as GeoTif
	 */
	public RasterLayer(String name, File rasterfile){
		this.name = name;
		
		AbstractGridFormat format = GridFormatFinder.findFormat(rasterfile);
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
		
		// TODO GIS test performance of WritableGridCoverage2D vs GridCoverage2D
	}
	
	// TODO need more than name for constructor
	public RasterLayer(String name){
		 GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
		 
		 // TODO GIS create the coverage from scratch, see:
		 // https://github.com/geotools/geotools/blob/master/modules/library/coverage/src/test/java/org/geotools/coverage/grid/GridCoverageTestBase.java
		 
		 Category[] categories;
     CoordinateReferenceSystem crs;
     Rectangle2D bounds;
     GridSampleDimension[] bands; 
		
     crs  = DefaultGeographicCRS.WGS84;
     categories = new Category[1];
     
     // TODO GIS set bounds
     bounds = new Rectangle2D.Double(35, -41, 45, 46);
     
     // Single band
     bands = new GridSampleDimension[] {
    		 
    		 new GridSampleDimension("Measure")  // name?
    	
    		 // TODO GIS set units and categories ?
//         new GridSampleDimension("Measure", categories, SI.CELSIUS)
    		 };
     
     GeneralEnvelope envelope = new GeneralEnvelope(bounds);
     envelope.setCoordinateReferenceSystem(crs);
	
     
     int width  = 500;
     int height = 500;
     BufferedImage image= new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
     WritableRaster raster =(WritableRaster) image.getData();
     
     // raster
     gridCoverage = factory.create(name, raster, envelope);
     
     // image
     gridCoverage = factory.create(name, image, envelope);
	
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

		// Operations resample returns a 
		// org.geotools.coverage.processing.operation.Resampler2D
    // so we need to create a new writable coverage if the original coverage
		// is an instance of WritableGridCoverage2D
		
		if (gridCoverage instanceof WritableGridCoverage2D){
			gridCoverage = new WritableGridCoverage2D((GridCoverage2D)cov);
		}
		else{
			gridCoverage = (GridCoverage2D)cov;
		}
	}
	
	public boolean isWritable(){
		return gridCoverage.isDataEditable();
	}

	public String getName() {
		return name;
	}

	public GridCoverage2D getGridCoverage() {
		return gridCoverage;
	}
	
	// TODO default way to get value if not specified?
	
	public Object getValue(double x, double y){
	
		return gridCoverage.evaluate(new DirectPosition2D(
				gridCoverage.getCoordinateReferenceSystem(), x, y));
	}
	
}
