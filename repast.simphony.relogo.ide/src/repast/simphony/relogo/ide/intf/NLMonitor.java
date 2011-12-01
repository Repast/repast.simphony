/*
 * NLMonitor.java
 *
 * Created on October 3, 2007, 3:43 PM
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
public class NLMonitor extends NLControl {
    String label;
    String variable;
    int decimalPlaces;
    
    /** Creates a new instance of NLMonitor */
    public NLMonitor(Rectangle bb, String lab, String var, int dp, int u1) {
        super(bb);
        label = (lab.trim().equals("NIL") ? null : lab.trim());
        variable = (var.trim().equals("NIL") ? null : var.trim());
        decimalPlaces = dp;
        if (u1 != 1) {
            System.out.println("Unknown monitor int == "+u1);
        }
    }
    
    public boolean isOutput() {
        return true;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getVariable() {
        return variable;
    }
    
    /**
     * Return a Score representation off the control, if possible.
     * In the case of a monitor, we can identify the name of an attribute,
     * but we don't have enough information to comlete the spec.
     */
    public String toScore() {
        return "<attributes label=\""+label+"\" ID=\""+variable+"\" sType=\"TBD\" />";
    }
    
    public String toString() {
        return "Monitor("+boundingBox.x+", "+boundingBox.y+", "+boundingBox.width+", "+boundingBox.height+", "+
                "label=\""+label+"\", variable=\""+variable+"\", decimalPlaces="+decimalPlaces+")";
    }
}
