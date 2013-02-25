package repast.simphony.gis.display;

import org.opengis.feature.simple.SimpleFeature;

import edu.umd.cs.piccolo.event.PInputEvent;

public interface ProbeHandler {

	public void handleFeatureProbe(SimpleFeature feature, PInputEvent ev);
}
