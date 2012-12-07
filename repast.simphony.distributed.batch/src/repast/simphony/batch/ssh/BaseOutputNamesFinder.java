/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import repast.simphony.data2.engine.FileSinkControllerActionIO;

/**
 * Finds the base output name from file_sink xml files in a scenario directory.
 * 
 * @author Nick Collier
 */
public class BaseOutputNamesFinder {

  private static final QName FILENAME = new QName("fileName");

  public List<String> find(String scenarioDirectory) throws IOException, XMLStreamException {
    List<String> names = new ArrayList<String>();
    for (File file : new File(scenarioDirectory).listFiles()) {
      if (file.getName().startsWith(FileSinkControllerActionIO.SERIALIZATION_ID)) {
        names.add(process(new FileInputStream(file)));
      }
    }

    return names;
  }
  
  /**
   * Reads the specified file sink descriptor for the filename.
   * 
   * @param fileSinkDescriptor
   * @return
   * @throws IOException
   * @throws XMLStreamException
   */
  public String find(InputStream fileSinkDescriptor) throws IOException, XMLStreamException {
    return process(fileSinkDescriptor);
  }

  private String process(InputStream in) throws IOException, XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    XMLEventReader reader = factory.createXMLEventReader(in);

    String name = null;
    try {
      while (reader.hasNext()) {
        XMLEvent evt = reader.nextEvent();
        if (evt.isStartElement()) {
          StartElement elm = evt.asStartElement();
          if (elm.getName().equals(FILENAME)) {
            name = reader.nextEvent().asCharacters().getData();
            break;
          }
        }
      }
    } finally {
      reader.close();
    }

    return name;
  }
}
