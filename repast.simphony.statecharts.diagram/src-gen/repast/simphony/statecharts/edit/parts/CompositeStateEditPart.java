package repast.simphony.statecharts.edit.parts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;

import repast.simphony.statecharts.edit.policies.CompositeStateItemSemanticEditPolicy;
import repast.simphony.statecharts.part.StatechartVisualIDRegistry;
import repast.simphony.statecharts.providers.StatechartElementTypes;

/**
 * @generated
 */
public class CompositeStateEditPart extends ShapeNodeEditPart {

  /**
   * @generated
   */
  public static final int VISUAL_ID = 2004;

  /**
   * @generated
   */
  protected IFigure contentPane;

  /**
   * @generated
   */
  protected IFigure primaryShape;

  /**
   * @generated
   */
  public CompositeStateEditPart(View view) {
    super(view);
  }

  /**
   * @generated
   */
  protected void createDefaultEditPolicies() {
    super.createDefaultEditPolicies();
    installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new CompositeStateItemSemanticEditPolicy());
    installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
    // XXX need an SCR to runtime to have another abstract superclass that would let children add reasonable editpolicies
    // removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CONNECTION_HANDLES_ROLE);
  }

  /**
   * @generated
   */
  protected LayoutEditPolicy createLayoutEditPolicy() {
    org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy lep = new org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy() {

      protected EditPolicy createChildEditPolicy(EditPart child) {
        EditPolicy result = child.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
        if (result == null) {
          result = new NonResizableEditPolicy();
        }
        return result;
      }

      protected Command getMoveChildrenCommand(Request request) {
        return null;
      }

      protected Command getCreateCommand(CreateRequest request) {
        return null;
      }
    };
    return lep;
  }

  /**
   * @generated
   */
  protected IFigure createNodeShape() {
    return primaryShape = new CompositeStateFigure();
  }

  /**
   * @generated
   */
  public CompositeStateFigure getPrimaryShape() {
    return (CompositeStateFigure) primaryShape;
  }

  /**
   * @generated
   */
  protected boolean addFixedChild(EditPart childEditPart) {
    if (childEditPart instanceof CompositeStateNameEditPart) {
      ((CompositeStateNameEditPart) childEditPart).setLabel(getPrimaryShape()
          .getFigureCompositeStateNameFigure());
      return true;
    }
    if (childEditPart instanceof CompositeStateCompositeStateCompartmentEditPart) {
      IFigure pane = getPrimaryShape().getFigureCompositeStateCompartmentRectangle();
      setupContentPane(pane); // FIXME each comparment should handle his content pane in his own way 
      pane.add(((CompositeStateCompositeStateCompartmentEditPart) childEditPart).getFigure());
      return true;
    }
    return false;
  }

  /**
   * @generated
   */
  protected boolean removeFixedChild(EditPart childEditPart) {
    if (childEditPart instanceof CompositeStateNameEditPart) {
      return true;
    }
    if (childEditPart instanceof CompositeStateCompositeStateCompartmentEditPart) {
      IFigure pane = getPrimaryShape().getFigureCompositeStateCompartmentRectangle();
      pane.remove(((CompositeStateCompositeStateCompartmentEditPart) childEditPart).getFigure());
      return true;
    }
    return false;
  }

  /**
   * @generated
   */
  protected void addChildVisual(EditPart childEditPart, int index) {
    if (addFixedChild(childEditPart)) {
      return;
    }
    super.addChildVisual(childEditPart, -1);
  }

  /**
   * @generated
   */
  protected void removeChildVisual(EditPart childEditPart) {
    if (removeFixedChild(childEditPart)) {
      return;
    }
    super.removeChildVisual(childEditPart);
  }

  /**
   * @generated
   */
  protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
    if (editPart instanceof CompositeStateCompositeStateCompartmentEditPart) {
      return getPrimaryShape().getFigureCompositeStateCompartmentRectangle();
    }
    return getContentPane();
  }

  /**
   * @generated
   */
  protected NodeFigure createNodePlate() {
    DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(100, 150);
    return result;
  }

  /**
   * Creates figure for this edit part.
   * 
   * Body of this method does not depend on settings in generation model
   * so you may safely remove <i>generated</i> tag and modify it.
   * 
   * @generated
   */
  protected NodeFigure createNodeFigure() {
    NodeFigure figure = createNodePlate();
    figure.setLayoutManager(new StackLayout());
    IFigure shape = createNodeShape();
    figure.add(shape);
    contentPane = setupContentPane(shape);
    return figure;
  }

  /**
   * Default implementation treats passed figure as content pane.
   * Respects layout one may have set for generated figure.
   * @param nodeShape instance of generated figure class
   * @generated
   */
  protected IFigure setupContentPane(IFigure nodeShape) {
    if (nodeShape.getLayoutManager() == null) {
      ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
      layout.setSpacing(5);
      nodeShape.setLayoutManager(layout);
    }
    return nodeShape; // use nodeShape itself as contentPane
  }

  /**
   * @generated
   */
  public IFigure getContentPane() {
    if (contentPane != null) {
      return contentPane;
    }
    return super.getContentPane();
  }

  /**
   * @generated
   */
  protected void setForegroundColor(Color color) {
    if (primaryShape != null) {
      primaryShape.setForegroundColor(color);
    }
  }

  /**
   * @generated
   */
  protected void setBackgroundColor(Color color) {
    if (primaryShape != null) {
      primaryShape.setBackgroundColor(color);
    }
  }

  /**
   * @generated
   */
  protected void setLineWidth(int width) {
    if (primaryShape instanceof Shape) {
      ((Shape) primaryShape).setLineWidth(width);
    }
  }

  /**
   * @generated
   */
  protected void setLineType(int style) {
    if (primaryShape instanceof Shape) {
      ((Shape) primaryShape).setLineStyle(style);
    }
  }

  /**
   * @generated
   */
  public EditPart getPrimaryChildEditPart() {
    return getChildBySemanticHint(StatechartVisualIDRegistry
        .getType(CompositeStateNameEditPart.VISUAL_ID));
  }

  /**
   * @generated
   */
  public List<IElementType> getMARelTypesOnSource() {
    ArrayList<IElementType> types = new ArrayList<IElementType>(1);
    types.add(StatechartElementTypes.Transition_4001);
    return types;
  }

  /**
   * @generated
   */
  public List<IElementType> getMARelTypesOnSourceAndTarget(IGraphicalEditPart targetEditPart) {
    LinkedList<IElementType> types = new LinkedList<IElementType>();
    if (targetEditPart instanceof StateEditPart) {
      types.add(StatechartElementTypes.Transition_4001);
    }
    if (targetEditPart instanceof repast.simphony.statecharts.edit.parts.CompositeStateEditPart) {
      types.add(StatechartElementTypes.Transition_4001);
    }
    if (targetEditPart instanceof PseudoStateEditPart) {
      types.add(StatechartElementTypes.Transition_4001);
    }
    if (targetEditPart instanceof PseudoState2EditPart) {
      types.add(StatechartElementTypes.Transition_4001);
    }
    if (targetEditPart instanceof PseudoState5EditPart) {
      types.add(StatechartElementTypes.Transition_4001);
    }
    if (targetEditPart instanceof FinalStateEditPart) {
      types.add(StatechartElementTypes.Transition_4001);
    }
    if (targetEditPart instanceof State2EditPart) {
      types.add(StatechartElementTypes.Transition_4001);
    }
    if (targetEditPart instanceof CompositeState2EditPart) {
      types.add(StatechartElementTypes.Transition_4001);
    }
    if (targetEditPart instanceof PseudoState3EditPart) {
      types.add(StatechartElementTypes.Transition_4001);
    }
    if (targetEditPart instanceof PseudoState4EditPart) {
      types.add(StatechartElementTypes.Transition_4001);
    }
    if (targetEditPart instanceof FinalState2EditPart) {
      types.add(StatechartElementTypes.Transition_4001);
    }
    if (targetEditPart instanceof HistoryEditPart) {
      types.add(StatechartElementTypes.Transition_4001);
    }
    if (targetEditPart instanceof History2EditPart) {
      types.add(StatechartElementTypes.Transition_4001);
    }
    return types;
  }

  /**
   * @generated
   */
  public List<IElementType> getMATypesForTarget(IElementType relationshipType) {
    LinkedList<IElementType> types = new LinkedList<IElementType>();
    if (relationshipType == StatechartElementTypes.Transition_4001) {
      types.add(StatechartElementTypes.State_2003);
      types.add(StatechartElementTypes.CompositeState_2004);
      types.add(StatechartElementTypes.PseudoState_2005);
      types.add(StatechartElementTypes.PseudoState_2006);
      types.add(StatechartElementTypes.PseudoState_2007);
      types.add(StatechartElementTypes.FinalState_2008);
      types.add(StatechartElementTypes.State_3001);
      types.add(StatechartElementTypes.CompositeState_3002);
      types.add(StatechartElementTypes.PseudoState_3003);
      types.add(StatechartElementTypes.PseudoState_3006);
      types.add(StatechartElementTypes.FinalState_3007);
      types.add(StatechartElementTypes.History_3008);
      types.add(StatechartElementTypes.History_3009);
    }
    return types;
  }

  /**
   * @generated
   */
  public List<IElementType> getMARelTypesOnTarget() {
    ArrayList<IElementType> types = new ArrayList<IElementType>(1);
    types.add(StatechartElementTypes.Transition_4001);
    return types;
  }

  /**
   * @generated
   */
  public List<IElementType> getMATypesForSource(IElementType relationshipType) {
    LinkedList<IElementType> types = new LinkedList<IElementType>();
    if (relationshipType == StatechartElementTypes.Transition_4001) {
      types.add(StatechartElementTypes.State_2003);
      types.add(StatechartElementTypes.CompositeState_2004);
      types.add(StatechartElementTypes.PseudoState_2005);
      types.add(StatechartElementTypes.PseudoState_2006);
      types.add(StatechartElementTypes.PseudoState_2007);
      types.add(StatechartElementTypes.FinalState_2008);
      types.add(StatechartElementTypes.State_3001);
      types.add(StatechartElementTypes.CompositeState_3002);
      types.add(StatechartElementTypes.PseudoState_3003);
      types.add(StatechartElementTypes.PseudoState_3006);
      types.add(StatechartElementTypes.FinalState_3007);
      types.add(StatechartElementTypes.History_3008);
      types.add(StatechartElementTypes.History_3009);
    }
    return types;
  }

  /**
   * @generated
   */
  public class CompositeStateFigure extends RoundedRectangle {

    /**
     * @generated
     */
    private RectangleFigure fFigureCompositeStateCompartmentRectangle;
    /**
     * @generated
     */
    private WrappingLabel fFigureCompositeStateNameFigure;

    /**
     * @generated
     */
    public CompositeStateFigure() {

      GridLayout layoutThis = new GridLayout();
      layoutThis.numColumns = 1;
      layoutThis.makeColumnsEqualWidth = true;
      this.setLayoutManager(layoutThis);

      this.setCornerDimensions(new Dimension(getMapMode().DPtoLP(8), getMapMode().DPtoLP(8)));
      this.setPreferredSize(new Dimension(getMapMode().DPtoLP(100), getMapMode().DPtoLP(150)));

      this.setBorder(new MarginBorder(getMapMode().DPtoLP(2), getMapMode().DPtoLP(2), getMapMode()
          .DPtoLP(2), getMapMode().DPtoLP(2)));
      createContents();
    }

    /**
     * @generated
     */
    private void createContents() {

      fFigureCompositeStateNameFigure = new WrappingLabel();

      fFigureCompositeStateNameFigure.setText("<?>");

      this.add(fFigureCompositeStateNameFigure);

      fFigureCompositeStateCompartmentRectangle = new RectangleFigure();

      GridData constraintFFigureCompositeStateCompartmentRectangle = new GridData();
      constraintFFigureCompositeStateCompartmentRectangle.verticalAlignment = GridData.FILL;
      constraintFFigureCompositeStateCompartmentRectangle.horizontalAlignment = GridData.FILL;
      constraintFFigureCompositeStateCompartmentRectangle.horizontalIndent = 0;
      constraintFFigureCompositeStateCompartmentRectangle.horizontalSpan = 1;
      constraintFFigureCompositeStateCompartmentRectangle.verticalSpan = 1;
      constraintFFigureCompositeStateCompartmentRectangle.grabExcessHorizontalSpace = true;
      constraintFFigureCompositeStateCompartmentRectangle.grabExcessVerticalSpace = true;
      this.add(fFigureCompositeStateCompartmentRectangle,
          constraintFFigureCompositeStateCompartmentRectangle);

    }

    /**
     * @generated
     */
    public RectangleFigure getFigureCompositeStateCompartmentRectangle() {
      return fFigureCompositeStateCompartmentRectangle;
    }

    /**
     * @generated
     */
    public WrappingLabel getFigureCompositeStateNameFigure() {
      return fFigureCompositeStateNameFigure;
    }

  }

}
