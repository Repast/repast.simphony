package repast.simphony.statecharts.runtime;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.net.URI;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.dom.svg.SVGContext;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.dom.svg.SVGOMRect;
import org.apache.batik.dom.svg.SVGSVGContext;
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
import org.w3c.dom.svg.SVGCircleElement;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGElement;
import org.w3c.dom.svg.SVGLineElement;
import org.w3c.dom.svg.SVGPolygonElement;
import org.w3c.dom.svg.SVGRect;
import org.w3c.dom.svg.SVGRectElement;
import org.w3c.dom.svg.SVGSVGElement;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunListener;
import repast.simphony.statecharts.AbstractState;
import repast.simphony.statecharts.DefaultStateChart;
import repast.simphony.statecharts.StateChartScheduler;
import repast.simphony.statecharts.Transition;

public class StateChartSVGDisplay {

	JFrame frame;
	URI uri;
	CustomJSVGCanvas svgCanvas;
	StateChartSVGDisplayController controller;
	StateChartSVGModel model;

	AbstractAction frameCloseAction;

	/**
	 * Custom JSVGCanvas. This is needed to account for the
	 * IllegalStateException thrown by RunnableQueue when updates related to
	 * mouse events or resizing events collide with the dynamic SVG updates we
	 * make to the statechart images.
	 * 
	 * @author jozik
	 * 
	 */
	private class CustomJSVGCanvas extends JSVGCanvas {

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

			private static final int SELECTION_WIDTH = 6;

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (SwingUtilities.isRightMouseButton(e)) {
					// System.out.println("Mouse right clicked.");
					// find enclosing uuid
					SVGSVGElement svgSvgElement = CustomJSVGCanvas.this
							.getSVGDocument().getRootElement();
					if (svgSvgElement instanceof SVGOMElement) {
						SVGOMElement svgOmElement = (SVGOMElement) svgSvgElement;
						SVGContext sContext = svgOmElement.getSVGContext();
						if (sContext instanceof SVGSVGContext) {
							SVGSVGContext svgSContext = (SVGSVGContext) sContext;
							AffineTransform at;
							try {
								at = CustomJSVGCanvas.this
										.getViewBoxTransform().createInverse();

								Point2D mousePoint = new Point2D.Float(
										e.getX(), e.getY());
								Point2D.Float transformedPoint = new Point2D.Float();
								at.transform(mousePoint, transformedPoint);

								SVGRect rect = new SVGOMRect(transformedPoint.x
										- SELECTION_WIDTH / 2,
										transformedPoint.y - SELECTION_WIDTH
												/ 2, SELECTION_WIDTH,
										SELECTION_WIDTH);// svgSvgElement.createSVGRect();

								if (rect instanceof SVGRect) {
									List intersectionList = svgSContext
											.getIntersectionList(rect, null);
									if (intersectionList.isEmpty()) {
										JPopupMenu menu = new JPopupMenu();
										JMenuItem item = new JMenuItem(
												"Initialize Statechart");
										item.addActionListener(new ActionListener() {

											@Override
											public void actionPerformed(
													ActionEvent e) {
												initializeStatechart();
											}
										});
										menu.add(item);
										menu.show(e.getComponent(), e.getX(),
												e.getY());

									} else {
										String uuid = null;
										// Find rects (simple and composite
										// states)
										// Find polygons (branching states and
										// transition arrow heads)
										// Find circles (final states)
										for (Object o : intersectionList) {
											// System.out.println(o);
											if (o instanceof SVGRectElement
													|| o instanceof SVGPolygonElement
													|| o instanceof SVGCircleElement) {
												SVGElement svgE = (SVGElement) o;
												String tempUuid = svgE
														.getAttribute("uuid");
												if (!tempUuid.isEmpty())
													uuid = tempUuid;
											}
										}

										// Find lines (transitions) transitions
										// override states
										for (Object o : intersectionList) {
											if (o instanceof SVGLineElement) {
												SVGLineElement sle = (SVGLineElement) o;
												String tempUuid = sle
														.getAttribute("uuid");
												if (!tempUuid.isEmpty())
													uuid = tempUuid;
											}
										}

										if (uuid != null) {
											final AbstractState state = StateChartSVGDisplay.this.controller.stateChart
													.getStateForUuid(uuid);
											if (state != null) {

												JPopupMenu menu = new JPopupMenu();
												JMenuItem item = new JMenuItem(
														"Activate "
																+ state.getId());
												item.addActionListener(new ActionListener() {

													@Override
													public void actionPerformed(
															ActionEvent e) {
														activateState(state);
													}
												});
												menu.add(item);

												menu.show(e.getComponent(),
														e.getX(), e.getY());
											} else {
												final Transition transition = StateChartSVGDisplay.this.controller.stateChart
														.getTransitionForUuid(uuid);
												if (transition != null) {

													JPopupMenu menu = new JPopupMenu();
													JMenuItem item = new JMenuItem(
															"Follow "
																	+ transition
																			.getId());
													item.addActionListener(new ActionListener() {

														@Override
														public void actionPerformed(
																ActionEvent e) {
															followTransition(transition);
														}
													});
													menu.add(item);
													menu.show(e.getComponent(),
															e.getX(), e.getY());
												}
											}
										}
									}
								}
							} catch (NoninvertibleTransformException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			}

			protected void activateState(final AbstractState state) {
				doStatechartAction(new Runnable() {
					@Override
					public void run() {
						controller.stateChart.activateState(state);

					}

				});
			}

			protected void followTransition(final Transition transition) {
				doStatechartAction(new Runnable() {
					@Override
					public void run() {
						controller.stateChart.followTransition(transition);
					}
				});
			}

			protected void initializeStatechart() {
				doStatechartAction(new Runnable() {
					@Override
					public void run() {
						StateChartScheduler.INSTANCE.beginNowWithoutScheduling((DefaultStateChart)controller.stateChart);
					}
				});
			}

			protected void doStatechartAction(final Runnable statechartAction) {
				if (controller.isRunning()) {
					RunListener listener = new RunListener() {

						@Override
						public void stopped() {
							RunEnvironment.getInstance()
									.removeRunListener(this);
						}

						@Override
						public void paused() {
							statechartAction.run();
							RunEnvironment.getInstance()
									.removeRunListener(this);
							new Thread(new Runnable() {
								@Override
								public void run() {
									RunEnvironment.getInstance().resumeRun();
								}
							}).start();
						}

						@Override
						public void started() {
						}

						@Override
						public void restarted() {
						}

					};
					RunEnvironment.getInstance().addRunListener(listener);
					RunEnvironment.getInstance().pauseRun();
				} else {
					statechartAction.run();
				}
			}

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

	public StateChartSVGDisplay(StateChartSVGDisplayController controller,
			String frameTitle, URI uri) {
		this.controller = controller;
		frame = new JFrame(frameTitle);
		frame.setAlwaysOnTop(true);
		this.uri = uri;
		frameCloseAction = new AbstractAction("Close Window") {
			@Override
			public void actionPerformed(ActionEvent e) {
				// For when the GUI is closed via keyboard shortcut
				timer.cancel();
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
				frame.setAlwaysOnTop(((JCheckBoxMenuItem) evt.getSource())
						.isSelected());
			}
		});
		menu.add(item);
		bar.add(menu);
		panel.add(bar, BorderLayout.NORTH);

		KeyStroke closeKey = KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask());
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(closeKey,
				"closeWindow");
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

	protected void closeFrame() {
		timer.cancel();
		frame.setVisible(false);
		frame.dispose();
		// no need to notify close listeners since the this is triggered by
		// the originating dock frame closing
	}

	/**
	 * Indicates whether the canvas has finished its first render, the canvas is
	 * now ready for modification of the dom
	 */
	private AtomicBoolean isReadyForModification = new AtomicBoolean(false);
	private AtomicBoolean needsInitialUpdate = new AtomicBoolean(true);

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

				isReadyForModification.set(true);
				if (needsInitialUpdate.compareAndSet(true, false)) {
					controller.update();
				} else {

					if (controller.tryAnotherUpdate) {
						renewDocument();
					}
				}
			}

		});

