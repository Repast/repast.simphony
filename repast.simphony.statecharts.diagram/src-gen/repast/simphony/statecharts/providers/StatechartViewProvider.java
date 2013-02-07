package repast.simphony.statecharts.providers;

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
import org.eclipse.gmf.runtime.notation.MeasurementUnit;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.Routing;
import org.eclipse.gmf.runtime.notation.RoutingStyle;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.gmf.runtime.notation.Smoothness;
import org.eclipse.gmf.runtime.notation.TitleStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;

import repast.simphony.statecharts.edit.parts.CompositeState2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateCompositeStateCompartment2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateCompositeStateCompartmentEditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateEditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateName2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateNameEditPart;
import repast.simphony.statecharts.edit.parts.FinalState2EditPart;
import repast.simphony.statecharts.edit.parts.FinalStateEditPart;
import repast.simphony.statecharts.edit.parts.History2EditPart;
import repast.simphony.statecharts.edit.parts.HistoryEditPart;
import repast.simphony.statecharts.edit.parts.PseudoState2EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState3EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState4EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState5EditPart;
import repast.simphony.statecharts.edit.parts.PseudoStateEditPart;
import repast.simphony.statecharts.edit.parts.State2EditPart;
import repast.simphony.statecharts.edit.parts.StateEditPart;
import repast.simphony.statecharts.edit.parts.StateMachineEditPart;
import repast.simphony.statecharts.edit.parts.StateName2EditPart;
import repast.simphony.statecharts.edit.parts.StateNameEditPart;
import repast.simphony.statecharts.edit.parts.TransitionEditPart;
import repast.simphony.statecharts.part.StatechartVisualIDRegistry;

/**
 * @generated
 */
