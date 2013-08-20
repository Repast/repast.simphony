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
  
  public CustomDataSourceDefinition(CustomDataSourceDefinition val) {
    this.id = val.id;
    this.dsClassName = val.dsClassName;
  }

  public boolean equals(Object obj) {
    if (obj instanceof CustomDataSourceDefinition) {
      CustomDataSourceDefinition other = (CustomDataSourceDefinition)obj;
      return id.equals(other.id) && dsClassName.equals(other.dsClassName);
    }
    return false;
  }
  
  public int hashCode() {
    int hash = 17;
    hash = 31 * hash + id.hashCode();
    return hash * 31 + dsClassName.hashCode();
  }

  public String getId() {
    return id;
  }

  public String getDataSourceClassName() {
    return dsClassName;
  }
}
