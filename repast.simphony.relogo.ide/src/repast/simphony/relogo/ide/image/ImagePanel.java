/**
 * 
 */
package repast.simphony.relogo.ide.image;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

class ImagePanel extends JPanel {

    NLImage img;
    Rectangle rect;

    public ImagePanel() {
        super();
        rect = new Rectangle();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            img.render(g, getBounds(rect), null);
            System.out.println(img.renderingCode(rect));
        }
    }

    public void setImage(NLImage i) {
        img = i;
        invalidate();
        repaint();
    }
}