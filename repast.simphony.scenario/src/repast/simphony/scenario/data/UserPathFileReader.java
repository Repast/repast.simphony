package repast.simphony.scenario.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Reads a model.xml file and produces a ModelData object.
 * 
 * @author Nick Collier
 */
public class UserPathFileReader {

  private static final String MODEL_E = "model";
  // private static final String CLASSPATH_E = "classpath";
  private static final String ENTRY_E = "entry";
  private static final String AGENT_E = "agents";
  private static final String BUILTIN_E = "builtin";
  private static final String LIST_SEPARATOR = ",";
  private static final QName NAME_A = new QName("name");
  private static final QName PATH_A = new QName("path");
  private static final QName FILTER_A = new QName("filter");
  private static final QName ANNOTATION_A = new QName("annotations");
  private static final QName FULLNAME_A = new QName("fullname");
  private static final String LIB_PATH = "../lib";

  public UserPathData read(File file) throws IOException, XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    XMLEventReader reader = factory.createXMLEventReader(new BufferedReader(new FileReader(file)));
    UserPathData data = null;

    while (reader.hasNext()) {
      XMLEvent evt = reader.nextEvent();
      if (evt.isStartElement()) {
        StartElement elmt = evt.asStartElement();
        String qName = elmt.getName().getLocalPart();
        if (qName.equals(MODEL_E)) {
          Attribute attrib = elmt.getAttributeByName(NAME_A);
          data = new UserPathData(attrib.getValue());
        } else if (qName.equals(ENTRY_E)) {
          processEntry(elmt, data, file);
        } else if (qName.equals(AGENT_E)) {
          processAgent(elmt, data, file);
        } else if (qName.equals(BUILTIN_E)) {
          processBuiltin(elmt, data, file);
        }
      }
    }

    return data;
  }

  private void processAgent(StartElement elmt, UserPathData data, File modelFile)
      throws IOException {
    Attribute entry = elmt.getAttributeByName(PATH_A);
    Attribute pkg = elmt.getAttributeByName(FILTER_A);

    List<String> pkgFilters = new ArrayList<String>();
    if (pkg != null) {
      for (StringTokenizer tok = new StringTokenizer(pkg.getValue(), LIST_SEPARATOR); tok
          .hasMoreTokens();) {
        pkgFilters.add(tok.nextToken().trim());
      }
    }
    if (pkgFilters.size() == 0)
      pkgFilters.add("*");

    for (StringTokenizer tok = new StringTokenizer(entry.getValue(), LIST_SEPARATOR); tok
        .hasMoreTokens();) {
      String file = parsePath(tok.nextToken(), modelFile);
      data.addAgentEntry(file, pkgFilters);
    }
  }

  private String parsePath(String path, File modelFile) throws IOException {
    path = path.trim();
    if (path.startsWith("..")) {
      // relative to the model file
      File parent = modelFile.getParentFile().getParentFile();
      // by default the repast wizards adds a "../lib" to the user_path. If
      // this is empty or it doesn't exist then we just ignore it.
      boolean checkForLib = path.equals(LIB_PATH);
      path = parent.getCanonicalPath() + path.substring(2, path.length());
      if (checkForLib) {
        File fpath = new File(path);
        if (!fpath.exists()) path = "";
        else if (fpath.isDirectory()) {
          String[] files = fpath.list();
          boolean empty = true;
          for (String file : files) {
            if (!file.endsWith(".txt") && !file.startsWith(".")) {
              empty = false;
              break;
            }
          }
          if (empty) path = "";
        }
      }
    } else if (path.startsWith(".")) {
      File parent = modelFile.getParentFile();
      path = parent.getCanonicalPath() + path.substring(1, path.length());
    }
    
    return path;
  }

  private void processEntry(StartElement elmt, UserPathData data, File modelFile)
      throws IOException {
    // attributes should be either jar or dir, with "," separated
    // entries for each
    Attribute entry = elmt.getAttributeByName(PATH_A);
    Attribute annotation = elmt.getAttributeByName(ANNOTATION_A);

    boolean process = annotation != null && annotation.getValue().equalsIgnoreCase("true");

    StringTokenizer tok = new StringTokenizer(entry.getValue(), LIST_SEPARATOR);
    while (tok.hasMoreTokens()) {
      String file = parsePath(tok.nextToken(), modelFile);
      if(file.length() > 0) data.addEntry(file, process);
    }
  }

  private void processBuiltin(StartElement elmt, UserPathData data, File modelFile)
      throws IOException {
    // the fullname should be the full name of the class, e.g.,
    // repast.simphony.relogo.WayPoint
    Attribute fullname = elmt.getAttributeByName(FULLNAME_A);
    List<String> pkgFilters = new ArrayList<String>();
    pkgFilters.add(fullname.getValue().trim());

    String file;
    try {
      URL url = Class.forName(fullname.getValue()).getProtectionDomain().getCodeSource()
          .getLocation();
      file = url.toURI().getPath();
      data.addAgentEntry(file, pkgFilters);
    } catch (URISyntaxException ex) {
      ex.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

  }
}
