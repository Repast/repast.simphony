/**
 * 
 */
package repast.simphony.data2;

import java.util.List;

/**
 * Interface for classes that can retrieve data from a DataSet. DataSink
 * supports grouping appended data into "rows" via the rowStarted() and
 * rowEnded() methods.
 * 
 * @author Nick Collier
 */
public interface DataSink {

  /**
   * Open this DataSink.
   * 
   * @param sourceIds
   *          a list of the DataSource ids for which this is the sink.
   * 
   * @throws DataException
   *           if there is an error opening the sink.
   */
  void open(List<String> sourceIds);

  /**
   * Notifies this DataSink that the next append is that start of a "row." of
   * data.
   */
  void rowStarted();

  /**
   * Appends the specified data to this sink. The key can be, for example, a
   * column name and the value the current value for that column.
   * 
   * @param key
   *          the data's key
   * @param value
   *          the data's value
   */
  void append(String key, Object value);

  /**
   * Notifies this DataSink that the last append was end of a "row."
   */
  void rowEnded();
  
  /**
   * Notified this DataSink that the current record has ended. 
   */
  void recordEnded();
  
  /**
   * Flushes any data buffered by this DataSink.
   */
  void flush();

  /**
   * Close this DataSink.
   */
  void close();

}
