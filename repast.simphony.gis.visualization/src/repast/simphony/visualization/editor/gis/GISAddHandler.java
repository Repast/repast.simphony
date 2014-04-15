package repast.simphony.visualization.editor.gis;

import static repast.simphony.gis.util.GeometryUtil.GeometryType.LINE;
import static repast.simphony.gis.util.GeometryUtil.GeometryType.POINT;
import static repast.simphony.gis.util.GeometryUtil.GeometryType.POLYGON;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.piccolo2d.PCamera;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.event.PInputEventListener;
import org.piccolo2d.nodes.PPath;

import repast.simphony.gis.display.PGISCanvas;
import repast.simphony.gis.util.GeometryUtil;
import repast.simphony.visualization.editor.PEditorEventListener;
import simphony.util.messages.MessageCenter;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Nick Collier Date: Feb 26, 2008 11:17:47 AM
 * @deprecated 2D piccolo based code is being removed
 */
public class GISAddHandler extends PBasicInputEventHandler implements PEditorEventListener {

  MessageCenter center = MessageCenter.getMessageCenter(getClass());

  private boolean active;
  private DisplayEditorGIS editor;
  // private CoordinateReferenceSystem crs, wgs84;
  // private MathTransform transform;
  private PCamera camera;
  private PGISCanvas canvas;
  private PInputEventListener evtListener = new PointEventListener();

  class BaseEventListener extends PBasicInputEventHandler {

    protected java.util.List<Coordinate> coords = new ArrayList<Coordinate>();

    protected Coordinate createCoordinate(PInputEvent event) {
      Coordinate coord = new Coordinate();
      coord.x = event.getPosition().getX();
      coord.y = event.getPosition().getY();

      /*
       * if (transform == null || crs.equals(wgs84)) { coord.x =
       * event.getPosition().getX(); coord.y = event.getPosition().getY(); }
       * else { coord = new Coordinate(event.getPosition().getX(),
       * event.getPosition().getY()); try { JTS.transform(coord, coord,
       * transform); } catch (TransformException e) {
       * center.warn("Unable to transform when adding", e);
       * editor.addCanceled(); } }
       */
      return coord;
    }
  }

  class LineEventListener extends BaseEventListener {

    private PPath.Double path;
    private Point2D startCanvas;

    protected GeometryUtil.GeometryType getGeomType() {
      return LINE;
    }

    public void mouseClicked(PInputEvent event) {
      camera = event.getCamera();
      if (event.isLeftMouseButton()) {
        active = true;
        path = new PPath.Double();
        path.setStrokePaint(Color.BLACK);
        path.setStroke(new BasicStroke(1));
        coords.add(createCoordinate(event));
        startCanvas = event.getCanvasPosition();

        path.moveTo((float) startCanvas.getX(), (float) startCanvas.getY());
        camera.addChild(path);
        if (event.getClickCount() == 2) {
          camera.removeAllChildren();
          camera.removeInputEventListener(this);
          editor.addAgentAt(coords, getGeomType());
        }
      } else if (event.isRightMouseButton()) {
        active = false;
        camera.removeInputEventListener(this);
        camera.removeAllChildren();
        editor.addCanceled();
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
  }

  class PolygonEventListener extends LineEventListener {
    protected GeometryUtil.GeometryType getGeomType() {
      return POLYGON;
    }
  }

  class PointEventListener extends BaseEventListener {

    @Override
    public void mouseEntered(PInputEvent event) {
      active = true;
    }

    @Override
    public void mouseExited(PInputEvent event) {
      active = false;
    }

    @Override
    public void mouseClicked(PInputEvent event) {
      if (active && event.isLeftMouseButton()) {
        coords.add(createCoordinate(event));
        camera.removeInputEventListener(this);
        editor.addAgentAt(coords, POINT);

      } else if (event.isRightMouseButton()) {
        camera.removeInputEventListener(this);
        editor.addCanceled();
      }
    }
  }

  public GISAddHandler(PGISCanvas canvas, DisplayEditorGIS editor) {
    // this.crs = crs;
    this.camera = canvas.getCamera();
    this.canvas = canvas;
    this.editor = editor;

    /*
     * try { wgs84 = CRS.decode("EPSG:4326"); transform = CRS.transform(crs,
     * wgs84, true); } catch (NoSuchAuthorityCodeException e) { wgs84 =
     * DefaultGeographicCRS.WGS84; } catch (FactoryException e) {
     * center.warn("Unable to create transform for gis add hanlder", e); }
     */
  }

  public void destroy() {
  }

  public void init() {
  }

  public void start() {
    camera.addInputEventListener(evtListener);
    canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

  }

  public void start(GeometryUtil.GeometryType geomType) {
    camera.removeInputEventListener(evtListener);
    if (geomType == POINT)
      evtListener = new PointEventListener();
    else if (geomType == LINE)
      evtListener = new LineEventListener();
    else if (geomType == POLYGON)
      evtListener = new PolygonEventListener();
    start();
  }

  public void stop() {
    active = false;
    camera.removeInputEventListener(evtListener);
    canvas.setCursor(Cursor.getDefaultCursor());
  }
}
