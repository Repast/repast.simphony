package repast.simphony.ui.probe;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import repast.simphony.parameter.StringConverter;
import repast.simphony.ui.RSApplication;

/**
 * Finds the probed properties for an object instance. 
 * 
 * @author Nick Collier
 */
public class ProbedPropertiesFinder {
	
  public static class Property implements Comparable<Property>{
    String name, displayName;
    Object value;
    StringConverter<?> converter;
    ProbedPropertyUICreator uiCreator;
    Class<?> type;
    
    Property(String name, String displayName, Object val, 
    		StringConverter<?> converter, Class<?> type) {
      this.name = name;
      this.displayName = displayName;
      this.value = val;
      this.converter = converter;
      this.type = type;
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

		public Class<?> getType() {
			return type;
		}

		public ProbedPropertyUICreator getUiCreator() {
			return uiCreator;
		}

		public void setUiCreator(ProbedPropertyUICreator uiCreator) {
			this.uiCreator = uiCreator;
		}

		@Override
		public int compareTo(Property other) {
		   return name.compareTo(other.getName());
		}
  }
  
  // convoluted to create a key that won't be an agent property name
  public final static String NAME_KEY = ProbedPropertiesFinder.class.getCanonicalName() + ".NAME_KEY";
  
  public List<ProbedPropertiesFinder.Property> findProperties(Object obj) throws 
  IntrospectionException, IllegalAccessException, IllegalArgumentException, 
  InvocationTargetException {
    
  	Set<String> propNames = new HashSet<String>();
  	
    List<Property> props = new ArrayList<>();
    ProbeInfo pbInfo = ProbeIntrospector.getInstance().getProbeInfo(obj.getClass());
    Property prop = new Property(NAME_KEY, "ID", 
    		ProbedPropertyFactory.createProbedTitle(pbInfo, obj), null, String.class);
    props.add(prop);
    
    // Fields are done first so that instrumented classes (eg groovy) with 
    //   generated getters are not added.
    createFieldProperties(pbInfo, obj, props);
    
    for (ProbedPropertiesFinder.Property p : props){
    	propNames.add(p.getName());
    }
    
    for (MethodPropertyDescriptor mpd : pbInfo.methodPropertyDescriptors()) {
      if (mpd.getReadMethod() != null) {
        Object val = mpd.getReadMethod().invoke(obj, new Object[0]);
        
        if (propNames.contains(mpd.getName())){
//        	System.out.println("Ignoring duplicate method prop " + mpd.getName() + 
//        			" for " + obj.getClass());
        }
        else {
        	prop = new Property(mpd.getName(), mpd.getDisplayName(), val, 
        			mpd.getStringConverter(), mpd.getPropertyType());
        	props.add(prop);
        }
      }
    }
    
    return props;
  }
  
  private void createFieldProperties(ProbeInfo pbInfo, Object obj, 
  		List<Property> props) throws IllegalAccessException {
    
  	ProbeManager probeManager = RSApplication.getRSApplicationInstance().getProbeManager();
  	Map<Class<?>, PPUICreatorFactory> creatorMap = probeManager.getUiCreatorMap();

  	for (FieldPropertyDescriptor fpd : pbInfo.fieldPropertyDescriptor()) {
  		Object val = fpd.getField().get(obj);
  		Class<?> clazz = fpd.getField().getType();
  		Property prop = new Property(fpd.getName(), fpd.getDisplayName(), val, 
  				fpd.getStringConverter(), clazz);
  		props.add(prop);
  		
  		PPUICreatorFactory fac  = null;

  		// try to find exact match
  		for (Class<?> key : creatorMap.keySet()) {
  			if (clazz.equals(key)) fac = creatorMap.get(key);
  		}

  		// get assignable
  		if (fac == null) {
  			// try to find exact match
  			for (Class<?> key : creatorMap.keySet()) {
  				if (key.isAssignableFrom(clazz)) fac = creatorMap.get(key);
  			}
  		}

  		if (fac != null) {
  			prop.setUiCreator(fac.createUICreator(obj, fpd));
  		}
  	}
  }
}
