/*
 * NLPlot.java
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
public class NLPlot extends NLControl {
    String title;
    String xTitle;
    String yTitle;
    double xMin;
    double xMax;
    double yMin;
    double yMax;
    boolean unknownBool;
    boolean multipen;
    LinkedList pens;
    
    public String getTitle() {
    	return title;
    }
    
    public String getXTitle() {
    	return xTitle;
    }
    
    public String getYTitle() {
    	return yTitle;
    }
    
    public Iterable<NLPen> getPens() {
    	return pens;
    }
    
    /** Creates a new instance of NLPlot */
    public NLPlot(Rectangle bb, String t, String x, String y, double x1, double x2, double y1, double y2, boolean u, boolean m, LinkedList pl) {
        super(bb);
        title = t.trim();
        xTitle = x.trim();
        yTitle = y.trim();
        xMin = x1;
        xMax = x2;
        yMin = y1;
        yMax = y2;
        unknownBool = u;
        multipen = m;
        pens = pl;
    }
    
    public String toString() {
        return "Plot("+boundingBox.x+", "+boundingBox.y+", "+boundingBox.width+", "+boundingBox.height+
                ", title="+title+", x-caption="+xTitle+", y-caption="+yTitle+
                ", x-min="+xMin+", x-max="+xMax+", y-min="+yMin+", y-max="+yMax+", x-varies="+unknownBool+", y-varies="+multipen+", pens="+pens+")";
    }
}
