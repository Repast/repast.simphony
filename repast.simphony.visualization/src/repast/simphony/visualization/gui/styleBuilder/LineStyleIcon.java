package repast.simphony.visualization.gui.styleBuilder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;

import repast.simphony.visualization.editedStyle.LineStyle;

/**
 * Icon for line style preview in style editor dialogs.
 * 
 * @author Eric Tatara
 *
 */
class LineStyleIcon implements Icon {

  protected float[] dash;
  protected float width;
  LineStyle lineStyle;
  private Color color;

  public LineStyleIcon(LineStyle lineStyle, float width) {
    this(lineStyle, width, Color.BLACK);
  }

  public LineStyleIcon(LineStyle lineStyle, float width, Color color) {
    this.color = color;

    if (lineStyle == LineStyle.DASH)
      dash = new float[] { 10f, 10f };
    else if (lineStyle == LineStyle.DASH_DOT)
      dash = new float[] { 10f, 4f, 2f, 4f };
    else if (lineStyle == LineStyle.DASH_DASH_DOT)
      dash = new float[] { 10f, 4f, 10f, 4f, 2f, 4f };
    else
      dash = null;

    this.width = width;
    this.lineStyle = lineStyle;
  }

  public int getIconHeight() {
    return 20;
  }

  public int getIconWidth() {
    return 100;
  }

  public void paintIcon(Component c, Graphics g, int x, int y) {

    g.setColor(c.getBackground());
    g.fillRect(x, y, getIconWidth(), getIconHeight());
    g.setColor(color);

    ((Graphics2D) g).setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT,
        BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));

    g.drawLine(0, getIconHeight() / 2, getIconWidth(), getIconHeight() / 2);
  }

  public LineStyle getLineStyle() {
    return lineStyle;
  }
}