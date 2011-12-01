/**
 * 
 */
package repast.simphony.chart2;

import javax.swing.JComponent;

import repast.simphony.engine.schedule.Descriptor;

/**
 * Interface for classes that can create charts.
 * 
 * @author Nick Collier
 */
public interface ChartCreator<T extends Descriptor> {

  /**
   * Creates and returns the chart components.
   * 
   * @return the created chart components.
   */
  JComponent createChartComponent(T descriptor);

  /**
   * Resets the creator prior to creating a chart.
   */
  void reset();

}
