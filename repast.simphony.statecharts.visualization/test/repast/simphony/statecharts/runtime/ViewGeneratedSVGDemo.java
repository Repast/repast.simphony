package repast.simphony.statecharts.runtime;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

import org.apache.batik.swing.*;
import org.apache.batik.svggen.*;
import org.apache.batik.dom.svg.SVGDOMImplementation;

import org.w3c.dom.*;
import org.w3c.dom.svg.*;

public class ViewGeneratedSVGDemo {

	// Make sure to run without -XstartOnFirstThread
    public static void main(String[] args) {
        // Create an SVG document.
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        SVGDocument doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);

        // Create a converter for this document.
        SVGGraphics2D g = new SVGGraphics2D(doc);

        // Do some drawing.
        Shape circle = new Ellipse2D.Double(0, 0, 50, 50);
        g.setPaint(Color.red);
        g.fill(circle);
        g.translate(60, 0);
        g.setPaint(Color.green);
        g.fill(circle);
        g.translate(60, 0);
        g.setPaint(Color.blue);
        g.fill(circle);
        g.setSVGCanvasSize(new Dimension(180, 50));

        // Populate the document root with the generated SVG content.
        Element root = doc.getDocumentElement();
        g.getRoot(root);

        // Display the document.
        JSVGCanvas canvas = new JSVGCanvas();
        JFrame f = new JFrame();
        f.getContentPane().add(canvas);
        canvas.setSVGDocument(doc);
        f.pack();
        f.setVisible(true);
    }
}