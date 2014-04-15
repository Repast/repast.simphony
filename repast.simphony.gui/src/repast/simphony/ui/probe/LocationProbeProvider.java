package repast.simphony.ui.probe;

import java.util.Map;

/**
 * Provides the location of a probed object in projections that store objects
 *   with a location, eg xyz for grid, or lat/lon for geography. 
 * 
 * @author Eric Tatara
 *
 */
public interface LocationProbeProvider {

	public Map<String,LocationProbe> getLocations(Object target);
	
}
