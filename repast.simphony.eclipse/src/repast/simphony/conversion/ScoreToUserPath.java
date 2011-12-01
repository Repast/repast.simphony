/**
 * 
 */
package repast.simphony.conversion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Deque;
import java.util.LinkedList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Creates a user_path xml from a score file.
 * 
 * @author Nick Collier
 */
public class ScoreToUserPath {

  private static final String CONTEXT_E = "SContext";
  private static final String AGENTS_E = "agents";
  private static final String IMP_E = "implementation";
  private static final QName NAME_A = new QName("label");
  private static final QName TYPE_A = new QName("http://www.w3.org/2001/XMLSchema-instance", "type");
  private static final QName BASE_PATH_A = new QName("basePath");
  private static final QName CLASS_NAME_A = new QName("className");
  private static final QName PKG_A = new QName("package");
  private static final QName BIN_DIR_A = new QName("binDir");

  private ScorePathData data;
  private boolean isAgent;
  private Deque<Boolean> readImpStack = new LinkedList<Boolean>();

  /**
   * Runs the conversion on the specified score file, writing the xml to the
   * specified writer.
   * 
   * @param scoreFile
   * @param out
   * @throws IOException
   *           if there is an error reading or writing
   */
  public void run(File scoreFile, Writer out) throws IOException, XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    XMLEventReader reader = factory.createXMLEventReader(new BufferedReader(new FileReader(
        scoreFile)));
    data = null;
    isAgent = false;
    readImpStack.clear();

    while (reader.hasNext()) {
      XMLEvent evt = reader.nextEvent();
      if (evt.isStartElement()) {
        StartElement elmt = evt.asStartElement();
        String qName = elmt.getName().getLocalPart();
        if (qName.equals(CONTEXT_E) && data == null) {
          Attribute attrib = elmt.getAttributeByName(NAME_A);
          data = new ScorePathData(attrib.getValue());
          readImpStack.push(Boolean.TRUE);
          isAgent = false;
        } else if (qName.equals(AGENTS_E)) {
          Attribute attrib = elmt.getAttributeByName(TYPE_A);
          readImpStack.push(attrib == null || !attrib.getValue().equals("score:SContext"));
          isAgent = readImpStack.peek();
        } else if (qName.equals(IMP_E)) {
          processImpElement(elmt);
        } else {
          readImpStack.push(Boolean.FALSE);
        }
      } else if (evt.isEndElement()) {
        EndElement elmt = evt.asEndElement();
        String qName = elmt.getName().getLocalPart();
        if (qName.equals(AGENTS_E)) {
          data.popStack();
        }
        if (!qName.equals(IMP_E)) {
          readImpStack.pop();
        }
      }
    }

    data.write(out);
  }

  private void processImpElement(StartElement elmt) {
    boolean readImp = readImpStack.peek();
    if (readImp) {
      Attribute attrib = elmt.getAttributeByName(BASE_PATH_A);
      String path = attrib == null ? data.getCurrentPath() : "../" +  attrib.getValue();

      attrib = elmt.getAttributeByName(PKG_A);
      String pkg = attrib == null ? data.getCurrentPkg() : attrib.getValue();
      data.pushStack(path, pkg);
      
      if (isAgent) {
        attrib = elmt.getAttributeByName(CLASS_NAME_A);
        data.addAgent(attrib.getValue());
      }
      
      attrib = elmt.getAttributeByName(BIN_DIR_A);
      if (attrib != null) {
        String[] items = attrib.getValue().split(",");
        for (String item : items) {
          item = item.trim();
          if (item.startsWith("./") || item.startsWith("../")) {
            data.addEntry(data.getCurrentPath() + "/" + item);
          } else {
            data.addEntry(item);
          }
        }
      }
    } else {
      data.dupStack();
    }
  }
}
