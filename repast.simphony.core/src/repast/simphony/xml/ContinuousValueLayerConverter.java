package repast.simphony.xml;

import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import repast.simphony.context.Context;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.util.collections.Pair;
import repast.simphony.valueLayer.ContinuousValueLayer;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * XStream converter for continuousValueLayers.
 *
 * @author Nick Collier
 */
public class ContinuousValueLayerConverter extends AbstractConverter {

  public boolean canConvert(Class aClass) {
    return aClass.equals(ContinuousValueLayer.class);
  }

  public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mContext) {
    try {
      ContinuousValueLayer layer = (ContinuousValueLayer) o;
      Field field = ContinuousValueLayer.class.getDeclaredField("defaultValue");
      field.setAccessible(true);
      double defVal = (Double) field.get(layer);
      writeString("name", layer.getName(), writer);
      writeString("defaultValue", String.valueOf(defVal), writer);
      double[] dims = layer.getDimensions().toDoubleArray(null);
      writeObject("dims", dims, writer, mContext);

      field = ContinuousValueLayer.class.getDeclaredField("locationMap");
      field.setAccessible(true);
      Map<NdPoint, Double> map = (Map<NdPoint, Double>) field.get(layer);

      writeString("item_count", String.valueOf(map.size()), writer);
      for (NdPoint pt : map.keySet()) {
        Double val = map.get(pt);
        Pair p = new Pair(pt, val);
        writeObject("item", p, writer, mContext);
      }
    } catch (Exception ex) {
      throw new XStreamException("Error while writing ContinuousValueLayer", ex);
    }
  }

  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext umContext) {
    try {

      Context context = (Context) umContext.get(Keys.CONTEXT);
      String name = readNextString(reader);

      String strVal = readNextString(reader);
      double defVal = Double.parseDouble(strVal);

      double[] dims = (double[]) readNextObject(context, reader, umContext);

      ContinuousValueLayer layer = new ContinuousValueLayer(name, defVal, true, dims);
      context.addValueLayer(layer);
      int itemCount = Integer.valueOf(readNextString(reader));
      for (int i = 0; i < itemCount; i++) {
        Pair pair = (Pair) readNextObject(layer, reader, umContext);
        NdPoint pt = (NdPoint) pair.getFirst();
        double val = (Double) pair.getSecond();
        layer.set(val, pt.toDoubleArray(null));
      }

      return layer;
    } catch (Exception ex) {
      throw new ConversionException("Error deserializing continuous value layer", ex);
    }

  }
}