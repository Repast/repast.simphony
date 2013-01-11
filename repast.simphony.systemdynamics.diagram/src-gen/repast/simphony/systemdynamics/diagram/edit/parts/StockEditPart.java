package repast.simphony.systemdynamics.diagram.edit.parts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
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
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.draw2d.CenterLayout;
import org.eclipse.swt.graphics.Color;

import repast.simphony.systemdynamics.diagram.edit.policies.StockItemSemanticEditPolicy;
import repast.simphony.systemdynamics.diagram.part.SystemdynamicsVisualIDRegistry;
import repast.simphony.systemdynamics.diagram.providers.SystemdynamicsElementTypes;

/**
 * @generated
 */
public class StockEditPart extends ShapeNodeEditPart {

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
  public StockEditPart(View view) {
    super(view);
  }

  /**
   * @generated
   */
  protected void createDefaultEditPolicies() {
    super.createDefaultEditPolicies();
    installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new StockItemSemanticEditPolicy());
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
    return primaryShape = new StockFigure();
  }

  /**
   * @generated
   */
  public StockFigure getPrimaryShape() {
    return (StockFigure) primaryShape;
  }

  /**
   * @generated
   */
  protected boolean addFixedChild(EditPart childEditPart) {
    if (childEditPart instanceof StockNameEditPart) {
      ((StockNameEditPart) childEditPart).setLabel(getPrimaryShape().getFigureStockNameFigure());
      return true;
    }
    return false;
  }

  /**
   * @generated
   */
  protected boolean removeFixedChild(EditPart childEditPart) {
    if (childEditPart instanceof StockNameEditPart) {
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
    return getChildBySemanticHint(SystemdynamicsVisualIDRegistry
        .getType(StockNameEditPart.VISUAL_ID));
  }

  /**
   * @generated
   */
  public List<IElementType> getMARelTypesOnSource() {
    ArrayList<IElementType> types = new ArrayList<IElementType>(2);
    types.add(SystemdynamicsElementTypes.Rate_4003);
    types.add(SystemdynamicsElementTypes.InfluenceLink_4004);
    return types;
  }

  /**
   * @generated
   */
  public List<IElementType> getMARelTypesOnSourceAndTarget(IGraphicalEditPart targetEditPart) {
    LinkedList<IElementType> types = new LinkedList<IElementType>();
    if (targetEditPart instanceof CloudEditPart) {
      types.add(SystemdynamicsElementTypes.Rate_4003);
    }
    if (targetEditPart instanceof repast.simphony.systemdynamics.diagram.edit.parts.StockEditPart) {
      types.add(SystemdynamicsElementTypes.Rate_4003);
    }
    if (targetEditPart instanceof VariableEditPart) {
      types.add(SystemdynamicsElementTypes.InfluenceLink_4004);
    }
    if (targetEditPart instanceof CloudEditPart) {
      types.add(SystemdynamicsElementTypes.InfluenceLink_4004);
    }
    if (targetEditPart instanceof repast.simphony.systemdynamics.diagram.edit.parts.StockEditPart) {
      types.add(SystemdynamicsElementTypes.InfluenceLink_4004);
    }
    if (targetEditPart instanceof Variable2EditPart) {
      types.add(SystemdynamicsElementTypes.InfluenceLink_4004);
    }
    return types;
  }

  /**
   * @generated
   */
  public List<IElementType> getMATypesForTarget(IElementType relationshipType) {
    LinkedList<IElementType> types = new LinkedList<IElementType>();
    if (relationshipType == SystemdynamicsElementTypes.Rate_4003) {
      types.add(SystemdynamicsElementTypes.Cloud_2002);
      types.add(SystemdynamicsElementTypes.Stock_2003);
    } else if (relationshipType == SystemdynamicsElementTypes.InfluenceLink_4004) {
      types.add(SystemdynamicsElementTypes.Variable_2001);
      types.add(SystemdynamicsElementTypes.Cloud_2002);
      types.add(SystemdynamicsElementTypes.Stock_2003);
      types.add(SystemdynamicsElementTypes.Variable_2004);
    }
    return types;
  }

  /**
   * @generated
   */
  public List<IElementType> getMARelTypesOnTarget() {
    ArrayList<IElementType> types = new ArrayList<IElementType>(2);
    types.add(SystemdynamicsElementTypes.Rate_4003);
    types.add(SystemdynamicsElementTypes.InfluenceLink_4004);
    return types;
  }

  /**
   * @generated
   */
  public List<IElementType> getMATypesForSource(IElementType relationshipType) {
    LinkedList<IElementType> types = new LinkedList<IElementType>();
    if (relationshipType == SystemdynamicsElementTypes.Rate_4003) {
      types.add(SystemdynamicsElementTypes.Cloud_2002);
      types.add(SystemdynamicsElementTypes.Stock_2003);
    } else if (relationshipType == SystemdynamicsElementTypes.InfluenceLink_4004) {
      types.add(SystemdynamicsElementTypes.Variable_2001);
      types.add(SystemdynamicsElementTypes.Cloud_2002);
      types.add(SystemdynamicsElementTypes.Stock_2003);
      types.add(SystemdynamicsElementTypes.Variable_2004);
    }
    return types;
  }

  /**
   * @generated
   */
  public class StockFigure extends RectangleFigure {

    /**
     * @generated
     */
    private WrappingLabel fFigureStockNameFigure;

    /**
     * @generated
     */
    public StockFigure() {
      this.setLayoutManager(new CenterLayout());

      this.setBorder(new MarginBorder(getMapMode().DPtoLP(0), getMapMode().DPtoLP(6), getMapMode()
          .DPtoLP(0), getMapMode().DPtoLP(6)));
      createContents();
    }

    /**
     * @generated
     */
    private void createContents() {

      fFigureStockNameFigure = new WrappingLabel();

      fFigureStockNameFigure.setText("<...>");

      this.add(fFigureStockNameFigure);

    }

    /**
     * @generated
     */
    public WrappingLabel getFigureStockNameFigure() {
      return fFigureStockNameFigure;
    }

  }

}
