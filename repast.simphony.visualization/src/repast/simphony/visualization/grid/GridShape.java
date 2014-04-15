package repast.simphony.visualization.grid;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import org.piccolo2d.PNode;
import org.piccolo2d.util.PPaintContext;

/**
 * Grid shaped PNode that can convert a point to row, column coordinates.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class GridShape extends PNode {

	private float cellSize;
	private Color color;
	private int cols, rows;

	public GridShape(float cellSize, Color color, int cols, int rows) {
		this.cellSize = cellSize;
		this.color = color;
		this.cols = cols;
		this.rows = rows;
		setBounds(0, 0, cols * cellSize, rows * cellSize);
	}

	@Override
	protected void paint(PPaintContext pPaintContext) {
		Graphics2D g2 = pPaintContext.getGraphics();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    Color c = g2.getColor();
		Stroke stroke = g2.getStroke();

		g2.setColor(color);
		g2.setStroke(new BasicStroke(1));
		GeneralPath path = new GeneralPath();

		float y = cellSize * rows;
		for (int i = 0; i <= cols; i++) {
			float x = i * cellSize;
			path.reset();
			path.moveTo(x, 0);
			path.lineTo(x, y);
			g2.draw(path);
		}

		// horiz lines
		float x = cellSize * cols;
		for (int i = 0; i <= rows; i++) {
			y = i * cellSize;
			path.reset();
			path.moveTo(0, y);
			path.lineTo(x, y);
			g2.draw(path);
		}

		g2.setColor(c);
		g2.setStroke(stroke);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  }

  /**
   * Gets the x,y location that corresponds to the specified
   * mouse pixel point.
   *
   * @param point the mouse pixel point
   *
   * @return the x,y location that corresponds to the specified
   * mouse pixel point.
   */
  public Point getXY(Point2D point) {
    double x1 = point.getX() + cellSize / 2;
    int x = (int)Math.floor(x1 / cellSize);
    double y1 = point.getY() + cellSize / 2;
    int y = (int)Math.floor(y1 / cellSize);
    if (x < 0 || x > cols - 1) return null;
    if (y < 0 || y > rows - 1) return null;
    return new Point(x, y);
  }
}
