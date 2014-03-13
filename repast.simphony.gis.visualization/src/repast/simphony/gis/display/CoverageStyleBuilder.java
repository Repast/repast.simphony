package repast.simphony.gis.display;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.style.ContrastMethod;

/**
 * Builds various types of Styles for GridGoverages.
 * 
 * @author Eric Tatara
 *
 */
public class CoverageStyleBuilder {

	private StyleFactory sf = CommonFactoryFinder.getStyleFactory();
  private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
	
  public CoverageStyleBuilder(){
  	
  }
  
	/**
   * This method examines the names of the sample dimensions in the provided coverage looking for
   * "red...", "green..." and "blue..." (case insensitive match). If these names are not found
   * it uses bands 1, 2, and 3 for the red, green and blue channels. It then sets up a raster
   * symbolizer and returns this wrapped in a Style.
   *
   * @return a new Style object containing a raster symbolizer set up for RGB image
   */
	public Style buildRGBStyle(GridCoverage2D cov){
	
    // We need at least three bands to create an RGB style
    int numBands = cov.getNumSampleDimensions();
    if (numBands < 3) {
        return null;
    }
    // Get the names of the bands
    String[] sampleDimensionNames = new String[numBands];
    for (int i = 0; i < numBands; i++) {
        GridSampleDimension dim = cov.getSampleDimension(i);
        sampleDimensionNames[i] = dim.getDescription().toString();
    }
    final int RED = 0, GREEN = 1, BLUE = 2;
    int[] channelNum = { -1, -1, -1 };
    // We examine the band names looking for "red...", "green...", "blue...".
    // Note that the channel numbers we record are indexed from 1, not 0.
    for (int i = 0; i < numBands; i++) {
        String name = sampleDimensionNames[i].toLowerCase();
        if (name != null) {
            if (name.matches("red.*")) {
                channelNum[RED] = i + 1;
            } else if (name.matches("green.*")) {
                channelNum[GREEN] = i + 1;
            } else if (name.matches("blue.*")) {
                channelNum[BLUE] = i + 1;
            }
        }
    }
    // If we didn't find named bands "red...", "green...", "blue..."
    // we fall back to using the first three bands in order
    if (channelNum[RED] < 0 || channelNum[GREEN] < 0 || channelNum[BLUE] < 0) {
        channelNum[RED] = 1;
        channelNum[GREEN] = 2;
        channelNum[BLUE] = 3;
    }
    // Now we create a RasterSymbolizer using the selected channels
    SelectedChannelType[] sct = new SelectedChannelType[cov.getNumSampleDimensions()];
    ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
    for (int i = 0; i < 3; i++) {
        sct[i] = sf.createSelectedChannelType(String.valueOf(channelNum[i]), ce);
    }
    RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
    ChannelSelection sel = sf.channelSelection(sct[RED], sct[GREEN], sct[BLUE]);
    sym.setChannelSelection(sel);

    return SLD.wrapSymbolizers(sym);
	}
}