		svgCanvas.setURI(uri.toString());
		panel.add("Center", svgCanvas);

		return panel;
	}

	private long lastRenderTS = 0;
	private static final long FRAME_UPDATE_INTERVAL = 50; // in milliseconds
	private volatile boolean isTimerScheduled = false;
	private Timer timer = new Timer(true);

	/**
	 * Renew the document by replacing the root node with the one of the new
	 * document
	 * 
	 * @param doc
	 *            The new document
	 * 
	 * @author jozik
	 * @author 
	 *         http://stackoverflow.com/questions/10838971/make-a-jsvgcanvas-inside
	 *         -jsvgscrollpane-match-the-svgdocument-size
	 */
	public void renewDocument() {
		long ts = System.currentTimeMillis();

		// Throttling here.
		if (ts - lastRenderTS > FRAME_UPDATE_INTERVAL /* true */) {
			if (isReadyForModification.compareAndSet(true, false)) {
				controller.tryAnotherUpdate = false;
				final SVGDocument doc = model.getCurrentSVGDocument();

				UpdateManager um = svgCanvas.getUpdateManager();
				if (um != null) {
					RunnableQueue rq = um.getUpdateRunnableQueue();
					if (rq.getQueueState().equals(RunnableQueue.RUNNING)) {

						try {
							rq.invokeLater(new Runnable() {
								@Override
								public void run() {

									// Get the root tags of the documents
									DOMImplementation impl;
									impl = SVGDOMImplementation
											.getDOMImplementation();
									Document d = DOMUtilities
											.deepCloneDocument(
													svgCanvas.getSVGDocument(),
													impl);

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
							// exceptions are ignored
						}
					}
				}
			} else {// wasn't ready for update, wait for gvt notification
				controller.tryAnotherUpdate = true;
			}
		} else {
			synchronized (this) {
				if (!isTimerScheduled) {
					isTimerScheduled = true;
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							renewDocument();
							isTimerScheduled = false;
						}

					}, FRAME_UPDATE_INTERVAL);
				}
			}
		}
	}
}
