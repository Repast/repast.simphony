package repast.simphony.systemdynamics.diagram.providers;

import java.util.ArrayList;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.providers.IViewProvider;
import org.eclipse.gmf.runtime.diagram.core.services.view.CreateDiagramViewOperation;
import org.eclipse.gmf.runtime.diagram.core.services.view.CreateEdgeViewOperation;
import org.eclipse.gmf.runtime.diagram.core.services.view.CreateNodeViewOperation;
import org.eclipse.gmf.runtime.diagram.core.services.view.CreateViewForKindOperation;
import org.eclipse.gmf.runtime.diagram.core.services.view.CreateViewOperation;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.notation.DecorationNode;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.FontStyle;
import org.eclipse.gmf.runtime.notation.Location;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.Routing;
import org.eclipse.gmf.runtime.notation.RoutingStyle;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.gmf.runtime.notation.Smoothness;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;

import repast.simphony.systemdynamics.diagram.edit.parts.CloudEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.InfluenceLinkEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.RateEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.RateNameEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.StockEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.StockNameEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.SystemModelEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.Variable2EditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.Variable3EditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.VariableEditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.VariableName2EditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.VariableName3EditPart;
import repast.simphony.systemdynamics.diagram.edit.parts.VariableNameEditPart;
import repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry;

/**
 * @generated
 */
public class SystemdynamicsViewProvider extends AbstractProvider implements IViewProvider {

  /**
   * @generated
   */
  public final boolean provides(IOperation operation) {
    if (operation instanceof CreateViewForKindOperation) {
      return provides((CreateViewForKindOperation) operation);
    }
    assert operation instanceof CreateViewOperation;
    if (operation instanceof CreateDiagramViewOperation) {
      return provides((CreateDiagramViewOperation) operation);
    } else if (operation instanceof CreateEdgeViewOperation) {
      return provides((CreateEdgeViewOperation) operation);
    } else if (operation instanceof CreateNodeViewOperation) {
      return provides((CreateNodeViewOperation) operation);
    }
    return false;
  }

  /**
   * @generated
   */
  protected boolean provides(CreateViewForKindOperation op) {
    /*
     if (op.getViewKind() == Node.class)
     return getNodeViewClass(op.getSemanticAdapter(), op.getContainerView(), op.getSemanticHint()) != null;
     if (op.getViewKind() == Edge.class)
     return getEdgeViewClass(op.getSemanticAdapter(), op.getContainerView(), op.getSemanticHint()) != null;
     */
    return true;
  }

  /**
   * @generated
   */
  protected boolean provides(CreateDiagramViewOperation op) {
    return SystemModelEditPart.MODEL_ID.equals(op.getSemanticHint())
        && SystemdynamicsVisualIDRegistry.getDiagramVisualID(getSemanticElement(op
            .getSemanticAdapter())) != -1;
  }

  /**
   * @generated
   */
  protected boolean provides(CreateNodeViewOperation op) {
    if (op.getContainerView() == null) {
      return false;
    }
    IElementType elementType = getSemanticElementType(op.getSemanticAdapter());
    EObject domainElement = getSemanticElement(op.getSemanticAdapter());
    int visualID;
    if (op.getSemanticHint() == null) {
      // Semantic hint is not specified. Can be a result of call from CanonicalEditPolicy.
      // In this situation there should be NO elementType, visualID will be determined
      // by VisualIDRegistry.getNodeVisualID() for domainElement.
      if (elementType != null || domainElement == null) {
        return false;
      }
      visualID = SystemdynamicsVisualIDRegistry.getNodeVisualID(op.getContainerView(),
          domainElement);
    } else {
      visualID = SystemdynamicsVisualIDRegistry.getVisualID(op.getSemanticHint());
      if (elementType != null) {
        if (!SystemdynamicsElementTypes.isKnownElementType(elementType)
            || (!(elementType instanceof IHintedType))) {
          return false; // foreign element type
        }
        String elementTypeHint = ((IHintedType) elementType).getSemanticHint();
        if (!op.getSemanticHint().equals(elementTypeHint)) {
          return false; // if semantic hint is specified it should be the same as in element type
        }
        if (domainElement != null
            && visualID != SystemdynamicsVisualIDRegistry.getNodeVisualID(op.getContainerView(),
                domainElement)) {
          return false; // visual id for node EClass should match visual id from element type
        }
      } else {
        if (!SystemModelEditPart.MODEL_ID.equals(SystemdynamicsVisualIDRegistry.getModelID(op
            .getContainerView()))) {
          return false; // foreign diagram
        }
        switch (visualID) {
        case VariableEditPart.VISUAL_ID:
        case CloudEditPart.VISUAL_ID:
        case StockEditPart.VISUAL_ID:
        case Variable2EditPart.VISUAL_ID:
        case Variable3EditPart.VISUAL_ID:
          if (domainElement == null
              || visualID != SystemdynamicsVisualIDRegistry.getNodeVisualID(op.getContainerView(),
                  domainElement)) {
            return false; // visual id in semantic hint should match visual id for domain element
          }
          break;
        default:
          return false;
        }
      }
    }
    return VariableEditPart.VISUAL_ID == visualID || CloudEditPart.VISUAL_ID == visualID
        || StockEditPart.VISUAL_ID == visualID || Variable2EditPart.VISUAL_ID == visualID
        || Variable3EditPart.VISUAL_ID == visualID;
  }

