/*
 * NLPolygonPrimitive.java
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
import java.awt.Polygon;
import java.awt.Rectangle;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.*;

/**
 *
 * @author CBURKE
 */
public class NLPolygonPrimitive extends NLImagePrimitive {
    List<Point2D.Double> points;
    int[] x;
    int[] y;
    int rgb24;
    
    /** Creates a new instance of NLPolygonPrimitive */
    public NLPolygonPrimitive(int c, boolean f, boolean change, List<Point> lp) {
        super(c, f, change);
        rgb24 = c;
        LinkedList<Point2D.Double> scaledPoints = new LinkedList<Point2D.Double>();
        x = new int[lp.size()];
        y = new int[lp.size()];
        for (Point p : lp) {
            double x = p.x;
            double y = p.y;
            Point2D.Double pt = new Point2D.Double(x/300.0, y/300.0);
            scaledPoints.add(pt);
        }
        points = scaledPoints;
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
        int i = 0;
        for (Point2D pt : points) {
            double w = pt.getX()*(double)bb.getWidth();
            double h = pt.getY()*(double)bb.getHeight();
            x[i] = (int)(w);
            y[i] = (int)(h);
            i++;
        }
        if (fill) {
            gc.fillPolygon(x, y, x.length);
        } else {
            gc.drawPolygon(x, y, x.length);
        }
        gc.setColor(previousColor);
    }
    
    /**
     * Generate a shape representation of this image in the specified rectangle.
     * Depending on the value of the fill attribute, this is either a Path2D
     * or a Polygon/Area. Note that Java may attempt to fill an arbitrary
     * Path2D anyway, if so instructed; the results in that case are likely
     * to be unsatisfactory.
     */
    public Shape renderingShape(Rectangle bb) {
        int i = 0;
        for (Point2D pt : points) {
            double w = pt.getX()*(double)bb.getWidth();
            double h = pt.getY()*(double)bb.getHeight();
            x[i] = (int)(w);
            y[i] = (int)(h);
            i++;
        }
        if (fill) {
            return new Polygon(x, y, x.length);
        } else {
            Path2D.Double path = new Path2D.Double(Path2D.WIND_NON_ZERO, i);
            path.moveTo(x[0], y[0]);
            for (int j=1; j<i; j++) {
                path.lineTo(x[j], y[j]);
            }
            path.lineTo(x[0], y[0]);
            return path;
        }
    }
    
    /**
     * Generate a shape representation of this image, appropriate to ReLogo,
     * in the specified rectangle.
     * Depending on the value of the fill attribute, this is either a Path2D
     * or a Polygon/Area. Note that Java may attempt to fill an arbitrary
     * Path2D anyway, if so instructed; the results in that case are likely
     * to be unsatisfactory.
     */
    public Shape reLogoShape(Rectangle bb) {
    	// conversion:
		// xR = 300 - yN
		// yR = 300 - xN
        int i = 0;
        for (Point2D pt : points) {
//            double w = pt.getX()*(double)bb.getWidth();
//            double h = pt.getY()*(double)bb.getHeight();
        	double w = (double)bb.getHeight() - pt.getY()*(double)bb.getHeight();
        	double h = (double)bb.getWidth() - pt.getX()*(double)bb.getWidth();
            x[i] = (int)(w);
            y[i] = (int)(h);
            i++;
        }
        if (fill) {
            return new Polygon(x, y, x.length);
        } else {
            Path2D.Double path = new Path2D.Double(Path2D.WIND_NON_ZERO, i);
            path.moveTo(x[0], y[0]);
            for (int j=1; j<i; j++) {
                path.lineTo(x[j], y[j]);
            }
            path.lineTo(x[0], y[0]);
            return path;
        }
    }
    
    /**
     * Generate code to render this image in the specified rectangle.
     * Assumes the presence of a color object stack, given RGB24.
     */
    public String renderingCode(Rectangle bb) {
        StringBuffer sb = new StringBuffer();
        sb.append("    pushColor(gc, "); sb.append(rgb24); sb.append(");\n");
        sb.append("    {\n");
        int i = 0;
        for (Point2D pt : points) {
            double w = pt.getX()*(double)bb.getWidth();
            double h = pt.getY()*(double)bb.getHeight();
            x[i] = (int)(w);
            y[i] = (int)(h);
            i++;
        }
        sb.append("        int[] x = {");
        for (int j=0; j<i; j++) {
            if (j > 0) sb.append(", ");
            sb.append(x[j]); 
        }
        sb.append("};\n");
        sb.append("        int[] y = {");
        for (int j=0; j<i; j++) {
            if (j > 0) sb.append(", ");
            sb.append(y[j]); 
        }
        sb.append("};\n");
        if (fill) {
            sb.append("    gc.fillPolygon(x, y, x.length);\n");
        } else {
            sb.append("    gc.drawPolygon(x, y, x.length);\n");
        }
        sb.append("    }\n");
        sb.append("    popColor(gc);\n");
        return sb.toString();
    }
}
