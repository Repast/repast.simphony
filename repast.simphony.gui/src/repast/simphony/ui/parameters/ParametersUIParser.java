/**
 * 
 */
package repast.simphony.ui.parameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import repast.simphony.parameter.ParameterConstants;

/**
 * Parses a parameters file into chunks suitable for creating a parameters GUI.
 * 
 * @author Nick Collier
 */
public class ParametersUIParser {

  // name, displayName, type, defaultValue, range, values, isReadOnly
  private static final QName PARAMETER_E = new QName("parameter");
  private static final QName NAME_A = new QName("name");
  private static final QName DISPLAY_NAME_A = new QName("displayName");
  private static final QName TYPE_A = new QName("type");
  private static final QName RANGE_A = new QName("range");
  private static final QName VALUES_A = new QName("values");
  private static final QName IS_READ_ONLY_A = new QName("isReadOnly");
  private static final QName GROUP_E = new QName("group");

  public static Map<String, Class<?>> typeMap = new HashMap<String, Class<?>>();

  static {
    typeMap.put("int", int.class);
    typeMap.put("double", double.class);
    typeMap.put("long", long.class);
    typeMap.put("float", float.class);
    typeMap.put("boolean", boolean.class);
    // this is on purpose
    typeMap.put("byte", int.class);
    // as is this
    typeMap.put("short", int.class);
    typeMap.put("String", String.class);
    typeMap.put("string", String.class);
  }

  private String group = ParametersUI.DEFAULT_PARAM_GROUP;

  public void read(ParametersUI paramsUI, File file) throws FileNotFoundException,
      XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    XMLEventReader reader = factory.createXMLEventReader(new BufferedReader(new FileReader(file)));

    while (reader.hasNext()) {
      XMLEvent evt = reader.nextEvent();
      if (evt.isStartElement()) {
        StartElement elmt = evt.asStartElement();
        if (elmt.getName().equals(PARAMETER_E)) {
          processParameter(elmt, paramsUI);
        } else if (elmt.getName().equals(GROUP_E)) {
          group = elmt.getAttributeByName(NAME_A).getValue();
        }
      } else if (evt.isEndElement()) {
        EndElement elmt = evt.asEndElement();
        if (elmt.getName().equals(GROUP_E)) {
          group = ParametersUI.DEFAULT_PARAM_GROUP;
        }
      }
    }
  }

  private void processParameter(StartElement elmt, ParametersUI creator) {
    String name = elmt.getAttributeByName(NAME_A).getValue();
    String displayName = elmt.getAttributeByName(DISPLAY_NAME_A).getValue();
    String type = elmt.getAttributeByName(TYPE_A).getValue();
    String range = elmt.getAttributeByName(RANGE_A) == null ? null : elmt.getAttributeByName(
        RANGE_A).getValue();
    // don't need the values because those should be in the parameter's schema
    boolean isList = elmt.getAttributeByName(VALUES_A) != null;
    boolean readOnly = elmt.getAttributeByName(IS_READ_ONLY_A) == null ? false : Boolean
        .parseBoolean(elmt.getAttributeByName(IS_READ_ONLY_A).getValue());

    if (readOnly) {
      creator.addBinder(group, new ReadOnlyParameterBinder(name, displayName));
    } else if (name.equals(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME)) {
      creator.addBinder(group, new RandomSeedParameterBinder(name, displayName));
    } else if (range != null) {
      if (!(type.equals("int") || type.equals("double") || type.equals("float")))
        throw new IllegalArgumentException(
            "Ranged parameters must be of the int, double, or float type");
      String[] vals = range.split(" ");
      if (vals.length != 3)
        throw new IllegalArgumentException("Invalid range for ranged parameter '" + name + "'");
      if (type.equals("int")) {
        try {
          int min = Integer.parseInt(vals[0]);
          int max = Integer.parseInt(vals[1]);
          int spacing = Integer.parseInt(vals[2]);
          creator.addBinder(group, new RangeParameterBinder(name, displayName, min, max, spacing));
        } catch (NumberFormatException ex) {
          throw new IllegalArgumentException("Invalid range for ranged parameter '" + name + "'");
        }
      } else {
        // type is double or float
        try {
          double min = Double.parseDouble(vals[0]);
          double max = Double.parseDouble(vals[1]);
          double spacing = Double.parseDouble(vals[2]);
          creator.addBinder(group, new FPRangeParameterBinder(name, displayName, min, max, spacing));
        } catch (NumberFormatException ex) {
          throw new IllegalArgumentException("Invalid range for ranged parameter '" + name + "'");
        }
      }
    } else if (isList) {
      creator.addBinder(group, new ListParameterBinder(name, displayName));
    } else if (type.equals("boolean")) {
      creator.addBinder(group, new BooleanParameterBinder(name, displayName));
    } else {
      Class<?> clazz = typeMap.get(type);
      if (clazz == null)
        clazz = Object.class;
      creator.addBinder(group, new DefaultParameterBinder(name, displayName, clazz));
    }
  }
}
