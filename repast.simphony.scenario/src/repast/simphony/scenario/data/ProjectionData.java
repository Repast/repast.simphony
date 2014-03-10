package repast.simphony.scenario.data;

/**
 * Encapsulates metadata about a projection in ContextData heirarchy.
 * 
 * @author Nick Collier
 */
public class ProjectionData extends AttributeContainer {
  
//  private ProjectionType type;
	private String type;
  
  public ProjectionData(String id, String type) {
    super(id);
//    this.type = type;
  }

  /**
   * Gets the projection's type.
   * 
   * @return the projection's type.
   */
  public String getType() {
    return type;
  }
}
