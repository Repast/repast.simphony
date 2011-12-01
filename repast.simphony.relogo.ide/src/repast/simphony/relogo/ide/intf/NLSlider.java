/*
 * NLSlider.java
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
public class NLSlider extends NLControl {
    String label;
    String variable;
    double minimum;
    double maximum;
    Number value;
    double step;
    String units;
    
    /** Creates a new instance of NLSlider */
    public NLSlider(Rectangle bb, String lab, String var, double min, double max, Number val, double inc, int u1, String unt) {
        super(bb);
        label = (lab.trim().equals("NIL") ? null : lab.trim());
        variable = (var.trim().equals("NIL") ? null : var.trim());
        minimum = min;
        maximum = max;
        value = val;
        step = inc;
        if (u1 != 1) {
            System.out.println("Slider unknown int == "+u1);
        }
        if (unt != null) {
            units = (unt.trim().equals("NIL") ? null : unt.trim());
        }
    }
    
    /**
	 * @return the minimum
	 */
	public double getMinimum() {
		return minimum;
	}

	/**
	 * @return the maximum
	 */
	public double getMaximum() {
		return maximum;
	}

	/**
	 * @return the step
	 */
	public double getStep() {
		return step;
	}

	/**
	 * @return the units
	 */
	public String getUnits() {
		return units;
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
    
    public Object getInitialValue() {
    	return value;
    }
    
    public String getScoreType() {
		String type = "";
		double floatValue = value.doubleValue();
		if ((value instanceof Integer || floatValue == Math.floor(floatValue))
				&& step == Math.floor(step) && minimum == Math.floor(minimum)
				&& maximum == Math.floor(maximum)) {
			type = "INTEGER";
		} else {
			type = "FLOAT";
		}
		return type;
    }
    
    /**
     * Return a Score representation of the control, if possible.
     * This variable is either a floating point value or an integer,
     * depending on the values of the parameters. There is no provision
     * for returning the NetLogo step parameter except as a comment.
     */
    public String toScore() {
		return "<attributes label=\"" + label + "\" ID=\"" + variable
				+ "\" sType=\"" + getScoreType() + "\" defaultValue=\"" + value
				+ "\" lowerBound=\"" + minimum + "\" upperBound=\"" + maximum
				+ "\"/> <!-- slider.step==" + step + " -->";
	}
    
    public String toString() {
        return "Slider("+boundingBox.x+", "+boundingBox.y+", "+boundingBox.width+", "+boundingBox.height+", "+
                "label=\""+label+"\", variable=\""+variable+"\", minimum="+minimum+", maximum="+maximum+", value="+value+", step="+step+", units="+units+")";
    }
}
