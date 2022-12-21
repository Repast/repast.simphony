package repast.simphony.gis.freezedry;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

import repast.simphony.freezedry.FreezeDryedObject;
import repast.simphony.freezedry.FreezeDryer;
import repast.simphony.freezedry.FreezeDryingException;

public class GeometryFreezeDryer implements FreezeDryer<Geometry> {

  private static final String STR_REP = "STR_REP";


  public Geometry rehydrate(FreezeDryedObject fdo) throws FreezeDryingException {
    String text = (String) fdo.get(STR_REP);
    try {
      return new WKTReader().read(text);
    } catch (ParseException e) {
      throw new FreezeDryingException(e);
    }
  }

  public FreezeDryedObject freezeDry(String id, Geometry geometry)
          throws FreezeDryingException {
    // need to get the class of the individual geometry
    // so we get the file name etc. correct.
    FreezeDryedObject fdo = new FreezeDryedObject(id, geometry.getClass());
    WKTWriter writer = new WKTWriter();
    fdo.put(STR_REP, writer.write(geometry));
    return fdo;
  }



  public boolean handles(Class<?> clazz) {
    return Geometry.class.isAssignableFrom(clazz);
  }
}