package repast.simphony.visualization.gis;

import com.vividsolutions.jts.geom.Envelope;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PBounds;
import repast.simphony.gis.display.AbstractMarqueeHandler;

/**
 * Handler for creating probes.
 *
 * @author Nick Collier
 */
public class GISProbeHandler extends AbstractMarqueeHandler {

  private DisplayGIS display;

  public GISProbeHandler(DisplayGIS display) {
    this.display = display;
  }

  protected void execute(PInputEvent ev, PBounds rect) {
    if (rect != null) {
      Envelope env = new Envelope(rect.getMaxX(), rect.getMinX(), rect.getMaxY(), rect.getMinY());
      this.display.probe(env);
    }
  }
}
