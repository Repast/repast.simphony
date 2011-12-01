/**
 * 
 */
package repast.simphony.chart2;

import java.awt.Color;

import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.xy.XYSeriesCollection;

import repast.simphony.chart2.engine.TimeSeriesChartDescriptor;

/**
 * Sets the series' colors as they are added.
 * 
 * @author Nick Collier
 */
public class ColorSetter implements DatasetChangeListener {
  
  private XYLineAndShapeRenderer renderer;
  private TimeSeriesChartDescriptor descriptor;
  private int expectedCount, currentCount;
  
  public ColorSetter(XYLineAndShapeRenderer renderer, TimeSeriesChartDescriptor descriptor) {
    this.renderer = renderer;
    this.descriptor = descriptor;
    this.expectedCount = descriptor.getSeriesIds().size();
  }

  @Override
  public void datasetChanged(DatasetChangeEvent evt) {
    XYSeriesCollection data = (XYSeriesCollection) evt.getDataset();
    int count = data.getSeriesCount();
    if (count > currentCount) {
      currentCount = count;
      String id = data.getSeriesKey(count - 1).toString();
      Color color = descriptor.getSeriesColor(id);
      if (color != null) {
        renderer.setSeriesPaint(count - 1, color);
      }
    }
    
    if (currentCount == expectedCount) data.removeChangeListener(this);
  }
}
