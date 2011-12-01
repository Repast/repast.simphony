/*
 * NLGraphicsWindow.java
 *
 * Created on October 3, 2007, 4:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package repast.simphony.relogo.ide.intf;

import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author CBURKE
 */
public class NLGraphicsWindow extends NLControl {
    
	int minPxcor;
	int minPycor;
	int maxPxcor;
	int maxPycor;
    /** Creates a new instance of NLGraphicsWindow */
    public NLGraphicsWindow(Rectangle bb) {
        super(bb);
    }
    
    /** Creates a new instance of NLGraphicsWindow with the min(max)-px(y)cor specified */
    
    public NLGraphicsWindow(Rectangle bb, int minPx, int maxPx, int minPy, int maxPy) {
        super(bb);
        minPxcor = minPx;
        maxPxcor = maxPx;
        minPycor = minPy;
        maxPycor = maxPy;
    }
    
    public NLGraphicsWindow(Rectangle bb, ArrayList<Integer> lOfInts) {
        super(bb);
        minPxcor = lOfInts.get(5);
        maxPxcor = lOfInts.get(6);
        minPycor = lOfInts.get(7);
        maxPycor = lOfInts.get(8);
//        System.out.println("The min(max)-px(y)cors are: ");
//        System.out.println("miX = " + minPxcor);
//        System.out.println("maX = " + maxPxcor);
//        System.out.println("miY = " + minPycor);
//        System.out.println("maY = " + maxPycor);
    }
    /**
	 * @return the minPxcor
	 */
	public int getMinPxcor() {
		return minPxcor;
	}

	/**
	 * @return the minPycor
	 */
	public int getMinPycor() {
		return minPycor;
	}

	/**
	 * @return the maxPxcor
	 */
	public int getMaxPxcor() {
		return maxPxcor;
	}

	/**
	 * @return the maxPycor
	 */
	public int getMaxPycor() {
		return maxPycor;
	}

	public String toString() {
        return "GraphicsWindow("+boundingBox.x+", "+boundingBox.y+", "+boundingBox.width+", "+boundingBox.height+")";
    }
}
