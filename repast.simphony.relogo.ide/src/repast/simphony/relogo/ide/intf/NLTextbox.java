/*
 * NLTextbox.java
 *
 * Created on October 2, 2007, 3:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package repast.simphony.relogo.ide.intf;

import java.awt.Rectangle;

/**
 *
 * @author CBURKE
 */
public class NLTextbox extends NLControl {
    String text;
    
    /**
     * Creates a new instance of NLTextbox
     */
    public NLTextbox(Rectangle bb, String t) {
        super(bb);
        text = (t != null ? t.trim() : "");
    }
    
    /**
     * Return a Score representation of the control, if possible.
     * A textbox simply contains text information for display; it is not
     * under program control. The closest equivalent is a comment.
     */
    public String toScore() {
        return "<!-- "+text+" -->";
    }
        
    public String toString() {
        return "Textbox("+boundingBox.x+", "+boundingBox.y+", "+boundingBox.width+", "+boundingBox.height+", "+text+")";
    }
}
