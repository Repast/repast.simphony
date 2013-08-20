/**
 * 
 */
package repast.simphony.chart2.engine;

import java.awt.Color;

import repast.simphony.scenario.AbstractDescriptor;

/**
 * @author Nick Collier
 */
public class ChartDescriptor extends AbstractDescriptor {

  public enum ChartType {
    TIME_SERIES, HISTOGRAM
  };

  protected String dataSet;
  protected String xAxisLabel, yAxisLabel, chartTitle;
  protected ChartType type = ChartType.TIME_SERIES;
  protected Color background, gridLineColor;
  protected boolean showGrid = true;
  protected boolean showLegend = true;

  // used by xstream serialization
  protected ChartDescriptor() {
    super("");
  }

  public ChartDescriptor(String name) {
    super(name);
    xAxisLabel = yAxisLabel = chartTitle = "";
    background = Color.LIGHT_GRAY;
    gridLineColor = Color.WHITE;
  }

  /**
   * @return the background
   */
  public Color getBackground() {
    return background;
  }

  /**
   * @param background
   *          the background to set
   */
  public void setBackground(Color background) {
    if (!this.background.equals(background)) {
      this.background = background;
      scs.fireScenarioChanged(this, "backgroundColor");
    }
  }

  /**
   * @return the gridLineColor
   */
  public Color getGridLineColor() {
    return gridLineColor;
  }

  /**
   * @param gridLineColor
   *          the gridLineColor to set
   */
  public void setGridLineColor(Color gridLineColor) {
    if (!this.gridLineColor.equals(gridLineColor)) {
      this.gridLineColor = gridLineColor;
      scs.fireScenarioChanged(this, "gridLineColor");
    }
  }

  /**
   * @return the showGrid
   */
  public boolean isShowGrid() {
    return showGrid;
  }

  /**
   * @param showGrid
   *          the showGrid to set
   */
  public void setShowGrid(boolean showGrid) {
    if (this.showGrid != showGrid) {
      this.showGrid = showGrid;
      scs.fireScenarioChanged(this, "showGrid");
    }
  }

  /**
   * @return the dataSet
   */
  public String getDataSet() {
    return dataSet;
  }

  /**
   * @param dataSet
   *          the dataSet to set
   */
  public void setDataSet(String dataSet) {
    if (this.dataSet == null || !this.dataSet.equals(dataSet)) {
      this.dataSet = dataSet;
      scs.fireScenarioChanged(this, "dataSet");
    }
  }

  /**
   * @return the xAxisLabel
   */
  public String getXAxisLabel() {
    return xAxisLabel;
  }

  /**
   * @param xAxisLabel
   *          the xAxisLabel to set
   */
  public void setXAxisLabel(String xAxisLabel) {
    if (!this.xAxisLabel.equals(xAxisLabel)) {
      this.xAxisLabel = xAxisLabel;
      scs.fireScenarioChanged(this, "xAxisLabel");
    }
  }

  /**
   * @return the yAxisLabel
   */
  public String getYAxisLabel() {
    return yAxisLabel;
  }

  /**
   * @param yAxisLabel
   *          the yAxisLabel to set
   */
  public void setYAxisLabel(String yAxisLabel) {
    if (!this.yAxisLabel.equals(yAxisLabel)) {
      this.yAxisLabel = yAxisLabel;
      scs.fireScenarioChanged(this, "yAxisLabel");
    }
  }

  /**
   * @return the chartTitle
   */
  public String getChartTitle() {
    return chartTitle;
  }

  /**
   * @param chartTitle
   *          the chartTitle to set
   */
  public void setChartTitle(String chartTitle) {
    if (!this.chartTitle.equals(chartTitle)) {
      this.chartTitle = chartTitle;
      scs.fireScenarioChanged(this, "chartTitle");
    }
  }

  public boolean doShowLegend() {
    return showLegend;
  }

  public void setShowLegend(boolean val) {
    if (this.showLegend != val) {
      this.showLegend = val;
      scs.fireScenarioChanged(this, "showLegend");
    }
  }
}
