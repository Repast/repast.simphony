/**
 * 
 */
package repast.simphony.data2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.data2.builder.FileNameFormatter;
import repast.simphony.data2.util.DataUtilities;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;

/**
 * DataSink that provides a mapping between a batch run number and the current
 * batch parameters.
 * 
 * @author Nick Collier
 */
public class BatchParamMapFileWriter implements DataSink {

  private Formatter formatter;
  private BufferedWriter writer;
  private FileNameFormatter fnFormatter;
  private String delimiter;
  private FormatType formatType;
  private AggregateDataSource batchRunDS;
  private List<AggregateDataSource> sources;

  public BatchParamMapFileWriter(BatchRunDataSource source, FileNameFormatter fnFormatter,
      String delimiter, FormatType formatType) {
    this.fnFormatter = fnFormatter;
    this.batchRunDS = source;
    this.formatType = formatType;
    this.delimiter = delimiter;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#open()
   */
  @Override
  public final void open(List<String> sourceIds) {
  }

  private void init() {
    sources = new ArrayList<AggregateDataSource>();
    sources.add(batchRunDS);
    Parameters params = RunEnvironment.getInstance().getParameters();
    for (String pName : params.getSchema().parameterNames()) {
      ParameterDataSource ds = new ParameterDataSource(pName);
      sources.add(ds);
    }

    formatter = formatType == FormatType.TABULAR ? new TabularFormatter(sources, delimiter)
        : new LineFormatter(sources, delimiter);

    try {
      String file = fnFormatter.getFilename("batch_param_map");
      DataUtilities.renameFileIfExists(file);
      writer = new BufferedWriter(new FileWriter(file));

      String header = formatter.getHeader();
      if (header.length() > 0) {
        writer.write(formatter.getHeader());
        writer.newLine();
        writer.flush();
      }

    } catch (IOException ex) {
      throw new DataException("Error opening BatchParamMapFileWriter.", ex);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#flush()
   */
  @Override
  public void flush() {
    try {
      if (writer != null) {
        writer.flush();
      }
    } catch (IOException ex) {
      throw new DataException("Error while flushing FileDataSink.", ex);
    }
  }

  /**
   * Notifies this BatchParamMapFileWriter that another batch run has ended, so we
   * write the current parameter values to a file.
   */
  public void runEnded() {
    if (formatter == null) init();
    for (AggregateDataSource source : sources) {
      formatter.addData(source.getId(), source.get(null, 0));
    }

    try {
      writer.write(formatter.formatData());
      writer.newLine();
    } catch (IOException ex) {
      throw new DataException("Error writing to BatchParamMap", ex);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#rowStarted()
   */
  @Override
  public final void rowStarted() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#append(java.lang.String,
   * java.lang.Object)
   */
  @Override
  public final void append(String key, Object value) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#rowEnded()
   */
  @Override
  public final void rowEnded() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#recordEnded()
   */
  @Override
  public final void recordEnded() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#close()
   */
  @Override
  public void close() {
    try {
      writer.flush();
    } catch (IOException ex) {
      throw new DataException("Error closing FileDataSink.", ex);
    } finally {
      try {
        writer.close();
      } catch (IOException ex) {
      }
    }
  }
}
