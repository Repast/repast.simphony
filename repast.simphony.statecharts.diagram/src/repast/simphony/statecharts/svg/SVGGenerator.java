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

import org.apache.batik.svggen.SVGGraphics2D;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
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
import org.eclipse.gmf.runtime.notation.View;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGRectElement;

import repast.simphony.statecharts.edit.parts.CompositeState2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateCompositeStateCompartment2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateCompositeStateCompartmentEditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateEditPart;
import repast.simphony.statecharts.edit.parts.FinalState2EditPart;
import repast.simphony.statecharts.edit.parts.FinalStateEditPart;
import repast.simphony.statecharts.edit.parts.History2EditPart;
import repast.simphony.statecharts.edit.parts.HistoryEditPart;
import repast.simphony.statecharts.edit.parts.PseudoState3EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState4EditPart;
import repast.simphony.statecharts.edit.parts.State2EditPart;
import repast.simphony.statecharts.edit.parts.StateEditPart;
import repast.simphony.statecharts.part.StatechartVisualIDRegistry;
import repast.simphony.statecharts.scmodel.impl.AbstractStateImpl;

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
	final protected void renderToGraphics(Graphics graphics,
			Graphics mapModeGraphics, Point translateOffset, List editparts) {
		GraphicsSVG svgG = (GraphicsSVG) graphics;
		// Document doc = svgG.getDocument();
		SVGGraphics2D svg2d = svgG.getSVGGraphics2D();
		Element tlg = svg2d.getTopLevelGroup();
		svg2d.setTopLevelGroup(tlg);

		mapModeGraphics.translate((-translateOffset.x), (-translateOffset.y));
		mapModeGraphics.pushState();

		List<GraphicalEditPart> connectionsToPaint = new LinkedList<GraphicalEditPart>();

		Map decorations = findDecorations(editparts);
		int oldLength = 0;
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
				paintDecorations(mapModeGraphics, figure, decorations);

				tlg = svg2d.getTopLevelGroup();

				NodeList nl = tlg.getChildNodes();
				int newLength = nl.getLength();

				// if the new nodelist contains more children, process them
				if (newLength > oldLength) {
					if (newLength - oldLength == 3) { // defs1 is included so skip first
						oldLength++;
					}
					switch (StatechartVisualIDRegistry.getVisualID(editPart
							.getNotationView())) {

					case CompositeStateEditPart.VISUAL_ID:
						processBaseCompositeState(oldLength, nl, editPart);
						break;
					case StateEditPart.VISUAL_ID:
						processBaseSimpleState(oldLength, nl, editPart);
						break;
					case FinalStateEditPart.VISUAL_ID:
						processBaseFinalState(oldLength, nl);
						break;
					default:
						// Nothing to do otherwise

					}

				}
				svg2d.setTopLevelGroup(tlg);
				oldLength = newLength;
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
	 * Fixes the final state appearance by adding a fill="black" attribute.
	 * 
	 * @param oldLength
	 * @param nl
	 */
	private void processBaseFinalState(int oldLength, NodeList nl) {
		Node g = nl.item(oldLength);
		((Element) g.getFirstChild()).setAttribute("fill", "black");

	}

	/**
	 * Adds uuid attribute to base simple state svg elements.
	 * 
	 * @param oldLength
	 * @param nl
	 */
	private void processBaseSimpleState(int oldLength, NodeList nl,
			IGraphicalEditPart editPart) {
		Element firstElement = processAndGetFirstStateElement(oldLength, nl);
		String uuid = findUUID(editPart);
		firstElement.setAttribute("uuid", uuid);
	}

	/**
	 * Adds uuid attribute to base composite state svg elements
	 * and recursively processes the sub-elements.
	 * 
	 * @param oldLength
	 * @param nl
	 * @param editPart
	 */
	private void processBaseCompositeState(int oldLength, NodeList nl,
			IGraphicalEditPart editPart) {
		Element firstElement = processAndGetFirstStateElement(oldLength, nl);
		if (!firstElement.getNodeName().equals("rect")) {
			throw new IllegalStateException(
					"The first svg element of a composite state should be 'rect'.");
		}
		String uuid = findUUID(editPart);
		firstElement.setAttribute("uuid", uuid);
		Element nextElement = getNextElementComposite(firstElement);
		// Look through subparts
		List baseCompositeStateEditPartChildren = editPart.getChildren();
		CompositeStateCompositeStateCompartmentEditPart compartmentEditPart = null;
		for (Object child : baseCompositeStateEditPartChildren) {
			if (child instanceof CompositeStateCompositeStateCompartmentEditPart) {
				compartmentEditPart = (CompositeStateCompositeStateCompartmentEditPart) child;
				break;
			}
		}
		if (compartmentEditPart != null) {
			List baseCompositeStateCompartmentEditPartChildren = compartmentEditPart
					.getChildren();
			// CompositeStateCompositeStateCompartmentEditPart
			// process in a depth first manner the EditParts contained within
			nextElement = processSubElements(nextElement,
					baseCompositeStateCompartmentEditPartChildren);
			if (!nextElement.getNodeName().equals("line")) {
				throw new IllegalStateException(
						"The final svg element of a composite state should be 'line'.");
			}

		}

	}

	/**
	 * Processing the svg child elements of a composite state.
	 * @param nextElement
	 * @param childrenEditParts
	 * @return
	 */
	private Element processSubElements(Element nextElement,
			List childrenEditParts) {
		for (Object child : childrenEditParts) {
			if (child instanceof IGraphicalEditPart) {
				IGraphicalEditPart childGraphicalEditPart = (IGraphicalEditPart) child;
				switch (StatechartVisualIDRegistry
						.getVisualID(childGraphicalEditPart
								.getNotationView())) {

				case CompositeState2EditPart.VISUAL_ID: // Sub Composite
														// State
					nextElement = processSubCompositeState(nextElement,
							childGraphicalEditPart);
					break;
				case State2EditPart.VISUAL_ID: // Sub Simple State
					nextElement = processSubSimpleState(nextElement,
							childGraphicalEditPart);
					break;
				case PseudoState3EditPart.VISUAL_ID: // Initial State Marker
					nextElement = processInitialStateMarker(nextElement);
					break;
				case PseudoState4EditPart.VISUAL_ID: // Sub Branching State
					nextElement = processSubBranchingState(nextElement);
					break;
				case FinalState2EditPart.VISUAL_ID: // Sub Final State
					nextElement = processSubFinalState(nextElement);
					break;
				case HistoryEditPart.VISUAL_ID: // Shallow History State
					nextElement = processShallowHistoryState(nextElement);
					break;
				case History2EditPart.VISUAL_ID: // Deep History State
					nextElement = processDeepHistoryState(nextElement);
					break;
				default:
					// Nothing to do otherwise

				}
			}
		}
		return nextElement;
	}

	/**
	 * Adds uuid attribute to sub composite state svg elements
	 * and recursively processes the sub-elements.
	 * 
	 * @param nextElement
	 * @param editPart
	 * @return the next svg element
	 */
	private Element processSubCompositeState(Element nextElement,
			IGraphicalEditPart editPart) {

		
		if (!nextElement.getNodeName().equals("rect")) {
			throw new IllegalStateException(
					"The first svg element of a composite state should be 'rect'.");
		}
		String uuid = findUUID(editPart);
		nextElement.setAttribute("uuid", uuid);
		

		nextElement = getNextElementComposite(nextElement);
		// Look through subparts
		List baseCompositeStateEditPartChildren = editPart.getChildren();
		CompositeStateCompositeStateCompartment2EditPart compartment2EditPart = null;
		for (Object child : baseCompositeStateEditPartChildren) {
			if (child instanceof CompositeStateCompositeStateCompartment2EditPart) {
				compartment2EditPart = (CompositeStateCompositeStateCompartment2EditPart) child;
				break;
			}
		}
		if (compartment2EditPart != null) {
			List baseCompositeStateCompartment2EditPartChildren = compartment2EditPart
					.getChildren();
			nextElement = processSubElements(nextElement,
					baseCompositeStateCompartment2EditPartChildren);
			if (!nextElement.getNodeName().equals("line")) {
				throw new IllegalStateException(
						"The final svg element of a composite state should be 'line'.");
			}
			return (Element)nextElement.getNextSibling();
		}
		else {
			throw new IllegalStateException("Composite state diagram element did not contain a CompositeStateCompositeStateCompartment2EditPart.");
		}

	}

	/**
	 * Shallow history state svg element processing.
	 * 
	 * @param nextElement
	 * @return
	 */
	private Element processShallowHistoryState(Element nextElement) {
		if (!nextElement.getNodeName().equals("circle")) {
			throw new IllegalStateException(
					"The first svg element of a shallow history state should be 'circle'.");
		}
		nextElement.setAttribute("fill", "black");
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("circle")) {
			throw new IllegalStateException(
					"The second svg element of a shallow history state should be 'circle'.");
		}
		nextElement.setAttribute("stroke", "black");
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("line")) {
			throw new IllegalStateException(
					"The third svg element of a shallow history state should be 'line'.");
		}
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("line")) {
			throw new IllegalStateException(
					"The fourth svg element of a shallow history state should be 'line'.");
		}
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("line")) {
			throw new IllegalStateException(
					"The fifth svg element of a shallow history state should be 'line'.");
		}
		return (Element) nextElement.getNextSibling();
	}

	/**
	 * Deep history state svg element processing.
	 * 
	 * @param nextElement
	 * @return
	 */
	private Element processDeepHistoryState(Element nextElement) {
		if (!nextElement.getNodeName().equals("circle")) {
			throw new IllegalStateException(
					"The first svg element of a deep history state should be 'circle'.");
		}
		nextElement.setAttribute("fill", "black");
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("circle")) {
			throw new IllegalStateException(
					"The second svg element of a deep history state should be 'circle'.");
		}
		nextElement.setAttribute("stroke", "black");
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("line")) {
			throw new IllegalStateException(
					"The third svg element of a deep history state should be 'line'.");
		}
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("line")) {
			throw new IllegalStateException(
					"The fourth svg element of a deep history state should be 'line'.");
		}
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("line")) {
			throw new IllegalStateException(
					"The fifth svg element of a deep history state should be 'line'.");
		}
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("line")) {
			throw new IllegalStateException(
					"The sixth svg element of a deep history state should be 'line'.");
		}
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("line")) {
			throw new IllegalStateException(
					"The seventh svg element of a deep history state should be 'line'.");
		}
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("line")) {
			throw new IllegalStateException(
					"The eighth svg element of a deep history state should be 'line'.");
		}
		return (Element) nextElement.getNextSibling();
	}

	/**
	 * Initial state marker svg element processing.
	 * 
	 * @param nextElement
	 * @return
	 */
	private Element processInitialStateMarker(Element nextElement) {
		if (!nextElement.getNodeName().equals("circle")) {
			throw new IllegalStateException(
					"The first svg element of an initial state marker should be 'circle'.");
		}
		nextElement.setAttribute("fill", "black");
		return (Element) nextElement.getNextSibling();
	}

	/**
	 * Sub branching state svg element processing.
	 * 
	 * @param nextElement
	 * @return
	 */
	private Element processSubBranchingState(Element nextElement) {
		if (!nextElement.getNodeName().equals("polygon")) {
			throw new IllegalStateException(
					"The first svg element of a branching state should be 'polygon'.");
		}
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("polygon")) {
			throw new IllegalStateException(
					"The second svg element of a branching state should be 'polygon'.");
		}
		return (Element) nextElement.getNextSibling();
	}

	/**
	 * Sub final state svg element processing.
	 * 
	 * @param nextElement
	 * @return
	 */
	private Element processSubFinalState(Element nextElement) {
		if (!nextElement.getNodeName().equals("circle")) {
			throw new IllegalStateException(
					"The first svg element of a final state should be 'circle'.");
		}
		nextElement.setAttribute("fill", "black");
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("circle")) {
			throw new IllegalStateException(
					"The second svg element of a branching state should be 'circle'.");
		}
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("circle")) {
			throw new IllegalStateException(
					"The third svg element of a branching state should be 'circle'.");
		}
		return (Element) nextElement.getNextSibling();
	}

	/**
	 * Sub simple state svg element processing.
	 * 
	 * @param nextElement
	 * @return
	 */
	private Element processSubSimpleState(Element nextElement,
			IGraphicalEditPart editPart) {
		if (!nextElement.getNodeName().equals("rect")) {
			throw new IllegalStateException(
					"The first svg element of a simple state should be 'rect'.");
		}
		String uuid = findUUID(editPart);
		nextElement.setAttribute("uuid", uuid);
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("rect")) {
			throw new IllegalStateException(
					"The second svg element of a simple state should be 'rect'.");
		}
		nextElement = (Element) nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("text")) {
			throw new IllegalStateException(
					"The third svg element of a simple state should be 'text'.");
		}
		return (Element) nextElement.getNextSibling();
	}

	/**
	 * Advances to the next element within a composite state.
	 * 
	 * @param firstElement
	 * @return
	 */
	private Element getNextElementComposite(Element firstElement) {
		// check to see if this element is part of the single g element
		// singleton corner case
		Node nextElement = firstElement.getNextSibling();
		if (nextElement == null) {
			Node parentNode = firstElement.getParentNode();
			if (parentNode == null) {
				throw new IllegalStateException(
						"The parent of the first svg element of a composite state should not be null.");
			} else {
				Node nextParentNode = parentNode.getNextSibling();
				if (nextParentNode == null) {
					throw new IllegalStateException(
							"The svg sibling of the parent in the corner case should exist.");
				} else {
					nextElement = nextParentNode.getFirstChild();
				}
			}
		}
		if (!nextElement.getNodeName().equals("rect")) {
			throw new IllegalStateException(
					"The second svg element of a composite state should be a 'rect'.");
		}
		nextElement = nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("text")) {
			throw new IllegalStateException(
					"The third svg element of a composite state should be a 'text'.");
		}
		nextElement = nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("rect")) {
			throw new IllegalStateException(
					"The fourth svg element of a composite state should be a 'rect'.");
		}
		nextElement = nextElement.getNextSibling();
		if (!nextElement.getNodeName().equals("rect")) {
			throw new IllegalStateException(
					"The fifth svg element of a composite state should be a 'rect'.");
		}
		nextElement = nextElement.getNextSibling();

		return (Element) nextElement;
	}

	/**
	 * Gets the first svg element for a base simple or composite state and adds
	 * stroke-width attribute if necessary.
	 * 
	 * @param oldLength
	 * @param nl
	 * @return
	 */
	private Element processAndGetFirstStateElement(int oldLength, NodeList nl) {
		Node g = nl.item(oldLength);
		Element firstElement = (Element) g.getFirstChild();
		// Two cases here.
		// g element is split into two parts
		if (nl.getLength() - oldLength == 2) {
			// Need to add a non-zero stroke-width
			firstElement.setAttribute("stroke-width", "1.1");
		}
		return firstElement;
	}

	/**
	 * Returns the AbstractStateImpl uuid property of the editPart
	 * or an empty string if it can't be found.
	 * @param editPart
	 * @return
	 */
	private String findUUID(IGraphicalEditPart editPart) {
		EObject stateObject = ViewUtil.resolveSemanticElement(editPart
				.getNotationView());
		String result = "";
		if (stateObject instanceof AbstractStateImpl) {
			result = ((AbstractStateImpl) stateObject).getUuid();
		}
		return result;
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
			Log.error(DiagramUIRenderPlugin.getInstance(), IStatus.ERROR,
					ex.getMessage(), ex);
		}
	}

	/*
	 * Set the svgRoot.
	 */
	protected void setSVGRoot(Graphics g) {
		GraphicsSVG svgG = (GraphicsSVG) g;
		svgRoot = svgG.getRoot();
	}

	/**
	 * Determine the minimal rectangle required to bound the list of editparts.
	 * A margin is used around each of the editpart's figures when calculating
	 * the size.
	 * 
	 * @param editparts
	 *            the list of <code>IGraphicalEditParts</code> from which their
	 *            figure bounds will be used
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
