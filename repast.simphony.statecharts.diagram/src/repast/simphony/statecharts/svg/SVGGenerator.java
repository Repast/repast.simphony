package repast.simphony.statecharts.svg;

import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.svggen.DOMTreeManager;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.IExpandableFigure;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderPlugin;
import org.eclipse.gmf.runtime.diagram.ui.render.util.DiagramImageUtils;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.Decoration;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.export.GraphicsSVG;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.RenderedMapModeGraphics;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGRectElement;

public class SVGGenerator {

	private DiagramEditPart _dgrmEP;
	private Rectangle viewBox = null;
	private Element svgRoot = null;
	private IFigure printableLayer;

	private int image_margin = 0;
	private Dimension emptyImageSize;
	private static final int DEFAULT_IMAGE_MARGIN_PIXELS = 10;
	private static final int DEFAULT_EMPTY_IMAGE_SIZE_PIXELS = 100;

	/**
	 * Creates a new instance.
	 * 
	 * @param dgrmEP
	 *            the diagram editpart
	 */
	public SVGGenerator(DiagramEditPart dgrmEP) {
		this._dgrmEP = dgrmEP;
		this.printableLayer = LayerManager.Helper.find(_dgrmEP).getLayer(
				LayerConstants.PRINTABLE_LAYERS);
		IMapMode mm = getMapMode();
		image_margin = mm.DPtoLP(DEFAULT_IMAGE_MARGIN_PIXELS);
		emptyImageSize = (Dimension) mm.DPtoLP(new Dimension(
				DEFAULT_EMPTY_IMAGE_SIZE_PIXELS,
				DEFAULT_EMPTY_IMAGE_SIZE_PIXELS));
		populateUUIDMap();
	}

	
	private void populateUUIDMap() {
		Diagram diag = _dgrmEP.getDiagramView();
		EObject eo = diag.getElement();
		System.out.println(eo.getClass());
		List editParts = _dgrmEP.getPrimaryEditParts();
		for (Object editPart : editParts){
//			list.clear();
			if (editPart instanceof GraphicalEditPart){ 
				EObject o = (EObject)((GraphicalEditPart) editPart).getAdapter(EObject.class);
//				System.out.println(o);
//				System.out.println(o.getClass());
				
			}
		}
	}

	public void renderPartsToGraphics(List editparts,
			org.eclipse.swt.graphics.Rectangle sourceRect) {

		Graphics graphics = null;
		try {
			IMapMode mm = getMapMode();

			PrecisionRectangle rect = new PrecisionRectangle();
			rect.setX(sourceRect.x);
			rect.setY(sourceRect.y);
			rect.setWidth(sourceRect.width);
			rect.setHeight(sourceRect.height);

			mm.LPtoDP(rect);

			// Create the graphics and wrap it with the HiMetric graphics object
			graphics = setUpGraphics((int) Math.round(rect.preciseWidth),
					(int) Math.round(rect.preciseHeight));

			RenderedMapModeGraphics mapModeGraphics = new RenderedMapModeGraphics(
					graphics, getMapMode());

			renderToGraphics(graphics, mapModeGraphics, new Point(sourceRect.x,
					sourceRect.y), editparts);
			setSVGRoot(graphics);
		} finally {
			if (graphics != null)
				disposeGraphics(graphics);
		}
	}

	/**
	 * @return
	 */
	protected IMapMode getMapMode() {
		return MapModeUtil.getMapMode(getDiagramEditPart().getFigure());
	}

