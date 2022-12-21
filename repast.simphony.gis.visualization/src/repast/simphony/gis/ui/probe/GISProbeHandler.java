package repast.simphony.gis.ui.probe;

import org.locationtech.jts.geom.Envelope;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.util.PBounds;

import repast.simphony.gis.tools.AbstractMarqueeHandler;
import repast.simphony.visualization.gis.DisplayGIS;

/**
 * Handler for creating probes.
 *
 * @author Nick Collier
 * 
 * @deprecated 2D piccolo based code is being removed
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
