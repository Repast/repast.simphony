/*
 * NLInputBox.java
 *
 * The input ox element has up to three boolean arguments whose function
 * is not immediately obvious. They will be disregarded.
 */

package repast.simphony.relogo.ide.intf;

import java.awt.Rectangle;

/**
 *
 * @author CBURKE
 */
public class NLInputBox extends NLControl {
	String label;
    String variable;
    String initial;
    
    /** Creates a new instance of NLInputBox */
    public NLInputBox(Rectangle bb, String v, String init) {
        super(bb);
        variable = v;
        initial = init;
    }
    
    public boolean isInput() {
        return true;
    }
    
    public String getLabel() {
    	return label;
    }
    
    public String getVariable() {
        return variable;
    }
    
    public String getInitialValue() {
    	return initial;
    }
    
    /**
     * Return a Score representation off the control, if possible.
     * Currently assumes that the variable type is String, although this
     * should be mutable based on information derived from other parts
     * of the model.
     */
    public String toScore() {
        return "<attributes label=\""+variable+"\" ID=\""+variable+"\" sType=\"STRING\" defaultValue=\""+initial+"\"/>";
    }
    
    public String toString() {
        return "InputBox("+boundingBox.x+", "+boundingBox.y+", "+boundingBox.width+", "+boundingBox.height+
                ", variable="+variable+", initial="+initial+")";
    }
}
