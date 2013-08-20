/*
 * NLCirclePrimitive.java
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
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

import repast.simphony.relogo.image.NLImagePrimitive;

/**
 *
 * @author CBURKE
 */
public class NLCirclePrimitive extends NLImagePrimitive {
    Point2D.Double upperLeft;
    double diameter;
    int rgb24;
    
    /** Creates a new instance of NLCirclePrimitive */
    public NLCirclePrimitive(int c, boolean f, boolean change, Point ul, int d) {
        super(c, f, change);
        rgb24 = c;
        double x = ul.x;
        double y = ul.y;
        upperLeft = new Point2D.Double(x/300.0, y/300.0);
        diameter = (double)d/300.0;
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
        int x1 = (int)(upperLeft.x*bb.getWidth());
        int y1 = (int)(upperLeft.y*bb.getHeight());
        int width = (int)(diameter*bb.getWidth());
        int height = (int)(diameter*bb.getHeight());
        if (fill) {
            gc.fillOval(x1, y1, width, height);
        } else {
            gc.drawOval(x1, y1, width, height);
        }
        gc.setColor(previousColor);
    }
    
    /**
     * Generate a shape representation of this image in the specified rectangle.
     * Depending on the value of the fill attribute, this is either an Arc2D
     * or an Ellipse2D.
     */
    public Shape renderingShape(Rectangle bb) {
        int x1 = (int)(upperLeft.x*bb.getWidth());
        int y1 = (int)(upperLeft.y*bb.getHeight());
        int width = (int)(diameter*bb.getWidth());
        int height = (int)(diameter*bb.getHeight());
        if (fill) {
            return new Ellipse2D.Double(x1, y1, width, height);
        } else {
            return new Arc2D.Double(x1, y1, width, height, 0, 360, Arc2D.OPEN);
        }
    }
    
    /**
     * Generate a shape representation of this image, appropriate for ReLogo, 
     * in the specified rectangle.
     * Depending on the value of the fill attribute, this is either an Arc2D
     * or an Ellipse2D.
     */
    public Shape reLogoShape(Rectangle bb) {
    	// conversion:
		// xR = 300 - yN
		// yR = xN
//        int x1 = (int)(upperLeft.x*bb.getWidth());
//        int y1 = (int)(upperLeft.y*bb.getHeight());
//        int width = (int)(diameter*bb.getWidth());
//        int height = (int)(diameter*bb.getHeight());
    	int x1 = (int)(bb.getHeight()-upperLeft.y*bb.getHeight());
    	int y1 = (int)(bb.getWidth()-upperLeft.x*bb.getWidth());
    	int width = (int)(diameter*bb.getHeight());
    	int height = (int)(diameter*bb.getWidth());
    	x1 = x1 - width;
    	y1 = y1 - height;
        if (fill) {
            return new Ellipse2D.Double(x1, y1, width, height);
        } else {
            return new Arc2D.Double(x1, y1, width, height, 0, 360, Arc2D.OPEN);
        }
    }
    
    /**
     * Generate code to render this image in the specified rectangle.
     * Assumes the presence of a color object stack, given RGB24.
     */
    public String renderingCode(Rectangle bb) {
        StringBuffer sb = new StringBuffer();
        sb.append("    pushColor(gc, "); sb.append(rgb24); sb.append(");\n");
        int x1 = (int)(upperLeft.x*bb.getWidth());
        int y1 = (int)(upperLeft.y*bb.getHeight());
        int width = (int)(diameter*bb.getWidth());
        int height = (int)(diameter*bb.getHeight());
        if (fill) {
            sb.append("    gc.fillOval("); sb.append(x1); sb.append(","); sb.append(y1); sb.append(","); sb.append(width); sb.append(","); sb.append(height); sb.append(");\n");
        } else {
            sb.append("    gc.drawOval("); sb.append(x1); sb.append(","); sb.append(y1); sb.append(","); sb.append(width); sb.append(","); sb.append(height); sb.append(");\n");
        }
        sb.append("    popColor(gc);\n");
        return sb.toString();
    }
}
