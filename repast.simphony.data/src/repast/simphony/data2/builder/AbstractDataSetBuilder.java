package repast.simphony.data2.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import repast.simphony.data2.ConsoleDataSink;
import repast.simphony.data2.DataSink;
import repast.simphony.data2.FormatType;
import repast.simphony.engine.schedule.ScheduleParameters;

public class AbstractDataSetBuilder {

  protected String id;
  private ScheduleParameters scheduleParams;
  protected List<SinkBuilder> sinkBuilders = new ArrayList<SinkBuilder>();

  public AbstractDataSetBuilder(String id) {
    this.id = id;
  }

  public void defineScheduleParameters(ScheduleParameters params) {
    this.scheduleParams = params;
  }

  public ScheduleParameters getScheduleParameters() {
    return scheduleParams;
  }

  public String getId() {
    return id;
  }

  /**
   * Defines a FileDataSink for this DataSet. The data retrieved from the
   * defined data sources will be written to the defined DataSink.
   * 
   * @param fileName
   * @param delimiter
   * @param formatType
   * @param addTimeStamp
   * @param sourceIds
   */
  public void addFileDataSinkBuilder(FileDataSinkBuilder builder) {
    sinkBuilders.add(builder);
  }

  /**
   * Defines a ConsoleDataSink for this DataSet. The data retrieved from the
   * defined data sources will be written to the defined DataSink.
   * 
   * @param target
   *          the output stream to write to
   * @param delimiter
   * @param formatType
   */
  public void defineConsoleDataSink(ConsoleDataSink.OutputStream target, String delimiter,
      FormatType formatType, Collection<String> sourceIds) {
    ConsoleDataSinkBuilder builder = new ConsoleDataSinkBuilder(target, delimiter, formatType);
    for (String id : sourceIds) {
      builder.addSource(id);
    }
    sinkBuilders.add(builder);
  }

  /**
   * Adds a DataSink directly to this DataSetBuilder.
   * 
   * @param sink
   *          the DataSink to add
   */
  public void addDataSink(DataSink sink) {
    sinkBuilders.add(new DummySinkBuilder(sink));
  }
}