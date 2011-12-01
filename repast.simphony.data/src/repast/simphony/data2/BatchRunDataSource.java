/**
 * 
 */
package repast.simphony.data2;

/**
 * DataSource that returns the current batch run number.
 * 
 * @author Nick Collier
 */
public class BatchRunDataSource implements AggregateDataSource,
    NonAggregateDataSource {

  public static final String ID = "run";

  private String id = ID;
  private int batchRun;

  /**
   * Resets the batch run number to the specified batch run.
   * 
   * @param batchRun
   */
  public void resetBatchRun(int batchRun) {
    this.batchRun = batchRun;
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
  public Object get(Object obj) {
    return batchRun;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.AggregateDataSource#get(java.lang.Iterable)
   */
  @Override
  public Object get(Iterable<?> objs, int size) {
    return batchRun;
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
