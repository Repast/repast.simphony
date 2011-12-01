/**
 * 
 */
package repast.simphony.chart2;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;

import repast.simphony.chart2.engine.HistogramChartDescriptor;
import repast.simphony.chart2.engine.HistogramChartDescriptor.HistogramType;

/**
 * ChartCreator for creating histogram charts.
 * 
 * @author Nick Collier
 */
public class HistogramChartCreator extends AbstractChartCreator implements ChartCreator<HistogramChartDescriptor> {

  private AbstractHistogramDataset data;
  private ChartPanel panel;
  
  public HistogramChartCreator(AbstractHistogramDataset data) {
    this.data = data;
  }

  /* (non-Javadoc)
   * @see repast.simphony.chart2.ChartCreator#createChartComponent()
   */
  @Override
  public JComponent createChartComponent(HistogramChartDescriptor descriptor) {
    JFreeChart chart = ChartFactory.createHistogram(descriptor.getChartTitle(), 
        descriptor.getXAxisLabel(), descriptor.getYAxisLabel(), data, 
        PlotOrientation.VERTICAL, false, true, false);

    XYPlot plot = (XYPlot) chart.getPlot();
    updatePlot(plot, descriptor);
    
    //SingleHistogramDomainAxis axis = new SingleHistogramDomainAxis(xAxisLabel);
    //axis.setDataSet(data);
    //plot.setDomainAxis(axis);
    
    XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
    renderer.setBarPainter(new StandardXYBarPainter());
    renderer.setDrawBarOutline(true);
    renderer.setSeriesPaint(0, descriptor.getBarColor());
    
    if (descriptor.getHistType() == HistogramType.STATIC && 
        descriptor.getOutOfRangeHandling() == OutOfRangeHandling.DISPLAY) {
      
       OverflowLegendUpdater updater = new OverflowLegendUpdater(chart, (StaticHistogramDataset)data);
       plot.addChangeListener(updater);
    }
 
    panel = new ChartPanel(chart, true);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    return panel;
    
  }

  /* (non-Javadoc)
   * @see repast.simphony.chart2.ChartCreator#reset()
   */
  @Override
  public void reset() {
    JFreeChart chart = panel.getChart();
    data.removeChangeListener(chart.getPlot());
    data.removeAllBins();
    panel.setChart(null);
  }
}
