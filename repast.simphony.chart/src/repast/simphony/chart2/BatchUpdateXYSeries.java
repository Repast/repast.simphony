/**
 * 
 */
package repast.simphony.chart2;

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Overrides some methods in XYSeriesCollection in order to avoid updates
 * whenever a single point is added.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class BatchUpdateXYSeries extends XYSeriesCollection {
  
  private boolean update = true;
  
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
    super.notifyListeners(new DatasetChangeEvent(this, this));
  }
}