public class StatechartViewProvider extends AbstractProvider implements IViewProvider {

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
    return StateMachineEditPart.MODEL_ID.equals(op.getSemanticHint())
        && StatechartVisualIDRegistry
            .getDiagramVisualID(getSemanticElement(op.getSemanticAdapter())) != -1;
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
      visualID = StatechartVisualIDRegistry.getNodeVisualID(op.getContainerView(), domainElement);
    } else {
      visualID = StatechartVisualIDRegistry.getVisualID(op.getSemanticHint());
      if (elementType != null) {
        if (!StatechartElementTypes.isKnownElementType(elementType)
            || (!(elementType instanceof IHintedType))) {
          return false; // foreign element type
        }
        String elementTypeHint = ((IHintedType) elementType).getSemanticHint();
        if (!op.getSemanticHint().equals(elementTypeHint)) {
          return false; // if semantic hint is specified it should be the same as in element type
        }
        if (domainElement != null
            && visualID != StatechartVisualIDRegistry.getNodeVisualID(op.getContainerView(),
                domainElement)) {
          return false; // visual id for node EClass should match visual id from element type
        }
      } else {
        if (!StateMachineEditPart.MODEL_ID.equals(StatechartVisualIDRegistry.getModelID(op
            .getContainerView()))) {
          return false; // foreign diagram
        }
        switch (visualID) {
        case StateEditPart.VISUAL_ID:
        case CompositeStateEditPart.VISUAL_ID:
        case PseudoState3EditPart.VISUAL_ID:
        case FinalState2EditPart.VISUAL_ID:
        case HistoryEditPart.VISUAL_ID:
        case PseudoStateEditPart.VISUAL_ID:
        case PseudoState2EditPart.VISUAL_ID:
        case PseudoState5EditPart.VISUAL_ID:
        case FinalStateEditPart.VISUAL_ID:
        case State2EditPart.VISUAL_ID:
        case CompositeState2EditPart.VISUAL_ID:
        case PseudoState4EditPart.VISUAL_ID:
        case History2EditPart.VISUAL_ID:
          if (domainElement == null
              || visualID != StatechartVisualIDRegistry.getNodeVisualID(op.getContainerView(),
                  domainElement)) {
            return false; // visual id in semantic hint should match visual id for domain element
          }
          break;
        default:
          return false;
        }
      }
    }
    return StateEditPart.VISUAL_ID == visualID || CompositeStateEditPart.VISUAL_ID == visualID
        || PseudoStateEditPart.VISUAL_ID == visualID || PseudoState2EditPart.VISUAL_ID == visualID
        || PseudoState5EditPart.VISUAL_ID == visualID || FinalStateEditPart.VISUAL_ID == visualID
        || State2EditPart.VISUAL_ID == visualID || CompositeState2EditPart.VISUAL_ID == visualID
        || PseudoState3EditPart.VISUAL_ID == visualID || PseudoState4EditPart.VISUAL_ID == visualID
        || FinalState2EditPart.VISUAL_ID == visualID || HistoryEditPart.VISUAL_ID == visualID
        || History2EditPart.VISUAL_ID == visualID;
  }

  /**
   * @generated
   */
  protected boolean provides(CreateEdgeViewOperation op) {
    IElementType elementType = getSemanticElementType(op.getSemanticAdapter());
    if (!StatechartElementTypes.isKnownElementType(elementType)
        || (!(elementType instanceof IHintedType))) {
      return false; // foreign element type
    }
    String elementTypeHint = ((IHintedType) elementType).getSemanticHint();
    if (elementTypeHint == null
        || (op.getSemanticHint() != null && !elementTypeHint.equals(op.getSemanticHint()))) {
      return false; // our hint is visual id and must be specified, and it should be the same as in element type
    }
    int visualID = StatechartVisualIDRegistry.getVisualID(elementTypeHint);
    EObject domainElement = getSemanticElement(op.getSemanticAdapter());
    if (domainElement != null
        && visualID != StatechartVisualIDRegistry.getLinkWithClassVisualID(domainElement)) {
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
    diagram.setType(StateMachineEditPart.MODEL_ID);
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
      visualID = StatechartVisualIDRegistry.getNodeVisualID(containerView, domainElement);
    } else {
      visualID = StatechartVisualIDRegistry.getVisualID(semanticHint);
    }
    switch (visualID) {
    case StateEditPart.VISUAL_ID:
      return createState_2003(domainElement, containerView, index, persisted, preferencesHint);
    case CompositeStateEditPart.VISUAL_ID:
      return createCompositeState_2004(domainElement, containerView, index, persisted,
          preferencesHint);
    case PseudoStateEditPart.VISUAL_ID:
      return createPseudoState_2005(domainElement, containerView, index, persisted, preferencesHint);
    case PseudoState2EditPart.VISUAL_ID:
      return createPseudoState_2006(domainElement, containerView, index, persisted, preferencesHint);
    case PseudoState5EditPart.VISUAL_ID:
      return createPseudoState_2007(domainElement, containerView, index, persisted, preferencesHint);
    case FinalStateEditPart.VISUAL_ID:
      return createFinalState_2008(domainElement, containerView, index, persisted, preferencesHint);
    case State2EditPart.VISUAL_ID:
      return createState_3001(domainElement, containerView, index, persisted, preferencesHint);
    case CompositeState2EditPart.VISUAL_ID:
      return createCompositeState_3002(domainElement, containerView, index, persisted,
          preferencesHint);
    case PseudoState3EditPart.VISUAL_ID:
      return createPseudoState_3003(domainElement, containerView, index, persisted, preferencesHint);
    case PseudoState4EditPart.VISUAL_ID:
      return createPseudoState_3006(domainElement, containerView, index, persisted, preferencesHint);
    case FinalState2EditPart.VISUAL_ID:
      return createFinalState_3007(domainElement, containerView, index, persisted, preferencesHint);
    case HistoryEditPart.VISUAL_ID:
      return createHistory_3008(domainElement, containerView, index, persisted, preferencesHint);
    case History2EditPart.VISUAL_ID:
      return createHistory_3009(domainElement, containerView, index, persisted, preferencesHint);
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
    switch (StatechartVisualIDRegistry.getVisualID(elementTypeHint)) {
    case TransitionEditPart.VISUAL_ID:
      return createTransition_4001(getSemanticElement(semanticAdapter), containerView, index,
          persisted, preferencesHint);
    }
    // can never happen, provided #provides(CreateEdgeViewOperation) is correct
    return null;
  }

  /**
   * @generated
   */
  public Node createState_2003(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(StatechartVisualIDRegistry.getType(StateEditPart.VISUAL_ID));
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
        StatechartVisualIDRegistry.getType(StateNameEditPart.VISUAL_ID));
    return node;
  }

  /**
   * @generated
   */
  public Node createCompositeState_2004(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(StatechartVisualIDRegistry.getType(CompositeStateEditPart.VISUAL_ID));
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
        StatechartVisualIDRegistry.getType(CompositeStateNameEditPart.VISUAL_ID));
    createCompartment(node,
        StatechartVisualIDRegistry
            .getType(CompositeStateCompositeStateCompartmentEditPart.VISUAL_ID), false, false,
        false, false);
    return node;
  }

  /**
   * @generated
   */
  public Node createPseudoState_2005(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Node node = NotationFactory.eINSTANCE.createNode();
    node.getStyles().add(NotationFactory.eINSTANCE.createDescriptionStyle());
    node.getStyles().add(NotationFactory.eINSTANCE.createFontStyle());
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(StatechartVisualIDRegistry.getType(PseudoStateEditPart.VISUAL_ID));
    ViewUtil.insertChildView(containerView, node, index, persisted);
    node.setElement(domainElement);
    stampShortcut(containerView, node);
    // initializeFromPreferences 
    final IPreferenceStore prefStore = (IPreferenceStore) preferencesHint.getPreferenceStore();
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
    return node;
  }

  /**
   * @generated
   */
  public Node createPseudoState_2006(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(StatechartVisualIDRegistry.getType(PseudoState2EditPart.VISUAL_ID));
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
  public Node createPseudoState_2007(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(StatechartVisualIDRegistry.getType(PseudoState5EditPart.VISUAL_ID));
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
  public Node createFinalState_2008(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(StatechartVisualIDRegistry.getType(FinalStateEditPart.VISUAL_ID));
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
  public Node createState_3001(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(StatechartVisualIDRegistry.getType(State2EditPart.VISUAL_ID));
    ViewUtil.insertChildView(containerView, node, index, persisted);
    node.setElement(domainElement);
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
        StatechartVisualIDRegistry.getType(StateName2EditPart.VISUAL_ID));
    return node;
  }

  /**
   * @generated
   */
  public Node createCompositeState_3002(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(StatechartVisualIDRegistry.getType(CompositeState2EditPart.VISUAL_ID));
    ViewUtil.insertChildView(containerView, node, index, persisted);
    node.setElement(domainElement);
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
        StatechartVisualIDRegistry.getType(CompositeStateName2EditPart.VISUAL_ID));
    createCompartment(node,
        StatechartVisualIDRegistry
            .getType(CompositeStateCompositeStateCompartment2EditPart.VISUAL_ID), false, false,
        false, false);
    return node;
  }

  /**
   * @generated
   */
  public Node createPseudoState_3003(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(StatechartVisualIDRegistry.getType(PseudoState3EditPart.VISUAL_ID));
    ViewUtil.insertChildView(containerView, node, index, persisted);
    node.setElement(domainElement);
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
  public Node createPseudoState_3006(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(StatechartVisualIDRegistry.getType(PseudoState4EditPart.VISUAL_ID));
    ViewUtil.insertChildView(containerView, node, index, persisted);
    node.setElement(domainElement);
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
  public Node createFinalState_3007(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(StatechartVisualIDRegistry.getType(FinalState2EditPart.VISUAL_ID));
    ViewUtil.insertChildView(containerView, node, index, persisted);
    node.setElement(domainElement);
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
  public Node createHistory_3008(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(StatechartVisualIDRegistry.getType(HistoryEditPart.VISUAL_ID));
    ViewUtil.insertChildView(containerView, node, index, persisted);
    node.setElement(domainElement);
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
  public Node createHistory_3009(EObject domainElement, View containerView, int index,
      boolean persisted, PreferencesHint preferencesHint) {
    Shape node = NotationFactory.eINSTANCE.createShape();
    node.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
    node.setType(StatechartVisualIDRegistry.getType(History2EditPart.VISUAL_ID));
    ViewUtil.insertChildView(containerView, node, index, persisted);
    node.setElement(domainElement);
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
   * @generated NOT -- added smoothness 
   */
  public Edge createTransition_4001(EObject domainElement, View containerView, int index,
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
    edge.setType(StatechartVisualIDRegistry.getType(TransitionEditPart.VISUAL_ID));
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
    if (!StateMachineEditPart.MODEL_ID.equals(StatechartVisualIDRegistry.getModelID(containerView))) {
      EAnnotation shortcutAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
      shortcutAnnotation.setSource("Shortcut"); //$NON-NLS-1$
      shortcutAnnotation.getDetails().put("modelID", StateMachineEditPart.MODEL_ID); //$NON-NLS-1$
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
  private Node createCompartment(View owner, String hint, boolean canCollapse, boolean hasTitle,
      boolean canSort, boolean canFilter) {
    //SemanticListCompartment rv = NotationFactory.eINSTANCE.createSemanticListCompartment();
    //rv.setShowTitle(showTitle);
    //rv.setCollapsed(isCollapsed);
    Node rv;
    if (canCollapse) {
      rv = NotationFactory.eINSTANCE.createBasicCompartment();
    } else {
      rv = NotationFactory.eINSTANCE.createDecorationNode();
    }
    if (hasTitle) {
      TitleStyle ts = NotationFactory.eINSTANCE.createTitleStyle();
      ts.setShowTitle(true);
      rv.getStyles().add(ts);
    }
    if (canSort) {
      rv.getStyles().add(NotationFactory.eINSTANCE.createSortingStyle());
    }
    if (canFilter) {
      rv.getStyles().add(NotationFactory.eINSTANCE.createFilteringStyle());
    }
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
