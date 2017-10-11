package repast.simphony.dataLoader.engine;

import com.thoughtworks.xstream.converters.Converter;
import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.xml.XMLSerializer;
import simphony.util.messages.MessageCenter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ContextBuilder that loads context from a xml serialized file.
 *
 * @author Nick Collier
 */
public class XMLContextBuilder implements ContextBuilder {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(XMLContextBuilder.class);

  private File file;
  private List<Converter> converters = new ArrayList<Converter>();

  /**
   * Creates an XMLContext builder that will load from the named file.
   *
   * @param fileName the name of the file to load from
   */
  public XMLContextBuilder(String fileName) {
    this.file = new File(fileName);
  }

  /**
   * Creates an XMLContext builder that will load from the named file.
   *
   * @param fileName   the name of the file to load from
   * @param converters a list of XStream Converters to register and use
   *                   in the XML conversion.
   */
  public XMLContextBuilder(String fileName, List<Converter> converters) {
    this.file = new File(fileName);
    this.converters.addAll(converters);
  }


  /**
   * Creates an XMLContext builder that will load from the named file.
   *
   * @param xmlFile    the file to load from
   * @param converters a list of XStream Converters to register and use
   *                   in the XML conversion.
   */
  public XMLContextBuilder(File xmlFile, List<Converter> converters) {
    this.file = xmlFile;
    this.converters.addAll(converters);
  }

  /**
   * Creates an XMLContext builder that will load the specified file
   *
   * @param xmlFile the file to load from
   */
  public XMLContextBuilder(File xmlFile) {
    this.file = xmlFile;
  }

  /**
   * Builds and returns a context. Building a context consists of filling it with
   * agents, adding projects and so forth. The returned context does not necessarily
   * have to be the passed in context.
   *
   * @param context a default context
   * @return the built context.
   */
  public Context build(Context context) {
    XMLSerializer xml = new XMLSerializer();
    xml.setClassLoader(this.getClass().getClassLoader());
    xml.setXmlFile(file);

    for (Converter converter : converters) {
      xml.registerConverter(converter);
    }

    Reader in = null;
    try {
      in = new BufferedReader(new FileReader(file));
      return (Context) xml.fromXML(in);
    } catch (FileNotFoundException e) {
      msg.error("Error while creating context from XML", e);
    } catch (IOException e) {
      msg.error("Error while creating context from XML", e);
    } finally {
      try {
        if (in != null) in.close();
      } catch (IOException ex) {
      }
    }

    return null;
  }

  /**
   * Gets the serialized xml file from which this builder loads
   * a context.
   *
   * @return the serialized xml file from which this builder loads
   *         a context.
   */
  public File getXMLFile() {
    return file;
  }
}