/**
 * 
 */
package repast.simphony.data2;

/**
 * AggregateDataSource that returns the count of a particular type of object.
 * Given that data sources operate on specific object types, this can just
 * return the size argument.
 * 
 * @author Nick Collier
 */
public class CountDataSource implements AggregateDataSource {

  private String id;
  private Class<?> sourceType;

  /**
   * @param id
   */
  public CountDataSource(String id, Class<?> sourceType) {
    this.id = id;
    this.sourceType = sourceType;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSource#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSource#getDataType()
   */
  @Override
  public Class<Integer> getDataType() {
    return Integer.class;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.AggregateDataSource#get(java.lang.Iterable, int)
   */
  @Override
  public Integer get(Iterable<?> objs, int size) {
    return size;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.AggregateDataSource#reset()
   */
  @Override
  public void reset() {
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSource#getSourceType()
   */
  @Override
  public Class<?> getSourceType() {
    return sourceType;
  }
}
