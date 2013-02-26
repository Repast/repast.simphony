package repast.simphony.statecharts.edit.parts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;

import repast.simphony.statecharts.edit.policies.StateItemSemanticEditPolicy;
import repast.simphony.statecharts.part.StatechartVisualIDRegistry;
import repast.simphony.statecharts.providers.StatechartElementTypes;

/**
 * @generated
 */
public class StateEditPart extends ShapeNodeEditPart {

  /**
   * @generated
   */
  public static final int VISUAL_ID = 2003;

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
  public StateEditPart(View view) {
    super(view);
  }

  /**
   * @generated
   */
  protected void createDefaultEditPolicies() {
    super.createDefaultEditPolicies();
    installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new StateItemSemanticEditPolicy());
    installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
    // XXX need an SCR to runtime to have another abstract superclass that would let children add reasonable editpolicies
    // removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CONNECTION_HANDLES_ROLE);
  }

  /**
   * @generated
   */
  protected LayoutEditPolicy createLayoutEditPolicy() {

    FlowLayoutEditPolicy lep = new FlowLayoutEditPolicy() {

      protected Command createAddCommand(EditPart child, EditPart after) {
        return null;
      }

      protected Command createMoveChildCommand(EditPart child, EditPart after) {
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
    return primaryShape = new StateFigure();
  }

  /**
   * @generated
   */
  public StateFigure getPrimaryShape() {
    return (StateFigure) primaryShape;
  }

  /**
   * @generated
   */
  protected boolean addFixedChild(EditPart childEditPart) {
    if (childEditPart instanceof StateNameEditPart) {
      ((StateNameEditPart) childEditPart).setLabel(getPrimaryShape().getFigureStateNameFigure());
      return true;
    }
    return false;
  }

  /**
   * @generated
   */
  protected boolean removeFixedChild(EditPart childEditPart) {
    if (childEditPart instanceof StateNameEditPart) {
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
    return getContentPane();
  }

  /**
   * @generated
   */
  protected NodeFigure createNodePlate() {
    DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(40, 40);
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
    return getChildBySemanticHint(StatechartVisualIDRegistry.getType(StateNameEditPart.VISUAL_ID));
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
    if (targetEditPart instanceof repast.simphony.statecharts.edit.parts.StateEditPart) {
      types.add(StatechartElementTypes.Transition_4001);
    }
    if (targetEditPart instanceof CompositeStateEditPart) {
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
  public class StateFigure extends RoundedRectangle {

    /**
     * @generated
     */
    private WrappingLabel fFigureStateNameFigure;

    /**
     * @generated
     */
    public StateFigure() {

      FlowLayout layoutThis = new FlowLayout();
      layoutThis.setStretchMinorAxis(false);
      layoutThis.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);

      layoutThis.setMajorAlignment(FlowLayout.ALIGN_LEFTTOP);
      layoutThis.setMajorSpacing(5);
      layoutThis.setMinorSpacing(5);
      layoutThis.setHorizontal(true);

      this.setLayoutManager(layoutThis);

      this.setCornerDimensions(new Dimension(getMapMode().DPtoLP(8), getMapMode().DPtoLP(8)));

      this.setBorder(new MarginBorder(getMapMode().DPtoLP(6), getMapMode().DPtoLP(6), getMapMode()
          .DPtoLP(6), getMapMode().DPtoLP(6)));
      createContents();
    }

    /**
     * @generated
     */
    private void createContents() {

      fFigureStateNameFigure = new WrappingLabel();

      fFigureStateNameFigure.setText("<É>");

      this.add(fFigureStateNameFigure);

    }

    /**
     * @generated
     */
    public WrappingLabel getFigureStateNameFigure() {
      return fFigureStateNameFigure;
    }

  }

}