  /**
   * @generated
   */
  protected boolean provides(CreateEdgeViewOperation op) {
    IElementType elementType = getSemanticElementType(op.getSemanticAdapter());
    if (!SystemdynamicsElementTypes.isKnownElementType(elementType)
        || (!(elementType instanceof IHintedType))) {
      return false; // foreign element type
    }
    String elementTypeHint = ((IHintedType) elementType).getSemanticHint();
    if (elementTypeHint == null
        || (op.getSemanticHint() != null && !elementTypeHint.equals(op.getSemanticHint()))) {
      return false; // our hint is visual id and must be specified, and it should be the same as in element type
    }
    int visualID = SystemdynamicsVisualIDRegistry.getVisualID(elementTypeHint);
    EObject domainElement = getSemanticElement(op.getSemanticAdapter());
    if (domainElement != null
        && visualID != SystemdynamicsVisualIDRegistry.getLinkWithClassVisualID(domainElement)) {
      return false; // visual id for link EClass should match visual id from element type
    }
    return true;
  }

  /**
   * @generated
   */
  public Diagram createDiagram(IAdaptable semanticAdapter, String diagramKind,
      PreferencesHint preferencesHint) {
    Diagram diagram = NotationFactory.eINSTANCE.createDiagram();
    diagram.getStyles().add(NotationFactory.eINSTANCE.createDiagramStyle());
    diagram.setType(SystemModelEditPart.MODEL_ID);
    diagram.setElement(getSemanticElement(semanticAdapter));
    diagram.setMeasurementUnit(MeasurementUnit.PIXEL_LITERAL);
    return diagram;
  }

  /**
   * @generated
   */
  public Node createNode(IAdaptable semanticAdapter, View containerView, String semanticHint,
      int index, boolean persisted, PreferencesHint preferencesHint) {
    final EObject domainElement = getSemanticElement(semanticAdapter);
    final int visualID;
    if (semanticHint == null) {
      visualID = SystemdynamicsVisualIDRegistry.getNodeVisualID(containerView, domainElement);
    } else {
      visualID = SystemdynamicsVisualIDRegistry.getVisualID(semanticHint);
    }
    switch (visualID) {
    case VariableEditPart.VISUAL_ID:
      return createVariable_2001(domainElement, containerView, index, persisted, preferencesHint);
    case CloudEditPart.VISUAL_ID:
      return createCloud_2002(domainElement, containerView, index, persisted, preferencesHint);
    case StockEditPart.VISUAL_ID:
      return createStock_2003(domainElement, containerView, index, persisted, preferencesHint);
    case Variable2EditPart.VISUAL_ID:
      return createVariable_2004(domainElement, containerView, index, persisted, preferencesHint);
    case Variable3EditPart.VISUAL_ID:
      return createVariable_2005(domainElement, containerView, index, persisted, preferencesHint);
    }
    // can't happen, provided #provides(CreateNodeViewOperation) is correct
    return null;
  }

  /**
   * @generated
   */
  public Edge createEdge(IAdaptable semanticAdapter, View containerView, String semanticHint,
      int index, boolean persisted, PreferencesHint preferencesHint) {
    IElementType elementType = getSemanticElementType(semanticAdapter);
    String elementTypeHint = ((IHintedType) elementType).getSemanticHint();
    switch (SystemdynamicsVisualIDRegistry.getVisualID(elementTypeHint)) {
    case RateEditPart.VISUAL_ID:
      return createRate_4003(getSemanticElement(semanticAdapter), containerView, index, persisted,
          preferencesHint);
    case InfluenceLinkEditPart.VISUAL_ID:
      return createInfluenceLink_4004(getSemanticElement(semanticAdapter), containerView, index,
          persisted, preferencesHint);
    }
    // can never happen, provided #provides(CreateEdgeViewOperation) is correct
    return null;
  }

