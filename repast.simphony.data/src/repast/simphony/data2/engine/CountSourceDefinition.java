/**
 * 
 */
package repast.simphony.data2.engine;

/**
 * Definition for a CountDataSource.
 * 
 * @author Nick Collier
 */
public class CountSourceDefinition {
  
  private String typeName, id;

  /**
   * @param typeName
   * @param id
   */
  public CountSourceDefinition(String id, String typeName) {
    this.typeName = typeName;
    this.id = id;
  }

  /**
   * @return the typeName
   */
  public String getTypeName() {
    return typeName;
  }

  /**
   * @param typeName the typeName to set
   */
  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }
  
  

}
