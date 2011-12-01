package repast.simphony.visualization.editor.gis;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import repast.simphony.gis.GeometryUtil;
import repast.simphony.space.gis.GISAdder;
import repast.simphony.space.gis.Geography;
import repast.simphony.visualization.gis.DisplayGIS;

import java.util.List;

/**
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class GISAddListener {

  private Geography geog;
  private GISAddHandler handler;
  private GeometryFactory gFactory = new GeometryFactory();
  private Adder adder = new Adder();
  private GISAdder oldAdder;

  private class Adder implements GISAdder<Object> {

    Geometry geom;

    public void add(Geography<Object> destination, Object object) {
      destination.move(object, geom);
    }
  }

  public GISAddListener(DisplayGIS disp, GISAddHandler handler) {
    this.geog = disp.getGeography();
    oldAdder = geog.getAdder();
    this.handler = handler;
  }

  public GISAddHandler getAddHandler() {
    return handler;
  }

  public void postAdd() {
    geog.setAdder(oldAdder);
  }

  public boolean preAdd(List<Coordinate> coords, GeometryUtil.GeometryType type) {
    adder.geom = createGeometry(coords, type);
    geog.setAdder(adder);
    return true;
  }

  private Geometry createGeometry(List<Coordinate> coords, GeometryUtil.GeometryType type) {
    if (type == GeometryUtil.GeometryType.POINT) {
      return gFactory.createPoint(coords.get(0));
    } else if (type == GeometryUtil.GeometryType.LINE) {
      Coordinate[] cArray = new Coordinate[coords.size()];

      return gFactory.createLineString(coords.toArray(cArray));
    } else if (type == GeometryUtil.GeometryType.POLYGON) {
      Coordinate[] cArray = new Coordinate[coords.size() + 1];
      cArray = coords.toArray(cArray);
      cArray[coords.size()] = new Coordinate(cArray[0].x, cArray[0].y);
      return gFactory.createPolygon(gFactory.createLinearRing(cArray), new LinearRing[]{});
    }

    return null;
  }
}
