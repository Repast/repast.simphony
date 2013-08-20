/**
 * 
 */
package repast.simphony.data2.engine;

import repast.simphony.data2.AggregateOp;

/**
 * @author Nick Collier
 */
public class MethodDataSourceDefinition {
  
  private String id, className, methodName;
  private AggregateOp aggType;

  public MethodDataSourceDefinition(String id, String className, String methodName) {
    this.id = id;
    this.className = className;
    this.methodName = methodName;
  }
  
  public MethodDataSourceDefinition(MethodDataSourceDefinition def){
    this.id = def.id;
    this.className = def.className;
    this.methodName = def.methodName;
    this.aggType = def.aggType;
  }
  
  public boolean equals(Object obj) {
    if (obj instanceof MethodDataSourceDefinition) {
      MethodDataSourceDefinition other = (MethodDataSourceDefinition)obj;
      return id.equals(other.id) && className.equals(other.className) &&
          methodName.equals(other.methodName) && aggType == other.aggType;
      
    }
    return false;
  }
  
  public int hashCode() {
    int hash = 31 * 17 + id.hashCode();
    hash = 31 * hash + className.hashCode();
    hash = 31 * hash + methodName.hashCode();
    if (aggType != null) hash = 31 * hash + aggType.hashCode();
    return hash;
  }
  /**
   * @return the AggregateType
   */
  public AggregateOp getAggregateOp() {
    return aggType;
  }

  /**
   * @param aggType the AggregateType to set
   */
  public void setAggregateOp(AggregateOp aggType) {
    this.aggType = aggType;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @return the className
   */
  public String getObjTargetClass() {
    return className;
  }
  
  public void setObjTargetClass(String className) {
    this.className = className;
  }

  /**
   * @return the methodName
   */
  public String getMethodName() {
    return methodName;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }
}
