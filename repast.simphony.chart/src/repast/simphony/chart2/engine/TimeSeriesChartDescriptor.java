/**
 * 
 */
package repast.simphony.chart2.engine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ChartDescriptor for TimeSeries type charts where tick is the 
 * time value. This descriptor can provide metadata for aggregate type time series
 * where each column of data is a series (e.g. "Agent Count") or non-aggregate
 * time series where each unique value of some data column becomes a series (e.g.
 * "Agent Id", and the data is the value of the dataValueId passed in the constructor.
 * 
 * @author Nick Collier
 */
public class TimeSeriesChartDescriptor extends ChartDescriptor {
  
  public static class SeriesData {
    
    String label;
    Color color;
    
    public SeriesData(String label, Color color) {
      this.label = label;
      this.color = color;
    }
  }
  
  // key is the series id, value is the chart series label to use for that id.
  private Map<String, SeriesData> seriesIds = new HashMap<String, SeriesData>();
  private String dataValueId;
  private List<String> dataValueIds = new ArrayList<String>();
  private int plotRangeLength = -1;
  
  // used by xstream to deserialize uisng the JavaReflectionProvider
  @SuppressWarnings("unused")
  private TimeSeriesChartDescriptor() {
    super();
  }

  public TimeSeriesChartDescriptor(String name) {
    super(name);
    type = ChartType.TIME_SERIES;
    xAxisLabel = "Tick Count";
  }
  
  public TimeSeriesChartDescriptor(String name, String dataValueId) {
    super(name);
    type = ChartType.TIME_SERIES;
    this.dataValueId = dataValueId;
    xAxisLabel = "Tick Count";
  }
  
  /**
   * @return the plotRangeLength
   */
  public int getPlotRangeLength() {
    return plotRangeLength;
  }

  /**
   * @param plotRangeLength the plotRangeLength to set
   */
  public void setPlotRangeLength(int plotRangeLength) {
    this.plotRangeLength = plotRangeLength;
  }

  /**
   * Gets the id of the data source used to provide the data for the time series.
   * This is only applicable for time series of non-aggregate data.
   * 
   * @return the id of the data source used to provide the data for the time series.
   */
  public String getDataValueId() {
    return dataValueId;
  }
  
  /**
   * Sts the id of the data source used to provide the data for the time series.
   * This is only applicable for time series of non-aggregate data.
   * 
   * param dataValueId the id of the data source used to provide the data for the time series
   */
  public void setDataValueId(String dataValueId) {
    this.dataValueId = dataValueId;
    
  }
  
  /**
   * Adds the id of a data source to record as a series.
   * 
   * @param id
   * @param label the chart series label for this id
   */
  public void addSeriesId(String id, String label, Color color) {
    seriesIds.put(id, new SeriesData(label, color));
  }
  
  /**
   * Adds the id of a data source to to record
   * as a series. 
   */
  public void addDataValueId(String dataValueId) {
    if (!dataValueIds.contains(dataValueId))
      dataValueIds.add(dataValueId);
  }
  
  /**
   * Gets an iterable over the data value ids.
   * @return
   */
  public Iterable<String> dataValueIds() {
    // this is necessary to make this version of the descriptor
    // compatible with previous serialized versions.
    if (dataValueId != null && !dataValueIds.contains(dataValueId)) dataValueIds.add(dataValueId);
    return dataValueIds;
  }
  
  /**
   * Clears the series data in this descriptor.
   * 
   */
  public void clearSeriesIds() {
    seriesIds.clear();
  }
  
  /**
   * Gets a list of the data source ids to use as series in the chart.
   * 
   * @return a list of the data source ids to use as series in the chart.
   */
  public List<String> getSeriesIds() {
    return new ArrayList<String>(seriesIds.keySet());
  }
  
  /**
   * Gets the label for the specified series.
   * 
   * @param seriesId
   * 
   * @return the label for the specified series.
   */
  public String getSeriesLabel(String seriesId) {
    return seriesIds.get(seriesId).label;
  }
  
  /**
   * Gets the color of the specified series.
   * 
   * @param seriesId
   * 
   * @return the color of the specified series, or null if there
   * is no color.
   */
  public Color getSeriesColor(String seriesId) {
    SeriesData data = seriesIds.get(seriesId);
    return data == null ? null : data.color;
  }
}
