package repast.simphony.gis.ui.probe;

import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.util.PBounds;

import repast.simphony.gis.tools.AbstractMarqueeHandler;
import repast.simphony.visualization.gis.DisplayGIS;

import com.vividsolutions.jts.geom.Envelope;

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

  @Override
  protected void execute(PInputEvent ev, PBounds rect) {
    if (rect != null) {
      Envelope env = new Envelope(rect.getMaxX(), rect.getMinX(), rect.getMaxY(), rect.getMinY());
      this.display.probe(env);
    }
  }
}
