package repast.simphony.visualization.visualization3D;

import java.awt.AWTEvent;
import java.awt.Event;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.Bounds;
import javax.media.j3d.CapabilityNotSetException;
import javax.media.j3d.Shape3D;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOr;

import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;

/**
 * Pick tool for probing objects. Double click attempts a probe at the current mouse location.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class PickProbeBehavior extends PickMouseBehavior {
  private Display3D display;

  public PickProbeBehavior(Display3D display, Bounds bounds) {
    super(display.getCanvas(), display.getSceneRoot(), bounds);
    setSchedulingBounds(bounds);
    pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO);
    //pickCanvas.setTolerance(0);
    this.display = display;
  }

  public void initialize() {
    conditions = new WakeupCriterion[1];
    conditions[0] = new WakeupOnAWTEvent(Event.MOUSE_DOWN);
    wakeupCondition = new WakeupOr(conditions);
    wakeupOn(wakeupCondition);
  }

  private void processMouseEvent(MouseEvent evt) {
    buttonPress = false;
    //if (evt.getID() == MouseEvent.MOUSE_PRESSED |
    //        evt.getID() == MouseEvent.MOUSE_CLICKED) {
    if (evt.getClickCount() == 2) {
      buttonPress = true;
      return;
    }
  }

  public void processStimulus(Enumeration criteria) {
    WakeupCriterion wakeup;
    AWTEvent[] evt = null;
    int xpos, ypos;

    while (criteria.hasMoreElements()) {
      wakeup = (WakeupCriterion) criteria.nextElement();
      if (wakeup instanceof WakeupOnAWTEvent)
        evt = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
    }

    if (evt[0] instanceof MouseEvent) {
      mevent = (MouseEvent) evt[0];
      //System.out.println("got mouse event");
      processMouseEvent((MouseEvent) evt[0]);
    }

    if (buttonPress) {
      xpos = mevent.getPoint().x;
      ypos = mevent.getPoint().y;
      //System.out.println("mouse position " + xpos + " " + ypos);
      updateScene(xpos, ypos);
    }
    wakeupOn(wakeupCondition);
  }


  public void updateScene(int xpos, int ypos) {
    pickCanvas.setShapeLocation(xpos, ypos);
    try {
      PickResult pickResult = pickCanvas.pickClosest();
      //PickResult[] pickResults = pickCanvas.pickAll();
      if (pickResult != null) {
        //for (PickResult pickResult : pickResults) {
          Shape3D shape = (Shape3D) pickResult.getNode(PickResult.SHAPE3D);
          if (shape != null) {
            display.probe(shape, pickResult.getIntersection(0).getClosestVertexCoordinates());
            
            //shape.setAppearance(AppearanceFactory.setPolygonAppearance(shape.getAppearance(),
            //        AppearanceFactory.PolygonDraw.LINE));
          }
        //}
      }
    } catch (CapabilityNotSetException ex) {
      ex.printStackTrace();
    }
  }
}
