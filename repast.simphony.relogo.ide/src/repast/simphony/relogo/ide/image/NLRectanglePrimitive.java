/*
 * NLRectanglePrimitive.java
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
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author CBURKE
 */
public class NLRectanglePrimitive extends NLImagePrimitive {
    Point2D.Double upperLeft;
    Point2D.Double dimension;
    int rgb24;
    
    /** Creates a new instance of NLRectanglePrimitive */
    public NLRectanglePrimitive(int c, boolean f, boolean change, Point ul, Point lr) {
        super(c, f, change);
        rgb24 = c;
        if (ul.x > lr.x && ul.y > lr.y) {
            Point tmp = ul;
            ul = lr;
            lr = tmp;
        }
        double x = ul.x;
        double y = ul.y;
        upperLeft = new Point2D.Double(x/300.0, y/300.0);
        x = lr.x - ul.x;
        y = lr.y - ul.y;
        dimension = new Point2D.Double(x/300.0, y/300.0);
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
        int width = (int)(dimension.x*bb.getWidth());
        int height = (int)(dimension.y*bb.getHeight());
        if (fill) {
            gc.fillRect(x1, y1, width, height);
        } else {
            gc.drawRect(x1, y1, width, height);
        }
        gc.setColor(previousColor);
    }
    
    /**
     * Generate a shape representation of this image in the specified rectangle.
     * Depending on the value of the fill attribute, this is either a Path2D
     * or a Rectangle2D.
     */
    public Shape renderingShape(Rectangle bb) {
        int x1 = (int)(upperLeft.x*bb.getWidth());
        int y1 = (int)(upperLeft.y*bb.getHeight());
        int width = (int)(dimension.x*bb.getWidth());
        int height = (int)(dimension.y*bb.getHeight());
        if (fill) {
            return new Rectangle2D.Double(x1, y1, width, height);
        } else {
            Path2D.Double path = new Path2D.Double();
            path.moveTo(x1, y1);
            path.lineTo(x1+width, y1);
            path.lineTo(x1+width, y1+height);
            path.lineTo(x1, y1+height);
            path.lineTo(x1, y1);
            return path;
        }
    }
    
    /**
     * Generate a shape representation of this image, appropriate to ReLogo, 
     * in the specified rectangle.
     * Depending on the value of the fill attribute, this is either a Path2D
     * or a Rectangle2D.
     */
    public Shape reLogoShape(Rectangle bb) {
    	// conversion:
		// xR = 300 - yN
		// yR = xN
//        int x1 = (int)(upperLeft.x*bb.getWidth());
//        int y1 = (int)(upperLeft.y*bb.getHeight());
//        int width = (int)(dimension.x*bb.getWidth());
//        int height = (int)(dimension.y*bb.getHeight());
    	int x1 = (int)(bb.getHeight() - upperLeft.y*bb.getHeight());
    	int y1 = (int)(bb.getWidth() - upperLeft.x*bb.getWidth());
    	int width = (int)(dimension.y*bb.getHeight());
    	int height = (int)(dimension.x*bb.getWidth());
    	x1 = x1 - width;
    	y1 = y1 - height;
        if (fill) {
            return new Rectangle2D.Double(x1, y1, width, height);
        } else {
            Path2D.Double path = new Path2D.Double();
            path.moveTo(x1, y1);
            path.lineTo(x1+width, y1);
            path.lineTo(x1+width, y1+height);
            path.lineTo(x1, y1+height);
            path.lineTo(x1, y1);
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
        int x1 = (int)(upperLeft.x*bb.getWidth());
        int y1 = (int)(upperLeft.y*bb.getHeight());
        int width = (int)(dimension.x*bb.getWidth());
        int height = (int)(dimension.y*bb.getHeight());
        if (fill) {
            sb.append("    gc.fillRect("); sb.append(x1); sb.append(","); sb.append(y1); sb.append(","); sb.append(width); sb.append(","); sb.append(height); sb.append(");\n");
        } else {
            sb.append("    gc.drawRect("); sb.append(x1); sb.append(","); sb.append(y1); sb.append(","); sb.append(width); sb.append(","); sb.append(height); sb.append(");\n");
        }
        sb.append("    popColor(gc);\n");
        return sb.toString();
    }
}
