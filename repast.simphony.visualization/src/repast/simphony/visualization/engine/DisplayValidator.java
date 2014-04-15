package repast.simphony.visualization.engine;

import java.util.List;

/**
 * Used to validate display types defined in the visualization registry.  
 *   Helpful for checking via the Display editor menus if selected display and
 *   projection types are compatible.
 * 
 * @author Eric Tatara
 *
 */
public interface DisplayValidator {

	/**
	 * Checks if the display type associated with a visualization registry data
	 *  entry supports the projection types in the provided list.   
	 *  
	 * @param selectedProjectionTypes
	 * @return
	 */
	public boolean validateDisplay(List<String>selectedProjectionTypes);
	
}
