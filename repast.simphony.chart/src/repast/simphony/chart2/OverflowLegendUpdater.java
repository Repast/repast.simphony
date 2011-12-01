/**
 * 
 */
package repast.simphony.chart2;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.PlotChangeListener;
import org.jfree.chart.title.TextTitle;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;

/**
 * Updates the a chart's overflow legend.
 * 
 * @author Nick Collier
 */
public class OverflowLegendUpdater implements PlotChangeListener {

  private TextTitle title;
  private StaticHistogramDataset data;

  /**
   * @param chart
   * @param title
   * @param data
   */
  public OverflowLegendUpdater(JFreeChart chart, StaticHistogramDataset data) {
    this.data = data;

    title = new TextTitle("Overflow: N/A, Underflow: N/A");

    title.setHorizontalAlignment(HorizontalAlignment.RIGHT);
    title.setPosition(RectangleEdge.BOTTOM);
    title.setPadding(5, 5, 5, 12);
    chart.addSubtitle(title);

  }

  /**
   * Updates the title to the latest overflow.
   */
  public void update() {
    int underflow = data.getUnderflow();
    int overflow = data.getOverflow();

    title.setText("Underflow: " + underflow + ", Overflow: " + overflow );
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.jfree.chart.event.PlotChangeListener#plotChanged(org.jfree.chart.event
   * .PlotChangeEvent)
   */
  @Override
  public void plotChanged(PlotChangeEvent arg0) {
    update();
  }
}
