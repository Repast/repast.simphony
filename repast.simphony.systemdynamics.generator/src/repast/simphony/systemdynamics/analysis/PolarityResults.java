package repast.simphony.systemdynamics.analysis;

import java.util.HashMap;
import java.util.Map;

public class PolarityResults {
    
    Map<String, String> results;

    public PolarityResults() {
	results = new HashMap<String, String>();
    }
    
    public String getPolarityForArrowStartingAt(String tail) {
	return results.get(tail);
    }
    
    public void setPolarityForArrowStartingAt(String tail, String polarity) {
	results.put(tail, polarity);
    }
}
