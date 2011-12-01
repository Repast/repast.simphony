package repast.simphony.gis.styleEditor;

import org.geotools.styling.Symbolizer;

/**
 * Interface for classes that create Symoblizers.
 *
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2007/04/18 19:25:53 $
 */
public interface SymbolizerFactory {

	public Symbolizer createSymbolizer();
}
