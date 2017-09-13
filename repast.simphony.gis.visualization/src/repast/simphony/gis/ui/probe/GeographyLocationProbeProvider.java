package repast.simphony.gis.ui.probe;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;
import repast.simphony.ui.probe.LocationProbe;
import repast.simphony.ui.probe.LocationProbeProvider;
import repast.simphony.util.ContextUtils;
import repast.simphony.visualization.gis3D.CoverageProbeObject;

/**
 * Provides location information to probe panels for the probed objects in a 
 *   Geography or GridCoverage2D
 * 
 * @author Eric Tatara
 *
 */
public class GeographyLocationProbeProvider implements LocationProbeProvider {
	
	@Override
	public Map<String,LocationProbe> getLocations(Object target){
		Context<?> context = ContextUtils.getContext(target);
		Map<String,LocationProbe>probes = new HashMap<String,LocationProbe>();

		if (context != null) {
			for (Geography<?> geog : context.getProjections(Geography.class)) {
				probes.put(geog.getName(), new GeographyLocationProbe(target, geog));				
			}
		}
		
		if (target instanceof CoverageProbeObject) {
			CoverageProbeObject probeObject = (CoverageProbeObject)target;
				probes.put(probeObject.getLayerName(), new CoverageLocationProbe(probeObject));				
		}
		
		return probes;
	}
}