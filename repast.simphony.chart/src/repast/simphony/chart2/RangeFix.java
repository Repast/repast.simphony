/**
 * 
 */
package repast.simphony.chart2;

import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Sets the initial range from -2 to some specified amount and then when that
 * max has been exceeded reverts back to auto range.
 * 
 * @author Nick Collier
 */
public class RangeFix implements DatasetChangeListener {

  private int max;;
  private XYPlot plot;

  public RangeFix(XYPlot plot, int max) {
    this.plot = plot;
    this.max = max;
    plot.getDomainAxis().setRange(new Range(-2, max));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.jfree.data.general.DatasetChangeListener#datasetChanged(org.jfree.data
   * .general.DatasetChangeEvent)
   */
  @Override
  public void datasetChanged(DatasetChangeEvent evt) {
    XYSeriesCollection data = (XYSeriesCollection) evt.getDataset();
    if (data.getSeriesCount() > 0) {
      XYSeries series = data.getSeries(0);
      if (!series.isEmpty()) {
        Number val = data.getSeries(0).getX(data.getSeries(0).getItemCount() - 1);
        if (val.doubleValue() >= max) {
          plot.getDomainAxis().setAutoRange(true);
          data.removeChangeListener(this);
        }
      }
    }
  }
}