  /**
   * @generated
   */
  public Node createVariable_2001(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(SystemdynamicsVisualIDRegistry.getType(VariableEditPart.VISUAL_ID));
    ViewUtil.insertChildView(containerView, node, index, persisted);
    node.setElement(domainElement);
    stampShortcut(containerView, node);
    // initializeFromPreferences 
    final IPreferenceStore prefStore = (IPreferenceStore) preferencesHint.getPreferenceStore();

    org.eclipse.swt.graphics.RGB lineRGB = PreferenceConverter.getColor(prefStore,
        IPreferenceConstants.PREF_LINE_COLOR);
    ViewUtil.setStructuralFeatureValue(node, NotationPackage.eINSTANCE.getLineStyle_LineColor(),
        FigureUtilities.RGBToInteger(lineRGB));
    FontStyle nodeFontStyle = (FontStyle) node.getStyle(NotationPackage.Literals.FONT_STYLE);
    if (nodeFontStyle != null) {
      FontData fontData = PreferenceConverter.getFontData(prefStore,
          IPreferenceConstants.PREF_DEFAULT_FONT);
      nodeFontStyle.setFontName(fontData.getName());
      nodeFontStyle.setFontHeight(fontData.getHeight());
      nodeFontStyle.setBold((fontData.getStyle() & SWT.BOLD) != 0);
      nodeFontStyle.setItalic((fontData.getStyle() & SWT.ITALIC) != 0);
      org.eclipse.swt.graphics.RGB fontRGB = PreferenceConverter.getColor(prefStore,
          IPreferenceConstants.PREF_FONT_COLOR);
      nodeFontStyle.setFontColor(FigureUtilities.RGBToInteger(fontRGB).intValue());
    }
    org.eclipse.swt.graphics.RGB fillRGB = PreferenceConverter.getColor(prefStore,
        IPreferenceConstants.PREF_FILL_COLOR);
    ViewUtil.setStructuralFeatureValue(node, NotationPackage.eINSTANCE.getFillStyle_FillColor(),
        FigureUtilities.RGBToInteger(fillRGB));
    Node label5001 = createLabel(node,
        SystemdynamicsVisualIDRegistry.getType(VariableNameEditPart.VISUAL_ID));
    return node;
  }

  /**
   * @generated
   */
  public Node createCloud_2002(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(SystemdynamicsVisualIDRegistry.getType(CloudEditPart.VISUAL_ID));
    ViewUtil.insertChildView(containerView, node, index, persisted);
    node.setElement(domainElement);
    stampShortcut(containerView, node);
    // initializeFromPreferences 
    final IPreferenceStore prefStore = (IPreferenceStore) preferencesHint.getPreferenceStore();

    org.eclipse.swt.graphics.RGB lineRGB = PreferenceConverter.getColor(prefStore,
        IPreferenceConstants.PREF_LINE_COLOR);
    ViewUtil.setStructuralFeatureValue(node, NotationPackage.eINSTANCE.getLineStyle_LineColor(),
        FigureUtilities.RGBToInteger(lineRGB));
    FontStyle nodeFontStyle = (FontStyle) node.getStyle(NotationPackage.Literals.FONT_STYLE);
    if (nodeFontStyle != null) {
      FontData fontData = PreferenceConverter.getFontData(prefStore,
          IPreferenceConstants.PREF_DEFAULT_FONT);
      nodeFontStyle.setFontName(fontData.getName());
      nodeFontStyle.setFontHeight(fontData.getHeight());
      nodeFontStyle.setBold((fontData.getStyle() & SWT.BOLD) != 0);
      nodeFontStyle.setItalic((fontData.getStyle() & SWT.ITALIC) != 0);
      org.eclipse.swt.graphics.RGB fontRGB = PreferenceConverter.getColor(prefStore,
          IPreferenceConstants.PREF_FONT_COLOR);
      nodeFontStyle.setFontColor(FigureUtilities.RGBToInteger(fontRGB).intValue());
    }
    org.eclipse.swt.graphics.RGB fillRGB = PreferenceConverter.getColor(prefStore,
        IPreferenceConstants.PREF_FILL_COLOR);
    ViewUtil.setStructuralFeatureValue(node, NotationPackage.eINSTANCE.getFillStyle_FillColor(),
        FigureUtilities.RGBToInteger(fillRGB));
    return node;
  }

