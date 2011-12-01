package repast.simphony.xml;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContextSpace;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.space.continuous.ContinuousAdder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.PointTranslator;
import repast.simphony.util.collections.Pair;

/**
 * XStream converter for ContextSpace, the default continuous space type
 * in simphony.
 *
 * @author Nick Collier
 */
public class SpaceConverter extends AbstractConverter {

  public boolean canConvert(Class aClass) {
    return aClass.equals(ContextSpace.class);
  }

  public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mContext) {
    ContinuousSpace space = (ContinuousSpace) o;
    writeString("name", space.getName(), writer);
    writeString("adder", space.getAdder().getClass().getName(), writer);
    writeString("translator", space.getPointTranslator().getClass().getName(), writer);
    double[] dims = space.getDimensions().toDoubleArray(null);
    writeObject("dims", dims, writer, mContext);
    double[] origin = space.getDimensions().originToDoubleArray(null);
    writeObject("origin", origin, writer, mContext);
    writeString("item_count", String.valueOf(space.size()), writer);
    for (Object obj : space.getObjects()) {
      NdPoint point = space.getLocation(obj);
      Pair p = new Pair(obj, point);
      writeObject("space_entry", p, writer, mContext);
    }
  }

  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext umContext) {
    try {

      Context context = (Context) umContext.get(Keys.CONTEXT);
      String name = readNextString(reader);

      String adder = readNextString(reader);
      Class adderClass = Class.forName(adder);
      ContinuousAdder cAdder = (ContinuousAdder) adderClass.newInstance();

      Class transClass = Class.forName(readNextString(reader));
      PointTranslator trans = (PointTranslator) transClass.newInstance();

      double[] dims = (double[]) readNextObject(context, reader, umContext);
      double[] origin = (double[]) readNextObject(context, reader, umContext);

      ContinuousSpace space = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null).
              createContinuousSpace(name, context, cAdder, trans, dims, origin);

      int itemCount = Integer.valueOf(readNextString(reader));
      for (int i = 0; i < itemCount; i++) {
        Pair pair = (Pair) readNextObject(space, reader, umContext);
        NdPoint point = (NdPoint) pair.getSecond();
        if (point != null) {
          space.moveTo(pair.getFirst(), point.toDoubleArray(null));
        }
      }

      return space;
    } catch (Exception ex) {
      throw new ConversionException("Error deserializing Geography", ex);
    }

  }
}