package repast.simphony.scenario;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

import repast.simphony.engine.schedule.Descriptor;

/**
 * XStream converter for simphony descriptors. Adds
 * the simphony version to the root tag, but otherwise
 * behaves as the default descriptor. 
 * 
 * @author Nick Collier
 */
public class DescriptorConverter implements Converter {
  
  private String version;
  private ReflectionConverter converter;
  
  public DescriptorConverter(Mapper mapper, ReflectionProvider provider, String version) {
    this.version = version;
    converter = new ReflectionConverter(mapper, provider);
  }
 
  @Override
  public boolean canConvert(Class type) {
    return type != null && Descriptor.class.isAssignableFrom(type);
  }

  @Override
  public void marshal(Object obj, HierarchicalStreamWriter writer, MarshallingContext context) {
    writer.addAttribute("simphonyVersion", version);
    converter.marshal(obj, writer, context);
  }

  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    return converter.unmarshal(reader, context);
  }
}
