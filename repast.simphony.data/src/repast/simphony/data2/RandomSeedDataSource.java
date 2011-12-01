/**
 * 
 */
package repast.simphony.data2;

/**
 * DataSource that returns the current random seed.
 * 
 * @author Nick Collier
 */
public class RandomSeedDataSource implements AggregateDataSource,
    NonAggregateDataSource {

  public static final String ID = "random_seed";

  private String id = ID;
  private Integer seed;

  /**
   * Resets the random seed that this data source produces to the specified
   * seed.
   * 
   * @param seed
   *          the new seed.
   */
  public void resetSeed(Integer seed) {
    this.seed = seed;
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
   * @see repast.simphony.data2.NonAggregateDataSource#get(java.lang.Object)
   */
  @Override
  public Integer get(Object obj) {
    return seed;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.AggregateDataSource#get(java.lang.Iterable)
   */
  @Override
  public Integer get(Iterable<?> objs, int size) {
    return seed;
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSource#getSourceType()
   */
  @Override
  public Class<?> getSourceType() {
    return void.class;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.AggregateDataSource#reset()
   */
  @Override
  public void reset() {
  }
}
