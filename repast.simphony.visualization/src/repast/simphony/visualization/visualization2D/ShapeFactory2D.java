/*CopyrightHere*/
package repast.simphony.visualization.visualization2D;

import java.awt.Paint;

import org.piccolo2d.extras.nodes.PLine;
import org.piccolo2d.nodes.PPath;

/**
 *  @deprecated replaced by ogl 2D
 */
public class ShapeFactory2D {

	public static PLine createAxes(double width, double height, double maxLength, Paint paint) {
		PLine axes = new PLine();
		axes.setStrokePaint(paint);
		
		// x axis
		axes.addPoint(0, 0, 0);
		axes.addPoint(1, width, 0);
		// y axis
		axes.addPoint(2, 0, 0);
		axes.addPoint(3, 0, height);
		
		return axes;
	}

	public static PPath createBoundingBox(float width, float height, Paint paint) {
		PPath path = PPath.createRectangle(0, 0, width, height);
		path.setStrokePaint(paint);
		path.setPaint(null);
		return path;
	}
}
