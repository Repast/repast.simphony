package repast.simphony.visualization.gis3D.style;

import org.geotools.styling.RasterSymbolizer;

/**
 * Default style for GIS coverage.
 *
 * @author Eric Tatara
 *
 * @param <T>
 */
public class DefaultCoverageStyle<T> implements CoverageStyle<T> {

	@Override
	public RasterSymbolizer getSymbolizer() {
		
		return null;
	}

	@Override
	public boolean isSmoothed() {
		return false;
	}

	@Override
	public double getOpacity() {
		return 1.0;
	}
}