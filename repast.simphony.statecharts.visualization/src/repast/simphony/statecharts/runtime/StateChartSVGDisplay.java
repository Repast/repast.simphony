package repast.simphony.statecharts.runtime;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.bridge.UpdateManagerAdapter;
import org.apache.batik.bridge.UpdateManagerEvent;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.util.RunnableQueue;
import org.apache.batik.util.RunnableQueue.RunnableQueueState;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

public class StateChartSVGDisplay {

	JFrame frame;
	URI uri;
	CustomJSVGCanvas svgCanvas;
	StateChartSVGDisplayController controller;
	StateChartSVGModel model;

	/**
	 * Custom JSVGCanvas.
	 * This is needed to account for the IllegalStateException thrown by RunnableQueue
	 * when updates related to mouse events or resizing events collide with the
	 * dynamic SVG updates we make to the statechart images.
	 * @author jozik
	 *
	 */
	private static class CustomJSVGCanvas extends JSVGCanvas {

		/**
		 * 
		 */
		private static final long serialVersionUID = 20863422779297277L;

		@Override
		protected boolean updateRenderingTransform() {
			boolean result = false;
			try {
				result = super.updateRenderingTransform();
			} catch (IllegalStateException e) {
				// to catch illegal state exception thrown by RunnableQueue
			}
			return result;
		}

		/**
		 * Creates an instance of Listener.
		 */
		protected Listener createListener() {
			return new CustomCanvasSVGListener();
		}

		/**
		 * To get rid of illegal state exception thrown by RunnableQueue.
		 */
		protected class CustomCanvasSVGListener extends CanvasSVGListener {

			@Override
			protected void dispatchKeyTyped(KeyEvent e) {
				try {
					super.dispatchKeyTyped(e);
				} catch (IllegalStateException e1) {
					// to catch illegal state exception thrown by RunnableQueue
				}
			}

			@Override
			protected void dispatchKeyPressed(KeyEvent e) {
				try {
					super.dispatchKeyPressed(e);
				} catch (IllegalStateException e1) {
					// to catch illegal state exception thrown by RunnableQueue
				}
			}

			@Override
			protected void dispatchKeyReleased(KeyEvent e) {

				try {
					super.dispatchKeyReleased(e);
				} catch (IllegalStateException e1) {
					// to catch illegal state exception thrown by RunnableQueue
				}
			}

			@Override
			protected void dispatchMouseClicked(MouseEvent e) {

				try {
					super.dispatchMouseClicked(e);
				} catch (IllegalStateException e1) {
					// to catch illegal state exception thrown by RunnableQueue
				}
			}

			@Override
			protected void dispatchMousePressed(MouseEvent e) {

				try {
					super.dispatchMousePressed(e);
				} catch (IllegalStateException e1) {
					// to catch illegal state exception thrown by RunnableQueue
				}
			}

			@Override
			protected void dispatchMouseReleased(MouseEvent e) {

				try {
					super.dispatchMouseReleased(e);
				} catch (IllegalStateException e1) {
					// to catch illegal state exception thrown by RunnableQueue
				}
			}

			@Override
			protected void dispatchMouseEntered(MouseEvent e) {

				try {
					super.dispatchMouseEntered(e);
				} catch (IllegalStateException e1) {
					// to catch illegal state exception thrown by RunnableQueue
				}
			}

			@Override
			protected void dispatchMouseExited(MouseEvent e) {

				try {
					super.dispatchMouseExited(e);
				} catch (IllegalStateException e1) {
					// to catch illegal state exception thrown by RunnableQueue
				}
			}

			@Override
			protected void dispatchMouseDragged(MouseEvent e) {

				try {
					super.dispatchMouseDragged(e);
				} catch (IllegalStateException e1) {
					// to catch illegal state exception thrown by RunnableQueue
				}
			}

			@Override
			protected void dispatchMouseMoved(MouseEvent e) {

				try {
					super.dispatchMouseMoved(e);
				} catch (IllegalStateException e1) {
					// to catch illegal state exception thrown by RunnableQueue
				}
			}

		}

	}

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
		frame.setSize(600, 400);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// TODO: clean up statechart references
			}
		});
		frame.setLocationByPlatform(true);
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
		svgCanvas = new CustomJSVGCanvas();
		svgCanvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		svgCanvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
			public void documentLoadingCompleted(SVGDocumentLoaderEvent evt) {
				SVGDocument svgDoc = svgCanvas.getSVGDocument();
				controller.initializeModel(svgDoc);
			}
		});
		svgCanvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {

			@Override
			public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
				isReadyForModification = true;
			}

		});

		// svgCanvas.addUpdateManagerListener(new UpdateManagerAdapter() {
		//
		//
		//
		// });
		// svgCanvas.addSVGLoadEventDispatcherListener(new
		// SVGLoadEventDispatcherAdapter() {
		//
		// @Override
		// public void svgLoadEventDispatchStarted(SVGLoadEventDispatcherEvent e) {
		// isReadyForModification = true;
		//
		// }
		// });

		svgCanvas.setURI(uri.toString());
		panel.add("Center", svgCanvas);

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
	 * @author 
	 *         http://stackoverflow.com/questions/10838971/make-a-jsvgcanvas-inside
	 *         -jsvgscrollpane-match-the-svgdocument-size
	 */
	private long lastRenderTS = 0;
	private static final long FRAME_UPDATE_INTERVAL = 16; // in milliseconds

	public void renewDocument() {
		long ts = System.currentTimeMillis();
		if (ts - lastRenderTS > FRAME_UPDATE_INTERVAL) {

			if (isReadyForModification) {

				final SVGDocument doc = model.getCurrentSVGDocument();

				// SVGUtils.printDocument(doc, System.out);
				UpdateManager um = svgCanvas.getUpdateManager();
				if (um != null) {
					RunnableQueue rq = um.getUpdateRunnableQueue();
					if (rq.getQueueState().equals(RunnableQueue.RUNNING)) {
						isReadyForModification = false;
						try {
							rq.invokeLater(new Runnable() {
								@Override
								public void run() {
									// isReadyForModification = false;
									// Get the root tags of the documents
									DOMImplementation impl;
									impl = SVGDOMImplementation.getDOMImplementation();
									Document d = DOMUtilities.deepCloneDocument(svgCanvas.getSVGDocument(), impl);

									Node oldRoot = d.getFirstChild();
									Node newRoot = doc.getFirstChild();

									// Make the new node suitable for the old
									// document
									newRoot = d.importNode(newRoot, true);

									// SVGDocument doc = svgCanvas.getSVGDocument();
									// doc.g
									d.replaceChild(newRoot, oldRoot);
									svgCanvas.setSVGDocument((SVGDocument) d);

									// svgCanvas.setSVGDocument(doc);
									// svgCanvas.getParent().validate();
									// svgCanvas.getParent().doLayout();
									// svgCanvas.invalidate();
								}
							});
							lastRenderTS = ts;
						} catch (Exception e) {
//							System.out.println("Caught exception.");
//							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
