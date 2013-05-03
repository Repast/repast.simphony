/**
 * 
 */
package repast.simphony.chart2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.jfree.data.xy.XYSeries;

import simphony.util.messages.MessageCenter;

/**
 * DataSink that appends data into series. A series is created for each unique
 * value produced by a specified data source. For example, if the data source
 * corresponds to an agent id, then a series will be created for each unique
 * agent id produced by that source.
 * 
 * @author Nick Collier
 */
public class XYDataSinkValueSeries extends AbstractXYSeriesDataSink {

  private static MessageCenter msg = MessageCenter.getMessageCenter(XYDataSinkValueSeries.class);

  private class InitDataConverter implements DataConverter {
    @Override
    public double convert(Object obj) {
      if (obj instanceof Number)
        converter = new NumberConverter();
      else if (obj instanceof Boolean)
        converter = new BooleanConverter();
      else {
        String str = "Error while charting data. Data Source '" + dataValueSourceId
            + "' is non-numeric.";
        IllegalArgumentException ex = new IllegalArgumentException(str);
        msg.error(str, ex);
        throw ex;
      }

      return converter.convert(obj);
    }
  }

  protected static class SeriesData {

    String key;
    double val;
    double tick;
    boolean addSeries;

  }

  private class Updater implements Runnable {

    private List<SeriesData> data = new ArrayList<SeriesData>();

    public Updater(List<SeriesData> data) {
      this.data.addAll(data);
    }

    public void run() {
      xydata.setUpdate(false);
      for (SeriesData datum : data) {
        if (datum.addSeries) {
          xydata.addSeries(new XYSeries(datum.key));
        }
        xydata.getSeries(datum.key).add(datum.tick, datum.val);
      }

      
      xydata.setUpdate(true);
      xydata.update();
    }
  }

  private String dsSeriesKey, dataValueSourceId;
  private List<SeriesData> allData = new ArrayList<SeriesData>();
  private SeriesData data;
  private Set<String> addedSeries = new HashSet<String>();
  private DataConverter converter = new InitDataConverter();

  /**
   * Creates a XYDataSinkValueSeries that will add series to the specified
   * XYSeriesCollection and update those series. A series is created for each
   * unique value produced by a specified data source. Each series records
   * successive x and y values where the x value is the value of the "X" (e.g.
   * tick) data source, and the y value is the value returned by that data
   * source.
   * 
   * @param xydata
   * @param dataSourceSeriesId
   *          the id of the data source that produces the series value.
   */
  public XYDataSinkValueSeries(BatchUpdateXYSeries xydata, String dataSourceSeriesId,
      String dataValueSourceId) {
    super(xydata);
    dsSeriesKey = dataSourceSeriesId;
    this.dataValueSourceId = dataValueSourceId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see projz.data.DataSink#open(java.util.List)
   */
  @Override
  public void open(List<String> sourceIds) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see projz.data.DataSink#rowStarted()
   */
  @Override
  public void rowStarted() {
    data = new SeriesData();
    allData.add(data);
  }

  /*
   * (non-Javadoc)
   * 
   * @see projz.data.DataSink#append(java.lang.String, java.lang.Object)
   */
  @Override
  public void append(String key, Object value) {
    if (key.equals(xId))
      data.tick = ((Number) value).doubleValue();
    else if (key.equals(dsSeriesKey)) {
      String id = value.toString();
      if (!addedSeries.contains(id)) {
        data.addSeries = true;
        addedSeries.add(id);
      }
      data.key = id + " " + dataValueSourceId;
    } else if (key.equals(dataValueSourceId)) {
      data.val = converter.convert(value);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see projz.data.DataSink#recordEnded()
   */
  @Override
  public void recordEnded() {
    // we need to update on the AWT thread.
    if (SwingUtilities.isEventDispatchThread())
      new Updater(allData).run();
    else
      // this makes a copy of the row, so
      // it is OK to clear it
      SwingUtilities.invokeLater(new Updater(allData));
    allData.clear();
  }

  /*
   * (non-Javadoc)
   * 
   * @see projz.data.DataSink#rowEnded()
   */
  @Override
  public void rowEnded() {
  }

  @Override
  public void close() {
    super.close();
    addedSeries.clear();
  }
}
