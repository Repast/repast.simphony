/**
 * 
 */
package repast.simphony.chart2;

import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;

import repast.simphony.chart2.engine.TimeSeriesChartDescriptor;

/**
 * ChartCreator for creating a line chart.
 * 
 * @author Nick Collier
 */
public class LineChartCreator extends AbstractChartCreator implements ChartCreator<TimeSeriesChartDescriptor> {

  private static class LabelGenerator extends StandardXYSeriesLabelGenerator {

    private Map<String, String> labelMap = new HashMap<String, String>();

    private static final long serialVersionUID = 947520053507087599L;

    @Override
    public String generateLabel(XYDataset dataset, int series) {
      String key = dataset.getSeriesKey(series).toString();
      String label = labelMap.get(key);
      return label == null ? key : label;
    }
  }

  private XYSeriesCollection xydata;
  private ChartPanel panel;
  private LabelGenerator labelGenerator = new LabelGenerator();

  public LineChartCreator(XYSeriesCollection xydata) {
    this.xydata = xydata;
  }

  /*
   * (non-Javadoc)
   * 
   * @see projz.chart.ChartCreator#reset()
   */
  @Override
  public void reset() {
    JFreeChart chart = panel.getChart();
    xydata.removeChangeListener(chart.getPlot());
    xydata.removeAllSeries();
    panel.setChart(null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see projz.chart.ChartCreator#createChartComponents()
   */
  @Override
  public JComponent createChartComponent(TimeSeriesChartDescriptor descriptor) {

    JFreeChart chart = ChartFactory.createXYLineChart(descriptor.getChartTitle(), 
        descriptor.getXAxisLabel(), descriptor.getYAxisLabel(), xydata,
        PlotOrientation.VERTICAL, descriptor.doShowLegend(), true, false);

    XYPlot plot = (XYPlot) chart.getPlot();
    updatePlot(plot, descriptor);
    
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setBaseShapesVisible(true);
    renderer.setBaseShapesFilled(true);
    if (descriptor.doShowLegend()) {
      renderer.setLegendItemLabelGenerator(labelGenerator);
    }
    
    for (String id : descriptor.getSeriesIds()) {
      labelGenerator.labelMap.put(id, descriptor.getSeriesLabel(id));
    }

    plot.setRenderer(renderer);
    
    ColorSetter setter = new ColorSetter(renderer, descriptor);
    xydata.addChangeListener(setter);
    
    if (descriptor.getPlotRangeLength() > 0) {
      plot.getDomainAxis().setFixedAutoRange(descriptor.getPlotRangeLength());
    } else {
      RangeFix rangeFix = new RangeFix(plot, 50);
      xydata.addChangeListener(rangeFix);
    }

    panel = new ChartPanel(chart, true);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    return panel;
  }
}
