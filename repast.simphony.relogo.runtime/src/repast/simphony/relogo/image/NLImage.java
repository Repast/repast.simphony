/*
 * NLImage.java
 *
 * Created on September 25, 2007, 7:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package repast.simphony.relogo.image;

import java.awt.Color;
import java.util.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 *
 * @author CBURKE
 */
public class NLImage implements Comparable {

    String name;
    boolean rotates;
    int changingColor;
    List<NLImagePrimitive> primitives;

    public List<NLImagePrimitive> getPrimitives() {
		return primitives;
	}

	/** Creates a new instance of NLImage */
    public NLImage(String n, boolean rot, int cc, List<NLImagePrimitive> prims) {
        name = n;
        rotates = rot;
        changingColor = cc;
        primitives = prims;
    }

    /**
     * Getter method for preferred name for this drawing.
     * 
     * @return
     */
    public String getName() {
        return name;
    }
    
    

    /**
	 * @return the rotates
	 */
	public boolean isRotates() {
		return rotates;
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
    public void render(Graphics gc, Rectangle rect, Color color) {
        for (NLImagePrimitive prim : primitives) {
            prim.render(gc, rect, color);
        }
    }

    /**
     * Render the primitive into the specified graphics context at the
     * specified scale. The primitive's notional size (300x300) should be
     * mapped to the specified dimensions. There is no guarantee that the
     * specified bounds cover the rendering surface, although that is the
     * assumption in Netlogo.
     * 
     * This version rotates the resulting shape as a whole
     *
     * @param gc Graphics context in which to render the primitive
     * @param bb Bounding box to which the primitive should be scaled.
     */
    public void render(Graphics gc, Rectangle rect, double rotation, Color color) {
        Graphics2D gc2 = (Graphics2D)gc;
        AffineTransform rot = AffineTransform.getRotateInstance(rotation, rect.x + rect.width / 2, rect.y + rect.height / 2);
        for (NLImagePrimitive prim : primitives) {
            Shape shape = prim.renderingShape(rect);
            shape = rot.createTransformedShape(shape);
            Color previousColor = gc.getColor();
            if (prim.changingColor) {
                gc.setColor(color);
            } else {
                gc.setColor(prim.color);
            }
            if (prim.fill) {
                gc2.fill(shape);
            } else {
                gc2.draw(shape);
            }
            gc.setColor(previousColor);
        }
    }

    /**
     * Generate code to render this image in the specified rectangle.
     * The returned string is not a complete statement; it needs to be
     * embedded as an expression within some other statement.
     * The value created is a new NLIcon.
     */
    public String renderingCode(Rectangle rect) {
        StringBuffer sb = new StringBuffer();
        sb.append("new NLIcon() {\npublic void render(Graphics gc) {\n");
        for (NLImagePrimitive prim : primitives) {
            sb.append(prim.renderingCode(rect));
        }
        sb.append("}\n}\n");
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("NLImage(");
        sb.append("\"");
        sb.append(name);
        sb.append("\", ");
        sb.append(rotates ? "rotates, " : "fixed, ");
        sb.append("color-index=");
        sb.append(changingColor);
        sb.append(", ");
        sb.append(primitives);
        sb.append(")");
        return sb.toString();
    }

    public int compareTo(Object o) {
        return name.compareTo(((NLImage)o).name);
    }
}
