package repast.simphony.ui.probe;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

public interface LocationProbe {

	 public PropertyDescriptor getLocationDescriptor() throws IntrospectionException; 
	
}
