package repast.simphony.gis.display;

import edu.umd.cs.piccolo.event.PInputEvent;
import org.geotools.feature.Feature;

public interface ProbeHandler {

	public void handleFeatureProbe(Feature feature, PInputEvent ev);
}
