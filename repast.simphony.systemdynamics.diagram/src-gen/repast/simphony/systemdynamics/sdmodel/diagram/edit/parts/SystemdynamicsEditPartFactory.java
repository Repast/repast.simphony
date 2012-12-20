package repast.simphony.systemdynamics.sdmodel.diagram.edit.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.directedit.locator.CellEditorLocatorAccess;

import repast.simphony.systemdynamics.sdmodel.diagram.part.SystemdynamicsVisualIDRegistry;

/**
 * @generated
 */
public class SystemdynamicsEditPartFactory implements EditPartFactory {

  /**
   * @generated
   */
  public EditPart createEditPart(EditPart context, Object model) {
    if (model instanceof View) {
      View view = (View) model;
      switch (SystemdynamicsVisualIDRegistry.getVisualID(view)) {

      case SystemModelEditPart.VISUAL_ID:
        return new SystemModelEditPart(view);

      case VariableEditPart.VISUAL_ID:
        return new VariableEditPart(view);

      case VariableNameEditPart.VISUAL_ID:
        return new VariableNameEditPart(view);

      case CloudEditPart.VISUAL_ID:
        return new CloudEditPart(view);

      case StockEditPart.VISUAL_ID:
        return new StockEditPart(view);

      case StockNameEditPart.VISUAL_ID:
        return new StockNameEditPart(view);

      case RateEditPart.VISUAL_ID:
        return new RateEditPart(view);

      case RateNameEditPart.VISUAL_ID:
        return new RateNameEditPart(view);

      case CausalLinkEditPart.VISUAL_ID:
        return new CausalLinkEditPart(view);

      }
    }
    return createUnrecognizedEditPart(context, model);
  }

  /**
   * @generated
   */
  private EditPart createUnrecognizedEditPart(EditPart context, Object model) {
    // Handle creation of unrecognized child node EditParts here
    return null;
  }

  /**
   * @generated
   */
  public static CellEditorLocator getTextCellEditorLocator(ITextAwareEditPart source) {
    return CellEditorLocatorAccess.INSTANCE.getTextCellEditorLocator(source);
  }

}
