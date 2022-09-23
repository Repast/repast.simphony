package repast.simphony.xml;

import javax.measure.Quantity;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import tech.units.indriya.AbstractQuantity;

/**
 * Converter for Amount classes.
 *
 * @author Nick Collier
 */
public class QuantityConverter extends AbstractConverter {

  public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
    Quantity<?> quantity = (Quantity<?>) o;
    writeString("amount_value", quantity.toString(), writer);
  }

  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext unmarshallingContext) {
    String val = readNextString(reader);
    return AbstractQuantity.parse(val);
  }

  public boolean canConvert(Class aClass) {
    return aClass.equals(Quantity.class);
  }
}
