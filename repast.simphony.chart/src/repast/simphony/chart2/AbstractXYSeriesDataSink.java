package repast.simphony.chart2;

import repast.simphony.data2.DataSink;
import repast.simphony.data2.TickCountDataSource;

public abstract class AbstractXYSeriesDataSink implements DataSink {

  protected BatchUpdateXYSeries xydata;
  protected String xId = TickCountDataSource.ID;

  public AbstractXYSeriesDataSink(BatchUpdateXYSeries xydata) {
    this.xydata = xydata;
  }

  /**
   * Gets the id of the data source that produces the x value.
   * 
   * @return the id of the data source that produces the x value.
   */
  public String getXSourceId() {
    return xId;
  }

  /**
   * Sets the id of the data source that produces the x value.
   * 
   * @param xSourceId
   *          the id of the data source that produces the x value
   */
  public void setXSourceId(String xSourceId) {
    this.xId = xSourceId;
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSink#recordEnded()
   */
  @Override
  public void recordEnded() {}

  /*
   * (non-Javadoc)
   * 
   * @see projz.data.DataSink#close()
   */
  @Override
  public void close() {
    xydata.removeAllSeries();
  }

  /* (non-Javadoc)
   * @see repast.simphony.data2.DataSink#flush()
   */
  @Override
  public void flush() {}
  
  
}
