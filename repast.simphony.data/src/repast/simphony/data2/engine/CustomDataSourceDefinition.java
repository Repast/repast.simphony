package repast.simphony.data2.engine;

/**
 * Definition for a custom data source. Encapsulates an id and the name of a 
 * class that implements the DataSource interface.
 * 
 * @author Nick Collier
 */
public class CustomDataSourceDefinition {
  
  private String id, dsClassName;
  
  public CustomDataSourceDefinition(String id, String className) {
    this.id = id;
    this.dsClassName = className;
  }

  public String getId() {
    return id;
  }

  public String getDataSourceClassName() {
    return dsClassName;
  }
}