  /**
   * @generated
   */
  public Node createStock_2003(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(SystemdynamicsVisualIDRegistry.getType(StockEditPart.VISUAL_ID));
    ViewUtil.insertChildView(containerView, node, index, persisted);
    node.setElement(domainElement);
    stampShortcut(containerView, node);
    // initializeFromPreferences 
    final IPreferenceStore prefStore = (IPreferenceStore) preferencesHint.getPreferenceStore();

    org.eclipse.swt.graphics.RGB lineRGB = PreferenceConverter.getColor(prefStore,
        IPreferenceConstants.PREF_LINE_COLOR);
    ViewUtil.setStructuralFeatureValue(node, NotationPackage.eINSTANCE.getLineStyle_LineColor(),
        FigureUtilities.RGBToInteger(lineRGB));
    FontStyle nodeFontStyle = (FontStyle) node.getStyle(NotationPackage.Literals.FONT_STYLE);
    if (nodeFontStyle != null) {
      FontData fontData = PreferenceConverter.getFontData(prefStore,
          IPreferenceConstants.PREF_DEFAULT_FONT);
      nodeFontStyle.setFontName(fontData.getName());
      nodeFontStyle.setFontHeight(fontData.getHeight());
      nodeFontStyle.setBold((fontData.getStyle() & SWT.BOLD) != 0);
      nodeFontStyle.setItalic((fontData.getStyle() & SWT.ITALIC) != 0);
      org.eclipse.swt.graphics.RGB fontRGB = PreferenceConverter.getColor(prefStore,
          IPreferenceConstants.PREF_FONT_COLOR);
      nodeFontStyle.setFontColor(FigureUtilities.RGBToInteger(fontRGB).intValue());
    }
    org.eclipse.swt.graphics.RGB fillRGB = PreferenceConverter.getColor(prefStore,
        IPreferenceConstants.PREF_FILL_COLOR);
    ViewUtil.setStructuralFeatureValue(node, NotationPackage.eINSTANCE.getFillStyle_FillColor(),
        FigureUtilities.RGBToInteger(fillRGB));
    Node label5002 = createLabel(node,
        SystemdynamicsVisualIDRegistry.getType(StockNameEditPart.VISUAL_ID));
    return node;
  }

  /**
   * @generated
   */
  public Node createVariable_2004(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(SystemdynamicsVisualIDRegistry.getType(Variable2EditPart.VISUAL_ID));
    ViewUtil.insertChildView(containerView, node, index, persisted);
    node.setElement(domainElement);
    stampShortcut(containerView, node);
    // initializeFromPreferences 
    final IPreferenceStore prefStore = (IPreferenceStore) preferencesHint.getPreferenceStore();

    org.eclipse.swt.graphics.RGB lineRGB = PreferenceConverter.getColor(prefStore,
        IPreferenceConstants.PREF_LINE_COLOR);
    ViewUtil.setStructuralFeatureValue(node, NotationPackage.eINSTANCE.getLineStyle_LineColor(),
        FigureUtilities.RGBToInteger(lineRGB));
    FontStyle nodeFontStyle = (FontStyle) node.getStyle(NotationPackage.Literals.FONT_STYLE);
    if (nodeFontStyle != null) {
      FontData fontData = PreferenceConverter.getFontData(prefStore,
          IPreferenceConstants.PREF_DEFAULT_FONT);
      nodeFontStyle.setFontName(fontData.getName());
      nodeFontStyle.setFontHeight(fontData.getHeight());
      nodeFontStyle.setBold((fontData.getStyle() & SWT.BOLD) != 0);
      nodeFontStyle.setItalic((fontData.getStyle() & SWT.ITALIC) != 0);
      org.eclipse.swt.graphics.RGB fontRGB = PreferenceConverter.getColor(prefStore,
          IPreferenceConstants.PREF_FONT_COLOR);
      nodeFontStyle.setFontColor(FigureUtilities.RGBToInteger(fontRGB).intValue());
    }
    org.eclipse.swt.graphics.RGB fillRGB = PreferenceConverter.getColor(prefStore,
        IPreferenceConstants.PREF_FILL_COLOR);
    ViewUtil.setStructuralFeatureValue(node, NotationPackage.eINSTANCE.getFillStyle_FillColor(),
        FigureUtilities.RGBToInteger(fillRGB));
    Node label5003 = createLabel(node,
        SystemdynamicsVisualIDRegistry.getType(VariableName2EditPart.VISUAL_ID));
    return node;
  }

