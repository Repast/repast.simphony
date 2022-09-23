package repast.simphony.xml;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import repast.simphony.engine.environment.ProjectionRegistry;
import repast.simphony.engine.environment.ProjectionRegistryData;

/**
 * Wraps an XStream instance and adds converters specialized
 * for simphony.
 *
 * @author Nick Collier
 */
public class XMLSerializer {

  // Thin root object that is the root of
  // of our serialization hierarchy
  private class Root {
    private Object obj;
  }
  
  private class RootConverter extends AbstractConverter {

    public boolean canConvert(Class aClass) {
      return aClass.equals(Root.class);
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext umContext) {
      try {
        int count = Integer.valueOf(readNextString(reader));
        for (int i = 0; i < count; i++) {
          Class clazz = Class.forName(readNextString(reader));
          Converter converter = (Converter) clazz.newInstance();
          xstream.registerConverter(converter);
        }

        return readNextObject(null, reader, umContext);
      } catch (ClassNotFoundException e) {
        throw new XStreamException("Error while reading converters", e);
      } catch (IllegalAccessException e) {
        throw new XStreamException("Error while reading converters", e);
      } catch (InstantiationException e) {
        throw new XStreamException("Error while reading converters", e);
      }
    }

    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mContext) {
      Root root = (Root) o;
      writeString("converter_count", String.valueOf(nonDefaultConverter.size()), writer);
      for (Converter converter : nonDefaultConverter) {
        writeString("converter_class", converter.getClass().getName(), writer);
      }

      writeObject("root_obj", root.obj, writer, mContext);
    }
  }


  private List<Converter> nonDefaultConverter = new ArrayList<Converter>();
  private XStream xstream = new XStream();

  private List<AbstractConverter> defaultConverters = new ArrayList<AbstractConverter>();

  /**
   * Creates an XMLSerializer.
   */
  public XMLSerializer() {
  	defaultConverters.add(new DefaultContextConverter());
  	defaultConverters.add(new NetworkConverter());
  	defaultConverters.add(new GridConverter());
  	defaultConverters.add(new SpaceConverter());
  	defaultConverters.add(new RootConverter());
  	defaultConverters.add(new QuantityConverter());
  	defaultConverters.add(new GridValueLayerConverter());
  	defaultConverters.add(new ContinuousValueLayerConverter());
  	 
    // Add additional converters from the projection registry.
    for (ProjectionRegistryData data : ProjectionRegistry.getRegistryData()){
    	defaultConverters.add(data.getProjectionXMLConverter());
    }
    
    for (AbstractConverter con : defaultConverters) {
    	xstream.registerConverter(con);
    }
    
    xstream.alias("root", Root.class);
    //xstream.registerConverter(new FastMethodConvertor(xstream));
    xstream.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
    
  }

  /**
   * Registers a xstream converter.
   *
   * @param converter the converter to register
   */
  public void registerConverter(Converter converter) {
    nonDefaultConverter.add(converter);
    xstream.registerConverter(converter);
  }

  /**
   * Serializes the specified object to XML and returns that
   * XML as a String.
   *
   * @param obj the object to serialize
   * @return the XML
   * @throws com.thoughtworks.xstream.XStreamException
   *          if the object cannot
   *          be serialized
   */
  public String toXML(Object obj) {
    StringWriter writer = new StringWriter();
    toXML(obj, writer);
    return writer.toString();
  }

  /**
   * Serializes the specified object to the specified writer.
   *
   * @param obj    the object to serialized
   * @param writer the writer to write the XML to
   * @throws com.thoughtworks.xstream.XStreamException
   *          if the object cannot
   *          be serialized
   */
  public void toXML(Object obj, Writer writer) {
    Root root = new Root();
    root.obj = obj;
    xstream.toXML(root, writer);

  }

  /**
   * Serializes the specified object to the specified OutputStream.
   *
   * @param obj    the object to serialized
   * @param stream the OutputStream to write the XML to
   * @throws com.thoughtworks.xstream.XStreamException
   *          if the object cannot
   *          be serialized
   */
  public void toXML(Object obj, OutputStream stream) {
    Root root = new Root();
    root.obj = obj;
    xstream.toXML(root, stream);
  }

  /**
   * Deserializes an object from an xml input stream.
   *
   * @param input the input stream
   * @return the deserialized object.
   * @throws com.thoughtworks.xstream.XStreamException
   *          if the object cannot be
   *          deserialized
   */
  public Object fromXML(InputStream input) {
    return xstream.fromXML(input);
  }


  /**
   * Deserializes an object from an xml reader.
   *
   * @param reader the xml reader
   * @return the deserialized object.
   * @throws com.thoughtworks.xstream.XStreamException
   *          if the object cannot be
   *          deserialized
   */
  public Object fromXML(Reader reader) {
    return xstream.fromXML(reader);
  }

  /*
  @SuppressWarnings({"EmptyCatchBlock"})
  private void readHeader(Reader reader) {
    try {
      ObjectInputStream in = xstream.createObjectInputStream(reader);
      String val = (String) in.readObject();
      System.out.println("val = " + val);
    } catch (IOException ex) {
      throw new XStreamException("Error reading header", ex);
    } catch (ClassNotFoundException ex) {
      throw new XStreamException("Error reading header", ex);
    }
  }
  */

  /**
   * Deserializes an object from an xml String.
   *
   * @param xml the xml String
   * @return the deserialized object.
   * @throws com.thoughtworks.xstream.XStreamException
   *          if the object cannot be
   *          deserialized
   */
  public Object fromXML(String xml) {
    return xstream.fromXML(xml);
  }

  /**
   * Sets the classloader used when serializing / deserializing.
   *
   * @param classLoader the classloader to use
   */
  public void setClassLoader(ClassLoader classLoader) {
    xstream.setClassLoader(classLoader);
  }

  /**
   * Set the XML file for converters that need the file path for serializing
   *   out objects to other file types in the same folder as the XML file.
   *   
   * @param xmlFile
   */
  public void setXmlFile(File xmlFile) {
  	for (AbstractConverter con : defaultConverters) {
  		con.setXmlFile(xmlFile);
  	}
  }
}
