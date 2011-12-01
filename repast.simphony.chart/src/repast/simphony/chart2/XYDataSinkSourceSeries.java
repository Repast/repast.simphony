/**
 * 
 */
package repast.simphony.chart2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.jfree.data.xy.XYSeries;

/**
 * DataSink that appends data into series. A series is created for each
 * individual data source.
 * 
 * @author Nick Collier
 */
public class XYDataSinkSourceSeries extends AbstractXYSeriesDataSink {

  protected static class SeriesData {

    int series;
    double val;

    public SeriesData(int series, double val) {
      this.series = series;
      this.val = val;
    }
  }

  protected static class Row {
    double tick;
    List<SeriesData> data = new ArrayList<SeriesData>();

    public Row() {
    }

    public Row(Row row) {
      this.tick = row.tick;
      data.addAll(row.data);
    }
  }

  private class Updater implements Runnable {

    private Row row;

    public Updater(Row row) {
      this.row = new Row(row);
    }

    public void run() {
      xydata.setUpdate(false);
      for (SeriesData sdata : row.data) {
        xydata.getSeries(sdata.series).add(row.tick, sdata.val);
      }
      xydata.setUpdate(true);
      xydata.update();
    }
  }

  private Map<String, Integer> indexMap = new HashMap<String, Integer>();
  private List<String> sourcesToRecord = new ArrayList<String>();
  protected Row row = new Row();

  /**
   * Creates a XYDataSinkSourceSeries that will add series to the specified
   * XYSeriesCollection and update those series. Each series records successive x and
   * y values where the x value is the value of the "X" (e.g. tick) data source,
   * and the y value is the value returned by that data source.
   * 
   * @param xydata
   */
  public XYDataSinkSourceSeries(BatchUpdateXYSeries xydata) {
    super(xydata);
  }

  /**
   * Creates a XYDataSinkSourceSeries that will add series to the specified
   * XYSeriesCollection and update those series.
   * 
   * @param xydata
   */
  public XYDataSinkSourceSeries(BatchUpdateXYSeries xydata, List<String> sourceIds) {
    this(xydata);
    sourcesToRecord.addAll(sourceIds);
  }

  /*
   * (non-Javadoc)
   * 
   * @see projz.data.DataSink#open(java.util.List)
   */
  @Override
  public void open(List<String> sourceIds) {
    indexMap.clear();

    for (String id : sourceIds) {
      if (!id.equals(xId) && isValidSource(id)) {
        xydata.addSeries(new XYSeries(id, false, false));
        int index = xydata.indexOf(id);
        indexMap.put(id, index);
      }
    }
  }

  private boolean isValidSource(String id) {
    if (sourcesToRecord.size() == 0)
      return true;
    return sourcesToRecord.contains(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see projz.data.DataSink#rowStarted()
   */
  @Override
  public void rowStarted() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see projz.data.DataSink#append(java.lang.String, java.lang.Object)
   */
  @Override
  public void append(String key, Object value) {
    if (key.equals(xId))
      row.tick = ((Number) value).doubleValue();
    else {
      Integer index = indexMap.get(key);
      if (index != null)
        // index will be null if this recording only
        // some of the sources, rather than all of them
        row.data.add(new SeriesData(index, ((Number) value).doubleValue()));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see projz.data.DataSink#rowEnded()
   */
  @Override
  public void rowEnded() {
    // we need to update on the AWT thread.
    if (SwingUtilities.isEventDispatchThread())
      new Updater(row).run();
    else
      // this makes a copy of the row, so
      // it is OK to clear it
      SwingUtilities.invokeLater(new Updater(row));
    row.data.clear();
  }
}
