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

  private class Updater {

    public void update() {
    }
  }

  private class OneTimeUpdater extends Updater {

    public void update() {
      write();
      updater = new Updater();
    }
  }

  private Formatter formatter;
  private BufferedWriter writer;
  private FileNameFormatter fnFormatter;
  private String delimiter;
  private FormatType formatType;
  private AggregateDataSource batchRunDS;
  private List<AggregateDataSource> sources;
  private Updater updater = new Updater();

  private boolean closed = false;

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
      // this keeps the synthetic parameter used by the distributed batch code
      // to track the run number out of the actual output.
      // we can't use a constant here because that would create bad
      // dependencies.
      if (!pName.equals("repast.simphony.batch.BatchConstantsbatch.name")) {
        ParameterDataSource ds = new ParameterDataSource(pName);
        sources.add(ds);
      }
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
  public synchronized void flush() {
    if (!closed) {
      try {
        if (writer != null) {
          writer.flush();
        }
      } catch (IOException ex) {
        throw new DataException("Error while flushing BatchParamMapFileWriter.", ex);
      }
    }
  }

  /**
   * Notifies this BatchParamMapFileWriter that another batch run has started,
   * so we write the current parameter values to a file.
   */
  public void runStarted() {
    updater = new OneTimeUpdater();
  }

  private void write() {
    if (formatter == null)
      init();
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
    updater.update();
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
  public synchronized void close() {
    if (!closed && writer != null) {
      try {
        writer.flush();
      } catch (IOException ex) {
        throw new DataException("Error closing BatchParamMapFileWriter.", ex);
      } finally {
        try {
          closed = true;
          writer.close();
        } catch (IOException ex) {
        }
      }
    }
  }
}
