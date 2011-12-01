/**
 * 
 */
package repast.simphony.data2.engine;

import repast.simphony.data2.NonAggregateDataSource;

/**
 * An NonAggregateDataSource implementation that delegates to 
 * a wrapped AggregateDataSource for all calls except getting
 * the id. 
 * 
 * @author Nick Collier
 */
public class NonAggregateDataSourceWrapper implements NonAggregateDataSource {
  
  private NonAggregateDataSource dataSource;
  private String id;
  
  public NonAggregateDataSourceWrapper(String id, NonAggregateDataSource dataSource) {
    this.id = id;
    this.dataSource = dataSource;
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSource#getId()
   */
  @Override
  public String getId() {
    return id;
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSource#getDataType()
   */
  @Override
  public Class<?> getDataType() {
    return dataSource.getDataType();
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSource#getSourceType()
   */
  @Override
  public Class<?> getSourceType() {
    return dataSource.getSourceType();
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.NonAggregateDataSource#get(java.lang.Object)
   */
  @Override
  public Object get(Object obj) {
    return dataSource.get(obj);
  }
}
