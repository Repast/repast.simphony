/**
 * 
 */
package repast.simphony.chart2;

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Overrides some methods in XYSeriesCollection in order to avoid updates
 * whenever a single point is added.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class BatchUpdateXYSeries extends XYSeriesCollection {
  
  private static long UPDATE_INTERVAL = 17;
  
  private boolean update = true;
  private int rangeLength = 0;
  private long lastUpdate = 0;
  
  private volatile boolean running = false;;
  
  public BatchUpdateXYSeries(int plotRangeLength) {
    this.rangeLength = plotRangeLength;
  }
  
  public void setUpdate(boolean update) {
    this.update = update;
  }

  /* (non-Javadoc)
   * @see org.jfree.data.general.AbstractDataset#notifyListeners(org.jfree.data.general.DatasetChangeEvent)
   */
  @Override
  protected void notifyListeners(DatasetChangeEvent evt) {
    if (update)
      super.notifyListeners(evt);
  }
  
  /**
   * Notifies listeners that 
   */
  public void update() {
    long ts = System.currentTimeMillis();
    if (!running || ts - lastUpdate > UPDATE_INTERVAL) {
      super.notifyListeners(new DatasetChangeEvent(this, this));
      lastUpdate = ts;
    }
  }

  /**
   * @return the running
   */
  public boolean isRunning() {
    return running;
  }

  /**
   * Tells this BatchUpdateXYSeries whether the sim is running or not.
   * 
   * @param running
   */
  public void setRunning(boolean running) {
    this.running = running;
  }

  @Override
  public void addSeries(XYSeries series) {
    if (rangeLength > 0) series.setMaximumItemCount(rangeLength);
    super.addSeries(series);
  }
  
  
}
