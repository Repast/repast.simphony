/*
 * NLOutput.java
 *
 * Created on October 3, 2007, 4:02 PM
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
public class NLOutput extends NLControl {
    
    /** Creates a new instance of NLOutput */
    public NLOutput(Rectangle bb) {
        super(bb);
    }
    
    public String toString() {
        return "Output("+boundingBox.x+", "+boundingBox.y+", "+boundingBox.width+", "+boundingBox.height+")";
    }
}
