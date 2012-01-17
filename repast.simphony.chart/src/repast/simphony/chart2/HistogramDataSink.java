/**
 * 
 */
package repast.simphony.chart2;

import java.util.List;

import cern.colt.list.DoubleArrayList;

import repast.simphony.data2.DataSink;
import simphony.util.ThreadUtilities;
import simphony.util.messages.MessageCenter;

/**
 * DataSink that passes data on to a HistogramDataSet.
 * 
 * @author Nick Collier
 */
public class HistogramDataSink implements DataSink {

  private static MessageCenter msg = MessageCenter.getMessageCenter(HistogramDataSink.class);

  private class Updater implements Runnable {
    
    private DoubleArrayList values;
    
    public Updater(DoubleArrayList values) {
      this.values = new DoubleArrayList();
      this.values.addAllOf(values);
    }

    public void run() {
      histData.update(values);
    }
  }

  private class InitDataConverter implements DataConverter {
    @Override
    public double convert(Object obj) {
      if (obj instanceof Number)
        converter = new NumberConverter();
      else if (obj instanceof Boolean)
        converter = new BooleanConverter();
      else {
        String str = "Error while histogramming data. Data Source '" + sourceId + "' is non-numeric.";
        IllegalArgumentException ex = new IllegalArgumentException(str);
        msg.error(str,
            ex);
        throw ex;
      }

      return converter.convert(obj);
    }
  }

  private AbstractHistogramDataset histData;
  private String sourceId;
  private DataConverter converter = new InitDataConverter();
  private DoubleArrayList data = new DoubleArrayList();

  /**
   * Creates a HistogramDataSink that will update the specified histogram
   * dataset with data from the specified source id.
   * 
   * @param data
   * @param sourceId
   */
  public HistogramDataSink(String sourceId, AbstractHistogramDataset data) {
    this.histData = data;
    this.sourceId = sourceId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#open(java.util.List)
   */
  @Override
  public void open(List<String> sourceIds) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#rowStarted()
   */
  @Override
  public void rowStarted() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#append(java.lang.String,
   * java.lang.Object)
   */
  @Override
  public void append(String key, Object value) {
    if (key.equals(sourceId)) {
      double val = converter.convert(value);
      data.add(val);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#rowEnded()
   */
  @Override
  public void rowEnded() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#recordEnded()
   */
  @Override
  public void recordEnded() {
    Updater updater = new Updater(data);
    data.clear();
    ThreadUtilities.runInEventThread(updater);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#flush()
   */
  @Override
  public void flush() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.data2.DataSink#close()
   */
  @Override
  public void close() {
    histData.removeAllBins();
    histData.clearObservations();
  }
}
