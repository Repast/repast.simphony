package repast.simphony.scenario.data;


/**
 * Encapsulates agent data in a ContextHeirarchy.
 * Tests for equality using the className property.
 * 
 * @author Nick Collier
 */
public class AgentData{
  
  private String className, shortName;
  
  public AgentData(String className) {
    this.className = className;
    int index = className.lastIndexOf(".");
    if (index != -1 && index != className.length() - 1) {
      shortName = className.substring(index + 1, className.length());
    }
    else {
    	shortName = className;
    }
  }
  
  public boolean equals(Object obj) {
    if (obj instanceof AgentData) {
      return ((AgentData)obj).className.equals(className);
    }
    return false;
  }
  
  public int hashCode() {
    return className.hashCode();
  }

  /**
   * @return the className
   */
  public String getClassName() {
    return className;
  }

  /**
   * @return the shortName
   */
  public String getShortName() {
    return shortName;
  }

}
