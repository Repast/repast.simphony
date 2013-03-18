/**
 * 
 */
package repast.simphony.systemdynamics.support;

import java.util.List;

/**
 * @author bragen
 *
 */
    
    public class DelayedMapping {
	
	private String definitionLHS;
	private String definitionRHS;
	private List<String> definitionLHSValues;
	private List<String> definitionRHSValues;
	
	public DelayedMapping(String definitionLHS, String definitionRHS) {
	    this.definitionLHS = definitionLHS;
	    this.definitionRHS = definitionRHS;
	}
	
	public DelayedMapping(String definitionLHS, String definitionRHS, List<String> definitionLHSValues) {
	    this.definitionLHS = definitionLHS;
	    this.definitionRHS = definitionRHS;
	    this.definitionLHSValues = definitionLHSValues;
	}
	public DelayedMapping(String definitionLHS, String definitionRHS, List<String> definitionLHSValues, List<String> definitionRHSValues) {
	    this.definitionLHS = definitionLHS;
	    this.definitionRHS = definitionRHS;
	    this.definitionLHSValues = definitionLHSValues;
	    this.definitionRHSValues = definitionRHSValues;
	}


	public String getDefinitionLHS() {
	    return definitionLHS;
	}


	public void setDefinitionLHS(String definitionLHS) {
	    this.definitionLHS = definitionLHS;
	}


	public String getDefinitionRHS() {
	    return definitionRHS;
	}


	public void setDefinitionRHS(String definitionRHS) {
	    this.definitionRHS = definitionRHS;
	}


	public List<String> getDefinitionLHSValues() {
	    return definitionLHSValues;
	}


	public void setDefinitionLHSValues(List<String> definitionLHSValues) {
	    this.definitionLHSValues = definitionLHSValues;
	}

	public List<String> getDefinitionRHSValues() {
	    return definitionRHSValues;
	}
    }

