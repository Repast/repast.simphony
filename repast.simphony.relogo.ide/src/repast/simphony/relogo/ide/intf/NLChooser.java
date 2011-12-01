/*
 * NLChooser.java
 *
 * Created on October 3, 2007, 4:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package repast.simphony.relogo.ide.intf;

import java.awt.Rectangle;
import java.util.LinkedList;

/**
 *
 * @author CBURKE
 */
public class NLChooser extends NLControl {
    String label;
    String variable;
    LinkedList<String> choices;
    int initialChoice;
    
    /** Creates a new instance of NLChooser.
     */
    public NLChooser(Rectangle bb, String lab, String var, LinkedList<String> ch, int init) {
        super(bb);
        label=lab;
        variable=var;
        choices=ch;
        initialChoice=init;
    }
    
    /**
	 * @return the choices
	 */
	public LinkedList<String> getChoices() {
		return choices;
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
    
    public int getInitialValue() {
    	return initialChoice;
    }
    
    /**
     * Return a Score representation of the control, if possible.
     * This variable is an enumeration value, which we most likely need to
     * emulate as an integer within the current framework. Enumeration labels
     * for indices can be output as comments.
     */
    public String toScore() {
        return "<attributes label=\""+label+"\" ID=\""+variable+"\" sType=\"INTEGER\" defaultValue=\""+initialChoice+"\" lowerBound=\"0\" upperBound=\""+(choices.size()-1)+"\"/><!-- chooser.choices=="+choices+" -->";
    }
    
    public String toString() {
        return "Chooser("+boundingBox.x+", "+boundingBox.y+", "+boundingBox.width+", "+boundingBox.height+", label="+label+", variable="+variable+", choices="+choices+", initialChoice="+initialChoice+")";
    }
}
