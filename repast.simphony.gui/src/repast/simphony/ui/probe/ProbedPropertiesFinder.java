package repast.simphony.ui.probe;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.parameter.StringConverter;

/**
 * Finds the probed properties for an object instance. 
 * 
 * @author Nick Collier
 */
public class ProbedPropertiesFinder {
  
  public static class Property {
    String name, displayName;
    Object value;
    StringConverter<?> converter;
    
    Property(String name, String displayName, Object val, StringConverter<?> converter) {
      this.name = name;
      this.displayName = displayName;
      this.value = val;
      this.converter = converter;
    }
    
    public String getName() {
      return name;
    }
    
    public String getDisplayName() {
      return displayName;
    }
    
    public Object getValue() {
      return value;
    }
    
    public StringConverter<?> getConverter() {
      return converter;
    }
  }
  
  // convoluted to create a key that won't be an agent property name
  public final static String NAME_KEY = ProbedPropertiesFinder.class.getCanonicalName() + ".NAME_KEY";
  
  
  public List<ProbedPropertiesFinder.Property> findProperties(Object obj) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, 
  InvocationTargetException {
    
    List<Property> props = new ArrayList<>();
    ProbeInfo pbInfo = ProbeIntrospector.getInstance().getProbeInfo(obj.getClass());
    Property prop = new Property(NAME_KEY, "ID", ProbedPropertyFactory.createProbedTitle(pbInfo, obj), null);
    props.add(prop);
    
    for (MethodPropertyDescriptor mpd : pbInfo.methodPropertyDescriptors()) {
      if (mpd.getReadMethod() != null) {
        Object val = mpd.getReadMethod().invoke(obj, new Object[0]);
        prop = new Property(mpd.getName(), mpd.getDisplayName(), val, mpd.getStringConverter());
        props.add(prop);
      }
    }
    
    for (FieldPropertyDescriptor fpd : pbInfo.fieldPropertyDescriptor()) {
      Object val = fpd.getField().get(obj);
      prop = new Property(fpd.getName(), fpd.getDisplayName(), val, fpd.getStringConverter());
      props.add(prop);
    }
    
    return props;
  }

}
