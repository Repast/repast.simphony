/**
 * 
 */
package repast.simphony.systemdynamics.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.systemdynamics.translator.InformationManagers;

/**
 * This class holds the values of the subscripts for this combination
 * 
 * @author bragen
 *
 */
public class SubscriptCombination {

    private Map<String, String> values;
    private List<String> order;
    
    public SubscriptCombination(List<String> subOrder) {
	values = new HashMap<String, String>();
	order = new ArrayList<String>(subOrder);
    }
    
    public void addSubscriptValue(String subscript, String value) {
	values.put(subscript, value);
    }
    
    public String getSubscriptValue(String subscript) {
	if (!values.containsKey(subscript)) {
	    
	    // here is where we would intercept a mapped reference
	    // the subscript is the name of the subscript
	    // we go to the subscript mapper
	    // ask if from the list of orders, there is a mapping from
	    // subscript -> LHS. The mapper would select the appropriate
	    // value in questions and map it back.
	    String alternate = InformationManagers.getInstance().getMappedSubscriptManager().getMappedValue(subscript, order, this);
	    if (alternate != null)
		return alternate;
	    else
		return subscript;
	} else {
	    return values.get(subscript);
	}
    }
    
    public String getSubscriptValueWithoutMapping(String subscript) {
	if (!values.containsKey(subscript)) {
	    
//	    // here is where we would intercept a mapped reference
//	    // the subscript is the name of the subscript
//	    // we go to the subscript mapper
//	    // ask if from the list of orders, there is a mapping from
//	    // subscript -> LHS. The mapper would select the appropriate
//	    // value in questions and map it back.
//	    String alternate = MappedSubscriptManager.getMappedValue(subscript, order, this);
//	    if (alternate != null)
//		return alternate;
//	    else
		return subscript;
	} else {
	    return values.get(subscript);
	}
    }
    
    public String getSubscriptValue() {
	// returns order, concatenated subscript
	String concatenated = "";
	for (String sub : order) {
	    if (concatenated.length() > 0)
		concatenated += ",";
	    concatenated += getSubscriptValueWithoutMapping(sub);
	}
	
	return concatenated;
    }
}
