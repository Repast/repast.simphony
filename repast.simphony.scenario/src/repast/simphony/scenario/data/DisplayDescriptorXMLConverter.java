package repast.simphony.scenario.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


/**
 * Converts old style S*Impl containing display descriptors to the new format.
 * 
 * @author Nick Collier
 */
public class DisplayDescriptorXMLConverter {

  private static final QName PROJECTIONS_E = new QName("projections");
  private static final QName NAME_E = new QName("name");
  private static final QName PROJ_E = new QName("proj");
  private static final QName STRING_E = new QName("string");

  public void convert(File in, Writer out) throws XMLStreamException, IOException {

    XMLInputFactory factory = XMLInputFactory.newInstance();
    XMLEventReader reader = factory.createXMLEventReader(new BufferedReader(new FileReader(in)));
    IEventProcessor processor = new EventProcessor(reader);
    ProjectionsProcessor pProcessor = new ProjectionsProcessor(reader);

    while (reader.hasNext()) {
      XMLEvent evt = reader.nextEvent();
      if (evt.isStartElement()) {
        QName qName = evt.asStartElement().getName();
        if (qName.equals(PROJECTIONS_E)) {
          processor = pProcessor;
        } else if (qName.equals(PROJ_E)) {
          processor = pProcessor;
        }
      }

      if (evt.isEndElement()) {
        QName qName = evt.asEndElement().getName();
        if (qName.equals(PROJECTIONS_E) || qName.equals(PROJ_E)) {
          processor = new EventProcessor(reader);
        }
      }

      if (!(evt.isStartDocument() || evt.isEndDocument()))
        processor.process(evt, out);
    }

    out.close();
  }
  
  private static String fixText(String txt) {
    txt = txt.replace("&", "&amp;");
    txt = txt.replace("<", "&lt;");
    txt = txt.replace(">", "&gt;");
    txt = txt.replace("\"", "&quot;");
    return txt;
    
  }

  private static interface IEventProcessor {

    void process(XMLEvent evt, Writer writer) throws IOException;
  }

  private static class EventProcessor implements IEventProcessor {

    private XMLEventReader reader;
    
    public EventProcessor(XMLEventReader reader) {
      this.reader = reader;
    }

    public void process(XMLEvent evt, Writer writer) throws IOException {
      if (evt.isStartElement() && (evt.asStartElement().getName().equals(STRING_E) || 
          evt.asStartElement().getName().equals(NAME_E))) {
        StartElement elmt = evt.asStartElement();
        try {
          String txt = reader.getElementText();
          txt = fixText(txt);
          writer.write(String.format("<%s>%s</%s>", elmt.getName(),txt, elmt.getName()));
        } catch (XMLStreamException ex) {
          writer.write(evt.toString());
        }
      } else {
        writer.write(evt.toString());
      }
    }
  }

  private static class ProjectionsProcessor implements IEventProcessor {

    Map<String, String> map = new HashMap<String, String>();
    String id;
    String type;
    XMLEventReader reader;

    List<String> foundTypes = new ArrayList<String>();

    public ProjectionsProcessor(XMLEventReader reader) {
      map.put("repast.score.impl.SNetworkImpl", ProjectionData.NETWORK_TYPE);
      map.put("repast.score.impl.SContinuousSpaceImpl", ProjectionData.CONTINUOUS_SPACE_TYPE);
      map.put("repast.score.impl.SGridImpl", ProjectionData.GRID_TYPE);
      map.put("repast.score.impl.SValueLayerImpl", ProjectionData.VALUE_LAYER_TYPE);
      map.put("repast.score.impl.SGeographyImpl", ProjectionData.GEOGRAPHY_TYPE);
      this.reader = reader;
    }

    public void process(XMLEvent evt, Writer writer) throws IOException {
      if (evt.isStartElement()) {
        StartElement elmt = evt.asStartElement();
        QName qName = elmt.getName();
        if (qName.equals(PROJECTIONS_E)) {
          writer.write("<projections>\n");
        } else if (qName.equals(NAME_E)) {
          try {
            id = reader.getElementText();
            id = fixText(id);
          } catch (XMLStreamException e) {
            e.printStackTrace();
          }
        } else if (qName.equals(PROJ_E)) {
          Attribute attrib = elmt.getAttributeByName(new QName("class"));
          String clazz = "";
          if (attrib != null) {
            clazz = attrib.getValue();
          } else {
            // parse the end of reference
            String ref = elmt.getAttributeByName(new QName("reference")).getValue();
            int index = ref.lastIndexOf("/");
            clazz = ref.substring(index + 1, ref.length());
          }
          String type = map.get(clazz);
          int index = foundTypes.indexOf(type);
          String suffix = index > 0 ? "[" + (index + 1) + "]" : "";
          String txt = String
              .format(
                  "<proj reference=\"../../../../projections/repast.simphony.scenario.data.ProjectionData%s\">%n",
                  suffix);
          writer.write(txt);
        } else {
          String name = qName.getLocalPart();
          type = map.get(name);
          foundTypes.add(type);
        }
      } else if (evt.isEndElement()) {
        EndElement elmt = evt.asEndElement();
        QName qName = elmt.getName();
        if (qName.equals(PROJECTIONS_E)) {
          writer.write("<projections/>\n");
        } else if (map.containsKey(qName.getLocalPart())) {
          writer.write("<repast.simphony.scenario.data.ProjectionData>\n");
          writer.write(String.format("<id>%s</id>%n", id));
          writer.write("<attributes/>\n");
          writer.write(String.format("<type>%s</type>%n", type));
          writer.write("</repast.simphony.scenario.data.ProjectionData>\n");
        }
      }
    }
  }

}
