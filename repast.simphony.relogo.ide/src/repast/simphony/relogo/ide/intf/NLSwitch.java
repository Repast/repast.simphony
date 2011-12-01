/*
 * NLSwitch.java
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
public class NLSwitch extends NLControl {
    String text;
    String variable;
    boolean on;
    int value2;
    int value3;
    
    /** Creates a new instance of NLSwitch */
    public NLSwitch(Rectangle bb, String t, String v, int n1, int n2, int n3) {
        super(bb);
        text = t.trim();
        variable = v.trim();
        on = (n1 == 0);
        value2 = n2;
        value3 = n3;
        if (value2 != 1) {
            System.out.println("Unexpected switch value2 == "+value2);
        }
        if (value3 != -1000) {
            System.out.println("Unexpected switch value3 == "+value3);
        }
    }

    public boolean isInput() {
        return true;
    }
    
    public String getLabel() {
    	return text;
    }
    
    public String getVariable() {
        return variable;
    }
    
    public Object getInitialValue() {
    	return on;
    }
    
    /**
     * Return a Score representation of the control, if possible.
     * This variable is a Boolean value.
     */
    public String toScore() {
        return "<attributes label=\""+text+"\" ID=\""+variable+"\" sType=\"BOOLEAN\" defaultValue=\""+on+"\"/>";
    }
    
    public String toString() {
        return "Switch("+boundingBox.x+", "+boundingBox.y+", "+boundingBox.width+", "+boundingBox.height+
                ", label="+text+", variable="+variable+", on?="+on+", v2="+value2+", v3="+value3+")";
    }
}
