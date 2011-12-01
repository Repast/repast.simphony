/**
 * 
 */
package repast.simphony.data2;

/**
 * Interface for classes that can function as the source of data to be logged or
 * charted.
 * 
 * @author Nick Collier
 */
public interface DataSource {

  /**
   * Gets the unique id of this DataSource. The id should be unique across the
   * DataSet that this source is added to.
   * 
   * @return the unique id of this DataSource.
   */
  String getId();

  /**
   * Gets the type of data produced by this DataSource.
   * 
   * @return the type of data produced by this DataSource.
   */
  Class<?> getDataType();
  
  /**
   * Gets the type of the object that this DataSource can retreive data from.
   * 
   * @return the type of the object that this DataSource can retreive data from.
   */
  Class<?> getSourceType();

}
