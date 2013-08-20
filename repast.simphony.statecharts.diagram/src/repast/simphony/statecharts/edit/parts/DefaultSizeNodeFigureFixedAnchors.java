/**
 * 
 */
package repast.simphony.statecharts.edit.parts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;

/**
 * DefaultSizeNodeFigure that has fixed anchors. These can be used as the
 * "node plate" to create fixed connnection points.
 * 
 * @source http://gmfsamples.tuxfamily.org/wiki/doku.php?id=gmf_tutorial6
 */
public class DefaultSizeNodeFigureFixedAnchors extends DefaultSizeNodeFigure {

  private Map<String, FixedConnectionAnchor> anchors = new HashMap<String, FixedConnectionAnchor>();

  public DefaultSizeNodeFigureFixedAnchors(Dimension defSize, Map<String, PrecisionPoint> anchorLocations) {
    this(defSize.width, defSize.height, anchorLocations);
  }

  public DefaultSizeNodeFigureFixedAnchors(int width, int height, Map<String, PrecisionPoint> anchorLocations) {
    super(width, height);
    if (anchorLocations.size() == 0)
      throw new IllegalArgumentException("At least one fixed anchor must be specified");
    Iterator<String> terminals = anchorLocations.keySet().iterator();
    while (terminals.hasNext()) {
      String terminal = terminals.next();
      PrecisionPoint anchorLocation = anchorLocations.get(terminal);
      anchors.put(terminal, new FixedConnectionAnchor(this, anchorLocation));
    }
  }

  @Override
  public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
    return findNearestAnchorFrom(p);
  }

  @Override
  public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
    return findNearestAnchorFrom(p);
  }

  @Override
  public ConnectionAnchor getConnectionAnchor(String terminal) {
    return anchors.get(terminal);
  }

  @Override
  public String getConnectionAnchorTerminal(ConnectionAnchor c) {
    String selectedTerminal = null;
    Iterator<String> terminals = anchors.keySet().iterator();
    while (terminals.hasNext() && selectedTerminal == null) {
      String terminal = terminals.next();
      FixedConnectionAnchor anchor = anchors.get(terminal);
      if (anchor == c) {
        selectedTerminal = terminal;
      }
    }
    return selectedTerminal;
  }

  private ConnectionAnchor findNearestAnchorFrom(Point point) {
    ConnectionAnchor result = null;
    if (point == null || anchors.size() == 1) {
      result = anchors.values().iterator().next();
    } else {
      double minDistance = Double.MAX_VALUE;
      String nearestTerminal = null;
      Iterator<String> terminals = anchors.keySet().iterator();
      while (terminals.hasNext()) {
        String terminal = terminals.next();
        FixedConnectionAnchor anchor = anchors.get(terminal);
        Point anchorPosition = anchor.getLocation();
        double distance = point.getDistance(anchorPosition);
        if (distance < minDistance) {
          minDistance = distance;
          nearestTerminal = terminal;
        }
      }
      result = anchors.get(nearestTerminal);
    }
    return result;
  }

}
