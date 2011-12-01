package repast.simphony.chart2;

public class AbstractXYChartCreator {

  protected String xAxisLabel;
  protected String yAxisLabel;
  protected String chartLabel;

  public AbstractXYChartCreator() {
    super();
  }

  /**
   * @param xAxisLabel
   *          the xAxisLabel to set
   */
  public void setXAxisLabel(String xAxisLabel) {
    this.xAxisLabel = xAxisLabel;
  }

  /**
   * @param yAxisLabel
   *          the yAxisLabel to set
   */
  public void setYAxisLabel(String yAxisLabel) {
    this.yAxisLabel = yAxisLabel;
  }

  /**
   * @param chartLabel
   *          the chartLabel to set
   */
  public void setChartLabel(String chartLabel) {
    this.chartLabel = chartLabel;
  }

  /**
   * @return the xAxisLabel
   */
  public String getXAxisLabel() {
    return xAxisLabel;
  }

  /**
   * @return the yAxisLabel
   */
  public String getYAxisLabel() {
    return yAxisLabel;
  }

  /**
   * @return the chartLabel
   */
  public String getChartLabel() {
    return chartLabel;
  }

}