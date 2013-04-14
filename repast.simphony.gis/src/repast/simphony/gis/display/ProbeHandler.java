package repast.simphony.gis.display;

import org.opengis.feature.simple.SimpleFeature;

import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * Probe event handler for clicks on the Piccolo GIS canvas.
 * 
 * ---- NOTE THAT THIS IS CURRENTLY NOT USED ----	
 *    
 *   Kept in case a mouse click handler is used in the future.
 *
 */
public interface ProbeHandler {

	public void handleFeatureProbe(SimpleFeature feature, PInputEvent ev);
}
