/**
 * 
 */
package repast.simphony.data2.engine;

import repast.simphony.data2.AggregateDataSource;

/**
 * An AggregateDataSource implementation that delegates to 
 * a wrapped AggregateDataSource for all calls except getting
 * the id. 
 * 
 * @author Nick Collier
 */
public class AggregateDataSourceWrapper implements AggregateDataSource {
  
  private AggregateDataSource dataSource;
  private String id;
  
  public AggregateDataSourceWrapper(String id, AggregateDataSource dataSource) {
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
   * @see repast.simphony.data2.AggregateDataSource#get(java.lang.Iterable, int)
   */
  @Override
  public Object get(Iterable<?> objs, int size) {
   return dataSource.get(objs, size);
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.data2.AggregateDataSource#reset()
   */
  @Override
  public void reset() {
    dataSource.reset();
  }


  
}