	/**
	 * @return DiagramEditPart
	 */
	protected DiagramEditPart getDiagramEditPart() {
		return this._dgrmEP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramGenerator#
	 * setUpGraphics(int, int)
	 */
	protected Graphics setUpGraphics(int width, int height) {
		viewBox = new Rectangle(0, 0, width, height);
		return GraphicsSVG.getInstance(viewBox);
	}

	/**
	 * Renders the list of editparts to the graphics object. Any connections
	 * where both the source and target editparts are passed in are also drawn.
	 * 
	 * @param graphics
	 *            the graphics object on which to draw
	 * @param translateOffset
	 *            a <code>Point</code> that the value the <code>graphics</code>
	 *            object will be translated by in relative coordinates.
	 * @param editparts
	 *            the list of <code>IGraphicalEditParts</code> that will be
	 *            rendered to the graphics object
	 */
	final protected void renderToGraphics(Graphics graphics, Graphics mapModeGraphics,
			Point translateOffset, List editparts) {
		GraphicsSVG svgG = (GraphicsSVG) graphics;
//		Document doc = svgG.getDocument();
		DOMTreeManager dtm = svgG.getSVGGraphics2D().getDOMTreeManager();
		
		// List sortedEditparts = sortSelection(editparts);

		mapModeGraphics.translate((-translateOffset.x), (-translateOffset.y));
		mapModeGraphics.pushState();

		List<GraphicalEditPart> connectionsToPaint = new LinkedList<GraphicalEditPart>();

		Map decorations = findDecorations(editparts);

		for (Iterator editPartsItr = editparts.listIterator(); editPartsItr
				.hasNext();) {
			IGraphicalEditPart editPart = (IGraphicalEditPart) editPartsItr
					.next();

			// do not paint selected connection part
			if (editPart instanceof ConnectionEditPart) {
				connectionsToPaint.add(editPart);
			} else {
				connectionsToPaint.addAll(findConnectionsToPaint(editPart));
				// paint shape figure
				IFigure figure = editPart.getFigure();
				paintFigure(mapModeGraphics, figure);
				Element topLevelGroup = dtm.getTopLevelGroup();
				
				NodeList nl = topLevelGroup.getElementsByTagNameNS("*", "rect");
				
				System.out.println("Beginning of printout:");
				for(int i = 0; i < nl.getLength(); i++) {
					SVGRectElement rect = (SVGRectElement)nl.item(i);
					System.out.println(rect.toString());
					rect.setAttributeNS(null, "id", "hello");
				}
				System.out.println("End of printout:");

				paintDecorations(mapModeGraphics, figure, decorations);
			}
		}

		// paint the connection parts after shape parts paint
		decorations = findDecorations(connectionsToPaint);

		for (Iterator<GraphicalEditPart> connItr = connectionsToPaint
				.iterator(); connItr.hasNext();) {
			IFigure figure = connItr.next().getFigure();
			paintFigure(graphics, figure);
			paintDecorations(graphics, figure, decorations);
		}
	}

	/**
	 * Find the decorations that adorn the specified <code>editParts</code>.
	 * 
	 * @param editparts
	 *            the list of <code>IGraphicalEditParts</code> for which to find
	 *            decorations
	 * @return a mapping of {@link IFigure}to ({@link Decoration}or
	 *         {@link Collection}of decorations})
	 */
	private Map findDecorations(Collection editparts) {
		// create inverse mapping of figures to edit parts (need this to map
		// decorations to edit parts)
		Map figureMap = mapFiguresToEditParts(editparts);

		Map result = new java.util.HashMap();

		if (!editparts.isEmpty()) {
			IGraphicalEditPart first = (IGraphicalEditPart) editparts
					.iterator().next();

			IFigure decorationLayer = LayerManager.Helper.find(first).getLayer(
					DiagramRootEditPart.DECORATION_PRINTABLE_LAYER);

			if (decorationLayer != null) {
				// compute the figures of the shapes
				List figures = new java.util.ArrayList(editparts);
				for (ListIterator iter = figures.listIterator(); iter.hasNext();) {
					iter.set(((IGraphicalEditPart) iter.next()).getFigure());
				}

				// find the decorations on figures that were selected
				for (Iterator iter = decorationLayer.getChildren().iterator(); iter
						.hasNext();) {
					Object next = iter.next();

					if (next instanceof Decoration) {
						Decoration decoration = (Decoration) next;
						IFigure owner = decoration.getOwnerFigure();

						while (owner != null) {
							if (figureMap.containsKey(owner)) {
								Object existing = result.get(owner);

								if (existing == null) {
									result.put(owner, decoration);
								} else if (existing instanceof Collection) {
									((Collection) existing).add(decoration);
								} else {
									Collection c = new java.util.ArrayList(2);
									c.add(existing);
									c.add(decoration);
									result.put(owner, c);
								}
								break;
							} else {
								owner = owner.getParent();
							}
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * Constructs a mapping of figures to their corresponding edit parts.
	 * 
	 * @param editParts
	 *            a collection of <code>IGraphicalEditParts</code>
	 * @return a mapping of {@link IFigure}to {@link IGraphicalEditPart}
	 */
	private Map mapFiguresToEditParts(Collection editParts) {
		Map result = new java.util.HashMap();

		for (Iterator iter = editParts.iterator(); iter.hasNext();) {
			IGraphicalEditPart next = (IGraphicalEditPart) iter.next();

			result.put(next.getFigure(), next);
		}

		return result;
	}

	/**
	 * This method is used when a figure needs to be painted to the graphics.
	 * The figure will be translated based on its absolute positioning.
	 * 
	 * @param graphics
	 *            Graphics object to render figure
	 * @param figure
	 *            the figure to be rendered
	 */
	private void paintFigure(Graphics graphics, IFigure figure) {

		if (!figure.isVisible() || figure.getBounds().isEmpty())
			return;

		// Calculate the Relative bounds and absolute bounds
		Rectangle relBounds = null;
		if (figure instanceof IExpandableFigure)
			relBounds = ((IExpandableFigure) figure).getExtendedBounds()
					.getCopy();
		else
			relBounds = figure.getBounds().getCopy();

		Rectangle abBounds = relBounds.getCopy();
		DiagramImageUtils.translateTo(abBounds, figure, printableLayer);

		// Calculate the difference
		int transX = abBounds.x - relBounds.x;
		int transY = abBounds.y - relBounds.y;

		// Paint the figure
		graphics.pushState();
		graphics.translate(transX, transY);
		figure.paint(graphics);
		graphics.popState();
		graphics.restoreState();
	}

	/**
	 * Allows hook to dispose of any artifacts around the creation of the
	 * <code>Graphics</code> object used for rendering.
	 * 
	 * @param g
	 *            Graphics element that is to be disposed.
	 */
	protected void disposeGraphics(Graphics g) {
		g.dispose();
	}

	/**
	 * Collects all connections contained within the given edit part
	 * 
	 * @param editPart
	 *            the container editpart
	 * @return connections within it
	 */
	private Collection<ConnectionEditPart> findConnectionsToPaint(
			IGraphicalEditPart editPart) {
		/*
		 * Set of node editparts contained within the given editpart
		 */
		HashSet<GraphicalEditPart> editParts = new HashSet<GraphicalEditPart>();

		/*
		 * All connection editparts that have a source contained within the
		 * given editpart
		 */
		HashSet<ConnectionEditPart> connectionEPs = new HashSet<ConnectionEditPart>();

		/*
		 * Connections contained within the given editpart (or just the
		 * connections to paint
		 */
		HashSet<ConnectionEditPart> connectionsToPaint = new HashSet<ConnectionEditPart>();

		/*
		 * Populate the set of node editparts
		 */
		getNestedEditParts(editPart, editParts);

		/*
		 * Populate the set of connections whose source is within the given
		 * editpart
		 */
		for (Iterator<GraphicalEditPart> editPartsItr = editParts.iterator(); editPartsItr
				.hasNext();) {
			connectionEPs.addAll(getAllConnectionsFrom(editPartsItr.next()));
		}

		/*
		 * Create a set of connections constained within the given editpart
		 */
		while (!connectionEPs.isEmpty()) {
			/*
			 * Take the first connection and check whethe there is a path
			 * through that connection that leads to the target contained within
			 * the given editpart
			 */
			Stack<ConnectionEditPart> connectionsPath = new Stack<ConnectionEditPart>();
			ConnectionEditPart conn = connectionEPs.iterator().next();
			connectionEPs.remove(conn);
			connectionsPath.add(conn);

			/*
			 * Initialize the target for the current path
			 */
			EditPart target = conn.getTarget();
			while (connectionEPs.contains(target)) {
				/*
				 * If the target end is a connection, check if it's one of the
				 * connection's whose target is a connection and within the
				 * given editpart. Append it to the path if it is. Otherwise
				 * check if the target is within the actual connections or nodes
				 * contained within the given editpart
				 */
				ConnectionEditPart targetConn = (ConnectionEditPart) target;
				connectionEPs.remove(targetConn);
				connectionsPath.add(targetConn);

				/*
				 * Update the target for the new path
				 */
				target = targetConn.getTarget();
			}

			/*
			 * The path is built, check if it's target is a node or a connection
			 * contained within the given editpart
			 */
			if (editParts.contains(target)
					|| connectionsToPaint.contains(target)) {
				connectionsToPaint.addAll(connectionsPath);
			}
		}
		return connectionsToPaint;
	}

	/**
	 * Paints the decorations adorning the specified <code>figure</code>, if
	 * any.
	 * 
	 * @param graphics
	 *            the graphics to paint on
	 * @param figure
	 *            the figure
	 * @param decorations
	 *            mapping of figures to decorations, in which we will find the
	 *            <code>figure</code>'s decorations
	 */
	private void paintDecorations(Graphics graphics, IFigure figure,
			Map decorations) {
		Object decoration = decorations.get(figure);

		if (decoration != null) {
			if (decoration instanceof Collection) {
				for (Iterator iter = ((Collection) decoration).iterator(); iter
						.hasNext();) {
					paintFigure(graphics, (IFigure) iter.next());
				}
			} else {
				paintFigure(graphics, (IFigure) decoration);
			}
		}
	}

	/**
	 * This method is used to obtain the list of child edit parts for shape
	 * compartments.
	 * 
	 * @param childEditPart
	 *            base edit part to get the list of children editparts
	 * @param editParts
	 *            list of nested shape edit parts
	 */
	private void getNestedEditParts(IGraphicalEditPart childEditPart,
			Collection editParts) {

		for (Iterator iter = childEditPart.getChildren().iterator(); iter
				.hasNext();) {

			IGraphicalEditPart child = (IGraphicalEditPart) iter.next();
			editParts.add(child);
			getNestedEditParts(child, editParts);
		}
	}

	/**
	 * Returns all connections orginating from a given editpart. All means that
	 * connections originating from connections that have a source given
	 * editpart will be included
	 * 
	 * @param ep
	 *            the editpart
	 * @return all source connections
	 */
	private List<ConnectionEditPart> getAllConnectionsFrom(GraphicalEditPart ep) {
		LinkedList<ConnectionEditPart> connections = new LinkedList<ConnectionEditPart>();
		for (Iterator itr = ep.getSourceConnections().iterator(); itr.hasNext();) {
			ConnectionEditPart sourceConn = (ConnectionEditPart) itr.next();
			connections.add(sourceConn);
			connections.addAll(getAllConnectionsFrom(sourceConn));
		}
		return connections;
	}
	
	/**
	 * Writes the SVG Model out to a file.
	 * 
	 * @param outputStream
	 *            output stream to store the SVG Model
	 */
	public void stream(OutputStream outputStream) {
		try {

			// Define the view box
			svgRoot.setAttributeNS(null,
				"viewBox", String.valueOf(viewBox.x) + " " + //$NON-NLS-1$ //$NON-NLS-2$
					String.valueOf(viewBox.y) + " " + //$NON-NLS-1$
					String.valueOf(viewBox.width) + " " + //$NON-NLS-1$
					String.valueOf(viewBox.height));

			// Write the document to the stream
			Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$

			DOMSource source = new DOMSource(svgRoot);
			StreamResult result = new StreamResult(outputStream);
			transformer.transform(source, result);
		} catch (Exception ex) {
			Log.error(DiagramUIRenderPlugin.getInstance(), IStatus.ERROR, ex
				.getMessage(), ex);
		}
	}
	
	/* 
	 * Example of how to access svgRoot.
	 */
	protected void setSVGRoot(Graphics g) {

			GraphicsSVG svgG = (GraphicsSVG) g;
			// Get the root element (the svg element)
			svgRoot = svgG.getRoot();
			NodeList nl = svgRoot.getElementsByTagNameNS("*", "rect");
			System.out.println("length of nl is:" + nl.getLength());
			for(int i = 0; i < nl.getLength(); i++) {
				SVGRectElement rect = (SVGRectElement)nl.item(i);
				System.out.println(rect.toString());
				rect.setAttributeNS(null, "id", "hello");
			}
	}
	
	/**
	 * Determine the minimal rectangle required to bound the list of editparts.
	 * A margin is used around each of the editpart's figures when calculating
	 * the size.
	 * 
	 * @param editparts
	 *            the list of <code>IGraphicalEditParts</code> from which
	 *            their figure bounds will be used
	 * @return Rectangle the minimal rectangle that can bound the figures of the
	 *         list of editparts
	 */
	public org.eclipse.swt.graphics.Rectangle calculateImageRectangle(
			List editparts) {
		Rectangle rect = DiagramImageUtils.calculateImageRectangle(editparts,
				getImageMargin(), emptyImageSize);
		return new org.eclipse.swt.graphics.Rectangle(rect.x, rect.y,
				rect.width, rect.height);
	}
	
	/**
	 * @return <code>int</code> value that is the margin around the generated
	 *         image in logical coordinates.
	 * @since 1.3
	 */
	public int getImageMargin() {
		return image_margin;
	}
    

}
