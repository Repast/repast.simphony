/**
 * 
 */
package repast.simphony.systemdynamics.support;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bragen
 *
 */
public class NamedSubscriptDefinition {

    private String name;
    private List<String> values;
    
    public NamedSubscriptDefinition(String name, List<String> vals) {
	this.name = name;
	values = new ArrayList<String>(vals);
    }
    
    public NamedSubscriptDefinition(String name, String...vals) {
	this.name = name;
	values = new ArrayList<String>();
	for (String val : vals)
	    values.add(val);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
    
    public int getNumValues() {
	return values.size();
    }
    
    public int getPosition(String value) {
	for (int i = 0; i < values.size(); i++)
	    if (values.get(i).equals(value))
		return i;
	return -1;
    }
}
