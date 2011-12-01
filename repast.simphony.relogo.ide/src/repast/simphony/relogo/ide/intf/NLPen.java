/*
 * NLPen.java
 *
 * Created on October 12, 2007, 4:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package repast.simphony.relogo.ide.intf;

import java.awt.Color;

/**
 *
 * @author CBURKE
 */
public class NLPen {
    String label;
    double point; // dunno
    int type;
    Color color;
    boolean showInLegend;
    
    public String getLabel() {
    	if (label.startsWith("\"")) {
    		return label.substring(1, label.length()-1);
    	} else {
    	    return label;
    	}
    }
    
    /** Creates a new instance of NLPen */
    public NLPen(String lb, double pt, int n1, int rgb, boolean b) {
        label = lb;
        point = pt;
        type = n1;
        int red = (rgb&0x00FF0000) >> 16;
        int green = (rgb&0x0000FF00) >> 8;
        int blue = (rgb&0x000000FF);
        color = new Color(red, green, blue);
        showInLegend = b;
    }
    
    public String toString() {
        return "Pen("+label+", width="+point+", type="+(type==0?"line":"box")+", color="+color+", showInLegend="+showInLegend+")";
    }
}
