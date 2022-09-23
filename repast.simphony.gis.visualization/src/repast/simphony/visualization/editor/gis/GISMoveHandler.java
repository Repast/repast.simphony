package repast.simphony.visualization.editor.gis;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.GeometryFilter;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.piccolo2d.PLayer;
import org.piccolo2d.PNode;
import org.piccolo2d.event.PDragSequenceEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.nodes.PPath;

import repast.simphony.gis.display.PGISCanvas;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.editor.EditorNotifier;
import repast.simphony.visualization.editor.FloatingList;
import repast.simphony.visualization.editor.PEditorEventListener;
import repast.simphony.visualization.gis.DisplayGIS;

/**
 * Event handler for moving gis objects.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class GISMoveHandler extends PDragSequenceEventHandler implements PEditorEventListener {

  private DisplayGIS display;
  private PGISCanvas canvas;
  private GeometryFactory factory = new GeometryFactory();
  private Object selectedObject = null;
  private Point2D nodeStartPosition;
  private PNode node;
  private PLayer layer = new PLayer();
  private EditorNotifier notifier;

  public GISMoveHandler(PGISCanvas canvas, DisplayGIS display, EditorNotifier notifier) {
    this.display = display;
    this.canvas = canvas;
    this.notifier = notifier;
  }

  /**
   * Initializes this listener. This happens only once whereas start / stop may
   * occur multiple times.
   */
  public void init() {
  }

  /**
   * Cleans up anything created by this listener in init. This happens only once whereas
   * start / stop may occur multiple times.
   */
  public void destroy() {
  }


  private List<Object> findObjects(PInputEvent event) {
    org.locationtech.jts.geom.Point point = factory.createPoint(new Coordinate(event.getPosition().getX(),
            event.getPosition().getY()));
    Geography geog = display.getGeography();
    Geometry gEnv = point.buffer(GISEditorUtilities.calcPointPickBuffer(point.getCoordinate(),
            geog.getCRS(), canvas.getScaleDenominator()));
    java.util.List<Object> objs = new ArrayList<Object>();

    for (Object obj : geog.queryInexact(gEnv.getEnvelopeInternal())) {
      Geometry geom = geog.getGeometry(obj);
      if (geom.intersects(gEnv) && !(obj instanceof RepastEdge)) {
        objs.add(obj);
      }
    }

    return objs;
  }

  public void mousePressed(final PInputEvent event) {
    selectedObject = null;
    List<Object> objs = findObjects(event);
    if (objs.size() == 1) {
      selectedObject = objs.get(0);
      super.mousePressed(event);
    } else if (objs.size() > 1) {
      final FloatingList list = new FloatingList(objs.toArray());
      MouseEvent mouseEvent = (MouseEvent) event.getSourceSwingEvent();
      list.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (list.getSelectedItems().length > 0) {
            selectedObject = list.getSelectedItems()[0];
            GISMoveHandler.super.mousePressed(event);
          }
        }
      });
      list.show(canvas, mouseEvent.getX(), mouseEvent.getY());

    } else {
      super.mousePressed(event);
    }
  }

  @Override
  protected boolean shouldStartDragInteraction(PInputEvent event) {
    return super.shouldStartDragInteraction(event) && selectedObject != null;
  }

  private PNode createNode() {
    PPath.Double path = new PPath.Double();
    Geometry geom = display.getGeography().getGeometry(selectedObject);
    Coordinate[] coords = geom.getCoordinates();
    if (geom instanceof Polygon || geom instanceof MultiPolygon || geom instanceof LineString ||
            geom instanceof MultiLineString) {
      path.moveTo((float) coords[0].x, (float) coords[0].y);
      for (int i = 1; i < coords.length; i++) {
        path.lineTo((float) coords[i].x, (float) coords[i].y);
      }

      if (geom instanceof LineString || geom instanceof MultiLineString) {
        // give some thickness to the line so we can see it better
        for (int i = coords.length - 1; i >= 0; i--) {
          path.lineTo((float) coords[i].x, (float) coords[i].y - .001f);
        }
      }

      path.lineTo((float) coords[0].x, (float) coords[0].y);
      path.setPaint(new Color(0, .9f, 0, .4f));


    } else {
      // must be a point -- but trouble is we don't know
      // the mark that represents it, so just make a square for now
      double val = GISEditorUtilities.calcPointPickBuffer(coords[0],
              display.getGeography().getCRS(), canvas.getScaleDenominator()) * 3;
      path = new PPath.Double(new Rectangle2D.Double(coords[0].x - val / 2, coords[0].y - val / 2, val, val));
      path.setPaint(new Color(0, .9f, 0, .8f));
    }

    // set stroke to null to avoid stroke scaling giant blob
    // issue
    path.setStroke(null);
    return path;
  }

  @Override
  protected void startDrag(PInputEvent event) {
    super.startDrag(event);
    node = createNode();
    nodeStartPosition = node.getOffset();
    layer.addChild(node);
  }

  protected void drag(PInputEvent event) {
    super.drag(event);
    Point2D start = canvas.getCamera().localToView((Point2D) getMousePressedCanvasPoint().clone());
    Point2D current = event.getPosition();
    Point2D dest = new Point2D.Double();

    dest.setLocation(nodeStartPosition.getX() + (current.getX() - start.getX()),
            nodeStartPosition.getY() + (current.getY() - start.getY()));
    node.setOffset(dest.getX(), dest.getY());
  }

  @Override
  protected void endDrag(PInputEvent event) {
    super.endDrag(event);
    if (selectedObject != null) {
      Geometry geom = display.getGeography().getGeometry(selectedObject);
      geom.apply(new TranslateFilter(node.getOffset()));
      display.getGeography().move(selectedObject, geom);
      layer.removeChild(node);
      node = null;
      selectedObject = null;
      notifier.editorEventOccurred();
    }
  }


  /**
   * Starts the listener. At the very least this should
   * add the listener to whatever PNode it is listening on.
   */
  public void start() {
    canvas.addInputEventListener(this);
    layer = canvas.getMapLayer();
  }

  /**
   * Stops the listener. At the very least this should
   * remove the listener to whatever PNode it is listening on.
   */
  public void stop() {
    canvas.removeInputEventListener(this);
    layer = null;
  }

  private static class TranslateFilter implements GeometryFilter {

    private Point2D offset;

    private TranslateFilter(Point2D offset) {
      this.offset = offset;
    }

    public void filter(Geometry geometry) {
      Coordinate[] coords = geometry.getCoordinates();
      double offX = offset.getX();
      double offY = offset.getY();
      for (int i = 0; i < coords.length; i++) {
        Coordinate coord = coords[i];
        coord.x += offX;
        coord.y += offY;
      }
      geometry.geometryChanged();
    }
  }
}