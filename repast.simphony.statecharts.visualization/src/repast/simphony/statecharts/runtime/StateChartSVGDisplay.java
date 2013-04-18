package repast.simphony.statecharts.runtime;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.util.RunnableQueue;
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
	
	AbstractAction frameCloseAction;

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
		frame.setAlwaysOnTop(true);
		this.uri = uri;
		frameCloseAction = new AbstractAction("Close Window") {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	   // For when the GUI is closed via keyboard shortcut
	      frame.setVisible(false);
	      frame.dispose();
	      StateChartSVGDisplay.this.controller.notifyCloseListeners();
	    }
	  };
		
	}

	public void initialize() {

		JPanel panel = createComponents();
		JMenuBar bar = new JMenuBar();
		    JMenu menu = new JMenu("Options");
		    menu.setMnemonic(KeyEvent.VK_O);
		    JCheckBoxMenuItem item = new JCheckBoxMenuItem("Always On Top");
		    item.setSelected(true);
		    item.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent evt) {
		       frame.setAlwaysOnTop(((JCheckBoxMenuItem) evt.getSource()).isSelected());
		      }
		    });
		    menu.add(item);
		    bar.add(menu);
		    panel.add(bar, BorderLayout.NORTH);
		    
		KeyStroke closeKey = KeyStroke.getKeyStroke(
	      KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(closeKey, "closeWindow");
		panel.getActionMap().put("closeWindow", frameCloseAction);
		
		frame.getContentPane().add(panel);
		// Display the frame.
		frame.setSize(600, 400);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// For when the GUI is closed via mouse click
				controller.notifyCloseListeners();
			}
		});
		frame.setLocationByPlatform(true);
		frame.setVisible(true);

	}
	
	protected void closeFrame(){
		frame.setVisible(false);
		frame.dispose();
		// no need to notify close listeners since the this is triggered by 
		// the originating dock frame closing
	}

	/**
	 * Indicates whether the canvas has finished its first render, the canvas is
	 * now ready for modification of the dom
	 */
	private boolean isReadyForModification = false;
	private boolean needsInitialUpdate = true;

	private JPanel createComponents() {
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
				
				if (needsInitialUpdate){
					needsInitialUpdate = false;
					controller.update();
				}
				if (controller.tryAnotherUpdate){
					renewDocument();
				}
			}

		});

		svgCanvas.setURI(uri.toString());
		panel.add("Center", svgCanvas);

		return panel;
	}

	
	private long lastRenderTS = 0;
	private static final long FRAME_UPDATE_INTERVAL = 16; // in milliseconds

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
	public void renewDocument() {
		long ts = System.currentTimeMillis();
//		System.out.println("Attempting to update, checking update interval.");
		if (/*ts - lastRenderTS > FRAME_UPDATE_INTERVAL*/ true) { // No throttling to avoid missing changes
//			System.out.println("###########   Passed update interval, checking is ready for modification.");
			if (isReadyForModification) {
				isReadyForModification = false;
				controller.tryAnotherUpdate = false;
//				System.out.println("#################################   Passed is ready for modification, updating.");
				final SVGDocument doc = model.getCurrentSVGDocument();

				// SVGUtils.printDocument(doc, System.out);
				UpdateManager um = svgCanvas.getUpdateManager();
				if (um != null) {
					RunnableQueue rq = um.getUpdateRunnableQueue();
					if (rq.getQueueState().equals(RunnableQueue.RUNNING)) {
						
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

									d.replaceChild(newRoot, oldRoot);
									svgCanvas.setSVGDocument((SVGDocument) d);

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
			else {// wasn't ready for update, wait to be notified
				controller.tryAnotherUpdate = true;
			}
		}
	}
}
