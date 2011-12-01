package repast.simphony.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.HashMap;
import java.util.Map;


/**
 * Abstract helper base class for mplementations of a xstream Converters.
 *
 * @author Nick Collier
 */
public abstract class AbstractConverter implements Converter {

  protected Map<String, Class> classMap = new HashMap<String, Class>();

  /**
   * Gets the next object in the tree. The object must have
   * been written using writeObject.
   *
   * @param parent    the "parent"
   * @param reader    the reader
   * @param umContext the unmarshalling context
   * @return the next Object
   * @throws ClassNotFoundException the class of the object is not found.
   */
  protected Object readNextObject(Object parent, HierarchicalStreamReader reader,
                                  UnmarshallingContext umContext) throws ClassNotFoundException {
    reader.moveDown();
    Object obj = umContext.convertAnother(parent, findClass(reader));
    reader.moveUp();
    return obj;
  }

  /**
   * Reads the next value as a String.
   *
   * @param reader the reader to use
   * @return the read String
   */
  protected String readNextString(HierarchicalStreamReader reader) {
    reader.moveDown();
    String val = reader.getValue();
    reader.moveUp();
    return val;
  }

  private Class findClass(HierarchicalStreamReader reader) throws ClassNotFoundException {
    String name = reader.getAttribute("class");
    Class clazz = classMap.get(name);
    if (clazz == null) {
      clazz = Class.forName(name);
      classMap.put(name, clazz);
    }
    return clazz;
  }

  /**
   * Writes an object to XML.
   *
   * @param nodeName the node name
   * @param obj      the object to write
   * @param writer   the writer used to write
   * @param mContext the marshalling context.
   */
  protected void writeObject(String nodeName, Object obj, HierarchicalStreamWriter writer,
                             MarshallingContext mContext) {
    writer.startNode(nodeName);
    writer.addAttribute("class", obj.getClass().getName());
    mContext.convertAnother(obj);
    writer.endNode();
  }

  /**
   * Writes a string to the xml stream.
   *
   * @param nodeName the node name
   * @param value    the value to write
   * @param writer   the writer used to write the value
   */
  protected void writeString(String nodeName, String value, HierarchicalStreamWriter writer) {
    writer.startNode(nodeName);
    writer.setValue(value);
    writer.endNode();
  }
}