  /**
   * @generated
   */
  public Node createVariable_2005(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(SystemdynamicsVisualIDRegistry.getType(Variable3EditPart.VISUAL_ID));
    ViewUtil.insertChildView(containerView, node, index, persisted);
    node.setElement(domainElement);
    stampShortcut(containerView, node);
    // initializeFromPreferences 
    final IPreferenceStore prefStore = (IPreferenceStore) preferencesHint.getPreferenceStore();

    org.eclipse.swt.graphics.RGB lineRGB = PreferenceConverter.getColor(prefStore,
        IPreferenceConstants.PREF_LINE_COLOR);
    ViewUtil.setStructuralFeatureValue(node, NotationPackage.eINSTANCE.getLineStyle_LineColor(),
        FigureUtilities.RGBToInteger(lineRGB));
    FontStyle nodeFontStyle = (FontStyle) node.getStyle(NotationPackage.Literals.FONT_STYLE);
    if (nodeFontStyle != null) {
      FontData fontData = PreferenceConverter.getFontData(prefStore,
          IPreferenceConstants.PREF_DEFAULT_FONT);
      nodeFontStyle.setFontName(fontData.getName());
      nodeFontStyle.setFontHeight(fontData.getHeight());
      nodeFontStyle.setBold((fontData.getStyle() & SWT.BOLD) != 0);
      nodeFontStyle.setItalic((fontData.getStyle() & SWT.ITALIC) != 0);
      org.eclipse.swt.graphics.RGB fontRGB = PreferenceConverter.getColor(prefStore,
          IPreferenceConstants.PREF_FONT_COLOR);
      nodeFontStyle.setFontColor(FigureUtilities.RGBToInteger(fontRGB).intValue());
    }
    org.eclipse.swt.graphics.RGB fillRGB = PreferenceConverter.getColor(prefStore,
        IPreferenceConstants.PREF_FILL_COLOR);
    ViewUtil.setStructuralFeatureValue(node, NotationPackage.eINSTANCE.getFillStyle_FillColor(),
        FigureUtilities.RGBToInteger(fillRGB));
    Node label5004 = createLabel(node,
        SystemdynamicsVisualIDRegistry.getType(VariableName3EditPart.VISUAL_ID));
    return node;
  }

  /**
   * @generated
   */
  public Edge createRate_4003(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Edge edge = NotationFactory.eINSTANCE.createEdge();
    edge.getStyles().add(NotationFactory.eINSTANCE.createRoutingStyle());
    edge.getStyles().add(NotationFactory.eINSTANCE.createFontStyle());
    RelativeBendpoints bendpoints = NotationFactory.eINSTANCE.createRelativeBendpoints();
    ArrayList<RelativeBendpoint> points = new ArrayList<RelativeBendpoint>(2);
    points.add(new RelativeBendpoint());
    points.add(new RelativeBendpoint());
    bendpoints.setPoints(points);
    edge.setBendpoints(bendpoints);
    ViewUtil.insertChildView(containerView, edge, index, persisted);
    edge.setType(SystemdynamicsVisualIDRegistry.getType(RateEditPart.VISUAL_ID));
    edge.setElement(domainElement);
    // initializePreferences
    final IPreferenceStore prefStore = (IPreferenceStore) preferencesHint.getPreferenceStore();
    FontStyle edgeFontStyle = (FontStyle) edge.getStyle(NotationPackage.Literals.FONT_STYLE);
    if (edgeFontStyle != null) {
      FontData fontData = PreferenceConverter.getFontData(prefStore,
          IPreferenceConstants.PREF_DEFAULT_FONT);
      edgeFontStyle.setFontName(fontData.getName());
      edgeFontStyle.setFontHeight(fontData.getHeight());
      edgeFontStyle.setBold((fontData.getStyle() & SWT.BOLD) != 0);
      edgeFontStyle.setItalic((fontData.getStyle() & SWT.ITALIC) != 0);
      org.eclipse.swt.graphics.RGB fontRGB = PreferenceConverter.getColor(prefStore,
          IPreferenceConstants.PREF_FONT_COLOR);
      edgeFontStyle.setFontColor(FigureUtilities.RGBToInteger(fontRGB).intValue());
    }
    Routing routing = Routing.get(prefStore.getInt(IPreferenceConstants.PREF_LINE_STYLE));
    if (routing != null) {
      ViewUtil.setStructuralFeatureValue(edge, NotationPackage.eINSTANCE.getRoutingStyle_Routing(),
          routing);
    }
    Node label6001 = createLabel(edge,
        SystemdynamicsVisualIDRegistry.getType(RateNameEditPart.VISUAL_ID));
    label6001.setLayoutConstraint(NotationFactory.eINSTANCE.createLocation());
    Location location6001 = (Location) label6001.getLayoutConstraint();
    location6001.setX(12);
    location6001.setY(12);
    return edge;
  }

