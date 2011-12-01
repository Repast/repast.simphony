package repast.simphony.xml;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import repast.simphony.context.Context;
import repast.simphony.context.space.gis.ContextGeography;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.space.gis.GISAdder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.util.collections.Pair;

/**
 * XStream converter for ContextGeographies, the default gis space type
 * in simphony.
 *
 * @author Nick Collier
 */
public class GeographyConverter extends AbstractConverter {

  public boolean canConvert(Class aClass) {
    return aClass.equals(ContextGeography.class);
  }

  public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mContext) {
    Geography geog = (Geography) o;
    writeString("name", geog.getName(), writer);
    writeString("adder", geog.getAdder().getClass().getName(), writer);
    writeString("crs", geog.getCRS().toWKT(), writer);
    writeString("geom_count", String.valueOf(geog.size()), writer);
    for (Object obj : geog.getAllObjects()) {
      Geometry geom = geog.getGeometry(obj);
      Pair p = new Pair(obj, geom.toText());
      writeObject("geom_entry", p, writer, mContext);
    }
  }

  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext umContext) {
    try {

      Context context = (Context) umContext.get(Keys.CONTEXT);
      String name = readNextString(reader);
      String adder = readNextString(reader);
      Class adderClass = Class.forName(adder);
      GeographyParameters params = new GeographyParameters((GISAdder) adderClass.newInstance());
      String sCrs = readNextString(reader);
      CoordinateReferenceSystem crs = CRS.parseWKT(sCrs);

      Geography geog = GeographyFactoryFinder.createGeographyFactory(null).createGeography(name, context, params);
      geog.setCRS(crs);

      WKTReader wktReader = new WKTReader();
      int geomCount = Integer.valueOf(readNextString(reader));
      for (int i = 0; i < geomCount; i++) {
        Pair pair = (Pair) readNextObject(geog, reader, umContext);
        Geometry geom = wktReader.read(pair.getSecond().toString());
        geog.move(pair.getFirst(), geom);
      }

      return geog;
    } catch (Exception ex) {
      throw new ConversionException("Error deserializing Geography", ex);
    }

  }
}