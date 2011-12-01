/*
 * NLCommandConsole.java
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
public class NLCommandConsole extends NLControl {
    String text;
    
    /** Creates a new instance of NLCommandConsole */
    public NLCommandConsole(Rectangle bb, String t, int unk) {
        super(bb);
        text = t.trim();
    }
    
    public String toString() {
        return "CommandConsole("+boundingBox.x+", "+boundingBox.y+", "+boundingBox.width+", "+boundingBox.height+", text="+text+")";
    }
}
