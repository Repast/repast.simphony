/*
 * NLControl.java
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
public class NLControl {
    Rectangle boundingBox;
    
    /** Creates a new instance of NLControl */
    public NLControl(Rectangle bb) {
        boundingBox = bb;
    }
    
    public boolean isInput() {
        return false;
    }
    
    public boolean isOutput() {
        return false;
    }
    
    /**
     * Return a Score representation of the control, if possible.
     */
    public String toScore() {
        return "";
    }
    
    public String toString() {
        return "Control("+boundingBox.x+", "+boundingBox.y+", "+boundingBox.width+", "+boundingBox.height+")";
    }
}
