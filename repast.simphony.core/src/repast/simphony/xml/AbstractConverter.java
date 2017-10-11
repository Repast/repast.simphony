package repast.simphony.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


/**
 * Abstract helper base class for mplementations of a xstream Converters.
 *
 * @author Nick Collier
 */
public abstract class AbstractConverter implements Converter {

  protected Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
  
  // The XML file path of the serialized context.  May be used by implementing
  //   classes that need the path for creating sub-components like serialized
  //   raster files that are not compatible with XML.
  protected File xmlFile;
  
  protected String arrayToString(double[] vals) {
    StringBuilder buf = new StringBuilder();
    for (double val : vals) {
      buf.append(val);
      buf.append(" ");
    }
    return buf.toString().trim();
  }
  
  protected double[] stringToDblArray(String val) {
    String[] vals = val.split(" ");
    double[] ret = new double[vals.length];
    for (int i = 0; i < vals.length; i++) {
      ret[i] = Double.parseDouble(vals[i]);
    }
    return ret;
  }
  
  protected String arrayToString(int[] vals) {
    StringBuilder buf = new StringBuilder();
    for (int val : vals) {
      buf.append(val);
      buf.append(" ");
    }
    return buf.toString().trim();
  }
  
  protected int[] stringToIntArray(String val) {
    String[] vals = val.split(" ");
    int[] ret = new int[vals.length];
    for (int i = 0; i < vals.length; i++) {
      ret[i] = Integer.parseInt(vals[i]);
    }
    return ret;
  }

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

  private Class<?> findClass(HierarchicalStreamReader reader) throws ClassNotFoundException {
    String name = reader.getAttribute("class");
    Class<?> clazz = classMap.get(name);
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

	public File getXmlFile() {
		return xmlFile;
	}

	public void setXmlFile(File xmlFile) {
		this.xmlFile = xmlFile;
	}	
}
