package repast.simphony.engine;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.OutputCollector;

import repast.simphony.data2.DataSink;

/**
 * DataSink for collecting Repast output in a hadoop 
 * compatible format.  
 * 
 * @author Nick Collier
 */
public class HadoopDataSink implements DataSink {
  
  private OutputCollector<Text, Text> output;
  private HadoopFormatter formatter;
  private Text id;
  
  public HadoopDataSink(String fileId, HadoopFormatter formatter, OutputCollector<Text, Text> output) {
    this.output = output;
    this.formatter = formatter;
    this.id = new Text(fileId);
  }
  
  /**
   * Resets this HadoopDataSink to use the specified OutputCollector.
   * 
   * @param output
   */
  public void resetsOutputCollector(OutputCollector<Text, Text> output) {
    this.output = output;
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSink#open(java.util.List)
   */
  @Override
  public void open(List<String> sourceIds) {}

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSink#rowStarted()
   */
  @Override
  public void rowStarted() {
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSink#append(java.lang.String, java.lang.Object)
   */
  @Override
  public void append(String key, Object value) {
    formatter.addData(key, value);
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSink#rowEnded()
   */
  @Override
  public void rowEnded() {
    try {
      output.collect(id, new Text(formatter.formatData()));
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSink#recordEnded()
   */
  @Override
  public void recordEnded() {}

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSink#flush()
   */
  @Override
  public void flush() {}

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSink#close()
   */
  @Override
  public void close() {}
}
