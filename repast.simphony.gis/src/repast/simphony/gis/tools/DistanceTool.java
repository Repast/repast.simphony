package repast.simphony.gis.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.geom.Point2D;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.map.MapContent;
import org.geotools.map.MapContext;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapBoundsListener;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.operation.TransformException;

import repast.simphony.gis.display.PiccoloMapPanel;
import simphony.util.messages.MessageCenter;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;

public class DistanceTool extends PBasicInputEventHandler implements MapTool, MapBoundsListener {

  protected PPath path;

  PLayer layer;

  protected boolean active;

  protected Point2D start;

  protected Point2D startCanvas;

  MapContent mapContext;

  protected Unit unit;

  protected GeodeticCalculator calculator;

  protected DistanceSetter setter;

  private double curDistance = 0;
  private double totalDistance = 0;
  private PCamera camera;

  // this unit mess is because geotools geodetic calculator uses the old jsr units
  public DistanceTool(MapContent context, javax.measure.unit.Unit unit, DistanceSetter setter) {
    this.mapContext = context;
    this.unit = Unit.valueOf(unit.toString());
    this.setter = setter;
    calculator = new GeodeticCalculator(context.getCoordinateReferenceSystem());

    context.addMapBoundsListener(this);
  }

  public void mapBoundsChanged(MapBoundsEvent arg0) {
    calculator = new GeodeticCalculator(mapContext.getCoordinateReferenceSystem());
  }

  public void cleanUp() {
    this.setter = null;
    mapContext.removeMapBoundsListener(this);
  }

  public void activate(PiccoloMapPanel panel) {
    curDistance = 0;
    totalDistance = 0;
  }

  public void deactivate() {
    curDistance = totalDistance = 0;
    if (camera != null) camera.removeAllChildren();
    active = false;
  }

  public void mouseClicked(PInputEvent event) {
    camera = event.getCamera();
    if (event.getClickCount() == 1 && event.isLeftMouseButton()) {
      totalDistance += curDistance;
      active = true;
      path = new PPath();
      path.setStrokePaint(Color.BLACK);
      path.setStroke(new BasicStroke(1));
      start = event.getPosition();
      startCanvas = event.getCanvasPosition();
      try {
        DirectPosition2D point = new DirectPosition2D(mapContext
                .getCoordinateReferenceSystem());
        point.setLocation(start.getX(), start.getY());
        calculator.setStartingPosition(point);
      } catch (TransformException ex) {
        active = false;
        MessageCenter.getMessageCenter(getClass()).warn("Error transforming anchor point");
      }

      path.moveTo((float) startCanvas.getX(), (float) startCanvas.getY());
      camera.addChild(path);
    } else {
      curDistance = 0;
      totalDistance = 0;
      camera.removeAllChildren();
      active = false;
      setter.clearDistance();
    }
  }

  public void mouseMoved(PInputEvent event) {
    if (active) {
      Point2D canvasPosition = event.getCanvasPosition();
      path.reset();
      path.moveTo((float) startCanvas.getX(), (float) startCanvas.getY());
      path.lineTo((float) canvasPosition.getX(), (float) canvasPosition.getY());
      Point2D current = event.getPosition();
      double distance;

      try {
        DirectPosition2D point = new DirectPosition2D(mapContext
                .getCoordinateReferenceSystem());
        point.setLocation(current.getX(), current.getY());
        calculator.setDestinationPosition(point);
      } catch (TransformException e) {
        active = false;
        MessageCenter.getMessageCenter(getClass()).warn("Error transforming destination point");
      }

      if (!calculator.getEllipsoid().getAxisUnit().equals(NonSI.DEGREE_ANGLE)) {
        if (unit == SI.METER) {
          distance = calculator.getOrthodromicDistance();
        } else {
          UnitConverter converter = SI.METER.getConverterTo(unit);
          distance = converter.convert(calculator.getOrthodromicDistance());
        }
      } else if (!unit.equals(calculator.getEllipsoid().getAxisUnit())) {
        UnitConverter converter = calculator.getEllipsoid().getAxisUnit()
                .getConverterTo(unit);
        distance = converter.convert(calculator
                .getOrthodromicDistance());
      } else {
        distance = calculator.getOrthodromicDistance();
      }

      curDistance = distance;
      setter.setDistance(totalDistance + curDistance, javax.measure.unit.Unit.valueOf(unit.toString()));
    }
  }

  public Cursor getCursor() {
    return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
  }
}
