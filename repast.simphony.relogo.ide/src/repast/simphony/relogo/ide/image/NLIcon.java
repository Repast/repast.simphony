/*
 * NLIcon.java
 *
 * Created on November 15, 2007, 6:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package repast.simphony.relogo.ide.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.*;

/**
 *
 * @author CBURKE
 */
abstract public class NLIcon {
    int[] rgbKey;
    Color[] colors;
    int numColors;
    Color[] colorStack; // probably overkill...
    int stackDepth;
    
    /** Creates a new instance of NLIcon */
    public NLIcon() {
        rgbKey = new int[5];
        colors = new Color[5];
        numColors = 0;
        colorStack = new Color[5];
        stackDepth = 0;
    }
    
    protected void pushColor(Graphics gc, int rgb24) {
        for (int i=0; i<numColors; i++) {
            if (rgbKey[i] == rgb24) {
                colorStack[stackDepth++] = gc.getColor();
                gc.setColor(colors[i]);
                return;
            }
        }
        rgbKey[numColors] = rgb24;
        int r = (rgb24 & 0x00FF0000) >> 16;
        int g = (rgb24 & 0x0000FF00) >> 8;
        int b = (rgb24 & 0x000000FF);
        colors[numColors] = new Color(r, g, b);
        colorStack[stackDepth++] = gc.getColor();
        gc.setColor(colors[numColors]);
        numColors++;
    }
    protected void popColor(Graphics gc) {
        gc.setColor(colorStack[--stackDepth]);
    }
    
    abstract public void render(Graphics gc);
}
