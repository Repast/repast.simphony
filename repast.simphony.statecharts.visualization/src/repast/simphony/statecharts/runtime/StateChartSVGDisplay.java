package repast.simphony.statecharts.runtime;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

public class StateChartSVGDisplay {

	JFrame frame;
	URI uri;
	JSVGCanvas svgCanvas;
	StateChartSVGDisplayController controller;
	StateChartSVGModel model;

	protected void setModel(StateChartSVGModel model) {
		this.model = model;
	}

	public StateChartSVGDisplay(StateChartSVGDisplayController controller, String frameTitle, URI uri) {
		this.controller = controller;
		frame = new JFrame(frameTitle);
		this.uri = uri;
	}

	public void initialize() {
		// Add components to the frame.
		frame.getContentPane().add(createComponents());
		// Display the frame.
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// TODO: clean up statechart references
			}
		});
		frame.setSize(400, 400);
		frame.setVisible(true);
	}

	/**
	 * Indicates whether the canvas has finished its first render, the canvas is
	 * now ready for modification of the dom
	 */
	private boolean isReadyForModification = false;

	private Component createComponents() {
		// Create a panel and add the button, status label and the SVG canvas.
		final JPanel panel = new JPanel(new BorderLayout());
		svgCanvas = new JSVGCanvas();
		svgCanvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		svgCanvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
			public void documentLoadingCompleted(SVGDocumentLoaderEvent evt) {
				SVGDocument svgDoc = svgCanvas.getSVGDocument();
				controller.initializeModel(svgDoc);
				isReadyForModification = true;
			}
		});
		panel.add("Center", svgCanvas);
		svgCanvas.setURI(uri.toString());
		return panel;
	}

	/**
	 * Renew the document by replacing the root node with the one of the new
	 * document
	 * 
	 * @param doc
	 *          The new document
	 *          
	 * @author jozik
	 * @author http://stackoverflow.com/questions/10838971/make-a-jsvgcanvas-inside-jsvgscrollpane-match-the-svgdocument-size
	 */
	public void renewDocument() {
		
		if (isReadyForModification) {
			final SVGDocument doc = model.getCurrentSVGDocument();
			svgCanvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(new Runnable() {
				@Override
				public void run() {
					// Get the root tags of the documents
					Node oldRoot = svgCanvas.getSVGDocument().getFirstChild();
					Node newRoot = doc.getFirstChild();

					// Make the new node suitable for the old
					// document
					newRoot = svgCanvas.getSVGDocument().importNode(newRoot, true);

					// Replace the nodes
					svgCanvas.getSVGDocument().replaceChild(newRoot, oldRoot);
				}
			});
		}

	}
}
