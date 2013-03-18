/**
 * 
 */
package repast.simphony.systemdynamics.support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Contains information on an array structure
 * 
 * @author bragen
 *
 */
public class ArrayDefinition {
    
    private String name;
    private Map<Integer, Set<String>> subscriptReferences;
    
    public ArrayDefinition(String name, int numDimensions) {
	this.name = name;
	subscriptReferences = new HashMap<Integer, Set<String>>();
	for (int i = 0; i < numDimensions; i++) {
	    subscriptReferences.put(i, new HashSet<String>());
	}
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void addReference(int pos, String subscript) {
	subscriptReferences.get(pos).add(subscript);
    }
    
    public Set<String> getSubscriptReferencesForPos(int pos) {
	return subscriptReferences.get(pos);
    }
    
    public int getNumDimensions() {
	return subscriptReferences.size();
    }
}
