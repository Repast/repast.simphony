/**
 * 
 */
package repast.simphony.chart2.engine;

import java.awt.Color;

import repast.simphony.chart2.OutOfRangeHandling;

/**
 * ChartDescriptor for histogram type charts.
 * 
 * @author Nick Collier
 */
public class HistogramChartDescriptor extends ChartDescriptor {

  public enum HistogramType {
    STATIC() {
      public String toString() {
        return "Static";
      }
    },
    DYNAMIC() {
      public String toString() {
        return "Dynamic";
      }
    }
  }

  private HistogramType histType = HistogramType.DYNAMIC;
  private int binCount = 10;
  private String sourceId;
  // this next to are just for static histograms.
  private OutOfRangeHandling outOfRange = OutOfRangeHandling.IGNORE;
  private double min, max;
  private Color barColor = Color.RED;

  public HistogramChartDescriptor(String name) {
    super(name);
    type = ChartType.HISTOGRAM;
  }

  /**
   * @return the barColor
   */
  public Color getBarColor() {
    return barColor;
  }

  /**
   * @param barColor
   *          the barColor to set
   */
  public void setBarColor(Color barColor) {
    this.barColor = barColor;
  }

  /**
   * @return the histType
   */
  public HistogramType getHistType() {
    return histType;
  }

  /**
   * @param histType
   *          the histType to set
   */
  public void setHistType(HistogramType histType) {
    this.histType = histType;
  }

  /**
   * @return the binCount
   */
  public int getBinCount() {
    return binCount;
  }

  /**
   * @param binCount
   *          the binCount to set
   */
  public void setBinCount(int binCount) {
    this.binCount = binCount;
  }

  /**
   * @return the sourceId
   */
  public String getSourceId() {
    return sourceId;
  }

  /**
   * @param sourceId
   *          the sourceId to set
   */
  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  /**
   * @return the outOfRange
   */
  public OutOfRangeHandling getOutOfRangeHandling() {
    return outOfRange;
  }

  /**
   * @param outOfRange
   *          the outOfRange to set
   */
  public void setOutOfRangeHandling(OutOfRangeHandling outOfRange) {
    this.outOfRange = outOfRange;
  }

  /**
   * @return the min
   */
  public double getMin() {
    return min;
  }

  /**
   * @param min
   *          the min to set
   */
  public void setMin(double min) {
    this.min = min;
  }

  /**
   * @return the max
   */
  public double getMax() {
    return max;
  }

  /**
   * @param max
   *          the max to set
   */
  public void setMax(double max) {
    this.max = max;
  }
}
