package repast.simphony.visualization.editor.gis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.piccolo2d.PCamera;
import org.piccolo2d.PCanvas;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.nodes.PPath;

import repast.simphony.gis.display.PGISCanvas;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.editor.FloatingList;
import repast.simphony.visualization.editor.PEditorEventListener;
import repast.simphony.visualization.gis.DisplayGIS;

/**
 * Event handler for adding edges to gis.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class GISNetAddEdgeHandler extends PBasicInputEventHandler implements PEditorEventListener {

  private GeometryFactory factory = new GeometryFactory();
  private PCanvas canvas;
  private DisplayGIS display;
  private PPath.Double path;
  private Point2D startCanvas;
  private DisplayEditorGIS editor;
  private java.util.List<Coordinate> coords = new ArrayList<Coordinate>();
  private PCamera camera;
  private boolean active = false;
  private Object source, target;

  public GISNetAddEdgeHandler(DisplayEditorGIS editor, PGISCanvas canvas, DisplayGIS display) {
    this.canvas = canvas;
    this.display = display;
    this.editor = editor;
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
    org.locationtech.jts.geom.Point point = factory.createPoint(new Coordinate(event.getPosition().getX(), event.getPosition().getY()));
    Geometry gEnv = point.buffer(.001);
    java.util.List<Object> objs = new ArrayList<Object>();
    Geography geog = display.getGeography();
    for (Object obj : geog.queryInexact(gEnv.getEnvelopeInternal())) {
      Geometry geom = geog.getGeometry(obj);
      if (geom.intersects(gEnv) && !(obj instanceof RepastEdge)) {
        objs.add(obj);
      }
    }

    return objs;
  }

  private void startPath(PInputEvent event) {
    path = new PPath.Double();
    path.setStrokePaint(Color.BLACK);
    path.setStroke(new BasicStroke(1));
    coords.add(createCoordinate(event));
    startCanvas = event.getCanvasPosition();

    path.moveTo((float) startCanvas.getX(), (float) startCanvas.getY());
    camera.addChild(path);
  }

  private void startPath(Object obj) {
    Geometry geom = display.getGeography().getGeometry(obj);
    Coordinate coord = new Coordinate(geom.getCentroid().getCoordinate());
    path = new PPath.Double();
    path.setStrokePaint(Color.BLACK);
    path.setStroke(new BasicStroke(1));
    coords.add(coord);
    startCanvas = canvas.getCamera().viewToLocal(new Point2D.Double(coord.x, coord.y));
    path.moveTo((float) startCanvas.getX(), (float) startCanvas.getY());
    camera.addChild(path);
  }

  public void mouseClicked(PInputEvent event) {
    camera = event.getCamera();
    if (event.isLeftMouseButton()) {
      if (event.getClickCount() == 1) {
        if (source == null) {
          // find a source
          List<Object> objs = findObjects(event);
          if (objs.size() == 0) return;


          if (objs.size() == 1) {
            source = objs.get(0);
            startPath(source);
            mouseMoved(event);
            active = true;
          } else {
            // multiple objs so need user to select
            FloatingList list = new FloatingList(objs.toArray());
            MouseEvent mouseEvent = (MouseEvent) event.getSourceSwingEvent();
            list.addActionListener(new SelectionListener(event, list, true));
            list.show(canvas, mouseEvent.getX(), mouseEvent.getY());
          }

        } else {
          startPath(event);
        }

      } else if (event.getClickCount() == 2) {
        if (source != null) {
          List<Object> objs = findObjects(event);
          if (objs.size() == 0) return;
          if (objs.size() == 1) {
            target = objs.get(0);
            // we seem to get click count == 1 before we get two so we need to
            // remove the intermediate coordiate
            if (coords.size() > 1) coords.remove(coords.size() - 1);
            Coordinate coord = new Coordinate(display.getGeography().getGeometry(target).getCentroid().getCoordinate());
            coords.add(coord);
            camera.removeAllChildren();
            editor.addEdge(source, target, coords);
            source = target = null;
            coords.clear();
            active = false;


          } else {
            // multiple objs so need user to select
            active = false;
            FloatingList list = new FloatingList(objs.toArray());
            MouseEvent mouseEvent = (MouseEvent) event.getSourceSwingEvent();
            list.addActionListener(new SelectionListener(event, list, false));
            list.show(canvas, mouseEvent.getX(), mouseEvent.getY());
          }

        }
      }
    } else if (event.isRightMouseButton()) {
      active = false;
      source = target = null;
      coords.clear();
      camera.removeAllChildren();
    }
  }

  class SelectionListener implements ActionListener {

    private FloatingList list;
    private PInputEvent evt;
    private boolean forSource;

    SelectionListener(PInputEvent evt, FloatingList list, boolean forSource) {
      this.forSource = forSource;
      this.evt = evt;
      this.list = list;
    }

    public void actionPerformed(ActionEvent e) {
      if (forSource) {
        source = list.getSelectedItems()[0];
        startPath(source);
        mouseMoved(evt);
        active = true;
      } else {
        active = false;
        target = list.getSelectedItems()[0];
        // we seem to get click count == 1 before we get two so we need to
        // remove the intermediate coordiate
        if (coords.size() > 1) coords.remove(coords.size() - 1);
        Coordinate coord = new Coordinate(display.getGeography().getGeometry(target).getCentroid().getCoordinate());
        coords.add(coord);
        camera.removeAllChildren();
        editor.addEdge(source, target, coords);
        source = target = null;
        coords.clear();
      }
    }
  }

  public void mouseMoved(PInputEvent event) {
    if (active) {
      Point2D canvasPosition = event.getCanvasPosition();
      path.reset();
      path.moveTo((float) startCanvas.getX(), (float) startCanvas.getY());
      path.lineTo((float) canvasPosition.getX(), (float) canvasPosition.getY());
    }
  }

  private Coordinate createCoordinate(PInputEvent event) {
    Coordinate coord = new Coordinate();
    coord.x = event.getPosition().getX();
    coord.y = event.getPosition().getY();
    return coord;

    /*
    if (transform == null || crs.equals(wgs84)) {
      coord.x = event.getPosition().getX();
      coord.y = event.getPosition().getY();
    } else {
      coord = new Coordinate(event.getPosition().getX(),
              event.getPosition().getY());
      try {
        JTS.transform(coord, coord, transform);
      } catch (TransformException e) {
        center.warn("Unable to transform when adding", e);
        editor.addCanceled();
      }
    }
    if (crs.equals(DefaultGeographicCRS.WGS84)) {
      double tmp = coord.x;
      coord.x = coord.y;
      coord.y = tmp;
    }
    return coord;
    */
  }


  /**
   * Starts the listener. At the very least this should
   * add the listener to whatever PNode it is listening on.
   */
  public void start() {
    canvas.addInputEventListener(this);
  }

  /**
   * Stops the listener. At the very least this should
   * remove the listener to whatever PNode it is listening on.
   */
  public void stop() {
    active = false;
    canvas.removeInputEventListener(this);
  }
}