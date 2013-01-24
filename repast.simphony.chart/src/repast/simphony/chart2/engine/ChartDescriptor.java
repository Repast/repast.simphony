/**
 * 
 */
package repast.simphony.chart2.engine;

import java.awt.Color;

import repast.simphony.engine.schedule.Descriptor;

/**
 * @author Nick Collier
 */
public class ChartDescriptor implements Descriptor {
  
  public enum ChartType { TIME_SERIES, HISTOGRAM}; 
  
  protected String name, dataSet;
  protected String xAxisLabel, yAxisLabel, chartTitle;
  protected ChartType type = ChartType.TIME_SERIES;
  protected Color background, gridLineColor;
  protected boolean showGrid = true;
  protected boolean showLegend = true;
  
  // used by xstream serialization
  protected ChartDescriptor() {}

  public ChartDescriptor(String name) {
    this.name = name;
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
   * @param background the background to set
   */
  public void setBackground(Color background) {
    this.background = background;
  }


  /**
   * @return the gridLineColor
   */
  public Color getGridLineColor() {
    return gridLineColor;
  }


  /**
   * @param gridLineColor the gridLineColor to set
   */
  public void setGridLineColor(Color gridLineColor) {
    this.gridLineColor = gridLineColor;
  }


  /**
   * @return the showGrid
   */
  public boolean isShowGrid() {
    return showGrid;
  }


  /**
   * @param showGrid the showGrid to set
   */
  public void setShowGrid(boolean showGrid) {
    this.showGrid = showGrid;
  }


  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.engine.schedule.Descriptor#getName()
   */
  @Override
  public String getName() {
    return name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.engine.schedule.Descriptor#setName(java.lang.String)
   */
  @Override
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * @return the dataSet
   */
  public String getDataSet() {
    return dataSet;
  }

  /**
   * @param dataSet the dataSet to set
   */
  public void setDataSet(String dataSet) {
    this.dataSet = dataSet;
  }

  /**
   * @return the xAxisLabel
   */
  public String getXAxisLabel() {
    return xAxisLabel;
  }

  /**
   * @param xAxisLabel the xAxisLabel to set
   */
  public void setXAxisLabel(String xAxisLabel) {
    this.xAxisLabel = xAxisLabel;
  }

  /**
   * @return the yAxisLabel
   */
  public String getYAxisLabel() {
    return yAxisLabel;
  }

  /**
   * @param yAxisLabel the yAxisLabel to set
   */
  public void setYAxisLabel(String yAxisLabel) {
    this.yAxisLabel = yAxisLabel;
  }

  /**
   * @return the chartTitle
   */
  public String getChartTitle() {
    return chartTitle;
  }

  /**
   * @param chartTitle the chartTitle to set
   */
  public void setChartTitle(String chartTitle) {
    this.chartTitle = chartTitle;
  }
  
  public boolean doShowLegend() {
    return showLegend;
  }
  
  public void setShowLegend(boolean val) {
    this.showLegend = val;
  }
}
