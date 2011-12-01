/*
 * NLLinePrimitive.java
 *
 * Created on September 26, 2007, 6:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package repast.simphony.relogo.ide.image;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D;

/**
 *
 * @author CBURKE
 */
public class NLLinePrimitive extends NLImagePrimitive {

    Point2D.Double from;
    Point2D.Double to;
    int rgb24;

    /** Creates a new instance of NLLinePrimitive */
    public NLLinePrimitive(int c, boolean f, boolean change, Point st, Point ds) {
        super(c, f, change);
        rgb24 = c;
        double x = st.x;
        double y = st.y;
        from = new Point2D.Double(x / 300.0, y / 300.0);
        x = ds.x;
        y = ds.y;
        to = new Point2D.Double(x / 300.0, y / 300.0);
    }

    /**
     * Render the primitive into the specified graphics context at the
     * specified scale. The primitive's notional size (300x300) should be
     * mapped to the specified dimensions. There is no guarantee that the
     * specified bounds cover the rendering surface, although that is the
     * assumption in Netlogo.
     *
     * @param gc Graphics context in which to render the primitive
     * @param bb Bounding box to which the primitive should be scaled.
     */
    public void render(Graphics gc, Rectangle bb, Color clr) {
        Color previousColor = gc.getColor();
        if (changingColor) {
            gc.setColor(clr);
        } else {
            gc.setColor(color);
        }
        int x1 = (int) (from.x * bb.getWidth());
        int y1 = (int) (from.y * bb.getHeight());
        int x2 = (int) (to.x * bb.getWidth());
        int y2 = (int) (to.y * bb.getHeight());
        gc.drawLine(x1, y1, x2, y2);
        gc.setColor(previousColor);
    }

    /**
     * Generate a shape representation of this image in the specified rectangle.
     */
    public Shape renderingShape(Rectangle bb) {
        int x1 = (int) (from.x * bb.getWidth());
        int y1 = (int) (from.y * bb.getHeight());
        int x2 = (int) (to.x * bb.getWidth());
        int y2 = (int) (to.y * bb.getHeight());
        return new Line2D.Double(x1, y1, x2, y2);
    }
    
    /**
     * Generate a shape representation, appropriate for ReLogo, of this image in the specified rectangle.
     */
    public Shape reLogoShape(Rectangle bb) {
    	// conversion:
		// xR = 300 - yN
		// yR = xN
//        int x1 = (int) (from.x * bb.getWidth());
//        int y1 = (int) (from.y * bb.getHeight());
//        int x2 = (int) (to.x * bb.getWidth());
//        int y2 = (int) (to.y * bb.getHeight());
    	int x1 = (int) (bb.getHeight() - from.y * bb.getHeight());
    	int y1 = (int) (bb.getWidth() - from.x * bb.getWidth());
    	int x2 = (int) (bb.getHeight() - to.y * bb.getHeight());
    	int y2 = (int) (bb.getWidth() - to.x * bb.getWidth());
        return new Line2D.Double(x1, y1, x2, y2);
    }

    /**
     * Generate code to render this image in the specified rectangle.
     * Assumes the presence of a color object stack, given RGB24.
     */
    public String renderingCode(Rectangle bb) {
        StringBuffer sb = new StringBuffer();
        sb.append("    pushColor(gc, ");
        sb.append(rgb24);
        sb.append(");\n");
        int x1 = (int) (from.x * bb.getWidth());
        int y1 = (int) (from.y * bb.getHeight());
        int x2 = (int) (to.x * bb.getWidth());
        int y2 = (int) (to.y * bb.getHeight());
        sb.append("    gc.drawLine(");
        sb.append(x1);
        sb.append(",");
        sb.append(y1);
        sb.append(",");
        sb.append(x2);
        sb.append(",");
        sb.append(y2);
        sb.append(");\n");
        sb.append("    popColor(gc);\n");
        return sb.toString();
    }
}
