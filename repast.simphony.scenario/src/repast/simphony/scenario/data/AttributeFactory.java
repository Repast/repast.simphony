package repast.simphony.scenario.data;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;

import repast.simphony.parameter.ParameterFormatException;
import repast.simphony.parameter.ParameterType;
import repast.simphony.parameter.ParameterTypeFactory;
import repast.simphony.parameter.StringConverter;
import repast.simphony.parameter.StringConverterFactory;
import simphony.util.messages.MessageCenter;

public class AttributeFactory {
  
  private static MessageCenter msg = MessageCenter.getMessageCenter(AttributeFactory.class);
  
  public static Map<String, Class<?>> typeMap = new HashMap<String, Class<?>>();

  static {
    typeMap.put("int", int.class);
    typeMap.put("double", double.class);
    typeMap.put("long", long.class);
    typeMap.put("float", float.class);
    typeMap.put("boolean", boolean.class);
    typeMap.put("byte", byte.class);
    typeMap.put("short", short.class);
    typeMap.put("String", String.class);
    typeMap.put("string", String.class);
  }
  
  private static final QName ID_A = new QName("id");
  private static final QName TYPE_A = new QName("type");
  private static final QName NAME_A = new QName("display_name");
  private static final QName CONVERTER_A = new QName("converter");
  private static final QName VALUE_A = new QName("value");
  
  public static String getTypeName(Class<?> type) {
    for (Map.Entry<String,Class<?>> entry : typeMap.entrySet()) {
      if (entry.getValue().equals(type)) return entry.getKey();
    }
    
    return type.getName();
  }
  
  public static Attribute parseAttribute(StartElement elmt) {
    DefaultAttribute attribute = new DefaultAttribute();
    javax.xml.stream.events.Attribute xmlA = elmt.getAttributeByName(ID_A);
    attribute.id = xmlA.getValue().trim();
    
    xmlA = elmt.getAttributeByName(VALUE_A);
    attribute.value = xmlA.getValue();
    
    xmlA = elmt.getAttributeByName(NAME_A);
    if (xmlA == null) attribute.name = attribute.id;
    else attribute.name = xmlA.getValue();
    
    //System.out.println("attribute.name = " + attribute.name);
    
    xmlA = elmt.getAttributeByName(TYPE_A);
    String clazz = xmlA.getValue();
    
    Class<?> type = typeMap.get(clazz);
    if (type == null) {
      try {
      type = Class.forName(clazz, false, Attribute.class.getClassLoader());
      } catch (ClassNotFoundException ex) {
        msg.error("Error finding type class for attribute '" + attribute.getId() + "'", ex);
      }
    }
    attribute.type = type;
    
    xmlA = elmt.getAttributeByName(CONVERTER_A);
    if (xmlA != null) {
      try {
        Class<?> conClazz = Class.forName(xmlA.getValue(), true, Attribute.class.getClassLoader());
        attribute.converter = (StringConverter)conClazz.newInstance();
      } catch (ClassNotFoundException ex) {
        msg.error("Error creating StringConverter for attribute '" + attribute.getId() + "'", ex);
      } catch (InstantiationException e) {
        msg.error("Error creating StringConverter for attribute '" + attribute.getId() + "'", e);
      } catch (IllegalAccessException e) {
        msg.error("Error creating StringConverter for attribute '" + attribute.getId() + "'", e);
      }
    } else {
      attribute.converter = StringConverterFactory.instance().getConverter(type);
    }
    
    return attribute;
  }
  
  public static ParameterType toParameterType(Attribute attrib) {
    ParameterType type = ParameterTypeFactory.instance().getParameterType(attrib.getType());
    if (type == null) {
      type = new PType(attrib.getType(), attrib.getConverter());
    }
    return type;
  }
  
  public static class PType implements ParameterType {
    
    Class<?> clazz;
    StringConverter conv;
    
    public PType(Class<?> clazz, StringConverter conv) {
      this.clazz = clazz;
      this.conv = conv;
    }

    /* (non-Javadoc)
     * @see repast.simphony.parameter.ParameterType#getConverter()
     */
    public StringConverter getConverter() {
      return conv;
    }

    /* (non-Javadoc)
     * @see repast.simphony.parameter.ParameterType#getJavaClass()
     */
    public Class getJavaClass() {
      return clazz;
    }

    /* (non-Javadoc)
     * @see repast.simphony.parameter.ParameterType#getValue(java.lang.String)
     */
    public Object getValue(String val) throws ParameterFormatException {
      return conv.fromString(val);
    }
    
  }
}
