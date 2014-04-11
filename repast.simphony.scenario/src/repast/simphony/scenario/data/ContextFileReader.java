package repast.simphony.scenario.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import static repast.simphony.scenario.data.ContextFileIOConstants.*;
/**
 * Reads a model.xml file and produces a ModelData object.
 * 
 * @author Nick Collier
 */
public class ContextFileReader {

	private Stack<ContextData> contexts = new Stack<ContextData>();
	private Stack<AttributeContainer> containers = new Stack<AttributeContainer>();
	private ContextData curContext;
	private AttributeContainer curContainer;

	public ContextData read(File file, Classpath contextClasspath) throws IOException, XMLStreamException {
		curContext = null;
		curContainer = null;
		contexts.clear();
		containers.clear();

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader reader = factory.createXMLEventReader(new BufferedReader(new FileReader(file)));

		while (reader.hasNext()) {
			XMLEvent evt = reader.nextEvent();
			if (evt.isStartElement()) {
				StartElement elmt = evt.asStartElement();
				String qName = elmt.getName().getLocalPart();
				if (qName.equals(CONTEXT_E)) {
					Attribute idA = elmt.getAttributeByName(ID_A);
					Attribute classA = elmt.getAttributeByName(CLASS_A);
					ContextData data; 
					if (classA != null) data = new ContextData(idA.getValue(), classA.getValue(), contextClasspath);
					else data = new ContextData(idA.getValue(), contextClasspath);

					if (curContext != null) contexts.push(curContext);
					if (curContainer != null) containers.push(curContainer);
					curContext = data;
					curContainer = data;
				} else if (qName.equals(PROJECTION_E)) {
					Attribute id = elmt.getAttributeByName(ID_A);
					Attribute type = elmt.getAttributeByName(TYPE_A);
					String pType = type.getValue();
					ProjectionData proj = curContext.addProjection(id.getValue(), pType);
					if (curContainer != null) containers.push(curContainer);
					curContainer = proj;

				} else if (qName.equals(ATTRIBUTE_E)) {
					repast.simphony.scenario.data.Attribute attribute = AttributeFactory.parseAttribute(elmt);
					curContainer.addAttribute(attribute);
				}

			} else if (evt.isEndElement()) {
				EndElement elmt = evt.asEndElement();
				String qName = elmt.getName().getLocalPart();
				if (qName.equals(CONTEXT_E)) {
					if (contexts.size() > 0) {
						ContextData data = contexts.pop();
						data.addSubContext(curContext);
						curContext = data;
					}

					if (containers.size() > 0) {
						curContainer = containers.pop();
					}
				} else if (qName.equals(PROJECTION_E)) {
					if (containers.size() > 0) {
						curContainer = containers.pop();
					}
				}
			}
		}

		return curContext;
	}
}
