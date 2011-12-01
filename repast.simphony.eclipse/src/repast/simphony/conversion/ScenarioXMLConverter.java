/**
 * 
 */
package repast.simphony.conversion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Converts a scenario.xml file to one compatible with RS 2.0. This doesn't do
 * much except remove any display elements.
 * 
 * @author Nick Collier
 */
public class ScenarioXMLConverter {
  
  private static final String DISPLAY_ELEMENT = "repast.simphony.action.display";
  private static final String SCENARIO_ELEMENT = "Scenario";
  private static final String LINE_SEP = System.getProperty("line.separator");

  public void run(File scenarioFile, Writer writer) throws IOException, XMLStreamException {
    
    XMLInputFactory factory = XMLInputFactory.newInstance();
    XMLEventReader reader = factory.createXMLEventReader(new BufferedReader(new FileReader(
        scenarioFile)));
    
    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
    writer.write(LINE_SEP);
    
    while (reader.hasNext()) {
      XMLEvent evt = reader.nextEvent();
      if (evt.isStartElement()) {
        StartElement elmt = evt.asStartElement();
        String qName = elmt.getName().getLocalPart();
        if (qName.equals(SCENARIO_ELEMENT)) {
          writer.write("<Scenario>");
          writer.write(LINE_SEP);
        } else if (!qName.equals(DISPLAY_ELEMENT)) {
          writeElement(elmt, writer);
        }
      } else if (evt.isEndElement()) {
        EndElement elmt = evt.asEndElement();
        String qName = elmt.getName().getLocalPart();
        if (qName.equals(SCENARIO_ELEMENT)) {
          writer.write("</Scenario>");
          writer.write(LINE_SEP);
        }
      }
    }
   
    writer.flush();
  }

  private void writeElement(StartElement elmt, Writer writer) throws IOException {
    writer.write(String.format("<%s", elmt.getName().getLocalPart()));
    for (@SuppressWarnings("unchecked")
    Iterator<Attribute> iter = elmt.getAttributes(); iter.hasNext(); ) {
      Attribute attrib = iter.next();
      writer.write(String.format(" %s=\"%s\"", attrib.getName().getLocalPart(), attrib.getValue()));
    }
    writer.write(" />");
    writer.write(LINE_SEP);
  }
}