  /**
   * @generated NOT -- added smoothness to routing style
   */
  public Edge createInfluenceLink_4004(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Edge edge = NotationFactory.eINSTANCE.createEdge();
    RoutingStyle routingStyle = NotationFactory.eINSTANCE.createRoutingStyle();
    routingStyle.setSmoothness(Smoothness.get(Smoothness.NORMAL));
    edge.getStyles().add(routingStyle);
    edge.getStyles().add(NotationFactory.eINSTANCE.createFontStyle());
    RelativeBendpoints bendpoints = NotationFactory.eINSTANCE.createRelativeBendpoints();
    ArrayList<RelativeBendpoint> points = new ArrayList<RelativeBendpoint>(2);
    points.add(new RelativeBendpoint());
    points.add(new RelativeBendpoint());
    bendpoints.setPoints(points);
    edge.setBendpoints(bendpoints);
    ViewUtil.insertChildView(containerView, edge, index, persisted);
    edge.setType(SystemdynamicsVisualIDRegistry.getType(InfluenceLinkEditPart.VISUAL_ID));
    edge.setElement(domainElement);
    // initializePreferences
    final IPreferenceStore prefStore = (IPreferenceStore) preferencesHint.getPreferenceStore();
    FontStyle edgeFontStyle = (FontStyle) edge.getStyle(NotationPackage.Literals.FONT_STYLE);
    if (edgeFontStyle != null) {
      FontData fontData = PreferenceConverter.getFontData(prefStore,
          IPreferenceConstants.PREF_DEFAULT_FONT);
      edgeFontStyle.setFontName(fontData.getName());
      edgeFontStyle.setFontHeight(fontData.getHeight());
      edgeFontStyle.setBold((fontData.getStyle() & SWT.BOLD) != 0);
      edgeFontStyle.setItalic((fontData.getStyle() & SWT.ITALIC) != 0);
      org.eclipse.swt.graphics.RGB fontRGB = PreferenceConverter.getColor(prefStore,
          IPreferenceConstants.PREF_FONT_COLOR);
      edgeFontStyle.setFontColor(FigureUtilities.RGBToInteger(fontRGB).intValue());
    }
    Routing routing = Routing.get(prefStore.getInt(IPreferenceConstants.PREF_LINE_STYLE));
    if (routing != null) {
      ViewUtil.setStructuralFeatureValue(edge, NotationPackage.eINSTANCE.getRoutingStyle_Routing(),
          routing);
    }
    return edge;
  }

  /**
   * @generated
   */
  private void stampShortcut(View containerView, Node target) {
    if (!SystemModelEditPart.MODEL_ID.equals(SystemdynamicsVisualIDRegistry
        .getModelID(containerView))) {
      EAnnotation shortcutAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
      shortcutAnnotation.setSource("Shortcut"); //$NON-NLS-1$
      shortcutAnnotation.getDetails().put("modelID", SystemModelEditPart.MODEL_ID); //$NON-NLS-1$
      target.getEAnnotations().add(shortcutAnnotation);
    }
  }

  /**
   * @generated
   */
  private Node createLabel(View owner, String hint) {
    DecorationNode rv = NotationFactory.eINSTANCE.createDecorationNode();
    rv.setType(hint);
    ViewUtil.insertChildView(owner, rv, ViewUtil.APPEND, true);
    return rv;
  }

  /**
   * @generated
   */
  private EObject getSemanticElement(IAdaptable semanticAdapter) {
    if (semanticAdapter == null) {
      return null;
    }
    EObject eObject = (EObject) semanticAdapter.getAdapter(EObject.class);
    if (eObject != null) {
      return EMFCoreUtil.resolve(TransactionUtil.getEditingDomain(eObject), eObject);
    }
    return null;
  }

  /**
   * @generated
   */
  private IElementType getSemanticElementType(IAdaptable semanticAdapter) {
    if (semanticAdapter == null) {
      return null;
    }
    return (IElementType) semanticAdapter.getAdapter(IElementType.class);
  }
}
