package repast.simphony.xml;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Converter for ConvertedObj.
 *
 * @author Nick Collier
 */
public class TestConverter extends AbstractConverter {

  public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
    ConvertedObj obj = (ConvertedObj) o;
    writeString("converted_obj_name", obj.getName(), writer);
    writeString("converted_obj_id", String.valueOf(obj.getId()), writer);
  }

  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext unmarshallingContext) {
    String name = readNextString(reader);
    int id = Integer.valueOf(readNextString(reader));
    return new ConvertedObj(id, name + "__converted");
  }

  public boolean canConvert(Class aClass) {
    return aClass.equals(ConvertedObj.class);
  }
}
