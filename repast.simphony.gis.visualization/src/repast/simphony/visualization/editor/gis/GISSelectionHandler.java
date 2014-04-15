package repast.simphony.visualization.editor.gis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.event.PInputEventListener;

import repast.simphony.gis.display.PGISCanvas;
import repast.simphony.gis.tools.MapTool;
import repast.simphony.space.gis.Geography;
import repast.simphony.visualization.editor.FloatingList;
import repast.simphony.visualization.editor.PEditorEventListener;
import repast.simphony.visualization.gis.DisplayGIS;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Event handler for selecting nodes.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class GISSelectionHandler extends PBasicInputEventHandler implements PEditorEventListener {

  private PGISCanvas canvas;
  private Geography geog;
  private List<ObjectSelectionListener> listeners = new ArrayList<ObjectSelectionListener>();
  private boolean fireSelected = true;
  private GeometryFactory factory = new GeometryFactory();
  private java.util.List<Object> selectedObjs = new ArrayList<Object>();
  private SelectionDecorator decorator;
  private DisplayGIS display;
  private boolean active = true;

  public GISSelectionHandler(DisplayGIS disp, PGISCanvas canvas) {
    this.canvas = canvas;
    this.geog = disp.getGeography();
    decorator = disp.getDecorator();
    display = disp;
  }


  public void mouseEntered(PInputEvent event) {
    active = true;
  }

  public void mouseExited(PInputEvent event) {
    active = false;
  }

  @Override
  public void mouseClicked(PInputEvent event) {
    if (active) {
      if (!(event.isControlDown() || event.isMetaDown())) unselectObjects();
      Point point = factory.createPoint(new Coordinate(event.getPosition().getX(), event.getPosition().getY()));
      double bufferVal = GISEditorUtilities.calcPointPickBuffer(point.getCoordinate(),
              geog.getCRS(), canvas.getScaleDenominator());
      selectObjects(point.buffer(bufferVal), event);
    }
  }

  private void unselectObjects() {
    selectedObjs.clear();
    decorator.clearSelected();
    display.render();
  }

  private void selectObjects(Geometry gEnv, PInputEvent event) {
    List<Object> objs = new ArrayList<Object>();
    for (Object obj : geog.queryInexact(gEnv.getEnvelopeInternal())) {
      Geometry geom = geog.getGeometry(obj);
      if (geom.intersects(gEnv)) {
        objs.add(obj);
      }
    }

    if (objs.size() > 1) {
      final FloatingList list = new FloatingList(objs.toArray());
      MouseEvent mouseEvent = (MouseEvent) event.getSourceSwingEvent();
      list.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          for (Object obj : list.getSelectedItems()) {
            selectedObjs.add(obj);
            decorator.addSelected(obj);
          }
          display.render();
          fireObjectsSelected();
        }
      });
      list.show(canvas, mouseEvent.getX(), mouseEvent.getY());

    } else if (objs.size() == 1) {

      selectedObjs.add(objs.get(0));
      decorator.addSelected(objs.get(0));
      display.render();
      fireObjectsSelected();
    }
  }

  public void addObjectSelectionListener(ObjectSelectionListener listener) {
    listeners.add(listener);
  }

  /**
   * Invoked when the specified objects have been selected in
   * code external to this
   *
   * @param objs the selected objects
   */
  public void objectsSelected(Object[] objs) {
    unselectObjects();
    for (Object obj : objs) {
      decorator.addSelected(obj);
    }
    display.render();
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

  /**
   * Starts the listener. At the very least this should
   * add the listener to whatever PNode it is listening on.
   */
  public void start() {
    PInputEventListener curListener = canvas.getCurrentEventHandler();
    if (curListener instanceof MapTool) {
      ((MapTool) curListener).deactivate();
    }
    canvas.setEventHandler(this);
    decorator.addHighightRules();
  }

  /**
   * Stops the listener. At the very least this should
   * remove the listener to whatever PNode it is listening on.
   */
  public void stop() {
    unselectObjects();
    canvas.removeInputEventListener(this);
    decorator.removeHighlightRules();
  }

  private void fireObjectsSelected() {
    if (fireSelected) {
      List<Object> nodes = new ArrayList<Object>(selectedObjs);
      for (ObjectSelectionListener listener : listeners) {
        listener.objectsSelected(nodes);
      }
    }
  }
}