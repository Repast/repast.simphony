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
 * Sets the initial range from -2 to some specified amount and then
 * when that max has been exceeded sets a fixed length of that amount.
 * 
 * @author Nick Collier
 */
public class FixedRangeFix implements DatasetChangeListener {

  private int length;
  private XYPlot plot;

  public FixedRangeFix(XYPlot plot, int length) {
    this.plot = plot;
    this.length = length;
    plot.getDomainAxis().setRange(new Range(-2, length));
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
    XYSeries series = data.getSeries(0);
    if (!series.isEmpty()) {
      Number val = data.getSeries(0).getX(data.getSeries(0).getItemCount() - 1);
      if (val.doubleValue() >= length) {
        plot.getDomainAxis().setFixedAutoRange(length);
        data.removeChangeListener(this);
      }
    }
  }
}
