package repast.simphony.xml;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.jscience.physics.amount.Amount;

/**
 * Converter for Amount classes.
 *
 * @author Nick Collier
 */
public class AmountConverter extends AbstractConverter {

  public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
    Amount amount = (Amount) o;
    writeString("amount_value", amount.toString(), writer);
  }

  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext unmarshallingContext) {
    String val = readNextString(reader);
    return Amount.valueOf(val);
  }

  public boolean canConvert(Class aClass) {
    return aClass.equals(Amount.class);
  }
}
