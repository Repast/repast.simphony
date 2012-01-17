/**
 * 
 */
package repast.simphony.data2.builder;

import java.util.Collection;

import repast.simphony.data2.ConsoleDataSink;
import repast.simphony.data2.DataSet;
import repast.simphony.data2.DataSink;
import repast.simphony.data2.DataSource;
import repast.simphony.data2.FormatType;
import repast.simphony.engine.schedule.ScheduleParameters;

/**
 * Interface for classes that can build a DataSet.
 * 
 * @author Nick Collier
 */
public interface DataSetBuilder<T extends DataSource> {
  
  /**
   * Gets whether or not this DataSetBuilder builds an aggregate DataSet.
   * 
   * @return true if this builder builds an aggregate DataSet, otherwise false.
   */
  boolean isAggregate();
  
  /**
   * Gets the id of the DataSet that this will build.
   * 
   * @return the id of the DataSet that this will build.
   */
  String getId();
  
  /**
   * Creates the DataSet.
   * 
   * @return the created DataSet.
   */
  DataSet create();
  
  /**
   * Adds a DataSource to this builder. The created DataSet
   * will use the specified data source as a source of data.
   * 
   * @param dataSource
   */
  void addDataSource(T dataSource);
  
  /**
   * Sets the schedule parameters that determine with the data set will 
   * record its data.
   * 
   * @param params
   */
  void defineScheduleParameters(ScheduleParameters params);
  
  /**
   * Gets the schedule parameters that determine with the data set will 
   * record its data.
   * 
   * @return the schedule parameters that determine with the data set will 
   * record its data.
   */
  public ScheduleParameters getScheduleParameters(); 
  
  /**
   * Adds a FileDataSink for this DataSet. The data retrieved from the
   * defined data sources will be written to the defined DataSink.
   * 
   * @param builder the FileDataSinkBuilder to add
   */
  void addFileDataSinkBuilder(FileDataSinkBuilder builder);
  
  /**
   * Defines a ConsoleDataSink for this DataSet. The data retrieved from the
   * defined data sources will be written to the defined DataSink.
   * 
   * @param target the output stream to write to
   * @param delimiter
   * @param formatType
   */
  void defineConsoleDataSink(ConsoleDataSink.OutputStream target, String delimiter, FormatType formatType, Collection<String> sourceIds);
  
  /**
   * Adds a DataSink directly.
   * 
   * @param sink the sink to add.
   */
  void addDataSink(DataSink sink);

}
