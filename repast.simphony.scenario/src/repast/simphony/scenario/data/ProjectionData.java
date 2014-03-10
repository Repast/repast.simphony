package repast.simphony.scenario.data;

/**
 * Encapsulates metadata about a projection in ContextData heirarchy.
 * 
 * @author Nick Collier
 */
public class ProjectionData extends AttributeContainer {
  
	// These string types help support some legacy code that still needs to
	//   check against a specific projection type.
	public static final String NETWORK_TYPE = "network";
	public static final String GRID_TYPE = "grid";
	public static final String CONTINUOUS_SPACE_TYPE = "continuous space";
	
//  private ProjectionType type;
	private String type;
  
  public ProjectionData(String id, String type) {
    super(id);
    this.type = type;
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
