package repast.simphony.chart2;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTick;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

/**
 * This class axis is setup to display the domain of a histogram. This only will
 * work correctly if the histogram has a single series in it, since only the
 * first series is considered in determining the ticks to show.
 * <p/>
 * 
 * One tick will be shown for each edge of the histogram's bins. This means a
 * tick will be shown at the beginning of each bin, and one at the end of each
 * bin.
 * <p/>
 * 
 * This class is based off of the {@link org.jfree.chart.axis.NumberAxis} class
 * from JFreeChart.
 * 
 * @author Jerry Vos
 */
public class SingleHistogramDomainAxis extends NumberAxis {
  private static final long serialVersionUID = 1L;

  private AbstractHistogramDataset dataSet;

  private NumberFormat defaultFormatter;

  public SingleHistogramDomainAxis() {
    super();
    init();
  }

  public SingleHistogramDomainAxis(String label) {
    super(label);
    init();
  }

  private void init() {
    defaultFormatter = new DecimalFormat();
    defaultFormatter.setMinimumFractionDigits(0);
    defaultFormatter.setMaximumFractionDigits(5);
    defaultFormatter.setMinimumIntegerDigits(0);
    defaultFormatter.setMaximumIntegerDigits(Integer.MAX_VALUE);
  }

  /**
   * Retrieves the data set that will be used for determining bin edges.
   * 
   * @return the data set that the bins will be grabbed from
   */
  public AbstractHistogramDataset getDataSet() {
    return dataSet;
  }

  /**
   * Sets the data set that will be used for determining bin edges.
   * 
   * @param dataSet
   *          the data set that the bins will be grabbed from
   */
  public void setDataSet(AbstractHistogramDataset dataSet) {
    this.dataSet = dataSet;
  }

  @Override
  protected List<?> refreshTicksVertical(Graphics2D g2, Rectangle2D dataArea, RectangleEdge edge) {
    ArrayList<NumberTick> ticks = new ArrayList<NumberTick>();

    if (dataSet.getSeriesCount() == 0) {
      return ticks;
    }

    NumberFormat formatter = getNumberFormatOverride();

    for (int i = 0; i < dataSet.getItemCount(0); i++) {
      {
        double value = dataSet.getStartXValue(0, i);

        String tickLabel;
        if (formatter != null) {
          tickLabel = formatter.format(value);
        } else {
          tickLabel = defaultFormatter.format(value);
        }

        TextAnchor anchor = null;
        TextAnchor rotationAnchor = null;
        double angle = 0.0;
        if (isVerticalTickLabels()) {
          if (edge == RectangleEdge.LEFT) {
            anchor = TextAnchor.BOTTOM_CENTER;
            rotationAnchor = TextAnchor.BOTTOM_CENTER;
            angle = -Math.PI / 2.0;
          } else {
            anchor = TextAnchor.BOTTOM_CENTER;
            rotationAnchor = TextAnchor.BOTTOM_CENTER;
            angle = Math.PI / 2.0;
          }
        } else {
          if (edge == RectangleEdge.LEFT) {
            anchor = TextAnchor.CENTER_RIGHT;
            rotationAnchor = TextAnchor.CENTER_RIGHT;
          } else {
            anchor = TextAnchor.CENTER_LEFT;
            rotationAnchor = TextAnchor.CENTER_LEFT;
          }
        }

        NumberTick tick = new NumberTick(new Double(value), tickLabel, anchor, rotationAnchor,
            angle);
        ticks.add(tick);
      }
      {
        double value = dataSet.getEndXValue(0, i);

        String tickLabel;
        if (formatter != null) {
          tickLabel = formatter.format(value);
        } else {
          tickLabel = defaultFormatter.format(value);
        }

        TextAnchor anchor = null;
        TextAnchor rotationAnchor = null;
        double angle = 0.0;
        if (isVerticalTickLabels()) {
          if (edge == RectangleEdge.LEFT) {
            anchor = TextAnchor.BOTTOM_CENTER;
            rotationAnchor = TextAnchor.BOTTOM_CENTER;
            angle = -Math.PI / 2.0;
          } else {
            anchor = TextAnchor.BOTTOM_CENTER;
            rotationAnchor = TextAnchor.BOTTOM_CENTER;
            angle = Math.PI / 2.0;
          }
        } else {
          if (edge == RectangleEdge.LEFT) {
            anchor = TextAnchor.CENTER_RIGHT;
            rotationAnchor = TextAnchor.CENTER_RIGHT;
          } else {
            anchor = TextAnchor.CENTER_LEFT;
            rotationAnchor = TextAnchor.CENTER_LEFT;
          }
        }

        NumberTick tick = new NumberTick(new Double(value), tickLabel, anchor, rotationAnchor,
            angle);
        ticks.add(tick);
      }
    }

    return ticks;
  }

  @Override
  protected List<?> refreshTicksHorizontal(Graphics2D g2, Rectangle2D dataArea, RectangleEdge edge) {
    ArrayList<NumberTick> ticks = new ArrayList<NumberTick>();

    if (dataSet.getSeriesCount() == 0) {
      return ticks;
    }

    NumberFormat formatter = getNumberFormatOverride();

    for (int i = 0; i < dataSet.getItemCount(0); i++) {
      {
        double value = dataSet.getStartXValue(0, i);

        String tickLabel;
        if (formatter != null) {
          tickLabel = formatter.format(value);
        } else {
          tickLabel = defaultFormatter.format(value);
        }

        TextAnchor anchor = null;
        TextAnchor rotationAnchor = null;
        double angle = 0.0;
        if (isVerticalTickLabels()) {
          anchor = TextAnchor.CENTER_RIGHT;
          rotationAnchor = TextAnchor.CENTER_RIGHT;
          if (edge == RectangleEdge.TOP) {
            angle = Math.PI / 2.0;
          } else {
            angle = -Math.PI / 2.0;
          }
        } else {
          if (edge == RectangleEdge.TOP) {
            anchor = TextAnchor.BOTTOM_CENTER;
            rotationAnchor = TextAnchor.BOTTOM_CENTER;
          } else {
            anchor = TextAnchor.TOP_CENTER;
            rotationAnchor = TextAnchor.TOP_CENTER;
          }
        }

        NumberTick tick = new NumberTick(new Double(value), tickLabel, anchor, rotationAnchor,
            angle);
        ticks.add(tick);
      }
      {
        double value = dataSet.getEndXValue(0, i);

        String tickLabel;
        if (formatter != null) {
          tickLabel = formatter.format(value);
        } else {
          tickLabel = defaultFormatter.format(value);
        }
        TextAnchor anchor = null;
        TextAnchor rotationAnchor = null;
        double angle = 0.0;
        if (isVerticalTickLabels()) {
          anchor = TextAnchor.CENTER_RIGHT;
          rotationAnchor = TextAnchor.CENTER_RIGHT;
          if (edge == RectangleEdge.TOP) {
            angle = Math.PI / 2.0;
          } else {
            angle = -Math.PI / 2.0;
          }
        } else {
          if (edge == RectangleEdge.TOP) {
            anchor = TextAnchor.BOTTOM_CENTER;
            rotationAnchor = TextAnchor.BOTTOM_CENTER;
          } else {
            anchor = TextAnchor.TOP_CENTER;
            rotationAnchor = TextAnchor.TOP_CENTER;
          }
        }

        NumberTick tick = new NumberTick(new Double(value), tickLabel, anchor, rotationAnchor,
            angle);
        ticks.add(tick);
      }
    }

    return ticks;
  }
}
