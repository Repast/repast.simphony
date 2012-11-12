/**
 * 
 */
package repast.simphony.batch.gui;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Parses a batch parameter file into ParameterData objects that suitable to fill
 * the ParameterInputPanel GUI.
 * 
 * @author Nick Collier
 */
public class BatchParameterParser {
  
  private static final QName PARAMETER = new QName("parameter");
  private static final QName NAME = new QName("name");
  private static final QName TYPE = new QName("type");
  private static final QName VALUE = new QName("value");
  private static final QName VALUES = new QName("values");
  private static final QName START = new QName("start");
  private static final QName END = new QName("end");
  private static final QName STEP = new QName("step");
  
  private File file;
  
  public BatchParameterParser(String file) {
    this.file = new File(file);
  }
  
  /**
   * Parses the batch parameter file into ParameterData. If the file does not 
   * exists this returns an empty list.
   *  
   * @return the parsed data.
   * @throws XMLStreamException 
   * @throws IOException 
   */
  public List<ParameterData> parse() throws IOException, XMLStreamException {
    List<ParameterData> list = new ArrayList<ParameterData>();
    if (!file.exists()) return list;
    
    XMLInputFactory factory = XMLInputFactory.newInstance();
    XMLEventReader reader = factory.createXMLEventReader(new FileReader(file));
    
    while (reader.hasNext()) {
      XMLEvent evt = reader.nextEvent();
      if (evt.isStartElement()) {
        StartElement elm = evt.asStartElement();
        QName elmName = elm.getName();
        
        if (elmName.equals(PARAMETER)) {
          PD parameter = new PD();
          list.add(parameter);
          parameter.data.put(ParameterAttribute.NAME, getAttribute(NAME, elm));
          ParameterType type = ParameterType.parse(getAttribute(TYPE, elm));
          if (type == null) {
            String msg = String.format("Parameter '%s' has an invalid type", parameter.getName());
            throw new IOException(msg);
          }
          parameter.type = type;
          
          if (type == ParameterType.LIST) parseList(elm, parameter);
          else if (type == ParameterType.CONSTANT) parseConstant(elm, parameter);
          else if (type == ParameterType.NUMBER) parseNumber(elm, parameter);
        }
      }
    }
    
    return list;
  }
  
  private void parseList(StartElement elm, PD parameter) throws IOException {
    parameter.data.put(ParameterAttribute.VALUES, getAttribute(VALUES, elm));
  }
  
  private void parseConstant(StartElement elm, PD parameter) throws IOException {
    parameter.data.put(ParameterAttribute.VALUE, getAttribute(VALUE, elm));
  }
  
  private void parseNumber(StartElement elm, PD parameter) throws IOException {
    parameter.data.put(ParameterAttribute.START, getAttribute(START, elm));
    parameter.data.put(ParameterAttribute.END, getAttribute(END, elm));
    parameter.data.put(ParameterAttribute.STEP, getAttribute(STEP, elm));
  }

  private String getAttribute(QName name, StartElement elm) throws IOException {
    Attribute attrib = elm.getAttributeByName(name);
    if (attrib == null || attrib.getValue().trim().length() == 0) 
      throw new IOException(String.format("Batch Parameter is missing required '%s' attribute.", 
        name.getLocalPart()));
    
    return attrib.getValue().trim();
  }
  
static class PD implements ParameterData {
    
    Map<ParameterAttribute, String> data = new HashMap<ParameterAttribute, String>();
    ParameterType type;

    @Override
    public String getName() {
      return data.get(ParameterAttribute.NAME);
    }

    @Override
    public String getAttribute(ParameterAttribute attributeName) {
      return data.get(attributeName);
    }

    @Override
    public ParameterType getType() {
      return type;
    }
    
    public boolean equals(Object obj) {
      if (obj == null) return false;
      if (obj instanceof ParameterData) {
        ParameterData pd = (ParameterData)obj;
        if (pd.getName().equals(getName()) && type.equals(pd.getType())) {
          for (ParameterAttribute key : data.keySet()) {
            String val = pd.getAttribute(key);
            if (val == null) return false;
            if (!val.equals(data.get(key))) return false;
          }
          return true;
        } else {
          return false;
        }
      }
      return false;
    }
  }
}
