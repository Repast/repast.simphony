/**
 * 
 */
package repast.simphony.chart2;

import org.jfree.chart.plot.XYPlot;

import repast.simphony.chart2.engine.ChartDescriptor;

/**
 * Sets plot properties given a chart descriptor.
 * 
 * @author Nick Collier
 */
public class AbstractChartCreator {
  
  /**
   * Updates the specified plot using the values in the ChartDescriptor.
   * 
   * @param plot
   * @param descriptor
   */
  void updatePlot(XYPlot plot, ChartDescriptor descriptor) {
    plot.setBackgroundPaint(descriptor.getBackground());
    plot.setDomainGridlinePaint(descriptor.getGridLineColor());
    plot.setDomainGridlinesVisible(descriptor.isShowGrid());
    plot.setRangeGridlinesVisible(descriptor.isShowGrid());
    plot.setRangeGridlinePaint(descriptor.getGridLineColor());
    
  }

}
