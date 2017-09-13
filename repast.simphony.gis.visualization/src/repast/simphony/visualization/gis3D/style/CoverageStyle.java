package repast.simphony.visualization.gis3D.style;

import org.geotools.styling.RasterSymbolizer;

public interface CoverageStyle<T> {

	public RasterSymbolizer getSymbolizer();
	
	public boolean isSmoothed();
	
	public double getOpacity();
	
}
