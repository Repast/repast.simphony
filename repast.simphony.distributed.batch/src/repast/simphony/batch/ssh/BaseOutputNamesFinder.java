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
  
  public class FinderOutput {
    private String fname;
    private boolean ts;
    
    public FinderOutput(String fname, boolean ts) {
      this.fname = fname;
      this.ts = ts;
    }
    
    public String getFileName() {
      return fname;
    }
    
    public boolean hasTimestamp() {
      return ts;
    }
  }

  private static final QName FILENAME = new QName("fileName");
  private static final QName TIME_STAMP = new QName("addTimeStamp");

  /**
   * Finds file sinks descriptors in the specified directories and returns a FinderOutput results
   * for each file sink.
   * 
   * @param scenarioDirectory
   * @return
   * @throws IOException
   * @throws XMLStreamException
   */
  public List<FinderOutput> find(String scenarioDirectory) throws IOException, XMLStreamException {
    List<FinderOutput> output = new ArrayList<FinderOutput>();
    for (File file : new File(scenarioDirectory).listFiles()) {
      if (file.getName().startsWith(FileSinkControllerActionIO.SERIALIZATION_ID)) {
        output.add(process(new FileInputStream(file)));
      }
    }

    return output;
  }
  
  /**
   * Reads the specified file sink descriptor for the filename.
   * 
   * @param fileSinkDescriptor
   * @return
   * @throws IOException
   * @throws XMLStreamException
   */
  public FinderOutput find(InputStream fileSinkDescriptor) throws IOException, XMLStreamException {
    return process(fileSinkDescriptor);
  }

  private FinderOutput process(InputStream in) throws IOException, XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    XMLEventReader reader = factory.createXMLEventReader(in);

    String name = null;
    Boolean ts = null;
    try {
      while (reader.hasNext()) {
        XMLEvent evt = reader.nextEvent();
        if (evt.isStartElement()) {
          StartElement elm = evt.asStartElement();
          if (elm.getName().equals(FILENAME)) {
            name = reader.nextEvent().asCharacters().getData();
          } else if (elm.getName().equals(TIME_STAMP)) {
            String val = reader.nextEvent().asCharacters().getData();
            ts = Boolean.valueOf(val);
          }
        }
        
        if (name != null && ts != null) break;
      }
    } finally {
      reader.close();
    }
    
    

    return new FinderOutput(name, ts);
  }
}
