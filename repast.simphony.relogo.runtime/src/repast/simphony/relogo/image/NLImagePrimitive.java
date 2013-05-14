/*
 * NLImagePrimitive.java
 *
 * Created on September 25, 2007, 7:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package repast.simphony.relogo.image;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Shape;

/**
 *
 * @author CBURKE
 */
abstract public class NLImagePrimitive {
    protected Color color;
	protected boolean changingColor;
    protected boolean fill;
    
    /** Creates a new instance of NLImagePrimitive */
    public NLImagePrimitive(int c, boolean f, boolean change) {
        int r = (c & 0x00FF0000) >> 16;
        int g = (c & 0x0000FF00) >> 8;
        int b = (c & 0x000000FF);
        color = new Color(r, g, b);
        changingColor=change;
        fill = f;
    }
    
    public Color getColor() {
		return color;
	}

	public boolean isChangingColor() {
		return changingColor;
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
    abstract public void render(Graphics gc, Rectangle bb, Color clr);
    
    /**
     * Generate a shape representation of this image in the specified rectangle.
     */
    abstract public Shape renderingShape(Rectangle bb);
    
    /**
     * Generate code to render this image in the specified rectangle.
     */
    abstract public String renderingCode(Rectangle bb);
    
    abstract public Shape reLogoShape(Rectangle bb);
}
