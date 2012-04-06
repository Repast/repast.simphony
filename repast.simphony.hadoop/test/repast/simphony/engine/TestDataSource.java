package repast.simphony.engine;

import java.util.Random;

import repast.simphony.data2.NonAggregateDataSource;

/**
 * DataSource implementation that produces random integers.
 * 
 * @author Nick Collier
 */
public class TestDataSource implements NonAggregateDataSource {
  
  private String id;
  private Integer lastVal;
  private Random random = new Random();
  
  public TestDataSource(String id) {
    this.id = id;
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSource#getId()
   */
  @Override
  public String getId() {
    return id;
  }
  
  public Integer getLastVal() {
    return lastVal;
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSource#getDataType()
   */
  @Override
  public Class<?> getDataType() {
    return Integer.class;
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSource#getSourceType()
   */
  @Override
  public Class<?> getSourceType() {
    return void.class;
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.NonAggregateDataSource#get(java.lang.Object)
   */
  @Override
  public Object get(Object obj) {
    lastVal = random.nextInt(300);
    return lastVal;
  }
}
